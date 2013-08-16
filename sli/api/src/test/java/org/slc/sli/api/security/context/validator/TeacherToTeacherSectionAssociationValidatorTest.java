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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slc.sli.api.util.SecurityUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.slc.sli.common.constants.EntityNames;
import org.slc.sli.api.resources.SecurityContextInjector;
import org.slc.sli.domain.Entity;

/**
 *
 * @author dkornishev
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class TeacherToTeacherSectionAssociationValidatorTest {

    private static final String CORRECT_ENTITY_TYPE = EntityNames.TEACHER_SECTION_ASSOCIATION;
    private static final String USER_ID = "Master of Magic";
    private static final String SECTION_ID = "Sorcery Node";

    @Resource
    private TeacherToTeacherSectionAssociationValidator val;

    @Resource
    private SecurityContextInjector injector;

    @Resource
    private ValidatorTestHelper vth;

    @Before
    public void init() {
        injector.setEducatorContext(USER_ID);
        SecurityUtil.setUserContext(SecurityUtil.UserContext.TEACHER_CONTEXT);
    }

    @After
    public void cleanUp() {
        SecurityContextHolder.clearContext();
        try {
            vth.resetRepo();
        } catch (Exception e) {
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateWrongType() {
        val.validate(EntityNames.ASSESSMENT, new HashSet<String>(Arrays.asList("Jomolungma")));
    }

    @Test
    public void testSuccessOne() {
        Entity tsa = this.vth.generateTSA(USER_ID, SECTION_ID, false);
        Set<String> ids =  Collections.singleton(tsa.getEntityId());
        Assert.assertTrue(val.validate(CORRECT_ENTITY_TYPE, ids).equals(ids));
    }

    @Test
    public void testSuccessMulti() {
        Set<String> ids = new HashSet<String>();

        for (int i = 0; i < 100; i++) {
            ids.add(this.vth.generateTSA(USER_ID, SECTION_ID, false).getEntityId());
        }

        Assert.assertTrue(val.validate(CORRECT_ENTITY_TYPE, ids).equals(ids));
    }

    @Test
    public void testWrongId() {
        Set<String> ids = Collections.singleton("Hammerhands");
        Assert.assertFalse(val.validate(CORRECT_ENTITY_TYPE, ids).equals(ids));

        ids =  Collections.singleton("Nagas");
        Assert.assertFalse(val.validate(CORRECT_ENTITY_TYPE,ids).equals(ids));

        ids = Collections.singleton("Phantom Warriors");
        Assert.assertFalse(val.validate(CORRECT_ENTITY_TYPE, ids).equals(ids));
    }

    @Test
    public void testHeterogenousList() {
        Set<String> ids = new HashSet<String>(Arrays
                .asList(this.vth.generateTSA(USER_ID, SECTION_ID, false).getEntityId(),
                        this.vth.generateTSA("Ssss'ra", "Arcanus", false).getEntityId(),
                        this.vth.generateTSA("Kali", "Arcanus", false).getEntityId()));
        Assert.assertFalse(val.validate(CORRECT_ENTITY_TYPE, ids).equals(ids));
    }
}
