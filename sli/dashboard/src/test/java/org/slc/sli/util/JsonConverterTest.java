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


/**
 *  JUnit test for JsonConverter
 */
package org.slc.sli.util;

import static org.junit.Assert.*;

import org.junit.Test;

import org.slc.sli.entity.GenericEntity;

/**
 * @author Takashi Osako
 * JUnit Test
 */
public class JsonConverterTest {
    
    @Test
    public void test() {
        GenericEntity entity = new GenericEntity();
        GenericEntity element = new GenericEntity();
        element.put("tire", "Yokohama");
        entity.put("car", element);
        assertEquals("{\"car\":{\"tire\":\"Yokohama\"}}", JsonConverter.toJson(entity));
    }
}
