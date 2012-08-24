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
package org.slc.sli.modeling.xmi;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests the enumeration and its association to string values
 * and the ability to recover the enumeration from the
 * associated string value.
 *
 *
 * @author kmyers
 *
 */
public class XmiElementNameTest {

    @Test
    public void testEnumValues() {
        for (XmiElementName xmiElementName : XmiElementName.values()) {
            String localName = xmiElementName.getLocalName();
            XmiElementName retrievedElementName = XmiElementName.getElementName(localName);
            assertTrue(retrievedElementName == xmiElementName);
            assertTrue(xmiElementName == XmiElementName.valueOf(xmiElementName.toString()));
        }
    }

    @Test
    public void testReturnNullForNoMatchingEnum() {
        assertNull(XmiElementName.getElementName("bogus input"));
    }
}
