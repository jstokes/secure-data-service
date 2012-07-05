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


package org.slc.sli.ingestion.transformation.normalization;

import java.util.List;
import java.util.Map;

/**
 * Holds definition for a field that is part of reference resolution.
 *
 * @author okrook
 *
 */
public class Field {
    private String path;
    private List<FieldValue> values;
    private boolean isList;
    private Map<String, String> queryList;
    private String entityKey;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<FieldValue> getValues() {
        return values;
    }

    public void setValues(List<FieldValue> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "Field [path=" + path + ", values=" + values + "]";
    }

    public boolean getIsList() {
        return isList;
    }

    public void setIsList(boolean value) {
        this.isList = value;
    }

    public Map<String, String> getQueryList() {
        return queryList;
    }

    public void setQueryList(Map<String, String> queryList) {
        this.queryList = queryList;
    }

    public String getEntityKey() {
        return entityKey;
    }

    public void setEntityKey(String entityKey) {
        this.entityKey = entityKey;
    }
}
