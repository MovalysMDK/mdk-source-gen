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

import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.BASE_ELEMENT_ATTR;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.BASE_ENUMERATION_ATTR;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.ID_ATTR;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.NAME_ATTR;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlEnum;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.uml.UmlStereotype;
import com.a2a.adjava.uml.UmlStereotypedObject;
import com.a2a.adjava.xmi.v12vpuml.XMI12StereoTypeReader;

/**
 * Stereotype reader
 * @author lmichenaud
 *
 */
public final class XMI21StereotypeReader {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(XMI21StereotypeReader.class);
	
	/**
	 * Stereotype reader
	 */
	private static XMI21StereotypeReader stereotypeReader = new XMI21StereotypeReader();
	
	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	protected static XMI21StereotypeReader getInstance() {
		return stereotypeReader ;
	}
	
	/**
	 * Constructor
	 */
	private XMI21StereotypeReader() {
		// private because singleton
	}
		
	/**
	 * Read stereotypes
	 * @param p_xModel model node
	 * @param p_oModelDictonary model dictionary
	 * @throws Exception exception
	 */
	@SuppressWarnings("unchecked")
	protected void readStereotypes(Element p_xModel, UmlDictionary p_oModelDictonary ) throws Exception {
		log.debug("read stereotypes");
		
		// XMI example:
		// <ownedMember name="Mm_model" xmi:id="Usage_Mm_model_id" xmi:type="uml:Stereotype">
				
		// stereotypes from module
		for (Element xStereotype : (List<Element>) p_xModel.getParent().selectNodes(
				".//ownedMember[(@*[ namespace-uri() and local-name()=\"type\"] = 'uml:Stereotype')]")) {	
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
		
		String sId = (String) p_xStereotype.attributeValue(ID_ATTR);
		if (sId != null) {
			String[] t_sParts = sId.split("_");
			String sPrefixeName = t_sParts[0];
			if (sPrefixeName.equals("Class") || sPrefixeName.equals("Usage") || sPrefixeName.equals("Association")) {
				String sName = (String) p_xStereotype.attributeValue(NAME_ATTR);			
				
				log.debug("  stereotype : {}, id: {}", new Object[] { sName, sId });
				
				UmlStereotype r_oUmlStereotype = new UmlStereotype(sId, sName);
				p_oModelDictionary.registerStereotype(sId, r_oUmlStereotype);
				
				r_oUmlStereotype.setDocumentation(XMI21DocReader.getInstance().readDoc(p_xStereotype, StringUtils.join("Stereotype: ", sId)));
				
				return r_oUmlStereotype ; 
			}
		}
		return null;
	}
	
}
