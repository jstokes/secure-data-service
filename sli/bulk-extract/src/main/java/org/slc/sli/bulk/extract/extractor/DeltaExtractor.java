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
package org.slc.sli.bulk.extract.extractor;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import org.slc.sli.bulk.extract.BulkExtractMongoDA;
import org.slc.sli.bulk.extract.context.resolver.TypeResolver;
import org.slc.sli.bulk.extract.context.resolver.impl.EducationOrganizationContextResolver;
import org.slc.sli.bulk.extract.delta.DeltaEntityIterator;
import org.slc.sli.bulk.extract.delta.DeltaEntityIterator.DeltaRecord;
import org.slc.sli.bulk.extract.delta.DeltaEntityIterator.Operation;
import org.slc.sli.bulk.extract.extractor.EntityExtractor.CollectionWrittenRecord;
import org.slc.sli.bulk.extract.files.EntityWriterManager;
import org.slc.sli.bulk.extract.files.ExtractFile;
import org.slc.sli.bulk.extract.files.metadata.ManifestFile;
import org.slc.sli.common.constants.EntityNames;
import org.slc.sli.common.util.tenantdb.TenantContext;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.MongoEntity;
import org.slc.sli.domain.Repository;

@Component
public class DeltaExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(DeltaExtractor.class);
    
    @Autowired
    DeltaEntityIterator deltaEntityIterator;

    @Autowired
    LocalEdOrgExtractor leaExtractor;
    
    @Autowired
    EntityExtractor entityExtractor;
    
    @Autowired
    BulkExtractMongoDA bulkExtractMongoDA;
    
    @Autowired
    EntityWriterManager entityWriteManager;
    
    @Autowired
    TypeResolver typeResolver;

    @Autowired
    EducationOrganizationContextResolver edorgContextResolver;
    
    @Autowired
    @Qualifier("validationRepo")
    Repository<Entity> repo;

    private Map<String, ExtractFile> appPerLeaExtractFiles = new HashMap<String, ExtractFile>();
    private Map<String, EntityExtractor.CollectionWrittenRecord> appPerLeaCollectionRecords = new HashMap<String, EntityExtractor.CollectionWrittenRecord>();

    public void execute(String tenant, DateTime deltaUptoTime, String baseDirectory) {
        TenantContext.setTenantId(tenant);
        Map<String, Set<String>> appsPerTopLEA = reverse(filter(leaExtractor.getBulkExtractLEAsPerApp()));
        deltaEntityIterator.init(tenant, deltaUptoTime);
        while (deltaEntityIterator.hasNext()) {
            DeltaRecord delta = deltaEntityIterator.next();
            if (delta.getOp() == Operation.UPDATE) {
                if (delta.isSpamDelete()) {
                    spamDeletes(delta, delta.getBelongsToLEA(), tenant, deltaUptoTime, appsPerTopLEA);
                }
                for (String lea : delta.getBelongsToLEA()) {
                    // we have apps for this lea
                    if (appsPerTopLEA.containsKey(lea)) {
                        for (String appId : appsPerTopLEA.get(lea)) {
                            ExtractFile extractFile = getExtractFile(appId, lea, tenant, deltaUptoTime);
                            EntityExtractor.CollectionWrittenRecord record = getCollectionRecord(appId, lea, delta.getType());
                            try {
                                entityExtractor.write(delta.getEntity(), extractFile, record);
                            } catch (IOException e) {
                                LOG.error("Error while extracting for " + lea + "with app " + appId, e);
                            }
                        }
                    }
                }
            } else if (delta.getOp() == Operation.DELETE) {
                spamDeletes(delta, Collections.<String> emptySet(), tenant, deltaUptoTime, appsPerTopLEA);
            }
        }
        
        finalizeExtraction(tenant, deltaUptoTime);
    }


    private void spamDeletes(DeltaRecord delta, Set<String> exceptions, String tenant, DateTime deltaUptoTime, Map<String, Set<String>> appsPerLEA) {
        for (Map.Entry<String, Set<String>> entry : appsPerLEA.entrySet()) {
            String lea = entry.getKey();
            Set<String> apps = entry.getValue();
            
            if (exceptions.contains(lea)) {
                continue;
            }
            
            for (String appId : apps) {
                ExtractFile extractFile = getExtractFile(appId, lea, tenant, deltaUptoTime);
                // for some entities we have to spam delete the same id in two collections
                // since we cannot reliably retrieve the "type". For example, teacher/staff
                // edorg/school, if the entity has been deleted, all we know if it a staff
                // or edorg, but it may be stored as teacher or school in vendor db, so
                // we must spam delete the id in both teacher/staff or edorg/school collection
                Entity entity = delta.getEntity();
                Set<String> types = typeResolver.resolveType(entity.getType());
                for (String type : types) {
                    Entity e = new MongoEntity(type, entity.getEntityId(), new HashMap<String, Object>(), null);
                    entityWriteManager.writeDelete(e, extractFile);
                }
            }
        }
    }

    private void finalizeExtraction(String tenant, DateTime startTime) {
        ManifestFile metaDataFile;
        for (Map.Entry<String, ExtractFile> entry : appPerLeaExtractFiles.entrySet()) {
            ExtractFile extractFile = entry.getValue();
            extractFile.closeWriters();
            
            try {
                metaDataFile = extractFile.getManifestFile();
                metaDataFile.generateMetaFile(startTime);
            } catch (IOException e) {
                LOG.error("Error creating metadata file: {}", e.getMessage());
            }
            
            try {
                extractFile.generateArchive();
            } catch (Exception e) {
                LOG.error("Error generating archive file: {}", e.getMessage());
            }
            
            for (Entry<String, File> archiveFile : extractFile.getArchiveFiles().entrySet()) {
                bulkExtractMongoDA.updateDBRecord(tenant, archiveFile.getValue().getAbsolutePath(),
                        archiveFile.getKey(), startTime.toDate(), true, extractFile.getEdorg());
            }
        }
    }

    private CollectionWrittenRecord getCollectionRecord(String appId, String lea, String type) {
        String key = appId + "_" + lea + "_" + type;
        if (!appPerLeaCollectionRecords.containsKey(key)) {
            EntityExtractor.CollectionWrittenRecord collectionRecord = new EntityExtractor.CollectionWrittenRecord(type);
            appPerLeaCollectionRecords.put(key, collectionRecord);
            return collectionRecord;
        }
        
        return appPerLeaCollectionRecords.get(key);
    }

    private ExtractFile getExtractFile(String appId, String lea, String tenant, DateTime deltaUptoTime) {
        String key = appId + "_" + lea;
        if (!appPerLeaExtractFiles.containsKey(key)) {
            ExtractFile appPerLeaExtractFile = leaExtractor.getExtractFilePerAppPerLEA(tenant, appId, lea, deltaUptoTime, true);
            appPerLeaExtractFiles.put(key, appPerLeaExtractFile);
        }
        
        return appPerLeaExtractFiles.get(key);
    }
    
    /* filter out all non top level LEAs */
    private Map<String, Set<String>> filter(Map<String, Set<String>> bulkExtractLEAsPerApp) {
        Map<String, Set<String>> result = new HashMap<String, Set<String>>();
        for (Map.Entry<String, Set<String>> entry : bulkExtractLEAsPerApp.entrySet()) {
            String app = entry.getKey();
            Set<String> topLEA = new HashSet<String>();
            for (String edorg : entry.getValue()) {
                Entity edorgEntity = repo.findById(EntityNames.EDUCATION_ORGANIZATION, edorg);
                topLEA.addAll(edorgContextResolver.findGoverningLEA(edorgEntity));
            }
            result.put(app, topLEA);
        }

        return result;
    }
    private Map<String, Set<String>> reverse(Map<String, Set<String>> leasPerApp) {
        Map<String, Set<String>> result = new HashMap<String, Set<String>>();
        for (Map.Entry<String, Set<String>> entry : leasPerApp.entrySet()) {
            for (String lea : entry.getValue()) {
                if (!result.containsKey(lea)) {
                    Set<String> apps = new HashSet<String>();
                    apps.add(entry.getKey());
                    result.put(lea, apps);
                }
                result.get(lea).add(entry.getKey());
            }
        }
        return result;
    }
    

}
