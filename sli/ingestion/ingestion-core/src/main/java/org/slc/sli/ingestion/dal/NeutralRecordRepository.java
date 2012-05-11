package org.slc.sli.ingestion.dal;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.mongodb.DBCollection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.index.IndexDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;

import org.slc.sli.dal.repository.MongoRepository;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.ingestion.NeutralRecord;

/**
 * Specialized class providing basic CRUD and field query methods for neutral records
 * using a Mongo Staging DB, for use for staging data for intermediate operations.
 *
 * @author Thomas Shewchuk tshewchuk@wgen.net 2/23/2012 (PI3 US1226)
 *
 */
public class NeutralRecordRepository extends MongoRepository<NeutralRecord> {
    // public class NeutralRecordRepository {

    protected static final Logger LOG = LoggerFactory.getLogger(NeutralRecordRepository.class);

    private MongoIndexManager mongoIndexManager;

    public MongoIndexManager getMongoIndexManager() {
        return mongoIndexManager;
    }

    public void setMongoIndexManager(MongoIndexManager mongoIndexManager) {
        this.mongoIndexManager = mongoIndexManager;
    }

    @Override
    public boolean update(String collection, NeutralRecord neutralRecord) {
        return update(neutralRecord.getRecordType(), neutralRecord, null);
    }

    @Override
    protected Query getUpdateQuery(NeutralRecord entity) {
        throw new UnsupportedOperationException("NeutralReordRepository.getUpdateQuery not implemented");
    }

    @Override
    protected NeutralRecord getEncryptedRecord(NeutralRecord entity) {
        throw new UnsupportedOperationException("NeutralReordRepository.getEncryptedRecord not implemented");
    }

    @Override
    protected Update getUpdateCommand(NeutralRecord entity) {
        throw new UnsupportedOperationException("NeutralReordRepository.getUpdateCommand not implemented");
    }

    @Override
    public NeutralRecord create(String type, Map<String, Object> body, Map<String, Object> metaData,
            String collectionName) {
        Assert.notNull(body, "The given entity must not be null!");
        NeutralRecord neutralRecord = new NeutralRecord();
        neutralRecord.setLocalId(metaData.get("externalId"));
        neutralRecord.setAttributes(body);
        return create(neutralRecord, collectionName);
    }

    public NeutralRecord createForJob(NeutralRecord neutralRecord, String jobId) {
        return create(neutralRecord, toStagingCollectionName(neutralRecord.getRecordType(), jobId));
    }

    public Iterable<NeutralRecord> findAllForJob(String collectionName, String jobId, NeutralQuery neutralQuery) {
        return findAll(toStagingCollectionName(collectionName, jobId), neutralQuery);
    }

    public Iterable<NeutralRecord> findByQueryForJob(String collectionName, Query query, String jobId, int skip, int max) {
        return findByQuery(toStagingCollectionName(collectionName, jobId), query, skip, max);
    }

    public Iterable<NeutralRecord> findByPathsForJob(String collectionName, Map<String, String> paths, String jobId) {
        return findByPaths(toStagingCollectionName(collectionName, jobId), paths);
    }

    public NeutralRecord findOneForJob(String collectionName, NeutralQuery neutralQuery, String jobId) {
        return findOne(toStagingCollectionName(collectionName, jobId), neutralQuery);
    }

    public DBCollection getCollectionForJob(String collectionName, String jobId) {
        return getCollection(toStagingCollectionName(collectionName, jobId));
    }

    public boolean collectionExistsForJob(String collectionName, String jobId) {
        return collectionExists(toStagingCollectionName(collectionName, jobId));
    }

    public void createCollectionForJob(String collectionName, String jobId) {
        createCollection(toStagingCollectionName(collectionName, jobId));
    }

    public Set<String> getCollectionNamesForJob(String batchJobId) {
        Set<String> collectionNamesForJob = new HashSet<String>();

        if (batchJobId != null) {
            String jobIdPattern = "_" + toMongoCleanId(batchJobId);

            Set<String> allCollectionNames = getTemplate().getCollectionNames();
            for (String currentCollection : allCollectionNames) {

                int jobPatternIndex = currentCollection.indexOf(jobIdPattern);
                if (jobPatternIndex != -1) {
                    collectionNamesForJob.add(currentCollection.substring(0, jobPatternIndex));
                }
            }
        }
        return collectionNamesForJob;
    }

    public Set<String> getCollectionFullNamesForJob(String batchJobId) {
        Set<String> collectionNamesForJob = new HashSet<String>();

        if (batchJobId != null) {
            String jobIdPattern = "_" + toMongoCleanId(batchJobId);

            Set<String> allCollectionNames = getTemplate().getCollectionNames();
            for (String currentCollection : allCollectionNames) {

                int jobPatternIndex = currentCollection.indexOf(jobIdPattern);
                if (jobPatternIndex != -1) {
                    collectionNamesForJob.add(currentCollection);
                }
            }
        }
        return collectionNamesForJob;
    }

    public void deleteCollectionsForJob(String batchJobId) {
        if (batchJobId != null) {
            String jobIdPattern = "_" + toMongoCleanId(batchJobId);

            Set<String> allCollectionNames = getTemplate().getCollectionNames();
            for (String currentCollection : allCollectionNames) {

                int jobPatternIndex = currentCollection.indexOf(jobIdPattern);
                if (jobPatternIndex != -1) {
                    getTemplate().dropCollection(currentCollection);
                }
            }
        }
    }

    public void ensureIndexesForJob(String batchJobId) {
        LOG.info("ENSURING ALL INDEXES FOR A DB");

        Set<String> collectionNames = mongoIndexManager.getCollectionIndexes().keySet();
        Iterator<String> it = collectionNames.iterator();
        String collectionName;

        while (it.hasNext()) {
            collectionName = it.next();
            LOG.info("INDEXING COLLECTION " + collectionName + " = "
                    + toStagingCollectionName(collectionName, batchJobId));

            if (!collectionExistsForJob(collectionName, batchJobId)) {
                createCollectionForJob(collectionName, batchJobId);
            }

            try {
                for (IndexDefinition definition : mongoIndexManager.getCollectionIndexes().get(collectionName)) {
                    LOG.info("Adding Index on " + collectionName);
                    ensureIndex(definition, toStagingCollectionName(collectionName, batchJobId));
                }
            } catch (Exception e) {
                LOG.error("Failed to create mongo indexes, reason: {}", e.getMessage());
            }
        }
    }

    @Override
    protected String getRecordId(NeutralRecord neutralRecord) {
        return neutralRecord.getRecordId();
    }

    @Override
    protected Class<NeutralRecord> getRecordClass() {
        return NeutralRecord.class;
    }

    private static String toStagingCollectionName(String collectionName, String jobId) {
        return collectionName + "_" + toMongoCleanId(jobId);
    }

    private static String toMongoCleanId(String id) {
        return id.substring(id.length() - 37, id.length()).replace("-", "");
    }

}
