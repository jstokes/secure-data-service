package org.slc.sli.api.resources.v1.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
 * @author jstokes
 * 
 */
@Path(PathConstants.V1 + "/" + PathConstants.STUDENTS)
@Component
@Scope("request")
@Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
public class StudentResource extends DefaultCrudEndpoint {

    public static final String UNIQUE_STATE_ID = "studentUniqueStateId";
    public static final String NAME = "name";
    public static final String SEX = "sex";
    public static final String BIRTH_DATA = "birthData";
    public static final String HISPANIC_LATINO_ETHNICITY = "hispanicLatinoEthnicity";

    /**
     * Logging utility.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentResource.class);
    
    /*
     *Constants for readWithGrade 
     * 
     */
    private static final String ENTRY_GRADE_LEVEL = "entryGradeLevel";
    private static final String ENTRY_DATE = "entryDate";
    private static final String EXIT_WITHDRAW_DATE = "exitWithdrawDate";
    private static final String GRADE_LEVEL = "gradeLevel";
    

    @Autowired
    public StudentResource(EntityDefinitionStore entityDefs) {
        super(entityDefs, ResourceNames.STUDENTS);
    }

    /**
     * Returns all $$students$$ entities for which the logged in User has permission and context.
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
     * Create a new $$students$$ entity.
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
     * Get a single $$students$$ entity
     * 
     * @param studentId
     *            The Id of the $$students$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return A single student entity
     */
    @GET
    @Path("{" + ParameterConstants.STUDENT_ID + "}")
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    public Response read(@PathParam(ParameterConstants.STUDENT_ID) final String id,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(id, headers, uriInfo);
    }

    
    /**
     * Get a single $$students$$ entity, with the grade included
     * Calculates the current grade based on entryDate and exitWithdrawDate in studentSchoolAssociations
     * Returns student with gradeLevel "Not Available" when information is insufficient, or the code experiences an exception
     * 
     * @param studentId
     *            The Id of the $$students$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return A single student entity
     * @throws ParseException 
     */
    @SuppressWarnings("unchecked")
    @GET
    @Path("{" + ParameterConstants.STUDENT_ID + "}" + "/" + PathConstants.STUDENT_WITH_GRADE)
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    public Response readWithGrade(@PathParam(ParameterConstants.STUDENT_ID) final String studentId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {

        // Most recent grade level, not available till found
        String mostRecentGradeLevel = "Not Available";
        
        //Retrieve student entity for student with id = studentId
        Response studentResponse = read(studentId, headers, uriInfo);
        
        if ((studentResponse == null) || !(studentResponse.getEntity() instanceof Map))
            return studentResponse;
        Map<String, String> student = (Map<String, String>) studentResponse.getEntity();
        
        //Retrieve studentSchoolAssociations for student with id = studentId
        Response studentSchoolAssociationsResponse = getStudentSchoolAssociations(studentId, headers, uriInfo);
        
        if ((studentSchoolAssociationsResponse == null) || !(studentSchoolAssociationsResponse.getEntity() instanceof List)) {
            student.put(GRADE_LEVEL, mostRecentGradeLevel);
            return studentResponse;
        }
        
        List<Map<?, ?>> studentSchoolAssociationList = (List<Map<?, ?>>) studentSchoolAssociationsResponse.getEntity();
        
        //Variable initialization for date functions
        Date currentDate = new Date();
        Date mostRecentEntry = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        //Try catch to stifle unexpected exceptions, and log them.
        //Returns "Not Available" for gradeLevel, when an exception is caught.
        try {
            // Loop through studentSchoolAssociations
            for (Map<?, ?> studentSchoolAssociation : studentSchoolAssociationList) {
                
                // If student has an exitWithdrawDate earlier than today, continue searching for current grade
                if (studentSchoolAssociation.containsKey(EXIT_WITHDRAW_DATE)) {
                    Date ssaDate = sdf.parse((String) studentSchoolAssociation.get(EXIT_WITHDRAW_DATE));
                    if (ssaDate.compareTo(currentDate) <= 0) {
                        continue;
                    }
                }
    
                //If student has no exitWithdrawDate, check for the latest entryDate
                // Mark the entryGradeLevel with the most recent entryDate as the current grade
                if (studentSchoolAssociation.containsKey(ENTRY_DATE)) {
                    Date ssaDate = sdf.parse((String) studentSchoolAssociation.get(ENTRY_DATE));
                    
                    if (mostRecentEntry == null) {
                        mostRecentEntry = ssaDate;
                        mostRecentGradeLevel = (String) studentSchoolAssociation.get(ENTRY_GRADE_LEVEL);
                    } else {
                        if (ssaDate.compareTo(mostRecentEntry) > 0) {
                            mostRecentEntry = ssaDate;
                            mostRecentGradeLevel = (String) studentSchoolAssociation.get(ENTRY_GRADE_LEVEL);
                        }
                    }
                }
            }
        } catch (Exception e) {
            String exceptionMessage = "Exception while retrieving current gradeLevel for student with id:  " + studentId + " Exception: " + e.getMessage();
            LOGGER.debug(exceptionMessage);
            mostRecentGradeLevel = "Not Available";
        }
        
        student.put(GRADE_LEVEL, mostRecentGradeLevel);
        return studentResponse;
    }
    
    
    /**
     * Delete a $$students$$ entity
     * 
     * @param studentId
     *            The Id of the $$students$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Returns a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    @DELETE
    @Path("{" + ParameterConstants.STUDENT_ID + "}")
    public Response delete(@PathParam(ParameterConstants.STUDENT_ID) final String studentId, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.delete(studentId, headers, uriInfo);
    }

    /**
     * Update an existing $$students$$ entity.
     * 
     * @param studentId
     *            The id of the $$students$$.
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
    @Path("{" + ParameterConstants.STUDENT_ID + "}")
    public Response update(@PathParam(ParameterConstants.STUDENT_ID) final String studentId,
            final EntityBody newEntityBody, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.update(studentId, newEntityBody, headers, uriInfo);
    }

    /**
     * Returns each $$studentSectionAssociations$$ that
     * references the given $$students$$
     * 
     * @param studentId
     *            The id of the $$students$$.
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
    @Path("{" + ParameterConstants.STUDENT_ID + "}" + "/" + PathConstants.STUDENT_SECTION_ASSOCIATIONS)
    public Response getStudentSectionAssociations(@PathParam(ParameterConstants.STUDENT_ID) final String studentId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_SECTION_ASSOCIATIONS, "studentId", studentId, headers,
                uriInfo);
    }

    /**
     * Returns each $$sections$$ associated to the given student through
     * a $$studentSectionAssociations$$
     * 
     * @param studentId
     *            The id of the $$students$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.STUDENT_ID + "}" + "/" + PathConstants.STUDENT_SECTION_ASSOCIATIONS + "/"
            + PathConstants.SECTIONS)
    public Response getStudentSectionAssociationSections(
            @PathParam(ParameterConstants.STUDENT_ID) final String studentId, @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_SECTION_ASSOCIATIONS, "studentId", studentId, "sectionId",
                ResourceNames.SECTIONS, headers, uriInfo);
    }
    
    /**
     * student school associations
     * 
     * @param studentId
     *            The Id of the Student.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.STUDENT_ID + "}" + "/" + PathConstants.STUDENT_SCHOOL_ASSOCIATIONS)
    public Response getStudentSchoolAssociations(@PathParam(ParameterConstants.STUDENT_ID) final String studentId,
            @Context HttpHeaders headers, 
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_SCHOOL_ASSOCIATIONS, "studentId", studentId, headers, uriInfo);
    }
    
    /**
     * student school associations - schools lookup
     * 
     * @param studentId
     *            The Id of the Student.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.STUDENT_ID + "}" + "/" + PathConstants.STUDENT_SCHOOL_ASSOCIATIONS + "/" + PathConstants.SCHOOLS)
    public Response getStudentSchoolAssociationSchools(@PathParam(ParameterConstants.STUDENT_ID) final String studentId,
            @Context HttpHeaders headers, 
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_SCHOOL_ASSOCIATIONS, "studentId", studentId, "schoolId", ResourceNames.SCHOOLS, headers, uriInfo);
    }

    /**
     * Returns each $$studentAssessmentAssociations$$ that
     * references the given $$students$$
     *
     * @param studentId   The id of the $$students$$.
     * @param offset      Index of the first result to return
     * @param limit       Maximum number of results to return.
     * @param expandDepth Number of hops (associations) for which to expand entities.
     * @param headers     HTTP Request Headers
     * @param uriInfo     URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.STUDENT_ID + "}" + "/" + PathConstants.STUDENT_ASSESSMENT_ASSOCIATIONS)
    public Response getStudentAssessmentAssociations(@PathParam(ParameterConstants.STUDENT_ID) final String studentId,
                                                     @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_ASSESSMENT_ASSOCIATIONS, "studentId", studentId, headers,
                uriInfo);
    }

    /**
     * Returns each $$assessments$$ associated to the given section through
     * a $$studentAssessmentAssociations$$
     *
     * @param studentId The id of the $$students$$.
     * @param headers   HTTP Request Headers
     * @param uriInfo   URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.STUDENT_ID + "}" + "/" + PathConstants.STUDENT_ASSESSMENT_ASSOCIATIONS + "/"
            + PathConstants.ASSESSMENTS)
    public Response getStudentAssessmentAssociationsAssessments(
            @PathParam(ParameterConstants.STUDENT_ID) final String studentId, @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_ASSESSMENT_ASSOCIATIONS, "studentId", studentId, "assessmentId",
                ResourceNames.ASSESSMENTS, headers, uriInfo);
    }

    /**
     * Returns each $$attendance$$ that
     * references the given $$students$$
     *
     * @param studentId   The id of the $$students$$.
     * @param offset      Index of the first result to return
     * @param limit       Maximum number of results to return.
     * @param expandDepth Number of hops (associations) for which to expand entities.
     * @param headers     HTTP Request Headers
     * @param uriInfo     URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.STUDENT_ID + "}" + "/" + PathConstants.ATTENDANCES)
    public Response getStudentsAttendance(@PathParam(ParameterConstants.STUDENT_ID) final String studentId,
                                                     @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.ATTENDANCES, "studentId", studentId, headers,
                uriInfo);
    }
    

    /**
     * $$studentTranscriptAssociations$$
     * 
     * @param studentId
     *            The Id of the Student.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.STUDENT_ID + "}" + "/" + PathConstants.STUDENT_TRANSCRIPT_ASSOCIATIONS)
    public Response getStudentTranscriptAssociations(@PathParam(ParameterConstants.STUDENT_ID) final String studentId,
            @Context HttpHeaders headers, 
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_TRANSCRIPT_ASSOCIATIONS, "studentId", studentId, headers, uriInfo);
    }


    /**
     * $$studentTranscriptAssociations$$ - courses lookup
     * 
     * @param studentId
     *            The Id of the Student.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.STUDENT_ID + "}" + "/" + PathConstants.STUDENT_TRANSCRIPT_ASSOCIATIONS + "/" + PathConstants.COURSES)
    public Response getStudentTranscriptAssociationCourses(@PathParam(ParameterConstants.STUDENT_ID) final String studentId,
            @Context HttpHeaders headers, 
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_TRANSCRIPT_ASSOCIATIONS, "studentId", studentId, "courseId", ResourceNames.COURSES, headers, uriInfo);
    }


    /**
     * $$studentParentAssociations$$
     *
     * @param studentId The Id of the Student.
     * @param headers   HTTP Request Headers
     * @param uriInfo   URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.STUDENT_ID + "}" + "/" + PathConstants.STUDENT_PARENT_ASSOCIATIONS)
    public Response getStudentParentAssociations(@PathParam(ParameterConstants.STUDENT_ID) final String studentId,
                                                 @Context HttpHeaders headers,
                                                 @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_PARENT_ASSOCIATIONS, "studentId", studentId, headers, uriInfo);
    }


    /**
     * $$studentParentAssociations$$ - parent lookup
     *
     * @param studentId The Id of the Student.
     * @param headers   HTTP Request Headers
     * @param uriInfo   URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.STUDENT_ID + "}" + "/" + PathConstants.STUDENT_PARENT_ASSOCIATIONS + "/" + PathConstants.PARENTS)
    public Response getStudentParentAssociationCourses(@PathParam(ParameterConstants.STUDENT_ID) final String studentId,
                                                       @Context HttpHeaders headers,
                                                       @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_PARENT_ASSOCIATIONS, "studentId", studentId, "parentId", ResourceNames.PARENTS, headers, uriInfo);
    }

    /**
     * $$studentDisciplineIncidentAssociations$$
     *
     * @param studentId The Id of the Student.
     * @param headers   HTTP Request Headers
     * @param uriInfo   URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.STUDENT_ID + "}" + "/" + PathConstants.STUDENT_DISCIPLINE_INCIDENT_ASSOCIATIONS)
    public Response getStudentDisciplineIncidentAssociations(@PathParam(ParameterConstants.STUDENT_ID) final String studentId,
                                                             @Context HttpHeaders headers,
                                                             @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_DISCIPLINE_INCIDENT_ASSOCIATIONS, "studentId", studentId, headers, uriInfo);
    }

    /**
     * $$studentDisciplineIncidentAssociations$$
     *
     * @param studentId The Id of the Student.
     * @param headers   HTTP Request Headers
     * @param uriInfo   URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.STUDENT_ID + "}" + "/" + PathConstants.STUDENT_DISCIPLINE_INCIDENT_ASSOCIATIONS + "/" + PathConstants.DISCIPLINE_INCIDENTS)
    public Response getStudentDisciplineIncidentAssociationDisciplineIncidents(@PathParam(ParameterConstants.STUDENT_ID) final String studentId,
                                                                               @Context HttpHeaders headers,
                                                                               @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_DISCIPLINE_INCIDENT_ASSOCIATIONS, "studentId", studentId, "disciplineIncidentId", ResourceNames.DISCIPLINE_INCIDENTS, headers, uriInfo);
    }

    /**
     * Returns each $$studentCohortAssociations$$ that
     * references the given $$students$$
     * 
     * @param studentId
     *            The Id of the $$student$.
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
    @Path("{" + ParameterConstants.STUDENT_ID + "}" + "/" + PathConstants.STUDENT_COHORT_ASSOCIATIONS)
    public Response getStaffCohortAssociations(@PathParam(ParameterConstants.STUDENT_ID) final String studentId,
            @Context HttpHeaders headers, 
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_COHORT_ASSOCIATIONS, ParameterConstants.STUDENT_ID, studentId, headers, uriInfo);
    }
    

    /**
     * Returns each $$cohorts$$ associated to the given $$students$$ through
     * a $$studentCohortAssociations$$ 
     * 
     * @param studentId
     *            The Id of the $$students$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.STUDENT_ID + "}" + "/" + PathConstants.STUDENT_COHORT_ASSOCIATIONS + "/" + PathConstants.COHORTS)
    public Response getStaffCohortAssociationCohorts(@PathParam(ParameterConstants.STUDENT_ID) final String studentId,
            @Context HttpHeaders headers, 
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_COHORT_ASSOCIATIONS, ParameterConstants.STUDENT_ID, studentId, 
                ParameterConstants.COHORT_ID, ResourceNames.COHORTS, headers, uriInfo);
    }

    /**
     * Returns each $$studentProgramAssociations$$ that
     * references the given $$students$$
     * 
     * @param studentId
     *            The Id of the Student.
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
    @Path("{" + ParameterConstants.STUDENT_ID + "}" + "/" + PathConstants.STUDENT_PROGRAM_ASSOCIATIONS)
    public Response getStudentProgramAssociations(@PathParam(ParameterConstants.STUDENT_ID) final String studentId,
            @Context HttpHeaders headers, 
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_PROGRAM_ASSOCIATIONS, "studentId", studentId, headers, uriInfo);
    }
    

    /**
     * Returns the $$programs$$ that are referenced from the $$studentsProgramAssociations$$ 
     * that references the given $$students$$.
     * 
     * @param studentId
     *            The Id of the Student.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.STUDENT_ID + "}" + "/" + PathConstants.STUDENT_PROGRAM_ASSOCIATIONS + "/" + PathConstants.PROGRAMS)
    public Response getStudentProgramAssociationPrograms(@PathParam(ParameterConstants.STUDENT_ID) final String studentId,
            @Context HttpHeaders headers, 
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_PROGRAM_ASSOCIATIONS, "studentId", studentId, 
                "programId", ResourceNames.PROGRAMS, headers, uriInfo);
    }

}
