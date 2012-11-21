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

package org.slc.sli.modeling.xmigen;

import org.apache.ws.commons.schema.XmlSchemaAppInfo;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slc.sli.modeling.psm.helpers.SliMongoConstants;
import org.slc.sli.modeling.psm.helpers.SliUmlConstants;
import org.slc.sli.modeling.uml.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** 
* Xsd2UmlPluginForSLI Tester. 
* 
* @author <Authors name> 
* @since <pre>Sep 12, 2012</pre> 
* @version 1.0 
*/ 
public class Xsd2UmlPluginForSLITest { 

    private Xsd2UmlPluginForSLI pluginForSLI;
    private TagDefinition tagDefinition;

@Before
public void before() throws Exception {
    pluginForSLI = new Xsd2UmlPluginForSLI();
}

/** 
* 
* Method: declareTagDefinitions(final Xsd2UmlPluginHost host) 
* 
*/ 
@Test
public void testDeclareTagDefinitions() throws Exception { 
    Xsd2UmlPluginHost host = mock(Xsd2UmlPluginHost.class);
    when(host.ensureTagDefinitionId(anyString())).thenReturn(Identifier.random());
    List<TagDefinition> tagDefinitionList = pluginForSLI.declareTagDefinitions(host);
    assertNotNull(tagDefinitionList);
    assertEquals(12 , tagDefinitionList.size());
} 

/** 
* 
* Method: getAssociationEndTypeName(final ClassType classType, final Attribute attribute, final Xsd2UmlPluginHost host) 
* 
*/ 
@Test
public void testGetAssociationEndTypeName() throws Exception { 
    ClassType classType = mock(ClassType.class);
    Attribute attribute = mock(Attribute.class);
    Xsd2UmlPluginHost host = mock(Xsd2UmlPluginHost.class);
    TaggedValue taggedValue = mock(TaggedValue.class);
    TagDefinition tagDefinition = mock(TagDefinition.class);
    Identifier id = Identifier.random();
    List<TaggedValue> taggedValueList = new ArrayList<TaggedValue>();
    taggedValueList.add(taggedValue);

    when(attribute.getTaggedValues()).thenReturn(taggedValueList);
    when(taggedValue.getTagDefinition()).thenReturn(id);
    when(host.getTagDefinition(any(Identifier.class))).thenReturn(tagDefinition);
    when(tagDefinition.getName()).thenReturn(SliUmlConstants.TAGDEF_REFERENCE);
    when(taggedValue.getValue()).thenReturn("test");

    String name = pluginForSLI.getAssociationEndTypeName(classType,attribute,host);
    assertNotNull(name);
} 

/** 
* 
* Method: isAssociationEnd(final ClassType classType, final Attribute attribute, final Xsd2UmlPluginHost host) 
* 
*/ 
@Test
public void testIsAssociationEnd() throws Exception { 
    ClassType classType = mock(ClassType.class);
    Attribute attribute = mock(Attribute.class);
    Xsd2UmlPluginHost host = mock(Xsd2UmlPluginHost.class);
    TaggedValue taggedValue = mock(TaggedValue.class);
    TagDefinition tagDefinition = mock(TagDefinition.class);
    Identifier id = Identifier.random();
    List<TaggedValue> taggedValueList = new ArrayList<TaggedValue>();
    taggedValueList.add(taggedValue);

    when(attribute.getTaggedValues()).thenReturn(taggedValueList);
    when(taggedValue.getTagDefinition()).thenReturn(id);
    when(host.getTagDefinition(any(Identifier.class))).thenReturn(tagDefinition);
    when(tagDefinition.getName()).thenReturn(SliUmlConstants.TAGDEF_REFERENCE);
    when(taggedValue.getValue()).thenReturn("test");
    assertTrue(pluginForSLI.isAssociationEnd(classType,attribute,host));
}
    @Test
    public void testIsAssociationEndFalse() throws Exception {
        ClassType classType = mock(ClassType.class);
        Attribute attribute = mock(Attribute.class);
        Xsd2UmlPluginHost host = mock(Xsd2UmlPluginHost.class);
        TaggedValue taggedValue = mock(TaggedValue.class);
        TagDefinition tagDefinition = mock(TagDefinition.class);
        Identifier id = Identifier.random();
        List<TaggedValue> taggedValueList = new ArrayList<TaggedValue>();
        taggedValueList.add(taggedValue);

        when(attribute.getTaggedValues()).thenReturn(taggedValueList);
        when(taggedValue.getTagDefinition()).thenReturn(id);
        when(host.getTagDefinition(any(Identifier.class))).thenReturn(tagDefinition);
        when(tagDefinition.getName()).thenReturn(SliUmlConstants.TAGDEF_REST_RESOURCE);
        when(taggedValue.getValue()).thenReturn("test");
        assertFalse(pluginForSLI.isAssociationEnd(classType,attribute,host));
    }

    /**
* 
* Method: nameAssociation(final AssociationEnd lhs, final AssociationEnd rhs, final Xsd2UmlPluginHost host) 
* 
*/ 
@Test
public void testNameAssociation() throws Exception { 
    AssociationEnd associationEnd = mock(AssociationEnd.class);
    Xsd2UmlPluginHost host = mock(Xsd2UmlPluginHost.class);
    assertTrue(pluginForSLI.nameAssociation(associationEnd,associationEnd,host).isEmpty());
} 

/** 
* 
* Method: tagsFromAppInfo(final XmlSchemaAppInfo appInfo, final Xsd2UmlPluginHost host) 
* 
*/ 
@Test
public void testTagsFromAppInfo() throws Exception {
    XmlSchemaAppInfo xmlSchemaAppInfo = mock(XmlSchemaAppInfo.class);
    Xsd2UmlPluginHost host = mock(Xsd2UmlPluginHost.class);
    NodeList nodeList = mock(NodeList.class);
    Element node = mock(Element.class);

    when(host.ensureTagDefinitionId(Mockito.anyString())).thenReturn(Identifier.random());
    when(xmlSchemaAppInfo.getMarkup()).thenReturn(nodeList);
    when(nodeList.item(anyInt())).thenReturn(node);
    when(nodeList.getLength()).thenReturn(1);
    when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
    when(node.getNamespaceURI()).thenReturn(SliMongoConstants.NAMESPACE_SLI);
    when(node.getLocalName()).thenReturn("CollectionType");
    when(node.getChildNodes()).thenReturn(nodeList);


    assertNotNull(pluginForSLI.tagsFromAppInfo(xmlSchemaAppInfo,host));
    when(node.getLocalName()).thenReturn("naturalKey");
    assertNotNull(pluginForSLI.tagsFromAppInfo(xmlSchemaAppInfo,host));
    when(node.getLocalName()).thenReturn("applyNaturalKeys");
    assertNotNull(pluginForSLI.tagsFromAppInfo(xmlSchemaAppInfo,host));
    when(node.getLocalName()).thenReturn("PersonallyIdentifiableInfo");
    assertNotNull(pluginForSLI.tagsFromAppInfo(xmlSchemaAppInfo,host));
    when(node.getLocalName()).thenReturn("ReferenceType");
    assertNotNull(pluginForSLI.tagsFromAppInfo(xmlSchemaAppInfo,host));
    when(node.getLocalName()).thenReturn("ReadEnforcement");
    when(node.getTextContent()).thenReturn("READ_RESTRICTED");
    assertNotNull(pluginForSLI.tagsFromAppInfo(xmlSchemaAppInfo,host));
    when(node.getLocalName()).thenReturn("SecuritySphere");
    when(node.getTextContent()).thenReturn("Public");
    assertNotNull(pluginForSLI.tagsFromAppInfo(xmlSchemaAppInfo,host));
    when(node.getLocalName()).thenReturn("RelaxedBlacklist");
    when(node.getTextContent()).thenReturn("true");
    assertNotNull(pluginForSLI.tagsFromAppInfo(xmlSchemaAppInfo,host));
    when(node.getLocalName()).thenReturn("RestrictedForLogging");
    assertNotNull(pluginForSLI.tagsFromAppInfo(xmlSchemaAppInfo,host));
    when(node.getLocalName()).thenReturn("WriteEnforcement");
    when(node.getTextContent()).thenReturn("WRITE_RESTRICTED");
    assertNotNull(pluginForSLI.tagsFromAppInfo(xmlSchemaAppInfo,host));
    when(node.getLocalName()).thenReturn("schemaVersion");
    assertNotNull(pluginForSLI.tagsFromAppInfo(xmlSchemaAppInfo,host));

}





} 
