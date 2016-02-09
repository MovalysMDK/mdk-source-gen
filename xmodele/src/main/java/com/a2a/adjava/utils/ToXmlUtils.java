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
package com.a2a.adjava.utils;

import org.dom4j.Element;

import com.a2a.adjava.xmodele.MLinkedInterface;
import com.a2a.adjava.xmodele.SInterface;

public class ToXmlUtils {

	/**
	 * @param p_xElement
	 * @param p_oInterface
	 */
	public static void addImplements(Element p_xElement, SInterface p_oInterface) {
		if (p_oInterface != null) {
			Element xImplements = p_xElement.addElement("implements");
			Element xElement = xImplements.addElement("interface");
			xElement.addAttribute("name", p_oInterface.getName());
			xElement.addAttribute("full-name", p_oInterface.getFullName());
			xElement.addAttribute("package", p_oInterface.getPackage().getFullName());
			Element xLinkedInterfaces = xElement.addElement("linked-interfaces");
			for (MLinkedInterface oLinkedInterface : p_oInterface.getLinkedInterfaces()) {
				xLinkedInterfaces.add(oLinkedInterface.toXml());
			}
		}
	}
}
