package org.slc.sli.ingestion.transformation.normalization.did;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;

/**
 * unit tests for DidSchemaParser
 *
 * @author jtully
 *
 */
public class DidSchemaParserTest {

    DidSchemaParser didSchemaParser;

    private static final String SECTION_TYPE = "section";
    private static final String EDORG_TYPE = "educationOrganization";
    private static final String SECTION_KEY_FIELD = "uniqueSectionCode";
    private static final String SECTION_SCHOOL_KEYFIELD = "schoolId";
    private static final String SCHOOL_KEYFIELD = "stateOrganizationId";


    @Before
    public void setup() {
        didSchemaParser = new DidSchemaParser();
        didSchemaParser.setResourceLoader(new DefaultResourceLoader());
        didSchemaParser.setXsdLocation("classpath:test-schema/Ed-Fi-Core.xsd");
        didSchemaParser.setExtensionXsdLocation("classpath:test-schema/SLI-Ed-Fi-Core.xsd");
        didSchemaParser.setup();
    }

    //TODO add exception path tests

    @Test
    public void shouldExtractCorrectRefConfigs() {
        Map<String, DidRefConfig> refConfigs = didSchemaParser.extractRefConfigs();
        Assert.assertEquals("Should extract 2 ref configs for the SLC section and edOrg referenceTypes", 2, refConfigs.size());
        Assert.assertTrue(refConfigs.containsKey(SECTION_TYPE));
        Assert.assertTrue(refConfigs.containsKey(EDORG_TYPE));

        DidRefConfig schoolRefConfig = refConfigs.get(EDORG_TYPE);
        Assert.assertNotNull(schoolRefConfig);
        Assert.assertEquals(EDORG_TYPE, schoolRefConfig.getEntityType());
        Assert.assertNotNull(schoolRefConfig.getKeyFields());
        Assert.assertEquals("nested schoolId ref should contain 1 key field", 1, schoolRefConfig.getKeyFields().size());

        KeyFieldDef school_stateOrgId = schoolRefConfig.getKeyFields().get(0);
        Assert.assertNotNull(school_stateOrgId);
        Assert.assertEquals(SCHOOL_KEYFIELD, school_stateOrgId.getKeyFieldName());
        Assert.assertEquals("EducationalOrgIdentity.StateOrganizationId", school_stateOrgId.getValueSource());
        Assert.assertNull("school stateOrgId should not contain a nested reference", school_stateOrgId.getRefConfig());

        DidRefConfig sectionRefConfig = refConfigs.get(SECTION_TYPE);
        Assert.assertNotNull(sectionRefConfig);
        Assert.assertEquals(sectionRefConfig.getEntityType(), SECTION_TYPE);

        Assert.assertNotNull(sectionRefConfig.getKeyFields());
        List<KeyFieldDef> keyFields = sectionRefConfig.getKeyFields();
        Assert.assertEquals("keyFields list should contain 2 keyfields", 2, keyFields.size());

        //create a map from the list because we don't care about the order of key fields
        Map<String, KeyFieldDef> keyFieldMap = new HashMap<String, KeyFieldDef>();
        for (KeyFieldDef keyField : keyFields) {
            keyFieldMap.put(keyField.getKeyFieldName(), keyField);
        }

        Assert.assertTrue(keyFieldMap.containsKey(SECTION_KEY_FIELD));
        KeyFieldDef uniqSectionCode = keyFieldMap.get(SECTION_KEY_FIELD);
        Assert.assertNotNull(uniqSectionCode);
        Assert.assertEquals(SECTION_KEY_FIELD, uniqSectionCode.getKeyFieldName());
        Assert.assertNull("uniqueSectionCode should not have a nested DID", uniqSectionCode.getRefConfig());
        Assert.assertEquals("SectionIdentity.UniqueSectionCode", uniqSectionCode.getValueSource());

        Assert.assertTrue(keyFieldMap.containsKey(SECTION_SCHOOL_KEYFIELD));
        KeyFieldDef schoolId = keyFieldMap.get(SECTION_SCHOOL_KEYFIELD);

        Assert.assertEquals(SECTION_SCHOOL_KEYFIELD, schoolId.getKeyFieldName());
        Assert.assertNotNull("schoolId should have a nested DID", schoolId.getRefConfig());
        Assert.assertNull("schoolId should not have a value source", schoolId.getValueSource());

        DidRefConfig nestedRefConfig = schoolId.getRefConfig();
        Assert.assertEquals(EDORG_TYPE, nestedRefConfig.getEntityType());
        Assert.assertNotNull(nestedRefConfig.getKeyFields());
        Assert.assertEquals("nested schoolId ref should contain 1 key field", 1, nestedRefConfig.getKeyFields().size());

        KeyFieldDef stateOrgId = nestedRefConfig.getKeyFields().get(0);
        Assert.assertNotNull(stateOrgId);
        Assert.assertEquals(SCHOOL_KEYFIELD, stateOrgId.getKeyFieldName());
        Assert.assertEquals("SectionIdentity.EducationalOrgReference.EducationalOrgIdentity.StateOrganizationId", stateOrgId.getValueSource());
        Assert.assertNull("nested stateOrgId should not contain a nested reference", stateOrgId.getRefConfig());
    }

    @Test
    public void shouldExtractCorrectEntityConfigs() {
        Map<String, DidEntityConfig> entityConfigs = didSchemaParser.extractEntityConfigs();

        Assert.assertEquals("Should extract 1 entity config for the 1 complexType containing a sectionReference (SLC-GradebookEntry)", 1, entityConfigs.size());

        //check the entity configs extracted are for the correct types
        Assert.assertTrue(entityConfigs.containsKey("SLC-GradebookEntry"));

        //test the entityConfig for StudentSectionAssociation
        DidEntityConfig GBEConfig = entityConfigs.get("SLC-GradebookEntry");
        Assert.assertNotNull(GBEConfig);
        Assert.assertNotNull(GBEConfig.getReferenceSources());

        List<DidRefSource> refSources = GBEConfig.getReferenceSources();
        Assert.assertEquals("entity config should contain a single DidRefSource (section)", 1, refSources.size());
        DidRefSource refSource = refSources.get(0);
        Assert.assertNotNull(refSource);
        Assert.assertEquals(refSource.getEntityType(), SECTION_TYPE);
        Assert.assertEquals(refSource.getSourceRefPath(), "body.SectionReference");
    }
}
