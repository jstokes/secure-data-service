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

package org.slc.sli.modeling.xmigen;

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After;
import org.slc.sli.modeling.uml.ClassType;
import org.slc.sli.modeling.uml.Identifier;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

/** 
* Xsd2UmlLookup Tester. 
* 
* @author <Authors name> 
* @since <pre>Sep 11, 2012</pre> 
* @version 1.0 
*/ 
public class Xsd2UmlLookupTest { 
    private Xsd2UmlLookup<ClassType> testObject;
@Before
public void before() throws Exception {
   testObject = new Xsd2UmlLookup<ClassType>();
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: from(final T key) 
* 
*/ 
@Test
public void testFrom() throws Exception {
    ClassType classType =  mock(ClassType.class);
    Identifier id = testObject.from(classType);
    assertNotNull(id);
    Identifier dupId = testObject.from(classType);
    assertEquals(id,dupId);
}

@Test(expected = IllegalArgumentException.class)
public void testFormQualifiedName() throws Exception {
    testObject.from(null);
}

} 
