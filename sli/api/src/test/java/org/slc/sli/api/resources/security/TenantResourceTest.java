package org.slc.sli.api.resources.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.sun.jersey.core.util.MultivaluedMapImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slc.sli.api.representation.EntityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.SecurityContextInjector;
import org.slc.sli.api.resources.security.TenantResource;
import org.slc.sli.api.resources.util.ResourceTestUtil;
import org.slc.sli.api.resources.v1.HypermediaType;
import org.slc.sli.api.service.EntityNotFoundException;
import org.slc.sli.api.test.WebContextTestExecutionListener;
import org.slc.sli.common.constants.ResourceConstants;

/**
 * Unit tests for the resource representing a tenant
 * @author srichards
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
public class TenantResourceTest {

    @Autowired
    TenantResource tenantResource; //class under test

    @Autowired
    private SecurityContextInjector injector;

    private UriInfo uriInfo;
    private HttpHeaders httpHeaders;

    private static final String TENANT_1 = "IL";
    private static final String TENANT_2 = "NC";
    private static final String TENANT_3 = "NY";
    private static final String ED_ORG_1 = "Daybreak";
    private static final String ED_ORG_2 = "Sunset";

    @Before
    public void setup() throws Exception {
        uriInfo = ResourceTestUtil.buildMockUriInfo(null);

        // inject administrator security context for unit testing
        injector.setRealmAdminContext();

        List<String> acceptRequestHeaders = new ArrayList<String>();
        acceptRequestHeaders.add(HypermediaType.VENDOR_SLC_JSON);

        httpHeaders = mock(HttpHeaders.class);
        when(httpHeaders.getRequestHeader("accept")).thenReturn(acceptRequestHeaders);
        when(httpHeaders.getRequestHeaders()).thenReturn(new MultivaluedMapImpl());
    }

    private Map<String, Object> createTestEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put(TenantResource.TENANT_ID, TENANT_1);
        Map<String, Object> landingZone = new HashMap<String, Object>();
        landingZone.put(TenantResource.LZ_INGESTION_SERVER, "example.com");
        landingZone.put(TenantResource.LZ_EDUCATION_ORGANIZATION, ED_ORG_1);
        landingZone.put(TenantResource.LZ_DESC, "Landing zone for IL_DAYBREAK");
        landingZone.put(TenantResource.LZ_PATH, "C:\\code\\sli\\sli\\ingestion\\ingestion-service\\target\\ingestion\\lz\\inbound\\IL-STATE-DAYBREAK");
        List<Map<String, Object>> landingZones = new ArrayList<Map<String, Object>>();
        landingZones.add(landingZone);
        entity.put(TenantResource.LZ, landingZones);
        return entity;
    }

    private Map<String, Object> createTestAppendEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put(TenantResource.TENANT_ID, TENANT_1);
        Map<String, Object> landingZone = new HashMap<String, Object>();
        landingZone.put(TenantResource.LZ_INGESTION_SERVER, "example.com");
        landingZone.put(TenantResource.LZ_EDUCATION_ORGANIZATION, ED_ORG_2);
        landingZone.put(TenantResource.LZ_DESC, "Landing zone for IL_SUNSET");
        landingZone.put(TenantResource.LZ_PATH, "C:\\code\\sli\\sli\\ingestion\\ingestion-service\\target\\ingestion\\lz\\inbound\\IL-STATE-SUNSET");
        List<Map<String, Object>> landingZones = new ArrayList<Map<String, Object>>();
        landingZones.add(landingZone);
        entity.put(TenantResource.LZ, landingZones);
        return entity;
    }

    private Map<String, Object> createTestUpdateEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put(TenantResource.TENANT_ID, TENANT_2);
        Map<String, Object> landingZone = new HashMap<String, Object>();
        landingZone.put(TenantResource.LZ_INGESTION_SERVER, "example.com");
        landingZone.put(TenantResource.LZ_EDUCATION_ORGANIZATION, ED_ORG_2);
        landingZone.put(TenantResource.LZ_DESC, "Landing zone for IL_SUNSET");
        landingZone.put(TenantResource.LZ_PATH, "C:\\code\\sli\\sli\\ingestion\\ingestion-service\\target\\ingestion\\lz\\inbound\\IL-STATE-SUNSET");
        List<Map<String, Object>> landingZones = new ArrayList<Map<String, Object>>();
        landingZones.add(landingZone);
        entity.put(TenantResource.LZ, landingZones);
        return entity;
    }

    private Map<String, Object> createTestSecondaryEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put(TenantResource.TENANT_ID, TENANT_3);
        Map<String, Object> landingZone = new HashMap<String, Object>();
        landingZone.put(TenantResource.LZ_INGESTION_SERVER, "example.com");
        landingZone.put(TenantResource.LZ_EDUCATION_ORGANIZATION, "NYC");
        landingZone.put(TenantResource.LZ_DESC, "Landing zone for NY");
        landingZone.put(TenantResource.LZ_PATH, "C:\\code\\sli\\sli\\ingestion\\ingestion-service\\target\\ingestion\\lz\\inbound\\NY-STATE-NYC");
        List<Map<String, Object>> landingZones = new ArrayList<Map<String, Object>>();
        landingZones.add(landingZone);
        entity.put(TenantResource.LZ, landingZones);
        return entity;
    }

    @Test
    public void testCreate() {
        Response response = tenantResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        assertEquals("Status code should be 201", Status.CREATED.getStatusCode(), response.getStatus());

        String id = ResourceTestUtil.parseIdFromLocation(response);
        assertNotNull("ID should not be null", id);
    }

    @Test
    public void testCreateAppends() {
        Response response = tenantResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        assertEquals("Status code should be 201", Status.CREATED.getStatusCode(), response.getStatus());
        String id1 = ResourceTestUtil.parseIdFromLocation(response);
        assertNotNull("ID should not be null", id1);

        response = tenantResource.create(new EntityBody(createTestAppendEntity()), httpHeaders, uriInfo);
        assertEquals("Status code should be 201", Status.CREATED.getStatusCode(), response.getStatus());
        String id2 = ResourceTestUtil.parseIdFromLocation(response);
        assertNotNull("ID should not be null", id2);

        assertEquals("Both creates should return same id", id1, id2);

        //try to get it
        Response getResponse = tenantResource.read(id1, httpHeaders, uriInfo);
        assertEquals("Status code should be OK", Status.OK.getStatusCode(), getResponse.getStatus());
        EntityResponse entityResponse = (EntityResponse) getResponse.getEntity();
        EntityBody body = (EntityBody) entityResponse.getEntity();
        assertNotNull("Should return an entity", body);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> landingZones = (List<Map<String, Object>>) body.get(TenantResource.LZ);
        assertEquals("Should have 2 landing zones", 2, landingZones.size());
    }

    @Test
    public void testRead() {
        //create one entity
        Response createResponse = tenantResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String id = ResourceTestUtil.parseIdFromLocation(createResponse);
        Response response = tenantResource.read(id, httpHeaders, uriInfo);

        Object responseEntityObj = null;

        if (response.getEntity() instanceof EntityResponse) {
            EntityResponse resp = (EntityResponse) response.getEntity();
            responseEntityObj = resp.getEntity();
        } else {
            fail("Should always return EntityResponse: " + response);
        }

        if (responseEntityObj instanceof EntityBody) {
            assertNotNull(responseEntityObj);
        } else if (responseEntityObj instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<EntityBody> results = (List<EntityBody>) responseEntityObj;
            assertTrue("Should have one entity", results.size() == 1);
        } else {
            fail("Response entity not recognized: " + response);
        }
    }

    @Test
    public void testDelete() {
        //create one entity
        Response createResponse = tenantResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String id = ResourceTestUtil.parseIdFromLocation(createResponse);

        //delete it
        Response response = tenantResource.delete(id, httpHeaders, uriInfo);
        assertEquals("Status code should be NO_CONTENT", Status.NO_CONTENT.getStatusCode(), response.getStatus());

        try {
            @SuppressWarnings("unused")
            Response getResponse = tenantResource.read(id, httpHeaders, uriInfo);
            fail("should have thrown EntityNotFoundException");
        } catch (EntityNotFoundException e) {
            return;
        } catch (Exception e) {
            fail("threw wrong exception: " + e);
        }
    }

    @Test
    public void testUpdate() {
        //create one entity
        Response createResponse = tenantResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String id = ResourceTestUtil.parseIdFromLocation(createResponse);

        //update it
        Response response = tenantResource.update(id, new EntityBody(createTestUpdateEntity()), httpHeaders, uriInfo);
        assertEquals("Status code should be NO_CONTENT", Status.NO_CONTENT.getStatusCode(), response.getStatus());

        //try to get it
        Response getResponse = tenantResource.read(id, httpHeaders, uriInfo);
        assertEquals("Status code should be OK", Status.OK.getStatusCode(), getResponse.getStatus());
        EntityResponse entityResponse = (EntityResponse) getResponse.getEntity();
        EntityBody body = (EntityBody) entityResponse.getEntity();
        assertNotNull("Should return an entity", body);
        assertEquals(TenantResource.TENANT_ID + " should be " + TENANT_2, body.get(TenantResource.TENANT_ID), TENANT_2);
        assertNotNull("Should include links", body.get(ResourceConstants.LINKS));
    }

    @Test
    public void testReadAll() {
        //create two entities
        tenantResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        tenantResource.create(new EntityBody(createTestSecondaryEntity()), httpHeaders, uriInfo);

        //read everything
        Response response = tenantResource.readAll(0, 100, httpHeaders, uriInfo);
        assertEquals("Status code should be OK", Status.OK.getStatusCode(), response.getStatus());

        EntityResponse entityResponse = (EntityResponse) response.getEntity();
        @SuppressWarnings("unchecked")
        List<EntityBody> results = (List<EntityBody>) entityResponse.getEntity();
        assertNotNull("Should return entities", results);
        assertTrue("Should have at least two entities", results.size() >= 2);
    }

    @Test
    public void testReadCommaSeparatedResources() {
        Response response = tenantResource.read(getIDList("tenants"), httpHeaders, uriInfo);
        assertEquals("Status code should be 200", Status.OK.getStatusCode(), response.getStatus());

        EntityResponse entityResponse = (EntityResponse) response.getEntity();
        @SuppressWarnings("unchecked")
        List<EntityBody> results = (List<EntityBody>) entityResponse.getEntity();
        assertEquals("Should get 2 entities", results.size(), 2);

        EntityBody body1 = results.get(0);
        assertNotNull("Should not be null", body1);
        assertEquals(TenantResource.TENANT_ID + " should be " + TENANT_1, body1.get(TenantResource.TENANT_ID), TENANT_1);
        assertNotNull("Should include links", body1.get(ResourceConstants.LINKS));

        EntityBody body2 = results.get(1);
        assertNotNull("Should not be null", body2);
        assertEquals(TenantResource.TENANT_ID + " should be " + TENANT_3, body2.get(TenantResource.TENANT_ID), TENANT_3);
        assertNotNull("Should include links", body2.get(ResourceConstants.LINKS));
    }

    private String getIDList(String resource) {
        //create more resources
        Response createResponse1 = tenantResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        Response createResponse2 = tenantResource.create(new EntityBody(createTestSecondaryEntity()), httpHeaders, uriInfo);

        return ResourceTestUtil.parseIdFromLocation(createResponse1) + "," + ResourceTestUtil.parseIdFromLocation(createResponse2);
    }
}