package org.slc.sli.api.security.context.resolver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slc.sli.api.test.WebContextTestExecutionListener;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
public class EdOrgHelperTest {
    
    @Autowired
    EdOrgHelper helper;
    
    @Autowired
    private Repository<Entity> repo;
    
    /*
     *  Create an EdOrg Hierarchy that looks like
     *  sea1 --> staff4
     *   |
     *   |
     *  lea1 --> staff1
     *   |  \
     *   |   school1 -- teacher1
     *  lea2 --> staff2
     *   |  \
     *   |   school2 --> teacher1
     *  lea3 --> staff2
     *   |
     *  school3 --> teacher3
     */
    
    Entity staff1 = null;   //directly associated with lea1
    Entity staff2 = null;   //directly associated with lea2
    Entity staff3 = null;   //directly associated with lea3
    Entity staff4 = null;   //directly associated with lea4
    Entity sea1 = null;
    Entity lea1 = null;
    Entity lea2 = null;
    Entity lea3 = null;
    Entity school1 = null;
    Entity school2 = null;
    Entity school3 = null;
    Entity teacher1 = null;
    Entity teacher2 = null;
    Entity teacher3 = null;
    
    @Before
    public void setup() {

        Map<String, Object> body = new HashMap<String, Object>();
        body.put("staffUniqueStateId", "staff1");
        staff1 = repo.create("staff", body);
        
        body = new HashMap<String, Object>();
        body.put("staffUniqueStateId", "staff2");
        staff2 = repo.create("staff", body);
        
        body = new HashMap<String, Object>();
        body.put("staffUniqueStateId", "staff3");
        staff3 = repo.create("staff", body);
        
        body = new HashMap<String, Object>();
        body.put("staffUniqueStateId", "staff4");
        staff4 = repo.create("staff", body);
        
        body = new HashMap<String, Object>();
        teacher1 = repo.create("teacher", body);
        
        body = new HashMap<String, Object>();
        teacher2 = repo.create("teacher", body);
        
        body = new HashMap<String, Object>();
        teacher3 = repo.create("teacher", body);
        
        body = new HashMap<String, Object>();
        body.put("organizationCategories", Arrays.asList("State Education Agency"));
        sea1 = repo.create("educationOrganization", body);      
        
        body = new HashMap<String, Object>();
        body.put("organizationCategories", Arrays.asList("Local Education Agency"));
        body.put("parentEducationAgencyReference", sea1.getEntityId());
        lea1 = repo.create("educationOrganization", body);
        
        body = new HashMap<String, Object>();
        body.put("organizationCategories", Arrays.asList("Local Education Agency"));
        body.put("parentEducationAgencyReference", lea1.getEntityId());
        lea2 = repo.create("educationOrganization", body);
        
        body = new HashMap<String, Object>();
        body.put("organizationCategories", Arrays.asList("Local Education Agency"));
        body.put("parentEducationAgencyReference", lea2.getEntityId());
        lea3 = repo.create("educationOrganization", body);

        body = new HashMap<String, Object>();
        body.put("organizationCategories", Arrays.asList("School"));
        body.put("parentEducationAgencyReference", lea1.getEntityId());
        school1 = repo.create("educationOrganization", body);
        
        body = new HashMap<String, Object>();
        body.put("organizationCategories", Arrays.asList("School"));
        body.put("parentEducationAgencyReference", lea2.getEntityId());
        school2 = repo.create("educationOrganization", body);
        
        body = new HashMap<String, Object>();
        body.put("organizationCategories", Arrays.asList("School"));
        body.put("parentEducationAgencyReference", lea3.getEntityId());
        school3 = repo.create("educationOrganization", body);
                          
        body = new HashMap<String, Object>();
        body.put("educationOrganizationReference", lea1.getEntityId());
        body.put("staffReference", staff1.getEntityId());
        repo.create("staffEducationOrganizationAssociation", body);
        
        body = new HashMap<String, Object>();
        body.put("educationOrganizationReference", lea2.getEntityId());
        body.put("staffReference", staff2.getEntityId());
        repo.create("staffEducationOrganizationAssociation", body);
        
        body = new HashMap<String, Object>();
        body.put("educationOrganizationReference", lea3.getEntityId());
        body.put("staffReference", staff3.getEntityId());
        repo.create("staffEducationOrganizationAssociation", body);
        
        body = new HashMap<String, Object>();
        body.put("educationOrganizationReference", sea1.getEntityId());
        body.put("staffReference", staff4.getEntityId());
        repo.create("staffEducationOrganizationAssociation", body);
        
        body = new HashMap<String, Object>();
        body.put("schoolId", school1.getEntityId());
        body.put("teacherId", teacher1.getEntityId());
        repo.create("teacherSchoolAssociation", body);
        
        body = new HashMap<String, Object>();
        body.put("schoolId", school2.getEntityId());
        body.put("teacherId", teacher2.getEntityId());
        repo.create("teacherSchoolAssociation", body);
        
        body = new HashMap<String, Object>();
        body.put("schoolId", school3.getEntityId());
        body.put("teacherId", teacher3.getEntityId());
        repo.create("teacherSchoolAssociation", body);
    }

    
    @Test
    public void testStaff1() {
        List<String> leas = helper.getLEAs(staff1);
        assertTrue("staff1 must see lea1", leas.contains(lea1.getEntityId()));
        assertTrue("staff1 must see lea2", leas.contains(lea2.getEntityId()));
        assertTrue("staff1 must see lea3", leas.contains(lea3.getEntityId()));
        assertFalse("staff1 must not see sea1", leas.contains(sea1.getEntityId()));
        
        List<String> seas = helper.getSEAs(staff1);
        assertTrue("staff1 must see sea1", seas.contains(sea1.getEntityId()));
    }
    
    @Test
    public void testStaff2() {
        List<String> leas = helper.getLEAs(staff2);
        assertTrue("staff2 must see lea1", leas.contains(lea1.getEntityId()));
        assertTrue("staff2 must see lea2", leas.contains(lea2.getEntityId()));
        assertTrue("staff2 must see lea3", leas.contains(lea3.getEntityId()));
        assertFalse("staff2 must not see sea1", leas.contains(sea1.getEntityId()));
        
        List<String> seas = helper.getSEAs(staff2);
        assertTrue("staff2 must see sea1", seas.contains(sea1.getEntityId()));
    }
    
    @Test
    public void testStaff3() {
        List<String> leas = helper.getLEAs(staff3);
        assertTrue("staff3 must see lea1", leas.contains(lea1.getEntityId()));
        assertTrue("staff3 must see lea2", leas.contains(lea2.getEntityId()));
        assertTrue("staff3 must see lea3", leas.contains(lea3.getEntityId()));
        assertFalse("staff3 must not see sea1", leas.contains(sea1.getEntityId()));
        
        List<String> seas = helper.getSEAs(staff3);
        assertTrue("staff3 must see sea1", seas.contains(sea1.getEntityId()));
    }
    
    @Test
    public void testStaff4() {
        List<String> leas = helper.getLEAs(staff4);
        assertTrue("staff4 must see lea1", leas.contains(lea1.getEntityId()));
        assertTrue("staff4 must see lea2", leas.contains(lea2.getEntityId()));
        assertTrue("staff4 must see lea3", leas.contains(lea3.getEntityId()));
        assertFalse("staff4 must not see sea1", leas.contains(sea1.getEntityId()));
        
        List<String> seas = helper.getSEAs(staff4);
        assertTrue("staff4 must see sea1", seas.contains(sea1.getEntityId()));
    }
    
    @Test
    public void testTeacher1() {
        List<String> leas = helper.getLEAs(teacher1);
        assertTrue("teacher1 must see lea1", leas.contains(lea1.getEntityId()));
        assertFalse("teacher1 must not see lea2", leas.contains(lea2.getEntityId()));
        assertFalse("teacher1 must not see lea3", leas.contains(lea3.getEntityId()));
        assertFalse("teacher1 must not see sea1", leas.contains(sea1.getEntityId()));
        
        List<String> seas = helper.getSEAs(teacher1);
        assertTrue("teacher1 must see sea1", seas.contains(sea1.getEntityId()));
    }
    
    @Test
    public void testTeacher2() {
        List<String> leas = helper.getLEAs(teacher2);
        assertTrue("teacher2 must see lea1", leas.contains(lea1.getEntityId()));
        assertTrue("teacher2 must see lea2", leas.contains(lea2.getEntityId()));
        assertFalse("teacher2 must not see lea3", leas.contains(lea3.getEntityId()));
        assertFalse("teacher2 must not see sea1", leas.contains(sea1.getEntityId()));
        
        List<String> seas = helper.getSEAs(teacher2);
        assertTrue("teacher2 must see sea1", seas.contains(sea1.getEntityId()));
    }
    
    @Test
    public void testTeacher3() {
        List<String> leas = helper.getLEAs(teacher3);
        assertTrue("teacher3 must see lea1", leas.contains(lea1.getEntityId()));
        assertTrue("teacher3 must see lea2", leas.contains(lea2.getEntityId()));
        assertTrue("teacher3 must see lea3", leas.contains(lea3.getEntityId()));
        assertFalse("teacher3 must not see sea1", leas.contains(sea1.getEntityId()));
        
        List<String> seas = helper.getSEAs(teacher3);
        assertTrue("teacher3 must see sea1", seas.contains(sea1.getEntityId()));
    }

}
