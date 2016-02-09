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

import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.NAME_ATTR;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.OWNEDATTRIBUTE_ASSOCIATION_ATTR;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.XMIID2011_ATTR;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlPackage;

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

		String sId = p_xClass.attributeValue(XMIID2011_ATTR);
		String sName = p_xClass.attributeValue(NAME_ATTR);
		C r_oClass = p_oClass.newInstance();
		r_oClass.init(sName, p_oPackage);
		p_oModelDictionary.registerClass(sId, r_oClass);
		log.debug("class: {}, id: {}", sName, sId);

		p_oModelDictionary.registerClass(sId, r_oClass);
		this.readAttributes(p_xClass, r_oClass, p_oModelDictionary);
		this.readOperations(p_xClass, r_oClass, p_oModelDictionary);
		this.readAssociationEnds(p_xClass, r_oClass, p_oModelDictionary);
		
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
	
	/**
	 * Read association ends of class
	 * @param p_xClass class node
	 * @param p_oUmlClass Uml class
	 * @param p_oModelDictonnary model dictionary
	 * @throws Exception exception
	 */
	@SuppressWarnings("unchecked")
	private void readAssociationEnds( Element p_xClass, UmlClass p_oUmlClass, UmlDictionary p_oModelDictonnary ) throws Exception {
		for (Element xOwnedAttr : (List<Element>) p_xClass.elements(XMI21Constants.OWNEDATTRIBUTE_NODE)) {
			if ( xOwnedAttr.attributeValue(OWNEDATTRIBUTE_ASSOCIATION_ATTR) != null) {
				p_oUmlClass.addAssociation(XMI21AssociationReader.getInstance().readAssociationEnd(xOwnedAttr, p_oModelDictonnary));
			}
		}
	}
}
