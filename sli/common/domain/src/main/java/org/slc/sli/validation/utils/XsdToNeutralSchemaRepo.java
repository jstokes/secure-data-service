package org.slc.sli.validation.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;

import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaAppInfo;
import org.apache.ws.commons.schema.XmlSchemaAttribute;
import org.apache.ws.commons.schema.XmlSchemaChoice;
import org.apache.ws.commons.schema.XmlSchemaCollection;
import org.apache.ws.commons.schema.XmlSchemaComplexContentExtension;
import org.apache.ws.commons.schema.XmlSchemaComplexType;
import org.apache.ws.commons.schema.XmlSchemaContent;
import org.apache.ws.commons.schema.XmlSchemaDocumentation;
import org.apache.ws.commons.schema.XmlSchemaElement;
import org.apache.ws.commons.schema.XmlSchemaEnumerationFacet;
import org.apache.ws.commons.schema.XmlSchemaFacet;
import org.apache.ws.commons.schema.XmlSchemaInclude;
import org.apache.ws.commons.schema.XmlSchemaObject;
import org.apache.ws.commons.schema.XmlSchemaObjectCollection;
import org.apache.ws.commons.schema.XmlSchemaParticle;
import org.apache.ws.commons.schema.XmlSchemaSequence;
import org.apache.ws.commons.schema.XmlSchemaSimpleContentExtension;
import org.apache.ws.commons.schema.XmlSchemaSimpleType;
import org.apache.ws.commons.schema.XmlSchemaSimpleTypeList;
import org.apache.ws.commons.schema.XmlSchemaSimpleTypeRestriction;
import org.apache.ws.commons.schema.XmlSchemaType;
import org.apache.ws.commons.schema.XmlSchemaUse;
import org.apache.ws.commons.schema.constants.Constants.BlockConstants;
import org.apache.ws.commons.schema.resolver.URIResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;

import org.slc.sli.validation.NeutralSchemaType;
import org.slc.sli.validation.SchemaFactory;
import org.slc.sli.validation.SchemaRepository;
import org.slc.sli.validation.schema.AppInfo;
import org.slc.sli.validation.schema.Documentation;
import org.slc.sli.validation.schema.ListSchema;
import org.slc.sli.validation.schema.NeutralSchema;
import org.slc.sli.validation.schema.TokenSchema;

/**
 * Generation tool used to convert XSD to SLI Neutral Schema.
 * This class leverages the prior art/work by Ryan Farris to convert XSD to Avro style schemas.
 * 
 * @author Aaron Saarela <asaarela@wgen.net>
 * @author Ryan Farris <rfarris@wgen.net>
 * @author Robert Bloh <rbloh@wgen.net>
 * 
 */
@Component
public class XsdToNeutralSchemaRepo implements SchemaRepository, ApplicationContextAware {
    
    // Logging
    private static final Logger LOG = LoggerFactory.getLogger(XsdToNeutralSchemaRepo.class);
    
    // Constants
    public static final String DEFAULT_INPUT_XSD_PATH = "xsd";
    public static final String XSD = "xsd";
    
    // Attributes
    private final String xsdPath;
    private final SchemaFactory schemaFactory;
    
    private Map<String, NeutralSchema> schemas = new HashMap<String, NeutralSchema>();
    private Map<String, NeutralSchema> elementSchemas = new HashMap<String, NeutralSchema>();
    
    private ResourceLoader resourceLoader;
    
    static final XmlSchemaUse REQUIRED_USE = new XmlSchemaUse(BlockConstants.REQUIRED);
    
    @Autowired
    public XsdToNeutralSchemaRepo(@Value("classpath:sliXsd") String xsdPath, SchemaFactory schemaFactory)
            throws IOException {
        this.xsdPath = xsdPath;
        this.schemaFactory = schemaFactory;
    }
    
    @Override
    public NeutralSchema getSchema(String type) {
        NeutralSchema ns = schemas.get(type);
        if (ns == null) {
            ns = elementSchemas.get(type);
        }
        return ns;
    }
    
    public List<NeutralSchema> getSchemas() {
        ArrayList<NeutralSchema> allSchemas = new ArrayList<NeutralSchema>(schemas.values());
        allSchemas.addAll(elementSchemas.values());
        return allSchemas;
    }
    
    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        try {
            resourceLoader = appContext;
            generateSchemas(appContext.getResources(getXsdPath() + "/*.xsd"));
        } catch (IOException e) {
            LOG.error("Unable to load schemas", e);
        }
    }
    
    private String getXsdPath() {
        return xsdPath;
    }
    
    private SchemaFactory getSchemaFactory() {
        return schemaFactory;
    }
    
    protected void generateSchemas(Resource[] schemaResources) throws IOException {
        
        LOG.info("Starting XSD -> NeutralSchema Generator...");
        LOG.info("Using XML Schema Directory Path: " + getXsdPath());
        
        // Scan XML Schemas on path
        List<XmlSchema> xmlSchemas = parseXmlSchemas(schemaResources, XSD);
        
        // Iterate XML Schemas
        for (XmlSchema schema : xmlSchemas) {
            loadSchema(schema);
        }
        
        LOG.info("Statistics:");
        LOG.info("Xml Total Schema Files Parsed: " + xmlSchemas.size());
        LOG.info("Xml Total Schema Count: " + schemas.size());
        
        LOG.info("Finished.");
    }
    
    void loadSchema(XmlSchema schema) {
        XmlSchemaObjectCollection schemaItems = schema.getItems();
        
        // Iterate XML Schema items
        for (int i = 0; i < schemaItems.getCount(); i++) {
            XmlSchemaObject schemaObject = schemaItems.getItem(i);
            
            NeutralSchema neutralSchema;
            if (schemaObject instanceof XmlSchemaType) {
                neutralSchema = parse((XmlSchemaType) schemaObject, schema);
            } else if (schemaObject instanceof XmlSchemaElement) {
                neutralSchema = parseElement((XmlSchemaElement) schemaObject, schema);
            } else if (schemaObject instanceof XmlSchemaInclude) {
                continue; // nothing to do for includes
            } else {
                throw new RuntimeException("Unhandled XmlSchemaObject: " + schemaObject.getClass().getCanonicalName());
            }
            schemas.put(neutralSchema.getType(), neutralSchema);
        }
    }
    
    private List<XmlSchema> parseXmlSchemas(Resource[] schemaResources, final String schemaRepresentation) {
        List<XmlSchema> xmlSchemas = new ArrayList<XmlSchema>();
        for (Resource schemaResource : schemaResources) {
            try {
                XmlSchema schema = parseXmlSchema(schemaResource.getInputStream());
                xmlSchemas.add(schema);
            } catch (IOException e) {
                throw new RuntimeException("Exception occurred loading schema", e);
            }
        }
        
        return xmlSchemas;
    }
    
    private XmlSchema parseXmlSchema(final InputStream is) {
        try {
            XmlSchemaCollection schemaCollection = new XmlSchemaCollection();
            schemaCollection.setSchemaResolver(new URIResolver() {
                @Override
                public InputSource resolveEntity(String targetNamespace, String schemaLocation, String baseUri) {
                    if (resourceLoader != null) {
                        Resource resource = resourceLoader.getResource(getXsdPath() + "/" + schemaLocation);
                        if (resource.exists()) {
                            try {
                                return new InputSource(resource.getInputStream());
                            } catch (IOException e) {
                                throw new RuntimeException("Exception occurred", e);
                            }
                        }
                    }
                    return new InputSource(Thread.currentThread().getContextClassLoader()
                            .getResourceAsStream(getXsdPath() + "/" + schemaLocation));
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
    
    private NeutralSchema parse(XmlSchemaType type, XmlSchema schema) {
        return parse(type, type.getName(), schema);
    }
    
    private NeutralSchema parse(XmlSchemaType type, String name, XmlSchema schema) {
        
        if (type instanceof XmlSchemaComplexType) {
            NeutralSchema complexSchema = getSchemaFactory().createSchema(name);
            return parseComplexType((XmlSchemaComplexType) type, complexSchema, schema);
            
        } else if (type instanceof XmlSchemaSimpleType) {
            return parseSimpleType((XmlSchemaSimpleType) type, schema, name);
            
        } else {
            throw new RuntimeException("Unsupported schema type: " + type.getClass().getCanonicalName());
        }
    }
    
    private NeutralSchema parseSimpleType(XmlSchemaSimpleType schemaSimpleType, XmlSchema schema, String name) {
        
        NeutralSchema ns = recursiveParseSimpleType(schemaSimpleType, schema);
        if (schemaSimpleType.getName() == null) {
            // Type defined in-line. Need to capture it in the element schema set.
            int i = 1;
            NeutralSchema existing = elementSchemas.get(name + i);
            while (existing != null && !schemasEqual(ns, existing)) {
                i++;
                existing = schemas.get(name + i);
            }
            ns.setType(name + i);
            elementSchemas.put(name + i, ns);
        }
        return ns;
    }
    
    private NeutralSchema recursiveParseSimpleType(XmlSchemaSimpleType schemaSimpleType, XmlSchema schema) {
        NeutralSchema simpleSchema = null;
        
        String simpleTypeName = schemaSimpleType.getName();
        
        if (NeutralSchemaType.isPrimitive(schemaSimpleType.getQName())) {
            simpleSchema = getSchemaFactory().createSchema(schemaSimpleType.getQName());
            
        } else if (NeutralSchemaType.exists(schemaSimpleType.getBaseSchemaTypeName())) {
            
            if (NeutralSchemaType.isPrimitive(schemaSimpleType.getBaseSchemaTypeName())) {
                simpleSchema = getSchemaFactory().createSchema(schemaSimpleType.getBaseSchemaTypeName());
                
            } else {
                XmlSchemaSimpleType simpleBaseType = getSimpleBaseType(schemaSimpleType.getBaseSchemaTypeName(), schema);
                if (simpleBaseType != null) {
                    
                    if (simpleTypeName == null) {
                        simpleTypeName = simpleBaseType.getName();
                    }
                    simpleSchema = getSchemaFactory().createSchema(simpleTypeName);
                }
            }
            
        } else if (schemaSimpleType.getContent() != null
                && schemaSimpleType.getContent() instanceof XmlSchemaSimpleTypeList) {
            
            ListSchema listSchema = (ListSchema) getSchemaFactory().createSchema("list");
            
            XmlSchemaSimpleTypeList content = (XmlSchemaSimpleTypeList) schemaSimpleType.getContent();
            NeutralSchema listContentSchema = null;
            
            if (content.getItemType() != null) {
                listContentSchema = parseSimpleType(content.getItemType(), schema, null);
                
            } else {
                QName itemTypeName = content.getItemTypeName();
                listContentSchema = getSchemaFactory().createSchema(itemTypeName.getLocalPart());
            }
            listSchema.getList().add(listContentSchema);
            return listSchema;
            
        } else if (getSimpleContentTypeName(schemaSimpleType) != null) {
            
            if (NeutralSchemaType.isPrimitive(getSimpleContentTypeName(schemaSimpleType))) {
                simpleSchema = getSchemaFactory().createSchema(getSimpleContentTypeName(schemaSimpleType));
                
            } else {
                
                XmlSchemaSimpleType simpleBaseType = getSimpleBaseType(getSimpleContentTypeName(schemaSimpleType),
                        schema);
                simpleSchema = recursiveParseSimpleType(simpleBaseType, schema);
                
            }
        }
        
        if (simpleSchema != null && schemaSimpleType.getContent() != null) {
            
            if (schemaSimpleType.getContent() instanceof XmlSchemaSimpleTypeRestriction) {
                
                XmlSchemaSimpleTypeRestriction simpleRestrictedContent = (XmlSchemaSimpleTypeRestriction) schemaSimpleType
                        .getContent();
                XmlSchemaObjectCollection facets = simpleRestrictedContent.getFacets();
                List<String> tokens = new ArrayList<String>();
                for (int i = 0; i < facets.getCount(); i++) {
                    XmlSchemaObject facetObject = facets.getItem(i);
                    
                    if (facetObject instanceof XmlSchemaEnumerationFacet) {
                        XmlSchemaEnumerationFacet enumerationFacet = (XmlSchemaEnumerationFacet) facetObject;
                        tokens.add(enumerationFacet.getValue().toString());
                    } else if (facetObject instanceof XmlSchemaFacet) {
                        XmlSchemaFacet facet = (XmlSchemaFacet) facetObject;
                        String facetPropertyName = NeutralSchemaType.lookupPropertyName(facet);
                        simpleSchema.getProperties().put(facetPropertyName, facet.getValue().toString());
                    }
                }
                
                if (tokens.size() > 0) {
                    // Token Schema
                    simpleSchema.getProperties().put(TokenSchema.TOKENS, tokens);
                }
            }
        }
        
        parseAnnotations(simpleSchema, schemaSimpleType);
        
        if ((simpleSchema != null) && (simpleTypeName != null)) {
            simpleSchema.setType(simpleTypeName);
            
        }
        
        return simpleSchema;
    }
    
    private void parseAnnotations(NeutralSchema neutralSchema, XmlSchemaType schemaType) {
        
        if (neutralSchema == null || schemaType == null || schemaType.getAnnotation() == null) {
            return;
        }
        
        parseDocumentation(neutralSchema, schemaType);
        parseAppInfo(neutralSchema, schemaType);
    }
    
    private void parseDocumentation(NeutralSchema neutralSchema, XmlSchemaType schemaType) {
        XmlSchemaObjectCollection annotations = schemaType.getAnnotation().getItems();
        for (int annotationIdx = 0; annotationIdx < annotations.getCount(); ++annotationIdx) {
            
            XmlSchemaObject annotation = annotations.getItem(annotationIdx);
            if (annotation instanceof XmlSchemaDocumentation) {
                XmlSchemaDocumentation docs = (XmlSchemaDocumentation) annotation;
                neutralSchema.addAnnotation(new Documentation(docs.getMarkup()));
            }
        }
    }
    
    private void parseAppInfo(NeutralSchema neutralSchema, XmlSchemaType schemaType) {
        
        XmlSchemaObjectCollection annotations = schemaType.getAnnotation().getItems();
        for (int annotationIdx = 0; annotationIdx < annotations.getCount(); ++annotationIdx) {
            
            XmlSchemaObject annotation = annotations.getItem(annotationIdx);
            if (annotation instanceof XmlSchemaAppInfo) {
                XmlSchemaAppInfo info = (XmlSchemaAppInfo) annotation;
                neutralSchema.addAnnotation(new AppInfo(info.getMarkup()));
            }
        }
    }
    
    private XmlSchemaSimpleType getSimpleBaseType(QName simpleBaseTypeName, XmlSchema schema) {
        XmlSchemaSimpleType simpleBaseType = null;
        if (simpleBaseTypeName != null) {
            XmlSchemaType baseType = schema.getTypeByName(simpleBaseTypeName);
            if (baseType != null) {
                if (baseType instanceof XmlSchemaSimpleType) {
                    simpleBaseType = (XmlSchemaSimpleType) baseType;
                } else {
                    throw new RuntimeException("Unsupported simple base type: "
                            + baseType.getClass().getCanonicalName());
                }
            } else {
                throw new RuntimeException("Schema simple base type not found: " + simpleBaseTypeName);
            }
        }
        
        return simpleBaseType;
    }
    
    private QName getSimpleContentTypeName(XmlSchemaSimpleType schemaSimpleType) {
        QName simpleContentTypeName = null;
        if (schemaSimpleType.getContent() != null
                && schemaSimpleType.getContent() instanceof XmlSchemaSimpleTypeRestriction) {
            XmlSchemaSimpleTypeRestriction simpleContent = (XmlSchemaSimpleTypeRestriction) schemaSimpleType
                    .getContent();
            simpleContentTypeName = simpleContent.getBaseTypeName();
        } else {
            throw new RuntimeException("Unsupported simple content model: "
                    + schemaSimpleType.getContent().getClass().getCanonicalName());
        }
        return simpleContentTypeName;
    }
    
    private NeutralSchema parseComplexType(XmlSchemaComplexType schemaComplexType, NeutralSchema complexSchema,
            XmlSchema schema) {
        
        if (schemaComplexType.getContentModel() != null && schemaComplexType.getContentModel().getContent() != null) {
            XmlSchemaContent content = schemaComplexType.getContentModel().getContent();
            if (content instanceof XmlSchemaComplexContentExtension) {
                XmlSchemaComplexContentExtension schemaComplexContent = (XmlSchemaComplexContentExtension) content;
                XmlSchemaComplexType complexBaseType = getComplexBaseType(schemaComplexContent, schema);
                if (complexBaseType != null) {
                    complexSchema = parseComplexType(complexBaseType, complexSchema, schema);
                }
                this.parseFields(schemaComplexContent, complexSchema, schema);
                
            } else if (content instanceof XmlSchemaSimpleContentExtension) {
                QName baseTypeName = ((XmlSchemaSimpleContentExtension) content).getBaseTypeName();
                NeutralSchema simpleContentSchema = schemaFactory.createSchema(baseTypeName);
                complexSchema.addField(complexSchema.getType(), simpleContentSchema);
                
                parseAttributes(((XmlSchemaSimpleContentExtension) content).getAttributes(), complexSchema, schema);
            }
        }
        
        // Annotations are inherited by ComplexType fields so we need to parse these first.
        parseAnnotations(complexSchema, schemaComplexType);
        
        this.parseFields(schemaComplexType, complexSchema, schema);
        
        return complexSchema;
    }
    
    private XmlSchemaComplexType getComplexBaseType(XmlSchemaComplexContentExtension schemaComplexContent,
            XmlSchema schema) {
        XmlSchemaComplexType complexBaseType = null;
        QName baseTypeName = schemaComplexContent.getBaseTypeName();
        XmlSchemaType baseType = schema.getTypeByName(baseTypeName);
        if (baseType != null) {
            if (baseType instanceof XmlSchemaComplexType) {
                complexBaseType = (XmlSchemaComplexType) baseType;
            } else {
                throw new RuntimeException("Unsupported complex base type: " + baseType.getClass().getCanonicalName());
            }
        } else {
            throw new RuntimeException("Schema complex base type not found: " + baseTypeName);
        }
        return complexBaseType;
    }
    
    private void parseFields(XmlSchemaComplexType schemaComplexType, NeutralSchema complexSchema, XmlSchema schema) {
        parseAttributes(schemaComplexType.getAttributes(), complexSchema, schema);
        parseParticle(schemaComplexType.getParticle(), complexSchema, schema);
    }
    
    private void parseFields(XmlSchemaComplexContentExtension schemaComplexContentExtension,
            NeutralSchema complexSchema, XmlSchema schema) {
        parseAttributes(schemaComplexContentExtension.getAttributes(), complexSchema, schema);
        parseParticle(schemaComplexContentExtension.getParticle(), complexSchema, schema);
    }
    
    private void parseAttributes(XmlSchemaObjectCollection attributes, NeutralSchema complexSchema, XmlSchema schema) {
        
        if (attributes != null) {
            for (int i = 0; i < attributes.getCount(); i++) {
                XmlSchemaAttribute attribute = (XmlSchemaAttribute) attributes.getItem(i);
                QName attributeTypeName = attribute.getSchemaTypeName();
                
                XmlSchemaType attributeSchemaType = attribute.getSchemaType();
                
                if (attribute.getName() != null) {
                    
                    String attributeName = attribute.getName();
                    
                    // Derive Attribute Schema
                    NeutralSchema attributeSchema = null;
                    if (attributeSchemaType != null) {
                        attributeSchema = parse(attributeSchemaType, schema);
                    } else if (attributeTypeName != null) {
                        attributeSchema = getSchemaFactory().createSchema(attributeTypeName);
                    }
                    
                    // Update Neutral Schema Field
                    if (attributeSchema != null) {
                        
                        // Optional Attributes
                        if (attribute.getUse().equals(REQUIRED_USE)) {
                            
                            AppInfo info = attributeSchema.getAppInfo();
                            
                            if (info == null) {
                                info = new AppInfo(null);
                            }
                            
                            info.put(REQUIRED_USE.getValue(), "true");
                            attributeSchema.addAnnotation(info);
                        }
                        
                        complexSchema.addField(attributeName, attributeSchema);
                    }
                }
            }
        }
    }
    
    private NeutralSchema parseElement(XmlSchemaElement element, XmlSchema schema) {
        
        QName elementTypeName = element.getSchemaTypeName();
        
        // Derive Element Schema
        XmlSchemaType elementSchemaType = element.getSchemaType();
        
        // Element annotations override type annotations.
        if (element.getAnnotation() != null) {
            elementSchemaType.setAnnotation(element.getAnnotation());
        }
        
        NeutralSchema elementSchema = null;
        if (elementSchemaType != null) {
            if (elementSchemaType.getName() != null) {
                elementSchema = this.parse(elementSchemaType, schema);
            } else {
                elementSchema = this.parse(elementSchemaType, element.getName(), schema);
            }
        } else if (elementTypeName != null) {
            elementSchema = getSchemaFactory().createSchema(elementTypeName);
        }
        
        if (elementSchema != null) {
            
            // List Schema
            if (element.getMaxOccurs() > 1) {
                ListSchema listSchema = (ListSchema) getSchemaFactory().createSchema("list");
                listSchema.getList().add(elementSchema);
                elementSchema = listSchema;
            }
        }
        
        return elementSchema;
    }
    
    private void parseParticle(XmlSchemaParticle particle, NeutralSchema complexSchema, XmlSchema schema) {
        
        if (particle != null) {
            if (particle instanceof XmlSchemaElement) {
                XmlSchemaElement element = (XmlSchemaElement) particle;
                
                NeutralSchema elementSchema = parseElement(element, schema);
                
                String elementName = element.getName();
                
                // Required Elements
                if (!element.isNillable() || (element.getMinOccurs() > 0)) {
                    AppInfo info = elementSchema.getAppInfo();
                    if (info == null) {
                        info = new AppInfo(null);
                    }
                    info.put(REQUIRED_USE.getValue(), "true");
                    elementSchema.addAnnotation(info);
                }
                
                // Update Neutral Schema Field
                complexSchema.addField(elementName, elementSchema);
                
            } else if (particle instanceof XmlSchemaSequence) {
                XmlSchemaSequence schemaSequence = (XmlSchemaSequence) particle;
                for (int i = 0; i < schemaSequence.getItems().getCount(); i++) {
                    XmlSchemaObject item = schemaSequence.getItems().getItem(i);
                    if (item instanceof XmlSchemaParticle) {
                        parseParticle((XmlSchemaParticle) item, complexSchema, schema);
                    } else {
                        throw new RuntimeException("Unsupported XmlSchemaSequence item: "
                                + item.getClass().getCanonicalName());
                    }
                }
            } else if (particle instanceof XmlSchemaChoice) {
                // TODO implement choice (or remove them from the XSDs)
                // throw new RuntimeException("Unhandled XmlSchemaChoice element: " + particle + " "
                // + complexSchema.getType());
                LOG.error("Unhandled XmlSchemaChoice element: " + particle + " " + complexSchema.getType());
                
            } else {
                throw new RuntimeException("Unsupported XmlSchemaParticle item: "
                        + particle.getClass().getCanonicalName());
            }
        }
    }
    
    private static boolean schemasEqual(NeutralSchema ns1, NeutralSchema ns2) {
        if (ns1.getValidatorClass().equals(ns2.getValidatorClass()) && ns1.getVersion().equals(ns2.getVersion())
                && ns1.getFields().size() == ns2.getFields().size()) {
            for (Entry<String, NeutralSchema> entry : ns1.getFields().entrySet()) {
                if (!ns2.getFields().containsKey(entry.getKey())) {
                    return false;
                }
                if (!schemasEqual(entry.getValue(), ns2.getFields().get(entry.getKey()))) {
                    return false;
                }
            }
            for (Entry<String, Object> entry : ns1.getProperties().entrySet()) {
                if (!ns2.getProperties().containsKey(entry.getKey())) {
                    return false;
                }
                if (!entry.getValue().getClass().equals(ns2.getProperties().get(entry.getKey()).getClass())) {
                    return false;
                }
                if (entry.getValue() instanceof List) {
                    List<?> list1 = (List<?>) entry.getValue();
                    List<?> list2 = (List<?>) ns2.getProperties().get(entry.getKey());
                    if (!list1.containsAll(list2)) {
                        return false;
                    }
                } else {
                    if (!entry.getValue().equals(ns2.getProperties().get(entry.getKey()))) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
