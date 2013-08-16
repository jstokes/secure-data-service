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

package org.slc.sli.api.security.context.validator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slc.sli.api.util.SecurityUtil;
import org.slc.sli.common.constants.EntityNames;
import org.slc.sli.api.resources.SecurityContextInjector;
import org.slc.sli.api.test.WebContextTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

/**
 * Tests the staff to education organization association validator.
 * 
 * 
 * @author kmyers
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
public class StaffToEducationOrganizationValidatorTest {

    @Resource
    private StaffToEducationOrganizationAssociationValidator val;
    
    @Resource
    private SecurityContextInjector injector;
    
    @Before
    public void init() {
        injector.setStaffContext();
    }
    
    @Test
    public void testCanValidate() {
        SecurityUtil.setUserContext(SecurityUtil.UserContext.STAFF_CONTEXT);
        Assert.assertTrue("Must be able to validate", val.canValidate(EntityNames.STAFF_ED_ORG_ASSOCIATION, false));
        Assert.assertFalse("Must not be able to validate", val.canValidate(EntityNames.ADMIN_DELEGATION, false));
    }
    
    @Test
    public void testValidation() {
        Set<String> ids = new HashSet<String>(Arrays.asList("lamb"));
        Assert.assertFalse(val.validate(EntityNames.STAFF_ED_ORG_ASSOCIATION, ids).equals(ids));
    }
}
