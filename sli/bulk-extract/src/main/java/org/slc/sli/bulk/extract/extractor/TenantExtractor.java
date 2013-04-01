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

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.slc.sli.bulk.extract.BulkExtractMongoDA;
import org.slc.sli.bulk.extract.files.ExtractFile;
import org.slc.sli.bulk.extract.files.metadata.ManifestFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bulk extractor to extract data for a tenant.
 *
 * @author tke
 *
 */
public class TenantExtractor{

    private static final Logger LOG = LoggerFactory.getLogger(TenantExtractor.class);

    private BulkExtractMongoDA bulkExtractMongoDA;

    private EntityExtractor entityExtractor;

    private ManifestFile metaDataFile;

    /**
     * Extract all the entities from a tenant.
     * @param tenant
     *          TenantId
     * @param extractFile
     *          Extract archive file
     * @param startTime
     *          start time stamp
     */
    public void execute(String tenant, ExtractFile extractFile, Date startTime) {

        List<String> collections = entityExtractor.getCollectionNames(tenant);
        for (String collection : collections) {
            entityExtractor.extractEntities(tenant, extractFile, collection);
        }

        try {
            metaDataFile = extractFile.getManifestFile();
            metaDataFile.generateMetaFile(startTime);
        } catch (IOException e) {
            LOG.error("Error creating metadata file: {}", e.getMessage());
        }

        try {
            extractFile.generateArchive();
        } catch (IOException e) {
            LOG.error("Error generating archive file: {}", e.getMessage());
        }

        bulkExtractMongoDA.updateDBRecord(tenant, extractFile.getArchiveFile().getAbsolutePath(), startTime, false);
    }

    /**
     * get bulkExtractMongoDA.
     * @return bulkExtractMongoDA
     */
    public BulkExtractMongoDA getBulkExtractMongoDA() {
        return bulkExtractMongoDA;
    }

    /**
     * set bulkExtractMongoDA.
     * @param bulkExtractMongoDA bulk extractMongoDA
     */
    public void setBulkExtractMongoDA(BulkExtractMongoDA bulkExtractMongoDA) {
        this.bulkExtractMongoDA = bulkExtractMongoDA;
    }

    /**
     * get entity extractor.
     * @return entity extractor
     */
    public EntityExtractor getEntityExtractor() {
        return entityExtractor;
    }

    /**
     * set entity extractor.
     * @param entityExtractor entity extractor
     */
    public void setEntityExtractor(EntityExtractor entityExtractor) {
        this.entityExtractor = entityExtractor;
    }
}
