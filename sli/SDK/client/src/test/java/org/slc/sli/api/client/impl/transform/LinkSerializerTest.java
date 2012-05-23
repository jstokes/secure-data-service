package org.slc.sli.api.client.impl.transform;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import org.slc.sli.api.client.impl.BasicLink;

/**
 * Test Link serialization
 */
public class LinkSerializerTest {
    
    ObjectMapper mapper = new ObjectMapper();
    
    @Test
    public void testLinkSerialize() throws IOException {
        
        BasicLink e = TestHelpers.createBasicLink();
        
        String jsonString = mapper.writeValueAsString(e);
        
        assertNotNull(jsonString);
        JsonNode eNode = mapper.readTree(jsonString);
        
        // System.err.println(TestHelpers.LinkJsonObject.toString());
        // System.err.println(eNode.toString());
        
        assertTrue(TestHelpers.LINK_JSON_OBJECT.equals(eNode));
    }
    
}
