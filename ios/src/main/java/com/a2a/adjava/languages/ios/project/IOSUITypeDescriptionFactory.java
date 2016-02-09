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
package com.a2a.adjava.languages.ios.project;

import org.dom4j.Element;

import com.a2a.adjava.types.IUITypeDescription;
import com.a2a.adjava.types.UITypeDescription;
import com.a2a.adjava.types.UITypeDescriptionFactory;
import com.a2a.adjava.utils.BeanUtils;

/**
 * UI Type Factory for IOS
 * @author lmichenaud
 *
 */
public class IOSUITypeDescriptionFactory extends UITypeDescriptionFactory {

	/**
	 * Create a ui type description from an xml configuration
	 * @param p_xType xml configuration
	 * @return IUITypeDescription
	 * @throws Exception exception
	 */
	public IUITypeDescription createUITypeDescription(Element p_xType) throws Exception {
		IOSUITypeDescription r_oTypeDesc = new IOSUITypeDescription();
		fillUITypeDescription(p_xType, r_oTypeDesc);
		return r_oTypeDesc;
	}
	
	/**
	 * Fill UI Type description using xml element
	 * @param p_xUiType xml element
	 * @param p_oTypeDescription UI Type description
	 * @throws Exception 
	 */
	protected void fillUITypeDescription(Element p_xUiType, UITypeDescription p_oTypeDescription ) throws Exception {
		super.fillUITypeDescription(p_xUiType, p_oTypeDescription);
		IOSUITypeDescription r_oTypeDesc = (IOSUITypeDescription) p_oTypeDescription;
		this.defineProperty(r_oTypeDesc, "rwComponentHeight", p_xUiType.element("read-write"), "component-height");
		this.defineProperty(r_oTypeDesc, "roComponentHeight", p_xUiType.element("read-only"), "component-height");
		this.defineProperty(r_oTypeDesc, "rwComponentWidth", p_xUiType.element("read-write"), "component-width");
		this.defineProperty(r_oTypeDesc, "roComponentWidth", p_xUiType.element("read-only"), "component-width");
		
		Element xRw = p_xUiType.element("read-write");
		Element xRo = p_xUiType.element("read-only");
		if (xRw != null) {
			r_oTypeDesc.setRwCellType(xRw.elementText("cell-type"));
		}
		if (xRo != null) {
			r_oTypeDesc.setRoCellType(xRw.elementText("cell-type"));
		}
	}
	
	/**
	 * Define property of bean with xml element
	 * @param p_oObject object to modify
	 * @param p_sPropertyName property name
	 * @param p_xElement parent xml element
	 * @param p_sElementName element name
	 * @throws Exception exception
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
