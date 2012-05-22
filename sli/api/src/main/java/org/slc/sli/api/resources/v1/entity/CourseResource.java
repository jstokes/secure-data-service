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

import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.resources.v1.DefaultCrudResource;
import org.slc.sli.common.constants.ResourceNames;
import org.slc.sli.common.constants.v1.ParameterConstants;
import org.slc.sli.common.constants.v1.PathConstants;

/**
 *
 * Represents the organization of subject matter and related learning experiences provided
 * for the instruction of students on a regular or systematic basis.
 *
 * This is similar to section except that a section is a specific instance of a course.
 */
@Path(PathConstants.V1 + "/" + PathConstants.COURSES)
@Component
@Scope("request")
public class CourseResource extends DefaultCrudResource {

    @Autowired
    public CourseResource(EntityDefinitionStore entityDefs) {
        super(entityDefs, ResourceNames.COURSES);
    }

    /**
     * Returns each $$courseOfferings$$ that references the given $$courses$$.
     *
     * @param courseId
     *            The id of the $$courses$$
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return $$courseOfferings$$ that reference the given $$courses$$
     *
     */
    @GET
    @Path("{" + ParameterConstants.COURSE_ID + "}" + "/" + PathConstants.COURSE_OFFERINGS)
    public Response getCourseOfferings(@PathParam(ParameterConstants.COURSE_ID) final String courseId,
            @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.COURSE_OFFERINGS, "courseId", courseId, headers, uriInfo);
    }


    /**
     * Returns each $$sessions$$ that is associated with the given $$courses$$.
     *
     * @param courseId
     *            The id of the $$courses$$
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return $$sessions$$ associated with the given $$courses$$
     */
    @GET
    @Path("{" + ParameterConstants.COURSE_ID + "}" + "/" + PathConstants.COURSE_OFFERINGS + "/" + PathConstants.SESSIONS)
    public Response getCourseOfferingCourses(@PathParam(ParameterConstants.COURSE_ID) final String courseId,
            @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.COURSE_OFFERINGS, "courseId", courseId, "sessionId", ResourceNames.SESSIONS, headers, uriInfo);
    }


    /**
     * Returns each $$studentTranscriptAssociations$$ that references the given $$courses$$.
     *
     * @param courseId
     *            The id of the $$courses$$
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return $$studentTranscriptAssociations$$ that reference the given $$courses$$
     */
    @GET
    @Path("{" + ParameterConstants.COURSE_ID + "}" + "/" + PathConstants.COURSE_TRANSCRIPTS)
    public Response getStudentTranscriptAssociations(@PathParam(ParameterConstants.COURSE_ID) final String courseId,
                                                     @Context HttpHeaders headers,
                                                     @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_TRANSCRIPT_ASSOCIATIONS, "courseId", courseId, headers, uriInfo);
    }

    /**
     * Returns each $$students$$ that is associated with the given $$courses$$ through $$studentTranscriptAssociations$$.
     *
     * @param courseId
     *            The id of the $$courses$$
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return $$students$$ associated with the given $$courses$$ through $$studentTranscriptAssociations$$
     */
    @GET
    @Path("{" + ParameterConstants.COURSE_ID + "}" + "/" + PathConstants.COURSE_TRANSCRIPTS + "/" + PathConstants.STUDENTS)
    public Response getStudentTranscriptAssociationStudents(@PathParam(ParameterConstants.COURSE_ID) final String courseId,
                                                            @Context HttpHeaders headers,
                                                            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_TRANSCRIPT_ASSOCIATIONS, "courseId", courseId, "studentId", ResourceNames.STUDENTS, headers, uriInfo);
    }


    /**
     * Returns each $$studentParentAssociations$$ that references the given $$courses$$.
     *
     * @param courseId
     *            The id of the $$courses$$
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return $$studentParentAssociations$$ that reference the given $$courses$$
     */
    @GET
    @Path("{" + ParameterConstants.COURSE_ID + "}" + "/" + PathConstants.STUDENT_PARENT_ASSOCIATIONS)
    public Response getStudentParentAssociations(@PathParam(ParameterConstants.COURSE_ID) final String courseId,
                                                     @Context HttpHeaders headers,
                                                     @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_PARENT_ASSOCIATIONS, "courseId", courseId, headers, uriInfo);
    }

    /**
     * Returns each $$students$$ that is associated with the given $$courses$$ through $$studentParentAssociations$$.
     *
     * @param courseId
     *            The id of the $$courses$$
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return $$students$$ associated with the given $$courses$$ through $$studentParentAssociations$$
     */
    @GET
    @Path("{" + ParameterConstants.COURSE_ID + "}" + "/" + PathConstants.STUDENT_PARENT_ASSOCIATIONS + "/" + PathConstants.STUDENTS)
    public Response getStudentParentAssociationStudents(@PathParam(ParameterConstants.COURSE_ID) final String courseId,
                                                            @Context HttpHeaders headers,
                                                            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_PARENT_ASSOCIATIONS, "courseId", courseId, "studentId", ResourceNames.STUDENTS, headers, uriInfo);
    }

}
