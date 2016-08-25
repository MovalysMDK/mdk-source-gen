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
package com.a2a.adjava.languages.ionic2.project;

import org.dom4j.Element;

import com.a2a.adjava.types.UITypeDescriptionFactory;
import com.a2a.adjava.types.IUITypeDescription;
import com.a2a.adjava.types.UITypeDescription;

public class H5UITypeDescriptionFactory extends UITypeDescriptionFactory {

	/**
	 * Create a ui type description from an xml configuration
	 * @param p_xType xml configuration
	 * @return IUITypeDescription
	 */
	@Override
	public IUITypeDescription createUITypeDescription(Element p_xType) throws Exception {
		H5UITypeDescription r_oTypeDesc = new H5UITypeDescription();
		fillUITypeDescription(p_xType, r_oTypeDesc);
		return r_oTypeDesc;
	}
	
	/**
	 * Fill UI Type description using xml element
	 * @param p_xType xml element
	 * @param p_oTypeDescription UI Type description
	 * @throws Exception 
	 */
	@Override
	protected void fillUITypeDescription(Element p_xUiType, UITypeDescription p_oTypeDescription ) throws Exception {
		super.fillUITypeDescription(p_xUiType, p_oTypeDescription);
		// H5UITypeDescription r_oTypeDesc = (H5UITypeDescription) p_oTypeDescription;		
	}
}
