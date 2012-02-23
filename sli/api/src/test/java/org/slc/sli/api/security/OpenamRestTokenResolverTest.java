package org.slc.sli.api.security;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import org.slc.sli.api.security.mock.Mocker;
import org.slc.sli.api.security.openam.OpenamRestTokenResolver;
import org.slc.sli.api.security.resolve.RolesToRightsResolver;
import org.slc.sli.api.security.resolve.UserLocator;
import org.slc.sli.api.test.WebContextTestExecutionListener;
import org.slc.sli.domain.enums.Right;

/**
 * Unit tests for the OpenamRestTokenResolver.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
public class OpenamRestTokenResolverTest {

    private static final String     DEFAULT_REALM_ID = "i am -> a default -> realm -> yay -> me!";

    private OpenamRestTokenResolver resolver;

    private RolesToRightsResolver   rightsResolver;
    
    @Value("${sli.security.tokenService.url}")
    private String tokenServiceUrl;
 

    @Before
    public void init() {
        resolver = new OpenamRestTokenResolver();
        resolver.setTokenServiceUrl(Mocker.MOCK_URL);
        resolver.setRest(Mocker.mockRest());
        UserLocator locator = Mocker.getLocator();
        SLIPrincipal principal = new SLIPrincipal(Mocker.VALID_INTERNAL_ID);
        principal.setRealm(DEFAULT_REALM_ID);
        Mockito.when(locator.locate("mock", "demo")).thenReturn(principal);
		resolver.setLocator(locator);

        
        rightsResolver = mock(RolesToRightsResolver.class);
        resolver.setResolver(rightsResolver);

        Set<GrantedAuthority> rights = new HashSet<GrantedAuthority>();
        rights.add(Right.READ_GENERAL);
        when(rightsResolver.resolveRoles(DEFAULT_REALM_ID, Arrays.asList(new String[] { "IT Administrator", "parent", "teacher" }))).thenReturn(rights);
    }

    @Test
    public void testResolveSuccess() {

        Authentication auth = resolver.resolve(Mocker.VALID_TOKEN);
        Assert.assertNotNull(auth.getAuthorities());
        Assert.assertTrue(auth.getAuthorities().contains(Right.READ_GENERAL));
    }

    @Test
    public void testResolveFailure() {
        when(rightsResolver.resolveRoles(DEFAULT_REALM_ID, null)).thenReturn(null);
        when(rightsResolver.resolveRoles(null, null)).thenReturn(null);
        Authentication auth = resolver.resolve(Mocker.INVALID_TOKEN);
        Assert.assertEquals(1, auth.getAuthorities().size());
        Assert.assertTrue(auth.getAuthorities().contains(Right.ANONYMOUS_ACCESS));
    }
}
