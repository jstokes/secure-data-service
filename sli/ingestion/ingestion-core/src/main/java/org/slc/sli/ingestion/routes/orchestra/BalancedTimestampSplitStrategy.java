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


package org.slc.sli.ingestion.routes.orchestra;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.slc.sli.ingestion.IngestionStagedEntity;
import org.slc.sli.ingestion.WorkNote;
import org.slc.sli.ingestion.dal.NeutralRecordAccess;

/**
 * Implementation of SplitStrategy that uses the "creationTime" of an Entity to partition and create
 * WorkNotes. A good pivot point is settled upon before recursively partitioning.
 *
 * @author dduran
 *
 */
@Component
public class BalancedTimestampSplitStrategy implements SplitStrategy {

    private static final int SINGLE_BATCH_SIZE = 1;

    @Value("${sli.ingestion.split.chunk.size}")
    private int splitChunkSize;

    @Value("${sli.ingestion.split.threshold.percentage}")
    private double splitThresholdPercentage;

    private NeutralRecordAccess neutralRecordAccess;

    @Override
    public List<WorkNote> splitForEntity(IngestionStagedEntity stagedEntity, String jobId) {
        List<WorkNote> workNotesForEntity;

        long minTime = neutralRecordAccess.getMinCreationTimeForEntity(stagedEntity, jobId);
        long maxTime = neutralRecordAccess.getMaxCreationTimeForEntity(stagedEntity, jobId);

        long numRecords = getCountOfRecords(stagedEntity.getCollectionNameAsStaged(), minTime, maxTime, jobId);

        if (!stagedEntity.getEdfiEntity().isSelfReferencing() && numRecords > splitChunkSize) {
            debug("Creating many WorkNotes for {} collection.", stagedEntity.getCollectionNameAsStaged());

            workNotesForEntity = partitionWorkNotes(minTime, maxTime, stagedEntity, jobId);

            for (WorkNote workNote : workNotesForEntity) {
                workNote.setBatchSize(workNotesForEntity.size());
            }

            info("Created {} WorkNotes for {}.", workNotesForEntity.size(),
                    stagedEntity.getCollectionNameAsStaged());
        } else {
            info("Creating one WorkNote for collection: {}.", stagedEntity.getCollectionNameAsStaged());
            workNotesForEntity = singleWorkNoteList(minTime, maxTime, numRecords, stagedEntity, jobId);
        }
        return workNotesForEntity;
    }

    /**
     * Recursively partition the insertion time ranges for the provided entity. Once within a margin
     * of the requested split chunk size, create a WorkNote. If we are unable to partition further
     * and have still not achieved the desired split chunk size, create a WorkNote anyway.
     *
     * @param min
     * @param max
     * @param stagedEntity
     * @param jobId
     * @return
     */
    private List<WorkNote> partitionWorkNotes(long min, long max, IngestionStagedEntity stagedEntity, String jobId) {
        String collectionName = stagedEntity.getCollectionNameAsStaged();
        long recordsInRange = getCountOfRecords(collectionName, min, max, jobId);

        if (recordsInRange <= splitChunkSize * (1.0 + splitThresholdPercentage) || (max - min <= 1)) {
            // we are within our target chunksize + margin.
            // OR we have a chunk size that cannot be partitioned further.
            debug("Creating WorkNote for {} with time range that contains {} records", stagedEntity, recordsInRange);
            return singleWorkNoteList(min, max, recordsInRange, stagedEntity, jobId);
        }

        long pivot = findGoodPivot(min, max, recordsInRange, collectionName, jobId);

        List<WorkNote> leftWorkNotes = partitionWorkNotes(min, pivot, stagedEntity, jobId);
        List<WorkNote> rightWorkNotes = partitionWorkNotes(pivot, max, stagedEntity, jobId);

        leftWorkNotes.addAll(rightWorkNotes);

        return leftWorkNotes;
    }

    /**
     * Try and bisect the provided time range such that the time ranges on either side of the pivot
     * have roughly equal number of records.
     *
     * @param minTime
     * @param maxTime
     * @param recordsInRange
     * @param collectionName
     * @param jobId
     * @return
     */
    private long findGoodPivot(long minTime, long maxTime, long recordsInRange, String collectionName, String jobId) {
        boolean pivotIsGood = false;
        long targetRecordCount = recordsInRange / 2;
        long pivotLowerBound = minTime;
        long pivotUpperBound = maxTime;

        long pivot = pivotLowerBound + ((pivotUpperBound - pivotLowerBound) / 2);
        while (!pivotIsGood) {
            long previousPivot = pivot;
            long recordsLeftOfPivot = getCountOfRecords(collectionName, minTime, pivot, jobId);

            if (recordsLeftOfPivot < targetRecordCount * (1.0 - splitThresholdPercentage)) {
                // move pivot right
                pivotLowerBound = pivot;
                pivot = pivot + ((pivotUpperBound - pivot) / 2);
            } else if (recordsLeftOfPivot > targetRecordCount * (1.0 + splitThresholdPercentage)) {
                // move pivot left
                pivotUpperBound = pivot;
                pivot = pivot - ((pivot - pivotLowerBound) / 2);
            } else {
                pivotIsGood = true;
            }

            if (!pivotIsGood && previousPivot == pivot) {
                // exit strategy when we can't find a 'truly' good pivot
                pivotIsGood = true;
            }
        }
        return pivot;
    }

    private List<WorkNote> singleWorkNoteList(long min, long max, long recordsInRange, IngestionStagedEntity entity,
            String jobId) {
        List<WorkNote> workNoteList = new ArrayList<WorkNote>();
        workNoteList.add(WorkNote.createBatchedWorkNote(jobId, entity, min, max, recordsInRange, SINGLE_BATCH_SIZE));
        return workNoteList;
    }

    private long getCountOfRecords(String collectionName, long min, long max, String jobId) {
        return neutralRecordAccess.countCreationTimeWithinRange(collectionName, min, max, jobId);
    }

    public void setNeutralRecordAccess(NeutralRecordAccess neutralRecordAccess) {
        this.neutralRecordAccess = neutralRecordAccess;
    }

    public void setSplitChunkSize(int splitChunkSize) {
        this.splitChunkSize = splitChunkSize;
    }

    public void setSplitThresholdPercentage(double splitThresholdPercentage) {
        this.splitThresholdPercentage = splitThresholdPercentage;
    }
}
