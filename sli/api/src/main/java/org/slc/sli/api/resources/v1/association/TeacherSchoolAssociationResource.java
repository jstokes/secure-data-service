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
import org.slc.sli.api.resources.v1.DefaultCrudResource;
import org.slc.sli.common.constants.ResourceNames;
import org.slc.sli.common.constants.v1.ParameterConstants;
import org.slc.sli.common.constants.v1.PathConstants;

/**
 * Prototype new api end points and versioning
 *
 * @author srupasinghe
 *
 */
@Path(PathConstants.V1 + "/" + PathConstants.TEACHER_SCHOOL_ASSOCIATIONS)
@Component
@Scope("request")
public class TeacherSchoolAssociationResource extends DefaultCrudResource {

    @Autowired
    public TeacherSchoolAssociationResource(EntityDefinitionStore entityDefs) {
        super(entityDefs, ResourceNames.TEACHER_SCHOOL_ASSOCIATIONS);
    }

    /**
     * Returns each $$teachers$$ that
     * references the given $$teacherSchoolAssociations$$
     *
     * @param teacherSchoolAssociationId
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
    @Path("{" + ParameterConstants.TEACHER_SCHOOL_ASSOC_ID + "}" + "/" + PathConstants.TEACHERS)
    public Response getTeachersForAssociation(@PathParam(ParameterConstants.TEACHER_SCHOOL_ASSOC_ID) final String teacherSchoolAssociationId,
            @QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
            @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.TEACHER_SCHOOL_ASSOCIATIONS, "_id", teacherSchoolAssociationId, "teacherId", ResourceNames.TEACHERS, headers, uriInfo);
    }

    /**
     * Returns each $$schools$$ that
     * references the given $$teacherSchoolAssociations$$
     *
     * @param teacherSchoolAssociationId
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
    @Path("{" + ParameterConstants.TEACHER_SCHOOL_ASSOC_ID + "}" + "/" + PathConstants.SCHOOLS)
    public Response getSchoolsForAssociation(@PathParam(ParameterConstants.TEACHER_SCHOOL_ASSOC_ID) final String teacherSchoolAssociationId,
            @QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
            @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.TEACHER_SCHOOL_ASSOCIATIONS, "_id", teacherSchoolAssociationId, "schoolId", ResourceNames.SCHOOLS, headers, uriInfo);
    }

}
