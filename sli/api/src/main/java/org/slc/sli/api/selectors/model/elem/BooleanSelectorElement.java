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

package org.slc.sli.api.selectors.model.elem;

import org.slc.sli.api.selectors.doc.SelectorQuery;
import org.slc.sli.api.selectors.doc.SelectorQueryVisitor;
import org.slc.sli.modeling.uml.ClassType;
import org.slc.sli.modeling.uml.ModelElement;


/**
 * Implementation of a selector element that has a ModelElement => boolean structure
 *
 * @author jstokes
 */
public class BooleanSelectorElement extends AbstractSelectorElement implements SelectorElement {
    private final boolean qualifier;

    public BooleanSelectorElement(final ModelElement modelElement, final boolean qualifier) {
        this.qualifier = qualifier;
        super.setElement(modelElement);
        super.setTyped(modelElement instanceof  ClassType);
    }

    @Override
    public Object getRHS() {
        return qualifier;
    }

    public boolean getQualifier() {
        return qualifier;
    }

    @Override
    public SelectorQuery accept(final SelectorQueryVisitor selectorQueryVisitor) {
        return selectorQueryVisitor.visit(this);
    }

    @Override
    public String toString() {
        return "{" + getElementName() + " : " + getQualifier() + "}";
    }
}
