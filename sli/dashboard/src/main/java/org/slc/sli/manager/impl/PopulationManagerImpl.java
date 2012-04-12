package org.slc.sli.manager.impl;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.googlecode.ehcache.annotations.Cacheable;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.slc.sli.config.ViewConfig;
import org.slc.sli.entity.Config;
import org.slc.sli.entity.GenericEntity;
import org.slc.sli.manager.EntityManager;
import org.slc.sli.manager.PopulationManager;
import org.slc.sli.util.Constants;
import org.slc.sli.view.TimedLogic2;

/**
 * PopulationManager facilitates creation of logical aggregations of EdFi entities/associations such
 * as a
 * student summary comprised of student profile, enrollment, program, and assessment information in
 * order to
 * deliver the Population Summary interaction.
 * 
 * @author Robert Bloh rbloh@wgen.net
 * 
 */
public class PopulationManagerImpl implements PopulationManager {
    
    private static Logger log = LoggerFactory.getLogger(PopulationManagerImpl.class);
    
    @Autowired
    private EntityManager entityManager;
    
    public PopulationManagerImpl() {
        
    }

    /* (non-Javadoc)
     * @see org.slc.sli.manager.PopulationManagerI#getAssessments(java.lang.String, java.util.List)
     */
    @Override
    public List<GenericEntity> getAssessments(String token, List<GenericEntity> studentSummaries) {
        Set<GenericEntity> assessments = new TreeSet<GenericEntity>(new Comparator<GenericEntity>() {
            @Override
            public int compare(GenericEntity att1, GenericEntity att2) {
                return (att2.getString("id")).compareTo(att1.getString("id"));
            }
        });
        for (GenericEntity studentSummary : studentSummaries) {
            List<Map<String, Object>> studentAssessments = (List<Map<String, Object>>) studentSummary.get(Constants.ATTR_STUDENT_ASSESSMENTS);
            
            for (Map<String, Object> studentAssessment : studentAssessments) {
                try {
                    GenericEntity assessment = new GenericEntity((Map) studentAssessment.get("assessments"));
                    assessments.add(assessment);
                } catch (ClassCastException cce) {
                    log.warn(cce.getMessage());
                }
            }
        }
        return new ArrayList<GenericEntity>(assessments);
    }
    
    /* (non-Javadoc)
     * @see org.slc.sli.manager.PopulationManagerI#getStudentSummaries(java.lang.String, java.util.List, org.slc.sli.config.ViewConfig, java.lang.String, java.lang.String)
     */
    @Override
    public List<GenericEntity> getStudentSummaries(String token, List<String> studentIds, ViewConfig viewConfig,
            String sessionId, String sectionId) {
        
        long startTime = System.nanoTime();
        // Initialize student summaries

        List<GenericEntity> studentSummaries = entityManager.getStudents(token, sectionId, studentIds);
        log.warn("@@@@@@@@@@@@@@@@@@ Benchmark for student section view: {}", (System.nanoTime() - startTime) * 1.0e-9);
        
        // Get student programs
        List<GenericEntity> studentPrograms = entityManager.getPrograms(token, studentIds);
        Map<String, Object> studentProgramMap = new HashMap<String, Object>();
        for (GenericEntity studentProgram : studentPrograms) {
            List<String> programs = (List<String>) studentProgram.get(Constants.ATTR_PROGRAMS);
            studentProgramMap.put(studentProgram.getString(Constants.ATTR_STUDENT_ID), programs);
        }
        
        // Add programs
        for (GenericEntity studentSummary : studentSummaries) {
            if (studentSummary == null)
                continue;
            String id = studentSummary.getString(Constants.ATTR_ID);
            studentSummary.put(Constants.ATTR_PROGRAMS, studentProgramMap.get(id));

        }
        
        return studentSummaries;
    }
    
    
    /* (non-Javadoc)
     * @see org.slc.sli.manager.PopulationManagerI#getListOfStudents(java.lang.String, java.lang.Object, org.slc.sli.entity.Config.Data)
     */
    @Override
    @Cacheable(cacheName = "user.panel.data")
    public GenericEntity getListOfStudents(String token, Object sectionId, Config.Data config) {
    
        // get student summary data
        List<GenericEntity> studentSummaries = getStudentSummaries(token, null, null,
                null, (String) sectionId);
        
        // apply assmt filters
        applyAssessmentFilters(studentSummaries, config);
    
        // data enhancements
        enhanceListOfStudents(studentSummaries);
        
        GenericEntity g = new GenericEntity();
        g.put("students", studentSummaries);
        return g;
    }

    /**
     * Make enhancements that make it easier for front-end javascript to use the data
     * 
     * @param studentSummaries
     */
    private void enhanceListOfStudents(List<GenericEntity> studentSummaries) {
        
        for (GenericEntity student : studentSummaries) {
            if (student == null)
                continue;
            
            // clean out some unneeded gunk
            student.remove(Constants.ATTR_LINKS);
         
            // add full name
            Map name = (Map) student.get("name");
            String fullName = (String) name.get("firstName") + " " + (String) name.get("lastSurname");
            name.put("fullName", fullName);
            
            // xform score format
            Map studentAssmtAssocs = (Map) student.get("assessments");
            if (studentAssmtAssocs == null)
                continue;
            
            Collection<Map> assmtResults = (Collection<Map>) studentAssmtAssocs.values();
            for (Map assmtResult : assmtResults) {
                
                if (assmtResult == null)
                    continue;
                
                List<Map> scoreResults = (List<Map>) assmtResult.get("scoreResults");
                for (Map scoreResult : scoreResults) {
                    
                    String type = (String) scoreResult.get("assessmentReportingMethod");
                    String result = (String) scoreResult.get("result");
                    assmtResult.put(type, result);
                }
                
                List<Map> perfLevelsDescs = (List<Map>) assmtResult.get("performanceLevelDescriptors");
                for (Map perfLevelDesc : perfLevelsDescs) {
                    
                    String perfLevel = (String) perfLevelDesc.get("description");
                    assmtResult.put("perfLevel", perfLevel);
                    break; // only get the first one
                }
            }

            // tally up attendance data
            Map<String, Object> attendanceBody = (Map<String, Object>) student.get(Constants.ATTR_STUDENT_ATTENDANCES);
            if (attendanceBody != null) {
                
                List<Map<String, Object>> attendances = (List<Map<String, Object>>) attendanceBody.get(Constants.ATTR_STUDENT_ATTENDANCES);
                int absenceCount = 0;
                int tardyCount = 0;
                if (attendances != null) {
                    for (Map attendance : attendances) {
                    
                        String event = (String) attendance.get("attendanceEventCategory");
                    
                        if (event.contains("Absence")) {
                            absenceCount++;
                    
                        } else if (event.contains("Tardy")) {
                            tardyCount++;
                        }                        
                    }
                
                    int attendanceRate = Math.round(((float) (attendances.size() - absenceCount) / attendances.size()) * 100);
                    int tardyRate = Math.round(((float) tardyCount / attendances.size()) * 100);
                
                    attendanceBody.remove(Constants.ATTR_STUDENT_ATTENDANCES);
                    attendanceBody.put("absenceCount", absenceCount);
                    attendanceBody.put("tardyCount", tardyCount);
                    attendanceBody.put("attendanceRate", attendanceRate);
                    attendanceBody.put("tardyRate", tardyRate);
                }
            }            
            
        }
    }
    
    /**
     * Find the required assessment results according to the data configuration. Filter out the rest.
     */
    private void applyAssessmentFilters(List<GenericEntity> studentSummaries, Config.Data config) {
        
        // Loop through student summaries
        for (GenericEntity summary : studentSummaries) {
            
            // Grab the student's assmt results. Grab assmt filters from config.
            List<Map> assmtResults = (List<Map>) (summary.remove(Constants.ATTR_STUDENT_ASSESSMENTS));

            Map<String, String> assmtFilters = (Map<String, String>) (config.getParams().get("assessmentFilter"));
            if (assmtFilters == null) {
                return;
            }
            
            Map<String, Object> newAssmtResults = new LinkedHashMap<String, Object>();
            
            // Loop through assmt filters
            for (String assmtName : assmtFilters.keySet()) {
            
                String timedLogic = assmtFilters.get(assmtName);
                
                // Apply filter. Add result to student summary.
                Map assmt = applyAssessmentFilter(assmtResults, assmtName, timedLogic);
                
                //Map<String, Map> assmt2 = new LinkedHashMap<String, Map>();
                //assmt2.put(timedLogic, assmt);
                newAssmtResults.put(assmtName, assmt);
            }
            
            summary.put("assessments", newAssmtResults);
        }
    }
    
    /**
     * Filter a list of assessment results, based on the assessment name and timed logic
     * 
     * @param assmtResults
     * @param assmtName
     * @param timedLogic
     * @return
     */
    private Map applyAssessmentFilter(List<Map> assmtResults, String assmtName, String timeSlot) {
        
        // filter by assmtName
        List<Map> studentAssessmentFiltered = new ArrayList<Map>();
        for (Map assmtResult : assmtResults) {
            String family = (String) ((Map) (assmtResult.get("assessments"))).get("assessmentFamilyHierarchyName");
            if (family.contains(assmtName)) {
                studentAssessmentFiltered.add(assmtResult);
            }
        }
        
        if (studentAssessmentFiltered.size() == 0) {
            return null;
        }
        
        // call timed logic
        Map chosenAssessment = null;
        
        if (TimedLogic2.TIMESLOT_MOSTRECENTRESULT.equals(timeSlot)) {
            chosenAssessment = TimedLogic2.getMostRecentAssessment(studentAssessmentFiltered);
        //} else if (TimedLogic2.TIMESLOT_HIGHESTEVER.equals(timeSlot) && !objAssmtCode.equals("")) {
            //chosenAssessment = TimedLogic.getHighestEverObjAssmt(studentAssessmentFiltered, objAssmtCode);
        } else if (TimedLogic2.TIMESLOT_HIGHESTEVER.equals(timeSlot)) {
            chosenAssessment = TimedLogic2.getHighestEverAssessment(studentAssessmentFiltered);
//        } else if (TimedLogic2.TIMESLOT_MOSTRECENTWINDOW.equals(timeSlot)) {
//            /*
//            List<GenericEntity> assessmentMetaData = new ArrayList<GenericEntity>();
//            Set<String> assessmentIds = new HashSet<String>();
//            for (GenericEntity studentAssessment : studentAssessmentFiltered) {
//                String assessmentId = studentAssessment.getString(Constants.ATTR_ASSESSMENT_ID);
//                if (!assessmentIds.contains(assessmentId)) {
//                    GenericEntity assessment = metaDataResolver.getAssmtById(assessmentId);
//                    assessmentMetaData.add(assessment);
//                    assessmentIds.add(assessmentId);
//                }
//            }
//            
//            chosenAssessment = TimedLogic.getMostRecentAssessmentWindow(studentAssessmentFiltered, assessmentMetaData);
//            */
//            
        } else {
            // Decide whether to throw runtime exception here. Should timed logic default @@@
            chosenAssessment = TimedLogic2.getMostRecentAssessment(studentAssessmentFiltered);
        }        
        
        
        return chosenAssessment;
    }
    
    
    private List<GenericEntity> getStudentAttendance(String token, String studentId, String startDate, String endDate) {
        return entityManager.getAttendance(token, studentId, startDate, endDate);
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
        
        // get all assessments for student
        List<GenericEntity> assmts = entityManager.getStudentAssessments(username, studentId);
        
        return assmts;
    }
    
    /* (non-Javadoc)
     * @see org.slc.sli.manager.PopulationManagerI#setEntityManager(org.slc.sli.manager.EntityManager)
     */
    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    /* (non-Javadoc)
     * @see org.slc.sli.manager.PopulationManagerI#getStudent(java.lang.String, java.lang.String)
     */
    @Override
    public GenericEntity getStudent(String token, String studentId) {
        return entityManager.getStudent(token, studentId);
    }
    
    /* (non-Javadoc)
     * @see org.slc.sli.manager.PopulationManagerI#getStudent(java.lang.String, java.lang.Object, org.slc.sli.entity.Config.Data)
     */
    @Override
    public GenericEntity getStudent(String token, Object studentId, Config.Data config) {
        String key = (String) studentId;
        return entityManager.getStudentForCSIPanel(token, key);
    }
    
    /* (non-Javadoc)
     * @see org.slc.sli.manager.PopulationManagerI#getAttendance(java.lang.String, java.lang.Object, org.slc.sli.entity.Config.Data)
     */
    @Override
    public GenericEntity getAttendance(String token, Object studentIdObj, Config.Data config) {
        String studentId = (String) studentIdObj;
        // TODO: start using periods
        String period = config.getParams() == null ? null : (String) config.getParams().get("daysBack");
        int daysBack = (period == null) ? 360 : Integer.parseInt(period);
        MutableDateTime daysBackTime = new DateTime().toMutableDateTime();
        daysBackTime.addDays(-1 * daysBack);
        
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTimeFormatter dtfMonth = DateTimeFormat.forPattern("yyyy-MM");
        List<GenericEntity> attendanceList = 
                this.getStudentAttendance(token, studentId, null, null);
        Collections.sort(attendanceList, new Comparator<GenericEntity>() {

            @Override
            public int compare(GenericEntity att1, GenericEntity att2) {
                return ((String) att2.get("eventDate")).compareTo((String) att1.get("eventDate"));
            }
            
        });
        GenericEntity attendance = new GenericEntity();
        GenericEntity currentEntry;
        String currentMonth = null, month;
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(0);
        int tardyCount = 0, eAbsenceCount = 0, uAbsenceCount = 0, totalCount = 0;
        for (GenericEntity entry : attendanceList) {
            month = dtf.parseDateTime((String) entry.get("eventDate")).toString(dtfMonth);
            if (currentMonth == null) {
                currentMonth = month;
            } else if (!currentMonth.equals(month)) {
                currentEntry = new GenericEntity();
                currentEntry.put("eventDate", month);
                currentEntry.put("totalCount", totalCount);
                currentEntry.put("excusedAbsenceCount", eAbsenceCount);
                currentEntry.put("unexcusedAbsenceCount", uAbsenceCount);
                currentEntry.put("tardyCount", tardyCount);
                currentEntry.put("tardyRate", nf.format(100. * tardyCount / totalCount));
                currentEntry.put("attendanceRate", nf.format(100. * (totalCount - (uAbsenceCount + eAbsenceCount)) / totalCount));
                attendance.appendToList("attendance", currentEntry);
                currentMonth = month;
                uAbsenceCount = 0;
                eAbsenceCount = 0;
                tardyCount = 0;
                totalCount = 0;
            } 
            String value = (String) entry.get("attendanceEventCategory");
            if (value.contains("Tardy")) {
                tardyCount++;
            } else if  (value.contains("Excused Absence")) {
                eAbsenceCount++;
            } else if  (value.contains("Unexcused Absence")) {
                uAbsenceCount++;
            }
            totalCount++;
        }
        return attendance;
    } 
    
    /* (non-Javadoc)
     * @see org.slc.sli.manager.PopulationManagerI#getSessionDates(java.lang.String, java.lang.String)
     */
    @Override
    public List<String> getSessionDates(String token, String sessionId) {
        // Get the session first.
        GenericEntity currentSession = entityManager.getSession(token, sessionId);
        List<String> dates = new ArrayList<String>();
        if (currentSession != null) {
            String beginDate = currentSession.getString("beginDate");
            String endDate = currentSession.getString("endDate");
            List<GenericEntity> potentialSessions = entityManager.getSessionsByYear(token,
                    currentSession.getString("schoolYear"));
            for (GenericEntity session : potentialSessions) {
                if (session.getString("beginDate").compareTo(beginDate) < 0) {
                    beginDate = session.getString("beginDate");
                }
                if (session.getString("endDate").compareTo(endDate) > 0) {
                    endDate = session.getString("endDate");
                }
            }
            
            dates.add(beginDate);
            dates.add(endDate);
        } else {
            dates.add("");
            dates.add("");
        }
        return dates;
    }
}
