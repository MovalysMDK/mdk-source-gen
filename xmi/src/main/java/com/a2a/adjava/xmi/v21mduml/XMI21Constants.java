/**
 * Copyright (C) 2010 Sopra Steria Group (movalys.support@soprasteria.com)
 *
 * This file is part of Movalys MDK.
 * Movalys MDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Movalys MDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with Movalys MDK. If not, see <http://www.gnu.org/licenses/>.
 */
package com.a2a.adjava.xmi.v21mduml;

import org.dom4j.Namespace;
import org.dom4j.QName;

/**
 * Constants class
 * @author lmichenaud
 *
 */
public final class XMI21Constants {

	/**
	 * Constructor
	 */
	private XMI21Constants() {
		// Utility class
	}
	
	/**
	 * Xmi namespace
	 */
	public static final Namespace XMI_NAMESPACE = new Namespace("xmi", "http://www.omg.org/XMI");
	
	/**
	 * Xsi namespace
	 */
	public static final Namespace XSI_NAMESPACE = new Namespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
	
	/**
	 * Xmi namespace 20110701
	 */
	public static final Namespace XMI20110701_NAMESPACE = new Namespace("xmi", "http://www.omg.org/spec/XMI/20110701");
	
	/**
	 * Name attribute
	 */
	public static final QName NAME_ATTR = new QName("name", Namespace.NO_NAMESPACE);
	
	/**
	 * id attribute
	 */
	public static final String ID_ATTR = "id";
	
	/**
	 * Xmi id attribute
	 */
	public static final QName XMIID_ATTR = new QName("id", XMI_NAMESPACE);
	
	/**
	 * Xsi id attribute
	 */
	public static final QName XSIID_ATTR = new QName("id", XSI_NAMESPACE);
		
	/**
	 * Xmi id attribute
	 */
	public static final QName XMIIDREF2011_ATTR = new QName("idref", XMI20110701_NAMESPACE);	
	
	/**
	 * Xmi id attribute
	 */
	public static final QName XMIID2011_ATTR = new QName("id", XMI20110701_NAMESPACE);

	/**
	 * Xmi Type attribute
	 */
	public static final QName XMITYPE_ATTR = new QName("type", XMI20110701_NAMESPACE);
	
	/**
	 * Type attribute
	 */
	public static final QName TYPE_ATTR = new QName("type", Namespace.NO_NAMESPACE);
	
	/**
	 * Type attribute
	 */
	public static final QName TYPE_NODE = new QName("type", Namespace.NO_NAMESPACE);
	
	/**
	 * Value attribute
	 */
	public static final String VALUE_ATTR = "value";
	
	/**
	 * Package element node
	 */
	public static final QName PACKAGEELEMENT_NODE = new QName("packagedElement", Namespace.NO_NAMESPACE);
	
	/**
	 * Package type
	 */
	public static final String PACKAGEELEMENT_TYPE_PACKAGE = "uml:Package";
	
	/**
	 * Class type
	 */
	public static final String PACKAGEELEMENT_TYPE_CLASS = "uml:Class";
	
	/**
	 * Association Class type
	 */
	public static final String PACKAGEELEMENT_TYPE_ASSOCIATIONCLASS = "uml:AssociationClass";	
	
	/**
	 * Enumeration type
	 */
	public static final String PACKAGEELEMENT_TYPE_ENUMERATION = "uml:Enumeration";
	
	/**
	 * Association type
	 */
	public static final String PACKAGEELEMENT_TYPE_ASSOCIATION = "uml:Association";

	/**
	 * Association type
	 */
	public static final String PACKAGEELEMENT_TYPE_USAGE = "uml:Usage";
	
	
	/**
	 * Owned literal (for enumeration)
	 */
	public static final String OWNEDLITERAL_NODE = "ownedLiteral";
	
	/**
	 * Enumeration literal
	 */
	public static final String OWNEDLITERAL_TYPE_ENUMERATIONLITERAL = "uml:EnumerationLiteral";
	
	/**
	 * Base element
	 */
	public static final String BASE_ELEMENT_ATTR = "base_Element";
	
	/**
	 * Base enumeration
	 */
	public static final String BASE_ENUMERATION_ATTR = "base_Enumeration";
	
	/**
	 * Owned attribute
	 */
	public static final QName OWNEDATTRIBUTE_NODE = new QName("ownedAttribute", Namespace.NO_NAMESPACE );
	
	/**
	 * Owned operation
	 */
	public static final String OWNEDOPERATION_NODE = "ownedOperation";
	
	/**
	 * Association attribute of node ownedAttribute
	 */
	public static final String OWNEDATTRIBUTE_ASSOCIATION_ATTR = "association";
	
	/**
	 * memberEnd node
	 */
	public static final QName MEMBEREND_NODE = new QName("memberEnd", Namespace.NO_NAMESPACE );
	
	/**
	 * Visibility attribute of node ownedAttribute
	 */
	public static final String OWNEDATTRIBUTE_VISIBILITY_ATTR = "visibility";
	
	/**
	 * isDerived attribute of node ownedAttribute
	 */
	public static final String OWNEDATTRIBUTE_DERIVED_ATTR = "isDerived";
	
	/**
	 * isID attribute of node ownedAttribute
	 */
	public static final String OWNEDATTRIBUTE_ISID_ATTR = "isID";
	
	/**
	 * aggregation attribute of node ownedAttribute
	 */
	public static final String OWNEDATTRIBUTE_AGGREGATION_ATTR = "aggregation";
	
	/**
	 * lowerValue node of node ownedAttribute
	 */
	public static final QName LOWERVALUE_NODE = new QName("lowerValue", Namespace.NO_NAMESPACE);
	
	/**
	 * upperValue node of node ownedAttribute
	 */
	public static final QName UPPERVALUE_NODE = new QName("upperValue", Namespace.NO_NAMESPACE);
	
	/**
	 * upperValue node of node ownedAttribute
	 */
	public static final String LITERAL_UNLIMITEDNATURAL_VALUE = "uml:LiteralUnlimitedNatural" ;
	
	/**
	 * defaultValue node (for ownedAttribute node)
	 */
	public static final String DEFAULTVALUE_NODE = "defaultValue";
		
	/**
	 * stereotypeHREF attribute of node stereotype
	 */
	public static final String STEREOTYPE_STEREOTYPEHREF_ATTR = "stereotypeHREF";
	
	/**
	 * modelExtension node
	 */
	public static final QName MODELEXTENSION_NODE = new QName("modelExtension", Namespace.NO_NAMESPACE);
	
	/**
	 * Extension node
	 */
	public static final QName XMIEXTENSION_NODE = new QName("Extension", XMI20110701_NAMESPACE);
	
	/**
	 * ownedEnd node
	 */
	public static final QName OWNEDEND_NODE = new QName("ownedEnd", Namespace.NO_NAMESPACE);
	
	/**
	 * client node
	 */
	public static final QName CLIENT_NODE = new QName("client", Namespace.NO_NAMESPACE);
	
	/**
	 * supplier node
	 */
	public static final QName SUPPLIER_NODE = new QName("supplier", Namespace.NO_NAMESPACE);

	/**
	 * ownedComment node
	 */
	public static final QName OWNEDCOMMENT_NODE = new QName("ownedComment", Namespace.NO_NAMESPACE);
	
	/**
	 * ownedComment node
	 */
	public static final QName OWNEDCOMMENT_BODY_ATTR = new QName("body", Namespace.NO_NAMESPACE);	
	
	/**
	 * Documentation node
	 */
	public static final QName DOCUMENTATION_NODE = new QName("Documentation", XMI20110701_NAMESPACE);
	
	/**
	 * exporter node
	 */
	public static final QName EXPORTER_NODE = new QName("exporter", XMI20110701_NAMESPACE);
	
	/**
	 * exporterVersion node
	 */
	public static final QName EXPORTERVERSION_NODE = new QName("exporterVersion", XMI20110701_NAMESPACE);
}
