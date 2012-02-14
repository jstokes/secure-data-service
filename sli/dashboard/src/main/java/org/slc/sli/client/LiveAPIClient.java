package org.slc.sli.client;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import org.slc.sli.entity.GenericEntity;
import org.slc.sli.entity.assessmentmetadata.AssessmentMetaData;
import org.slc.sli.util.Constants;
import org.slc.sli.util.SecurityUtil;



/**
 *
 * API Client class used by the Dashboard to make calls to the API service.
 *
 * @author svankina
 *
 */
public class LiveAPIClient implements APIClient {

    private RESTClient restClient;
    private Gson gson;

    // For now, the live client will use the mock client for api calls not yet implemented
    private MockAPIClient mockClient;

    /**
     * Getter and Setter used by Spring to instantiate the live/test api class
     * @return
     */
    public RESTClient getRestClient() {
        return restClient;
    }
    public void setRestClient(RESTClient restClient) {
        this.restClient = restClient;
    }

    public LiveAPIClient() {
        mockClient = new MockAPIClient();
        gson = new Gson();
    }

    @Override
    public List<GenericEntity> getSchools(String token, List<String> schoolIds) {
        
        String teacherId = getId(token);
        GenericEntity[] sections = getSectionsForTeacher(teacherId, token);
        GenericEntity[] schools = getSchoolsForSection(sections, token);

        List<GenericEntity> schoolList = new ArrayList<GenericEntity>();
        
        for (GenericEntity school : schools) {
            schoolList.add(school);
        }
        return schoolList;
    }

    @Override
    public List<GenericEntity> getStudents(final String token, List<String> urls) {
        if (urls == null) {
            return null;
        }
        
        List<GenericEntity> students = new ArrayList<GenericEntity>();
        
        int i = 0;
        for (String url: urls) {
            students.add(createEntityFromJson(getStudent(url, token)));
        }

        return students;
    }

    
    private String getStudent(String id, String token) {
        String url = Constants.API_SERVER_URI + "/students/" + id;
        try {
            return restClient.makeJsonRequestWHeaders(url, token);
        } catch (org.springframework.web.client.HttpClientErrorException ex) {
            throw ex;
        }
    }
    
    private GenericEntity getSchool(String id, String token) {

        String url = Constants.API_SERVER_URI + "/schools/" + id;
        GenericEntity school = createEntityFromJson(restClient.makeJsonRequestWHeaders(url, token));
        return school;
    }

    private String[] getStudentIdsForSection(String id, String token) {
        String url = Constants.API_SERVER_URI + "/student-section-associations/" + id + "/targets";

        List<GenericEntity> responses = fromAPI(url, token);
        
        String[] studentIds = new String[responses.size()];

        int i = 0;
        for (GenericEntity response : responses) {
            studentIds[i++] = (String) (response.get(Constants.ATTR_ID));
        }
        return studentIds;
    }
    
    
    private GenericEntity getSection(String id, String token) {
        String url = Constants.API_SERVER_URI + "/sections/" + id;
        GenericEntity section = createEntityFromJson(restClient.makeJsonRequestWHeaders(url, token));
        section.put(Constants.ATTR_STUDENT_UIDS, getStudentIdsForSection(id, token));

        if (section.get("sectionName") == null) { section.put("sectionName", section.get("uniqueSectionCode")); }

        return section;
    }

    private GenericEntity getCourse(String id, String token) {
        String url = Constants.API_SERVER_URI + "/courses/" + id;
        GenericEntity course = createEntityFromJson(restClient.makeJsonRequestWHeaders(url, token));
        return course;
    }

    private GenericEntity getEducationalOrganization(String id, String token) {
        String url = Constants.API_SERVER_URI + "/educationOrganizations/" + id;
        GenericEntity edOrg = createEntityFromJson(restClient.makeJsonRequestWHeaders(url, token));
        return edOrg;
    }

    @Override
    public List<GenericEntity> getAssessments(final String token, List<String> studentIds) {
        return mockClient.getAssessments(getUsername(), studentIds);
    }
    
    @Override
    public List<GenericEntity> getCustomData(final String token, String key) {
        return mockClient.getCustomData(getUsername(), key);
    }

    @Override
    public AssessmentMetaData[] getAssessmentMetaData(final String token) {
        return mockClient.getAssessmentMetaData(getUsername());
    }
    @Override
    public List<GenericEntity> getPrograms(final String token, List<String> studentIds) {
        return mockClient.getPrograms(getUsername(), studentIds);
    }

    @Override
    public List<GenericEntity> getAssociatedEducationalOrganizations(final String token, GenericEntity school) {
        String url = Constants.API_SERVER_URI + "/educationOrganization-school-associations/" + school.get(Constants.ATTR_ID) + "/targets";
        List<GenericEntity> responses = fromAPI(url, token);
        List<GenericEntity> edOrgs = new ArrayList<GenericEntity>();
        for (GenericEntity response : responses) {
            edOrgs.add(getEducationalOrganization(parseId((Map) (response.get(Constants.ATTR_LINK))), token));
        }
        return edOrgs;
    }

    @Override
    public List<GenericEntity> getParentEducationalOrganizations(final String token, GenericEntity edOrg) {
        String url = Constants.API_SERVER_URI + "/educationOrganization-associations/" + edOrg.get(Constants.ATTR_ID);
        List<GenericEntity> responses = fromAPI(url, token);
        List<GenericEntity> edOrgs = new ArrayList<GenericEntity>();
        // For every association, and find the ones that this ed org is a child, and follow the parent 
        for (GenericEntity response : responses) {
            try {
                String assLink = (String) (((Map) (response.get(Constants.ATTR_LINK))).get(Constants.ATTR_HREF));
                JSONObject assResponse = new JSONObject(restClient.makeJsonRequestWHeaders(assLink, token));
                String childId = assResponse.optString(Constants.ATTR_ED_ORG_CHILD_ID);
                if (childId != null && childId.equals(edOrg.get(Constants.ATTR_ID))) {
                    String parentId = assResponse.optString(Constants.ATTR_ED_ORG_PARENT_ID);
                    if (parentId != null) {
                        edOrgs.add(getEducationalOrganization(parentId, token));
                    }
                }
            } catch (JSONException e) {
                throw new RuntimeException("malformed json from educationOrganization-associations api call.");
            }
        }
        return edOrgs;
    }

    private String getId(String token) {

        // Make a call to the /home uri and retrieve id from there
        String returnValue = "";
        String url = Constants.API_SERVER_URI + "/home";
        GenericEntity response = createEntityFromJson(restClient.makeJsonRequestWHeaders(url, token));
        
        for (Map link : (List<Map>) (response.get(Constants.ATTR_LINKS))) {
            if (link.get(Constants.ATTR_REL).equals(Constants.ATTR_SELF)) {
                returnValue = parseId(link);
            }
        }
        
        return returnValue;
    }

    private String parseId(Map link) {
        String returnValue;
        int index = ((String) (link.get(Constants.ATTR_HREF))).lastIndexOf("/");
        returnValue = ((String) (link.get(Constants.ATTR_HREF))).substring(index + 1);
        return returnValue;
    }
    
    private GenericEntity[] getSectionsForTeacher(String id, String token) {
        String url = Constants.API_SERVER_URI + "/teacher-section-associations/" + id + "/targets";
        List<GenericEntity> responses = fromAPI(url, token);
        List<GenericEntity> sections = new ArrayList<GenericEntity>();
        
        for (GenericEntity response : responses) {
            sections.add(getSection(parseId(((Map) (response.get(Constants.ATTR_LINK)))), token));
        }

        // FIXME: converting like this because we suck.
        GenericEntity[] sections2 = new GenericEntity[sections.size()];
        int index = 0;
        for (GenericEntity s : sections) {
            sections2[index++] = s;
        }

        return sections2;
    }

        
    private GenericEntity[] getSchoolsForSection(GenericEntity[] sections, String token) {
        // collect associated course first.
        HashMap<String, GenericEntity> courseMap = new HashMap<String, GenericEntity>();
        HashMap<String, String> sectionIDToCourseIDMap = new HashMap<String, String>();
        getCourseSectionsMappings(sections, token, courseMap, sectionIDToCourseIDMap);

        // now collect associated schools.
        HashMap<String, GenericEntity> schoolMap = new HashMap<String, GenericEntity>();
        HashMap<String, String> sectionIDToSchoolIDMap = new HashMap<String, String>();
        getSchoolSectionsMappings(sections, token, schoolMap, sectionIDToSchoolIDMap);

        
        
        // TODO: Hack for Sprint 3.1 demo
        // @@@ Begin Hack
        // The associations called above won't work because API and ingestion teams have not 
        // implemented the direct linke *or* the temporary association. So, we're faking a
        // course association here now. 
        // First, create a fake course and associate it to all sections
        GenericEntity fakeCourse = new GenericEntity(); 
        fakeCourse.put("courseTitle", "Dummy Course");
        String dummmyCourseID = "dummy course id";
        courseMap.put(dummmyCourseID, fakeCourse);
        for (int j = 0; j < sections.length; j++) {
            sectionIDToCourseIDMap.put(sections[j].get(Constants.ATTR_ID).toString(), dummmyCourseID);
            fakeCourse.appendToList(Constants.ATTR_SECTIONS, sections[j]);
        }
        // Then create schools and associate the first one to all sections
        String teacherId = getId(token); 
        String url = Constants.API_SERVER_URI + "/teacher-school-associations/" + teacherId + "/targets"; 
        List<GenericEntity> responses = fromAPI(url, token);
        for (int i = 0; i < responses.size(); i++) {
            GenericEntity response = responses.get(i);
            String schoolId = parseId((Map) (response.get(Constants.ATTR_LINK)));
            GenericEntity school = getSchool(schoolId, token); 
            schoolMap.put(schoolId, school);
            if (i == 0) {
                for (int j = 0; j < sections.length; j++) {
                    sectionIDToSchoolIDMap.put(sections[j].get(Constants.ATTR_ID).toString(), schoolId);
                }
            }
        }
        // @@@ End Hack for Sprint 3.1 demo
        
        
        
        // Now associate course and school.
        // There is no direct course-school association in ed-fi, so in dashboard
        // the "course-school" association is defined as follows:
        // course C is associated with school S if there exists a section X s.t. C is associated
        // with X and S is associated with X.
        HashMap<String, HashSet<String> > schoolIDToCourseIDMap = new HashMap<String, HashSet<String> >();
        for (int i = 0; i < sections.length; i++) {
            GenericEntity section = sections[i];
            if (sectionIDToSchoolIDMap.containsKey(section.get(Constants.ATTR_ID))
                && sectionIDToCourseIDMap.containsKey(section.get(Constants.ATTR_ID))) {
                String schoolId = sectionIDToSchoolIDMap.get(section.get(Constants.ATTR_ID));
                String courseId = sectionIDToCourseIDMap.get(section.get(Constants.ATTR_ID));
                if (!schoolIDToCourseIDMap.containsKey(schoolId)) { schoolIDToCourseIDMap.put(schoolId, new HashSet<String> ()); }
                schoolIDToCourseIDMap.get(schoolId).add(courseId);
            }
        }

        // now create the generic entity
        for (String schoolId : schoolIDToCourseIDMap.keySet()) {
            for (String courseId : schoolIDToCourseIDMap.get(schoolId)) {
                GenericEntity s = schoolMap.get(schoolId);
                GenericEntity c = courseMap.get(courseId);
                s.appendToList(Constants.ATTR_COURSES, c);
            }
        }
        
        GenericEntity[] retVal = new GenericEntity[schoolMap.values().size()];
        return schoolMap.values().toArray(retVal);
    }

    private void getCourseSectionsMappings(GenericEntity[] sections,
                                           String token,
                                           Map<String, GenericEntity> courseMap,
                                           Map<String, String> sectionIDToCourseIDMap) { 
        for (int i = 0; i < sections.length; i++) {
            GenericEntity section = sections[i];
            // TODO: This API team is going to remove this call when they have implemented direct course  
            //       reference from the section entity. This "parseId(response.getLink()" expression
            //       below should then be replaced by looking up the schoolReferenceId in the section entity
            //       (refer to ed-fi).  
            String url = Constants.API_SERVER_URI + "/course-section-associations/" + section.get(Constants.ATTR_ID) + "/targets";
            List<GenericEntity> responses = fromAPI(url, token);
            if (responses.size() > 0) {
                GenericEntity response = responses.get(0); // there should be only one.
                GenericEntity course = getCourse(parseId((Map) (response.get(Constants.ATTR_LINK))), token);
                if (!courseMap.containsKey(course.get(Constants.ATTR_ID))) {
                    courseMap.put((String) (course.get(Constants.ATTR_ID)), course);
                }
                course = courseMap.get(course.get(Constants.ATTR_ID));
                course.appendToList(Constants.ATTR_SECTIONS, section);
                sectionIDToCourseIDMap.put((String) (section.get(Constants.ATTR_ID)), (String) (course.get(Constants.ATTR_ID)));
            }
        }
    }
    private void getSchoolSectionsMappings(GenericEntity[] sections,
                                           String token,
                                           Map<String, GenericEntity> schoolMap,
                                           Map<String, String> sectionIDToSchoolIDMap) { 
        for (int i = 0; i < sections.length; i++) {
            GenericEntity section = sections[i];
            // TODO: This API team is going to remove this call when they have implemented direct school  
            //       reference from the section entity. This "parseId(response.getLink()" expression
            //       below should then be replaced by looking up the schoolReferenceId in the section entity
            //       (refer to ed-fi).  
            String url = Constants.API_SERVER_URI + "/section-school-associations/" + section.get(Constants.ATTR_ID) + "/targets";
            List<GenericEntity> responses = fromAPI(url, token);
            if (responses.size() > 0) {
                GenericEntity response = responses.get(0); // there should be only one.
                GenericEntity school = getSchool(parseId((Map) (response.get(Constants.ATTR_LINK))), token);
                if (!schoolMap.containsKey(school.get(Constants.ATTR_ID))) {
                    schoolMap.put((String) (school.get(Constants.ATTR_ID)), school);
                }
                sectionIDToSchoolIDMap.put((String) (section.get(Constants.ATTR_ID)), (String) (school.get(Constants.ATTR_ID)));
            }
        }
    }
        
    private String getUsername() {
        return SecurityUtil.getPrincipal().getUsername().replace(" ", "");
    }

    
    /**
     * Creates a generic entity from a json string
     * 
     * @param json
     * @param type
     * @return
     */
    private GenericEntity createEntityFromJson(String json) {
        
        GenericEntity e = gson.fromJson(json, GenericEntity.class);

        return e;
    }
    

    /**
     * Retrieves an entity list from the specified API url
     * and instantiates from its JSON representation
     * 
     * @param token
     *            - the principle authentication token
     * @param url
     *            - the API url to retrieve the entity list JSON string representation
     * @return entityList
     *         - the generic entity list
     */
    public List<GenericEntity> fromAPI(String url, String token) {
        List<GenericEntity> entityList = new ArrayList<GenericEntity>();

        // Parse JSON
        List<Map> maps = gson.fromJson(restClient.makeJsonRequestWHeaders(url, token), new ArrayList<Map>().getClass());
            
        for (Map<String, Object> map : maps) {
            entityList.add(new GenericEntity(map));
        }

        return entityList;
    }

}
