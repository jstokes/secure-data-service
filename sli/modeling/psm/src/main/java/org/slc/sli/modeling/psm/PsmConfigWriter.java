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


package org.slc.sli.modeling.psm;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.slc.sli.modeling.uml.Attribute;
import org.slc.sli.modeling.uml.ClassType;
import org.slc.sli.modeling.uml.DataType;
import org.slc.sli.modeling.uml.EnumLiteral;
import org.slc.sli.modeling.uml.EnumType;
import org.slc.sli.modeling.uml.Model;
import org.slc.sli.modeling.uml.Occurs;
import org.slc.sli.modeling.uml.Range;
import org.slc.sli.modeling.uml.Taggable;
import org.slc.sli.modeling.uml.TaggedValue;
import org.slc.sli.modeling.uml.Type;
import org.slc.sli.modeling.uml.index.ModelIndex;
import org.slc.sli.modeling.xmi.XmiAttributeName;
import org.slc.sli.modeling.xml.IndentingXMLStreamWriter;

public final class PsmConfigWriter {

    private static final void closeQuiet(final Closeable closeable) {
        try {
            closeable.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private static final void writeAttribute(final Attribute attribute, final ModelIndex model,
            final XMLStreamWriter xsw) throws XMLStreamException {
        xsw.writeStartElement(PsmConfigElements.ATTRIBUTE.getLocalPart());
        try {
            xsw.writeStartElement(PsmConfigElements.NAME.getLocalPart());
            try {
                xsw.writeCharacters(attribute.getName());
            } finally {
                xsw.writeEndElement();
            }
            writeDescription(attribute, model, xsw);
            final Range range = attribute.getMultiplicity().getRange();
            xsw.writeStartElement(PsmConfigElements.LOWER.getLocalPart());
            try {
                xsw.writeCharacters(toString(range.getLower()));
            } finally {
                xsw.writeEndElement();
            }
            xsw.writeStartElement(PsmConfigElements.UPPER.getLocalPart());
            try {
                xsw.writeCharacters(toString(range.getUpper()));
            } finally {
                xsw.writeEndElement();
            }
            xsw.writeStartElement(PsmConfigElements.TYPE.getLocalPart());
            try {
                xsw.writeStartElement(PsmConfigElements.NAME.getLocalPart());
                try {
                    xsw.writeCharacters(model.getType(attribute.getType()).getName());
                } finally {
                    xsw.writeEndElement();
                }
            } finally {
                xsw.writeEndElement();
            }
        } finally {
            xsw.writeEndElement();
        }
    }

    private static final String toString(final Occurs value) {
        if (value == null) {
            throw new NullPointerException("value");
        }
        switch (value) {
            case ZERO: {
                return "0";
            }
            case ONE: {
                return "1";
            }
            case UNBOUNDED: {
                return "*";
            }
            default: {
                throw new AssertionError(value);
            }
        }
    }

    @SuppressWarnings("unused")
    private static final void writePimClassType(final ClassType classType, final ModelIndex model,
            final XMLStreamWriter xsw) throws XMLStreamException {
        xsw.writeStartElement(PsmConfigElements.CLASS_TYPE.getLocalPart());
        try {
            xsw.writeStartElement(PsmConfigElements.NAME.getLocalPart());
            try {
                xsw.writeCharacters(classType.getName());
            } finally {
                xsw.writeEndElement();
            }
            writeDescription(classType, model, xsw);
            for (final Attribute attribute : classType.getAttributes()) {
                writeAttribute(attribute, model, xsw);
            }
        } finally {
            xsw.writeEndElement();
        }
    }

    @SuppressWarnings("unused")
    private static final void writePimDataType(final DataType dataType, final ModelIndex model,
            final XMLStreamWriter xsw) throws XMLStreamException {
        xsw.writeStartElement(PsmConfigElements.DATA_TYPE.getLocalPart());
        try {
            xsw.writeStartElement(PsmConfigElements.NAME.getLocalPart());
            try {
                xsw.writeCharacters(dataType.getName());
            } finally {
                xsw.writeEndElement();
            }
            writeDescription(dataType, model, xsw);
            writeFacets(dataType, model, xsw);
        } finally {
            xsw.writeEndElement();
        }
    }

    @SuppressWarnings("unused")
    private static final void writePimEnumType(final EnumType enumType, final ModelIndex model,
            final XMLStreamWriter xsw) throws XMLStreamException {
        xsw.writeStartElement(PsmConfigElements.ENUM_TYPE.getLocalPart());
        try {
            xsw.writeStartElement(PsmConfigElements.NAME.getLocalPart());
            try {
                xsw.writeCharacters(enumType.getName());
            } finally {
                xsw.writeEndElement();
            }
            writeDescription(enumType, model, xsw);
            for (final EnumLiteral literal : enumType.getLiterals()) {
                xsw.writeStartElement(PsmConfigElements.LITERAL.getLocalPart());
                try {
                    xsw.writeAttribute("value", literal.getName());
                } finally {
                    xsw.writeEndElement();
                }
            }
            writeFacets(enumType, model, xsw);
        } finally {
            xsw.writeEndElement();
        }
    }

    private static final void writeDescription(final Taggable type, final ModelIndex model, final XMLStreamWriter xsw)
            throws XMLStreamException {
        for (final TaggedValue taggedValue : type.getTaggedValues()) {
            final String name = model.getTagDefinition(taggedValue.getTagDefinition()).getName();
            if ("documentation".equals(name)) {
                xsw.writeStartElement(PsmConfigElements.DESCRIPTION.getLocalPart());
                try {
                    xsw.writeCharacters(taggedValue.getValue());
                } finally {
                    xsw.writeEndElement();
                }
            }
        }
    }

    private static final void writeSingularResourceName(final PsmCollection collection, final XMLStreamWriter xsw)
            throws XMLStreamException {
        xsw.writeStartElement(PsmConfigElements.SINGULAR_RESOURCE_NAME.getLocalPart());
        try {
            xsw.writeCharacters(collection.getName());
        } finally {
            xsw.writeEndElement();
        }
    }

    private static final void writePluralResourceName(final PsmResource resource, final XMLStreamWriter xsw)
            throws XMLStreamException {
        xsw.writeStartElement(PsmConfigElements.GRAPH_RESOURCE_NAME.getLocalPart());
        try {
            xsw.writeCharacters(resource.getName());
        } finally {
            xsw.writeEndElement();
        }
    }

    private static final void writeFacets(final Taggable type, final ModelIndex model, final XMLStreamWriter xsw)
            throws XMLStreamException {
        for (final TaggedValue taggedValue : type.getTaggedValues()) {
            final String localName = model.getTagDefinition(taggedValue.getTagDefinition()).getName();
            if (!"documentation".equals(localName)) {
                xsw.writeStartElement(localName);
                try {
                    xsw.writeCharacters(taggedValue.getValue());
                } finally {
                    xsw.writeEndElement();
                }
            }
        }
    }

    public static final void writeConfig(final PsmConfig<Type> documentation, final Model model,
            final OutputStream outstream) {
        final XMLOutputFactory xof = XMLOutputFactory.newInstance();
        try {
            final XMLStreamWriter xsw = new IndentingXMLStreamWriter(xof.createXMLStreamWriter(outstream, "UTF-8"));
            xsw.writeStartDocument("UTF-8", "1.0");
            try {
                writeRoot(documentation, model, xsw);
            } finally {
                xsw.writeEndDocument();
            }
            xsw.flush();
            xsw.close();
        } catch (final XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    public static final void writeConfig(final PsmConfig<Type> documentation, final Model model, final String fileName) {
        try {
            final OutputStream outstream = new BufferedOutputStream(new FileOutputStream(fileName));
            try {
                writeConfig(documentation, model, outstream);
            } finally {
                closeQuiet(outstream);
            }
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static final void writeDocument(final PsmDocument<Type> document, final XMLStreamWriter xsw)
            throws XMLStreamException {
        xsw.writeStartElement(PsmConfigElements.DOCUMENT.getLocalPart());
        try {
            final Type type = document.getType();
            // The type name is a reasonable default for the document name.
            xsw.writeAttribute(XmiAttributeName.NAME.getLocalName(), type.getName());
            xsw.writeStartElement(PsmConfigElements.CLASS_TYPE.getLocalPart());
            try {
                xsw.writeAttribute(XmiAttributeName.NAME.getLocalName(), type.getName());
            } finally {
                xsw.writeEndElement();
            }
            writePluralResourceName(document.getGraphAssociationEndName(), xsw);
            writeSingularResourceName(document.getSingularResourceName(), xsw);
        } finally {
            xsw.writeEndElement();
        }
    }

    private static final void writeRoot(final PsmConfig<Type> documentation, final Model model,
            final XMLStreamWriter xsw) throws XMLStreamException {
        xsw.writeStartElement(PsmConfigElements.DOCUMENTS.getLocalPart());
        try {
            for (final PsmDocument<Type> document : documentation.getDocuments()) {
                writeDocument(document, xsw);
            }
        } finally {
            xsw.writeEndElement();
        }
    }
}
