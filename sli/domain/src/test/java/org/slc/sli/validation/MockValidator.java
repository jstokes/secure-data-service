package org.slc.sli.validation;

import java.util.ArrayList;

import org.slc.sli.domain.Entity;

/**
 * Mock validator for the dal
 */
public class MockValidator implements EntityValidator {

    @Override
    public boolean validate(Entity entity) throws EntityValidationException {
        if (entity.getBody().containsKey("bad-entity")) {
            throw new EntityValidationException(entity.getEntityId(), entity.getType(), new ArrayList<ValidationError>());
        }
        return true;
    }

    @Override
    public void setReferenceCheck(String referenceCheck) {
    }
}
