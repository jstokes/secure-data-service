/**
 * 
 */
package org.slc.sli.entity.util;

import java.util.HashMap;
import java.util.Map;

import org.slc.sli.entity.GenericEntity;

/**
 * @author tosako
 * 
 */
public class GenericEntityEnhancer {
    private static Map<String, String> gradeConversion = null;
    static {
        gradeConversion = new HashMap<String, String>();
        gradeConversion.put("Adult Education", "");
        gradeConversion.put("Early Education", "");
        gradeConversion.put("Eighth grade", "8");
        gradeConversion.put("Eleventh grade", "11");
        gradeConversion.put("Fifth grade", "5");
        gradeConversion.put("First grade", "1");
        gradeConversion.put("Fourth grade", "4");
        gradeConversion.put("Grade 13", "");
        gradeConversion.put("Infant/toddler", "");
        gradeConversion.put("Kindergarten", "K");
        gradeConversion.put("Ninth grade", "9");
        gradeConversion.put("Other", "");
        gradeConversion.put("Postsecondary", "");
        gradeConversion.put("Preschool/Prekindergarten", "");
        gradeConversion.put("Second grade", "2");
        gradeConversion.put("Seventh grade", "7");
        gradeConversion.put("Sixth grade", "6");
        gradeConversion.put("Tenth grade", "10");
        gradeConversion.put("Third grade", "3");
        gradeConversion.put("Transitional Kindergarten", "");
        gradeConversion.put("Twelfth grade", "12");
        gradeConversion.put("Ungraded", "");
    }
    
    /**
     * Enhance input GenericEntity for Student
     * 
     * @param entity
     * @return enhanced GenericEntity
     */
    public static GenericEntity enhanceStudent(GenericEntity entity) {
        // determin this entity has gradeLevel
        String gradeLevel = entity.getString("gradeLevel");
        
        // if gradeLevel exists, then add gradeLevelCode in the entity
        if (gradeLevel != null && gradeConversion.containsKey(gradeLevel))
            entity.put("gradeLevelCode", gradeConversion.get(gradeLevel));
        return entity;
    }
    /**
     * Enhance input GenericEntity for StudentiSchoolAssociation
     * 
     * @param entity
     * @return enhanced GenericEntity
     */
    public static GenericEntity enhanceStudentSchoolAssociation(GenericEntity entity) {
        // repeat for another element
        String gradeLevel = entity.getString("entryGradeLevel");
        
        // if gradeLevel exists, then add gradeLevelCode in the entity
        if (gradeLevel != null && gradeConversion.containsKey(gradeLevel))
            entity.put("entryGradeLevelCode", gradeConversion.get(gradeLevel));
        return entity;
    }
}
