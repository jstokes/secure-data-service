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


package org.slc.sli.modeling.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

/**
 * A <code>representation</code> element describes a representation of a resource's state.
 */
public final class Representation extends WadlElement {
    private final String id;
    private final QName element;
    private final String mediaType;
    private final List<String> profiles;
    private final List<Param> params;

    public Representation(final String id, final QName element, final String mediaType, final List<String> profiles,
            final List<Documentation> doc, final List<Param> params) {
        super(doc);
        if (null == mediaType) {
            throw new NullPointerException("mediaType");
        }
        if (null == profiles) {
            throw new NullPointerException("profiles");
        }
        if (null == params) {
            throw new NullPointerException("params");
        }
        this.id = id;
        this.element = element;
        this.mediaType = mediaType;
        this.profiles = Collections.unmodifiableList(new ArrayList<String>(profiles));
        this.params = Collections.unmodifiableList(new ArrayList<Param>(params));
    }

    public String getId() {
        return id;
    }

    public QName getElement() {
        return element;
    }

    public String getMediaType() {
        return mediaType;
    }

    public List<String> getProfiles() {
        return profiles;
    }

    public List<Param> getParams() {
        return params;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("id").append(" : ").append(id);
        sb.append(", ");
        sb.append("element").append(" : ").append(element);
        sb.append(", ");
        sb.append("mediaType").append(" : ").append(mediaType);
        sb.append(", ");
        sb.append("profiles").append(" : ").append(profiles);
        sb.append(", ");
        sb.append("params").append(" : ").append(params);
        sb.append(", ");
        sb.append("doc").append(" : ").append(getDocumentation());
        sb.append("}");
        return sb.toString();
    }
}