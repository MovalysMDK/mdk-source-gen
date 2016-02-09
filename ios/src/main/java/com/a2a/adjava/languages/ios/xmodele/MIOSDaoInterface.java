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
import com.a2a.adjava.xmodele.MDaoInterface;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MPackage;

/**
 * IOS Dao interface
 * @author lmichenaud
 *
 */
public class MIOSDaoInterface extends MDaoInterface {

	
	/**
	 * Constructor
	 * @param p_sName name for dao interface
	 * @param p_sBeanName bean name
	 * @param p_oPackage package of dao interface
	 * @param p_oMDao dao implementation
	 * @param p_oClass entity impl
	 */
	public MIOSDaoInterface(String p_sName, String p_sBeanName,
			MPackage p_oPackage, MDaoImpl p_oMDao, MEntityImpl p_oClass) {
		super(p_sName, p_sBeanName, p_oPackage, p_oMDao, p_oClass);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.SPackagedElement#addImport(java.lang.String)
	 */
	@Override
	public void addImport(String p_sImport) {
		super.addImport(p_sImport);
	}
}
