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

package org.slc.sli.ingestion.landingzone.validation;

import java.io.File;
import java.io.FileNotFoundException;

import junitx.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.slc.sli.ingestion.IngestionTest;
import org.slc.sli.ingestion.reporting.AbstractMessageReport;
import org.slc.sli.ingestion.reporting.AbstractReportStats;
import org.slc.sli.ingestion.reporting.DummyMessageReport;
import org.slc.sli.ingestion.reporting.SimpleReportStats;

/**
 * Tests for zip file validator.
 *
 * @author npandey
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class ZipFileValidatorTest {

    @Autowired
    ZipFileValidator zipFileValidator;

    File file;

    @Test
    public void zipFileHasPath() throws FileNotFoundException {
        AbstractMessageReport report = new DummyMessageReport();
        AbstractReportStats reportStats = new SimpleReportStats(null, null, null);

        file = IngestionTest.getFile("zip/ZipWithPath.zip");
        boolean isValid = zipFileValidator.isValid(file, report, reportStats, null);
        Assert.assertFalse(isValid);

    }

    @Test
    public void noControlFile() throws FileNotFoundException {
        AbstractMessageReport report = new DummyMessageReport();
        AbstractReportStats reportStats = new SimpleReportStats(null, null, null);

        file = IngestionTest.getFile("zip/NoControlFile.zip");
        boolean isValid = zipFileValidator.isValid(file, report, reportStats, null);
        Assert.assertFalse(isValid);
    }

    @Test
    public void validZip() throws FileNotFoundException {
        AbstractMessageReport report = new DummyMessageReport();
        AbstractReportStats reportStats = new SimpleReportStats(null, null, null);

        file = IngestionTest.getFile("zip/ValidZip.zip");
        boolean isValid = zipFileValidator.isValid(file, report, reportStats, null);
        Assert.assertTrue(isValid);
    }

}
