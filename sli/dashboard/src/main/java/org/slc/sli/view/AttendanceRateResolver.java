package org.slc.sli.view;

import java.util.List;
import java.util.Map;

import org.slc.sli.config.Field;
import org.slc.sli.entity.GenericEntity;
import org.slc.sli.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author pwolf
 *
 */
public class AttendanceRateResolver implements AggregateRatioResolver {

    private static Logger logger = LoggerFactory.getLogger(AttendanceAbsenceResolver.class);

    private GenericEntity student;

    public static final String CATEGORY = "attendanceEventCategory";
    private static final String ATTENDANCE_RATE = "ATTENDANCE.AttendanceRate";
    private static final String TARDY_RATE = "ATTENDANCE.TardyRate";

    public AttendanceRateResolver(GenericEntity student) {
        this.student = student;
    }

    public AttendanceRateResolver() {
    }

    public void setStudent(GenericEntity student) {
        this.student = student;
    }

    @Override
    public int getSize(Field configField) {
        Map<String, Object> attendanceBody = (Map<String, Object>) student.get(Constants.ATTR_STUDENT_ATTENDANCES);
        if (attendanceBody == null)
            return 0;

        List<Map<String, Object>> attendances = (List<Map<String, Object>>) attendanceBody.get(Constants.ATTR_STUDENT_ATTENDANCES);
        if (attendances == null)
            return 0;
        return attendances.size();
    }

    @Override
    public int getCountForPath(Field configField) {
        Map<String, Object> attendanceBody = (Map<String, Object>) student.get(Constants.ATTR_STUDENT_ATTENDANCES);
        if (attendanceBody == null)
            return 0;

        List<Map<String, Object>> attendances = (List<Map<String, Object>>) attendanceBody.get(Constants.ATTR_STUDENT_ATTENDANCES);

        int count = 0;
        if (attendances != null)
            if (configField.getValue().equals(ATTENDANCE_RATE)) {
                return getAttendanceCount(attendances);
            } else if (configField.getValue().equals(TARDY_RATE)) {
                return getTardyCount(attendances);
            }
        assert false;   //should never get here unless it's an known field value
        return 0;
    }

    private int getAttendanceCount(List<Map<String, Object>> attendances) {
        int count = attendances.size();
        for (Map attendance : attendances) {
            String value = (String) attendance.get(CATEGORY);
            if (value.contains("Absence")) {
                count--;
            }
        }
        return count;
    }

    private int getTardyCount(List<Map<String, Object>> attendances) {
        int count = 0;
        for (Map attendance : attendances) {
            String value = (String) attendance.get(CATEGORY);
            if (value.contains("Tardy")) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int[] getCutoffPoints(Field field) {

        if (field.getValue().equals(TARDY_RATE)) {
            return new int[] {10, 5, 1};
        } else if (field.getValue().equals(ATTENDANCE_RATE)) {
            return new int[] {90, 95, 99};
        }
        return new int[] {};

    }

}
