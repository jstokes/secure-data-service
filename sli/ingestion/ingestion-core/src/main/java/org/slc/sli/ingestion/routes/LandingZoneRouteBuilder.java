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

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import org.slc.sli.ingestion.FileFormat;
import org.slc.sli.ingestion.processors.ControlFilePreProcessor;
import org.slc.sli.ingestion.processors.FilePreProcessor;
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
    private FilePreProcessor filePreProcessor;

    private List<String> landingZonePaths;

    public static final String CTRL_POLLER_PREFIX = "ctlFilePoller-";
    public static final String ZIP_POLLER_PREFIX = "zipFilePoller-";

    private final static String invalidCharacters = "?";

    private String workItemQueueUri;

    /**
     * @param landingZonePaths, the landing zone directories to poll
     * @param workItemQueueUri, the URI for the main ingestion queue
     * @param filePreProcessor, the ingestion file preProcessor
     * @param zipFileProcessor, the ingestion zip processor
     * @param controlFilePreProcessor, the ingestion controlFilePreProcessor
     */
    public LandingZoneRouteBuilder(List<String> landingZonePaths, String workItemQueueUri, FilePreProcessor filePreProcessor,
            ZipFileProcessor zipFileProcessor, ControlFilePreProcessor controlFilePreProcessor, NoExtractProcessor noExtractProcessor) {

        this.landingZonePaths = landingZonePaths;
        this.filePreProcessor = filePreProcessor;
        this.zipFileProcessor = zipFileProcessor;
        this.controlFilePreProcessor = controlFilePreProcessor;
        this.noExtractProcessor = noExtractProcessor;
        this.workItemQueueUri = workItemQueueUri;
    }

    @Override
    public void configure() throws Exception {

        for (String inboundDir : landingZonePaths) {
            log.info("Configuring route for landing zone: {} ", inboundDir);

            //Don't create the file poller if failed to create
            //the landing zone or the name is invalid
            if (!isValidDirName(inboundDir) || !createValidDir(inboundDir)) {
                continue;
            }
            // routeId: FilePoller-inboundDir
            from(
                    "file:" + inboundDir + "?include=^(.*)\\.(" + FileFormat.CONTROL_FILE.getExtension()
            + "|" +  FileFormat.ZIP_FILE.getExtension() + ")"
            + "$&exclude=\\.in\\.*&preMove="
            + inboundDir + "/.done&moveFailed=" + inboundDir
            + "/.error"
            + "&readLock=changed&readLockCheckInterval=1000")
                    .log(LoggingLevel.INFO, "CamelRouting", "File detected. Routing FilePreProcessor.")
                    .process(filePreProcessor)
                    .choice()
                        .when(header("hasErrors").isEqualTo(true))
                            .to("direct:stop")
                        .when(header("fileType").isEqualTo(FileFormat.CONTROL_FILE.getExtension()))
                            .to("direct:ctlFilePoller" + inboundDir)
                        .when(header("fileType").isEqualTo(FileFormat.ZIP_FILE.getExtension()))
                            .to("direct:zipFilePoller" + inboundDir);

            // routeId: zipFilePoller-inboundDir
            from("direct:zipFilePoller" + inboundDir)
                .routeId(ZIP_POLLER_PREFIX + inboundDir)
                .log(LoggingLevel.INFO, "CamelRouting", "Zip file detected. Routing to ZipFileProcessor.")
                .process(zipFileProcessor)
                .choice()
                    .when(header("hasErrors").isEqualTo(true))
                        .to("direct:stop")
                    .otherwise()
                        .log(LoggingLevel.INFO, "CamelRouting", "No errors in zip file. Routing to ControlFilePreProcessor.")
                        .process(controlFilePreProcessor)
                        .choice()
                            .when(header("hasErrors").isEqualTo(true))
                                .to("direct:stop")
                            .otherwise()
                                .to(workItemQueueUri);

            // routeId: ctlFilePoller-inboundDir
            from("direct:ctlFilePoller" + inboundDir)
                .routeId(CTRL_POLLER_PREFIX + inboundDir)
                .log(LoggingLevel.INFO, "CamelRouting", "No errors in file preprocessing. Routing to ControlFilePreProcessor.")
                .process(controlFilePreProcessor)
                .choice()
                    .when(header("hasErrors").isEqualTo(true))
                        .to("direct:stop")
                    .otherwise()
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

    /** Creating the directory, so that camel won't complain about
     * directory name with dot(.) in it.
     * @param inboundDir: the absolute directory path to be created
     * @return : true if successfully created the directory.
     **/
   private boolean createValidDir(String inboundDir) {

       File ibDir = new File(inboundDir);
       try {
           FileUtils.forceMkdir(ibDir);
       } catch (IOException e) {
           log.error("Failed to create landing zone: {} ", inboundDir);
           return false;
       }
       return true;
   }

   /**
    * Check if the inboundDir name contains any invalid characters
    *
    * @param inboundDir : directory name to be checked
    * @return : true if directory name doesn't contain any invalid character.
    */
   private boolean isValidDirName(String inboundDir) {
       boolean res = StringUtils.containsNone(inboundDir, invalidCharacters);
       if(!res) {
           log.error("Failed to create file poller, because of invalid characters in {}", inboundDir);
       }

       return res;
   }
}
