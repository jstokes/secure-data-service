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
import org.springframework.stereotype.Component;

/**
 * MessageReport implementation that is constructed with a logger and uses that logger to act on
 * errors and warnings
 *
 * @author npandey
 *
 */
@Component
public class LoggingMessageReport extends AbstractMessageReport {

    private Logger logger;  //NOPMD logger is a member variable and should not be final by design

    public LoggingMessageReport(Logger logger) {
        this.logger = logger;
    }

    public LoggingMessageReport() {
        //a compnent needs a default constructor
    }

    @Override
    protected void reportError(AbstractReportStats reportStats, Source source, MessageCode code, Object... args) {
        logger.error(getMessage(reportStats, source, code, args));
    }

    @Override
    protected void reportWarning(AbstractReportStats reportStats, Source source, MessageCode code, Object... args) {
        logger.warn(getMessage(reportStats, source, code, args));
    }

    @Override
    protected void reportInfo(AbstractReportStats reportStats, Source source, MessageCode code, Object... args) {
        logger.info(getMessage(reportStats, source, code, args));
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

}
