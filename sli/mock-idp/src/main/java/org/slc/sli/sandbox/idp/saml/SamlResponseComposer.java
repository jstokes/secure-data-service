package org.slc.sli.sandbox.idp.saml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Handles creating and signing SAMLResponse xmls
 * 
 * @author Ryan Farris <rfarris@wgen.net>
 * 
 */
@Component
public class SamlResponseComposer {
    
    @Autowired
    XmlSignatureHelper signer;
    
    private static String ROLE_TEMPLATE = "<saml:AttributeValue xmlns:xs='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:type='xs:string'>__ROLE__</saml:AttributeValue>";
    
    public String componseResponse(String destination, String issuer, String requestId, String userId, String userName,
            List<String> roles) {
        
        String unsignedResponse = createUnsignedResponse(destination, issuer, requestId, userId, userName, roles);
        byte[] signedResponse = signResponse(unsignedResponse);
        try {
            System.err.println(new String(signedResponse, "UTF8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Base64.encodeBase64String(signedResponse);
    }
    
    private String createUnsignedResponse(String destination, String issuer, String requestId, String userId,
            String userName, List<String> roles) {
        String template;
        try {
            template = IOUtils.toString(this.getClass().getResourceAsStream("/samlResponseTemplate.xml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        template = template.replace("__RESPONSE_ID__", UUID.randomUUID().toString());
        template = template.replace("__ASSERTION_ID__", UUID.randomUUID().toString());
        template = template.replace("__REQUEST_ID__", requestId);
        template = template.replace("__ISSUE_INSTANT__", currentTimeUTC());
        template = template.replace("__DESTINATION__", destination);
        template = template.replace("__ISSUER__", issuer);
        template = template.replace("__USER_ID__", userId);
        template = template.replace("__USER_NAME__", userName);
        
        StringBuilder buf = new StringBuilder();
        for (String role : roles) {
            buf.append(ROLE_TEMPLATE.replace("__ROLE__", role));
        }
        template = template.replace("__ROLE_ELEMENTS__", buf.toString());
        return template;
    }
    
    private byte[] signResponse(String template) {
        try {
            InputSource stringSource = new InputSource();
            stringSource.setCharacterStream(new StringReader(template));
            
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            Document unsignedDoc = docFactory.newDocumentBuilder().parse(stringSource);
            Document signedDoc = signer.signSamlAssertion(unsignedDoc);
            
            // any transforms (indentation, etc) will break the XML Signatures. No touch!
            Transformer trans = TransformerFactory.newInstance().newTransformer();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            trans.transform(new DOMSource(signedDoc), new StreamResult(bos));
            return bos.toByteArray();
            
        } catch (SAXException e) {
            // holy checked exception list, Batman!
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        } catch (MarshalException e) {
            throw new RuntimeException(e);
        } catch (XMLSignatureException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static String currentTimeUTC() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(new Date());
    }
}
