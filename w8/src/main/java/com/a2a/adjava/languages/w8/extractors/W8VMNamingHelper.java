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
package com.a2a.adjava.languages.w8.extractors;

import org.apache.commons.lang3.StringUtils;

import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.MViewModelImpl;

/**
 * View model naming helper class
 */
public class W8VMNamingHelper {

	/**
	 * Define the suffix of the name of the item view model in the fixed list 
	 */
	public static final String FIXED_LIST_VIEW_MODEL_ITEM_SUFFIX = "Item" ;
	/**
	 * Define the suffix of the name of the fixed list view model 
	 */
	public static final String FIXED_LIST_VIEW_MODEL_LIST_SUFFIX = "List" ;

	/**
	 * Define the suffix of the name of the combo selected item
	 */
	public static final String COMBO_XIB_SELECTED_ITEM_SUFFIX = "SelectedItem" ;
	
	/**
	 * Define the suffix of the name of the combo item
	 */
	public static final String COMBO_ITEM_SUFFIX = "Item" ;
	
	/**
	 * Define the prefix of the name of list combo items
	 */
	public static final String COMBO_ITEMS_PREFIX = "lst" ;

	
	/**
	 * Define the suffix of the name of the combo view model 
	 */
	public static final String COMBO_VIEW_MODEL_LIST_SUFFIX = "Combo" ;
	
	/**
	 * Singleton instance
	 */
	private static final W8VMNamingHelper instance = new W8VMNamingHelper();

	/**
	 * Return singleton instance
	 * 
	 * @return singleton instance
	 */
	public static W8VMNamingHelper getInstance() {
		return instance;
	}

	/**
	 * Define the view model name of the item in a fixed list
	 * @param p_oPage page containing the fixed list
	 * @param p_oViewModel view model of the fixed list
	 * @return the view model name of the item in a fixed list
	 */
	public String computeViewModelNameOfFixedListItem(MPage p_oPage , MViewModelImpl p_oViewModel){
		String r_sName = null ;	
		if ( p_oViewModel != null && p_oPage != null  ){
			r_sName = StringUtils.join(  p_oPage.getUmlName() ,p_oViewModel.getUmlName(), FIXED_LIST_VIEW_MODEL_ITEM_SUFFIX ,"Cell" ); 
		}
		return r_sName;
	}
	
	
	/**
	 * Define the new view model name of the item in a combo list
	 * @param p_oPage page containing the combo
	 * @param p_oViewModel view model of the combo
	 * @return the view model name of the item in a combo
	 */
	public String computeItemViewModelNameOfCombo(MPage p_oPage , MViewModelImpl p_oViewModel){
		String r_sName = null ;	
		if ( p_oViewModel != null && p_oPage != null  ){
			r_sName = StringUtils.join(  p_oPage.getUmlName(), p_oViewModel.getUmlName(), "Item" ); 
		}
		return r_sName;
	}
	
	/**
	 * Define the view model name of the combo
	 * @param p_oViewModel view model of the combo
	 * @return the view model name of the combo
	 */
	public String computeViewModelNameOfCombo(MViewModelImpl p_oViewModel){
		String r_sName = null ;	
		if ( p_oViewModel != null ){
			r_sName = StringUtils.join( p_oViewModel.getName() , COMBO_VIEW_MODEL_LIST_SUFFIX );
		}
		return r_sName;
	}
	
	/**
	 * Define the view model name of the fixed list
	 * @param p_oViewModel view model of the fixed list
	 * @return the view model name of the fixed list
	 */
	public String computeViewModelNameOfFixedList(MViewModelImpl p_oViewModel){
		String r_sName = null ;	
		if ( p_oViewModel != null ){
			r_sName = StringUtils.join( p_oViewModel.getName() , FIXED_LIST_VIEW_MODEL_LIST_SUFFIX );
		}
		return r_sName;
	}

	/**
	 * Define the XIB container name of the item in a fixed list
	 * @param p_oViewModel view model of the fixed list
	 * @return  the XIB container name of the item in a fixed list
	 */
	public String computeXibNameOfFixedListItem(MViewModelImpl p_oViewModel){
		String r_sName = null ;	
		if ( p_oViewModel != null && p_oViewModel.getFirstParent() != null ){
			r_sName = StringUtils.join(  p_oViewModel.getFirstParent().getUmlName() ,p_oViewModel.getUmlName(), "ItemCell" ); 
		}
		return r_sName;
	}
	/**
	 * Define the component name of the fixed list
	 * @param p_oViewModel view model of the fixed list
	 * @return the component name of the fixed list
	 */
	public String computeViewNameOfFixedList(MViewModelImpl p_oViewModel){
		String r_sName = null ;	
		if ( p_oViewModel != null && p_oViewModel.getFirstParent() != null ){
			r_sName = StringUtils.join( p_oViewModel.getFirstParent().getUmlName() , p_oViewModel.getUmlName() ,"View" );
		}
		return r_sName;
	}
	
	/**
	 * Define the component name of the combo
	 * @param p_oViewModel view model of the combo
	 * @return the component name of the combo
	 */
	public String computeViewNameOfCombo(MViewModelImpl p_oViewModel){
		String r_sName = null ;	
		if ( p_oViewModel != null && p_oViewModel.getFirstParent() != null ){
			r_sName = StringUtils.join( p_oViewModel.getFirstParent().getUmlName() , "View" );
		}
		return r_sName;
	}
	/**
	 * Define the detail controller name to see the complete item in a fixed list
	 * @param p_oPage page containing the fixed list
	 * @param p_oViewModel view model of the fixed list
	 * @return the detail controller name in a fixed list
	 */
	public String computeDetailControllerNameOfFixedList(MPage p_oPage , MViewModelImpl p_oViewModel){
		String r_sName = null ;	
		if ( p_oViewModel != null && p_oPage != null ){
			r_sName = StringUtils.join(	p_oPage.getName() , p_oViewModel.getUmlName(), FIXED_LIST_VIEW_MODEL_ITEM_SUFFIX , "Detail" );
		}
		return r_sName;
	}

	/**
	 * Create the general name of all component for the picker list. Always the same for all component
	 * @param parentVM ViewModel hosting the picker list 
	 * @param currentVm ViewModel of the picker list
	 * @return Generated name for the picker list's viewmodel
	 */
	public String createGeneralComboName(MViewModelImpl parentVM , MViewModelImpl currentVm){
		String comboName = "";
		
		if(currentVm == null)
		{
			return comboName;
		}
		if(parentVM !=null)
		{
			if(parentVM.getFirstParent()!= null)
			{
				comboName = StringUtils.join(parentVM.getFirstParent().getUmlName()+currentVm.getUmlName());
			}else
			{
				comboName = StringUtils.join(parentVM.getUmlName(),currentVm.getUmlName());
			}
		}else
		{
			comboName = currentVm.getUmlName();
		}
		return comboName;
		
	}

	/**
	 * Define the XIB container name of the selected item in a combo (picker list)
	 * @param p_oViewModel view model of the combo
	 * @return  the XIB container name of the selected item in a combo
	 */
	public String computeXibNameOfSelectedItemForCombo(MViewModelImpl p_oViewModel){
		String r_sName = null ;	
		if ( p_oViewModel != null ){
			r_sName = StringUtils.join(this.createGeneralComboName(p_oViewModel.getParent(), p_oViewModel ),COMBO_XIB_SELECTED_ITEM_SUFFIX);
		}
		return r_sName;
	}
	
	/**
	 * Define the XIB container name of the item combo (picker list)
	 * @param p_oViewModel view model of the combo
	 * @return  the XIB container name of the item combo
	 */
	public String computeXibNameOfItemForCombo( MViewModelImpl p_oViewModel){
		String r_sName = null ;	
		if ( p_oViewModel != null ){
			r_sName = StringUtils.join(this.createGeneralComboName(p_oViewModel.getParent(), p_oViewModel ),COMBO_ITEM_SUFFIX);
		}
		return r_sName;
	}
	
	/**
	 * Define the detail controller name to see the complete item in a picker list
	 * @param p_oGeneralName name of the picker list
	 * @return the detail controller name in a picker list
	 */
	public String computeDetailControllerNameOfItemCombo(String p_oGeneralName){
		return StringUtils.join(p_oGeneralName , COMBO_VIEW_MODEL_LIST_SUFFIX , "ItemController" );
	}
	
	/**
	 * Define the detail controller name to see the complete item in a picker list
	 * @param p_oGeneralName name of the picker list
	 * @return the detail controller name in a picker list
	 */
	public String computeDetailControllerNameOfSelectedItemCombo(String p_oGeneralName){
		return StringUtils.join( p_oGeneralName , COMBO_VIEW_MODEL_LIST_SUFFIX , "SelectedItemController" );
	}
	
	/**
	 * Define the detail controller name to see the complete item in a picker list
	 * @param p_oViewModel view model of the picker list
	 * @param p_sParentBindingPath Binding path of the parent
	 * @return the detail controller name in a picker list
	 */
	public String computeCustomParameterNameForCombo( MViewModelImpl p_oViewModel, String p_sParentBindingPath){
		String r_sName = null ;	
		String r_sParentBindingPath = p_sParentBindingPath;
		if ( p_oViewModel != null ){
			if(r_sParentBindingPath != null) {
				r_sParentBindingPath = r_sParentBindingPath + ".";
			}
			else {
				r_sParentBindingPath = "";
			}
			r_sName = StringUtils.join(r_sParentBindingPath, COMBO_ITEMS_PREFIX, p_oViewModel.getName());
		}
		return r_sName;
	}
	
}
