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

package org.slc.sli.sif.reporting;

import java.util.Properties;

import openadk.library.ADK;
import openadk.library.ADKException;
import openadk.library.DataObjectOutputStream;
import openadk.library.Event;
import openadk.library.EventAction;
import openadk.library.MessageInfo;
import openadk.library.Publisher;
import openadk.library.PublishingOptions;
import openadk.library.Query;
import openadk.library.SIFDataObject;
import openadk.library.SIFVersion;
import openadk.library.Zone;
import openadk.library.common.CommonDTD;
import openadk.library.common.ExitTypeCode;
import openadk.library.common.GradeLevelCode;
import openadk.library.common.StudentLEARelationship;
import openadk.library.student.LEAInfo;
import openadk.library.student.SchoolInfo;
import openadk.library.student.StudentDTD;
import openadk.library.student.StudentSchoolEnrollment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.slc.sli.sif.agent.SifAgent;
import org.slc.sli.sif.zone.PublishZoneConfigurator;

/**
 * Test agent to trigger event reports
 *
 */
public class EventReporter implements Publisher {

    static {
        // Simple workaround to get logging paths set up correctly when run from the command line
        String catalinaHome = System.getProperty("catalina.home");
        if (catalinaHome == null) {
            System.setProperty("catalina.home", "target");
        }
        String sliConf = System.getProperty("sli.conf");
        if (sliConf == null) {
            System.setProperty("sli.conf", "../../config/properties/sli.properties");
        }
    }

    public static void main(String[] args) throws ADKException {
        Logger logger = LoggerFactory.getLogger(EventReporter.class);

        ADK.initialize();
        ADK.debug = ADK.DBG_ALL;

        try {
            if (args.length == 3) {
                String agentId = args[0];
                String zoneUrl = args[1];
                String localZoneId = args[2];
                boolean reportSchoolLeaInfo = true;
                SifAgent agent = createReporterAgent(agentId, zoneUrl);
                agent.startAgent();
                Zone zone = agent.getZoneFactory().getZone(localZoneId);
                EventReporter reporter = new EventReporter(zone);

                reporter.setEventGenerator(new CustomEventGenerator());
                if (reportSchoolLeaInfo) {
                    reporter.reportSchoolLeaInfoEvents();
                }
            } else if (args.length == 2) {
                SifAgent agent = createReporterAgent("test.publisher.agent", "http://10.163.6.73:50002/TestZone");
                agent.startAgent();
                String zoneId = args[EventReporter.ZONE_ID];
                String messageFile = args[EventReporter.MESSAGE_FILE];
                Zone zone = agent.getZoneFactory().getZone(zoneId);
                EventReporter reporter = new EventReporter(zone);

                reporter.setEventGenerator(new CustomEventGenerator());
                reporter.reportEvent(messageFile);
            } else {
                SifAgent agent = createReporterAgent("test.publisher.agent", "http://10.163.6.73:50002/TestZone");
                agent.startAgent();
                Zone zone = agent.getZoneFactory().getZone("TestZone");
                EventReporter reporter = new EventReporter(zone);
                reporter.reportEvent();
            }
        } catch (Exception e) {
            logger.error("Exception trying to report event", e);
        }
    }

    private static SifAgent createReporterAgent(String agentId, String zoneUrl) {
        Properties agentProperties = new Properties();
        agentProperties.put("adk.messaging.mode", "Push");
        agentProperties.put("adk.messaging.transport", "http");
        agentProperties.put("adk.messaging.pullFrequency", "30000");
        agentProperties.put("adk.messaging.maxBufferSize", "32000");

        Properties httpProperties = new Properties();
        httpProperties.put("port", "25102");

        Properties httpsProperties = new Properties();

        return new SifAgent(agentId, new PublishZoneConfigurator(), agentProperties, httpProperties, httpsProperties,
                "TestZone", zoneUrl, SIFVersion.SIF23);
    }

    public static final int ZONE_ID = 0;
    public static final int MESSAGE_FILE = 1;

    private static final Logger LOG = LoggerFactory.getLogger(EventReporter.class);

    private Zone zone;
    private EventGenerator generator;

    public EventReporter(Zone zone) throws Exception {
        this.zone = zone;
        // this.zone.setPublisher(this);
        this.zone.setPublisher(this, StudentDTD.SCHOOLINFO, new PublishingOptions(true));
        this.zone.setPublisher(this, StudentDTD.LEAINFO, new PublishingOptions(true));
        this.zone.setPublisher(this, StudentDTD.STUDENTPERSONAL, new PublishingOptions(true));
        this.zone.setPublisher(this, StudentDTD.STUDENTSCHOOLENROLLMENT, new PublishingOptions(true));
        this.zone.setPublisher(this, CommonDTD.STUDENTLEARELATIONSHIP, new PublishingOptions(true));
        generator = new HCStudentPersonalGenerator();
    }

    public void setEventGenerator(EventGenerator generator) {
        this.generator = generator;
    }

    public void reportEvent() throws ADKException {
        Event event = generator.generateEvent(null);
        if (zone.isConnected()) {
            zone.reportEvent(event);
        } else {
            LOG.error("Zone is not connected");
        }
    }

    public void reportSchoolLeaInfoEvents() throws ADKException {
        SchoolInfo schoolInfo = org.slc.sli.sif.generator.SifEntityGenerator.generateTestSchoolInfo();
        LEAInfo leaInfo = org.slc.sli.sif.generator.SifEntityGenerator.generateTestLEAInfo();
        StudentSchoolEnrollment studentSchoolEnrollment = org.slc.sli.sif.generator.SifEntityGenerator
                .generateTestStudentSchoolEnrollment();
        StudentLEARelationship studentLEARelationship = org.slc.sli.sif.generator.SifEntityGenerator
                .generateTestStudentLEARelationship();

        if (zone.isConnected()) {
            try {
                zone.reportEvent(leaInfo, EventAction.ADD);
                Thread.sleep(5000);
                zone.reportEvent(schoolInfo, EventAction.ADD);
                Thread.sleep(5000);
                zone.reportEvent(studentSchoolEnrollment, EventAction.ADD);
                Thread.sleep(5000);
                zone.reportEvent(studentLEARelationship, EventAction.ADD);
                Thread.sleep(5000);
                schoolInfo.setChanged();
                schoolInfo.setSchoolURL("http://www.IL-DAYBREAK.edu");
                zone.reportEvent(schoolInfo, EventAction.CHANGE);
                Thread.sleep(5000);
                studentLEARelationship.setChanged();
                studentLEARelationship.setGradeLevel(GradeLevelCode._09);
                zone.reportEvent(studentLEARelationship, EventAction.CHANGE);
                Thread.sleep(5000);
                zone.reportEvent(studentLEARelationship, EventAction.DELETE);
                Thread.sleep(5000);
                studentSchoolEnrollment.setChanged();
                studentSchoolEnrollment.setExitType(ExitTypeCode._1923_DIED_OR_INCAPACITATED);
                zone.reportEvent(studentSchoolEnrollment, EventAction.CHANGE);
                Thread.sleep(5000);
                zone.reportEvent(studentSchoolEnrollment, EventAction.DELETE);
                Thread.sleep(5000);
                zone.reportEvent(schoolInfo, EventAction.DELETE);
                Thread.sleep(5000);
                zone.reportEvent(leaInfo, EventAction.DELETE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            LOG.error("Zone is not connected");
        }
    }

    public void reportEvent(String messageFile) throws ADKException {
        Properties props = new Properties();
        props.setProperty(CustomEventGenerator.MESSAGE_FILE, messageFile);
        Event event = generator.generateEvent(props);
        if (zone.isConnected()) {
            zone.reportEvent(event);
        } else {
            LOG.error("Zone is not connected");
        }
    }

    @Override
    public void onRequest(DataObjectOutputStream out, Query query, Zone zone, MessageInfo info) throws ADKException {
        LOG.info("Received request to publish data:\n" + "\tQuery:\n" + query.toXML() + "\n" + "\tZone: "
                + zone.getZoneId() + "\n" + "\tInfo: " + info.getMessage());
    }

    @SuppressWarnings("unused")
    private void inspectAndDestroyEvent(Event e) {
        LOG.info("###########################################################################");
        try {
            SIFDataObject dataObj = e.getData().readDataObject();
            LOG.info(dataObj.toString());
        } catch (ADKException e1) {
            LOG.error("Error trying to inspect event", e1);
        }
        LOG.info("###########################################################################");
    }
}
