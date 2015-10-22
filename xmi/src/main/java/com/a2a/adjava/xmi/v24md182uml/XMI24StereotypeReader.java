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

import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.BASE_ELEMENT_ATTR;
import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.BASE_ENUMERATION_ATTR;
import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.NAME_ATTR;
import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.STEREOTYPE_STEREOTYPEHREF_ATTR;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlEnum;
import com.a2a.adjava.uml.UmlStereotype;
import com.a2a.adjava.uml.UmlStereotypedObject;

/**
 * Stereotype reader
 * @author lmichenaud
 *
 */
public final class XMI24StereotypeReader {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(XMI24StereotypeReader.class);
	
	/**
	 * Stereotype reader
	 */
	private static XMI24StereotypeReader stereotypeReader = new XMI24StereotypeReader();
	
	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	protected static XMI24StereotypeReader getInstance() {
		return stereotypeReader ;
	}
	
	/**
	 * Constructor
	 */
	private XMI24StereotypeReader() {
		// private because singleton
	}
		
	/**
	 * Read stereotypes
	 * @param p_xModel model node
	 * @param p_oModelDictonary model dictionary
	 * @throws Exception exception
	 */
	@SuppressWarnings("unchecked")
	protected void readStereotypes( Element p_xModel, UmlDictionary p_oModelDictonary ) throws Exception {
		log.debug("read stereotypes");
				
		// stereotypes from module
		for (Element xStereotype : (List<Element>) p_xModel.getParent().selectNodes(
				"./*[ namespace-uri() = 'http://www.omg.org/spec/XMI/20131001' and local-name() = 'Extension']/stereotypesHREFS/stereotype")) {			
			this.readStereoType( xStereotype, p_oModelDictonary);
		}
	}
	
	/**
	 * Read stereotype
	 * @param p_xStereotype stereotype node
	 * @param p_oModelDictionary model dictionary
	 * @return Uml stereotype
	 * @throws Exception exception
	 */
	protected UmlStereotype readStereoType(Element p_xStereotype,
			UmlDictionary p_oModelDictionary)
			throws Exception {
		
		String sFullName = p_xStereotype.attributeValue(NAME_ATTR);
		String[] t_sParts = sFullName.split(":");
		String sId = StringUtils.substringAfterLast(p_xStereotype.attributeValue(STEREOTYPE_STEREOTYPEHREF_ATTR), "#");
		String sName = t_sParts[1];
		String sNamespace = t_sParts[0];
		
		log.debug("  stereotype : {}, namespace: {}, id: {}", new Object[] { sName, sNamespace, sId });
		
		UmlStereotype r_oUmlStereotype = new UmlStereotype(sId, sName);
		r_oUmlStereotype.setNamespace(sNamespace);
		p_oModelDictionary.registerStereotype(sId, r_oUmlStereotype);
		
		r_oUmlStereotype.setDocumentation(XMI24DocReader.getInstance().readDoc(p_xStereotype, StringUtils.join("Stereotype: ", sFullName)));
		
		return r_oUmlStereotype ; 
	}
	
	/**
	 * Apply stereotypes on model elements
	 * @param p_xModel model node
	 * @param p_oModelDictionary uml dictionnary
	 */
	@SuppressWarnings("unchecked")
	protected void applyStereotypes( Element p_xModel, UmlDictionary p_oModelDictionary ) {
		log.debug("add stereotypes to uml objects");
		
		for( UmlStereotype oUmlStereotype : p_oModelDictionary.getAllStereotypes()) {
			String sNodeName = StringUtils.join( oUmlStereotype.getNamespace(), ":", oUmlStereotype.getName());
			for( Element xDataNode: (List<Element>) p_xModel.getParent().selectNodes( sNodeName)) {
				String sBase = xDataNode.attributeValue(BASE_ELEMENT_ATTR);
				if ( sBase != null ) {
					UmlStereotypedObject oUmlStereotypedObject = p_oModelDictionary.getStereotypeObjectById(sBase);
					if ( oUmlStereotypedObject != null ) {
						// stereotype of class
						oUmlStereotypedObject.addStereotype(oUmlStereotype);
						log.debug("  add stereotype: {} to: {}", oUmlStereotype.getName(), 
							oUmlStereotypedObject.toString());
					}
					else {
						MessageHandler.getInstance().addError("Can't find stereotype object with id: {}", sBase);
					}
				}
				else {
					sBase = xDataNode.attributeValue(BASE_ENUMERATION_ATTR);
					if ( sBase != null ) {
						// stereotype of enumeration
						UmlEnum oUmlEnum = p_oModelDictionary.getEnumById(sBase);
						oUmlEnum.addStereotype(oUmlStereotype);
						log.debug("  add stereotype: {} to enumeration: {}", oUmlStereotype.getName(), oUmlEnum.getName());
					}
				}
			}
		}
	}
}
