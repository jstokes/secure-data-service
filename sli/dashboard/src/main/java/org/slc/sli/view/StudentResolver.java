package org.slc.sli.view;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slc.sli.config.Field;
import org.slc.sli.entity.GenericEntity;
import org.slc.sli.entity.util.StudentProgramUtil;
import org.slc.sli.util.Constants;


//Hopefully there will be one for each of dataSet types

/**
 * A utility class for views in SLI dashboard. As a wrapper around student data passed onto
 *  dashboard views. Contains useful tools look up student data
 * 
 * @author syau
 *
 */
public class StudentResolver {
    List<GenericEntity> students;
    List<GenericEntity> programs;
    
    /**
     * Constructor
     */
    public StudentResolver(List<GenericEntity> s, List<GenericEntity> p) {
        students = s;
        programs = p;
    }
    
    public List<GenericEntity> list() {
        return students;
    }
    
    /**
     * Returns the string representation of the student information, identified by the datapoint ID
     */
    //public String get(Field field, GenericEntity student) {
    public String get(Field field, Map student) {
        String dataPointName = field.getValue();
        if (dataPointName == null) { return ""; }
        if (dataPointName.equals(Constants.ATTR_NAME)) {
            // formatting class and logic should be added here later. Or maybe in the view. Don't know... 
            //return student.getFirstName() + " " + student.getLastName();
            return ((Map) (student.get(Constants.ATTR_NAME))).get(Constants.ATTR_FIRST_NAME) + " " 
                 + ((Map) (student.get(Constants.ATTR_NAME))).get(Constants.ATTR_LAST_SURNAME);
        } 
        return "";
    }

    /**
     * returns true if the given lozenge code applies to the given student
     */
    public boolean lozengeApplies(Map student, String code) {
        
        String[] studentProgramCodes = StudentProgramUtil.getProgramCodesForStudent();
        
        // Check if program in student entity
        if (Arrays.asList(studentProgramCodes).contains(code)) {
            return StudentProgramUtil.hasProgramParticipation(student, code);
        } 
        
        // Now check program participation
        for (GenericEntity p : programs) {
            if (p.get(Constants.ATTR_STUDENT_ID).equals(student.get(Constants.ATTR_ID))) {
                return ((List<String>) (p.get(Constants.ATTR_PROGRAMS))).contains(code);
            }
        }

        return false;
    }
}
