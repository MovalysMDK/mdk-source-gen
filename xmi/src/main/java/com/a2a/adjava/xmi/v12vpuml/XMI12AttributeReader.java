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
package com.a2a.adjava.xmi.v12vpuml;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dom4j.Element;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.uml.UmlAttribute;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDataType;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlStereotype;

/**
 * 
 * <p>
 * TODO Decrire la classe XMI12AttributeReader
 * </p>
 * 
 * <p>
 * Copyright (c) 2009
 * <p>
 * Company: Adeuza
 * 
 * @author mmadigand
 * @author lmichenaud
 * 
 */
public class XMI12AttributeReader {
	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(XMI12AttributeReader.class);

	private static XMI12AttributeReader attributeReader = null;

	/**
	 * @return
	 */
	protected static XMI12AttributeReader getInstance() {
		if (attributeReader == null) {
			attributeReader = new XMI12AttributeReader();
		}
		return attributeReader;
	}

	/**
	 * @param p_xAttr
	 * @param p_oClass
	 * @param modelDictonnary
	 * @return
	 * @throws Exception
	 */
	protected UmlAttribute readAttribute(Element p_xAttr, UmlClass p_oClass, UmlDictionary p_oModelDictonnary)
			throws Exception {
		log.debug("> XMIReader.readAttribute");

		UmlAttribute r_oAttr = null;

		String sAttrName = p_xAttr.attributeValue("name").trim();
		log.debug("name = " + sAttrName);

		if (!sAttrName.isEmpty()) {

			String sVisibility = p_xAttr.attributeValue("visibility");
			String sTypeRef = p_xAttr.attributeValue("type");
			if (sTypeRef == null) {
				Element xType = p_xAttr.element("StructuralFeature.type");
				if (xType != null) {
					if (xType.element("DataType") != null) {
						sTypeRef = xType.element("DataType").attributeValue("xmi.idref");
					} else if (xType.element("Class") != null) {
						sTypeRef = xType.element("Class").attributeValue("xmi.idref");
					} else if (xType.element("Classifier") != null) {
						sTypeRef = xType.element("Classifier").attributeValue("xmi.idref");
					}
				}
			}

			UmlDataType oUmlDataType = null ;
			if (sTypeRef != null) {
				oUmlDataType = p_oModelDictonnary.getDataType(sTypeRef);
			}
			
			// Lecture des options, valeur par default, etc...
			String sInitialValue = "";
			Element xInitialValue = p_xAttr.element("Attribute.initialValue");
			if (xInitialValue != null) {
				Element xExpressionValue = xInitialValue.element("Expression");
				if (xExpressionValue != null) {
					sInitialValue = xExpressionValue.attributeValue("body");
				}
			}

			String sMsgIdentifiant = "attribute " + sAttrName + " of class " + p_oClass.getName();
			String sDocumentation = XMI12DocReader.getInstance().readDoc(p_xAttr, sMsgIdentifiant);

			if (log.isDebugEnabled()) {
				log.debug("  name : " + sAttrName);
					log.debug("  visibility : " + sVisibility);
				log.debug("  initial value : " + sInitialValue);
				log.debug("  type ref : " + sTypeRef);
				log.debug("  sDocumentation: " + sDocumentation);
			}

			r_oAttr = new UmlAttribute(sAttrName, p_oClass, sVisibility, oUmlDataType, sInitialValue, sDocumentation);
				
			//lecture des complements derived ... dans le tag XMI.extension
			Element xExtension = p_xAttr.element("XMI.extension");
			if (xExtension!=null) {
				Element xElem = xExtension.element("isDerived");
				if (xElem!=null && "true".equals(xElem.attributeValue("xmi.value"))) {
					if (log.isDebugEnabled()) {
						log.debug("  derived : true");
					}
					r_oAttr.setDerived(true);
				}
				xElem = xExtension.element("javaTransient");
				if (xElem!=null && "true".equals(xElem.attributeValue("xmi.value"))) {
					if (log.isDebugEnabled()) {
						log.debug("  transient : true");
					}
					r_oAttr.setTransient(true);
				}
			}
			
			// Read stereotypes
			Element xStereoTypeValues = p_xAttr.element("ModelElement.stereotype");
			if (xStereoTypeValues != null) {
				for (Element xStereoType : (List<Element>) xStereoTypeValues.elements("Stereotype")) {
					String sIdRef = xStereoType.attributeValue("xmi.idref");
					UmlStereotype oUmlStereoType = p_oModelDictonnary.getStereotypeById(sIdRef);
					if (oUmlStereoType == null) {
						throw new AdjavaException("Impossible de trouver le stereotype d'id {}", sIdRef);
					}
					r_oAttr.addStereotype(oUmlStereoType);
				}
			}
		} else {
			MessageHandler.getInstance().addError("Attribute name is empty in class : {}", p_oClass.getName());
		}

		log.debug("< XMIReader.readAttribute");

		return r_oAttr;
	}
}
