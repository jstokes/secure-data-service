package org.slc.sli.api.security.oauth;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
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
import org.springframework.security.oauth2.common.DefaultOAuth2SerializationService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2SerializationService;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
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
import org.slc.sli.api.security.saml.SamlHelper;
import org.slc.sli.api.service.EntityService;
import org.slc.sli.api.util.SecurityUtil;
import org.slc.sli.api.util.SecurityUtil.SecurityTask;
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
public class AuthController {
    
    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
    
    private static final Pattern BASIC_AUTH = Pattern.compile("Basic (.+)", Pattern.CASE_INSENSITIVE);
    
    private OAuth2SerializationService serializationService = new DefaultOAuth2SerializationService();
    
    @Autowired
    private EntityDefinitionStore store;
    
    @Autowired
    private MongoAuthorizationCodeServices authCodeService;
    
    @Autowired
    private SamlHelper saml;
    
    @Autowired
    private AuthorizationServerTokenServices tokenServices;
    
    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;
    
    @Autowired
    private ClientDetailsService clientDetailsService;
    
    private EntityService service;
    
    private AuthorizationCodeTokenGranter granter;
    
    @PostConstruct
    public void init() {
        EntityDefinition def = store.lookupByResourceName("realm");
        service = def.getService();
        granter = new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetailsService);
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
    public String listRealms(@RequestParam(value = "redirect_uri", required = false) final String relayState, @RequestParam(value = "RealmName", required = false) final String realmName,
            @RequestParam(value = "client_id", required = true) final String clientId, @RequestParam(value = "state", required = false) final String state, @CookieValue(value = "realmCookie", required = false) final String cookie,
            final HttpServletResponse res, final Model model) throws IOException {
        LOG.debug("Realm Cookie is {}", cookie);
        
        if (cookie != null && cookie.length() > 0) {
            return ssoInit(cookie, relayState, clientId, state, res, model);
        }
        
        Object result = SecurityUtil.sudoRun(new SecurityTask<Object>() {
            @Override
            public Object execute() {
                NeutralQuery neutralQuery = new NeutralQuery();
                neutralQuery.setOffset(0);
                neutralQuery.setLimit(9999);
                Iterable<String> realmList = service.listIds(neutralQuery);
                
                Map<String, String> map = new HashMap<String, String>();
                for (String realmId : realmList) {
                    EntityBody node = service.get(realmId);
                    map.put(node.get("id").toString(), node.get("name").toString());
                    if (realmName != null && realmName.length() > 0) {
                        if (realmName.equals(node.get("name"))) {
                            try {
                                return ssoInit(node.get("id").toString(), relayState, clientId, state, res, model);
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
        model.addAttribute("redirect_uri", relayState != null ? relayState : "");
        model.addAttribute("clientId", clientId);
        model.addAttribute("state", state);
        
        if (relayState == null) {
            model.addAttribute("errorMsg", "No relay state provided.  User won't be redirected back to the application");
        }
        
        return "realms";
    }
    
    @RequestMapping(value = "token", method = { RequestMethod.POST, RequestMethod.GET })
    public ResponseEntity<String> getAccessToken(@RequestParam("code") String authorizationCode, @RequestParam("redirect_uri") String redirectUri, @RequestHeader(value = "Authorization", required = false) String authz,
            @RequestParam("client_id") String clientId, @RequestParam("client_secret") String clientSecret, Model model) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("code", authorizationCode);
        parameters.put("redirect_uri", redirectUri);
        
        info("Hoora");
        Pair<String, String> tuple = Pair.of(clientId, clientSecret); // extractClientCredentials(authz);
        OAuth2AccessToken token = granter.grant("authorization_code", parameters, tuple.getLeft(), tuple.getRight(), new HashSet<String>());
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        return new ResponseEntity<String>(serializationService.serialize(token), headers, HttpStatus.OK);
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
    public String ssoInit(@RequestParam(value = "realmId", required = true) final String realmId, @RequestParam(value = "redirect_uri", required = false) String appRelayState,
            @RequestParam(value = "clientId", required = true) final String clientId, @RequestParam(value = "state", required = false) final String state, HttpServletResponse res, Model model) throws IOException {
        
        String endpoint = SecurityUtil.sudoRun(new SecurityTask<String>() {
            @Override
            public String execute() {
                EntityBody eb = service.get(realmId);
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
        
        // {messageId,encodedSAML}
        Pair<String, String> tuple = saml.createSamlAuthnRequestForRedirect(endpoint);
        
        authCodeService.create(clientId, state, appRelayState, tuple.getLeft());
        LOG.debug("redirecting to: {}", endpoint);
        Cookie cookie = new Cookie("realmCookie", realmId);
        cookie.setMaxAge(60 * 60);
        cookie.setDomain(".slidev.org");
        cookie.setPath("/");
        res.addCookie(cookie);
        LOG.debug("Set the realm cookie to {}", realmId);
        return "redirect:" + endpoint + "?SAMLRequest=" + tuple.getRight();
    }
    
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
