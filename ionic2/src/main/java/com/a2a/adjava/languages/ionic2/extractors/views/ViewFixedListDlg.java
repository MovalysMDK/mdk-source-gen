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

import java.util.ArrayList;
import java.util.List;

import com.a2a.adjava.languages.ionic2.xmodele.MH5FixedListAttribute;
import org.apache.commons.lang3.StringUtils;

import com.a2a.adjava.languages.ionic2.extractors.ViewExtractor;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.MVisualField;
import com.a2a.adjava.xmodele.ui.view.MVFLocalization;

/**
 * @author lmichenaud
 *
 */
public class ViewFixedListDlg {

	private ViewExtractor viewExtractor;
	
	/**
	 * @param p_oViewExtractor
	 */
	public ViewFixedListDlg( ViewExtractor p_oViewExtractor ) {
		this.viewExtractor = p_oViewExtractor;
	}
	
	/**
	 * @param p_oFixedListVisualField
	 * @param p_oViewModel
	 * @param p_sViewName
	 * @return
	 */
	public MH5FixedListAttribute createMH5AttributeForFixedLists(MVisualField p_oFixedListVisualField,
																 MViewModelImpl p_oViewModel, String p_sViewName ) {
		
		MH5FixedListAttribute r_oMH5Attr = new MH5FixedListAttribute(p_oFixedListVisualField);
		
		// search subviewmodel matching the visual field of the fixedlist
		for( MViewModelImpl oSubVm: p_oViewModel.getSubViewModels()) {
			
			if ( oSubVm.getName().equals(p_oFixedListVisualField.getParameters().get("fixedListVmShortName"))) {
				
				r_oMH5Attr.setDetailPartial(StringUtils.join(
					"views/", p_sViewName, "/", p_sViewName, oSubVm.getName(), ".html" ));
				
				this.computeChildAttributeForFixedList(r_oMH5Attr, oSubVm, p_oFixedListVisualField, p_sViewName);
				break;
			}
		}
		
		return r_oMH5Attr;
	}
	
	/**
	 * @param p_oMH5Attribute attribute of the fixed list
	 * @param p_oFixedListVm viewmodel of fixed list
	 * @param p_oFixedListVisualField visual field of fixedlist
	 * @return
	 */
	private void computeChildAttributeForFixedList( MH5FixedListAttribute p_oMH5Attribute, 
			MViewModelImpl p_oFixedListVm, MVisualField p_oFixedListVisualField, String p_sViewName ) {
		
		// Two lists, one for the visual fields of the item of the fixed list,
		// the other, for the visual fields of the detail form of the fixed list
		List<MVisualField> listVisualFields = new ArrayList<>();
		List<MVisualField> detailVisualFields = new ArrayList<>();
		
		// Loop over visual fields
		for( MVisualField oVisualField : p_oFixedListVm.getVisualFields()) {
			
			// if visual field displayed in the item of the fixed list
			if ( oVisualField.getLocalization().equals(MVFLocalization.LIST)) {
				listVisualFields.add(oVisualField);
			}
			else {
				// visual field displayed in the detail form of the fixed list
				detailVisualFields.add(oVisualField);
			}
		}
		
		// Compute the content of the item of the fixed list
		this.viewExtractor.computeViewAttributes(listVisualFields, p_oFixedListVm, p_oMH5Attribute.getChildAttributes(), p_sViewName);
		
		// Compute the content of the detail form of the fixed list
		this.viewExtractor.computeViewAttributes(detailVisualFields, p_oFixedListVm, p_oMH5Attribute.getDetailAttributes(), p_sViewName);
	}
}
