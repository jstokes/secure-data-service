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
package org.slc.sli.bulk.extract.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junitx.util.PrivateAccessor;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.slc.sli.bulk.extract.TestUtils;
/**
 * JUnit tests for ArchivedExtractFile class.
 * @author npandey
 *
 */
public class ArchivedExtractFileTest {

    ArchivedExtractFile archiveFile;

    private static final String FILE_NAME = "Test";
    private static final String FILE_EXT = ".tar";
    /**
     * Runs before JUnit tests and does the initiation work for the tests.
     * @throws IOException
     *          if an I/O error occurred
     * @throws NoSuchFieldException
     *          if a field is not found
     */
    @Before
    public void init() throws IOException, NoSuchFieldException {
        archiveFile = new ArchivedExtractFile("./", FILE_NAME);
        List<File> files = new ArrayList<File>();

        File studentFile = TestUtils.createTempFile("student", ".json.gz");
        files.add(studentFile);
        File assessmentFile = TestUtils.createTempFile("assessment", ".json.gz");
        files.add(assessmentFile);
        File metadataFile = TestUtils.createTempFile("metadata", ".txt");
        files.add(metadataFile);

        PrivateAccessor.setField(archiveFile, "filesToArchive", files);
    }

    /**
     * Performs cleanup after test run.
     */
    @After
    public void cleanup() {
        FileUtils.deleteQuietly(new File(FILE_NAME + FILE_EXT));
    }

    /**
     * Test generation of archive file.
     * @throws IOException
     *          if an I/O error occurred
     */
    @Test
    public void generateArchiveTest() throws IOException {
        archiveFile.generateArchive();

        TarArchiveInputStream tarInputStream = null;
        List<String> names = new ArrayList<String>();

        try {
        tarInputStream = new TarArchiveInputStream(new FileInputStream(FILE_NAME + FILE_EXT));

        TarArchiveEntry entry = null;
        while((entry = tarInputStream.getNextTarEntry())!= null) {
            names.add(entry.getName());
        }
        } finally {
            IOUtils.closeQuietly(tarInputStream);
        }

        Assert.assertEquals(3, names.size());
        Assert.assertTrue("Student extract file not found", names.get(0).contains("student"));
        Assert.assertTrue("Assessment extract file not found", names.get(1).contains("assessment"));
        Assert.assertTrue("Metadata file not found", names.get(2).contains("metadata"));
    }

}
