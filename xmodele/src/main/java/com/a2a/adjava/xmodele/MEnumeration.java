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
package com.a2a.adjava.xmodele;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import com.a2a.adjava.types.ITypeDescription;

/**
 * <p>Objet Enumération</p>
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @since Annapurna
 */
public class MEnumeration extends STypedElement {

	/** la liste des noms des énumérations */
	private List<String> enumValues ;
	
	/**
	 * Construct an <em>MEnumeration</em> object.
	 * @param p_sName
	 * @param p_oPackage
	 * @param p_listEnumValues
	 * @param p_oTypeDescription
	 */
	public MEnumeration( String p_sName, MPackage p_oPackage, List<String> p_listEnumValues, ITypeDescription p_oTypeDescription ) {
		super("enum", p_sName, p_oPackage, p_oTypeDescription);
		this.enumValues = p_listEnumValues ;
	}

	/**
	 * Return the list of enum names
	 * @return list of string.
	 */
	public List<String> getEnumValues() {
		return enumValues;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void toXmlInsertBeforeDocumentation(Element p_xElement) {
		super.toXmlInsertBeforeDocumentation(p_xElement);
		p_xElement.addElement("name-uncapitalized").setText(StringUtils.uncapitalize(this.getName()));
		p_xElement.addElement("name-uppercase").setText(this.getName().toUpperCase());
		Element xEnumValues = p_xElement.addElement("enum-values");
		for( String sValue : this.enumValues) {
			xEnumValues.addElement("value").setText(sValue);
		}
	}
}
