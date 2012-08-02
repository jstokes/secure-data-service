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

package org.slc.sli.sif.domain.slientity;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * An GenericEntity in the SLI domain. Each SLI Entity can be converted to a
 * JSON Node ready for SLI operations.
 *
 * @author slee
 *
 */
public abstract class GenericEntity {
    protected static final ObjectMapper mapper = new ObjectMapper();

    /**
    * Constructor
    */
    public GenericEntity() {

    }

    /**
    * Output this Entity as a JSON Node
    */
    public JsonNode json() {
        return mapper.valueToTree(this);
    }

    /**
    * Output this object as a JSON String
    */
    @Override
    public String toString() {
        return json().toString();
    }

}
