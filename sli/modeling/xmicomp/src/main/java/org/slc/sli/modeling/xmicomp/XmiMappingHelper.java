package org.slc.sli.modeling.xmicomp;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slc.sli.modeling.uml.AssociationEnd;
import org.slc.sli.modeling.uml.Attribute;
import org.slc.sli.modeling.uml.ClassType;
import org.slc.sli.modeling.uml.Feature;
import org.slc.sli.modeling.uml.index.ModelIndex;

/**
 * Utilities for manipulating structures used in establishing mappings.
 */
public final class XmiMappingHelper {
    
    private static Set<CaseInsensitiveQName> commonFeatures(final Set<CaseInsensitiveQName> lhs,
            final Set<CaseInsensitiveQName> rhs) {
        final Set<CaseInsensitiveQName> result = new HashSet<CaseInsensitiveQName>(lhs);
        result.retainAll(rhs);
        return Collections.unmodifiableSet(result);
    }
    
    private static XmiFeature computeMissingFeature(final Map<CaseInsensitiveQName, XmiFeature> declaredFeatures,
            final ClassType classType, final Feature feature, final XmiDefinition which) {
        final CaseInsensitiveQName name = makeKey(classType, feature);
        if (!declaredFeatures.containsKey(name)) {
            final String className = classType.getName();
            final String featureName = feature.getName();
            return new XmiFeature(featureName, className);
        } else {
            return null;
        }
    }
    
    private static final CaseInsensitiveQName makeKey(final ClassType classType, final Feature feature) {
        if (classType == null) {
            throw new NullPointerException("classType");
        }
        if (feature == null) {
            throw new NullPointerException("feature");
        }
        return new CaseInsensitiveQName(classType.getName(), feature.getName());
    }
    
    private static final CaseInsensitiveQName makeKey(final XmiFeature feature) {
        if (feature == null) {
            throw new NullPointerException("feature");
        }
        return new CaseInsensitiveQName(feature.getType(), feature.getName());
    }
    
    private static final Map<CaseInsensitiveQName, Integer> makeView(final List<XmiMapping> mappings,
            final boolean isLHS) {
        final Map<CaseInsensitiveQName, Integer> view = new HashMap<CaseInsensitiveQName, Integer>();
        for (int index = 0; index < mappings.size(); index++) {
            final XmiMapping mapping = mappings.get(index);
            final XmiFeature feature = isLHS ? mapping.getLhsFeature() : mapping.getRhsFeature();
            if (feature != null) {
                view.put(new CaseInsensitiveQName(feature.getType(), feature.getName()), index);
            }
        }
        return view;
    }
    
    public static final void mergeCommonFeatures(final Map<CaseInsensitiveQName, XmiFeature> lhsMissing,
            final Map<CaseInsensitiveQName, XmiFeature> rhsMissing, final List<XmiMapping> mappings,
            final String mergeComment) {
        final Set<CaseInsensitiveQName> common = commonFeatures(lhsMissing.keySet(), rhsMissing.keySet());
        for (final CaseInsensitiveQName name : common) {
            mappings.add(new XmiMapping(lhsMissing.get(name), rhsMissing.get(name), XmiMappingStatus.MATCH,
                    mergeComment));
            lhsMissing.remove(name);
            rhsMissing.remove(name);
        }
    }
    
    public static final void mergeMissingFeatures(final Map<CaseInsensitiveQName, XmiFeature> missing,
            final List<XmiMapping> mappings, final boolean isLHS, final String mergeComment) {
        final Map<CaseInsensitiveQName, Integer> view = XmiMappingHelper.makeView(mappings, !isLHS);
        final Set<CaseInsensitiveQName> missingNames = new HashSet<CaseInsensitiveQName>(missing.keySet());
        for (final CaseInsensitiveQName missingName : missingNames) {
            if (view.containsKey(missingName)) {
                final int index = view.get(missingName);
                final XmiMapping oldMapping = mappings.get(index);
                final XmiFeature lhs = isLHS ? missing.get(missingName) : oldMapping.getLhsFeature();
                final XmiFeature rhs = isLHS ? oldMapping.getRhsFeature() : missing.get(missingName);
                final XmiMapping newMapping = new XmiMapping(lhs, rhs, XmiMappingStatus.MATCH, mergeComment);
                mappings.set(index, newMapping);
                missing.remove(missingName);
            }
        }
    }
    
    public static void mergeRemaining(final Map<CaseInsensitiveQName, XmiFeature> features,
            final List<XmiMapping> mappings, final boolean isLHS, final String comment) {
        if (comment == null) {
            throw new NullPointerException("comment");
        }
        final boolean isRHS = !isLHS;
        final Set<CaseInsensitiveQName> names = new HashSet<CaseInsensitiveQName>(features.keySet());
        for (final CaseInsensitiveQName name : names) {
            final XmiFeature lhsFeature = isLHS ? features.get(name) : null;
            final XmiFeature rhsFeature = isRHS ? features.get(name) : null;
            final XmiMapping mapping = new XmiMapping(lhsFeature, rhsFeature, XmiMappingStatus.UNKNOWN, comment);
            mappings.add(mapping);
            features.remove(name);
        }
        
    }
    
    public static final Map<CaseInsensitiveQName, XmiFeature> missingFeatures(final List<XmiMapping> mappings,
            final XmiDefinition which, final ModelIndex model, final boolean isLHS) {
        
        final Map<CaseInsensitiveQName, XmiFeature> missingFeatures = new HashMap<CaseInsensitiveQName, XmiFeature>();
        
        final Map<CaseInsensitiveQName, XmiFeature> declaredFeatures = new HashMap<CaseInsensitiveQName, XmiFeature>();
        for (final XmiMapping mapping : mappings) {
            final XmiFeature feature = isLHS ? mapping.getLhsFeature() : mapping.getRhsFeature();
            if (feature != null) {
                declaredFeatures.put(makeKey(feature), feature);
            }
        }
        for (final ClassType classType : model.getClassTypes().values()) {
            for (final Attribute attribute : classType.getAttributes()) {
                final XmiFeature missing = computeMissingFeature(declaredFeatures, classType, attribute, which);
                if (missing != null) {
                    missingFeatures.put(makeKey(missing), missing);
                }
            }
            for (final AssociationEnd associationEnd : model.getAssociationEnds(classType.getId())) {
                final XmiFeature missing = computeMissingFeature(declaredFeatures, classType, associationEnd, which);
                if (missing != null) {
                    missingFeatures.put(makeKey(missing), missing);
                }
            }
        }
        return Collections.unmodifiableMap(missingFeatures);
    }
    
    public static final <T> Map<String, T> makeLowerCaseKey(final Map<String, T> map) {
        final Map<String, T> copy = new HashMap<String, T>(map.size());
        for (final String key : map.keySet()) {
            copy.put(key.toLowerCase(), map.get(key));
        }
        return copy;
    }
    
    private XmiMappingHelper() {
        
    }
}
