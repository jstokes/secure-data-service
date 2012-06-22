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


package org.slc.sli.entity;

import java.io.Serializable;

/**
 * Represents a simplified EdOrg path selection to be used to determine configs
 * and some other application related logic.
 *
 */
public class EdOrgKey implements Serializable {
    private static final long serialVersionUID = -6946791865233296339L;
    private String sliId;

    public EdOrgKey(String sliId) {
        this.sliId = sliId;
    }

    /**
     * Get the SLI id of the district
     *
     * @return The SLI id
     */
    public String getSliId() {
        return sliId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return prime + ((sliId == null) ? 0 : sliId.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        EdOrgKey other = (EdOrgKey) obj;
        if (sliId == null) {
            if (other.sliId != null) {
                return false;
            }
        } else if (!sliId.equals(other.sliId)) {
            return false;
        }
        return true;
    }
}
