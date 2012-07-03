package org.slc.sli.api.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import javax.ws.rs.core.Response;

public interface RESTClient {
    
    /**
     * Get the URL used to authenticate with the IDP.
     *
     * @return URL
     */
    public abstract URL getLoginURL();
    
    /**
     * Connect to the IDP and redirect to the callback URL.
     *
     * @param requestCode
     *            Authorization request code returned by oauth to the callbackURL.
     * @param authorizationToken
     *            for the authenticated user, or null if the request failed.
     * @return Response containing the status code, headers, and body values.
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public abstract Response connect(final String authorizationCode, String authorizationToken)
            throws MalformedURLException, URISyntaxException;
    
    /**
     * Disconnect from the IDP.
     */
    public abstract void disconnect();
    
    /**
     * Call the session/check API. If the SAML token is invalid or null, this will redirect
     * to the realm selector page.
     *
     * @param token
     *            SAML token or null.
     * @param redirectUrl
     *            The redirect URL after a successful authentication - set by the Security API.
     * @return String containing the authentication token.
     * @throws URISyntaxException
     * @throws IOException
     */
    public abstract String sessionCheck(final String token) throws URISyntaxException, IOException;
    
    /**
     * Make a synchronous GET request to a REST service.
     *
     * @param url
     *            full URL to the request.
     * @return ClientResponse containing the status code and return values.
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public abstract Response getRequest(final URL url) throws MalformedURLException, URISyntaxException;
    
    /**
     * Make a synchronous GET request to a REST service.
     *
     * @param sessionToken
     *            Session token.
     * @param url
     *            full URL to the request.
     * @return ClientResponse containing the status code and return values.
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public abstract Response getRequest(final String sessionToken, final URL url) throws MalformedURLException,
            URISyntaxException;
    
    /**
     * Make a synchronous GET request to a REST service. The request includes additional header
     * information.
     *
     * @param url
     *
     * @param URL
     *            Fully qualified URL to the ReSTful resource.
     * @param headers
     *            key / value pairs of the headers to attach to the request.
     * @return ClientResponse containing the status code and return value(s).
     * @throws URISyntaxException
     */
    public abstract Response getRequestWithHeaders(final URL url, final Map<String, Object> headers)
            throws URISyntaxException;
    
    /**
     * Get the sessionToken for all SLI API ReSTful service calls.
     *
     * @return sessionToken
     */
    public abstract String getSessionToken();
    
    /**
     * Make a synchronous GET request to a REST service. The request includes additional header
     * information.
     *
     * @param sessionToken
     *            Session token.
     *
     * @param url
     *
     * @param URL
     *            Fully qualified URL to the ReSTful resource.
     * @param headers
     *            key / value pairs of the headers to attach to the request.
     * @return ClientResponse containing the status code and return value(s).
     * @throws URISyntaxException
     */
    public abstract Response getRequestWithHeaders(final String sessionToken, final URL url,
            final Map<String, Object> headers) throws URISyntaxException;
    
    /**
     * Synchronously post a new entity to the REST service. This corresponds to a create operation.
     *
     * @param url
     *            Fully qualified URL to the ReSTful resource.
     * @param json
     *            Json entity to post.
     * @return ClientResponse containing the status code and return value(s).
     * @throws URISyntaxException
     * @throws MalformedURLException
     */
    public abstract Response postRequest(final URL url, final String json) throws URISyntaxException,
            MalformedURLException;
    
    /**
     * Synchronously post a new entity to the REST service. This corresponds to a create operation.
     *
     * @param sessionToken
     *            Session token.
     * @param url
     *            Fully qualified URL to the ReSTful resource.
     * @param json
     *            Json entity to post.
     * @return ClientResponse containing the status code and return value(s).
     * @throws URISyntaxException
     * @throws MalformedURLException
     */
    public abstract Response postRequest(final String sessionToken, final URL url, final String json)
            throws URISyntaxException, MalformedURLException;
    
    /**
     * Synchronously post a new entity to the REST service. This request includes additional header
     * information.
     *
     * @param url
     *            Fully qualified URL to the ReSTful resource.
     * @param json
     *            JSON to post.
     * @param headers
     *            key / value pairs of the headers to attach to the request. A key can map
     *            to multiple values.
     * @return ClientResponse containing the status code and return value(s).
     * @throws URISyntaxException
     * @throws MalformedURLException
     */
    public abstract Response postRequestWithHeaders(final URL url, final String json, final Map<String, Object> headers)
            throws URISyntaxException, MalformedURLException;
    
    /**
     * Synchronously post a new entity to the REST service. This request includes additional header
     * information.
     *
     * @param sessionToken
     *            Session token.
     * @param url
     *            Fully qualified URL to the ReSTful resource.
     * @param json
     *            JSON to post.
     * @param headers
     *            key / value pairs of the headers to attach to the request. A key can map
     *            to multiple values.
     * @return ClientResponse containing the status code and return value(s).
     * @throws URISyntaxException
     * @throws MalformedURLException
     */
    public abstract Response postRequestWithHeaders(final String sessionToken, final URL url, final String json,
            final Map<String, Object> headers) throws URISyntaxException, MalformedURLException;
    
    /**
     * Synchronous Put request to the REST service. This corresponds to an update operation.
     *
     * @param url
     *            Fully qualified URL to the ReSTful resource.
     * @param json
     *            JSON of the entity to PUT.
     * @return ClientResponse containing the status code and return value(s).
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public abstract Response putRequest(final URL url, final String json) throws MalformedURLException,
            URISyntaxException;
    
    /**
     * Synchronous Put request to the REST service. This corresponds to an update operation.
     *
     * @param sessionToken
     *            Session token.
     *
     * @param url
     *            Fully qualified URL to the ReSTful resource.
     * @param json
     *            JSON of the entity to PUT.
     * @return ClientResponse containing the status code and return value(s).
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public abstract Response putRequest(final String sessionToken, final URL url, final String json)
            throws MalformedURLException, URISyntaxException;
    
    /**
     * Synchronous Put request to the REST service. This corresponds to an update operation.
     * This request includes additional header information.
     *
     * @param url
     *            Fully qualified URL to the ReSTful resource.
     * @param json
     *            JSON of the entity to PUT.
     * @param headers
     *            key / value pairs of the headers to attach to the request. A key can map
     *            to multiple values.
     * @return ClientResponse containing the status code and return value(s).
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public abstract Response putRequestWithHeaders(final URL url, final String json, final Map<String, Object> headers)
            throws MalformedURLException, URISyntaxException;
    
    /**
     * Synchronous Put request to the REST service. This corresponds to an update operation.
     * This request includes additional header information.
     *
     * @param sessionToken
     *            Session token.
     * @param url
     *            Fully qualified URL to the ReSTful resource.
     * @param json
     *            JSON of the entity to PUT.
     * @param headers
     *            key / value pairs of the headers to attach to the request. A key can map
     *            to multiple values.
     * @return ClientResponse containing the status code and return value(s).
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public abstract Response putRequestWithHeaders(final String sessionToken, final URL url, final String json,
            final Map<String, Object> headers) throws MalformedURLException, URISyntaxException;
    
    /**
     * Synchronously delete an existing entity using the REST service.
     *
     * @param url
     *            Fully qualified URL to the ReSTful resource.
     * @return ClientResponse containing the status code and return value(s).
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public abstract Response deleteRequest(final URL url) throws MalformedURLException, URISyntaxException;
    
    /**
     * Synchronously delete an existing entity using the REST service.
     *
     * @param sessionToken
     *            Session token.
     * @param url
     *            Fully qualified URL to the ReSTful resource.
     * @return ClientResponse containing the status code and return value(s).
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public abstract Response deleteRequest(final String sessionToken, final URL url) throws MalformedURLException,
            URISyntaxException;
    
    /**
     * Synchronously delete an existing entity using the REST service. This request includes
     * additional header
     * information.
     *
     * @param url
     *            Fully qualified URL to the ReSTful resource.
     * @param headers
     *            key / value pairs of the headers to attach to the request. A key can map
     *            to multiple values.
     * @return ClientResponse containing the status code and return value(s).
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public abstract Response deleteRequestWithHeaders(final URL url, final Map<String, Object> headers)
            throws MalformedURLException, URISyntaxException;
    
    /**
     * Synchronously delete an existing entity using the REST service. This request includes
     * additional header
     * information.
     *
     * @param sessionToken
     *            Session token.
     *
     * @param url
     *            Fully qualified URL to the ReSTful resource.
     * @param headers
     *            key / value pairs of the headers to attach to the request. A key can map
     *            to multiple values.
     * @return ClientResponse containing the status code and return value(s).
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public abstract Response deleteRequestWithHeaders(final String sessionToken, final URL url,
            final Map<String, Object> headers) throws MalformedURLException, URISyntaxException;
    
    /**
     * Get the base URL for all SLI API ReSTful service calls.
     *
     * @return Server URL string.
     */
    public abstract String getBaseURL();
    
    /**
     * Set the sessionToken for all SLI API ReSTful service calls.
     *
     * @param sessionToken
     */
    public abstract void setSessionToken(String sessionToken);
    
}