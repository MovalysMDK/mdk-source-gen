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
import com.a2a.adjava.uml.UmlDataType;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlEnum;
import com.a2a.adjava.uml.UmlStereotype;

public final class XMI12EnumReader {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(XMI12AssociationReader.class);

	/**
	 * Enum Reader
	 */
	private static XMI12EnumReader enumReader = null ;
	
	/**
	 * @return
	 */
	protected static XMI12EnumReader getInstance() {
		if ( enumReader == null ) {
			enumReader = new XMI12EnumReader();
		}
		return enumReader ;
	}
	
	/**
	 * 
	 */
	private XMI12EnumReader() {
		
	}
	
	/**
	 * @param p_xEnum
	 * @param p_oPackage
	 * @param p_oModelDictonnary
	 * @param p_oConfig
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public UmlEnum readEnum(Element p_xEnum, UmlDictionary p_oModelDictonnary) throws Exception {
		
		log.debug("> XMI12EnumReader.readEnum");
		
		String sId = p_xEnum.attributeValue("xmi.id");
		String sName = p_xEnum.attributeValue("name");

		UmlDataType oUmlDataType = new UmlDataType(sName);
		
		UmlEnum r_oEnum = new UmlEnum( sName, oUmlDataType );
		
		log.debug(" name : " + r_oEnum.getName());
		
		Element xClassifierFeature = p_xEnum.element("Classifier.feature");
		if ( xClassifierFeature != null ) {
			for( Element xEnumValue : (List<Element>) xClassifierFeature.elements("Attribute")) {
				r_oEnum.addEnumValue(xEnumValue.attributeValue("name"));
			}
		}
		
		// Lecture des stereotypes de la classe
		Element xStereoTypeValues = p_xEnum.element("ModelElement.stereotype");
		if(xStereoTypeValues!=null){
			for (Element xStereoType : (List<Element>) xStereoTypeValues.elements("Stereotype")) {
				String sIdRef = xStereoType.attributeValue("xmi.idref");
				UmlStereotype oUmlStereoType = p_oModelDictonnary.getStereotypeById(sIdRef);
				// on ne prend pas en compte le stereotype enumeration
				if (oUmlStereoType == null) {
					throw new AdjavaException("Impossible de trouver le stereotype d'id {}", sIdRef);
				}
				if (!"enumeration".equals(oUmlStereoType.getName())) {
					r_oEnum.addStereotype(oUmlStereoType);
				}
			}
		}

		p_oModelDictonnary.addDataType(sId, oUmlDataType);
		p_oModelDictonnary.registerEnumeration(sId, r_oEnum );
		
		log.debug(" values : " + r_oEnum.getEnumValues().toString());
		
		log.debug("< XMI12EnumReader.readEnum");
		
		return r_oEnum;
	}
}
