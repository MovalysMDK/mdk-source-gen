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

import com.a2a.adjava.languages.ionic2.extractors.ViewExtractor;
import com.a2a.adjava.languages.ionic2.xmodele.MH5ComboAttribute;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.MVisualField;
import com.a2a.adjava.xmodele.ui.view.MVFLocalization;

public class ViewComboDlg {

	private ViewExtractor viewExtractor;

	/**
	 * @param p_oViewExtractor
	 */
	public ViewComboDlg(ViewExtractor p_oViewExtractor) {
		this.viewExtractor = p_oViewExtractor;
	}

	/**
	 * @param p_oFixedListVisualField
	 * @param p_oViewModel
	 * @param p_sViewName
	 * @return
	 */
	public MH5ComboAttribute createMH5AttributeForCombo(
			MVisualField p_oComboVisualField, MViewModelImpl p_oViewModel,
			String p_sViewName) {
		MH5ComboAttribute r_oMH5ComboAttribute;

		r_oMH5ComboAttribute = new MH5ComboAttribute(p_oComboVisualField);
		for (MViewModelImpl extVm : p_oViewModel.getExternalViewModels()) {
			if (extVm.getFullName().equals(
					p_oComboVisualField.getParameters().get("comboVm"))) {

				if (extVm.getIdentifier() != null
						&& extVm.getIdentifier().getElemOfTypeAttribute() != null
						&& extVm.getIdentifier().getElemOfTypeAttribute()
								.get(0) != null) {
					r_oMH5ComboAttribute.setValueAttribute(extVm
							.getIdentifier().getElemOfTypeAttribute().get(0)
							.getName());
					
					this.computeChildAttributesForCombo(r_oMH5ComboAttribute, extVm, p_oComboVisualField, p_sViewName);
					
					r_oMH5ComboAttribute.setAttributeFieldName(p_oComboVisualField.getViewModelProperty());
				}
				break;
			}
		}

		return r_oMH5ComboAttribute;
	}

	/**
	 * @param p_oMH5Attribute
	 *            attribute of the fixed list
	 * @param p_oCombo
	 *            viewmodel of fixed list
	 * @param p_oComboVisualField
	 *            visual field of combo
	 * @return
	 */
	private void computeChildAttributesForCombo(
			MH5ComboAttribute p_oMH5Attribute, MViewModelImpl p_oComboVm,
			MVisualField p_oComboVisualField, String p_sViewName) {

		// Two lists, one for the visual fields of the selection,
		// the other, for the visual fields of items in the list
		List<MVisualField> listVisualFields = new ArrayList<>();
		List<MVisualField> selectionVisualFields = new ArrayList<>();

		// Loop over visual fields
		for (MVisualField oVisualField : p_oComboVm.getVisualFields()) {

			// if visual field displayed in the list of the combo
			if (oVisualField.getLocalization().equals(MVFLocalization.LIST)) {
				listVisualFields.add(oVisualField);
			} else {
				// visual field displayed in the selection of the combo
				selectionVisualFields.add(oVisualField);
			}
		}

		// Compute the content of the item of the fixed list
		this.viewExtractor.computeViewAttributes(listVisualFields,
				p_oComboVm, p_oMH5Attribute.getChildAttributes(),
				p_sViewName);

		// Compute the content of the detail form of the fixed list
		this.viewExtractor.computeViewAttributes(selectionVisualFields,
				p_oComboVm, p_oMH5Attribute.getDisplayedAttributesInSelection(),
				p_sViewName);
	}
}
