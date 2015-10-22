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
package com.a2a.adjava.xmi.v24md182uml;

import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.NAME_ATTR;
import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.PACKAGEELEMENT_NODE;
import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.PACKAGEELEMENT_TYPE_ASSOCIATION;
import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.PACKAGEELEMENT_TYPE_ASSOCIATIONCLASS;
import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.PACKAGEELEMENT_TYPE_CLASS;
import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.PACKAGEELEMENT_TYPE_ENUMERATION;
import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.PACKAGEELEMENT_TYPE_PACKAGE;
import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.PACKAGEELEMENT_TYPE_USAGE;
import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.XMIID2011_ATTR;
import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.XMITYPE_ATTR;

import java.util.List;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.uml.UmlAssociationClass;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlEnum;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.uml.UmlPackage;

/**
 * Package reader
 * 
 * @author lmichenaud
 * 
 */
public final class XMI24PackageReader {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(XMI24PackageReader.class);

	/**
	 * Singleton instance
	 */
	private static XMI24PackageReader packageReader = new XMI24PackageReader();

	/**
	 * Get singleton instance
	 * 
	 * @return singleton instance
	 */
	protected static XMI24PackageReader getInstance() {
		return packageReader;
	}

	/**
	 * Constructor
	 */
	private XMI24PackageReader() {
		// private because singleton pattern
	}

	/**
	 * Read package
	 * @param p_xPackage package node
	 * @param p_oParent parent package
	 * @param p_oModelDictonary model dictionary
	 * @param p_oUmlModel uml model
	 * @param p_listAssociationToRead associations to read
	 * @param p_listUsageToRead usages to read
	 * @return Uml package
	 * @throws Exception exception
	 */
	protected UmlPackage readPackage(Element p_xPackage, UmlPackage p_oParent,
			UmlDictionary p_oModelDictonary, UmlModel p_oUmlModel, 
			List<Element> p_listAssociationToRead, List<Element> p_listUsageToRead, List<Element> p_listCommentToRead) throws Exception {

		// Xmi example
		// <packagedElement xmi:type='uml:Package'
		// xmi:id='_17_0_1_2_16a6041b_1355752322315_243294_1860' name='com'>

		String sName = p_xPackage.attributeValue(NAME_ATTR);	
		UmlPackage r_oPackage = new UmlPackage(sName, p_oParent);
		log.debug("package: {}", r_oPackage.getFullName());

		if ( p_oParent != null ) {
			p_oParent.addPackage(r_oPackage);
		}
		else {
			p_oUmlModel.addPackage(r_oPackage);
		}
		
		this.processChildren(p_xPackage, r_oPackage, p_oModelDictonary, p_oUmlModel, p_listAssociationToRead, p_listUsageToRead, p_listCommentToRead );

		return r_oPackage;
	}

	/**
	 * Read children of package
	 * @param p_xParent parent node
	 * @param p_oParent parent package
	 * @param p_oModelDictonary model dictionary
	 * @param p_oUmlModel uml model
	 * @param p_listAssociationToRead associations to end
	 * @param p_listUsageToRead usages to read
	 * @throws Exception exception
	 */
	@SuppressWarnings("unchecked")
	protected void processChildren(Element p_xParent, UmlPackage p_oParent, UmlDictionary p_oModelDictonary,
			UmlModel p_oUmlModel, List<Element> p_listAssociationToRead, List<Element> p_listUsageToRead, List<Element> p_listCommentToRead ) throws Exception {
		
		boolean bRoot = p_oParent == null ;
		
		// Read childpackages, classes, association classes, enumeration and association
		for (Element xPackagedElement : (List<Element>) p_xParent.elements(PACKAGEELEMENT_NODE)) {
			
			String sChildName = xPackagedElement.attributeValue(NAME_ATTR);
			String sXmiType = xPackagedElement.attributeValue(XMITYPE_ATTR);
			
			// Sub package
			if (sXmiType.equals(PACKAGEELEMENT_TYPE_PACKAGE)) {
				// Read comments of package
				for (Element xComment : (List<Element>) xPackagedElement.elements(XMI24Constants.OWNEDCOMMENT_NODE)) {
					p_listCommentToRead.add(xComment);
				}
				
				UmlPackage oPackage = this.readPackage(xPackagedElement, p_oParent, p_oModelDictonary,
							p_oUmlModel, p_listAssociationToRead, p_listUsageToRead, p_listCommentToRead);
					p_oModelDictonary.registerPackage(oPackage);
			}
			
			// Class
			else if (sXmiType.equals(PACKAGEELEMENT_TYPE_CLASS)) {
				
				if ( bRoot ) {
					MessageHandler.getInstance().addError("Class {} must be inside a package.", sChildName);
				}
				
				UmlClass oClass = XMI24ClassReader.getInstance().readClass(xPackagedElement, p_oParent,
						p_oModelDictonary, UmlClass.class);
				p_oParent.addClass(oClass);
			}
			
			// Association Class			
			else if (sXmiType.equals(PACKAGEELEMENT_TYPE_ASSOCIATIONCLASS)) {
				
				if ( bRoot ) {
					MessageHandler.getInstance().addError("Association Class {} must be inside a package.", sChildName);
				}
				
				UmlClass oClass = XMI24ClassReader.getInstance().readClass(xPackagedElement, p_oParent,
						p_oModelDictonary, UmlAssociationClass.class);
				p_oParent.addClass(oClass);
				
				// Will read association ends later
				p_listAssociationToRead.add(xPackagedElement);
			}
			
			// Read enumeration
			else if (sXmiType.equals(PACKAGEELEMENT_TYPE_ENUMERATION)) {

				if ( bRoot ) {
					MessageHandler.getInstance().addError("Enumeration {} must be inside a package.", sChildName);
				}
				else {
					String sXmiId = xPackagedElement.attributeValue(XMIID2011_ATTR);
					UmlEnum oUmlEnum = p_oModelDictonary.getEnumById(sXmiId);
					log.debug("  set package:{} to enum: {}", p_oParent.getFullName(), oUmlEnum.getName());
					oUmlEnum.setUmlPackage(p_oParent);
					p_oParent.addEnumeration(oUmlEnum);
				}
			}
			
			// Read association			
			else if (sXmiType.equals(PACKAGEELEMENT_TYPE_ASSOCIATION)) {
				p_listAssociationToRead.add(xPackagedElement);
			}
			// Read usage			
			else if (sXmiType.equals(PACKAGEELEMENT_TYPE_USAGE)) {
				p_listUsageToRead.add(xPackagedElement);
			}
		}
	}
}
