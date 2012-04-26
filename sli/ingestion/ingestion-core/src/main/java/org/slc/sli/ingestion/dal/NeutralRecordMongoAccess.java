package org.slc.sli.ingestion.dal;

import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.ResourceWriter;

/**
 * write NeutralRecord objects to mongo
 *
 * @author dduran
 *
 */
public class NeutralRecordMongoAccess implements ResourceWriter<NeutralRecord> {

    private NeutralRecordRepository neutralRecordRepository;

    private MongoIndexManager mongoIndexManager;

    public MongoIndexManager getMongoIndexManager() {
        return mongoIndexManager;
    }

    public void setMongoIndexManager(MongoIndexManager mongoIndexManager) {
        this.mongoIndexManager = mongoIndexManager;
    }

    @Override
    public void writeResource(NeutralRecord neutralRecord) {
        neutralRecordRepository.create(neutralRecord);
    }

    public NeutralRecordRepository getRecordRepository() {
        return this.neutralRecordRepository;
    }

    public void setNeutralRecordRepository(NeutralRecordRepository neutralRecordRepository) {
        this.neutralRecordRepository = neutralRecordRepository;
    }

    public void registerBatchId(String batchJobId) {
        neutralRecordRepository.registerBatchId(removeUnsupportedChars(batchJobId));
    }

    public static String removeUnsupportedChars(String data) {
        return data.substring(data.length() - 51, data.length()).replace("-", "");
    }

    public void cleanupGroupedCollections() {
        neutralRecordRepository.deleteGroupedCollections();
    }

    /** ensureIndexes for all the collections configured
     *
     * @author tke
     */
    public void ensureIndex() {
        mongoIndexManager.ensureIndex(neutralRecordRepository);
    }

}
