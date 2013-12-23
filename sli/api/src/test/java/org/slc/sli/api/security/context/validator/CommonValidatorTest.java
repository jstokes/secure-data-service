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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slc.sli.api.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import org.slc.sli.api.resources.SecurityContextInjector;
import org.slc.sli.api.security.roles.SecureRoleRightAccessImpl;
import org.slc.sli.api.test.WebContextTestExecutionListener;
import org.slc.sli.common.constants.EntityNames;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.MongoEntity;

/**
 * Tests common.
 *
 * @author kmyers
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
public class CommonValidatorTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private SecurityContextInjector injector;

    private Collection<IContextValidator> validators;
    private Set<String> ignored;
    private List<String> messages;
    private Set<String> globalEntities;

    @Before
    public void init() {
        validators = context.getBeansOfType(IContextValidator.class).values();
        ignored = new HashSet<String>();
        messages = new LinkedList<String>();
        globalEntities = new HashSet<String>();

        ignored.add(EntityNames.ADMIN_DELEGATION);
        ignored.add(EntityNames.AGGREGATION);
        ignored.add(EntityNames.AGGREGATION_DEFINITION);
        ignored.add(EntityNames.APPLICATION);
        ignored.add(EntityNames.APPLICATION_AUTHORIZATION);
        ignored.add(EntityNames.ASSESSMENT_FAMILY);
        ignored.add(EntityNames.ASSESSMENT_PERIOD_DESCRIPTOR);
        ignored.add(EntityNames.COMPETENCY_LEVEL_DESCRIPTOR_TYPE);
        ignored.add(EntityNames.OBJECTIVE_ASSESSMENT);
        ignored.add(EntityNames.REALM);
        ignored.add(EntityNames.STUDENT_OBJECTIVE_ASSESSMENT);
        ignored.add(EntityNames.SEARCH);
        ignored.add(EntityNames.CALENDAR_DATE);
        ignored.add(EntityNames.SECURITY_EVENT);
        ignored.add(EntityNames.CLASS_PERIOD);
        ignored.add(EntityNames.BELL_SCHEDULE);
        ignored.add(EntityNames.MEETING_TIME);

        globalEntities.add(EntityNames.ASSESSMENT);
        globalEntities.add(EntityNames.COMPETENCY_LEVEL_DESCRIPTOR);
        globalEntities.add(EntityNames.COURSE);
        globalEntities.add(EntityNames.COURSE_OFFERING);
        globalEntities.add(EntityNames.GRADING_PERIOD);
        globalEntities.add(EntityNames.GRADUATION_PLAN);
        globalEntities.add(EntityNames.LEARNING_OBJECTIVE);
        globalEntities.add(EntityNames.LEARNING_STANDARD);
        globalEntities.add(EntityNames.SESSION);
        globalEntities.add(EntityNames.STUDENT_COMPETENCY_OBJECTIVE);
        globalEntities.add(EntityNames.CLASS_PERIOD);
        globalEntities.add(EntityNames.BELL_SCHEDULE);
        globalEntities.add(EntityNames.MEETING_TIME);
    }

    @After
    public void tearDown() {
        SecurityContextHolder.clearContext();
        validators.clear();
        ignored.clear();
        messages.clear();
        globalEntities.clear();
    }

    @Test
    public void verifyNumberOfStaffValidatorsForEachEntity() throws Exception {
        SecurityUtil.setUserContext(SecurityUtil.UserContext.STAFF_CONTEXT);
        setupCurrentUser(new MongoEntity("staff", new HashMap<String, Object>()));
        validateValidators();
    }

    @Test
    public void verifyNumberOfTeacherValidatorsForEachEntity() throws Exception {
        SecurityUtil.setUserContext(SecurityUtil.UserContext.TEACHER_CONTEXT);
        setupCurrentUser(new MongoEntity("teacher", new HashMap<String, Object>()));
        validateValidators();
    }

    @Test
    public void verifyNumberOfStudentValidatorsForEachEntity() throws Exception {
        SecurityUtil.setUserContext(SecurityUtil.UserContext.NO_CONTEXT);
        MongoEntity student = new MongoEntity("student", new HashMap<String, Object>());
        injector.setCustomContext("Studentious","Stendarious","Myrran",Arrays.asList(SecureRoleRightAccessImpl.STUDENT),student,"High Elves");
        validateValidators();
    }
    
    @Test
    public void verifyNumberOfParentValidatorsForEachEntity() throws Exception {
        MongoEntity parent = new MongoEntity("parent", new HashMap<String, Object>());
        injector.setCustomContext("Parentious", "Stendarious' Dad", "Myrran", Arrays.asList(SecureRoleRightAccessImpl.PARENT), parent, "High Elves's Dad");
        validateValidators();
    }


    /**
     * Validate that an {@link IllegalArgumentException} is thrown when the entity type is null
     *
     * @throws Exception
     */
    @Test
    public void testNullParms() throws Exception {
        List<String> entities = getEntityNames();
        for (String entity : entities) {
            for (String type : new String[] { "teacher", "staff" }) {
                setupCurrentUser(new MongoEntity(type, new HashMap<String, Object>()));
                for (Boolean isTransitive : new Boolean[] { true, false }) {
                    for (IContextValidator validator : validators) {
                        if (validator.canValidate(entity, isTransitive)) {
                            try {
                                validator.validate(null, new HashSet<String>(Arrays.asList("blah")));
                                Assert.fail("Expected IllegalArgumentException for " + validator + " validating "
                                        + entity);
                            } catch (IllegalArgumentException e) {
                                // expected
                            } catch (IllegalStateException e) {
                                // expected for specified types
                                if (!globalEntities.contains(entity)) {
                                    messages.add("Caught illegal state exception in the wrong place --> entity:"
                                            + entity + ", isTransitive: " + isTransitive);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (messages.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (String message : messages) {
                builder.append(message);
            }
            Assert.fail(builder.toString());
        }
    }

    /**
     * Validate that an {@link IllegalArgumentException} is thrown if the entity type
     * is wrong.
     *
     * @throws Exception
     */
    @Test
    public void testNonMatchingTypeParms() throws Exception {
        List<String> entities = getEntityNames();
        for (String entity : entities) {
            for (String type : new String[] { "teacher", "staff" }) {
                setupCurrentUser(new MongoEntity(type, new HashMap<String, Object>()));
                for (Boolean isTransitive : new Boolean[] { true, false }) {
                    for (IContextValidator validator : validators) {
                        if (validator.canValidate(entity, isTransitive)) {
                            try {
                                validator.validate("fakeEntity", new HashSet<String>(Arrays.asList("blah")));
                                Assert.fail("Expected IllegalArgumentException for " + validator
                                        + " validating incorrect entity type.");
                            } catch (IllegalArgumentException e) {
                                // expected
                            } catch (IllegalStateException e) {
                                // expected for specified types
                                if (!globalEntities.contains(entity)) {
                                    messages.add("Caught illegal state exception in the wrong place --> entity:"
                                            + entity + ", isTransitive: " + isTransitive);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (messages.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (String message : messages) {
                builder.append(message);
            }
            Assert.fail(builder.toString());
        }
    }

    /**
     * Validate that a validator returns false if the id list is empty or null
     *
     * @throws Exception
     */
    @Test
    public void testEmptyIdList() throws Exception {
        List<String> entities = getEntityNames();
        for (String entity : entities) {
            for (String type : new String[] { "teacher", "staff" }) {
                setupCurrentUser(new MongoEntity(type, new HashMap<String, Object>()));
                for (Boolean isTransitive : new Boolean[] { true, false }) {
                    for (IContextValidator validator : validators) {
                        if (validator.canValidate(entity, isTransitive)) {
                            try {
                                Assert.assertTrue(validator + " must return false for null IDs", validator.validate(entity, null).isEmpty());
                                Assert.assertTrue(validator + " must return false for empty IDs", validator.validate(entity, new HashSet<String>()).isEmpty());
                            } catch (IllegalStateException e) {
                                // expected for specified types
                                if (!globalEntities.contains(entity)) {
                                    messages.add("Caught illegal state exception in the wrong place --> entity:"
                                            + entity + ", isTransitive: " + isTransitive);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (messages.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (String message : messages) {
                builder.append(message);
            }
            Assert.fail(builder.toString());
        }
    }

    /**
     * Set up the principal
     *
     * @param staff
     */
    protected void setupCurrentUser(Entity staff) {
        String user = "fake staff";
        String fullName = "Fake Staff";
        List<String> roles = Arrays.asList(SecureRoleRightAccessImpl.IT_ADMINISTRATOR);
        injector.setCustomContext(user, fullName, "MERPREALM", roles, staff, "111");
    }

    /**
     * Get all the available entity names
     *
     * @return
     * @throws Exception
     */
    protected List<String> getEntityNames() throws Exception {
        List<String> entityNames = new ArrayList<String>();
        Field[] fields = EntityNames.class.getDeclaredFields();

        for (Field f : fields) {
            if (f.getType() == String.class && Modifier.isStatic(f.getModifiers())) {
                entityNames.add((String) f.get(null));
            }
        }
        return entityNames;
    }

    private void validateValidators() throws Exception {
        List<String> entities = getEntityNames();
        for (String entity : entities) {
            // skip entities that don't require staff --> entity validation
            if (ignored.contains(entity)) {
                continue;
            }

            for (Boolean isTransitive : Arrays.asList(true, false)) {

                int numValidators = 0;
                for (IContextValidator validator : validators) {
                    if (validator.canValidate(entity, isTransitive)) {
                        numValidators++;
                    }
                }

                if (numValidators != 1) {
                    messages.add("Incorrect number of validators found for entity: " + entity + " [transitive: "
                            + isTransitive + "], (expected:1, actual:" + numValidators + "). \n");
                }
            }
        }

        if (messages.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (String message : messages) {
                System.out.print(message);
                builder.append(message);
            }
            Assert.fail(builder.toString());
        }
    }
}
