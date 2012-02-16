package org.slc.sli.api.security.oauth;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;

import org.slc.sli.api.config.EntityDefinition;
import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.security.SLIPrincipal;
import org.slc.sli.api.security.resolve.RolesToRightsResolver;
import org.slc.sli.api.service.EntityService;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.EntityRepository;

/**
 * Responsible for storage and management of access and refresh tokens for OAuth
 * 2.0 implementation.
 * 
 * 
{
  "_id" : UUID [auto-generated by Mongo],
  "userId" : String,  
  "userRealm" : String,  
  "userRoles" : String,  
  "accessToken" : 
  {
    "value" : String,
    "expiration" : Date,
    "tokenType" : String,
    "refreshToken" :
    {
      "value" : String,
      "expiration" : Date
    }
  }
}
 * 
 * @author shalka
 */
public class TokenManager implements TokenStore {
    
    @Autowired
    private EntityRepository repo;

    @Autowired
    private EntityDefinitionStore store;

    private EntityService         service;
    
    @Autowired
    private RolesToRightsResolver rolesToRightsResolver;

    @PostConstruct
    public void init() {
        EntityDefinition def = store.lookupByResourceName("authSession");
        setService(def.getService());
    }
    
    public void setService(EntityService service) {
        this.service = service;
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        Iterable<Entity> results = repo.findByQuery("authSession", new Query(Criteria.where("body.accessToken.value").is(token.getValue())), 0, 1);
        for (Entity cur : results) {
            Map<String, Object> body = cur.getBody();
            
            ClientToken clientToken = new ClientToken(((String) body.get("userId")), null, null, null, 
                    rolesToRightsResolver.resolveRoles((String) body.get("userRealm"), (List<String>) body.get("userRoles")));
            return new OAuth2Authentication(clientToken, null);
        }
        return null;
    }

    @Override
    public OAuth2Authentication readAuthentication(ExpiringOAuth2RefreshToken token) {
        Iterable<Entity> results = repo.findByQuery("authSession", new Query(Criteria.where("body.accessToken.refreshToken.value").is(token.getValue())), 0, 1);
        for (Entity cur : results) {
            Map<String, Object> body = cur.getBody();
            
            ClientToken clientToken = new ClientToken(((String) body.get("userId")), null, null, null, 
                    rolesToRightsResolver.resolveRoles((String) body.get("userRealm"), (List<String>) body.get("userRoles")));
            return new OAuth2Authentication(clientToken, null);
        }
        return null;
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        EntityBody container = new EntityBody();
        SLIPrincipal principal = (SLIPrincipal) authentication.getPrincipal();
        container.put("userId", principal.getId());
        container.put("userRealm", principal.getRealm());
        container.put("userRoles", principal.getRoles());  // ?

        EntityBody accessToken = new EntityBody();
        accessToken.put("value", token.getValue());
        accessToken.put("expiration", token.getExpiration());
        accessToken.put("tokenType", token.getTokenType());
        ExpiringOAuth2RefreshToken rt = (ExpiringOAuth2RefreshToken) token.getRefreshToken();
        EntityBody refreshToken = new EntityBody();
        refreshToken.put("value", token.getRefreshToken().getValue());
        refreshToken.put("expiration", rt.getExpiration());
        accessToken.put("refreshToken", refreshToken);
        
        container.put("accessToken", accessToken);
        
        service.create(container);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        Iterable<Entity> results = repo.findByQuery("authSession", 
                new Query(Criteria.where("body.accessToken.value").is(tokenValue)), 0, 1);
        for (Entity entity : results) {
            OAuth2AccessToken result = new OAuth2AccessToken(tokenValue);
            @SuppressWarnings("unchecked")
            Map<String, Object> accessToken = (Map<String, Object>) entity.getBody().get("accessToken");
            result.setExpiration((Date) accessToken.get("expiration"));
            result.setTokenType((String) accessToken.get("tokenType"));
            
            Map<String, Object> refreshToken = (Map<String, Object>) accessToken.get("refreshToken");
            
            ExpiringOAuth2RefreshToken rt = new ExpiringOAuth2RefreshToken((String) refreshToken.get("value"), 
                    (Date) refreshToken.get("expiration"));
            return result;
        }
        return null;
    }

    @Override
    public void removeAccessToken(String tokenValue) {
        Iterable<Entity> results = repo.findByQuery("authSession", 
                new Query(Criteria.where("body.accessToken.value").is(tokenValue)), 0, 1);
        for (Entity entity : results) {
            service.delete(entity.getEntityId());
        }
    }

    @Override
    public void storeRefreshToken(ExpiringOAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        SLIPrincipal principal = (SLIPrincipal) authentication.getClientAuthentication().getPrincipal();
        if (principal != null) {
            
            Iterable<Entity> results = repo.findByQuery("authSession",
                    new Query(Criteria.where("body.userId").is(principal.getId())), 0, 1);
            for (Entity cur : results) {
                Map<String, Object> body = cur.getBody();
                Map<String, Object> rt = new HashMap<String, Object>();
                rt.put("expiration", refreshToken.getExpiration());
                rt.put("value", refreshToken.getValue());
                Map<String, Object> accessToken = (Map<String, Object>) body.get("accessToken");
                accessToken.put("refreshToken", rt);
                service.update(cur.getEntityId(), (EntityBody) body);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public ExpiringOAuth2RefreshToken readRefreshToken(String tokenValue) {
        Iterable<Entity> results = repo.findByQuery("authSession", new Query(Criteria.where("body.accessToken.refreshToken.value").is(tokenValue)), 0, 1);
        for (Entity cur : results) {
            Map<String, Object> accessToken = (Map<String, Object>) cur.getBody().get("accessToken");
            Map<String, Object> refreshToken = (Map<String, Object>) accessToken.get("refreshToken");
            Date expirationDate = (Date) refreshToken.get("expiration");
            return new ExpiringOAuth2RefreshToken(tokenValue, expirationDate);
        }
        return null;
    }

    @Override
    public void removeRefreshToken(String tokenValue) {
        Iterable<Entity> results = repo.findByQuery("authSession", new Query(Criteria.where("body.accessToken.refreshToken.value").is(tokenValue)), 0, 1);
        for (Entity cur : results) {
            cur.getBody().remove("refreshToken");
            service.update(cur.getEntityId(), (EntityBody) cur.getBody());
        }
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(String refreshToken) {
        Iterable<Entity> results = repo.findByQuery("authSession", new Query(Criteria.where("body.accessToken.value").is(refreshToken)), 0, 1);
        for (Entity cur : results) {
            service.delete(cur.getEntityId());
        }
    }
}
