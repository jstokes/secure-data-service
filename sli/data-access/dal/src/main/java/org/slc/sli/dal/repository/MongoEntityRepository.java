package org.slc.sli.dal.repository;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

import org.slc.sli.dal.encrypt.EntityEncryption;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.EntityMetadataKey;
import org.slc.sli.domain.MongoEntity;
import org.slc.sli.util.datetime.DateTimeUtil;
import org.slc.sli.validation.EntityValidator;

/**
 * mongodb implementation of the entity repository interface that provides basic
 * CRUD and field query methods for entities including core entities and
 * association entities
 * @author Dong Liu dliu@wgen.net
 */

public class MongoEntityRepository extends MongoRepository<Entity> {
    protected static final Logger LOG = LoggerFactory.getLogger(MongoEntityRepository.class);

    @Autowired
    private EntityValidator validator;

    @Autowired(required = false)
    @Qualifier("entityEncryption")
    EntityEncryption encrypt;


    @Override
    protected String getRecordId(Entity entity) {
        return entity.getEntityId();
    }

    @Override
    protected Class<Entity> getRecordClass() {
        return Entity.class;
    }


    @Override
    public Entity create(String type, Map<String, Object> body, Map<String, Object> metaData, String collectionName) {
        Assert.notNull(body, "The given entity must not be null!");
        Entity entity = new MongoEntity(type, null, body, metaData);
        validator.validate(entity);
        this.addTimestamps(entity);

        return super.create(entity, collectionName);
    }

    @Override
    public boolean update(String collection, Entity entity) {
        validator.validate(entity);
        this.updateTimestamp(entity);
        Map<String, Object> body = entity.getBody();
        if (encrypt != null) {
            body = encrypt.encrypt(entity.getType(), entity.getBody());
        }
        return super.update(collection, entity, body);
    }

    /** Add the created and updated timestamp to the document metadata. */
    private void addTimestamps(Entity entity) {
        // String now = DateTimeUtil.getNowInUTC();
        Date now = DateTimeUtil.getNowInUTC();

        Map<String, Object> metaData = entity.getMetaData();
        metaData.put(EntityMetadataKey.CREATED.getKey(), now);
        metaData.put(EntityMetadataKey.UPDATED.getKey(), now);
    }

    /** Update the updated timestamp on the document metadata. */
    public void updateTimestamp(Entity entity) {
        Date now = DateTimeUtil.getNowInUTC();
        entity.getMetaData().put(EntityMetadataKey.UPDATED.getKey(), now);
    }

}
