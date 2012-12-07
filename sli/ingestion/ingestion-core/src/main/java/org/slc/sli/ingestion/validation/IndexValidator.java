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

package org.slc.sli.ingestion.validation;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.slc.sli.ingestion.tenant.TenantDA;
import org.slc.sli.ingestion.util.MongoCommander;
import org.slc.sli.ingestion.util.MongoIndex;
import org.slc.sli.ingestion.validation.spring.SimpleValidatorSpring;

/**
 * Checks if the indexes are present for all the dbs before processing this job.
 * This validator fails if index files are not defined, it is only to add an
 * error message in the log file if the index counts do not match.
 *
 * @author unavani
 *
 */
public class IndexValidator extends SimpleValidatorSpring<Object> {

    private Logger log = LoggerFactory.getLogger(IndexValidator.class);

    @Autowired
    private MongoTemplate batchJobMongoTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TenantDA tenantDA;

    private Map<String, List<HashMap<String, Object>>> sliIndexCache;
    private Map<String, List<HashMap<String, Object>>> batchJobIndexCache;
    private Map<String, List<HashMap<String, Object>>> tenantIndexCache;

    final private String TENANT_INDEX = "tenantDB_indexes.txt";

    @Override
    public boolean isValid(Object object, ErrorReport callback) {

        String errorMessage = "";

        if (sliIndexCache == null) {
            sliIndexCache = new HashMap<String, List<HashMap<String, Object>>>();
        }
        if (batchJobIndexCache == null) {
            batchJobIndexCache = new HashMap<String, List<HashMap<String, Object>>>();
        }
        if(tenantIndexCache == null) {
            tenantIndexCache = new HashMap<String, List<HashMap<String, Object>>>();
        }

        try {
            errorMessage += parseFile("sli_indexes.js", sliIndexCache, mongoTemplate);
            errorMessage += parseFile("ingestion_batch_job_indexes.js", batchJobIndexCache, batchJobMongoTemplate);
            errorMessage += verifyTenantDbs(tenantIndexCache, mongoTemplate);
        } catch (URISyntaxException e) {
            log.error("Error occured while verifying indexes: " + e.getLocalizedMessage());
        }

        if (!errorMessage.equals("")) {
            log.error("The following indexes are missing" + errorMessage);
        } else {
            log.info("Indexes verified");
        }

        return true;
    }

    /**Verify the tenantDB indexes.
     * This method assumes all tenant DB have the same indexes
     * @return
     */
    @SuppressWarnings("unchecked")
    private String verifyTenantDbs(Map<String, List<HashMap<String, Object>>> indexCache, MongoTemplate mongoTemplate) {
        StringBuilder errorMessage = new StringBuilder("");

        List<String> tenantDbs = tenantDA.getAllTenantDbs();
        Set<String> indexes = MongoCommander.readIndexes(TENANT_INDEX);

        for (String tenantDb : tenantDbs) {
            for(String indexEntry : indexes) {
                MongoIndex indexObj = MongoCommander.parseIndex(indexEntry);
                if (indexObj != null) {
                    boolean indexPresent = verifyIndex(indexCache, mongoTemplate.getDb().getSisterDB(tenantDb), indexObj.getCollection(), indexObj.getKeys().toMap());
                    if (!indexPresent) {
                        errorMessage.append("\nIndex " + indexObj.getKeys().toString() + " missing from collection " + indexObj.getCollection());
                    }
                }
            }
        }

        return errorMessage.toString();
    }

    private String parseFile(String fileName, Map<String, List<HashMap<String, Object>>> indexCache,
            MongoTemplate mongoTemplate) throws URISyntaxException {
        String message = "";
        URL resourceFile = Thread.currentThread().getContextClassLoader().getResource(fileName);
        if (resourceFile != null) {
            File file = new File(resourceFile.toURI());
            if (file != null) {
                message += parseFile(file, indexCache, mongoTemplate);
            }
        }
        return message;
    }

    private String parseFile(File file, Map<String, List<HashMap<String, Object>>> indexCache,
            MongoTemplate mongoTemplate) {
        StringBuilder errorMessage = new StringBuilder("");
        FileInputStream fstream = null;
        BufferedReader br = null;

        try {
            fstream = new FileInputStream(file);

            DataInputStream in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));

            String currentFileLine;
            while ((currentFileLine = br.readLine()) != null) {
                Matcher indexMatcher = ensureIndexStatement(currentFileLine);
                if (indexMatcher != null) {
                    String collectionName = indexMatcher.group(1);
                    String jsonString = indexMatcher.group(2);

                    Map<String, Object> indexMap = parseJson(jsonString);
                    if (indexMap != null) {
                        boolean indexPresent = verifyIndex(indexCache, mongoTemplate.getDb(), collectionName, indexMap);
                        if (!indexPresent) {
                            errorMessage.append("\nIndex " + jsonString + " missing from collection " + collectionName);
                        }
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error occured while verifying indexes: " + e.getLocalizedMessage());
        } finally {
            IOUtils.closeQuietly(br);
            IOUtils.closeQuietly(fstream);
        }

        return errorMessage.toString();
    }

    protected Matcher ensureIndexStatement(String statement) {

        Pattern ensureIndexPattern = Pattern.compile("^db\\[\"(\\S+)\"]\\.ensureIndex\\((\\{[^}]*\\}).*\\);",
                Pattern.MULTILINE);
        Matcher ensureIndexMatcher = ensureIndexPattern.matcher(statement);
        if (ensureIndexMatcher.matches()) {
            return ensureIndexMatcher;
        }
        return null;
    }

    protected Map<String, Object> parseJson(String jsonString) {

        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);

        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
        };
        try {
            return mapper.readValue(jsonString, typeRef);
        } catch (JsonParseException e) {
            log.error("Error validating indexes " + e.getLocalizedMessage());
        } catch (JsonMappingException e) {
            log.error("Error validating indexes " + e.getLocalizedMessage());
        } catch (IOException e) {
            log.error("Error validating indexes " + e.getLocalizedMessage());
        }
        return null;
    }

    private boolean verifyIndex(Map<String, List<HashMap<String, Object>>> indexCache, DB db,
            String collectionName, Map<String, Object> indexMapFromJson) {

        // UN: Check the index cache, if the collection exists in the cache, use that collection,
        // else query from Mongo and save it in the cache.
        if (!indexCache.containsKey(collectionName)) {
            if (db.collectionExists(collectionName)) {
                DBCollection collection = db.getCollection(collectionName);
                List<DBObject> indexList = collection.getIndexInfo();
                List<HashMap<String, Object>> indices = new ArrayList<HashMap<String, Object>>();
                for (DBObject dbObject : indexList) {
                    Object object = dbObject.get("key");
                    indices.add((HashMap<String, Object>) parseJson(object.toString()));
                }
                indexCache.put(collectionName, indices);
            }
        }

        boolean found = false;
        if (indexCache.containsKey(collectionName)) {
            List<HashMap<String, Object>> indices = indexCache.get(collectionName);
            for (Map<String, Object> indexMapFromCache : indices) {
                if (indexMapFromCache.size() != indexMapFromJson.size()) {
                    continue;
                }
                boolean indexMatch = true;
                for (Map.Entry<String, Object> indexCacheEntry : indexMapFromCache.entrySet()) {
                    if (!indexMapFromJson.containsKey(indexCacheEntry.getKey())) {
                        indexMatch = false;
                        break;
                    }

                    // UN: The value in DB is either saved as a double or integer
                    // (nondeterministic), so I
                    // need to compare it with both double as well as integer and verify that the
                    // index
                    // does not match.
                    double indexMapDoubleValue = Double.valueOf(indexMapFromJson.get(indexCacheEntry.getKey())
                            .toString());
                    if (!indexCacheEntry.getValue().equals(indexMapDoubleValue)
                            && !indexCacheEntry.getValue().equals(indexMapFromJson.get(indexCacheEntry.getKey()))) {
                        indexMatch = false;
                        break;
                    }
                }
                if (indexMatch) {
                    log.info("{} : {} Indexes verified", db.getName(), collectionName );
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    public TenantDA getTenantDA() {
        return tenantDA;
    }

    public void setTenantDA(TenantDA tenantDA) {
        this.tenantDA = tenantDA;
    }
    public MongoTemplate getBatchJobMongoTemplate() {
        return batchJobMongoTemplate;
    }

    public void setBatchJobMongoTemplate(MongoTemplate batchJobMongoTemplate) {
        this.batchJobMongoTemplate = batchJobMongoTemplate;
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
