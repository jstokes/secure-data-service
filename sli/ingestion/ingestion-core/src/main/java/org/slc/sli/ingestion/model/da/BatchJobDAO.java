package org.slc.sli.ingestion.model.da;

import org.slc.sli.ingestion.model.NewBatchJob;

/**
 *
 * @author dduran
 *
 */
public interface BatchJobDAO {

    BatchJobStatus saveBatchJob(NewBatchJob newBatchJob);

    NewBatchJob findBatchJobById(String batchJobId);

}
