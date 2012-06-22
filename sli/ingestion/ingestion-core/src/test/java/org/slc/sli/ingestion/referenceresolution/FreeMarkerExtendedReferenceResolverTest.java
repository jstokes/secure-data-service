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


package org.slc.sli.ingestion.referenceresolution;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.Holder;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.xml.sax.SAXException;

import org.slc.sli.ingestion.IngestionTest;

/**
 *
 * @author tke
 *
 */
public class FreeMarkerExtendedReferenceResolverTest {
    FreeMarkerExtendedReferenceResolver referenceFactory = new FreeMarkerExtendedReferenceResolver();

    private void test(String content, File expected, String xpath) throws IOException {
        String result = null;
        result = referenceFactory.resolve(xpath, content);

        Assert.assertNotNull(result);

        String expectedXML = readFromFile(expected);
        String actualXML = result;

        expectedXML = expectedXML.replaceAll("\\n|\\r", "");
        expectedXML = expectedXML.replaceAll("\\s+", "");
        actualXML = actualXML.replaceAll("\\n|\\r", "");
        actualXML = actualXML.replaceAll("\\s+", "");

        Assert.assertEquals(expectedXML, actualXML);
    }

    private String readFromFile(File file) throws IOException {
        FileReader reader = null;

        try {
            reader = new FileReader(file);

            List<String> lines = IOUtils.readLines(reader);

            return StringUtils.join(lines, '\n');
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    @Test
    public void testResolution() throws IOException, SAXException {
        final File input = IngestionTest
                .getFile("idRefResolutionData/InterchangeStudentParent/StudentReference_input.xml");
        final File expected = IngestionTest
                .getFile("idRefResolutionData/InterchangeStudentParent/StudentReference_output.xml");

        Map<String, String> config = new HashMap<String, String>();
        config.put("/InterchangeStudentParent/StudentParentAssociation/StudentReference",
                "idRefResolution/InterchangeStudentParent/StudentParentAssociation/StudentReference.ftl");

        referenceFactory.setIdRefConfigs(config);

        test(readFromFile(input), expected, "/InterchangeStudentParent/StudentParentAssociation/StudentReference");

        final Holder<Boolean> exceptionThrown = new Holder<Boolean>(Boolean.FALSE);

        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    test(readFromFile(input), expected,
                            "/InterchangeStudentParent/StudentParentAssociation/StudentReference");
                } catch (Throwable t) {
                    exceptionThrown.value = Boolean.TRUE;
                    throw new RuntimeException(t);
                }
            }
        };

        Thread th1 = new Thread(run, "Thread1");

        Thread th2 = new Thread(run, "Thread2");

        th1.start();
        th2.start();

        try {
            th1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Assert.fail();
        }

        try {
            th2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Assert.fail();
        }

        Assert.assertFalse(exceptionThrown.value);
    }

    @Test
    public void testBadConfiguration() {
        Map<String, String> config = new HashMap<String, String>();
        referenceFactory.setIdRefConfigs(config);

        config.put("/InterchangeStudentAssessment/StudentAssessment/AssessmentReference",
                "idRefResolution/InterchangeAssessmentMetadata/Assessment/AssessmentFamilyReference.ftl");

        Assert.assertNull(referenceFactory.resolve(
                "/InterchangeStudentAssessment/StudentAssessment/AssessmentReference2", null));
    }

}
