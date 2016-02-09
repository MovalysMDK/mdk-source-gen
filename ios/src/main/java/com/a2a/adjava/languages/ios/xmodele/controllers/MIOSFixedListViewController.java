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
import javax.xml.bind.annotation.XmlTransient;

import com.a2a.adjava.languages.ios.xmodele.MIOSStoryBoard;

/**
 * Fixed view controller
 * @author lmichenaud
 *
 */
@XmlRootElement(name="controller")
@XmlAccessorType(XmlAccessType.FIELD)
public class MIOSFixedListViewController extends MIOSViewController {
	
	/**
	 * Class name of cell
	 */
	@XmlElement
	private String cellClassName ; 
	
	/**  
	 * Specifies if the cell must be generated or not
	 * Used in the cell generator conditioning the cell files generation
	 */
	@XmlTransient
	private boolean doCellGeneration;
	
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
	 * Storyboard used for detail
	 */
	@XmlElement
	private MIOSStoryBoard detailStoryboard;
	
	/**
	 * Controller used for detail
	 */
	@XmlElement
	private MIOSFormViewController detailViewController;
	
	/**
	 * Constructor
	 */
	public MIOSFixedListViewController() {
		this.setControllerType(MIOSControllerType.FIXEDLISTVIEW);
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
	 * Get the cell generation boolean
	 * @return cell generation boolean
	 */
	public boolean isDoCellGeneration() {
		return doCellGeneration;
	}

	/**
	 * Set do cell generation
	 * @param p_bDoCellGeneration boolean which specifies if the cell must be generated or not
	 */
	public void setDoCellGeneration(boolean p_bDoCellGeneration) {
		this.doCellGeneration = p_bDoCellGeneration;
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
}
