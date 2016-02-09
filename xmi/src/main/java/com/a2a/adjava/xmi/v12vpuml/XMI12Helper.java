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

import org.dom4j.Element;

public class XMI12Helper {

	/**
	 * @param p_sClassId
	 * @return
	 */
	public static boolean isPartOfAssociationClass(Element p_xElement,
			String p_sClassId) {
		return p_xElement
				.getDocument()
				.selectSingleNode(
						"//UML:AssociationClass/UML:Association.connection/UML:AssociationEnd[@participant = '"
								+ p_sClassId + "']") != null;
	}

	/**
	 * @param p_sClassId
	 * @return
	 */
	public static String getClassIdOnAssociationClassByAssociationId(
			Element p_xElement, String p_sAssociationId) {
		Element xAssociationEnd = (Element) p_xElement
				.getDocument()
				.selectSingleNode(
						"//UML:AssociationClass/UML:Association.connection/UML:AssociationEnd[@participant = '"
								+ p_sAssociationId + "']");
		String r_sClassId = null;
		if (xAssociationEnd != null) {
			List<Element> listAssociationEnds = xAssociationEnd.getParent()
					.elements();
			if (listAssociationEnds.get(0).attributeValue("participant").equals( p_sAssociationId )) {
				r_sClassId = listAssociationEnds.get(1).attributeValue(
						"participant");
			} else {
				r_sClassId = listAssociationEnds.get(0).attributeValue(
						"participant");
			}
		}
		return r_sClassId;
	}
	
	
	/**
	 * @param p_sClassId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Element> getEnumerations( Element p_xRoot, String p_sEnumerationStereoTypeId ) {
		List<Element> r_xEnumeration = new ArrayList<Element>();
		List<Element> listElements = p_xRoot
				.getDocument()
				.selectNodes(
						"//UML:Class/UML:ModelElement.stereotype/UML:Stereotype[@xmi.idref = '"
								+ p_sEnumerationStereoTypeId + "']");
		for( Element xElement : listElements ) {
			r_xEnumeration.add( xElement.getParent().getParent());
		}
		return r_xEnumeration ;
	}
}
