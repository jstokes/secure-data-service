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

package org.slc.sli.ingestion.model.da;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.Bytes;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.CursorPreparer;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import org.slc.sli.ingestion.IngestionStagedEntity;
import org.slc.sli.ingestion.model.Error;
import org.slc.sli.ingestion.model.NewBatchJob;
import org.slc.sli.ingestion.model.Stage;
import org.slc.sli.ingestion.queues.MessageType;

/**
 *
 * @author ldalgado
 *
 */
@Component
public class BatchJobMongoDA implements BatchJobDAO {

    private static final Logger LOG = LoggerFactory.getLogger(BatchJobMongoDA.class);

    private static final String BATCHJOB_ERROR_COLLECTION = "error";
    private static final String BATCHJOB_STAGE_SEPARATE_COLLECTION = "batchJobStage";
    private static final String BATCHJOB_NEW_BATCH_JOB_COLLECTION = "newBatchJob";

    private static final String ERROR = "error";
    private static final String WARNING = "warning";
    private static final String BATCHJOBID_FIELDNAME = "batchJobId";
    private static final String TRANSFORMATION_LATCH = "transformationLatch";
    private static final String PERSISTENCE_LATCH = "persistenceLatch";
    private static final String STAGED_ENTITIES = "stagedEntities";
    private static final int DUP_KEY_CODE = 11000;

    private MongoTemplate batchJobMongoTemplate;

    @Value("${sli.ingestion.errors.tracking}")
    private String trackIngestionErrors;

    @Value("${sli.ingestion.warnings.tracking}")
    private String trackIngestionWarnings;

    @Override
    public void saveBatchJob(NewBatchJob job) {
        if (job != null) {
            batchJobMongoTemplate.save(job);
        }
    }

    @Override
    public void saveBatchJobStage(String batchJobId, Stage stage) {
        stage.setJobId(batchJobId);
        batchJobMongoTemplate.save(stage, BATCHJOB_STAGE_SEPARATE_COLLECTION);
    }

    @Override
    public List<Stage> getBatchStagesStoredSeperatelly(String batchJobId) {
        return batchJobMongoTemplate.find(query(where("jobId").is(batchJobId)), Stage.class,
                BATCHJOB_STAGE_SEPARATE_COLLECTION);
    }

    @Override
    public NewBatchJob findBatchJobById(String batchJobId) {
        return batchJobMongoTemplate.findOne(query(where("_id").is(batchJobId)), NewBatchJob.class);
    }

    @Override
    public List<Error> findBatchJobErrors(String jobId) {
        List<Error> errors = batchJobMongoTemplate.find(query(where(BATCHJOBID_FIELDNAME).is(jobId)), Error.class,
                BATCHJOB_ERROR_COLLECTION);
        return errors;
    }

    public List<Error> findBatchJobErrors(String jobId, CursorPreparer cursorPreparer) {
        List<Error> errors = batchJobMongoTemplate.find(query(where(BATCHJOBID_FIELDNAME).is(jobId)), Error.class,
                cursorPreparer, BATCHJOB_ERROR_COLLECTION);
        return errors;
    }

    public NewBatchJob findLatestBatchJob() {
        Query query = new Query();
        query.sort().on("jobStartTimestamp", Order.DESCENDING);
        query.limit(1);
        List<NewBatchJob> sortedBatchJobs = batchJobMongoTemplate.find(query, NewBatchJob.class);
        if (sortedBatchJobs == null || sortedBatchJobs.size() == 0) {
            return null;
        }
        return batchJobMongoTemplate.find(query, NewBatchJob.class).get(0);
    }

    @Override
    public void saveError(Error error) {
        if (error != null && "true".equals(trackIngestionErrors) && ERROR.equalsIgnoreCase(error.getSeverity())) {
            batchJobMongoTemplate.save(error);
        }

        if (error != null && "true".equals(trackIngestionWarnings) && WARNING.equalsIgnoreCase(error.getSeverity())) {
            batchJobMongoTemplate.save(error);
        }
    }

    @Override
    public Iterable<Error> getBatchJobErrors(String jobId, int limit) {
        return new ErrorIterable(jobId, limit);
    }

    @Override
    public boolean attemptTentantLockForJob(String tenantId, String batchJobId) {
        if (tenantId != null && batchJobId != null) {

            try {
                BasicDBObject tenantLock = new BasicDBObject();
                tenantLock.put("_id", tenantId);
                tenantLock.put("batchJobId", batchJobId);

                batchJobMongoTemplate.getCollection("tenantJobLock").insert(tenantLock, WriteConcern.SAFE);
                return true;
            } catch (MongoException me) {
                if (me.getCode() == DUP_KEY_CODE) {
                    LOG.debug("Cannot obtain lock for tenant: {}", tenantId);
                } else {
                    throw me;
                }
            }

        } else {
            throw new IllegalArgumentException(
                    "Must specify a valid tenant id and batch job id for which to attempt lock.");
        }
        return false;
    }

    @Override
    public void releaseTenantLockForJob(String tenantId, String batchJobId) {
        if (tenantId != null && batchJobId != null) {

            BasicDBObject tenantLock = new BasicDBObject();
            tenantLock.put("_id", tenantId);
            tenantLock.put("batchJobId", batchJobId);

            batchJobMongoTemplate.getCollection("tenantJobLock").remove(tenantLock, WriteConcern.SAFE);

        } else {
            throw new IllegalArgumentException(
                    "Must specify a valid tenant id and batch job id for which to attempt lock release.");
        }
    }

    @Override
    public boolean createTransformationLatch(String jobId, String recordType, int count) {
        try {

            BasicDBObject latchObject = new BasicDBObject();
            latchObject.put("syncStage", MessageType.DATA_TRANSFORMATION.name());
            latchObject.put("jobId", jobId);
            latchObject.put("recordType", recordType);
            latchObject.put("count", count);
            batchJobMongoTemplate.getCollection(TRANSFORMATION_LATCH).insert(latchObject, WriteConcern.SAFE);

        } catch (MongoException me) {
            if (me.getCode() == DUP_KEY_CODE) {
                LOG.debug(me.getMessage());
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean createPersistanceLatch(List<Map<String, Object>> defaultPersistenceLatch, String jobId) {
        try {
            BasicDBObject latchObject = new BasicDBObject();
            latchObject.put("syncStage", MessageType.PERSIST_REQUEST.name());
            latchObject.put("jobId", jobId);
            latchObject.put("entities", defaultPersistenceLatch);
            batchJobMongoTemplate.getCollection(PERSISTENCE_LATCH).insert(latchObject, WriteConcern.SAFE);
        } catch (MongoException me) {
            if (me.getCode() == DUP_KEY_CODE) {
                LOG.debug(me.getMessage());
            }
            return false;
        }
        return true;

    }

    @Override
    public boolean countDownLatch(String syncStage, String jobId, String recordType) {
        if (syncStage.equals(MessageType.DATA_TRANSFORMATION.name())) {
            return countDownTransformationLatch(jobId, recordType);
        } else {
            return countDownPersistenceLatches(jobId, recordType);
        }
    }

    private boolean countDownTransformationLatch(String jobId, String recordType) {

        BasicDBObject query = new BasicDBObject();
        query.put("syncStage", MessageType.DATA_TRANSFORMATION.name());
        query.put("jobId", jobId);
        query.put("recordType", recordType);

        BasicDBObject decrementCount = new BasicDBObject("count", -1);
        BasicDBObject update = new BasicDBObject("$inc", decrementCount);

        DBObject latchObject = batchJobMongoTemplate.getCollection(TRANSFORMATION_LATCH).findAndModify(query, null, null,
                false, update, true, false);

        return (Integer) latchObject.get("count") <= 0;
    }

    @SuppressWarnings("unchecked")
    private boolean countDownPersistenceLatches(String jobId, String recordType) {

        BasicDBObject query = new BasicDBObject();
        query.put("syncStage", MessageType.PERSIST_REQUEST.name());
        query.put("jobId", jobId);
        query.put("entities.type", recordType);

        BasicDBObject decrementCount = new BasicDBObject("entities.$.count", -1);
        BasicDBObject update = new BasicDBObject("$inc", decrementCount);

        DBObject latchObject = batchJobMongoTemplate.getCollection(PERSISTENCE_LATCH).findAndModify(query, null, null,
                false, update, true, false);

         List<Map<String, Object>> entities = (List<Map<String, Object>>) latchObject.get("entities");

        boolean isEmpty = true;

        for (Map<String, Object> entityMap : entities) {
            int count = (Integer) entityMap.get("count");
            if (count > 0) {
                isEmpty = false;
            }
        }
        return isEmpty;
    }

    @Override
    public void setPersistenceLatchCount(String jobId, String collectionNameAsStaged, int size) {
        BasicDBObject query = new BasicDBObject();
        query.put("syncStage", MessageType.PERSIST_REQUEST.name());
        query.put("jobId", jobId);
        query.put("entities.type", collectionNameAsStaged);

        BasicDBObject decrementCount = new BasicDBObject("entities.$.count", size);
        BasicDBObject update = new BasicDBObject("$set", decrementCount);

        batchJobMongoTemplate.getCollection(PERSISTENCE_LATCH).findAndModify(query, null, null,
                false, update, true, false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<IngestionStagedEntity> getStagedEntitiesForJob(String jobId) {
        BasicDBObject query = new BasicDBObject();
        query.put("jobId", jobId);

        DBObject entities = batchJobMongoTemplate.getCollection(STAGED_ENTITIES).findOne(query);
        List<String> recordTypes = (List<String>) entities.get("recordTypes");

        Set<IngestionStagedEntity> stagedEntities = new HashSet<IngestionStagedEntity>();
        for (String recordType: recordTypes) {
            stagedEntities.add(IngestionStagedEntity.createFromRecordType(recordType));
        }

        return stagedEntities;

    }

    @Override
    public void setStagedEntitiesForJob(Set<IngestionStagedEntity> stagedEntities, String jobId) {
        List<String> recordTypes = IngestionStagedEntity.toEntityNames(stagedEntities);

        try {
            BasicDBObject entities = new BasicDBObject();
            entities.put("jobId", jobId);
            entities.put("recordTypes", recordTypes);

            batchJobMongoTemplate.getCollection(STAGED_ENTITIES).insert(entities);
        } catch (MongoException me) {
            if (me.getCode() == DUP_KEY_CODE) {
                LOG.debug(me.getMessage());
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean removeAllPersistedStagedEntitiesFromJob(String jobId) {
        DBCursor cursor = getWorkNoteLatchesForStage(jobId, MessageType.PERSIST_REQUEST.name());
        boolean isEmpty = false;

        while (cursor.hasNext()) {
            DBObject latch = cursor.next();
            List<Map<String, Object>> entities = (List<Map<String, Object>>) latch.get("entities");
            for (Map<String, Object> entityMap : entities) {
                   isEmpty = removeStagedEntityForJob((String) entityMap.get("type") , jobId);
            }

        }

        return isEmpty;

    }

    @SuppressWarnings("unchecked")
    public boolean removeStagedEntityForJob(String recordType, String jobId) {

        BasicDBObject query = new BasicDBObject();
        query.put("jobId", jobId);

        BasicDBObject decrementCount = new BasicDBObject("recordTypes", recordType);
        BasicDBObject update = new BasicDBObject("$pull", decrementCount);

        DBObject latchObject = batchJobMongoTemplate.getCollection(STAGED_ENTITIES).findAndModify(query, null, null,
                false, update, true, false);


        return ((List<String>) latchObject.get("recordTypes")).size() == 0;
    }

    private DBCursor getWorkNoteLatchesForStage(String jobId, String syncStage) {

        BasicDBObject ref = new BasicDBObject();
        ref.put("syncStage", syncStage);
        ref.put("jobId", jobId);

        DBCursor cursor;

        if (syncStage.equals(MessageType.DATA_TRANSFORMATION.name())) {
            cursor = batchJobMongoTemplate.getCollection(TRANSFORMATION_LATCH).find(ref);
        } else {
            cursor = batchJobMongoTemplate.getCollection(PERSISTENCE_LATCH).find(ref);
        }


        return cursor;

    }

    @Override
    public void cleanUpWorkNoteLatchAndStagedEntites(String jobId) {
        BasicDBObject dbObj = new BasicDBObject();
        dbObj.put("jobId", jobId);
        batchJobMongoTemplate.getCollection(TRANSFORMATION_LATCH).remove(dbObj);
        batchJobMongoTemplate.getCollection(PERSISTENCE_LATCH).remove(dbObj);
        batchJobMongoTemplate.getCollection(STAGED_ENTITIES).remove(dbObj);
    }

    /**
     * Iterable error class
     *
     * @author bsuzuki
     *
     */
    private class ErrorIterable implements Iterable<Error> {

        private static final int ERROR_QUERY_DEFAULT_LIMIT = 100;

        private String jobId = null;
        private int resultLimit = ERROR_QUERY_DEFAULT_LIMIT;

        public ErrorIterable(String jobId, int queryResultLimit) {
            this.jobId = jobId;
            this.resultLimit = queryResultLimit;
        }

        @Override
        public Iterator<Error> iterator() {
            return new ErrorIterator(jobId, resultLimit);
        }

        /**
         * Iterator for errors
         *
         * @author bsuzuki
         *
         */
        private final class ErrorIterator implements Iterator<Error> {
            private String jobId = null;
            private Iterator<Error> currentIterator;
            private long remainingResults = 0;

            private LimitedCursorPreparer cursorPreparer;

            private ErrorIterator(String jobId, int queryResultLimit) {
                this.jobId = jobId;
                this.cursorPreparer = new LimitedCursorPreparer(queryResultLimit);
                this.remainingResults = batchJobMongoTemplate.getCollection(BATCHJOB_ERROR_COLLECTION).count(
                        query(where(BATCHJOBID_FIELDNAME).is(jobId)).getQueryObject());
                // TODO use the following rather than the previous line when we upgrade to
                // mongotemplate 1.0.0.M5 or above
                // this.remainingResults =
                // batchJobMongoTemplate.count(query(where(BATCHJOBID_FIELDNAME).is(jobId)),
                // BATCHJOB_ERROR_COLLECTION);
                this.currentIterator = getNextList();
            }

            @Override
            public boolean hasNext() {
                return currentIterator.hasNext() || (remainingResults > 0);
            }

            @Override
            public Error next() {
                if (!currentIterator.hasNext()) {
                    currentIterator = getNextList();
                }
                return currentIterator.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            private Iterator<Error> getNextList() {
                List<Error> errors = batchJobMongoTemplate.find(query(where(BATCHJOBID_FIELDNAME).is(jobId)),
                        Error.class, cursorPreparer, BATCHJOB_ERROR_COLLECTION);
                remainingResults -= errors.size();
                return errors.iterator();
            }
        }

        /**
         * Prepares the cursor to be used when querying for errors
         *
         * @author bsuzuki
         *
         */
        private final class LimitedCursorPreparer implements CursorPreparer {

            private final int limit;
            private int position = 0;

            public LimitedCursorPreparer(int limit) {
                this.limit = limit;
            }

            @Override
            /**
             * set the skip and limit for the internal cursor to get the result
             * in chunks of up to 'limit' size
             */
            public DBCursor prepare(DBCursor cursor) {

                DBCursor cursorToUse = cursor;

                cursorToUse = cursorToUse.skip(position);
                cursorToUse = cursorToUse.limit(limit);
                cursorToUse.batchSize(1000);

                position += limit;

                cursorToUse.addOption(Bytes.QUERYOPTION_NOTIMEOUT);

                return cursorToUse;
            }

        }

    }

    public void setBatchJobMongoTemplate(MongoTemplate mongoTemplate) {
        this.batchJobMongoTemplate = mongoTemplate;
    }

}
