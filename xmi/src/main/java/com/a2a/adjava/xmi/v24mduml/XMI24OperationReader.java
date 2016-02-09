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

import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.XMIID2011_ATTR;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.NAME_ATTR;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlOperation;
import com.a2a.adjava.utils.StrUtils;

/**
 * Operation reader
 * @author lmichenaud
 *
 */
public final class XMI24OperationReader {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(XMI24OperationReader.class);

	/**
	 * Operation reader
	 */
	private static XMI24OperationReader operationReader = new XMI24OperationReader();
	
	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	protected static XMI24OperationReader getInstance() {
		return operationReader ;
	}
	
	/**
	 * Constructor
	 */
	private XMI24OperationReader() {
		// private constructor because singleton
	}
	
	/**
	 * Read operation
	 * @param p_xOperation operation node
	 * @param p_oClass uml class
	 * @param p_oModelDictonnary model dictionary
	 * @return Uml operation
	 * @throws Exception exception
	 */
	protected UmlOperation readOperation(Element p_xOperation, UmlClass p_oClass,
			UmlDictionary p_oModelDictonnary )
			throws Exception {

		String sOperationName = p_xOperation.attributeValue(NAME_ATTR);
		String sOperationId = p_xOperation.attributeValue(XMIID2011_ATTR);

		log.debug("operation: {}, id: {}", sOperationName, sOperationId);
		
		UmlOperation r_oOperation = new UmlOperation( sOperationName, p_oClass );
		p_oModelDictonnary.registerStereotypeObject(sOperationId, r_oOperation);
		
		r_oOperation.setDocumentation(XMI24DocReader.getInstance().readDoc(p_xOperation, StringUtils.join("Operation:", p_oClass.getName(), StrUtils.DOT_S, sOperationName)));
			
		return r_oOperation;
	}
}
