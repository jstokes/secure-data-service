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
 * An GenericEntity in the SLI domain.
 *
 * @author slee
 *
 */
public abstract class GenericEntity
{
    protected static ObjectMapper mapper = new ObjectMapper();

    /**
     *  Constructor
     */
    public GenericEntity() {

    }

    /**
     * Output this Entity as a JSON String
     */
    abstract public String jsonString();
    /**
     * Output this Entity as a JSON Node
     */
    abstract public JsonNode json();

}
