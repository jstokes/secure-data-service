package org.slc.sli.ingestion.routes.orchestra;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slc.sli.ingestion.IngestionStagedEntity;
import org.slc.sli.ingestion.WorkNote;
import org.slc.sli.ingestion.WorkNoteImpl;

/**
 *
 * @author dduran
 *
 */
@Component
public class AggregationPostProcessor implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(AggregationPostProcessor.class);

    @Autowired
    private StagedEntityTypeDAO stagedEntityTypeDAO;

    @Override
    public void process(Exchange exchange) throws Exception {

        LOG.info("Aggregation result {} ", exchange);

        @SuppressWarnings("unchecked")
        List<WorkNote> workNoteList = exchange.getIn().getBody(List.class);

        String jobId = null;

        for (WorkNote workNote : workNoteList) {

            // all of these WorkNotes should have the same batchjobid, since this is the aggregation
            // criteria. grabbing it out of the first one will suffice.
            if (jobId == null) {
                jobId = workNote.getBatchJobId();
            }

            IngestionStagedEntity stagedEntityToRemove = workNote.getIngestionStagedEntity();

            if (stagedEntityTypeDAO.getStagedEntitiesForJob(jobId).remove(stagedEntityToRemove)) {

                LOG.info("removed EdfiEntity: {}", stagedEntityToRemove);
            }
        }
        exchange.getIn().setHeader("jobId", jobId);

        if (stagedEntityTypeDAO.getStagedEntitiesForJob(jobId).size() == 0) {
            exchange.getIn().setHeader("processedAllStagedEntities", true);
            WorkNote workNote = new WorkNoteImpl(jobId, null, 0, 0);
            exchange.getIn().setBody(workNote);
        }
    }

}
