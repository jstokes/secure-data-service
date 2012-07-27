/*
 * Copyright 2012 Shared Learning Collaborative, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.slc.sli.api.resources.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slc.sli.api.config.EntityDefinition;
import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.Resource;
import org.slc.sli.api.security.SecurityEventBuilder;
import org.slc.sli.api.service.EntityService;
import org.slc.sli.api.util.SecurityUtil;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.Repository;
import org.slc.sli.domain.enums.Right;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * CRUD resource for custom roles.
 * 
 * @author jnanney
 */
@Component
@Scope("request")
@Path("/customRoles")
@Produces({ Resource.JSON_MEDIA_TYPE + ";charset=utf-8" })
public class CustomRoleResource {
    
    @Autowired
    private EntityDefinitionStore store;
    
    @Autowired
    private SecurityEventBuilder securityEventBuilder;
    
    private EntityService service;
    
    @Autowired
    @Qualifier("validationRepo")
    private Repository<Entity> repo;
    
    public static final String RESOURCE_NAME = "customRole";
    
    public static final String ERROR_DUPLICATE_ROLE = "Cannot list duplicate roles";
    public static final String ERROR_INVALID_REALM = "Cannot modify custom roles for that realm/tenant";
    public static final String ERROR_INVALID_RIGHT = "Invalid right listed in custom role document";
    public static final String ERROR_MULTIPLE_DOCS = "Cannot create multiple custom role documents per realm/tenant";
    public static final String ERROR_FORBIDDEN = "User does not have access to requested role document";
    public static final String ERROR_DUPLICATE_RIGHTS = "Cannot have the same right listed more than once in a role";
    
    @PostConstruct
    public void init() {
        EntityDefinition def = store.lookupByResourceName("customRole");
        service = def.getService();
    }
    
    @GET
    public Response readAll(@Context final UriInfo uriInfo) {
        if (!SecurityUtil.hasRight(Right.CRUD_ROLE)) {
            audit(securityEventBuilder.createSecurityEvent(CustomRoleResource.class.getName(), uriInfo,
                    "Failed to read custom roles --> insufficient permissions."));
            return SecurityUtil.forbiddenResponse();
        }
        
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        NeutralQuery customRoleQuery = new NeutralQuery();
        customRoleQuery.addCriteria(new NeutralCriteria("metaData.tenantId", NeutralCriteria.OPERATOR_EQUAL,
                SecurityUtil.getTenantId(), false));
        customRoleQuery.addCriteria(new NeutralCriteria("realmId", NeutralCriteria.OPERATOR_EQUAL, getRealmId()));
        
        Entity customRole = repo.findOne("customRole", customRoleQuery);
        if (customRole != null) {
            EntityBody result = service.get(customRole.getEntityId());
            results.add(result);
        }
        return Response.ok(results).build();
    }
    
    @GET
    @Path("{id}")
    public Response read(@PathParam("id") String id, @Context final UriInfo uriInfo) {
        if (!SecurityUtil.hasRight(Right.CRUD_ROLE)) {
            audit(securityEventBuilder.createSecurityEvent(CustomRoleResource.class.getName(), uriInfo,
                    "Failed to read custom role with id: " + id + "  --> insufficient permissions."));
            return SecurityUtil.forbiddenResponse();
        }
        EntityBody customRole = service.get(id);
        if (!customRole.get("realmId").equals(getRealmId())) {
            audit(securityEventBuilder.createSecurityEvent(CustomRoleResource.class.getName(), uriInfo,
                    "Failed to read custom role with id: " + id + "  --> wrong tenant + realm combination."));
            return Response.status(Status.FORBIDDEN).entity(ERROR_FORBIDDEN).build();
        }
        return Response.ok(customRole).build();
    }
    
    @POST
    public Response createCustomRole(EntityBody newCustomRole, @Context final UriInfo uriInfo) {
        SecurityUtil.ensureAuthenticated();
        if (!SecurityUtil.hasRight(Right.CRUD_ROLE)) {
            audit(securityEventBuilder.createSecurityEvent(CustomRoleResource.class.getName(), uriInfo,
                    "Failed to create custom role --> insufficient permissions."));
            return SecurityUtil.forbiddenResponse();
        }
        
        Response res = validateRights(newCustomRole);
        if (res != null) {
            audit(securityEventBuilder.createSecurityEvent(CustomRoleResource.class.getName(), uriInfo,
                    "Failed to create custom role --> rights validation failed."));
            return res;
        }
        res = validateUniqueRoles(newCustomRole);
        if (res != null) {
            audit(securityEventBuilder.createSecurityEvent(CustomRoleResource.class.getName(), uriInfo,
                    "Failed to create custom role --> unique roles check failed."));
            return res;
        }
        res = validateValidRealm(newCustomRole);
        if (res != null) {
            audit(securityEventBuilder.createSecurityEvent(CustomRoleResource.class.getName(), uriInfo,
                    "Failed to create custom role --> invalid realm specified."));
            return res;
        }
        
        String realmId = (String) newCustomRole.get("realmId");
        NeutralQuery existingCustomRoleQuery = new NeutralQuery();
        existingCustomRoleQuery.addCriteria(new NeutralCriteria("metaData.tenantId", NeutralCriteria.OPERATOR_EQUAL,
                SecurityUtil.getTenantId(), false));
        existingCustomRoleQuery.addCriteria(new NeutralCriteria("realmId", NeutralCriteria.OPERATOR_EQUAL, realmId));
        if (repo.findOne("customRole", existingCustomRoleQuery) != null) {
            audit(securityEventBuilder.createSecurityEvent(CustomRoleResource.class.getName(), uriInfo,
                    "Failed to create custom role --> Already exists."));
            return Response.status(Status.BAD_REQUEST).entity(ERROR_MULTIPLE_DOCS).build();
        }
        
        String id = service.create(newCustomRole);
        if (id != null) {
            String uri = uriToString(uriInfo) + "/" + id;
            audit(securityEventBuilder.createSecurityEvent(CustomRoleResource.class.getName(), uriInfo,
                    "Created custom role with id: " + id));
            return Response.status(Status.CREATED).header("Location", uri).build();
        }
        return Response.status(Status.BAD_REQUEST).build();
    }
    
    @PUT
    @Path("{id}")
    public Response updateCustomRole(@PathParam("id") String id, EntityBody updated, @Context final UriInfo uriInfo) {
        if (!SecurityUtil.hasRight(Right.CRUD_ROLE)) {
            audit(securityEventBuilder.createSecurityEvent(CustomRoleResource.class.getName(), uriInfo,
                    "Failed to update role with id: " + id + " --> insufficient permissions."));
            return SecurityUtil.forbiddenResponse();
        }
        
        Response res = validateRights(updated);
        if (res != null) {
            audit(securityEventBuilder.createSecurityEvent(CustomRoleResource.class.getName(), uriInfo,
                    "Failed to create custom role --> rights validation failed."));
            return res;
        }
        res = validateUniqueRoles(updated);
        if (res != null) {
            audit(securityEventBuilder.createSecurityEvent(CustomRoleResource.class.getName(), uriInfo,
                    "Failed to create custom role --> unique roles check failed."));
            return res;
        }
        res = validateValidRealm(updated);
        if (res != null) {
            audit(securityEventBuilder.createSecurityEvent(CustomRoleResource.class.getName(), uriInfo,
                    "Failed to create custom role --> invalid realm specified."));
            return res;
        }
        
        EntityBody oldRealm = service.get(id);
        String oldRealmId = (String) oldRealm.get("realmId");
        String updatedRealmId = (String) updated.get("realmId");
        if (!updatedRealmId.equals(oldRealmId)) {
            audit(securityEventBuilder.createSecurityEvent(CustomRoleResource.class.getName(), uriInfo,
                    "Failed to update realmId { from: " + oldRealmId + ", to: " + updatedRealmId
                            + " } for role with id:" + id));
            return Response.status(Status.BAD_REQUEST).entity("Cannot update the realmId on a custom role document")
                    .build();
        }
        
        if (service.update(id, updated)) {
            audit(securityEventBuilder.createSecurityEvent(CustomRoleResource.class.getName(), uriInfo,
                    "Updated role with id:" + id));
            return Response.status(Status.NO_CONTENT).build();
        }
        return Response.status(Status.BAD_REQUEST).build();
    }
    
    private static String uriToString(UriInfo uri) {
        return uri.getBaseUri() + uri.getPath().replaceAll("/$", "");
    }
    
    @DELETE
    @Path("{id}")
    public Response deleteCustomRole(@PathParam("id") String id, @Context final UriInfo uriInfo) {
        if (!SecurityUtil.hasRight(Right.CRUD_ROLE)) {
            audit(securityEventBuilder.createSecurityEvent(CustomRoleResource.class.getName(), uriInfo,
                    "Failed to delete role with id:" + id));
            return SecurityUtil.forbiddenResponse();
        }
        service.delete(id);
        audit(securityEventBuilder.createSecurityEvent(CustomRoleResource.class.getName(), uriInfo,
                "Deleted role with id:" + id));
        return Response.status(Status.NO_CONTENT).build();
    }
    
    private Response validateRights(EntityBody customRoleDoc) {
        @SuppressWarnings("unchecked")
        List<Map<String, List<String>>> roles = (List<Map<String, List<String>>>) customRoleDoc.get("roles");
        for (Map<String, List<String>> cur : roles) {
            List<String> rights = cur.get("rights");
            Set<Right> rightsSet = new HashSet<Right>();
            for (String rightName : rights) {
                Right right = null;
                try {
                    right = Right.valueOf(rightName);
                } catch (IllegalArgumentException iae) {
                    return Response.status(Status.BAD_REQUEST).entity(ERROR_INVALID_RIGHT).build();
                }
                
                if (rightsSet.contains(right)) {
                    return Response.status(Status.BAD_REQUEST).entity(ERROR_DUPLICATE_RIGHTS).build();
                } else {
                    rightsSet.add(right);
                }
            }
            
        }
        return null;
    }
    
    private Response validateUniqueRoles(EntityBody customRoleDoc) {
        Set<String> roleNames = new HashSet<String>();
        @SuppressWarnings("unchecked")
        List<Map<String, List<String>>> roles = (List<Map<String, List<String>>>) customRoleDoc.get("roles");
        for (Map<String, List<String>> cur : roles) {
            List<String> names = cur.get("names");
            for (String name : names) {
                if (roleNames.contains(name)) {
                    return Response.status(Status.BAD_REQUEST).entity(ERROR_DUPLICATE_ROLE).build();
                } else {
                    roleNames.add(name);
                }
            }
        }
        return null;
    }
    
    private Response validateValidRealm(EntityBody customRoleDoc) {
        String realmId = getRealmId();
        if (!realmId.equals(customRoleDoc.get("realmId"))) {
            return Response.status(Status.FORBIDDEN).entity(ERROR_INVALID_REALM).build();
        }
        return null;
    }
    
    private String getRealmId() {
        NeutralQuery realmQuery = new NeutralQuery();
        realmQuery.addCriteria(new NeutralCriteria("edOrg", NeutralCriteria.OPERATOR_EQUAL, SecurityUtil.getEdOrg()));
        Entity realm = repo.findOne("realm", realmQuery);
        if (realm != null) {
            return realm.getEntityId();
        }
        return null;
    }
}
