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

package org.slc.sli.dal.repository.tenancy.apsect;

import java.io.PrintStream;

import junit.framework.Assert;

import org.junit.Test;
import org.slc.sli.dal.repository.tenancy.CurrentTenantHolder;
import org.slc.sli.dal.repository.tenancy.SystemCall;
import org.slc.sli.dal.repository.tenancy.TenantCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author okrook
 *
 */
public class TenantAwareCallAspectTest {
    Logger log = LoggerFactory.getLogger(TenantAwareCallAspectTest.class);

    @Test
    public void testTenantCall() {
        tenantCall("MyTenant");

        systemCall();

        tenantCall2(null, "MyTenant");
    }

    @Test
    public void mutliThreadedCalls() throws InterruptedException {
        Runnable th = new Runnable() {

            @Override
            public void run() {
                tenantCall("MyTenant" + Thread.currentThread().getId());

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }

                systemCall();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }

                tenantCall2(null, "MyTenant" + Thread.currentThread().getId());
            }
        };

        Thread th1 = new Thread(th);
        Thread th2 = new Thread(th);

        th1.start();
        th2.start();

        th1.join();
        th2.join();
    }

    @Test(expected = IllegalStateException.class)
    public void testBadTenantCall() {
        badTenantCall("MyTenant");
    }

    @TenantCall(param = "tenantId")
    private void tenantCall(String tenantId) {
        log.debug(String.format("Expected: %s, Actual: %s", tenantId, CurrentTenantHolder.getCurrentTenant()));

        Assert.assertEquals(tenantId, CurrentTenantHolder.getCurrentTenant());

        systemCall();

        log.debug(String.format("Expected: %s, Actual: %s", tenantId, CurrentTenantHolder.getCurrentTenant()));

        Assert.assertEquals(tenantId, CurrentTenantHolder.getCurrentTenant());
    }

    @TenantCall(param = "tenantId")
    private void tenantCall2(Object someValue, String tenantId) {
        log.debug(String.format("Expected: %s, Actual: %s", tenantId, CurrentTenantHolder.getCurrentTenant()));

        Assert.assertEquals(tenantId, CurrentTenantHolder.getCurrentTenant());
    }

    @TenantCall(param = "myTenant")
    private void badTenantCall(String tenantId) {
        Assert.assertEquals(tenantId, CurrentTenantHolder.getCurrentTenant());
    }

    @SystemCall
    private void systemCall() {
        log.debug(String.format("Expected: %s, Actual: %s", "null", String.valueOf(CurrentTenantHolder.getCurrentTenant())));

        Assert.assertEquals(null, CurrentTenantHolder.getCurrentTenant());
    }
}
