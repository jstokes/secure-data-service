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
 * Responds to requests for school-related information.
 * 
 * @author srupasinghe
 * @author kmyers
 * 
 */
@Path(PathConstants.V1 + "/" + PathConstants.SCHOOLS)
@Component
@Scope("request")
@Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
public class SchoolResource extends DefaultCrudEndpoint {
    
    @Autowired
    public SchoolResource(EntityDefinitionStore entityDefs) {
        super(entityDefs, ResourceNames.SCHOOLS);
    }

    /**
     * Returns all $$schools$$ entities for which the logged in User has permission and context.
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
     * Create a new $$schools$$ entity.
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
    @Consumes({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    public Response create(final EntityBody newEntityBody, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.create(newEntityBody, headers, uriInfo);
    }

    /**
     * Get a single $$schools$$ entity
     * 
     * @param schoolId
     *            The Id of the $$schools$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return A single school entity
     */
    @GET
    @Path("{" + ParameterConstants.SCHOOL_ID + "}")
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    public Response read(@PathParam(ParameterConstants.SCHOOL_ID) final String schoolId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(schoolId, headers, uriInfo);
    }

    /**
     * Delete a $$schools$$ entity
     * 
     * @param schoolId
     *            The Id of the $$schools$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Returns a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    @DELETE
    @Path("{" + ParameterConstants.SCHOOL_ID + "}")
    public Response delete(@PathParam(ParameterConstants.SCHOOL_ID) final String schoolId, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.delete(schoolId, headers, uriInfo);
    }

    /**
     * Update an existing $$schools$$ entity.
     * 
     * @param schoolId
     *            The id of the $$schools$$.
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
    @Path("{" + ParameterConstants.SCHOOL_ID + "}")
    public Response update(@PathParam(ParameterConstants.SCHOOL_ID) final String schoolId,
            final EntityBody newEntityBody, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.update(schoolId, newEntityBody, headers, uriInfo);
    }

    /**
     * Returns each $$teacherSchoolAssociations$$ that
     * references the given $$schools$$
     * 
     * @param schoolId
     *            The Id of the School.
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
    @Path("{" + ParameterConstants.SCHOOL_ID + "}" + "/" + PathConstants.TEACHER_SCHOOL_ASSOCIATIONS)
    public Response getTeacherSchoolAssociations(@PathParam(ParameterConstants.SCHOOL_ID) final String schoolId,
            @Context HttpHeaders headers, 
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.TEACHER_SCHOOL_ASSOCIATIONS, "schoolId", schoolId, headers, uriInfo);
    }
    

    /**
     * $$teacherSchoolAssociations$$ - teacher lookup
     * 
     * @param schoolId
     *            The Id of the School.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.SCHOOL_ID + "}" + "/" + PathConstants.TEACHER_SCHOOL_ASSOCIATIONS + "/" + PathConstants.TEACHERS)
    public Response getTeacherSchoolAssociationTeachers(@PathParam(ParameterConstants.SCHOOL_ID) final String schoolId,
            @Context HttpHeaders headers, 
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.TEACHER_SCHOOL_ASSOCIATIONS, "schoolId", schoolId, "teacherId", ResourceNames.TEACHERS, headers, uriInfo);
    }

    
    /**
     * $$studentSchoolAssociations$$
     * 
     * @param schoolId
     *            The Id of the School.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.SCHOOL_ID + "}" + "/" + PathConstants.STUDENT_SCHOOL_ASSOCIATIONS)
    public Response getStudentSchoolAssociations(@PathParam(ParameterConstants.SCHOOL_ID) final String schoolId,
            @Context HttpHeaders headers, 
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_SCHOOL_ASSOCIATIONS, "schoolId", schoolId, headers, uriInfo);
    }

    /**
     * $$studentSchoolAssociations$$ - student lookup
     * 
     * @param schoolId
     *            The Id of the School.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.SCHOOL_ID + "}" + "/" + PathConstants.STUDENT_SCHOOL_ASSOCIATIONS + "/" + PathConstants.STUDENTS)
    public Response getStudentSchoolAssociationStudents(@PathParam(ParameterConstants.SCHOOL_ID) final String schoolId,
            @Context HttpHeaders headers, 
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_SCHOOL_ASSOCIATIONS, "schoolId", schoolId, "studentId", ResourceNames.STUDENTS, headers, uriInfo);
    }
    
    /**
     * Returns each $$sections$$ at the school. Section's reference does not use an association,
     * so this method returns sections and not section/school associations.
     * 
     * @param schoolId
     *            The Id of the School.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.SCHOOL_ID + "}" + "/" + PathConstants.SECTIONS)
    public Response getSectionsForSchool(@PathParam(ParameterConstants.SCHOOL_ID) final String schoolId,
            @Context HttpHeaders headers, 
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.SECTIONS, "schoolId", schoolId, headers, uriInfo);
    }
    

    /**
     * Returns each $$schoolSessionAssociations$$ that
     * references the given $$schools$$
     * 
     * @param schoolId
     *            The Id of the School.
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
    @Path("{" + ParameterConstants.SCHOOL_ID + "}" + "/" + PathConstants.SCHOOL_SESSION_ASSOCIATIONS)
    public Response getSchoolSessionAssociations(@PathParam(ParameterConstants.SCHOOL_ID) final String schoolId,
            @Context HttpHeaders headers, 
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.SCHOOL_SESSION_ASSOCIATIONS, "schoolId", schoolId, headers, uriInfo);
    }
    

    /**
     * Returns each $$sessions$$ associated to the given school through
     * a $$schoolSessionAssociations$$ 
     * 
     * @param schoolId
     *            The Id of the School.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.SCHOOL_ID + "}" + "/" + PathConstants.SCHOOL_SESSION_ASSOCIATIONS + "/" + PathConstants.SESSIONS)
    public Response getSchoolSessionAssociationSessions(@PathParam(ParameterConstants.SCHOOL_ID) final String schoolId,
            @Context HttpHeaders headers, 
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.SCHOOL_SESSION_ASSOCIATIONS, "schoolId", schoolId, "sessionId", ResourceNames.SESSIONS, headers, uriInfo);
    }

}
