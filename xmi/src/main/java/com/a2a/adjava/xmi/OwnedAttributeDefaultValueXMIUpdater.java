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
package com.a2a.adjava.xmi;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.utils.Chrono;

/**
 * <p>Replace les valeurs par default des attirbuts dans le document XMI.</p>
 * 
 * <p>Les valeurs par default des variables ne sont pas rensiegnées dans les 
 * document XMI si c'est la valeur par defaut du type de la variable.</p>
 * <p>Ex : pour un entier la valeur "0" ne sera pas renseignée.</p>
 *
 * <p>La classe OwnedAttributeDefaultValueXMIUpdater ajoute un attribue "value" 
 * à la balise defaultValue si : 
 * <ul>
 *  <li>- la balise default value est presente (une valeur par defaut à été rensigner)</li>
 *  <li>- aucun attribut "value" n'est rensigné (prend la valeur par defaut du type)</li>
 * </ul>
 * </p>
 * 
 * <p>Cette classe gere aussi le cas exeptionnel ou le type de la valeur par default est "OpaqueExpression". Dans ce cas l'attribut "value" prend la valeur du texte dans la balise "body"</p>
 *
 * <p>Copyright (c) 2013</p>
 * <p>Company: Adeuza</p>
 * 
 * @author abelliard
 *
 */
public class OwnedAttributeDefaultValueXMIUpdater extends AbstractXMIUpdater {
	/** Xmi namspace */
	private static final Namespace XMI_NAMESPACE = new Namespace("xmi", "http://www.omg.org/spec/XMI/20110701");
	/** Xmi type attribute */
	private static final QName XMI_ATTRIBUTE_TYPE = new QName("type", XMI_NAMESPACE);
	/** Xmi value attribute */
	private static final QName ATTRIBUTE_VALUE = new QName("value", Namespace.NO_NAMESPACE);
	/** XPATH to select defaultValue */
	private static final String XPATH_DEFAULT_VALUE = "//ownedAttribute/defaultValue";
	/** Body */
	private static final String ELEMENT_BODY = "body";
	/** Enum for DefaultValue in XMI file */
	private enum DefaultValueType {
		/** XMI type entier */
		LiteralInteger ("uml:LiteralInteger", "0"),
		/** XMI type chaine */
		LiteralString ("uml:LiteralString", StringUtils.EMPTY),
		/** XMI type opaque */
		OpaqueExpression ("uml:OpaqueExpression", StringUtils.EMPTY),
		/** XMI type réel */
		LiteralReal ("uml:LiteralReal", "0.0"),
		/** XMI type durée */
		Duration ("uml:Duration", StringUtils.EMPTY),
		/** XMI type interval */
		DurationInterval ("uml:DurationInterval", StringUtils.EMPTY),
		/** XMI type expression */
		Expression ("uml:Expression", StringUtils.EMPTY),
		/** XMI type boolean */
		LiteralBoolean ("uml:LiteralBoolean", "false"),
		/** XMI type null */
		LiteralNull ("uml:LiteralNull", "null"),
		/** XMI type naturel */
		LiteralUnlimitedNatural ("uml:LiteralUnlimitedNatural", "0");
		
		/** attribute umlValue */
		private String umlValue;
		/** Xattribute defaultValue */
		private String defaultValue;

		/**
		 * Constructeur de l'enumeration
		 * @param umlValue
		 * @param defaultValue
		 */
		private DefaultValueType(String umlValue, String defaultValue) {
			this.umlValue = umlValue;
			this.defaultValue = defaultValue;
		}

		/**
		 * Return the UmlType in a String
		 * @return String that containt UmlType
		 */
		public String getUmlType() {
			return this.umlValue;
		}

		/**
		 * Return the default value of the type
		 * @param p_eType the type (in enum)
		 * @param p_oElement XML element of default value
		 * @return default value of the type
		 */
		public String getDefaultOrValue(DefaultValueType p_eType, Element p_oElement) {
			if (OpaqueExpression.equals(p_eType)) {
				Element oBodyOfOpaqueExp = p_oElement.element(ELEMENT_BODY);
				return oBodyOfOpaqueExp.getTextTrim();
			} else {
				return this.defaultValue;
			}
		}
		
	}
	
	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(OwnedAttributeDefaultValueXMIUpdater.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Document p_xXmi) throws Exception {

		log.debug(" > Begin OwnedAttributeDefaultValueXMIUpdater");
		
		Chrono oChrono = new Chrono(true);
		
		// selectNodes return a list of Nodes
		@SuppressWarnings("unchecked")
		List<Node> list = p_xXmi.selectNodes(XPATH_DEFAULT_VALUE);
		
		log.debug("parsing time: {}", oChrono.stopAndDisplay());
		
		if (list != null && !list.isEmpty()) {
			for (Node node : list) {
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					
					Element oDefaultValueElement = (Element) node;
					Attribute sAttributeType = oDefaultValueElement.attribute(XMI_ATTRIBUTE_TYPE);
					
					if ( sAttributeType != null ) {
						
						Attribute sAttributeValue = oDefaultValueElement.attribute(ATTRIBUTE_VALUE);						
						log.debug("  element name: {}", oDefaultValueElement.getParent().attribute("name").getValue());
						log.debug("   attribute type: {}", sAttributeType.getValue());
						
						if (sAttributeValue == null) {
							// add attribute value
							for (DefaultValueType eDefaultDefualtValueType : DefaultValueType.values()) {
								
								if (eDefaultDefualtValueType.getUmlType().equals(sAttributeType.getValue())) {
									oDefaultValueElement.addAttribute(
											ATTRIBUTE_VALUE, 
											eDefaultDefualtValueType.getDefaultOrValue(
													eDefaultDefualtValueType, 
													oDefaultValueElement
												)
										);
								}
							}
						}
						
						sAttributeValue = oDefaultValueElement.attribute(ATTRIBUTE_VALUE);
						log.debug("   attribute value: {}", sAttributeValue.getValue());
					}
				}
			}
		}
		
		log.debug(" < End OwnedAttributeDefaultValueXMIUpdater");
		
	}

}
