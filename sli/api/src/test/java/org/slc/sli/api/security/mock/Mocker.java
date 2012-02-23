package org.slc.sli.api.security.mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.client.RestTemplate;

import org.slc.sli.api.security.SLIPrincipal;
import org.slc.sli.api.security.SecurityTokenResolver;
import org.slc.sli.api.security.openam.OpenamRestTokenResolver;
import org.slc.sli.api.security.resolve.RolesToRightsResolver;
import org.slc.sli.api.security.resolve.UserLocator;
import org.slc.sli.api.security.resolve.impl.MongoUserLocator;
import org.slc.sli.api.service.MockRepo;
import org.slc.sli.domain.EntityRepository;
import org.slc.sli.domain.enums.Right;

/**
 * Generates mocked objects for unit tests
 *
 * @author dkornishev
 */
public class Mocker {

    @Autowired
    private static RolesToRightsResolver rolesToRightsResolver;

    @Autowired
    private static SecurityTokenResolver openamRestTokenResolver;
    
    public static final String MOCK_URL = "mock";
    public static final String VALID_TOKEN = "valid_token";
    public static final String INVALID_TOKEN = "invalid_token";

    public static final String PAYLOAD = "userdetails.token.id=AQIC5wM2LY4Sfczq7rqe5T89hyJzxvtfmlagB2VFZEFxE2I.*AAJTSQACMDE.*\r\n"
            + "userdetails.role=id=IT Administrator,ou=group,dc=opensso,dc=java,dc=net\r\n"
            + "userdetails.role=id=parent,ou=group,dc=opensso,dc=java,dc=net\r\n"
            + "userdetails.role=id=teacher,ou=group,dc=opensso,dc=java,dc=net\r\n"
            + "userdetails.attribute.name=uid\r\n"
            + "userdetails.attribute.value=demo\r\n"
            + "userdetails.attribute.name=userpassword\r\n"
            + "userdetails.attribute.value={SSHA}lDuocwqvaaTzkN5Bi+beD2BJBP4sI/dtzD/BfQ==\r\n"
            + "userdetails.attribute.name=sn\r\n"
            + "userdetails.attribute.value=demo\r\n"
            + "userdetails.attribute.name=cn\r\n"
            + "userdetails.attribute.value=demo demo\r\n"
            + "userdetails.attribute.name=givenname\r\n"
            + "userdetails.attribute.value=demo\r\n"
            + "userdetails.attribute.name=inetuserstatus\r\n"
            + "userdetails.attribute.value=Active\r\n"
            + "userdetails.attribute.name=dn\r\n"
            + "userdetails.attribute.value=uid=demo,ou=people,dc=slidev,dc=net\r\n"
            + "userdetails.attribute.name=objectclass\r\n"
            + "userdetails.attribute.value=person\r\n"
            + "userdetails.attribute.value=sunIdentityServerLibertyPPService\r\n"
            + "userdetails.attribute.value=inetorgperson\r\n"
            + "userdetails.attribute.value=sunFederationManagerDataStore\r\n"
            + "userdetails.attribute.value=iPlanetPreferences\r\n"
            + "userdetails.attribute.value=iplanet-am-auth-configuration-service\r\n"
            + "userdetails.attribute.value=organizationalperson\r\n"
            + "userdetails.attribute.value=sunFMSAML2NameIdentifier\r\n"
            + "userdetails.attribute.value=inetuser\r\n"
            + "userdetails.attribute.value=iplanet-am-managed-person\r\n"
            + "userdetails.attribute.value=iplanet-am-user-service\r\n"
            + "userdetails.attribute.value=sunAMAuthAccountLockout\r\n" + "userdetails.attribute.value=top";

    public static final String VALID_REALM = "realm->valid";
    public static final String VALID_USER_ID = "id->valid";
    public static final String INVALID_USER_ID = "~id->invalid";
    public static final String VALID_INTERNAL_ID = "id->internal->valid";
    public static final String INVALID_INTERNAL_ID = "~id->internal->invalid";
    private static final String DEFAULT_REALM_ID = "dc=slidev,dc=net";

    public static RestTemplate mockRest() {
        RestTemplate rest = mock(RestTemplate.class);

        ResponseEntity<String> validationOK = new ResponseEntity<String>("boolean=true", HttpStatus.OK);
        ResponseEntity<String> validationFail = new ResponseEntity<String>("boolean=true", HttpStatus.UNAUTHORIZED);
        ResponseEntity<String> attributesOK = new ResponseEntity<String>(PAYLOAD, HttpStatus.OK);

        when(
                rest.getForEntity(MOCK_URL + "/identity/isTokenValid?tokenid=" + VALID_TOKEN, String.class,
                        Collections.<String, Object>emptyMap())).thenReturn(validationOK);
        when(
                rest.getForEntity(MOCK_URL + "/identity/isTokenValid?tokenid=" + INVALID_TOKEN, String.class,
                        Collections.<String, Object>emptyMap())).thenReturn(validationFail);
        when(
                rest.getForEntity(MOCK_URL + "/identity/attributes?subjectid=" + VALID_TOKEN, String.class,
                        Collections.<String, Object>emptyMap())).thenReturn(attributesOK);

        return rest;
    }

    public static SecurityTokenResolver getMockedOpenamResolver() {
        OpenamRestTokenResolver resolver = (OpenamRestTokenResolver) openamRestTokenResolver;
        resolver.setTokenServiceUrl(Mocker.MOCK_URL);
        resolver.setRest(Mocker.mockRest());
        resolver.setResolver(getRolesToRightsResolver());
        resolver.setLocator(getLocator());

        return resolver;
    }

    public static UserLocator getLocator() {
        MongoUserLocator locator = Mockito.mock(MongoUserLocator.class);
        Mockito.when(locator.locate(VALID_REALM, VALID_USER_ID)).thenReturn(new SLIPrincipal(VALID_INTERNAL_ID));
        Mockito.when(locator.locate("dc=slidev,dc=net", "demo")).thenReturn(new SLIPrincipal(VALID_INTERNAL_ID));
        locator.setRepo(getRepo());
        return locator;
    }

    private static EntityRepository getRepo() {
        return new MockRepo();
    }

    public static void setRolesToRightsResolver(RolesToRightsResolver rolesToRightsResolver) {

        Mocker.rolesToRightsResolver = mock(RolesToRightsResolver.class);

    }

    public static void setOpenamRestTokenResolver(SecurityTokenResolver openamRestTokenResolver) {
        Mocker.openamRestTokenResolver = openamRestTokenResolver;
    }

    private static RolesToRightsResolver getRolesToRightsResolver() {
        Mocker.rolesToRightsResolver = mock(RolesToRightsResolver.class);
        Set<GrantedAuthority> rights = new HashSet<GrantedAuthority>();
        rights.add(Right.READ_GENERAL);
        when(
                rolesToRightsResolver.resolveRoles(DEFAULT_REALM_ID,
                        Arrays.asList(new String[] { "IT Administrator", "parent", "teacher" }))).thenReturn(rights);
        return rolesToRightsResolver;
    }
}
