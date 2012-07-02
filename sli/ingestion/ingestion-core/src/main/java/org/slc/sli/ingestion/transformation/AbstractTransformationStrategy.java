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


package org.slc.sli.ingestion.transformation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.ingestion.BatchJobStageType;
import org.slc.sli.ingestion.Job;
import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.WorkNote;
import org.slc.sli.ingestion.dal.NeutralRecordMongoAccess;
import org.slc.sli.ingestion.model.da.BatchJobDAO;
import org.slc.sli.ingestion.validation.DatabaseLoggingErrorReport;
import org.slc.sli.ingestion.validation.ErrorReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Base TransformationStrategy.
 * 
 * @author dduran
 * @author shalka
 */
public abstract class AbstractTransformationStrategy implements TransformationStrategy {
    
    private static final Logger LOG = LoggerFactory.getLogger(AbstractTransformationStrategy.class);
    
    protected static final String BATCH_JOB_ID_KEY = "batchJobId";
    protected static final String TYPE_KEY = "type";
    
    private String batchJobId;
    private Job job;
    private WorkNote workNote;
    
    @Autowired
    private NeutralRecordMongoAccess neutralRecordMongoAccess;
    
    @Autowired
    private BatchJobDAO batchJobDAO;
    
    @Override
    public void perform(Job job) {
        this.setJob(job);
        this.setBatchJobId(job.getId());
        this.performTransformation();
    }
    
    @Override
    public void perform(Job job, WorkNote workNote) {
        this.setJob(job);
        this.setBatchJobId(job.getId());
        this.setWorkNote(workNote);
        this.performTransformation();
    }
    
    protected abstract void performTransformation();
    
    /**
     * @return the neutralRecordMongoAccess
     */
    public NeutralRecordMongoAccess getNeutralRecordMongoAccess() {
        return neutralRecordMongoAccess;
    }
    
    /**
     * @param neutralRecordMongoAccess
     *            the neutralRecordMongoAccess to set
     */
    public void setNeutralRecordMongoAccess(NeutralRecordMongoAccess neutralRecordMongoAccess) {
        this.neutralRecordMongoAccess = neutralRecordMongoAccess;
    }
    
    public String getBatchJobId() {
        return batchJobId;
    }
    
    public ErrorReport getErrorReport(String fileName) {
        return new DatabaseLoggingErrorReport(this.batchJobId, BatchJobStageType.TRANSFORMATION_PROCESSOR, fileName,
                this.batchJobDAO);
    }
    
    public void setBatchJobId(String batchJobId) {
        this.batchJobId = batchJobId;
    }
    
    public Job getJob() {
        return job;
    }
    
    public void setJob(Job job) {
        this.job = job;
    }
    
    /**
     * Gets the Work Note corresponding to this job.
     * 
     * @return Work Note containing information about collection and range to operate on.
     */
    public WorkNote getWorkNote() {
        return workNote;
    }
    
    /**
     * Sets the Work Note for this job.
     * 
     * @param workNote
     *            Work Note containing information about collection and range to operate on.
     */
    public void setWorkNote(WorkNote workNote) {
        this.workNote = workNote;
    }
    
    /**
     * Returns collection entities found in staging ingestion database. If a work note was not
     * provided for
     * the job, then all entities in the collection will be returned.
     * 
     * @param collectionName
     *            name of collection to be queried for.
     */
    public Map<Object, NeutralRecord> getCollectionFromDb(String collectionName) {
        WorkNote workNote = getWorkNote();
        
        Query query = buildCreationTimeQuery(workNote);
        
        Iterable<NeutralRecord> data = getNeutralRecordMongoAccess().getRecordRepository().findByQueryForJob(
                collectionName, query, getJob().getId());
        
        if (!data.iterator().hasNext()) {
            LOG.warn("Pulled nothing from {}", collectionName);
            LOG.warn("Total for {}: {}", collectionName, getNeutralRecordMongoAccess().getRecordRepository()
                    .countForJob(collectionName, new NeutralQuery(0), getJob().getId()));
        }
        
        Map<Object, NeutralRecord> collection = iterableResultsToMap(data);
        
        if (collection.size() != workNote.getRecordsInRange()) {
            LOG.error("Number of records in creationTime query result ({}) does not match resultsInRange of {} ",
                    collection.size(), workNote);
        }
        
        return collection;
    }
    
    /**
     * Invokes the 'insert' mongo operation. Use when concurrent writes are known to provide
     * uniqueness (one-to-one mapping between original and _transformed collection).
     * 
     * @param record
     *            Neutral Record to be written to data store.
     */
    public void insertRecord(NeutralRecord record) {
        neutralRecordMongoAccess.getRecordRepository().insertForJob(record, job.getId());
    }
    
    /**
     * Invokes the 'insert' mongo operation (for multiple records). Use when concurrent writes are
     * known to provide uniqueness (one-to-one mapping between original and _transformed
     * collection).
     * 
     * @param records
     *            Neutral Records to be written to data store.
     * @param collectionName
     *            Collection to write Neutral Records to in data store.
     */
    public void insertRecords(List<NeutralRecord> records, String collectionName) {
        neutralRecordMongoAccess.getRecordRepository().insertAllForJob(records, collectionName, job.getId());
        LOG.info("Successfully persisted {} records for collection: {}", records.size(), collectionName);
    }
    
    /**
     * Invokes the 'upsert' mongo operation. Use when concurrent writes fail to provide uniqueness
     * (for instance, when many record are being condensed into a small subset of records).
     * 
     * @param record
     *            Neutral Record to be written to data store.
     */
    public void createRecord(NeutralRecord record) {
        neutralRecordMongoAccess.getRecordRepository().createForJob(record, job.getId());
    }
    
    private Query buildCreationTimeQuery(WorkNote note) {
        Query query = new Query().limit(0);
        if (note.getBatchSize() == 1) {
            Criteria limiter = Criteria.where("creationTime").gt(0);
            query.addCriteria(limiter);
        } else {
            Criteria limiter = Criteria.where("creationTime").gte(note.getRangeMinimum()).lt(note.getRangeMaximum());
            query.addCriteria(limiter);
        }
        return query;
    }
    
    private Map<Object, NeutralRecord> iterableResultsToMap(Iterable<NeutralRecord> data) {
        Map<Object, NeutralRecord> collection = new HashMap<Object, NeutralRecord>();
        NeutralRecord tempNr = null;
        
        Iterator<NeutralRecord> neutralRecordIterator = data.iterator();
        while (neutralRecordIterator.hasNext()) {
            tempNr = neutralRecordIterator.next();
            collection.put(tempNr.getRecordId(), tempNr);
        }
        return collection;
    }
}
