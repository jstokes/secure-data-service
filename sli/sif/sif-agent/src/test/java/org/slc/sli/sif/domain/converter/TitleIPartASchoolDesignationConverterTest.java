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
package org.slc.sli.sif.domain.converter;

import junit.framework.Assert;
import openadk.library.student.Title1Status;

public class TitleIPartASchoolDesignationConverterTest {

    private TitleIPartASchoolDesignationConverter converter = new TitleIPartASchoolDesignationConverter();

    // @Test
    // public void testNA() {
    // test(Title1Status.NA, TitleIPartASchoolDesignation.NOT_DESIGNATED.getText());
    // }
    //
    // @Test
    // public void testSchoolwide() {
    // test(Title1Status.SCHOOLWIDE, TitleIPartASchoolDesignation.PART_A_SCHOOLWIDE.getText());
    // }
    //
    // @Test
    // public void testTargeted() {
    // test(Title1Status.TARGETED, TitleIPartASchoolDesignation.TARGETED.getText());
    // }

    private void test(Title1Status status, String expected) {
        String result = converter.convert(Title1Status.NA);
        Assert.assertEquals(expected, result);
    }
}
