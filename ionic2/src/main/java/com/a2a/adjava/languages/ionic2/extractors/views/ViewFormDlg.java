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

import com.a2a.adjava.languages.ionic2.xmodele.MH5PanelView;
import com.a2a.adjava.xmodele.MAction;
import com.a2a.adjava.xmodele.MActionType;
import com.a2a.adjava.xmodele.MPage;

public class ViewFormDlg {

	/**
	 * Create panel view for form
	 * @param p_oPage page
	 * @return panel view
	 */
	public MH5PanelView createPanelViewForForm(MPage p_oPage) {
		//In case of a basic panel
		MH5PanelView r_oNewView = new MH5PanelView();
		
		// Define save action if exists
		MAction oAction = p_oPage.getActionOfType(MActionType.SAVEDETAIL);
		if  ( oAction != null ) {
			r_oNewView.setSaveActionName(oAction.getName());
		}
		
		// Define delete action if exists
		oAction = p_oPage.getActionOfType(MActionType.DELETEDETAIL);
		if  ( oAction != null ) {
			r_oNewView.setDeleteActionName(oAction.getName());
		}
		return r_oNewView;
	}
}
