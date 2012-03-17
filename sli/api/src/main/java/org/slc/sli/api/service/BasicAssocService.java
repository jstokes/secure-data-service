package org.slc.sli.api.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.EntityDefinition;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
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
    
    private final EntityDefinition sourceDefn;
    private final EntityDefinition targetDefn;
    private final String sourceKey;
    private final String targetKey;
    
    public BasicAssocService(final String collectionName, final List<Treatment> treatments, final EntityDefinition sourceDefn,
            final String sourceKey, final EntityDefinition targetDefn, final String targetKey) {
        super(collectionName, treatments);
        this.sourceDefn = sourceDefn;
        this.targetDefn = targetDefn;
        this.sourceKey = sourceKey;
        this.targetKey = targetKey;
    }
    
    @Override
    public Iterable<String> getAssociationsFor(final String id, final NeutralQuery neutralQuery) {
        List<String> results = new ArrayList<String>();
        results.addAll(getAssociationsList(sourceDefn, id, sourceKey, neutralQuery));
        results.addAll(getAssociationsList(targetDefn, id, targetKey, neutralQuery));
        return results;
    }
    
    @Override
    public Iterable<String> getAssociationsWith(final String id, final NeutralQuery neutralQuery) {
        return getAssociations(sourceDefn, id, sourceKey, neutralQuery);
    }
    
    @Override
    public Iterable<String> getAssociationsTo(final String id, final NeutralQuery neutralQuery) {
        return getAssociations(targetDefn, id, targetKey, neutralQuery);
    }
    
    @Override
    public EntityIdList getAssociatedEntitiesWith(final String id, final NeutralQuery neutralQuery) {
        return getAssociatedEntities(sourceDefn, id, sourceKey, targetKey, neutralQuery);
    }
    
    @Override
    public EntityIdList getAssociatedEntitiesTo(final String id, final NeutralQuery neutralQuery) {
        return getAssociatedEntities(targetDefn, id, targetKey, sourceKey, neutralQuery);
    }
    
    @Override
    public String create(final EntityBody content) {
        
        //validateAssociationContent(content);
        return super.create(content);
    }
    
    @Override
    public long countAssociationsWith(final String id, NeutralQuery neutralQuery) {
        return countAssociationsTo(id, neutralQuery) + countAssociationsFor(id, neutralQuery);
    }
    
    @Override
    public long countAssociationsTo(final String id, NeutralQuery neutralQuery) {
        return getAssociationCount(targetDefn, id, targetKey, neutralQuery);
    }
    
    @Override
    public long countAssociationsFor(final String id, NeutralQuery neutralQuery) {
        return getAssociationCount(sourceDefn, id, sourceKey, neutralQuery);
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
    private Iterable<String> getAssociations(final EntityDefinition type, final String id, final String key, final NeutralQuery neutralQuery) {
        //LOG.debug("Getting assocations with {} from {} through {}", new Object[] { id, start, numResults });
        return getAssociationsList(type, id, key, neutralQuery);
    }
    
    private List<String> getAssociationsList(final EntityDefinition type, final String id, final String key, final NeutralQuery neutralQuery) {
        List<String> results = new ArrayList<String>();
        Iterable<Entity> entityObjects = getAssociationObjects(type, id, key, neutralQuery);
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
    private EntityIdList getAssociatedEntities(final EntityDefinition type, final String id, final String key, final String otherEntityKey,
            final NeutralQuery neutralQuery) {
        //LOG.debug("Getting assocated entities with {} from {} through {}", new Object[] { id, start, numResults });
        EntityDefinition otherEntityDefn = type == sourceDefn ? targetDefn : sourceDefn;
        
        Iterable<Entity> entityObjects = getAssociationObjects(type, id, key, new NeutralQuery());
        // there can be multiple association objects pointing to the same associated entity, and we
        // need the number of unique ones for the totalCount
        Set<String> associatedEntityIdSet = new HashSet<String>();
        List<String> ids = new ArrayList<String>();
        for (Entity entity : entityObjects) {
            Object other = entity.getBody().get(otherEntityKey);
            if (other != null && other instanceof String) {
                if (associatedEntityIdSet.add((String) other)) {
                    ids.add((String) other);
                }
            } else {
                LOG.error("Association had bad value of key {}: {}", new Object[] { otherEntityKey, other });
            }
        }
        
        NeutralQuery localNeutralQuery = new NeutralQuery(neutralQuery);
        localNeutralQuery.addCriteria(new NeutralCriteria("_id", "in", ids));
        
        final Iterable<String> results = getRepo().findAllIds(otherEntityDefn.getStoredCollectionName(), localNeutralQuery);
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
    private Iterable<Entity> getAssociationObjects(final EntityDefinition type, final String id, final String key, final NeutralQuery neutralQuery) {
        EntityBody existingEntity = type.getService().get(id);
        if (existingEntity == null) {
            throw new EntityNotFoundException(id);
        }
        
        NeutralQuery localNeutralQuery = new NeutralQuery(neutralQuery);
        localNeutralQuery.addCriteria(new NeutralCriteria(key, "=", id));
        
        return getRepo().findAll(getCollectionName(), localNeutralQuery);
    }
    
    private long getAssociationCount(final EntityDefinition type, final String id, final String key, final NeutralQuery neutralQuery) {
        NeutralQuery localNeutralQuery = new NeutralQuery(neutralQuery);
        localNeutralQuery.addCriteria(new NeutralCriteria(key, "=", id));
        
        return getRepo().count(getCollectionName(), localNeutralQuery);
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
        boolean parentChild = sourceDefn == targetDefn;
        
        // check for source and target entities in data store, log errors to list if/when
        // appropriate
        checkEntityExists(sourceDefn, content, parentChild ? ParentChild.PARENT : ParentChild.NONE,
                errorList);
        checkEntityExists(targetDefn, content, parentChild ? ParentChild.CHILD : ParentChild.NONE,
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
        if (!checkEntityExists(entityDefinition.getStoredCollectionName(), id)) {
            // log error of missing data when using specified field name containing specified value
            errorList.add(new ValidationError(ValidationError.ErrorType.REFERENTIAL_INFO_MISSING, fieldName, id, null));
        }
    }
    
    private boolean checkEntityExists(final String collectionName, final String id) {
        try {
            Entity entity = getRepo().find(collectionName, id);
            if (entity == null) {
                return false;
            }
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
    private static String getFieldName(final String entityType, final ParentChild relationship) {
        
        // if no parent child relationship applies
        if (relationship == ParentChild.NONE) {
            return entityType + "Id";
        }
        
        // field names for edorg-edorg associations say edorg, then parent/child then ID
        if (entityType.equals("educationOrganization")) {
            return entityType + (relationship == ParentChild.PARENT ? "Parent" : "Child") + "Id";
        }
        
        // insert any other like-like associations' specific field name(s) logic here:
        
        // default
        return entityType + (relationship == ParentChild.PARENT ? "Parent" : "Child") + "Id";
    }
}
