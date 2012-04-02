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
 * @author jstokes
 * 
 */
@Path(PathConstants.V1 + "/" + PathConstants.DISCIPLINE_INCIDENTS)
@Component
@Scope("request")
@Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
public class DisciplineIncidentResource extends DefaultCrudEndpoint {
    
    @Autowired
    public DisciplineIncidentResource(EntityDefinitionStore entityDefs) {
        super(entityDefs, ResourceNames.DISCIPLINE_INCIDENTS);
    }

    /**
     * Returns all $$disciplineIncidents$$ entities for which the logged in User has permission and context.
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
     * Create a new $$disciplineIncidents$$ entity.
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
     *                 item is accessible.}
     */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response create(final EntityBody newEntityBody, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.create(newEntityBody, headers, uriInfo);
    }

    /**
     * Get a single $$disciplineIncidents$$ entity
     * 
     * @param disciplineIncidentId
     *            The Id of the $$disciplineIncidents$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return A single disciplineIncident entity
     */
    @GET
    @Path("{" + ParameterConstants.DISCIPLINE_INCIDENT_ID + "}")
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    public Response read(@PathParam(ParameterConstants.DISCIPLINE_INCIDENT_ID) final String disciplineIncidentId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(disciplineIncidentId, headers, uriInfo);
    }

    /**
     * Delete a $$disciplineIncidents$$ entity
     * 
     * @param disciplineIncidentId
     *            The Id of the $$disciplineIncidents$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Returns a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    @DELETE
    @Path("{" + ParameterConstants.DISCIPLINE_INCIDENT_ID + "}")
    public Response delete(@PathParam(ParameterConstants.DISCIPLINE_INCIDENT_ID) final String disciplineIncidentId, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.delete(disciplineIncidentId, headers, uriInfo);
    }

    /**
     * Update an existing $$disciplineIncidents$$ entity.
     * 
     * @param disciplineIncidentId
     *            The id of the $$disciplineIncidents$$.
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
    @Path("{" + ParameterConstants.DISCIPLINE_INCIDENT_ID + "}")
    public Response update(@PathParam(ParameterConstants.DISCIPLINE_INCIDENT_ID) final String disciplineIncidentId,
            final EntityBody newEntityBody, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.update(disciplineIncidentId, newEntityBody, headers, uriInfo);
    }


    /**
     * $$studentDisciplineIncidentAssociations$$
     *
     * @param disciplineIncidentId
     *            The id of the $$disciplineIncidents$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.DISCIPLINE_INCIDENT_ID + "}" + "/" + PathConstants.STUDENT_DISCIPLINE_INCIDENT_ASSOCIATIONS)
    public Response getStudentDisciplineIncidentAssociations(@PathParam(ParameterConstants.DISCIPLINE_INCIDENT_ID) final String disciplineIncidentId,
                                                             @Context HttpHeaders headers,
                                                             @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_DISCIPLINE_INCIDENT_ASSOCIATIONS, "disciplineIncidentId", disciplineIncidentId, headers, uriInfo);
    }


    /**
     * $$studentDisciplineIncidentAssociations$$ - student lookup
     *
     * @param disciplineIncidentId
     *            The id of the $$disciplineIncidents$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.DISCIPLINE_INCIDENT_ID + "}" + "/" + PathConstants.STUDENT_DISCIPLINE_INCIDENT_ASSOCIATIONS + "/" + PathConstants.STUDENTS)
    public Response getStudentDisciplineIncidentAssociationCourses(@PathParam(ParameterConstants.DISCIPLINE_INCIDENT_ID) final String disciplineIncidentId,
                                                                   @Context HttpHeaders headers,
                                                                   @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_DISCIPLINE_INCIDENT_ASSOCIATIONS, "disciplineIncidentId", disciplineIncidentId, "studentId", ResourceNames.STUDENTS, headers, uriInfo);
    }
}
