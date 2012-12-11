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
package org.slc.sli.dal.migration.strategy;

import org.slc.sli.domain.Entity;

import java.util.Map;

/**
 * Defines a strategy for transforming entities between versions
 * 
 * @author sashton
 * @author kmyers
 * 
 */
public interface TransformStrategy {

    /**
     * Transforms a single entity
     * 
     * @param entity
     */
    public Entity transform(Entity entity) throws MigrationException;
    
    
    /**
     * Sets the transforms strategy implementation's specific parameters.
     * 
     * @param parameter
     */
    public void setParameters(Map<String, Object> parameters);

}
