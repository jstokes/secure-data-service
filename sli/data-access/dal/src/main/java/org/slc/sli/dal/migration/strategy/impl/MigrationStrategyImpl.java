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

import org.slc.sli.dal.migration.strategy.TransformStrategy;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: pghosh
 * Date: 12/10/12
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */

@Component
public abstract class MigrationStrategyImpl implements TransformStrategy {
    /**
     *
     * @return
     */
    public Map<String, Object> getParameters() {
        return parameters;
    }

    /**
     *
     * @param parameters
     */
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    /**
     *  Parameters to be populated from the config
     */
    private Map<String,Object> parameters;

}
