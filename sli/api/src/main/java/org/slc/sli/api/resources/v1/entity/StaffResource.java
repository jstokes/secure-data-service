package org.slc.sli.api.resources.v1.entity;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slc.sli.api.resources.v1.DefaultCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.client.constants.ResourceNames;
import org.slc.sli.client.constants.v1.ParameterConstants;
import org.slc.sli.client.constants.v1.PathConstants;

/**
 * Represents the definition of a staff resource. A staff is a person that is employed by
 * an LEA or other educational unit engaged in student instruction.
 * These persons are instructional-type staff members.
 * A teacher entity is a staff member with additional properties. See the $$teachers$$ resource for details.
 * A staff is associated with a school, cohort and program through staff education organization association,
 * $$staffCohortAssociations$$, and $$staffProgramAssociations$$.
 * For more details about the resources, see $$schools$$, $$cohorts$$ and $$programs$$ resources.
 *
 * @author jstokes
 *
 */
@Path(PathConstants.V1 + "/" + PathConstants.STAFF)
@Component
@Scope("request")
public class StaffResource extends DefaultCrudResource {

    public static final String UNIQUE_STATE_ID = "staffUniqueStateId";
    public static final String NAME = "name";
    public static final String SEX = "sex";
    public static final String HISPANIC_LATINO_ETHNICITY = "hispanicLatinoEthnicity";
    public static final String EDUCATION_LEVEL = "highestLevelOfEducationCompleted";

    @Autowired
    public StaffResource(EntityDefinitionStore entityDefs) {
        super(entityDefs, ResourceNames.STAFF);
    }

    /**
     * Returns the requested collection of resources that are associated with the specified resource.
     *
     * @param staffId
     *            The Id of the School.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Returns each $$staffEducationOrganizationAssociations$$ that references the given $$staff$$
     */
    @GET
    @Path("{" + ParameterConstants.STAFF_ID + "}" + "/" + PathConstants.STAFF_EDUCATION_ORGANIZATION_ASSOCIATIONS)
    public Response getStaffEducationOrganizationAssociations(@PathParam(ParameterConstants.STAFF_ID) final String staffId,
            @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STAFF_EDUCATION_ORGANIZATION_ASSOCIATIONS, "staffReference", staffId, headers, uriInfo);
    }


    /**
     * Returns the requested collection of resources that are associated with the specified resource.
     *
     * @param staffId
     *            The Id of the School.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Returns each $$staff$$ associated to the given school
     * through a $$staffEducationOrganizationAssociations$$
     */
    @GET
    @Path("{" + ParameterConstants.STAFF_ID + "}" + "/" + PathConstants.STAFF_EDUCATION_ORGANIZATION_ASSOCIATIONS + "/" + PathConstants.EDUCATION_ORGANIZATIONS)
    public Response getStaffEducationOrganizationAssociationEducationOrganizations(@PathParam(ParameterConstants.STAFF_ID) final String staffId,
            @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STAFF_EDUCATION_ORGANIZATION_ASSOCIATIONS, "staffReference", staffId,
                "educationOrganizationReference", ResourceNames.EDUCATION_ORGANIZATIONS, headers, uriInfo);
    }

    /**
     * Returns the requested collection of resources that are associated with the specified resource.
     *
     * @param staffId
     *            The Id of the Staff.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Returns each $$staffCohortAssociations$$ that references the given $$staff$$
     */
    @GET
    @Path("{" + ParameterConstants.STAFF_ID + "}" + "/" + PathConstants.STAFF_COHORT_ASSOCIATIONS)
    public Response getStaffCohortAssociations(@PathParam(ParameterConstants.STAFF_ID) final String staffId,
            @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STAFF_COHORT_ASSOCIATIONS, ParameterConstants.STAFF_ID, staffId, headers, uriInfo);
    }


    /**
     * Returns the requested collection of resources that are associated with the specified resource.
     *
     * @param staffId
     *            The Id of the Staff.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Returns each $$cohorts$$ associated to the given staff through a $$staffCohortAssociations$$
     */
    @GET
    @Path("{" + ParameterConstants.STAFF_ID + "}" + "/" + PathConstants.STAFF_COHORT_ASSOCIATIONS + "/" + PathConstants.COHORTS)
    public Response getStaffCohortAssociationCohorts(@PathParam(ParameterConstants.STAFF_ID) final String staffId,
            @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STAFF_COHORT_ASSOCIATIONS, ParameterConstants.STAFF_ID, staffId,
                ParameterConstants.COHORT_ID, ResourceNames.COHORTS, headers, uriInfo);
    }
    /**
     * Returns the requested collection of resources that are associated with the specified resource.
     *
     * @param staffId
     *            The Id of the $$staff$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Returns each $$staffProgramAssociations$$ that references the given $$staff$$
     */
    @GET
    @Path("{" + ParameterConstants.STAFF_ID + "}" + "/" + PathConstants.STAFF_PROGRAM_ASSOCIATIONS)
    public Response getStaffProgramAssociations(@PathParam(ParameterConstants.STAFF_ID) final String staffId,
            @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STAFF_PROGRAM_ASSOCIATIONS, "staffId", staffId, headers, uriInfo);
    }


    /**
     * Returns the requested collection of resources that are associated with the specified resource.
     *
     * @param staffId
     *            The Id of the $$staff$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Returns the $$programs$$ that are referenced from the $$staffProgramAssociations$$
     * that references the given $$staff$$.
     */
    @GET
    @Path("{" + ParameterConstants.STAFF_ID + "}" + "/" + PathConstants.STAFF_PROGRAM_ASSOCIATIONS + "/" + PathConstants.PROGRAMS)
    public Response getStaffProgramAssociationPrograms(@PathParam(ParameterConstants.STAFF_ID) final String staffId,
            @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STAFF_PROGRAM_ASSOCIATIONS, "staffId", staffId,
                "programId", ResourceNames.PROGRAMS, headers, uriInfo);
    }

}
