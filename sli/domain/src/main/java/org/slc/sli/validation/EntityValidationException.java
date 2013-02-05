/*
 * Copyright 2012-2013 inBloom, Inc. and its affiliates.
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


package org.slc.sli.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Ryan Farris <rfarris@wgen.net>
 *
 */
public class EntityValidationException extends RuntimeException {

    private static final long serialVersionUID = 5579596873501684518L;

    final List<ValidationError> errors;
    final String entityId;
    final String entityType;

    public EntityValidationException(String entityId, String entityType, List<ValidationError> errors) {
    	super();
        this.entityId = entityId;
        this.entityType = entityType;
        this.errors = Collections.unmodifiableList(errors);
    }
    
    public EntityValidationException(Exception e, String entityId,
			String entityType, ArrayList<ValidationError> errors) {
    	super(e);
        this.entityId = entityId;
        this.entityType = entityType;
        this.errors = Collections.unmodifiableList(errors);
	}

	public List<ValidationError> getValidationErrors() {
        return errors;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getEntityType() {
        return entityType;
    }
}


