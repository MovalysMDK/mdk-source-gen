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

import com.a2a.adjava.types.ITypeDescription;

public class STypedElement extends SPackagedElement {

	private ITypeDescription typeDescription;
	
	/**
	 * @param p_oUmlClass
	 * @param p_sTableName
	 */
	public STypedElement(String p_sType, String p_sName, MPackage p_oPackage, ITypeDescription p_oTypeDescription ) {
		super(p_sType, null, p_sName, p_oPackage);
		this.typeDescription = p_oTypeDescription ;
		this.typeDescription.setName(this.getFullName());
	}
		
	/**
	 * @return
	 */
	public ITypeDescription getTypeDescription() {
		return typeDescription;
	}
}
