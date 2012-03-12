package org.slc.sli.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.slc.sli.config.ConfigUtil;
import org.slc.sli.config.Field;
import org.slc.sli.config.ViewConfig;
import org.slc.sli.entity.GenericEntity;
import org.slc.sli.util.Constants;


/**
 * PopulationManager facilitates creation of logical aggregations of EdFi entities/associations such as a
 * student summary comprised of student profile, enrollment, program, and assessment information in order to 
 * deliver the Population Summary interaction.
 *       
 * @author Robert Bloh rbloh@wgen.net
 * 
 */
public class PopulationManager {
    
    private static Logger log = LoggerFactory.getLogger(PopulationManager.class);
    
    @Autowired
    private EntityManager entityManager;
    
    public PopulationManager() { 
        
    }
    
    /**
     * Get the list of student summaries identified by the student id list and authorized for the
     * security token
     * 
     * @param token
     *            - the principle authentication token
     * @param studentIds
     *            - the student id list
     * @return studentList
     *         - the student summary entity list
     */
    public List<GenericEntity> getStudentSummaries(String token, List<String> studentIds, ViewConfig viewConfig) {
       
        // Initialize student summaries
        List<GenericEntity> studentSummaries = entityManager.getStudents(token, studentIds);
            
        // Get student programs
        List<GenericEntity> studentPrograms = entityManager.getPrograms(token, studentIds);
        Map<String, Object> studentProgramMap = new HashMap<String, Object>();
        for (GenericEntity studentProgram : studentPrograms) {
            List<String> programs = (List<String>) studentProgram.get(Constants.ATTR_PROGRAMS);            
            studentProgramMap.put(studentProgram.getString(Constants.ATTR_STUDENT_ID), programs);
        }
        
        // Get student assessments
        Map<String, Object> studentAssessmentMap = new HashMap<String, Object>();
        for (String studentId : studentIds) {
            List<GenericEntity> studentAssessments = getStudentAssessments(token, studentId, viewConfig);
            studentAssessmentMap.put(studentId, studentAssessments);
        }
        Map<String, Object> studentAttendanceMap = createStudentAttendanceMap(token, studentIds);


        // Add programs, attendance, and student assessment results to summaries
        for (GenericEntity studentSummary : studentSummaries) {
            if (studentSummary == null) continue;
            String id = studentSummary.getString(Constants.ATTR_ID);
            studentSummary.put(Constants.ATTR_PROGRAMS, studentProgramMap.get(id));
            studentSummary.put(Constants.ATTR_STUDENT_ASSESSMENTS, studentAssessmentMap.get(id));
            studentSummary.put(Constants.ATTR_STUDENT_ATTENDANCES, studentAttendanceMap.get(id));
        }
        
        return studentSummaries;
    }

    public Map<String, Object> createStudentAttendanceMap(String token, List<String> studentIds) {
        // Get attendance
        Map<String, Object> studentAttendanceMap = new HashMap<String, Object>();
        for (String studentId : studentIds) {
            List<GenericEntity> studentAttendance = getStudentAttendance(token, studentId);
            
            if (studentAttendance != null && !studentAttendance.isEmpty())
                studentAttendanceMap.put(studentId, studentAttendance);
        }
        return studentAttendanceMap;
    }

    private List<GenericEntity> getStudentAttendance(String token, String studentId) {
        return entityManager.getAttendance(token, studentId);
    }


    /**
     * Get a list of assessment results for one student, filtered by assessment name
     * 
     * @param username
     * @param studentId
     * @param config
     * @return
     */
    private List<GenericEntity> getStudentAssessments(String username, String studentId, ViewConfig config) {

        // get list of assmt names from config
        List<Field> dataFields = ConfigUtil.getDataFields(config, Constants.FIELD_TYPE_ASSESSMENT);
        Set<String> assmtNames = getAssmtNames(dataFields);
        
        // get all assessments for student
        List<GenericEntity> assmts = entityManager.getStudentAssessments(username, studentId);
        
        // filter out unwanted assmts
        List<GenericEntity> filteredAssmts = new ArrayList<GenericEntity>();
        filteredAssmts.addAll(assmts);
        
        /* To do this right, we'll need all the assessments under the assmt family's name, and
         * we'll require assessment metadata for it
        for (Assessment assmt : assmts) {
            if (assmtNames.contains(assmt.getAssessmentName()))
                filteredAssmts.add(assmt);
        }
        */
        
        return filteredAssmts;
    }

    /*
     * Get names of assessments we need data for
     */
    private Set<String> getAssmtNames(List<Field> dataFields) {

        Set<String> assmtNames = new HashSet<String>();
        for (Field field : dataFields) {
            String fieldValue = field.getValue();
            assmtNames.add(fieldValue.substring(0, fieldValue.indexOf('.')));
        }
        return assmtNames;
    }

    /**
     * Get assessments from the api, given student assessment data
     * 
     * @param username
     * @param studentAssessments
     * @return
     */
    public List<GenericEntity> getAssessments(String username, List<GenericEntity> studentSummaries) {
        
        // get the list of assessment ids from the student assessments
        List<String> assmtIds = extractAssessmentIds(studentSummaries);
        
        // get the assessment objects from the api
        List<GenericEntity> assmts = entityManager.getAssessments(username, assmtIds);
        return assmts;    
    }

    /**
     * Helper method to grab the assessment ids from the student assessment results
     * 
     * @return
     */
    private List<String> extractAssessmentIds(List<GenericEntity> studentSummaries) {
        
        List<String> assmtIds = new ArrayList<String>();
        
        // loop through student summaries, grab student assessment lists
        for (GenericEntity studentSummary : studentSummaries) {
            
            List<GenericEntity> studentAssmts = (List<GenericEntity>) studentSummary.get(Constants.ATTR_STUDENT_ASSESSMENTS);
            for (GenericEntity studentAssmt : studentAssmts) {
                
                // add assessment id to the list
                String assmtId = studentAssmt.getString(Constants.ATTR_ASSESSMENT_ID);
                if (!(assmtIds.contains(assmtId))) {
                    assmtIds.add(assmtId);
                }
            }
        }
        
        return assmtIds;
    }
    
        
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    
    /**
     * Get student entity
     * @param token
     * @param studentId
     * @return
     */
    public GenericEntity getStudent(String token, String studentId) {
        return entityManager.getStudent(token, studentId);
    }
    
    /**
     * Get student with additional info for CSI panel
     * @param token
     * @param studentId
     * @return
     */
    public GenericEntity getStudentForCSIPanel(String token, String studentId) {
        return entityManager.getStudentForCSIPanel(token, studentId);
    }
    
}



