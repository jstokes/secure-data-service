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


package org.slc.sli.unit.manager;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import org.slc.sli.client.RESTClient;
import org.slc.sli.manager.impl.PortalWSManagerImpl;

/**
 * Testing PortalWSManagerImpl
 * @author agrebneva
 *
 */
public class PortalWSManagerImplTest {
    private PortalWSManagerImpl portalWSManager = new PortalWSManagerImpl();
    private RESTClient goodRestClient = new RESTClient() {
        @Override
        public String getJsonRequest(String path, boolean timeout) {
            return "";
        }
    };

    private RESTClient badRestClient = new RESTClient() {
        @Override
        public String getJsonRequest(String path, boolean timeout) {
            throw new IllegalArgumentException();
        }
    };

    @Before
    public void setUp() {
        portalWSManager.setPortalFooterUrl("");
        portalWSManager.setPortalHeaderUrl("");
    }

    @Test
    public void testHeaderFooterNoThrowingErrors() {
        portalWSManager.setRestClient(goodRestClient);
        Assert.assertNotNull(portalWSManager.getFooter(true));
        Assert.assertNotNull(portalWSManager.getHeader(true));
        portalWSManager.setRestClient(badRestClient);
        Assert.assertNotNull(portalWSManager.getFooter(true));
        Assert.assertNotNull(portalWSManager.getHeader(true));
    }
}
