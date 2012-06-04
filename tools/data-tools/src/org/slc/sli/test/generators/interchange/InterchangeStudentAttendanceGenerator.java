package org.slc.sli.test.generators.interchange;

import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.slc.sli.test.edfi.entities.AttendanceEvent;
import org.slc.sli.test.edfi.entities.InterchangeStudentAttendance;
import org.slc.sli.test.edfi.entities.meta.StudentMeta;
import org.slc.sli.test.edfi.entities.meta.relations.MetaRelations;
import org.slc.sli.test.generators.AttendanceEventGenerator;
import org.slc.sli.test.utils.JaxbUtils;
import org.slc.sli.test.xmlgen.StateEdFiXmlGenerator;

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
//    public static InterchangeStudentAttendance generate() {
//
//        InterchangeStudentAttendance interchange = new InterchangeStudentAttendance();
//        List<AttendanceEvent> interchangeObjects = interchange.getAttendanceEvent();
//
//        writeEntitiesToInterchange(interchangeObjects);
//
//        return interchange;
//    }

    /**
     * Sets up a new Student Attendance Interchange and populates it.
     *
     * @return
     * @throws JAXBException 
     * @throws XMLStreamException 
     */
    public static void generate(XMLStreamWriter writer) throws JAXBException, XMLStreamException {

        writer.writeStartElement("InterchangeStudentAttendance");
        writer.writeNamespace(null, "http://ed-fi.org/0100");
        
        InterchangeStudentAttendance interchange = new InterchangeStudentAttendance();
        List<AttendanceEvent> interchangeObjects = interchange.getAttendanceEvent();

        writeEntitiesToInterchange(interchangeObjects, writer);

        writer.writeEndElement();
        
    }

    /**
     * Generate the individual Student Attendance Association entities and write them out.
     *
     * @param interchangeObjects
     * @throws JAXBException 
     */
    private static void writeEntitiesToInterchange(List<AttendanceEvent> interchangeObjects, XMLStreamWriter writer) throws JAXBException {

        generateStudentAttendanceEventAssoc(interchangeObjects, MetaRelations.STUDENT_MAP.values(), writer);

    }

    private static void generateStudentAttendanceEventAssoc(List<AttendanceEvent> interchangeObjects,
            Collection<StudentMeta> studentMetas, XMLStreamWriter writer) throws JAXBException {
        long startTime = System.currentTimeMillis();

        int objGenCounter = 0;
        for (StudentMeta studentMeta : studentMetas) {

            AttendanceEventGenerator.resetCalendar();

            for (String sectionId : studentMeta.sectionIds) {


                for (int count = 0; count < MetaRelations.ATTENDANCE_PER_STUDENT_SECTION; count++) {

                    AttendanceEvent attendanceEvent;

                    if ("medium".equals(StateEdFiXmlGenerator.fidelityOfData)) {
                        attendanceEvent = null;
                    } else {
                        attendanceEvent = AttendanceEventGenerator.generateLowFi(studentMeta.id,
                                studentMeta.schoolIds.get(0), sectionId);
                    }

                    JaxbUtils.marshal(attendanceEvent, writer);

                    objGenCounter++;
                }
            }
       }

        System.out.println("generated " + objGenCounter + " AttendanceEvent objects in: "
                + (System.currentTimeMillis() - startTime));
    }

}
