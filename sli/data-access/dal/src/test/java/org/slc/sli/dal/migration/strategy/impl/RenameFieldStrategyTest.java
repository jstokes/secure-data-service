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

package org.slc.sli.dal.migration.strategy.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slc.sli.dal.migration.strategy.MigrationException;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.MongoEntity;

/**
 * Tests the migration code responsible for adding new fields with or without a
 * default value.
 * 
 * 
 * @author kmyers
 *
 */
public class RenameFieldStrategyTest {
    
    private RenameFieldStrategy addStrategy;
    private Entity testEntity;
    
    @Before
    public void init() {
        this.addStrategy = new RenameFieldStrategy();
        this.testEntity = this.createTestEntity();
    }
    
    private Entity createTestEntity() {
        String entityType = "Type";
        String entityId = "ID";
        Map<String, Object> body = new HashMap<String, Object>();
        Map<String, Object> metaData = new HashMap<String, Object>();
        body.put("nested", new HashMap<String, Object>());
        
        return new MongoEntity(entityType, entityId, body, metaData);
    }

    /*
     * no field name specified (non-null map of parameters)
     */
    @Test(expected = MigrationException.class)
    public void testBadParams1() throws MigrationException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(RenameFieldStrategy.OLD_FIELD_NAME, "foo");
        this.addStrategy.setParameters(params);
    }

    /*
     * no field name specified (non-null map of parameters)
     */
    @Test(expected = MigrationException.class)
    public void testBadParams2() throws MigrationException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(RenameFieldStrategy.NEW_FIELD_NAME, "foo");
        this.addStrategy.setParameters(params);
    }
    
    /*
     * no field name specified (null map of parameters)
     */
    @Test(expected = MigrationException.class)
    public void testNullParams() throws MigrationException {
        this.addStrategy.setParameters(null);
    }

    @Test
    public void testSimpleRenameWithFieldMissing() throws MigrationException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(RenameFieldStrategy.OLD_FIELD_NAME, "foo");
        parameters.put(RenameFieldStrategy.NEW_FIELD_NAME, "bar");
        
        this.addStrategy.setParameters(parameters);
        this.addStrategy.migrate(this.testEntity);
        
        assertTrue(this.testEntity.getBody().containsKey("bar"));
        assertTrue(this.testEntity.getBody().get("bar") == null);
        assertFalse(this.testEntity.getBody().containsKey("foo"));
    }

    @Test
    public void testSimpleRenameWithFieldPresent() throws MigrationException {
        Object testData = "testString";
        
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(RenameFieldStrategy.OLD_FIELD_NAME, "foo");
        parameters.put(RenameFieldStrategy.NEW_FIELD_NAME, "bar");
        this.testEntity.getBody().put("foo", testData);
        
        this.addStrategy.setParameters(parameters);
        this.addStrategy.migrate(this.testEntity);
        
        assertTrue(this.testEntity.getBody().containsKey("bar"));
        assertTrue(this.testEntity.getBody().get("bar").equals(testData));
        assertFalse(this.testEntity.getBody().containsKey("foo"));
    }
}
