package org.slc.sli.view;

import org.slc.sli.config.Field;
import org.slc.sli.entity.GenericEntity;
import org.slc.sli.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;


/**
 * Class to handle the oddities of getting absences from attendance objects.
 */
public class AttendanceAbsenceResolver implements AggregateResolver {
    private static Logger logger = LoggerFactory.getLogger(AttendanceAbsenceResolver.class);


    private GenericEntity student;
    
    private final String COMPARE_VALUE = "Absence";
    public static final String CATEGORY = "attendanceEventCategory";

    public AttendanceAbsenceResolver(GenericEntity student) {
        this.student = student;
    }

    public AttendanceAbsenceResolver() {
    }

    public void setStudent(GenericEntity student) {
        this.student = student;
    }

    @Override
    public int getCountForPath(Field configField) {
        //TODO: This should be a lot more generic.
        List<Map> attendances = student.getList(Constants.ATTR_STUDENT_ATTENDANCES);
        int count = 0;
        for (Map attendance : attendances) {
            logger.debug("Attendance: " + attendance);
            String value = (String) attendance.get(CATEGORY);
            if (value.contains(COMPARE_VALUE)) { ++count; }
        }
        return count;
    }
}
