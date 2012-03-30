package org.slc.sli.api.resources.v1.association;

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

import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.config.ResourceNames;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.v1.DefaultCrudEndpoint;
import org.slc.sli.api.resources.v1.HypermediaType;
import org.slc.sli.api.resources.v1.ParameterConstants;
import org.slc.sli.api.resources.v1.PathConstants;

/**
 * Prototype new api end points and versioning
 * 
 * @author kmyers
 * 
 */
@Path(PathConstants.V1 + "/" + PathConstants.STAFF_EDUCATION_ORGANIZATION_ASSOCIATIONS)
@Component
@Scope("request")
@Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
public class StaffEducationOrganizationAssociation extends DefaultCrudEndpoint {
 
    public static final String STAFF_REFERENCE = "staffReference";
    public static final String EDUCATION_ORGANIZATION_REFERENCE = "educationOrganizationReference";
    public static final String STAFF_CLASSIFICATION = "staffClassification";
    public static final String BEGIN_DATE = "beginDate";

    /**
     * Logging utility.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(StaffEducationOrganizationAssociation.class);
    
    @Autowired
    public StaffEducationOrganizationAssociation(EntityDefinitionStore entityDefs) {
        super(entityDefs, ResourceNames.STAFF_EDUCATION_ORGANIZATION_ASSOCIATIONS);
        LOGGER.debug("New resource handler created: {}", this);
    }

    /**
     * Returns all $$staffEducationOrganizationAssociations$$ entities for which the logged in User has permission and context.
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
        return super.readAll(offset, limit, headers, uriInfo);
    }

    /**
     * Create a new $$staffEducationOrganizationAssociations$$ entity.
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
    @Consumes({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    public Response create(final EntityBody newEntityBody, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.create(newEntityBody, headers, uriInfo);
    }

    /**
     * Get a single $$staffEducationOrganizationAssociations$$ entity
     * 
     * @param staffEducationOrganizationId
     *            The Id of the $$staffEducationOrganizationAssociations$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return A single school entity
     */
    @GET
    @Path("{" + ParameterConstants.STAFF_EDUCATION_ORGANIZATION_ID + "}")
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    public Response read(@PathParam(ParameterConstants.STAFF_EDUCATION_ORGANIZATION_ID) final String staffEducationOrganizationId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(staffEducationOrganizationId, headers, uriInfo);
    }

    /**
     * Delete a $$staffEducationOrganizationAssociations$$ entity
     * 
     * @param staffEducationOrganizationId
     *            The Id of the $$staffEducationOrganizationAssociations$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Returns a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    @DELETE
    @Path("{" + ParameterConstants.STAFF_EDUCATION_ORGANIZATION_ID + "}")
    public Response delete(@PathParam(ParameterConstants.STAFF_EDUCATION_ORGANIZATION_ID) final String staffEducationOrganizationId, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.delete(staffEducationOrganizationId, headers, uriInfo);
    }

    /**
     * Update an existing $$staffEducationOrganizationAssociations$$ entity.
     * 
     * @param staffEducationOrganizationId
     *            The id of the $$staffEducationOrganizationAssociations$$.
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
    @Path("{" + ParameterConstants.STAFF_EDUCATION_ORGANIZATION_ID + "}")
    @Consumes({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    public Response update(@PathParam(ParameterConstants.STAFF_EDUCATION_ORGANIZATION_ID) final String staffEducationOrganizationId,
            final EntityBody newEntityBody, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.update(staffEducationOrganizationId, newEntityBody, headers, uriInfo);
    }

    /**
     * Returns each $$staff$$ that
     * references the given $$staffEducationOrganizationAssociations$$
     * 
     * @param staffEducationOrganizationId
     *            The Id of the teacherSchoolAssociation.
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
     * @return       
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.STAFF_EDUCATION_ORGANIZATION_ID + "}" + "/" + PathConstants.STAFF)
    public Response getStaffEducationOrganizationAssocationStaff(@PathParam(ParameterConstants.STAFF_EDUCATION_ORGANIZATION_ID) final String staffEducationOrganizationId,
            @QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
            @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
       return super.read(ResourceNames.STAFF_EDUCATION_ORGANIZATION_ASSOCIATIONS, "_id", staffEducationOrganizationId, 
               "staffReference", ResourceNames.STAFF, headers, uriInfo);
    }
    
    /**
     * Returns each $$educationalOrganizations$$ that
     * references the given $$staffEducationOrganizationAssociations$$
     * 
     * @param staffEducationOrganizationId
     *            The Id of the teacherSchoolAssociation.
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
     * @return       
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.STAFF_EDUCATION_ORGANIZATION_ID + "}" + "/" + PathConstants.EDUCATION_ORGANIZATIONS)
    public Response getStaffEducationOrganizationAssocationEducationOrganizations(@PathParam(ParameterConstants.STAFF_EDUCATION_ORGANIZATION_ID) final String staffEducationOrganizationId,
            @QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
            @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STAFF_EDUCATION_ORGANIZATION_ASSOCIATIONS, "_id", staffEducationOrganizationId, 
                "educationOrganizationReference", ResourceNames.EDUCATION_ORGANIZATIONS, headers, uriInfo);
    }
}
