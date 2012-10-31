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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
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
 * Test the smooks mappings for StaffCohortAssociation entity.
 *
 * @author slee
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class StaffCohortAssociationEntityTest {

    @Value("${sli.ingestion.recordLevelDeltaEntities}")
    private String recordLevelDeltaEnabledEntityNames;

    /**
     * Test that Ed-Fi staffCohortAssociation is correctly mapped to a NeutralRecord.
     *
     * @throws IOException
     * @throws SAXException
     */
    @Test
    public final void mapEdfiXmlToNeutralRecordTest() throws IOException, SAXException {

        String smooksXmlConfigFilePath = "smooks_conf/smooks-all-xml.xml";

        String targetSelector = "InterchangeStudentCohort/StaffCohortAssociation";

        String edfiXml = null;

        try {
            edfiXml = EntityTestUtils.readResourceAsString("smooks/unitTestData/StaffCohortAssociationEntity.xml");
        } catch (FileNotFoundException e) {
            System.err.println(e);
            Assert.fail();
        }

        NeutralRecord neutralRecord = EntityTestUtils.smooksGetSingleNeutralRecord(smooksXmlConfigFilePath,
                targetSelector, edfiXml, recordLevelDeltaEnabledEntityNames);

        checkValidNeutralRecord(neutralRecord);
    }

    @SuppressWarnings("unchecked")
    private void checkValidNeutralRecord(NeutralRecord neutralRecord) {
        assertEquals("Expecting different record type", "staffCohortAssociation", neutralRecord.getRecordType());

        assertEquals("Expected 0 local parent ids", 0, neutralRecord.getLocalParentIds().size());

        Map<String, Object> attributes = neutralRecord.getAttributes();
        assertEquals("Expected different number of attributes", 5, attributes.size());

        assertEquals("Expected different beginDate", "2011-01-01", attributes.get("beginDate"));
        assertEquals("Expected different endDate", "2011-01-01", attributes.get("endDate"));
        assertEquals("Expected different studentRecordAccess", Boolean.TRUE, attributes.get("studentRecordAccess"));

        Map<String, Object> staffReference = (Map<String, Object>) attributes.get("StaffReference");
        assertNotNull("Expected non-null staffReference map", staffReference);

        Map<String, Object> staffMap = (Map<String, Object>) staffReference.get("StaffIdentity");
        assertNotNull("Expected non-null staff identity map", staffMap);
        assertEquals("Expected different staffUniqueStateId", "linda.kim", staffMap.get("StaffUniqueStateId"));

        Map<String, Object> cohortReference = (Map<String, Object>) attributes.get("cohortReference");
        assertNotNull("Expected non-null list of cohortReferences", cohortReference);

        Map<String, Object> cohortIdentityMap = (Map<String, Object>) cohortReference.get("cohortIdentity");
        assertEquals("Expected different cohortIdentifier", "ACC-TEST-COH-1", cohortIdentityMap.get("cohortIdentifier"));
    }

}