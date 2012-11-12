/*
 * Copyright 2012 Shared Learning Collaborative, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.slc.sli.ingestion.smooks.mappings;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.util.EntityTestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

/**
 * Smooks test for AssessmentItem
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class AssessmentItemTest {

    @Value("${sli.ingestion.recordLevelDeltaEntities}")
    private String recordLevelDeltaEnabledEntityNames;

    private String validXmlTestData = "<InterchangeAssessmentMetadata xmlns=\"http://ed-fi.org/0100\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"Interchange-AssessmentMetadata.xsd\">"
            + "<AssessmentItem id='test-id'>"
            + "  <IdentificationCode>test-code</IdentificationCode>"
            + "  <ItemCategory>List Question</ItemCategory>"
            + "  <MaxRawScore>100</MaxRawScore>"
            + "  <CorrectResponse>Hello World!</CorrectResponse>"
            + "   <AssessmentReference>"
            + "    <AssessmentIdentity>"
            + "      <AssessmentTitle>c-aKzuT08</AssessmentTitle>"
            + "      <AcademicSubject>English</AcademicSubject>"
            + "      <GradeLevelAssessed>Postsecondary</GradeLevelAssessed>"
            + "      <Version>1</Version>"
            + "    </AssessmentIdentity>"
            + "  </AssessmentReference>"
            + "  <LearningStandardReference>"
            + "    <LearningStandardIdentity>"
            + "      <LearningStandardId ContentStandardName='Common Core'>"
            + "        <IdentificationCode>id-code-1</IdentificationCode>"
            + "      </LearningStandardId>"
            + "    </LearningStandardIdentity>"
            + "  </LearningStandardReference>"
            + "  <LearningStandardReference>"
            + "    <LearningStandardIdentity>"
            + "      <LearningStandardId ContentStandardName='Unusual Periphery'>"
            + "        <IdentificationCode>id-code-2</IdentificationCode>"
            + "      </LearningStandardId>"
            + "    </LearningStandardIdentity>"
            + "  </LearningStandardReference>"
            + "  <Nomenclature>nomen</Nomenclature>"
            + "</AssessmentItem>" + "</InterchangeAssessmentMetadata>";


    @SuppressWarnings("unchecked")
    @Test
    public void testLearningObjectiveXML() throws IOException, SAXException {
        String smooksConfig = "smooks_conf/smooks-all-xml.xml";
        String targetSelector = "InterchangeAssessmentMetadata/AssessmentItem";

        NeutralRecord nr = EntityTestUtils.smooksGetSingleNeutralRecord(smooksConfig, targetSelector, validXmlTestData, recordLevelDeltaEnabledEntityNames);
        Map<String, Object> m = nr.getAttributes();
        Assert.assertEquals("test-id", nr.getLocalId());
        Assert.assertEquals("test-code", m.get("identificationCode"));
        Assert.assertEquals("List Question", m.get("itemCategory"));
        Assert.assertEquals(100, m.get("maxRawScore"));
        Assert.assertEquals("Hello World!", m.get("correctResponse"));

        List<Map<String, Object>> refs = (List<Map<String, Object>>) nr.getAttributes().get("learningStandards");
        Assert.assertNotNull(refs);
        Assert.assertEquals(2, refs.size());
        Assert.assertEquals("id-code-1", refs.get(0).get("identificationCode"));
        Assert.assertEquals("Common Core", refs.get(0).get("contentStandardName"));
        Assert.assertEquals("id-code-2", refs.get(1).get("identificationCode"));
        Assert.assertEquals("Unusual Periphery", refs.get(1).get("contentStandardName"));
        
        Map<String, Object> assessmentRef = (Map<String, Object>) nr.getAttributes().get("assessmentReference");
        Assert.assertNotNull(assessmentRef);
        Map<String, Object> assessmentIdentity = (Map<String, Object>) assessmentRef.get("AssessmentIdentity");
        Assert.assertNotNull(assessmentIdentity);
        Assert.assertEquals("c-aKzuT08", assessmentIdentity.get("AssessmentTitle"));
        Assert.assertEquals("English", assessmentIdentity.get("AcademicSubject"));
        Assert.assertEquals("Postsecondary", assessmentIdentity.get("GradeLevelAssessed"));
        Assert.assertEquals(1, assessmentIdentity.get("Version"));

        Assert.assertEquals("nomen", m.get("nomenclature"));
    }
}
