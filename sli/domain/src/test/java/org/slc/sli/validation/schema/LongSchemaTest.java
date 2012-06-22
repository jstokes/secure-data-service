/*
 * Copyright 2012 Shared Learning Collaborative, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.slc.sli.validation.schema;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * JUnits for LongSchema
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class LongSchemaTest {
    
    @Autowired
    LongSchema schema;
    
    @Test
    public void testDecimalValidation() throws IllegalArgumentException {
        assertFalse(schema.validate(1000.00D));
        assertTrue(schema.validate(1000));
        assertTrue(schema.validate(1000L));
        assertFalse(schema.validate(1000.24));
        assertTrue(schema.validate("1234"));
        assertFalse(schema.validate("test"));
        assertFalse(schema.validate(""));
    }
    
    @Test
    public void testRestrictions() {
        schema.getProperties().put(Restriction.MIN_INCLUSIVE.getValue(), 0);
        schema.getProperties().put(Restriction.MAX_INCLUSIVE.getValue(), 20);
        
        assertTrue(schema.validate(0));
        assertTrue(schema.validate(1));
        assertTrue(schema.validate(20));
        assertFalse(schema.validate(-1));
        assertFalse(schema.validate(30));
        
        schema.getProperties().put(Restriction.MIN_EXCLUSIVE.getValue(), 0);
        schema.getProperties().put(Restriction.MAX_EXCLUSIVE.getValue(), 19);
        assertFalse(schema.validate(0));
        assertTrue(schema.validate(18));
        assertFalse(schema.validate(19));
    }
    
    @Test
    public void testConvert() throws Exception {
        long value = 10L;
        Object convertedValue = this.schema.convert("" + value);
        assertTrue(convertedValue instanceof Long);
        Long convertedInput = (Long) convertedValue;
        assertTrue(convertedInput.longValue() == value);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBadConvert() {
        this.schema.convert("INVALID INPUT");
    }

    @Test
    public void testNonConvert() {
        Object convertedValue = this.schema.convert(12345L);
        assertTrue(convertedValue instanceof Long);
    }
    

    @Test
    public void testLongConverter() {
        
        long data = 12345L;
        int intData = (int) data;
        
        assertTrue("Failure returning same object",
                this.schema.convert(data).equals(data));
        assertTrue("Failure parsing long from integer",
                this.schema.convert(intData).equals(data));
        assertTrue("Failure parsing long data", 
                this.schema.convert("" + data).equals(data));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStringThrowsException() throws IllegalArgumentException {
        this.schema.convert("INVALID INPUT");
    }
    

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedObjectTypeThrowsException() throws IllegalArgumentException {
        this.schema.convert(new Object());
    }
    
}
