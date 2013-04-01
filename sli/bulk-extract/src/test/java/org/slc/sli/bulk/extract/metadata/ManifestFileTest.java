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
package org.slc.sli.bulk.extract.metadata;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.slc.sli.bulk.extract.files.metadata.ManifestFile;

/**
 * JUnit tests for ManifestFile class.
 * @author tke
 *
 */
public class ManifestFileTest {

    ManifestFile meta = null;

    /**
     * Runs before JUnit tests and does the initiation work for the tests.
     * @throws IOException
     *          if an I/O error occurred
     */
    @Before
    public void init() throws IOException {
        meta = new ManifestFile("./");
    }

    /**
     * JUnit test to test fetching api version.
     * @throws IOException
     *          if an I/O error occurred
     */
    @Test
    public void testGetVersion() throws IOException {

        String version = meta.getApiVersion();

        Assert.assertTrue(version.equals("v1.4"));
    }

    /**
     * JUnit test to test generation of metadata file.
     * @throws IOException
     *          if an I/O error occurred
     */
    @Test
    public void testGenerateFile() throws IOException {
        Date startTime = new Date();

        meta.generateMetaFile(startTime);

        File manifestFile = meta.getFile();

        String fileContent = FileUtils.readFileToString(manifestFile);

        assertTrue("Correct metadata version not found in metadata file", fileContent.contains("metadata_version=1.0"));
        assertTrue("Correct api version not found in metadata file", fileContent.contains("api_version=v1.4"));
        assertTrue("Correct time stamp entry not found in metadata file", fileContent.contains("timeStamp=" + ManifestFile.getTimeStamp(startTime)));

        FileUtils.forceDelete(meta.getFile());
    }

}
