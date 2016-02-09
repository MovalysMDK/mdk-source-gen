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
package com.a2a.adjava.languages.ios.xmodele;

import com.a2a.adjava.xmodele.MDaoImpl;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MEntityInterface;
import com.a2a.adjava.xmodele.MPackage;

/**
 * Dao implementation for IOS
 * @author lmichenaud
 *
 */
public class MIOSDaoImpl extends MDaoImpl {
	
	/**
	 * Constructor
	 * @param p_sName dao name
	 * @param p_sBeanName bean name
	 * @param p_oPackage dao package
	 * @param p_oClass entity
	 * @param p_oInterface entity interface
	 * @param p_sQueryDefinitionFile query definition file
	 */
	public MIOSDaoImpl(String p_sName, String p_sBeanName, MPackage p_oPackage,
			MEntityImpl p_oClass, MEntityInterface p_oInterface,
			String p_sQueryDefinitionFile) {
		super(p_sName, p_sBeanName, p_oPackage, p_oClass, p_oInterface,
				p_sQueryDefinitionFile);
	}
}
