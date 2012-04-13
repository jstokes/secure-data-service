package org.slc.sli.api.security.oauth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import com.sun.jersey.core.util.Base64;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.slc.sli.api.config.EntityDefinition;
import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.security.OauthSessionManager;
import org.slc.sli.api.security.saml.SamlHelper;
import org.slc.sli.api.service.EntityService;
import org.slc.sli.api.util.SecurityUtil;
import org.slc.sli.api.util.SecurityUtil.SecurityTask;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralQuery;

/**
 * Controller for Discovery Service
 * 
 * @author dkornishev
 */
@Controller
@Scope("request")
@RequestMapping("/oauth")
public class AuthController {
    
    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
    
    private static final Pattern BASIC_AUTH = Pattern.compile("Basic (.+)", Pattern.CASE_INSENSITIVE);
    
    @Autowired
    private EntityDefinitionStore store;
    
    @Autowired
    private MongoAuthorizationCodeServices authCodeService;
    
    @Autowired
    private SamlHelper saml;
    
    @Autowired
    private OauthSessionManager sessionManager;
    
    /**
     * Returns the Entity Service that will make calls to the realm collection.
     * 
     * @return Entity Service.
     */
    public EntityService getRealmEntityService() {
        EntityDefinition defn = store.lookupByResourceName("realm");
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
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "authorize", method = RequestMethod.GET)
    public String listRealms(@RequestParam(value = "redirect_uri", required = false) final String redirectUri, @RequestParam(value = "RealmName", required = false) final String realmName,
            @RequestParam(value = "client_id", required = true) final String clientId, @RequestParam(value = "state", required = false) final String state, @CookieValue(value = "_tla", required = false) final String sessionId,
            final HttpServletResponse res, final Model model) throws IOException {
        
        if (sessionId != null) {
            String realmId = getRealmId(sessionId);
            if (realmId != null) {
                LOG.debug("found valid session --> user authenticated in realm with id: {}", realmId);
                return ssoInit(realmId, sessionId, redirectUri, clientId, state, res, model);
            } else {
                LOG.debug("session does not map to a valid oauth session");
            }
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
                                return ssoInit(node.get("id").toString(), sessionId, redirectUri, clientId, state, res, model);
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
        
        Map<String, String> map = (Map<String, String>) result;
        model.addAttribute("dummy", new HashMap<String, String>());
        model.addAttribute("realms", map);
        model.addAttribute("redirect_uri", redirectUri != null ? redirectUri : "");
        model.addAttribute("clientId", clientId);
        model.addAttribute("state", state);
        
        if (redirectUri == null) {
            model.addAttribute("errorMsg", "No relay state provided.  User won't be redirected back to the application");
        }
        
        // Create session
        
        return "realms";
    }
    
    @RequestMapping(value = "token", method = { RequestMethod.POST, RequestMethod.GET })
    public ResponseEntity<String> getAccessToken(@RequestParam("code") String authorizationCode, @RequestParam("redirect_uri") String redirectUri, @RequestHeader(value = "Authorization", required = false) String authz,
            @RequestParam("client_id") String clientId, @RequestParam("client_secret") String clientSecret, Model model) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("code", authorizationCode);
        parameters.put("redirect_uri", redirectUri);
        
        Pair<String, String> tuple = Pair.of(clientId, clientSecret); // extractClientCredentials(authz);
        
        String token = this.sessionManager.verify(authorizationCode, Pair.of(clientId, clientSecret));
        // OAuth2AccessToken token = granter.grant("authorization_code", parameters, tuple.getLeft(), tuple.getRight(), new HashSet<String>());
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        String response = String.format("{\"access_token\":\"%s\"}", token);
        info("Access Token: \n{}", response);
        
        return new ResponseEntity<String>(response, headers, HttpStatus.OK);
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
    public String ssoInit(@RequestParam(value = "realmId", required = true) final String realmIndex, @RequestParam(value = "sessionId", required = false) final String sessionId,
            @RequestParam(value = "redirect_uri", required = false) String redirectUri, @RequestParam(value = "clientId", required = true) final String clientId, @RequestParam(value = "state", required = false) final String state,
            HttpServletResponse res, Model model) throws IOException {
        
        String realmId = getRealmId(sessionId);
        boolean forceAuthn = (sessionId != null && realmId != null) ? false : true;
        
        EntityBody realmEnt = SecurityUtil.sudoRun(new SecurityTask<EntityBody>() {
            @Override
            public EntityBody execute() {
                EntityBody body = getRealmEntityService().get(realmIndex);
                if (body == null) {
                    throw new IllegalArgumentException("couldn't locate idp for realm: " + realmIndex);
                }
                return body;
            }
        });
        
        @SuppressWarnings("unused")
        String tenantId = (String) realmEnt.get("tenantId");
        
        @SuppressWarnings("unchecked")
        Map<String, String> idpData = (Map<String, String>) realmEnt.get("idp");
        String endpoint = (String) idpData.get("redirectEndpoint");
        
        if (endpoint == null) {
            throw new IllegalArgumentException("realm " + realmIndex + " doesn't have an endpoint");
        }
        
        // {messageId,encodedSAML}
        LOG.debug("creating saml authnrequest with ForceAuthn equal to {}", forceAuthn);
        Pair<String, String> tuple = saml.createSamlAuthnRequestForRedirect(endpoint, forceAuthn);
        
        this.sessionManager.createAppSession(sessionId, clientId, redirectUri, state, tenantId, tuple.getLeft());
        
        LOG.debug("redirecting to: {}", endpoint);
        
        return "redirect:" + endpoint + "?SAMLRequest=" + tuple.getRight();
    }
    
    @SuppressWarnings("unchecked")
    private String getRealmId(final String sessionId) {
        String realmId = null;
        if (sessionId != null) {
            Entity session = this.sessionManager.getSession(sessionId);
            if (session != null) {
                Map<String, Object> principal = (Map<String, Object>) session.getBody().get("principal");
                realmId = (String) principal.get("realm");
            }
        }
        
        return realmId;
    }
    
    @SuppressWarnings("unused")
    private Pair<String, String> extractClientCredentials(String authz) {
        Matcher m = BASIC_AUTH.matcher(authz);
        
        if (authz != null && m.find()) {
            String decoded = Base64.base64Decode(m.group(1));
            String[] values = decoded.split(":");
            return Pair.of(values[0], values[1]);
        } else {
            throw new IllegalArgumentException("Client auth must be provided");
        }
    }
}
