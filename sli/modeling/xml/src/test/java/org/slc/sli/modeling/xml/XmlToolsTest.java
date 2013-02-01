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

package org.slc.sli.modeling.xml;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author chung
 */
public class XmlToolsTest {

    @Test
    public void testCollapseWhitespace() {
        String str = XmlTools.collapseWhitespace(" this\ris\na\ttest  string ");
        assertTrue(str.equals("this is a test string"));
    }

    @Test
    public void testCreateObject() {
        @SuppressWarnings("unused")
        XmlTools xmlTools = new XmlTools();
    }

}
