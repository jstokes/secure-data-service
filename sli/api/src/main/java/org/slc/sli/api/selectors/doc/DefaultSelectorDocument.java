package org.slc.sli.api.selectors.doc;

import org.codehaus.plexus.util.StringUtils;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.modeling.uml.AssociationEnd;
import org.slc.sli.modeling.uml.ClassType;
import org.slc.sli.modeling.uml.index.DefaultModelIndex;
import org.slc.sli.modeling.uml.index.ModelIndex;
import org.slc.sli.modeling.xmi.reader.XmiReader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of a selector document
 *
 * @author srupasinghe
 *
 */
@Component
public class DefaultSelectorDocument implements SelectorDocument {

    private ModelIndex model;
    private Map<String, ClassType> types;


    @PostConstruct
    public void init() throws FileNotFoundException {
        String xmiFile = "/Users/srupasinghe/git/SLI/sli/domain/src/main/resources/sliModel/SLI.xmi";
        model = new DefaultModelIndex(XmiReader.readModel(xmiFile));

        types = model.getClassTypes();
    }

    @Override
    public void aggregate(Map<ClassType, Object> queryMap, Constraint constraint) {
        List<String> result = null;

        if (queryMap == null) return;

        for (Map.Entry<ClassType, Object> entry : queryMap.entrySet()) {

            if (Map.class.isInstance(entry.getValue())) {

            } else if (List.class.isInstance(entry.getValue())) {

                List<Object> attributes = (List<Object>) entry.getValue();
                ClassType type = entry.getKey();

                if (constraint == null) {
                    constraint = new Constraint();

                    String key = getKeyName(type);
                    constraint.setKey(key);
                    constraint.setValue(result);
                }

                //result = executeQuery(type, attributes, constraint);
                constraint = null;
            }
        }
    }

    private List<String> executeQuery(ClassType type, List<String> attributes, Constraint constraint) {
        NeutralQuery query = new NeutralQuery();
        query.addCriteria(new NeutralCriteria(constraint.getKey(),
                NeutralCriteria.CRITERIA_IN, constraint.getValue()));
        query.setIncludeFields(StringUtils.join(attributes.iterator(), ","));

        System.out.println("Running query : collection[" + type.getName() + "] " + query);

        List<String> result = new ArrayList<String>();
        result.add("34");
        result.add("56");

        return result;
    }

    private String getKeyName(ClassType type) {
        String key = "id";

        if (type.isAssociation()) {
            AssociationEnd end = type.getRHS();

            key = StringUtils.uncapitalise(end.getName()) + "Id";
        }

        return key;
    }

    protected Map<String, ClassType> getTypes() {
        return types;
    }
}
