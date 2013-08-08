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
import org.slc.sli.common.constants.EntityNames;
import org.slc.sli.api.resources.SecurityContextInjector;
import org.slc.sli.domain.Entity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * @author dkornishev
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class TransitiveTeacherToTeacherSchoolAssociationValidatorTest {

    private static final String CORRECT_ENTITY_TYPE = EntityNames.TEACHER_SCHOOL_ASSOCIATION;
    private static final String SCHOOL_ID = "Myrran";
    private static final String USER_ID = "Master of Magic";

    @Resource
    private TransitiveTeacherToTeacherSchoolAssociationValidator val;

    @Resource
    private SecurityContextInjector injector;

    @Resource
    private ValidatorTestHelper vth;

    @Before
    public void init() {
        injector.setEducatorContext(USER_ID);
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
        this.vth.generateTeacherSchool(USER_ID, SCHOOL_ID);

        Entity tsa = this.vth.generateTeacherSchool("Heru-er", SCHOOL_ID);
        Assert.assertTrue(val.validate(CORRECT_ENTITY_TYPE, Collections.singleton(tsa.getEntityId())).contains(tsa.getEntityId()));

        tsa = this.vth.generateTeacherSchool("Izida", SCHOOL_ID);
        Assert.assertTrue(val.validate(CORRECT_ENTITY_TYPE, Collections.singleton(tsa.getEntityId())).contains(tsa.getEntityId()));

        tsa = this.vth.generateTeacherSchool("Ptah", SCHOOL_ID);
        Assert.assertTrue(val.validate(CORRECT_ENTITY_TYPE, Collections.singleton(tsa.getEntityId())).contains(tsa.getEntityId()));
    }

    @Test
    public void testSuccessMulti() {
        this.vth.generateTeacherSchool(USER_ID, SCHOOL_ID);
        Set<String> ids = new HashSet<String>();

        for (int i = 0; i < 100; i++) {
            ids.add(this.vth.generateTeacherSchool("Thor" + i, SCHOOL_ID).getEntityId());
        }

        Assert.assertTrue(val.validate(CORRECT_ENTITY_TYPE, ids).containsAll(ids));
    }

    @Test
    public void testWrongId() {
        Set<String> idsToValidate = Collections.singleton("Hammerhands");
        Assert.assertFalse(val.validate(CORRECT_ENTITY_TYPE, idsToValidate).containsAll(idsToValidate));

        idsToValidate = Collections.singleton("Nagas");
        Assert.assertFalse(val.validate(CORRECT_ENTITY_TYPE, idsToValidate).containsAll(idsToValidate));

        idsToValidate = Collections.singleton("Phantom Warriors");
        Assert.assertFalse(val.validate(CORRECT_ENTITY_TYPE, idsToValidate).containsAll(idsToValidate));
    }

    @Test
    public void testHeterogenousList() {
        Set<String> idsToValidate = new HashSet<String>(Arrays.asList(this.vth.generateTeacherSchool(USER_ID, SCHOOL_ID).getEntityId(), this.vth.generateTeacherSchool("Ssss'ra", "Arcanus").getEntityId(), this.vth.generateTeacherSchool("Kali", "Arcanus")
                .getEntityId()));
                Assert.assertFalse(val.validate(CORRECT_ENTITY_TYPE, idsToValidate).containsAll(idsToValidate));
    }
}
