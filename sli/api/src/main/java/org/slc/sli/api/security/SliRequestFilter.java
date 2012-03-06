package org.slc.sli.api.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * A security filter responsible for checking SLI session
 * 
 * @author dkornishev
 */
@Component
public class SliRequestFilter extends GenericFilterBean {
    
    private static final Logger LOG = LoggerFactory.getLogger(SliRequestFilter.class);
    
    private static final String HEADER_SESSION_NAME = "sessionId";
    
    @Autowired
    private SecurityTokenResolver resolver;
    
    /**
     * Intercepter method called by spring Checks cookies to see if SLI session
     * id exists If session does exist, resolution will be attempted
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest http = (HttpServletRequest) request;
        String authToken = http.getHeader("Authorization");
        String sessionId = getSessionIdFromRequest(http);
        
        LOG.debug("Request URL: " + http.getRequestURL() + (http.getQueryString() == null ? "" : http.getQueryString()));
        
        Authentication auth = resolver.resolve(sessionId);
        if (auth != null) {
            LOG.debug("Created Auth Hash: {}@{}", auth.getClass(), Integer.toHexString(auth.hashCode()));
            
        }
        
        SecurityContextHolder.getContext().setAuthentication(auth);
        
        if (authToken != null || SecurityContextHolder.getContext().getAuthentication() == null || !(SecurityContextHolder.getContext().getAuthentication().getCredentials() instanceof PreAuthenticatedAuthenticationToken)) {
            chain.doFilter(request, response);
        }
    }
    
    private String getSessionIdFromRequest(HttpServletRequest req) {
        
        String sessionId = req.getHeader(HEADER_SESSION_NAME);
        LOG.debug("Session Id: " + sessionId);
        
        return sessionId;
    }
    
    public void setResolver(SecurityTokenResolver resolver) {
        this.resolver = resolver;
    }
}
