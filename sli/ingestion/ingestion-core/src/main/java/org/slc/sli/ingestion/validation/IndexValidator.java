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

package org.slc.sli.ingestion.validation;

import com.mongodb.DB;

import org.slc.sli.ingestion.reporting.AbstractMessageReport;
import org.slc.sli.ingestion.reporting.AbstractReportStats;

/**
 * Checks if the indexes are present for all the dbs before processing this job.
 * This validator fails if index files are not defined, it is only to add an
 * error message in the log file if the index counts do not match.
 *
 * @author unavani
 *
 */
public class IndexValidator extends ComplexValidator<DB> {

    @Override
    public boolean isValid(DB db, AbstractMessageReport report, AbstractReportStats reportStats) {
        boolean isValid = true;

        for (Validator<DB> validator : this.getValidators()) {
            isValid &= validator.isValid(db, report, reportStats);
        }

        return isValid;
    }

}
