package org.slc.sli.api.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.UnconfirmedAuthorizationCodeClientToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.security.SLIPrincipal;
import org.slc.sli.api.security.oauth.MongoTokenStore;
import org.slc.sli.api.security.resolve.RolesToRightsResolver;
import org.slc.sli.api.security.resolve.UserLocator;
import org.slc.sli.api.util.SecurityUtil.SecurityTask;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.Repository;
import org.slc.sli.domain.enums.Right;

/**
 * Utilities for the OAuth 2.0 implementations of SliTokenService and
 * SliTokenStore.
 *
 * @author shalka
 */
@Component
public class OAuthTokenUtil {

    @Autowired
    private UserLocator locator;

    @Autowired
    private Repository<Entity> repo;

    @Autowired
    private RolesToRightsResolver resolver;

    @Autowired
    private MongoTokenStore tokenStore;

    /**
     * Name of the collection in Mongo that stores OAuth 2.0 session
     * information.
     */
    private static final String OAUTH_ACCESS_TOKEN_COLLECTION = "oauth_access_token";

    /**
     * Lifetime (duration of validity) of an Access Token in seconds.
     */
    private static final int ACCESS_TOKEN_VALIDITY = 1800;


    /**
     * Get the name of the collection in Mongo that stores OAuth 2.0 session
     * information.
     *
     * @return String representing Mongo collection name.
     */
    public static String getOAuthAccessTokenCollectionName() {
        return OAUTH_ACCESS_TOKEN_COLLECTION;
    }

    /**
     * Returns true if the current time (in ms) is greater than the specified
     * expiration date (indicating that expiration is true).
     *
     * @param expiration Date to be checked (represented by number of milliseconds since last epoch).
     * @return 'true' if expired, 'false' if not expired.
     */
    public static boolean isTokenExpired(long expiration) {
        return System.currentTimeMillis() > expiration;
    }

    /**
     * Returns the validity of an access token in seconds.
     *
     * @return Integer representing the number of seconds that the access token is valid for.
     */
    public static int getAccessTokenValidity() {
        return ACCESS_TOKEN_VALIDITY;
    }

    /**
     * This method will create an OAuth2Authentication based on the data
     * that comes from the access token table.
     *
     * @param data - the data that comes from the access token table
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public OAuth2Authentication createOAuth2Authentication(Map data) {
        String realm = (String) data.get("realm");
        String externalId = (String) data.get("externalId");
        NeutralQuery neutralQuery = new NeutralQuery();
        neutralQuery.addCriteria(new NeutralCriteria("_id", "=", realm));

        Entity realmEntity = repo.findOne("realm", neutralQuery);
        SLIPrincipal principal = locator.locate((String) realmEntity.getBody().get("tenantId"), externalId);
        principal.setName((String) data.get("name"));
        principal.setRoles((List<String>) data.get("roles"));
        principal.setRealm(realm);
        principal.setAdminRealm((String) data.get("adminRealm"));
        principal.setEdOrg((String) data.get("edOrg"));
        return reconstituteAuth(principal, data);
    }

    /**
     * Helper method used by createOAuth2Authentication
     *
     * @param principal
     * @param data
     * @return
     */
    @SuppressWarnings("rawtypes")
    protected OAuth2Authentication reconstituteAuth(final SLIPrincipal principal, Map data) {
        Set<String> scope = listToSet((List) data.get("scope"));
        Set<String> resourceIds = listToSet((List) data.get("resourceIds"));
        Collection<GrantedAuthority> clientAuthorities = deserializeAuthorities(listToSet((List) data.get("clientAuthorities")));
        Collection<GrantedAuthority> userAuthorities = SecurityUtil.sudoRun(new SecurityTask<Collection<GrantedAuthority>>() {
            @Override
            public Collection<GrantedAuthority> execute() {
                return resolver.resolveRoles(principal.getRealm(), principal.getRoles());
            }
        });

        ClientToken client = new ClientToken((String) data.get("clientId"),
                resourceIds,
                (String) data.get("clientSecret"),
                scope,
                clientAuthorities);
        UnconfirmedAuthorizationCodeClientToken token = new UnconfirmedAuthorizationCodeClientToken(client.getClientId(),
                client.getClientSecret(),
                scope,
                (String) data.get("state"),
                (String) data.get("requestedRedirect"));
        PreAuthenticatedAuthenticationToken user = new PreAuthenticatedAuthenticationToken(principal, token, userAuthorities);
        user.setDetails((String) data.get("sessionIndex"));
        return new OAuth2Authentication(client, user);
    }

    /**
     * Given an OAuth2Authentication object, this method will create Entity data
     * out of that object so that it can be saved to Mongo and reconstituted at a later time
     *
     * @param auth
     * @return
     */
    public EntityBody serializeOauth2Auth(OAuth2Authentication auth) {
        EntityBody body = new EntityBody();
        SLIPrincipal principal = (SLIPrincipal) auth.getPrincipal();
        body.put("realm", principal.getRealm());
        body.put("externalId", principal.getExternalId());
        body.put("name", principal.getName());
        body.put("roles", principal.getRoles());
        body.put("adminRealm", principal.getAdminRealm());
        body.put("edOrg", principal.getEdOrg());
        body.put("clientId", auth.getClientAuthentication().getClientId());
        body.put("clientSecret", auth.getClientAuthentication().getClientSecret());
        body.put("scope", auth.getClientAuthentication().getScope());
        body.put("userAuthorities", serializeAuthorities(auth.getUserAuthentication().getAuthorities()));
        body.put("clientAuthorities", serializeAuthorities(auth.getClientAuthentication().getAuthorities()));
        body.put("resourceIds", auth.getClientAuthentication().getResourceIds());
        body.put("sessionIndex", auth.getUserAuthentication().getDetails().toString());
        UnconfirmedAuthorizationCodeClientToken token = (UnconfirmedAuthorizationCodeClientToken) auth.getUserAuthentication().getCredentials();
        body.put("state", token.getState());
        body.put("requestedRedirect", token.getRequestedRedirect());
        return body;
    }

    public Collection<String> getTokensForUser(String userName, String realmId) {
        NeutralQuery query = new NeutralQuery();
        query.addCriteria(new NeutralCriteria("authentication.name", NeutralCriteria.OPERATOR_EQUAL, userName));
        query.addCriteria(new NeutralCriteria("authentication.realm", NeutralCriteria.OPERATOR_EQUAL, realmId));

        Iterable<Entity> entities = repo.findAll(OAUTH_ACCESS_TOKEN_COLLECTION, query);

        Collection<String> tokens = new ArrayList<String>();
        for (Entity entity : entities) {
            tokens.add((String) entity.getBody().get("token"));
        }
        return tokens;
    }

    public EntityBody serializeAccessToken(OAuth2AccessToken token) {
        EntityBody body = new EntityBody();
        body.put("type", token.getTokenType());
        body.put("expiration", token.getExpiration());
        body.put("value", token.getValue());
        body.put("scope", token.getScope());
        return body;
    }

    @SuppressWarnings("rawtypes")
    public OAuth2AccessToken deserializeAccessToken(Map data) {
        OAuth2AccessToken token = new OAuth2AccessToken((String) data.get("value"));
        token.setExpiration((Date) data.get("expiration"));
        token.setTokenType((String) data.get("type"));
        token.setScope(listToSet((List) data.get("scope")));
        return token;
    }


    @SuppressWarnings("rawtypes")
    private static Set<String> listToSet(List list) {
        HashSet<String> set = new HashSet<String>();
        for (Object o : list) {
            set.add((String) o);
        }
        return set;
    }

    private static Collection<GrantedAuthority> deserializeAuthorities(Set<String> auths) {
        List<GrantedAuthority> toReturn = new ArrayList<GrantedAuthority>();
        for (String auth : auths) {
            toReturn.add(Right.valueOf(auth));
        }
        return toReturn;
    }

    private static Set<String> serializeAuthorities(Collection<GrantedAuthority> auths) {
        Set<String> toReturn = new HashSet<String>();
        for (GrantedAuthority auth : auths) {
            toReturn.add(auth.toString());
        }
        return toReturn;
    }

    public void removeExpiredTokens() {
        NeutralQuery removeQuery = new NeutralQuery();
        removeQuery.addCriteria(new NeutralCriteria("accessToken.expiration", "<", new Date()));

        Iterable<Entity> expired = repo.findAll(OAUTH_ACCESS_TOKEN_COLLECTION, removeQuery);
        for (Entity remover : expired) {
            repo.delete(OAUTH_ACCESS_TOKEN_COLLECTION, remover.getEntityId());
        }
    }

    public boolean deleteTokensForUser(String userName, String realmId) {
        RemoveTokensTask removeTokensTask = new RemoveTokensTask();
        removeTokensTask.setUserName(userName);
        removeTokensTask.setRealmId(realmId);
        Object result = SecurityUtil.sudoRun(removeTokensTask);
        return Boolean.valueOf(result.toString());
    }

    public boolean deleteTokensForPrincipal(Authentication oAuth) {
        SLIPrincipal principal = (SLIPrincipal) oAuth.getPrincipal();
        if (deleteTokensForUser(principal.getName(), principal.getRealm())) {
            return true;
        }
        return false;
    }

    private class RemoveTokensTask implements SecurityTask<Object> {

        private String userName;
        private String realmId;

        @Override
        public java.lang.Object execute() {
            Collection<String> appTokens = getTokensForUser(userName, realmId);
            for (String token : appTokens) {
                tokenStore.removeAccessToken(token);
            }
            return true;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setRealmId(String realmId) {
            this.realmId = realmId;
        }
    }
}
