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
package org.slc.sli.bulk.extract.context.resolver;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class TypeResolverTest {
    
    @Autowired
    TypeResolver resolver;
    
    @Test
    public void educationOrganzationCollectionAlsoContainsSchools() {
        Set<String> types = resolver.resolveType("educationOrganization");
        assertTrue(types.size() == 2);
        assertTrue(types.contains("school"));
        assertTrue(types.contains("educationOrganization"));
        types = resolver.resolveType("staff");
        assertTrue(types.size() == 2);
        assertTrue(types.contains("teacher"));
        assertTrue(types.contains("staff"));
        types = resolver.resolveType("non-existance");
        assertTrue(types.size() == 1);
    }
    
}
