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
package org.slc.sli.modeling.uml;


import org.junit.Before;
import org.junit.Test;
import org.slc.sli.modeling.uml.index.DefaultVisitor;

import static org.junit.Assert.assertEquals;

/**
 * JUnit test for Attribute
 * @author chung
 */
public class AttributeTest {

    private Attribute attribute;
    private Identifier identifier = Identifier.random();
    private Range range = new Range(Occurs.ZERO, Occurs.ONE);
    private Visitor visitor = new DefaultVisitor();

    @Before
    public void setup() {
        attribute = new Attribute(identifier, "TestAttribute", identifier,
                new Multiplicity(range), ModelElement.EMPTY_TAGGED_VALUES);
    }

    @Test
    public void testAccept() {
        attribute.accept(visitor);
    }

    @Test
    public void testIsAttribute() {
        assertEquals(attribute.isAttribute(), true);
    }

    @Test
    public void testIsAssociationEnd() {
        assertEquals(attribute.isAssociationEnd(), false);
    }

    @Test
    public void testToString() {
        String string1 = attribute.toString();
        String string2 = "{id: " + identifier.toString() + ", name: TestAttribute, type: " + identifier
                + ", multiplicity: " + new Multiplicity(range) + "}";
        assertEquals(string1, string2);
    }
}
