package org.slc.sli.ingestion.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang3.text.WordUtils;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.EntityMetadataKey;
import org.slc.sli.domain.Repository;
import org.slc.sli.ingestion.validation.ErrorReport;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * 
 * @author ablum
 * 
 */
public class IdNormalizer {
    
    private static final String METADATA_BLOCK = "metaData";
    
    /**
     * Resolve references defined by external IDs (from clients) with internal IDs from SLI data
     * store. Uses a multiple search criteria to resolve the reference
     * 
     * @param collection
     *            Referenced collection
     * @param tenantId
     *            ID namespace that uniquely identifies external ID
     * @param externalSearchCriteria
     *            Search criteria that is used to resolve an externalId
     * @param errorReport
     *            Error reporting
     * @return The resolved internalId
     */
    public static String resolveInternalId(Repository<Entity> entityRepository, String collection, String tenantId,
            Map<?, ?> externalSearchCriteria, ErrorReport errorReport) {
        Map<String, String> filterFields = new HashMap<String, String>();
        filterFields.put(METADATA_BLOCK + "." + EntityMetadataKey.TENANT_ID.getKey(), tenantId);
        
        Query query = new Query();
        resolveSearchCriteria(entityRepository, collection, filterFields, externalSearchCriteria, query, tenantId,
                errorReport);
        
        Iterable<Entity> found = entityRepository.findByQuery(collection, query, 0, 1);
        
        if (found == null || !found.iterator().hasNext()) {
            errorReport.error("Cannot find [" + collection + "] record using the following filter: "
                    + query.getQueryObject().toString(), IdNormalizer.class);
            
            return null;
        }
        
        Entity entity = found.iterator().next();
        
        return entity.getEntityId();
    }
    
    /**
     * Resolve references defined by external IDs (from clients) with internal IDs from SLI data
     * store.
     * 
     * @param collection
     *            Referenced collection
     * @param tenantId
     *            ID namespace that uniquely identifies external ID
     * @param externalId
     *            External ID to be resolved
     * @param errorReport
     *            Error reporting
     * @return Resolved internal ID
     */
    public static String resolveInternalId(Repository<Entity> entityRepository, String collection, String tenantId,
            String externalId, ErrorReport errorReport) {
        Map<String, String> filterFields = new HashMap<String, String>();
        
        filterFields.put(METADATA_BLOCK + "." + EntityMetadataKey.TENANT_ID.getKey(), tenantId);
        filterFields.put(METADATA_BLOCK + "." + EntityMetadataKey.EXTERNAL_ID.getKey(), externalId);
        
        Iterable<Entity> found = entityRepository.findByPaths(collection, filterFields);
        if (found == null || !found.iterator().hasNext()) {
            errorReport.error(
                    "Cannot find [" + collection + "] record using the following filter: " + filterFields.toString(),
                    IdNormalizer.class);
            
            return null;
        }
        
        return found.iterator().next().getEntityId();
    }
    
    /**
     * Adds the criteria that searches within the same collection to the query
     * 
     * @param collection
     * @param filterFields
     * @param externalSearchCriteria
     * @param query
     */
    private static void resolveSearchCriteria(Repository<Entity> entityRepository, String collection,
            Map<String, String> filterFields, Map<?, ?> externalSearchCriteria, Query query, String tenantId,
            ErrorReport errorReport) {
        for (Map.Entry<?, ?> searchCriteriaEntry : externalSearchCriteria.entrySet()) {
            
            StringTokenizer tokenizer = new StringTokenizer(searchCriteriaEntry.getKey().toString(), "#");
            String pathCollection = tokenizer.nextToken();
            pathCollection = WordUtils.uncapitalize(pathCollection);
            
            if (pathCollection.equals(collection) && searchCriteriaEntry.getValue() != null) {
                
                resolveSameCollectionCriteria(filterFields, searchCriteriaEntry.getKey().toString(),
                        searchCriteriaEntry.getValue());
                addSearchPathsToQuery(query, filterFields);
                
            } else {
                
                resolveDifferentCollectionCriteria(entityRepository, query, searchCriteriaEntry, tenantId, errorReport);
                
            }
        }
        
    }
    
    /**
     * Recursively traverses a complex reference and adds all mongo path and value pairs to the
     * query filter
     * 
     * @param filterFields
     *            Mongo path and value pairs that are filtered in the query
     * @param key
     * @param value
     */
    private static void resolveSameCollectionCriteria(Map<String, String> filterFields, String key, Object value) {
        if (String.class.isInstance(value)) {
            
            StringTokenizer tokenizer = new StringTokenizer(key, "#");
            tokenizer.nextToken();
            String newPath = tokenizer.nextToken();
            filterFields.put(newPath, value.toString());
            
        } else if (Map.class.isInstance(value)) {
            
            for (Map.Entry<?, ?> searchCriteriaEntry : ((Map<?, ?>) value).entrySet()) {
                
                resolveSameCollectionCriteria(filterFields, searchCriteriaEntry.getKey().toString(),
                        searchCriteriaEntry.getValue());
                
            }
            
        } else if (List.class.isInstance(value)) {
            
            for (Object object : (List<?>) value) {
                
                resolveSameCollectionCriteria(filterFields, key, object);
                
            }
            
        }
        
    }
    
    /**
     * Adds the criteria that searches within a different collection to the query
     * 
     * @param query
     * @param externalSearchCriteria
     * @param errorReport
     */
    private static void resolveDifferentCollectionCriteria(Repository<Entity> entityRepository, Query query,
            Map.Entry<?, ?> searchCriteriaEntry, String tenantId, ErrorReport errorReport) {
        StringTokenizer tokenizer = new StringTokenizer(searchCriteriaEntry.getKey().toString(), "#");
        String pathCollection = tokenizer.nextToken();
        pathCollection = WordUtils.uncapitalize(pathCollection);
        String referencePath = tokenizer.nextToken();
        
        Map<String, String> tempFilter = new HashMap<String, String>();
        Query referenceQuery = new Query();
        if (searchCriteriaEntry.getValue() instanceof String) {
            Map<String, String> searchCriteriaEntryMap = new HashMap<String, String>();
            searchCriteriaEntryMap.put((String) searchCriteriaEntry.getKey(), (String) searchCriteriaEntry.getValue());
            resolveSearchCriteria(entityRepository, pathCollection, tempFilter, searchCriteriaEntryMap,
                    referenceQuery, tenantId, errorReport);
        } else {
            resolveSearchCriteria(entityRepository, pathCollection, tempFilter, (Map<?, ?>) searchCriteriaEntry.getValue(),
                    referenceQuery, tenantId, errorReport);
        }
        
        if (tempFilter.isEmpty()) {
            
            return;
        }
        tempFilter.put(METADATA_BLOCK + "." + EntityMetadataKey.TENANT_ID.getKey(), tenantId);
        Iterable<Entity> referenceFound = entityRepository.findByPaths(pathCollection, tempFilter);
        
        if (referenceFound == null || !referenceFound.iterator().hasNext()) {
            errorReport.error(
                    "Cannot find [" + pathCollection + "] record using the following filter: " + tempFilter.toString(),
                    IdNormalizer.class);
        } else {
            
            Map<String, String> orFilter = new HashMap<String, String>();
            
            for (Entity found : referenceFound) {
                
                orFilter.put(referencePath, found.getEntityId());
                
            }
            
            addOrToQuery(query, orFilter);
        }
        
    }
    
    private static Query addSearchPathsToQuery(Query query, Map<String, String> searchPaths) {
        for (Map.Entry<String, String> field : searchPaths.entrySet()) {
            Criteria criteria = Criteria.where(field.getKey()).is(field.getValue());
            query.addCriteria(criteria);
        }
        
        return query;
    }
    
    private static void addOrToQuery(Query query, Map<String, String> orFilter) {
        List<Query> queries = new ArrayList<Query>();
        for (Map.Entry<String, String> field : orFilter.entrySet()) {
            queries.add(new Query(Criteria.where(field.getKey()).is(field.getValue())));
        }
        query.or(queries.toArray(new Query[0]));
    }
}
