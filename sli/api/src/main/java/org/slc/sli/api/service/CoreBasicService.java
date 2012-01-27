package org.slc.sli.api.service;

import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.dal.repository.EntityRepository;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.MongoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Core operations of the Basic Service.
 */
@Scope("prototype")
@Component("coreBasicService")
public class CoreBasicService implements CoreEntityService {
    
    private String collectionName;
    
    @Autowired
    private EntityRepository repo;
    
    // TODO repalce old avro validator with neutral validator
    // @Autowired
    // private EntityValidator validator;
    
    public CoreBasicService(String collectionName) {
        this.collectionName = collectionName;
    }
    
    @Override
    public String create(EntityBody body, String type) {
        MongoEntity entity = MongoEntity.create(type, body);
        
        // TODO repalce old avro validator with neutral validator
        // validator.validate(entity);
        return repo.create(type, body, collectionName).getEntityId();
    }
    
    @Override
    public Entity get(String id) {
        Entity entity = repo.find(collectionName, id);
        return entity;
    }
    
    @Override
    public boolean update(Entity entity, EntityBody updates) {
        Entity repoEntity = repo.find(collectionName, entity.getEntityId());
        repoEntity.getBody().clear();
        repoEntity.getBody().putAll(updates);
        // TODO repalce old avro validator with neutral validator
        // validator.validate(repoEntity);
        return repo.update(collectionName, repoEntity);
    }
    
    @Override
    public boolean delete(String id) {
        return repo.delete(collectionName, id);
    }
}
