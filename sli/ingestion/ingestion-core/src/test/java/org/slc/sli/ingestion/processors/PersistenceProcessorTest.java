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

package org.slc.sli.ingestion.processors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.slc.sli.ingestion.util.NeutralRecordUtils.getByPath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import org.slc.sli.ingestion.NeutralRecord;

/**
 * Tests for PersistenceProcessor
 *
 * @author dduran
 *
 */

public class PersistenceProcessorTest {

    @Test
    public void testSortLearningObjectivesByDependency() {
        List<NeutralRecord> unsortedRecords = new ArrayList<NeutralRecord>();
        unsortedRecords.add(createNeutralRecord("objective1", null));
        unsortedRecords.add(createNeutralRecord("objective3", "objective2"));
        unsortedRecords.add(createNeutralRecord("objective4", "objective3"));
        unsortedRecords.add(createNeutralRecord("objective5", "objective4"));
        unsortedRecords.add(createNeutralRecord("objective6", null));
        unsortedRecords.add(createNeutralRecord("objective7", "objective6"));

        // do n! runs with random shuffle. shuffle won't cover every combination but does enough
        for (int count = 0; count < factorial(unsortedRecords.size()); count++) {

            Collections.shuffle(unsortedRecords);

            List<NeutralRecord> sortedRecords = PersistenceProcessor
                    .sortLearningObjectivesByDependency(unsortedRecords);

            for (int i = 0; i < sortedRecords.size(); i++) {
                NeutralRecord sortedRecord = sortedRecords.get(i);
                String parentObjectiveId = (String) sortedRecord.getLocalParentIds().get("parentObjectiveId");

                assertParentNotLaterInList(sortedRecords, i + 1, parentObjectiveId);
            }
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testCyclicLearningObjectivesByDependency() throws javax.jms.IllegalStateException {
        List<NeutralRecord> unsortedRecords = new ArrayList<NeutralRecord>();
        unsortedRecords.add(createNeutralRecord("objective1", "objective4"));
        unsortedRecords.add(createNeutralRecord("objective2", "objective1"));
        unsortedRecords.add(createNeutralRecord("objective3", "objective2"));
        unsortedRecords.add(createNeutralRecord("objective4", "objective3"));

        // do n! runs with random shuffle. shuffle won't cover every combination but does enough
        for (int count = 0; count < factorial(unsortedRecords.size()); count++) {

            Collections.shuffle(unsortedRecords);

            List<NeutralRecord> sortedRecords = PersistenceProcessor
                    .sortLearningObjectivesByDependency(unsortedRecords);

            assertNotNull(sortedRecords);
        }
    }

    private void assertParentNotLaterInList(List<NeutralRecord> sortedRecords, int startIndex, String parentObjectiveId) {
        if (parentObjectiveId != null) {
            for (int i = startIndex; i < sortedRecords.size(); i++) {
                String sortedId = getByPath("learningObjectiveId.identificationCode", sortedRecords.get(i)
                        .getAttributes());

                assertFalse("parent should not be after child in insertion order.", parentObjectiveId.equals(sortedId));
            }
        }
    }

    private static NeutralRecord createNeutralRecord(String objectiveId, String parentObjectiveId) {
        String contentStandardName = "common core";

        Map<String, Object> learningObjectiveId = new HashMap<String, Object>();
        learningObjectiveId.put("identificationCode", objectiveId);
        learningObjectiveId.put("contentStandardName", contentStandardName);

        NeutralRecord record = new NeutralRecord();
        record.getAttributes().put("learningObjectiveId", learningObjectiveId);
        if (parentObjectiveId != null) {
            record.getLocalParentIds().put("parentObjectiveId", parentObjectiveId);
            record.getLocalParentIds().put("parentContentStandardName", contentStandardName);
        }
        return record;
    }

    private static int factorial(int n) {
        if (n == 0) {
            return 1;
        }
        return n * factorial(n - 1);
    }
}
