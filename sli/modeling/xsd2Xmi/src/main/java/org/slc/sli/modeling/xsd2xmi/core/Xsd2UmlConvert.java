package org.slc.sli.modeling.xsd2xmi.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaAnnotated;
import org.apache.ws.commons.schema.XmlSchemaAnnotation;
import org.apache.ws.commons.schema.XmlSchemaAppInfo;
import org.apache.ws.commons.schema.XmlSchemaChoice;
import org.apache.ws.commons.schema.XmlSchemaComplexContentExtension;
import org.apache.ws.commons.schema.XmlSchemaComplexContentRestriction;
import org.apache.ws.commons.schema.XmlSchemaComplexType;
import org.apache.ws.commons.schema.XmlSchemaContent;
import org.apache.ws.commons.schema.XmlSchemaDocumentation;
import org.apache.ws.commons.schema.XmlSchemaElement;
import org.apache.ws.commons.schema.XmlSchemaEnumerationFacet;
import org.apache.ws.commons.schema.XmlSchemaFacet;
import org.apache.ws.commons.schema.XmlSchemaInclude;
import org.apache.ws.commons.schema.XmlSchemaMaxLengthFacet;
import org.apache.ws.commons.schema.XmlSchemaMinLengthFacet;
import org.apache.ws.commons.schema.XmlSchemaObject;
import org.apache.ws.commons.schema.XmlSchemaObjectCollection;
import org.apache.ws.commons.schema.XmlSchemaParticle;
import org.apache.ws.commons.schema.XmlSchemaPatternFacet;
import org.apache.ws.commons.schema.XmlSchemaSequence;
import org.apache.ws.commons.schema.XmlSchemaSimpleContentExtension;
import org.apache.ws.commons.schema.XmlSchemaSimpleContentRestriction;
import org.apache.ws.commons.schema.XmlSchemaSimpleType;
import org.apache.ws.commons.schema.XmlSchemaSimpleTypeContent;
import org.apache.ws.commons.schema.XmlSchemaSimpleTypeList;
import org.apache.ws.commons.schema.XmlSchemaSimpleTypeRestriction;
import org.apache.ws.commons.schema.XmlSchemaSimpleTypeUnion;
import org.apache.ws.commons.schema.XmlSchemaType;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.slc.sli.modeling.uml.Association;
import org.slc.sli.modeling.uml.AssociationEnd;
import org.slc.sli.modeling.uml.Attribute;
import org.slc.sli.modeling.uml.ClassType;
import org.slc.sli.modeling.uml.DataType;
import org.slc.sli.modeling.uml.EnumLiteral;
import org.slc.sli.modeling.uml.EnumType;
import org.slc.sli.modeling.uml.Generalization;
import org.slc.sli.modeling.uml.Identifier;
import org.slc.sli.modeling.uml.Model;
import org.slc.sli.modeling.uml.Multiplicity;
import org.slc.sli.modeling.uml.NamespaceOwnedElement;
import org.slc.sli.modeling.uml.Occurs;
import org.slc.sli.modeling.uml.Range;
import org.slc.sli.modeling.uml.TagDefinition;
import org.slc.sli.modeling.uml.TaggedValue;
import org.slc.sli.modeling.uml.UmlPackage;
import org.slc.sli.modeling.uml.Visitor;
import org.slc.sli.modeling.xml.XmlTools;
import org.slc.sli.modeling.xsd.WxsNamespace;

public final class Xsd2UmlConvert {

	private static final List<TaggedValue> EMPTY_TAGGED_VALUES = Collections
			.emptyList();

	/**
	 * The xs:annotation is parsed into {@link TaggedValue} with one entry per
	 * xs:documentation or xs:appinfo.
	 */
	private static final List<TaggedValue> annotations(
			final XmlSchemaAnnotated schemaType, final Xsd2UmlConfig config) {
		final XmlSchemaAnnotation annotation = schemaType.getAnnotation();
		if (schemaType == null || annotation == null) {
			return EMPTY_TAGGED_VALUES;
		} else {
			final List<TaggedValue> taggedValues = new LinkedList<TaggedValue>();
			final XmlSchemaObjectCollection children = annotation.getItems();

			for (int childIdx = 0; childIdx < children.getCount(); ++childIdx) {

				final XmlSchemaObject child = children.getItem(childIdx);
				if (child instanceof XmlSchemaDocumentation) {
					final XmlSchemaDocumentation documentation = (XmlSchemaDocumentation) child;
					taggedValues.add(documentation(documentation, config));
				} else if (child instanceof XmlSchemaAppInfo) {
					final XmlSchemaAppInfo appInfo = (XmlSchemaAppInfo) child;
					taggedValues
							.addAll(config.tagsFromAppInfo(appInfo, config));
				} else {
					throw new AssertionError(child);
				}
			}

			return taggedValues;
		}
	}

	private static final void convertComplexType(
			final XmlSchemaComplexType complexType, final XmlSchema schema,
			final Xsd2UmlConfig config, final Visitor handler,
			final QName complexTypeName, final List<TaggedValue> taggedValues) {

		// final QName complexTypeName = complexType.getQName();
		final Identifier complexTypeId = config.ensureId(complexTypeName);

		final List<Attribute> attributes = new LinkedList<Attribute>();

		if (complexType.getContentModel() != null
				&& complexType.getContentModel().getContent() != null) {
			final XmlSchemaContent content = complexType.getContentModel()
					.getContent();
			if (content instanceof XmlSchemaComplexContentExtension) {
				final XmlSchemaComplexContentExtension complexContentExtension = (XmlSchemaComplexContentExtension) content;
				attributes.addAll(parseFields(complexContentExtension, schema,
						config));
				// The base of the restriction is interpreted as a UML
				// generalization.
				final QName base = complexContentExtension.getBaseTypeName();
				final Identifier baseId = config.ensureId(base);
				// Hack here to support anonymous complex types in the context
				// of elements.
				// Need to fix the SLI MongoDB schemes so that all types are
				// named.
				handler.visit(new Generalization(config
						.nameFromComplexTypeExtension(complexTypeName, base),
						complexTypeId, baseId));
			} else if (content instanceof XmlSchemaComplexContentRestriction) {
				throw new AssertionError(content);
			} else if (content instanceof XmlSchemaSimpleContentExtension) {
				throw new AssertionError(content);
			} else if (content instanceof XmlSchemaSimpleContentRestriction) {
				throw new AssertionError(content);
			} else {
				throw new AssertionError(content);
			}
		}

		attributes.addAll(parseFields(complexType, schema, config));

		handler.visit(new ClassType(complexTypeId, config
				.nameFromTypeName(complexTypeName), false, attributes,
				taggedValues));
	}

	/**
	 * The strategy here is that we discover the facets and then decide whether
	 * we are going to create the UML EnumType or DataType.
	 */
	private static final void convertSimpleType(
			final XmlSchemaSimpleType simpleType, final XmlSchema schema,
			final Xsd2UmlConfig config, final Visitor handler) {
		final Identifier simpleTypeId = config.ensureId(simpleType.getQName());

		final XmlSchemaSimpleTypeContent content = simpleType.getContent();
		if (content instanceof XmlSchemaSimpleTypeList) {
			throw new AssertionError(content);
		} else if (content instanceof XmlSchemaSimpleTypeRestriction) {
			final XmlSchemaSimpleTypeRestriction restriction = (XmlSchemaSimpleTypeRestriction) content;

			// The base of the restriction is interpreted as a UML
			// generalization.
			final Identifier baseId = config.ensureId(restriction
					.getBaseTypeName());

			// Facets are collected as either tagged values or enumeration
			// literals.
			final XmlSchemaObjectCollection facets = restriction.getFacets();

			final List<TaggedValue> taggedValues = new LinkedList<TaggedValue>();
			final List<EnumLiteral> enumLiterals = new LinkedList<EnumLiteral>();

			for (int i = 0, length = facets.getCount(); i < length; i++) {
				final XmlSchemaObject item = facets.getItem(i);
				if (item instanceof XmlSchemaFacet) {
					final XmlSchemaFacet facet = (XmlSchemaFacet) item;
					if (facet instanceof XmlSchemaEnumerationFacet) {
						final XmlSchemaEnumerationFacet enumFacet = (XmlSchemaEnumerationFacet) facet;
						final EnumLiteral enumLiteral = enumLiteralFromFacet(
								enumFacet, config);
						enumLiterals.add(enumLiteral);
					} else if (facet instanceof XmlSchemaMinLengthFacet) {
						final XmlSchemaMinLengthFacet minLengthFacet = (XmlSchemaMinLengthFacet) facet;
						final TaggedValue minLength = minLength(minLengthFacet,
								config);
						taggedValues.add(minLength);
					} else if (facet instanceof XmlSchemaMaxLengthFacet) {
						final XmlSchemaMaxLengthFacet maxLengthFacet = (XmlSchemaMaxLengthFacet) facet;
						final TaggedValue maxLength = maxLength(maxLengthFacet,
								config);
						taggedValues.add(maxLength);
					} else if (facet instanceof XmlSchemaPatternFacet) {
						final XmlSchemaPatternFacet patternFacet = (XmlSchemaPatternFacet) facet;
						final TaggedValue pattern = pattern(patternFacet,
								config);
						taggedValues.add(pattern);
					} else {
						// Implement other facets as we need them.
						throw new AssertionError(facet);
					}
				} else {
					throw new AssertionError(item);
				}
			}

			// We also add the annotations to the list of tagged values.
			taggedValues.addAll(annotations(simpleType, config));
			final Identifier typeIdentifier;
			if (enumLiterals.isEmpty()) {
				final DataType dataType = new DataType(simpleTypeId,
						config.nameFromTypeName(simpleType.getQName()), false,
						taggedValues);
				handler.visit(dataType);
				typeIdentifier = dataType.getId();
				if (!restriction.getBaseTypeName().equals(WxsNamespace.STRING)) {
					handler.visit(new Generalization(config
							.nameFromSimpleTypeRestriction(
									simpleType.getQName(),
									restriction.getBaseTypeName()),
							typeIdentifier, baseId));
				}
			} else {
				final EnumType enumType = new EnumType(simpleTypeId,
						config.nameFromTypeName(simpleType.getQName()),
						enumLiterals, taggedValues);
				handler.visit(enumType);
				typeIdentifier = enumType.getId();
				if (!restriction.getBaseTypeName().equals(WxsNamespace.TOKEN)) {
					handler.visit(new Generalization(config
							.nameFromSimpleTypeRestriction(
									simpleType.getQName(),
									restriction.getBaseTypeName()),
							typeIdentifier, baseId));
				}
			}
		} else if (content instanceof XmlSchemaSimpleTypeUnion) {
			throw new AssertionError(content);
		} else {
			throw new AssertionError(content);
		}
	}

	private static final void convertType(final XmlSchemaType type,
			final XmlSchema schema, final Xsd2UmlConfig context,
			final Visitor handler, final QName name,
			final List<TaggedValue> taggedValues) {

		if (type instanceof XmlSchemaComplexType) {
			final XmlSchemaComplexType complexType = (XmlSchemaComplexType) type;
			convertComplexType(complexType, schema, context, handler, name,
					taggedValues);
		} else if (type instanceof XmlSchemaSimpleType) {
			final XmlSchemaSimpleType simpleType = (XmlSchemaSimpleType) type;
			convertSimpleType(simpleType, schema, context, handler);
		} else {
			throw new AssertionError(type);
		}
	}

	private static final DataType declareBuiltInDataType(
			final String localName, final Xsd2UmlConfig context) {
		final Identifier id = context.ensureId(new QName(WxsNamespace.URI,
				localName));
		return new DataType(id, localName);
	}

	private static final List<DataType> declareBuiltInDataTypes(
			final Xsd2UmlConfig context) {
		final List<DataType> dataTypes = new LinkedList<DataType>();
		dataTypes.add(declareBuiltInDataType("duration", context));
		dataTypes.add(declareBuiltInDataType("dateTime", context));
		dataTypes.add(declareBuiltInDataType("time", context));
		dataTypes.add(declareBuiltInDataType("date", context));
		dataTypes.add(declareBuiltInDataType("gYearMonth", context));
		dataTypes.add(declareBuiltInDataType("gYear", context));
		dataTypes.add(declareBuiltInDataType("gMonthDay", context));
		dataTypes.add(declareBuiltInDataType("gDay", context));
		dataTypes.add(declareBuiltInDataType("gMonth", context));
		dataTypes.add(declareBuiltInDataType("boolean", context));
		dataTypes.add(declareBuiltInDataType("base64Binary", context));
		dataTypes.add(declareBuiltInDataType("hexBinary", context));
		dataTypes.add(declareBuiltInDataType("float", context));
		dataTypes.add(declareBuiltInDataType("decimal", context));
		dataTypes.add(declareBuiltInDataType("integer", context));
		dataTypes.add(declareBuiltInDataType("long", context));
		dataTypes.add(declareBuiltInDataType("int", context));
		dataTypes.add(declareBuiltInDataType("short", context));
		dataTypes.add(declareBuiltInDataType("byte", context));
		dataTypes.add(declareBuiltInDataType("double", context));
		dataTypes.add(declareBuiltInDataType("anyURI", context));
		dataTypes.add(declareBuiltInDataType("QName", context));
		dataTypes.add(declareBuiltInDataType("NOTATION", context));
		dataTypes.add(declareBuiltInDataType("string", context));
		dataTypes.add(declareBuiltInDataType("normalizedString", context));
		dataTypes.add(declareBuiltInDataType("token", context));
		dataTypes.add(declareBuiltInDataType("language", context));
		dataTypes.add(declareBuiltInDataType("ID", context));
		dataTypes.add(declareBuiltInDataType("IDREF", context));
		return dataTypes;
	}

	private static final List<TagDefinition> declareTagDefinitions(
			final Xsd2UmlConfig config) {
		final List<TagDefinition> tagDefinitions = new LinkedList<TagDefinition>();
		tagDefinitions.add(makeTagDefinition("author", Occurs.ZERO, Occurs.ONE,
				config));
		tagDefinitions.add(makeTagDefinition("deprecated", Occurs.ZERO,
				Occurs.ONE, config));
		tagDefinitions.add(makeTagDefinition("documentation", Occurs.ZERO,
				Occurs.ONE, config));
		tagDefinitions.add(makeTagDefinition("version", Occurs.ZERO,
				Occurs.ONE, config));
		// Tag definitions used to represent W3C XML Schema facets.
		tagDefinitions.add(makeTagDefinition("length", Occurs.ZERO, Occurs.ONE,
				config));
		tagDefinitions.add(makeTagDefinition("maxLength", Occurs.ZERO,
				Occurs.ONE, config));
		tagDefinitions.add(makeTagDefinition("minLength", Occurs.ZERO,
				Occurs.ONE, config));
		tagDefinitions.add(makeTagDefinition("pattern", Occurs.ZERO,
				Occurs.ONE, config));
		tagDefinitions.add(makeTagDefinition("whiteSpace", Occurs.ZERO,
				Occurs.ONE, config));
		tagDefinitions.add(makeTagDefinition("maxExclusive", Occurs.ZERO,
				Occurs.ONE, config));
		tagDefinitions.add(makeTagDefinition("minExclusive", Occurs.ZERO,
				Occurs.ONE, config));
		tagDefinitions.add(makeTagDefinition("maxInclusive", Occurs.ZERO,
				Occurs.ONE, config));
		tagDefinitions.add(makeTagDefinition("minInclusive", Occurs.ZERO,
				Occurs.ONE, config));
		// Tag definition used by the plug-in.
		tagDefinitions.addAll(config.declareTagDefinitions(config));
		return tagDefinitions;
	}

	private static final TaggedValue documentation(
			final XmlSchemaDocumentation documentation,
			final Xsd2UmlConfig context) {
		final NodeList markup = documentation.getMarkup();
		final String text = XmlTools.collapseWhitespace(stringValue(markup));
		final Identifier tagDefinition = context
				.ensureTagDefinitionId("documentation");
		return new TaggedValue(text, tagDefinition);
	}

	private static final EnumLiteral enumLiteralFromFacet(
			final XmlSchemaEnumerationFacet enumFacet,
			final Xsd2UmlConfig context) {
		final List<TaggedValue> annotation = annotations(enumFacet, context);
		// We're assuming here that the whiteSpace facet for this enumeration
		// type is "collapse".
		// In general, this will not be true and we will have to compute it.
		final String name = XmlTools.collapseWhitespace(enumFacet.getValue()
				.toString());
		return new EnumLiteral(name, annotation);
	}

	/**
	 * Extracts the {@link Model} from the {@link XmlSchema}.
	 *
	 * This function does not do anything to create associations because these
	 * may require some heuristics to extract. This functionality will be
	 * deferred to a post-processor.
	 */
	public static final Model transform(final String name,
			final XmlSchema schema, final Xsd2UmlPlugin plugin) {
		return extractModel(name, 0, schema, plugin);
	}

	private static final Model extractModel(final String name, final int level,
			final XmlSchema schema, final Xsd2UmlPlugin plugin) {
		final Xsd2UmlConfig context = new Xsd2UmlConfig(plugin);
		return parseXmlSchema(name, level, schema, context, plugin);
	}

	private static final QName getSimpleTypeName(
			final XmlSchemaSimpleType simpleType) {
		final QName typeName = simpleType.getQName();
		if (null == typeName) {
			// The type is anonymous.
			final XmlSchemaSimpleTypeContent content = simpleType.getContent();
			if (content instanceof XmlSchemaSimpleTypeRestriction) {
				final XmlSchemaSimpleTypeRestriction restriction = (XmlSchemaSimpleTypeRestriction) content;
				return restriction.getBaseTypeName();
			} else if (content instanceof XmlSchemaSimpleTypeList) {
				final XmlSchemaSimpleTypeList list = (XmlSchemaSimpleTypeList) content;
				return list.getItemTypeName();
			} else {
				throw new AssertionError(content);
			}
		} else {
			return typeName;
		}
	}

	private static final TagDefinition makeTagDefinition(final String name,
			final Occurs lower, final Occurs upper, final Xsd2UmlConfig config) {
		final Range range = new Range(Identifier.random(), lower, upper,
				EMPTY_TAGGED_VALUES);
		final Multiplicity multiplicity = new Multiplicity(Identifier.random(),
				EMPTY_TAGGED_VALUES, range);
		final Identifier id = config.ensureTagDefinitionId(name);
		return new TagDefinition(id, name, multiplicity);
	}

	private static final TaggedValue maxLength(
			final XmlSchemaMaxLengthFacet maxLength, final Xsd2UmlConfig ctxt) {
		final String value = maxLength.getValue().toString();
		final List<TaggedValue> annotations = annotations(maxLength, ctxt);
		final Identifier tagDefinition = ctxt
				.ensureTagDefinitionId("maxLength");
		return new TaggedValue(Identifier.random(), annotations, value,
				tagDefinition);
	}

	private static final TaggedValue minLength(
			final XmlSchemaMinLengthFacet minLength, final Xsd2UmlConfig ctxt) {
		final String value = minLength.getValue().toString();
		final List<TaggedValue> annotations = annotations(minLength, ctxt);
		final Identifier tagDefinition = ctxt
				.ensureTagDefinitionId("minLength");
		return new TaggedValue(Identifier.random(), annotations, value,
				tagDefinition);
	}

	private static final Multiplicity multiplicity(final Range range) {
		return new Multiplicity(Identifier.random(), EMPTY_TAGGED_VALUES, range);
	}

	private static final Occurs occurs(final long occurs) {
		if (occurs == 0L) {
			return Occurs.ZERO;
		} else if (occurs == 1L) {
			return Occurs.ONE;
		} else if (occurs == Long.MAX_VALUE) {
			return Occurs.UNBOUNDED;
		} else {
			throw new AssertionError(Long.valueOf(occurs));
		}
	}

	private static final Attribute parseElement(final XmlSchemaElement element,
			final XmlSchema schema, final Xsd2UmlConfig config) {

		final String name = config.nameFromElementName(element.getQName());
		final List<TaggedValue> taggedValues = new LinkedList<TaggedValue>();
		taggedValues.addAll(annotations(element, config));

		final XmlSchemaType elementSchemaType = element.getSchemaType();
		final Occurs minOccurs = occurs(element.getMinOccurs());
		final Occurs maxOccurs = occurs(element.getMaxOccurs());
		final Multiplicity multiplicity = multiplicity(range(minOccurs,
				maxOccurs));
		if (elementSchemaType instanceof XmlSchemaComplexType) {
			final QName typeName = elementSchemaType.getQName();
			if (null == typeName) {
				// The type is anonymous.
				final QName baseType = elementSchemaType
						.getBaseSchemaTypeName();
				throw new RuntimeException(element.getQName() + " : "
						+ baseType);
			} else {
				final Identifier type = config.ensureId(typeName);
				return new Attribute(Identifier.random(), name, type,
						multiplicity, taggedValues);
			}
		} else if (elementSchemaType instanceof XmlSchemaSimpleType) {
			final XmlSchemaSimpleType simpleType = (XmlSchemaSimpleType) elementSchemaType;
			final Identifier type = config
					.ensureId(getSimpleTypeName(simpleType));
			return new Attribute(Identifier.random(), name, type, multiplicity,
					taggedValues);
		} else {
			throw new AssertionError(elementSchemaType);
		}
	}

	private static final List<Attribute> parseFields(
			final XmlSchemaComplexContentExtension schemaComplexContentExtension,
			final XmlSchema schema, final Xsd2UmlConfig context) {
		// parseAttributes(schemaComplexContentExtension.getAttributes(),
		// schema);
		return parseParticle(schemaComplexContentExtension.getParticle(),
				schema, context);
	}

	private static final List<Attribute> parseFields(
			final XmlSchemaComplexType schemaComplexType,
			final XmlSchema schema, final Xsd2UmlConfig context) {
		// parseAttributes(schemaComplexType.getAttributes(), schema);
		final List<Attribute> attributes = parseParticle(
				schemaComplexType.getParticle(), schema, context);
		return attributes;
	}

	private static final List<Attribute> parseParticle(
			final XmlSchemaParticle particle, final XmlSchema schema,
			final Xsd2UmlConfig context) {

		final List<Attribute> attributes = new LinkedList<Attribute>();

		if (particle != null) {
			if (particle instanceof XmlSchemaElement) {
				final XmlSchemaElement element = (XmlSchemaElement) particle;
				attributes.add(parseElement(element, schema, context));
			} else if (particle instanceof XmlSchemaSequence) {
				final XmlSchemaSequence schemaSequence = (XmlSchemaSequence) particle;
				for (int i = 0; i < schemaSequence.getItems().getCount(); i++) {
					XmlSchemaObject item = schemaSequence.getItems().getItem(i);
					if (item instanceof XmlSchemaParticle) {
						attributes.addAll(parseParticle(
								(XmlSchemaParticle) item, schema, context));
					} else {
						throw new RuntimeException(
								"Unsupported XmlSchemaSequence item: "
										+ item.getClass().getCanonicalName());
					}
				}
			} else if (particle instanceof XmlSchemaChoice) {

				final XmlSchemaChoice xmlSchemaChoice = (XmlSchemaChoice) particle;

				final XmlSchemaObjectCollection choices = xmlSchemaChoice
						.getItems();
				for (int i = 0; i < choices.getCount(); ++i) {
					XmlSchemaObject item = xmlSchemaChoice.getItems()
							.getItem(i);
					if (item instanceof XmlSchemaParticle) {
						attributes.addAll(parseParticle(
								(XmlSchemaParticle) item, schema, context));
					}
				}

			} else {
				throw new RuntimeException(
						"Unsupported XmlSchemaParticle item: "
								+ particle.getClass().getCanonicalName());
			}
		}
		return Collections.unmodifiableList(attributes);
	}

	private static final Model parseXmlSchema(final String name,
			final int level, final XmlSchema schema,
			final Xsd2UmlConfig config, final Xsd2UmlPlugin plugin) {

		final List<NamespaceOwnedElement> ownedElements = new LinkedList<NamespaceOwnedElement>();

		final XmlSchemaObjectCollection schemaItems = schema.getItems();

		if (level == 0) {
			for (final TagDefinition tagDefinition : declareTagDefinitions(config)) {
				ownedElements.add(tagDefinition);
			}
			final List<DataType> dataTypes = declareBuiltInDataTypes(config);
			final UmlPackage wxsPackage = new UmlPackage(WxsNamespace.URI,
					dataTypes);
			ownedElements.add(wxsPackage);
		}

		// Iterate XML Schema items
		for (int i = 0, count = schemaItems.getCount(); i < count; i++) {
			final XmlSchemaObject schemaObject = schemaItems.getItem(i);

			if (schemaObject instanceof XmlSchemaType) {
				// Capture the anonymous inner type as a type with the name of
				// the element.
				// The SLI.xsd should be fixed to only have named types.
				final XmlSchemaType schemaType = (XmlSchemaType) schemaObject;

				convertType(schemaType, schema, config, new Visitor() {

					@Override
					public void visit(final Association association) {
						throw new AssertionError();
					}

					@Override
					public void visit(final AssociationEnd associationEnd) {
						throw new AssertionError();
					}

					@Override
					public void visit(final Attribute attribute) {
						throw new AssertionError();
					}

					@Override
					public void visit(final ClassType classType) {
						ownedElements.add(classType);
					}

					@Override
					public void visit(final DataType dataType) {
						ownedElements.add(dataType);
					}

					@Override
					public void visit(final EnumLiteral enumLiteral) {
						throw new AssertionError();
					}

					@Override
					public void visit(final EnumType enumType) {
						ownedElements.add(enumType);
					}

					@Override
					public void visit(final Generalization generalization) {
						ownedElements.add(generalization);
					}

					@Override
					public void visit(final Model model) {
						throw new AssertionError();
					}

					@Override
					public void visit(final Multiplicity multiplicity) {
						throw new AssertionError();
					}

					@Override
					public void visit(final Range range) {
						throw new AssertionError();
					}

					@Override
					public void visit(final TagDefinition tagDefinition) {
						throw new AssertionError();
					}

					@Override
					public void visit(final TaggedValue taggedValue) {
						throw new AssertionError();
					}

					@Override
					public void visit(final UmlPackage pkg) {
						throw new AssertionError();
					}
				}, schemaType.getQName(), annotations(schemaType, config));
			} else if (schemaObject instanceof XmlSchemaElement) {
				// These are the top-level elements.
				// There's a bit of hacking here to handle anonymous types
				// in the SLI.xsd. We
				// should try to get rid of the cause. We'll still use the
				// existence of the element
				// to infer the existence of REST resource and we'll guess at a
				// collection name.
				final XmlSchemaElement element = (XmlSchemaElement) schemaObject;
				if (element.getSchemaTypeName() == null) {
					final XmlSchemaType elementType = element.getSchemaType();

					// The first hack will be to pick up the documentation for
					// the type from the
					// element.
					final List<TaggedValue> taggedValues = new ArrayList<TaggedValue>(
							annotations(element, config));
					taggedValues.addAll(plugin.tagsFromTopLevelElement(
							element.getQName(), config));

					convertType(elementType, schema, config, new Visitor() {

						@Override
						public void visit(final Association association) {
							throw new AssertionError();
						}

						@Override
						public void visit(final AssociationEnd associationEnd) {
							throw new AssertionError();
						}

						@Override
						public void visit(final Attribute attribute) {
							throw new AssertionError();
						}

						@Override
						public void visit(final ClassType classType) {
							ownedElements.add(classType);
						}

						@Override
						public void visit(final DataType dataType) {
							ownedElements.add(dataType);
						}

						@Override
						public void visit(final EnumLiteral enumLiteral) {
							throw new AssertionError();
						}

						@Override
						public void visit(final EnumType enumType) {
							ownedElements.add(enumType);
						}

						@Override
						public void visit(final Generalization generalization) {
							ownedElements.add(generalization);
						}

						@Override
						public void visit(final Model model) {
							throw new AssertionError();
						}

						@Override
						public void visit(final Multiplicity multiplicity) {
							throw new AssertionError();
						}

						@Override
						public void visit(final Range range) {
							throw new AssertionError();
						}

						@Override
						public void visit(final TagDefinition tagDefinition) {
							throw new AssertionError();
						}

						@Override
						public void visit(final TaggedValue taggedValue) {
							throw new AssertionError();
						}

						@Override
						public void visit(final UmlPackage pkg) {
							throw new AssertionError();
						}
					}, element.getQName(), taggedValues);
				}
			} else if (schemaObject instanceof XmlSchemaInclude) {
				final XmlSchemaInclude includedSchema = (XmlSchemaInclude) schemaObject;
				final XmlSchema embeddedSchema = includedSchema.getSchema();

				// While we load the embedded schema the context for looking up
				// identifiers is
				// shared so that names resolve to the same objects.
				final Model embeddedModel = parseXmlSchema("", level + 1,
						embeddedSchema, config, plugin);
				ownedElements.addAll(embeddedModel.getOwnedElements());
			} else {
				throw new AssertionError(schemaObject);
			}
		}
		return new Model(Identifier.random(), name, EMPTY_TAGGED_VALUES,
				ownedElements);
	}

	private static final TaggedValue pattern(
			final XmlSchemaPatternFacet patternFacet, final Xsd2UmlConfig ctxt) {
		final String value = patternFacet.getValue().toString();
		final List<TaggedValue> annotations = annotations(patternFacet, ctxt);
		final Identifier tagDefinition = ctxt.ensureTagDefinitionId("pattern");
		return new TaggedValue(Identifier.random(), annotations, value,
				tagDefinition);
	}

	private static final Range range(final Occurs lowerBound,
			final Occurs upperBound) {
		return new Range(Identifier.random(), lowerBound, upperBound,
				EMPTY_TAGGED_VALUES);
	}

	private static final String stringValue(final NodeList markup) {
		final StringBuilder sb = new StringBuilder();
		final int length = markup.getLength();
		for (int i = 0; i < length; i++) {
			final Node node = markup.item(i);
			sb.append(node.getTextContent());
		}
		return sb.toString();
	}
}
