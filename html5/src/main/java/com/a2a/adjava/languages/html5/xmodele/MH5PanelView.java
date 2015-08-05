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
package com.a2a.adjava.languages.html5.xmodele;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.a2a.adjava.xmodele.MDialog;
import com.a2a.adjava.xmodele.MLinkedInterface;
import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.ui.panel.MPanelOperation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



/**
 * HTML5 view data
 * @author ftollec
 *
 */
@XmlRootElement(name="html5-view")
@XmlAccessorType(XmlAccessType.FIELD)
public class MH5PanelView extends MH5View{

	
	/**
	 * Visual fields
	 */
	@XmlElement(name="sectionAttributes")
	private List<MH5Attribute> sectionAttributes = new ArrayList<>();
	
	/**
	 * type of the panel, originaly to separate lists from other panels
	 */
	@XmlAttribute
	private String type;

	
	/**
	 * true if this panel is a list
	 */
	@XmlAttribute
	private boolean isList = false;
	
	/**
	 * true if this panel is in a multisection
	 */
	@XmlAttribute
	private boolean isPanelOfMultiSection;
	
	/**
	 * true if this panel is in a multisection and is its first panel
	 */
	@XmlAttribute
	private boolean isFirstPanelOfMultiSection;
	
	/**
	 * true if this panel is in a workspace
	 */
	@XmlAttribute
	private boolean isPanelOfWorkspace;
	
	/**
	 * true if this panel is attached to one entity at least
	 */
	@XmlAttribute
	private boolean isAttachedToEntity;
	
	/**
	 * true if this panel is attached to one entity with the stereotype Mm_applicationScope
	 */
	private String applicationScopeEntityAttached;

	/**
	 * Name of the save action
	 */
	private String screenName ;
	
	/**
	 * Name of the save action
	 */
	private String saveActionName ;
	
	/**
	 * Name of the delete action
	 */
	private String deleteActionName ;
	
	/**
	 * Add a list of attributes
	 * @param list the list of attributes to add
	 */
	public void addAllSectionAttributes(List<MH5Attribute> p_oList) {
		this.sectionAttributes.addAll(p_oList);
	}
	
	/**
	 * Add an attributes to the view 
	 * @param p_oScene scene to add
	 */
	public void addSectionAttribute(MH5Attribute p_oAttr) {
		this.sectionAttributes.add(p_oAttr);
	}
	
	

	/**
	 * Get scenes
	 * @return scenes
	 */
	public List<MH5Attribute> getSectionAttributes() {
		return this.sectionAttributes;
	}
	

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(String pType) {
		this.type = pType;
	}


	/**
	 * @return the isPanelOfMultiSection
	 */
	public boolean isPanelOfMultiSection() {
		return isPanelOfMultiSection;
	}

	/**
	 * @param isPanelOfMultiSection the isPanelOfMultiSection to set
	 */
	public void setPanelOfMultiSection(boolean pIsPanelOfMultiSection) {
		this.isPanelOfMultiSection = pIsPanelOfMultiSection;
	}

	/**
	 * @return the isFirstPanelOfMultiSection
	 */
	public boolean isFirstPanelOfMultiSection() {
		return isFirstPanelOfMultiSection;
	}

	/**
	 * @param isFirstPanelOfMultiSection the isFirstPanelOfMultiSection to set
	 */
	public void setFirstPanelOfMultiSection(boolean pIsFirstPanelOfMultiSection) {
		this.isFirstPanelOfMultiSection = pIsFirstPanelOfMultiSection;
	}

	/**
	 * return isPanelOfWorkspace
	 * @return true if the panel is in a workspace
	 */
	public boolean isPanelOfWorkspace() {
		return isPanelOfWorkspace;
	}
	/**
	 * Define if the panel is in a workspace
	 * @param isPanelOfWorkspace
	 */
	public void setPanelOfWorkspace(boolean pIsPanelOfWorkspace) {
		this.isPanelOfWorkspace = pIsPanelOfWorkspace;
	}
	
	/**
	 * @return isAttachedToEntityModel
	 * return true if the panel is attached to one entity model at least
	 */
	public boolean isAttachedToEntity() {
		return isAttachedToEntity;
	}

	/**
	 * Define if the panel is attached to one entity model at least
	 * @param isAttachedToEntityModel
	 */
	public void setAttachedToEntity(boolean isAttachedToEntity) {
		this.isAttachedToEntity = isAttachedToEntity;
	}
	
	/**
	 * @return applicationScopeEntityAttached
	 * return the name of the Entity attached to the PanelView if this entity
	 * has the stereotype Mm_ApplicationScope. Otherwise, it returns an empty string.
	 */
	public String getApplicationScopeEntityAttached() {
		return applicationScopeEntityAttached;
	}

	/**
	 * Set the name of the Entity attached to the PanelView when this entity
	 * has the stereotype Mm_ApplicationScope.
	 * @param applicationScopeEntityAttached
	 */
	public void setApplicationScopeEntityAttached(
			String applicationScopeEntityAttached) {
		this.applicationScopeEntityAttached = applicationScopeEntityAttached;
	}

	/**
	 * @return the isList
	 */
	public boolean isList() {
		return isList;
	}

	public void setListItemName(String name) {
		// TODO Auto-generated method stub
		
	}

	public String getSaveActionName() {
		return saveActionName;
	}

	public void setSaveActionName(String pSaveActionName) {
		this.saveActionName = pSaveActionName;
	}

	public String getDeleteActionName() {
		return deleteActionName;
	}

	public void setDeleteActionName(String pDeleteActionName) {
		this.deleteActionName = pDeleteActionName;
	}

	/**
	 * @return the screenName
	 */
	public String getScreenName() {
		return screenName;
	}

	/**
	 * @param screenName the screenName to set
	 */
	public void setScreenName(String pScreenName) {
		this.screenName = pScreenName;
	}

	/**
	 * to Xml of the HTML5 view
	 * @return the xml element of the view
	 */
	@Override
	public Element toXml() {
		Element r_xView = super.toXml();

		r_xView.addAttribute("isScreen", "false");
		r_xView.addAttribute("isMainScreen", "false");
		if(this.isPanelOfMultiSection()){
			r_xView.addAttribute("isPanelOfMultiSection", "true");
			if(this.isFirstPanelOfMultiSection()){
				r_xView.addAttribute("isFirstPanelOfMultiSection", "true");
			}
			else{
				r_xView.addAttribute("isFirstPanelOfMultiSection", "false");
			}
		}else{
			r_xView.addAttribute("isPanelOfMultiSection", "false");

		}
		if(this.isPanelOfWorkspace()){
			r_xView.addAttribute("isPanelOfWorkspace", "true");
		}else{
			r_xView.addAttribute("isPanelOfWorkspace", "false");

		}
		
		if (this.isAttachedToEntity()){
			r_xView.addAttribute("isAttachedToEntityModel", "true");
		}else{
			r_xView.addAttribute("isAttachedToEntityModel", "false");
		}
		
		if (this.getApplicationScopeEntityAttached() != null){
			r_xView.addAttribute("applicationScopeEntityAttached", this.getApplicationScopeEntityAttached());
		}else{
			r_xView.addAttribute("applicationScopeEntityAttached", "");
		}
		
		r_xView.addAttribute("type", this.type);
		r_xView.addAttribute("screen-name", this.screenName);
		
		Element xAttrs = r_xView.addElement("attributes");
		for( MH5Attribute oAttr : this.getSectionAttributes() ) {
			if(oAttr != null)
			{
				Element xAttr = oAttr.toXml();
				xAttr.setName("HTML-attribute");
				xAttrs.add(xAttr);
			}
		}
		
		if ( this.saveActionName != null ) {
			r_xView.addElement("saveAction").setText(this.saveActionName);
		}
		
		if ( this.deleteActionName != null ) {
			r_xView.addElement("deleteAction").setText(this.deleteActionName);
		}
		
		return r_xView;
	}
}
