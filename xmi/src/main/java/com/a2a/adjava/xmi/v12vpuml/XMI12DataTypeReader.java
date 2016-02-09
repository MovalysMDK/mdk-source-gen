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

import com.a2a.adjava.uml.UmlDataType;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.utils.StrUtils;

public final class XMI12DataTypeReader {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(XMI12DataTypeReader.class);

	private static XMI12DataTypeReader dataTypeReader = null;

	/**
	 * @return
	 */
	protected static XMI12DataTypeReader getInstance() {
		if (dataTypeReader == null) {
			dataTypeReader = new XMI12DataTypeReader();
		}
		return dataTypeReader;
	}

	/**
	 * 
	 */
	private XMI12DataTypeReader() {

	}

	/**
	 * @throws Exception
	 * 
	 */
	protected void readDataTypes(Element p_xModel, UmlDictionary p_oModelDictonnary) throws Exception {

		log.debug("> XML12DataTypeReader.readDataTypes");

		// lecture des types de base
		for (Element xElement : (List<Element>) p_xModel.elements("DataType")) {
			String sId = (String) xElement.attributeValue("xmi.id");
			String sName = (String) xElement.attributeValue("name");
			p_oModelDictonnary.addDataType(sId, new UmlDataType(sName));
		}

		// lecture du package java.lang
		List<Element> listClasses = (List<Element>) p_xModel
				.selectNodes("UML:Package[@name='java']/UML:Namespace.ownedElement/UML:Package[@name='lang' or @name='util' or @name='sql']/UML:Namespace.ownedElement/UML:Class");
		for (Element xElement : listClasses) {
			String sParentName = xElement.getParent().getParent().attributeValue("name");
			String sId = xElement.attributeValue("xmi.id");
			String sName = "java." + sParentName + StrUtils.DOT_S + xElement.attributeValue("name");

			log.debug("  datatype : " + sName);

			p_oModelDictonnary.addDataType(sId, new UmlDataType(sName));
		}

		log.debug("< XML12DataTypeReader.readDataTypes");
	}
}
