package org.slc.sli.ingestion.transformation.normalization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import org.slc.sli.domain.Entity;
import org.slc.sli.domain.EntityMetadataKey;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.Repository;
import org.slc.sli.ingestion.validation.ErrorReport;
import org.slc.sli.ingestion.validation.ProxyErrorReport;


/**
 * Internal ID resolver.
 *
 * @author okrook
 *
 */
public class IdNormalizer {
    private static final Logger LOG = LoggerFactory.getLogger(IdNormalizer.class);

    private static final String METADATA_BLOCK = "metaData";

    private Repository<Entity> entityRepository;
    
    protected static Map<String, ComplexIdNormalizer> complexIdNormalizers = new HashMap<String, ComplexIdNormalizer>();
    static {
        complexIdNormalizers.put("studentTranscriptAssociation:body.studentId", 
                new StudentTranscriptAssociationStudentIdComplexIdNormalizer());
    }
    
    
    /**
     * Resolves the specified field's reference and returns the associated ID. Returns an empty
     * list if ID cannot be resolved or if this class is not aware how to resolve that field. This method
     * is for the non-standard resolvers that have custom definitions.
     * 
     * @param entity entity containing field that needs to be resolved
     * @param field which field is currently being resolved
     * @param neutralQuery a query where "tenantId" is already specified
     * @param entityRepository access to execute query
     * @return resolved ID or an empty list
     */
    protected List<String> resolveComplexInternalId(Entity entity, String field, NeutralQuery neutralQuery) throws IdResolutionException {

        if (entity == null) {
            throw new IdResolutionException("Entity to resolve was null", field, null);
        }
        
        if (field == null) {
            throw new IdResolutionException("Field to resolve was null", null, null);
        }
        
        if (neutralQuery == null) {
            throw new IdResolutionException("NeutralQuery for ID resolution was null", field, null);
        }

        ComplexIdNormalizer complexIdNormalizer = complexIdNormalizers.get(entity.getType() + ":" + field);
        
        if (complexIdNormalizer == null) {
            throw new IdResolutionException("No defined complex resolver", field, null);
        } else {
            return complexIdNormalizer.resolveInternalId(entity, neutralQuery, this.entityRepository);
        }
    }

    public void resolveInternalIds(Entity entity, String tenantId, EntityConfig entityConfig, ErrorReport errorReport) {
        if (entityConfig.getReferences() == null) {
            return;
        }

        try {
            for (RefDef reference : entityConfig.getReferences()) {
                String fieldPath = reference.getFieldPath();
                
                if (fieldPath.equals("body.studentId")) {
                    System.out.println("Resolving " + fieldPath);
                } else if (entity.getType().equals("studentAcademicRecord")) {
                    System.out.println("Resolving SAR");
                }
                
                String id = resolveInternalId(entity, tenantId, reference.getRef(), fieldPath, errorReport);

                if (errorReport.hasErrors()) {
                    return;
                }

                PropertyUtils.setProperty(entity, fieldPath, id);
            }
        } catch (Exception e) {
            LOG.error("Error accessing property", e);
            System.out.println("Error! " + entity.getBody());
            errorReport.error("Failed to resolve a reference1. TenantId: " + tenantId + " Entity: " + entity.getBody(), this);
        }
    }

    public String resolveInternalId(Entity entity, String tenantId, Ref refConfig, String fieldPath, ErrorReport errorReport) {
        List<String> ids = resolveInternalIds(entity, tenantId, refConfig, fieldPath, errorReport);

        if (ids.size() == 0) {
            errorReport.error("Failed to resolve a reference2. TenantId: " + tenantId + " Entity: " + entity.getBody(), this);
            return null;
        }

        return ids.get(0);
    }

    public List<String> resolveInternalIds(Entity entity, String tenantId, Ref refConfig, String fieldPath, ErrorReport errorReport) {
        ProxyErrorReport proxyErrorReport = new ProxyErrorReport(errorReport);
        
        Query filter = new Query();
        
        try {
            for (List<Field> fields : refConfig.getChoiceOfFields()) {
                Query choice = new Query();

                choice.addCriteria(Criteria.where(METADATA_BLOCK + "." + EntityMetadataKey.TENANT_ID.getKey()).is(tenantId));

                for (Field field : fields) {
                    List<Object> filterValues = new ArrayList<Object>();

                    for (FieldValue fv : field.getValues()) {
                        if (fv.getRef() != null) {
                            filterValues.addAll(resolveInternalIds(entity, tenantId, fv.getRef(), fieldPath, proxyErrorReport));
                        } else {
                            Object entityValue = PropertyUtils.getProperty(entity, fv.getValueSource());
                            if (entityValue instanceof Collection) {
                                Collection<?> entityValues = (Collection<?>) entityValue;
                                filterValues.addAll(entityValues);
                            } else if (entityValue == null) {
                                NeutralQuery neutralQuery = new NeutralQuery();
                                String tenantKey = METADATA_BLOCK + "." + EntityMetadataKey.TENANT_ID.getKey();
                                neutralQuery.addCriteria(new NeutralCriteria(tenantKey, "=", tenantId, false));
                                return this.resolveComplexInternalId(entity, fieldPath, neutralQuery);
                            } else {
                                filterValues.add(entityValue.toString());
                            }
                        }
                    }

                    choice.addCriteria(Criteria.where(field.getPath()).in(filterValues));
                }

                filter.or(choice);
            }
        } catch (Exception e) {
            LOG.error("Error accessing property", e);
            proxyErrorReport.error("Failed to resolve a reference3. TenantId: " + tenantId + " Entity: " + entity.getBody(), this);
        }

        if (proxyErrorReport.hasErrors()) {
            return null;
        }

        Iterable<Entity> foundRecords = entityRepository.findByQuery(refConfig.getCollectionName(), filter, 0, 0);

        List<String> ids = new ArrayList<String>();

        if (foundRecords != null) {
            for (Entity record : foundRecords) {
                ids.add(record.getEntityId());
            }
        }

        return ids;
    }
    
    /**
     * @return the entityRepository
     */
    public Repository<Entity> getEntityRepository() {
        return entityRepository;
    }

    /**
     * @param entityRepository the entityRepository to set
     */
    public void setEntityRepository(Repository<Entity> entityRepository) {
        this.entityRepository = entityRepository;
    }

}
