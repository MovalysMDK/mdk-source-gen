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
package com.a2a.adjava.languages.ios.xmodele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.a2a.adjava.languages.ios.xmodele.views.MIOSEditableView;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSView;

/**
 * IOS Xib components description
 * @author ftollec
 *
 */
@XmlRootElement(name="xib-container")
@XmlAccessorType(XmlAccessType.FIELD)
public class MIOSXibComboContainer extends MIOSXibContainer{

	/**
	 * Name of the view of the fixed list
	 */
	@XmlElement(name="is-selected-item")
	private boolean isSelectedItem = true;
	
	/**
	 * Name of the view of the fixed list
	 */
	@XmlElement(name="pickerlist-item-name")
	private String pickerItemName = null;
	
	/**
	 * Name of the view of the fixed list
	 */
	@XmlElement(name="pickerlist-selected-item-name")
	private String pickerSelectedItemName = null;
	
	/**
	 * Does nothing
	 */
	public MIOSXibComboContainer() {

	}
	
	public MIOSXibComboContainer(String p_sName, boolean p_oIsSelectedItem) {
		super(p_sName);
		this.isSelectedItem = p_oIsSelectedItem;
	}


	/**
	 * Modify the name of the class cell related to the list xib  
	 * @param new name of the class cell related to the list xib
	 */
	public void setPickerSelectedItemName(String newPickerSelectedItemName) {
		this.pickerSelectedItemName = newPickerSelectedItemName;
	}
	/**
	 * Return the name of class cell related to the list xib
	 * @return name of the class cell related to the list xib
	 */
	public String getPickerSelectedItemName() {
		return pickerSelectedItemName;
	}
	

	/**
	 * Modify the name of the class cell related to the item xib  
	 * @param new name of the class cell related to the item xib
	 */
	public void setPickerItemName(String newPickerItemName) {
		this.pickerItemName = newPickerItemName;
	}
	/**
	 * Return the name of class cell related to the item xib
	 * @return name of the class cell related to the item xib
	 */
	public String getPickerItemName() {
		return pickerItemName;
	}
	
	/**
	 * Modify the name of the class cell related to the item xib  
	 * @param new name of the class cell related to the item xib
	 */
	public void setIsSelectedItem(boolean newSelectedItemValue) {
		this.isSelectedItem = newSelectedItemValue;
	}
	/**
	 * Return the name of class cell related to the item xib
	 * @return name of the class cell related to the item xib
	 */
	public boolean isSelectedItem() {
		return isSelectedItem;
	}

	
	/**
	 * Return a clone of the current MIOSXibComboContainer
	 * @return a clone of the current MIOSXibComboContainer
	 */
	public MIOSXibComboContainer clone(){
		
		MIOSXibComboContainer clonedMIOSXibComboContainer = new MIOSXibComboContainer(this.getName(), this.isSelectedItem);
		
		clonedMIOSXibComboContainer.clearAndAddComponents(this.getComponents());
		clonedMIOSXibComboContainer.setPickerItemName(this.pickerItemName);
		clonedMIOSXibComboContainer.setPickerSelectedItemName(this.pickerSelectedItemName);
		
		return clonedMIOSXibComboContainer;
	}
	
	
	
}


