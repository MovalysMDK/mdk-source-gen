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

import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlStereotype;
import com.a2a.adjava.uml.UmlUsage;

/**
 * <p>
 * XMI12UsageReader
 * </p>
 * 
 * <p>
 * Copyright (c) 2011
 * <p>
 * Company: Adeuza
 * 
 * @author smaitre
 * 
 */
public final class XMI12UsageReader {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(XMI12UsageReader.class);

	/**
	 * Association reader
	 */
	private static XMI12UsageReader usageReader = null;

	/**
	 * @return
	 */
	protected static XMI12UsageReader getInstance() {
		if (usageReader == null) {
			usageReader = new XMI12UsageReader();
		}
		return usageReader;
	}

	/**
	 * Constructor
	 */
	private XMI12UsageReader() {

	}

	/**
	 * @param p_xUsage
	 * @param p_oModelDictonnary
	 * @param p_oConfig
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected void readUsage(Element p_xUsage, UmlDictionary p_oModelDictionnary) {
		log.debug("> XMI12Reader.readUsage");
		String sClientXmiId = p_xUsage.attributeValue("client");
		String sSupplierXmiId = p_xUsage.attributeValue("supplier");
		String sName = p_xUsage.attributeValue("name");
		String sId = p_xUsage.attributeValue("xmi.id");
		log.debug("lecture de l'usage entre " + sClientXmiId + " (client) et " + sSupplierXmiId + " (supplier)");
		UmlClass oClient = p_oModelDictionnary.getClassById(sClientXmiId);
		if (oClient == null) {
			MessageHandler.getInstance().addError("Impossible de trouver le client " + sClientXmiId);
		}
		UmlClass oSupplier = p_oModelDictionnary.getClassById(sSupplierXmiId);
		if (oSupplier == null) {
			MessageHandler.getInstance().addError("Impossible de trouver le supplier " + sSupplierXmiId);
		}

		UmlUsage oUsage = new UmlUsage(sName, oClient, oSupplier);
		oClient.addUsage(oUsage);
		
		//lecture des stérétotype
		Element xStereotypes = p_xUsage.element("ModelElement.stereotype");
		if (xStereotypes!=null) {
			String sIdRef = null;
			UmlStereotype oStereotype = null;
			for(Object xStereotype : xStereotypes.elements("Stereotype")) {
				sIdRef = ((Element)xStereotype).attributeValue("xmi.idref");
				oStereotype = p_oModelDictionnary.getStereotypeById(sIdRef);
				// ignore "use" stereotypes
				if (!"use".equals(oStereotype.getName())) {
					oUsage.addStereotype(oStereotype);
				}
			}
		}
		
		//ajout de l'usage dans le dictionnaire
		p_oModelDictionnary.registerUsage(sId, oUsage);
		log.debug("< XMI12Reader.readUsage");
	}
}
