package org.slc.sli.dal.encrypt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.slc.sli.validation.SchemaRepository;
import org.slc.sli.validation.schema.ChoiceSchema;
import org.slc.sli.validation.schema.ComplexSchema;
import org.slc.sli.validation.schema.ListSchema;
import org.slc.sli.validation.schema.NeutralSchema;

/**
 * Provides encryption/decryption of entities based upon what is marked as PII in the data model.
 * 
 * @author Ryan Farris <rfarris@wgen.net>
 * 
 */
public class EntityEncryption {
    private static final Logger LOG = LoggerFactory.getLogger(EntityEncryption.class);
    
    @Autowired
    Cipher aes;
    
    @Autowired
    SchemaRepository schemaReg;
    
    Map<NeutralSchema, Map<String, Object>> piiMapCache = new ConcurrentHashMap<NeutralSchema, Map<String, Object>>();
    
    public Map<String, Object> encrypt(String entityType, Map<String, Object> body) {
        NeutralSchema schema = schemaReg.getSchema(entityType);
        Map<String, Object> piiMap = buildPiiMap(schema);
        if (piiMap == null) {
            return body;
        }
        
        Map<String, Object> clonedEntity = cloneEntity(body);
        encryptInPlace(piiMap, clonedEntity, Operation.ENCRYPT);
        return clonedEntity;
    }
    
    public Map<String, Object> decrypt(String entityType, Map<String, Object> body) {
        NeutralSchema schema = schemaReg.getSchema(entityType);
        Map<String, Object> piiMap = buildPiiMap(schema);
        if (piiMap == null) {
            return body;
        }
        
        Map<String, Object> clonedEntity = cloneEntity(body);
        encryptInPlace(piiMap, clonedEntity, Operation.DECRYPT);
        return clonedEntity;
    }
    
    public String encryptSingleValue(Object value) {
        return aes.encrypt(value);
    }
    
    public Object decryptSingleValue(Object value) {
        if (!(value instanceof String)) {
            LOG.warn("Value was expected to be encrypted but wasn't: " + value);
        }
        Object decrypted = aes.decrypt((String) value);
        if (decrypted == null) {
            LOG.warn("Value was expected to be encrypted but wasn't: " + value);
            return value;
        }
        return decrypted;
    }
    
    private static enum Operation {
        ENCRYPT, DECRYPT;
    }
    
    @SuppressWarnings({ "unchecked" })
    private void encryptInPlace(Map<String, Object> piiMap, Map<String, Object> body, Operation op) {
        if (piiMap == null) {
            return;
        }
        for (Entry<String, Object> piiField : piiMap.entrySet()) {
            Object fieldValue = body.get(piiField.getKey());
            if (fieldValue == null) {
                LOG.debug("PII field was null: {}", piiField.getKey());
                continue;
            } else if (fieldValue instanceof Map) {
                if (!(piiField.getValue() instanceof Map)) {
                    LOG.error("Expected field to be a Map: " + piiField.getKey());
                    continue;
                }
                encryptInPlace((Map<String, Object>) piiField.getValue(), (Map<String, Object>) fieldValue, op);
            } else if (fieldValue instanceof List) {
                List<Object> list = (List<Object>) fieldValue;
                LOG.debug("En/decrypting PII list: {}, size={}", piiField.getKey(), list.size());
                for (int i = 0; i < list.size(); i++) {
                    Object item = list.get(i);
                    if (item instanceof Map) {
                        encryptInPlace((Map<String, Object>) piiField.getValue(), (Map<String, Object>) item, op);
                    } else if (item instanceof List) {
                        LOG.error("Unexpected situation, List inside a List: " + piiField.getKey());
                        continue;
                    } else {
                        Object newValue;
                        if (Operation.ENCRYPT == op) {
                            newValue = aes.encrypt(item);
                        } else {
                            if (item instanceof String) {
                                newValue = aes.decrypt((String) item);
                                if (newValue == null) {
                                    LOG.warn("Data was expected to be encrypted but wasn't: " + piiField.getKey()
                                            + " = " + item);
                                    newValue = item;
                                }
                            } else {
                                LOG.warn("Data was expected to be encrypted but wasn't: " + piiField.getKey() + " = "
                                        + item);
                                newValue = item;
                            }
                        }
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("En/decrypting PII list field: {}, item={}, old={}, new={}", 
                                    new Object[] {piiField.getKey(), i, item, newValue});
                        }
                        list.set(i, newValue);
                    }
                }
                continue;
            } else {
                Object newValue;
                if (Operation.ENCRYPT == op) {
                    newValue = aes.encrypt(fieldValue);
                } else {
                    if (fieldValue instanceof String) {
                        newValue = aes.decrypt((String) fieldValue);
                        if (newValue == null) {
                            LOG.warn("Data was expected to be encrypted but wasn't: " + piiField.getKey() + " = "
                                    + fieldValue);
                            newValue = fieldValue;
                        }
                    } else {
                        LOG.warn("Data was expected to be encrypted but wasn't: " + piiField.getKey() + " = "
                                + fieldValue);
                        newValue = fieldValue;
                    }
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("En/decrypting PII value: {}, old={}, new={}", new Object[] {piiField.getKey(), fieldValue, newValue});
                }
                body.put(piiField.getKey(), newValue);
            }
            
        }
    }
    
    private Map<String, Object> cloneEntity(Map<String, Object> body) {
        Map<String, Object> clone = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : body.entrySet()) {
            cloneEntity(clone, entry.getKey(), entry.getValue());
        }
        return clone;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void cloneEntity(Object parent, String key, Object value) {
        if (value instanceof Map) {
            Map<String, Object> map = new HashMap<String, Object>();
            if (parent instanceof Map) {
                ((Map) parent).put(key, map);
            } else if (parent instanceof List) {
                ((List) parent).add(map);
            } else {
                throw new IllegalArgumentException("Data is not valid");
            }
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) value).entrySet()) {
                cloneEntity(map, entry.getKey(), entry.getValue());
            }
        } else if (value instanceof List) {
            List<Object> list = new ArrayList<Object>();
            if (parent instanceof Map) {
                ((Map) parent).put(key, list);
            } else if (parent instanceof List) {
                ((List) parent).add(list);
            } else {
                throw new IllegalArgumentException("Data is not valid");
            }
            for (Object child : (List) value) {
                cloneEntity(list, null, child);
            }
        } else {
            if (parent instanceof Map) {
                ((Map) parent).put(key, value);
            } else if (parent instanceof List) {
                ((List) parent).add(value);
            } else {
                throw new IllegalArgumentException("Data is not valid");
            }
        }
    }
    
    /**
     * Build a tree structure where all leaves are fields that should be PII.
     * Returns null if no data for this schema is PII
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> buildPiiMap(NeutralSchema schema) {
        if (schema == null) {
            return null;
        }
        Map<String, Object> piiMap = piiMapCache.get(schema);
        if (piiMap == null) {
            piiMap = (Map<String, Object>) recursiveBuildPiiMap(schema);
            if (piiMap == null) {
                piiMap = new HashMap<String, Object>();
            }
            piiMapCache.put(schema, piiMap);
        }
        if (piiMap.size() == 0) {
            return null;
        } else {
            return piiMap;
        }
    }
    
    private Object recursiveBuildPiiMap(NeutralSchema schema) {
        if (schema == null) {
            return null;
        }
        if (schema instanceof ComplexSchema || schema instanceof ChoiceSchema) {
            Map<String, Object> fields = new HashMap<String, Object>();
            for (Map.Entry<String, NeutralSchema> field : schema.getFields().entrySet()) {
                Object result = recursiveBuildPiiMap(field.getValue());
                if (result != null) {
                    fields.put(field.getKey(), result);
                }
            }
            return fields.size() > 0 ? fields : null;
        } else if (schema instanceof ListSchema) {
            List<NeutralSchema> possibleSchemas = ((ListSchema) schema).getList();
            if (possibleSchemas.size() != 1) {
                // TODO remove when multiple list types are removed. This is a tech debt item.
                // ChoiceSchema should be used for this case.
                throw new RuntimeException("Unable to handle multiple list schemas.");
            }
            NeutralSchema listContent = possibleSchemas.get(0);
            return recursiveBuildPiiMap(listContent);
        } else {
            if (schema.getAppInfo() == null) {
                return null;
            }
            return schema.getAppInfo().isPersonallyIdentifiableInfo() ? true : null;
        }
    }
}
