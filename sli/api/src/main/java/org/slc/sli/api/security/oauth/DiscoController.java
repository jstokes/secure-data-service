package org.slc.sli.api.security.oauth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.slc.sli.api.config.EntityDefinition;
import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.security.saml.SamlHelper;
import org.slc.sli.api.service.EntityService;
import org.slc.sli.api.util.SecurityUtil;
import org.slc.sli.api.util.SecurityUtil.SecurityTask;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;

/**
 * Controller for Discovery Service
 * 
 * @author dkornishev
 * 
 */
@Controller
@Scope("request")
@RequestMapping("/oauth")
public class DiscoController {

    private static final Logger LOG = LoggerFactory.getLogger(DiscoController.class);

    @Autowired
    private EntityDefinitionStore store;

    @Autowired
    private MongoAuthorizationCodeServices authCodeService;

    @Autowired
    private SamlHelper saml;
    
    /**
     * Returns the Entity Service that will make calls to the realm collection.
     * @return Entity Service.
     */
    public EntityService getRealmEntityService() {
        EntityDefinition defn = store.lookupByResourceName("realm");
        return defn.getService();
    }
    
    /**
     * Returns the Entity Service that will make calls to the oauth_access_token collection.
     * @return Entity Service.
     */
    public EntityService getOauthAccessTokenEntityService() {
        EntityDefinition defn = store.lookupByResourceName("oauth_access_token");
        return defn.getService();
    }

    /**
     * Calls api to list available realms and injects into model
     * 
     * @param model
     *            spring injected model
     * @return name of the template to use
     * @throws IOException
     */
    @RequestMapping(value = "authorize", method = RequestMethod.GET)
    public String listRealms(@RequestParam(value = "redirect_uri", required = false) final String relayState,
            @RequestParam(value = "RealmName", required = false) final String realmName, 
            @RequestParam(value = "client_id", required = true) final String clientId, 
            @RequestParam(value = "state", required = false) final String state,
            @CookieValue(value = "realmCookie", required = false) final String cookie,
            @CookieValue(value = "_tla", required = false) final String sessionIndex,
            final HttpServletResponse res, final Model model) throws IOException {
        
        if (cookie != null && cookie.length() > 0) {
            LOG.debug("Realm Cookie is {}", cookie);
            return ssoInit(cookie, sessionIndex, relayState, clientId, state, res, model);
        }
        
        Object result = SecurityUtil.sudoRun(new SecurityTask<Object>() {
            @Override
            public Object execute() {
                NeutralQuery neutralQuery = new NeutralQuery();
                neutralQuery.setOffset(0);
                neutralQuery.setLimit(9999);
                Iterable<String> realmList = getRealmEntityService().listIds(neutralQuery);

                Map<String, String> map = new HashMap<String, String>();
                for (String realmId : realmList) {
                    EntityBody node = getRealmEntityService().get(realmId);
                    map.put(node.get("id").toString(), node.get("name").toString());
                    if (realmName != null && realmName.length() > 0) {
                        if (realmName.equals(node.get("name"))) {
                            try {
                                return ssoInit(node.get("id").toString(), sessionIndex, relayState, clientId, state, res, model);
                            } catch (IOException e) {
                                LOG.error("Error initiating SSO", e);
                            }
                        }
                    }
                }
                return map;
            }

        });

        if (result instanceof String) {
            return (String) result;
        }

        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>) result;
        model.addAttribute("dummy", new HashMap<String, String>());
        model.addAttribute("realms", map);
        model.addAttribute("redirect_uri", relayState != null ? relayState : "");
        model.addAttribute("clientId", clientId);
        model.addAttribute("state", state);

        if (relayState == null) {
            model.addAttribute("errorMsg", "No relay state provided.  User won't be redirected back to the application");
        }

        return "realms";
    }

    /**
     * Redirects user to the sso init url given valid id
     * 
     * @param realmId
     *            id of the realm
     * @return directive to redirect to sso init page
     * @throws IOException
     */
    @RequestMapping(value = "sso", method = { RequestMethod.GET, RequestMethod.POST })
    public String ssoInit(
            @RequestParam(value = "realmId", required = true) final String realmId,
            @RequestParam(value = "sessionId", required = false) final String sessionId,
            @RequestParam(value = "redirect_uri", required = false) String appRelayState, 
            @RequestParam(value = "clientId", required = true) final String clientId, 
            @RequestParam(value = "state", required = false) final String state,
            HttpServletResponse res, 
            Model model) throws IOException {

        String endpoint = SecurityUtil.sudoRun(new SecurityTask<String>() {
            @Override
            public String execute() {
                EntityBody eb = getRealmEntityService().get(realmId);
                if (eb == null) {
                    throw new IllegalArgumentException("Couldn't locate idp for realm: " + realmId);
                }

                @SuppressWarnings("unchecked")
                Map<String, String> idpData = (Map<String, String>) eb.get("idp");
                return (String) idpData.get("redirectEndpoint");
            }
        });
        if (endpoint == null) {
            throw new IllegalArgumentException("Realm " + realmId + " doesn't have an endpoint");
        }
        
        // look at cookies to whether user has authenticated at their idp already
        boolean forceAuthnAtIdp = true;
        if (sessionId != null) {
            if (doesIdMapToValidOAuthSession(sessionId)) {
                LOG.info("found valid oauth session corresponding to _id: {}", sessionId);
                forceAuthnAtIdp = false;
            } else {
                LOG.info("could not find valid oauth session corresponding to _id: {}", sessionId);
            }
        } else {
            LOG.info("no oauth session provided... forcing authentication at idp");
        }      
        
        // {messageId,encodedSAML}
        Pair<String, String> tuple = saml.createSamlAuthnRequestForRedirect(endpoint, forceAuthnAtIdp);
        
        authCodeService.create(clientId, state, appRelayState, tuple.getLeft());
        LOG.debug("redirecting to: {}", endpoint);
        //DateTime expiration = new DateTime();
        //HttpCookie cookie = new HttpCookie(expiration.toDate(), "", "", "", false); --> set realmCookie to HTTP-Only
        
        Cookie cookie = new Cookie("realmCookie", realmId);
        cookie.setMaxAge(60 * 60);
        cookie.setDomain(".slidev.org");
        cookie.setPath("/");
        res.addCookie(cookie);
        LOG.debug("Set the realm cookie to {}", realmId);
        
        return "redirect:" + endpoint + "?SAMLRequest=" + tuple.getRight();
    }

    /**
     * Determines if the specified mongo id maps to a valid OAuth access token.
     * @param mongoId id of the oauth session in mongo.
     * @return true (valid session) or false (not a valid session).
     */
    public boolean doesIdMapToValidOAuthSession(String mongoId) {
        NeutralQuery neutralQuery = new NeutralQuery();
        neutralQuery.addCriteria(new NeutralCriteria("authentication.sessionIndex", "=", mongoId));
        neutralQuery.setOffset(0);
        neutralQuery.setLimit(1);
        try {
            return getOauthAccessTokenEntityService().count(neutralQuery) == 1;
        } catch(Exception e) {
            return false;
        }
    }
}
