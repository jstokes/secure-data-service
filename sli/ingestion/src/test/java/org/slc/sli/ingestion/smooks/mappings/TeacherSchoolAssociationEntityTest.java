package org.slc.sli.ingestion.smooks.mappings;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

import org.slc.sli.domain.Entity;
import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.util.EntityTestUtils;
import org.slc.sli.validation.EntityValidationException;
import org.slc.sli.validation.EntityValidator;

/**
*
* @author ablum
*
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class TeacherSchoolAssociationEntityTest {
    @Autowired
    EntityValidator validator;

    String xmlTestData =  "<InterchangeStaffAssociation xmlns=\"http://ed-fi.org/0100\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"Interchange-StaffAssociation.xsd\">"
    + "<TeacherSchoolAssociation>"
       + "<TeacherReference>"
          + "<StaffIdentity>"
             + "<StaffUniqueStateId>333333332</StaffUniqueStateId>"
          + "</StaffIdentity>"
       + "</TeacherReference>"
       + "<SchoolReference>"
          + "<EducationalOrgIdentity>"
             + "<StateOrganizationId>123456111</StateOrganizationId>"
          + "</EducationalOrgIdentity>"
       + "</SchoolReference>"
    + "<ProgramAssignment>Title I-Academic</ProgramAssignment>"
    + "<InstructionalGradeLevels>"
       + "<GradeLevel>Ungraded</GradeLevel>"
    + "</InstructionalGradeLevels>"
    + "<AcademicSubjects>"
       + "<AcademicSubject>English</AcademicSubject>"
    + "</AcademicSubjects>"
    + "</TeacherSchoolAssociation>"
    + "</InterchangeStaffAssociation>";

    @Ignore
    @Test
    public void testValidTeacherSectionAssociation() throws Exception {
          String smooksConfig = "smooks_conf/smooks-all-xml.xml";

          String targetSelector = "InterchangeStaffAssociation/TeacherSchoolAssociation";

          NeutralRecord neutralRecord = EntityTestUtils.smooksGetSingleNeutralRecord(smooksConfig, targetSelector, xmlTestData);

          Entity e = mock(Entity.class);

          when(e.getBody()).thenReturn(neutralRecord.getAttributes());
          when(e.getType()).thenReturn("teacherSchoolAssociation");

          Assert.assertTrue(validator.validate(e));
    }

    @Ignore
    @Test(expected = EntityValidationException.class)
    public void testInvalidTeacherSchoolAssociationMissingTeacherReference() throws Exception {
        String smooksConfig = "smooks_conf/smooks-all-xml.xml";
        String targetSelector = "InterchangeStaffAssociation/TeacherSchoolAssociation";

        String invalidXmlMissingTeacherReference =  "<InterchangeStaffAssociation xmlns=\"http://ed-fi.org/0100\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"Interchange-StaffAssociation.xsd\">"
                + "<TeacherSchoolAssociation>"
                   + "<SchoolReference>"
                      + "<EducationalOrgIdentity>"
                          + "<StateOrganizationId>123456111</StateOrganizationId>"
                      + "</EducationalOrgIdentity>"
                   + "</SchoolReference>"
                + "<ProgramAssignment>Title I-Academic</ProgramAssignment>"
                + "<InstructionalGradeLevels>"
                   + "<GradeLevel>Ungraded</GradeLevel>"
                + "</InstructionalGradeLevels>"
                + "<AcademicSubjects>"
                   + "<AcademicSubject>English</AcademicSubject>"
                + "</AcademicSubjects>"
                + "</TeacherSchoolAssociation>"
                + "</InterchangeStaffAssociation>";

        NeutralRecord neutralRecord = EntityTestUtils.smooksGetSingleNeutralRecord(smooksConfig, targetSelector, invalidXmlMissingTeacherReference);

        Entity e = mock(Entity.class);
        when(e.getBody()).thenReturn(neutralRecord.getAttributes());
        when(e.getType()).thenReturn("teacherSchoolAssociation");

        validator.validate(e);

    }

    @Ignore
    @Test(expected = EntityValidationException.class)
    public void testInvalidTeacherSchoolAssociationMissingSchoolReference() throws Exception {
        String smooksConfig = "smooks_conf/smooks-all-xml.xml";
        String targetSelector = "InterchangeStaffAssociation/TeacherSchoolAssociation";

        String invalidXmlMissingSchoolReference =  "<InterchangeStaffAssociation xmlns=\"http://ed-fi.org/0100\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"Interchange-StaffAssociation.xsd\">"
                + "<TeacherSchoolAssociation>"
                   + "<TeacherReference>"
                      + "<StaffIdentity>"
                          + "<StaffUniqueStateId>333333332</StaffUniqueStateId>"
                      + "</StaffIdentity>"
                      + "</TeacherReference>"
                + "<ProgramAssignment>Title I-Academic</ProgramAssignment>"
                + "<InstructionalGradeLevels>"
                      + "<GradeLevel>Ungraded</GradeLevel>"
                + "</InstructionalGradeLevels>"
                + "<AcademicSubjects>"
                      + "<AcademicSubject>English</AcademicSubject>"
                + "</AcademicSubjects>"
                + "</TeacherSchoolAssociation>"
                + "</InterchangeStaffAssociation>";

        NeutralRecord neutralRecord = EntityTestUtils.smooksGetSingleNeutralRecord(smooksConfig, targetSelector, invalidXmlMissingSchoolReference);

        Entity e = mock(Entity.class);
        when(e.getBody()).thenReturn(neutralRecord.getAttributes());
        when(e.getType()).thenReturn("teacherSchoolAssociation");

        validator.validate(e);

    }

    @Ignore
    @Test(expected = EntityValidationException.class)
    public void testInvalidTeacherSchoolAssociationMissingProgramAssignment() throws Exception {
        String smooksConfig = "smooks_conf/smooks-all-xml.xml";
        String targetSelector = "InterchangeStaffAssociation/TeacherSchoolAssociation";

        String invalidXmlMissingProgramAssignment =  "<InterchangeStaffAssociation xmlns=\"http://ed-fi.org/0100\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"Interchange-StaffAssociation.xsd\">"
               + "<TeacherSchoolAssociation>"
                   + "<TeacherReference>"
                      + "<StaffIdentity>"
                           + "<StaffUniqueStateId>333333332</StaffUniqueStateId>"
                      + "</StaffIdentity>"
                   + "</TeacherReference>"
                    + "<SchoolReference>"
                      + "<EducationalOrgIdentity>"
                           + "<StateOrganizationId>123456111</StateOrganizationId>"
                      + "</EducationalOrgIdentity>"
                   + "</SchoolReference>"
                + "<InstructionalGradeLevels>"
                   + "<GradeLevel>Ungraded</GradeLevel>"
                + "</InstructionalGradeLevels>"
                + "<AcademicSubjects>"
                   + "<AcademicSubject>English</AcademicSubject>"
                + "</AcademicSubjects>"
                + "</TeacherSchoolAssociation>"
                + "</InterchangeStaffAssociation>";

        NeutralRecord neutralRecord = EntityTestUtils.smooksGetSingleNeutralRecord(smooksConfig, targetSelector, invalidXmlMissingProgramAssignment);

        Entity e = mock(Entity.class);
        when(e.getBody()).thenReturn(neutralRecord.getAttributes());
        when(e.getType()).thenReturn("teacherSchoolAssociation");

        validator.validate(e);

    }

    @Ignore
    @Test(expected = EntityValidationException.class)
    public void testInvalidTeacherSchoolAssociationMissingInstructionalGradeLevels() throws Exception {
        String smooksConfig = "smooks_conf/smooks-all-xml.xml";
        String targetSelector = "InterchangeStaffAssociation/TeacherSchoolAssociation";

        String invalidXmlMissingInstructionalGradeLevels =  "<InterchangeStaffAssociation xmlns=\"http://ed-fi.org/0100\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"Interchange-StaffAssociation.xsd\">"
                   + "<TeacherSchoolAssociation>"
                   + "<TeacherReference>"
                      + "<StaffIdentity>"
                          + "<StaffUniqueStateId>333333332</StaffUniqueStateId>"
                      + "</StaffIdentity>"
                   + "</TeacherReference>"
                   + "<SchoolReference>"
                      + "<EducationalOrgIdentity>"
                          + "<StateOrganizationId>123456111</StateOrganizationId>"
                      + "</EducationalOrgIdentity>"
                   + "</SchoolReference>"
                + "<ProgramAssignment>Title I-Academic</ProgramAssignment>"
                + "<AcademicSubjects>"
                    + "<AcademicSubject>English</AcademicSubject>"
                + "</AcademicSubjects>"
                + "</TeacherSchoolAssociation>"
                + "</InterchangeStaffAssociation>";

        NeutralRecord neutralRecord = EntityTestUtils.smooksGetSingleNeutralRecord(smooksConfig, targetSelector, invalidXmlMissingInstructionalGradeLevels);

        Entity e = mock(Entity.class);
        when(e.getBody()).thenReturn(neutralRecord.getAttributes());
        when(e.getType()).thenReturn("teacherSchoolAssociation");

        validator.validate(e);

    }

    @Ignore
    @Test(expected = EntityValidationException.class)
    public void testInvalidTeacherSchoolAssociationMissingAcademicSubjects() throws Exception {
        String smooksConfig = "smooks_conf/smooks-all-xml.xml";
        String targetSelector = "InterchangeStaffAssociation/TeacherSchoolAssociation";

        String invalidXmlMissingAcademicSubjects =  "<InterchangeStaffAssociation xmlns=\"http://ed-fi.org/0100\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"Interchange-StaffAssociation.xsd\">"
                   + "<TeacherSchoolAssociation>"
                   + "<TeacherReference>"
                      + "<StaffIdentity>"
                          + "<StaffUniqueStateId>333333332</StaffUniqueStateId>"
                      + "</StaffIdentity>"
                   + "</TeacherReference>"
                   + "<SchoolReference>"
                      + "<EducationalOrgIdentity>"
                           + "<StateOrganizationId>123456111</StateOrganizationId>"
                      + "</EducationalOrgIdentity>"
                   + "</SchoolReference>"
                + "<ProgramAssignment>Title I-Academic</ProgramAssignment>"
                + "<InstructionalGradeLevels>"
                   + "<GradeLevel>Ungraded</GradeLevel>"
                + "</InstructionalGradeLevels>"
                + "</TeacherSchoolAssociation>"
                + "</InterchangeStaffAssociation>";

        NeutralRecord neutralRecord = EntityTestUtils.smooksGetSingleNeutralRecord(smooksConfig, targetSelector, invalidXmlMissingAcademicSubjects);

        Entity e = mock(Entity.class);
        when(e.getBody()).thenReturn(neutralRecord.getAttributes());
        when(e.getType()).thenReturn("teacherSchoolAssociation");

        validator.validate(e);

    }

    @Ignore
    @Test(expected = EntityValidationException.class)
    public void testInvalidTeacherSchoolAssociationIncorrectEnum() throws Exception {
        String smooksConfig = "smooks_conf/smooks-all-xml.xml";
        String targetSelector = "InterchangeStaffAssociation/TeacherSchoolAssociation";

        String invalidXmIncorrectEnum =  "<InterchangeStaffAssociation xmlns=\"http://ed-fi.org/0100\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"Interchange-StaffAssociation.xsd\">"
        	    + "<TeacherSchoolAssociation>"
        	       + "<TeacherReference>"
        	          + "<StaffIdentity>"
        	             + "<StaffUniqueStateId>333333332</StaffUniqueStateId>"
        	          + "</StaffIdentity>"
        	       + "</TeacherReference>"
        	       + "<SchoolReference>"
        	          + "<EducationalOrgIdentity>"
        	             + "<StateOrganizationId>123456111</StateOrganizationId>"
        	          + "</EducationalOrgIdentity>"
        	       + "</SchoolReference>"
        	    + "<ProgramAssignment>Title I-Academics</ProgramAssignment>"
        	    + "<InstructionalGradeLevels>"
        	       + "<GradeLevel>Ungraded</GradeLevel>"
        	    + "</InstructionalGradeLevels>"
        	    + "<AcademicSubjects>"
        	       + "<AcademicSubject>English</AcademicSubject>"
        	    + "</AcademicSubjects>"
        	    + "</TeacherSchoolAssociation>"
        	    + "</InterchangeStaffAssociation>";

        NeutralRecord neutralRecord = EntityTestUtils.smooksGetSingleNeutralRecord(smooksConfig, targetSelector, invalidXmIncorrectEnum);

        Entity e = mock(Entity.class);
        when(e.getBody()).thenReturn(neutralRecord.getAttributes());
        when(e.getType()).thenReturn("teacherSchoolAssociation");

        validator.validate(e);

    }

    @Test
    public void testValidTeacherSchoolAssociaitionCSV() throws Exception {

        String smooksConfig = "smooks_conf/smooks-teacherSchoolAssociation-csv.xml";

        String targetSelector = "csv-record";

        String csvTestData = "333333332,123456111,Title I-Academic,Ungraded,English";

        NeutralRecord neutralRecord = EntityTestUtils.smooksGetSingleNeutralRecord(smooksConfig, targetSelector,
                csvTestData);

        checkValidTeacherSchoolAssociationNeutralRecord(neutralRecord);

    }

    @Test
    public void testValidTeacherSchoolAssociationXML() throws IOException, SAXException {
        String smooksConfig = "smooks_conf/smooks-all-xml.xml";

        String targetSelector = "InterchangeStaffAssociation/TeacherSchoolAssociation";

        NeutralRecord neutralRecord = EntityTestUtils.smooksGetSingleNeutralRecord(smooksConfig,
                targetSelector, xmlTestData);

        checkValidTeacherSchoolAssociationNeutralRecord(neutralRecord);
    }

    private void checkValidTeacherSchoolAssociationNeutralRecord(NeutralRecord neutralRecord) {

        Assert.assertEquals("333333332", neutralRecord.getAttributes().get("teacherReference"));

        List schoolReferences = (List) neutralRecord.getAttributes().get("schoolReference");
        Assert.assertTrue(schoolReferences != null);
        Assert.assertEquals("123456111", schoolReferences.get(0));

        Assert.assertEquals("Title I-Academic", neutralRecord.getAttributes().get("programAssignment"));

        Map gradelevels = (Map) neutralRecord.getAttributes().get("instructionalGradeLevels");
        Assert.assertTrue(gradelevels != null);
        List gradelevel = (List) gradelevels.get("gradeLevel");
        Assert.assertTrue(gradelevel != null);
        Assert.assertEquals("Ungraded", gradelevel.get(0));

        Map academicSubjects = (Map) neutralRecord.getAttributes().get("academicSubjects");
        Assert.assertTrue(academicSubjects != null);
        List academicSubject = (List) academicSubjects.get("academicSubject");
        Assert.assertTrue(academicSubject != null);
        Assert.assertEquals("English", academicSubject.get(0));
    }
}
