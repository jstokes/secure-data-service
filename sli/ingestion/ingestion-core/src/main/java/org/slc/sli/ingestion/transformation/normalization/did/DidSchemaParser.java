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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.xml.namespace.QName;

import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaAnnotation;
import org.apache.ws.commons.schema.XmlSchemaAppInfo;
import org.apache.ws.commons.schema.XmlSchemaChoice;
import org.apache.ws.commons.schema.XmlSchemaCollection;
import org.apache.ws.commons.schema.XmlSchemaComplexContentExtension;
import org.apache.ws.commons.schema.XmlSchemaComplexContentRestriction;
import org.apache.ws.commons.schema.XmlSchemaComplexType;
import org.apache.ws.commons.schema.XmlSchemaContent;
import org.apache.ws.commons.schema.XmlSchemaElement;
import org.apache.ws.commons.schema.XmlSchemaObject;
import org.apache.ws.commons.schema.XmlSchemaObjectCollection;
import org.apache.ws.commons.schema.XmlSchemaParticle;
import org.apache.ws.commons.schema.XmlSchemaSequence;
import org.apache.ws.commons.schema.resolver.URIResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Parser for the Ed-Fi XSD and Ed-Fi extension XSD.
 * Responsible for creating deterministic id reference resolution
 * configuration objects, based on XSD annotations.
 *
 * @author jtully
 *
 */
public class DidSchemaParser implements ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    // cache for complex types
    private Map<String, XmlSchemaComplexType> complexTypes;
    // cache for reference types
    private Map<String, XmlSchemaComplexType> referenceTypes;

    // per entity configs for deterministic id resolution
    private Map<String, DidEntityConfig> entityConfigs;

    // per reference configs for deterministic id resolution
    private Map<String, DidRefConfig> refConfigs;

    private String xsdLocation;
    private String xsdParentLocation;

    private String extensionXsdLocation;
    private String extensionXsdParentLocation;

    private Map<String, DidRefSource> refSourceCache;

    // schema type constants
    private static final String REFERENCE_TYPE = "ReferenceType";
    private static final String IDENTITY_TYPE = "IdentityType";

    // Did annotation constants
    private static final String APPLY_KEY_FIELDS = "applyKeyFields";
    private static final String REF_TYPE = "refType";
    private static final String RECORD_TYPE = "recordType";
    private static final String KEY_FIELD_NAME = "keyFieldName";
    private static final String XPATH_PREFIX = "body.";

    private static final Logger LOG = LoggerFactory.getLogger(DidSchemaParser.class);

    public String getExtensionXsdLocation() {
        return extensionXsdLocation;
    }

    public void setExtensionXsdLocation(String entensionXsdLocation) {
        this.extensionXsdLocation = entensionXsdLocation;
    }

    public String getExtensionXsdParentLocation() {
        return extensionXsdParentLocation;
    }

    public void setExtensionXsdParentLocation(String extensionXsdParentLocation) {
        this.extensionXsdParentLocation = extensionXsdParentLocation;
    }

    public String getXsdParentLocation() {
        return xsdParentLocation;
    }

    public void setXsdParentLocation(String xsdParentLocation) {
        this.xsdParentLocation = xsdParentLocation;
    }

    public String getXsdLocation() {
        return xsdLocation;
    }

    public void setXsdLocation(String xsdLocation) {
        this.xsdLocation = xsdLocation;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Initialization method, parses XSD for complexTypes and referenceTypes
     */
    @PostConstruct
    public void setup() {
        complexTypes = new HashMap<String, XmlSchemaComplexType>();

        Resource xsdResource = resourceLoader.getResource(xsdLocation);
        Resource extensionXsdResource = resourceLoader.getResource(extensionXsdLocation);

        // extract complex types from base schema
        cacheComplexTypesFromResource(xsdResource, xsdParentLocation);
        // extract complex types from extension schema
        cacheComplexTypesFromResource(extensionXsdResource, extensionXsdParentLocation);

        // extract and cache the reference types from the complexTypes
        cacheReferenceTypes();

        removeParentTypesFromCache();

        refSourceCache = new HashMap<String, DidRefSource>();

        // extract the Did configuration objects
        entityConfigs = extractEntityConfigs();
        refConfigs = extractRefConfigs();
    }

    public Map<String, DidRefConfig> getRefConfigs() {
        return refConfigs;
    }

    public Map<String, DidEntityConfig> getEntityConfigs() {
        return entityConfigs;
    }

    /**
     * Extract entity configs
     */
    private Map<String, DidEntityConfig> extractEntityConfigs() {
        Map<String, DidEntityConfig> entityConfigs = new HashMap<String, DidEntityConfig>();

        // Iterate XML Schema items
        for (Entry<String, XmlSchemaComplexType> complexType : complexTypes.entrySet()) {

            // exclude IdentityTypes which may also contain referenceTypes but shouldn't result in
            // an EntityConfig
            if (complexType.getKey().contains(IDENTITY_TYPE)) {
                continue;
            }

            DidEntityConfig entityConfig = extractEntityConfig(complexType.getValue());
            if (entityConfig != null) {
                //extract the entity annotations
                XmlSchemaAnnotation annotation = complexType.getValue().getAnnotation();
                if (annotation != null) {
                    String recordType = parseAnnotationForRecordType(annotation);
                    if (recordType != null) {
                        entityConfigs.put(recordType, entityConfig);
                    } else {
                        LOG.error("Failed to extract DidEntityConfig for type " + complexType.getKey() + ", couldn't find recordType annotation");
                    }
                } else {
                    LOG.error("Failed to extract DidEntityConfig for type " + complexType.getKey() + " - null annotation");
                }
            }
        }

        return entityConfigs;
    }

    /**
     * Extract ref configs
     */
    private Map<String, DidRefConfig> extractRefConfigs() {
        Map<String, DidRefConfig> refConfigs = new HashMap<String, DidRefConfig>();

        // Iterate XML Schema items
        for (Entry<String, XmlSchemaComplexType> refType : referenceTypes.entrySet()) {
            DidRefConfig refConfig = extractRefConfig(refType.getValue());
            if (refConfig != null) {
                refConfigs.put(refConfig.getEntityType(), refConfig);
            }
        }

        return refConfigs;
    }

    /**
     * extract complex types from a schema resource and cache in complexTypes
     */
    private void cacheComplexTypesFromResource(Resource schemaResource, String baseXsdPath) {
        try {
            // parse the xsd schema and pull out complex types
            XmlSchema xmlSchema = parseXmlSchema(schemaResource.getInputStream(), baseXsdPath);
            cacheComplexTypes(xmlSchema);
        } catch (IOException e) {
            LOG.error("Failed parse schema " + schemaResource.getFilename(), e);
        }
    }

    private XmlSchema parseXmlSchema(final InputStream is, final String baseXsdPath) {
        try {
            XmlSchemaCollection schemaCollection = new XmlSchemaCollection();
            // schemaCollection.setBaseUri(baseUri);
            schemaCollection.setSchemaResolver(new URIResolver() {
                @Override
                public InputSource resolveEntity(String targetNamespace, String schemaLocation, String baseUri) {
                    if (resourceLoader != null) {
                        Resource resource = resourceLoader.getResource(baseXsdPath + "/" + schemaLocation);
                        if (resource.exists()) {
                            try {
                                return new InputSource(resource.getInputStream());
                            } catch (IOException e) {
                                throw new RuntimeException("Exception occurred", e);
                            }
                        }
                    }
                    return new InputSource(Thread.currentThread().getContextClassLoader()
                            .getResourceAsStream(baseXsdPath + "/" + schemaLocation));
                }
            });
            return schemaCollection.read(new InputSource(is), null);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        } finally {
            try {
                is.close();
            } catch (IOException ioException) {
                throw new RuntimeException(ioException);
            }
        }
    }

    /**
     * extract all complex types from a schema and cache into a map
     */
    private void cacheComplexTypes(XmlSchema schema) {
        XmlSchemaObjectCollection schemaItems = schema.getItems();

        int numElements = schemaItems.getCount();

        // Iterate XML Schema items
        for (int i = 0; i < numElements; i++) {
            XmlSchemaObject schemaObject = schemaItems.getItem(i);
            if (schemaObject instanceof XmlSchemaComplexType) {
                XmlSchemaComplexType complexType = (XmlSchemaComplexType) schemaObject;
                String elementTypeName = complexType.getName();
                complexTypes.put(elementTypeName, complexType);
            }
        }
    }

    /**
     * Remove parent types from the complexTypes cache.
     * We are only interested in the leaf node extended types.
     */
    private void removeParentTypesFromCache() {
        // find all the parent types
        Set<String> parentTypeSet = new HashSet<String>();

        for (XmlSchemaComplexType complexType : complexTypes.values()) {
            // this needs to also respect restriction
            String baseName = extractBaseTypeName(complexType);
            if (baseName != null) {
                parentTypeSet.add(baseName);
            }
        }

        // remove all the parentTypes from cache
        for (String parentType : parentTypeSet) {
            complexTypes.remove(parentType);
            referenceTypes.remove(parentType);
        }
    }

    /**
     * Extract and cache all reference types from the complexTypes map.
     */
    private void cacheReferenceTypes() {
        referenceTypes = new HashMap<String, XmlSchemaComplexType>();

        // extract referenceTypes from the complexTypes
        for (Entry<String, XmlSchemaComplexType> complexTypeEntry : complexTypes.entrySet()) {
            if (isReferenceType(complexTypeEntry.getValue())) {
                referenceTypes.put(complexTypeEntry.getKey(), complexTypeEntry.getValue());
            }
        }
    }

    /**
     * determine whether a given complexType is a referenceType
     * by traversing through all baseSchemas looking for ReferenceType
     */
    private boolean isReferenceType(XmlSchemaComplexType complexType) {

        String baseName = extractBaseTypeName(complexType);


        if (complexType.getName().contains(REFERENCE_TYPE)) {
            return true;
        }

        return false;
    }

    /**
     * Extract a particle from a complex type
     * returns null if it can't be extracted.
     */
    private XmlSchemaParticle extractParticle(XmlSchemaComplexType complexType) {
        XmlSchemaParticle particle = complexType.getParticle();

        // handle case where the complexType is an extension
        if (particle == null && complexType.getContentModel() != null
                && complexType.getContentModel().getContent() != null) {
            XmlSchemaContent content = complexType.getContentModel().getContent();
            if (content instanceof XmlSchemaComplexContentExtension) {
                XmlSchemaComplexContentExtension complexContent = (XmlSchemaComplexContentExtension) content;
                particle = complexContent.getParticle();
            }
        }

        return particle;
    }

    /**
     * Extract a particle from a complex type, respecting both extensions and restrictions
     * returns null if there isn't one.
     */
    private String extractBaseTypeName(XmlSchemaComplexType complexType) {
        String baseTypeName = null;

        if (complexType.getBaseSchemaTypeName() != null) {
            baseTypeName = complexType.getBaseSchemaTypeName().getLocalPart();
        } else if (complexType.getContentModel() != null && complexType.getContentModel().getContent() != null) {
            XmlSchemaContent content = complexType.getContentModel().getContent();
            if (content instanceof XmlSchemaComplexContentRestriction) {
                XmlSchemaComplexContentRestriction contentRestriction = (XmlSchemaComplexContentRestriction) content;
                if (contentRestriction.getBaseTypeName() != null) {
                    baseTypeName = contentRestriction.getBaseTypeName().getLocalPart();
                }
            }
        }

        return baseTypeName;
    }

    /**
     * Extract refConfig for a refType
     */
    private DidRefConfig extractRefConfig(XmlSchemaComplexType refType) {
        // get the identityType out of the refType
        DidRefConfig refConfig = null;

        // check that this refConfig is configures to go through DID Resolver
        DidRefSource refSource = getRefSource(refType);

        if (refSource != null) {
            // find the identity type element
            XmlSchemaElement identityTypeElement = null;

            identityTypeElement = parseParticleForIdentityType(extractParticle(refType));

            if (identityTypeElement != null) {
                XmlSchemaComplexType identityType = null;
                identityType = complexTypes.get(identityTypeElement.getSchemaTypeName().getLocalPart());
                String baseXPath = identityTypeElement.getName() + ".";

                // need this to recursively extract refConfigs
                refConfig = new DidRefConfig();
                refConfig.setEntityType(refSource.getEntityType());

                // parse the reference type
                parseParticleForRefConfig(extractParticle(identityType), refConfig, baseXPath, false);

            } else {
                LOG.error("Failed to extract IdentityType for referenceType " + refType.getName());
                return null;
            }
        }
        return refConfig;
    }

    /**
     * Extract a DidEntityConfig for a ComplexType.
     * Returns null if the reference contains no DID references.
     */
    private DidEntityConfig extractEntityConfig(XmlSchemaComplexType complexType) {
        DidEntityConfig entityConfig = null;

        List<DidRefSource> refSources = new ArrayList<DidRefSource>();
        parseParticleForRef(extractParticle(complexType), refSources, false);

        // if any DidRefSources were found for this complex type, create a DidEntityConfig
        if (refSources.size() > 0) {
            entityConfig = new DidEntityConfig();
            entityConfig.setReferenceSources(refSources);
        }

        return entityConfig;
    }

    /**
     * Recursively parse through an XmlSchemaPatricle to the elements
     * collecting all DidRefSources
     */
    private XmlSchemaElement parseParticleForIdentityType(XmlSchemaParticle particle) {
        XmlSchemaElement identityType = null;
        if (particle != null) {
            if (particle instanceof XmlSchemaElement) {
                XmlSchemaElement element = (XmlSchemaElement) particle;
                String elementName = element.getSchemaTypeName().getLocalPart();
                if (elementName.contains(IDENTITY_TYPE)) {
                    identityType = element;
                }
            } else if (particle instanceof XmlSchemaSequence) {
                XmlSchemaSequence schemaSequence = (XmlSchemaSequence) particle;
                for (int i = 0; i < schemaSequence.getItems().getCount(); i++) {
                    XmlSchemaObject item = schemaSequence.getItems().getItem(i);
                    if (item instanceof XmlSchemaParticle) {
                        identityType = parseParticleForIdentityType((XmlSchemaParticle) item);
                    }
                }
            }
        }
        return identityType;
    }

    /**
     * Recursively parse through an XmlSchemaPatricle to the elements
     * filling in the refConfig data, including nested refConfigs
     */
    private void parseParticleForRefConfig(XmlSchemaParticle particle, DidRefConfig refConfig, String baseXPath, boolean isOptional) {
        if (particle != null) {
            if (particle instanceof XmlSchemaElement) {
                XmlSchemaElement element = (XmlSchemaElement) particle;
                String elementName = element.getName();

                Map<String, String> keyFieldMap = parseAnnotationForKeyField(element.getAnnotation());

                if (keyFieldMap.containsKey(KEY_FIELD_NAME)) {
                    // create a new key field
                    KeyFieldDef keyfield = new KeyFieldDef();
                    String xPath = baseXPath + elementName;

                    keyfield.setKeyFieldName(keyFieldMap.get(KEY_FIELD_NAME));

                    QName elementType = element.getSchemaTypeName();

                    if (element.getMinOccurs() == 0) {
                        isOptional = true;
                    }

                    keyfield.setOptional(isOptional);

                    // check whether we have a nested Ref and create
                    if (elementType != null && referenceTypes.containsKey(elementType.getLocalPart())) {
                        XmlSchemaComplexType nestedRefType = referenceTypes.get(elementType.getLocalPart());
                        DidRefConfig nestedRefConfig = extractRefConfig(nestedRefType);
                        keyfield.setRefConfig(nestedRefConfig);
                    }
                    keyfield.setValueSource(xPath);

                    refConfig.getKeyFields().add(keyfield);

                }

            } else if (particle instanceof XmlSchemaSequence) {
                XmlSchemaSequence schemaSequence = (XmlSchemaSequence) particle;
                for (int i = 0; i < schemaSequence.getItems().getCount(); i++) {
                    XmlSchemaObject item = schemaSequence.getItems().getItem(i);
                    if (item instanceof XmlSchemaParticle) {
                        parseParticleForRefConfig((XmlSchemaParticle) item, refConfig, baseXPath, isOptional);
                    }
                }
            } else if (particle instanceof XmlSchemaChoice) {
                XmlSchemaChoice xmlSchemaChoice = (XmlSchemaChoice) particle;
                XmlSchemaObjectCollection choices = xmlSchemaChoice.getItems();

                //treat fields within a choice as being optional
                isOptional = true;

                for (int i = 0; i < choices.getCount(); i++) {
                    XmlSchemaObject item = xmlSchemaChoice.getItems().getItem(i);
                    if (item instanceof XmlSchemaParticle) {
                        parseParticleForRefConfig((XmlSchemaParticle) item, refConfig, baseXPath, isOptional);
                    }
                }
            }
        }
    }

    /**
     * Get the DidRefSource for a reference schema type
     */
    DidRefSource getRefSource(XmlSchemaComplexType refSchema) {
        DidRefSource refSource = null;
        String schemaName = refSchema.getName();
        if (refSourceCache.containsKey(schemaName)) {
            refSource = refSourceCache.get(schemaName);
            // if a cached refSource is found create return new DidRefSource of same type
            if (refSource != null) {
                DidRefSource cachedRefSource = refSource;
                refSource = new DidRefSource();
                refSource.setEntityType(cachedRefSource.getEntityType());
            }
        } else {
            XmlSchemaAnnotation annotation = refSchema.getAnnotation();
            if (annotation == null) {
                LOG.debug("Annotation missing from refSchema: " + refSchema.getName());
            } else {
                refSource = parseAnnotationForRef(annotation);
                refSourceCache.put(schemaName, refSource);
            }
        }
        return refSource;
    }

    /**
     * Recursively parse through an XmlSchemaPatricle to the elements
     * collecting all DidRefSources
     */
    private void parseParticleForRef(XmlSchemaParticle particle, List<DidRefSource> refs, boolean isOptional) {
        if (particle != null) {
            if (particle instanceof XmlSchemaElement) {
                XmlSchemaElement element = (XmlSchemaElement) particle;
                String elementName = element.getName();
                QName elementType = element.getSchemaTypeName();

                if (elementType != null && referenceTypes.containsKey(elementType.getLocalPart())) {

                    if (element.getMinOccurs() == 0) {
                        isOptional = true;
                    }

                    // TODO, this could be pre-computed for all refTypes to avoid some repetition
                    XmlSchemaComplexType refSchema = referenceTypes.get(elementType.getLocalPart());

                    DidRefSource refSource = getRefSource(refSchema);
                    if (refSource != null) {
                        refSource.setOptional(isOptional);
                        refSource.setSourceRefPath(XPATH_PREFIX + elementName);
                        refs.add(refSource);
                    }
                }
            } else if (particle instanceof XmlSchemaSequence) {
                XmlSchemaSequence schemaSequence = (XmlSchemaSequence) particle;
                for (int i = 0; i < schemaSequence.getItems().getCount(); i++) {
                    XmlSchemaObject item = schemaSequence.getItems().getItem(i);
                    if (item instanceof XmlSchemaParticle) {
                        parseParticleForRef((XmlSchemaParticle) item, refs, isOptional);
                    }
                }
            } else if (particle instanceof XmlSchemaChoice) {
                isOptional = true;
                XmlSchemaChoice xmlSchemaChoice = (XmlSchemaChoice) particle;
                XmlSchemaObjectCollection choices = xmlSchemaChoice.getItems();
                for (int i = 0; i < choices.getCount(); i++) {
                    XmlSchemaObject item = xmlSchemaChoice.getItems().getItem(i);
                    if (item instanceof XmlSchemaParticle) {
                        parseParticleForRef((XmlSchemaParticle) item, refs, isOptional);
                    }
                }
            }
        }
    }

    /**
     * Get SLI appInfor from an annotation
     */
    private XmlSchemaAppInfo getAppInfo(XmlSchemaAnnotation annotation) {
        XmlSchemaAppInfo appInfo = null;

        if (annotation != null) {
        	XmlSchemaObjectCollection items = annotation.getItems();

        	for (int annotationIdx = 0; annotationIdx < items.getCount(); annotationIdx++) {

        		XmlSchemaObject item = items.getItem(annotationIdx);
        		if (item instanceof XmlSchemaAppInfo) {
        			appInfo = (XmlSchemaAppInfo) item;
        			break;
        		}
        	}
        }

        return appInfo;
    }

    /**
     * Parse an annotation for keyfields and add to a map
     */
    private Map<String, String> parseAnnotationForKeyField(XmlSchemaAnnotation annotation) {
        Map<String, String> keyField = new HashMap<String, String>();

        XmlSchemaAppInfo appInfo = getAppInfo(annotation);
        if (appInfo != null) {
            NodeList nodes = appInfo.getMarkup();
            for (int nodeIdx = 0; nodeIdx < nodes.getLength(); nodeIdx++) {
                Node node = nodes.item(nodeIdx);
                if (node instanceof Element) {
                    String key = node.getLocalName().trim();
                    String value = node.getFirstChild().getNodeValue().trim();

                    if (key.equals(REF_TYPE) || key.equals(KEY_FIELD_NAME)) {
                        keyField.put(key, value);
                    }
                }
            }
        }
        return keyField;
    }

    /**
    * Parse an annotation for recordType annotation
    */
   private String parseAnnotationForRecordType(XmlSchemaAnnotation annotation) {
       String recordType = null;

       XmlSchemaAppInfo appInfo = getAppInfo(annotation);
       if (appInfo != null) {
           NodeList nodes = appInfo.getMarkup();
           for (int nodeIdx = 0; nodeIdx < nodes.getLength(); nodeIdx++) {
               Node node = nodes.item(nodeIdx);
               if (node instanceof Element) {
                   String key = node.getLocalName().trim();
                   String value = node.getFirstChild().getNodeValue().trim();

                   if (key.equals(RECORD_TYPE)) {
                       recordType = value;
                       break;
                   }
               }
           }
       }
       return recordType;
   }


    /**
     * Parse an annotation for DidRefSource data
     */
    private DidRefSource parseAnnotationForRef(XmlSchemaAnnotation annotation) {
        DidRefSource refSource = null;

        boolean applyKeyFields = false;
        String refType = null;

        XmlSchemaAppInfo appInfo = getAppInfo(annotation);
        if (appInfo != null) {
            // get applyKeyFields and refType from appInfo
            NodeList nodes = appInfo.getMarkup();
            for (int nodeIdx = 0; nodeIdx < nodes.getLength(); nodeIdx++) {
                Node node = nodes.item(nodeIdx);
                if (node instanceof Element) {

                    String key = node.getLocalName().trim();
                    String value = node.getFirstChild().getNodeValue().trim();

                    if (key.equals(APPLY_KEY_FIELDS)) {
                        if (value.equals("true")) {
                            applyKeyFields = true;
                        }
                    } else if (key.equals(REF_TYPE)) {
                        refType = value;
                    }
                }
            }
            if (applyKeyFields && refType != null) {
                refSource = new DidRefSource();
                refSource.setEntityType(refType);
            }
        }

        return refSource;
    }
}
