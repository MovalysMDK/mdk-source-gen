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
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.ID_ATTR;

import java.util.List;

import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.uml.UmlDataType;
import com.a2a.adjava.uml.UmlDictionary;

/**
 * Datatype Reader
 * 
 * @author lmichenaud
 */
public final class XMI21DataTypeReader {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory
			.getLogger(XMI21DataTypeReader.class);

	/**
	 * Singleton
	 */
	private static XMI21DataTypeReader dataTypeReader = new XMI21DataTypeReader();

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	protected static XMI21DataTypeReader getInstance() {
		return dataTypeReader;
	}

	/**
	 * Constructor
	 */
	private XMI21DataTypeReader() {
		// private because singleton
	}

	/**
	 * Read datatypes
	 * @param p_xModel model node
	 * @param p_oModelDictonnary model dictionary
	 * @throws Exception exception
	 * 
	 */
	@SuppressWarnings("unchecked")
	protected void readDataTypes(Element p_xModel,
			UmlDictionary p_oModelDictonnary) throws Exception {

		log.debug("datatypes:");

		// XMI example:
		// <ownedMember name="String" visibility="public" xmi:id="String_id" xmi:type="uml:DataType"/>
		
		for (Element xElement : (List<Element>) p_xModel.selectNodes(
				".//ownedMember[(@*[ namespace-uri() and local-name()=\"type\"] = 'uml:DataType')]")) {
			
			String sId = (String) xElement.attributeValue(ID_ATTR);
			String sName = (String) xElement.attributeValue(NAME_ATTR);			
			log.debug("  datatype: {}, id: {}", sName, sId );
			
			p_oModelDictonnary.addDataType(sId, new UmlDataType(sName));
		}
	}
}
