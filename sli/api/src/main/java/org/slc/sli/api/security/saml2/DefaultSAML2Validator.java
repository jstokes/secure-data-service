package org.slc.sli.api.security.saml2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.keyinfo.X509Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * A basic implementation of a SAML2 validator. This is based on OpenAM
 * as it was configured on 2/16/2012.
 */
@Component
public class DefaultSAML2Validator implements SAML2Validator {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSAML2Validator.class);
    
    private DOMValidateContext valContext;
    
    /**
     * Pulls the <Signature> tag from the SAML assertion document.
     * 
     * @param samlDocument
     *            Document containing SAML assertion.
     * @return Node representing the Signature block from the SAML assertion.
     */
    private Node getSignatureElement(Document samlDocument) {
        return samlDocument.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature").item(0);
    }
    
    /**
     * Creates a DOM validator for the SAML assertion document.
     * 
     * @param samlDocument
     *            Document containing SAML assertion.
     */
    private void createContext(Document samlDocument) {
        this.valContext = new DOMValidateContext(new KeyValueKeySelector(), getSignatureElement(samlDocument));
    }
    
    /**
     * Unmarshals the XML signature from the SAML assertion document.
     * 
     * @param samlDocument
     *            Document containing SAML assertion.
     * @return XML Signature element.
     * @throws MarshalException
     *             thrown if an issue occurs during unmarshalling process.
     */
    private XMLSignature getSignature(Document samlDocument) throws MarshalException {
        createContext(samlDocument);
        XMLSignatureFactory factory = XMLSignatureFactory.getInstance("DOM");
        return factory.unmarshalXMLSignature(valContext);
    }
    
    /**
     * Checks that the specified signature value maps to a trusted Certificate Authority (in
     * ${JAVA_HOME}/lib/security/cacerts).
     * 
     * @param signature
     *            xml signature (contains KeyInfo, SignatureValue, and SignedInfo)
     * @return boolean indicating whether the signature corresponds to a trusted certificate
     *         authority
     * @throws InvalidAlgorithmParameterException
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     */
    private boolean isSignatureTrusted(XMLSignature signature) throws KeyStoreException,
            InvalidAlgorithmParameterException, CertificateException, NoSuchAlgorithmException {
        boolean trusted = false;
        X509Certificate certificate = null;
        
        @SuppressWarnings("unchecked")
        List<XMLStructure> keyInfoContext = signature.getKeyInfo().getContent();
        
        for (XMLStructure xmlStructure : keyInfoContext) {
            if (xmlStructure instanceof X509Data) {
                X509Data xd = (X509Data) xmlStructure;
                @SuppressWarnings("unchecked")
                Iterator<Object> data = xd.getContent().iterator();
                for (; data.hasNext();) {
                    Object o = data.next();
                    if (o instanceof X509Certificate) {
                        certificate = (X509Certificate) o;
                        break;
                    }
                }
            }
        }
        
        if (certificate != null) {
            KeyStore cacerts = loadCaCerts();
            PKIXParameters params = new PKIXParameters(cacerts);
            params.setRevocationEnabled(true);
            
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            CertPath certPath = certFactory.generateCertPath(Arrays.asList(certificate));
            CertPathValidator certPathValidator = CertPathValidator.getInstance(CertPathValidator.getDefaultType());
            try {
                PKIXCertPathValidatorResult result = (PKIXCertPathValidatorResult) certPathValidator.validate(certPath,
                        params);
                TrustAnchor ta = result.getTrustAnchor();
                trusted = true;
                LOG.debug("X509 Certificate is trusted --> Signed by: {}", ta.getCAName());
            } catch (CertPathValidatorException e) {
                LOG.warn("X509 Certificate is not trusted.");
            }
        } else {
            LOG.warn("X509 Certificate is null --> no trust can be established.");
        }
        return trusted;
    }
    
    /**
     * Loads the 'cacerts' Certificate Authority keystore.
     * 
     * @return KeyStore containing trusted certificate authorities.
     */
    private KeyStore loadCaCerts() {
        KeyStore cacerts = null;
        try {
            cacerts = KeyStore.getInstance("JKS");
            cacerts.load(new FileInputStream(new File(System.getProperty("java.home"), "lib/security/cacerts")), null);
        } catch (NoSuchAlgorithmException e) {
            LOG.warn("Requested cryptographic algorithm is invalid or unavailable in the current environment", e);
        } catch (CertificateException e) {
            LOG.warn("There is an issue with the X509 Certificate", e);
        } catch (FileNotFoundException e) {
            LOG.warn("Trusted Certificate Authority store was not found", e);
        } catch (IOException e) {
            LOG.warn("There was an issue opening the trusted Certificate Authority store", e);
        } catch (KeyStoreException e) {
            LOG.warn("There is an issue with the trusted Certificate Authority store", e);
        }
        return cacerts;
    }
    
    /**
     * Checks that the SAML assertion is both trusted and valid.
     * 
     * @param samlDocument
     *            Document containing SAML assertion.
     * @return true if the SAML assertion has been signed by a trusted certificate authority, as
     *         well as passes validation. false, otherwise.
     */
    public boolean isDocumentTrustedAndValid(Document samlDocument) {
        return isDocumentTrusted(samlDocument) && isDocumentValid(samlDocument);
    }
    
    /**
     * Checks that the SAML assertion is trusted.
     * 
     * @param samlDocument
     *            Document containing SAML assertion.
     * @return true if the SAML assertion has been signed by a trusted certificate authority. false,
     *         otherwise.
     */
    public boolean isDocumentTrusted(Document samlDocument) {
        try {
            return isSignatureTrusted(getSignature(samlDocument));
        } catch (MarshalException e) {
            LOG.warn("Couldn't validate Document", e);
        } catch (InvalidAlgorithmParameterException e) {
            LOG.warn("Invalid or inappropriate algorithm parameters", e);
        } catch (CertificateException e) {
            LOG.warn("There is an issue with the X509 Certificate", e);
        } catch (NoSuchAlgorithmException e) {
            LOG.warn("Requested cryptographic algorithm is invalid or unavailable in the current environment", e);
        } catch (KeyStoreException e) {
            LOG.warn("There is an issue with the trusted Certificate Authority store", e);
        }
        return false;
    }
    
    /**
     * Checks that the SAML assertion is valid.
     */
    @Override
    public boolean isDocumentValid(Document samlDocument) {
        try {
            return getSignature(samlDocument).validate(valContext);
        } catch (MarshalException e) {
            LOG.warn("Couldn't validate Document", e);
        } catch (XMLSignatureException e) {
            LOG.warn("Couldn't extract XML Signature from Document", e);
        }
        return false;
    }
    
    @Override
    public boolean isSignatureValid(Document samlDocument) {
        try {
            return getSignature(samlDocument).getSignatureValue().validate(valContext);
        } catch (MarshalException e) {
            LOG.warn("Couldn't validate signature", e);
        } catch (XMLSignatureException e) {
            LOG.warn("Couldn't validate signature", e);
        }
        return false;
    }
    
    @Override
    public boolean isDigestValid(Document samlDocument) {
        boolean valid = false;
        try {
            @SuppressWarnings("unchecked")
            Iterator<Reference> iterator = getSignature(samlDocument).getSignedInfo().getReferences().iterator();
            while (iterator.hasNext()) {
                Reference ref = ((Reference) iterator.next());
                valid = ref.validate(valContext);
            }
        } catch (XMLSignatureException e) {
            LOG.warn("Couldn't validate digest", e);
        } catch (MarshalException e) {
            LOG.warn("Couldn't validate digest", e);
        }
        return valid;
    }
    
    /**
     * Suggest deleting this --> functionality exists within XMLSignatureHelper class.
     */
    @Override
    public Document signDocumentWithSAMLSigner(Document samlDocument, SAML2Signer signer) {
        return null;
    }
    
    private static class KeyValueKeySelector extends KeySelector {
        
        public KeySelectorResult select(KeyInfo keyInfo, KeySelector.Purpose purpose, AlgorithmMethod method,
                XMLCryptoContext context) throws KeySelectorException {
            
            if (keyInfo == null) {
                throw new KeySelectorException("Null KeyInfo object!");
            }
            SignatureMethod sm = (SignatureMethod) method;
            @SuppressWarnings("unchecked")
            List<XMLStructure> list = keyInfo.getContent();
            
            for (XMLStructure xmlStructure : list) {
                if (xmlStructure instanceof KeyValue) {
                    PublicKey pk = null;
                    try {
                        pk = ((KeyValue) xmlStructure).getPublicKey();
                    } catch (KeyException ke) {
                        throw new KeySelectorException(ke);
                    }
                    // make sure algorithm is compatible with method
                    if (algEquals(sm.getAlgorithm(), pk.getAlgorithm())) {
                        return new SimpleKeySelectorResult(pk);
                    }
                }
                if (xmlStructure instanceof X509Data) {
                    X509Data xd = (X509Data) xmlStructure;
                    @SuppressWarnings("unchecked")
                    Iterator<Object> data = xd.getContent().iterator();
                    for (; data.hasNext();) {
                        Object o = data.next();
                        if (o instanceof X509Certificate) {
                            X509Certificate cert = (X509Certificate) o;
                            return new SimpleKeySelectorResult(cert.getPublicKey());
                        }
                    }
                }
            }
            throw new KeySelectorException("No KeyValue element found!");
        }
        
        static boolean algEquals(String algURI, String algName) {
            return (algName.equalsIgnoreCase("DSA") && algURI.equalsIgnoreCase(SignatureMethod.DSA_SHA1))
                    || (algName.equalsIgnoreCase("RSA") && algURI.equalsIgnoreCase(SignatureMethod.RSA_SHA1));
        }
        
        public static class SimpleKeySelectorResult implements KeySelectorResult {
            private Key k;
            
            public SimpleKeySelectorResult(PublicKey k) {
                this.k = k;
            }
            
            @Override
            public Key getKey() {
                return k;
            }
        }
    }
}
