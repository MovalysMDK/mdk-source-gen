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
package com.a2a.adjava.languages.w8.project;

import org.dom4j.Element;

import com.a2a.adjava.types.UITypeDescriptionFactory;
import com.a2a.adjava.types.IUITypeDescription;
import com.a2a.adjava.types.UITypeDescription;
import com.a2a.adjava.utils.BeanUtils;

public class W8UITypeDescriptionFactory extends UITypeDescriptionFactory {

	/**
	 * Create a ui type description from an xml configuration
	 * @param p_xType xml configuration
	 * @return IUITypeDescription
	 */
	public IUITypeDescription createUITypeDescription(Element p_xType) throws Exception {
		W8UITypeDescription r_oTypeDesc = new W8UITypeDescription();
		fillUITypeDescription(p_xType, r_oTypeDesc);
		return r_oTypeDesc;
	}
	
	/**
	 * Fill UI Type description using xml element
	 * @param p_xType xml element
	 * @param p_oTypeDescription UI Type description
	 * @throws Exception 
	 */
	protected void fillUITypeDescription(Element p_xUiType, UITypeDescription p_oTypeDescription ) throws Exception {
		super.fillUITypeDescription(p_xUiType, p_oTypeDescription);
		W8UITypeDescription r_oTypeDesc = (W8UITypeDescription) p_oTypeDescription;
		
	}
	
	/**
	 * Define property of bean with xml element
	 * @param p_oObject object to modify
	 * @param p_sPropertyName property name
	 * @param p_xElement parent xml element
	 * @param p_sElementName element name
	 * @throws Exception
	 */
	private void defineProperty( Object p_oObject, String p_sPropertyName, Element p_xElement, String p_sElementName ) throws Exception {
		if (p_xElement != null) {
			String sValue = p_xElement.elementText(p_sElementName);
			if ( sValue != null ) {
				BeanUtils.setIfNotNull(p_oObject, p_sPropertyName, Integer.parseInt(sValue));
			}
		}
	}
}
