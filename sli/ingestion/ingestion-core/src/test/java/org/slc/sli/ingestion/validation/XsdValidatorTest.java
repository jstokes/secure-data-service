package org.slc.sli.ingestion.validation;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.slc.sli.ingestion.FileFormat;
import org.slc.sli.ingestion.FileType;
import org.slc.sli.ingestion.IngestionTest;
import org.slc.sli.ingestion.landingzone.IngestionFileEntry;

/**
 *
 * @author ablum
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/validation-context.xml" })
public class XsdValidatorTest {

    @Autowired
    private XsdValidator xsdValidator;

    @Test
    @Ignore
    public void testValidXml() throws IOException {
        File xmlFile = IngestionTest.getFile("InterchangeStudent-Valid.xml");
        IngestionFileEntry ife = new IngestionFileEntry(FileFormat.EDFI_XML, FileType.XML_STUDENT, xmlFile.getAbsolutePath(), "");
        ife.setFile(xmlFile);
        Assert.assertTrue(xsdValidator.isValid(ife, Mockito.mock(ErrorReport.class)));
    }

    @Test
    public void testInValidXml() throws IOException {
        File xmlFile = IngestionTest.getFile("XsdValidation/InterchangeStudent-InValid.xml");
        IngestionFileEntry ife = new IngestionFileEntry(FileFormat.EDFI_XML, FileType.XML_STUDENT, xmlFile.getAbsolutePath(), "");
        ife.setFile(xmlFile);
        Assert.assertFalse(xsdValidator.isValid(ife, Mockito.mock(ErrorReport.class)));
    }

    @Test
    public void testXmlNotExists() {
        IngestionFileEntry ife = new IngestionFileEntry(FileFormat.EDFI_XML, FileType.XML_STUDENT, "XsdValidation/NoFile.xml", "");
        //ife.setFile(xmlFile.getFile());
        Assert.assertFalse(xsdValidator.isValid(ife, Mockito.mock(ErrorReport.class)));
    }

    @Test
    public void testLoadXsds() {
        Map<String, Resource> resources = xsdValidator.getXsd();

        Assert.assertNotNull(resources.get("AssessmentMetadata"));
        Assert.assertNotNull(resources.get("EducationOrganization"));
        Assert.assertNotNull(resources.get("EducationOrgCalendar"));
        Assert.assertNotNull(resources.get("HSGeneratedStudentTranscript"));
        Assert.assertNotNull(resources.get("MasterSchedule"));
        Assert.assertNotNull(resources.get("StaffAssociation"));
        Assert.assertNotNull(resources.get("Student"));
        Assert.assertNotNull(resources.get("StudentAssessment"));
        Assert.assertNotNull(resources.get("Attendance"));
        Assert.assertNotNull(resources.get("StudentCohort"));
        Assert.assertNotNull(resources.get("StudentDiscipline"));
        Assert.assertNotNull(resources.get("StudentEnrollment"));
        Assert.assertNotNull(resources.get("StudentGrades"));
        Assert.assertNotNull(resources.get("Parent"));
        Assert.assertNotNull(resources.get("StudentProgram"));

        Assert.assertTrue(resources.get("AssessmentMetadata").exists());
        Assert.assertTrue(resources.get("EducationOrganization").exists());
        Assert.assertTrue(resources.get("EducationOrgCalendar").exists());
        Assert.assertTrue(resources.get("HSGeneratedStudentTranscript").exists());
        Assert.assertTrue(resources.get("MasterSchedule").exists());
        Assert.assertTrue(resources.get("StaffAssociation").exists());
        Assert.assertTrue(resources.get("Student").exists());
        Assert.assertTrue(resources.get("StudentAssessment").exists());
        Assert.assertTrue(resources.get("Attendance").exists());
        Assert.assertTrue(resources.get("StudentCohort").exists());
        Assert.assertTrue(resources.get("StudentDiscipline").exists());
        Assert.assertTrue(resources.get("StudentEnrollment").exists());
        Assert.assertTrue(resources.get("StudentGrades").exists());
        Assert.assertTrue(resources.get("Parent").exists());
        Assert.assertTrue(resources.get("StudentProgram").exists());
    }
}
