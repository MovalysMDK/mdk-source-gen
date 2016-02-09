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
package com.a2a.adjava.uml2xmodele.ui.screens;

import java.io.Serializable;
import java.util.Comparator;

import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.ui.component.MWorkspaceConfig;

/**
 * Comparator for workspace page
 * @author lmichenaud
 *
 */
public class WorkspacePageComparator implements Comparator<MPage>, Serializable {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 6254421982174253832L;

	/**
	 * {@inheritDoc}
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(MPage p_oPage1, MPage p_oPage2) {
		int r_iResult = 0 ;
		// master pages are before detail pages
		if ( p_oPage1.getParameterValue(MWorkspaceConfig.PANELTYPE_PARAMETER).equals(MWorkspaceConfig.MASTER_PANELTYPE) 
			&& p_oPage2.getParameterValue(MWorkspaceConfig.PANELTYPE_PARAMETER).equals(MWorkspaceConfig.DETAIL_PANELTYPE)) {
			r_iResult = -1;
		}
		else
		// details pages are after master pages
		if ( p_oPage1.getParameterValue(MWorkspaceConfig.PANELTYPE_PARAMETER).equals(MWorkspaceConfig.DETAIL_PANELTYPE)
			&& p_oPage2.getParameterValue(MWorkspaceConfig.PANELTYPE_PARAMETER).equals(MWorkspaceConfig.MASTER_PANELTYPE)) {
			r_iResult = 1;
		}
		else {
			// same type, we have to compare columns and section
			int iPage1Column = Integer.parseInt(p_oPage1.getParameterValue(MWorkspaceConfig.COLUMN_PARAMETER));
			int iPage2Column = Integer.parseInt(p_oPage2.getParameterValue(MWorkspaceConfig.COLUMN_PARAMETER));
			
			if ( iPage1Column < iPage2Column ) {
				r_iResult = -1 ;
			}
			else {
				if ( iPage1Column > iPage2Column ) {
					r_iResult = 1 ;
				}
				else {
					int iPage1Section = Integer.parseInt(p_oPage1.getParameterValue(MWorkspaceConfig.SECTION_PARAMETER));
					int iPage2Section = Integer.parseInt(p_oPage2.getParameterValue(MWorkspaceConfig.SECTION_PARAMETER));
					
					if ( iPage1Section < iPage2Section ) {
						r_iResult = -1 ;
					}
					else
					if ( iPage1Section > iPage2Section ) {
						r_iResult = 1 ;
					}
				}
			}
		}
		return r_iResult ;
	}
}
