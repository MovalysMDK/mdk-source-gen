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

import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlEnum;
import com.a2a.adjava.uml.UmlPackage;

public final class XMI12PackageReader {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(XMI12AssociationReader.class);

	/**
	 * Singleton instance
	 */
	private static XMI12PackageReader packageReader = null;

	/**
	 * Get singleton instance
	 * @return
	 */
	protected static XMI12PackageReader getInstance() {
		if (packageReader == null) {
			packageReader = new XMI12PackageReader();
		}
		return packageReader;
	}

	/**
	 * Constructor
	 */
	private XMI12PackageReader() {
	}

	/**
	 * @param p_xPackage
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	protected UmlPackage readPackage(Element p_xPackage, UmlPackage p_oParent,
			UmlDictionary p_oModelDictonnary, List<Element> p_listAssociationToRead) throws Exception {
		log.debug("> XMI12PackageReader.readPackage");

		String sName = p_xPackage.attributeValue("name");

		UmlPackage r_oPackage = new UmlPackage(sName, p_oParent);
		log.debug("  nom : " + r_oPackage.getFullName());

		// On ne tient compte que des classes dans le package com
		if (r_oPackage.getFullName().startsWith("com")
				|| r_oPackage.getFullName().startsWith("fr")
				|| r_oPackage.getFullName().startsWith("org")
				|| r_oPackage.getFullName().startsWith("biz")) {

			// Lecture des sous-packages
			Element xOwnedElement = p_xPackage.element("Namespace.ownedElement");
			if (xOwnedElement != null) {
				for (Element xPackage : (List<Element>) xOwnedElement.elements("Package")) {
					UmlPackage oPackage = readPackage(xPackage, r_oPackage, p_oModelDictonnary,
							p_listAssociationToRead);
					r_oPackage.addPackage(oPackage);
					p_oModelDictonnary.registerPackage(oPackage);
				}

				// Lecture des classes
				for (Element xClasse : (List<Element>) xOwnedElement.elements("Class")) {

					String sXmiId = xClasse.attributeValue("xmi.id");
					UmlEnum oUmlEnum = p_oModelDictonnary.getEnumById(sXmiId);

					// On verifie que ce n'est pas une enumeration
					if (oUmlEnum == null) {

						UmlClass oClass = XMI12ClassReader.getInstance().readClass(xClasse, r_oPackage,
								p_oModelDictonnary);
						r_oPackage.addClass(oClass);
					} else {
						oUmlEnum.setUmlPackage(r_oPackage);
						r_oPackage.addEnumeration(oUmlEnum);
					}
				}

				// Lecture des associations
				for (Element xAssociation : (List<Element>) xOwnedElement.elements("Association")) {
					p_listAssociationToRead.add(xAssociation);
				}
			}
		} else {
			r_oPackage = null;
		}

		log.debug("< XMI12PackageReader.readPackage");

		return r_oPackage;
	}
}
