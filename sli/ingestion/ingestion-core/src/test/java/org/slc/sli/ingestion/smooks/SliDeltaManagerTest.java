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
package org.slc.sli.ingestion.smooks;
import static org.mockito.Matchers.any;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.slc.sli.common.domain.NaturalKeyDescriptor;
import org.slc.sli.common.util.tenantdb.TenantContext;
import org.slc.sli.common.util.uuid.DeterministicUUIDGeneratorStrategy;
import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.model.RecordHash;
import org.slc.sli.ingestion.model.da.BatchJobMongoDA;
import org.slc.sli.ingestion.reporting.AbstractMessageReport;
import org.slc.sli.ingestion.reporting.AbstractReportStats;
import org.slc.sli.ingestion.reporting.SimpleReportStats;
import org.slc.sli.ingestion.reporting.SimpleSource;
import org.slc.sli.ingestion.transformation.normalization.did.DeterministicIdResolver;

/**
 * Tests for SliDeltaManager
 *
 * @author ldalgado
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class SliDeltaManagerTest {
    @Mock
    private AbstractMessageReport errorReport;
    @Mock
    private BatchJobMongoDA mockBatchJobMongoDA;
    @Mock
    private DeterministicUUIDGeneratorStrategy mockDIdStrategy;
    @Mock
    private DeterministicIdResolver mockDidResolver;

    private static final String RECORD_DID = "theRecordId";

    AbstractReportStats reportStats = new SimpleReportStats(new SimpleSource("TestJob", "Resource", "StageName"));

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * TODO Unit test coverage for the following items:
     * (low) NoSuchAlgorithmException
     * (low) UnsupportedEncodingException
     */
    @Test
    public void testIsPreviouslyIngested()  {
        NeutralRecord originalRecord = createBaseNeutralRecord();

        NeutralRecord recordClone = (NeutralRecord) originalRecord.clone();

        TenantContext.setTenantId("tenantId");

        // Return a null to simulate no match for the record the first time we call isPreviouslyIngested
        Mockito.when(mockBatchJobMongoDA.findRecordHash(any(String.class), any(String.class))).thenReturn(null);

        // Return hash._id when we generate Did for this record
        Mockito.when(mockDIdStrategy.generateId(any(NaturalKeyDescriptor.class))).thenReturn(RECORD_DID);

        // Simulate a record being ingested the first time - should return false
        Assert.assertFalse(SliDeltaManager.isPreviouslyIngested(originalRecord, mockBatchJobMongoDA, mockDIdStrategy, mockDidResolver, errorReport, reportStats));
        // Confirm hash related metaData is updated
        confirmMetaDataUpdated(originalRecord);

        List<Map<String, String>> fData = (List<Map<String, String>>) originalRecord.getMetaData().get("rhData");
        String fTenantId = (String) originalRecord.getMetaData().get("rhTenantId");
        String fHash=fData.get(0).get("rhHash");
        String fRhId=fData.get(0).get("rhId");

        // Create the hash to be returned when simulating a recordHash match
        RecordHash hash = createRecordHash(fHash);

        // Return a hash to simulate a previously ingested record with the same Id in the recordHash
        Mockito.when(mockBatchJobMongoDA.findRecordHash(any(String.class), any(String.class))).thenReturn(hash);

        // Simulate a matching record being ingested - should return true
        Assert.assertTrue(SliDeltaManager.isPreviouslyIngested(recordClone, mockBatchJobMongoDA, mockDIdStrategy, mockDidResolver, errorReport, reportStats));
        // Confirm hash related metaData is updated
        confirmMetaDataUpdated(recordClone);

        List<Map<String, String>> sData = (List<Map<String, String>>) originalRecord.getMetaData().get("rhData");
        String sTenantId = (String) originalRecord.getMetaData().get("rhTenantId");
        String sHash=sData.get(0).get("rhHash");
        String sRhId=sData.get(0).get("rhId");

        // Confirm the rhId, rhHash, and rhTenantId values were populated consistently
        Assert.assertEquals(fRhId, sRhId);
        Assert.assertEquals(fHash, sHash);
        Assert.assertEquals(fTenantId, sTenantId);

    }

    @Test
    public void testIsPreviouslyIngestedModified()  {
        NeutralRecord originalRecord = createBaseNeutralRecord();

        NeutralRecord modifiedRecord = (NeutralRecord) originalRecord.clone();
        modifiedRecord.getAttributes().put("commonAttrib1", "commonAttrib1_modified_value");

        TenantContext.setTenantId("tenantId");

        // Return a null to simulate no match for the record the first time we call isPreviouslyIngested
        Mockito.when(mockBatchJobMongoDA.findRecordHash(any(String.class), any(String.class))).thenReturn(null);

        // Return hash._id when we generate Did for this record
        Mockito.when(mockDIdStrategy.generateId(any(NaturalKeyDescriptor.class))).thenReturn(RECORD_DID);

        // Simulate a record being ingested the first time - should return false
        Assert.assertFalse(SliDeltaManager.isPreviouslyIngested(originalRecord, mockBatchJobMongoDA, mockDIdStrategy, mockDidResolver, errorReport, reportStats));
        // Confirm hash related metaData is updated
        confirmMetaDataUpdated(originalRecord);

        List<Map<String, String>> fData = (List<Map<String, String>>) originalRecord.getMetaData().get("rhData");
        String fTenantId = (String) originalRecord.getMetaData().get("rhTenantId");
        String fHash=fData.get(0).get("rhHash");
        String fRhId=fData.get(0).get("rhId");

        // Create the hash to be returned when simulating a recordHash match
        RecordHash hash = createRecordHash(fHash);

        // Return a hash to simulate a previously ingested record with the same Id in the recordHash
        Mockito.when(mockBatchJobMongoDA.findRecordHash(any(String.class), any(String.class))).thenReturn(hash);

        // Simulate a matching record with updated attributes being ingested (i.e. different hash)
        Assert.assertFalse(SliDeltaManager.isPreviouslyIngested(modifiedRecord, mockBatchJobMongoDA, mockDIdStrategy, mockDidResolver, errorReport, reportStats));
        confirmMetaDataUpdated(modifiedRecord);

        List<Map<String, String>> sData = (List<Map<String, String>>) modifiedRecord.getMetaData().get("rhData");
        String sTenantId = (String) originalRecord.getMetaData().get("rhTenantId");
        String sHash=sData.get(0).get("rhHash");
        String sRhId=sData.get(0).get("rhId");

        // Confirm the rhId and rhTenantId values were populated consistently
        Assert.assertEquals(fRhId, sRhId);
        Assert.assertEquals(fTenantId, sTenantId);

        // Confirm the calculated hashes differ since the attribute values have changed

        Assert.assertFalse(fHash.equals(sHash));

    }

    private void confirmMetaDataUpdated(NeutralRecord record) {
        Assert.assertNotNull(((List<Map<String, String>>) record.getMetaData().get("rhData")).get(0).get("rhId"));
        Assert.assertNotNull(((List<Map<String, String>>) record.getMetaData().get("rhData")).get(0).get("rhHash"));
    }

    private RecordHash createRecordHash(String rHash) {
        RecordHash hash = new RecordHash();
        hash.setId(RECORD_DID);
        hash.setHash(rHash);
        hash.setCreated(12345);
        hash.setUpdated(23456);
        hash.setTenantId("tenantId");
        return hash;
    }

    private NeutralRecord createBaseNeutralRecord() {
        NeutralRecord originalRecord = new NeutralRecord();
        originalRecord.setRecordType("recordType");
        originalRecord.getMetaData().put(SliDeltaManager.NRKEYVALUEFIELDNAMES, "key1,key2");
        originalRecord.getMetaData().put(SliDeltaManager.OPTIONALNRKEYVALUEFIELDNAMES, "key3,key4");
        originalRecord.getAttributes().put("key1", "value1");
        originalRecord.getAttributes().put("key2", "value2");
        originalRecord.getAttributes().put("key3", "value3");
        originalRecord.getAttributes().put("commonAttrib1", "commonAttrib1_value");
        return originalRecord;
    }
}
