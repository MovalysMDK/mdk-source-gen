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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dom4j.Document;
import org.dom4j.Element;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.uml.UmlPackage;
import com.a2a.adjava.uml.UmlStereotype;
import com.a2a.adjava.xmi.XMIReader;

/**
 * XMI 1.2 Reader for VP-Uml
 * @author lmichenaud
 *
 */
public class XMI12Reader implements XMIReader {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(XMI12Reader.class);
	
	/**
	 * Xmi version 1.2
	 */
	private static final String XMI_VERSION = "1.2";
	
	/**
	 * VP Exporter
	 */
	private static final String VP_EXPORTER = "Visual Paradigm for UML";

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmi.XMIReader#canRead(org.dom4j.Document)
	 */
	@Override
	public boolean canRead(Document p_xDocument) {
//		<xmi-reader version="1.2" exporter="">com.a2a.adjava.xmi.v12vpuml.XMI12Reader
//		</xmi-reader>		
		boolean r_bCanRead = false;
		try {
			r_bCanRead = XMI_VERSION.equals(p_xDocument.getRootElement().attributeValue("xmi.version")) &&
				VP_EXPORTER.equals(p_xDocument.getRootElement().element("XMI.header").element("XMI.documentation").elementText("XMI.exporter"));
		} catch (Exception oException) {
			r_bCanRead = false;
		}
		
		return r_bCanRead;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmi.XMIReader#read(org.dom4j.Document)
	 */
	public UmlModel read(Document p_xDocument)
	throws Exception {

		log.debug("> XMI12Reader.read");
		
		List<Element> listAssociationToRead = new ArrayList<Element>();
		UmlModel r_oModele = new UmlModel();
		UmlDictionary oDictionnary = r_oModele.getDictionnary();
		Element xRoot = p_xDocument.getRootElement();
		Element xContent = xRoot.element("XMI.content");
		Element xModel = xContent.element("Model");
		Element xOwnedElement = xModel.element("Namespace.ownedElement");

		// Lecture des types de donnees
		XMI12DataTypeReader.getInstance().readDataTypes(xOwnedElement, oDictionnary);

		// lecture des stereotypes
		for( Element xStereoType : (List<Element>) xOwnedElement.elements("Stereotype")) { 
			r_oModele.addStereotype(
					XMI12StereoTypeReader.getInstance().readStereoType(
							xStereoType, oDictionnary));
		}

		// lecture des enumerations
		UmlStereotype oEnumStereoTypeId = oDictionnary.getStereotypeByName("enumeration");
		if ( oEnumStereoTypeId != null ) {
			for( Element xEnumeration : XMI12Helper.getEnumerations(xOwnedElement, oEnumStereoTypeId.getId())) { 
				XMI12EnumReader.getInstance().readEnum(xEnumeration, oDictionnary);
			}	
		} else {
			log.debug("no stereotype enumeration found");
		}

		// lecture des packages
		for( Element xPackage : (List<Element>) xOwnedElement.elements("Package")) {
			UmlPackage oPackage = XMI12PackageReader.getInstance().readPackage(
					xPackage, null, oDictionnary, listAssociationToRead );
			if ( oPackage != null ) {
				oDictionnary.registerPackage(oPackage);
				r_oModele.addPackage(oPackage);
			}
		}

		// Lecture des enumerations
		for( Element xEnum : (List<Element>) xOwnedElement.elements("Enumeration")) {
			// Erreur 10023
			MessageHandler.getInstance().addError("Enumeration "+xEnum.attributeValue("name")+" must be inside a package.");

		}

		// lecture des classes
		for( Element xClass : (List<Element>) xOwnedElement.elements("Class")) {
			// Erreur 10023
			MessageHandler.getInstance().addError("Class "+xClass.attributeValue("name")+" must be inside a package.");
		}
		// Ajout des associations et des classes d'associations a la racine
		listAssociationToRead.addAll(xOwnedElement.elements("Association"));

		// Add associationEnd to classes
		for( Element xAssociation : listAssociationToRead ) {
			XMI12AssociationReader.getInstance().readAssociation(
					xAssociation, oDictionnary);
		}
		
		//lecture des usages
		for( Element xUsage : (List<Element>) xOwnedElement.elements("Usage")) {
			XMI12UsageReader.getInstance().readUsage(xUsage, oDictionnary);
		}

		log.debug("< XMI12Reader.read");

		return r_oModele;
	}
}
