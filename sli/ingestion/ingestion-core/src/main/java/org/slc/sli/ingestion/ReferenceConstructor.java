package org.slc.sli.ingestion;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * Class to construct a list of Ed-Fi references contained in ingested XML files.
 *
 * @author tshewchuk
 *
 */
public class ReferenceConstructor extends DefaultHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ReferenceConstructor.class);

    private Map<String, String> referenceObjects;

    private Set<String> referenceObjectList;

    private boolean isInsideReferenceEntity;
    private String currentReferenceXMLString;
    private String tempVal;
    private String currentReferenceId;
    private String topElementName;

    public ReferenceConstructor(Set<String> referenceSet) {
        tempVal = "";
        topElementName = "";
        referenceObjects = new HashMap<String, String>();
        referenceObjectList = referenceSet;
    }

    /**
     * Main method of the reference constructor.
     *
     * @param filePath
     *            Full pathname of input XML file from which references are extracted.
     *
     * @return Map<String, String>
     *         Memory map of all references within the input file.
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws IOException
     */
    public Map<String, String> execute(String filePath) throws SAXException, ParserConfigurationException, IOException {
        // Extract references bodies from the input file into the memory map.
        SAXParserFactory spf = SAXParserFactory.newInstance();
        File inputFile = new File(filePath);
        try {
            SAXParser sp = spf.newSAXParser();
            sp.parse(filePath, this);
        } catch (SAXException se) {
            LOG.error("Error parsing XML file " + inputFile.getName() + ": " + se.getMessage());
            throw (se);
        } catch (ParserConfigurationException pce) {
            LOG.error("Error configuring parser for XML file " + inputFile.getName() + ": " + pce.getMessage());
            throw (pce);
        } catch (IOException ie) {
            LOG.error("Error reading XML file " + inputFile.getName() + ": " + ie.getMessage());
            throw (ie);
        }

        return referenceObjects;
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

        // Check if top-level element is a reference.
        if ((qName.equals(topElementName)) && referenceObjectList.contains(qName)
                && (attributes.getValue("id") != null)) {
            currentReferenceXMLString = new String();
            currentReferenceId = attributes.getValue("id");
            isInsideReferenceEntity = true;
        }

        // If in reference, add element to reference body.
        if (isInsideReferenceEntity) {
            currentReferenceXMLString += tempVal;
            currentReferenceXMLString += "\t<" + qName;
            if (attributes != null) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    currentReferenceXMLString += " " + attributes.getQName(i) + "=\"" + attributes.getValue(i) + "\"";
                }
            }
            currentReferenceXMLString += ">";
        }
    }

    /**
     * SAX parser callback method for XML element internal characters.
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
    public void characters(char[] ch, int start, int length) throws SAXException {
        // Get characters between element markers.
        tempVal = "";
        for (int i = start; i < start + length; i++) {
            tempVal += String.valueOf(ch[i]);
        }
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

        // If in reference, add reference to reference map.
        if (isInsideReferenceEntity) {
            currentReferenceXMLString += tempVal;
            if (tempVal.contains("\n") && tempVal.trim().isEmpty()) {
                currentReferenceXMLString += "\t";
            }
            tempVal = "\n\t";
            currentReferenceXMLString += "</" + qName + ">";
            if (referenceObjectList.contains(qName)) {
                referenceObjects.put(currentReferenceId, currentReferenceXMLString);
                isInsideReferenceEntity = false;
            }
        }
    }

}
