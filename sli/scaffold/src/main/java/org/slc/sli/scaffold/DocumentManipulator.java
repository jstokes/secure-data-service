package org.slc.sli.scaffold;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * This class handles the document manipulation tasks
 * @author srupasinghe
 *
 */
public class DocumentManipulator {
    private DocumentBuilderFactory docFactory;
    private XPathFactory xPathFactory;
 
    protected void init() {
        docFactory = DocumentBuilderFactory.newInstance();
        xPathFactory = XPathFactory.newInstance();
        
        docFactory.setNamespaceAware(true);
    }
    
    /**
     * Parse a xml document
     * @param fileName
     * @throws ScaffoldException 
     */
    protected Document parseDocument(File file) throws DocumentManipulatorException {
        Document doc = null;
        
        try {
            DocumentBuilder builder = docFactory.newDocumentBuilder();
            doc = builder.parse(file);
        } catch (SAXException e) {
            //change to better exception handling later
            throw new DocumentManipulatorException(e);
        } catch (IOException e) {
            throw new DocumentManipulatorException(e);
        } catch (ParserConfigurationException e) {
            throw new DocumentManipulatorException(e);
        }
        
        return doc;
    }
    
    /**
     * Evaluates a xpath expression and returns a NodeList 
     * @param doc
     * @param expression
     * @return
     * @throws XPathException
     */
    protected NodeList getNodeList(Document doc, String expression) throws XPathException {
        XPath xpath = xPathFactory.newXPath();        
        xpath.setNamespaceContext(new DocumentNamespaceResolver(doc));
        
        XPathExpression exp = xpath.compile(expression);
            
        Object result = exp.evaluate(doc, XPathConstants.NODESET);
            
        return (NodeList) result;
    }
    
    /**
     * Serializes a document to html
     * @param document
     * @param outputFile
     * @param xslFile
     * @throws DocumentManipulatorException
     */
    public void serializeDocumentToHtml(Document document, File outputFile, File xslFile) throws DocumentManipulatorException {
        FileOutputStream fileOutput = null;     
        StreamSource xslSource = null;
        
        Properties props = new Properties();
            
        //set the output properties
        props.put(OutputKeys.METHOD, "html");
            
        try {
            fileOutput = new FileOutputStream(outputFile);
            //create the stream source from the xsl stylesheet
            xslSource = new StreamSource(xslFile);
                
            serialize(document, new StreamResult(fileOutput), xslSource, props);
        } catch (FileNotFoundException e) {
            throw new DocumentManipulatorException(e);
        }
    }
    
    /**
     * Serialize a document in XML format to a file
     * @param document
     * @param outputFile
     * @throws DocumentManipulatorException
     */
    public void serializeDocumentToXml(Document document, File outputFile) throws DocumentManipulatorException {
        PrintWriter out = null;     
            
        try {
            out = new PrintWriter(new FileOutputStream(outputFile, false));
                
            out.print(serializeDocumentToString(document));
            out.flush();
        } catch (FileNotFoundException e) {
            throw new DocumentManipulatorException(e);
        } finally {
            out.close();
        }
        
    }
    
    /**
     * Serializes a document to string
     * @param document
     * @return
     * @throws DocumentManipulatorException
     */
    public String serializeDocumentToString(Document document) throws DocumentManipulatorException {
        StringWriter outText = new StringWriter();
        
        Properties props = new Properties();            
        props.put(OutputKeys.METHOD, "xml");            
            
        serialize(document, new StreamResult(outText), null, props);
            
        return outText.toString();
    }
    
    /**
     * Serializes a given document to a stream
     * @param document
     * @param stream
     * @param source
     * @param props
     * @throws DocumentManipulatorException
     */
    private void serialize(Document document, StreamResult stream, Source source, Properties props) throws DocumentManipulatorException {
        TransformerFactory factory = null;
        Transformer transformer = null;
        
        try {            
            //create the factory
            factory = TransformerFactory.newInstance();
            
            //create the transformer
            if (source != null) {
                transformer = factory.newTransformer(source);
            } else {
                transformer = factory.newTransformer();
            }
            
            //set the properties
            transformer.setOutputProperties(props);
            //perform the transformation
            transformer.transform(new DOMSource(document), stream);
            
        } catch (TransformerConfigurationException e) {
            throw new DocumentManipulatorException(e);
        } catch (TransformerFactoryConfigurationError e) {
            throw new DocumentManipulatorException(e);
        } catch (TransformerException e) {
            throw new DocumentManipulatorException(e);
        }
    }
}
