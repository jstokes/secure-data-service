package org.slc.sli.ingestion.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Map.Entry;

import javax.xml.transform.stream.StreamSource;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.milyn.Smooks;
import org.milyn.SmooksException;
import org.milyn.payload.JavaResult;
import org.milyn.payload.StringSource;
import org.slc.sli.domain.Entity;
import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.ResourceWriter;
import org.slc.sli.ingestion.smooks.SmooksEdFiVisitor;
import org.slc.sli.ingestion.transformation.SimpleEntity;
import org.slc.sli.validation.EntityValidationException;
import org.slc.sli.validation.EntityValidator;
import org.slc.sli.validation.ValidationError;
import org.xml.sax.SAXException;

/**
 *
 * @author ablum
 *
 */
public class EntityTestUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final Charset CHARSET_UTF8 = Charset.forName("utf-8");
    
    public static List<NeutralRecord> getNeutralRecords(InputStream dataSource, String smooksConfig,
            String targetSelector) throws IOException, SAXException, SmooksException {

        DummyResourceWriter dummyResourceWriter = new DummyResourceWriter();

        Smooks smooks = new Smooks(smooksConfig);
        SmooksEdFiVisitor smooksEdFiVisitor = SmooksEdFiVisitor.createInstance("record", null, null, null);
        smooksEdFiVisitor.setNrMongoStagingWriter(dummyResourceWriter);
        smooks.addVisitor(smooksEdFiVisitor, targetSelector);

        JavaResult result = new JavaResult();
        smooks.filterSource(new StreamSource(dataSource), result);
        
        List<NeutralRecord> entityList = dummyResourceWriter.getNeutralRecordsList();

        return entityList;
    }

    public static void mapValidation(Map<String, Object> obj, String schemaName, EntityValidator validator) {

        Entity e = mock(Entity.class);
        when(e.getBody()).thenReturn(obj);
        when(e.getType()).thenReturn(schemaName);

        try {
            Assert.assertTrue(validator.validate(e));
        } catch (EntityValidationException ex) {
            for (ValidationError err : ex.getValidationErrors()) {
                System.err.println(err);
            }
            Assert.fail();
        }
    }

    /**
     * Utility to make checking values in a map less verbose.
     *
     * @param map
     *            The map containing the entry we want to check.
     * @param key
     *            The string key for the entry
     * @param expectedValue
     *            The Object value we will assertEquals against
     */
    @SuppressWarnings("rawtypes")
    public static void assertObjectInMapEquals(Map map, String key, Object expectedValue) {
        assertEquals("Object value in map does not match expected.", expectedValue, map.get(key));
    }

    /**
     * Utility to run smooks with provided configurations and return the first NeutralRecord (if
     * there is one)
     *
     * @param smooksXmlConfigFilePath
     *            path to smooks config xml
     * @param targetSelector
     *            selector for this run
     * @param testData
     *            string we want to use as the input for smooks run
     * @return first NeutralRecord
     * @throws IOException
     * @throws SAXException
     */
    public static NeutralRecord smooksGetSingleNeutralRecord(String smooksXmlConfigFilePath, String targetSelector,
            String testData) throws IOException, SAXException {
        ByteArrayInputStream testDataStream = new ByteArrayInputStream(testData.getBytes());

        NeutralRecord neutralRecord = null;
        
        List<NeutralRecord> records = EntityTestUtils.getNeutralRecords(testDataStream, smooksXmlConfigFilePath,
                targetSelector);
        if (records != null && records.size() > 0) {
            neutralRecord = records.get(0);
        }
        return neutralRecord;        
    }

    @SuppressWarnings("unchecked")
	public static SimpleEntity smooksGetSingleSimpleEntity(String smooksConfigPath, NeutralRecord item)
			throws IOException, SAXException {
        JavaResult result = new JavaResult();
        Smooks smooks = new Smooks(smooksConfigPath);
        List<SimpleEntity> entityList = new ArrayList<SimpleEntity>();
        try {

            StringSource source = new StringSource(MAPPER.writeValueAsString(item));

            smooks.filterSource(source, result);


            for (Entry<String, Object> resEntry : result.getResultMap().entrySet()) {
                if (resEntry.getValue() instanceof List) {
                    List<?> list = (List<?>) resEntry.getValue();
                    if (list.size() != 0 && list.get(0) instanceof SimpleEntity) {
                        entityList = (List<SimpleEntity>) list;
                        break;
                    }
                }
            }
            return entityList.get(0);
        } catch (IOException e) {
            entityList = Collections.emptyList();
        }
        return null;
    }

    /**
     * Reads the entire contents of a resource file found on the classpath and returns it as a
     * string.
     *
     * @param resourceName
     *            the name of the resource to locate on the classpath
     * @return the contents of the resource file
     * @throws FileNotFoundException
     *             if the resource cannot be located on the classpath
     */
    public static String readResourceAsString(String resourceName) throws FileNotFoundException {
        InputStream stream = EntityTestUtils.getResourceAsStream(resourceName);
        if (stream == null) {
            throw new FileNotFoundException("Could not find resource " + resourceName + " in the classpath");
        }

        try {
            return EntityTestUtils.convertStreamToString(stream);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                System.err.println(e);
                Assert.fail();
            }
        }
    }

    /**
     * Reads the contents of a stream in UTF-8 format and returns it as a string.
     *
     * @param stream
     *            the stream to read
     * @return the contents of the stream or an empty string if the stream has not content
     */
    public static String convertStreamToString(InputStream stream) {
        // Reference: http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        // "The reason this works is because Scanner iterates over tokens in the stream,
        // and in this case we separate tokens using "beginning of the input boundary" (\A)
        // thus giving us only one token for the entire contents of the stream."
        // See also:
        // http://stackoverflow.com/questions/309424/in-java-how-do-a-read-convert-an-inputstream-in-to-a-string
        try {
            return new Scanner(stream, "UTF-8").useDelimiter("\\A").next();
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    public static String delimit(String[] strings, String delimiter) {
        StringBuilder builder = new StringBuilder();
        for (String string : strings) {
            if (builder.length() > 0) {
                builder.append(delimiter);
            }
            builder.append(string);
        }
        return builder.toString();
    }

    public static URL getResource(String resourceName) {
        return EntityTestUtils.class.getClassLoader().getResource(resourceName);
    }

    public static InputStream getResourceAsStream(String resourceName) {
        return EntityTestUtils.class.getClassLoader().getResourceAsStream(resourceName);
    }

    private static final class DummyResourceWriter implements ResourceWriter<NeutralRecord> {

        private List<NeutralRecord> neutralRecordList;

        private DummyResourceWriter() {
            this.neutralRecordList = new ArrayList<NeutralRecord>();
        }

        @Override
        public void writeResource(NeutralRecord neutralRecord, String jobId) {
            neutralRecordList.add(neutralRecord);
        }

        public List<NeutralRecord> getNeutralRecordsList() {
            return neutralRecordList;
        }

    }
}
