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
package com.a2a.adjava.xmodele;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.a2a.adjava.AdjavaRuntimeException;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;

/**
 *  About Layout:
 * <ul>
 *      <li>Layout name is composed in three parts separated by double underscore
 *      <li>Prefix is the prefix of the layout name.
 *      <li>Sub prefix is the prefix in the second part of the name of the layout.</li>
 *		<li>Suffix is the third part.
 * </ul>
 * @author lmichenaud
 *         
 */
public final class MLayoutFactory {

	/**
	 * Inner layout types for list
	 * @author lmichenaud
	 * 
	 */
	private static enum InnerListLayoutType {
		/**
		 * Layout type of list item 
		 */
		ITEM,
		
		/**
		 * Layout type for selected item of list
		 */
		SELECTED
	}

	/**
	 * Prefix pour le nom du layout
	 * 
	 */
	private static final String LAYOUT_PREFIX = "g";

	/**
	 * Master layout suffix
	 */
	private static final String MASTER_LAYOUT_SUFFIX = "__master";

	/**
	 * Layout sub-prefix for screen without viewmodel
	 **/
	private static final String NOVMSCREEN_SUBPREFIX = "screen";

	/**
	 * Layout sub-prefix for detail screen
	 **/
	private static final String SCREENDETAIL_SUBPREFIX = "screendetail";

	/**
	 * Layout sub-prefix for list screen
	 **/
	private static final String SCREENLIST1_SUBPREFIX = "screenlist1";

	/**
	 * Layout sub-prefix for list screen
	 **/
	public static final String SCREENLIST2_SUBPREFIX = "screenlist2";

	/**
	 * Layout sub-prefix for list screen
	 **/
	private static final String SCREENLIST3_SUBPREFIX = "screenlist3";

	/**
	 * Layout sub-prefix for detail dialog
	 **/
	private static final String DIALOG_SUBPREFIX = "dialog";
	
	/**
	 * Layout sub-prefix for list item in a spinner
	 **/
	private static final String SPINNERITEM_SUBPREFIX = "spinitem";

	/**
	 * Layout sub-prefix for selected item in a spinner
	 **/
	private static final String SPINNERSEL_SUBPREFIX = "spinsel";

	/**
	 * Layout sub-prefix for list item in a spinner
	 **/
	private static final String MULTISELECTED_ITEM_SUBPREFIX = "mutiitem";

	/**
	 * Layout sub-prefix for selected item in a spinner
	 **/
	private static final String MULTISELECTED_SEL_SUBPREFIX = "multisel";
	/**
	 * Layout sub-prefix for list item in a fixed list
	 **/
	private static final String FIXEDLISTITEM_SUBPREFIX = "flistitem";

	/**
	 * Layout sub-prefix for selected item in a fixed list
	 **/
	private static final String FIXEDLISTSEL_SUBPREFIX = "flistsel";

	/**
	 * Layout sub-prefix for item of list1
	 **/
	private static final String LISTITEM1_SUBPREFIX = "listitem1";

	/**
	 * Layout sub-prefix for item of list2
	 **/
	private static final String LISTITEM2_SUBPREFIX = "listitem2";

	/**
	 * Layout sub-prefix for item of list3
	 **/
	private static final String LISTITEM3_SUBPREFIX = "listitem3";

	/**
	 * Layout title sub-prefix
	 */
	private static final String TITLE_SUBPREFIX = "title";
	
	/**
	 * Separator for layout name
	 */
	private static final String SEPARATOR = "__";

	/**
	 * Sub prefix for tabs
	 */
	private static final String TABS_SUBPREFIX = "tabs";

	/**
	 * Singleton instance
	 */
	private static final MLayoutFactory instance = new MLayoutFactory();

	/**
	 * Constructor
	 */
	private MLayoutFactory() {
		// Empty constructor
	}

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	public static MLayoutFactory getInstance() {
		return instance;
	}

	/**
	 * Create a layout for a detail screen.
	 * @param p_sPanelName panel name
	 * @return MLayout
	 */
	public MLayout createDetailLayoutForScreen(String p_sPanelName) {
		String sLayoutName = StringUtils.join(LAYOUT_PREFIX, p_sPanelName.toLowerCase(Locale.getDefault()), SEPARATOR,
				SCREENDETAIL_SUBPREFIX, MASTER_LAYOUT_SUFFIX);
		return new MLayout(sLayoutName, p_sPanelName, p_sPanelName);
	}
	
	/**
	 * Create a detail layout for screen
	 * @param p_oMPage page
	 * @return detail layout
	 */
	public MLayout createDetailLayoutForScreen(MPage p_oMPage) {
		String sLayoutName = StringUtils.join(LAYOUT_PREFIX, p_oMPage.getUmlName().toLowerCase(Locale.getDefault()), SEPARATOR,
				SCREENDETAIL_SUBPREFIX, MASTER_LAYOUT_SUFFIX);
		String sPanelTitle = null;
		if (p_oMPage.isTitled()) {
			sPanelTitle = StringUtils.join(LAYOUT_PREFIX, p_oMPage.getUmlName().toLowerCase(Locale.getDefault()), SEPARATOR,
					SCREENDETAIL_SUBPREFIX, SEPARATOR, TITLE_SUBPREFIX, MASTER_LAYOUT_SUFFIX);
		}
		return new MLayout(sLayoutName, p_oMPage.getUmlName(), p_oMPage.getUmlName(), sPanelTitle);
	}
	
	/**
	 * Compute layout name for a workspace with tabs
	 * @param p_oScreen screen
	 * @return layout name
	 */
	public String computeLayoutNameForWorkspaceTabs( MScreen p_oScreen ) {
		return StringUtils.join(LAYOUT_PREFIX, p_oScreen.getName().toLowerCase(Locale.getDefault()), SEPARATOR,
				TABS_SUBPREFIX, MASTER_LAYOUT_SUFFIX);
	}

	/**
	 * Create a layout for a dialog.
	 * @param p_sPanelName panel name
	 * @return layout for dialog
	 */
	public MLayout createLayoutForDialog(String p_sPanelName) {

		String sLayoutName = StringUtils.join(LAYOUT_PREFIX, p_sPanelName.toLowerCase(Locale.getDefault()), SEPARATOR,
				DIALOG_SUBPREFIX, MASTER_LAYOUT_SUFFIX);
		
		return new MLayout(sLayoutName, p_sPanelName, p_sPanelName);
	}
	
	/**
	 * Create a layout for a list screen.
	 * @param p_sPanelName panel name
	 * @param p_oVmType vm type
	 * @return layout for list screen
	 */
	public MLayout createListLayoutForScreen(String p_sPanelName, ViewModelType p_oVmType) {

		String sLayoutName = StringUtils.join(LAYOUT_PREFIX, p_sPanelName.toLowerCase(Locale.getDefault()), SEPARATOR,
				getLayoutSubPrefixForList(p_oVmType), MASTER_LAYOUT_SUFFIX);
		
		return new MLayout(sLayoutName, p_sPanelName, p_sPanelName);
	}

	/**
	 * Create a layout for a screen without viewmodel (no autobinding).
	 * @param p_sPanelName panel name
	 * @return layout for noviewmodel screen
	 */
	public MLayout createLayoutForNoVMScreen(String p_sPanelName) {

		String sLayoutName = StringUtils.join(LAYOUT_PREFIX, p_sPanelName.toLowerCase(Locale.getDefault()), SEPARATOR,
				NOVMSCREEN_SUBPREFIX, MASTER_LAYOUT_SUFFIX);
		
		return new MLayout(sLayoutName, "", "");
	}

	/**
	 * Create a layout for a list item of a list(n).
	 * @param p_sPanelName panel name
	 * @param p_oViewModel viewmodel of list item
	 * @return layout
	 */
	public MLayout createItemLayoutForList(String p_sPanelName, MViewModelImpl p_oViewModel) {
		
		String sSubPrefix = null;
		if (ViewModelType.LISTITEM_1.equals(p_oViewModel.getType())) {
			sSubPrefix = LISTITEM1_SUBPREFIX;
		} else if (ViewModelType.LISTITEM_2.equals(p_oViewModel.getType())) {
			sSubPrefix = LISTITEM2_SUBPREFIX;
		} else if (ViewModelType.LISTITEM_3.equals(p_oViewModel.getType())) {
			sSubPrefix = LISTITEM3_SUBPREFIX;
		} else {
			throw new IllegalArgumentException("View model " + p_oViewModel.getName()
					+ " is not a list(n) item viewmodel");
		}

		String sLayoutName = StringUtils.join(LAYOUT_PREFIX, p_sPanelName.toLowerCase(Locale.getDefault()),
				SEPARATOR, sSubPrefix, MASTER_LAYOUT_SUFFIX);
		
		MLayout r_oLayout = new MLayout(sLayoutName, p_sPanelName, p_sPanelName);
		r_oLayout.addParameter("vmtype-itemlayoutforlist", p_oViewModel.getType().toString());
		return r_oLayout ;
	}

	/**
	 * Create a item layout for inner list
	 * @param p_oPage page
	 * @param p_oViewModelImpl viewmodel of list item
	 * @return item layout for inner list
	 */
	public MLayout createItemLayoutForInnerList(MPage p_oPage, MViewModelImpl p_oViewModelImpl) {
		
		String sLayoutName = StringUtils.join(LAYOUT_PREFIX, p_oPage.getUmlName().toLowerCase(Locale.getDefault()), SEPARATOR,
				this.getLayoutSubPrefixByViewModelType(p_oViewModelImpl, InnerListLayoutType.ITEM),
				p_oViewModelImpl.getMasterInterface().getName().toLowerCase(Locale.getDefault()), MASTER_LAYOUT_SUFFIX);

		String sListItemShortName = "lst".concat(p_oViewModelImpl.getMasterInterface().getName());
		MLayout r_oLayout = new MLayout(sLayoutName, sListItemShortName, sListItemShortName) ;
		r_oLayout.addParameter("vmtype-itemlayoutforinnerlist", p_oViewModelImpl.getType().toString());
		return r_oLayout;
	}

	/**
	 * Create a selected item layout for inner list
	 * @param p_oPage page
	 * @param p_oViewModelImpl viewmodel of inner list
	 * @return layout for selected item of inner list
	 */
	public MLayout createSelectedItemLayoutForInnerList(MPage p_oPage, MViewModelImpl p_oViewModelImpl) {
		
		String sLayoutName = StringUtils.join(LAYOUT_PREFIX, p_oPage.getUmlName().toLowerCase(Locale.getDefault()), SEPARATOR,
				this.getLayoutSubPrefixByViewModelType(p_oViewModelImpl, InnerListLayoutType.SELECTED),
				p_oViewModelImpl.getMasterInterface().getName().toLowerCase(Locale.getDefault()), MASTER_LAYOUT_SUFFIX);

		String sListItemShortName = "sel".concat(p_oViewModelImpl.getMasterInterface().getName());
		MLayout r_oLayout = new MLayout(sLayoutName, sListItemShortName, sListItemShortName);
		r_oLayout.addParameter("vmtype-selecteditemlayoutforinnerlist", p_oViewModelImpl.getType().toString());
		return r_oLayout;
	}

	/**
	 * Return layout sub prefix for a viewmodel 
	 * @param p_oViewModel view model
	 * @param p_oInnerListLayoutType layout type of inner list
	 * @return layout sub prefix
	 */
	private String getLayoutSubPrefixByViewModelType(MViewModelImpl p_oViewModel,
			InnerListLayoutType p_oInnerListLayoutType) {

		String r_sSubPrefix = null;
		if (ViewModelType.FIXED_LIST.equals(p_oViewModel.getType())) {
			if (InnerListLayoutType.ITEM.equals(p_oInnerListLayoutType)) {
				r_sSubPrefix = FIXEDLISTITEM_SUBPREFIX;
			} else {
				r_sSubPrefix = FIXEDLISTSEL_SUBPREFIX;
			}
		} else if (ViewModelType.LIST_1__ONE_SELECTED.equals(p_oViewModel.getType())) {
			if (InnerListLayoutType.ITEM.equals(p_oInnerListLayoutType)) {
				r_sSubPrefix = SPINNERITEM_SUBPREFIX;
			} else {
				r_sSubPrefix = SPINNERSEL_SUBPREFIX;
			}
		} else if (ViewModelType.LIST_1__N_SELECTED.equals(p_oViewModel.getType())) {
			// TODO change constants
			if (InnerListLayoutType.ITEM.equals(p_oInnerListLayoutType)) {
				r_sSubPrefix = MULTISELECTED_ITEM_SUBPREFIX;
			} else {
				r_sSubPrefix = MULTISELECTED_SEL_SUBPREFIX;
			}
		} else {
			throw new AdjavaRuntimeException("View model {} is neither a fixedlist list or a spinner list", 
				p_oViewModel.getName());
		}
		return r_sSubPrefix;
	}

	/**
	 * Return layout subprefix for list
	 * @param p_oViewModelType view model type
	 * @return layout subprefix for list
	 */
	private String getLayoutSubPrefixForList(ViewModelType p_oViewModelType) {

		String r_sSubPrefix = null;
		if (ViewModelType.LIST_1.equals(p_oViewModelType)) {
			r_sSubPrefix = SCREENLIST1_SUBPREFIX;
		} else if (ViewModelType.LIST_2.equals(p_oViewModelType)) {
			r_sSubPrefix = SCREENLIST2_SUBPREFIX; 
		} else if (ViewModelType.LIST_3.equals(p_oViewModelType)) {
			r_sSubPrefix = SCREENLIST3_SUBPREFIX;
		} else {
			throw new AdjavaRuntimeException("View model type '{}' is neither a list1, or list2 or list3 viewmodel type.", 
				p_oViewModelType.name());
		}
		return r_sSubPrefix;
	}
}
