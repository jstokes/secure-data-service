package org.slc.sli.api.resources.v1.entity;

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
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.ResourceNames;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.util.ResourceUtil;
import org.slc.sli.api.resources.v1.CrudEndpoint;
import org.slc.sli.api.resources.v1.HypermediaType;
import org.slc.sli.api.resources.v1.ParameterConstants;
import org.slc.sli.api.resources.v1.PathConstants;

/**
 * Resource for obtaining assessment information
 * 
 * @author jstokes
 * 
 */
@Path(PathConstants.V1 + "/" + PathConstants.ASSESSMENTS)
@Component
@Scope("request")
@Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
public class AssessmentResource {
    
    /*
     * Interface capable of performing CRUD operations.
     */
    private final CrudEndpoint crudDelegate;
    
    @Autowired
    public AssessmentResource(CrudEndpoint crudDelegate) {
        this.crudDelegate = crudDelegate;
    }
    
    /**
     * Returns all $$assessments$$ for which the logged in user has permission to see.
     * 
     * @param offset
     *            starting position in results to return to user
     * @param limit
     *            maximum number of results to return to user (starting from offset)
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @GET
    public Response readAll(
            @QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
            @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        ResourceUtil.putValue(headers.getRequestHeaders(), ParameterConstants.LIMIT, limit);
        ResourceUtil.putValue(headers.getRequestHeaders(), ParameterConstants.OFFSET, offset);
        return this.crudDelegate.readAll(ResourceNames.ASSESSMENTS, headers, uriInfo);
    }
    
    /**
     * Create a new $$assessment$$.
     * 
     * @param newEntityBody
     *            assessment data
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     * @response.param {@name Location} {@style header} {@type
     *                 {http://www.w3.org/2001/XMLSchema}anyURI} {@doc The URI where the created
     *                 item is accessable.}
     */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    public Response create(final EntityBody newEntityBody, @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return this.crudDelegate.create(ResourceNames.ASSESSMENTS, newEntityBody, headers, uriInfo);
    }
    
    /**
     * Get a single $$assessment$$
     * 
     * @param assessmentId
     *            The comma separated list of ids of $$assessments$$
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return A single assessment entity
     */
    @GET
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}")
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    public Response read(@PathParam(ParameterConstants.ASSESSMENT_ID) final String assessmentId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return this.crudDelegate.read(ResourceNames.ASSESSMENTS, assessmentId, headers, uriInfo);
    }
    
    /**
     * Delete an $$assessment$$
     * 
     * @param assessmentId
     *            The id of the $$assessment$$
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Returns a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    @DELETE
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}")
    public Response delete(@PathParam(ParameterConstants.ASSESSMENT_ID) final String assessmentId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return this.crudDelegate.delete(ResourceNames.ASSESSMENTS, assessmentId, headers, uriInfo);
    }
    
    /**
     * Update an existing $$assessment$$
     * 
     * @param assessmentId
     *            The id of the $$assessment$$
     * @param newEntityBody
     *            assessment data
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Response with a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    @PUT
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}")
    public Response update(@PathParam(ParameterConstants.ASSESSMENT_ID) final String assessmentId,
            final EntityBody newEntityBody, @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return this.crudDelegate.update(ResourceNames.ASSESSMENTS, assessmentId, newEntityBody, headers, uriInfo);
    }
    
    /**
     * Returns all the $$studentAssessmentAssociations$$ for the given $$assessments$$
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
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}" + "/" + PathConstants.STUDENT_ASSESSMENT_ASSOCIATIONS)
    public Response getStudentAssessmentAssociations(
            @PathParam(ParameterConstants.ASSESSMENT_ID) final String assessmentId, @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return this.crudDelegate.read(ResourceNames.STUDENT_ASSESSMENT_ASSOCIATIONS, "assessmentId", assessmentId,
                headers, uriInfo);
    }
    
    /**
     * Returns each $$student$$ associated to the given assessment through
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
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}" + "/" + PathConstants.STUDENT_ASSESSMENT_ASSOCIATIONS + "/"
            + PathConstants.STUDENTS)
    public Response getStudentAssessmentAssociationsStudents(
            @PathParam(ParameterConstants.ASSESSMENT_ID) final String assessmentId, @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return this.crudDelegate.read(ResourceNames.STUDENT_ASSESSMENT_ASSOCIATIONS, "assessmentId", assessmentId,
                "studentId", ResourceNames.STUDENTS, headers, uriInfo);
    }
    
    /**
     * Returns each $$sectionAssessmentAssociations$$ that
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
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}" + "/" + PathConstants.SECTION_ASSESSMENT_ASSOCIATIONS)
    public Response getSectionAssessmentAssociations(
            @PathParam(ParameterConstants.ASSESSMENT_ID) final String assessmentId, @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return this.crudDelegate.read(ResourceNames.SECTION_ASSESSMENT_ASSOCIATIONS, "assessmentId", assessmentId,
                headers, uriInfo);
    }
    
    /**
     * Returns each $$section$$ associated to the given $$assessment$$ through
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
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}" + "/" + PathConstants.SECTION_ASSESSMENT_ASSOCIATIONS + "/"
            + PathConstants.SECTIONS)
    public Response getSectionAssessmentAssociationsSections(
            @PathParam(ParameterConstants.ASSESSMENT_ID) final String assessmentId, @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return this.crudDelegate.read(ResourceNames.SECTION_ASSESSMENT_ASSOCIATIONS, "assessmentId", assessmentId,
                "sectionId", ResourceNames.SECTIONS, headers, uriInfo);
    }
    
}
