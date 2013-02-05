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
package org.slc.sli.ingestion.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.EventReaderDelegate;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import org.slc.sli.ingestion.parser.impl.XsdEdfiType;

/**
 * A reader delegate that will intercept an XML Validator's calls to nextEvent() and build the
 * document into a Map of Maps data structure.
 *
 * Additionally, the class implements ErrorHandler so
 * that the parsing of a specific entity can be aware of validation errors.
 *
 * @author dduran
 *
 */
@Component
public class EdfiEventReaderDelegate extends EventReaderDelegate implements ErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(EdfiEventReaderDelegate.class);

    @Autowired
    private TypeProvider typeProvider;

    private String interchange;

    Stack<Pair<EdfiType, Map<String, Object>>> complexTypeStack = new Stack<Pair<EdfiType, Map<String, Object>>>();
    String currentEntityName = null;
    boolean currentEntityValid = false;

    @Override
    public XMLEvent nextEvent() throws XMLStreamException {
        XMLEvent event = super.nextEvent();

        parseXmlEvent(event);

        return event;
    }

    private void parseXmlEvent(XMLEvent event) {
        try {
            String eventName = extractTagName(event);
            if (interchange != null) {
                parseInterchangeEvent(event, eventName);

            } else if (eventName.startsWith("Interchange")) {
                interchange = eventName;
            }
        } catch (XMLStreamException e) {
            LOG.error("Error parsing.", e);
        }
    }

    private void parseInterchangeEvent(XMLEvent event, String eventName) throws XMLStreamException {

        if (complexTypeStack.isEmpty() && event.isStartElement()) {
            initCurrentEntity(eventName);
        }

        if (!complexTypeStack.isEmpty()) {
            if (currentEntityValid) {
                parseEntityEvent(event, eventName);

            } else if (eventName.equals(currentEntityName) && event.isEndElement()) {
                endOfInvalidEntity();
            }
        }
    }

    private void initCurrentEntity(String eventName) {
        String xsdType = typeProvider.getTypeFromInterchange(interchange, eventName);
        complexTypeStack.push(createElementEntry(new XsdEdfiType(eventName, xsdType)));
        currentEntityName = eventName;
        currentEntityValid = true;
    }

    private void parseEntityEvent(XMLEvent e, String eventName) throws XMLStreamException {

        if (e.isStartElement()) {
            parseStartElement(e.asStartElement(), eventName);

        } else if (e.isCharacters()) {
            parseCharacters(e.asCharacters());

        } else if (e.isEndElement() && eventName.equals(complexTypeStack.peek().getLeft().getName())) {
            parseEndElement();
        }
    }

    private void endOfInvalidEntity() {
        LOG.info("Entity had validation problems: {}", currentEntityName);
        complexTypeStack.removeAllElements();
        currentEntityName = null;
    }

    private void parseStartElement(StartElement e, String eventName) {
        if (!currentEntityName.equals(eventName)) {
            // don't process for root entity element - we already pushed it in initCurrentEntity
            newEventToStack(eventName);
        }

        parseEventAttributes(e);
    }

    private void newEventToStack(String eventName) {
        EdfiType typeMeta = typeProvider.getTypeFromParentType(complexTypeStack.peek().getLeft().getType(), eventName);

        Pair<EdfiType, Map<String, Object>> subElement = createElementEntry(typeMeta);

        Object mapValue = subElement.getRight();
        if (typeMeta.isList() && complexTypeStack.peek().getRight().get(eventName) == null) {
            mapValue = new ArrayList<Object>(Arrays.asList(mapValue));
        }

        complexTypeStack.peek().getRight().put(eventName, mapValue);
        complexTypeStack.push(subElement);
    }

    @SuppressWarnings("unchecked")
    private void parseEventAttributes(StartElement e) {
        Iterator<Attribute> it = e.getAttributes();
        while (it.hasNext()) {
            Attribute a = it.next();
            complexTypeStack.peek().getRight().put(a.getName().getLocalPart(), a.getValue());
        }
    }

    private void parseCharacters(Characters characters) {
        if (!characters.isIgnorableWhiteSpace() && !characters.isWhiteSpace()) {
            String text = characters.getData();
            Object convertedValue = typeProvider.convertType(complexTypeStack.peek().getLeft().getType(), text);
            complexTypeStack.peek().getRight().put("_value", convertedValue);
        }
    }

    private void parseEndElement() {
        if (complexTypeStack.size() > 1) {
            complexTypeStack.pop();
        } else if (complexTypeStack.size() == 1) {
            // completed parsing an entity
            Map<String, Object> entity = complexTypeStack.pop().getRight();

            LOG.info("Parsed entity: {} - {}", currentEntityName, entity);

            currentEntityName = null;
        }
    }

    private Pair<EdfiType, Map<String, Object>> createElementEntry(EdfiType edfiType) {
        return new ImmutablePair<EdfiType, Map<String, Object>>(edfiType, new InnerMap());
    }

    private static String extractTagName(XMLEvent e) {
        String result = "";
        if (e.isEndElement()) {
            result = e.asEndElement().getName().getLocalPart();
        } else if (e.isStartElement()) {
            result = e.asStartElement().getName().getLocalPart();
        }
        return result;
    }

    @Override
    public void warning(SAXParseException exception) throws SAXException {
        LOG.warn("Warning: ", exception);
        currentEntityValid = false;
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        LOG.error("Error: ", exception);
        currentEntityValid = false;
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        LOG.error("FatalError: ", exception);
        currentEntityValid = false;
    }

    @SuppressWarnings({ "unchecked", "serial" })
    private static class InnerMap extends HashMap<String, Object> {
        @Override
        public Object put(String key, Object value) {
            Object result;
            Object stored = this.get(key);
            if (stored != null) {
                if (List.class.isAssignableFrom(stored.getClass())) {
                    List<Object> storage = (List<Object>) stored;
                    storage.add(value);
                    result = storage;
                } else {
                    result = super.put(key, new ArrayList<Object>(Arrays.asList(stored, value)));
                }
            } else {
                result = super.put(key, value);
            }
            return result;
        }
    }
}
