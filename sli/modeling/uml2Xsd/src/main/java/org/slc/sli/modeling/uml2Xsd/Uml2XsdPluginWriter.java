package org.slc.sli.modeling.uml2Xsd;

import javax.xml.namespace.QName;

import org.slc.sli.modeling.uml.Occurs;

/**
 * The interface for writing the W3C XML Schema from the {@link Uml2XsdPluginWriter}.
 */
public interface Uml2XsdPluginWriter {
    
    void annotation();
    
    void characters(String text);
    
    void choice();
    
    void comment(String data);
    
    void documentation();
    
    void element();
    
    void end();
    
    void maxOccurs(final Occurs value);
    
    void minOccurs(final Occurs value);
    
    void name(QName name);
    
    void type(QName name);
    
}
