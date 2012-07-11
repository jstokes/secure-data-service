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

package org.slc.sli.sif.agent;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import openadk.library.ADK;
import openadk.library.ADKException;
import openadk.library.ADKFlags;
import openadk.library.SubscriptionOptions;
import openadk.library.Zone;
import openadk.library.student.StudentDTD;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slc.sli.sif.subscriber.SifSubscriber;

@Component
public class AgentManager {

    @Autowired
    private SifAgent agent;

    @PostConstruct
    public void setup() throws Exception {

        ADK.initialize();
        ADK.debug = ADK.DBG_ALL;

        agent.startAgent();

        //create a subscriber and add it to the agents TestZone
        Zone zone = agent.getZoneFactory().getZone("TestZone");
        SifSubscriber subscriber = new SifSubscriber();

        //create a subscriber and add it to the agents TestZone
        zone.setSubscriber(subscriber, StudentDTD.SCHOOLINFO, new SubscriptionOptions());
        zone.setSubscriber(subscriber, StudentDTD.STUDENTPERSONAL, new SubscriptionOptions());
    }

    @PreDestroy
    public void cleanup() throws ADKException {
        agent.shutdown( ADKFlags.PROV_NONE );
    }
}
