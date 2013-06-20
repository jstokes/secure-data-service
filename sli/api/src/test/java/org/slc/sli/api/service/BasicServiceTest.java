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


package org.slc.sli.api.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import org.slc.sli.api.config.BasicDefinitionStore;
import org.slc.sli.api.config.DefinitionFactory;
import org.slc.sli.api.config.EntityDefinition;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.SecurityContextInjector;
import org.slc.sli.api.test.WebContextTestExecutionListener;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.QueryParseException;
import org.slc.sli.domain.Repository;

/**
 *
 * Unit Tests
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
public class BasicServiceTest {
    private BasicService service = null; //class under test

    @Autowired
    private ApplicationContext context;

    @Autowired
    private SecurityContextInjector securityContextInjector;

    @Autowired
    private BasicDefinitionStore definitionStore;

    @Autowired
    DefinitionFactory factory;

    @Autowired
    @Qualifier("validationRepo")
    private Repository<Entity> securityRepo;

    @Before
    public void setup() {
        service = (BasicService) context.getBean("basicService", "student", null, securityRepo);

        EntityDefinition student = factory.makeEntity("student")
                .exposeAs("students").build();

        service.setDefn(student);
    }

    @Test
    public void testCheckFieldAccessAdmin() {
        // inject administrator security context for unit testing
        securityContextInjector.setAdminContextWithElevatedRights();

        NeutralQuery query = new NeutralQuery();
        query.addCriteria(new NeutralCriteria("economicDisadvantaged", "=", "true"));

        NeutralQuery query1 = new NeutralQuery(query);

        service.checkFieldAccess(query, false);
        assertTrue("Should match", query1.equals(query));
    }

    @Test (expected = QueryParseException.class)
    public void testCheckFieldAccessEducator() {
        // inject administrator security context for unit testing
        securityContextInjector.setEducatorContext();

        NeutralQuery query = new NeutralQuery();
        query.addCriteria(new NeutralCriteria("economicDisadvantaged", "=", "true"));

        service.checkFieldAccess(query, false);
    }

    @Test
    public void testWriteSelf() {
        BasicService basicService = (BasicService) context.getBean("basicService", "teacher", new ArrayList<Treatment>(), securityRepo);
        basicService.setDefn(definitionStore.lookupByEntityType("teacher"));
        securityContextInjector.setEducatorContext("my-id");

        Map<String, Object> body = new HashMap<String, Object>();
        Entity entity = securityRepo.create("teacher", body);

        EntityBody updated = new EntityBody();
        basicService.update(entity.getEntityId(), updated);
    }

    @Test
    public void testIsSelf() {
        BasicService basicService = (BasicService) context.getBean("basicService", "teacher", new ArrayList<Treatment>(), securityRepo);
        basicService.setDefn(definitionStore.lookupByEntityType("teacher"));
        securityContextInjector.setEducatorContext("my-id");
        assertTrue(basicService.isSelf(new NeutralQuery(new NeutralCriteria("_id", NeutralCriteria.OPERATOR_EQUAL, "my-id"))));
        NeutralQuery query = new NeutralQuery(new NeutralCriteria("_id", NeutralCriteria.CRITERIA_IN, Arrays.asList("my-id")));
        assertTrue(basicService.isSelf(query));
        query.addCriteria(new NeutralCriteria("someOtherProperty", NeutralCriteria.OPERATOR_EQUAL, "somethingElse"));
        assertTrue(basicService.isSelf(query));
        query.addOrQuery(new NeutralQuery(new NeutralCriteria("refProperty", NeutralCriteria.OPERATOR_EQUAL, "my-id")));
        assertTrue(basicService.isSelf(query));
        query.addOrQuery(new NeutralQuery(new NeutralCriteria("_id", NeutralCriteria.OPERATOR_EQUAL, "someoneElse")));
        assertFalse(basicService.isSelf(query));
        assertFalse(basicService.isSelf(new NeutralQuery(new NeutralCriteria("_id", NeutralCriteria.CRITERIA_IN, Arrays.asList("my-id", "someoneElse")))));

    }

}
