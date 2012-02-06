package org.slc.sli.entity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * EntityManager which engages with the API client to build "logical" entity graphs to be leveraged
 * by the MVC framework.
 * Adapted from Sravan Vankina's API artifacts in org.slc.sli.client, as well as David Wu's
 * GenericEntity kickoff.
 * 
 * @author Sravan Vankina svankina@wgen.net
 * @author David Wu dwu@wgen.net
 * @author Robert Bloh rbloh@wgen.net
 * 
 */
@Component
public class EntityManager {
    
    private static Logger log = LoggerFactory.getLogger(EntityManager.class);
    
    // API Session Id
    private static final String API_SESSION_KEY = "sessionId";
    
    // Mock Data Files
    private static final String MOCK_DATA_DIRECTORY = "mock_data/";
    private static final String MOCK_ENROLLMENT_FILE = "school.json";
    private static final String MOCK_STUDENTS_FILE = "student.json";
    private static final String MOCK_PROGRAMS_FILE = "student_program_association.json";
    private static final String MOCK_ASSESSMENT_METADATA_FILE = "assessment_meta_data.json";
    private static final String MOCK_ASSESSMENTS_FILE = "assessment.json";
    private static final String MOCK_ATTENDANCE_FILE = "attendance.json";
    
    public EntityManager() {
        
    }
    
    /**
     * Get the list of school entities identified by the school id list and authorized for the
     * security token
     * 
     * @param token
     *            - the principle authentication token
     * @param schoolIds
     *            - the school id list
     * @return schoolList
     *         - the school entity list
     */
    public List<SimpleEntity> getSchools(final String token, List<String> schoolIds) {
        return this.getEntities(token, getResourceFilePath(MOCK_DATA_DIRECTORY + token + "/" + MOCK_ENROLLMENT_FILE), schoolIds);
    }
    
    /**
     * Get the school entity identified by the school id and authorized for the security token
     * 
     * @param token
     *            - the principle authentication token
     * @param schoolId
     *            - the school id
     * @return school
     *         - the school entity
     */
    public SimpleEntity getSchool(final String token, String schoolId) {
        return this.getEntity(token, getResourceFilePath(MOCK_DATA_DIRECTORY + token + "/" + MOCK_ENROLLMENT_FILE), schoolId);
    }
    
    /**
     * Get the list of student entities identified by the student id list and authorized for the
     * security token
     * 
     * @param token
     *            - the principle authentication token
     * @param studentIds
     *            - the student id list
     * @return studentList
     *         - the student entity list
     */
    public List<SimpleEntity> getStudents(final String token, List<String> studentIds) {
        return this.getEntities(token, getResourceFilePath(MOCK_DATA_DIRECTORY + token + "/" + MOCK_STUDENTS_FILE), studentIds);
    }
    
    /**
     * Get the student entity identified by the student id and authorized for the security token
     * 
     * @param token
     *            - the principle authentication token
     * @param studentId
     *            - the student id
     * @return student
     *         - the student entity
     */
    public SimpleEntity getStudent(final String token, String studentId) {
        return this.getEntity(token, getResourceFilePath(MOCK_DATA_DIRECTORY + token + "/" + MOCK_STUDENTS_FILE), studentId);
    }
    
    /**
     * Get the list of student program entities identified by the student id list and authorized for the
     * security token
     * 
     * @param token
     *            - the principle authentication token
     * @param studentIds
     *            - the student id list
     * @return programList
     *         - the program entity list
     */
    public List<SimpleEntity> getPrograms(final String token, List<String> studentIds) {
        return this.getEntities(token, getResourceFilePath(MOCK_DATA_DIRECTORY + token + "/" + MOCK_PROGRAMS_FILE), studentIds);
    }
    
    /**
     * Get the student program entity identified by the student id and authorized for the security token
     * 
     * @param token
     *            - the principle authentication token
     * @param studentId
     *            - the student id
     * @return program
     *         - the program entity
     */
    public SimpleEntity getProgram(final String token, String studentId) {
        return this.getEntity(token, getResourceFilePath(MOCK_DATA_DIRECTORY + token + "/" + MOCK_PROGRAMS_FILE), studentId);
    }
    
    /**
     * Get the list of assessment metadata entities
     * 
     * @return assessmentMetadataList
     *         - the assessment metadata entity list
     */
    public List<SimpleEntity> getAssessmentMetadata() {
        return this.getEntities("", getResourceFilePath(MOCK_DATA_DIRECTORY + MOCK_ASSESSMENT_METADATA_FILE), null);
    }
    
    /**
     * Get the list of assessment entities identified by the student id list and authorized for the
     * security token
     * 
     * @param token
     *            - the principle authentication token
     * @param studentIds
     *            - the student id list
     * @return assessmentList
     *         - the assessment entity list
     */
    public List<SimpleEntity> getAssessments(final String token, List<String> studentIds) {
        return this.getEntities(token, getResourceFilePath(MOCK_DATA_DIRECTORY + token + "/" + MOCK_ASSESSMENTS_FILE), studentIds);
    }
    
    /**
     * Get the assessment entity identified by the student id and authorized for the security token
     * 
     * @param token
     *            - the principle authentication token
     * @param studentId
     *            - the student id
     * @return assessment
     *         - the assessment entity
     */
    public SimpleEntity getAssessment(final String token, String studentId) {
        return this.getEntity(token, getResourceFilePath(MOCK_DATA_DIRECTORY + token + "/" + MOCK_ASSESSMENTS_FILE), studentId);
    }
    
    /**
     * Get the list of entities identified by the entity id list and authorized for the security token
     * 
     * @param token
     *            - the principle authentication token
     * @param filePath
     *            - the file containing the JSON entities representation
     * @param entityIds
     *            - the list of entity ids
     * @return entityList
     *         - the entity list
     */
    public List<SimpleEntity> getEntities(final String token, String filePath, List<String> entityIds) {
        
        // Get all the entities for the user identified by token
        List<SimpleEntity> entities = fromFile(filePath);
        
        // Filter entities according to the entity id list
        List<SimpleEntity> filteredEntities = new ArrayList<SimpleEntity>();
        if (entityIds != null) {
            for (SimpleEntity entity : entities) {
                if (entityIds.contains(entity.get("id"))) {
                    filteredEntities.add(entity);
                }
            }
        } else {
            filteredEntities.addAll(entities);
        }
        
        return filteredEntities;
    }
    
    /**
     * Get the entity identified by the entity id and authorized for the security token
     * 
     * @param token
     *            - the principle authentication token
     * @param filePath
     *            - the file containing the JSON entities representation
     * @param id
     *            - the entity id
     * @return entity
     *         - the entity entity
     */
    public SimpleEntity getEntity(final String token, String filePath, String id) {
        
        // Get all the entities for the user identified by token
        List<SimpleEntity> entities = fromFile(filePath);
        
        // Select entity identified by id
        if (id != null) {
            for (SimpleEntity entity : entities) {
                if (id.equals(entity.get("id"))) {
                    return entity;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Saves an entity list to the specified REST API url using its JSON representation
     * 
     * @param token
     *            - the principle authentication token
     * @param url
     *            - the API url to persist the entity list JSON string representation
     * @param entityList
     *            - the generic entity list
     */
    public void toAPI(String token, String url, List<SimpleEntity> entityList) {
        
        // TODO - Implement when supported by REST API
        
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
    public List<SimpleEntity> fromAPI(String token, String url) {
        List<SimpleEntity> entityList = new ArrayList<SimpleEntity>();
        
        // Invoke REST API
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(API_SESSION_KEY, token);
        HttpEntity httpEntity = new HttpEntity(headers);
        
        log.debug("Accessing API: " + url);  
        
        HttpEntity<String> apiResponse = template.exchange(url, HttpMethod.GET, httpEntity, String.class);

        // Parse JSON
        Gson gson = new Gson();
        List<SimpleEntity> maps = gson.fromJson(apiResponse.getBody(), new ArrayList<SimpleEntity>().getClass());
            
        for (Map<String, Object> map : maps) {
            entityList.add(new SimpleEntity(map));
        }

        return entityList;
    }
    
    /**
     * Saves an entity list to the specified file using its JSON representation
     * 
     * @param filePath
     *            - the file path to persist the entity list JSON string representation
     * @param entityList
     *            - the generic entity list
     */
    public void toFile(String filePath, List<SimpleEntity> entityList) {
        
        BufferedWriter writer = null;
        
        try {
            
            // Write JSON file
            writer = new BufferedWriter(new FileWriter(filePath));
            Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
            String json = gson.toJson(entityList, new ArrayList<Map>().getClass());
            writer.write(json);
            
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
    
    /**
     * Retrieves an entity list from the specified file
     * and instantiates from its JSON representation
     * 
     * @param filePath
     *            - the file path to persist the view component XML string representation
     * @return entityList
     *         - the generic entity list
     */
    public List<SimpleEntity> fromFile(String filePath) {
        List<SimpleEntity> entityList = new ArrayList<SimpleEntity>();
        
        BufferedReader reader = null;
        
        try {
            
            // Read JSON file
            reader = new BufferedReader(new FileReader(filePath));
            StringBuffer jsonBuffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
            
            // Parse JSON
            Gson gson = new Gson();
            List<Map> maps = gson.fromJson(jsonBuffer.toString(), new ArrayList<Map>().getClass());
            
            for (Map<String, Object> map : maps) {
                entityList.add(new SimpleEntity(map));
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        
        return entityList;
    }
    
    /**
     * Gets the file path of a specified web resource.
     * 
     * @return filePath
     *         possible object is {@link String }
     * 
     */
    public String getResourceFilePath(String resourceName) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(resourceName);
        return url.getFile();
    }
    
    public static void main(String[] arguments) {
        
        log.info("Starting EntityManager...");
        
        try {
            String token = "rbraverman";
            
            EntityManager entityManager = new EntityManager();
            
            List<SimpleEntity> students = entityManager.getStudents(token, null);
            
            log.info(students.toString());
            
            entityManager.toFile("testStudents.json", students);
            
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
        
        log.info("Finished EntityManager.");
        
    }
    
}
