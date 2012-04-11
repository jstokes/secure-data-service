package org.slc.sli.ingestion.smooks.mappings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.util.EntityTestUtils;

/**
 * Test the smooks mappings for Program entity.
 *
 * @author vmcglaughlin
 *
 */
public class ProgramEntityTest {

    /**
     * Test that Ed-Fi program is correctly mapped to a NeutralRecord.
     * @throws IOException
     * @throws SAXException
     */
    @Ignore
    @Test
    public final void mapEdfiXmlProgramToNeutralRecordTest() throws IOException, SAXException {

        String smooksXmlConfigFilePath = "smooks_conf/smooks-all-xml.xml";

        String targetSelector = "InterchangeEducationOrganization/Program";

        String edfiXml = null;

        try {
            edfiXml = EntityTestUtils.readResourceAsString("smooks/unitTestData/ProgramEntity.xml");
        } catch (FileNotFoundException e) {
            System.err.println(e);
            Assert.fail();
        }

        NeutralRecord neutralRecord = EntityTestUtils
                .smooksGetSingleNeutralRecord(smooksXmlConfigFilePath,
                        targetSelector, edfiXml);

        checkValidNeutralRecord(neutralRecord);
    }

    private void checkValidNeutralRecord(NeutralRecord neutralRecord) {
        assertEquals("Expecting different record type", "program", neutralRecord.getRecordType());

        assertEquals("Expected different local id", "ACC-TEST-PROG-1", neutralRecord.getLocalId());
        assertEquals("Expected no local parent ids", 0, neutralRecord.getLocalParentIds().size());

        Map<String, Object> attributes = neutralRecord.getAttributes();

        assertEquals("Expected different entity id", "ACC-TEST-PROG-1", attributes.get("programId"));
        assertEquals("Expected different program sponsor", "State Education Agency", attributes.get("programSponsor"));
        assertEquals("Expected different program type", "Regular Education", attributes.get("programType"));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> services = (List<Map<String, Object>>) attributes.get("services");

        assertNotNull("Expected non-null list of services", services);

        assertEquals("Expected two services", 2, services.size());

        Map<String, Object> service1 = services.get(0);
        assertNotNull("Expected non-null service", service1);
        assertEquals("Expected different short description of service", "Short description for acceptance test program service 1", service1.get("shortDescription"));
        assertEquals("Expected different description of service", "This is a longer description of the services provided by acceptance test program service 1. More detail could be provided here.", service1.get("description"));
        assertEquals("Expected different service code value", "Test service 1", service1.get("codeValue"));

        Map<String, Object> service2 = services.get(1);
        assertNotNull("Expected non-null service", service2);
        assertEquals("Expected different short description of service", "Short description for acceptance test program service 2", service2.get("shortDescription"));
        assertEquals("Expected different description of service", "This is a longer description of the services provided by acceptance test program service 2. More detail could be provided here.", service2.get("description"));
        assertEquals("Expected different service code value", "Test service 2", service2.get("codeValue"));
    }

}
