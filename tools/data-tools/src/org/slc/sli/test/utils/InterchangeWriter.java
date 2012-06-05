package org.slc.sli.test.utils;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter;
import org.slc.sli.test.xmlgen.StateEdFiXmlGenerator;

/**
 * Interchange writer
 *
 * @author bsuzuki
 *
 */
public class InterchangeWriter<T> {

    private static final int XML_WRITER_BUFFER_SIZE = 104857600; // 100 MB
    private static final boolean FORMAT_INTERCHANGE_XML = true;

    private String interchangeName = null;
    private String xmlFilePath = null;

    private XMLStreamWriter writer = null;
    private Marshaller streamMarshaller = null;
    
    private long interchangeStartTime;
    
    public InterchangeWriter(Class<T> interchange) {
        
        interchangeStartTime = System.currentTimeMillis();
        interchangeName = interchange.getSimpleName();
        xmlFilePath = StateEdFiXmlGenerator.rootOutputPath + "/" + interchangeName + ".xml";

        System.out.println("Creating interchange " + interchangeName);
        try {
            JAXBContext context = JAXBContext.newInstance(interchange);
            streamMarshaller = context.createMarshaller();
            // Doesn't work for XMLStreamWriter
//            streamMarshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);
            streamMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        } catch (JAXBException e1) {
            e1.printStackTrace();
            System.exit(1);  // fail fast for now
        }

        try {
            FileOutputStream xmlFos = new FileOutputStream(xmlFilePath);
            OutputStream xmlBos = new BufferedOutputStream(xmlFos, XML_WRITER_BUFFER_SIZE);
            writer = XMLOutputFactory.newFactory().createXMLStreamWriter(xmlBos, "UTF-8");
            if (FORMAT_INTERCHANGE_XML) {
                IndentingXMLStreamWriter indentingWriter = new IndentingXMLStreamWriter(writer);
                indentingWriter.setIndentStep("    ");
                writer = indentingWriter;
            }
            writer.writeStartDocument("UTF-8", "1.0");

            // remove unwanted population of namespace attributes
            writer.setNamespaceContext(new NamespaceContext() {
                public Iterator<String> getPrefixes(String namespaceURI) {
                    return null;
                }

                public String getPrefix(String namespaceURI) {
                    return "";
                }

                public String getNamespaceURI(String prefix) {
                    return null;
                }
            });
            
            writer.writeStartElement(interchangeName);
            writer.writeNamespace(null, "http://ed-fi.org/0100");

        } catch (XMLStreamException e) {
            e.printStackTrace();
            System.exit(1);  // fail fast for now
        } catch (FactoryConfigurationError e) {
            e.printStackTrace();
            System.exit(1);  // fail fast for now
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);  // fail fast for now
        }
    }
    
    public void close() {
        
      System.out.println("generated and marshaled in: "
      + (System.currentTimeMillis() - interchangeStartTime));

        try {

//            writer.writeEndElement();
            writer.writeEndDocument();
            writer.close();
        } catch (XMLStreamException e) {
            e.printStackTrace();
            System.exit(1);  // fail fast for now
        }

        streamMarshaller = null;
    }

    /**
     * Marshal the provided object using the XMLStreamWriter specified
     *
     * @param objectToMarshal
     * @param outputStream
     * @throws JAXBException
     */
    public void marshal(Object objectToMarshal) {
        if (objectToMarshal != null) {

            try {                
                streamMarshaller.marshal(objectToMarshal, writer);
            } catch (JAXBException e) {
                e.printStackTrace();
                System.exit(1);  // fail fast for now
            }

        } else {
            throw new IllegalArgumentException("Cannot marshal null object");
        }
    }

    public String getXmlFilePath() {
        return xmlFilePath;
    }

}
