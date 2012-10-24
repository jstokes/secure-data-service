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
package org.slc.sli.search.transform;

import java.util.Map;

import org.slc.sli.dal.encrypt.EntityEncryption;
import org.slc.sli.search.config.IndexConfig;
import org.slc.sli.search.config.IndexConfigStore;
import org.slc.sli.search.entity.IndexEntity;
import org.slc.sli.search.entity.IndexEntity.Action;
import org.slc.sli.search.transform.impl.GenericFilter;
import org.slc.sli.search.transform.impl.GenericTransformer;
import org.slc.sli.search.util.IndexEntityUtil;
import org.slc.sli.search.util.NestedMapUtil;
import org.slc.sli.search.util.SearchIndexerException;

/**
 * IndexEntityConverter handles conversion of IndexEntity to and from entity
 * 
 */
public class IndexEntityConverter {
    private EntityEncryption entityEncryption;
    private IndexConfigStore indexConfigStore;
    private final GenericTransformer transformer = new GenericTransformer();
    private final GenericFilter filter = new GenericFilter();
    // decrypt records flag
    private boolean decrypt = true;
    
    public IndexEntity fromEntityJson(String index, String entity) {
        return fromEntityJson(index, Action.INDEX, entity);
    }
    
    public IndexEntity fromEntityJson(String index, Action action, String entity) {
        Map<String, Object> entityMap = IndexEntityUtil.getEntity(entity);
        return fromEntity(index, action, entityMap);
    }
    
    @SuppressWarnings("unchecked")
    public IndexEntity fromEntity(String index, Action action, Map<String, Object> entityMap) {
        try {
            Map<String, Object> body = (Map<String, Object>) entityMap.get("body");
            Map<String, Object> metaData = (Map<String, Object>) entityMap.get("metaData");
            String type = (String)entityMap.get("type");
            // decrypt body if needed
            Map<String, Object> decryptedMap = decrypt ? entityEncryption.decrypt(type, body): body;
            // get tenantId
            String indexName = (index == null) ? ((String)metaData.get("tenantId")).toLowerCase() : index.toLowerCase();
            IndexConfig config = indexConfigStore.getConfig(type);
            //re-assemble entity map
            entityMap.put("body", decryptedMap);
            // filter out
            if (!filter.matchesCondition(config, entityMap))
                return null;
            
            // transform the entities
            transformer.transform(config, entityMap);
            
            String id = (String)entityMap.get("_id");
            String parent = (config.getParentField() != null) ? 
                    (String)NestedMapUtil.get(config.getParentField(), entityMap) : null;
            String indexType = config.getIndexType() == null ? type : config.getIndexType();
            action = config.isChildDoc() ?  IndexEntity.Action.UPDATE : action;
            return new IndexEntity(action, indexName, indexType, id, parent, (Map<String, Object>)entityMap.get("body"));
            
        } catch (Exception e) {
            throw new SearchIndexerException("Unable to convert entity", e);
        } 
    }
    
    public void setDecrypt(boolean decrypt) {
        this.decrypt = decrypt;
    }
    public void setEntityEncryption(EntityEncryption entityEncryption) {
        this.entityEncryption = entityEncryption;
    }
    
    public void setIndexConfigStore(IndexConfigStore indexConfigStore) {
        this.indexConfigStore = indexConfigStore;
    }
}
