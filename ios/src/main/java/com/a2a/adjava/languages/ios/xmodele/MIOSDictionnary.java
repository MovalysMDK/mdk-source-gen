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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSController;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSSection;
import com.a2a.adjava.xmodele.MScreen;
import com.a2a.adjava.xmodele.ModelDictionary;

/**
 * IOS Dictonnary
 * @author lmichenaud
 *
 */
public class MIOSDictionnary extends ModelDictionary {

	/**
	 * Story boards
	 */
	private Map<String,MIOSStoryBoard> storyBoards = new HashMap<String,MIOSStoryBoard>();
	
	/**
	 * Controllers
	 */
	private Map<String,MIOSController> controllers = new HashMap<String,MIOSController>();
	
	/**
	 * Story boards by screen
	 */
	private Map<MScreen,MIOSStoryBoard> storyBoardByScreen = new HashMap<MScreen,MIOSStoryBoard>();
		
	/**
	 * Sections
	 */
	private Map<String,MIOSSection> sections = new HashMap<String,MIOSSection>();
	
	/**
	 * Xib container
	 */
	private Map<String,MIOSXibContainer> xibContainers = new HashMap<String,MIOSXibContainer>();
	
	/**
	 * Register a story board
	 * @param p_oMIOSStoryBoard story board
	 * @param p_oScreen corresponding screen
	 */
	public void registerIOSStoryBoard( MIOSStoryBoard p_oMIOSStoryBoard, MScreen p_oScreen ) {
		this.storyBoards.put(p_oMIOSStoryBoard.getName(), p_oMIOSStoryBoard);
		this.storyBoardByScreen.put(p_oScreen, p_oMIOSStoryBoard);
	}
	
	/**
	 * Register a ios controller
	 * @param p_oMIOSController ios controller
	 */
	public void registerIOSController( MIOSController p_oMIOSController ) {
		this.controllers.put(p_oMIOSController.getId(), p_oMIOSController);
	}
	
	/**
	 * Register a ios section
	 * @param p_oMIOSSection ios section
	 */
	public void registerIOSSection( MIOSSection p_oMIOSSection ) {
		this.sections.put(p_oMIOSSection.getName(), p_oMIOSSection);
	}
	
	/**
	 * Return all story boards
	 * @return story boards
	 */
	public Collection<MIOSStoryBoard> getAllIOSStoryBoards() {
		return this.storyBoards.values();
	}

	/**
	 * Return the storyboard corresponding to the screen
	 * @param p_oMScreen screen
	 * @return storyboard
	 */
	public MIOSStoryBoard getIOSStoryBoardByScreen(MScreen p_oMScreen) {
		return this.storyBoardByScreen.get(p_oMScreen);
	}
	
	/**
	 * Return the controller with the given id
	 * @param p_sId controller id
	 * @return controller found
	 */
	public MIOSController getControllerBiId(String p_sId) {
		return this.controllers.get(p_sId);
	}
	
	/**
	 * Return all controllers
	 * @return controllers
	 */
	public Collection<MIOSController> getAllIOSControllers() {
		return this.controllers.values();
	}
		
	/**
	 * Return all sections
	 * @return all sections
	 */
	public Collection<MIOSSection> getAllIOSSections() {
		return this.sections.values();
	}
	
	/**
	 * Register a XIB container with its name
	 * @param p_oMIOSXibContainer XIB container
	 */
	public void registerIOSXibContainer( MIOSXibContainer p_oMIOSXibContainer ) {
		this.xibContainers.put(p_oMIOSXibContainer.getName(), p_oMIOSXibContainer);
	}
	
	/**
	 * Return all XIB container 
	 * @return all XIB container
	 */
	public Collection<MIOSXibContainer> getAllIOSXibContainers() {
		return this.xibContainers.values();
	}
	
	/**
	 * Return a XIB container with his name
	 * @param p_sName the name of the xib container to find
	 * @return one XIB container or null if not in the map
	 */
	public MIOSXibContainer getIOSXibContainersByName(String p_sName ) {
		return this.xibContainers.get(p_sName) ;
	}
}
