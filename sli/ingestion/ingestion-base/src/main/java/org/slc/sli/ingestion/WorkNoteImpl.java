package org.slc.sli.ingestion;

/**
 * Unit of work, chunked by Maestro, to be executed by a member of the orchestra (pit).
 *
 * @author shalka
 */
public class WorkNoteImpl implements WorkNote {
    private String batchJobId;
    private String collection;
    private int rangeMinimum;
    private int rangeMaximum;

    /**
     * Default constructor for the WorkOrder class.
     *
     * @param batchJobId
     * @param collection
     * @param minimum
     * @param maximum
     */
    public WorkNoteImpl(String batchJobId, String collection, int minimum, int maximum) {
        this.setBatchJobId(batchJobId);
        this.setCollection(collection);
        this.setRangeMinimum(minimum);
        this.setRangeMaximum(maximum);
    }

    /**
     * Gets the batch job id.
     *
     * @return String representing batch job id.
     */
    @Override
    public String getBatchJobId() {
        return batchJobId;
    }

    /**
     * Sets the batch job id.
     *
     * @param batchJobId
     *            String representing the batch job id.
     */
    @Override
    public void setBatchJobId(String batchJobId) {
        this.batchJobId = batchJobId;
    }

    /**
     * Gets the collection.
     *
     * @return collection to perform work on.
     */
    @Override
    public String getCollection() {
        return collection;
    }

    /**
     * Sets the collection to perform work on.
     *
     * @param collection
     *            collection to perform work on.
     */
    @Override
    public void setCollection(String collection) {
        this.collection = collection;
    }

    /**
     * Gets the minimum value of the index [inclusive] to perform work on.
     *
     * @return minimum index value.
     */
    @Override
    public int getRangeMinimum() {
        return rangeMinimum;
    }

    /**
     * Sets the minimum value of the index [inclusive] to perform work on.
     *
     * @param rangeMinimum
     *            minimum index value.
     */
    @Override
    public void setRangeMinimum(int rangeMinimum) {
        this.rangeMinimum = rangeMinimum;
    }

    /**
     * Gets the maximum value of the index [inclusive] to perform work on.
     *
     * @return maximum index value.
     */
    @Override
    public int getRangeMaximum() {
        return rangeMaximum;
    }

    /**
     * Sets the maximum value of the index [inclusive] to perform work on.
     *
     * @param rangeMaximum
     *            maximum index value.
     */
    @Override
    public void setRangeMaximum(int rangeMaximum) {
        this.rangeMaximum = rangeMaximum;
    }
}
