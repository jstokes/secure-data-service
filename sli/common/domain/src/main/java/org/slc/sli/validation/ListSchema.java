package org.slc.sli.validation;

import java.util.LinkedList;
import java.util.List;

import org.slc.sli.validation.ValidationError.ErrorType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 
 * SLI List Schema which validates a list or collection of entities
 * 
 * @author Robert Bloh <rbloh@wgen.net>
 * 
 */
@Scope("prototype")
@Component
public class ListSchema extends NeutralSchema {
    
    // Attributes
    private List<NeutralSchema> list = new LinkedList<NeutralSchema>();
    
    // Constructors
    public ListSchema() {
        this(NeutralSchemaType.LIST.getName());
    }
    
    public ListSchema(String xsdType) {
        super(xsdType);
    }
    
    // Methods
    public boolean isPrimitive() {
        return false;
    }
    
    public void setList(List<NeutralSchema> list) {
        this.list = list;
    }
    
    public List<NeutralSchema> getList() {
        return this.list;
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
     * @return true if valid
     */
    protected boolean validate(String fieldName, Object entity, List<ValidationError> errors) {
        boolean isValid = true;
        
        if (entity instanceof List) {
            List<?> entityList = (List<?>) entity;
            for (Object fieldEntity : entityList) {
                
                // Allow validation according to ANY item Schemas in the ListSchema list (xs:choice
                // scenario)
                boolean isFieldValid = false;
                for (NeutralSchema itemSchema : this.getList()) {
                    
                    // Choice scenario will not provide validation errors (null)
                    if (itemSchema.validate(fieldEntity)) {
                        isFieldValid = true;
                        break;
                    }
                }
                
                if (!isFieldValid) {
                    isValid = false;
                    if (errors == null) {
                        return false;
                    }
                }
            }
        } else {
            return addError(false, fieldName, entity, "List", ErrorType.INVALID_DATATYPE, errors);
        }
        
        return isValid;
    }
    
}
