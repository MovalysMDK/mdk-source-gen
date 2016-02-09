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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.dom4j.Element;



/**
 * HTML5 view data
 * @author ftollec
 *
 */
@XmlRootElement(name="html5-view")
@XmlAccessorType(XmlAccessType.FIELD)
public class MH5ListPanelView extends MH5PanelView{
	
	/**
	 * Name of the corresponding panel that contain the list
	 */
	@XmlAttribute
	private String panelListName;
	
	/**
	 * Name of the corresponding screen that contain the detail
	 */
	@XmlAttribute
	private String detailScreenName;
	
	/**
	 * if the panel is a list, here it's ID
	 */
	@XmlAttribute
	private String  listId;
	
	@XmlAttribute
	private boolean canAdd = false;
	
	/**
	 * @return the isList
	 */
	@Override
	public boolean isList() {
		return true;
	}



	/**
	 * @return the listId
	 */
	public String getListId() {
		return listId;
	}


	/**
	 * @param listId the listId to set
	 */
	public void setListId(String pListId) {
		this.listId = pListId;
	}

	/**
	 * @return the panelListName
	 */
	public String getPanelListName() {
		return panelListName;
	}


	/**
	 * @param panelListName the panelListName to set
	 */
	public void setPanelListName(String pPanelListName) {
		this.panelListName = pPanelListName;
	}


	/**
	 * @return the detailScreenName
	 */
	public String getDetailScreenName() {
		return detailScreenName;
	}


	/**
	 * @param detailScreenName the detailScreenName to set
	 */
	public void setDetailScreenName(String pDetailScreenName) {
		this.detailScreenName = pDetailScreenName;
	}


	
	public boolean canAdd() {
		return canAdd;
	}

	public void setCanAdd(boolean pCanAdd) {
		this.canAdd = pCanAdd;
	}


	/**
	 * to Xml of the HTML5 view
	 * @return the xml element of the view
	 */
	@Override
	public Element toXml() {
		Element r_xAttr = super.toXml();
		
		/* Temporary fix for generating NotesDeFrais HTML5 */
		/* TODO fix getDetailScreenName() = null */
		if(this.getDetailScreenName() == null) {
			this.setDetailScreenName("");
		}

		r_xAttr.addAttribute("is-list", "true");
		r_xAttr.addAttribute("list-id", this.getListId());
		r_xAttr.addAttribute("can-add", Boolean.toString(this.canAdd));
		r_xAttr.addElement("panel-list-name").setText(this.getPanelListName());
		r_xAttr.addElement("detail-screen-name").setText(this.getDetailScreenName());
		
		return r_xAttr;
	}
}
