//
// Copyright (c)1998-2011 Pearson Education, Inc. or its affiliate(s). 
// All rights reserved.
//

package openadk.library.common;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import openadk.library.ADK;
import openadk.library.Element;
import openadk.library.ElementDef;
import openadk.library.SIFKeyedElement;
import openadk.library.SIFSimpleType;
import openadk.library.SIFString;
import openadk.library.SIFWriter;
import openadk.library.SimpleField;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;


/**
 *  <p>
 *
 *  @author Generated by adkgen
 *  @version 2.4
 *  @since 1.5r1
 */
public class SIF_ExtendedElement extends SIFKeyedElement {
	private static final long serialVersionUID = Element.CURRENT_SERIALIZE_VERSION;
	/**
	 *  Constructor
	 */
	public SIF_ExtendedElement() {
		super( CommonDTD.SIF_EXTENDEDELEMENT );
	}

	/**
	 *  Constructor that accepts values for all mandatory fields
	 *  @param value Gets or sets the content value of the &lt;SIF_ExtendedElement&gt; element
	 */
	public SIF_ExtendedElement( String value ) {
		super( CommonDTD.SIF_EXTENDEDELEMENT );
		this.setValue(value);
	}
	
	public SIF_ExtendedElement( String name, String value ){
		super( CommonDTD.SIF_EXTENDEDELEMENT );
		this.setValue( value );
		this.setName( name );
	}

	/**
	 *  Gets the key of this object
	 *  @return The value of the object's Mandatory or Required attribute. If
	 *      an object has more than one such attribute, the key is a period-
	 *      delimited concatenation of the attribute values in sequential order
	 */
	public String getKey() {
		return getAttributeAsString(CommonDTD.SIF_EXTENDEDELEMENT_NAME);
	}

	/**
	 *  Gets the metadata fields that make up the key of this object
	 *  @return an array of metadata fields that make up the object's key
	 */
	public ElementDef[] getKeyFields() {
		return new ElementDef[] { CommonDTD.SIF_EXTENDEDELEMENT_NAME };
	}
	

	/**
	 *  Gets the value of the <code>&lt;Value&gt;</code> element.
	* <p> The SIF specification defines the meaning of this element as: 
	* <i>"Gets or sets the content value of the &lt;SIF_ExtendedElement&gt; element"</i><p>
	 *
	 *  @return The <code>Value</code> element of this object.
	 *  @since 1.5r1
	 */
	public String getValue() { 
		return getAttributeAsString(CommonDTD.SIF_EXTENDEDELEMENT);
	}

	/**
	 *  Sets the value of the <code>&lt;Value&gt;</code> element.
	* <p> The SIF specification defines the meaning of this element as: 
	* <i>"Gets or sets the content value of the &lt;SIF_ExtendedElement&gt; element"</i><p>
	 *
	 *  @param value A <code>String</code> object
	 *  @since 1.5r1
	 */
	public void setValue( String value ) { 
		setAttributeWithString(CommonDTD.SIF_EXTENDEDELEMENT, value);
	}

	/**
	 *  Gets the value of the <code>Name</code> attribute.
	* <p> The SIF specification defines the meaning of this attribute as: 
	* <i>"The name of the extended element. As it is possible that names for extended elements may collide from agent to agent, it is recommended that the names of extended elements be configurable in an agent, or that agents use URIs for the names of extended elements."</i><p>
	 *
	 *  @return The <code>Name</code> attribute of this object.
	 *  @since 1.5r1
	 */
	public String getName() { 
		return getAttributeAsString(CommonDTD.SIF_EXTENDEDELEMENT_NAME);
	}

	/**
	 *  Sets the value of the <code>Name</code> attribute.
	* <p> The SIF specification defines the meaning of this attribute as: 
	* <i>"The name of the extended element. As it is possible that names for extended elements may collide from agent to agent, it is recommended that the names of extended elements be configurable in an agent, or that agents use URIs for the names of extended elements."</i><p>
	 *
	 *  @param value A <code>String</code> object
	 *  @since 1.5r1
	 */
	public void setName( String value ) { 
		setAttributeWithString(CommonDTD.SIF_EXTENDEDELEMENT_NAME, value);
	}

	/**
	 *  Gets the value of the <code>XsiType</code> attribute.
	* <p> The SIF specification defines the meaning of this attribute as: 
	* <i>"Allows type of element to be explicitly communicated."</i><p>
	 *
	 *  @return The <code>XsiType</code> attribute of this object.
	 *  @since 1.5r1
	 */
	public String getXsiType() { 
		return getAttributeAsString(CommonDTD.SIF_EXTENDEDELEMENT_XSITYPE);
	}

	/**
	 *  Sets the value of the <code>XsiType</code> attribute.
	* <p> The SIF specification defines the meaning of this attribute as: 
	* <i>"Allows type of element to be explicitly communicated."</i><p>
	 *
	 *  @param value A <code>String</code> object
	 *  @since 1.5r1
	 */
	public void setXsiType( String value ) { 
		setAttributeWithString(CommonDTD.SIF_EXTENDEDELEMENT_XSITYPE, value);
	}

	/**
	 *  Gets the value of the <code>SIF_Action</code> attribute.
	* <p> The SIF specification defines the meaning of this attribute as: 
	* <i>"In a Change event, this flag can be used to indicate an element has been deleted from the parent list container. At a minimum the key for the list must also be present."</i><p>
	 *
	 *  @return The <code>SIF_Action</code> attribute of this object.
	 *  @since 1.5r1
	 */
	public String getSIF_Action() { 
		return getAttributeAsString(CommonDTD.SIF_EXTENDEDELEMENT_SIF_ACTION);
	}

	/**
	 *  Sets the value of the <code>SIF_Action</code> attribute.
	* <p> The SIF specification defines the meaning of this attribute as: 
	* <i>"In a Change event, this flag can be used to indicate an element has been deleted from the parent list container. At a minimum the key for the list must also be present."</i><p>
	 *
	 *  @param value A constant defined by the <code>SIFActionType</code> class
	 *  @since 1.5r1
	 */
	public void setSIF_Action( SIFActionType value ) { 
		setSIF_Action(value.toString());
	}

	/**
	 *  Sets the value of the <code>SIF_Action</code> attribute as a String.
	* <p> The SIF specification defines the meaning of this attribute as: 
	* <i>"In a Change event, this flag can be used to indicate an element has been deleted from the parent list container. At a minimum the key for the list must also be present."</i><p>
	 *
	 *  @param value The value as a String
	 *  @since 1.5r1
	 */
	public void setSIF_Action( String value ) {
		setAttributeWithString(CommonDTD.SIF_EXTENDEDELEMENT_SIF_ACTION, value);
	}
	
	@Override
	protected Object getSIFSimpleFieldValue(ElementDef id) {
		return getAttributeAsString(id);
	}

	@Override
	public SIFSimpleType<String> getSIFValue() {
		return new SIFString(getValue());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setSIFValue(SIFSimpleType value) {
		setValue(value == null ? null : value.toString());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public SimpleField getField(ElementDef id) {
		return new SimpleField<String>(this, id, new SIFString(getAttributeAsString(id)));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public SimpleField getField(String name) {
		return getField(ADK.DTD().lookupElementDef(fElementDef, name));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String getFieldValue(ElementDef id) {
		SimpleField sf = getField(id);
		return sf == null ? null : sf.getTextValue();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public SimpleField setField(ElementDef id, int value) {
		return setField(id, String.valueOf(value));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public SimpleField setField(ElementDef id, SIFSimpleType value) {
		return setField(id, value.toString());
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected SimpleField setFieldValue(ElementDef id,
			SIFSimpleType wrappedValue, Object unwrappedValue) {
		// for now, we want the super class's behavior, but will leave the override as a marker for things we must maintain
		return super.setFieldValue(id, wrappedValue, unwrappedValue);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public SimpleField setField(ElementDef id, String value) {
		setAttributeWithString(id, value);
		return getField(id);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setField(SimpleField field) {
		setAttributeWithString(field.getElementDef(), field.getTextValue());
	}

	@Override
	protected void removeField(ElementDef id) {
		setAttributeWithString(id, null);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SimpleField> getFields() {
		NamedNodeMap namedNodeMap = getXML().getAttributes();
		List<SimpleField> fields = new ArrayList<SimpleField>(namedNodeMap.getLength() + 1);
		fields.add(getField(fElementDef));
		for (int nodeMapIndex = 0; nodeMapIndex < namedNodeMap.getLength(); nodeMapIndex++) {
			Node attrNode = namedNodeMap.item(nodeMapIndex);
			String attrName = attrNode.getNodeName();
			ElementDef attrDef;
			if (SIFWriter.XSI_NAMESPACE.equals(attrNode.getNamespaceURI())) {
				attrDef = ADK.DTD().lookupElementDef(fElementDef, "xsi:" + attrName);
			} else {
				attrDef = ADK.DTD().lookupElementDef(fElementDef, attrName);
			}
			if (attrDef != null) {
				fields.add(getField(attrDef));
			}
		}
		return fields;
	}

	@Override
	public int getFieldCount() {
		return getFields().size();
	}

	/**
	 * The XML DOM Document that is a child of this element
	 */
	private org.w3c.dom.Element fXmlData;
	
	/**
	 * Returns the XML DOM Document that is a child of this element
	 * @return The DOM Document contained as a child of the <code>&lt;XMLData&gt;</code> or null
	 */
	public org.w3c.dom.Element getXML()
	{	
		if ( fXmlData == null )
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder;
			try {
				builder = factory.newDocumentBuilder();
				Document document = builder.newDocument();
				fXmlData = document.createElement("SIF_ExtendedElement");
				document.appendChild(fXmlData);
			} catch (ParserConfigurationException e) {
				ADK.getLog().debug("Document builder failed to return a document builder in SIF_ExtendedElement.getXML(): " + e.getMessage());
				fXmlData = null;
			}
			
		}

		return fXmlData;
	}
	
	/**
	 * Sets an XML DOM Document as the child of this element
	 * @param doc
	 */
	public void setXML( org.w3c.dom.Element doc )
	{
		fXmlData = doc;
	}
	
	private String getAttributeAsString(Object id) {
		String attrName = null;
		String attrValue = null;
		if (id instanceof ElementDef) {
			ElementDef def = (ElementDef)id;
			attrName = def.tag(ADK.getSIFVersion());
		} else {
			attrName = id.toString();
		}
		if ("SIF_ExtendedElement".equals(attrName)) {
			attrValue = "";
			if (!getXML().hasChildNodes()) {
				// Preempting changes to namespace awareness for now.
				// This may be simplified when namespace handling is solidified 
				if ((getXML().hasAttribute("xsi:nil") && "true".equals(getXML().getAttribute("xsi:nil"))) ||
					(getXML().hasAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "nil") && "true".equals(getXML().getAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "nil"))) ||
					(getXML().hasAttribute("SIF_Action") && "Delete".equalsIgnoreCase(getXML().getAttribute("SIF_Action")))
					) {
					attrValue = null;
				}
			} else {
				String txtCnt = getXML().getTextContent();
				attrValue = txtCnt == null ? "" : txtCnt;
			}
		} else {
			if (attrName != null && attrName.startsWith("xsi:")) {
				attrValue = getXML().getAttributeNS(SIFWriter.XSI_NAMESPACE, attrName.substring(4));
			} else {
				attrValue = getXML().getAttribute(attrName);
			}
		}
		return attrValue;
	}

	private void setAttributeWithString(final Object id, final String value) {
		String attrName = null;
		if (id instanceof ElementDef) {
			attrName = ((ElementDef)id).tag(ADK.getSIFVersion());
		} else {
			attrName = id.toString();
		}
		if ("SIF_ExtendedElement".equals(attrName)) {
			getXML().setTextContent(value == null ? "" : value);
			if (value == null) {
				setAttributeWithString("xsi:nil", "true");
			} else {
				setAttributeWithString("xsi:nil", null);
			}
		} else {
			if (value == null) {
				getXML().removeAttribute(attrName);
			} else {
				if (attrName != null && attrName.startsWith("xsi:")) {
					getXML().setAttributeNS(SIFWriter.XSI_NAMESPACE, attrName, value);
				} else {
					getXML().setAttribute(attrName, value);
				}
			}
		}
	}
}
