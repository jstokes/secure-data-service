package org.slc.sli.unit.entity;

import junit.framework.Assert;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.slc.sli.entity.CustomConfig;
import org.springframework.test.context.ContextConfiguration;


/**
 * CustomConfig JUnit test.
 * 
 */
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = { "/application-context-test.xml" })
public class CustomConfigTest {
    
    public static final String DEFAULT_CUSTOM_CONFIG_JSON = "{" 

            + "\"component_1\":"
            + "{"
            + "\"id\":\"component_1\","
            + "\"name\":\"Component 1\","
            + "\"type\":\"LAYOUT\","
            + "\"items\":["
            + "{\"id\":\"component_1_1\",\"name\":\"First Child Component\",\"type\":\"PANEL\"},"
            + "{\"id\":\"component_1_2\",\"name\":\"Second Child Component\",\"type\":\"PANEL\"}"
            + "]"
            + "}"
            
            + ","

            + "\"component_2\":"
            + "{"
            + "\"id\":\"component_2\","
            + "\"name\":\"Component 2\","
            + "\"type\":\"LAYOUT\","
            + "\"items\":["
            + "{\"id\":\"component_2_1\",\"name\":\"First Child Component\",\"type\":\"PANEL\"},"
            + "{\"id\":\"component_2_2\",\"name\":\"Second Child Component\",\"type\":\"PANEL\"}"
            + "]"
            + "}"
            
            + "}";
    
    @Before
    public void setup() {
    }
    
    @Test
    public void testCreate() throws Exception {
        
        Gson gson = new GsonBuilder().create();
        CustomConfig customConfig = gson.fromJson(DEFAULT_CUSTOM_CONFIG_JSON, CustomConfig.class);
        Assert.assertEquals(2, customConfig.size());
        Assert.assertEquals("component_1", customConfig.get("component_1").getId());
        Assert.assertEquals(DEFAULT_CUSTOM_CONFIG_JSON, customConfig.toJson());
    }
        
}
