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
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import com.a2a.adjava.languages.ios.xmodele.MIOSStoryBoard;

/**
 * List view controller
 * @author lmichenaud
 *
 */
@XmlRootElement(name="controller")
@XmlAccessorType(XmlAccessType.FIELD)
public class MIOSListViewController extends MIOSViewController {
	
	/**
	 * Class name of cell
	 */
	@XmlElement
	protected String cellClassName ; 
	
	/**
	 * Viewmodel of item
	 */
	@XmlElement
	protected String itemViewModel ;
	
	/**
	 * Identifier of item
	 */
	@XmlElement
	protected String itemIdentifier ;
	
	/**
	 * Has Search ViewController
	 */
	@XmlElement
	private boolean hasSearchViewController = false;
	
	/**
	 * Has Search ViewController
	 */
	@XmlElement
	private String searchViewController;
	

	/**
	 * Storyboard used for detail
	 */
	@XmlElement
	protected MIOSStoryBoard detailStoryboard;
	
	/**
	 * Controller used for detail
	 */
	@XmlIDREF
	protected MIOSFormViewController detailViewController;
		
	/**
	 * Action to delete an item of the list
	 */
	protected String deleteAction;
	
	/**
	 * Add a button in top bar to create an new item in the list
	 */
	@XmlElement
	protected boolean newItemButton;
	
	/**
	 * Constructor
	 */
	public MIOSListViewController() {
		this.setControllerType(MIOSControllerType.LISTVIEW);
	}

	/**
	 * className of cell
	 * @return className of cell
	 */
	public String getCellClassName() {
		return this.cellClassName;
	}

	/**
	 * Set class name of cell
	 * @param p_sCellClassName class name of cell
	 */
	public void setCellClassName(String p_sCellClassName) {
		this.cellClassName = p_sCellClassName;
	}

	/**
	 * Get storyboard of detail view
	 * @return storyboard of detail view
	 */
	public MIOSStoryBoard getDetailStoryboard() {
		return this.detailStoryboard;
	}

	/**
	 * Set storyboard of detail view
	 * @param p_oDetailStoryboard storyboard of detail view
	 */
	public void setDetailStoryboard(MIOSStoryBoard p_oDetailStoryboard) {
		this.detailStoryboard = p_oDetailStoryboard;
	}

	/**
	 * Get view controller of detail
	 * @return detail view of controller
	 */
	public MIOSFormViewController getDetailViewController() {
		return this.detailViewController;
	}

	/**
	 * Set view controller of detail
	 * @param p_oDetailViewController view controller of detail
	 */
	public void setDetailViewController(MIOSFormViewController p_oDetailViewController) {
		this.detailViewController = p_oDetailViewController;
	}
	
	/**
	 * has searchViewController
	 * @return has searchViewController
	 */
	public boolean isHasSearchViewController() {
		return hasSearchViewController;
	}

	/**
	 * Set has searchViewController
	 * @param p_bHasSearchViewController new value
	 */
	public void setHasSearchViewController(boolean p_bHasSearchViewController) {
		this.hasSearchViewController = p_bHasSearchViewController;
	}

	/**
	 * Get searchViewController
	 * @return searchViewController name
	 */
	public String getSearchViewController() {
		return searchViewController;
	}

	/**
	 * Set searchViewController
	 * @param p_sSearchViewController new searchViewController
	 */
	public void setSearchViewController(String p_sSearchViewController) {
		this.searchViewController = p_sSearchViewController;
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
	 * Get name of delete action
	 * @return name of delete action
	 */
	public String getDeleteAction() {
		return deleteAction;
	}

	/**
	 * Set name of delete action
	 * @param p_sDeleteAction
	 */
	public void setDeleteAction(String p_sDeleteAction) {
		this.deleteAction = p_sDeleteAction;
	}

	/**
	 * True if the controller must have a button to create a new item in the list
	 * @return true if the controller must have a button to create a new item in the list
	 */
	public boolean isNewItemButton() {
		return newItemButton;
	}

	/**
	 * Define if controller must have a button to create a new item in the list
	 * @param p_bNewItemButton true if controller must have a button to create a new item in the list
	 */
	public void setNewItemButton(boolean p_bNewItemButton) {
		this.newItemButton = p_bNewItemButton;
	}
	
	
}
