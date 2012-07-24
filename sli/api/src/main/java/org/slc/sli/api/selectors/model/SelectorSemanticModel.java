package org.slc.sli.api.selectors.model;

import org.slc.sli.modeling.uml.Type;

import java.util.Map;

/**
 * Represents a semantic model of the selectors
 *
 * @author srupasinghe
 *
 */
public interface SelectorSemanticModel {

    public Map<Type, Object> parse(Map<String, Object> selectors, Type type);

}
