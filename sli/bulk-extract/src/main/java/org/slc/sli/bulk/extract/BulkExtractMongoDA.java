/*
 * Copyright 2012-2013 inBloom, Inc. and its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.slc.sli.bulk.extract;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Query;

import org.slc.sli.common.util.tenantdb.TenantContext;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.Repository;

/**
 * Mongo access to bulk extract data.
 * @author tke
 *
 */
public class BulkExtractMongoDA {
    private static final Logger LOG = LoggerFactory.getLogger(BulkExtractMongoDA.class);

    /**
     * name of bulkExtract collection.
     */
    public static final String BULK_EXTRACT_COLLECTION = "bulkExtractFiles";
    private static final String FILES = "files";
    private static final String FILE_PATH = "path";
    private static final String DATE = "date";
    private static final String TENANT_ID = "tenantId";
    private static final String IS_DELTA = "isDelta";

    private static final String APP_AUTH_COLLECTION = "applicationAuthorization";
    private static final String APP_COLLECTION = "application";
    private static final String APP_ID = "applicationId";
    private static final String IS_BULKEXTRACT = "isBulkExtract";
    private static final String PUB_KEY = "public_key";

    private Repository<Entity> entityRepository;


    /** Insert a new record is the tenant doesn't exist. Update if existed
     * @param tenantId tenant id
     * @param path  path to the extracted file.
     * @param date  the date when the bulk extract was created
     */
    public void updateDBRecord(String tenantId, String path, Date date) {
        updateDBRecord(tenantId, path, date, false);
    }

    /** Insert a new record is the tenant doesn't exist. Update if existed
     * @param tenantId tenant id
     * @param path  path to the extracted file.
     * @param date  the date when the bulk extract was created
     * @param isDelta TODO
     */
    public void updateDBRecord(String tenantId, String path, Date date, boolean isDelta) {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put(TENANT_ID, tenantId);
        body.put(FILE_PATH, path);
        body.put(DATE, date);
        body.put(IS_DELTA, Boolean.toString(isDelta));

        BulkExtractEntity bulkExtractEntity = new BulkExtractEntity(body, tenantId);

        entityRepository.update(BULK_EXTRACT_COLLECTION, bulkExtractEntity, false);

        LOG.info("Finished creating bulk extract record");
    }

    /**
     * Get the public keys for all the bulk extract applications.
     * @return A map from clientId to public key
     */
    public Map<String, String> getAppPublicKeys() {
        Map<String, String> appKeys = new HashMap<String, String>();

        Iterator<Entity> cursor = entityRepository.findEach(APP_AUTH_COLLECTION, new Query());
        while(cursor.hasNext()){
            Entity app = cursor.next();
            String appId = (String) app.getBody().get(APP_ID);
            appKeys.putAll(getClientIdAndPublicKey(appId));
            LOG.info("app: {}, key: {}", appId, getClientIdAndPublicKey(appId).get(appId));
        }

        LOG.info("appKeys size is {}", appKeys.size());
        return appKeys;
    }

    @SuppressWarnings("boxing")
    private Map<String, String> getClientIdAndPublicKey(String appId) {
        Map<String, String> clientPubKeys = new HashMap<String, String>();

        TenantContext.setIsSystemCall(true);
        Entity app = this.entityRepository.findOne(APP_COLLECTION, new NeutralQuery(new NeutralCriteria(
                "_id", NeutralCriteria.OPERATOR_EQUAL, appId)));
        TenantContext.setIsSystemCall(false);

        if(app != null){
            Map<String, Object> body = app.getBody();
            if (body.containsKey(IS_BULKEXTRACT)) {
                if((Boolean) body.get(IS_BULKEXTRACT)) {
                    String key = (String)body.get(PUB_KEY);
                    if(key != null) {
                        clientPubKeys.put(appId, key);
                    } else {
                        LOG.info("App {} doesn't have public key", appId);
                    }
                }
            }
        }

        return clientPubKeys;
    }

    /**
     * get entity repository.
     * @return repository
     */
    public Repository<Entity> getEntityRepository() {
        return entityRepository;
    }

    /**
     * set entity repository.
     * @param entityRepository the entityRepository to set
     */
    public void setEntityRepository(Repository<Entity> entityRepository) {
        this.entityRepository = entityRepository;
    }

}
