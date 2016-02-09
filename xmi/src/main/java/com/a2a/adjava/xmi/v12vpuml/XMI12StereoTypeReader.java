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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dom4j.Element;

import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlStereotype;

/**
 * 
 * <p>TODO Décrire la classe XMI12StereoTypeReader</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author mmadigand
 *
 */
public final class XMI12StereoTypeReader {

	private static final Logger log = LoggerFactory.getLogger(XMI12StereoTypeReader.class);
	
	private static XMI12StereoTypeReader stereoTypeReader = null ;
	
	/**
	 * @return
	 */
	protected static XMI12StereoTypeReader getInstance() {
		if ( stereoTypeReader == null ) {
			stereoTypeReader = new XMI12StereoTypeReader();
		}
		return stereoTypeReader ;
	}
	
	/**
	 * 
	 */
	private XMI12StereoTypeReader() {
		
	}
		
	/**
	 * @param p_xClasse
	 * @param p_oPackage
	 * @return
	 * @throws Exception
	 */
	protected UmlStereotype readStereoType(Element p_xStereoType,
			UmlDictionary p_oModelDictonnary)
			throws Exception {
		log.debug("> XMI12StereoTypeReader.readStereoType");
		
		String sId = p_xStereoType.attributeValue("xmi.id"); 
		String sName = p_xStereoType.attributeValue("name");
		log.debug("stereotype : name='" + sName + "', id='" + sId + "'");
		UmlStereotype r_oUmlStereotype = new UmlStereotype(sId, sName);
		p_oModelDictonnary.registerStereotype(sId, r_oUmlStereotype);
		
		String sMsgIdentifiant = "class " + r_oUmlStereotype.getName();
		String sDocumentation = XMI12DocReader.getInstance().readDoc(p_xStereoType, sMsgIdentifiant);
		r_oUmlStereotype.setDocumentation(sDocumentation);
		
		log.debug("< XMI12StereoTypeReader.readStereoType");
		
		return r_oUmlStereotype ; 
	}
}
