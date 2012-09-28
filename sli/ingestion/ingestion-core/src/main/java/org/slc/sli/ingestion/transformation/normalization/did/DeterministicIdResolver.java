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

package org.slc.sli.ingestion.transformation.normalization.did;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import org.slc.sli.common.domain.NaturalKeyDescriptor;
import org.slc.sli.common.util.uuid.UUIDGeneratorStrategy;
import org.slc.sli.domain.Entity;
import org.slc.sli.ingestion.transformation.normalization.IdResolutionException;
import org.slc.sli.ingestion.validation.ErrorReport;
import org.slc.sli.validation.SchemaRepository;
import org.slc.sli.validation.schema.AppInfo;
import org.slc.sli.validation.schema.NeutralSchema;

/**
 * Resolver for deterministic id resolution.
 *
 * @author jtully
 * @author vmcglaughlin
 *
 */
@Component
public class DeterministicIdResolver {

    @Autowired
    @Qualifier("deterministicUUIDGeneratorStrategy")
    private UUIDGeneratorStrategy uuidGeneratorStrategy;

    @Autowired
    private DidEntityConfigFactory didEntityConfigurations;

    @Autowired
    private DidRefConfigFactory didRefConfigurations;

    @Autowired
    private SchemaRepository schemaRepository;

    private static final Logger LOG = LoggerFactory.getLogger(DeterministicIdResolver.class);

    public void resolveInternalIds(Entity entity, String tenantId, ErrorReport errorReport) {

        DidEntityConfig entityConfig = didEntityConfigurations.getDidEntityConfiguration(entity.getType());

        if (entityConfig == null) {
            LOG.warn("Entity configuration is null --> returning...");
            return;
        }

        if (entityConfig.getReferenceSources() == null || entityConfig.getReferenceSources().isEmpty()) {
            LOG.debug("Entity configuration contains no references --> returning...");
            return;
        }

        String collectionName = "";

        for (DidRefSource didRefSource : entityConfig.getReferenceSources()) {
            // TODO should we check if these are null or empty up here?
            String entityType = didRefSource.getEntityType();
            String didFieldPath = didRefSource.getDidFieldPath();
            String sourceRefPath = didRefSource.getSourceRefPath();

            try {

                DidRefConfig didRefConfig = didRefConfigurations.getDidRefConfiguration(entityType);

                if (didRefConfig == null) {
                     continue;
                }

                NeutralSchema schema = schemaRepository.getSchema(entityType);
                if (schema != null) {
                    AppInfo appInfo = schema.getAppInfo();
                    if (appInfo != null) {
                        collectionName = appInfo.getCollectionType();
                    }
                }

                Object referenceObject = PropertyUtils.getProperty(entity, sourceRefPath);
                if (referenceObject == null) {
                    //ignore an empty reference if it is optional
                    if (didRefSource.isOptional()) {
                        continue;
                    } else {
                        throw new IdResolutionException("Entity missing key", sourceRefPath, null);
                    }
                }

                if (referenceObject instanceof List) {
                    //handle a lists of reference object
                    @SuppressWarnings("unchecked")
                    List<Object> refList = (List<Object>) referenceObject;
                    List<String> uuidList = new ArrayList<String>();

                    for (Object reference :  refList) {
                        @SuppressWarnings("unchecked")
                        String uuid = getId((Map<String, Object>) reference, tenantId, didRefConfig);
                        if (uuid != null && !uuid.isEmpty()) {
                            uuidList.add(uuid);
                            LOG.debug("Set a DID for an entity in a list " + entity + ": " + uuid);
                        } else {
                            // TODO key and value below aren't what we want
                            throw new IdResolutionException("Null or empty deterministic id generated", didFieldPath, uuid);
                        }
                    }
                    PropertyUtils.setProperty(entity, didFieldPath, uuidList);
                } else {
                    //handle a single reference object
                    @SuppressWarnings("unchecked")
                    Map<String, Object> reference = (Map<String, Object>) referenceObject;

                    String uuid = getId(reference, tenantId, didRefConfig);
                    if (uuid != null && !uuid.isEmpty()) {
                        PropertyUtils.setProperty(entity, didFieldPath, uuid);
                        LOG.debug("Sed a DID for entity " + entity + ": " + uuid);
                    } else {
                        // TODO key and value below aren't what we want
                        throw new IdResolutionException("Null or empty deterministic id generated", didFieldPath, uuid);
                    }
                }
            } catch (IllegalAccessException e) {
                handleException(sourceRefPath, entityType, collectionName, e, errorReport);
            } catch (InvocationTargetException e) {
                handleException(sourceRefPath, entityType, collectionName, e, errorReport);
            } catch (NoSuchMethodException e) {
                handleException(sourceRefPath, entityType, collectionName, e, errorReport);
            } catch (IllegalArgumentException e) {
                handleException(sourceRefPath, entityType, collectionName, e, errorReport);
            } catch (IdResolutionException e) {
                handleException(sourceRefPath, entityType, collectionName, e, errorReport);
            }
        }
    }

    private void handleException(String sourceRefPath, String entityType, String collectionName, Exception e, ErrorReport errorReport) {
        LOG.error("Error accessing indexed bean property " + sourceRefPath
                + " for bean " + entityType, e);
        String errorMessage = "ERROR: Failed to resolve a reference"
                + "\n       Entity " + entityType + ": Reference to " + collectionName
                + " is incomplete because the following reference field is not resolved: "
                + sourceRefPath.substring(sourceRefPath.lastIndexOf('.') + 1);

        errorReport.error(errorMessage, this);
    }

    // function which, given reference type map (source object) and refConfig, return a did
    private String getId(Map<String, Object> reference, String tenantId, DidRefConfig didRefConfig)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IllegalArgumentException {

        if (didRefConfig.getEntityType() == null || didRefConfig.getEntityType().isEmpty()) {
            return null;
        }

        if (didRefConfig.getKeyFields() == null || didRefConfig.getKeyFields().isEmpty()) {
            return null;
        }

        Map<String, String> naturalKeys = new HashMap<String, String>();

        for (KeyFieldDef keyFieldDef : didRefConfig.getKeyFields()) {
            // populate naturalKeys
            String value = null;
            if (keyFieldDef.getRefConfig() != null) {
                value = getId(reference, tenantId, keyFieldDef.getRefConfig());
            } else {
                value = (String) PropertyUtils.getProperty(reference, keyFieldDef.getValueSource());
            }

            String fieldName = keyFieldDef.getKeyFieldName();
            // don't add null or empty keys or values to the naturalKeys map
            if (fieldName == null || fieldName.isEmpty() || value == null || value.isEmpty()) {
                continue;
            }
            naturalKeys.put(fieldName, value);
        }

        // no natural keys found
        if (naturalKeys.isEmpty()) {
            return null;
        }

        NaturalKeyDescriptor naturalKeyDescriptor = new NaturalKeyDescriptor(naturalKeys, tenantId, didRefConfig.getEntityType());
        return uuidGeneratorStrategy.generateId(naturalKeyDescriptor);
    }
}
