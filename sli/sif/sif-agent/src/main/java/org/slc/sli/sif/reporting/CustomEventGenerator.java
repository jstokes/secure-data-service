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

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import openadk.library.Event;
import openadk.library.EventAction;
import openadk.library.SIFDataObject;
import openadk.library.SIFParser;
import openadk.library.SIFVersion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom event generator.
 */
public class CustomEventGenerator implements EventGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(CustomEventGenerator.class);

    @Override
    public Event generateEvent(Properties eventProps) {
        String messageFile = eventProps.getProperty(CustomEventGenerator.MESSAGE_FILE);
        FileReader in = null;
        Event event = null;
        try {
            // Construct a SIFParser instance that can be used for parsing
            SIFParser p = SIFParser.newInstance();
            // Open a FileReader to read the text file
            in = new FileReader(messageFile);
            // Read the file into a StringBuffer in 4K chunks
            StringBuffer xml = new StringBuffer();
            int bufSize = 4096;
            char[] buf = new char[bufSize];
            while (in.ready()) {
                bufSize = in.read(buf, 0, buf.length);
                xml.append(buf, 0, bufSize);
            }
            // Parse it
            SIFDataObject generic = (SIFDataObject) p.parse(xml.toString(), null, 0, SIFVersion.SIF23);
            event = new Event(generic, EventAction.ADD);
        } catch (Exception e) {
            LOG.error("Caught exception trying to load entity from file", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOG.warn("Unable to close file", e);
                }
            }
        }

        return event;
    }
}
