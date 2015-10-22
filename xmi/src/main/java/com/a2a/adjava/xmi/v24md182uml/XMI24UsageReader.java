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

import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.CLIENT_NODE;
import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.NAME_ATTR;
import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.SUPPLIER_NODE;
import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.XMIID2011_ATTR;
import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.XMIIDREF2011_ATTR;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlUsage;

/**
 * Usage reader
 * @author lmichenaud
 *
 */
public final class XMI24UsageReader {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(XMI24UsageReader.class);

	/**
	 * Association reader
	 */
	private static XMI24UsageReader usageReader = new XMI24UsageReader();

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	protected static XMI24UsageReader getInstance() {
		return usageReader;
	}

	/**
	 * Constructor
	 */
	private XMI24UsageReader() {
		// private cause singleton pattern
	}

	/**
	 * Read usage
	 * @param p_xUsage usage
	 * @param p_oModelDictionary model dictionary
	 */
	protected void readUsage(Element p_xUsage, UmlDictionary p_oModelDictionary) {
	
//##
//	<packagedElement xmi:type='uml:Usage' xmi:id='_17_0_1_2_16a6041b_1355753632655_154075_1982' name='note'>
//		<client xmi:idref='_17_0_1_2_16a6041b_1355753435811_827827_1957'/>
//		<supplier xmi:idref='_17_0_1_2_16a6041b_1355752304188_339393_1841'/>
//	</packagedElement>		

		Element xClient = p_xUsage.element(CLIENT_NODE);
		Element xSupplier = p_xUsage.element(SUPPLIER_NODE);
		
		String sClientId = xClient.attributeValue(XMIIDREF2011_ATTR);
		String sSupplierId = xSupplier.attributeValue(XMIIDREF2011_ATTR);
		
		String sId = p_xUsage.attributeValue(XMIID2011_ATTR);
		String sName = p_xUsage.attributeValue(NAME_ATTR);

		log.debug("usage: {}, client id: {}, supplierid: {}, id: {}", new Object[] {sName, sClientId, sSupplierId, sId} );
		UmlClass oClientClass = p_oModelDictionary.getClassById(sClientId);
		UmlClass oSupplierClass = p_oModelDictionary.getClassById(sSupplierId);
		UmlUsage oUsage = new UmlUsage(sName, oClientClass, oSupplierClass);
		oClientClass.addUsage(oUsage);

		p_oModelDictionary.registerUsage(sId, oUsage);
	}
}
