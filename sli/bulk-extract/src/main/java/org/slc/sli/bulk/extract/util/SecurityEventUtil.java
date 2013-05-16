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
package org.slc.sli.bulk.extract.util;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import org.slc.sli.common.util.logging.LogLevelType;
import org.slc.sli.common.util.logging.SecurityEvent;
import org.slc.sli.common.util.tenantdb.TenantContext;

/**
 * Util class to create a security event.
 *
 * @author npandey
 */
@Component
public class SecurityEventUtil {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityEventUtil.class);

    /**
     * Utility method to create a security event object.
     *
     * @param loggingClass
     *            name of the originating class
     * @param logMessage
     *            security event log message
     * @param actionDesc
     *            description of action being performed
     * @param logLevel
     *            log level of the security event message
     *
     * @return SecurityEvent
     *            new SecurityEvent associated with parameters
     */
    public static SecurityEvent createSecurityEvent(String loggingClass, String logMessage,
            String actionDesc, LogLevelType logLevel) {
        SecurityEvent event = new SecurityEvent();

        event.setTenantId(TenantContext.getTenantId());
        event.setAppId("BulkExtract");
        event.setActionUri(actionDesc);
        event.setTimeStamp(new Date());
        event.setProcessNameOrId(ManagementFactory.getRuntimeMXBean().getName());
        try {
            event.setExecutedOn(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            LOG.info("Could not find hostname/process for SecurityEventLogging!");
            event.setExecutedOn("localhost");
        }
        event.setClassName(loggingClass);
        event.setLogLevel(logLevel);
        event.setLogMessage(logMessage);

        LOG.debug(event.toString());

        return event;
    }
}
