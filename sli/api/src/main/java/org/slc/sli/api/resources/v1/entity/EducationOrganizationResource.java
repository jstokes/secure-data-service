package org.slc.sli.api.resources.v1.entity;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.api.client.constants.ResourceNames;
import org.slc.sli.api.client.constants.v1.ParameterConstants;
import org.slc.sli.api.client.constants.v1.PathConstants;
import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.resources.v1.DefaultCrudResource;

/**
 * Represents any public or private institution, organization, or agency that provides instructional
 * or support services to students or staff at any level.
 *
 * For more information, see the schema for $$EducationOrganization$$ resources.
 *
 * @author kmyers
 */
@Path(PathConstants.V1 + "/" + PathConstants.EDUCATION_ORGANIZATIONS)
@Component
@Scope("request")
public class EducationOrganizationResource extends DefaultCrudResource {

    @Autowired
    public EducationOrganizationResource(EntityDefinitionStore entityDefs) {
        super(entityDefs, ResourceNames.EDUCATION_ORGANIZATIONS);
    }

    /**
     * Returns the requested collection of resources that are associated with the specified
     * resource.
     */
    @GET
    @Path("{" + ParameterConstants.EDUCATION_ORGANIZATION_ID + "}" + "/"
            + PathConstants.STAFF_EDUCATION_ORGANIZATION_ASSOCIATIONS)
    public Response getStaffEducationOrganizationAssociations(
            @PathParam(ParameterConstants.EDUCATION_ORGANIZATION_ID) final String educationOrganizationId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STAFF_EDUCATION_ORGANIZATION_ASSOCIATIONS, "educationOrganizationReference",
                educationOrganizationId, headers, uriInfo);
    }

    /**
     * Returns the requested collection of resources that are associated with the specified resource.
     */
    @GET
    @Path("{" + ParameterConstants.EDUCATION_ORGANIZATION_ID + "}" + "/"
            + PathConstants.STAFF_EDUCATION_ORGANIZATION_ASSOCIATIONS + "/" + PathConstants.STAFF)
    public Response getStaffEducationOrganizationAssociationStaff(
            @PathParam(ParameterConstants.EDUCATION_ORGANIZATION_ID) final String educationOrganizationId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STAFF_EDUCATION_ORGANIZATION_ASSOCIATIONS, "educationOrganizationReference",
                educationOrganizationId, "staffReference", ResourceNames.STAFF, headers, uriInfo);
    }

    @Override
    protected boolean shouldReadAll() {
        return true;
    }

}
