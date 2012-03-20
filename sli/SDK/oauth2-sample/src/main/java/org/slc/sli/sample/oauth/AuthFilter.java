package org.slc.sli.sample.oauth;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.slc.sli.api.client.impl.BasicClient;

/**
 * Basic authentication example using the SLI SDK.
 */
public class AuthFilter implements Filter {
    
    private static final Logger LOG = LoggerFactory.getLogger(AuthFilter.class);
    private String clientId;
    private String clientSecret;
    private URL apiUrl;
    private String callbackUrl;
    private String afterCallbackRedirect;
    
    @Override
    public void destroy() {
        LOG.info("Destroy auth filter");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if (req.getRequestURI().equals("/oauth2-sample/callback")) {
            handleCallback(request, response);
            ((HttpServletResponse) response).sendRedirect(afterCallbackRedirect);
            return;
        } else if (req.getSession().getAttribute("client") == null) {
            authenticate(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }
    
    private void handleCallback(ServletRequest request, ServletResponse response) {
        BasicClient client = (BasicClient) ((HttpServletRequest) request).getSession().getAttribute("client");
        String code = ((HttpServletRequest) request).getParameter("code");
        LOG.debug("Got authoriation code: {}", code);
        String accessToken = client.connect(code);
        LOG.debug("Got access token: {}", accessToken);
    }
    
    private void authenticate(ServletRequest req, ServletResponse res) {
        
        BasicClient client = new BasicClient(apiUrl, clientId, clientSecret, callbackUrl);
        try {
            ((HttpServletResponse) res).sendRedirect(client.getLoginURL().toExternalForm());
        } catch (IOException e) {
            LOG.error("Bad redirect", e);
        }
        ((HttpServletRequest) req).getSession().setAttribute("client", client);
    }
    
    @Override
    public void init(FilterConfig conf) throws ServletException {
        // TODO refector to use spring + env specific config files as soon as possible
        clientId = conf.getInitParameter("clientId");
        clientSecret = conf.getInitParameter("clientSecret");
        afterCallbackRedirect = conf.getInitParameter("afterCallbackRedirect");
        
        try {
            apiUrl = new URL(conf.getInitParameter("apiUrl"));
        } catch (MalformedURLException e) {
            throw new ServletException("Bad API URL: " + apiUrl, e);
        }
        
        String env = System.getProperty("sli.env");
        if (env != null && "local".equalsIgnoreCase(env)) {
            // use the default value in the web.xml
            callbackUrl = conf.getInitParameter("callbackUrl");
        } else if (env != null && "nxbuild2".equalsIgnoreCase(env)) {
            callbackUrl = "https://nxbuild2.slidev.org/oauth2-sample/callback";
            clientId = "ET1k3PdHzX";
            clientSecret = "Ok2iofHvnmguiGXvePLQY2K6UHFK+WZNNgVfYQaBYcAa3iXQ";
        } else {
            throw new RuntimeException("Unsuported environment: " + env);
        }
    }
    
}
