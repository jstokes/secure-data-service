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

package org.slc.sli.modeling.xmicomp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * JUnit test for XmiMappingException class.
 */
public class XmiMappingExceptionTest {

    @Test
    public void test() {
        String message = "foo";

        XmiMappingException xmiMappingException = new XmiMappingException(message);

        assertEquals(message, xmiMappingException.getMessage());
    }
}
