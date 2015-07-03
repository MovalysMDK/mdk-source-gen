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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.exec.util.StringUtils;

/**
 * IOS Xib components description
 * @author spacreau
 *
 */
@XmlRootElement(name="xib-container")
@XmlAccessorType(XmlAccessType.FIELD)
public class MIOSXibFixedListContainer extends MIOSXibContainer{

	/**
	 * Name of the view of the fixed list
	 */
	@XmlElement(name="view-fixedlist-name")
	private String viewFixedListName = null ;
	/**
	 * Name of the item of the fixed list
	 */
	@XmlElement(name="cellitem-fixedlist-name")
	private String cellItemFixedListName = null;

	/**
	 * Margin between elements in the right or top or bottom
	 */
	@XmlElement(name="detailscreen-storyboard-name")
	private String detailScreenStorboardName = null;


	/**
	 * Margin between elements in the right or top or bottom
	 */
	@XmlElement(name="detailscreen-viewcontroller-name")
	private String detailScreenViewControllerName = null;

	/**
	 * Name of the view model item of the fixed list
	 */
	@XmlElement(name="viewmodel-item-name")
	private String viewModelItemName = null ;

	/**
	 * Does nothing
	 */
	public MIOSXibFixedListContainer() {

	}

	public MIOSXibFixedListContainer(String p_sName) {
		super(p_sName);
	}

	/**
	 * Modify the name of the view of the fixed list
	 * @param new name of the view of the fixed list
	 */
	public void setViewFixedListName(String viewFixedListName) {
		this.viewFixedListName = viewFixedListName;
	}
	/**
	 * Return the name of the view of the fixed list
	 * @return name of the view of the fixed list
	 */
	public String getViewFixedListName() {
		return viewFixedListName;
	}
	/**
	 * Modify the name of item of the fixed list  
	 * @param new name of the item of the fixed list
	 */
	public void setCellItemFixedListName(String cellItemFixedListName) {
		this.cellItemFixedListName = cellItemFixedListName;
	}
	/**
	 * Return the name of item of the fixed list  
	 * @return name of the item of the fixed list
	 */
	public String getCellItemFixedListName() {
		return cellItemFixedListName;
	}
	
	/**
	 * Returns the detailScreen Storyboard name
	 * @return The detailScreen Storyboard name
	 */
	public String getDetailScreenStoryBoardName() {
		return detailScreenStorboardName;
	}

	/**
	 * Sets the ScreenDetail Storyboard name
	 * @param p_sDetailScreenStoryboardName The ScreenDetail Storyboard name to set
	 */
	public void setDetailScreenStoryBoardName(String p_sDetailScreenStoryboardName) {
		this.detailScreenStorboardName = p_sDetailScreenStoryboardName;
	}

	/**
	 * Returns the detailScreen ViewController name
	 * @return The detailScreen ViewController name
	 */
	public String getFixedListDetailViewControllerName() {
		return detailScreenViewControllerName;
	}

	/**
	 * Sets the ScreenDetail ViewController name
	 * @param p_sDetailScreenViewControllerName The ScreenDetail ViewController name to set
	 */
	public void setDetailScreenViewControllerName(
			String p_sDetailScreenViewControllerName) {
		this.detailScreenViewControllerName = p_sDetailScreenViewControllerName;
	}
	
	/**
	 * Returns the ViewModel item name
	 * @return The ViewModel item name
	 */
	public String getViewModelItemName() {
		return viewModelItemName;
	}

	/**
	 * Sets the ViewModel item name
	 * @param viewModelItemName The ViewModel item name
	 */
	public void setViewModelItemName(String p_sViewModelItemName) {
		this.viewModelItemName = p_sViewModelItemName;
	}

}