package org.slc.sli.ingestion.transformation.assessment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slc.sli.common.constants.EntityNames;
import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.transformation.AbstractTransformationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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

    /**
     * Default constructor.
     */
    public AssessmentCombiner() {
        assessments = new HashMap<Object, NeutralRecord>();
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
     * Pre-requisite interchanges for assessment data to be successfully transformed:
     * none (as of 5/8/2012)
     */
    public void loadData() {
        LOG.info("Loading data for assessment transformation.");
        assessments = getCollectionFromDb(EntityNames.ASSESSMENT);
        LOG.info("{} is loaded into local storage.  Total Count = {}", EntityNames.ASSESSMENT, assessments.size());
    }
    
    /**
     * Transforms assessments from Ed-Fi model into SLI model.
     */
    public void transform() {
        LOG.info("Transforming assessment data");
        for (Map.Entry<Object, NeutralRecord> neutralRecordEntry : assessments.entrySet()) {
            NeutralRecord neutralRecord = neutralRecordEntry.getValue();
            
            // get the key of parent
            Map<String, Object> attrs = neutralRecord.getAttributes();
            String parentFamilyId = (String) attrs.remove("parentAssessmentFamilyId");
            String familyHierarchyName = "";
            familyHierarchyName = getAssocationFamilyMap(parentFamilyId, new HashMap<String, Map<String, Object>>(),
                    familyHierarchyName);
            
            attrs.put("assessmentFamilyHierarchyName", familyHierarchyName);
            
            @SuppressWarnings("unchecked")
            List<String> objectiveAssessmentRefs = (List<String>) attrs.get("objectiveAssessmentRefs");
            attrs.remove("objectiveAssessmentRefs");
            List<Map<String, Object>> familyObjectiveAssessments = new ArrayList<Map<String, Object>>();
            if (objectiveAssessmentRefs != null && !(objectiveAssessmentRefs.isEmpty())) {
                for (String objectiveAssessmentRef : objectiveAssessmentRefs) {
                    Map<String, Object> objectiveAssessment = new ObjectiveAssessmentBuilder(getNeutralRecordMongoAccess(), getJob().getId()).
                            getObjectiveAssessment(objectiveAssessmentRef);
                    
                    if (objectiveAssessment != null && !objectiveAssessment.isEmpty()) {
                        LOG.info("Found objective assessment: {} for family: {}", objectiveAssessmentRef, familyHierarchyName);
                        familyObjectiveAssessments.add(objectiveAssessment);
                    } else {
                        LOG.warn("Failed to match objective assessment: {} for family: {}", objectiveAssessmentRef,
                                familyHierarchyName);
                    }
                }
                attrs.put("objectiveAssessment", familyObjectiveAssessments);
            }
            
            String assessmentPeriodDescriptorRef = (String) attrs.remove("periodDescriptorRef");
            if (assessmentPeriodDescriptorRef != null) {
                attrs.put(EntityNames.ASSESSMENT_PERIOD_DESCRIPTOR, getAssessmentPeriodDescriptor(assessmentPeriodDescriptorRef));
            }
            neutralRecord.setRecordType(neutralRecord.getRecordType() + "_transformed");            
            getNeutralRecordMongoAccess().getRecordRepository().createForJob(neutralRecord, getJob().getId());
        }
    }
    
    private Map<String, Object> getAssessmentPeriodDescriptor(String assessmentPeriodDescriptorRef) {
        Map<String, String> paths = new HashMap<String, String>();
        paths.put("body.codeValue", assessmentPeriodDescriptorRef);
        
        Iterable<NeutralRecord> data = getNeutralRecordMongoAccess().getRecordRepository().findByPathsForJob(
                EntityNames.ASSESSMENT_PERIOD_DESCRIPTOR, paths, getJob().getId());
        
        if (data.iterator().hasNext()) {
            return data.iterator().next().getAttributes();
        }
        
        return null;
        
    }
    
    @SuppressWarnings("unchecked")
    private String getAssocationFamilyMap(String key, HashMap<String, Map<String, Object>> deepFamilyMap,
            String familyHierarchyName) {
        
        Map<String, String> paths = new HashMap<String, String>();
        paths.put("body.AssessmentFamilyIdentificationCode.ID", key);
        
        Iterable<NeutralRecord> data = getNeutralRecordMongoAccess().getRecordRepository().findByPathsForJob(
                "assessmentFamily", paths, getJob().getId());
        
        Map<String, Object> associationAttrs;
        
        ArrayList<Map<String, Object>> tempIdentificationCodes;
        Map<String, Object> tempMap;
        
        for (NeutralRecord tempNr : data) {
            associationAttrs = tempNr.getAttributes();
            
            if (associationAttrs.get("AssessmentFamilyIdentificationCode") instanceof ArrayList<?>) {
                tempIdentificationCodes = (ArrayList<Map<String, Object>>) associationAttrs
                        .get("AssessmentFamilyIdentificationCode");
                
                tempMap = tempIdentificationCodes.get(0);
                if (familyHierarchyName.equals("")) {
                    
                    familyHierarchyName = (String) associationAttrs.get("AssessmentFamilyTitle");
                    
                } else {
                    
                    familyHierarchyName = associationAttrs.get("AssessmentFamilyTitle") + "." + familyHierarchyName;
                    
                }
                deepFamilyMap.put((String) tempMap.get("ID"), associationAttrs);
            }
            
            // check if there are parent nodes
            if (associationAttrs.containsKey("parentAssessmentFamilyId")
                    && !deepFamilyMap.containsKey(associationAttrs.get("parentAssessmentFamilyId"))) {
                familyHierarchyName = getAssocationFamilyMap((String) associationAttrs.get("parentAssessmentFamilyId"),
                        deepFamilyMap, familyHierarchyName);
            }
            
        }
        
        return familyHierarchyName;
    }
}
