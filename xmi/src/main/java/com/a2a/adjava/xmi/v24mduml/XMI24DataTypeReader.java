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
package com.a2a.adjava.xmi.v24mduml;

import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.NAME_ATTR;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.ID_ATTR;

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
public final class XMI24DataTypeReader {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory
			.getLogger(XMI24DataTypeReader.class);

	/**
	 * Singleton
	 */
	private static XMI24DataTypeReader dataTypeReader = new XMI24DataTypeReader();

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	protected static XMI24DataTypeReader getInstance() {
		return dataTypeReader;
	}

	/**
	 * Constructor
	 */
	private XMI24DataTypeReader() {
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

		//## Xmi examples:
		// <packagedElement xsi:type="uml:DataType" xmi:id="eee_1045467100323_191782_59" ID="eee_1045467100323_191782_59" name="boolean"/>
		// <packagedElement xsi:type="uml:PrimitiveType" xmi:id="_9_0_2_91a0295_1110274713995_297054_0" ID="_9_0_2_91a0295_1110274713995_297054_0" name="String"/>
		
		for (Element xElement : (List<Element>) p_xModel.selectNodes(
				".//packagedElement[(@*[ namespace-uri() and local-name()=\"type\"] = 'uml:DataType') or (@*[ namespace-uri() and local-name()=\"type\"] = 'uml:PrimitiveType')]")) {
			
			String sId = (String) xElement.attributeValue(ID_ATTR);
			
			String sName = (String) xElement.attributeValue(NAME_ATTR);			
			log.debug("  datatype: {}, id: {}", sName, sId );
			
			p_oModelDictonnary.addDataType(sId, new UmlDataType(sName));
		}
	}
}
