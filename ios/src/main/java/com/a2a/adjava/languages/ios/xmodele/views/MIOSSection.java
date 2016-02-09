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
package com.a2a.adjava.languages.ios.xmodele.views;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSControllerType;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSSectionType;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSViewController;
import com.a2a.adjava.xmodele.ui.view.MVFLocalization;

/**
 * MIOSSection
 * @author lmichenaud
 *
 */
@XmlRootElement(name="section")
@XmlAccessorType(XmlAccessType.FIELD)
public class MIOSSection {

	/**
	 * Gives the original "adding" position by the parent controller.
	 */
	@XmlTransient
	private int parentControllerPosition;
	

	/**
	 * Basic with of a frame
	 */
	private static final int FRAME_WIDTH = 600 ;
	
	/**
	 * Basic height of a frame
	 */
	private static final int FRAME_HEIGHT = -1;
	
	/**
	 * Basic cell margin
	 */
	private static final int CELL_MARGIN = 10 ;
	
	/**
	 * constructor
	 */
	public MIOSSection(){
		super();
	}

	/**
	 * Name
	 */
	@XmlAttribute
	private String name ;

	/**
	 * Titled
	 */
	@XmlAttribute
	private boolean titled = false ;

	/**
	 * isNoTable
	 */
	@XmlAttribute
	private boolean isNoTable = false;

	/**
	 * Layout
	 */
	@XmlElementWrapper
	@XmlElement(name="subView")
	private List<MIOSView> subViews = new ArrayList<MIOSView>();

	/**
	 * Parent controller
	 */
	@XmlTransient
	private MIOSController controller ;

	/**
	 * Viewmodel
	 */
	@XmlElement
	private String viewModel ;

	/**
	 * parentViewModelClass
	 */
	@XmlElement
	private String parentViewModelClass ;

	/**
	 * viewModelAttributeInParent
	 */
	@XmlElement
	private String viewModelAttributeInParent ;

	/**
	 * @return the parentViewModelClass
	 */
	public String getParentViewModelClass() {
		return parentViewModelClass;
	}

	/**
	 * Sets the parent view model class name
	 * @param p_sParentViewModelClass the parentViewModelClass to set
	 */
	public void setParentViewModelClass(String p_sParentViewModelClass) {
		this.parentViewModelClass = p_sParentViewModelClass;
	}

	/**
	 * returns the view model attribute in parent
	 * @return the viewModelAttributeInParent
	 */
	public String getViewModelAttributeInParent() {
		return viewModelAttributeInParent;
	}

	/**
	 * Sets the view model attribute in parent
	 * @param p_sViewModelAttributeInParent the viewModelAttributeInParent to set
	 */
	public void setViewModelAttributeInParent(String p_sViewModelAttributeInParent) {
		this.viewModelAttributeInParent = p_sViewModelAttributeInParent;
	}

	/**
	 * Viewmodel
	 */
	@XmlElement
	private MIOSSectionType sectionType ;

	/**
	 * Dataloader name
	 */
	@XmlElement
	private String dataloader;
	
	/**
	 * Width of the main view in the section
	 */
	@XmlAttribute
	private int frameWidth = FRAME_WIDTH ;
	
	/**
	 * Height of the main view in the section
	 */
	@XmlAttribute
	private int frameHeight = FRAME_HEIGHT;
	
	/**
	 * Height of the main view in the section
	 */
	@XmlAttribute
	private int framePosY = 0;

	/**
	 * Gets the frame height of this section
	 * 
	 * @return the frame height of this section
	 */
	public int getFrameHeight() {
		return frameHeight;
	}

	/**
	 * Updates the Y position frame of this section in its controller
	 * 
	 * @param framePosY
	 */
	public void updateFramePosY(int framePosY) {
		this.framePosY = framePosY;
	}
	
	/**
	 * Margin between elements in the right or top or bottom
	 */
	@XmlAttribute
	private int cellMargin = CELL_MARGIN;

	/**
	 * Add a sub view
	 * @param p_oIOSView subview to add
	 */
	public void addSubView( MIOSView p_oIOSView ) {
		this.subViews.add(p_oIOSView);
	}

	/**
	 * Return subviews
	 * @return subviews
	 */
	public List<MIOSView> getSubViews() {
		return this.subViews;
	}

	/**
	 * Return subview count
	 * @return subview count
	 */
	public int subviewCount() {
		return this.subViews.size();
	}

	/**
	 * Clear the current list of subviews, and add the new list as the list of subview of this sections
	 * @param subViews2 the new list of subviews
	 */
	public void clearAndAddSubviews(List<MIOSView> subViews2) {
		this.subViews.clear();
		this.subViews.addAll(subViews2);
	}


	/**
	 * Get section name
	 * @return section name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set section name
	 * @param p_sName section name
	 */
	public void setName(String p_sName) {
		this.name = p_sName;
	}

	/**
	 * Get section title visibility
	 * @return section title visibility
	 */
	public boolean getTitled() {
		return this.titled;
	}

	/**
	 * Set section title visibility
	 * @param p_bTitled section title visibility
	 */
	public void setTitled(boolean p_bTitled) {
		this.titled = p_bTitled;
	}

	/**
	 * Get controller
	 * @return controller
	 */
	public MIOSController getController() {
		return this.controller;
	}

	/**
	 * Set controller
	 * @param p_oController the controller to set
	 */
	public void setController(MIOSController p_oController) {
		this.controller = p_oController;
	}

	/**
	 * Get viewmodel name
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
	 * Get dataloader
	 * @return dataloader
	 */
	public String getDataloader() {
		return this.dataloader;
	}

	/**
	 * Set dataloader
	 * @param p_sDataloader dataloader
	 */
	public void setDataloader(String p_sDataloader) {
		this.dataloader = p_sDataloader;
	}

	/**
	 * Returns the type oif the section
	 * @return the type of the section
	 */
	public MIOSSectionType getSectionType() {
		return sectionType;
	}

	/**
	 * Sets the section type
	 * @param p_oSectionType The section type to set
	 */
	public void setSectionType(MIOSSectionType p_oSectionType) {
		this.sectionType = p_oSectionType;
	}

	/**
	 * Indicates if the section is "NoTable"
	 * 
	 * @return true if the section should not be displayed as a UITableView,
	 *         false otherwise.
	 */
	public boolean isNoTable() {
		return isNoTable;
	}

	/**
	 * Set the "NoTable" behavior of this section
	 * 
	 * @param isNoTable
	 *            A boolean, true if the section should not be displayed as a
	 *            UITableView, false otherwise.
	 */
	public void setNoTable(boolean isNoTable) {
		if (getController() != null && getController().getControllerType().equals(MIOSControllerType.FORMVIEW)) {
			this.isNoTable = isNoTable;
			if (getController() instanceof MIOSViewController) {
				((MIOSViewController) getController()).updateFormType();
			}
		}
	}
	/**
	 * Compute the X and Y coordinate of the subviews of the section to view all 
	 * the fields from the top to the bottom of the main. 
	 * Compoute the height of the view describing the section
	 */
	public void computeSubviewsPosition() {
		int iLastHeight = 0 ;
		for( MIOSView oView :  this.subViews ) {
			if ( oView instanceof MIOSButtonView ){// on ne traite pas les boutons
				continue ;
			}
			oView.setPosX( this.cellMargin );
			oView.setPosY( this.cellMargin + iLastHeight );
			iLastHeight = iLastHeight + oView.getTotalHeight() + this.cellMargin;  
		}
		this.frameHeight = iLastHeight + this.cellMargin ;
		if (getController() instanceof MIOSViewController) {
			((MIOSViewController) getController()).computeSectionsPositions();
		}
	}	

	/**
	 * Compute the X and Y coordinate of the subviews of the section to view all 
	 * the fields from the top to the bottom of the main. 
	 * Compoute the height of the view describing the section
	 * @param p_oLocalization A filter to compute positions of subviews that have this localization only. Set null to
	 * compute positions for any kind of subviews.
	 */
	public void computeSubviewsPositionForLocalization(MVFLocalization p_oLocalization) {
		if(p_oLocalization == null) {
			computeSubviewsPosition();
		}
		else {
			int lastHeight = 0 ;
			for( MIOSView oView :  this.subViews ) {
				if(p_oLocalization.equals(oView.getLocalization())) {
					if ( oView instanceof MIOSButtonView ){// on ne traite pas les boutons
						continue ;
					}
					oView.setPosX( this.cellMargin );
					oView.setPosY( this.cellMargin + lastHeight );
					lastHeight = lastHeight + oView.getTotalHeight() + this.cellMargin;  
				}
				this.frameHeight = lastHeight + this.cellMargin ;
			}
		}
	}

	/**
	 * Gives the parent controller position
	 * @return the parent controller position
	 */
	public int getParentControllerPosition() {
		return parentControllerPosition;
	}

	/**
	 * Sets the parent controller 
	 * @param parentControllerPosition
	 */
	public void setParentControllerPosition(int p_iParentControllerPosition) {
		this.parentControllerPosition = p_iParentControllerPosition;
	}

	/**
	 * Clones the current instance
	 * @return clone of the instance
	 */
	public MIOSSection clone()
	{
		MIOSSection newMiosSection = new MIOSSection();
		newMiosSection.setController(this.controller);
		newMiosSection.setDataloader(this.dataloader);
		newMiosSection.setName(this.name);
		newMiosSection.setViewModel(this.viewModel);
		newMiosSection.clearAndAddSubviews(this.subViews);
		newMiosSection.computeSubviewsPosition();

		return newMiosSection;
	}

	public static Comparator<MIOSSection> MIOSSectionComparator = new Comparator<MIOSSection>() {
		@Override
		public int compare(MIOSSection p_oSection, MIOSSection p_oAnotherSection) {
			if(p_oSection.isNoTable) {
				if(p_oAnotherSection.isNoTable) {
					return p_oSection.getParentControllerPosition() - p_oAnotherSection.getParentControllerPosition();
				}
				else {
					return -1;
				}
			}
			else {
				if(p_oAnotherSection.isNoTable) {
					return 1 ;
				}
				else {
					return p_oSection.getParentControllerPosition() - p_oAnotherSection.getParentControllerPosition();
				}
			}
		}

	};

}
