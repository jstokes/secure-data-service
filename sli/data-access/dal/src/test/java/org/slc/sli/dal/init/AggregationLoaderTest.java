package org.slc.sli.dal.init;

import com.mongodb.CommandResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.io.InputStream;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author jstokes
 */
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class AggregationLoaderTest {
    private static Logger log = LoggerFactory.getLogger(AggregationLoaderTest.class);

    @InjectMocks
    @Spy
    private AggregationLoader aggregationLoader = new AggregationLoader(); // class under test

    @Mock
    private MongoTemplate mongoTemplate;

    @Before
    public void setup() {
        // assume mongo template can do its job
        CommandResult cr = mock(CommandResult.class);
        when(mongoTemplate.executeCommand(anyString())).thenReturn(cr);
        when(cr.ok()).thenReturn(true);
    }

    @Test
    public void testInit() {
        when(aggregationLoader.getFiles()).thenReturn(new ArrayList<String>());
        aggregationLoader.init();
    }

    @Test
    public void testLoadJavascriptFile() {
        String testFile = "testJS.js";
        InputStream in = getClass().getResourceAsStream("/aggregationDefinitions/" + testFile);

        String out = aggregationLoader.loadJavascriptFile(in);
        assertEquals("test aggregation loader\n\n", out);
    }

    @Test
    public void testLoadNonExistentFile() {
        String testFile = "nonExistent";
        InputStream in = getClass().getResourceAsStream(testFile);

        String out = aggregationLoader.loadJavascriptFile(in);
        assertEquals("", out);
    }
}
