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
package org.slc.sli.validation.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slc.sli.common.domain.NaturalKeyDescriptor;
import org.slc.sli.domain.Entity;
import org.slc.sli.validation.SchemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author sashton
 */
@Component
public class NaturalKeyExtractor implements INaturalKeyExtractor {
    
    @Autowired
    protected SchemaRepository entitySchemaRegistry;
    
    /* (non-Javadoc)
     * @see org.slc.sli.validation.schema.INaturalKeyExtractor#getNaturalKeys(org.slc.sli.domain.Entity)
     */
    @Override
    public Map<String, String> getNaturalKeys(Entity entity) {
        Map<String, String> map = new HashMap<String, String>();
        
        List<String> naturalKeyFields = getNaturalKeyFields(entity);
        for (String keyField : naturalKeyFields) {
            Object value = entity.getBody().get(keyField);
            if (value instanceof String) {
                String strValue = (String) value;
                map.put(keyField, strValue);
            }
        }
        
        return map;
    }
    
    /* (non-Javadoc)
     * @see org.slc.sli.validation.schema.INaturalKeyExtractor#getNaturalKeyFields(org.slc.sli.domain.Entity)
     */
    @Override
    public List<String> getNaturalKeyFields(Entity entity) {
        
        List<String> naturalKeyFields = new ArrayList<String>();
        
        NeutralSchema schema = entitySchemaRegistry.getSchema(entity.getType());
        if (schema != null) {
            
            AppInfo appInfo = schema.getAppInfo();
            if (appInfo != null) {
                if (appInfo.applyNaturalKeys()) {
                    Map<String, NeutralSchema> fields = schema.getFields();
                    for (Iterator<Map.Entry<String, NeutralSchema>> i = fields.entrySet().iterator(); i.hasNext();) {
                        Map.Entry entry = i.next();
                        String field = (String) entry.getKey();
                        NeutralSchema fieldSchema = (NeutralSchema) entry.getValue();
                        
                        AppInfo fieldsAppInfo = fieldSchema.getAppInfo();
                        if (fieldsAppInfo != null) {
                            if (fieldsAppInfo.isNaturalKey()) {
                                naturalKeyFields.add(field);
                            }
                        }
                    }
                }
            }
        }
        
        return naturalKeyFields;
    }
    
    /* (non-Javadoc)
     * @see org.slc.sli.validation.schema.INaturalKeyExtractor#getNaturalKeyDescriptor(org.slc.sli.domain.Entity)
     */
    @Override
    public NaturalKeyDescriptor getNaturalKeyDescriptor(Entity entity) {
        
        Map<String, String> map = getNaturalKeys(entity);
        if (map.isEmpty()) {
            // no natural keys were found, don't return a descriptor
            return null;
        }
        
        String entityType = entity.getType();
        String tenantId = (String) entity.getMetaData().get("tenantId");
        NaturalKeyDescriptor naturalKeyDescriptor = new NaturalKeyDescriptor(map, entityType, tenantId);
        return naturalKeyDescriptor;
    }
}
