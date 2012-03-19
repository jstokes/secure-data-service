package org.slc.sli.ingestion.transformation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.slc.sli.dal.repository.MongoEntityRepository;
import org.slc.sli.domain.Entity;
import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.transformation.normalization.EntityConfigFactory;
import org.slc.sli.ingestion.transformation.normalization.IdNormalizer;
import org.slc.sli.ingestion.util.EntityTestUtils;
import org.slc.sli.ingestion.validation.DummyErrorReport;
import org.slc.sli.validation.EntityValidator;

/**
 * Unit Test for SmooksEdFi2SLITransformer
 *
 * @author okrook
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/transformation2-context.xml" })
public class SmooksEdFi2SLITransformerTest {

    @Autowired
    SmooksEdFi2SLITransformer transformer;

    @Autowired
    private EntityValidator validator;

    private static final String STUDENT_ID = "303A1";
    private static final String REGION_ID = "https://devapp1.slidev.org:443/sp";
    private static final String METADATA_BLOCK = "metaData";
    private static final String REGION_ID_FIELD = "idNamespace";
    private static final String EXTERNAL_ID_FIELD = "externalId";
    private static final String ASSESSMENT_TITLE = "assessmentTitle";

    @Test
    public void testDirectMapping() {
        NeutralRecord directlyMapped = new NeutralRecord();
        directlyMapped.setRecordType("directEntity");
        directlyMapped.setAttributeField("field2", "Test String");

        List<? extends Entity> result = transformer.transform(directlyMapped, new DummyErrorReport());

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("Test String", result.get(0).getBody().get("field1"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAssessmentMapping() {
        NeutralRecord assessment = new NeutralRecord();
        assessment.setRecordType("assessment");
        assessment.setAttributeField("assessmentTitle", "assessmentTitle");
        assessment.setAttributeField("assessmentFamilyHierarchyName", "assessmentFamilyHierarchyName");

        List<Map<String, Object>> assessmentIdentificationCodeList = new ArrayList<Map<String, Object>>();
        Map<String, Object> assessmentIdentificationCode1 = new HashMap<String, Object>();
        assessmentIdentificationCode1.put("ID", "202A1");
        assessmentIdentificationCode1.put("identificationSystem", "School");
        assessmentIdentificationCode1.put("assigningOrganizationCode", "assigningOrganizationCode");
        Map<String, Object> assessmentIdentificationCode2 = new HashMap<String, Object>();
        assessmentIdentificationCode2.put("ID", "303A1");
        assessmentIdentificationCode2.put("identificationSystem", "State");
        assessmentIdentificationCode2.put("assigningOrganizationCode", "assigningOrganizationCode2");
        assessmentIdentificationCodeList.add(assessmentIdentificationCode1);
        assessmentIdentificationCodeList.add(assessmentIdentificationCode2);
        assessment.setAttributeField("assessmentIdentificationCode", assessmentIdentificationCodeList);

        assessment.setAttributeField("assessmentCategory", "Achievement test");
        assessment.setAttributeField("academicSubject", "English");
        assessment.setAttributeField("gradeLevelAssessed", "Adult Education");
        assessment.setAttributeField("lowestGradeLevelAssessed", "Early Education");

        List<Map<String, Object>> assessmentPerformanceLevelList = new ArrayList<Map<String, Object>>();
        Map<String, Object> assessmentPerformanceLevel1 = new HashMap<String, Object>();
        assessmentPerformanceLevel1.put("maximumScore", "1600");
        assessmentPerformanceLevel1.put("minimumScore", "2400");
        assessmentPerformanceLevel1.put("assessmentReportingMethod", "C-scaled scores");
        Map<String, Object> performanceLevelDescriptor1 = new HashMap<String, Object>();
        performanceLevelDescriptor1.put("description", "description1");
        assessmentPerformanceLevel1.put("performanceLevelDescriptor", performanceLevelDescriptor1);

        Map<String, Object> assessmentPerformanceLevel2 = new HashMap<String, Object>();
        assessmentPerformanceLevel2.put("maximumScore", "1800");
        assessmentPerformanceLevel2.put("minimumScore", "2600");
        assessmentPerformanceLevel2.put("assessmentReportingMethod", "ACT score");
        Map<String, Object> performanceLevelDescriptor2 = new HashMap<String, Object>();
        performanceLevelDescriptor2.put("description", "description2");
        assessmentPerformanceLevel2.put("performanceLevelDescriptor", performanceLevelDescriptor2);

        assessmentPerformanceLevelList.add(assessmentPerformanceLevel1);
        assessmentPerformanceLevelList.add(assessmentPerformanceLevel2);
        assessment.setAttributeField("assessmentPerformanceLevel", assessmentPerformanceLevelList);


        assessment.setAttributeField("contentStandard", "SAT");
        assessment.setAttributeField("assessmentForm", "assessmentForm");
        assessment.setAttributeField("version", "1");
        assessment.setAttributeField("revisionDate", "1999-01-01");
        assessment.setAttributeField("maxRawScore", "2400");
        assessment.setAttributeField("nomenclature", "nomenclature");

        List<? extends Entity> result = transformer.transform(assessment, new DummyErrorReport());

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());

        Assert.assertEquals("assessmentTitle", result.get(0).getBody().get("assessmentTitle"));
        Assert.assertEquals("assessmentFamilyHierarchyName", result.get(0).getBody().get("assessmentFamilyHierarchyName"));

        List<Map<String, Object>> assessmentIDCodeList = (List<Map<String, Object>>) result.get(0).getBody().get("assessmentIdentificationCode");
        Assert.assertNotNull(assessmentIDCodeList);
        Assert.assertEquals(2, assessmentIDCodeList.size());
        Map<String, Object> assessmentIDCode1 = assessmentIDCodeList.get(0);
        Assert.assertNotNull(assessmentIDCode1);
        Assert.assertEquals("202A1", assessmentIDCode1.get("ID"));
        Assert.assertEquals("School", assessmentIDCode1.get("identificationSystem"));
        Assert.assertEquals("assigningOrganizationCode", assessmentIDCode1.get("assigningOrganizationCode"));

        Map<String, Object> assessmentIDCode2 = assessmentIDCodeList.get(1);
        Assert.assertNotNull(assessmentIDCode2);
        Assert.assertEquals("303A1", assessmentIDCode2.get("ID"));
        Assert.assertEquals("State", assessmentIDCode2.get("identificationSystem"));
        Assert.assertEquals("assigningOrganizationCode2", assessmentIDCode2.get("assigningOrganizationCode"));

        List<Map<String, Object>> assessmentPerfLevelList = (List<Map<String, Object>>) result.get(0).getBody().get("assessmentPerformanceLevel");
        Assert.assertNotNull(assessmentPerfLevelList);
        Assert.assertEquals(2, assessmentPerfLevelList.size());
        Map<String, Object> assessmentPerfLevel1 = assessmentPerfLevelList.get(0);
        Assert.assertNotNull(assessmentPerfLevel1);
        Assert.assertEquals(1600, assessmentPerfLevel1.get("maximumScore"));
        Assert.assertEquals(2400, assessmentPerfLevel1.get("minimumScore"));
        Assert.assertEquals("C-scaled scores", assessmentPerfLevel1.get("assessmentReportingMethod"));
        Map<String, Object> perfLevelDescriptor1 = (Map<String, Object>) assessmentPerfLevel1.get("performanceLevelDescriptor");
        Assert.assertNotNull(perfLevelDescriptor1);
        Assert.assertEquals("description1", perfLevelDescriptor1.get("description"));

        Map<String, Object> assessmentPerfLevel2 = assessmentPerfLevelList.get(1);
        Assert.assertNotNull(assessmentPerfLevel2);
        Assert.assertEquals(1800, assessmentPerfLevel2.get("maximumScore"));
        Assert.assertEquals(2600, assessmentPerfLevel2.get("minimumScore"));
        Assert.assertEquals("ACT score", assessmentPerfLevel2.get("assessmentReportingMethod"));
        Map<String, Object> perfLevelDescriptor2 = (Map<String, Object>) assessmentPerfLevel2.get("performanceLevelDescriptor");
        Assert.assertNotNull(perfLevelDescriptor2);
        Assert.assertEquals("description2", perfLevelDescriptor2.get("description"));

        Assert.assertEquals("Achievement test", result.get(0).getBody().get("assessmentCategory"));
        Assert.assertEquals("English", result.get(0).getBody().get("academicSubject"));
        Assert.assertEquals("Adult Education", result.get(0).getBody().get("gradeLevelAssessed"));
        Assert.assertEquals("Early Education", result.get(0).getBody().get("lowestGradeLevelAssessed"));
        Assert.assertEquals("SAT", result.get(0).getBody().get("contentStandard"));
        Assert.assertEquals("assessmentForm", result.get(0).getBody().get("assessmentForm"));
        Assert.assertEquals(1, result.get(0).getBody().get("version"));
        Assert.assertEquals("1999-01-01", result.get(0).getBody().get("revisionDate"));
        Assert.assertEquals(2400, result.get(0).getBody().get("maxRawScore"));
        Assert.assertEquals("nomenclature", result.get(0).getBody().get("nomenclature"));
   }

    @Test
    public void testAssessmentValidation() {
        NeutralRecord assessment = new NeutralRecord();
        assessment.setRecordType("assessment");
        assessment.setAttributeField("assessmentTitle", "assessmentTitle");
        assessment.setAttributeField("assessmentFamilyHierarchyName", "assessmentFamilyHierarchyName");

        List<Map<String, Object>> assessmentIdentificationCodeList = new ArrayList<Map<String, Object>>();
        Map<String, Object> assessmentIdentificationCode1 = new HashMap<String, Object>();
        assessmentIdentificationCode1.put("ID", "202A1");
        assessmentIdentificationCode1.put("identificationSystem", "School");
        assessmentIdentificationCode1.put("assigningOrganizationCode", "assigningOrganizationCode");
        Map<String, Object> assessmentIdentificationCode2 = new HashMap<String, Object>();
        assessmentIdentificationCode2.put("ID", "303A1");
        assessmentIdentificationCode2.put("identificationSystem", "State");
        assessmentIdentificationCode2.put("assigningOrganizationCode", "assigningOrganizationCode2");
        assessmentIdentificationCodeList.add(assessmentIdentificationCode1);
        assessmentIdentificationCodeList.add(assessmentIdentificationCode2);
        assessment.setAttributeField("assessmentIdentificationCode", assessmentIdentificationCodeList);

        assessment.setAttributeField("assessmentCategory", "Achievement test");
        assessment.setAttributeField("academicSubject", "English");
        assessment.setAttributeField("gradeLevelAssessed", "Adult Education");
        assessment.setAttributeField("lowestGradeLevelAssessed", "Early Education");

        List<Map<String, Object>> assessmentPerformanceLevelList = new ArrayList<Map<String, Object>>();
        Map<String, Object> assessmentPerformanceLevel1 = new HashMap<String, Object>();
        assessmentPerformanceLevel1.put("maximumScore", "1600");
        assessmentPerformanceLevel1.put("minimumScore", "2400");
        assessmentPerformanceLevel1.put("assessmentReportingMethod", "C-scaled scores");
        Map<String, Object> performanceLevelDescriptor1 = new HashMap<String, Object>();
        performanceLevelDescriptor1.put("description", "description1");
        assessmentPerformanceLevel1.put("performanceLevelDescriptor", performanceLevelDescriptor1);

        Map<String, Object> assessmentPerformanceLevel2 = new HashMap<String, Object>();
        assessmentPerformanceLevel2.put("maximumScore", "1800");
        assessmentPerformanceLevel2.put("minimumScore", "2600");
        assessmentPerformanceLevel2.put("assessmentReportingMethod", "ACT score");
        Map<String, Object> performanceLevelDescriptor2 = new HashMap<String, Object>();
        performanceLevelDescriptor2.put("description", "description2");
        assessmentPerformanceLevel2.put("performanceLevelDescriptor", performanceLevelDescriptor2);

        assessmentPerformanceLevelList.add(assessmentPerformanceLevel1);
        assessmentPerformanceLevelList.add(assessmentPerformanceLevel2);
        assessment.setAttributeField("assessmentPerformanceLevel", assessmentPerformanceLevelList);


        assessment.setAttributeField("contentStandard", "SAT");
        assessment.setAttributeField("assessmentForm", "assessmentForm");
        assessment.setAttributeField("version", 1);
        assessment.setAttributeField("revisionDate", "1999-01-01");
        assessment.setAttributeField("maxRawScore", "2400");
        assessment.setAttributeField("nomenclature", "nomenclature");

        List<? extends Entity> result = transformer.transform(assessment, new DummyErrorReport());

        Entity entity = result.get(0);

        EntityTestUtils.mapValidation(entity.getBody(), "assessment", validator);
    }


    /**
     * @author tke
     * Test the transformation and matching steps behave as expected
     */
    @Test
    public void testCreateAssessmentEntity() {
        EntityConfigFactory entityConfigurations = new EntityConfigFactory();
        MongoEntityRepository mockedEntityRepository = mock(MongoEntityRepository.class);
        NeutralRecord assessmentRC = createAssessmentNeutralRecord(true);

        entityConfigurations.setSearchPath(new ClassPathResource("/smooksEdFi2SLI/"));
        transformer.setEntityRepository(mockedEntityRepository);

        transformer.setEntityConfigurations(entityConfigurations);
        transformer.setIdNormalizer(new IdNormalizer());

        Map<String, String> assessmentFilterFields = new HashMap<String, String>();
        assessmentFilterFields.put("body.assessmentTitle", ASSESSMENT_TITLE);
        assessmentFilterFields.put(METADATA_BLOCK + "." + REGION_ID_FIELD, REGION_ID);
        assessmentFilterFields.put(METADATA_BLOCK + "." + EXTERNAL_ID_FIELD, STUDENT_ID);

        List<Entity> le = new ArrayList<Entity>();
        le.add(createAssessmentEntity(true));

        when(mockedEntityRepository.findByPaths("assessment", assessmentFilterFields)).thenReturn(le);

        List<SimpleEntity> res = transformer.handle(assessmentRC);

        verify(mockedEntityRepository).findByPaths("assessment", assessmentFilterFields);

        Assert.assertNotNull(res);
        Assert.assertEquals(ASSESSMENT_TITLE, res.get(0).getBody().get("assessmentTitle"));
        Assert.assertEquals(REGION_ID, res.get(0).getMetaData().get(REGION_ID_FIELD));
        Assert.assertEquals(STUDENT_ID, res.get(0).getMetaData().get(EXTERNAL_ID_FIELD));

    }

    /**
     * @author tke
     * @param setId : localId will be set if it is true
     * @return neutral record
     */
    private NeutralRecord createAssessmentNeutralRecord(boolean setId) {
        // Create neutral record for entity.
        NeutralRecord assessment = new NeutralRecord();

        if (setId)
            assessment.setLocalId(STUDENT_ID);

        //This will become IdNamespace field after transformed into neutral record entity
        assessment.setSourceId(REGION_ID);

        assessment.setRecordType("assessment");
        assessment.setAttributeField("assessmentTitle", "assessmentTitle");
        assessment.setAttributeField("assessmentFamilyHierarchyName", "assessmentFamilyHierarchyName");

        List<Map<String, Object>> assessmentIdentificationCodeList = new ArrayList<Map<String, Object>>();
        Map<String, Object> assessmentIdentificationCode1 = new HashMap<String, Object>();
        assessmentIdentificationCode1.put("ID", "202A1");
        assessmentIdentificationCode1.put("identificationSystem", "School");
        assessmentIdentificationCode1.put("assigningOrganizationCode", "assigningOrganizationCode");
        Map<String, Object> assessmentIdentificationCode2 = new HashMap<String, Object>();
        assessmentIdentificationCode2.put("ID", "303A1");
        assessmentIdentificationCode2.put("identificationSystem", "State");
        assessmentIdentificationCode2.put("assigningOrganizationCode", "assigningOrganizationCode2");
        assessmentIdentificationCodeList.add(assessmentIdentificationCode1);
        assessmentIdentificationCodeList.add(assessmentIdentificationCode2);
        assessment.setAttributeField("assessmentIdentificationCode", assessmentIdentificationCodeList);

        assessment.setAttributeField("assessmentCategory", "Achievement test");
        assessment.setAttributeField("academicSubject", "English");
        assessment.setAttributeField("gradeLevelAssessed", "Adult Education");
        assessment.setAttributeField("lowestGradeLevelAssessed", "Early Education");

        List<Map<String, Object>> assessmentPerformanceLevelList = new ArrayList<Map<String, Object>>();
        Map<String, Object> assessmentPerformanceLevel1 = new HashMap<String, Object>();
        assessmentPerformanceLevel1.put("maximumScore", "1600");
        assessmentPerformanceLevel1.put("minimumScore", "2400");
        assessmentPerformanceLevel1.put("assessmentReportingMethod", "C-scaled scores");
        Map<String, Object> performanceLevelDescriptor1 = new HashMap<String, Object>();
        performanceLevelDescriptor1.put("description", "description1");
        assessmentPerformanceLevel1.put("performanceLevelDescriptor", performanceLevelDescriptor1);

        Map<String, Object> assessmentPerformanceLevel2 = new HashMap<String, Object>();
        assessmentPerformanceLevel2.put("maximumScore", "1800");
        assessmentPerformanceLevel2.put("minimumScore", "2600");
        assessmentPerformanceLevel2.put("assessmentReportingMethod", "ACT score");
        Map<String, Object> performanceLevelDescriptor2 = new HashMap<String, Object>();
        performanceLevelDescriptor2.put("description", "description2");
        assessmentPerformanceLevel2.put("performanceLevelDescriptor", performanceLevelDescriptor2);

        assessmentPerformanceLevelList.add(assessmentPerformanceLevel1);
        assessmentPerformanceLevelList.add(assessmentPerformanceLevel2);
        assessment.setAttributeField("assessmentPerformanceLevel", assessmentPerformanceLevelList);

        assessment.setAttributeField("contentStandard", "SAT");
        assessment.setAttributeField("assessmentForm", "assessmentForm");
        assessment.setAttributeField("version", 1);
        assessment.setAttributeField("revisionDate", "1999-01-01");
        assessment.setAttributeField("maxRawScore", "2400");
        assessment.setAttributeField("nomenclature", "nomenclature");

        return assessment;
    }


    /**
     * @author tke
     * @param setId:entityId will be set if it is true
     * @return assessment entity
     */
    public SimpleEntity createAssessmentEntity(boolean setId) {
        SimpleEntity entity = new SimpleEntity();

        if (setId)
            entity.setEntityId(STUDENT_ID);
        entity.setType("assessment");
        Map<String, Object> field = new HashMap<String, Object>();
        field.put("studentUniqueStateId", STUDENT_ID);
        field.put("Sex", "Male");
        field.put("assessmentTitle", ASSESSMENT_TITLE);

        entity.setBody(field);
        entity.setMetaData(new HashMap<String, Object>());
        entity.getMetaData().put(REGION_ID_FIELD, REGION_ID);
        entity.getMetaData().put(EXTERNAL_ID_FIELD, STUDENT_ID);

        return entity;
    }

}
