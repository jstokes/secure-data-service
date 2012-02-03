package org.slc.sli.ingestion.smooks;

import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.NeutralRecordFileReader;
import org.slc.sli.ingestion.NeutralRecordFileWriter;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.milyn.Smooks;
import org.milyn.payload.JavaResult;
import org.springframework.util.ResourceUtils;
import org.xml.sax.SAXException;

/**
 * Test for SmooksEdFiVisitor
 *
 */
// @RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations = { "classpath:/spring/applicationContext.xml" })
public class SmooksEdFiVisitorTest {

    private static final String SMOOKS_CONFIG = "smooks_conf/smooks-all-xml.xml";

    @Test
    public void testJavaResult() throws IOException, SAXException {

        // Instantiate Smooks with the config...
        InputStream messageIn = null;

        try {

            File inFile = ResourceUtils.getFile("classpath:smooks/InterchangeStudent.xml");

            messageIn = new BufferedInputStream(new FileInputStream(inFile));

            Smooks smooks = new Smooks(SMOOKS_CONFIG);
            // smooks.addVisitor(new SmooksEdFiVisitor("student"),
            // "InterchangeStudent/Student");

            JavaResult result = new JavaResult();

            smooks.filterSource(new StreamSource(messageIn), result);

            ArrayList<NeutralRecord> records = (ArrayList<NeutralRecord>) result.getBean("records");
            assertEquals(100, records.size());

            // TODO assert for a few entries that all values got successfully
            // mapped

        } finally {

            IOUtils.closeQuietly(messageIn);
        }

    }

    @Test
    public void testFileWriterResult() throws IOException, SAXException {

        // Instantiate Smooks with the config...
        InputStream messageIn = null;

        try {

            File inFile = ResourceUtils.getFile("classpath:smooks/InterchangeStudent.xml");

            messageIn = new BufferedInputStream(new FileInputStream(inFile));
            File outputFile = File.createTempFile("test", ".dat");
            outputFile.deleteOnExit();
            NeutralRecordFileWriter nrfWriter = new NeutralRecordFileWriter(outputFile);

            Smooks smooks = new Smooks(SMOOKS_CONFIG);
            smooks.addVisitor(SmooksEdFiVisitor.createInstance("record", nrfWriter), "InterchangeStudent/Student");

            try {
                smooks.filterSource(new StreamSource(messageIn));
            } finally {
                nrfWriter.close();
            }

            int c = 0;
            NeutralRecordFileReader nrfr = new NeutralRecordFileReader(new File(outputFile.getAbsolutePath()));
            try {
                while (nrfr.hasNext()) {
                    NeutralRecord value = nrfr.next();
                    // System.out.println(value.getAttributes());
                    c++;
                }
                assertEquals(100, c);
            } finally {
                nrfr.close();
            }

        } finally {

            IOUtils.closeQuietly(messageIn);
        }

    }

    @Test
    public void testSchool() throws IOException, SAXException {

        // Instantiate Smooks with the config...
        InputStream messageIn = null;

        try {

            File inFile = ResourceUtils.getFile("classpath:smooks/InterchangeSchool.xml");

            messageIn = new BufferedInputStream(new FileInputStream(inFile));

            Smooks smooks = new Smooks(SMOOKS_CONFIG);
            // smooks.addVisitor(new SmooksEdFiVisitor("student"),
            // "InterchangeStudent/Student");

            JavaResult result = new JavaResult();

            smooks.filterSource(new StreamSource(messageIn), result);

            ArrayList<NeutralRecord> records = (ArrayList<NeutralRecord>) result.getBean("records");
            assertEquals(2, records.size());

            // TODO assert for a few entries that all values got successfully
            // mapped

        } finally {

            IOUtils.closeQuietly(messageIn);
        }

    }

    @Test
    public void testStudentSchoolAssociation() throws IOException, SAXException {

        // Instantiate Smooks with the config...
        InputStream messageIn = null;

        try {

            File inFile = ResourceUtils.getFile("classpath:smooks/InterchangeEnrollment.xml");

            messageIn = new BufferedInputStream(new FileInputStream(inFile));

            Smooks smooks = new Smooks(SMOOKS_CONFIG);
            // smooks.addVisitor(new SmooksEdFiVisitor("student"),
            // "InterchangeStudent/Student");

            JavaResult result = new JavaResult();

            smooks.filterSource(new StreamSource(messageIn), result);

            ArrayList<NeutralRecord> records = (ArrayList<NeutralRecord>) result.getBean("records");
            assertEquals(100, records.size());

            // TODO assert for a few entries that all values got successfully
            // mapped

        } finally {

            IOUtils.closeQuietly(messageIn);
        }

    }

    @Test
    public void testTeacher() throws IOException, SAXException {

        // Instantiate Smooks with the config...
        InputStream messageIn = null;

        try {

            File inFile = ResourceUtils.getFile("classpath:smooks/InterchangeStaffAssociation.xml");

            messageIn = new BufferedInputStream(new FileInputStream(inFile));

            Smooks smooks = new Smooks(SMOOKS_CONFIG);
            // smooks.addVisitor(new SmooksEdFiVisitor("student"),
            // "InterchangeStudent/Student");

            JavaResult result = new JavaResult();

            smooks.filterSource(new StreamSource(messageIn), result);

            ArrayList<NeutralRecord> records = (ArrayList<NeutralRecord>) result.getBean("records");
            assertEquals(2, records.size());

            // TODO assert for a few entries that all values got successfully
            // mapped

        } finally {

            IOUtils.closeQuietly(messageIn);
        }

    }

}
