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


package org.slc.sli.ingestion.model;

import java.util.Date;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import org.slc.sli.ingestion.util.BatchJobUtils2;

/**
 *
 * @author dduran
 *
 */
@Document
public final class Error {

    @Indexed
    private String batchJobId;

    private String stageName;

    private String resourceId;

    private String sourceIp;

    private String hostname;

    private String recordIdentifier;

    private Date timestamp;

    private String severity;

    private String errorType;

    private String errorDetail;

    // mongoTemplate requires this constructor.
    public Error() {
    }

    public Error(String batchJobId, String stageName, String resourceId, String sourceIp, String hostname,
            String recordIdentifier, Date timestamp, String severity, String errorType, String errorDetail) {
        this.batchJobId = batchJobId;
        this.stageName = stageName;
        this.resourceId = resourceId;
        this.sourceIp = sourceIp;
        this.hostname = hostname;
        this.recordIdentifier = recordIdentifier;
        this.timestamp = new Date(timestamp.getTime());
        this.severity = severity;
        this.errorType = errorType;
        this.errorDetail = errorDetail;
    }

    // TODO: too many params. refactor.
    public static Error createIngestionError(String ingestionJobId, String resourceId, String stageName,
            String sourceIp, String hostname, String recordIdentifier, String severity, String errorType,
            String errorDetail) {

        String theSourceIp = sourceIp;
        if (theSourceIp == null) {
            theSourceIp = BatchJobUtils2.getHostAddress();
        }

        String theHostname = hostname;
        if (theHostname == null) {
            theHostname = BatchJobUtils2.getHostName();
        }

        Error error = new Error(ingestionJobId, stageName, resourceId, theSourceIp, theHostname, recordIdentifier,
                BatchJobUtils2.getCurrentTimeStamp(), severity, errorType, errorDetail);

        return error;
    }

    public String getBatchJobId() {
        return batchJobId;
    }

    public void setBatchJobId(String batchJobId) {
        this.batchJobId = batchJobId;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getRecordIdentifier() {
        return recordIdentifier;
    }

    public void setRecordIdentifier(String recordIdentifier) {
        this.recordIdentifier = recordIdentifier;
    }

    public Date getTimestamp() {
        return new Date(timestamp.getTime());
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = new Date(timestamp.getTime());
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }

}
