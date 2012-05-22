package org.slc.sli.client;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slc.sli.api.client.Entity;
import org.slc.sli.api.client.SLIClient;
import org.slc.sli.entity.ConfigMap;
import org.slc.sli.entity.GenericEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This client will use the SDK client to communicate with the SLI API.
 * 
 * @author dwalker
 *
 */
public class SDKAPIClient implements APIClient{

    private static final Logger LOGGER = LoggerFactory.getLogger(SDKAPIClient.class);
    
    private static final String SDK_EDORGS_URL = "/educationOrganizations/";

    // base urls
    private static final String STAFF_URL = "/v1/staff/";
    private static final String EDORGS_URL = "/v1/educationOrganizations/";
    private static final String SCHOOLS_URL = "/v1/schools";
    private static final String SECTIONS_URL = "/v1/sections/";
    private static final String STUDENTS_URL = "/v1/students/";
    private static final String TEACHERS_URL = "/v1/teachers/";
    private static final String HOME_URL = "/v1/home/";
    private static final String ASSMT_URL = "/v1/assessments/";
    private static final String SESSION_URL = "/v1/sessions/";
    private static final String STUDENT_ASSMT_ASSOC_URL = "/v1/studentAssessmentAssociations/";
    private static final String STUDENT_SECTION_GRADEBOOK = "/v1/studentSectionGradebookEntries";
    private static final String STUDENT_ACADEMIC_RECORD_URL = "/v1/studentAcademicRecords";

    // resources to append to base urls
    private static final String STAFF_EDORG_ASSOC = "/staffEducationOrgAssignmentAssociations/educationOrganizations";
    private static final String ATTENDANCES = "/attendances";
    private static final String STUDENT_SECTION_ASSOC = "/studentSectionAssociations";
    private static final String TEACHER_SECTION_ASSOC = "/teacherSectionAssociations";
    private static final String STUDENT_ASSMT_ASSOC = "/studentAssessmentAssociations";
    private static final String SECTIONS = "/sections";
    private static final String STUDENTS = "/students";
    private static final String STUDENT_TRANSCRIPT_ASSOC = "/studentTranscriptAssociations";
    private static final String CUSTOM_DATA = "/custom";

    // link names
    private static final String ED_ORG_LINK = "getEducationOrganization";
    private static final String SCHOOL_LINK = "getSchool";
    private static final String STUDENT_SCHOOL_ASSOCIATIONS_LINK = "getStudentSchoolAssociations";

    // attributes
    private static final String EDORG_SLI_ID_ATTRIBUTE = "edOrgSliId";
    private static final String EDORG_ATTRIBUTE = "edOrg";

    /**
     * Dashboard client to API
     */
    APIClient liveApiClient;
    
    public APIClient getLiveApiClient() {
        return liveApiClient;
    }

    public void setLiveApiClient(APIClient liveApiClient) {
        this.liveApiClient = liveApiClient;
    }

    /**
     * SDK Client to API
     */
    SLIClient sdkClient;
    
    public SLIClient getSdkClient() {
        return sdkClient;
    }

    public void setSdkClient(SLIClient sdkClient) {
        this.sdkClient = sdkClient;
    }

    /**
     * SLI Grace Period for Historical Data Access
     */
    private String gracePeriod;

    public void setGracePeriod(String gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public String getGracePeriod() {
        return this.gracePeriod;
    }

    @Override
    public GenericEntity getStaffInfo(String token) {
        // TODO Auto-generated method stub
        return liveApiClient.getStaffInfo(token);
    }

    @Override
    public ConfigMap getEdOrgCustomData(String token, String id) {
        ConfigMap configMap = null;
        try {
            List entityList = new ArrayList();
            sdkClient.read(entityList, SDK_EDORGS_URL + id + CUSTOM_DATA, ConfigMap.class);
            if (entityList.size() > 0) {
                configMap = (ConfigMap) entityList.get(0);
            }
        } catch (URISyntaxException e) {
            LOGGER.error("Exception occurred", e);
        } catch (MalformedURLException e) {
            LOGGER.error("Exception occurred", e);
        }
        return configMap;
    }

    @Override
    public void putEdOrgCustomData(String token, String id, ConfigMap configMap) {
        try {
            Map<String,Object> entityMap = new HashMap<String,Object>();
            entityMap.put("config", configMap.getConfig());
            Entity configMapEntity = new GenericEntity(entityMap);
            sdkClient.create(SDK_EDORGS_URL + id + CUSTOM_DATA, configMapEntity);
        } catch (URISyntaxException e) {
            LOGGER.error("Exception occurred", e);
        } catch (MalformedURLException e) {
            LOGGER.error("Exception occurred", e);
        }
    }

    @Override
    public List<GenericEntity> getSchools(String token, List<String> schoolIds) {
        // TODO Auto-generated method stub
        return liveApiClient.getSchools(token, schoolIds);
    }

    @Override
    public List<GenericEntity> getStudents(String token, Collection<String> studentIds) {
        // TODO Auto-generated method stub
        return liveApiClient.getStudents(token, studentIds);
    }

    @Override
    public GenericEntity getStudent(String token, String id) {
        // TODO Auto-generated method stub
        return liveApiClient.getStudent(token, id);
    }

    @Override
    public List<GenericEntity> getStudentAssessments(String token, String studentId) {
        // TODO Auto-generated method stub
        return liveApiClient.getStudentAssessments(token, studentId);

    }

    @Override
    public List<GenericEntity> getAssessments(String token, List<String> assessmentIds) {
        // TODO Auto-generated method stub
        return liveApiClient.getAssessments(token, assessmentIds);
    }

    @Override
    public List<GenericEntity> getStudentAttendance(String token, String studentId, String start, String end) {
        // TODO Auto-generated method stub
        return liveApiClient.getStudentAttendance(token, studentId, start, end);
    }

    @Override
    public GenericEntity getParentEducationalOrganization(String token, GenericEntity educationalOrganization) {
        // TODO Auto-generated method stub
        return liveApiClient.getParentEducationalOrganization(token, educationalOrganization);
    }

    @Override
    public List<GenericEntity> getParentEducationalOrganizations(String token,
            List<GenericEntity> educationalOrganizations) {
        // TODO Auto-generated method stub
        return liveApiClient.getParentEducationalOrganizations(token, educationalOrganizations);
    }

    @Override
    public List<GenericEntity> getStudentEnrollment(String token, GenericEntity student) {
        // TODO Auto-generated method stub
        return liveApiClient.getStudentEnrollment(token, student);
    }

    @Override
    public List<GenericEntity> getStudentsWithGradebookEntries(String token, String sectionId) {
        // TODO Auto-generated method stub
        return liveApiClient.getStudentsWithGradebookEntries(token, sectionId);
    }

    @Override
    public List<GenericEntity> getStudentsWithSearch(String token, String firstName, String lastName) {
        // TODO Auto-generated method stub
        return liveApiClient.getStudentsWithSearch(token, firstName, lastName);
    }

    @Override
    public GenericEntity getStudentWithOptionalFields(String token, String studentId, List<String> optionalFields) {
        // TODO Auto-generated method stub
        return liveApiClient.getStudentWithOptionalFields(token, studentId, optionalFields);
    }

    @Override
    public List<GenericEntity> getCourses(String token, String studentId, Map<String, String> params) {
        // TODO Auto-generated method stub
        return liveApiClient.getCourses(token, studentId, params);
    }

    @Override
    public List<GenericEntity> getStudentTranscriptAssociations(String token, String studentId,
            Map<String, String> params) {
        // TODO Auto-generated method stub
        return liveApiClient.getStudentTranscriptAssociations(token, studentId, params);
    }

    @Override
    public List<GenericEntity> getSections(String token, String studentId, Map<String, String> params) {
        // TODO Auto-generated method stub
        return liveApiClient.getSections(token, studentId, params);
    }

    @Override
    public GenericEntity getEntity(String token, String type, String id, Map<String, String> params) {
        // TODO Auto-generated method stub
        return liveApiClient.getEntity(token, type, id, params);
    }

    @Override
    public List<GenericEntity> getEntities(String token, String type, String id, Map<String, String> params) {
        // TODO Auto-generated method stub
        return liveApiClient.getEntities(token, type, id, params);
    }

    @Override
    public List<GenericEntity> getStudentSectionGradebookEntries(String token, String studentId,
            Map<String, String> params) {
        // TODO Auto-generated method stub
        return liveApiClient.getStudentSectionGradebookEntries(token, studentId, params);
    }

    @Override
    public List<GenericEntity> getStudents(String token, String sectionId, List<String> studentIds) {
        // TODO Auto-generated method stub
        return liveApiClient.getStudents(token, sectionId, studentIds);
    }

    @Override
    public GenericEntity getTeacherForSection(String sectionId, String token) {
        // TODO Auto-generated method stub
        return liveApiClient.getTeacherForSection(sectionId, token);
    }

    @Override
    public GenericEntity getHomeRoomForStudent(String studentId, String token) {
        // TODO Auto-generated method stub
        return liveApiClient.getHomeRoomForStudent(studentId, token);
    }

    @Override
    public GenericEntity getSession(String token, String sessionId) {
        // TODO Auto-generated method stub
        return liveApiClient.getSession(token, sessionId);
    }

    @Override
    public List<GenericEntity> getSessions(String token) {
        // TODO Auto-generated method stub
        return liveApiClient.getSessions(token);
    }

    @Override
    public List<GenericEntity> getSessionsByYear(String token, String schoolYear) {
        // TODO Auto-generated method stub
        return liveApiClient.getSessionsByYear(token, schoolYear);
    }

    @Override
    public String sortBy(String url, String sortBy) {
        // TODO Auto-generated method stub
        return liveApiClient.sortBy(url, sortBy);
    }

    @Override
    public String sortBy(String url, String sortBy, String sortOrder) {
        // TODO Auto-generated method stub
        return liveApiClient.sortBy(url, sortBy, sortOrder);
    }

    @Override
    public GenericEntity getAcademicRecord(String token, Map<String, String> params) {
        // TODO Auto-generated method stub
        return liveApiClient.getAcademicRecord(token, params);
    }
    
}
