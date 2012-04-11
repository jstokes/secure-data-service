package org.slc.sli.test.generators.interchange;

import java.util.Collection;
import java.util.List;

import org.slc.sli.test.edfi.entities.AttendanceEvent;
import org.slc.sli.test.edfi.entities.InterchangeStudentAttendance;
import org.slc.sli.test.edfi.entities.relations.StudentMeta;
import org.slc.sli.test.generators.AttendanceEventGenerator;
import org.slc.sli.test.mappingGenerator.MetaRelations;
import org.slc.sli.test.mappingGenerator.StateEdFiXmlGenerator;

/**
 * Generates the Student Attendance Interchange as derived from the associations
 * determined during the call to MetaRelations.buildFromSea() in StateEdFiXmlGenerator.
 *
 * @author lchen
 */
public class InterchangeStudentAttendanceGenerator {

    /**
     * Sets up a new Student Attendance Interchange and populates it.
     *
     * @return
     */
    public static InterchangeStudentAttendance generate() {

        InterchangeStudentAttendance interchange = new InterchangeStudentAttendance();
        List<AttendanceEvent> interchangeObjects = interchange.getAttendanceEvent();

        addEntitiesToInterchange(interchangeObjects);

        return interchange;
    }

    /**
     * Generate the individual Student Attendance Association entities.
     *
     * @param interchangeObjects
     */
    private static void addEntitiesToInterchange(List<AttendanceEvent> interchangeObjects) {


    	generateStudentAttendanceEventAssoc(interchangeObjects, MetaRelations.STUDENT_MAP.values());

    }


    private static void generateStudentAttendanceEventAssoc(List<AttendanceEvent> interchangeObjects,
            Collection<StudentMeta> studentMetas) {
        long startTime = System.currentTimeMillis();

        int objGenCounter = 0;
        for (StudentMeta studentMeta : studentMetas) {
            for (String sectionId : studentMeta.sectionIds) {
                AttendanceEvent attendanceEvent;

                if ("medium".equals(StateEdFiXmlGenerator.fidelityOfData)) {
                	attendanceEvent = null;
                } else {
                	attendanceEvent = AttendanceEventGenerator.getAttendanceEvent(studentMeta.id, studentMeta.schoolIds.get(0), sectionId);
                }

                interchangeObjects.add(attendanceEvent);

                objGenCounter++;
            }
        }

        System.out.println("generated " + objGenCounter + " StudentAttendanceEvent Association objects in: "
                + (System.currentTimeMillis() - startTime));
    }

}
