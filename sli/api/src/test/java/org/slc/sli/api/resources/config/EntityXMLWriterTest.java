package org.slc.sli.api.resources.config;

import org.junit.Before;
import org.junit.Test;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.representation.EntityResponse;
import org.slc.sli.api.representation.ErrorResponse;
import org.slc.sli.api.representation.Home;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for EntityWriter
 *
 */
public class EntityXMLWriterTest {
    private EntityXMLWriter writer = null;

    @Before
    public void setup() {
        writer = new EntityXMLWriter();
    }

    @Test
    public void testEntityBody() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        EntityBody body = new EntityBody();

        body.put("id", "1234");
        body.put("name", "test name");

        EntityResponse response = new EntityResponse("TestEntity", body);
        writer.writeTo(response, null, null, null, null, null, out);

        assertNotNull("Should not be null", out);

        String value = new String(out.toByteArray());
        assertTrue("Should match", value.indexOf("<TestEntity>") > 0);
        assertTrue("Should match", value.indexOf("<id>") > 0);
        assertTrue("Should match", value.indexOf("<name>") > 0);
    }

    @Test
    public void testEntityNullCollection() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        EntityBody body = new EntityBody();
        body.put("id", "1234");
        body.put("name", "test name");

        EntityResponse response = new EntityResponse(null, body);
        writer.writeTo(response, null, null, null, null, null, out);

        assertNotNull("Should not be null", out);

        String value = new String(out.toByteArray());
        assertTrue("Should match", value.indexOf("<Entity>") > 0);
    }

    @Test
    public void testIsWritable() {
        assertFalse(writer.isWriteable(ErrorResponse.class, null, null, null));
        assertFalse(writer.isWriteable(Home.class, null, null, null));
        assertTrue(writer.isWriteable(EntityResponse.class, null, null, null));
    }
}
