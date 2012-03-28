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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Prototype new api end points and versioning
 * 
 * @author jstokes
 * 
 */
@Path(PathConstants.V1 + "/" + PathConstants.COHORTS)
@Component
@Scope("request")
@Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
public class CohortResource {
    
    public static final String COHORT_DESCRIPTION = "cohortDescription";
    public static final String COHORT_TYPE = "cohortType";
    public static final String COHORT_SCOPE = "cohortScope";
    public static final String ACADEMIC_SUBJECT = "academicSubject";
    public static final String EDUCATION_ORGANIZATION_ID = "educationOrgId";
    
    /**
     * Logging utility.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CohortResource.class);
    
    /*
     * Interface capable of performing CRUD operations.
     */
    private final CrudEndpoint crudDelegate;

    @Autowired
    public CohortResource(CrudEndpoint crudDelegate) {
        this.crudDelegate = crudDelegate;
    }

    /**
     * Returns all $$cohorts$$ entities for which the logged in User has permission and context.
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
    public Response readAll(@QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
            @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        ResourceUtil.putValue(headers.getRequestHeaders(), ParameterConstants.LIMIT, limit);
        ResourceUtil.putValue(headers.getRequestHeaders(), ParameterConstants.OFFSET, offset);
        return this.crudDelegate.readAll(ResourceNames.COHORTS, headers, uriInfo);
    }

    /**
     * Create a new $$cohorts$$ entity.
     * 
     * @param newEntityBody
     *            entity data
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *              URI information including path and query parameters
     * @return result of CRUD operation
     * @response.param {@name Location} {@style header} {@type
     *                 {http://www.w3.org/2001/XMLSchema}anyURI} {@doc The URI where the created
     *                 item is accessable.}
     */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response create(final EntityBody newEntityBody, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return this.crudDelegate.create(ResourceNames.COHORTS, newEntityBody, headers, uriInfo);
    }

    /**
     * Get a single $$cohorts$$ entity
     * 
     * @param cohortId
     *            The Id of the $$cohorts$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return A single cohort entity
     */
    @GET
    @Path("{" + ParameterConstants.COHORT_ID + "}")
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    public Response read(@PathParam(ParameterConstants.COHORT_ID) final String cohortId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return this.crudDelegate.read(ResourceNames.COHORTS, cohortId, headers, uriInfo);
    }

    /**
     * Delete a $$cohorts$$ entity
     * 
     * @param cohortId
     *            The Id of the $$cohorts$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Returns a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    @DELETE
    @Path("{" + ParameterConstants.COHORT_ID + "}")
    public Response delete(@PathParam(ParameterConstants.COHORT_ID) final String cohortId, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return this.crudDelegate.delete(ResourceNames.COHORTS, cohortId, headers, uriInfo);
    }

    /**
     * Update an existing $$cohorts$$ entity.
     * 
     * @param cohortId
     *            The id of the $$cohorts$$.
     * @param newEntityBody
     *            entity data
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Response with a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    @PUT
    @Path("{" + ParameterConstants.COHORT_ID + "}")
    public Response update(@PathParam(ParameterConstants.COHORT_ID) final String cohortId,
            final EntityBody newEntityBody, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return this.crudDelegate.update(ResourceNames.COHORTS, cohortId, newEntityBody, headers, uriInfo);
    }

    /**
     * Returns each $$staffCohortAssociations$$ that
     * references the given $$cohort$$
     * 
     * @param cohortId
     *            The Id of the Cohort.
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
    @Path("{" + ParameterConstants.COHORT_ID + "}" + "/" + PathConstants.STAFF_COHORT_ASSOCIATIONS)
    public Response getStaffCohortAssociations(@PathParam(ParameterConstants.COHORT_ID) final String cohortId,
            @Context HttpHeaders headers, 
            @Context final UriInfo uriInfo) {
        return this.crudDelegate.read(ResourceNames.STAFF_COHORT_ASSOCIATIONS, "cohortReference", cohortId, headers, uriInfo);
    }
    

    /**
     * Returns each $$staff$$ associated to the given cohort through
     * a $$staffCohortAssociations$$ 
     * 
     * @param cohortId
     *            The Id of the Cohort.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.COHORT_ID + "}" + "/" + PathConstants.STAFF_COHORT_ASSOCIATIONS + "/" + PathConstants.STAFF)
    public Response getStaffCohortAssociationStaff(@PathParam(ParameterConstants.COHORT_ID) final String cohortId,
            @Context HttpHeaders headers, 
            @Context final UriInfo uriInfo) {
        return this.crudDelegate.read(ResourceNames.STAFF_COHORT_ASSOCIATIONS, "cohortReference", cohortId, 
                "staffReference", ResourceNames.STAFF, headers, uriInfo);
    }

    /**
     * Returns each $$studentCohortAssociations$$ that
     * references the given $$cohort$$
     * 
     * @param cohortId
     *            The Id of the Cohort.
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
    @Path("{" + ParameterConstants.COHORT_ID + "}" + "/" + PathConstants.STUDENT_COHORT_ASSOCIATIONS)
    public Response getStudentCohortAssociations(@PathParam(ParameterConstants.COHORT_ID) final String cohortId,
            @Context HttpHeaders headers, 
            @Context final UriInfo uriInfo) {
        return this.crudDelegate.read(ResourceNames.STUDENT_COHORT_ASSOCIATIONS, "cohortReference", cohortId, headers, uriInfo);
    }
    

    /**
     * Returns each $$student$$ associated to the given cohort through
     * a $$studentCohortAssociations$$ 
     * 
     * @param cohortId
     *            The Id of the Cohort.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.COHORT_ID + "}" + "/" + PathConstants.STUDENT_COHORT_ASSOCIATIONS + "/" + PathConstants.STUDENTS)
    public Response getStudentCohortAssociationStaff(@PathParam(ParameterConstants.COHORT_ID) final String cohortId,
            @Context HttpHeaders headers, 
            @Context final UriInfo uriInfo) {
        return this.crudDelegate.read(ResourceNames.STUDENT_COHORT_ASSOCIATIONS, "cohortReference", cohortId, 
                "studentReference", ResourceNames.STUDENTS, headers, uriInfo);
    }
}
