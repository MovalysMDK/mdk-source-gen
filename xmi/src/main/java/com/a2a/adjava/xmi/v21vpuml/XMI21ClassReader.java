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
package com.a2a.adjava.xmi.v21vpuml;

import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.NAME_ATTR;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.OWNEDATTRIBUTE_ASSOCIATION_ATTR;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.XMIEXTENSION_NODE;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.XMIID2011_ATTR;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.XMIVALUE2011_ATTR;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlPackage;
import com.a2a.adjava.uml.UmlStereotype;
import com.a2a.adjava.xmi.v12vpuml.XMI12StereoTypeReader;

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
 * @author lmichenaud
 * 
 */
public final class XMI21ClassReader {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(XMI21ClassReader.class);

	/**
	 * Singleton
	 */
	private static XMI21ClassReader classReader = new XMI21ClassReader();

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	protected static XMI21ClassReader getInstance() {
		return classReader;
	}

	/**
	 * Constructor
	 */
	private XMI21ClassReader() {
		// private constructor because singleton pattern
	}
	
	/**
	 * Read class
	 * @param p_xClass class node
	 * @param p_oPackage package of class
	 * @param p_oModelDictionary model dictionary
	 * @param p_oClass class to use for Uml class
	 * @param <C> subclass of UmlClass
	 * @return uml class
	 * @throws Exception exception
	 */
	protected <C extends UmlClass> C readClass(Element p_xClass, UmlPackage p_oPackage, UmlDictionary p_oModelDictionary, Class<C> p_oClass)
			throws Exception {

		// XMI example:
		// <ownedMember isAbstract="false" isActive="false" isLeaf="false" name="MainScreen" visibility="public" xmi:id="HeygoAqAUDAUUQ6d" xmi:type="uml:Class">
		//	<xmi:Extension extender="Visual Paradigm">
		//		<isRoot xmi:value="false"/>
		//		<modelType value="Class"/>
		//		<businessModel xmi:value="false"/>
		//		<qualityScore value="-1"/>
		//		<appliedStereotype xmi:value="Class_Mm_screenroot_id"/>
		//	</xmi:Extension>
		// </ownedMember>

		// Add Class
		String sId = p_xClass.attributeValue(XMIID2011_ATTR);
		String sName = p_xClass.attributeValue(NAME_ATTR);
		C r_oClass = p_oClass.newInstance();
		r_oClass.init(sName, p_oPackage);
		p_oModelDictionary.registerClass(sId, r_oClass);

		// Read Extension - Stereotype
		Element xEltExtension = p_xClass.element(XMIEXTENSION_NODE);
		if (xEltExtension != null) {
			for (Element xStereoType : (List<Element>) xEltExtension.elements("appliedStereotype")) { 
				String sIdRef = xStereoType.attributeValue(XMIVALUE2011_ATTR);
				UmlStereotype oUmlStereoType = p_oModelDictionary.getStereotypeById(sIdRef);
				if (oUmlStereoType == null) {
					throw new AdjavaException("Impossible de trouver le stereotype d'id {}", sIdRef);
				}
				r_oClass.addStereotype(oUmlStereoType);
			}
		}
		log.debug("class: {}, id: {}, Stereotypes: {}", new Object[] {sName, sId, r_oClass.getStereotypeNames().toString()});

		p_oModelDictionary.registerClass(sId, r_oClass);
		this.readAttributes(p_xClass, r_oClass, p_oModelDictionary);
		this.readOperations(p_xClass, r_oClass, p_oModelDictionary);
		
		// Read documentation
		r_oClass.setDocumentation(XMI21DocReader.getInstance().readDoc(p_xClass, StringUtils.join("Class:", sName)));

		return r_oClass;
	}
	
	/**
	 * Read attributes
	 * @param p_xClass class node
	 * @param p_oUmlClass uml class
	 * @param p_oModelDictonnary model dictionary
	 * @throws Exception exception
	 */
	@SuppressWarnings("unchecked")
	private void readAttributes( Element p_xClass, UmlClass p_oUmlClass, UmlDictionary p_oModelDictonnary ) throws Exception {
		
		for (Element xOwnedAttr : (List<Element>) p_xClass.elements(XMI21Constants.OWNEDATTRIBUTE_NODE)) {
			if ( xOwnedAttr.attributeValue(OWNEDATTRIBUTE_ASSOCIATION_ATTR) == null) {
				p_oUmlClass.addAttribute(XMI21AttributeReader.getInstance().readAttribute(xOwnedAttr, p_oUmlClass, p_oModelDictonnary));
			}
		}
	}
	
	/**
	 * Read operations of class
	 * @param p_xClass class node
	 * @param p_oUmlClass uml class
	 * @param p_oModelDictonnary model dictionary
	 * @throws Exception exception
	 */
	@SuppressWarnings("unchecked")
	private void readOperations( Element p_xClass, UmlClass p_oUmlClass, UmlDictionary p_oModelDictonnary ) throws Exception {
		
		for (Element xOperation : (List<Element>) p_xClass.elements(XMI21Constants.OWNEDOPERATION_NODE)) {
			p_oUmlClass.addOperation(XMI21OperationReader.getInstance().readOperation(xOperation, p_oUmlClass,
					p_oModelDictonnary));
		}
	}
}
