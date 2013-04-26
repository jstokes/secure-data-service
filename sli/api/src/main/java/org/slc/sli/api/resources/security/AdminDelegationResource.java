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

import org.slc.sli.api.config.EntityDefinition;
import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.v1.HypermediaType;
import org.slc.sli.api.security.RightsAllowed;
import org.slc.sli.api.service.EntityNotFoundException;
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

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Endpoints to access admin delegation data.
 */
@Component
@Scope("request")
@Path("/adminDelegation")
@Produces({ HypermediaType.JSON + ";charset=utf-8" })
public class AdminDelegationResource {

    @Autowired
    private EntityDefinitionStore store;

    @Autowired
    @Qualifier("validationRepo")
    private Repository<Entity> repo;

    @Autowired
    private DelegationUtil util;

    private EntityService service;

    public static final String RESOURCE_NAME = "adminDelegation";
    public static final String LEA_ID = "localEdOrgId";

    @PostConstruct
    public void init() {
        EntityDefinition def = store.lookupByResourceName(RESOURCE_NAME);
        this.service = def.getService();
    }

    /**
     * Get admin delegation records for principals Education Organization.
     * If SEA admin method returns a list of delegation records for child LEAs.
     * If LEA admin method returns a list containing only the delegation record for the LEA.
     *
     * @return A list of admin delegation records.
     */
    @GET
    @RightsAllowed({Right.EDORG_DELEGATE, Right.EDORG_APP_AUTHZ })
    public Response getDelegations() {
        SecurityUtil.ensureAuthenticated();
        if (SecurityUtil.hasRight(Right.EDORG_DELEGATE)) {

            String edOrg = SecurityUtil.getEdOrg();
            if (edOrg == null) {
                throw new EntityNotFoundException("No edorg exists on principal.");
            }

            List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
            NeutralQuery query = new NeutralQuery();
            Set<String> delegatedEdorgs = new HashSet<String>();
            delegatedEdorgs.addAll(util.getAppApprovalDelegateEdOrgs());
            delegatedEdorgs.addAll(util.getSecurityEventDelegateEdOrgs());
            query.addCriteria(new NeutralCriteria(LEA_ID, NeutralCriteria.CRITERIA_IN, delegatedEdorgs));
            for (Entity entity : repo.findAll(RESOURCE_NAME, query)) {
                entity.getBody().put("id", entity.getEntityId());
                results.add(entity.getBody());
            }

            return Response.ok(results).build();

        } else if (SecurityUtil.hasRight(Right.EDORG_APP_AUTHZ)) {
            EntityBody entity = getEntity();
            if (entity == null) {
                return Response.status(Status.NOT_FOUND).build();
            }
            return Response.status(Status.OK).entity(Arrays.asList(entity)).build();

        }

        return SecurityUtil.forbiddenResponse();

    }


    /**
     * Set the admin delegation record for an LEA admin user's EdOrg.
     *
     * @param body Admin delegation record to be written.
     * @return NO_CONTENT on success. BAD_REQUEST or FORBIDDEN on failure.
     */
    @PUT
    @Path("myEdOrg")
    @RightsAllowed({Right.EDORG_APP_AUTHZ })
    public Response setLocalDelegation(EntityBody body) {
        //verifyBodyEdOrgMatchesPrincipalEdOrg
        if (body == null || !body.containsKey(LEA_ID) || !body.get(LEA_ID).equals(SecurityUtil.getEdOrgId())) {
            EntityBody response = new EntityBody();
            response.put("message", "Entity EdOrg must match principal's EdOrg when writing delegation record.");
            return Response.status(Status.BAD_REQUEST).entity(response).build();
        }

        String delgId = getIdOfDelegationRecordForPrincipal();
        if (delgId == null) {

            if (service.create(body).isEmpty()) {
                return Response.status(Status.BAD_REQUEST).build();
            } else {
                return Response.status(Status.CREATED).build();
            }

        } else {

            if (!service.update(delgId, body)) {
                return Response.status(Status.BAD_REQUEST).build();
            }

        }

        return Response.status(Status.NO_CONTENT).build();
    }

    @POST
    @RightsAllowed({Right.EDORG_APP_AUTHZ })
    public Response create(EntityBody body) {
        return setLocalDelegation(body);
    }

    @GET
    @Path("myEdOrg")
    @RightsAllowed({Right.EDORG_DELEGATE, Right.EDORG_APP_AUTHZ })
    public Response getSingleDelegation() {
        EntityBody entity = getEntity();
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.status(Status.OK).entity(entity).build();
    }


    private String getIdOfDelegationRecordForPrincipal() {
        String edOrgId = SecurityUtil.getEdOrgId();
        if (edOrgId == null) {
            throw new EntityNotFoundException("No edorg exists on principal.");
        }

        NeutralQuery query = new NeutralQuery();
        query.addCriteria(new NeutralCriteria(LEA_ID, "=", edOrgId));
        Iterator<String> it = service.listIds(query).iterator();
        if (it.hasNext()) {
            return it.next();
        } else {
            return null;
        }
    }


    private EntityBody getEntity() {
        if (SecurityUtil.hasRight(Right.EDORG_APP_AUTHZ)) {

            String entId = getIdOfDelegationRecordForPrincipal();
            if (entId != null) {
                return service.get(entId);
            }

        }
        return null;
    }
}
