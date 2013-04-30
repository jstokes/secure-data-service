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
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.slc.sli.bulk.extract.BulkExtractMongoDA;
import org.slc.sli.common.constants.EntityNames;
import org.slc.sli.common.constants.ParameterConstants;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.Repository;

/**
 * Extract the Public Data for the State Education Agency.
 * ablum
 */
public class StatePublicDataExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(TenantExtractor.class);

    @Autowired
    private BulkExtractMongoDA bulkExtractMongoDA;

    @Autowired
    @Qualifier("secondaryRepo")
    private Repository<Entity> entityRepository;

    private static final String STATE_EDUCATION_AGENCY = "State Education Agency";

    /**
     * Creates unencrypted SEA public data bulk extract files if any are needed for the given tenant.
     *
     * @param tenant name of tenant to extract
     * @param tenantDirectory for the extract
     * @param startTime of the extract
     */
    public void execute(String tenant, File tenantDirectory, DateTime startTime) {
        String seaId = retrieveSEAId();

        if(seaId == null) {
            LOG.error("Unable to trigger extract for the tenant");
            return;
        }

        Map<String, PublicKey> clientKeys = bulkExtractMongoDA.getAppPublicKeys();
        if(clientKeys == null || clientKeys.isEmpty()) {
            LOG.info("No authorized application to extract data.");
            return;
        }

        extractPublicData(seaId, clientKeys);
    }

    /**
     *
     * @param seaId the ID of the SEA to extract
     */
    private boolean extractPublicData(String seaId, Map<String, PublicKey> clientKeys) {
        return false;
    }

    private String retrieveSEAId() {
        String seaId = null;
        NeutralQuery query = new NeutralQuery(new NeutralCriteria(ParameterConstants.ORGANIZATION_CATEGORIES,
                NeutralCriteria.CRITERIA_IN, Arrays.asList(STATE_EDUCATION_AGENCY)));
        final Iterable<Entity> entities = entityRepository.findAll(EntityNames.EDUCATION_ORGANIZATION, query);

        if (entities != null) {
            for(Entity seaEntity : entities) {
                if (seaId != null) {
                    LOG.error("More than one SEA is found for the tenant");
                    return null;
                }
                seaId = seaEntity.getEntityId();
            }
        } else {
            LOG.error("No SEA is available for the tenant");
        }

        return seaId;
    }
}
