package org.slc.sli.api.security.saml2;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.crypto.*;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.security.Key;
import java.security.KeyException;
import java.security.PublicKey;
import java.util.Iterator;
import java.util.List;

/**
 * A basic implementation of a SAML2 validator. This is based on OpenAM
 * as it was configured on 2/16/2012.
 */
public class DefaultSAML2Validator implements SAML2Validator {

    private DOMValidateContext valContext;

    public NodeList getSignatureElement(Document samlDocument) {
        return samlDocument.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
    }

    public void createContext(Document samlDocument) {
        this.valContext = new DOMValidateContext(new KeyValueKeySelector(), getSignatureElement(samlDocument).item(0));
    }

    public XMLSignature getSignature(Document samlDocument) throws MarshalException {
        createContext(samlDocument);
        XMLSignatureFactory factory = XMLSignatureFactory.getInstance("DOM");
        return factory.unmarshalXMLSignature(valContext);
    }

    @Override
    public boolean isDocumentValid(Document samlDocument) {
        try {
            return getSignature(samlDocument).validate(valContext);
        } catch (MarshalException e) {
            return false;
        } catch (XMLSignatureException e) {
            return false;
        }
    }

    @Override
    public boolean isSignatureValid(Document samlDocument) {
        try {
            return getSignature(samlDocument).getSignatureValue().validate(valContext);
        } catch (MarshalException e) {
            return false;
        } catch (XMLSignatureException e) {
            return false;
        }

    }

    @Override
    public boolean isDigestValid(Document samlDocument) {
        boolean valid = false;
        try {
            Iterator iterator = getSignature(samlDocument).getSignedInfo().getReferences().iterator();
            for (; iterator.hasNext();) {
                valid = ((Reference) iterator.next()).validate(valContext);
            }
        } catch (XMLSignatureException e) {
            return false;
        } catch (MarshalException e) {
            return false;
        }
        return valid;
    }

    @Override
    public Document signDocumentWithSAMLSigner(Document samlDocument, SAML2Signer signer) {
        return null;
    }

    private static class KeyValueKeySelector extends KeySelector {

        public KeySelectorResult select(KeyInfo keyInfo,
                                        KeySelector.Purpose purpose,
                                        AlgorithmMethod method,
                                        XMLCryptoContext context)
                throws KeySelectorException {

            if (keyInfo == null) {
                throw new KeySelectorException("Null KeyInfo object!");
            }
            SignatureMethod sm = (SignatureMethod) method;
            List list = keyInfo.getContent();

            for (Object struct : list) {
                XMLStructure xmlStructure = (XMLStructure) struct;
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
            }
            throw new KeySelectorException("No KeyValue element found!");
        }

        static boolean algEquals(String algURI, String algName) {
            if (algName.equalsIgnoreCase("DSA") &&
                    algURI.equalsIgnoreCase(SignatureMethod.DSA_SHA1)) {
                return true;
            } else if (algName.equalsIgnoreCase("RSA") &&
                    algURI.equalsIgnoreCase(SignatureMethod.RSA_SHA1)) {
                return true;
            } else {
                return false;
            }
        }

        public class SimpleKeySelectorResult implements KeySelectorResult {
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
