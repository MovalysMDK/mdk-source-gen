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
package com.a2a.adjava.uml.jaxb;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.a2a.adjava.uml.UmlStereotype;

/**
 * Xml adapter for map of UmlStereotype
 * @author lmichenaud
 * 
 */
public class MapUmlStereotypeAdapter extends XmlAdapter<String, Map<String, UmlStereotype>> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public String marshal(Map<String, UmlStereotype> p_mapStereotype) throws Exception {

		StringBuilder oSb = new StringBuilder();
		boolean bFirst = true;
		for (UmlStereotype oUmlStereotype : p_mapStereotype.values()) {
			if (bFirst) {
				bFirst = false;
			} else {
				oSb.append(',');
			}
			oSb.append(oUmlStereotype.getName());
		}
		return oSb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override	
	public Map<String, UmlStereotype> unmarshal(String p_sValue) throws Exception {
		Map<String, UmlStereotype> r_mapStereotypes = new HashMap<String, UmlStereotype>();
		// not implemented
		return r_mapStereotypes;
	}
}