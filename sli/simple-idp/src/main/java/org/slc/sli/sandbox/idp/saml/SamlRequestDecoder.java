package org.slc.sli.sandbox.idp.saml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Decodes SAMLRequests and parses out the relevant information.
 * 
 * @author Ryan Farris <rfarris@wgen.net>
 * 
 */
@Component
public class SamlRequestDecoder {
    
    // private static final Logger LOG = LoggerFactory.getLogger(SamlRequestDecoder.class);

    @Value("${sli.simple-idp.cot}")
    private String cotString;
    
    private Map<String, String> cot;
    
    @SuppressWarnings("unused")
    @PostConstruct
    void initialize(){
        cot = new HashMap<String, String>();
        String[] trustedIssuers = cotString.split(",");
        for(String trustedIssuerPair : trustedIssuers){
            String[] trustedIssuer = trustedIssuerPair.split("=");
            cot.put(trustedIssuer[0], trustedIssuer[1]);
        }
    }
    /**
     * Holds saml request info
     */
    public static class SamlRequest {
        private final String spDestination;
        private final String id;
        private final String idpDestination;
        
        SamlRequest(String spDestination, String idpDestination, String id) {
            this.spDestination = spDestination;
            this.idpDestination = idpDestination;
            this.id = id;
        }
        public String getIdpDestination(){
            return idpDestination;
        }
        public String getSpDestination() {
            return spDestination;
        }
        
        public String getId() {
            return id;
        }
    }
    
    public SamlRequest decode(String encodedSamlRequest) {
        byte[] decodedCompressed = Base64.decodeBase64(encodedSamlRequest);
        Inflater inflater = new Inflater(true);
        InflaterInputStream xmlInputStream = new InflaterInputStream(new ByteArrayInputStream(decodedCompressed),
                inflater);
        Document doc;
        
        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = docBuilder.parse(xmlInputStream);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Element element = doc.getDocumentElement();
        String id = element.getAttribute("ID");
        String simpleIDPDestination = element.getAttribute("Destination");
        NodeList nodes = element.getElementsByTagName("saml:Issuer");
        String issuer = null;
        if (nodes.getLength() > 0) {
            Node item = nodes.item(0);
            issuer = item.getFirstChild().getNodeValue();
        } else {
            throw new IllegalArgumentException("No Issuer element on AuthnRequest");
        }
        
        if (id == null) {
            throw new IllegalArgumentException("No ID attribute on AuthnRequest.");
        }
        String responseDestination = cot.get(issuer);
        if (responseDestination == null) {
            throw new IllegalArgumentException("Issuer of AuthnRequest is unknown.");
        }
        
        return new SamlRequest(responseDestination, simpleIDPDestination, id);
    }

    public void setCotString(String cotString) {
        this.cotString = cotString;
    }
    
    
}
