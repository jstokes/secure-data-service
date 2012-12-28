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

/**
 * Represents a source of the report. Implementations should contain everything that is necessary
 * for a Source to be understood by users.
 *
 * @author okrook
 *
 */
public interface Source {

    /**
     * @return the batch job id associated with this Source.
     */
    String getBatchJobId();

    /**
     *
     * @return the resource id associated with this Source.
     */
    String getResourceId();

    /**
     * @return the ingestion processing stage for this Source.
     */
    String getStageName();

    /**
     *
     * @return a user-friendly description of this Source.
     */
    String getUserFriendlyMessage();
}
