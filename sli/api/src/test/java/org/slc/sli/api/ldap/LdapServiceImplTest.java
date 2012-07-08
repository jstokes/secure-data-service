package org.slc.sli.api.ldap;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Unit tests
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class LdapServiceImplTest {

    @Autowired
    LdapService ldapService;
    
    @Test
    public void testGetUser() {
        User slcoperator = ldapService.getUser("SLIAdmin", "slcoperator");
        assertNotNull(slcoperator);
        assertTrue(slcoperator.getRoles().contains("SLC Operator"));
        assertTrue(slcoperator.getEmail().equals("slcoperator@slidev.org"));
        assertTrue(slcoperator.getUid().equals("slcoperator"));
        assertNotNull(slcoperator.getHomeDir());
        assertNotNull(slcoperator.getFirstName());
        assertNotNull(slcoperator.getLastName());
        assertNotNull(slcoperator.getFullName());
        assertNull(slcoperator.getTenant());
        assertNull(slcoperator.getEdorg());

    }
    
    @Test
    public void testGetUserGroups() {
        
        List<String> groups = ldapService.getUserGroups("SLIAdmin", "slcoperator");
        assertNotNull(groups);
        assertTrue(groups.contains("SLC Operator"));

    }

}
