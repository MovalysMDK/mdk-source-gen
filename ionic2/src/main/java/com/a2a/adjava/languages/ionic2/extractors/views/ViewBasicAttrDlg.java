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
package com.a2a.adjava.languages.ionic2.extractors.views;

import com.a2a.adjava.languages.ionic2.xmodele.MH5Attribute;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.MVisualField;

public class ViewBasicAttrDlg {
	
	/**
	 * @param p_oFixedListVisualField
	 * @param p_oViewModel
	 * @param p_sViewName
	 * @return
	 */
	public MH5Attribute createMH5AttributeForBasicAttribute( MVisualField p_oVisualField, 
			MViewModelImpl p_oViewModel, String p_sViewName ) {
		return new MH5Attribute(p_oVisualField);
	}
}
