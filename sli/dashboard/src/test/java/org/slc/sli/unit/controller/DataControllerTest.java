/*
 * Copyright 2012 Shared Learning Collaborative, LLC
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


package org.slc.sli.unit.controller;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.slc.sli.entity.GenericEntity;
import org.slc.sli.manager.component.impl.CustomizationAssemblyFactoryImpl;
import org.slc.sli.web.controller.DataController;
import org.slc.sli.web.entity.SafeUUID;

/**
 * Testing data controller
 *
 * @author ccheng
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/application-context.xml" })
public class DataControllerTest {

    private final String componentId = "abc01";
    private final String rawId = "abcd-abcd-abcd-abcd-abcd";

    private MockHttpServletRequest request;
    private DataController dataController;
    private SafeUUID id;

    // partially mock CustomizationAssemblyFactoryImpl
    CustomizationAssemblyFactoryImpl dataFactory = new CustomizationAssemblyFactoryImpl() {

        @Override
        public GenericEntity getDataComponent(String componentId, Object entityKey) {
            GenericEntity simpleEntity = new GenericEntity();
            simpleEntity.put("id", componentId);
            return simpleEntity;
        }
    };

    @Before
    public void setup() {
        request = new MockHttpServletRequest();
        dataController = new DataController();
        id = new SafeUUID(rawId);
    }

    @Test
    public void testHandleValidId() throws Exception {

        dataController.setCustomizedDataFactory(dataFactory);

        GenericEntity res = dataController.handle(componentId, id, request);
        Assert.assertEquals(componentId, res.getId());
    }
}
