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
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlOperation;
import com.a2a.adjava.uml.UmlStereotype;

public final class XMI12OperationReader {

	private static final Logger log = LoggerFactory.getLogger(XMI12OperationReader.class);

	private static XMI12OperationReader operationReader = null ;
	
	/**
	 * @return
	 */
	protected static XMI12OperationReader getInstance() {
		if ( operationReader == null ) {
			operationReader = new XMI12OperationReader();
		}
		return operationReader ;
	}
	
	/**
	 * 
	 */
	private XMI12OperationReader() { 
	}
	
	/**
	 * @param p_xPackage
	 * @return
	 * @throws Exception
	 */
	protected UmlOperation readOperation(Element p_xOperation, UmlClass p_oClass,
			UmlDictionary p_oModelDictonnary )
			throws Exception {
		log.debug("> XMI12OperationReader.readOperation");
		
		String sOperationName = p_xOperation.attributeValue("name");
		sOperationName = sOperationName.replace("getVecteur", "getList");

		UmlOperation r_oOperation = new UmlOperation( sOperationName, p_oClass );
		
		String sMsgIdentifiant = "operation " + sOperationName + " of class " + p_oClass.getName();
		String sDoc = XMI12DocReader.getInstance().readDoc(p_xOperation, sMsgIdentifiant);
		r_oOperation.setDocumentation(sDoc);
		
		// Lecture des stereotypes de la classe
		Element xStereoTypeValues = p_xOperation.element("ModelElement.stereotype");
		if (xStereoTypeValues != null) {
			for (Element xStereoType : (List<Element>) xStereoTypeValues.elements("Stereotype")) {
				String sIdRef = xStereoType.attributeValue("xmi.idref");
				UmlStereotype oUmlStereoType = p_oModelDictonnary.getStereotypeById(sIdRef);
				if (oUmlStereoType == null) {
					throw new AdjavaException("Impossible de trouver le stereotype d'id {}", sIdRef);
				}
				r_oOperation.addStereotype(oUmlStereoType);
			}
		}
		
		log.debug("< XMI12OperationReader.readOperation");
		
		return r_oOperation;
	}
}
