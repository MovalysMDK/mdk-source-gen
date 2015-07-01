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
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.a2a.adjava.languages.ios.xmodele.connections.MIOSConnection;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSSection;

/**
 * IOS View Controller
 * @author lmichenaud
 *
 */
@XmlRootElement(name="controller")
@XmlAccessorType(XmlAccessType.FIELD)
public class MIOSViewController extends MIOSController implements Cloneable{

	/**
	 * View Controller prefix
	 */
	protected static final String VIEWCONTROLLER_PREFIX = "viewcontroller_";

	/**
	 * View Id
	 */
	@XmlAttribute
	private String viewId ;

	/**
	 * Navigation item id
	 */
	@XmlAttribute
	private String navigationItemId ;

	/**
	 * View model
	 */
	@XmlElement
	private String viewModel ;

	/**
	 * Form name
	 */
	@XmlElement
	private String formName;

	/**
	 * Form type
	 */
	@XmlElement
	private MIOSFormType formType;

	/**
	 * Is Comment in screen
	 */
	@XmlElement
	private boolean isInCommentScreen  = false;

	/**
	 * Sections
	 */
	@XmlElementWrapper
	@XmlElement(name="section")
	private List<MIOSSection> sections = new ArrayList<MIOSSection>();

	/**
	 * Workspace Role
	 */
	@XmlElement
	private MIOSWorkspaceRole workspaceRole = MIOSWorkspaceRole.NOT_IN_WORKSPACE;

	/**
	 * Is Controller in ContainerViewController
	 */
	@XmlElement
	private boolean isInContainerViewController = false;

	/**
	 * Constructor
	 */
	public MIOSViewController() {
		this.setControllerType(MIOSControllerType.VIEW);
		this.formType = MIOSFormType.TABLE;
	}

	/**
	 * Add a section
	 * @param p_oIOSSection section to add
	 */
	public void addSection( MIOSSection p_oIOSSection ) {
		this.sections.add(p_oIOSSection);
		p_oIOSSection.setParentControllerPosition(this.sections.indexOf(p_oIOSSection));
		p_oIOSSection.setController(this);
	}


	/**
	 * Clears and sets the sections
	 * @param p_listSections sections to set
	 */
	public void clearAndAddSection( List<MIOSSection> p_listSections ) {
		this.sections.clear();

		for (MIOSSection oSection: p_listSections)
		{
			MIOSSection oNewSection = oSection.clone();
			oNewSection.setController(this);
			this.sections.add(oNewSection);
		}
	}

	/**
	 * Return navigation item id
	 * @return navigation item id
	 */
	public String getNavigationItemId() {
		return this.navigationItemId;
	}

	/**
	 * Define navigation item id
	 * @param p_sNavigationItemId navigation item id
	 */
	public void setNavigationItemId(String p_sNavigationItemId) {
		this.navigationItemId = p_sNavigationItemId;
	}

	/**
	 * Return view id
	 * @return view id
	 */
	public String getViewId() {
		return this.viewId;
	}

	/**
	 * Define view id
	 * @param p_sViewId view id
	 */
	public void setViewId(String p_sViewId) {
		this.viewId = p_sViewId;
	}

	/**
	 * Return first section
	 * @return first section
	 */
	public MIOSSection getFirstSection() {
		return !this.sections.isEmpty()?this.sections.get(0):null;
	}

	/**
	 * Return subviews
	 * @return subviews
	 */
	public List<MIOSSection> getSections() {
		Collections.sort(this.sections, MIOSSection.MIOSSectionComparator);
		return this.sections;
	}

	/**
	 * Return subviews
	 * @return subviews
	 */
	public List<MIOSSection> getOrderedSections() {
		List<MIOSSection> sortedList = new ArrayList<>();
		for(MIOSSection oSection : this.sections) {
			sortedList.add(oSection);
		}
		Collections.sort(sortedList, MIOSSection.MIOSSectionComparator);
		return sortedList;
	}

	/**
	 * Set sections
	 * @param p_listSections sections
	 */
	public void setSections(List<MIOSSection> p_listSections) {
		this.sections = p_listSections;
	}

	/**
	 * Return section count
	 * @return section count
	 */
	public int sectionCount() {
		return this.sections.size();
	}

	/**
	 * View model name
	 * @return viewmodel name
	 */
	public String getViewModel() {
		return this.viewModel;
	}

	/**
	 * Set viewmodel name
	 * @param p_sViewModel viewmodel name
	 */
	public void setViewModel(String p_sViewModel) {
		this.viewModel = p_sViewModel;
	}

	/**
	 * Return form name
	 * @return form name
	 */
	public String getFormName() {
		return this.formName;
	}

	/**
	 * Set form name
	 * @param p_sFormName form name
	 */
	public void setFormName(String p_sFormName) {
		this.formName = p_sFormName;
	}

	/**
	 * Remove a section from the list of sections
	 * @param p_oSection The section to remove
	 */
	public void removeSection(MIOSSection p_oSection) {
		this.sections.remove(p_oSection);
	}

	/**
	 * Remove a section from the list of sections
	 * @param p_oSection The section to remove
	 */
	public void removeAllSections() {
		this.sections.removeAll(this.sections);
	}

	/**
	 * Get the workspace role
	 * @return the workspaceRole
	 */
	public MIOSWorkspaceRole getWorkspaceRole() {
		return workspaceRole;
	}

	/**
	 * Set the workspace role
	 * @param p_oWorkspaceRole the workspaceRole to set
	 */
	public void setWorkspaceRole(MIOSWorkspaceRole p_oWorkspaceRole) {
		this.workspaceRole = p_oWorkspaceRole;
	}

	/**
	 * Returns wether the view controller is in a container
	 * @return wether the view controller is in a container
	 */
	public boolean getIsInContainerViewController() {
		return this.isInContainerViewController;
	}

	/**
	 * Sets wether the view controller is in a container
	 * @param p_bIsInContainerView wether the view controller is in a container
	 */
	public void setIsInContainerViewController(boolean p_bIsInContainerView) {
		this.isInContainerViewController = p_bIsInContainerView;
	}

	/**
	 * Returns wether the comment is in a screen
	 * @return wether the comment is in a screen
	 */
	public boolean getIsInCommentScreen() {
		return this.isInCommentScreen;
	}

	/**
	 * Sets wether the comment is in a screen
	 * @param p_bIsInCommentScreen wether the comment is in a screen
	 */
	public void setIsInCommentScreen(boolean p_bIsInCommentScreen) {
		this.isInCommentScreen = p_bIsInCommentScreen;
	}

	/**
	 * Compute the sections Y position. This method is called from the sections themselves
	 */
	public void computeSectionsPositions() {
		if(this.getControllerType().equals(MIOSControllerType.FORMVIEW)) {
			int currentOffset = 0;
			for (MIOSSection oSection : this.getSections()) {
				oSection.updateFramePosY(currentOffset);
				currentOffset += oSection.getFrameHeight();
			}
		}
	}

	/**
	 * Update the form type of this ViewController, depending on 
	 * sections it contains. This method is called from the sections
	 * themselves.
	 */
	public void updateFormType() {
		boolean hasTableSection = false;
		boolean hasNoTableSection = false;
		for (MIOSSection oSection : this.getSections()) {
			hasTableSection = hasTableSection || !oSection.isNoTable();
			hasNoTableSection = hasNoTableSection || oSection.isNoTable();
		}
		if(!hasTableSection) {
			this.formType = MIOSFormType.NO_TABLE;
		}
		else if (!hasNoTableSection) {
			this.formType = MIOSFormType.TABLE;
		}
		else {
			this.formType = MIOSFormType.MIXTE;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see clone()
	 */
	@Override
	public MIOSViewController clone() {
		MIOSViewController r_oViewController = new MIOSViewController();

		//Clone these properties
		r_oViewController.setViewId(this.getViewId());
		r_oViewController.setNavigationItemId(this.getNavigationItemId());
		r_oViewController.setViewModel(this.getViewModel());
		r_oViewController.setFormName(this.getFormName());
		r_oViewController.setWorkspaceRole(this.getWorkspaceRole());
		r_oViewController.setSections((this.getSections() != null) ? new ArrayList<MIOSSection>(this.getSections()) : null);

		//Clone parent properties
		r_oViewController.setId(this.getId());
		r_oViewController.setName(this.getName());
		r_oViewController.setCustomClass(this.getCustomClass());
		r_oViewController.setControllerType(this.getControllerType());
		r_oViewController.setConnections((this.getConnections() != null) ? new ArrayList<MIOSConnection>(this.getConnections()) : null);
		r_oViewController.setIsInCommentScreen(this.getIsInCommentScreen());
		return r_oViewController;
	}


}
