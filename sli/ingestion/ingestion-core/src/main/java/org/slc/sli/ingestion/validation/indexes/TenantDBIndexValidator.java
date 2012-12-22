/*
* Copyright 2012 Shared Learning Collaborative, LLC
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
package org.slc.sli.ingestion.validation.indexes;

import java.util.List;
import java.util.Set;

import com.mongodb.DB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.slc.sli.ingestion.reporting.AbstractMessageReport;
import org.slc.sli.ingestion.reporting.AbstractReportStats;
import org.slc.sli.ingestion.tenant.TenantDA;
import org.slc.sli.ingestion.util.IndexParser;
import org.slc.sli.ingestion.util.MongoIndex;


/**
 * @author tke
 *
 */
public class TenantDBIndexValidator extends DbIndexValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(TenantDBIndexValidator.class);

    private String indexFile;

    private IndexParser<String> indexTxtFileParser;

    private TenantDA tenantDA;

    @Override
    protected Set<MongoIndex> loadExpectedIndexes() {
        return indexTxtFileParser.parse(indexFile);
    }

    @Override
    public boolean isValid(DB db, AbstractMessageReport report, AbstractReportStats reportStats) {
        List<String> tenantDbs = tenantDA.getAllTenantDbs();

        boolean isValid = true;
        Set<MongoIndex> expectedIndexes = loadExpectedIndexes();

        LOGGER.info("Validating indexes for tenant databases..");
        for (String tenantDb : tenantDbs) {
            LOGGER.info("Validating indexes for tenantDB:" + tenantDb);
            Set<MongoIndex> actualIndexes = loadIndexInfoFromDB(db.getSisterDB(tenantDb));
            isValid &= super.isValid(expectedIndexes, actualIndexes, report, reportStats);
        }

        return isValid;
    }

    /**
     * @return the indexFile
     */
    public String getIndexFile() {
        return indexFile;
    }

    /**
     * @param indexFile the indexFile to set
     */
    public void setIndexFile(String indexFile) {
        this.indexFile = indexFile;
    }

    /**
     * @return the indexTxtFileParser
     */
    public IndexParser<String> getIndexTxtFileParser() {
        return indexTxtFileParser;
    }

    /**
     * @param indexTxtFileParser the indexTxtFileParser to set
     */
    public void setIndexTxtFileParser(IndexParser<String> indexTxtFileParser) {
        this.indexTxtFileParser = indexTxtFileParser;
    }

    /**
     * @return the tenantDA
     */
    public TenantDA getTenantDA() {
        return tenantDA;
    }

    /**
     * @param tenantDA the tenantDA to set
     */
    public void setTenantDA(TenantDA tenantDA) {
        this.tenantDA = tenantDA;
    }

}
