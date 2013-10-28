/*
 * Copyright 2012-2013 inBloom, Inc. and its affiliates.
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

import java.net.URI;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.google.common.collect.Sets;
import org.slc.sli.api.security.SLIPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.EntityDefinition;
import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.v1.HypermediaType;
import org.slc.sli.api.security.RightsAllowed;
import org.slc.sli.api.security.SecurityEventBuilder;
import org.slc.sli.api.security.context.resolver.EdOrgHelper;
import org.slc.sli.api.service.EntityService;
import org.slc.sli.api.util.SecurityUtil;
import org.slc.sli.common.util.logging.SecurityEvent;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.Repository;
import org.slc.sli.domain.enums.Right;

/**
 *
 * App auths are stored in mongo in the format
 *
 * {
 *  applicationId: id of application from application collection,
 *  edorgs: ids of all the edorgs (schools, LEAs, and SEAs) that have authorized the application.
 * }
 *
 * The endpoint supports three operations
 *
 * GET /applicationAuthorization
 * GET /applicationAuthorization/id
 * PUT /applicationAuthorization/id
 *
 * On a GET, it returns data of the format
 * {
 *  appId: id of the application
 *  authorized: true|false
 * }
 *
 * For LEA administrators the content is based on the user's LEA.
 * For SEA administrators the content is based on delegated SEAs.
 *
 * If an SEA administrator needs to distinguish between two edorgs, a
 * ?edorgs=... query parameter can be used on all operations.
 *
 * On a PUT, the endpoint automatically registers parent and child edorgs.
 */
@Component
@Scope("request")
@Path("/applicationAuthorization")
@Produces({ HypermediaType.JSON + ";charset=utf-8" })
public class ApplicationAuthorizationResource {

    @Autowired
    private EntityDefinitionStore store;

    @Autowired
    @Qualifier("validationRepo")
    Repository<Entity> repo;

    @Autowired
    private EdOrgHelper helper;

    @Autowired
    private DelegationUtil delegation;

    private EntityService service;

    @Autowired
    private SecurityEventBuilder securityEventBuilder;

    @Context
    UriInfo uri;

    public static final String RESOURCE_NAME = "applicationAuthorization";
    public static final String APP_ID = "applicationId";
    public static final String EDORG_IDS = "edorgs";

    @PostConstruct
    public void init() {
        EntityDefinition def = store.lookupByResourceName(RESOURCE_NAME);
        service = def.getService();
    }

    @GET
    @Path("{appId}")
    @RightsAllowed({Right.EDORG_APP_AUTHZ, Right.EDORG_DELEGATE })
    public Response getAuthorization(@PathParam("appId") String appId, @QueryParam("edorg") String edorg) {
        String myEdorg = validateEdOrg(edorg);
        EntityBody appAuth = getAppAuth(appId);
        if (appAuth == null) {
            //See if this is an actual app
            Entity appEntity = repo.findOne("application", new NeutralQuery(new NeutralCriteria("_id", "=", appId)));
            if (appEntity == null) {
                return Response.status(Status.NOT_FOUND).build();
            } else {
                HashMap<String, Object> entity = new HashMap<String, Object>();
                entity.put("id", appId);
                entity.put("appId", appId);
                entity.put("authorized", false);
                entity.put("edorgs", Collections.emptyList());//(TA10857)
                return Response.status(Status.OK).entity(entity).build();
            }
        } else {
            HashMap<String, Object> entity = new HashMap<String, Object>();
            entity.put("appId", appId);
            entity.put("id", appId);
            List<Map<String,Object>> edOrgs = (List<Map<String,Object>>) appAuth.get("edorgs");
            entity.put("authorized", containsEdOrg(edOrgs, myEdorg));
            entity.put("edorgs", edOrgs);//(TA10857)
            return Response.status(Status.OK).entity(entity).build();
        }

    }

    private boolean containsEdOrg(List<Map<String,Object>> edOrgList, String edOrg) {
        if( edOrgList == null || edOrg == null ) {
            return false;
        }
        for (Map<String,Object> edOrgListElement :edOrgList ){
            String authorizedEdorg = (String)edOrgListElement.get("authorizedEdorg");
            if(authorizedEdorg != null){
                if(edOrg.equals(authorizedEdorg)){
                    return true;
                }
            }
        }
        return false;
    }

    private EntityBody getAppAuth(String appId) {
        Iterable<EntityBody> appAuths = service.list(new NeutralQuery(new NeutralCriteria("applicationId", "=", appId)));
        for (EntityBody auth : appAuths) {
            return auth;
        }
        return null;
    }

    @PUT
    @Path("{appId}")
    @RightsAllowed({Right.EDORG_APP_AUTHZ, Right.EDORG_DELEGATE })
    public Response updateAuthorization(@PathParam("appId") String appId, EntityBody auth) {
        if (!auth.containsKey("authorized")) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        List<String> edOrgsToAuthorize = (List<String>) auth.get("edorgs");//(TA10857)
        if( edOrgsToAuthorize == null) {
            edOrgsToAuthorize = Collections.emptyList();
        }

        EntityBody existingAuth = getAppAuth(appId);
        if (existingAuth == null) {
            //See if this is an actual app
            Entity appEntity = repo.findOne("application", new NeutralQuery(new NeutralCriteria("_id", "=", appId)));
            if (appEntity == null) {
                return Response.status(Status.NOT_FOUND).build();
            } else {
                if (((Boolean) auth.get("authorized")).booleanValue()) { //being set to true. if false, there's no work to be done
                    //We don't have an appauth entry for this app, so create one
                    EntityBody body = new EntityBody();
                    body.put("applicationId", appId);
                    body.put("edorgs", enrichAuthorizedEdOrgsList(edOrgsToAuthorize));

                    service.create(body);
                    logSecurityEvent(appId, null, edOrgsToAuthorize);
                }
                return Response.status(Status.NO_CONTENT).build();
            }
        } else {
            List<Map<String,Object>> oldEdOrgs = (List<Map<String,Object>>)existingAuth.get("edorgs");
            Set<String>  oldAuth = getSetOfAuthorizedIds(oldEdOrgs);
            boolean add = ((Boolean) auth.get("authorized")).booleanValue();
            List<Map<String,Object>> modifiedAuthList = modifyEdOrgList(oldEdOrgs, add, edOrgsToAuthorize) ;
            Set<String>  newAuth = getSetOfAuthorizedIds(modifiedAuthList);
            existingAuth.put("edorgs", modifiedAuthList);
            logSecurityEvent(appId, oldAuth, newAuth);
            service.update((String) existingAuth.get("id"), existingAuth);
            return Response.status(Status.NO_CONTENT).build();
        }
    }

    private Set<String> getSetOfAuthorizedIds( List<Map<String,Object>> currentAuthList) {
        Set<String> authSet = new HashSet<String>();
        for(Map<String, Object> currentAuthListItem:currentAuthList) {
            String authorizedEdorg = (String)currentAuthListItem.get("authorizedEdorg");
            authSet.add(authorizedEdorg);
        }
        return  authSet;
    }

    @SuppressWarnings("PMD.AvoidReassigningParameters")
    private List<Map<String,Object>> modifyEdOrgList( List<Map<String,Object>> currentAuthList, boolean add, Collection<String> newEdOrgList ) {
        if(currentAuthList == null) {
            currentAuthList = new LinkedList<Map<String, Object>>();
        }
        Set<String> newAuthSet = new HashSet<String>(newEdOrgList);
        Set<String> oldAuthSet = getSetOfAuthorizedIds(currentAuthList);

        if(add) {
            newAuthSet.removeAll(oldAuthSet);
            for(String newAuthItem :newAuthSet) {
                Map<String, Object> newAuthItemProps = new HashMap<String, Object>();
                newAuthItemProps.put("authorizedEdorg", newAuthItem);
                enrichAuthorizedEdOrg(newAuthItemProps);
                currentAuthList.add(newAuthItemProps);
            }
        } else {
            ListIterator<Map<String, Object>> it = currentAuthList.listIterator();
            while(it.hasNext()) {
                Map<String, Object> currentAuthListItem = it.next();
                String authorizedEdorg = (String)currentAuthListItem.get("authorizedEdorg");
                if(newAuthSet.contains(authorizedEdorg)) {
                    it.remove();
                }
            }
        }
        return currentAuthList;
    }

    public static List<Map<String, Object>> enrichAuthorizedEdOrgsList(List<String> edOrgIds) {
        List<Map<String, Object>>  enrichedAEOList = new LinkedList<Map<String, Object>>();
        for(String edOrgId:edOrgIds) {
            Map<String, Object> enrichedAEO = new HashMap<String, Object>();
            enrichedAEO.put("authorizedEdorg", edOrgId);
            enrichedAEOList.add(enrichAuthorizedEdOrg(enrichedAEO));
        }
        return  enrichedAEOList;
    }

    private static Map<String, Object> enrichAuthorizedEdOrg(Map<String, Object> authInfo) {
        SLIPrincipal principal = (SLIPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String user = principal.getExternalId();
        String time = String.valueOf(new Date().getTime());

        String lastAuthorizingRealmEdorg     = principal.getRealmEdOrg();
        String lastAuthorizingUser           = user;
        String lastAuthorizedDate            = time;

        authInfo.put("lastAuthorizingRealmEdorg", lastAuthorizingRealmEdorg);
        authInfo.put("lastAuthorizingUser",       lastAuthorizingUser);
        authInfo.put("lastAuthorizedDate",        lastAuthorizedDate);

        return authInfo;
    }

    List<String> getParentEdorgs(String rootEdorg) {
        return helper.getParentEdOrgs(helper.byId(rootEdorg));
    }

    Set<String> getChildEdorgs(String rootEdorg) {
        return helper.getChildEdOrgs(Arrays.asList(rootEdorg));
    }

    @GET
    @RightsAllowed({Right.EDORG_APP_AUTHZ, Right.EDORG_DELEGATE })
    public Response getAuthorizations(@QueryParam("edorg") String edorg) {
        String myEdorg = validateEdOrg(edorg);
        Iterable<Entity> appQuery = repo.findAll("application", new NeutralQuery());
        Map<String, Entity> allApps = new HashMap<String, Entity>();
        for (Entity ent : appQuery) {
            allApps.put(ent.getEntityId(), ent);
        }

        Iterable<EntityBody> ents = service.list(new NeutralQuery(new NeutralCriteria("edorgs.authorizedEdorg", "=", myEdorg)));

        List<Map> results = new ArrayList<Map>();
        for (EntityBody body : ents) {
            HashMap<String, Object> entity = new HashMap<String, Object>();
            String appId = (String) body.get("applicationId");
            entity.put("id", appId);
            entity.put("appId", appId);
            entity.put("authorized", true);
            results.add(entity);
            allApps.remove(appId);
        }
        for (Map.Entry<String, Entity> entry : allApps.entrySet()) {
            Boolean    autoApprove = (Boolean) entry.getValue().getBody().get("allowed_for_all_edorgs");
            List<String> approvedEdorgs = (List<String>) entry.getValue().getBody().get("authorized_ed_orgs");
            if ((autoApprove != null && autoApprove) || (approvedEdorgs != null && approvedEdorgs.contains(myEdorg))) {
                HashMap<String, Object> entity = new HashMap<String, Object>();
                entity.put("id", entry.getKey());
                entity.put("appId", entry.getKey());
                entity.put("authorized", false);
                results.add(entity);
            }
        }
        return Response.status(Status.OK).entity(results).build();
    }

    private void logSecurityEvent(String appId, Collection<String> oldEdOrgs, Collection<String> newEdOrgs) {
        Set<String> oldEO = (oldEdOrgs == null)?Collections.<String>emptySet():new HashSet<String>(oldEdOrgs);
        Set<String> newEO = (newEdOrgs == null)?Collections.<String>emptySet():new HashSet<String>(newEdOrgs);

        info("EdOrgs that App could access earlier " + helper.getEdOrgStateOrganizationIds(oldEO));
        info("EdOrgs that App can access now "       + helper.getEdOrgStateOrganizationIds(newEO));

        URI path = (uri != null)?uri.getRequestUri():null;
        String resourceClassName = ApplicationAuthorizationResource.class.getName();
        Set<String> granted = Sets.difference(newEO, oldEO);
        if(granted.size() > 0) {
            SecurityEvent event = securityEventBuilder.createSecurityEvent(resourceClassName,
                    path, "Application granted access to EdOrg data!", true);
            event.setAppId(appId);
            Set<String> targetEdOrgList = helper.getEdOrgStateOrganizationIds(granted);
            event.setTargetEdOrgList(new ArrayList<String>(targetEdOrgList));
            event.setTargetEdOrg("");
            audit(event);
        }

        Set<String> revoked = Sets.difference(oldEO, newEO);
        if(revoked.size() > 0) {
            SecurityEvent event = securityEventBuilder.createSecurityEvent(resourceClassName,
                    path, "EdOrg data access has been revoked!", true);
            event.setAppId(appId);
            Set<String> targetEdOrgList = helper.getEdOrgStateOrganizationIds(revoked);
            event.setTargetEdOrgList(new ArrayList<String>(targetEdOrgList));
            event.setTargetEdOrg("");
            audit(event);
        }

    }

    private String validateEdOrg(String edorg) {
        if (edorg == null) {
            return SecurityUtil.getEdOrgId();
        }
        // US5894 removed the need for LEA to delegate app approval to SEA
        /*
        if (!edorg.equals(SecurityUtil.getEdOrgId()) && !delegation.getAppApprovalDelegateEdOrgs().contains(edorg) ) {
            Set<String> edOrgIds = new HashSet<String>();
            edOrgIds.add(edorg);
            throw new APIAccessDeniedException("Cannot perform authorizations for edorg ", edOrgIds);
        }
        */
        return edorg;
    }

}
