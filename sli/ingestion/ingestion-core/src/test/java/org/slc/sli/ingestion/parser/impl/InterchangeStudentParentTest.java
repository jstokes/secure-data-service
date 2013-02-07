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
package org.slc.sli.ingestion.parser.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.util.Map;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import org.slc.sli.ingestion.parser.RecordMeta;
import org.slc.sli.ingestion.parser.RecordVisitor;

/**
 *
 * @author dduran
 *
 */
public class InterchangeStudentParentTest {

    public static final Logger LOG = LoggerFactory.getLogger(InterchangeStudentParentTest.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    Resource schemaDir = new ClassPathResource("edfiXsd-SLI");

    @Test
    public void testStudent() throws Throwable {

        Resource schema = new ClassPathResource("edfiXsd-SLI/SLI-Interchange-StudentParent.xsd");
        Resource inputXml = new ClassPathResource("parser/InterchangeStudentParent/Student.xml");
        Resource expectedJson = new ClassPathResource("parser/InterchangeStudentParent/Student.expected.json");

        entityTestHelper(schema, inputXml, expectedJson);
    }

    @Test
    public void testParent() throws Throwable {

        Resource schema = new ClassPathResource("edfiXsd-SLI/SLI-Interchange-StudentParent.xsd");
        Resource inputXml = new ClassPathResource("parser/InterchangeStudentParent/Parent.xml");
        Resource expectedJson = new ClassPathResource("parser/InterchangeStudentParent/Parent.expected.json");

        entityTestHelper(schema, inputXml, expectedJson);
    }

    @Test
    public void testStudentParentAssociation() throws Throwable {

        Resource schema = new ClassPathResource("edfiXsd-SLI/SLI-Interchange-StudentParent.xsd");
        Resource inputXml = new ClassPathResource("parser/InterchangeStudentParent/StudentParentAssociation.xml");
        Resource expectedJson = new ClassPathResource("parser/InterchangeStudentParent/StudentParentAssociation.expected.json");

        entityTestHelper(schema, inputXml, expectedJson);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void entityTestHelper(Resource schema, Resource inputXmlResource, Resource expectedJsonResource)
            throws Throwable {

        XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(inputXmlResource.getInputStream());

        XsdTypeProvider tp = new XsdTypeProvider();
        tp.initEdfiSchema(schemaDir);

        RecordVisitor mockVisitor = Mockito.mock(RecordVisitor.class);
        EdfiRecordParserImpl.parse(reader, schema, tp, mockVisitor);

        ArgumentCaptor<Map> mapArgCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockVisitor, atLeastOnce()).visit(any(RecordMeta.class), mapArgCaptor.capture());
        LOG.info("Visitor invoked {} times.", mapArgCaptor.getAllValues().size());

        LOG.info(objectMapper.writeValueAsString(mapArgCaptor.getValue()));
        Object expected = objectMapper.readValue(expectedJsonResource.getFile(), Map.class);
        assertEquals(expected, mapArgCaptor.getValue());
    }

}
