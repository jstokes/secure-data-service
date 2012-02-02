package org.slc.sli.validation.schema;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * JUnits for ListSchema
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class ListSchemaTest {
    
    @Autowired
    ListSchema schema;
    
    @Autowired
    ComplexSchema complexSchema;
    
    @Autowired
    BooleanSchema booleanSchema;
    
    @Autowired
    LongSchema longSchema;
    
    @Autowired
    DoubleSchema doubleSchema;
    
    @Autowired
    StringSchema stringSchema;
    
    @Autowired
    TokenSchema tokenSchema;
    
    @Autowired
    DateTimeSchema dateTimeSchema;
    
    @Test
    public void testListOfBooleanValidation() throws IllegalArgumentException {
        schema.getFields().clear();
        schema.getList().add(booleanSchema);
        List<Boolean> listEntity = new ArrayList<Boolean>();
        Boolean booleanEntity = true;
        listEntity.add(booleanEntity);
        assertTrue("List of boolean entity validation failed", schema.validate(listEntity));
    }
    
    @Test
    public void testListOfBooleanFailureValidation() throws IllegalArgumentException {
        schema.getFields().clear();
        schema.getList().add(booleanSchema);
        List<String> listEntity = new ArrayList<String>();
        String stringEntity = "test";
        
        // Setup for failure
        listEntity.add(stringEntity);
        
        assertFalse("Expected ListSchema boolean validation failure did not succeed", schema.validate(listEntity));
    }
    
    @Test
    public void testRestrictions() {
        schema.getFields().clear();
        schema.getList().add(longSchema);
        schema.getProperties().put(Restriction.MIN_LENGTH.getValue(), 1);
        schema.getProperties().put(Restriction.MAX_LENGTH.getValue(), 3);
        List<Long> listEntity = new ArrayList<Long>();
        assertFalse(schema.validate(listEntity));
        listEntity.add(1L);
        assertTrue(schema.validate(listEntity));
        listEntity.add(2L);
        assertTrue(schema.validate(listEntity));
        listEntity.add(3L);
        assertTrue(schema.validate(listEntity));
        listEntity.add(4L);
        assertFalse(schema.validate(listEntity));
        schema.getProperties().put(Restriction.LENGTH.getValue(), 2);
        assertFalse(schema.validate(listEntity));
        listEntity.clear();
        assertFalse(schema.validate(listEntity));
        listEntity.add(1L);
        listEntity.add(2L);
        assertTrue(schema.validate(listEntity));
    }
    
    @Test
    public void testListOfLongValidation() throws IllegalArgumentException {
        schema.getFields().clear();
        schema.getList().add(longSchema);
        List<Long> listEntity = new ArrayList<Long>();
        Long longEntity = 0L;
        listEntity.add(longEntity);
        assertTrue("List entity long validation failed", schema.validate(listEntity));
    }
    
    @Test
    public void testListOfLongFailureValidation() throws IllegalArgumentException {
        schema.getFields().clear();
        schema.getList().add(longSchema);
        List<String> listEntity = new ArrayList<String>();
        String stringEntity = "test";
        
        // Setup for failure
        listEntity.add(stringEntity);
        
        assertFalse("Expected ListSchema long validation failure did not succeed", schema.validate(listEntity));
    }
    
    @Test
    public void testListOfStringValidation() throws IllegalArgumentException {
        schema.getFields().clear();
        schema.getList().add(stringSchema);
        List<String> listEntity = new ArrayList<String>();
        String stringEntity = "test";
        listEntity.add(stringEntity);
        assertTrue("List entity string validation failed", schema.validate(listEntity));
    }
    
    @Test
    public void testListOfStringFailureValidation() throws IllegalArgumentException {
        schema.getFields().clear();
        schema.getList().add(stringSchema);
        List<Double> listEntity = new ArrayList<Double>();
        Double doubleEntity = 0.0;
        
        // Setup for failure
        listEntity.add(doubleEntity);
        
        assertFalse("Expected ListSchema string validation failure did not succeed", schema.validate(listEntity));
    }
    
    @Test
    public void testListOfComplexValidation() throws IllegalArgumentException {
        schema.getFields().clear();
        schema.getList().add(complexSchema);
        complexSchema.getFields().clear();
        complexSchema.getFields().put("booleanField", booleanSchema);
        complexSchema.getFields().put("longField", longSchema);
        complexSchema.getFields().put("doubleField", doubleSchema);
        complexSchema.getFields().put("stringField", stringSchema);
        complexSchema.getFields().put("tokenField", tokenSchema);
        complexSchema.getFields().put("dateTimeField", dateTimeSchema);
        tokenSchema.getProperties().clear();
        List<String> tokens = new ArrayList<String>();
        tokens.add("validToken");
        tokenSchema.getProperties().put(TokenSchema.TOKENS, tokens);
        Map<String, Object> complexEntity = new HashMap<String, Object>();
        Boolean booleanEntity = true;
        Long longEntity = 0L;
        Double doubleEntity = 0.0;
        BigDecimal decimalEntity = new BigDecimal(0);
        String stringEntity = "test";
        String tokenEntity = "validToken";
        String dateTimeEntity = "2012-01-01T12:00:00-05:00";
        complexEntity.put("booleanField", booleanEntity);
        complexEntity.put("longField", longEntity);
        complexEntity.put("doubleField", doubleEntity);
        complexEntity.put("decimalField", decimalEntity);
        complexEntity.put("stringField", stringEntity);
        complexEntity.put("tokenField", tokenEntity);
        complexEntity.put("dateTimeField", dateTimeEntity);
        List<Map<String, Object>> listEntity = new ArrayList<Map<String, Object>>();
        listEntity.add(complexEntity);
        assertTrue("List entity complex validation failed", schema.validate(listEntity));
    }
    
    @Test
    public void testListOfComplexFailureValidation() throws IllegalArgumentException {
        schema.getFields().clear();
        schema.getList().add(complexSchema);
        List<String> listEntity = new ArrayList<String>();
        String stringEntity = "test";
        
        // Setup for failure
        listEntity.add(stringEntity);
        
        assertFalse("Expected ListSchema complex validation failure did not succeed", schema.validate(listEntity));
    }
    
    @Test
    public void testValidationOfBooleanFailure() {
        Boolean booleanEntity = true;
        assertFalse("Expected ListSchema boolean validation failure did not succeed", schema.validate(booleanEntity));
    }
    
    @Test
    public void testValidationOfIntegerFailure() {
        Integer integerEntity = 0;
        assertFalse("Expected ListSchema integer validation failure did not succeed", schema.validate(integerEntity));
    }
    
    @Test
    public void testValidationOfFloatFailure() {
        Float floatEntity = new Float(0);
        assertFalse("Expected ListSchema float validation failure did not succeed", schema.validate(floatEntity));
    }
    
}
