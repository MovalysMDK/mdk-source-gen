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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSController;

/**
 * IOS Story board
 * @author lmichenaud
 *
 */
@XmlRootElement(name="storyboard")
@XmlAccessorType(XmlAccessType.FIELD)
public class MIOSStoryBoard {

	/**
	 * Name
	 */
	@XmlID
	private String name ;
	
	/**
	 * Main controller
	 */
	@XmlIDREF
	private MIOSController mainController ;
	
	/**
	 * Scenes
	 */
	@XmlElementWrapper
	@XmlElement(name="scene")
	private List<MIOSScene> scenes = new ArrayList<MIOSScene>();
	
	@XmlTransient
	private MIOSScene mainScene;
	
	/**
	 * Return name
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Define name
	 * @param p_sName name
	 */
	public void setName(String p_sName) {
		this.name = p_sName;
	}

	/**
	 * Return main controller
	 * @return main controller
	 */
	public MIOSController getMainController() {
		return this.mainController;
	}

	/**
	 * Define main controller
	 * @param p_oMainController main controller
	 */
	public void setMainController(MIOSController p_oMainController) {
		this.mainController = p_oMainController;
	}

	/**
	 * Add a scene
	 * @param p_oScene scene to add
	 */
	public void addScene(MIOSScene p_oScene) {
		this.scenes.add(p_oScene);
	}
	
	/**
	 * Add a scene
	 * @param p_oScene scene to add
	 */
	public void setMainScene(MIOSScene p_oScene) {
		this.mainScene = p_oScene;
		this.scenes.add(0, p_oScene);
	}

	/**
	 * Get scenes
	 * @return scenes
	 */
	public List<MIOSScene> getScenes() {
		return this.scenes;
	}

	public MIOSScene getMainScene() {
		return mainScene;
	}
}
