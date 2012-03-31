package org.slc.sli.api.resources.v1.view;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.SecurityContextInjector;
import org.slc.sli.api.service.MockRepo;
import org.slc.sli.api.test.WebContextTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests
 * @author srupasinghe
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
public class OptionalFieldAppenderHelperTest {

    @Autowired
    OptionalFieldAppenderHelper helper;

    @Autowired
    MockRepo repo;

    @Autowired
    private SecurityContextInjector injector;

    @Before
    public void setup() {
        // inject administrator security context for unit testing
        injector.setAdminContextWithElevatedRights();
    }

    @After
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testQueryEntities() {

    }

    @Test
    public void testGetEntityFromList() {
        EntityBody body = helper.getEntityFromList(createEntityList(), "field1", "2");
        assertEquals("Should match", "2", body.get("field1"));
        assertEquals("Should match", "2", body.get("field2"));
        assertEquals("Should match", "2", body.get("id"));

        body =  helper.getEntityFromList(null, "field1", "2");
        assertNull("Should be null", body);

        body =  helper.getEntityFromList(createEntityList(), null, "2");
        assertNull("Should be null", body);

        body =  helper.getEntityFromList(createEntityList(), "field1", null);
        assertNull("Should be null", body);

        body =  helper.getEntityFromList(null, null, null);
        assertNull("Should be null", body);

        body =  helper.getEntityFromList(createEntityList(), "", "");
        assertNull("Should be null", body);
    }

    @Test
    public void testGetEntitySubList() {
        List<EntityBody> list = helper.getEntitySubList(createEntityList(), "field1", "3");
        assertEquals("Should match", 2, list.size());
        assertEquals("Should match", "3", list.get(0).get("field1"));
        assertEquals("Should match", "3", list.get(1).get("field1"));

        list = helper.getEntitySubList(createEntityList(), "field1", "0");
        assertEquals("Should match", 0, list.size());

        list =  helper.getEntitySubList(null, "field1", "2");
        assertEquals("Should match", 0, list.size());

        list =  helper.getEntitySubList(createEntityList(), null, "2");
        assertEquals("Should match", 0, list.size());

        list =  helper.getEntitySubList(createEntityList(), "field1", null);
        assertEquals("Should match", 0, list.size());

        list =  helper.getEntitySubList(null, null, null);
        assertEquals("Should match", 0, list.size());

        list =  helper.getEntitySubList(createEntityList(), "", "");
        assertEquals("Should match", 0, list.size());
    }

    @Test
    public void testGetIdList() {
        List<String> list = helper.getIdList(createEntityList(), "id");

        assertEquals("Should match", 4, list.size());
        assertTrue("Should contain", list.contains("1"));
        assertTrue("Should contain", list.contains("2"));
        assertTrue("Should contain", list.contains("3"));
        assertTrue("Should contain", list.contains("4"));
        assertFalse("Should not contain", list.contains("5"));

        list =  helper.getIdList(null, "id");
        assertEquals("Should match", 0, list.size());

        list =  helper.getIdList(null, null);
        assertEquals("Should match", 0, list.size());

        list =  helper.getIdList(createEntityList(), "");
        assertEquals("Should match", 0, list.size());
    }

    private List<EntityBody> createEntityList() {
        List<EntityBody> list = new ArrayList<EntityBody>();

        list.add(createTestEntity("1", "1", "1"));
        list.add(createTestEntity("2", "2", "2"));
        list.add(createTestEntity("3", "3", "3"));
        list.add(createTestEntity("4", "3", "4"));

        return list;
    }

    private EntityBody createTestEntity(String id, String value1, String value2) {
        EntityBody body = new EntityBody();
        body.put("id", id);
        body.put("field1", value1);
        body.put("field2", value2);

        return body;
    }
}
