package org.slc.sli.modeling.tools.uml2Xsd.core;

import javax.xml.namespace.QName;

import org.slc.sli.modeling.uml.Occurs;

/**
 * The interface for writing the W3C XML Schema from the {@link Uml2XsdPluginWriter}.
 */
public interface Uml2XsdPluginWriter {

    void annotation();

    void appinfo();

    void begin(final String prefix, final String localName, final String namespace);

    void characters(String text);

    void choice();

    void comment(String data);

    void complexType();

    void documentation();

    void element();

    void end();

    void maxOccurs(final Occurs value);

    void minOccurs(final Occurs value);

    void name(QName name);

    void sequence();

    void type(QName name);

}
