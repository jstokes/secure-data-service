package net.wgenhq.dcoleman_wks3._8443.sftest;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

import java.util.logging.Logger;

import com.sforce.soap.authentication.AuthenticateResult;

/**
 * This class was generated by Apache CXF 2.4.1
 * 2011-07-28T11:58:22.987-04:00
 * Generated source version: 2.4.1
 */
 
@WebService(targetNamespace = "http://dcoleman-wks3.wgenhq.net:8443/sftest/", name = "SimpleAdAuthSoap")
@XmlSeeAlso({com.sforce.soap.authentication.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE) 
public class SimpleAdAuthSoapImpl implements SimpleAdAuthSoap {

    @WebResult(name = "AuthenticateResult", targetNamespace = "urn:authentication.soap.sforce.com", partName = "parameters")
    @WebMethod(operationName = "Authenticate")
    @Override
    public AuthenticateResult authenticate(
        @WebParam(partName = "parameters", name = "Authenticate", targetNamespace = "urn:authentication.soap.sforce.com")
        com.sforce.soap.authentication.Authenticate parameters
    ) {
        Logger log = Logger.getLogger(SimpleAdAuthSoapImpl.class.getName());
        log.info("starting SimpleAdAuthSoapImpl");
        AuthenticateResult result = new AuthenticateResult();
        result.setAuthenticated(false);
        if(parameters.getUsername().equals("dave")) {
            result.setAuthenticated(true);
        }
        log.info("done SimpleAdAuthSoapImpl");
        return result;
    }
}