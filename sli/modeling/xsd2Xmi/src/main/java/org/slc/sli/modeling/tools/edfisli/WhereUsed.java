package org.slc.sli.modeling.tools.edfisli;

import java.io.FileNotFoundException;
import java.util.Set;

import org.slc.sli.modeling.uml.Attribute;
import org.slc.sli.modeling.uml.ClassType;
import org.slc.sli.modeling.uml.Identifier;
import org.slc.sli.modeling.uml.Model;
import org.slc.sli.modeling.uml.UmlModelElement;
import org.slc.sli.modeling.uml.index.DefaultMapper;
import org.slc.sli.modeling.uml.index.Mapper;
import org.slc.sli.modeling.xmi.reader.XmiReader;

public final class WhereUsed {

    public static void main(final String[] args) {
        try {
            final Model model = XmiReader.readModel("Ed-Fi-Core.xmi");
            final Mapper index = new DefaultMapper(model);

            final String name = "Grade";
            final Set<UmlModelElement> matches = index.lookupByName(name);
            for (final UmlModelElement match : matches) {
                System.out.println("name : " + name + " => " + match);
                showUsage(index, match.getId(), "  ");
            }
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static final void showUsage(final Mapper index, final Identifier id, final String indent) {
        final Set<UmlModelElement> usages = index.whereUsed(id);
        for (final UmlModelElement usage : usages) {
            if (usage instanceof ClassType) {
                final ClassType classType = (ClassType) usage;
                System.out.println(indent + "classType : " + classType.getName());
                showUsage(index, classType.getId(), indent.concat("  "));
            } else if (usage instanceof Attribute) {
                final Attribute attribute = (Attribute) usage;
                System.out.println(indent + "attribute : " + attribute.getName());
                showUsage(index, attribute.getId(), indent.concat("  "));
            } else {
                System.out.println(indent + "usage : " + usage.getClass());
            }
        }
    }
}
