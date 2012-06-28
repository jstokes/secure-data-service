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
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.ingestion.NeutralRecord;

/**
 * Transformer for StudentTranscriptAssociation Entities
 *
 * @author jcole
 * @author shalka
 */
@Scope("prototype")
@Component("studentTranscriptAssociationTransformationStrategy")
public class StudentTranscriptAssociationCombiner extends AbstractTransformationStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(StudentTranscriptAssociationCombiner.class);

    private static final String STUDENT_TRANSCRIPT_ASSOCIATION = "studentTranscriptAssociation";

    private Map<Object, NeutralRecord> studentTranscripts;

    /**
     * Default constructor.
     */
    public StudentTranscriptAssociationCombiner() {
        this.studentTranscripts = new HashMap<Object, NeutralRecord>();
    }

    /**
     * The chaining of transformation steps. This implementation assumes that all data will be
     * processed in "one-go"
     */
    @Override
    public void performTransformation() {
        loadData();
        transform();
    }

    /**
     * Pre-requisite interchanges for student transcript data to be successfully transformed:
     * student
     */
    public void loadData() {
        LOG.info("Loading data for studentTranscriptAssociation transformation.");
        this.studentTranscripts = getCollectionFromDb(STUDENT_TRANSCRIPT_ASSOCIATION);
        LOG.info("{} is loaded into local storage.  Total Count = {}", STUDENT_TRANSCRIPT_ASSOCIATION,
                studentTranscripts.size());
    }

    /**
     * Transforms student transcript association data to pass SLI data validation and writes into
     * staging mongo db.
     */
    public void transform() {
        LOG.info("Transforming and persisting student transcript association data");
        for (Map.Entry<Object, NeutralRecord> neutralRecordEntry : studentTranscripts.entrySet()) {
            NeutralRecord neutralRecord = neutralRecordEntry.getValue();
            Map<String, Object> attributes = neutralRecord.getAttributes();
            if (attributes.get("creditsAttempted") == null) {
                attributes.remove("creditsAttempted");
            }

            if (attributes.get("gradeType") == null) {
                attributes.put("gradeType", "Final");
            }
            neutralRecord.setRecordType(neutralRecord.getRecordType() + "_transformed");
            neutralRecord.setCreationTime(getWorkNote().getRangeMinimum());
            insertRecord(neutralRecord);
        }
        LOG.info("Finished transforming and persisting student transcript association data");
    }
}
