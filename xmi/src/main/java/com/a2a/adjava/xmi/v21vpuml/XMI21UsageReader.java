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

import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.CLIENT_ATTR;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.NAME_ATTR;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.SUPPLIER_ATTR;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.XMIEXTENSION_NODE;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.XMIID2011_ATTR;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.XMIVALUE2011_ATTR;

import java.util.List;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlStereotype;
import com.a2a.adjava.uml.UmlUsage;

/**
 * Usage reader
 * @author lmichenaud
 *
 */
public final class XMI21UsageReader {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(XMI21UsageReader.class);

	/**
	 * Association reader
	 */
	private static XMI21UsageReader usageReader = new XMI21UsageReader();

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	protected static XMI21UsageReader getInstance() {
		return usageReader;
	}

	/**
	 * Constructor
	 */
	private XMI21UsageReader() {
		// private cause singleton pattern
	}

	/**
	 * Read usage
	 * @param p_xUsage usage
	 * @param p_oModelDictionary model dictionary
	 */
	protected void readUsage(Element p_xUsage, UmlDictionary p_oModelDictionary) 
			throws Exception {
	
		// XMI example :
		// <ownedMember client="WlWgoAqAUDAUUQ61" name="children" supplier="XpBgoAqAUDAUUQ7K" xmi:id="cpUcoAqAUDAUUQph" xmi:type="uml:Usage">
		//	<xmi:Extension extender="Visual Paradigm">
		//		<qualityScore value="-1"/>
		//		<appliedStereotype xmi:value="Usage_use_id"/>
		//		<appliedStereotype xmi:value="Usage_Mm_model_id"/>
		//	</xmi:Extension>
		// </ownedMember>

		String sClientId = p_xUsage.attributeValue(CLIENT_ATTR);
		String sSupplierId = p_xUsage.attributeValue(SUPPLIER_ATTR);
		
		String sId = p_xUsage.attributeValue(XMIID2011_ATTR);
		String sName = p_xUsage.attributeValue(NAME_ATTR);

		log.debug("usage: {}, client id: {}, supplierid: {}, id: {}", new Object[] {sName, sClientId, sSupplierId, sId} );
		UmlClass oClientClass = p_oModelDictionary.getClassById(sClientId);
		UmlClass oSupplierClass = p_oModelDictionary.getClassById(sSupplierId);
		UmlUsage oUsage = new UmlUsage(sName, oClientClass, oSupplierClass);
		oClientClass.addUsage(oUsage);

		// Read Extension - Stereotype
		Element xEltExtension = p_xUsage.element(XMIEXTENSION_NODE);
		if (xEltExtension != null) {
			for (Element xStereoType : (List<Element>) xEltExtension.elements("appliedStereotype")) { 
				String sIdRef = xStereoType.attributeValue(XMIVALUE2011_ATTR);
				if (!"Usage_use_id".equals(sIdRef)) {
					UmlStereotype oUmlStereoType = p_oModelDictionary.getStereotypeById(sIdRef);
					if (oUmlStereoType == null) {
						throw new AdjavaException("Impossible de trouver le stereotype d'id {}", sIdRef);
					}
					oUsage.addStereotype(oUmlStereoType);
				}
			}
		}
		
		p_oModelDictionary.registerUsage(sId, oUsage);
	}
}
