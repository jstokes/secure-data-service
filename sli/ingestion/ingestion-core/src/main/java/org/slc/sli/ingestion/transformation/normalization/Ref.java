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

/**
 * Holds definition for external reference that needs to be resolved.
 *
 * @author okrook
 *
 */
public class Ref {
    private String entityType;
    private List<List<Field>> choiceOfFields;
    private boolean isRefList = false;
    private String refObjectPath;
    private boolean optional = false;

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public List<List<Field>> getChoiceOfFields() {
        return choiceOfFields;
    }

    public void setChoiceOfFields(List<List<Field>> choiceOfFields) {
        this.choiceOfFields = choiceOfFields;
    }

    public boolean isRefList() {
        return isRefList;
    }

    public void setIsRefList(boolean isRefList) {
        this.isRefList = isRefList;
    }

    public String getRefObjectPath() {
        return refObjectPath;
    }

    public void setRefObjectPath(String refObjectPath) {
        this.refObjectPath = refObjectPath;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public boolean isOptional() {
        return optional;
    }

    @Override
    public String toString() {
        return "Ref [entityType=" + entityType + ", choiceOfFields=" + choiceOfFields + ", isRefList="
                + isRefList + ", refObjectPath=" + refObjectPath + ", optional=" + optional + "]";
    }

}
