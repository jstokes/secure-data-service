package org.slc.sli.api.selectors.model;

import org.slc.sli.api.selectors.doc.SelectorQuery;
import org.slc.sli.api.selectors.doc.SelectorQueryVisitor;
import org.slc.sli.modeling.uml.Attribute;
import org.slc.sli.modeling.uml.ClassType;
import org.slc.sli.modeling.uml.ModelElement;


/**
 * Implementation of a selector element that has a ModelElement => SemanticSelector structure
 *
 * @author jstokes
 */
public class ComplexSelectorElement implements SelectorElement {
    private final SemanticSelector selector;
    private final ModelElement modelElement;
    private final boolean typed;

    public ComplexSelectorElement(final ModelElement modelElement, final SemanticSelector selector) {
        this.modelElement = modelElement;
        this.selector = selector;
        this.typed = modelElement instanceof ClassType;
    }

    @Override
    public boolean isTyped() {
        return typed;
    }

    @Override
    public boolean isAttribute() {
        return !typed;
    }

    @Override
    public ModelElement getLHS() {
        return modelElement;
    }

    @Override
    public Object getRHS() {
        return selector;
    }

    @Override
    public SelectorQuery accept(final SelectorQueryVisitor selectorQueryVisitor) {
        return selectorQueryVisitor.visit(this);
    }

    @Override
    public String getElementName() {
        if (modelElement instanceof ClassType) {
            return ((ClassType) modelElement).getName();
        } else if (modelElement instanceof Attribute) {
            return ((Attribute) modelElement).getName();
        }
        return null;
    }

    public SemanticSelector getSelector() {
        return selector;
    }

    @Override
    public String toString() {
        return "{" + getElementName() + " : " + getSelector() + "}";
    }
}
