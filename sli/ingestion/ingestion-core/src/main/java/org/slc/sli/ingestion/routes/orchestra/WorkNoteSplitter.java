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

package org.slc.sli.ingestion.routes.orchestra;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slc.sli.ingestion.IngestionStagedEntity;
import org.slc.sli.ingestion.WorkNote;
import org.slc.sli.ingestion.model.da.BatchJobDAO;
import org.slc.sli.ingestion.queues.MessageType;

/**
 * WorkNote splitter to be used from camel
 *
 * @author dduran
 *
 */
@Component
public class WorkNoteSplitter {
    private static final Logger LOG = LoggerFactory.getLogger(WorkNoteSplitter.class);

    @Autowired
    private SplitStrategy balancedTimestampSplitStrategy;

    @Autowired
    private BatchJobDAO batchJobDAO;

    /**
     * Splits the work that can be processed in parallel next round into individual WorkNotes.
     *
     * @param exchange
     * @return list of WorkNotes that camel will iterate over, issuing each as a new message
     * @throws IllegalStateException
     */
    public List<WorkNote> splitTransformationWorkNotes(Exchange exchange) {

        String jobId = exchange.getIn().getHeader("jobId").toString();
        LOG.info("orchestrating splitting for job: {}", jobId);

        Set<IngestionStagedEntity> stagedEntities = batchJobDAO.getStagedEntitiesForJob(jobId);

        if (stagedEntities.size() == 0) {
            throw new IllegalStateException(
                    "stagedEntities is empty at splitting stage. should have been redirected prior to this point.");
        }

        Set<IngestionStagedEntity> nextTierEntities = IngestionStagedEntity.cleanse(stagedEntities);

        List<WorkNote> workNoteList = createWorkNotes(nextTierEntities, jobId);

        return workNoteList;
    }

    private List<WorkNote> createWorkNotes(Set<IngestionStagedEntity> stagedEntities, String jobId) {
        LOG.info("creating WorkNotes for processable entities: {}", stagedEntities);

        List<WorkNote> workNoteList = new ArrayList<WorkNote>();
        for (IngestionStagedEntity stagedEntity : stagedEntities) {

            List<WorkNote> workNotesForEntity = balancedTimestampSplitStrategy.splitForEntity(stagedEntity, jobId);

            batchJobDAO.createWorkNoteCountdownLatch(MessageType.DATA_TRANSFORMATION.name(), jobId,
                    stagedEntity.getCollectionNameAsStaged(), workNotesForEntity.size());

            batchJobDAO.createWorkNoteCountdownLatch(MessageType.PERSIST_REQUEST.name(), jobId, stagedEntity.getCollectionNameAsStaged(), 1);
            workNoteList.addAll(workNotesForEntity);
        }

        LOG.info("{} total WorkNotes created and ready for splitting for current tier.", workNoteList.size());
        return workNoteList;
    }

    public List<WorkNote> splitPersistanceWorkNotes(Exchange exchange) {
        WorkNote workNote = exchange.getIn().getBody(WorkNote.class);
        IngestionStagedEntity stagedEntity = workNote.getIngestionStagedEntity();
        List<WorkNote> workNoteList = new ArrayList<WorkNote>();

        String jobId = exchange.getIn().getHeader("jobId").toString();

        LOG.debug("Splitting out (pass-through) list of WorkNotes: {}", workNoteList);
        List<WorkNote> workNotesForEntity = balancedTimestampSplitStrategy.splitForEntity(stagedEntity, jobId);
        batchJobDAO.setWorkNoteLatchCount(MessageType.PERSIST_REQUEST.name(), jobId, stagedEntity.getCollectionNameAsStaged(), workNotesForEntity.size());

        workNoteList.addAll(workNotesForEntity);
        return workNoteList;
    }

}
