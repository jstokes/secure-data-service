/*
 * Copyright 2012 Shared Learning Collaborative, LLC
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

package org.slc.sli.dal.migration.strategy.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slc.sli.dal.migration.strategy.MigrationException;
import org.slc.sli.dal.migration.strategy.MigrationStrategy;
import org.slc.sli.domain.Entity;

/**
 * Supports the migration of entities by renaming a data field.
 * 
 * @author kmyers
 */

public class RenameFieldStrategy implements MigrationStrategy {

    public static final String OLD_FIELD_NAME = "oldFieldName";
    public static final String NEW_FIELD_NAME = "newFieldName";
    
    private String oldFieldName;
    private String newFieldName;
    
    @Override
    public Entity migrate(Entity entity) throws MigrationException {
        
        try {
            Object fieldValue = PropertyUtils.getNestedProperty(entity.getBody(), oldFieldName);
            PropertyUtils.setProperty(entity.getBody(), newFieldName, fieldValue);
        } catch (IllegalAccessException e) {
            throw new MigrationException(e);
        } catch (InvocationTargetException e) {
            throw new MigrationException(e);
        } catch (NoSuchMethodException e) {
            throw new MigrationException(e);
        }
        return entity;
    }

    @Override
    public void setParameters(Map<String, Object> parameters) throws MigrationException {

        if (parameters == null) {
            throw new MigrationException(new IllegalArgumentException("Rename strategy missing required arguments: "));
        }

        if (!parameters.containsKey(OLD_FIELD_NAME)) {
            throw new MigrationException(new IllegalArgumentException("Rename strategy missing required argument: " + OLD_FIELD_NAME));
        }

        if (!parameters.containsKey(NEW_FIELD_NAME)) {
            throw new MigrationException(new IllegalArgumentException("Rename strategy missing required argument: " + NEW_FIELD_NAME));
        }
        
        this.oldFieldName = parameters.get(OLD_FIELD_NAME).toString();
        this.newFieldName = parameters.get(NEW_FIELD_NAME).toString();
    }
    
}
