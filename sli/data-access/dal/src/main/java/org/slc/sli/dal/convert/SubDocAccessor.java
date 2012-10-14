package org.slc.sli.dal.convert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import org.slc.sli.common.domain.EmbeddedDocumentRelations;
import org.slc.sli.common.util.uuid.UUIDGeneratorStrategy;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.MongoEntity;
import org.slc.sli.validation.schema.INaturalKeyExtractor;

/**
 * Utility for accessing subdocuments that have been collapsed into a super-doc
 *
 * @author nbrown
 *
 */
public class SubDocAccessor {

    private static final Logger LOG = LoggerFactory.getLogger(SubDocAccessor.class);

    private final Map<String, Location> locations = new HashMap<String, SubDocAccessor.Location>();

    private final MongoTemplate template;

    private final UUIDGeneratorStrategy didGenerator;

    private final INaturalKeyExtractor naturalKeyExtractor;

    public SubDocAccessor(MongoTemplate template, UUIDGeneratorStrategy didGenerator,
            INaturalKeyExtractor naturalKeyExtractor) {
        this.template = template;
        this.didGenerator = didGenerator;
        this.naturalKeyExtractor = naturalKeyExtractor;
        // // this will store student assessment associations under the student documents in the
        // // assessments field
        // store("studentAssessmentAssociation").within("student").as("assessments").mapping("studentId",
        // "_id")
        // .register();
        for (String entityType : EmbeddedDocumentRelations.getSubDocuments()) {
            String parent = EmbeddedDocumentRelations.getParentEntityType(entityType);
            String parentKey = EmbeddedDocumentRelations.getParentFieldReference(entityType);
            if (parent != null && parentKey != null) {
                store(entityType).within(parent).as(entityType).mapping(parentKey, "_id").register();
            }
        }
    }

    /**
     * Start a location for a given sub doc type
     *
     * @param type
     * @return
     */
    public LocationBuilder store(String type) {
        return new LocationBuilder(type);
    }

    private class LocationBuilder {
        private Map<String, String> lookup = new HashMap<String, String>();
        private String collection;
        private String subField;
        private final String type;

        public LocationBuilder(String type) {
            super();
            this.type = type;
        }

        /**
         * Store the subdoc within the given super doc collection
         *
         * @param collection
         *            the collection the subdoc gets stored in
         * @return
         */
        public LocationBuilder within(String collection) {
            this.collection = collection;
            return this;
        }

        /**
         * The field the subdocs show up in
         *
         * @param subField
         *            The field the subdocs show up in
         * @return
         */
        public LocationBuilder as(String subField) {
            this.subField = subField;
            return this;
        }

        /**
         * Map a field in the sub doc to the super doc. This will be used when resolving parenthood
         *
         * @param subDocField
         * @param superDocField
         * @return
         */
        public LocationBuilder mapping(String subDocField, String superDocField) {
            lookup.put(superDocField, subDocField);
            return this;
        }

        /**
         * Register it as a sub resource location
         */
        public void register() {
            locations.put(type, new Location(collection, lookup, subField));
        }

    }

    /**
     * THe location of the subDoc
     *
     * @author nbrown
     *
     */
    public class Location {

        private final String collection;
        private final Map<String, String> lookup;
        private final String subField;

        /**
         * Create a new location to store subdocs
         *
         * @param collection
         *            the collection the superdoc is in
         * @param key
         *            the field in the subdoc that refers to the super doc's id
         * @param subField
         *            the place to put the sub doc
         */
        public Location(String collection, Map<String, String> lookup, String subField) {
            super();
            this.collection = collection;
            this.lookup = lookup;
            this.subField = subField;
        }

        private String getParentEntityId(String entityId) {
            return entityId.substring(0, 43);
        }

        private DBObject getParentQuery(Map<String, Object> body) {
            Query parentQuery = new Query();
            for (Entry<String, String> entry : lookup.entrySet()) {
                parentQuery.addCriteria(new Criteria(entry.getKey()).is(body.get(entry.getValue())));
            }
            return parentQuery.getQueryObject();
        }

        // this method is for supporting patch sub doc
        public boolean doUpdate(Query query, Update update) {
            DBObject queryDBObject = toSubDocQuery(query, false);
            
            DBObject elementMatch = new BasicDBObject("$elemMatch", query.getQueryObject());
            queryDBObject.put(subField, elementMatch);

            DBObject patchUpdate = toSubDocUpdate(update);
            String updateCommand = "{findAndModify:\"" + collection + "\",query:" + queryDBObject.toString()
                    + ",update:" + patchUpdate.toString() + "}";
            LOG.debug("the update date mongo command is: {}", updateCommand);
            CommandResult result = template.executeCommand(updateCommand);
            return result.get("value") != null;

        }

        // transform original update to sub doc update with positional operator $
        @SuppressWarnings("unchecked")
        private DBObject toSubDocUpdate(Update originalUpdate) {
            DBObject updateDBObject = new BasicDBObject();

            for (String key : originalUpdate.getUpdateObject().keySet()) {
                if (key.startsWith("$")) {
                    Map<String, Object> fieldAndValue = (Map<String, Object>) originalUpdate.getUpdateObject().get(key);
                    Map<String, Object> newFieldAndValue = new HashMap<String, Object>();
                    for (String field : fieldAndValue.keySet()) {
                        if (!field.startsWith("$")) {
                            newFieldAndValue.put(subField + ".$." + field, fieldAndValue.get(field));
                        }
                    }
                    updateDBObject.put(key, newFieldAndValue);
                }
            }
            return updateDBObject;
        }

        private boolean doUpdate(DBObject parentQuery, List<Entity> subEntities) {
            boolean result = true;
            result &= template.getCollection(collection)
                    .update(parentQuery, buildPullObject(subEntities), false, false).getLastError().ok();
            result &= template.getCollection(collection)
                    .update(parentQuery, buildPushObject(subEntities), false, false).getLastError().ok();
            return result;
        }

        private DBObject buildPullObject(List<Entity> subEntities) {
            Set<String> existingIds = new HashSet<String>();
            for (Entity entity : subEntities) {
                if (entity.getEntityId() != null && !entity.getEntityId().isEmpty()) {
                    existingIds.add(entity.getEntityId());
                }
            }
            existingIds.addAll(getSubDocDids(subEntities));
            Query pullQuery = new Query(Criteria.where("_id").in(existingIds));
            Update update = new Update();
            update.pull(subField, pullQuery.getQueryObject());
            return update.getUpdateObject();
        }

        private DBObject buildPushObject(List<Entity> subEntities) {
            List<DBObject> subDocs = new ArrayList<DBObject>();
            for (Entity entity : subEntities) {
                subDocs.add(subDocToDBObject(entity));
            }
            Update update = new Update();
            update.pushAll(subField, subDocs.toArray());
            return update.getUpdateObject();
        }

        private List<String> getSubDocDids(List<Entity> subEntities) {
            List<String> subDocDids = new ArrayList<String>();
            for (Entity entity : subEntities) {
                subDocDids.add((String) subDocToDBObject(entity).get("_id"));
            }
            return subDocDids;
        }

        private DBObject subDocToDBObject(Entity entity) {
            MongoEntity mongoEntity;

            if (entity instanceof MongoEntity) {
                mongoEntity = (MongoEntity) entity;
            } else {
                mongoEntity = new MongoEntity(entity.getType(), entity.getEntityId(), entity.getBody(),
                        entity.getMetaData());
            }

            DBObject dbObject = mongoEntity.toDBObject(didGenerator, naturalKeyExtractor);

            return dbObject;
        }

        private Entity convertDBObjectToSubDoc(DBObject dbObject) {
            return MongoEntity.fromDBObject(dbObject);
        }

        private boolean bulkUpdate(DBObject parentQuery, List<Entity> newEntities) {
            return doUpdate(parentQuery, newEntities);
        }

        public boolean create(Entity entity) {
            // return update(makeEntityId(entity), entity.getBody());
            DBObject parentQuery = getParentQuery(entity.getBody());
            List<Entity> subEntities = new ArrayList<Entity>();
            subEntities.add(entity);
            return doUpdate(parentQuery, subEntities);
        }

        public boolean delete(String id) {
            Entity entity = findById(id);
            if (entity == null) {
                return false;
            }
            DBObject parentQuery = getParentQuery(entity.getBody());
            List<Entity> subEntities = new ArrayList<Entity>();
            subEntities.add(entity);

            return template.getCollection(collection).update(parentQuery, buildPullObject(subEntities), false, false)
                    .getLastError().ok();
        }

        private boolean bulkCreate(DBObject parentQuery, List<Entity> entities) {
            return bulkUpdate(parentQuery, entities);
        }

        public boolean insert(List<Entity> entities) {
            ConcurrentMap<DBObject, List<Entity>> parentEntityMap = new ConcurrentHashMap<DBObject, List<Entity>>();
            for (Entity entity : entities) {
                DBObject parentQuery = getParentQuery(entity.getBody());
                parentEntityMap.putIfAbsent(parentQuery, new ArrayList<Entity>());
                parentEntityMap.get(parentQuery).add(entity);
            }
            boolean result = true;
            for (Entry<DBObject, List<Entity>> entry : parentEntityMap.entrySet()) {
                result &= bulkCreate(entry.getKey(), entry.getValue());
                // result &= doUpdate(entry.getKey(), entry.getValue());
            }
            return result;
        }

        public Entity findById(String id) {
            LOG.info("the subDoc id is: {}", id);
            Query subDocQuery = new Query(Criteria.where(subField + "." + "_id").is(id));
            Query parentQuery = subDocQuery;
            if (!id.equals(getParentId(id))) {
                parentQuery = new Query(Criteria.where("_id").is(getParentId(id)));
            }
            List<Entity> entities = findSubDocs(parentQuery.getQueryObject(), subDocQuery.getQueryObject(),
                    new Query().getQueryObject());
            if (entities != null && entities.size() == 1) {
                return entities.get(0);
            }
            return null;

        }

        public List<Entity> findAll(Query originalQuery) {
            DBObject subDocQueryDBObject = toSubDocQuery(originalQuery, false);
            DBObject parentQueryDBObject = toSubDocQuery(originalQuery, true);
            List<Entity> entities = findSubDocs(parentQueryDBObject, subDocQueryDBObject, getLimitQuery(originalQuery));
            return entities;
        }

        // convert original query match criteria to match embeded subDocs
        @SuppressWarnings("unchecked")
        private DBObject toSubDocQuery(Query originalQuery, boolean isParentQuery) {
            return toSubDocQuery(originalQuery.getQueryObject(), isParentQuery);
        }

        @SuppressWarnings("unchecked")
        private DBObject toSubDocQuery(DBObject originalQueryDBObject, boolean isParentQuery) {

            DBObject queryDBObject = appendSubField(originalQueryDBObject, isParentQuery);
            for (String key : originalQueryDBObject.keySet()) {
                if (key.equals("$or") || key.equals("$and")) {
                    List<DBObject> originalOrQueryDBObjects = (List<DBObject>) originalQueryDBObject.get(key);
                    List<DBObject> orQueryDBObjects = new ArrayList<DBObject>();
                    for (DBObject originalOrQueryDBObject : originalOrQueryDBObjects) {
                        DBObject orQueryDBObject = appendSubField(originalOrQueryDBObject, isParentQuery);
                        orQueryDBObjects.add(orQueryDBObject);
                    }
                    queryDBObject.put(key, orQueryDBObjects);
                }
            }
            return queryDBObject;
        }
        
        private String getParentId(String embededId) {
            String parentId = embededId;
            if (embededId.split("_id").length == 2) {
                parentId = embededId.split("_id")[0] + "_id";
            }
            return parentId;
        }

        // retrieve limit/offset/sort info from the original query and make them applicable to
        // subDocs
        private DBObject getLimitQuery(Query originalQuery) {
            DBObject limitQueryDBObject = new Query().getQueryObject();
            DBObject originalSortDBObject = originalQuery.getSortObject();
            if (originalSortDBObject != null && originalSortDBObject.keySet().size() > 0) {
                limitQueryDBObject.put("$sort", appendSubField(originalSortDBObject, false));
            }
            if (originalQuery.getSkip() > 0) {
                limitQueryDBObject.put("$skip", originalQuery.getSkip());
            }
            if (originalQuery.getLimit() > 0) {
                limitQueryDBObject.put("$limit", originalQuery.getLimit());
            }
            return limitQueryDBObject;
        }

        // append subField to original query key, so it can query in subDocs
        @SuppressWarnings("unchecked")
        private DBObject appendSubField(DBObject originalDBObject, boolean isParentQuery) {
            DBObject newDBObject = new Query().getQueryObject();
            for (String key : originalDBObject.keySet()) {
                if (!key.startsWith("$")) {
                    String newKey = key;
                    Object newValue = originalDBObject.get(key);
                    
                    if (isParentQuery && key.equals("_id") && getId(newValue) != null
                            && !getId(newValue).equals(getParentId(getId(newValue)))) {
                        // use parent id for id query
                        newDBObject.put(newKey, getParentId(getId(newValue)));
                    } else if (isParentQuery && key.equals("metaData.tenantId")) {
                        // assume the super doc has same tenantId as sub Doc
                        newDBObject.put(newKey, newValue);
                    } else {
                        // for other query, append the subfield to original key
                        newKey = subField + "." + key;
                        newDBObject.put(newKey, newValue);
                    }
                } else if (key.equals("$or") || key.equals("$and")) {
                    List<DBObject> dbObjects = (List<DBObject>) originalDBObject.get(key);
                    List<DBObject> orQueryDBObjects = new ArrayList<DBObject>();
                    for (DBObject dbObject : dbObjects) {
                        orQueryDBObjects.add(toSubDocQuery(dbObject, isParentQuery));
                    }
                    newDBObject.put(key, orQueryDBObjects);
                }
            }
            return newDBObject;
        }
        
        // retrieve the a single id from DBObject value for "_id" field
        private String getId(Object queryValue) {
            if (queryValue instanceof String) {
                return (String) queryValue;
            } else if (queryValue instanceof DBObject) {
                DBObject dbValue = (DBObject) queryValue;
                Object inQuery = dbValue.get("$in");
                if (inQuery != null && inQuery instanceof List<?> && ((List<?>) inQuery).size() == 1) {
                    return ((List<?>) inQuery).get(0).toString();
                } else if (inQuery != null && inQuery instanceof Object[] && ((Object[]) inQuery).length == 1) {
                    return ((Object[]) inQuery)[0].toString();
                }
            }
            return null;
        }

        @SuppressWarnings("unchecked")
        private List<Entity> findSubDocs(DBObject parentQuery, DBObject subDocQuery, DBObject limitQuery) {
            StringBuilder limitQuerySB = new StringBuilder();
            if (limitQuery != null && limitQuery.keySet().size() > 0) {
                if (limitQuery.get("$sort") != null) {
                    limitQuerySB.append(",{$sort:" + limitQuery.get("$sort").toString() + "}");
                }
                if (limitQuery.get("$skip") != null) {
                    limitQuerySB.append(",{$skip:" + limitQuery.get("$skip") + "}");
                }
                if (limitQuery.get("$limit") != null) {
                    limitQuerySB.append(",{$limit:" + limitQuery.get("$limit") + "}");
                }
            }
            String queryCommand = "{aggregate : \"" + collection + "\", pipeline:[{$match : " + parentQuery.toString()
                    + "},{$project : {\"" + subField + "\":1,\"_id\":0 } },{$unwind: \"$" + subField + "\"},{$match:"
                    + subDocQuery.toString() + "}" + limitQuerySB.toString() + "]}";
            LOG.info("the aggregate query command is: {}", queryCommand);
            CommandResult result = template.executeCommand(queryCommand);
            List<DBObject> subDocs = (List<DBObject>) result.get("result");
            List<Entity> entities = new ArrayList<Entity>();
            for (DBObject dbObject : subDocs) {
                entities.add(convertDBObjectToSubDoc(((DBObject) dbObject.get(subField))));
            }

            return entities;
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> read(String id, Criteria additionalCriteria) {
            Query query = Query.query(Criteria.where("_id").is(getParentEntityId(id)));
            query.fields().include(getField(id));
            if (additionalCriteria != null) {
                query.addCriteria(additionalCriteria);
            }
            Map<?, ?> result = template.findOne(query, Map.class, collection);
            if (result == null) {
                return null;
            }
            return (Map<String, Object>) ((Map<String, Object>) result.get(subField)).get(id);
        }

        private String getField(String id) {
            return subField + "." + id;
        }

        /*
         * Returns a query to find the subdoc with the given id.
         * Note: Firgure out how to merge it with buildSubDocQuery method
         */
        private DBObject getExactSubDocQuery(String id) {
            // String targetDoc = "body." + lookup.get("_id") + "." + id;
            DBObject query = new BasicDBObject();
            query.put(subField + "._id", id);
            // query.put(targetDoc, new BasicDBObject("$exists", true));
            return query;
        }

        public boolean exists(String id) {
            DBObject query = this.getExactSubDocQuery(id);
            return template.getCollection(collection).count(query) > 0;
        }

        // Note: This is suboptimal and too memory intensive. Should be implemented
        // by iterating over the cursor without instantiating instances of MongoEntity as
        // done in the find(..) method, which should also be refactored.
        public long count(Query query) {
            return this.findAll(query).size();
        }

        // public boolean delete(String id) {
        // String targetDoc = "body." + lookup.get("_id") + "." + id;
        // DBObject query = this.getExactSubDocQuery(id);
        // Update update = new Update();
        // update.unset(targetDoc);
        // return this.doUpdate(query, update);
        // }

        public void deleteAll(Query query) {
            for (Entity e : findAll(query)) {
                delete(e.getEntityId());
            }
        }
    }

    public boolean isSubDoc(String docType) {
        return locations.containsKey(docType);
    }

    public Location subDoc(String docType) {
        return locations.get(docType);
    }
}
