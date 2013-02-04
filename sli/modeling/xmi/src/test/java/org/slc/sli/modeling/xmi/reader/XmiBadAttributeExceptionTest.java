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

package org.slc.sli.modeling.xmi.reader;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests the proper placement of data into a RuntimeException.
 *
 * @author kmyers
 *
 */
public class XmiBadAttributeExceptionTest {

    @Test
    public void testDataStoredCorrectly() {
        RuntimeException re = new IllegalArgumentException();
        String message = "You're doing it wrong.";

        XmiBadAttributeException xmibae = new XmiBadAttributeException(message, re);
        assertTrue(xmibae.getMessage().equals(message));
        assertTrue(xmibae.getCause() == re);
    }
}
