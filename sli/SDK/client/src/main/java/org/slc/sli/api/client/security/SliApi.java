package org.slc.sli.api.client.security;

import java.net.URL;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuth20ServiceImpl;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

/**
 * @author jnanney
 * 
 */
public class SliApi extends DefaultApi20 {
    
    // TODO - this assumes we're sharing this across all sessions. Is this assumption valid?
    private static URL apiUrl;
    private static final String REQUEST_TOKEN_FRAGMENT = "%sapi/oauth/authorize?response_type=code&client_id=%s&redirect_uri=%s";
    private static final String AUTH_TOKEN_FRAGMENT = "%sapi/oauth/token?grant_type=authorization_code";
    
    @Override
    public String getAccessTokenEndpoint() {
        return String.format(AUTH_TOKEN_FRAGMENT, apiUrl.toString());
    }
    
    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback.");
        
        return String.format(REQUEST_TOKEN_FRAGMENT, apiUrl.toString(), config.getApiKey(),
                OAuthEncoder.encode(config.getCallback()));
    }
    
    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new SliTokenExtractor();
    }
    
    /**
     * @param baseUrl
     *            the base URL for the API ReST server.
     */
    public static void setBaseUrl(final URL baseUrl) {
        SliApi.apiUrl = baseUrl;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public OAuthService createService(OAuthConfig config) {
        return new SLIOauth20ServiceImpl(this, config);
    }
    
    public class SLIOauth20ServiceImpl extends OAuth20ServiceImpl {
        
        private static final String VERSION = "2.0";
        
        private final DefaultApi20 myApi;
        private final OAuthConfig myConfig;
        
        public SLIOauth20ServiceImpl(DefaultApi20 api, OAuthConfig config) {
            super(api, config);
            myApi = api;
            myConfig = config;
        }
        
        public Response getAccessToken(Token requestToken, Verifier verifier, Token token) {
            OAuthRequest request = new OAuthRequest(myApi.getAccessTokenVerb(), myApi.getAccessTokenEndpoint());
            request.addQuerystringParameter(OAuthConstants.CLIENT_ID, myConfig.getApiKey());
            request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, myConfig.getApiSecret());
            request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
            request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, myConfig.getCallback());
            if (myConfig.hasScope()) {
                request.addQuerystringParameter(OAuthConstants.SCOPE, myConfig.getScope());
            }
            Response response = request.send();
            token = myApi.getAccessTokenExtractor().extract(response.getBody());
            return response;
        }
    }
}
