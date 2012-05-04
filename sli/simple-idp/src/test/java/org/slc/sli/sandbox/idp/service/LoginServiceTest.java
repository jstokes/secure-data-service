package org.slc.sli.sandbox.idp.service;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slc.sli.sandbox.idp.saml.SamlResponseComposer;
import org.slc.sli.sandbox.idp.service.AuthRequests.Request;
import org.slc.sli.sandbox.idp.service.UserService.User;

/**
 * Unit tests
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {
    
    @Mock
    SamlResponseComposer samlComposer;
    
    @InjectMocks
    LoginService login = new LoginService();
    
    @Test
    public void testLogin() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, KeyException,
            TransformerException, MarshalException, XMLSignatureException {
        
        login.setIssuerBase("http://local.slidev.org:8082/simple-idp");
        
        List<String> roles = Arrays.asList("role1", "role2");
        
        Map<String, String> attributes = new HashMap<String, String>();
        Mockito.when(
                samlComposer.componseResponse("destUri", "http://local.slidev.org:8082/simple-idp?realm=realm",
                        "request_id", "unique_id", attributes, roles)).thenReturn("samlResponse");
        
        Request request = Mockito.mock(AuthRequests.Request.class);
        Mockito.when(request.getRequestId()).thenReturn("request_id");
        Mockito.when(request.getRealm()).thenReturn("realm");
        Mockito.when(request.getDestination()).thenReturn("destUri");
        
        User user = Mockito.mock(User.class);
        Mockito.when(user.getUserId()).thenReturn("unique_id");
        
        login.login("unique_id", roles, attributes, request);
        
        Mockito.verify(samlComposer).componseResponse("destUri", "http://local.slidev.org:8082/simple-idp?realm=realm",
                "request_id", "unique_id", attributes, roles);
        
    }
}
