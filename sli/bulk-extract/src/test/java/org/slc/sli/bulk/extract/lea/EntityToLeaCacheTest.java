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

package org.slc.sli.bulk.extract.lea;

import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class EntityToLeaCacheTest {
    
    private EntityToLeaCache cache;
    
    @Before
    public void setUp() {
        cache = new EntityToLeaCache();
    }
    
    @After
    public void tearDown() {
        
    }
    
    @Test
    public void testAddEntryToCache() {
        cache.addEntry("entityId", "LEAid");
        Assert.assertTrue(((Set<String>) cache.getEntriesById("entityId")).contains("LEAid"));
        Assert.assertTrue(cache.getEntriesById("NotReal").size() == 0);
    }
}

