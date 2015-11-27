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
package com.a2a.adjava.languages.w8.xmodele;

import java.util.List;

import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.xmodele.MDaoMethodSignature;

public class MW8DaoMethodSignature extends MDaoMethodSignature {

	public MW8DaoMethodSignature(String p_sName, String p_sVisibility, String p_sType, ITypeDescription p_oReturnedType,
			List<String> p_listReturnedProperties, List<String> p_listNeedImports, boolean p_bByValue) {
		
		super(p_sName, p_sVisibility, p_sType, p_oReturnedType, p_listReturnedProperties, p_listNeedImports, p_bByValue);

		// Set to upper case the first letter of the method name
		p_sName = Character.toString(p_sName.charAt(0)).toUpperCase()+p_sName.substring(1);
		this.setName(p_sName);
	}

}
