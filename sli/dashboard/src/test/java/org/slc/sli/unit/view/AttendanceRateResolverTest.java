package org.slc.sli.unit.view;

import org.junit.Before;
import org.junit.Test;
import org.slc.sli.config.Field;
import org.slc.sli.entity.GenericEntity;
import org.slc.sli.util.Constants;
import org.slc.sli.view.AttendanceRateResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 *
 * @author pwolf
 *
 */
public class AttendanceRateResolverTest {

    private AttendanceRateResolver resolver;
    private GenericEntity mockStudent;

    private static final String IN_ATTENDANCE = "In Attendance";
    private static final String TARDY = "Tardy";
    private static final String EXCUSED = "Excused Absence";
    private Field tardyField = null;
    private Field attendanceField = null;


    @Before
    public void setUp() throws Exception {
        resolver = new AttendanceRateResolver();
        mockStudent = new GenericEntity();
        resolver.setStudent(mockStudent);

        tardyField = new Field();
        tardyField.setValue("ATTENDANCE.TardyRate");

        attendanceField = new Field();
        attendanceField.setValue("ATTENDANCE.AttendanceRate");
    }

    @Test
    public void testNoTardy() {
        List<Map<String, String>> attendances = new ArrayList<Map<String, String>>();
        for (int i = 0; i < 10; ++i) {
            attendances.add(getAttendanceObject(IN_ATTENDANCE));
        }
        GenericEntity body = new GenericEntity();
        body.put("attendances", attendances);
        mockStudent.put("attendances", body);
        assertEquals(0, resolver.getCountForPath(tardyField));
        assertEquals(10, resolver.getSize(tardyField));
    }

    @Test
    public void testNoTardyMix() {
        List<Map<String, String>> attendances = new ArrayList<Map<String, String>>();
        for (int i = 0; i < 10; ++i) {
            attendances.add(getAttendanceObject(IN_ATTENDANCE));
        }
        for (int i = 0; i < 10; ++i) {
            attendances.add(getAttendanceObject(EXCUSED));
        }
        GenericEntity body = new GenericEntity();
        body.put("attendances", attendances);
        mockStudent.put("attendances", body);
        assertEquals(0, resolver.getCountForPath(tardyField));
        assertEquals(20, resolver.getSize(tardyField));
    }

    @Test
    public void testSomeTardy() {
        List<Map<String, String>> attendances = new ArrayList<Map<String, String>>();
        for (int i = 0; i < 10; ++i) {
            attendances.add(getAttendanceObject(IN_ATTENDANCE));
        }
        for (int i = 0; i < 10; ++i) {
            attendances.add(getAttendanceObject(EXCUSED));
        }
        for (int i = 0; i < 5; ++i) {
            attendances.add(getAttendanceObject(TARDY));
        }
        GenericEntity body = new GenericEntity();
        body.put("attendances", attendances);
        mockStudent.put("attendances", body);
        assertEquals(5, resolver.getCountForPath(tardyField));
        assertEquals(25, resolver.getSize(tardyField));

        assertEquals(15, resolver.getCountForPath(attendanceField));
        assertEquals(25, resolver.getSize(attendanceField));
    }

    @Test
    public void testPerfectAttendance() {
        List<Map<String, String>> attendances = new ArrayList<Map<String, String>>();
        for (int i = 0; i < 10; ++i) {
            attendances.add(getAttendanceObject(IN_ATTENDANCE));
        }
        GenericEntity body = new GenericEntity();
        body.put("attendances", attendances);
        mockStudent.put("attendances", body);
        assertEquals(10, resolver.getCountForPath(attendanceField));
        assertEquals(10, resolver.getSize(attendanceField));
    }

    @Test
    public void testBadAttendance() {
        List<Map<String, String>> attendances = new ArrayList<Map<String, String>>();
        for (int i = 0; i < 10; ++i) {
            attendances.add(getAttendanceObject(EXCUSED));
        }
        GenericEntity body = new GenericEntity();
        body.put("attendances", attendances);
        mockStudent.put("attendances", body);
        assertEquals(0, resolver.getCountForPath(attendanceField));
        assertEquals(10, resolver.getSize(attendanceField));
    }

    @Test
    public void testHasCutoffPoints() {
        assertNotNull(resolver.getCutoffPoints(tardyField));
        assertNotNull(resolver.getCutoffPoints(attendanceField));
    }

    @Test
    public void testNoAttendance() {
        assertEquals(0, resolver.getSize(tardyField));
    }

    @Test
    public void testUnknownField() {
        Field field = new Field();
        field.setValue("Hello");
        assertEquals(0, resolver.getCutoffPoints(field).length);
    }

    private Map<String, String> getAttendanceObject(String type) {
        Map<String, String> attendance = new HashMap<String, String>();
        attendance.put(Constants.ATTR_ATTENDANCE_EVENT_CATEGORY, type);
        return attendance;
    }


}
