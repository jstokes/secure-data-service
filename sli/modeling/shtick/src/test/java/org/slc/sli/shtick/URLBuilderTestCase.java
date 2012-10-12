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

package org.slc.sli.shtick;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Unit test for URL builder
 */
public final class URLBuilderTestCase {

    @Test
    public void testIds() throws Exception {
        final URIBuilder builder = URIBuilder.baseUri("http://localhost");
        final List<String> ids = Arrays.asList("1", "2", "3", "4", "5");
        builder.addPath("api/rest/v1").addPath("students").ids(ids);
        final URI url = builder.build();
        assertEquals("the URI should be http://localhost/api/rest/v1/students/1,2,3,4,5", "http://localhost/"
                + "api/rest/v1" + "/students/1,2,3,4,5", url.toString());
    }
}
