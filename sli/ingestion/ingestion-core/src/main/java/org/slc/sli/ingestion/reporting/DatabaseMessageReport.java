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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slc.sli.ingestion.FaultType;
import org.slc.sli.ingestion.model.Error;
import org.slc.sli.ingestion.model.da.BatchJobDAO;
import org.slc.sli.ingestion.util.BatchJobUtils2;

/**
 * Message report that persists errors and warnings to a database and the log.
 *
 * @author dduran
 *
 */
@Component
public class DatabaseMessageReport extends AbstractMessageReport {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseMessageReport.class);

    @Autowired
    private BatchJobDAO batchJobDAO;

    @Override
    protected void reportError(ReportStats reportStats, MessageCode code, Object... args) {
        String message = getMessage(code, args);
        logError(message);

        if (reportStats != null && reportStats.getSource() != null) {
            Source source = reportStats.getSource();

            persistFault(FaultType.TYPE_ERROR, message, source);
        }
    }

    @Override
    protected void reportWarning(ReportStats reportStats, MessageCode code, Object... args) {
        String message = getMessage(code, args);
        logWarning(message);

        if (reportStats != null && reportStats.getSource() != null) {
            Source source = reportStats.getSource();

            persistFault(FaultType.TYPE_WARNING, message, source);
        }
    }

    private void persistFault(FaultType faultType, String message, Source source) {
        Error error = Error.createIngestionError(source.getBatchJobId(), source.getResourceId(), source.getStageName(),
                BatchJobUtils2.getHostName(), BatchJobUtils2.getHostAddress(), null, faultType.getName(),
                faultType.getName(), message);

        batchJobDAO.saveError(error);
    }

    @Override
    protected void logError(MessageCode code, Object... args) {
        LOG.error(getMessage(code, args));
    }

    @Override
    protected void logWarning(MessageCode code, Object... args) {
        LOG.warn(getMessage(code, args));
    }
    
    protected void logError(String message) {
        LOG.error(message);
    }

    protected void logWarning(String message) {
        LOG.warn(message);
    }

}
