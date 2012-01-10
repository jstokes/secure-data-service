package org.slc.sli.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.slc.sli.dal.repository.EntityRepository;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.MongoEntity;
import org.springframework.stereotype.Component;

/**
 * Mock implementation of the EntityRepository for unit testing.
 * 
 */
@Component
public class MockRepo implements EntityRepository {
    
    private Map<String, Map<String, Entity>> repo = new HashMap<String, Map<String, Entity>>();
    private static String[] reservedQueryKeys = { "start-index", "max-results", "query" };
    
    AtomicLong nextID = new AtomicLong();
    
    public MockRepo() {
        repo.put("student", new LinkedHashMap<String, Entity>());
        repo.put("school", new LinkedHashMap<String, Entity>());
        repo.put("roles", new LinkedHashMap<String, Entity>());
        repo.put("studentschoolassociation", new LinkedHashMap<String, Entity>());
        repo.put("teacher", new LinkedHashMap<String, Entity>());
        repo.put("section", new LinkedHashMap<String, Entity>());
        repo.put("assessment", new LinkedHashMap<String, Entity>());
        repo.put("studentassessmentassociation", new LinkedHashMap<String, Entity>());
        repo.put("studentsectionassociation", new LinkedHashMap<String, Entity>());
        repo.put("teacherschoolassociation", new LinkedHashMap<String, Entity>());
        repo.put("staff", new LinkedHashMap<String, Entity>());

        //Create default roles.

    }
    
    protected Map<String, Map<String, Entity>> getRepo() {
        return repo;
    }
    
    protected void setRepo(Map<String, Map<String, Entity>> repo) {
        this.repo = repo;
    }
    
    @Override
    public Entity find(String entityType, String id) {
        return repo.get(entityType).get(id);
    }
    
    @Override
    public Iterable<Entity> findAll(String entityType, int skip, int max) {
        List<Entity> all = new ArrayList<Entity>(repo.get(entityType).values());
        return all.subList(skip, (Math.min(skip + max, all.size())));
    }
    
    @Override
    public void update(String type, Entity entity) {
        repo.get(type).put(entity.getEntityId(), entity);
    }
    
    @Override
    public Entity create(String type, Map<String, Object> body) {
        return create(type, body, type);
    }
    
    @Override
    public Entity create(String type, Map<String, Object> body, String collectionName) {
        Entity newEntity = new MongoEntity(type, Long.toString(nextID.getAndIncrement()), body, null);
        update(collectionName, newEntity);
        return newEntity;
    }
    
    @Override
    public void delete(String entityType, String id) {
        repo.get(entityType).remove(id);
    }
    
    @Override
    public Iterable<Entity> findByFields(String entityType, Map<String, String> fields, int skip, int max) {
        List<Entity> toReturn = new ArrayList<Entity>();
        
        if (repo.containsKey(entityType)) {
            List<Entity> all = new ArrayList<Entity>(repo.get(entityType).values());
            for (Entity entity : all) {
                if (matchesFields(entity, fields)) {
                    toReturn.add(entity);
                }
            }
        }
        return toReturn.subList(skip, (Math.min(skip + max, toReturn.size())));
    }
    
    private boolean matchesFields(Entity entity, Map<String, String> fields) {
        for (Map.Entry<String, String> field : fields.entrySet()) {
            Object value = entity.getBody().get(field.getKey());
            if (value == null || !value.toString().equals(field.getValue())) {
                return false;
            }
        }
        return true;
        
    }
    
    @Override
    public void deleteAll(String entityType) {
        Map<String, Entity> repository = repo.get(entityType);
        if (repository != null) {
            repository.clear();
        }
        
    }
    
    @Override
    public Iterable<Entity> findAll(String entityType) {
        List<Entity> all = new ArrayList<Entity>(repo.get(entityType).values());
        return all;
    }
    
    @Override
    public Iterable<Entity> findByFields(String entityType, Map<String, String> fields) {
        List<Entity> toReturn = new ArrayList<Entity>();
        
        if (repo.containsKey(entityType)) {
            List<Entity> all = new ArrayList<Entity>(repo.get(entityType).values());
            for (Entity entity : all) {
                if (matchesFields(entity, fields)) {
                    toReturn.add(entity);
                }
            }
        }
        return toReturn;
    }
    
    @Override
    public Iterable<Entity> findByFields(String entityType, String queryString, int skip, int max) {
        List<Entity> toReturn = new ArrayList<Entity>();
        Map<String[], String> queryMap = stringToQuery(queryString);
        if (repo.containsKey(entityType)) {
            List<Entity> all = new ArrayList<Entity>(repo.get(entityType).values());
            for (Entity entity : all) {
                try {
                    if (matchQueries(entity, queryMap)) {
                        toReturn.add(entity);
                    }
                } catch (Exception e) {
                }
            }
        }
        return toReturn.subList(skip, (Math.min(skip + max, toReturn.size())));
    }
    
    private Map<String[], String> stringToQuery(String queryString) {
        Map<String[], String> queryMap = new HashMap<String[], String>();
        if (queryString != null) {
            String[] queryStrings = queryString.split("&");
            for (String query : queryStrings) {
                if (!isReservedQueryKey(query)) {
                    if (query.contains(">=")) {
                        String[] keyAndValue = getKeyAndValue(query, ">=");
                        if (keyAndValue != null)
                            queryMap.put(keyAndValue, ">=");
                    } else if (query.contains("<=")) {
                        String[] keyAndValue = getKeyAndValue(query, "<=");
                        if (keyAndValue != null)
                            queryMap.put(keyAndValue, "<=");
                    } else if (query.contains("=")) {
                        String[] keyAndValue = getKeyAndValue(query, "=");
                        if (keyAndValue != null)
                            queryMap.put(keyAndValue, "=");
                        
                    } else if (query.contains("<")) {
                        String[] keyAndValue = getKeyAndValue(query, "<");
                        if (keyAndValue != null)
                            queryMap.put(keyAndValue, "<");
                        
                    } else if (query.contains(">")) {
                        String[] keyAndValue = getKeyAndValue(query, ">");
                        if (keyAndValue != null)
                            queryMap.put(keyAndValue, ">");
                    }
                }
            }
        }
        return queryMap;
    }
    
    private boolean isReservedQueryKey(String queryString) {
        boolean found = false;
        for (String key : reservedQueryKeys) {
            if (queryString.indexOf(key) >= 0)
                found = true;
        }
        return found;
    }
    
    private String[] getKeyAndValue(String queryString, String operator) {
        String[] keyAndValue = queryString.split(operator);
        if (keyAndValue.length != 2)
            return null;
        else
            return keyAndValue;
    }
    
    private boolean matchQueries(Entity entity, Map<String[], String> queryMap) throws Exception {
        boolean match = true;
        for (String[] keyAndValue : queryMap.keySet()) {
            String key = keyAndValue[0];
            String value = keyAndValue[1];
            String operator = queryMap.get(keyAndValue);
            if (operator.equals(">=")) {
                if ((!entity.getBody().containsKey(key)) || (!(compareToValue(entity.getBody().get(key), value) >= 0)))
                    match = false;
            } else if (operator.equals("<=")) {
                if ((!entity.getBody().containsKey(key)) || (!(compareToValue(entity.getBody().get(key), value) <= 0)))
                    match = false;
            } else if (operator.equals("=")) {
                if ((!entity.getBody().containsKey(key)) || (!(compareToValue(entity.getBody().get(key), value) == 0)))
                    match = false;
            } else if (operator.equals("<")) {
                if ((!entity.getBody().containsKey(key)) || (!(compareToValue(entity.getBody().get(key), value) < 0)))
                    match = false;
            } else if (operator.equals(">")) {
                if ((!entity.getBody().containsKey(key)) || (!(compareToValue(entity.getBody().get(key), value) > 0)))
                    match = false;
            }
        }
        return match;
    }
    
    private int compareToValue(Object entityValue, String value) throws Exception {
        int compare = 0;
        if (entityValue instanceof String) {
            compare = ((String) entityValue).compareTo(value);
        } else if (entityValue instanceof Integer)
            compare = (Integer) entityValue - Integer.parseInt(value);
        return compare;
    }

    @Override
    public boolean matchQuery(String entityType, String id, String queryString) {
        boolean match = false;
        List<Entity> toReturn = new ArrayList<Entity>();
        Map<String[], String> queryMap = stringToQuery(queryString);
        if (repo.containsKey(entityType)) {
            List<Entity> all = new ArrayList<Entity>(repo.get(entityType).values());
            for (Entity entity : all) {
                try {
                    if (matchQueries(entity, queryMap)) {
                        toReturn.add(entity);
                    }
                } catch (Exception e) {
                }
            }
        }
        for (Entity entity : toReturn) {
            if (entity.getEntityId().equals(id))
                match = true;
        }
        return match;
    }
}
