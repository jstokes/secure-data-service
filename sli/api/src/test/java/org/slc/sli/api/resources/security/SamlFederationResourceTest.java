/*
 * Copyright 2012-2013 inBloom, Inc. and its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.slc.sli.api.resources.security;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slc.sli.common.constants.EntityNames;
import org.slc.sli.common.constants.ParameterConstants;
import org.slc.sli.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.slc.sli.api.security.context.resolver.EdOrgHelper;
import org.slc.sli.api.test.WebContextTestExecutionListener;

/**
 * Unit tests for the Saml Federation Resource class.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
public class SamlFederationResourceTest {

    @Autowired
    SamlFederationResource resource;

    public final static String EDORG_REF = "educationOrganizationReference";
    private final static String testStaffId = "staff1";

    public static SimpleDateFormat ft = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");

    @Test
    public void getMetadataTest() {
        Response response = resource.getMetadata();
        Assert.assertNotNull(response);
        Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());
        Assert.assertNotNull(response.getEntity());

        Exception exception = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader((String) response.getEntity()));
            db.parse(is);
        } catch (ParserConfigurationException e) {
            exception = e;
        } catch (SAXException e) {
            exception = e;
        } catch (IOException e) {
            exception = e;
        }
        Assert.assertNull(exception);

    }

    @Test (expected = AccessDeniedException.class)
    public void consumeBadSAMLDataTest() {
        String postData = "badSAMLData";

        Exception exception = null;
        UriInfo uriInfo = Mockito.mock(UriInfo.class);
        resource.consume(postData, uriInfo);
    }

    @Test
    //@Ignore
    public void matchRoleTest() throws ParseException {
        Set<String> samlRoles = new HashSet<String>();
        samlRoles.add("teacher");
        samlRoles.add("admin");
        samlRoles.add("principal");
        samlRoles.add("terminator");
        samlRoles.add("nobody");

        Date now = new Date();

        Entity staff = getStaff();

        Set<Entity> edorgs = setupSEOAs(staff.getEntityId(), now.getTime());

        EdOrgHelper edorgHelper = Mockito.mock(EdOrgHelper.class);

        Mockito.when(edorgHelper.locateSEOAs(Matchers.anyString(), Matchers.eq(false))).thenReturn(edorgs);

        Set<String> matchedRoles = resource.matchRoles(staff.getEntityId(), samlRoles);
        Set<String> expectedRoles = new HashSet<String>();
        expectedRoles.add("teacher");
        expectedRoles.add("principal");
        expectedRoles.add("terminator");

        Assert.assertTrue(expectedRoles.equals(matchedRoles));
    }

    Map<String, Object> createSEOA(String classification, String edorg, String staff, String endDate) {
        Map<String, Object> seoa = new HashMap<String, Object>();
        seoa.put("staffClassification", classification);
        seoa.put(EDORG_REF, "edorg1");
        seoa.put(ParameterConstants.STAFF_REFERENCE, staff);
        if(endDate != null)
            seoa.put(ParameterConstants.STAFF_EDORG_ASSOC_END_DATE, endDate);

        return seoa;
    }

    @Test
    public void testBuildEdOrgContextualRoles () {
        Set<Entity> seoas = new HashSet<Entity>();
        seoas.add(createSEOA("LEA1", "IT Admin"));
        seoas.add(createSEOA("LEA1", "Educator"));

        seoas.add(createSEOA("LEA2", "Educator"));
        seoas.add(createSEOA("LEA2", "Educator"));

        Map<String, Set<String>> edOrgRoles = resource.buildEdOrgContextualRoles(seoas);

        Assert.assertNotNull(edOrgRoles);
        Assert.assertEquals(2,edOrgRoles.size());

        Set<String> edOrg1Roles = edOrgRoles.get("LEA1");
        Assert.assertNotNull(edOrg1Roles);
        Assert.assertEquals(2, edOrg1Roles.size());
        Assert.assertTrue(edOrg1Roles.contains("IT Admin"));
        Assert.assertTrue(edOrg1Roles.contains("Educator"));

        Set<String> edOrg2Roles = edOrgRoles.get("LEA2");
        Assert.assertNotNull(edOrg2Roles);
        Assert.assertEquals(1, edOrg2Roles.size());
        Assert.assertTrue(edOrg2Roles.contains("Educator"));

    }

    @Test
    public void testInvalidEdOrgRoles() {
        Map<String, Set<String>> edOrgRoles = resource.buildEdOrgContextualRoles(null);

        Assert.assertNotNull(edOrgRoles);
        Assert.assertEquals(0,edOrgRoles.size());
    }


    private Entity getStaff() {
        Repository repo = resource.getRepository();

        Map<String, Object> staff = new HashMap<String, Object>();
        staff.put("staffUniqueStateId", testStaffId);
        repo.create(EntityNames.STAFF, staff);

        NeutralQuery staffQuery = new NeutralQuery();
        staffQuery.addCriteria(new NeutralCriteria(ParameterConstants.STAFF_UNIQUE_STATE_ID, NeutralCriteria.OPERATOR_EQUAL, testStaffId));

        Entity staffEntity = (Entity)repo.findOne("staff", staffQuery);

        return staffEntity;
    }

    private Entity createSEOA(String edorgId, String staffClassification) {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put(ParameterConstants.STAFF_EDORG_ASSOC_STAFF_CLASSIFICATION, staffClassification);
        body.put(ParameterConstants.EDUCATION_ORGANIZATION_REFERENCE, edorgId);
        body.put(ParameterConstants.STAFF_REFERENCE, "staff1");

        return new MongoEntity(EntityNames.STAFF_ED_ORG_ASSOCIATION, "0", body, null);

    }

    private Set<Entity>  setupSEOAs(String staffId, long time) {
        Repository repo = resource.getRepository();
        Set<Entity> res = new HashSet<Entity>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date(time);
        long milSecInAYear = 31557600000L;
        long milSecInADay = 86400000L;

        String dateString = df.format(date);

        res.add((Entity) repo.create(EntityNames.STAFF_ED_ORG_ASSOCIATION, createSEOA("teacher", "edorg1", staffId, dateString)));

        date = new Date(time - milSecInAYear);
        dateString = df.format(date);
        res.add((Entity) repo.create(EntityNames.STAFF_ED_ORG_ASSOCIATION, createSEOA("admin", "edorg1", staffId, dateString)));

        date = new Date(time + milSecInADay);
        dateString = df.format(date);
        res.add((Entity) repo.create(EntityNames.STAFF_ED_ORG_ASSOCIATION, createSEOA("teacher", "edorg1", staffId, dateString)));

        date = new Date(time + milSecInAYear);
        dateString = df.format(date);
        res.add((Entity) repo.create(EntityNames.STAFF_ED_ORG_ASSOCIATION, createSEOA("principal", "edorg2", staffId, dateString)));
        res.add((Entity) repo.create(EntityNames.STAFF_ED_ORG_ASSOCIATION, createSEOA("prophet", "edorg2", staffId, dateString)));

        res.add((Entity) repo.create(EntityNames.STAFF_ED_ORG_ASSOCIATION, createSEOA("terminator", "edorg2", staffId, null)));

        return res;
    }
}
