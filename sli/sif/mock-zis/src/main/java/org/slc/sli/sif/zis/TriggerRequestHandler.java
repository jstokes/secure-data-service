package org.slc.sli.sif.zis;

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


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Http request handler acts as a trigger
 * for the mockZis to broadcast any POSTed messages.
 * 
 * @author jtully
 *
 */
public class TriggerRequestHandler extends AbstractRequestHandler {

    @Autowired
    private MockZis mockZis;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        System.out.println("GET Trigger");
        
        resp.setContentType("text/xml");
        
        writeResponseString(resp, mockZis.createAckString());
        
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String xmlString = getRequestString(req);
        
        System.out.println("POST Trigger with MESSAGE: \n" + xmlString);
        
        mockZis.broadcastMessage(xmlString);
        
        writeResponseString(resp, mockZis.createAckString());
    }
    
}
