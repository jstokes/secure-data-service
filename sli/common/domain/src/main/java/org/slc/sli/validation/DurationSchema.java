package org.slc.sli.validation;

import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.slc.sli.validation.ValidationError.ErrorType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 
 * SLI Duration Schema which validates duration entities
 * 
 * @author Robert Bloh <rbloh@wgen.net>
 * 
 */
@Scope("prototype")
@Component
public class DurationSchema extends NeutralSchema {
    
    // Constructors
    public DurationSchema() {
        this(NeutralSchemaType.DURATION.getName());
    }
    
    public DurationSchema(String xsdType) {
        super(xsdType);
    }
    
    // Methods
    
    /**
     * Validates the given entity
     * Returns true if the validation was successful or a ValidationException if the validation was
     * unsuccessful.
     * 
     * @param fieldName
     *            name of entity field being validated
     * @param entity
     *            being validated using this SLI Schema
     * @param errors
     *            list of current errors
     * @return true if valid
     */
    protected boolean validate(String fieldName, Object entity, List<ValidationError> errors) {
        boolean isValid = false;
        try {
            javax.xml.datatype.DatatypeFactory.newInstance().newDuration((String) entity);
            isValid = true;
        } catch (IllegalArgumentException e2) {
            // do nothing
        } catch (DatatypeConfigurationException e) {
            // do nothing
        }
        return addError(isValid, fieldName, entity, "ISO 8601 Duration", ErrorType.INVALID_DATE_FORMAT, errors);
    }
    
}
