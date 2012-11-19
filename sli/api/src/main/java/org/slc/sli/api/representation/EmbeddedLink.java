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


package org.slc.sli.api.representation;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Representation of a link to an entity, association, or other resource.
 * Intended for use within response bodies, not headers.
 *
 * @author Ryan Farris <rfarris@wgen.net>
 *
 */
public class EmbeddedLink {
    @JsonProperty("rel")
    private String rel;
    @JsonIgnore
    @JsonProperty("type")
    private String type;
    @JsonProperty("href")
    private String href;

    public EmbeddedLink(String rel, String type, String href) {
        this.rel = rel;
        this.type = type;
        this.href = href;
    }

    @JsonIgnore
    public String getRel() {
        return rel;
    }

    @JsonIgnore
    public String getType() {
        return type;
    }

    @JsonIgnore
    public String getHref() {
        return href;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((href == null) ? 0 : href.hashCode());
        result = prime * result + ((rel == null) ? 0 : rel.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
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
        
        EmbeddedLink other = (EmbeddedLink) obj;
        if (href == null && other.href != null) {
            return false;
        } else if (!href.equals(other.href)) {
            return false;
        }
        
        if (rel == null && other.rel != null) {
            return false;
        } else if (!rel.equals(other.rel)) {
            return false;
        }
        
        if (type == null && other.type != null) {
            return false;
        } else if (!type.equals(other.type)) {
            return false;
        } 
        
        return true;
    }

    @Override
    public String toString() {
        return "EmbeddedLink [rel=" + rel + ", type=" + type + ", href=" + href + "]";
    }

}
