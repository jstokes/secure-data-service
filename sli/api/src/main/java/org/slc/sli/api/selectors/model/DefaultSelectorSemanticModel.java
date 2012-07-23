package org.slc.sli.api.selectors.model;

import org.slc.sli.modeling.uml.ClassType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of semantic model of the selectors
 *
 * @author srupasinghe
 *
 */
@Component
public class DefaultSelectorSemanticModel implements SelectorSemanticModel {

    @Autowired
    private ModelProvider modelProvider;

    @Override
    public Map<ClassType, Object> parse(Map<String, Object> selectors, ClassType type) {
        Map<ClassType, Object> selectorsWithType = new HashMap<ClassType, Object>();
        parse(selectors, type, selectorsWithType);
        return selectorsWithType;
    }

    private void parse(Map<String, Object> selectors, ClassType type, Map<ClassType, Object> selectorsWithType) {
        for (Map.Entry<String, Object> entry : selectors.entrySet()) {
            Object value = entry.getValue();

            if (Map.class.isInstance(value)) {
                ClassType newType = modelProvider.getType(type, entry.getKey());

                if (newType != null) {
                    Map<ClassType, Object> newMap = new HashMap<ClassType, Object>();
                    parse((Map<String, Object>) value, newType, newMap);

                    if (selectorsWithType.containsKey(type)) {
                        ((List<Object>) selectorsWithType.get(type)).add(newMap);
                    } else {
                        List<Object> attrs = new ArrayList<Object>();
                        attrs.add(newMap);
                        selectorsWithType.put(type, attrs);
                    }

                } else {
                    throw new RuntimeException("Invalid Selectors " + entry.getKey());
                }
            } else {

                if (modelProvider.isAssociation(type, entry.getKey())) {
                    type = modelProvider.getType(type, entry.getKey());
                }

                //if (isInModel(type, entry.getKey())) {
                //AttributeType attributeType = new AttributeType(entry.getKey(), type);
                if (selectorsWithType.containsKey(type)) {
                    ((List<Object>) selectorsWithType.get(type)).add(entry.getKey());
                } else {
                    List<Object> attrs = new ArrayList<Object>();
                    attrs.add(entry.getKey());
                    selectorsWithType.put(type, attrs);
                }

                //    selectorsWithType.put(type, value);
                //} else {
                //    throw new RuntimeException("Invalid Selectors " + entry.getKey());
                //}

            }
        }
    }

}
