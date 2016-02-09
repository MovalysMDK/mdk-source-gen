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

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.uml.UmlAssociationClass;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlPackage;
import com.a2a.adjava.uml.UmlStereotype;

/**
 * 
 * <p>
 * Read UmlClass in Xmi
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
public final class XMI12ClassReader {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(XMI12AssociationReader.class);

	/**
	 * Singleton
	 */
	private static XMI12ClassReader classReader = null;

	/**
	 * @return
	 */
	protected static XMI12ClassReader getInstance() {
		if (classReader == null) {
			classReader = new XMI12ClassReader();
		}
		return classReader;
	}

	/**
	 * 
	 */
	private XMI12ClassReader() {

	}

	/**
	 * @param p_xClasse
	 * @param p_oPackage
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected UmlClass readClass(Element p_xClass, UmlPackage p_oPackage, UmlDictionary p_oModelDictonnary)
			throws Exception {

		log.debug("> XML12ClassReader.readClass");

		String sId = p_xClass.attributeValue("xmi.id");
		String sName = p_xClass.attributeValue("name");

		UmlClass r_oClass = null;

		if (!XMI12Helper.isPartOfAssociationClass(p_xClass, sId)) {
			r_oClass = new UmlClass();
		} else {
			r_oClass = new UmlAssociationClass();
		}
		r_oClass.init(sName, p_oPackage);

		p_oModelDictonnary.registerClass(sId, r_oClass);
		log.debug(" nom : " + r_oClass.getFullName());

		// Lecture des attributs
		Element xClassifierFeature = p_xClass.element("Classifier.feature");
		if (xClassifierFeature != null) {
			for (Element xAttr : (List<Element>) xClassifierFeature.elements("Attribute")) {
				r_oClass.addAttribute(XMI12AttributeReader.getInstance().readAttribute(xAttr, r_oClass,
						p_oModelDictonnary));
			}
			// Lecture des operations
			for (Element xOperation : (List<Element>) xClassifierFeature.elements("Operation")) {
				r_oClass.addOperation(XMI12OperationReader.getInstance().readOperation(xOperation, r_oClass,
						p_oModelDictonnary));
			}
		}

		// Lecture des stereotypes de la classe
		Element xStereoTypeValues = p_xClass.element("ModelElement.stereotype");
		if (xStereoTypeValues != null) {
			for (Element xStereoType : (List<Element>) xStereoTypeValues.elements("Stereotype")) {
				String sIdRef = xStereoType.attributeValue("xmi.idref");
				UmlStereotype oUmlStereoType = p_oModelDictonnary.getStereotypeById(sIdRef);
				if (oUmlStereoType == null) {
					throw new AdjavaException("Impossible de trouver le stereotype d'id {}", sIdRef);
				}
				r_oClass.addStereotype(oUmlStereoType);
			}
		}

		// Lecture de la documentation
		String sMsgIdentifiant = "class " + r_oClass.getName();
		r_oClass.setDocumentation(XMI12DocReader.getInstance().readDoc(p_xClass, sMsgIdentifiant));

		log.debug("< XML12ClassReader.readClass");

		return r_oClass;
	}
}
