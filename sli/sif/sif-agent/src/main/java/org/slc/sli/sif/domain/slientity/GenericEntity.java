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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slc.sli.api.client.Entity;

/**
 * An GenericEntity in the SLI domain.
 * Each SLI Entity can be converted to a JSON Node ready for SLI operations.
 *
 * @author slee
 *
 */
public abstract class GenericEntity implements Entity
{
    protected static ObjectMapper mapper = new ObjectMapper();
    private JsonNode jsonNode = null;

    /**
     *  Constructor
     */
    public GenericEntity() {

    }

    /**
     * Output this Entity as a JSON Node
     */
    public JsonNode json() {
        if (this.jsonNode==null) 
            this.jsonNode = mapper.valueToTree(this);
        return this.jsonNode;
    }

    /**
     * Output this object as a JSON String
     */
    @Override
    public String toString() {
        return json().toString();
    }

    /**
     * Get the data associated with this entity. If the entity has no data, returns
     * an empty map. The key into this map is the property name. The values of this
     * map can one of the following JSON types:
     * 
     * <ul>
     * <li>List</li>
     * <li>Map</li>
     * <li>null</li>
     * <li>Boolean</li>
     * <li>Character</li>
     * <li>Long</li>
     * <li>Double</li>
     * <li>String</li>
     * </ul>
     * 
     * @return Map of data.
     */
    @Override
    public Map<String, Object> getData() {
        try
        {
            if (this.jsonNode==null)
                return null;
            else 
                return mapper.readValue(this.jsonNode, new TypeReference<Map<String, Object>>(){});
        } catch (JsonParseException e)
        {
            e.printStackTrace();
        } catch (JsonMappingException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
