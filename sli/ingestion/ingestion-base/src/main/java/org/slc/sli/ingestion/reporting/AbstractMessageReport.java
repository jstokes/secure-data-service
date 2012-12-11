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

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

/**
 * Abstract class for reporting user-facing errors and warnings.
 *
 * @author dduran
 *
 */
public abstract class AbstractMessageReport implements MessageSourceAware {

    protected MessageSource messageSource;

    /**
     * Reports a message as error.
     *
     * @param source
     *            Source of the report
     * @param code
     *            message defined by a code
     * @param args
     *            additional arguments for the message
     */
    public void error(Source source, MessageCode code, Object... args) {
        // implement
    }

    public void error(Source source, ReportStats reportStats, MessageCode code, Object... args) {
        // implement
    }

    /**
     * Reports a message as warning.
     *
     * @param source
     *            Source of the report
     * @param code
     *            message defined by a code
     */
    public void warning(Source source, MessageCode code, Object... args) {
        // implement
    }

    public void warning(Source source, ReportStats reportStats, MessageCode code, Object... args) {
        // implement
    }

    /**
     * Look up the corresponding message for a MessageCode.
     *
     * @param code
     * @param args
     * @return Message String mapped to the provided MessageCode, if one exists, with any args
     *         provided substituted in. If no message is mapped for this code, return #?CODE?# were
     *         CODE is the MessageCode provided.
     */
    protected String getMessage(MessageCode code, Object... args) {
        return messageSource.getMessage(code.getCode(), args, "#?" + code.getCode() + "?#", null);
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

}
