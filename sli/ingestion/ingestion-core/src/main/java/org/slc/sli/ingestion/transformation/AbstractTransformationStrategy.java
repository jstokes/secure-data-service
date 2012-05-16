package org.slc.sli.ingestion.transformation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slc.sli.ingestion.Job;
import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.WorkNote;
import org.slc.sli.ingestion.dal.NeutralRecordMongoAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Base TransformationStrategy.
 * 
 * @author dduran
 * 
 */
public abstract class AbstractTransformationStrategy implements TransformationStrategy {
    
    protected static final String BATCH_JOB_ID_KEY = "batchJobId";
    protected static final String TYPE_KEY = "type";
    
    private String batchJobId;
    private Job job;
    private WorkNote workNote;
    
    @Autowired
    private NeutralRecordMongoAccess neutralRecordMongoAccess;
    
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
     * @return Work Note containing information about collection and range to operate on.
     */
    public WorkNote getWorkNote() {
        return workNote;
    }
    
    /**
     * Sets the Work Note for this job.
     * @param workNote Work Note containing information about collection and range to operate on.
     */
    public void setWorkNote(WorkNote workNote) {
        this.workNote = workNote;
    }
    
    /**
     * Returns collection entities found in staging ingestion database. If a work note was not provided for 
     * the job, then all entities in the collection will be returned.
     *
     * @param collectionName name of collection to be queried for.
     */
    public Map<Object, NeutralRecord> getCollectionFromDb(String collectionName) {
        Criteria jobIdCriteria = Criteria.where(BATCH_JOB_ID_KEY).is(getBatchJobId());
        Query query = new Query(jobIdCriteria);
        
        Iterable<NeutralRecord> data;
        int max = 0;
        int skip = 0;
        if (getWorkNote() != null) {
            WorkNote note = getWorkNote();
            max = (int) (note.getRangeMaximum() - note.getRangeMinimum() + 1);
            skip = (int) note.getRangeMinimum();
        }         
        
        data = getNeutralRecordMongoAccess().getRecordRepository().findByQueryForJob(
                collectionName, query, getJob().getId(), skip, max);
        
        Map<Object, NeutralRecord> collection = new HashMap<Object, NeutralRecord>();
        NeutralRecord tempNr;

        Iterator<NeutralRecord> neutralRecordIterator = data.iterator();
        while (neutralRecordIterator.hasNext()) {
            tempNr = neutralRecordIterator.next();
            collection.put(tempNr.getRecordId(), tempNr);
        }
        return collection;
    }
}
