package org.slc.sli.dal.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.QueryParseException;

import org.slc.sli.dal.convert.IdConverter;
import org.slc.sli.dal.encrypt.EntityEncryption;
import org.slc.sli.validation.SchemaRepository;
import org.slc.sli.validation.schema.ListSchema;
import org.slc.sli.validation.schema.NeutralSchema;

/**
 * mongodb implementation of the entity repository interface that provides basic
 * CRUD and field query methods for entities including core entities and
 * association entities
 * 
 * @author Dong Liu dliu@wgen.net
 * @author kmyers
 */
@Component
public class MongoQueryConverter {
    private static final String MONGO_ID = "_id";
    private static final String MONGO_BODY = "body.";
    private static final String ENCRYPTION_ERROR = "Unable to perform search operation on PII field ";
    
    private static final Logger LOG = LoggerFactory.getLogger(MongoQueryConverter.class);

    @Autowired
    private IdConverter idConverter;

    @Autowired(required = false)
    private EntityEncryption encryptor;

    @Autowired
    private SchemaRepository schemaRepo;
    
    /**
     * Each operator (>, <, !=, etc) mapped to how to create a Mongo criteria for it
     * 
     * 
     * @author kmyers
     *
     */
    protected interface MongoCriteriaGenerator {
        public Criteria generateCriteria(String key, Object value);
    }
    
    private Map<String, MongoCriteriaGenerator> operatorImplementations = new HashMap<String, MongoCriteriaGenerator>();
    
    public MongoQueryConverter() {

        // =
        this.operatorImplementations.put("=", new MongoCriteriaGenerator() {
            public Criteria generateCriteria(String key, Object value) {
                if (key.equals(MONGO_ID)) {
                    List<Object> databaseIds = new ArrayList<Object>();
                    String ids = (String) value;
                    for (String id : ids.split(",")) {
                        Object databaseId = idConverter.toDatabaseId(id);
                        if (databaseId == null) {
                            LOG.debug("Unable to process id {}", new Object[] { id });
                        }
                        databaseIds.add(databaseId);
                    }
                    return Criteria.where(MONGO_ID).in(databaseIds);
                } else {
                    return Criteria.where(MONGO_BODY + key).is(value);
                }
            }
        });
        
        // =
        this.operatorImplementations.put("in", new MongoCriteriaGenerator() {
            public Criteria generateCriteria(String key, Object value) {
                if (key.equals(MONGO_ID)) {
                    try {
                        
                        List<String> rawIds = (List<String>) value;
                        List<Object> databaseIds = new ArrayList<Object>();
                        for (String id : rawIds) {
                            Object databaseId = idConverter.toDatabaseId(id);
                            if (databaseId == null) {
                                LOG.debug("Unable to process id {}", new Object[] { id });
                            }
                            databaseIds.add(databaseId);
                        }
                        return Criteria.where(MONGO_ID).in(databaseIds);
                    } catch (ClassCastException cce) {
                        LOG.debug("input not a list: {}", value);
                        return Criteria.where(MONGO_ID).in(value);
                    } catch (Exception e) {
                        LOG.debug("input not a list2: {}{}", value, e);
                        return Criteria.where(MONGO_ID).in(value);
                    }
                } else {
                    return Criteria.where(MONGO_BODY + key).in((List<Object>) value);
                }
            }
        });

        // >=
        this.operatorImplementations.put(">=", new MongoCriteriaGenerator() {
            public Criteria generateCriteria(String key, Object value) {
                return Criteria.where(MONGO_BODY + key).gte(value);
            }
        });

        // <=
        this.operatorImplementations.put("<=", new MongoCriteriaGenerator() {
            public Criteria generateCriteria(String key, Object value) {
                return Criteria.where(MONGO_BODY + key).lte(value);
            }
        });
        
        // !=
        this.operatorImplementations.put("!=", new MongoCriteriaGenerator() {
            public Criteria generateCriteria(String key, Object value) {
                return Criteria.where(MONGO_BODY + key).ne(value);
            }
        });
        
        // =~
        this.operatorImplementations.put("=~", new MongoCriteriaGenerator() {
            public Criteria generateCriteria(String key, Object value) {
                return Criteria.where(MONGO_BODY + key).regex((String) value);
            }
        });
        
        // <
        this.operatorImplementations.put("<", new MongoCriteriaGenerator() {
            public Criteria generateCriteria(String key, Object value) {
                return Criteria.where(MONGO_BODY + key).lt(value);
            }
        });
        
        // >
        this.operatorImplementations.put(">", new MongoCriteriaGenerator() {
            public Criteria generateCriteria(String key, Object value) {
                return Criteria.where(MONGO_BODY + key).gt(value);
            }
        });
    }
    
    /**
     * Converts a given neutral query into a mongo Query object where each argument has
     * been converted into the proper type.
     * 
     * 
     * 
     * @param entityName name of collection being queried against
     * @param neutralQuery database independent representation of query to be read
     * @return a mongo specific database query implementation of the neutral query
     */
    public Query convert(String entityName, NeutralQuery neutralQuery) {
        Query mongoQuery = new Query();
        
        if (neutralQuery != null) {
            // Include fields
            if (neutralQuery.getIncludeFields() != null) {
                for (String includeField : neutralQuery.getIncludeFields().split(",")) {
                    mongoQuery.fields().include(MONGO_BODY + includeField);
                }
            } else if (neutralQuery.getExcludeFields() != null) {
                for (String excludeField : neutralQuery.getExcludeFields().split(",")) {
                    mongoQuery.fields().exclude(MONGO_BODY + excludeField);
                }
            }
            
            // offset
            if (neutralQuery.getOffset() > 0) {
                mongoQuery.skip(neutralQuery.getOffset());
            }
            
            // limit
            if (neutralQuery.getLimit() > 0) {
                mongoQuery.limit(neutralQuery.getLimit());
            }

            NeutralSchema entitySchema = this.schemaRepo.getSchema(entityName);
            
            // sorting
            if (neutralQuery.getSortBy() != null) {
                if (neutralQuery.getSortOrder() != null) {
                    Order sortOrder = neutralQuery.getSortOrder().equals(NeutralQuery.SortOrder.ascending) ? Order.ASCENDING
                            : Order.DESCENDING;
                    mongoQuery.sort().on(MONGO_BODY + neutralQuery.getSortBy(), sortOrder);
                } else { // default to ascending order
                    mongoQuery.sort().on(MONGO_BODY + neutralQuery.getSortBy(), Order.ASCENDING);
                }
                
                NeutralSchema fieldSchema = this.getFieldSchema(entitySchema, neutralQuery.getSortBy());
                if (fieldSchema != null && fieldSchema.isPii()) {
                    throw new QueryParseException(ENCRYPTION_ERROR + " cannot be sorted on", neutralQuery.toString());
                }
            }

            // other criteria
            for (NeutralCriteria neutralCriteria : neutralQuery.getCriteria()) {
                String key = neutralCriteria.getKey();
                String operator = neutralCriteria.getOperator();
                Object value = neutralCriteria.getValue();
                NeutralSchema fieldSchema = this.getFieldSchema(entitySchema, key);
                
                if (fieldSchema != null) {
                    value = fieldSchema.convert(neutralCriteria.getValue());
                    if (fieldSchema.isPii()) {
                        if (operator.contains("<") || operator.contains(">") || operator.contains("~")) {
                            throw new QueryParseException(ENCRYPTION_ERROR + value, neutralQuery.toString());
                        }
                        
                        if (encryptor != null) {
                            value = encryptor.encryptSingleValue(value);
                        }
                    }
                }
                
                mongoQuery.addCriteria(this.operatorImplementations.get(operator).generateCriteria(key, value));
            }
            
            Query[] mongoOrQueries = this.translateQueries(entityName, neutralQuery.getOrQueries());
            if (mongoOrQueries.length > 0) {
                mongoQuery.or(mongoOrQueries);
            }
        }

        return mongoQuery;
    }
    
    private Query[] translateQueries(String entityName, List<NeutralQuery> queries) {
        if (queries == null || queries.isEmpty()) {
            return new Query[]{};
        }
        
        Query[] mongoQueries = new Query[queries.size()];
        
        for (int i = 0; i < mongoQueries.length; i++) {
            mongoQueries[i] = this.convert(entityName, queries.get(i));
        }
        
        return mongoQueries;
    }
    

    private NeutralSchema getNestedSchema(NeutralSchema schema, String field) {
        if (schema == null)
            return null;
        
        switch (schema.getSchemaType()) {
            case STRING:
            case INTEGER:
            case DATE:
            case TIME:
            case DATETIME:
            case ID:
            case IDREF:
            case INT:
            case LONG:
            case DOUBLE:
            case BOOLEAN:
            case TOKEN:
                return null;
            case LIST:
                for (NeutralSchema possibleSchema : ((ListSchema) schema).getList()) {
                    LOG.info("possible schema type is {}", possibleSchema.getSchemaType());
                    if (getNestedSchema(possibleSchema, field) != null) {
                        return getNestedSchema(possibleSchema, field);
                    }
                }
                return null;
            case COMPLEX:
                for (String key : schema.getFields().keySet()) {
                    NeutralSchema possibleSchema = schema.getFields().get(key);
                    if (key.startsWith("*")) {
                        key = key.substring(1);
                    }
                    if (key.equals(field)) {
                        return possibleSchema;
                    }
                }
                return null;
            default: {
                throw new RuntimeException("Unknown Schema Type: " + schema.getSchemaType());
            }
        }
    }
    
    private NeutralSchema getFieldSchema(NeutralSchema schema, String dottedField) {
        for (String field : dottedField.split("\\.")) {
            schema = this.getNestedSchema(schema, field);
            if (schema != null) {
                LOG.info("nested schema type is {}", schema.getSchemaType());
            } else
                LOG.info("nested schema type is {}", "NULL");
        }
        
        return schema;
    }
    
}
