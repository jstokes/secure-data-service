package org.slc.sli.validation.schema;

import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.validation.EntityValidationRepository;
import org.slc.sli.validation.NeutralSchemaType;
import org.slc.sli.validation.ValidationError;
import org.slc.sli.validation.ValidationError.ErrorType;

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
    
    @Override
    public NeutralSchemaType getSchemaType() {
        return NeutralSchemaType.DURATION;
    }

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
     * @param repo
     *            reference to the entity repository           
     * @return true if valid
     */
    protected boolean validate(String fieldName, Object entity, List<ValidationError> errors, EntityValidationRepository repo) {
        boolean isValid;
        try {
            javax.xml.datatype.DatatypeFactory.newInstance().newDuration((String) entity);
            isValid = true;
        } catch (IllegalArgumentException e2) {
            isValid = false;
        } catch (DatatypeConfigurationException e) {
            isValid = false;
        }
        return addError(isValid, fieldName, entity, "ISO 8601 Duration", ErrorType.INVALID_DATE_FORMAT, errors);
    }
    
}
