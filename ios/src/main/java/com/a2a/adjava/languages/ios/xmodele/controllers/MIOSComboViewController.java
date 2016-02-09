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
package com.a2a.adjava.languages.ios.xmodele.controllers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;

/**
 * Fixed view controller
 *
 */
@XmlRootElement(name="controller")
@XmlAccessorType(XmlAccessType.FIELD)
public class MIOSComboViewController extends MIOSViewController {
	
	/**
	 * Class name of the selected cell
	 */
	@XmlElement
	private String selectedItemCellClassName ; 
	
	/**
	 * Class name of a cell
	 */
	@XmlElement
	private String itemCellClassName ;
	
	/**
	 * is a item selected?
	 */
	@XmlElement
	private boolean isSelectedItem ;
	
	/**
	 * Viewmodel of item
	 */
	@XmlElement
	private String itemViewModel ;
	
	/**
	 * Identifier of item
	 */
	@XmlElement
	private String itemIdentifier ;
	

	/**
	 * Constructor
	 */
	public MIOSComboViewController() {
		this.setControllerType(MIOSControllerType.COMBOVIEW);
	}

	/**
	 * className of the cell of the combo
	 * @return className of cell
	 */
	public String getItemCellClassName() {
		return this.itemCellClassName;
	}

	/**
	 * Set class name of cell of the combo
	 * @param p_sCellClassName class name of cell
	 */
	public void setItemCellClassName(String p_sCellClassName) {
		this.itemCellClassName = p_sCellClassName;
	}
	
	/**
	 * className of the cell of the selected item
	 * @return className of cell
	 */
	public String getSelectedItemCellClassName() {
		return selectedItemCellClassName;
	}

	/**
	 * Set class name of cell of the selected item
	 * @param p_sSelectedItemCellClassName class name of cell
	 */
	public void setSelectedItemCellClassName(String p_sSelectedItemCellClassName) {
		this.selectedItemCellClassName = p_sSelectedItemCellClassName;
	}
	
	/**
	 * Get viewmodel name of item
	 * @return viewmodel name of item
	 */
	public String getItemViewModel() {
		return this.itemViewModel;
	}

	/**
	 * Set viewmodel name of item
	 * @param p_sItemViewModel viewmodel name of item
	 */
	public void setItemViewModel(String p_sItemViewModel) {
		this.itemViewModel = p_sItemViewModel;
	}
	

	/**
	 * Get identifier of item
	 * @return identifier of item
	 */
	public String getItemIdentifier() {
		return this.itemIdentifier;
	}

	/**
	 * Set identifier of item
	 * @param p_sItemIdentifier identifier of item
	 */
	public void setItemIdentifier(String p_sItemIdentifier) {
		this.itemIdentifier = p_sItemIdentifier;
	}

	/**
	 * Get isSelectedItem
	 * @return true if this Combo>ViewController correspond to the selected Item
	 */
	public boolean isSelectedItem() {
		return this.isSelectedItem;		
	}
	
	
	/**
	 * Set isSelectedItem
	 * @param p_bIsSelectedItem the new value of isSelectedItem
	 */
	public void setIsSelectedItem(boolean p_bIsSelectedItem) {
		this.isSelectedItem = p_bIsSelectedItem;		
	}
	
	/**
	 * Clones the controller with a new name
	 * @param p_sNewName the name of the clone
	 * @return the new cloned controller
	 */
	public MIOSComboViewController cloneWithNewName(String p_sNewName)
	{
		MIOSComboViewController r_oNewMIOSComboViewController = new MIOSComboViewController();
		r_oNewMIOSComboViewController.setControllerType(this.getControllerType());
		r_oNewMIOSComboViewController.setCustomClass(this.getCustomClass());
		r_oNewMIOSComboViewController.setFormName(p_sNewName);
		r_oNewMIOSComboViewController.setId(Integer.toString(Math.abs(StringUtils.join(VIEWCONTROLLER_PREFIX, p_sNewName).hashCode())));
		r_oNewMIOSComboViewController.setItemCellClassName(this.itemCellClassName);
		r_oNewMIOSComboViewController.setItemIdentifier(this.itemIdentifier);
		r_oNewMIOSComboViewController.setItemViewModel(this.itemViewModel);
		r_oNewMIOSComboViewController.setName(p_sNewName);
		r_oNewMIOSComboViewController.setNavigationItemId(this.getNavigationItemId());
		r_oNewMIOSComboViewController.clearAndAddSection(this.getSections());
		r_oNewMIOSComboViewController.setSelectedItemCellClassName(this.selectedItemCellClassName);
		r_oNewMIOSComboViewController.setViewId(this.getViewId());
		r_oNewMIOSComboViewController.setViewModel(this.getViewModel());
		r_oNewMIOSComboViewController.setIsSelectedItem(this.isSelectedItem);
		
		if (StringUtils.join(VIEWCONTROLLER_PREFIX, p_sNewName).hashCode() > Integer.MIN_VALUE) {
			r_oNewMIOSComboViewController.setId(Integer.toString(Math.abs(StringUtils.join(VIEWCONTROLLER_PREFIX, p_sNewName).hashCode())));
		} else {
			r_oNewMIOSComboViewController.setId("0");
		}
			
		
		return r_oNewMIOSComboViewController;
	}


}
