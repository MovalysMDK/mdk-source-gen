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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;

import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSController;


/**
 * IOS Scene
 * @author lmichenaud
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class MIOSScene {

	/**
	 * Scene id
	 */
	@XmlID
	@XmlAttribute
	private String id ;
	
	/**
	 * Scene X
	 */
	@XmlAttribute
	private int posX ;
	
	/**
	 * Scene Y
	 */
	@XmlAttribute
	private int posY ;
	
	/**
	 * Place holder id
	 */
	@XmlAttribute
	private String placeHolderId ;
	
	/**
	 * Controller
	 */
	@XmlElement
	private MIOSController controller ;

	@XmlElement
	private String tag ;
	
	/**
	 * Scenes that this scene targets
	 */
	@XmlTransient
	private List<MIOSScene> linksTo = new ArrayList<MIOSScene>();
	
	/**
	 * Scenes that targets this scene
	 */
	@XmlTransient
	private List<MIOSScene> linksFrom = new ArrayList<MIOSScene>();
	
	/**
	 * Return scenes that this scene targets
	 * @return scenes that this scene targets
	 */
	public List<MIOSScene> getLinksTo() {
		return this.linksTo;
	}

	/**
	 * Return scenes that targets this scene
	 * @return scenes that targets this scene
	 */
	public List<MIOSScene> getLinksFrom() {
		return this.linksFrom;
	}
	
	/**
	 * Add a target link to a scene (xcode editor)
	 * @param p_oMIOSScene scene to add
	 */
	public void addLinkTo( MIOSScene p_oMIOSScene) {
		this.linksTo.add(p_oMIOSScene);
	}
	
	/**
	 * Add a link to this scene (xcode editor)
	 * @param p_oMIOSScene scene to add
	 */
	public void addLinkFrom( MIOSScene p_oMIOSScene) {
		this.linksFrom.add(p_oMIOSScene);
	}

	/**
	 * Return id
	 * @return id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Define id
	 * @param p_sId id
	 */
	public void setId(String p_sId) {
		this.id = p_sId;
	}

	/**
	 * Return x position
	 * @return x position
	 */
	public int getPosX() {
		return this.posX;
	}

	/**
	 * Define x position
	 * @param p_iPosX x position
	 */
	public void setPosX(int p_iPosX) {
		this.posX = p_iPosX;
	}

	/**
	 * Return y position
	 * @return y position
	 */
	public int getPosY() {
		return this.posY;
	}

	/**
	 * Define y position
	 * @param p_iPosY y position
	 */
	public void setPosY(int p_iPosY) {
		this.posY = p_iPosY;
	}

	/**
	 * Return place holder id
	 * @return place holder id
	 */
	public String getPlaceHolderId() {
		return this.placeHolderId;
	}

	/**
	 * Define place holder id
	 * @param p_sPlaceHolderId place holder id
	 */
	public void setPlaceHolderId(String p_sPlaceHolderId) {
		this.placeHolderId = p_sPlaceHolderId;
	}

	/**
	 * Return controller
	 * @return controller
	 */
	public MIOSController getController() {
		return this.controller;
	}

	/**
	 * Define controller
	 * @param p_oController controller
	 */
	public void setController(MIOSController p_oController) {
		this.controller = p_oController;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	
}
