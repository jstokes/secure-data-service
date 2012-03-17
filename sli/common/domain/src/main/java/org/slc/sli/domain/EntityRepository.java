package org.slc.sli.domain;

import java.util.Map;


/**
 * Define the entity repository interface that provides basic CRUD and field
 * query methods for entities including core entities and association entities
 * 
 * @author Dong Liu dliu@wgen.net
 * 
 */
public interface EntityRepository {

    /**
     * Create an entry with the collection set to the type name
     * 
     * @param type
     *            the type of entity to be persisted
     * @param body
     *            the entity body that will be persisted
     * @return the entity that has been persisted
     */
    public Entity create(String type, Map<String, Object> body);
    
    /**
     * @param type
     *            the type of entity to be persisted
     * @param body
     *            the entity body that will be persisted
     * @param collectioName
     *            the name of the collection to save it into
     * @return the entity that has been persisted
     */
    public Entity create(String type, Map<String, Object> body, String collectionName);
    
    /**
     * @param type
     *            the type of entity to be persisted
     * @param body
     *            the entity body that will be persisted
     * @param metaData
     *            the entity meta data that will be persisted
     * @param collectionName
     *            the name of the collection to save it into
     * @return the entity that has been persisted
     */
    public Entity create(String type, Map<String, Object> body, Map<String, Object> metaData, String collectionName);
    
    /**
     * @param collectionName
     *            the name of the collection to look in
     * @param id
     *            the global unique id of the entity
     * @return the entity retrieved
     */
    public Entity find(String collectionName, String id);

    /**
     * @param collectionName
     *            the name of the collection to look in
     * @param id
     *            the global unique id of the entity
     * @param query
     *            all parameters to be included in query
     * @return the entity retrieved
     */
    public Entity find(String collectionName, NeutralQuery query);

    /**
     * @param collectioName
     *            the name of the collection to look in
     * @return the collection of entities
     */
    public Iterable<Entity> findAll(String collectioName);

    /**
     * @param collectionName
     *            the name of the collection to look in
     * @param query
     *            all parameters to be included in query
     * @return the entity retrieved
     */
    public Iterable<Entity> findAll(String collectionName, NeutralQuery query);

    
    /**
     * Fetches first element from given query
     * 
     * @param collectionName
     * @param query
     * @return
     */
    public Entity findOne(String collectionName, NeutralQuery query);
    
    /**
     * Get the number of elements in the collection matching a particular query
     * 
     * @param collectionName
     *            the name of the collection to look in
     * @param query
     *            the query to look for
     * @return the number of entities matching the query in the collection
     */
    public long count(String collectionName, NeutralQuery query);
    
    /**
     * @param collection
     *            the collection the entity is in
     * @param entity
     *            the entity that will be updated
     * @return whether or not the entity was updated
     */
    public boolean update(String collection, Entity entity);
    
    /**
     * @param collectionName
     *            the name of the collection to delete from
     * @param id
     *            the global unique id of the entity
     */
    public boolean delete(String collectionName, String id);
    
    /**
     * @param collectionName
     *            the name of the collection to delete from
     */
    public void deleteAll(String collectionName);
    
    /**
     * @param collectionName
     *            the name of the collection to look in
     * @param paths
     *            a map with key value pairs as string that define the search
     *            criteria for example: new HashMap().put("body.firstName","Jane"),
     *            or new HashMap().put("metadata.regionId","Region")
     * @param skip
     *            the beginning index of the entity that will be returned
     * @param max
     *            the max number of entities that will be returned
     * @return the collection of entities
     */
    public Iterable<Entity> findByPaths(String collectionName, Map<String, String> paths, NeutralQuery neutralQuery);
    
    /**
     * @param collectionName
     *            the name of the collection to look in
     * @param paths
     *            a map with key value pairs as string that define the search
     *            criteria for example: new HashMap().put("body.firstName","Jane"),
     *            or new HashMap().put("metadata.regionId","Region")
     * @return the collection of entities
     */
    public Iterable<Entity> findByPaths(String collectionName, Map<String, String> paths);
    
    /**
     * Filter a collection of IDs by
     * 
     * @param collectionName
     *            the name of the collection to look in
     * @param query
     *            the query used to find entities
     * @param skip
     *            start index of the entity that will be returned
     * @param max
     *            maximum number of results returned
     * @return
     */
    public Iterable<String> findAllIds(String collectionName, NeutralQuery query);
}
