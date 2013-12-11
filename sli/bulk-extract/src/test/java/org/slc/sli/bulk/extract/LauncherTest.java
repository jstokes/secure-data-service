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

package org.slc.sli.bulk.extract;

import java.io.File;
import java.util.Locale;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;

import org.slc.sli.bulk.extract.extractor.EdOrgExtractor;
import org.slc.sli.bulk.extract.extractor.TenantPublicDataExtractor;
import org.slc.sli.bulk.extract.util.SecurityEventUtil;
import org.slc.sli.domain.Entity;

/**
 * JUnit test for Launcher class.
 * @author npandey
 *
 */
public class LauncherTest {

    Launcher launcher;
    BulkExtractMongoDA bulkExtractMongoDA;
    EdOrgExtractor localEdOrgExtractor;
    private TenantPublicDataExtractor tenantPublicDataExtractor;
    private SecurityEventUtil securityEventUtil;

    Entity testTenantEntity = TestUtils.makeDummyEntity("tenant", "testTenant", null);

    /**
     * Runs before JUnit tests and does the initiation work for the tests.
     */
    @Before
    public void setUp() {
        launcher = new Launcher();

        bulkExtractMongoDA = Mockito.mock(BulkExtractMongoDA.class);
        localEdOrgExtractor = Mockito.mock(EdOrgExtractor.class);
        tenantPublicDataExtractor = Mockito.mock(TenantPublicDataExtractor.class);

        MessageSource messageSource = Mockito.mock(MessageSource.class);
        securityEventUtil = new SecurityEventUtil();
        securityEventUtil.setMessageSource(messageSource);
        Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.any(Object[].class), Mockito.anyString(), Mockito.any(Locale.class))).thenReturn("TestMessage");

        launcher.setBulkExtractMongoDA(bulkExtractMongoDA);
        launcher.setEdOrgExtractor(localEdOrgExtractor);
        launcher.setTenantPublicDataExtractor(tenantPublicDataExtractor);
        launcher.setBaseDirectory("./");
        launcher.setSecurityEventUtil(securityEventUtil);
    }

    /**
     *Test initiating bulk extract with a tenant that has not been onboarded.
     */
    @Test
    public void testInvalidTenant() {
        String tenantId = "testTenant";

        Mockito.when(bulkExtractMongoDA.getTenant(tenantId)).thenReturn(null);

        launcher.execute(tenantId, false);

        Mockito.verify(localEdOrgExtractor, Mockito.never()).execute(Mockito.eq("tenant"), Mockito.any(File.class), Mockito.any(DateTime.class));
    }

    /**
     * Test initiating bulk extractor for a valid tenant.
     */
    @Test
    public void testValidTenant() {
        String tenantId = "Midgar";
        Mockito.doNothing().when(localEdOrgExtractor).execute(Mockito.eq("tenant"), Mockito.any(File.class), Mockito.any(DateTime.class));


        Mockito.when(bulkExtractMongoDA.getTenant(tenantId)).thenReturn(testTenantEntity);

        launcher.execute(tenantId, false);

        Mockito.verify(localEdOrgExtractor, Mockito.times(1)).execute(Mockito.eq(tenantId), Mockito.any(File.class), Mockito.any(DateTime.class));
    }

}
