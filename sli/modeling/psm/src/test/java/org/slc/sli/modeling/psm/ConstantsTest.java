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

package org.slc.sli.modeling.psm;

import org.junit.Test;
import org.slc.sli.modeling.psm.helpers.PsmHelpers;
import org.slc.sli.modeling.psm.helpers.SliMongoConstants;
import org.slc.sli.modeling.psm.helpers.SliUmlConstants;
import org.slc.sli.modeling.psm.helpers.TagName;

import static junit.framework.Assert.assertNotNull;

/**
 */
public class ConstantsTest {
    @Test
    public void testInit() {
        final PsmHelpers helpers = new PsmHelpers();
        assertNotNull(helpers);
        assertNotNull(SliMongoConstants.NAMESPACE_SLI);
        assertNotNull(SliUmlConstants.TAGDEF_APPLY_NATURAL_KEYS);
        assertNotNull(TagName.DOCUMENTATION);
    }
}
