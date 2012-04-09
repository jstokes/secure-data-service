package org.slc.sli.ingestion.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import org.slc.sli.ingestion.landingzone.validation.TestErrorReport;

/**
 *
 * @author Thomas Shewchuk (tshewchuk@wgen.net)
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/validation-context.xml" })
public class XsdErrorHandlerTest {

    @Autowired
    private XsdErrorHandler xsdErrorHandler;

    private final ErrorReport errorReport = new TestErrorReport();

    private final SAXParseException mockedSAXParseException = Mockito.mock(SAXParseException.class);

    @Before
    public void setup() {
        // Assign the error report to the error handler.
        xsdErrorHandler.setErrorReport(errorReport);
    }

    @Test
    public void testWarning() {
        // Test receiving a SAX warning.
        xsdErrorHandler.setIsValid(true);
        when(mockedSAXParseException.getMessage()).thenReturn("SAXParseException warning");
        xsdErrorHandler.warning(mockedSAXParseException);
        assertTrue(errorReport.hasErrors());
        assertTrue(xsdErrorHandler.isValid());
    }

    @Test
    public void testError() {
        // Test receiving a SAX error.
        xsdErrorHandler.setIsValid(true);
        when(mockedSAXParseException.getMessage()).thenReturn("SAXParseException error");
        xsdErrorHandler.error(mockedSAXParseException);
        assertTrue(errorReport.hasErrors());
        assertFalse(xsdErrorHandler.isValid());
    }

    @Test
    public void testFatalError() throws SAXException {
        // Test receiving a SAX fatal error.
        xsdErrorHandler.setIsValid(true);
        when(mockedSAXParseException.getMessage()).thenReturn("SAXParseException fatal error");
        try {
            xsdErrorHandler.fatalError(mockedSAXParseException);
        } catch (SAXException e) {
            // This is expected.
            assertNotNull(e);
        }
        assertTrue(errorReport.hasErrors());
        assertFalse(xsdErrorHandler.isValid());
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void testGetErrorMessage() throws Exception {
        // Test conversion of a SAX to ingestion error message.
        // Make the XsdErrorHandler private method getErrorMessage accessible.
        try {
            Class[] argClasses = new Class[1];
            argClasses[0] = org.xml.sax.SAXParseException.class;
            Method method = XsdErrorHandler.class.getDeclaredMethod("getErrorMessage", argClasses);
            method.setAccessible(true);
            Object[] argObjects = new Object[1];
            when(mockedSAXParseException.getSystemId()).thenReturn("\\home\\landingzone\\TestFile.xml");
            when(mockedSAXParseException.getLineNumber()).thenReturn(12581);
            when(mockedSAXParseException.getColumnNumber()).thenReturn(36);
            when(mockedSAXParseException.getMessage()).thenReturn("SAXParseException fatal error");
            argObjects[0] = mockedSAXParseException;
            String errorMessage = method.invoke(xsdErrorHandler, argObjects).toString();
            assertEquals(errorMessage,
                    "File TestFile.xml, Line 12581, Column 36:\nSAXParseException fatal error");
        } catch (Exception e) {
            // Should never happen.
            throw e;
        }
    }

}
