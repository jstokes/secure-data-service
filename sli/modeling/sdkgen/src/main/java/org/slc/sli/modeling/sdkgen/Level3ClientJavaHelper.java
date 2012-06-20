package org.slc.sli.modeling.sdkgen;

import java.util.Stack;

import javax.xml.namespace.QName;

import org.apache.ws.commons.schema.XmlSchemaChoice;
import org.apache.ws.commons.schema.XmlSchemaComplexType;
import org.apache.ws.commons.schema.XmlSchemaDatatype;
import org.apache.ws.commons.schema.XmlSchemaDerivationMethod;
import org.apache.ws.commons.schema.XmlSchemaElement;
import org.apache.ws.commons.schema.XmlSchemaObject;
import org.apache.ws.commons.schema.XmlSchemaObjectCollection;
import org.apache.ws.commons.schema.XmlSchemaParticle;
import org.apache.ws.commons.schema.XmlSchemaSequence;
import org.apache.ws.commons.schema.XmlSchemaType;

import org.slc.sli.modeling.jgen.JavaType;

public final class Level3ClientJavaHelper {

    public static JavaType showElement(final XmlSchemaElement element, final Stack<QName> elementNames) {
        final QName elementName = element.getQName();
        @SuppressWarnings("unused")
        final long minOccurs = element.getMinOccurs();
        final long maxOccurs = element.getMaxOccurs();
        if (elementNames.contains(elementName)) {
            // Avoid infinite recursion.
            return JavaType.JT_OBJECT;
        }
        elementNames.push(elementName);
        try {
            final XmlSchemaType schemaType = element.getSchemaType();
            final JavaType primeType = showSchemaType(schemaType, elementNames);
            if (maxOccurs > 0) {
                return JavaType.listType(primeType);
            } else {
                return primeType;
            }
        } finally {
            elementNames.pop();
        }
    }

    private static JavaType showSchemaType(final XmlSchemaType schemaType, final Stack<QName> elementNames) {
        if (schemaType instanceof XmlSchemaComplexType) {
            final XmlSchemaComplexType complexType = (XmlSchemaComplexType) schemaType;
            return showComplexType(complexType, elementNames);
        } else {
            throw new AssertionError(schemaType);
        }
    }

    private static JavaType showComplexType(final XmlSchemaComplexType complexType, final Stack<QName> elementNames) {
        final QName name = complexType.getQName();
        if (name != null) {
            return JavaType.complexType(name.getLocalPart(), JavaType.JT_OBJECT);
        } else {
            @SuppressWarnings("unused")
            final XmlSchemaDatatype dataType = complexType.getDataType();
            // System.out.println("dataType : " + dataType);
            @SuppressWarnings("unused")
            final XmlSchemaDerivationMethod deriveBy = complexType.getDeriveBy();
            // System.out.println("deriveBy : " + deriveBy);
            final XmlSchemaParticle particle = complexType.getParticle();
            if (particle != null) {
                return showParticle(particle, elementNames);
            } else {
                return JavaType.JT_OBJECT;
            }
        }
    }

    private static JavaType showParticle(final XmlSchemaParticle particle, final Stack<QName> elementNames) {
        if (particle == null) {
            throw new NullPointerException("particle");
        }
        if (particle instanceof XmlSchemaSequence) {
            final XmlSchemaSequence sequence = (XmlSchemaSequence) particle;
            return showSequence(sequence, elementNames);
        } else {
            throw new AssertionError(particle);
        }
    }

    private static JavaType showSequence(final XmlSchemaSequence sequence, final Stack<QName> elementNames) {
        final XmlSchemaObjectCollection items = sequence.getItems();
        final int count = items.getCount();
        if (count == 1) {
            final XmlSchemaObject schemaObject = items.getItem(0);
            return showObject(schemaObject, elementNames);
        } else {
            for (int i = 0; i < count; i++) {
                final XmlSchemaObject schemaObject = items.getItem(i);
                @SuppressWarnings("unused")
                final JavaType child = showObject(schemaObject, elementNames);
            }
            return JavaType.JT_VOID;
        }
    }

    private static JavaType showObject(final XmlSchemaObject schemaObject, final Stack<QName> elementNames) {
        if (schemaObject instanceof XmlSchemaElement) {
            final XmlSchemaElement element = (XmlSchemaElement) schemaObject;
            // Just gone recursive on element depth.
            return showElement(element, elementNames);
        } else if (schemaObject instanceof XmlSchemaChoice) {
            return JavaType.JT_OBJECT;
        } else {
            throw new AssertionError(schemaObject);
        }
    }

}
