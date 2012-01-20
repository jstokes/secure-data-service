package org.slc.sli.ingestion.smooks;

import static org.junit.Assert.assertEquals;



import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.milyn.Smooks;
import org.milyn.payload.JavaResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ResourceUtils;

import org.slc.sli.domain.Entity;
import org.slc.sli.ingestion.FileFormat;
import org.slc.sli.ingestion.FileType;
import org.slc.sli.ingestion.IngestionTest;
import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.NeutralRecordFileReader;
import org.slc.sli.ingestion.NeutralRecordFileWriter;
import org.slc.sli.ingestion.landingzone.IngestionFileEntry;
import org.slc.sli.ingestion.processors.EdFiProcessor;
import org.slc.sli.ingestion.util.MD5;
import org.slc.sli.ingestion.validation.IngestionAvroEntityValidator;
import org.slc.sli.validation.EntitySchemaRegistry;
import org.slc.sli.validation.EntityValidationException;
import org.slc.sli.validation.ValidationError;

/**
 *
 * @author ablum
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })

public class SmooksValidationTest {

    @Autowired
    private EntitySchemaRegistry schemaReg;

    @Autowired
    private EdFiProcessor edFiProcessor;

    @Test
    public void testValidStudentCSV() throws Exception {
    	 InputStream messageIn = null;
    	 String SMOOKS_CONFIG = "smooks_conf/smooks-student-csv.xml";
         try {
             IngestionAvroEntityValidator validator = new IngestionAvroEntityValidator();
             validator.setSchemaRegistry(schemaReg);
                 File inFile = ResourceUtils
                         .getFile("classpath:smooks/InterchangeStudent.csv");

                 messageIn = new BufferedInputStream(new FileInputStream(inFile));
                 File outputFile = File.createTempFile("test", ".dat");
                 outputFile.deleteOnExit();
                 NeutralRecordFileWriter nrfWriter = new NeutralRecordFileWriter(
                         outputFile);

                 Smooks smooks = new Smooks(SMOOKS_CONFIG);
                 smooks.addVisitor(SmooksEdFiVisitor.createInstance("record", nrfWriter),
                         "csv-record");

                 try {
                     smooks.filterSource(new StreamSource(messageIn));
                 } finally {
                     nrfWriter.close();
                 }

                 NeutralRecordFileReader nrfr = new NeutralRecordFileReader(
                         new File(outputFile.getAbsolutePath()));

                 assertTrue(nrfr.hasNext());
                 try {
                     while (nrfr.hasNext()) {
                         NeutralRecord record = nrfr.next();
                         Map<String, Object> entity = record.getAttributes();
                         mapValidation(entity, "student", validator);

                     }
                 } finally {
                 }

         } finally {

             IOUtils.closeQuietly(messageIn);
         }

    }

    @Test
    public void testValidSchoolCSV() throws Exception {
    	 InputStream messageIn = null;
    	 String SMOOKS_CONFIG = "smooks_conf/smooks-school-csv.xml";
         try {
             IngestionAvroEntityValidator validator = new IngestionAvroEntityValidator();
             validator.setSchemaRegistry(schemaReg);
             File inFile = ResourceUtils
                     .getFile("classpath:smooks/InterchangeSchool.csv");

             messageIn = new BufferedInputStream(new FileInputStream(inFile));
             File outputFile = File.createTempFile("test", ".dat");
             outputFile.deleteOnExit();
             NeutralRecordFileWriter nrfWriter = new NeutralRecordFileWriter(
                     outputFile);

             Smooks smooks = new Smooks(SMOOKS_CONFIG);
             smooks.addVisitor(SmooksEdFiVisitor.createInstance("record", nrfWriter),
                     "csv-record");

             try {
                 smooks.filterSource(new StreamSource(messageIn));
             } finally {
                 nrfWriter.close();
             }

             NeutralRecordFileReader nrfr = new NeutralRecordFileReader(
                     new File(outputFile.getAbsolutePath()));

             assertTrue(nrfr.hasNext());
             try {
                 while (nrfr.hasNext()) {
                     NeutralRecord record = nrfr.next();
                     Map<String, Object> entity = record.getAttributes();
                     mapValidation(entity, "school", validator);

                 }
             } finally {
             }

         } finally {

             IOUtils.closeQuietly(messageIn);
         }

    }

    @Test
    public void testValidStudentSchoolAssociationCSV() throws Exception {
    	 InputStream messageIn = null;
    	 String SMOOKS_CONFIG = "smooks_conf/smooks-studentSchoolAssociation-csv.xml";
         try {
             IngestionAvroEntityValidator validator = new IngestionAvroEntityValidator();
             validator.setSchemaRegistry(schemaReg);

             File inFile = ResourceUtils
                     .getFile("classpath:smooks/InterchangeStudentSchoolAssociation.csv");

             messageIn = new BufferedInputStream(new FileInputStream(inFile));
             File outputFile = File.createTempFile("test", ".dat");
             outputFile.deleteOnExit();
             NeutralRecordFileWriter nrfWriter = new NeutralRecordFileWriter(
                     outputFile);

             Smooks smooks = new Smooks(SMOOKS_CONFIG);
             smooks.addVisitor(SmooksEdFiVisitor.createInstance("record", nrfWriter),
                     "csv-record");

             try {
                 smooks.filterSource(new StreamSource(messageIn));
             } finally {
                 nrfWriter.close();
             }

             NeutralRecordFileReader nrfr = new NeutralRecordFileReader(
                     new File(outputFile.getAbsolutePath()));

             assertTrue(nrfr.hasNext());
             try {
                 while (nrfr.hasNext()) {
                     NeutralRecord record = nrfr.next();
                     Map<String, Object> entity = record.getAttributes();
                     mapValidation(entity, "studentSchoolAssociation", validator);

                 }
             } finally {
             }
         } finally {

             IOUtils.closeQuietly(messageIn);
         }

    }

    @Test
    public void testValidStudentXML() throws Exception {
    	 InputStream messageIn = null;
    	 String SMOOKS_CONFIG = "smooks_conf/smooks-all-xml.xml";
         try {
             IngestionAvroEntityValidator validator = new IngestionAvroEntityValidator();
             validator.setSchemaRegistry(schemaReg);
             File inFile = ResourceUtils
                     .getFile("classpath:smooks/InterchangeStudent.xml");


             messageIn = new BufferedInputStream(new FileInputStream(inFile));
             File outputFile = File.createTempFile("test", ".dat");
             outputFile.deleteOnExit();
             NeutralRecordFileWriter nrfWriter = new NeutralRecordFileWriter(
                     outputFile);

             Smooks smooks = new Smooks(SMOOKS_CONFIG);

             smooks.addVisitor(SmooksEdFiVisitor.createInstance("record", nrfWriter),
                     "InterchangeStudent/Student");

             try {
                 smooks.filterSource(new StreamSource(messageIn));
             } finally {
                 nrfWriter.close();
             }

             NeutralRecordFileReader nrfr = new NeutralRecordFileReader(
                     new File(outputFile.getAbsolutePath()));

             assertTrue(nrfr.hasNext());
             try {
                 while (nrfr.hasNext()) {
                     NeutralRecord record = nrfr.next();
                     Map<String, Object> entity = record.getAttributes();
                     mapValidation(entity, "student", validator);

                 }
             } finally {
             }

         } finally {

             IOUtils.closeQuietly(messageIn);
         }

    }

    @Test
    public void testValidSchoolXML() throws Exception {
    	 InputStream messageIn = null;
    	 String SMOOKS_CONFIG = "smooks_conf/smooks-all-xml.xml";

         try {
             IngestionAvroEntityValidator validator = new IngestionAvroEntityValidator();
             validator.setSchemaRegistry(schemaReg);
             File inFile = ResourceUtils
                     .getFile("classpath:smooks/InterchangeSchool.xml");


             messageIn = new BufferedInputStream(new FileInputStream(inFile));
             File outputFile = File.createTempFile("test", ".dat");
             outputFile.deleteOnExit();
             NeutralRecordFileWriter nrfWriter = new NeutralRecordFileWriter(
                     outputFile);


             Smooks smooks = new Smooks(SMOOKS_CONFIG);

             smooks.addVisitor(SmooksEdFiVisitor.createInstance("record", nrfWriter),
                     "InterchangeEducationOrganization/School");

             try {
                 smooks.filterSource(new StreamSource(messageIn));
             } finally {
                 nrfWriter.close();
             }

             NeutralRecordFileReader nrfr = new NeutralRecordFileReader(
                     new File(outputFile.getAbsolutePath()));

             assertTrue(nrfr.hasNext());
             try {
                 while (nrfr.hasNext()) {
                     NeutralRecord record = nrfr.next();
                     Map<String, Object> entity = record.getAttributes();
                     mapValidation(entity, "school", validator);

                 }
             } finally {
             }

         } finally {

             IOUtils.closeQuietly(messageIn);
         }

    }

    @Test
    public void testValidStudentSchoolAssociationXML() throws Exception {
   	 InputStream messageIn = null;
   	String SMOOKS_CONFIG = "smooks_conf/smooks-all-xml.xml";

        try {
            IngestionAvroEntityValidator validator = new IngestionAvroEntityValidator();
            validator.setSchemaRegistry(schemaReg);
            File inFile = ResourceUtils
                    .getFile("classpath:smooks/InterchangeEnrollment.xml");

            messageIn = new BufferedInputStream(new FileInputStream(inFile));
            File outputFile = File.createTempFile("test", ".dat");
            outputFile.deleteOnExit();
            NeutralRecordFileWriter nrfWriter = new NeutralRecordFileWriter(
                        outputFile);

            Smooks smooks = new Smooks(SMOOKS_CONFIG);

            smooks.addVisitor(SmooksEdFiVisitor.createInstance("record", nrfWriter),
                    "InterchangeStudentEnrollment/StudentSchoolAssociation");

            try {
                smooks.filterSource(new StreamSource(messageIn));
            } finally {
                nrfWriter.close();
            }

            NeutralRecordFileReader nrfr = new NeutralRecordFileReader(
                    new File(outputFile.getAbsolutePath()));

            assertTrue(nrfr.hasNext());
            try {
                while (nrfr.hasNext()) {
                    NeutralRecord record = nrfr.next();
                    Map<String, Object> entity = record.getAttributes();
                    mapValidation(entity, "studentSchoolAssociation", validator);

                }
            } finally {
            }


        } finally {

            IOUtils.closeQuietly(messageIn);
        }

   }

    private void mapValidation(Map<String, Object> obj, String schemaName, IngestionAvroEntityValidator validator) {


        Entity e = mock(Entity.class);
        when(e.getBody()).thenReturn(obj);
        when(e.getType()).thenReturn(schemaName);

        try {
            assertTrue(validator.validate(e));
        } catch (EntityValidationException ex) {
            for (ValidationError err : ex.getValidationErrors()) {
                System.err.println(err);
            }
            fail();
        }
    }

}

