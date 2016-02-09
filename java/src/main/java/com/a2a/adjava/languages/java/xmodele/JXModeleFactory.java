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
package com.a2a.adjava.languages.java.xmodele;

import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.xmodele.MAttribute;
import com.a2a.adjava.xmodele.XModeleFactory;

/**
 * <p>
 * XModeleFactory for java generation
 * </p>
 * 
 * <p>
 * Copyright (c) 2011
 * <p>
 * Company: Adeuza
 * 
 * @author lmichenaud
 * 
 */

public class JXModeleFactory extends XModeleFactory {

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.a2a.adjava.xmodele.XModeleFactory#createMAttribute(java.lang.String, java.lang.String, boolean,
	 *      com.a2a.adjava.project.ITypeDescription, java.lang.String)
	 */
	@Override
	public MAttribute createMAttribute(String p_sName, String p_sVisibility, boolean p_bIdentifier, 
			boolean p_bDerived, boolean p_bTranscient, ITypeDescription p_oTypeDescription,
			String p_sDocumentation) {
		return new MJAttribute(p_sName, p_sVisibility, p_bIdentifier, p_bDerived, p_bTranscient, p_oTypeDescription, p_sDocumentation);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.a2a.adjava.xmodele.XModeleFactory#createMAttribute(java.lang.String, java.lang.String, boolean,
	 *      com.a2a.adjava.project.ITypeDescription, java.lang.String, java.lang.String, boolean, int, int, int, boolean, boolean, java.lang.String, java.lang.String)
	 */
	@Override
	public MAttribute createMAttribute(String p_sName, String p_sVisibility, boolean p_bIdentifier, boolean p_bDerived, boolean p_bTranscient, ITypeDescription p_oTypeDescription,
			String p_sInitialisation, String p_sDefaultValue, boolean p_bIsMandory, int p_iLength, int p_iPrecision, int p_iScale, boolean p_bHasSequence, boolean p_bUnique,
			String p_sUniqueKey, String p_sDocumentation) {
		return new MJAttribute(p_sName, p_sVisibility, p_bIdentifier, p_bDerived, p_bTranscient, p_oTypeDescription, p_sInitialisation, p_sDefaultValue, p_bIsMandory, p_iLength, p_iPrecision,
				p_iScale, p_bHasSequence, p_bUnique, p_sUniqueKey, p_sDocumentation);
	}
}
