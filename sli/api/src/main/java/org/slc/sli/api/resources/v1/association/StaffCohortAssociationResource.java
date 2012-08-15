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


package org.slc.sli.api.resources.v1.association;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.constants.ParameterConstants;
import org.slc.sli.api.constants.PathConstants;
import org.slc.sli.api.constants.ResourceNames;
import org.slc.sli.api.resources.v1.DefaultCrudResource;

/**
 * Represents the association between a $$Staff$$ member and a $$Cohort$$.
 *
 * For more information, see the schema for $$StaffCohortAssociation$$ resources.
 *
 * @author kmyers
 * @author srichards
 *
 */
@Path(PathConstants.V1 + "/" + PathConstants.STAFF_COHORT_ASSOCIATIONS)
@Component
@Scope("request")
public class StaffCohortAssociationResource extends DefaultCrudResource {

    public static final String BEGIN_DATE = "beginDate";

    /**
     * Logging utility.
     */
//    private static final Logger LOGGER = LoggerFactory.getLogger(StaffCohortAssociation.class);

    @Autowired
    public StaffCohortAssociationResource(EntityDefinitionStore entityDefs) {
        super(entityDefs, ResourceNames.STAFF_COHORT_ASSOCIATIONS);
//        DE260 - Logging of possibly sensitive data
//        LOGGER.debug("New resource handler created: {}", this);
    }

    /**
     * Returns the requested collection of resources that are associated with the specified resource.
     */
    @GET
    @Path("{" + ParameterConstants.STAFF_COHORT_ASSOCIATION_ID + "}" + "/" + PathConstants.STAFF)
    public Response getStaffCohortAssocationStaff(@PathParam(ParameterConstants.STAFF_COHORT_ASSOCIATION_ID) final String staffCohortAssociationId,
            @QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
            @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
       return super.read(ResourceNames.STAFF_COHORT_ASSOCIATIONS, "_id", staffCohortAssociationId,
               ParameterConstants.STAFF_ID, ResourceNames.STAFF, headers, uriInfo);
    }

    /**
     * Returns the requested collection of resources that are associated with the specified resource.
     */
    @GET
    @Path("{" + ParameterConstants.STAFF_COHORT_ASSOCIATION_ID + "}" + "/" + PathConstants.COHORTS)
    public Response getStaffCohortAssocationCohorts(@PathParam(ParameterConstants.STAFF_COHORT_ASSOCIATION_ID) final String staffCohortAssociationId,
            @QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
            @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STAFF_COHORT_ASSOCIATIONS, "_id", staffCohortAssociationId,
                ParameterConstants.COHORT_ID, ResourceNames.COHORTS, headers, uriInfo);
    }
}
