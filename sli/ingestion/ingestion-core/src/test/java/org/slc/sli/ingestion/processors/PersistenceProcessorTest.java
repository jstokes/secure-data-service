/*
 * Copyright 2012-2013 inBloom, Inc. and its affiliates.
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

package org.slc.sli.ingestion.processors;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.slc.sli.ingestion.ActionVerb;
import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.model.RecordHash;
import org.slc.sli.ingestion.model.da.BatchJobDAO;


/**
 * Tests for PersistenceProcessor
 *
 * @author dduran
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/processor-test.xml" })
public class PersistenceProcessorTest {

    @Autowired
    PersistenceProcessor processor;

    @Test
    public void testRecordHashIngestedforSimpleEntity() {
        NeutralRecord originalRecord = createBaseNeutralRecord("simple");

        testRecordHashIngested(originalRecord, 1);
    }

    @Test
    public void testRecordHashIngestedforTransformedEntity() {
        NeutralRecord originalRecord = createBaseNeutralRecord("transformed");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> rhData = (List<Map<String, Object>>) originalRecord.getMetaDataByName("rhData");
        testRecordHashIngested(originalRecord, rhData.size());
    }

    @Test
    public void testRecordHashDeletedforTransformedEntity() {
        NeutralRecord originalRecord = createBaseNeutralRecord("transformed");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> rhData = (List<Map<String, Object>>) originalRecord.getMetaDataByName("rhData");
        testRecordHashIngested(originalRecord, rhData.size());
        originalRecord.setActionVerb( ActionVerb.CASCADE_DELETE);
        testRecordHashDeleted( originalRecord, rhData.size());
    }

    private void testRecordHashDeleted( NeutralRecord originalRecord, int count) {
        processor.upsertRecordHash(originalRecord);
        verify(processor.getBatchJobDAO(), times(count)).removeRecordHash(any(RecordHash.class));

    }

    private void testRecordHashIngested(NeutralRecord originalRecord, int count) {
        recordHashTestPreConfiguration();

        processor.upsertRecordHash(originalRecord);
        verify(processor.getBatchJobDAO(), times(count)).insertRecordHash(any(String.class), any(String.class));

        processor.upsertRecordHash(addRecordHashMetadata(originalRecord));
        verify(processor.getBatchJobDAO(), times(count)).updateRecordHash(any(RecordHash.class), any(String.class));
    }

    private  NeutralRecord addRecordHashMetadata(NeutralRecord originalRecord) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> rhDataList = (List<Map<String, Object>>)originalRecord.getMetaDataByName("rhData");
        for(Map<String, Object> rhDataItem: rhDataList) {
            Map<String, Object> hashData = new HashMap<String, Object>();
            hashData.put("id",         "id");
            hashData.put("hash",       "existingRecordHash");
            hashData.put("created",    new Long(1));
            hashData.put("updated",    new Long(1));
            hashData.put("version",    new Integer(1));
            hashData.put("tenantId",   "tenantId");
            rhDataItem.put("rhCurrentHash", hashData);
        }
        return originalRecord;
    }

    private void recordHashTestPreConfiguration() {
        BatchJobDAO batchJobDAO = Mockito.mock(BatchJobDAO.class);
        processor.setBatchJobDAO(batchJobDAO);

        Set<String> recordTypes = new HashSet<String>();
        recordTypes.add("recordType");
        processor.setRecordLvlHashNeutralRecordTypes(recordTypes);
    }

    private NeutralRecord createBaseNeutralRecord(String entityType) {
        NeutralRecord originalRecord = new NeutralRecord();
        originalRecord.setRecordType("recordType");

        List<Map<String, Object>> rhData = new ArrayList<Map<String, Object>>();

        if (entityType.equals("simple")) {
            Map<String, Object> rhDataElement = new HashMap<String, Object>();
            rhDataElement.put("rhId", "rhId1");
            rhDataElement.put("rhHash", "rhHash1");
            rhData.add(rhDataElement);
        } else if (entityType.equals("transformed")) {
            Map<String, Object> rhDataElement = new HashMap<String, Object>();
            rhDataElement.put("rhId", "rhId1");
            rhDataElement.put("rhHash", "rhHash1");
            rhData.add(rhDataElement);

            rhDataElement = new HashMap<String, Object>();
            rhDataElement.put("rhId", "rhId2");
            rhDataElement.put("rhHash", "rhHash2");
            rhData.add(rhDataElement);

            rhDataElement = new HashMap<String, Object>();
            rhDataElement.put("rhId", "rhId3");
            rhDataElement.put("rhHash", "rhHash3");
            rhData.add(rhDataElement);
        }

        originalRecord.addMetaData("rhData", rhData);
        originalRecord.addMetaData("rhTenantId", "rhTenantId");

        return originalRecord;
    }
}
