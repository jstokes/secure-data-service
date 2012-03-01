package org.slc.sli.api.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.EntityDefinition;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.service.query.QueryConverter;
import org.slc.sli.api.service.query.SortOrder;
import org.slc.sli.domain.Entity;
import org.slc.sli.validation.EntityValidationException;
import org.slc.sli.validation.ValidationError;

/**
 * Implementation of AssociationService.
 * 
 */
@Scope("prototype")
@Component("basicAssociationService")
public class BasicAssocService extends BasicService implements AssociationService {
    private static final Logger LOG = LoggerFactory.getLogger(BasicAssocService.class);
    
    private enum ParentChild {
        PARENT, CHILD, NONE
    }
    
    @Autowired
    QueryConverter queryConverter;
    
    private EntityDefinition sourceDefn;
    private EntityDefinition targetDefn;
    private String sourceKey;
    private String targetKey;
    
    public BasicAssocService(String collectionName, List<Treatment> treatments, EntityDefinition sourceDefn,
            String sourceKey, EntityDefinition targetDefn, String targetKey) {
        super(collectionName, treatments);
        this.sourceDefn = sourceDefn;
        this.targetDefn = targetDefn;
        this.sourceKey = sourceKey;
        this.targetKey = targetKey;
    }
    
    @Override
    public Iterable<String> getAssociationsFor(String id, int start, int numResults, String queryString, String sortBy,
            SortOrder sortOrder) {
        List<String> results = new ArrayList<String>();
        results.addAll(this.getAssociationsList(sourceDefn, id, sourceKey, start, numResults, queryString, sortBy,
                sortOrder));
        results.addAll(this.getAssociationsList(targetDefn, id, targetKey, start, numResults, queryString, sortBy,
                sortOrder));
        return results;
    }
    
    @Override
    public Iterable<String> getAssociationsWith(String id, int start, int numResults, String queryString,
            String sortBy, SortOrder sortOrder) {
        return getAssociations(sourceDefn, id, sourceKey, start, numResults, queryString, sortBy, sortOrder);
    }
    
    @Override
    public Iterable<String> getAssociationsTo(String id, int start, int numResults, String queryString, String sortBy,
            SortOrder sortOrder) {
        return getAssociations(targetDefn, id, targetKey, start, numResults, queryString, sortBy, sortOrder);
    }
    
    @Override
    public EntityIdList getAssociatedEntitiesWith(String id, int start, int numResults, String queryString,
            String sortBy, SortOrder sortOrder) {
        return getAssociatedEntities(sourceDefn, id, sourceKey, targetKey, start, numResults, queryString, sortBy,
                sortOrder);
    }
    
    @Override
    public EntityIdList getAssociatedEntitiesTo(String id, int start, int numResults, String queryString,
            String sortBy, SortOrder sortOrder) {
        return getAssociatedEntities(targetDefn, id, targetKey, sourceKey, start, numResults, queryString, sortBy,
                sortOrder);
    }
    
    public String create(EntityBody content) {
        
        //validateAssociationContent(content);
        return super.create(content);
    }
    
    @Override
    public long countAssociationsWith(String id, String queryString) {
        return countAssociationsTo(id, queryString) + countAssociationsFor(id, queryString);
    }
    
    @Override
    public long countAssociationsTo(String id, String queryString) {
        return getAssociationCount(targetDefn, id, targetKey, queryString);
    }
    
    @Override
    public long countAssociationsFor(String id, String queryString) {
        return getAssociationCount(sourceDefn, id, sourceKey, queryString);
    }
    
    /**
     * Get associations to the entity of the given type and id, where id is keyed off of key
     * 
     * @param type
     *            the type of the entity being queried
     * @param id
     *            the id of the entity being queried
     * @param key
     *            the key the id maps to
     * @param start
     *            the number of entities in the list to skip
     * @param numResults
     *            the number of entities to return
     * @param queryString
     *            the query string to filter returned collection results
     * @return
     */
    private Iterable<String> getAssociations(EntityDefinition type, String id, String key, int start, int numResults,
            String queryString, String sortBy, SortOrder sortOrder) {
        LOG.debug("Getting assocations with {} from {} through {}", new Object[] { id, start, numResults });
        return this.getAssociationsList(type, id, key, start, numResults, queryString, sortBy, sortOrder);
    }
    
    private List<String> getAssociationsList(EntityDefinition type, String id, String key, int start, int numResults,
            String queryString, String sortBy, SortOrder sortOrder) {
        List<String> results = new ArrayList<String>();
        Iterable<Entity> entityObjects = getAssociationObjects(type, id, key, start, numResults, queryString, sortBy,
                sortOrder);
        for (Entity entity : entityObjects) {
            results.add(entity.getEntityId());
        }
        return results;
    }
    
    /**
     * Get associations to the entity of the given type and id, where id is keyed off of key
     * 
     * @param type
     *            the type of the entity being queried
     * @param id
     *            the id of the entity being queried
     * @param key
     *            the key the id maps to
     * @param start
     *            the number of entities in the list to skip
     * @param numResults
     *            the number of entities to return
     * @param queryString
     *            the query string to filter returned collection results
     * @return
     */
    private EntityIdList getAssociatedEntities(EntityDefinition type, String id, String key, String otherEntityKey,
            int start, int numResults, String queryString, String sortBy, SortOrder sortOrder) {
        LOG.debug("Getting assocated entities with {} from {} through {}", new Object[] { id, start, numResults });
        EntityDefinition otherEntityDefn = type == sourceDefn ? targetDefn : sourceDefn;
        
        Iterable<Entity> entityObjects = getAssociationObjects(type, id, key, 0, Integer.MAX_VALUE, null, null, null);
        // there can be multiple association objects pointing to the same associated entity, and we
        // need the number of unique ones for the totalCount
        Set<String> associatedEntityIdSet = new HashSet<String>();
        List<UUID> ids = new ArrayList<UUID>();
        for (Entity entity : entityObjects) {
            Object other = entity.getBody().get(otherEntityKey);
            if (other != null && other instanceof String) {
                if (associatedEntityIdSet.add((String) other)) {
                    ids.add(UUID.fromString((String) other));
                }
            } else {
                LOG.error("Association had bad value of key {}: {}", new Object[] { otherEntityKey, other });
            }
        }
        Query query = queryConverter.stringToQuery(otherEntityDefn.getType(), queryString, sortBy, sortOrder);
        query.addCriteria(Criteria.where("_id").in(ids));
        final Iterable<String> results = getRepo().findIdsByQuery(otherEntityDefn.getStoredCollectionName(), query,
                start, numResults);
        final long totalCount = associatedEntityIdSet.size();
        return new EntityIdList() {
            @Override
            public Iterator<String> iterator() {
                return results.iterator();
            }
            
            @Override
            public long getTotalCount() {
                return totalCount;
            }
        };
    }
    
    /**
     * Gets the actual association objects (and not just the ids
     * 
     * @param type
     *            the type of the entity being queried
     * @param id
     *            the id of the entity being queried
     * @param key
     *            the key the id maps to
     * @param start
     *            the number of entities in the list to skip
     * @param numResults
     *            the number of entities to return
     * @param queryString
     *            the query string to filter returned collection results
     * @return
     */
    private Iterable<Entity> getAssociationObjects(EntityDefinition type, String id, String key, int start,
            int numResults, String queryString, String sortBy, SortOrder sortOrder) {
        EntityBody existingEntity = type.getService().get(id);
        if (existingEntity == null) {
            throw new EntityNotFoundException(id);
        }
        Query query = queryConverter
                .stringToQuery(this.getEntityDefinition().getType(), queryString, sortBy, sortOrder);
        if (query == null)
            query = new Query(Criteria.where("body." + key).is(id));
        else
            query.addCriteria(Criteria.where("body." + key).is(id));
        
        Iterable<Entity> entityObjects = getRepo().findByQuery(getCollectionName(), query, start, numResults);
        return entityObjects;
    }
    
    private long getAssociationCount(EntityDefinition type, String id, String key, String queryString) {
        Query query = queryConverter.stringToQuery(this.getEntityDefinition().getType(), queryString, null, null);
        if (query == null)
            query = new Query(Criteria.where("body." + key).is(id));
        else
            query.addCriteria(Criteria.where("body." + key).is(id));
        return getRepo().count(getCollectionName(), query);
    }
    
    /**
     * Validates that the two entities' types that make up this association are defined in the
     * content
     * under appropriate ID fields.
     * 
     * Deprecated because all validation is done by neutral schemas and not in this layer.
     * 
     * @param content
     *            key/value association data
     * @return true if all tests pass
     * @throws EntityValidationException
     *             if referential information is not valid
     */
    @Deprecated
    private boolean validateAssociationContent(EntityBody content) throws EntityValidationException {
        
        // new list to hold validation errors
        List<ValidationError> errorList = new ArrayList<ValidationError>();
        
        // determine if this is a parent child association
        boolean parentChild = (this.sourceDefn == this.targetDefn);
        
        // check for source and target entities in data store, log errors to list if/when
        // appropriate
        this.checkEntityExists(this.sourceDefn, content, (parentChild ? ParentChild.PARENT : ParentChild.NONE),
                errorList);
        this.checkEntityExists(this.targetDefn, content, (parentChild ? ParentChild.CHILD : ParentChild.NONE),
                errorList);
        
        // if error list is not empty
        if (!errorList.isEmpty()) {
            // throw an exception detailing errors instead of completing method
            throw new EntityValidationException("", super.getEntityDefinition().getType(), errorList);
        }
        
        // all validations passed
        return true;
    }
    
    /**
     * Determines what key (field name) in content stores the desired ID value, uses that key to get
     * the ID value,
     * and then checks that the ID exists in the mongo collection for that entity type.
     * 
     * THis method is deprecated because reference integrity is not checked by association functionality
     * but instead by direct reference functionality. Associations just have direct references.
     * 
     * @param entityDefinition
     *            type of entity. Used to determine which collection in mongo to query
     * @param content
     *            values of the association. Used to get ID values to validate their existence
     * @param parentChild
     *            a string to insert into the field name in case both ends of association are same
     *            entity type
     * @param errorList
     *            list of errors to write to if a problem is discovered
     */
    @Deprecated
    private void checkEntityExists(EntityDefinition entityDefinition, EntityBody content, ParentChild relationship,
            List<ValidationError> errorList) {
        
        // determine what key should store the desired value
        String fieldName = BasicAssocService.getFieldName(entityDefinition.getType(), relationship);
        
        // retrieve the appropriate ID from the field
        String id = (String) content.get(fieldName);
        
        // if checking the mongo collection for the existence of the specified ID was not successful
        if (!checkEntityExistsInMongo(entityDefinition.getStoredCollectionName(), id)) {
            // log error of missing data when using specified field name containing specified value
            errorList.add(new ValidationError(ValidationError.ErrorType.REFERENTIAL_INFO_MISSING, fieldName, id, null));
        }
    }
    
    private boolean checkEntityExistsInMongo(String collectionName, String id) {
        try {
            Entity entity = getRepo().find(collectionName, id);
            if (entity == null)
                return false;
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }
    
    /**
     * Returns the appropriate field name for a given entity type. Handles scenarios where
     * associations are
     * between different entity types and when entity types are same.
     * 
     * @param entityType
     *            text describing field name
     * @param parentChild
     *            Parent, child
     * @return entity type with "Id" or other qualifiers added
     */
    private static String getFieldName(String entityType, ParentChild relationship) {
        
        // if no parent child relationship applies
        if (relationship == ParentChild.NONE) {
            return entityType + "Id";
        }
        
        // field names for edorg-edorg associations say edorg, then parent/child then ID
        if (entityType.equals("educationOrganization")) {
            return entityType + ((relationship == ParentChild.PARENT) ? "Parent" : "Child") + "Id";
        }
        
        // insert any other like-like associations' specific field name(s) logic here:
        
        // default
        return entityType + ((relationship == ParentChild.PARENT) ? "Parent" : "Child") + "Id";
    }
}
