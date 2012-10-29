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
 * Smooks test for StudentAssessmentItem
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class StudentAssessmentItemTests {

    @Value("${sli.ingestion.recordLevelDeltaEntities}")
    private String recordLevelDeltaEnabledEntityNames;

    private String validXmlTestData = "<InterchangeStudentAssessment xmlns=\"http://ed-fi.org/0100\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"Interchange-InterchangeStudentAssessment.xsd\">"
            + "<StudentAssessmentItem>"
            + "  <AssessmentResponse>response-1</AssessmentResponse>"
            + "  <ResponseIndicator>Effective response</ResponseIndicator>"
            + "  <AssessmentItemResult>Correct</AssessmentItemResult>"
            + "  <RawScoreResult>100</RawScoreResult>"
            + "  <StudentAssessmentReference ref='sass-ref' />"
            + "  <StudentObjectiveAssessmentReference ref='soa-ref' />"
            + "  <AssessmentItemReference>"
            + "    <AssessmentItemIdentity>"
            + "      <AssessmentItemIdentificationCode>aii-code</AssessmentItemIdentificationCode>"
            + "    </AssessmentItemIdentity>"
            + "  </AssessmentItemReference>"
            + "</StudentAssessmentItem>" + "</InterchangeStudentAssessment>";


    @SuppressWarnings("unchecked")
    @Test
    public void testLearningObjectiveXML() throws IOException, SAXException {
        String smooksConfig = "smooks_conf/smooks-all-xml.xml";
        String targetSelector = "InterchangeStudentAssessment/StudentAssessmentItem";

        NeutralRecord nr = EntityTestUtils.smooksGetSingleNeutralRecord(smooksConfig, targetSelector,
                validXmlTestData, recordLevelDeltaEnabledEntityNames);
        Map<String, Object> m = nr.getAttributes();
        Assert.assertEquals("response-1", m.get("assessmentResponse"));
        Assert.assertEquals("Effective response", m.get("responseIndicator"));
        Assert.assertEquals("Correct", m.get("assessmentItemResult"));
        Assert.assertEquals(100, m.get("rawScoreResult"));

        Map<String, Object> pIds = nr.getLocalParentIds();
        Assert.assertEquals("sass-ref", pIds.get("studentResultRef"));
        Assert.assertEquals("soa-ref", pIds.get("studentObjectiveAssessmentRef"));
        Assert.assertEquals("aii-code", pIds.get("assessmentItemIdentificatonCode"));
    }

}
