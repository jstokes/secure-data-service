package org.slc.sli.api.resources;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.mockito.Mockito;
import org.slc.sli.api.security.roles.SecureRoleRightAccessImpl;
import org.slc.sli.api.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import org.slc.sli.api.security.SLIPrincipal;
import org.slc.sli.api.security.resolve.RolesToRightsResolver;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.enums.Right;

/**
 * Simple class for injecting a security context for unit tests. Future implementations will allow
 * for greater
 * flexibility in selecting which roles become available to the user (including multiple roles).
 * 
 * @author shalka
 */

@Component
public class SecurityContextInjector {
    private static final Logger   LOG              = LoggerFactory.getLogger(SecurityContextInjector.class);
    private static final String   DEFAULT_REALM_ID = "dc=slidev,dc=net";
    
    @Autowired
    private RolesToRightsResolver resolver;
    
    public void setAdminContext() {
        String user = "administrator";
        String fullName = "IT Administrator";
        List<String> roles = Arrays.asList(SecureRoleRightAccessImpl.IT_ADMINISTRATOR);
        
        Entity entity = Mockito.mock(Entity.class);
        Mockito.when(entity.getType()).thenReturn("admin-staff");
        SLIPrincipal principal = buildPrincipal(user, fullName, DEFAULT_REALM_ID, roles, entity);
        setSecurityContext(principal);
    }
    
    public void setAdminContextWithElevatedRights() {
        setAdminContext();
        
        PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(SecurityContextHolder.getContext().getAuthentication().getPrincipal(), SecurityContextHolder.getContext().getAuthentication().getCredentials(),
                Arrays.asList(Right.FULL_ACCESS));
        
        LOG.debug("elevating rights to {}", Right.FULL_ACCESS);
        SecurityContextHolder.getContext().setAuthentication(token);
        
    }
    
    public void setResolver(RolesToRightsResolver resolver) {
        this.resolver = resolver;
    }
    
    private PreAuthenticatedAuthenticationToken getAuthenticationToken(String token, final SLIPrincipal principal) {
        final RolesToRightsResolver finalResolver = this.resolver;
        Set<GrantedAuthority> authorities = SecurityUtil.sudoRun(new SecurityUtil.SecurityTask<Set<GrantedAuthority>>() {
            @Override
            public Set<GrantedAuthority> execute() {
                return finalResolver.resolveRoles(principal.getRealm(), principal.getRoles());
            }
        });
                
        return new PreAuthenticatedAuthenticationToken(principal, token, authorities);
    }
    
    public void setEducatorContext() {
        String user = "educator";
        String fullName = "Educator";
        List<String> roles = Arrays.asList(SecureRoleRightAccessImpl.EDUCATOR);
        
        Entity entity = Mockito.mock(Entity.class);
        SLIPrincipal principal = buildPrincipal(user, fullName, DEFAULT_REALM_ID, roles, entity);
        setSecurityContext(principal);
    }
    
    private SLIPrincipal setSecurityContext(SLIPrincipal principal) {
        String token = "AQIC5wM2LY4SfczsoqTgHpfSEciO4J34Hc5ThvD0QaM2QUI.*AAJTSQACMDE.*";
        
        LOG.debug("assembling authentication token");
        PreAuthenticatedAuthenticationToken authenticationToken = getAuthenticationToken(token, principal);
        
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        
        return principal;
    }
    
    private SLIPrincipal buildPrincipal(String user, String fullName, String realmId, List<String> roles, Entity principalEntity) {
        SLIPrincipal principal = new SLIPrincipal(user);
        principal.setId(user);
        principal.setName(fullName);
        principal.setRoles(roles);
        principal.setEntity(principalEntity);
        principal.setRealm(realmId);
        return principal;
    }
    
}
