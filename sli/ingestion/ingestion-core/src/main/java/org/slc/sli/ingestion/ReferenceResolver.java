package org.slc.sli.ingestion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * Class to resolve and expand references to Ed-Fi references within ingested XML files.
 *
 * @author tshewchuk
 *
 */
public class ReferenceResolver extends DefaultHandler {

    private Map<String, String> referenceObjects;

    private BufferedWriter outputFileWriter;

    private String currentXMLString;
    private StringBuffer tempVal;
    private String topElementName;
    private boolean isValidEntity;
    private boolean startCharacters;

    public ReferenceResolver(Map<String, String> referenceMap) {
        referenceObjects = referenceMap;

        tempVal = new StringBuffer();
        topElementName = "";
        currentXMLString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        isValidEntity = true;
    }

    /**
     * Main method of the reference resolver.
     *
     * @param inputFilePath
     *            Full pathname of input XML file to be expanded.
     * @param outputFilePath
     *            Full pathname of expanded output XML file to be written.
     *
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws IOException
     */
    public void execute(String inputFilePath, String outputFilePath)
            throws SAXException, ParserConfigurationException, IOException {
        // Resolve references to references within the input file using the reference memory map.
        SAXParserFactory spf = SAXParserFactory.newInstance();
        File inputFile = new File(inputFilePath);
        try {
            FileWriter fstream = new FileWriter(outputFilePath);
            outputFileWriter = new BufferedWriter(fstream);
            SAXParser sp = spf.newSAXParser();
            sp.parse(inputFile, this);
        } catch (SAXException se) {
            // Just rethrow the exception to caller.
            throw (se);
        } catch (ParserConfigurationException pce) {
            // Just rethrow the exception to caller.
            throw (pce);
        } catch (IOException ie) {
            // Just rethrow the exception to caller.
            throw (ie);
        } finally {
            outputFileWriter.close();
        }
    }

    /**
     * SAX parser callback method for XML element start.
     *
     * @param uri
     *            Element URI returned by SAX
     * @param localName
     *            Element local name returned by SAX
     * @param qName
     *            Element qualified name returned by SAX
     * @param attributes
     *            Element attribute name/value set returned by SAX
     *
     * @throws SAXException
     *             Parser exception thrown by SAX
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // Mark if top-level element.
        if ((!qName.startsWith("Interchange")) && topElementName.isEmpty()) {
            topElementName = qName;
        }

        // Expand XML references.
        if ((qName.equals(topElementName)) && isValidEntity && qName.endsWith("Reference")
                && (attributes.getValue("id") != null)) {
            // Reference top-level element.
            isValidEntity = false;
        } else if (isValidEntity && qName.endsWith("Reference") && (attributes.getValue("ref") != null)) {
            // Reference to reference - get reference body from map.
            String refId = attributes.getValue("ref");
            if (referenceObjects.containsKey(refId)) {
                isValidEntity = false;
                currentXMLString += referenceObjects.get(refId);
            } else {
                // Unresolved reference! Abort processing.
                SAXException se = new SAXException("Unresolved reference, id=\"" + refId + "\"");
                throw (se);
            }
        }

        // Process non-reference XML element.
        if (isValidEntity) {
            currentXMLString += tempVal.toString();
            currentXMLString += "<" + qName;
            if (attributes != null) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    currentXMLString += " " + attributes.getQName(i) + "=\"" + attributes.getValue(i) + "\"";
                }
            }
            currentXMLString += ">";
        }
        startCharacters = true;
    }

    /**
     * SAX parser callback method for XML element internal characters.
     *
     * @param ch
     *            Character array returned by SAX
     * @param start
     *            Start index in character array returned by SAX
     * @param length
     *            Length of character string returned by SAX
     *
     * @throws SAXException
     *             Parser exception thrown by SAX
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // Get characters between element markers.
        if (startCharacters) {
            tempVal.delete(0, tempVal.length());
            startCharacters = false;
        }
        tempVal.append(ch, start, length);
    }

    /**
     * SAX parser callback method for XML element end.
     *
     * @param uri
     *            Element URI returned by SAX
     * @param localName
     *            Element local name returned by SAX
     * @param qName
     *            Element qualified name returned by SAX
     * @param attributes
     *            Element attribute name/value set returned by SAX
     *
     * @throws SAXException
     *             Parser exception thrown by SAX
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        // Unmark if top-level element.
        if (qName.equals(topElementName)) {
            topElementName = "";
        }

        // Write element to output file.
        if (!isValidEntity && qName.endsWith("Reference")) {
            isValidEntity = true;
        } else if (isValidEntity) {
            currentXMLString += tempVal.toString();
            tempVal.append("\n");
            currentXMLString += "</" + qName + ">";
            writeXML(currentXMLString);
            currentXMLString = "";
        }
        startCharacters = true;
    }

    /**
     * Write the input string to the output XML file.
     *
     * @param xml
     *            Full pathname of output XML file to be written.
     *
     * @throws SAXException
     */
    private void writeXML(String xml) throws SAXException {
        // Write the input string to the output XML file.
        try {
            outputFileWriter.write(xml);
        } catch (IOException e) {
            SAXException se = new SAXException(e.getMessage());
            throw (se);
        }
    }

}
