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


package org.slc.sli.ingestion.routes;

import java.util.List;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slc.sli.ingestion.FileFormat;
import org.slc.sli.ingestion.processors.ControlFilePreProcessor;
import org.slc.sli.ingestion.processors.NoExtractProcessor;
import org.slc.sli.ingestion.processors.ZipFileProcessor;

/**
 * RouteBuilder class to create file polling routes
 * for a list of landing zone paths.
 *
 * This class must be instantiated every time file polling routes need to be added.
 *
 * @author jtully
 *
 */
public class LandingZoneRouteBuilder extends RouteBuilder {

    //TODO is it possible to inject these?
    private ZipFileProcessor zipFileProcessor;
    private ControlFilePreProcessor controlFilePreProcessor;
    private NoExtractProcessor noExtractProcessor;

    private List<String> landingZonePaths;

    public static final String CTRL_POLLER_PREFIX = "ctlFilePoller-";
    public static final String ZIP_POLLER_PREFIX = "zipFilePoller-";

    private String workItemQueueUri;

    /**
     * @param landingZonePaths, the landing zone directories to poll
     * @param workItemQueueUri, the URI for the main ingestion queue
     * @param zipFileProcessor, the ingestion zip processor
     * @param controlFilePreProcessor, the ingestion controlFilePreProcessor
     */
    public LandingZoneRouteBuilder(List<String> landingZonePaths, String workItemQueueUri,
            ZipFileProcessor zipFileProcessor, ControlFilePreProcessor controlFilePreProcessor, NoExtractProcessor noExtractProcessor) {

        this.landingZonePaths = landingZonePaths;
        this.zipFileProcessor = zipFileProcessor;
        this.controlFilePreProcessor = controlFilePreProcessor;
        this.noExtractProcessor = noExtractProcessor;
        this.workItemQueueUri = workItemQueueUri;
    }

    @Override
    public void configure() throws Exception {
        for (String inboundDir : landingZonePaths) {
            log.info("Configuring route for landing zone: {} ", inboundDir);
            // routeId: ctlFilePoller-inboundDir
            from(
                    "file:" + inboundDir + "?include=^(.*)\\." + FileFormat.CONTROL_FILE.getExtension()
            + "&delete=true"
            + "&readLock=changed&readLockCheckInterval=1000")
                    .routeId(CTRL_POLLER_PREFIX + inboundDir)
                    .log(LoggingLevel.INFO, "CamelRouting", "Control file detected. Routing to ControlFilePreProcessor.")
                    .process(controlFilePreProcessor)
                    .to(workItemQueueUri);

            // routeId: zipFilePoller-inboundDir
            from(
                    "file:" + inboundDir + "?include=^(.*)\\." + FileFormat.ZIP_FILE.getExtension()
            + "$&exclude=\\.in\\.*&preMove="
                            + inboundDir + "/.done&moveFailed=" + inboundDir
                            + "/.error"
                            + "&readLock=changed&readLockCheckInterval=1000" + "&delete=true")
                    .routeId(ZIP_POLLER_PREFIX + inboundDir)
                    .log(LoggingLevel.INFO, "CamelRouting", "Zip file detected. Routing to ZipFileProcessor.")
                    .process(zipFileProcessor)
                    .choice()
                    .when(header("hasErrors").isEqualTo(true))
                        .to("direct:stop")
                    .otherwise()
                .log(LoggingLevel.INFO, "CamelRouting", "No errors in zip file. Routing to ControlFilePreProcessor.")
                        .process(controlFilePreProcessor)
                                    .to(workItemQueueUri);
            from(
                    "file:" + inboundDir + "?include=^(.*)\\.noextract$" + "&move=" + inboundDir
                            + "/.done/${file:onlyname}.${date:now:yyyyMMddHHmmssSSS}" + "&moveFailed=" + inboundDir
                            + "/.error/${file:onlyname}.${date:now:yyyyMMddHHmmssSSS}"
                + "&readLock=changed&readLockCheckInterval=1000")
                    .routeId("noextract-" + inboundDir)
                .log(LoggingLevel.INFO, "CamelRouting",
                        "No-extract command file detected. Routing to NoExtractProcessor.").process(noExtractProcessor)
            .to("direct:postExtract");
        }
    }
}
