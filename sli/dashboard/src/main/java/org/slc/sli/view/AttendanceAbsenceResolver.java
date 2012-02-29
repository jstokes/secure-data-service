package org.slc.sli.view;

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
    
    private final String compareValue = "Absence";

    public AttendanceAbsenceResolver(GenericEntity student) {
        this.student = student;
    }

    public AttendanceAbsenceResolver() {
    }

    public void setStudent(GenericEntity student) {
        this.student = student;
    }

    @Override
    public int getCountForPath(String path) {
        //TODO: This should be a lot more generic.
        List<GenericEntity> attendances = student.getList(Constants.ATTR_STUDENT_ATTENDANCES);
        int count = 0;
        for (GenericEntity attendance : attendances) {
            if (attendance.getString("attendanceEventCategory").contains(compareValue)) { ++count; }
        }
        logger.debug(student.toString());
        return count;
    }
}
