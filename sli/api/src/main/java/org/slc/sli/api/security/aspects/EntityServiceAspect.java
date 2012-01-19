package org.slc.sli.api.security.aspects;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.avro.Schema;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.slc.sli.api.security.SLIPrincipal;
import org.slc.sli.api.security.context.AssociativeContextResolver;
import org.slc.sli.api.security.context.ContextResolverStore;
import org.slc.sli.api.security.context.EntityContextResolver;
import org.slc.sli.api.security.enums.Right;
import org.slc.sli.domain.Entity;
import org.slc.sli.validation.EntitySchemaRegistry;

// add this import if we move to CoreEntityService paradigm
// import org.slc.sli.api.service.CoreEntityService;

/**
 * Aspect for handling Entity Service operations.
 * 
 * @author shalka
 */

@Aspect
public class EntityServiceAspect {
    
    private static final Logger  LOG = LoggerFactory.getLogger(EntityServiceAspect.class);
    
    @Autowired
    private EntitySchemaRegistry schemaRegistry;
    
    @Autowired
    private ContextResolverStore contextResolverStore;
    
    /**
     * Controls access to functions in the EntityService class.
     * 
     * @param pjp Method invoked if principal has required rights.
     * @return Entity returned from invoked method (if method is entered).
     * @throws Throwable AccessDeniedException (HTTP 403).
     */
    @Around("call(* org.slc.sli.api.service.CoreEntityService.create(..)) || " + "call(* org.slc.sli.api.service.CoreEntityService.update(..)) || " + "call(* org.slc.sli.api.service.CoreEntityService.delete(..))")
    public Object authorizeWrite(ProceedingJoinPoint pjp) throws Throwable {
        if (hasElevatedAccess()) {
            return pjp.proceed();
        }
        
        if (!isPublicContext() && !getGrantedRights().contains(Right.WRITE_GENERAL)) {
            throwAccessDeniedException();
        }
        
        return pjp.proceed();
    }
    
    /**
     * Controls access to functions in the EntityService class.
     * 
     * @param pjp Method invoked if principal has required rights.
     * @return Entity returned from invoked method (if method is entered).
     * @throws Throwable AccessDeniedException (HTTP 403).
     */
    @Around("call(* org.slc.sli.api.service.CoreEntityService.get(..))")
    public Entity filterEntityRead(ProceedingJoinPoint pjp) throws Throwable {
        if (hasElevatedAccess()) {
            return (Entity) pjp.proceed();
        }
        
        if (!isPublicContext() && !getGrantedRights().contains(Right.READ_GENERAL)) {
            throwAccessDeniedException();
        }
        
        Entity entity = (Entity) pjp.proceed();
        
        if (entity != null && !isPublicContext()) {
            
            // determine if principal entity is valid
            SLIPrincipal principal = (SLIPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal == null || principal.getEntity() == null) {
                LOG.error("Security Context does not contain a principal entity");
                throwAccessDeniedException();
            }
            
            EntityContextResolver resolver = contextResolverStore.getContextResolver(principal.getEntity(), entity);
            
            if (resolver instanceof AssociativeContextResolver) { // FIXME temporary hack so that only educator is context-enforced
                List<String> accessibleIds = resolver.findAccessible(principal.getEntity());
                
                if (!accessibleIds.contains(entity.getEntityId())) {
                    throwAccessDeniedException();
                }
            }
            // filter
            Collection<GrantedAuthority> grantedRights = getGrantedRights();
            LOG.debug("Rights {}", grantedRights);
            
            if (!grantedRights.contains(Right.READ_RESTRICTED)) {
                LOG.debug("Filtering restricted on {}", entity.getEntityId());
                removeReadRestrictedAttributes(entity);
            }
            if (!grantedRights.contains(Right.READ_GENERAL)) {
                LOG.debug("Filtering general on {}", entity.getEntityId());
                removeReadGeneralAttributes(entity);
            }
        }
        
        return entity;
    }
    
    private boolean hasElevatedAccess() {
        return getGrantedRights().contains(Right.FULL_ACCESS);
    }
    
    /**
     * Controls access to functions in the EntityService class.
     * 
     * @param pjp Method invoked if principal has required rights.
     * @return Entity returned from invoked method (if method is entered).
     * @throws Throwable AccessDeniedException (HTTP 403).
     */
    @Around("call(* org.slc.sli.api.service.EntityService.list(..)) || " + "call(* org.slc.sli.api.service.EntityService.exists(..))")
    public Object authorizeExists(ProceedingJoinPoint pjp) throws Throwable {
        if (hasElevatedAccess()) {
            return pjp.proceed();
        }
        
        if (!isPublicContext() && !getGrantedRights().contains(Right.READ_GENERAL)) {
            throwAccessDeniedException();
        }
        return pjp.proceed();
    }
    
    private void throwAccessDeniedException() throws Throwable {
        LOG.debug("user was denied access due to insufficient permissions.");
        throw new AccessDeniedException("User does not have authority to access entity.");
    }
    
    private boolean isPublicContext() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth instanceof AnonymousAuthenticationToken;
    }
    
    private void removeReadGeneralAttributes(Entity entity) {
        if (entity.getBody() == null)
            return;
        
        Schema schema = schemaRegistry.findSchemaForType(entity);
        LOG.debug("schema fields {}", schema.getFields());
        Iterator<String> keyIter = entity.getBody().keySet().iterator();
        
        while (keyIter.hasNext()) {
            String fieldName = keyIter.next();
            
            Schema.Field field = schema.getField(fieldName);
            LOG.debug("Field {} is general {}", fieldName, isReadGeneral(field));
            if (isReadGeneral(field)) {
                keyIter.remove();
            }
        }
    }
    
    private boolean isReadGeneral(Schema.Field field) {
        if (field == null) {
            return false;
        }
        
        String readProp = field.getProp("read_enforcement");
        return (readProp != null && !readProp.matches("restricted") && !readProp.matches("aggregate"));
    }
    
    private void removeReadRestrictedAttributes(Entity entity) {
        if (entity.getBody() == null)
            return;
        
        Schema schema = schemaRegistry.findSchemaForType(entity);
        Iterator<String> keyIter = entity.getBody().keySet().iterator();
        
        while (keyIter.hasNext()) {
            String fieldName = keyIter.next();
            
            Schema.Field field = schema.getField(fieldName);
            LOG.debug("Field {} is restricted {}", fieldName, isRestrictedField(field));
            if (isRestrictedField(field)) {
                keyIter.remove();
            }
        }
    }
    
    /**
     * Returns true if the Field is marked "restricted" under "read_enforcement".
     * 
     * @param field Field to be checked for a 'restricted' read enforcement flag.
     * @return Boolean indicating whether or not the Field requires READ_RESTRICTED right to be
     *         read.
     */
    private boolean isRestrictedField(Schema.Field field) {
        if (field == null) {
            return false;
        }
        
        String readProp = field.getProp("read_enforcement");
        return (readProp != null && readProp.equals("restricted"));
    }
    
    private Collection<GrantedAuthority> getGrantedRights() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }
    
    public void setSchemaRegistry(EntitySchemaRegistry schemaRegistry) {
        this.schemaRegistry = schemaRegistry;
    }
    
    public void setContextResolverStore(ContextResolverStore contextResolverStore) {
        this.contextResolverStore = contextResolverStore;
    }
}