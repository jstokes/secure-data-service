package org.slc.sli.modeling.uml.index;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slc.sli.modeling.uml.Association;
import org.slc.sli.modeling.uml.AssociationEnd;
import org.slc.sli.modeling.uml.ClassType;
import org.slc.sli.modeling.uml.DataType;
import org.slc.sli.modeling.uml.EnumType;
import org.slc.sli.modeling.uml.Generalization;
import org.slc.sli.modeling.uml.Identifier;
import org.slc.sli.modeling.uml.Model;
import org.slc.sli.modeling.uml.NamespaceOwnedElement;
import org.slc.sli.modeling.uml.TagDefinition;
import org.slc.sli.modeling.uml.Type;
import org.slc.sli.modeling.uml.ModelElement;
import org.slc.sli.modeling.uml.UmlPackage;
import org.slc.sli.modeling.uml.Visitor;

/**
 * A default implementation of {@link Mapper} that uses {@link Model}.
 *
 * Usage: When parsing into a {@link Model} is complete, the model should be set on this class. The
 * object contained in the model will then be available as a graph.
 */
public final class DefaultMapper implements Mapper {

    private static final <T> T assertNotNull(final T obj, final Identifier memo) {
        if (obj != null) {
            return obj;
        } else {
            throw new RuntimeException(memo.toString());
        }
    }

    private final List<Association> associations;
    private final Map<Identifier, ClassType> classTypeIndex;
    private final Map<Identifier, DataType> dataTypeIndex;
    private final Map<Identifier, ModelElement> elementMap;
    private final Map<String, Set<ModelElement>> nameMap;
    private final Map<Identifier, Set<ModelElement>> whereUsed;
    private final Map<Identifier, EnumType> enumTypeIndex;
    private final List<Generalization> generalizations;

    private final Map<Identifier, TagDefinition> tagDefinitionIndex;

    public DefaultMapper(final Model model) {
        final IndexingVisitor visitor = new IndexingVisitor();
        model.accept(visitor);
        elementMap = Collections
                .unmodifiableMap(new HashMap<Identifier, ModelElement>(visitor.getModelElementMap()));
        whereUsed = Collections.unmodifiableMap(new HashMap<Identifier, Set<ModelElement>>(visitor.getWhereUsed()));
        nameMap = Collections.unmodifiableMap(new HashMap<String, Set<ModelElement>>(visitor.getNameMap()));

        final Map<Identifier, ClassType> classTypeIndex = new HashMap<Identifier, ClassType>();
        final Map<Identifier, DataType> dataTypeIndex = new HashMap<Identifier, DataType>();
        final Map<Identifier, EnumType> enumTypeIndex = new HashMap<Identifier, EnumType>();
        final Map<Identifier, TagDefinition> tagDefinitionIndex = new HashMap<Identifier, TagDefinition>();
        final List<Association> associations = new LinkedList<Association>();
        final List<Generalization> generalizations = new LinkedList<Generalization>();
        final List<UmlPackage> pkgs = new LinkedList<UmlPackage>();

        for (final NamespaceOwnedElement ownedElement : model.getOwnedElements()) {
            if (ownedElement instanceof ClassType) {
                final ClassType classType = (ClassType) ownedElement;
                classTypeIndex.put(classType.getId(), classType);
            } else if (ownedElement instanceof DataType) {
                final DataType dataType = (DataType) ownedElement;
                dataTypeIndex.put(dataType.getId(), dataType);
            } else if (ownedElement instanceof EnumType) {
                final EnumType dataType = (EnumType) ownedElement;
                enumTypeIndex.put(dataType.getId(), dataType);
            } else if (ownedElement instanceof TagDefinition) {
                final TagDefinition dataType = (TagDefinition) ownedElement;
                tagDefinitionIndex.put(dataType.getId(), dataType);
            } else if (ownedElement instanceof Association) {
                final Association association = (Association) ownedElement;
                associations.add(association);
            } else if (ownedElement instanceof Generalization) {
                final Generalization generalization = (Generalization) ownedElement;
                generalizations.add(generalization);
            } else if (ownedElement instanceof UmlPackage) {
                final UmlPackage pkg = (UmlPackage) ownedElement;
                pkgs.add(pkg);
            } else {
                throw new AssertionError(ownedElement);
            }
        }
        this.classTypeIndex = Collections.unmodifiableMap(classTypeIndex);
        this.dataTypeIndex = Collections.unmodifiableMap(dataTypeIndex);
        this.enumTypeIndex = Collections.unmodifiableMap(enumTypeIndex);
        this.tagDefinitionIndex = Collections.unmodifiableMap(tagDefinitionIndex);
        this.associations = Collections.unmodifiableList(associations);
        this.generalizations = Collections.unmodifiableList(generalizations);
    }

    @Override
    public List<AssociationEnd> getAssociationEnds(final Identifier type) {
        // It might be a good idea to cache this when the model is known.
        final List<AssociationEnd> ends = new LinkedList<AssociationEnd>();
        for (final Association candidate : associations) {
            {
                final AssociationEnd candidateEnd = candidate.getLHS();
                final Identifier endType = candidateEnd.getType();
                if (endType.equals(type)) {
                    ends.add(candidate.getRHS());
                }
            }
            {
                final AssociationEnd candidateEnd = candidate.getRHS();
                final Identifier endType = candidateEnd.getType();
                if (endType.equals(type)) {
                    ends.add(candidate.getLHS());
                }
            }
        }
        return Collections.unmodifiableList(ends);
    }

    @Override
    public Iterable<ClassType> getClassTypes() {
        return classTypeIndex.values();
    }

    @Override
    public Iterable<DataType> getDataTypes() {
        return dataTypeIndex.values();
    }

    @Override
    public Iterable<EnumType> getEnumTypes() {
        return enumTypeIndex.values();
    }

    @Override
    public List<Generalization> getGeneralizationBase(final Identifier derived) {
        // It might be a good idea to cache this when the model is known.
        final List<Generalization> base = new LinkedList<Generalization>();
        for (final Generalization generalization : generalizations) {
            final Identifier child = generalization.getChild();
            if (child.equals(derived)) {
                base.add(generalization);
            }
        }
        return Collections.unmodifiableList(base);
    }

    @Override
    public List<Generalization> getGeneralizationDerived(final Identifier base) {
        // It might be a good idea to cache this when the model is known.
        final List<Generalization> derived = new LinkedList<Generalization>();
        for (final Generalization generalization : generalizations) {
            final Identifier parent = generalization.getParent();
            if (parent.equals(base)) {
                derived.add(generalization);
            }
        }
        return Collections.unmodifiableList(derived);
    }

    @Override
    public TagDefinition getTagDefinition(final Identifier reference) {
        return assertNotNull(tagDefinitionIndex.get(reference), reference);
    }

    @Override
    public Type getType(final Identifier reference) {
        if (elementMap.containsKey(reference)) {
            final ModelElement element = elementMap.get(reference);
            if (element instanceof Type) {
                return (Type) element;
            } else {
                throw new IllegalArgumentException(reference.toString());
            }
        } else {
            throw new IllegalArgumentException(reference.toString());
        }
    }

    @Override
    public Set<ModelElement> whereUsed(final Identifier id) {
        if (whereUsed.containsKey(id)) {
            return Collections.unmodifiableSet(whereUsed.get(id));
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public Set<ModelElement> lookupByName(final String name) {
        if (nameMap.containsKey(name)) {
            return Collections.unmodifiableSet(nameMap.get(name));
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public void lookup(final Identifier id, final Visitor visitor) {
        elementMap.get(id).accept(visitor);
    }
}
