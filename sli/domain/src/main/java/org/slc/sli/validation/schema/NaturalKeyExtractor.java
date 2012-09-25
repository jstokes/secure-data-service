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
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slc.sli.common.domain.NaturalKeyDescriptor;
import org.slc.sli.domain.Entity;
import org.slc.sli.validation.NaturalKeyValidationException;
import org.slc.sli.validation.SchemaRepository;

/**
 * @author sashton
 */
@Component
public class NaturalKeyExtractor implements INaturalKeyExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(NaturalKeyExtractor.class);

    @Autowired
    protected SchemaRepository entitySchemaRegistry;

    /* (non-Javadoc)
     * @see org.slc.sli.validation.schema.INaturalKeyExtractor#getNaturalKeys(org.slc.sli.domain.Entity)
     */
    @Override
    public Map<String, String> getNaturalKeys(Entity entity) {
        Map<String, String> map = new HashMap<String, String>();

        List<String> missingKeys = new ArrayList<String>();

        Map<String, Boolean> naturalKeyFields = getNaturalKeyFields(entity);
        for (Entry<String, Boolean> keyField : naturalKeyFields.entrySet()) {
            Object value = entity.getBody().get(keyField.getKey());
            if (value == null) {
                if (keyField.getValue().booleanValue()) {
                    map.put(keyField.getKey(), "");
                }
                else{
                    // if the required key field is not found, there's a problem
                    missingKeys.add(keyField.getKey());
                }
            }
            if (value instanceof String) {
                String strValue = (String) value;
                map.put(keyField.getKey(), strValue);
            }
        }
        if (!missingKeys.isEmpty()) {
            throw new NaturalKeyValidationException(entity.getType(), missingKeys);
        }

        return map;
    }

    /* (non-Javadoc)
     * @see org.slc.sli.validation.schema.INaturalKeyExtractor#getNaturalKeyFields(org.slc.sli.domain.Entity)
     */
    @Override
    public Map<String, Boolean> getNaturalKeyFields(Entity entity) {

        Map<String, Boolean> naturalKeyFields = new HashMap<String, Boolean>();

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
                                Boolean isOptional = null;
                                if (fieldsAppInfo.isRequired()) {
                                   isOptional = new Boolean(false);
                                } else {
                                    isOptional = new Boolean(true);
                                }
                                naturalKeyFields.put(field, isOptional);
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
     * .Entity)
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
        NaturalKeyDescriptor naturalKeyDescriptor = new NaturalKeyDescriptor(map, tenantId, entityType);
        return naturalKeyDescriptor;
    }
}
