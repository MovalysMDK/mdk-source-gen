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

import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.types.TypeDescription;

/**
 * IOS Type Description
 * @author lmichenaud
 *
 */
public class IOSTypeDescription extends TypeDescription implements Cloneable {

	/**
	 * Matching Coredata type
	 */
	private String coreDataType ;
	
	/**
	 * Binded in viewmodel
	 */
	private Boolean vmBinded = Boolean.TRUE;
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.a2a.adjava.project.TypeDescription#clone()
	 */
	@Override
	public Object clone() {
		IOSTypeDescription r_oTypeDescription = new IOSTypeDescription();
		this.copyPropertiesTo(r_oTypeDescription);
		return r_oTypeDescription;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.project.TypeDescription#copyPropertiesTo(com.a2a.adjava.project.ITypeDescription)
	 */
	@Override
	protected void copyPropertiesTo(ITypeDescription p_oTypeDescription) {
		super.copyPropertiesTo(p_oTypeDescription);
		IOSTypeDescription oTypeDescription = (IOSTypeDescription) p_oTypeDescription ;
		oTypeDescription.setCoreDataType(this.getCoreDataType());
	}

	/**
	 * Return coredata type
	 * @return coredata type
	 */
	public String getCoreDataType() {
		return this.coreDataType;
	}

	/**
	 * Set coredata type
	 * @param p_sCoreDataType coredata type
	 */
	public void setCoreDataType(String p_sCoreDataType) {
		this.coreDataType = p_sCoreDataType;
	}

	/**
	 * Is binded in viewmodel
	 * @return binded in viewmodel
	 */
	public Boolean getVmBinded() {
		return this.vmBinded;
	}

	/**
	 * Set if binded in viewmodel
	 * @param p_bVmBinded binded in viewmodel
	 */
	public void setVmBinded(Boolean p_bVmBinded) {
		this.vmBinded = p_bVmBinded;
	}
}
