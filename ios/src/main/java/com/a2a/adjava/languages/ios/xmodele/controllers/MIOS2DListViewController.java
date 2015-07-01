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

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;

import com.a2a.adjava.languages.ios.xmodele.connections.MIOSConnection;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSSection;

/**
 * List view controller
 * @author qlagarde
 *
 */
@XmlRootElement(name="controller")
@XmlAccessorType(XmlAccessType.FIELD)
public class MIOS2DListViewController extends MIOSListViewController {
	
	/**
	 * Xsl template for plist
	 */
	private static final String LISTVIEW2D_SECTION_PLIST_SUFFIX = "Section";
	
	
	/**
	 * The name of the PLIST section form
	 */
	@XmlElement
	private String sectionFormName;

	
	/**
	 * Constructor
	 */
	public MIOS2DListViewController() {
		this.setControllerType(MIOSControllerType.LISTVIEW2D);
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
	 * Set section form name
	 * @param p_sSectionFormName name of the PLIST of the section (2D)
	 */
	public void setSectionFormName(String p_sSectionFormName) {
		this.sectionFormName = p_sSectionFormName;
	}
	
	/**
	 * Get section form name
	 * @return the name of the PLIST for the section (2D)
	 */
	public String getSectionFormName() {
		return this.sectionFormName;
	}
	
	@Override
	public void setFormName(String p_sFormName) {
		super.setFormName(p_sFormName);
		this.setSectionFormName(StringUtils.join(p_sFormName,LISTVIEW2D_SECTION_PLIST_SUFFIX));
	}
	
	/**
	 * Return the different levels names associtaed to this 2D List
	 * @return An array of names used for generation
	 */
	public String[] getFormsTitles() {
		return new String[]{this.getFormName(), this.getSectionFormName()};
	}
	
	/**
	 * Return the different levels names associated to this 2D List
	 * @return An array of names used for generation
	 */
	public String[] getTypeNames() {
		return new String[]{this.getFormName()+"Cell", this.getSectionFormName()};
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see clone()
	 */
	@Override
	public MIOS2DListViewController clone() {
		super.clone();
		MIOS2DListViewController r_oViewController = new MIOS2DListViewController();
		
		//Clone these properties
		r_oViewController.setNewItemButton(this.isNewItemButton());
		r_oViewController.setCellClassName(this.getCellClassName());
		r_oViewController.setItemViewModel(this.getItemViewModel());
		r_oViewController.setItemIdentifier(this.getItemIdentifier());
		r_oViewController.setSectionFormName(this.getSectionFormName());
		r_oViewController.setDetailStoryboard(this.getDetailStoryboard());
		r_oViewController.setDetailViewController(this.getDetailViewController());
		
		//Clone these properties
		r_oViewController.setViewId(this.getViewId());
		r_oViewController.setNavigationItemId(this.getNavigationItemId());
		r_oViewController.setViewModel(this.getViewModel());
		r_oViewController.setFormName(this.getFormName());
		r_oViewController.setSections((this.getSections() != null) ? new ArrayList<MIOSSection>(this.getSections()) : null);
		r_oViewController.setId(this.getId());
		r_oViewController.setName(this.getName());
		r_oViewController.setCustomClass(this.getCustomClass());
		r_oViewController.setControllerType(this.getControllerType());
		r_oViewController.setIsInCommentScreen(this.getIsInCommentScreen());
		r_oViewController.setConnections((this.getConnections() != null) ? new ArrayList<MIOSConnection>(this.getConnections()) : null);
		return r_oViewController;
	}
}
