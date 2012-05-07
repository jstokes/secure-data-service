package org.slc.sli.api.resources.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.EntityDefinition;
import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.Resource;
import org.slc.sli.api.security.roles.RoleRightAccess;
import org.slc.sli.api.service.EntityNotFoundException;
import org.slc.sli.api.service.EntityService;
import org.slc.sli.api.util.SecurityUtil;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.Repository;
import org.slc.sli.domain.enums.Right;

/**
 * Realm role mapping API. Allows full CRUD on realm objects. Primarily intended to allow
 * mappings between SLI roles and client roles as realms should not be created or deleted
 * frequently.
 * 
 * @author jnanney
 * 
 */
@Component
@Scope("request")
@Path("/realm")
@Produces({ Resource.JSON_MEDIA_TYPE })
public class RealmRoleManagerResource {
    
    @Autowired
    private EntityDefinitionStore store;
    
    @Autowired
    private RoleRightAccess       roleRightAccess;
    
    private EntityService         service;
    
    @Autowired
    private Repository<Entity> repo;
    
    @PostConstruct
    public void init() {
        EntityDefinition def = store.lookupByResourceName("realm");
        setService(def.getService());
    }
    
    // Injector
    public void setStore(EntityDefinitionStore store) {
        this.store = store;
    }
    
    // Injector
    public void setService(EntityService service) {
        this.service = service;
    }
    
    @SuppressWarnings("unchecked")
    @PUT
    @Path("{realmId}")
    @Consumes("application/json")
    public Response updateClientRole(@PathParam("realmId") String realmId, EntityBody updatedRealm) {
        if (updatedRealm == null) {
            throw new EntityNotFoundException("Entity was null");
        }
        if (!SecurityUtil.hasRight(Right.WRITE_GENERAL_REALM)) {
            EntityBody body = new EntityBody();
            body.put("response", "You are not authorized to update realms.");
            return Response.status(Status.FORBIDDEN).entity(body).build();
        }
        EntityBody oldRealm = service.get(realmId);
        
        if (!canEditCurrentRealm(updatedRealm) || (oldRealm.get("edOrg") != null && !oldRealm.get("edOrg").equals(SecurityUtil.getEdOrg()))) {
            EntityBody body = new EntityBody();
            body.put("response", "You are not authorized to update this realm.");
            return Response.status(Status.FORBIDDEN).entity(body).build();
        }
        
        Map<String, List<Map<String, Object>>> mappings = (Map<String, List<Map<String, Object>>>) updatedRealm.get("mappings");
        if (mappings != null) {
            Response validateResponse = validateMappings(mappings);
            Response validateUniqueId = validateUniqueId(realmId, (String) updatedRealm.get("uniqueIdentifier"));
            if (validateResponse != null) {
                return validateResponse;
            } else if (validateUniqueId != null) {
                return validateUniqueId;
            }
        }
        Map<String, List<Map<String, Object>>> oldMappings = (Map<String, List<Map<String, Object>>>) oldRealm.get("mappings");
        if (!oldMappings.equals(mappings) && !SecurityUtil.hasRight(Right.WRITE_ROLE_MAPPING)) {
            EntityBody body = new EntityBody();
            body.put("response", "You are not authorized to update role mappings.");
            return Response.status(Status.FORBIDDEN).entity(body).build();
        }
        if (service.update(realmId, updatedRealm)) {
            return Response.status(Status.NO_CONTENT).build();
        }
        return Response.status(Status.BAD_REQUEST).build();
    }
    
    @DELETE
    @Path("{realmId}")
    public Response deleteRealm(@PathParam("realmId") String realmId) {
        if (SecurityUtil.hasRight(Right.WRITE_GENERAL_REALM) && SecurityUtil.hasRight(Right.WRITE_ROLE_MAPPING)) {
            service.delete(realmId);
            return Response.status(Status.NO_CONTENT).build();
        } else {
            EntityBody body = new EntityBody();
            body.put("response", "You are not authorized to delete realms.");
            return Response.status(Status.FORBIDDEN).entity(body).build();
        }
    }
    
    @POST
    @SuppressWarnings("unchecked")
    public Response createRealm(EntityBody newRealm, @Context final UriInfo uriInfo) {
        if (!SecurityUtil.hasRight(Right.WRITE_GENERAL_REALM) && SecurityUtil.hasRight(Right.WRITE_ROLE_MAPPING)) {
            EntityBody body = new EntityBody();
            body.put("response", "You are not authorized to create realms.");
            return Response.status(Status.FORBIDDEN).entity(body).build();
        }
        
        if (!canEditCurrentRealm(newRealm)) {
            EntityBody body = new EntityBody();
            body.put("response", "You are not authorized to create a realm for another ed org");
            return Response.status(Status.FORBIDDEN).entity(body).build();
        }
        
        Map<String, List<Map<String, Object>>> mappings = (Map<String, List<Map<String, Object>>>) newRealm.get("mappings");
        if (mappings != null) {
            Response validateResponse = validateMappings(mappings);
            Response validateUniqueId = validateUniqueId(null, (String) newRealm.get("uniqueIdentifier"));
            if (validateResponse != null) {
                return validateResponse;
            } else if (validateUniqueId != null) {
                return validateUniqueId;
            }
        }
        
        String id = service.create(newRealm);
        String uri = uriToString(uriInfo) + "/" + id;

        return Response.status(Status.CREATED).header("Location", uri).build();
    }
    
    @GET
    @Path("{realmId}")
    public EntityBody getMappings(@PathParam("realmId") String realmId) {
        EntityBody result = service.get(realmId);
        if (result != null && result.get("mappings") == null) {
            result.put("mappings", new HashMap<String, List<String>>());
        }
        return result;
    }
    
    @GET
    public List<EntityBody> getRealms(@QueryParam("realm") @DefaultValue("") String realm, @Context UriInfo info) {
        NeutralQuery neutralQuery = new NeutralQuery();
        neutralQuery.setOffset(0);
        neutralQuery.setLimit(100);
        
        
        List<EntityBody> result = new ArrayList<EntityBody>();
        Iterable<String> realmList = service.listIds(neutralQuery);
        for (String id : realmList) {
            EntityBody curEntity = getMappings(id);
            if (realm.length() == 0) {
                curEntity.remove("mappings");
                curEntity.put("link", info.getBaseUri() + info.getPath().replaceAll("/$", "") + "/" + id);
                result.add(curEntity);
            } else {
                if (realm.equals(curEntity.get("realm"))) {
                    result.add(curEntity);
                }
            }
        }
        return result;
    }
        
    @SuppressWarnings("unchecked")
    private Response validateMappings(Map<String, List<Map<String, Object>>> mappings) {
        HashMap<String, String> res = new HashMap<String, String>();
        
        List<Map<String, Object>> roles = mappings.get("role");
        if (roles == null) {
            roles = new ArrayList<Map<String, Object>>();
        }
        Set<String> clientRoles = new HashSet<String>();
        for (Map<String, Object> role : roles) {
            String sliRoleName = (String) role.get("sliRoleName");
            List<String> clientRoleNameList = (List<String>) role.get("clientRoleName");
            if (roleRightAccess.getDefaultRole(sliRoleName) == null) {
                res.put("response", "Invalid SLI Role");
                return Response.status(Status.BAD_REQUEST).build();
            }
            
            for (String clientRole : clientRoleNameList) {
                if (clientRole.length() == 0) {
                    res.put("response", "Cannot have client role of length 0");
                    return Response.status(Status.BAD_REQUEST).entity(res).build();
                }
                
                if (clientRoles.contains(clientRole)) {
                    res.put("response", "Cannot have duplicate client roles");
                    return Response.status(Status.BAD_REQUEST).entity(res).build();
                }
                
                clientRoles.add(clientRole);
            }
        }
        return null;
    }
    
    private Response validateUniqueId(String realmId, String uniqueId) {
        if (uniqueId == null || uniqueId.length() == 0) {
            return null;
        }
        NeutralQuery query = new NeutralQuery();
        query.addCriteria(new NeutralCriteria("uniqueIdentifier", "=", uniqueId));
//        Iterable<Entity> entities = repo.findAll("realm", query);
        List<String> ids = Lists.newArrayList(service.listIds(query));
        ids.remove(realmId);
        if (ids != null && ids.iterator().hasNext()) {
            Map<String, String> res = new HashMap<String, String>();
            res.put("response", "Cannot have duplicate unique identifiers");
            return Response.status(Status.BAD_REQUEST).entity(res).build();
        }
        return null;
    }
    
    private static String uriToString(UriInfo uri) {
        return uri.getBaseUri() + uri.getPath().replaceAll("/$", "");
    }
    
    private boolean canEditCurrentRealm(EntityBody realm) {
        String edOrg = SecurityUtil.getEdOrg();
        return !(edOrg == null || !edOrg.equals(realm.get("edOrg")));

    }
    

}
