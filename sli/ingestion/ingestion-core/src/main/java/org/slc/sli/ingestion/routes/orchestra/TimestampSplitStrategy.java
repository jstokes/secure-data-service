package org.slc.sli.ingestion.routes.orchestra;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.ingestion.IngestionStagedEntity;
import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.WorkNote;
import org.slc.sli.ingestion.WorkNoteImpl;
import org.slc.sli.ingestion.dal.NeutralRecordMongoAccess;

/**
 * Implementation of SplitStrategy that uses the "creationTime" of an Entity to partition and create
 * WorkNotes.
 *
 * @author ifaybyshev
 *
 */
@Component
public class TimestampSplitStrategy implements SplitStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(TimestampSplitStrategy.class);

    @Value("${sli.ingestion.splitChunkSize}")
    private int splitChunkSize;

    private int thresholdPct = 30;

    @Autowired
    private NeutralRecordMongoAccess neutralRecordMongoAccess;

    @Override
    public List<WorkNote> splitForEntity(IngestionStagedEntity stagedEntity, String jobId) {
        List<WorkNote> workNotesForEntity = new ArrayList<WorkNote>();

        // potentially unsafe cast but it was decided that it is unlikely for us to hit max int
        // size for a staging db collection count.
        int numRecords = (int) neutralRecordMongoAccess.getRecordRepository().countForJob(
                stagedEntity.getCollectionNameAsStaged(), new NeutralQuery(), jobId);
        LOG.info("Records for collection {}: {}", stagedEntity.getCollectionNameAsStaged(), numRecords);

        long minTime = getMinCreationTimeForEntity(stagedEntity, jobId);
        long maxTime = getMaxCreationTimeForEntity(stagedEntity, jobId);

        if (!stagedEntity.getEdfiEntity().isSelfReferencing() && numRecords > splitChunkSize) {

            List<WorkNote> collectionWorkNotes = constructCollectionWorkNotes(new ArrayList<WorkNote>(), jobId,
                    stagedEntity, minTime, maxTime);

            Iterator<WorkNote> workNoteIterator = collectionWorkNotes.iterator();
            while (workNoteIterator.hasNext()) {
                WorkNote wn = workNoteIterator.next();
                wn.setBatchSize(collectionWorkNotes.size());
                workNotesForEntity.add(wn);
            }

            LOG.info("Entity split threshold reached. Splitting {} collection into {} batches of WorkNotes.",
                    stagedEntity.getCollectionNameAsStaged(), collectionWorkNotes.size());

        } else {
            LOG.info("Creating one WorkNote for collection: {}.", stagedEntity.getCollectionNameAsStaged());

            WorkNote workNote = WorkNoteImpl.createBatchedWorkNote(jobId, stagedEntity, minTime, maxTime, 1);
            workNotesForEntity.add(workNote);
        }
        return workNotesForEntity;
    }

    private List<WorkNote> constructCollectionWorkNotes(List<WorkNote> workNotes, String jobId,
            IngestionStagedEntity stagedEntity, long minTime, long maxTime) {

        String collectionName = stagedEntity.getCollectionNameAsStaged();

        long recordsCountInSegment = getCountOfRecords(collectionName, jobId, minTime, maxTime);

        if (chunkMatch(recordsCountInSegment, MatchEnumeration.good)) {
            // Current chunk is within acceptable threshold, add it to workNotes
            LOG.info("Adding unsplit chunk - it's within acceptable limits");
            WorkNote workNoteUnsplit = WorkNoteImpl.createBatchedWorkNote(jobId, stagedEntity, minTime, maxTime, 0);
            workNotes.add(workNoteUnsplit);

        } else {

            long intervalElapsedTime = maxTime - minTime;
            double splitFactor = 0.5;
            int splitAttempts = 0;

            boolean done = false;
            while (!done) {
                // check right interval
                long pivot = minTime + (long) (intervalElapsedTime * splitFactor);

                long recordsInRightChunk = getCountOfRecords(collectionName, jobId, pivot, maxTime);
                long recordsInLeftChunk = recordsCountInSegment - recordsInRightChunk;

                LOG.info("Interval = {} / {} ", minTime, maxTime);
                LOG.info("Total ms in interval = {}", intervalElapsedTime);
                LOG.info("Right Interval (Start / End) {} / {} ", pivot, maxTime);
                LOG.info("Splitting with records in left chunk = {}  and records in right chunk = {}, split factor = "
                        + splitFactor, recordsInLeftChunk, recordsInRightChunk);

                done = true;

                if (chunkMatch(recordsInRightChunk, MatchEnumeration.good)) {
                    // RIGHT SIDE IS GOOD

                    WorkNote right = WorkNoteImpl.createBatchedWorkNote(jobId, stagedEntity, pivot, maxTime, 0);
                    workNotes.add(right);

                    if (chunkMatch(recordsInLeftChunk, MatchEnumeration.good)
                            || chunkMatch(recordsInLeftChunk, MatchEnumeration.small)) {

                        LOG.info("Adding left + right work notes");

                        WorkNote left = WorkNoteImpl.createBatchedWorkNote(jobId, stagedEntity, minTime, pivot, 0);
                        workNotes.add(left);

                    } else if (chunkMatch(recordsInLeftChunk, MatchEnumeration.large)) {

                        LOG.info("Recursing on left + adding right work note");
                        constructCollectionWorkNotes(workNotes, jobId, stagedEntity, minTime, pivot);
                    }
                } else if (chunkMatch(recordsInRightChunk, MatchEnumeration.small)) {
                    // RIGHT SIDE IS TOO SMALL
                    if (chunkMatch(recordsInLeftChunk, MatchEnumeration.good)) {
                        LOG.info("Adding left + right work notes");
                        WorkNote left = WorkNoteImpl.createBatchedWorkNote(jobId, stagedEntity, minTime, pivot, 0);
                        workNotes.add(left);

                        WorkNote right = WorkNoteImpl.createBatchedWorkNote(jobId, stagedEntity, pivot, maxTime, 0);
                        workNotes.add(right);

                    } else if (chunkMatch(recordsInLeftChunk, MatchEnumeration.small)) {
                        LOG.info("Adding left + right work notes");
                        WorkNote left = WorkNoteImpl.createBatchedWorkNote(jobId, stagedEntity, minTime, pivot, 0);
                        workNotes.add(left);

                        WorkNote right = WorkNoteImpl.createBatchedWorkNote(jobId, stagedEntity, pivot, maxTime, 0);
                        workNotes.add(right);

                    } else if (chunkMatch(recordsInLeftChunk, MatchEnumeration.large)) {
                        LOG.info("Reintervaling - moving interval to the left (left side is too heavy)");
                        splitFactor = splitFactor / 2;
                        done = false;
                    }
                } else {
                    // RIGHT SIDE IS TOO LARGE
                    if (chunkMatch(recordsInLeftChunk, MatchEnumeration.good)) {
                        LOG.info("Adding left work note + recursing on the right");
                        WorkNote left = WorkNoteImpl.createBatchedWorkNote(jobId, stagedEntity, minTime, pivot, 0);
                        workNotes.add(left);

                        constructCollectionWorkNotes(workNotes, jobId, stagedEntity, pivot, maxTime);

                    } else if (chunkMatch(recordsInLeftChunk, MatchEnumeration.small)) {
                        LOG.info("Reintervaling - moving interval to the right (right side is too heavy)");
                        splitFactor = splitFactor + (splitFactor / 2);
                        done = false;

                    } else if (chunkMatch(recordsInLeftChunk, MatchEnumeration.large)) {
                        LOG.info("Recursing on both left and right");
                        constructCollectionWorkNotes(workNotes, jobId, stagedEntity, minTime, pivot);
                        constructCollectionWorkNotes(workNotes, jobId, stagedEntity, pivot, maxTime);
                    }
                }

                splitAttempts++;

                if (splitAttempts > 20 && !done) {
                    LOG.info("Split Max reached.");

                    if (chunkMatch(recordsInLeftChunk, MatchEnumeration.large)) {
                        LOG.info("Recursing on left");
                        constructCollectionWorkNotes(workNotes, jobId, stagedEntity, minTime, pivot);
                    } else {
                        LOG.info("Adding left work note");
                        WorkNote left = WorkNoteImpl.createBatchedWorkNote(jobId, stagedEntity, minTime, pivot, 0);
                        workNotes.add(left);
                    }

                    if (chunkMatch(recordsInRightChunk, MatchEnumeration.large)) {
                        LOG.info("Recursing on right");
                        constructCollectionWorkNotes(workNotes, jobId, stagedEntity, pivot, maxTime);
                    } else {
                        LOG.info("Adding right work note");
                        WorkNote right = WorkNoteImpl.createBatchedWorkNote(jobId, stagedEntity, pivot, maxTime, 0);
                        workNotes.add(right);
                    }

                    done = true;
                }
            }
        }

        return workNotes;
    }

    private boolean chunkMatch(long count, MatchEnumeration matchEnum) {
        return matchEnum == checkRecordsInChunk(count);
    }

    private MatchEnumeration checkRecordsInChunk(long count) {
        int splitChunkUpperBound = splitChunkSize * (1 + thresholdPct / 100);
        int splitChunkLowerBound = splitChunkSize * (1 - thresholdPct / 100);

        if (count <= splitChunkUpperBound) {
            if (count >= splitChunkLowerBound) {
                return MatchEnumeration.good;
            }
            return MatchEnumeration.small;
        }
        return MatchEnumeration.large;
    }

    private long getCountOfRecords(String collectionName, String jobId, long min, long max) {
        Criteria limiter = Criteria.where("creationTime").gte(min).lt(max);
        Query query = new Query().addCriteria(limiter);
        return neutralRecordMongoAccess.getRecordRepository().countForJob(collectionName, query, jobId);
    }

    private long getMaxCreationTimeForEntity(IngestionStagedEntity stagedEntity, String jobId) {
        return getCreationTimeForEntity(stagedEntity, jobId, Order.DESCENDING) + 2000;
    }

    private long getMinCreationTimeForEntity(IngestionStagedEntity stagedEntity, String jobId) {
        return getCreationTimeForEntity(stagedEntity, jobId, Order.ASCENDING);
    }

    private long getCreationTimeForEntity(IngestionStagedEntity stagedEntity, String jobId, Order order) {
        Query query = new Query();
        query.sort().on("creationTime", order);
        query.limit(1);
        Iterable<NeutralRecord> nr = neutralRecordMongoAccess.getRecordRepository().findByQueryForJob(
                stagedEntity.getCollectionNameAsStaged(), query, jobId);
        Iterator<NeutralRecord> nrIterator = nr.iterator();

        return nrIterator.next().getCreationTime();
    }

    private enum MatchEnumeration {
        good, small, large
    }

}
