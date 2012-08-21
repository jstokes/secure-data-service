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

package org.slc.sli.modeling.rest;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for Option class.
 *
 * @author wscott
 *
 */
public class OptionTest {

    private Option option; //class under test

    private static final String VALUE = "option-test";
    private static final ArrayList<Documentation> DOC = new ArrayList<Documentation>(0);

    @Before
    public void setup() {
        option = new Option(VALUE, DOC);
    }

    @Test (expected = NullPointerException.class)
    public void testNullValue() {
        new Option(null, DOC);
    }

    @Test (expected = NullPointerException.class)
    public void testNullDocumentation() {
        new Option(VALUE, null);
    }

    @Test
    public void testGetValue() {
        assertEquals(VALUE, option.getValue());
    }

    @Test
    public void testGetDocumentation() {
        assertEquals(DOC, option.getDocumentation());
    }

}
