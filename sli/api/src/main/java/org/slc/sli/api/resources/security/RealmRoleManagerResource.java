package org.slc.sli.api.resources.security;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

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

/**
 * Realm role mapping API. Allows a user to define mappings between SLI roles
 * and client roles.
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
    private RoleRightAccess roleRightAccess;
    
    private EntityService service;

    @PostConstruct
    public void init() {
        EntityDefinition def = store.lookupByResourceName("realm");
        setService(def.getService());
    }
    
    //Injector
    public void setStore(EntityDefinitionStore store) {
        this.store = store;
    }

    //Injector
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
        Map<String, List<String>> mappings = (Map<String, List<String>>) updatedRealm.get("mappings");
        if (mappings != null) {
            if (!uniqueMappings(mappings)) {
                return Response.status(Status.FORBIDDEN).build();
             }
            
            for (String sliRole : mappings.keySet()) {
                if (roleRightAccess.getDefaultRole(sliRole) == null) {
                    return Response.status(Status.FORBIDDEN).build();
                }
            }
        }
        if (service.update(realmId, updatedRealm)) {
            return Response.status(Status.NO_CONTENT).build();
        }
        return Response.status(Status.FORBIDDEN).build();
    }

//    @DELETE
//    @RequestMapping("/realms/{realmId}")
//    public Response deleteClientRole(@PathVariable("realmId") String realmId,
//            String clientRoleName, String sliRoleName) {
//        try {
//            if (roleManager.deleteClientRole(realmId, clientRoleName)) {
//                return Response.ok().build();
//            }
//        } catch (RealmRoleMappingException e) {
//            return Response.status(Status.FORBIDDEN).entity(e.getMessage())
//                    .build();
//        }
//        return Response.status(Status.NOT_FOUND).build();
//    }

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
    public List<EntityBody> getRealms(@Context UriInfo info) {
        List<EntityBody> result = new ArrayList<EntityBody>();
        Iterable<String> realmList = service.list(0, 100);
        for (String id : realmList) {
            EntityBody curEntity = getMappings(id);
            curEntity.remove("mappings");
            curEntity.put("link", info.getBaseUri() + info.getPath() + "/" + id);
            result.add(curEntity);
        }
        return result;
    }
    
    private boolean uniqueMappings(Map<String, List<String>> mappings) {
        // A crappy, inefficient way to ensure uniqueness of mappings.
        for (String sliRole : mappings.keySet()) {
            List<String> clientRoles = mappings.get(sliRole);
            for (String clientRole : clientRoles) {
                for (String otherSliRole : mappings.keySet()) {
                    if (!otherSliRole.equals(sliRole)) {
                        List<String> secondClientRoles = mappings
                                .get(otherSliRole);
                        if (secondClientRoles.contains(clientRole)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

}
