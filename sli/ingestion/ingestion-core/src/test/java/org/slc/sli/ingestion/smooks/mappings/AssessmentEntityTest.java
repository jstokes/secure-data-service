package org.slc.sli.ingestion.smooks.mappings;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.xml.sax.SAXException;

import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.util.EntityTestUtils;

/**
 * Test the smooks mappings for Assessment entity
 *
 * @author dduran
 *
 */
public class AssessmentEntityTest {

    // @Test
    // public void csvAssessmentTest() throws Exception {
    //
    // String smooksConfig = "smooks_conf/smooks-assessment-csv.xml";
    //
    // String targetSelector = "csv-record";
    //
    // String assessmentCsv = "";
    //
    // NeutralRecord neutralRecord = EntityTestUtils.smooksGetSingleNeutralRecord(smooksConfig,
    // targetSelector,
    // assessmentCsv);
    //
    // checkValidAssessmentNeutralRecord(neutralRecord);
    // }

    @Test
    public void edfiXmlAssessmentTest() throws IOException, SAXException {

        String smooksXmlConfigFilePath = "smooks_conf/smooks-all-xml.xml";

        String targetSelector = "InterchangeAssessmentMetadata/Assessment";

        String edfiAssessmentXml = "<InterchangeAssessmentMetadata xmlns=\"http://ed-fi.org/0100\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"Interchange-AssessmentMetadata.xsd\">"
                + "<Assessment id=\"TAKSReading3\">"
                + "  <AssessmentTitle>TAKS</AssessmentTitle>"
                + "  <AssessmentIdentificationCode IdentificationSystem=\"Test Contractor \" AssigningOrganizationCode=\"AssigningOrg\">"
                + "    <ID>TAKS 3rd Grade Reading</ID>"
                + "  </AssessmentIdentificationCode>"
                + "  <AssessmentCategory>State summative assessment 3-8 general</AssessmentCategory>"
                + "  <AcademicSubject>Reading</AcademicSubject>"
                + "  <GradeLevelAssessed>Third grade</GradeLevelAssessed>"
                + "  <LowestGradeLevelAssessed>Fourth grade</LowestGradeLevelAssessed>"
                + "  <AssessmentPerformanceLevel>"
                + "    <PerformanceLevel id=\"perf id\" ref=\"perf ref\">"
                + "      <CodeValue>the code value</CodeValue>"
                + "      <Description>TAKSMetStandard</Description>"
                + "    </PerformanceLevel>"
                + "    <AssessmentReportingMethod>Scale score</AssessmentReportingMethod>"
                + "    <MinimumScore>2100</MinimumScore>"
                + "    <MaximumScore>3500</MaximumScore>"
                + "  </AssessmentPerformanceLevel>"
                + "  <AssessmentPerformanceLevel>"
                + "    <PerformanceLevel>"
                + "      <CodeValue>the code value2</CodeValue>"
                + "      <Description>TAKSCommendedPerformance</Description>"
                + "    </PerformanceLevel>"
                + "    <AssessmentReportingMethod>Scale score2</AssessmentReportingMethod>"
                + "    <MaximumScore>3600</MaximumScore>"
                + "  </AssessmentPerformanceLevel>"
                + "  <ContentStandard>State Standard</ContentStandard>"
                + "  <AssessmentForm>Assessment Form</AssessmentForm>"
                + "  <Version>2002</Version>"
                + "  <RevisionDate>2002-09-01</RevisionDate>"
                + "  <MaxRawScore>36</MaxRawScore>"
                + "  <Nomenclature>the nomenclature</Nomenclature>"
                + "  <AssessmentPeriod id=\"theid\" ref=\"theref\">"
                + "    <CodeValue>code value</CodeValue>"
                + "    <ShortDescription>short desc</ShortDescription>"
                + "    <Description>descript</Description>"
                + "  </AssessmentPeriod>"
                + "  <AssessmentItemReference id=\"tk31\" ref=\"TAKSReading3-1\"/>"
                + "  <AssessmentItemReference id=\"tk32\" ref=\"TAKSReading3-2\"/>"
                + "  <ObjectiveAssessmentReference id=\"oar1\" ref=\"TAKSReading2-1\"/>"
                + "  <ObjectiveAssessmentReference id=\"oar2\" ref=\"TAKSReading2-2\"/>"
                + "  <AssessmentFamilyReference id=\"famid\" ref=\"famref\">"
                + "    <AssessmentFamilyIdentity>"
                + "      <AssessmentFamilyIdentificationCode IdentificationSystem=\"idsys\" AssigningOrganizationCode=\"orgcode\">"
                + "        <ID>1234</ID>"
                + "      </AssessmentFamilyIdentificationCode>"
                + "      <AssessmentFamilyIdentificationCode IdentificationSystem=\"idsys2\" AssigningOrganizationCode=\"orgcode2\">"
                + "        <ID>1235</ID>"
                + "      </AssessmentFamilyIdentificationCode>"
                + "      <AssessmentFamilyTitle>family title</AssessmentFamilyTitle>"
                + "      <Version>first</Version>"
                + "    </AssessmentFamilyIdentity>"
                + "  </AssessmentFamilyReference>"
                + "  <SectionReference id=\"myid\" ref=\"myref\">"
                + "    <SectionIdentity>"
                + "      <StateOrganizationId>6789</StateOrganizationId>"
                + "      <EducationOrgIdentificationCode IdentificationSystem=\"edorgsystem\">"
                + "        <ID>edorgid</ID>"
                + "      </EducationOrgIdentificationCode>"
                + "      <EducationOrgIdentificationCode IdentificationSystem=\"edorgsystem2\">"
                + "        <ID>edorgid2</ID>"
                + "      </EducationOrgIdentificationCode>"
                + "      <UniqueSectionCode>uniquesectioncode</UniqueSectionCode>"
                + "      <CourseCode IdentificationSystem=\"courseIdentificationSystem\" AssigningOrganizationCode=\"assigncode\">"
                + "        <ID>courseId</ID>"
                + "      </CourseCode>"
                + "      <LocalCourseCode>localcoursecode</LocalCourseCode>"
                + "      <SchoolYear>2012</SchoolYear>"
                + "      <Term>myterm</Term>"
                + "      <ClassPeriodName>classperiodname</ClassPeriodName>"
                + "      <Location>here</Location>"
                + "    </SectionIdentity>"
                + "  </SectionReference>"
                + "</Assessment></InterchangeAssessmentMetadata>";

        NeutralRecord neutralRecord = EntityTestUtils.smooksGetSingleNeutralRecord(smooksXmlConfigFilePath,
                targetSelector, edfiAssessmentXml);

        checkValidAssessmentNeutralRecord(neutralRecord);
    }

    @SuppressWarnings("rawtypes")
    private void checkValidAssessmentNeutralRecord(NeutralRecord assessmentNeutralRecord) {

        assertEquals("assessment", assessmentNeutralRecord.getRecordType());
        assertEquals("TAKS 3rd Grade Reading", assessmentNeutralRecord.getLocalId());
        assertEquals("TAKS", assessmentNeutralRecord.getAttributes().get("assessmentTitle"));

        List assessmentIdentificationCodeList = (List) assessmentNeutralRecord.getAttributes().get(
                "assessmentIdentificationCode");
        Map assessmentIdentificationCodeMap = (Map) assessmentIdentificationCodeList.get(0);
        EntityTestUtils.assertObjectInMapEquals(assessmentIdentificationCodeMap, "identificationSystem",
                "Test Contractor");
        EntityTestUtils.assertObjectInMapEquals(assessmentIdentificationCodeMap, "assigningOrganizationCode",
                "AssigningOrg");
        EntityTestUtils.assertObjectInMapEquals(assessmentIdentificationCodeMap, "ID", "TAKS 3rd Grade Reading");

        assertEquals("State summative assessment 3-8 general",
                assessmentNeutralRecord.getAttributes().get("assessmentCategory"));
        assertEquals("Reading", assessmentNeutralRecord.getAttributes().get("academicSubject"));
        assertEquals("Third grade", assessmentNeutralRecord.getAttributes().get("gradeLevelAssessed"));
        assertEquals("Fourth grade", assessmentNeutralRecord.getAttributes().get("lowestGradeLevelAssessed"));

        List assessmentPerformanceLevelList = (List) assessmentNeutralRecord.getAttributes().get(
                "assessmentPerformanceLevel");

        Map assessmentPerformanceLevelMap = (Map) assessmentPerformanceLevelList.get(0);
        Map performanceLevelMap = (Map) assessmentPerformanceLevelMap.get("performanceLevelDescriptor");
        EntityTestUtils.assertObjectInMapEquals(performanceLevelMap, "id", "perf id");
        EntityTestUtils.assertObjectInMapEquals(performanceLevelMap, "ref", "perf ref");
        EntityTestUtils.assertObjectInMapEquals(performanceLevelMap, "codeValue", "the code value");
        EntityTestUtils.assertObjectInMapEquals(performanceLevelMap, "description", "TAKSMetStandard");

        EntityTestUtils.assertObjectInMapEquals(assessmentPerformanceLevelMap, "assessmentReportingMethod",
                "Scale score");
        EntityTestUtils.assertObjectInMapEquals(assessmentPerformanceLevelMap, "minimumScore", 2100);
        EntityTestUtils.assertObjectInMapEquals(assessmentPerformanceLevelMap, "maximumScore", 3500);
        if (assessmentPerformanceLevelList.size() > 1) {
            // TODO: remove this if block when we support csv lists
            Map assessmentPerformanceLevelMap2 = (Map) assessmentPerformanceLevelList.get(1);
            Map performanceLevelMap2 = (Map) assessmentPerformanceLevelMap2.get("performanceLevelDescriptor");
            EntityTestUtils.assertObjectInMapEquals(performanceLevelMap2, "id", null);
            EntityTestUtils.assertObjectInMapEquals(performanceLevelMap2, "ref", null);
            EntityTestUtils.assertObjectInMapEquals(performanceLevelMap2, "codeValue", "the code value2");
            EntityTestUtils.assertObjectInMapEquals(performanceLevelMap2, "description", "TAKSCommendedPerformance");

            EntityTestUtils.assertObjectInMapEquals(assessmentPerformanceLevelMap2, "assessmentReportingMethod",
                    "Scale score2");
            EntityTestUtils.assertObjectInMapEquals(assessmentPerformanceLevelMap2, "minimumScore", null);
            EntityTestUtils.assertObjectInMapEquals(assessmentPerformanceLevelMap2, "maximumScore", 3600);
        }

        assertEquals("State Standard", assessmentNeutralRecord.getAttributes().get("contentStandard"));
        assertEquals("Assessment Form", assessmentNeutralRecord.getAttributes().get("assessmentForm"));
        assertEquals(2002, assessmentNeutralRecord.getAttributes().get("version"));
        assertEquals("2002-09-01", assessmentNeutralRecord.getAttributes().get("revisionDate"));
        assertEquals(36, assessmentNeutralRecord.getAttributes().get("maxRawScore"));
        assertEquals("the nomenclature", assessmentNeutralRecord.getAttributes().get("nomenclature"));

        assertEquals("code value", assessmentNeutralRecord.getAttributes().get("periodDescriptorRef"));
        assertEquals(Arrays.asList("TAKSReading2-1", "TAKSReading2-2"), assessmentNeutralRecord.getAttributes().get("objectiveAssessmentRefs"));


        /*
        List assessmentItemReferenceList = (List) assessmentNeutralRecord.getAttributes().get(
                "assessmentItemReferences");
        Map assessmentItemReferenceMap = (Map) assessmentItemReferenceList.get(0);
        EntityTestUtils.assertObjectInMapEquals(assessmentItemReferenceMap, "id", "tk31");
        EntityTestUtils.assertObjectInMapEquals(assessmentItemReferenceMap, "ref", "TAKSReading3-1");
        if (assessmentItemReferenceList.size() > 1) {
            // TODO: remove when we support csv lists
            Map assessmentItemReferenceMap2 = (Map) assessmentItemReferenceList.get(1);
            EntityTestUtils.assertObjectInMapEquals(assessmentItemReferenceMap2, "id", "tk32");
            EntityTestUtils.assertObjectInMapEquals(assessmentItemReferenceMap2, "ref", "TAKSReading3-2");
        }

        List objectiveAssessmentReferenceList = (List) assessmentNeutralRecord.getAttributes().get(
                "objectiveAssessmentReferences");
        Map objectiveAssessmentReferenceMap = (Map) objectiveAssessmentReferenceList.get(0);
        EntityTestUtils.assertObjectInMapEquals(objectiveAssessmentReferenceMap, "id", "oar1");
        EntityTestUtils.assertObjectInMapEquals(objectiveAssessmentReferenceMap, "ref", "TAKSReading2-1");
        if (objectiveAssessmentReferenceList.size() > 1) {
            // TODO: remove when we support csv lists
            Map objectiveAssessmentReferenceMap2 = (Map) objectiveAssessmentReferenceList.get(1);
            EntityTestUtils.assertObjectInMapEquals(objectiveAssessmentReferenceMap2, "id", "oar2");
            EntityTestUtils.assertObjectInMapEquals(objectiveAssessmentReferenceMap2, "ref", "TAKSReading2-2");
        }

        Map assessmentFamilyReferenceMap = (Map) assessmentNeutralRecord.getAttributes().get(
                "assessmentFamilyReference");
        EntityTestUtils.assertObjectInMapEquals(assessmentFamilyReferenceMap, "id", "famid");
        EntityTestUtils.assertObjectInMapEquals(assessmentFamilyReferenceMap, "ref", "famref");
        Map assessmentFamilyIdentityMap = (Map) assessmentFamilyReferenceMap.get("assessmentFamilyIdentity");
        EntityTestUtils.assertObjectInMapEquals(assessmentFamilyIdentityMap, "assessmentFamilyTitle", "family title");
        EntityTestUtils.assertObjectInMapEquals(assessmentFamilyIdentityMap, "version", "first");
        List assessmentFamilyIdentificationCodeList = (List) assessmentFamilyIdentityMap
                .get("assessmentFamilyIdentificationCodes");
        Map assessmentFamilyIdentificationCodeMap = (Map) assessmentFamilyIdentificationCodeList.get(0);
        EntityTestUtils.assertObjectInMapEquals(assessmentFamilyIdentificationCodeMap, "identificationSystem", "idsys");
        EntityTestUtils.assertObjectInMapEquals(assessmentFamilyIdentificationCodeMap, "assigningOrganizationCode",
                "orgcode");
        EntityTestUtils.assertObjectInMapEquals(assessmentFamilyIdentificationCodeMap, "ID", "1234");
        if (assessmentFamilyIdentificationCodeList.size() > 1) {
            // TODO: remove when we support csv lists
            Map assessmentFamilyIdentificationCodeMap2 = (Map) assessmentFamilyIdentificationCodeList.get(1);
            EntityTestUtils.assertObjectInMapEquals(assessmentFamilyIdentificationCodeMap2, "identificationSystem",
                    "idsys2");
            EntityTestUtils.assertObjectInMapEquals(assessmentFamilyIdentificationCodeMap2,
                    "assigningOrganizationCode", "orgcode2");
            EntityTestUtils.assertObjectInMapEquals(assessmentFamilyIdentificationCodeMap2, "ID", "1235");
        }
        
        List sectionReferenceTypeList = (List) assessmentNeutralRecord.getAttributes().get("sectionReferences");
        Map sectionReferenceTypeMap = (Map) sectionReferenceTypeList.get(0);
        EntityTestUtils.assertObjectInMapEquals(sectionReferenceTypeMap, "id", "myid");
        EntityTestUtils.assertObjectInMapEquals(sectionReferenceTypeMap, "ref", "myref");

        Map sectionIdentityTypeMap = (Map) sectionReferenceTypeMap.get("sectionIdentity");
        EntityTestUtils.assertObjectInMapEquals(sectionIdentityTypeMap, "stateOrganizationId", "6789");

        List educationOrgIdentificationCodeList = (List) sectionIdentityTypeMap.get("educationOrgIdentificationCodes");
        Map educationOrgIdentificationCodeMap = (Map) educationOrgIdentificationCodeList.get(0);
        EntityTestUtils.assertObjectInMapEquals(educationOrgIdentificationCodeMap, "identificationSystem",
                "edorgsystem");
        EntityTestUtils.assertObjectInMapEquals(educationOrgIdentificationCodeMap, "id", "edorgid");
        if (educationOrgIdentificationCodeList.size() > 1) {
            // TODO: remove once we support csv lists
            Map educationOrgIdentificationCodeMap2 = (Map) educationOrgIdentificationCodeList.get(1);
            EntityTestUtils.assertObjectInMapEquals(educationOrgIdentificationCodeMap2, "identificationSystem",
                    "edorgsystem2");
            EntityTestUtils.assertObjectInMapEquals(educationOrgIdentificationCodeMap2, "id", "edorgid2");
        }

        EntityTestUtils.assertObjectInMapEquals(sectionIdentityTypeMap, "uniqueSectionCode", "uniquesectioncode");
        EntityTestUtils.assertObjectInMapEquals(sectionIdentityTypeMap, "localCourseCode", "localcoursecode");
        EntityTestUtils.assertObjectInMapEquals(sectionIdentityTypeMap, "schoolYear", "2012");
        EntityTestUtils.assertObjectInMapEquals(sectionIdentityTypeMap, "term", "myterm");
        EntityTestUtils.assertObjectInMapEquals(sectionIdentityTypeMap, "classPeriodName", "classperiodname");
        EntityTestUtils.assertObjectInMapEquals(sectionIdentityTypeMap, "location", "here");

        Map courseCodeMap = (Map) sectionIdentityTypeMap.get("courseCode");
        EntityTestUtils.assertObjectInMapEquals(courseCodeMap, "identificationSystem", "courseIdentificationSystem");
        EntityTestUtils.assertObjectInMapEquals(courseCodeMap, "id", "courseId");
        */
    }

}
