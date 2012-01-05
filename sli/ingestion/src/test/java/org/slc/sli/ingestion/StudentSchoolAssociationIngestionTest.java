package org.slc.sli.ingestion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.xml.sax.SAXException;

import org.slc.sli.dal.repository.EntityRepository;
import org.slc.sli.domain.Entity;
import org.slc.sli.ingestion.landingzone.IngestionFileEntry;
import org.slc.sli.ingestion.processors.EdFiProcessor;
import org.slc.sli.ingestion.processors.PersistenceProcessor;
import org.slc.sli.ingestion.util.MD5;

/**
 * Tests for StudentSchoolAssociation entity
 *
 * @author dduran
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
public class StudentSchoolAssociationIngestionTest {

    public static final int MOCK_STUDENT_SCHOOL_REPOSITORY_ID_OFFSET = 99;

    @Autowired
    private EdFiProcessor edFiProcessor;

    @Autowired
    private PersistenceProcessor persistenceProcessor;

    @Autowired
    private EntityRepository repository;

    private static final String SCHOOL_ENTITY = "school";
    private static final String STUDENT_ENTITY = "student";
    private static final String STUDENT_SCHOOL_ASSOC_ENTITY = "studentSchoolAssociation";

    @Test
    public void testStudentSchoolAssociationInterchangeXmlParsing() throws IOException, SAXException {

        int numberOfStudentSchoolAssociations = 2;

        repository.deleteAll(STUDENT_ENTITY);
        this.createStudents(numberOfStudentSchoolAssociations);

        repository.deleteAll(SCHOOL_ENTITY);
        this.createSchools(numberOfStudentSchoolAssociations);

        repository.deleteAll(STUDENT_SCHOOL_ASSOC_ENTITY);

        String xmlRecords = createStudentSchoolAssociationInterchangeXml(numberOfStudentSchoolAssociations);

        File inputFile = IngestionTest.createTestFile(xmlRecords);

        // Create Ingestion File Entry
        IngestionFileEntry inputFileEntry = new IngestionFileEntry(FileFormat.EDFI_XML,
                FileType.XML_STUDENT_ENROLLMENT, inputFile.getName(), MD5.calculate(inputFile));
        inputFileEntry.setFile(inputFile);

        edFiProcessor.processFileEntry(inputFileEntry);

        File ingestionPersistenceProcessorOutputFile = IngestionTest.createTempFile();

        persistenceProcessor.processIngestionStream(inputFileEntry.getNeutralRecordFile(),
                ingestionPersistenceProcessorOutputFile);

        verifyStudentSchoolAssociations(repository, numberOfStudentSchoolAssociations);

    }

    @Test
    public void testStudentSchoolAssociationInterchangeCsvParsing() throws IOException, SAXException {

        int numberOfStudentSchoolAssociations = 2;

        repository.deleteAll(STUDENT_ENTITY);
        this.createStudents(numberOfStudentSchoolAssociations);

        repository.deleteAll(SCHOOL_ENTITY);
        this.createSchools(numberOfStudentSchoolAssociations);

        repository.deleteAll(STUDENT_SCHOOL_ASSOC_ENTITY);

        String csvRecords = createStudentSchoolAssociationInterchangeCsv(numberOfStudentSchoolAssociations);

        File inputFile = IngestionTest.createTestFile(csvRecords);

        // Create Ingestion File Entry
        IngestionFileEntry inputFileEntry = new IngestionFileEntry(FileFormat.CSV,
                FileType.CSV_STUDENT_SCHOOL_ASSOCIATION, inputFile.getName(), MD5.calculate(inputFile));
        inputFileEntry.setFile(inputFile);

        edFiProcessor.processFileEntry(inputFileEntry);

        File ingestionPersistenceProcessorOutputFile = IngestionTest.createTempFile();

        persistenceProcessor.processIngestionStream(inputFileEntry.getNeutralRecordFile(),
                ingestionPersistenceProcessorOutputFile);

        verifyStudentSchoolAssociations(repository, numberOfStudentSchoolAssociations);

    }

    private void createStudents(int numberOfStudents) throws IOException, SAXException {

        repository.deleteAll(STUDENT_ENTITY);

        List<NeutralRecord> neutralRecords = StudentIngestionTest
                .createStudentIngestionNeutralRecords(numberOfStudents);

        File neutralRecordsFile = IngestionTest.createNeutralRecordsFile(neutralRecords);

        File ingestionPersistenceProcessorOutputFile = IngestionTest.createTempFile();

        persistenceProcessor.processIngestionStream(neutralRecordsFile, ingestionPersistenceProcessorOutputFile);

    }

    private void createSchools(int numberOfSchools) throws IOException, SAXException {

        repository.deleteAll(SCHOOL_ENTITY);

        List<NeutralRecord> neutralRecords = SchoolIngestionTest.createSchoolIngestionNeutralRecords(numberOfSchools);

        File neutralRecordsFile = IngestionTest.createNeutralRecordsFile(neutralRecords);

        File ingestionPersistenceProcessorOutputFile = IngestionTest.createTempFile();

        persistenceProcessor.processIngestionStream(neutralRecordsFile, ingestionPersistenceProcessorOutputFile);

    }

    public static String createStudentSchoolAssociationInterchangeXml(int numberOfAssociations) {
        StringBuilder builder = new StringBuilder();

        builder.append(createStudentSchoolAssociationInterchangeXmlHeader());

        for (int index = 1; index <= numberOfAssociations; index++) {
            StudentSchoolAssociationInterchange studentSchoolAssociation = createStudentSchoolAssociation(index);
            builder.append(createStudentSchoolAssociationXml(studentSchoolAssociation));
        }
        builder.append(createStudentSchoolAssociationInterchangeXmlFooter());

        return builder.toString();
    }

    public static String createStudentSchoolAssociationInterchangeCsv(int numberOfAssociations) {
        StringBuilder builder = new StringBuilder();

        for (int index = 1; index <= numberOfAssociations; index++) {
            StudentSchoolAssociationInterchange studentSchoolAssociation = createStudentSchoolAssociation(index);
            builder.append(createStudentSchoolAssociationCsv(studentSchoolAssociation));
            builder.append(System.getProperty("line.separator"));
        }

        return builder.toString();
    }

    public static String createStudentSchoolAssociationInterchangeXmlHeader() {

        String interchangeXmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n";
        interchangeXmlHeader += "<InterchangeStudentEnrollment xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"Interchange-StudentEnrollment.xsd\" xmlns=\"http://ed-fi.org/0100RFC062811\">"
                + "\n";

        return interchangeXmlHeader;
    }

    public static String createStudentSchoolAssociationInterchangeXmlFooter() {

        String interchangeXmlFooter = "</InterchangeStudentEnrollment>" + "\n";

        return interchangeXmlFooter;
    }

    public static String createStudentSchoolAssociationXml(StudentSchoolAssociationInterchange studentSchoolAssociation) {
        String studentSchoolAssociationXml = "";

        studentSchoolAssociationXml += "<StudentSchoolAssociation>" + "\n";

        // Test Version Only - allow specification of Association ID
        studentSchoolAssociationXml += "<AssociationId>" + studentSchoolAssociation.getAssociationId()
                + "</AssociationId>" + "\n";

        studentSchoolAssociationXml += "<StudentReference><StudentIdentity>" + "\n";
        studentSchoolAssociationXml += "<StudentUniqueStateId>";

        studentSchoolAssociationXml += studentSchoolAssociation.getStudentUniqueStateId();

        studentSchoolAssociationXml += "</StudentUniqueStateId>" + "\n";
        studentSchoolAssociationXml += "</StudentIdentity></StudentReference>" + "\n";

        studentSchoolAssociationXml += "<SchoolReference><EducationalOrgIdentity>" + "\n";
        studentSchoolAssociationXml += "<StateOrganizationId>";

        studentSchoolAssociationXml += studentSchoolAssociation.getStateOrganizationId();

        studentSchoolAssociationXml += "</StateOrganizationId>" + "\n";
        studentSchoolAssociationXml += "</EducationalOrgIdentity></SchoolReference>" + "\n";

        studentSchoolAssociationXml += "</StudentSchoolAssociation>" + "\n";

        return studentSchoolAssociationXml;
    }

    public static String createStudentSchoolAssociationCsv(StudentSchoolAssociationInterchange studentSchoolAssociation) {
        String studentSchoolAssociationCsv = "";

        // Test Version Only - allow specification of Student ID
        studentSchoolAssociationCsv += studentSchoolAssociation.getAssociationId() + ",";

        // Skip 2 fields
        studentSchoolAssociationCsv += ",,";

        studentSchoolAssociationCsv += studentSchoolAssociation.getStudentUniqueStateId() + ",";

        // Skip 22 fields for now
        studentSchoolAssociationCsv += ",,,,,,,,,,,,,,,,,,,,,,";

        studentSchoolAssociationCsv += studentSchoolAssociation.getStateOrganizationId();

        return studentSchoolAssociationCsv;
    }

    public static StudentSchoolAssociationInterchange createStudentSchoolAssociation(int associationId) {
        StudentSchoolAssociationInterchange studentSchoolAssociation = new StudentSchoolAssociationInterchange();

        studentSchoolAssociation.setAssociationId(associationId);
        studentSchoolAssociation.setStudentUniqueStateId("" + associationId);
        studentSchoolAssociation.setStateOrganizationId("" + associationId);

        return studentSchoolAssociation;
    }

    // TODO: FIX WITH CORRECT IDS
    public static void verifyStudentSchoolAssociations(EntityRepository repository, long expectedCount) {

        long repositorySize = IngestionTest.getTotalCountOfEntityInRepository(repository, STUDENT_SCHOOL_ASSOC_ENTITY);

        assertEquals(expectedCount, repositorySize);

        for (int index = 1; index <= repositorySize; index++) {
            Map<String, String> queryMap = new HashMap<String, String>();
            queryMap.put("ssaId", Integer.toString(index));

            Iterator<Entity> studentSchoolAssociations = (repository
                    .findByFields(STUDENT_SCHOOL_ASSOC_ENTITY, queryMap)).iterator();
            if (studentSchoolAssociations.hasNext()) {
                verifyStudentSchoolAssociation(index, studentSchoolAssociations.next());
            }
        }

    }

    // TODO: FIX WITH CORRECT IDS
    public static void verifyStudentSchoolAssociation(int ssaId, Entity studentSchoolAssociation) {
        assertNotNull(studentSchoolAssociation);
        assertEquals("" + ssaId, (studentSchoolAssociation.getBody()).get("ssaId"));
        assertEquals("nameOfInstitution" + "_" + ssaId, (studentSchoolAssociation.getBody()).get("fullName"));

    }

}
