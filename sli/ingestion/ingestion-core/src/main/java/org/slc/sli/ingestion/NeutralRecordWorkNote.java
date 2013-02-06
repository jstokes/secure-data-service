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

package org.slc.sli.ingestion;

import java.util.List;

/**
 *
 * @author npandey
 *
 */

public class NeutralRecordWorkNote extends WorkNote {

    private static final long serialVersionUID = 1L;

    private List<NeutralRecord> neutralRecords;

    public NeutralRecordWorkNote(List<NeutralRecord> neutralRecords, String batchJobId, String tenantId, boolean hasErrors) {
        super(batchJobId, tenantId, hasErrors);
        this.neutralRecords = neutralRecords;
    }

    public List<NeutralRecord> getNeutralRecords() {
        return neutralRecords;
    }

    public void setNeutralRecords(List<NeutralRecord> neutralRecords) {
        this.neutralRecords = neutralRecords;
    }

}
