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
package com.a2a.adjava.languages.ios.extractors;

import org.apache.commons.lang3.StringUtils;

import com.a2a.adjava.languages.ios.xmodele.MIOSMultiXibContainer;
import com.a2a.adjava.languages.ios.xmodele.MIOSXibContainer;
import com.a2a.adjava.languages.ios.xmodele.MIOSXibFixedListContainer;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOS2DListViewController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSSectionType;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSSection;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSXibType;
import com.a2a.adjava.uml2xmodele.extractors.viewmodel.VMNamingHelper;
import com.a2a.adjava.xmodele.MDialog;
import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.MViewModelImpl;

public class IOSVMNamingHelper {

	/**
	 * Define the suffix of the name of the item view model in the fixed list 
	 */
	public static final String ITEM_SUFFIX = "Item" ;
	/**
	 * Define the suffix of the name of the fixed list view model 
	 */
	public static final String LIST_SUFFIX = "List" ;
	/**
	 * Define the suffix of the name of the fixed list view model 
	 */
	public static final String SELECTED_SUFFIX = "Selected" ;

	/**
	 * Define the suffix of the name of the combo selected item
	 */
	public static final String COMBO_XIB_SELECTED_ITEM_SUFFIX = "SelectedItem" ;

	/**
	 * Define the suffix of the name of the combo item
	 */
	public static final String COMBO_ITEM_SUFFIX = "Item" ;

	/**
	 * Define the suffix of the name of the combo item
	 */
	public static final String COMBO_DELEGATE_SUFFIX = "BindingDelegate" ;

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
	private static final IOSVMNamingHelper instance = new IOSVMNamingHelper();

	/**
	 * Return singleton instance
	 * 
	 * @return singleton instance
	 */
	public static IOSVMNamingHelper getInstance() {
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
			r_sName = StringUtils.join(  p_oPage.getUmlName() ,p_oViewModel.getUmlName(), ITEM_SUFFIX ,"Cell" ); 
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
			if(p_oPage instanceof MDialog) {
				r_sName = StringUtils.join(  p_oPage.getViewModelImpl().getUmlName(), p_oViewModel.getUmlName(), "Item" ); 
			}
			else {
				r_sName = StringUtils.join(  p_oPage.getUmlName(), p_oViewModel.getUmlName(), "Item" ); 
			}
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
			r_sName = StringUtils.join( p_oViewModel.getName() , LIST_SUFFIX);
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
	 * @param p_oXibContainer the xib container of the fixed list
	 * @return the component name of the fixed list
	 */
	public String computeViewNameOfFixedList(MIOSXibFixedListContainer p_oXibContainer){
		String r_sName = null ;	
		if ( p_oXibContainer != null ){
			r_sName = p_oXibContainer.getViewFixedListName();
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
			r_sName = StringUtils.join(	p_oPage.getName() , p_oViewModel.getUmlName(), ITEM_SUFFIX , "Detail" );
		}
		return r_sName;
	}


	/**
	 * Define the XIB container name of the item in a fixed list
	 * @param p_oController controller of the fixed list
	 * @return  the XIB container name of the item in a fixed list
	 */
	public String computeXibNameOfExpandableListSection(MIOS2DListViewController p_oController){
		String r_sName = null ;	
		if ( p_oController != null ){
			r_sName = StringUtils.join(p_oController.getSectionFormName()); 
		}
		return r_sName;
	}

	/**
	 * Define the component name of the fixed list
	 * @param p_oController controller of the fixed list
	 * @param p_oSection The section that is used to build the section name
	 * @return the component name of the fixed list
	 */
	public String computeViewNameOfExpandableListSection(MIOS2DListViewController p_oController, MIOSSection p_oSection){
		String r_sName = null ;	
		if ( p_oController != null){
			if(p_oSection.getSectionType().equals(MIOSSectionType.LISTCELL)) {
				r_sName = StringUtils.join( p_oController.getFormName(),"Cell");
			}
			else if(p_oSection.getSectionType().equals(MIOSSectionType.LISTSECTION)) {
				r_sName = StringUtils.join( p_oController.getSectionFormName(),"View");
			}
			else  if(p_oSection.getSectionType().equals(MIOSSectionType.LISTHEADER)) {
				r_sName = StringUtils.join( p_oController.getSectionFormName(),"Header");
			}
			else {
				r_sName = StringUtils.join( p_oController.getSectionFormName(),"");
			}

		}
		return r_sName;
	}


	/**
	 * Create the general name of all component for the picker list. Always the same for all component
	 * @param p_oParentVM parent view model of the combo
	 * @param p_oCurrentVm view model of the combo
	 * @return the name of the combo
	 */
	public String createGeneralComboName(MViewModelImpl p_oParentVM , MViewModelImpl p_oCurrentVm){
		String sComboName = "";

		if(p_oCurrentVm == null)
		{
			return sComboName;
		}
		if(p_oParentVM !=null)
		{
			if(p_oParentVM.getFirstParent()!= null)
			{
				sComboName = StringUtils.join(p_oParentVM.getFirstParent().getUmlName()+p_oCurrentVm.getUmlName());
			}else
			{
				sComboName = StringUtils.join(p_oParentVM.getUmlName(),p_oCurrentVm.getUmlName());
			}
		}else
		{
			sComboName = p_oCurrentVm.getUmlName();
		}
		return sComboName;

	}

	/**
	 * Define the XIB container name of the selected item in a combo (picker list)
	 * @param p_oPage page containing the combo
	 * @param p_oViewModel view model of the combo
	 * @return  the XIB container name of the selected item in a combo
	 */
	public String computeXibNameOfSelectedItemForCombo(MViewModelImpl p_oViewModel){
		String r_sName = null ;	
		if ( p_oViewModel != null ){
			r_sName = StringUtils.join(this.createGeneralComboName(p_oViewModel.getParent(), p_oViewModel ),SELECTED_SUFFIX, COMBO_ITEM_SUFFIX);
		}
		return r_sName;
	}

	/**
	 * Define the XIB container name of the item combo (picker list)
	 * @param p_oPage page containing the combo
	 * @param p_oViewModel view model of the combo
	 * @return  the XIB container name of the item combo
	 */
	public String computeXibNameOfListItemForCombo( MViewModelImpl p_oViewModel){
		String r_sName = null ;	
		if ( p_oViewModel != null ){
			r_sName = StringUtils.join(this.createGeneralComboName(p_oViewModel.getParent(), p_oViewModel ),LIST_SUFFIX, COMBO_ITEM_SUFFIX);
		}
		return r_sName;
	}
	/**
	 * Define the delegate name of the Picker list
	 * @param p_oMultiXibContainer the multi-xib container of the picker list
	 * @return the delegate name of the picker list
	 */
	public String computeDelegateNameOfPickerList(MIOSXibContainer p_oXibContainer){
		String r_sName = null ;	
		if ( p_oXibContainer != null ){
			r_sName = p_oXibContainer.getName();
			if(p_oXibContainer.getXibType().equals(MIOSXibType.COMBOVIEWLISTITEM)) {
				r_sName = StringUtils.join(p_oXibContainer.getName(), COMBO_DELEGATE_SUFFIX);
			}
			else {
				r_sName = StringUtils.join(p_oXibContainer.getName(), COMBO_DELEGATE_SUFFIX);
			}
		}
		return r_sName;
	}

	/**
	 * Define the XIB container name of the item combo (picker list)
	 * @param p_oPage page containing the combo
	 * @param p_oViewModel view model of the combo
	 * @param p_bIsListItem indicates if the name is for the list item
	 * @return  the XIB container name of the item combo
	 */
	public String computeDelegateNameForCombo( MViewModelImpl p_oViewModel, boolean p_bIsListItem){
		String r_sName = null ;	
		if ( p_oViewModel != null ){
			if(p_bIsListItem) {
				r_sName = StringUtils.join(p_oViewModel.getFirstParent().getUmlName(), p_oViewModel.getUmlName(), LIST_SUFFIX, ITEM_SUFFIX, COMBO_DELEGATE_SUFFIX);
			}
			else {
				r_sName = StringUtils.join(p_oViewModel.getFirstParent().getUmlName(), p_oViewModel.getUmlName(), SELECTED_SUFFIX, ITEM_SUFFIX, COMBO_DELEGATE_SUFFIX);
			}
		}
		return r_sName;
	}


	/**
	 * Define the detail controller name to see the complete item in a picker list
	 * @param p_oGeneralName name of the master controller
	 * @return the detail controller name in a picker list
	 */
	public String computeDetailControllerNameOfItemCombo(String p_oGeneralName){
		return StringUtils.join(p_oGeneralName , COMBO_VIEW_MODEL_LIST_SUFFIX , "ItemController" );
	}

	/**
	 * Define the detail controller name to see the complete item in a picker list
	 * @param p_oGeneralName name of the master controller
	 * @return the detail controller name in a picker list
	 */
	public String computeDetailControllerNameOfSelectedItemCombo(String p_oGeneralName){
		return StringUtils.join( p_oGeneralName , COMBO_VIEW_MODEL_LIST_SUFFIX , "SelectedItemController" );
	}

	/**
	 * Define the detail controller name to see the complete item in a picker list
	 * @param p_oViewModel view model of the picker list
	 * @param p_sParentBindingPath binding path of the combo
	 * @return the detail controller name in a picker list
	 */
	public String computeCustomParameterNameForCombo( MViewModelImpl p_oViewModel, String p_sParentBindingPath){
		String r_sName = null ;	
		String sParentBindingPath = p_sParentBindingPath;
		if ( p_oViewModel != null ){
			if(sParentBindingPath != null) {
				sParentBindingPath = StringUtils.join(sParentBindingPath, ".");
			}
			else {
				sParentBindingPath = "";
			}
			r_sName = StringUtils.join(sParentBindingPath, COMBO_ITEMS_PREFIX, p_oViewModel.getName());
		}
		return r_sName;
	}

}
