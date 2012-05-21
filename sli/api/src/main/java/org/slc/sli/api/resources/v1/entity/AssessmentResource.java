package org.slc.sli.api.resources.v1.entity;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.resources.v1.DefaultCrudResource;
import org.slc.sli.common.constants.ResourceNames;
import org.slc.sli.common.constants.v1.ParameterConstants;
import org.slc.sli.common.constants.v1.PathConstants;

/**
 * Represents a tool, instrument, process, or exhibit that is used
 * to assess student performance.
 *
 * @author jstokes
 *
 */
@Path(PathConstants.V1 + "/" + PathConstants.ASSESSMENTS)
@Component
@Scope("request")
public class AssessmentResource extends DefaultCrudResource {

    @Autowired
    public AssessmentResource(EntityDefinitionStore entityDefs) {
        super(entityDefs, ResourceNames.ASSESSMENTS);
    }


    /**
     * Returns all the $$studentAssessmentAssociations$$ for the given $$assessments$$
     *
     * @param assessmentId
     *            Comma separated list of ids of the $$assessments$$ entities
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}" + "/" + PathConstants.STUDENT_ASSESSMENT_ASSOCIATIONS)
    public Response getStudentAssessmentAssociations(
            @PathParam(ParameterConstants.ASSESSMENT_ID) final String assessmentId, @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super
                .read(ResourceNames.STUDENT_ASSESSMENT_ASSOCIATIONS, "assessmentId", assessmentId, headers, uriInfo);
    }

    /**
     * Returns each $$students$$ entities associated to the given assessment through
     * a $$studentAssessmentAssociations$$
     *
     * @param assessmentId
     *            Comma separated list of ids of the $$assessments$$
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}" + "/" + PathConstants.STUDENT_ASSESSMENT_ASSOCIATIONS + "/"
            + PathConstants.STUDENTS)
    public Response getStudentAssessmentAssociationsStudents(
            @PathParam(ParameterConstants.ASSESSMENT_ID) final String assessmentId, @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_ASSESSMENT_ASSOCIATIONS, "assessmentId", assessmentId, "studentId",
                ResourceNames.STUDENTS, headers, uriInfo);
    }

    /**
     * Returns each $$sectionAssessmentAssociations$$ entities that
     * references the given $$assessments$$
     *
     * @param assessmentId
     *            Comma separated list of ids of the $$assessments$$
     * @param offset
     *            Index of the first result to return
     * @param limit
     *            Maximum number of results to return.
     * @param expandDepth
     *            Number of hops (associations) for which to expand entities.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}" + "/" + PathConstants.SECTION_ASSESSMENT_ASSOCIATIONS)
    public Response getSectionAssessmentAssociations(
            @PathParam(ParameterConstants.ASSESSMENT_ID) final String assessmentId, @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super
                .read(ResourceNames.SECTION_ASSESSMENT_ASSOCIATIONS, "assessmentId", assessmentId, headers, uriInfo);
    }

    /**
     * Returns each $$sections$$ entity associated to the given $$assessments$$ through
     * a $$sectionAssessmentAssociations$$
     *
     * @param assessmentId
     *            Comma separated list of ids of the $$assessments$$
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}" + "/" + PathConstants.SECTION_ASSESSMENT_ASSOCIATIONS + "/"
            + PathConstants.SECTIONS)
    public Response getSectionAssessmentAssociationsSections(
            @PathParam(ParameterConstants.ASSESSMENT_ID) final String assessmentId, @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.SECTION_ASSESSMENT_ASSOCIATIONS, "assessmentId", assessmentId, "sectionId",
                ResourceNames.SECTIONS, headers, uriInfo);
    }

    /**
     * Get a map of the objective assessment ids to $$learningStandards$$ entities for the given $$assessments$$.
     *
     * @param id
     *            the id of the assessment
     * @return the learning standards
     */
    @GET
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}" + "/" + PathConstants.LEARNING_STANDARDS)
    public Response getLearningStandards(@PathParam(ParameterConstants.ASSESSMENT_ID) final String id) {
        return Response.status(Status.NOT_FOUND).build();
    }

    /**
     * Get a map of the objective assessment ids and assessmentItem ids to $$learningObjectives$$ entities for the given $$assessments$$.
     *
     * @param id
     *            the id of the assessment
     * @return the learning objectives
     */
    @GET
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}" + "/" + PathConstants.LEARNING_OBJECTIVES)
    public Response getLearningObjectives(@PathParam(ParameterConstants.ASSESSMENT_ID) final String id) {
        return Response.status(Status.NOT_FOUND).build();
    }



}
