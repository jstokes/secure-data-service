package org.slc.sli.dal.convert;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.slc.sli.common.domain.EmbeddedDocumentRelations;
import org.slc.sli.dal.TenantContext;
import org.slc.sli.domain.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Utility for denormalizing entities
 *
 * @author srupasinghe
 */
public class Denormalizer {

    private static final Logger LOG = LoggerFactory.getLogger(Denormalizer.class);

    private final Map<String, Denormalization> denormalizations = new HashMap<String, Denormalization>();

    private final MongoTemplate template;

    public Denormalizer(MongoTemplate template) {
        this.template = template;

        for (String entityType : EmbeddedDocumentRelations.getDenormalizedDocuments()) {
            String toEntity = EmbeddedDocumentRelations.getDenormalizeToEntity(entityType);
            String field = EmbeddedDocumentRelations.getDenormalizedToField(entityType);
            Map<String, String> referenceKeys = EmbeddedDocumentRelations.getReferenceKeys(entityType);
            String idKey = EmbeddedDocumentRelations.getDenormalizedIdKey(entityType);
            List<String> denormalizedFields = EmbeddedDocumentRelations.getDenormalizedFields(entityType);

            if (toEntity != null && referenceKeys != null) {
                denormalize(entityType).data(denormalizedFields).to(toEntity).as(field).using(referenceKeys).idKey(idKey).register();
            }
        }
    }

    /**
     * Builds a denormalization
     *
     * @param type
     * @return
     */
    public DenormalizationBuilder denormalize(String type) {
        return new DenormalizationBuilder(type);
    }

    private class DenormalizationBuilder {
        private String collection;
        private String field;
        private final String type;
        private Map<String, String> referenceKeys;
        private String idKey;
        private List<String> fields;

        public DenormalizationBuilder(String type) {
            super();
            this.type = type;
        }

        public DenormalizationBuilder to(String collection) {
            this.collection = collection;
            return this;
        }

        public DenormalizationBuilder as(String field) {
            this.field = field;
            return this;
        }

        public DenormalizationBuilder using(Map<String, String> referenceKeys) {
            this.referenceKeys = referenceKeys;
            return this;
        }

        public DenormalizationBuilder data(List<String> fields) {
            this.fields = fields;
            return this;
        }

        public DenormalizationBuilder idKey(String idKey) {
            this.idKey = idKey;
            return this;
        }

        public void register() {
            denormalizations.put(type, new Denormalization(collection, field, referenceKeys, idKey, fields));
        }

    }

    /**
     * Encapsulates the de-normalizing logic for each entity
     *
     */
    public class Denormalization {

        private String denormalizeToEntity;
        private Map<String, String> denormalizationReferenceKeys;
        private String denormalizedIdKey;
        private List<String> denormalizedFields;
        private String denormalizedToField;

        public Denormalization(String denormalizeToEntity, String denormalizedToField, Map<String, String> denormalizationReferenceKeys,
                               String denormalizedIdKey, List<String> denormalizedFields) {
            this.denormalizeToEntity = denormalizeToEntity;
            this.denormalizedToField = denormalizedToField;
            this.denormalizationReferenceKeys = denormalizationReferenceKeys;
            this.denormalizedIdKey = denormalizedIdKey;
            this.denormalizedFields = denormalizedFields;
        }

        public boolean create(Entity entity) {
            DBObject parentQuery = getParentQuery(entity.getBody());

            List<Entity> entities = new ArrayList<Entity>();
            entities.add(entity);

            return doUpdate(parentQuery, entities);
        }

        private DBObject getParentQuery(Map<String, Object> body) {
            Query parentQuery = new Query();

            for (Map.Entry<String, String> entry : denormalizationReferenceKeys.entrySet()) {
                String value = (String) body.get(entry.getKey());

                if (entry.getValue().equals("_id")) {
                    parentQuery.addCriteria(new Criteria(entry.getValue()).is(value));
                } else {
                    parentQuery.addCriteria(new Criteria("body." + entry.getValue()).is(value));
                }
            }

            return parentQuery.getQueryObject();
        }

        private BasicDBObject getDbObject(Entity entity) {
            Map<String, Object> body = entity.getBody();
            BasicDBObject dbObj = new BasicDBObject();
            String internalId = null;

            if (denormalizedIdKey.equals("_id")) {
                internalId = entity.getEntityId();
            } else {
                internalId = (String) body.get(denormalizedIdKey);
            }

            //add the id field
            dbObj.put("_id", internalId);

            for (String field : denormalizedFields) {
                if (body.containsKey(field)) {
                    dbObj.put(field, body.get(field));
                }
            }

            return dbObj;
        }

        private boolean doUpdate(DBObject parentQuery, List<Entity> entities) {
            boolean result = true;

            TenantContext.setIsSystemCall(false);
            result &= template.getCollection(denormalizeToEntity)
                    .update(parentQuery, buildPullObject(entities), false, true).getLastError().ok();
            result &= template.getCollection(denormalizeToEntity)
                    .update(parentQuery, buildPushObject(entities), false, true).getLastError().ok();

            return result;
        }

        private DBObject buildPushObject(List<Entity> entities) {
            List<DBObject> docs = new ArrayList<DBObject>();
            for (Entity entity : entities) {
                docs.add(getDbObject(entity));
            }

            Update update = new Update();
            update.pushAll(denormalizedToField, docs.toArray());

            return update.getUpdateObject();
        }

        private DBObject buildPullObject(List<Entity> entities) {
            Set<String> existingIds = new HashSet<String>();
            Query pullQuery = new Query();

            for (Entity entity : entities) {
                String internalId = null;
                if (denormalizedIdKey.equals("_id")) {
                    internalId = entity.getEntityId();
                } else {
                    internalId = (String) entity.getBody().get(denormalizedIdKey);
                }
                existingIds.add(internalId);
            }

            pullQuery.addCriteria(Criteria.where("_id").in(existingIds));

            Update update = new Update();
            update.pull(denormalizedToField, pullQuery.getQueryObject());

            return update.getUpdateObject();
        }

        public boolean insert(List<Entity> entities) {
            ConcurrentMap<DBObject, List<Entity>> parentEntityMap = new ConcurrentHashMap<DBObject, List<Entity>>();
            for (Entity entity : entities) {
                DBObject parentQuery = getParentQuery(entity.getBody());
                parentEntityMap.putIfAbsent(parentQuery, new ArrayList<Entity>());
                parentEntityMap.get(parentQuery).add(entity);
            }
            boolean result = true;
            for (Map.Entry<DBObject, List<Entity>> entry : parentEntityMap.entrySet()) {
                result &= bulkCreate(entry.getKey(), entry.getValue());
            }
            return result;
        }

        private boolean bulkCreate(DBObject parentQuery, List<Entity> entities) {
            return bulkUpdate(parentQuery, entities);
        }

        private boolean bulkUpdate(DBObject parentQuery, List<Entity> newEntities) {
            return doUpdate(parentQuery, newEntities);
        }

    }

    public boolean isDenormalizedDoc(String docType) {
        return denormalizations.containsKey(docType);
    }

    public Denormalization denormalization(String docType) {
        return denormalizations.get(docType);
    }
}
