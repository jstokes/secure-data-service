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


package org.slc.sli.modeling.xdm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

public final class DmDocument implements DmNode {

    private static final QName NO_NAME = new QName("");

    private final List<DmNode> children;

    public DmDocument(final List<? extends DmNode> childAxis) {
        if (childAxis == null) {
            throw new NullPointerException("childAxis");
        }
        this.children = Collections.unmodifiableList(new ArrayList<DmNode>(childAxis));
    }

    @Override
    public List<DmNode> getChildAxis() {
        return children;
    }

    @Override
    public QName getName() {
        return NO_NAME;
    }

    @Override
    public String getStringValue() {
        throw new UnsupportedOperationException("TODO");
    }
}
