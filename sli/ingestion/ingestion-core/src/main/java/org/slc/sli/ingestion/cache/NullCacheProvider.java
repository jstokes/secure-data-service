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


package org.slc.sli.ingestion.cache;
/**
 * Null cache
 *
 * @author smelody
 *
 */
public class NullCacheProvider implements CacheProvider {

    @Override
    // Does nothing since we are the null provider
    public void add(String key, Object value) {
        // Does nothing
    }

    @Override
    public Object get(String key) {
        return null;
    }

    @Override
    public void flush() {
        // TODO Auto-generated method stub

    }

}
