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
package com.a2a.adjava.languages.html5.extractors.views;

import com.a2a.adjava.languages.html5.extractors.ViewExtractor;
import com.a2a.adjava.languages.html5.xmodele.MH5Attribute;
import com.a2a.adjava.languages.html5.xmodele.MH5Dictionary;
import com.a2a.adjava.languages.html5.xmodele.MH5ListAttribute;
import com.a2a.adjava.languages.html5.xmodele.MH5ListPanelView;
import com.a2a.adjava.languages.html5.xmodele.MH5PanelView;
import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.MVisualField;
import com.a2a.adjava.xmodele.ui.navigation.MNavigation;
import com.a2a.adjava.xmodele.ui.navigation.MNavigationType;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;

/**
 * @author lmichenaud
 *
 */
public class ViewListDlg {

	/**
	 * View Extractor
	 */
	private ViewExtractor viewExtractor;
	
	/**
	 * @param p_oViewExtractor
	 */
	public ViewListDlg( ViewExtractor p_oViewExtractor ) {
		this.viewExtractor = p_oViewExtractor;
	}
	
	/**
	 * Create panel view for list
	 * @param p_oPage page
	 * @return
	 */
	public MH5PanelView createPanelViewForList(MPage p_oPage, MH5Dictionary p_oDictionary, String p_sViewName) {
		
		MH5ListPanelView r_oNewView = new MH5ListPanelView();
		
		r_oNewView.setCanAdd(p_oPage.hasPanelOperation("create"));
		
		MViewModelImpl itemVm = p_oPage.getViewModelImpl().getSubViewModels().get(0);
		
		// Handle navigation to detail
		for(MNavigation oNav : p_oDictionary.getNavigationsFromScreen(p_oPage.getParent())){
			if(oNav.getNavigationType().equals(MNavigationType.NAVIGATION_DETAIL)){
				r_oNewView.setDetailScreenName(oNav.getTarget().getName());
			}
		}
		
		// Define panel list name and list id
		r_oNewView.setPanelListName(p_oPage.getViewModelImpl().getName());
		
		// "List id" is the attribute used to identify each viewmodel item.
		r_oNewView.setListId(itemVm.getIdentifier().getElemOfTypeAttribute().get(0).getName());
		
		// Define list item name
		if ( itemVm.getEntityToUpdate() != null) {
			//TODO: List item name ? cé quoi ?
			r_oNewView.setListItemName(itemVm.getEntityToUpdate().getName());
		}
		
		return r_oNewView;
	}

	public MH5Attribute createMH5AttributeForListAttribute(
			MVisualField p_oVisualField, MViewModelImpl p_oViewModel,
			String p_sViewName) {
		
		MH5ListAttribute r_oMH5ListAttribute = new MH5ListAttribute(p_oVisualField);
		
		MViewModelImpl itemVm = p_oViewModel.getSubViewModels().get(0);
		
		this.viewExtractor.computeViewAttributes(itemVm.getVisualFields(), itemVm, 
				r_oMH5ListAttribute.getChildAttributes(), p_sViewName);
		
		if ( itemVm.getType().equals(ViewModelType.LISTITEM_2) || itemVm.getType().equals(ViewModelType.LISTITEM_3)) {
			itemVm = itemVm.getSubViewModels().get(0);
			
			this.viewExtractor.computeViewAttributes(itemVm.getVisualFields(), itemVm, 
					r_oMH5ListAttribute.getLevel2Attrs(), p_sViewName);
			
			if ( itemVm.getType().equals(ViewModelType.LISTITEM_2)) {
				itemVm = itemVm.getSubViewModels().get(0);
				
				this.viewExtractor.computeViewAttributes(itemVm.getVisualFields(), itemVm, 
						r_oMH5ListAttribute.getLevel3Attrs(), p_sViewName);
			}
		}
		
		return r_oMH5ListAttribute;
	}
}
