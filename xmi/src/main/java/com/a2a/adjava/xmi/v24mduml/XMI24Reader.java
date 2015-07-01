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

import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.DOCUMENTATION_NODE;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.EXPORTERVERSION_NODE;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.EXPORTER_NODE;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.xmi.XMIReader;

/**
 * 
 * <p>XMI 2.1 Reader for MagicDraw UML</p>
 *
 * <p>Copyright (c) 2012
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 * 
 */
public class XMI24Reader implements XMIReader {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(XMI24Reader.class);

	/**
	 * VP Exporter
	 */
	private static final String MD_EXPORTER = "MagicDraw UML";
	
	/**
	 * VP Exporter version
	 */
	private static final String MD_EXPORTER_VERSION = "17.0.5";
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmi.XMIReader#canRead(org.dom4j.Document)
	 */
	@Override
	public boolean canRead(Document p_xDocument) {
//		<xmi:Documentation>
//			<xmi:exporter>MagicDraw UML</xmi:exporter>
//			<xmi:exporterVersion>17.0.1</xmi:exporterVersion>
//		</xmi:Documentation>
		boolean r_bCanRead = false;
		try {
			Element xDoc = p_xDocument.getRootElement().element(DOCUMENTATION_NODE);
			r_bCanRead = MD_EXPORTER.equals(xDoc.elementText(EXPORTER_NODE)) && 
					MD_EXPORTER_VERSION.equals(xDoc.elementText(EXPORTERVERSION_NODE)); 
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

		log.debug("read model");
		
		List<Element> listAssociationToRead = new ArrayList<Element>();
		List<Element> listUsageToRead = new ArrayList<Element>();
		List<Element> listCommentToRead = new ArrayList<Element>();
		
		UmlModel r_oModele = new UmlModel();
		UmlDictionary oDictionnary = r_oModele.getDictionnary();
		Element xRoot = p_xDocument.getRootElement();
		Element xModel = xRoot.element("Model");

		// Read datatypes
		XMI24DataTypeReader.getInstance().readDataTypes(xRoot, oDictionnary);

		// Read stereotypes
		XMI24StereotypeReader.getInstance().readStereotypes(xModel, oDictionnary);

		// Read enumerations
		// All enumerations must be read before classes because they are referenced by attributes.
		XMI24EnumReader.getInstance().readEnums(xModel, oDictionnary);

		// Read packages
		XMI24PackageReader.getInstance().processChildren(xModel, null, oDictionnary, r_oModele, listAssociationToRead, listUsageToRead, listCommentToRead);

		// Add associationEnd to classes
		for( Element xAssociation : listAssociationToRead ) {
			XMI24AssociationReader.getInstance().readAssociation(
					xAssociation, oDictionnary);
		}
		
		// Add comment to classes
		for( Element xComment : listCommentToRead ) {
			XMI24CommentReader.getInstance().readComment(
					xComment, oDictionnary);
		}
		
		// Read usages
		for( Element xUsage : listUsageToRead ) {
			XMI24UsageReader.getInstance().readUsage(xUsage, oDictionnary);
		}
		
		// Add steoreotypes to uml objects
		XMI24StereotypeReader.getInstance().applyStereotypes(xModel, oDictionnary);
		
		log.debug("read model finished.");

		return r_oModele;
	}
}
