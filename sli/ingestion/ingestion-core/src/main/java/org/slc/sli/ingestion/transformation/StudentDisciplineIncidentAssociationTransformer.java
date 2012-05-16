package org.slc.sli.ingestion.transformation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slc.sli.common.constants.EntityNames;
import org.slc.sli.ingestion.NeutralRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

/**
 * Transforms discipline incident relates entities to allow resolution of
 * disciplineIncident reference in discipline incidents
 *
 * This strategy will be replaced once automatic idref ref
 * resolution is fully implemented.
 *
 * @author jtully
 */
@Scope("prototype")
@Component("studentDisciplineIncidentAssociationTransformationStrategy")
public class StudentDisciplineIncidentAssociationTransformer extends AbstractTransformationStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(StudentDisciplineIncidentAssociationTransformer.class);

    private Map<Object, NeutralRecord> collection;

    public StudentDisciplineIncidentAssociationTransformer() {
        this.collection = new HashMap<Object, NeutralRecord>();
    }

    /**
     * The chaining of transformation steps. This implementation forces that all data will be
     * processed in "one-go"
     */
    @Override
    public void performTransformation() {
        loadData();
        transform();
        persist();
    }

    private void loadData() {
        LOG.info("Loading data for student discipline incident association transformation.");
        collection = getCollectionFromDb(EntityNames.STUDENT_DISCIPLINE_INCIDENT_ASSOCIATION);
        LOG.info("{} is loaded into local storage.  Total Count = {}", EntityNames.STUDENT_DISCIPLINE_INCIDENT_ASSOCIATION, collection.size());
        LOG.info("Finished loading data for student discipline incident association transformation.");
    }

    private void transform() {
        try {
            LOG.info("Transforming data: resolving disciplineIncident reference");
            for (Map.Entry<Object, NeutralRecord> neutralRecordEntry : collection.entrySet()) {
                NeutralRecord nr = neutralRecordEntry.getValue();
                String idRef = (String) PropertyUtils.getProperty(nr, "attributes.DisciplineIncidentReference.ref");
                NeutralRecord disciplineIncidentRef = findDisciplineIncidentNRFromIdRef(idRef);
                String incidentIdentifier = (String) PropertyUtils.getProperty(disciplineIncidentRef,
                        "attributes.IncidentIdentifier");
                PropertyUtils.setProperty(nr, "attributes.refIncidentIdentifier", incidentIdentifier);
            }
        } catch (Exception e) {
            LOG.error("Exception encountered resolving DisciplineIncidentAssociation reference:", e);
        }
    }

    private void persist() {
        LOG.info("Persisting {} data to storage.");

        for (Map.Entry<Object, NeutralRecord> neutralRecordEntry : collection.entrySet()) {

            NeutralRecord neutralRecord = neutralRecordEntry.getValue();
            neutralRecord.setRecordType(neutralRecord.getRecordType() + "_transformed");

            getNeutralRecordMongoAccess().getRecordRepository().createForJob(neutralRecord, getBatchJobId());
        }
    }

    private NeutralRecord findDisciplineIncidentNRFromIdRef(String idRef) {
        NeutralRecord result = null;
        Criteria jobIdCriteria = Criteria.where(BATCH_JOB_ID_KEY).is(getBatchJobId());
        Criteria idCriteria = Criteria.where("body.id").is(idRef);
        Query query = new Query(jobIdCriteria);
        query.addCriteria(idCriteria);
        List<NeutralRecord> data = (List<NeutralRecord>) getNeutralRecordMongoAccess().getRecordRepository()
                .findByQueryForJob("disciplineIncident", query, getBatchJobId(), 0, 0);
        if (data.size() == 1) {
            result = data.get(0);
        }
        return result;

    }
}
