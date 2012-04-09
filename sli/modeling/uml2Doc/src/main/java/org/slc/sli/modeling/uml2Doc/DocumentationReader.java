package org.slc.sli.modeling.uml2Doc;

import static org.slc.sli.modeling.xml.XMLStreamReaderTools.skipElement;
import static org.slc.sli.modeling.xml.XmlTools.collapseWhitespace;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slc.sli.modeling.uml.Identifier;
import org.slc.sli.modeling.uml.Model;
import org.slc.sli.modeling.uml.Reference;
import org.slc.sli.modeling.uml.ReferenceType;
import org.slc.sli.modeling.xmi.XmiAttributeName;

public final class DocumentationReader {
    
    private static final void closeQuiet(final Closeable closeable) {
        try {
            closeable.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    public static final Documentation<Reference> readInterchange(final String fileName) throws FileNotFoundException {
        final InputStream istream = new BufferedInputStream(new FileInputStream(fileName));
        try {
            return readInterchange(istream);
        } finally {
            closeQuiet(istream);
        }
    }
    
    /**
     * Reads XMI from an {@link InputStream}.
     * 
     * @param stream
     *            The {@link InputStream}.
     * @return The parsed {@link Model}.
     */
    public static final Documentation<Reference> readInterchange(final InputStream stream) {
        final XMLInputFactory factory = XMLInputFactory.newInstance();
        try {
            final XMLStreamReader reader = factory.createXMLStreamReader(stream);
            try {
                final Documentation<Reference> model = readDocument(reader);
                return model;
            } finally {
                reader.close();
            }
        } catch (final XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static final boolean match(final QName name, final XMLStreamReader reader) {
        return name.getLocalPart().equals(reader.getLocalName());
    }
    
    private static final Documentation<Reference> readDocument(final XMLStreamReader reader) throws XMLStreamException {
        assertStartDocument(reader);
        Documentation<Reference> dm = null;
        while (reader.hasNext()) {
            reader.next();
            switch (reader.getEventType()) {
                case XMLStreamConstants.START_ELEMENT: {
                    if (match(DocumentationElements.DOMAINS, reader)) {
                        dm = assertNotNull(readDomains(reader));
                    } else {
                        skipElement(reader);
                    }
                    break;
                }
                case XMLStreamConstants.END_DOCUMENT: {
                    return dm;
                }
                default: {
                    throw new AssertionError(reader.getEventType());
                }
            }
        }
        throw new AssertionError();
    }
    
    private static final Documentation<Reference> readDomains(final XMLStreamReader reader) throws XMLStreamException {
        assertStartElement(reader);
        assertName(DocumentationElements.DOMAINS, reader);
        final List<Domain<Reference>> domains = new LinkedList<Domain<Reference>>();
        boolean done = false;
        while (!done && reader.hasNext()) {
            reader.next();
            switch (reader.getEventType()) {
                case XMLStreamConstants.START_ELEMENT: {
                    if (match(DocumentationElements.DOMAIN, reader)) {
                        domains.add(assertNotNull(readDomain(reader)));
                    } else {
                        skipElement(reader);
                    }
                    break;
                }
                case XMLStreamConstants.END_ELEMENT: {
                    assertName(DocumentationElements.DOMAINS, reader);
                    done = true;
                    break;
                }
                case XMLStreamConstants.CHARACTERS: {
                    // Ignore.
                    break;
                }
                default: {
                    throw new AssertionError(reader.getEventType());
                }
            }
        }
        return new Documentation<Reference>(domains);
    }
    
    private static final Domain<Reference> readDomain(final XMLStreamReader reader) throws XMLStreamException {
        assertStartElement(reader);
        assertName(DocumentationElements.DOMAIN, reader);
        String title = null;
        String description = "";
        final List<Entity<Reference>> entities = new LinkedList<Entity<Reference>>();
        final List<Diagram> diagrams = new LinkedList<Diagram>();
        boolean done = false;
        while (!done && reader.hasNext()) {
            reader.next();
            switch (reader.getEventType()) {
                case XMLStreamConstants.START_ELEMENT: {
                    if (match(DocumentationElements.TITLE, reader)) {
                        title = assertNotNull(readTitle(reader));
                    } else if (match(DocumentationElements.DESCRIPTION, reader)) {
                        description = assertNotNull(readDescription(reader));
                    } else if (match(DocumentationElements.ENTITY, reader)) {
                        entities.add(assertNotNull(readEntity(reader)));
                    } else if (match(DocumentationElements.DIAGRAM, reader)) {
                        diagrams.add(assertNotNull(readDiagram(reader)));
                    } else {
                        throw new AssertionError(reader.getLocalName());
                    }
                    break;
                }
                case XMLStreamConstants.END_ELEMENT: {
                    assertName(DocumentationElements.DOMAIN, reader);
                    done = true;
                    break;
                }
                case XMLStreamConstants.CHARACTERS: {
                    // Ignore.
                    break;
                }
                default: {
                    throw new AssertionError(reader.getEventType());
                }
            }
        }
        return new Domain<Reference>(title, description, entities, diagrams);
    }
    
    private static final Reference readReference(final QName name, final ReferenceType kind,
            final XMLStreamReader reader) throws XMLStreamException {
        assertStartElement(reader);
        assertName(name, reader);
        final Identifier id = getIdRef(reader);
        boolean done = false;
        while (!done && reader.hasNext()) {
            reader.next();
            switch (reader.getEventType()) {
                case XMLStreamConstants.START_ELEMENT: {
                    throw new AssertionError(reader.getLocalName());
                }
                case XMLStreamConstants.END_ELEMENT: {
                    assertName(name, reader);
                    done = true;
                    break;
                }
                case XMLStreamConstants.CHARACTERS: {
                    // Ignore.
                    break;
                }
                default: {
                    throw new AssertionError(reader.getEventType());
                }
            }
        }
        return new Reference(id, kind);
    }
    
    private static final Identifier getIdRef(final XMLStreamReader reader) {
        final String value = reader.getAttributeValue("", XmiAttributeName.IDREF.getLocalName());
        if (value != null) {
            return Identifier.fromString(value);
        } else {
            throw new AssertionError(XmiAttributeName.IDREF.getLocalName());
        }
    }
    
    private static final Entity<Reference> readEntity(final XMLStreamReader reader) throws XMLStreamException {
        assertStartElement(reader);
        assertName(DocumentationElements.ENTITY, reader);
        String title = null;
        Reference type = null;
        final List<Diagram> diagrams = new LinkedList<Diagram>();
        boolean done = false;
        while (!done && reader.hasNext()) {
            reader.next();
            switch (reader.getEventType()) {
                case XMLStreamConstants.START_ELEMENT: {
                    if (match(DocumentationElements.TITLE, reader)) {
                        title = assertNotNull(readTitle(reader));
                    } else if (match(DocumentationElements.CLASS, reader)) {
                        final Reference classRef = readReference(DocumentationElements.CLASS, ReferenceType.CLASS_TYPE,
                                reader);
                        type = assertNotNull(classRef);
                    } else {
                        throw new AssertionError(reader.getLocalName());
                    }
                    break;
                }
                case XMLStreamConstants.END_ELEMENT: {
                    assertName(DocumentationElements.ENTITY, reader);
                    done = true;
                    break;
                }
                case XMLStreamConstants.CHARACTERS: {
                    // Ignore.
                    break;
                }
                default: {
                    throw new AssertionError(reader.getEventType());
                }
            }
        }
        assertNotNull(title);
        validateNotNull(type, "Missing reference for entity with title : " + title);
        return new Entity<Reference>(title, type, diagrams);
    }
    
    private static final Diagram readDiagram(final XMLStreamReader reader) throws XMLStreamException {
        assertStartElement(reader);
        assertName(DocumentationElements.DIAGRAM, reader);
        String title = null;
        String source = null;
        boolean done = false;
        while (!done && reader.hasNext()) {
            reader.next();
            switch (reader.getEventType()) {
                case XMLStreamConstants.START_ELEMENT: {
                    if (match(DocumentationElements.TITLE, reader)) {
                        title = assertNotNull(readTitle(reader));
                    } else if (match(DocumentationElements.SOURCE, reader)) {
                        source = assertNotNull(readSource(reader));
                    } else {
                        throw new AssertionError(reader.getLocalName());
                    }
                    break;
                }
                case XMLStreamConstants.END_ELEMENT: {
                    assertName(DocumentationElements.DIAGRAM, reader);
                    done = true;
                    break;
                }
                case XMLStreamConstants.CHARACTERS: {
                    // Ignore.
                    break;
                }
                default: {
                    throw new AssertionError(reader.getEventType());
                }
            }
        }
        assertNotNull(title);
        validateNotNull(source, "Missing source for diagram with title : " + title);
        return new Diagram(title, source);
    }
    
    private static final String readTitle(final XMLStreamReader reader) throws XMLStreamException {
        return readElementText(DocumentationElements.TITLE, reader);
    }
    
    private static final String readSource(final XMLStreamReader reader) throws XMLStreamException {
        return readElementText(DocumentationElements.SOURCE, reader);
    }
    
    private static final String readDescription(final XMLStreamReader reader) throws XMLStreamException {
        return readElementText(DocumentationElements.DESCRIPTION, reader);
    }
    
    private static final String readElementText(final QName name, final XMLStreamReader reader)
            throws XMLStreamException {
        assertStartElement(reader);
        assertName(name, reader);
        final StringBuilder sb = new StringBuilder();
        boolean done = false;
        while (!done && reader.hasNext()) {
            reader.next();
            switch (reader.getEventType()) {
                case XMLStreamConstants.START_ELEMENT: {
                    skipElement(reader);
                    break;
                }
                case XMLStreamConstants.END_ELEMENT: {
                    assertName(name, reader);
                    done = true;
                    break;
                }
                case XMLStreamConstants.CHARACTERS: {
                    sb.append(reader.getText());
                    break;
                }
                default: {
                    throw new AssertionError(reader.getEventType());
                }
            }
        }
        return collapseWhitespace(sb.toString());
    }
    
    private static final <T> T assertNotNull(final T obj) {
        if (obj != null) {
            return obj;
        } else {
            throw new AssertionError();
        }
    }
    
    private static final <T> T validateNotNull(final T obj, final String msg) {
        if (obj != null) {
            return obj;
        } else {
            throw new RuntimeException(msg);
        }
    }
    
    /**
     * A programmatic assertion that we have the reader positioned on the correct element.
     * 
     * @param expectLocalName
     *            The local name that we expect.
     * @param reader
     *            The reader.
     */
    private static final void assertName(final QName name, final XMLStreamReader reader) {
        if (!match(name, reader)) {
            throw new AssertionError(reader.getLocalName());
        }
    }
    
    private static final void assertStartDocument(final XMLStreamReader reader) {
        if (XMLStreamConstants.START_DOCUMENT != reader.getEventType()) {
            throw new AssertionError(reader.getLocalName());
        }
    }
    
    private static final void assertStartElement(final XMLStreamReader reader) {
        if (XMLStreamConstants.START_ELEMENT != reader.getEventType()) {
            throw new AssertionError(reader.getLocalName());
        }
    }
}
