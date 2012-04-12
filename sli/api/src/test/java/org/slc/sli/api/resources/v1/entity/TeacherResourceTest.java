package org.slc.sli.api.resources.v1.entity;

import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.SecurityContextInjector;
import org.slc.sli.api.resources.util.ResourceTestUtil;
import org.slc.sli.api.resources.v1.HypermediaType;
import org.slc.sli.api.resources.v1.association.TeacherSchoolAssociationResource;
import org.slc.sli.api.resources.v1.association.TeacherSectionAssociationResource;
import org.slc.sli.api.test.WebContextTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * JUnit for teacher resources
 *
 * @author chung
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
public class TeacherResourceTest {

    private final String teacherResourceName = "TeacherResource";
    private final String schoolResourceName = "SchoolResource";
    private final String sectionResourceName = "SectionResource";
    private final String teacherSchoolAssociationResourceName = "TeacherSchoolAssociationResource";
    private final String teacherSectionAssociationResourceName = "TeacherSectionAssociationResource";

    @Autowired
    private SecurityContextInjector injector;
    @Autowired
    private TeacherResource teacherResource;
    @Autowired
    private SchoolResource schoolResource;
    @Autowired
    private SectionResource sectionResource;
    @Autowired
    private TeacherSchoolAssociationResource teacherSchoolAssociationResource;
    @Autowired
    private TeacherSectionAssociationResource teacherSectionAssociationResource;

    HttpHeaders httpHeaders;
    UriInfo uriInfo;

    @Before
    public void setup() throws Exception {
        uriInfo = ResourceTestUtil.buildMockUriInfo(null);

        // inject administrator security context for unit testing
        injector.setAdminContextWithElevatedRights();

        List<String> acceptRequestHeaders = new ArrayList<String>();
        acceptRequestHeaders.add(HypermediaType.VENDOR_SLC_JSON);

        httpHeaders = mock(HttpHeaders.class);
        when(httpHeaders.getRequestHeader("accept")).thenReturn(acceptRequestHeaders);
        when(httpHeaders.getRequestHeaders()).thenReturn(new MultivaluedMapImpl());
    }

    @Test
    public void testGetTeacherSchoolAssociations() {
        Response createResponse = teacherResource.create(new EntityBody(
                ResourceTestUtil.createTestEntity(teacherResourceName)), httpHeaders, uriInfo);
        String teacherId = ResourceTestUtil.parseIdFromLocation(createResponse);
        createResponse = schoolResource.create(new EntityBody(
                ResourceTestUtil.createTestEntity(schoolResourceName)), httpHeaders, uriInfo);
        String schoolId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = ResourceTestUtil.createTestAssociationEntity(
                teacherSchoolAssociationResourceName, teacherResourceName, teacherId, schoolResourceName, schoolId);
        teacherSchoolAssociationResource.create(new EntityBody(map), httpHeaders, uriInfo);

        Response response = teacherResource.getTeacherSchoolAssociations(teacherId, httpHeaders, uriInfo);
        ResourceTestUtil.assertions(response);
    }

    @Test
    public void testGetTeacherSchoolAssociationsSchools() {
        Response createResponse = teacherResource.create(new EntityBody(
                ResourceTestUtil.createTestEntity(teacherResourceName)), httpHeaders, uriInfo);
        String teacherId = ResourceTestUtil.parseIdFromLocation(createResponse);
        createResponse = schoolResource.create(new EntityBody(
                ResourceTestUtil.createTestEntity(schoolResourceName)), httpHeaders, uriInfo);
        String schoolId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = ResourceTestUtil.createTestAssociationEntity(
                teacherSchoolAssociationResourceName, teacherResourceName, teacherId, schoolResourceName, schoolId);
        teacherSchoolAssociationResource.create(new EntityBody(map), httpHeaders, uriInfo);

        Response response = teacherResource.getTeacherSchoolAssociationsSchools(teacherId, httpHeaders, uriInfo);
        ResourceTestUtil.assertions(response);
    }

    @Test
    public void testGetTeacherSectionAssociations() {
        Response createResponse = teacherResource.create(new EntityBody(
                ResourceTestUtil.createTestEntity(teacherResourceName)), httpHeaders, uriInfo);
        String teacherId = ResourceTestUtil.parseIdFromLocation(createResponse);
        createResponse = sectionResource.create(new EntityBody(
                ResourceTestUtil.createTestEntity(sectionResourceName)), httpHeaders, uriInfo);
        String sectionId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = ResourceTestUtil.createTestAssociationEntity(
                teacherSectionAssociationResourceName, teacherResourceName, teacherId, sectionResourceName, sectionId);
        teacherSectionAssociationResource.create(new EntityBody(map), httpHeaders, uriInfo);

        Response response = teacherResource.getTeacherSectionAssociations(teacherId, httpHeaders, uriInfo);
        ResourceTestUtil.assertions(response);
    }

    @Test
    public void testGetTeacherSectionAssociationsSections() {
        Response createResponse = teacherResource.create(new EntityBody(
                ResourceTestUtil.createTestEntity(teacherResourceName)), httpHeaders, uriInfo);
        String teacherId = ResourceTestUtil.parseIdFromLocation(createResponse);
        createResponse = sectionResource.create(new EntityBody(
                ResourceTestUtil.createTestEntity(sectionResourceName)), httpHeaders, uriInfo);
        String sectionId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = ResourceTestUtil.createTestAssociationEntity(
                teacherSectionAssociationResourceName, teacherResourceName, teacherId, sectionResourceName, sectionId);
        teacherSectionAssociationResource.create(new EntityBody(map), httpHeaders, uriInfo);

        Response response = teacherResource.getTeacherSectionAssociationsSections(teacherId, httpHeaders, uriInfo);
        ResourceTestUtil.assertions(response);
    }
}
