/*
 * Copyright 2012-2013 inBloom, Inc. and its affiliates.
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

package org.slc.sli.ingestion.transformation.assessment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import org.slc.sli.dal.repository.MongoEntityRepository;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.dal.NeutralRecordRepository;
import org.slc.sli.ingestion.reporting.impl.CoreMessageCode;
import org.slc.sli.ingestion.transformation.AbstractTransformationStrategy;

/**
 * Transformer for Assessment Entities
 *
 * @author ifaybyshev
 * @author shalka
 */
@Scope("prototype")
@Component("assessmentTransformationStrategy")
public class AssessmentCombiner extends AbstractTransformationStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(AssessmentCombiner.class);

    private Map<Object, NeutralRecord> assessments;
    private List<NeutralRecord> transformedAssessments;

    private static final String ASSESSMENT = "assessment";
    private static final String ASSESSMENT_FAMILY = "assessmentFamily";
    private static final String ASSESSMENT_PERIOD_DESCRIPTOR = "assessmentPeriodDescriptor";
    private static final String ASSESSMENT_TRANSFORMED = "assessment_transformed";
    private static final String ASSESSMENT_ITEM = "assessmentItem";
    private static final String PARENT_ASSESSMENT_FAMILY_TITLE = "parentAssessmentFamilyTitle";

    @Autowired
    private ObjectiveAssessmentBuilder builder;

    /**
     * Default constructor.
     */
    public AssessmentCombiner() {
        assessments = new HashMap<Object, NeutralRecord>();
        transformedAssessments = new ArrayList<NeutralRecord>();
    }

    /**
     * The chaining of transformation steps. This implementation assumes that all data will be
     * processed in "one-go"
     */
    @Override
    public void performTransformation() {
        loadData();
        transform();
        insertRecords(transformedAssessments, ASSESSMENT_TRANSFORMED);
    }

    /**
     * Pre-requisite interchanges for assessment data to be successfully transformed:
     * none (as of 5/8/2012)
     */
    public void loadData() {
        LOG.info("Loading data for assessment transformation.");
        assessments = getCollectionFromDb(ASSESSMENT);
        LOG.info("{} is loaded into local storage.  Total Count = {}", ASSESSMENT, assessments.size());
    }

    /**
     * Transforms assessments from Ed-Fi model into SLI model.
     */
    public void transform() {
        LOG.info("Transforming assessment data");
        builder.setAbstractTransformationStrategy(this);
        for (Map.Entry<Object, NeutralRecord> neutralRecordEntry : assessments.entrySet()) {
            NeutralRecord neutralRecord = neutralRecordEntry.getValue();

            // get the key of parent
            Map<String, Object> attrs = neutralRecord.getAttributes();
            String parentFamilyTitle = (String) attrs.remove(PARENT_ASSESSMENT_FAMILY_TITLE);
            String familyHierarchyName = getAssocationFamilyMap(parentFamilyTitle, new HashMap<String, Map<String, Object>>(), "");
            attrs.put("assessmentFamilyHierarchyName", familyHierarchyName);

            @SuppressWarnings("unchecked")
            List<String> objectiveAssessmentRefs = (List<String>) attrs.get("objectiveAssessmentRefs");
            attrs.remove("objectiveAssessmentRefs");
            List<Map<String, Object>> familyObjectiveAssessments = new ArrayList<Map<String, Object>>();
            if (objectiveAssessmentRefs != null && !(objectiveAssessmentRefs.isEmpty())) {
                for (String objectiveAssessmentRef : objectiveAssessmentRefs) {
                    Map<String, Object> objectiveAssessment = builder.getObjectiveAssessment(
                            getNeutralRecordMongoAccess(), getJob(), objectiveAssessmentRef);

                    if (objectiveAssessment != null && !objectiveAssessment.isEmpty()) {
                        LOG.info("Found objective assessment: {} for family: {}", objectiveAssessmentRef,
                                familyHierarchyName);
                        familyObjectiveAssessments.add(objectiveAssessment);
                    } else {
                        LOG.warn("Failed to match objective assessment: {} for family: {}", objectiveAssessmentRef,
                                familyHierarchyName);
                        builder.reportAggregatedWarnings(CoreMessageCode.CORE_0045, getNeutralRecordMongoAccess(), getJob(), objectiveAssessmentRef, familyHierarchyName);
                    }
                }
                attrs.put("objectiveAssessment", familyObjectiveAssessments);
            }

            if (attrs.containsKey(ASSESSMENT_ITEM)) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> items = (List<Map<String, Object>>) attrs.get(ASSESSMENT_ITEM);
                if (items == null || items.size() == 0) {
                    attrs.remove(ASSESSMENT_ITEM);
                } else {
                    List<Map<String, Object>> assessmentItems = getAssessmentItems(items);
                    if (assessmentItems != null) {
                        attrs.put(ASSESSMENT_ITEM, assessmentItems);
                    }
                }
            }

            String assessmentPeriodDescriptorRef = (String) attrs.remove("periodDescriptorRef");
            if (assessmentPeriodDescriptorRef != null) {
                attrs.put(ASSESSMENT_PERIOD_DESCRIPTOR, getAssessmentPeriodDescriptor(assessmentPeriodDescriptorRef));
            }

            neutralRecord.setRecordType(neutralRecord.getRecordType() + "_transformed");
            neutralRecord.setCreationTime(getWorkNote().getRangeMinimum());
            transformedAssessments.add(neutralRecord);
        }
    }

    private List<Map<String, Object>> getAssessmentItems(List<Map<String, Object>> itemReferences) {
        List<String> identificationCodes = new ArrayList<String>();
        // build in clause
        for (Map<String, Object> item : itemReferences) {
            if (item.containsKey("identificationCode")) {
                identificationCodes.add((String) item.get("identificationCode"));
            }
        }

        if (identificationCodes.size() > 0) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("query for assessmentItems: {}", identificationCodes);
            }
            NeutralQuery constraint = new NeutralQuery();
            constraint.addCriteria(new NeutralCriteria("identificationCode", NeutralCriteria.CRITERIA_IN,
                    identificationCodes));
            NeutralRecordRepository repo = getNeutralRecordMongoAccess().getRecordRepository();
            Iterable<NeutralRecord> records = repo.findAllForJob(ASSESSMENT_ITEM, constraint);
            List<Map<String, Object>> assessmentItems = new ArrayList<Map<String, Object>>();
            if (records != null) {
                for (NeutralRecord record : records) {
                    assessmentItems.add(record.getAttributes());
                }

                return assessmentItems;
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getAssessmentPeriodDescriptor(String assessmentPeriodDescriptorRef) {
        Query query = new Query().limit(0);
        query.addCriteria(Criteria.where(BATCH_JOB_ID_KEY).is(getBatchJobId()));
        query.addCriteria(Criteria.where("body.codeValue").is(assessmentPeriodDescriptorRef));

        Iterable<NeutralRecord> data = getNeutralRecordMongoAccess().getRecordRepository().findAllByQuery(
                ASSESSMENT_PERIOD_DESCRIPTOR, query);

        if (data.iterator().hasNext()) {
            return data.iterator().next().getAttributes();
        } else {
            Query choice = new Query().limit(0);
            choice.addCriteria(Criteria.where("body.assessmentPeriodDescriptor.codeValue").is(
                    assessmentPeriodDescriptorRef));
            MongoEntityRepository mongoEntityRepository = getMongoEntityRepository();
            Entity assessmentEntity = mongoEntityRepository.findOne(ASSESSMENT, choice);
            if (assessmentEntity != null) {
                Map<String, Object> assessmentPeriodDescriptor = (Map<String, Object>) assessmentEntity.getBody().get(
                        ASSESSMENT_PERIOD_DESCRIPTOR);
                return assessmentPeriodDescriptor;
            }
        }
        return null;
    }

    private String getAssocationFamilyMap(String assessmentFamilyTitle, Map<String, Map<String, Object>> deepFamilyMap,
            String familyHierarchyName) {
        String theFamilyHierarchyName = familyHierarchyName;
        Query query = new Query().limit(0);
        query.addCriteria(Criteria.where(BATCH_JOB_ID_KEY).is(getBatchJobId()));
        query.addCriteria(Criteria.where("body.AssessmentFamilyTitle").is(assessmentFamilyTitle));
        Iterable<NeutralRecord> neutralRecords = getNeutralRecordMongoAccess().getRecordRepository().findAllByQuery(ASSESSMENT_FAMILY, query);

        // Should only iterate exactly once because AssessmentFamilyTitle should be unique for each AssessmentFamily.
        for (NeutralRecord neutralRecord : neutralRecords) {
            Map<String, Object> associationAttrs = neutralRecord.getAttributes();

            if ("".equals(theFamilyHierarchyName)) {
                theFamilyHierarchyName = (String) associationAttrs.get("AssessmentFamilyTitle");
            } else {
                theFamilyHierarchyName = associationAttrs.get("AssessmentFamilyTitle") + "." + theFamilyHierarchyName;
            }
            deepFamilyMap.put((String) associationAttrs.get("AssessmentFamilyTitle"), associationAttrs);

            // check if there are parent nodes
            if (associationAttrs.containsKey("parentAssessmentFamilyTitle")
                    && !deepFamilyMap.containsKey(associationAttrs.get("parentAssessmentFamilyTitle"))) {
                theFamilyHierarchyName = getAssocationFamilyMap((String) associationAttrs.get("parentAssessmentFamilyTitle"),
                        deepFamilyMap, theFamilyHierarchyName);
            }
        }

        return theFamilyHierarchyName;
    }
}
