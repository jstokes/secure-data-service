package org.slc.sli.api.ldap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
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
    
    private static String uid;
    private static User testUser;

    @Before
    public void init() throws UnknownHostException {
        testUser = buildTestUser();
        uid = testUser.getUid();
        ldapService.removeUser("local", uid);
    }
    
    @After
    public void clear() {
        ldapService.removeUser("local", uid);
    }

    @Test
    public void testGetUser() {
        User slcoperator = ldapService.getUser("local", "slcoperator");
        assertNotNull(slcoperator);
        assertTrue(slcoperator.getGroups().contains("SLC Operator"));
        assertTrue(slcoperator.getEmail().equals("slcoperator@slidev.org"));
        assertTrue(slcoperator.getUid().equals("slcoperator"));
        assertNotNull(slcoperator.getHomeDir());
        assertNotNull(slcoperator.getFirstName());
        assertNotNull(slcoperator.getLastName());
        assertNotNull(slcoperator.getFullName());
        assertNull(slcoperator.getTenant());
        assertNull(slcoperator.getEdorg());
        assertNotNull(slcoperator.getCreateTime());
        assertNotNull(slcoperator.getModifyTime());
    }

    @Test
    public void testGetGroup() {
        Group slcoperatorGroup = ldapService.getGroup("local", "SLC Operator");
        assertNotNull(slcoperatorGroup);
        assertEquals("SLC Operator", slcoperatorGroup.getGroupName());
        assertTrue(slcoperatorGroup.getMemberUids().contains("slcoperator"));
    }

    @Test
    public void testGetUserGroups() {

        List<Group> groups = ldapService.getUserGroups("local", "slcoperator");
        assertNotNull(groups);
        List<String> groupNames = new ArrayList<String>();
        for (Group group : groups) {
            groupNames.add(group.getGroupName());
        }
        assertTrue(groupNames.contains("SLC Operator"));

    }

    @Test
    public void testFindUserByGroups() {
        String[] groups = new String[] { "SEA Administrator" };
        List<User> users = ldapService.findUserByGroups("local", Arrays.asList(groups));
        assertNotNull(users);
    }
    
    @Test
    public void testCRUDUser() throws UnknownHostException {
        
        // test create
        String newUserUid = ldapService.createUser("local", testUser);
        User newUser = ldapService.getUser("local", newUserUid);
        assertNotNull(newUser);
        assertNotNull(newUser.getGroups());
        assertTrue(newUser.getGroups().contains("SLC Operator"));
        assertTrue(newUser.getGroups().contains("SEA Administrator"));
        assertTrue(newUser.getGroups().contains("LEA Administrator"));
        assertEquals(uid,newUser.getUid());
        assertEquals("testemail@slidev.org", newUser.getEmail());
        assertEquals("testFirst", newUser.getFirstName());
        assertEquals("testLast", newUser.getLastName());
        assertEquals("/dev/null", newUser.getHomeDir());
        assertEquals("testTenant", newUser.getTenant());
        assertEquals("testEdorg", newUser.getEdorg());
        
        // test delete
        ldapService.removeUser("local", uid);
        User testUser = ldapService.getUser("local", uid);
        assertNull(testUser);

    }
    
    private User buildTestUser() throws UnknownHostException {
        User testUser = new User();
        testUser.setFirstName("testFirst");
        testUser.setLastName("testLast");
        testUser.setUid("testUid_" + InetAddress.getLocalHost().getHostName());
        testUser.setEdorg("testEdorg");
        testUser.setTenant("testTenant");
        testUser.setEmail("testemail@slidev.org");
        testUser.setHomeDir("/dev/null");
        testUser.addGroup("SLC Operator");
        testUser.addGroup("SEA Administrator");
        testUser.addGroup("LEA Administrator");
        return testUser;
    }

}
