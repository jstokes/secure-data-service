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

package org.slc.sli.sif.subscriber;

import openadk.library.ADK;
import openadk.library.ADKException;
import openadk.library.Event;
import openadk.library.MessageInfo;
import openadk.library.Subscriber;
import openadk.library.Zone;

public class SifSubscriber implements Subscriber {

    @Override
    public void onEvent(Event event, Zone zone, MessageInfo info) throws ADKException {
        ADK.getLog().info("Received event:\n"
                + "\tEvent: " + event.getActionString() + "\n"
                + "\tZone: " + zone.getZoneId() + "\n"
                + "\tInfo: " + info.getMessage());
    }

}
