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

package org.slc.sli.ingestion.reporting;

import org.springframework.stereotype.Component;

/**
 * Dummy implementation of the AbstractMessageReport. Do not report warnings/errors.
 *
 * @author okrook
 *
 */
@Component
public class DummyMessageReport extends AbstractMessageReport {

    @Override
    public void reportError(ReportStats reportStats, MessageCode code, Object... args) {
        // Do nothing
    }

    @Override
    public void reportWarning(ReportStats reportStats, MessageCode code, Object... args) {
     // Do nothing
    }
}
