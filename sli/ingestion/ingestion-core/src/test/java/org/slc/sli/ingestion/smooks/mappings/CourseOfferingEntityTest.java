package org.slc.sli.ingestion.smooks.mappings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.util.EntityTestUtils;

/**
 * Test the smooks mappings for CourseOffering entity.
 *
 * @author srichards
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class CourseOfferingEntityTest {

    @Value("${sli.ingestion.recordLevelDeltaEntities}")
    private String recordLevelDeltaEnabledEntityNames;

    /**
     * Test that Ed-Fi entity is correctly mapped to a NeutralRecord.
     * @throws IOException
     * @throws SAXException
     */
    @Test
    public final void mapEdfiXmlToNeutralRecordTest() throws IOException, SAXException {

        String smooksXmlConfigFilePath = "smooks_conf/smooks-all-xml.xml";

        String targetSelector = "InterchangeMasterSchedule/CourseOffering";

        String edfiXml = null;

        try {
            edfiXml = EntityTestUtils.readResourceAsString("smooks/unitTestData/CourseOfferingEntity.xml");
        } catch (FileNotFoundException e) {
            System.err.println(e);
            Assert.fail();
        }

        NeutralRecord neutralRecord = EntityTestUtils
                .smooksGetSingleNeutralRecord(smooksXmlConfigFilePath,
                        targetSelector, edfiXml, recordLevelDeltaEnabledEntityNames);

        checkValidNeutralRecord(neutralRecord);
    }

    private void checkValidNeutralRecord(NeutralRecord neutralRecord) {
        assertEquals("Expecting different record type", "courseOffering", neutralRecord.getRecordType());

        Map<String, Object> attributes = neutralRecord.getAttributes();
        assertEquals("Expected different number of attributes", 7, attributes.size()); //TODO: hack - remove when DE608 is resolved
        //assertEquals("Expected different id", "Course Offering id", attributes.get("id"));
        assertEquals("Expected different LocalCourseCode", "Local Course Code", attributes.get("LocalCourseCode"));
        assertEquals("Expected different LocalCourseTitle", "Local Course Title", attributes.get("LocalCourseTitle"));

        //SchoolReference
        @SuppressWarnings("unchecked")
        Map<String, Map<String, Object>> schoolReference = (Map<String, Map<String, Object>>) attributes.get("SchoolReference");
        assertNotNull("Expected non-null SchoolReference", schoolReference);

        Map<String, Object> educationalOrgIdentity = schoolReference.get("EducationalOrgIdentity");
        assertNotNull("Expected non-null EducationalOrgIdentity", educationalOrgIdentity);
        assertEquals("Expected different StateOrganizationId", "School Reference State Organization Id", educationalOrgIdentity.get("StateOrganizationId"));

        @SuppressWarnings("unchecked")
        List<Map<String, String>> educationOrgIdentificationCodes = (List<Map<String, String>>) educationalOrgIdentity.get("EducationOrgIdentificationCode");
        assertNotNull("Expected non-null EducationOrgIdentificationCodes", educationOrgIdentificationCodes);
        assertEquals("Expected two EducationOrgIdentificationCodes", 2, educationOrgIdentificationCodes.size());

        Map<String, String> educationOrgIdentificationCode = educationOrgIdentificationCodes.get(0);
        assertNotNull("Expected non-null EducationOrgIdentificationCode", educationOrgIdentificationCode);
        assertEquals("Expected different IdentificationSystem", "School Reference Identification System 1", educationOrgIdentificationCode.get("IdentificationSystem"));
        assertEquals("Expected different ID", "School Reference ID 1", educationOrgIdentificationCode.get("ID"));

        educationOrgIdentificationCode = educationOrgIdentificationCodes.get(1);
        assertNotNull("Expected non-null EducationOrgIdentificationCode", educationOrgIdentificationCode);
        assertEquals("Expected different IdentificationSystem", "School Reference Identification System 2", educationOrgIdentificationCode.get("IdentificationSystem"));
        assertEquals("Expected different ID", "School Reference ID 2", educationOrgIdentificationCode.get("ID"));

        //SessionReference
        @SuppressWarnings("unchecked")
        Map<String, Map<String, Object>> sessionReference = (Map<String, Map<String, Object>>) attributes.get("SessionReference");
        assertNotNull("Expected non-null SessionReference", sessionReference);

        Map<String, Object> sessionIdentity = sessionReference.get("SessionIdentity");
        assertNotNull("Expected non-null SessionIdentity", sessionIdentity);
        assertEquals("Expected different SessionName", "Session Name", sessionIdentity.get("SessionName"));
        assertEquals("Expected different SchoolYear", "School Year", sessionIdentity.get("SchoolYear"));
        assertEquals("Expected different Term", "My Term", sessionIdentity.get("Term"));

        @SuppressWarnings("unchecked")
        List<String> stateOrganizationIds = (List<String>) sessionIdentity.get("StateOrganizationId");
        assertNotNull("Expected non-null StateOrganizationIds", stateOrganizationIds);
        assertEquals("Expected two StateOrganizationIds", 2, stateOrganizationIds.size());

        String stateOrganizationId = stateOrganizationIds.get(0);
        assertNotNull("Expected non-null StateOrganizationId", stateOrganizationId);
        assertEquals("Expected different StateOrganizationId", "Session Reference State Organization Id 1", stateOrganizationId);

        stateOrganizationId = stateOrganizationIds.get(1);
        assertNotNull("Expected non-null StateOrganizationId", stateOrganizationId);
        assertEquals("Expected different StateOrganizationId", "Session Reference State Organization Id 2", stateOrganizationId);

        educationOrgIdentificationCodes = (List<Map<String, String>>) sessionIdentity.get("EducationOrgIdentificationCode");
        assertNotNull("Expected non-null EducationOrgIdentificationCodes", educationOrgIdentificationCodes);
        assertEquals("Expected two EducationOrgIdentificationCodes", 2, educationOrgIdentificationCodes.size());

        educationOrgIdentificationCode = educationOrgIdentificationCodes.get(0);
        assertNotNull("Expected non-null EducationOrgIdentificationCode", educationOrgIdentificationCode);
        assertEquals("Expected different IdentificationSystem", "Session Reference Identification System 1", educationOrgIdentificationCode.get("IdentificationSystem"));
        assertEquals("Expected different ID", "Session Reference ID 1", educationOrgIdentificationCode.get("ID"));

        educationOrgIdentificationCode = educationOrgIdentificationCodes.get(1);
        assertNotNull("Expected non-null EducationOrgIdentificationCode", educationOrgIdentificationCode);
        assertEquals("Expected different IdentificationSystem", "Session Reference Identification System 2", educationOrgIdentificationCode.get("IdentificationSystem"));
        assertEquals("Expected different ID", "Session Reference ID 2", educationOrgIdentificationCode.get("ID"));

        //CourseReference
        @SuppressWarnings("unchecked")
        Map<String, Map<String, Object>> courseReference = (Map<String, Map<String, Object>>) attributes.get("CourseReference");
        assertNotNull("Expected non-null CourseReference", courseReference);
        //assertEquals("Expected different id", "Course Reference id", courseReference.get("id"));
        //assertEquals("Expected different ref", "Course Reference ref", courseReference.get("ref"));

        Map<String, Object> courseIdentity = courseReference.get("CourseIdentity");
        assertNotNull("Expected non-null CourseIdentity", courseIdentity);

        @SuppressWarnings("unchecked")
        List<Map<String, String>> courseCodes = (List<Map<String, String>>) courseIdentity.get("CourseCode");
        assertNotNull("Expected non-null CourseCodes", courseCodes);
        assertEquals("Expected two CourseCodes", 2, courseCodes.size());

        Map<String, String> courseCode = courseCodes.get(0);
        assertNotNull("Expected non-null CourseCode", courseCode);
        assertEquals("Expected different IdentificationSystem", "Course Reference Identification System 1", courseCode.get("IdentificationSystem"));
        assertEquals("Expected different AssigningOrganizationCode", "Course Reference Assigning Organization Code 1", courseCode.get("AssigningOrganizationCode"));
        assertEquals("Expected different ID", "Course Reference ID 1", courseCode.get("ID"));

        courseCode = courseCodes.get(1);
        assertNotNull("Expected non-null CourseCode", courseCode);
        assertEquals("Expected different IdentificationSystem", "Course Reference Identification System 2", courseCode.get("IdentificationSystem"));
        assertEquals("Expected different AssigningOrganizationCode", "Course Reference Assigning Organization Code 2", courseCode.get("AssigningOrganizationCode"));
        assertEquals("Expected different ID", "Course Reference ID 2", courseCode.get("ID"));
    }
}