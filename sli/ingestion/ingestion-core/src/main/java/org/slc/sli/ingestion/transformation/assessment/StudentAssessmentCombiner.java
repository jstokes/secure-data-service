package org.slc.sli.ingestion.transformation.assessment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.transformation.AbstractTransformationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Transformer for StudentAssessmentAssociation entities.
 * 
 * @author nbrown
 * @author shalka
 */
@Scope("prototype")
@Component("studentAssessmentAssociationTransformationStrategy")
public class StudentAssessmentCombiner extends AbstractTransformationStrategy {
    
    private static final Logger LOG = LoggerFactory.getLogger(StudentAssessmentCombiner.class);
    
    private static final String STUDENT_ASSESSMENT_ASSOCIATION = "studentAssessmentAssociation";
    private static final String STUDENT_OBJECTIVE_ASSESSMENT = "studentObjectiveAssessment";
    private static final String STUDENT_ASSESSMENT_REFERENCE = "studentTestAssessmentRef";
    private static final String OBJECTIVE_ASSESSMENT_REFERENCE = "objectiveAssessmentRef";
    
    private Map<Object, NeutralRecord> studentAssessments;
    
    /**
     * Default constructor.
     */
    public StudentAssessmentCombiner() {
        studentAssessments = new HashMap<Object, NeutralRecord>();
    }
    
    /**
     * The chaining of transformation steps. This implementation assumes that all data will be
     * processed in "one-go."
     */
    @Override
    public void performTransformation() {
        loadData();
        transform();
    }
    
    /**
     * Pre-requisite interchanges for student assessment data to be successfully transformed:
     * student, assessment metadata
     */
    public void loadData() {
        LOG.info("Loading data for studentAssessmentAssociation transformation.");
        studentAssessments = getCollectionFromDb(STUDENT_ASSESSMENT_ASSOCIATION);
        LOG.info("{} is loaded into local storage.  Total Count = {}", STUDENT_ASSESSMENT_ASSOCIATION,
                studentAssessments.size());
    }
    
    /**
     * Transforms student assessments from Ed-Fi model into SLI model.
     */
    public void transform() {
        LOG.info("Transforming student assessment data");
        
        for (Map.Entry<Object, NeutralRecord> neutralRecordEntry : studentAssessments.entrySet()) {
            NeutralRecord neutralRecord = neutralRecordEntry.getValue();
            Map<String, Object> attributes = neutralRecord.getAttributes();
            String studentAssessmentAssociationId = (String) attributes.remove("xmlId");
            
            if (studentAssessmentAssociationId != null) {
                List<Map<String, Object>> studentObjectiveAssessments = getStudentObjectiveAssessments(studentAssessmentAssociationId);
                LOG.info("found {} student objective assessments for student assessment id: {}.",
                        studentObjectiveAssessments.size(), studentAssessmentAssociationId);
                attributes.put("studentObjectiveAssessments", studentObjectiveAssessments);
            } else {
                LOG.warn(
                        "no local id for student assessment association: {}. cannot embed student objective assessment objects.",
                        studentAssessmentAssociationId);
            }
            // newCollection.put(neutralRecord.getRecordId(), neutralRecord);
            neutralRecord.setRecordType(neutralRecord.getRecordType() + "_transformed");
            getNeutralRecordMongoAccess().getRecordRepository().createForJob(neutralRecord, getJob().getId());
        }
        LOG.info("Finished transforming student assessment data for {} student assessment associations.",
                studentAssessments.size());
    }
    
    /**
     * Gets all student objective assessments that reference the student assessment's local (xml)
     * id.
     * 
     * @param studentAssessmentAssociationId
     *            volatile identifier.
     * @return list of student objective assessments (represented by neutral records).
     */
    private List<Map<String, Object>> getStudentObjectiveAssessments(String studentAssessmentAssociationId) {
        List<Map<String, Object>> assessments = new ArrayList<Map<String, Object>>();
        NeutralQuery query = new NeutralQuery(0);
        query.addCriteria(new NeutralCriteria(STUDENT_ASSESSMENT_REFERENCE, "=", studentAssessmentAssociationId));
        
        Iterable<NeutralRecord> studentObjectiveAssessments = getNeutralRecordMongoAccess().getRecordRepository()
                .findAllForJob(STUDENT_OBJECTIVE_ASSESSMENT, getJob().getId(), query);
        
        if (studentObjectiveAssessments != null) {
            Iterator<NeutralRecord> itr = studentObjectiveAssessments.iterator();
            NeutralRecord studentObjectiveAssessment = null;
            while (itr.hasNext()) {
                studentObjectiveAssessment = itr.next();
                Map<String, Object> assessmentAttributes = studentObjectiveAssessment.getAttributes();
                String objectiveAssessmentRef = (String) assessmentAttributes.get(OBJECTIVE_ASSESSMENT_REFERENCE);
                
                Map<String, Object> objectiveAssessment = new ObjectiveAssessmentBuilder(getNeutralRecordMongoAccess(),
                        getJob().getId()).getObjectiveAssessment(objectiveAssessmentRef);
                
                if (objectiveAssessment != null) {
                    LOG.info("Found objective assessment: {}", objectiveAssessmentRef);
                    assessmentAttributes.put("objectiveAssessment", objectiveAssessment);
                } else {
                    LOG.warn("Failed to find objective assessment: {} for student assessment: {}",
                            objectiveAssessmentRef, studentAssessmentAssociationId);
                }
                
                Map<String, Object> attributes = new HashMap<String, Object>();
                for (Map.Entry<String, Object> entry : assessmentAttributes.entrySet()) {
                    if (!entry.getKey().equals(OBJECTIVE_ASSESSMENT_REFERENCE)
                            && !entry.getKey().equals(STUDENT_ASSESSMENT_REFERENCE)) {
                        attributes.put(entry.getKey(), entry.getValue());
                    }
                }
                LOG.info("added student objective assessment: {}", attributes);
                assessments.add(attributes);
            }
        } else {
            LOG.warn("Couldn't find any student objective assessments for student assessment: {}",
                    studentAssessmentAssociationId);
        }
        return assessments;
    }
}
