package org.slc.sli.dal.repository;

import java.util.Map;

import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import org.springframework.data.mongodb.core.index.IndexDefinition;
import org.springframework.data.mongodb.core.query.Query;

import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.Repository;

/**
 * @author ifaybyshev
 *
 */
//@Component
public class SimpleEntityRepository implements Repository<Entity> {

    @Override
    public Entity create(String type, Map<String, Object> body) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Entity create(String type, Map<String, Object> body, String collectionName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Entity create(String type, Map<String, Object> body, Map<String, Object> metaData, String collectionName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Entity findById(String collectionName, String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean exists(String collectionName, String id) {
        return true;
    }

    @Override
    public Entity findOne(String collectionName, NeutralQuery neutralQuery) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterable<Entity> findAll(String collectionName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterable<Entity> findAll(String collectionName, NeutralQuery neutralQuery) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterable<Entity> findAllByPaths(String collectionName, Map<String, String> paths, NeutralQuery neutralQuery) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterable<String> findAllIds(String collectionName, NeutralQuery neutralQuery) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long count(String collectionName, NeutralQuery neutralQuery) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean update(String collection, Entity object) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean delete(String collectionName, String id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void deleteAll(String collectionName) {
        // TODO Auto-generated method stub

    }

    @Override
    public CommandResult execute(DBObject command) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DBCollection getCollection(String collectionName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterable<Entity> findByPaths(String collectionName, Map<String, String> paths) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterable<Entity> findByQuery(String collectionName, Query query, int skip, int max) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean collectionExists(String collection) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void createCollection(String collection) {
        // TODO Auto-generated method stub

    }

    @Override
    public void ensureIndex(IndexDefinition index, String collection) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setWriteConcern(String writeConcern) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setReferenceCheck(String referenceCheck) {
        // TODO Auto-generated method stub

    }

    @Override
    public long count(String collectionName, Query query) {
        // TODO Auto-generated method stub
        return 0;
    }

}
