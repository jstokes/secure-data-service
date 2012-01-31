package org.slc.sli.validation.schema;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * JUnits for DateSchema
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class DateSchemaTest {
    
    @Autowired
    DateSchema schema;
    
    @Test
    public void testDateStringValidation() throws IllegalArgumentException {
        // String dateString = "2012-01-01-05:00";
        String dateString = "2012-01-01T12:00:00-05:00";
        assertTrue("DateTime entity validation failed", schema.validate(dateString));
    }
    
    @Test
    public void testDateValidation() {
        Calendar calendar = Calendar.getInstance();
        String dateString = javax.xml.bind.DatatypeConverter.printDate(calendar);
        assertTrue("Date entity validation failed", schema.validate(dateString));
    }
    
    @Test
    public void testValidationOfStringFailure() {
        String stringEntity = "";
        assertFalse("Expected DateSchema string validation failure did not succeed", schema.validate(stringEntity));
    }
    
}
