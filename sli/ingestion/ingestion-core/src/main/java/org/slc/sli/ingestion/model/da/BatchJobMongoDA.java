package org.slc.sli.ingestion.model.da;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Iterator;
import java.util.List;

import org.slc.sli.ingestion.model.Error;
import org.slc.sli.ingestion.model.NewBatchJob;
import org.slc.sli.ingestion.model.Stage;
import org.springframework.data.mongodb.core.CursorPreparer;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.mongodb.DBCursor;

/**
 * 
 * @author ldalgado
 * 
 */
@Component
public class BatchJobMongoDA implements BatchJobDAO {
    private static final String BATCHJOB_ERROR_COLLECTION = "error";
    private static final String BATCHJOBID_FIELDNAME = "batchJobId";
    
    private MongoTemplate batchJobMongoTemplate;
    
    @Override
    public void saveBatchJob(NewBatchJob job) {
        if (job != null) {
            batchJobMongoTemplate.save(job);
        }
    }
    
    @Override
    public void updateBatchJob(String batchJobId, Stage stage) {
          Query query = new Query(Criteria.where("_id").is(batchJobId).and("stageName").is(stage.getStageName()));
          Update update = Update.update("stages.$.chunks", stage);
          this.batchJobMongoTemplate.updateFirst(query, update, "newBatchJob");
         
          // findAndModify would be better, but would need to move to GA, or manually map stage object :(
        
/*        DBCollection col = this.batchJobMongoTemplate.getCollection("newBatchJob");
        
        BasicDBObject query = new BasicDBObject("$eq", new BasicDBObject("_id", batchJobId));
        query.append("$eq", new BasicDBObject("stageName", stage.getStageName()));
        DBObject update = new BasicDBObject("$push", new BasicDBObject("stages.$.chunks", stage));
        col.findAndModify(query, update);*/
    }
    
    @Override
    public NewBatchJob findBatchJobById(String batchJobId) {
        return batchJobMongoTemplate.findOne(query(where("_id").is(batchJobId)), NewBatchJob.class);
    }
    
    @Override
    public List<Error> findBatchJobErrors(String jobId) {
        List<Error> errors = batchJobMongoTemplate.find(query(where(BATCHJOBID_FIELDNAME).is(jobId)), Error.class, BATCHJOB_ERROR_COLLECTION);
        return errors;
    }
    
    public List<Error> findBatchJobErrors(String jobId, CursorPreparer cursorPreparer) {
        List<Error> errors = batchJobMongoTemplate.find(query(where(BATCHJOBID_FIELDNAME).is(jobId)), Error.class, cursorPreparer, BATCHJOB_ERROR_COLLECTION);
        return errors;
    }
    
    @Override
    public void saveError(Error error) {
        if (error != null) {
            batchJobMongoTemplate.save(error);
        }
    }
    
    @Override
    public Iterable<Error> getBatchJobErrors(String jobId, int limit) {
        return new ErrorIterable(jobId, limit);
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
                this.remainingResults = batchJobMongoTemplate.getCollection(BATCHJOB_ERROR_COLLECTION).count(query(where(BATCHJOBID_FIELDNAME).is(jobId)).getQueryObject());
                // TODO use the following rather than the previous line when we upgrade to mongotemplate 1.0.0.M5 or above
                // this.remainingResults = batchJobMongoTemplate.count(query(where(BATCHJOBID_FIELDNAME).is(jobId)), BATCHJOB_ERROR_COLLECTION);
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
                List<Error> errors = batchJobMongoTemplate.find(query(where(BATCHJOBID_FIELDNAME).is(jobId)), Error.class, cursorPreparer, BATCHJOB_ERROR_COLLECTION);
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
                cursorToUse = cursorToUse.batchSize(limit);
                
                position += limit;
                
                return cursorToUse;
            }
            
        }
        
    }
    
    public void setBatchJobMongoTemplate(MongoTemplate mongoTemplate) {
        this.batchJobMongoTemplate = mongoTemplate;
    }
    
}
