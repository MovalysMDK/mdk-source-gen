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


import com.a2a.adjava.types.UITypeDescription;

/**
 * 
 * @author pedubreuil
 *
 */
public class H5UITypeDescription extends UITypeDescription implements Cloneable {
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.a2a.adjava.project.TypeDescription#clone()
	 */
	@Override
	public Object clone() {
		H5UITypeDescription r_oTypeDescription = new H5UITypeDescription();
		this.copyPropertiesTo(r_oTypeDescription);
		return r_oTypeDescription;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.project.TypeDescription#copyPropertiesTo(com.a2a.adjava.project.ITypeDescription)
	 */
	/*@Override
	protected void copyPropertiesTo(UITypeDescription p_oUITypeDescription) {
		super.copyPropertiesTo(p_oUITypeDescription);
		//H5UITypeDescription oUITypeDescription = (H5UITypeDescription) p_oUITypeDescription;
	}*/
}
