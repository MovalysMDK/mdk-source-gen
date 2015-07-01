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
package com.a2a.adjava.languages.ios.generators;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.a2a.adjava.generator.core.XslTemplate;
import com.a2a.adjava.generator.core.xmlmerge.AbstractXmlMergeGenerator;
import com.a2a.adjava.generator.core.xmlmerge.xa.configuration.XaConfFile;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.languages.ios.xmodele.MIOSDictionnary;
import com.a2a.adjava.languages.ios.xmodele.MIOSDomain;
import com.a2a.adjava.languages.ios.xmodele.MIOSModeleFactory;
import com.a2a.adjava.languages.ios.xmodele.MIOSStoryBoard;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSControllerType;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.utils.JaxbUtils;
import com.a2a.adjava.utils.StrUtils;
import com.a2a.adjava.xmodele.MScreen;
import com.a2a.adjava.xmodele.XProject;
import com.a2a.adjava.xmodele.ui.menu.MMenu;
import com.a2a.adjava.xmodele.ui.menu.MMenuItem;

/**
 * PList Generator
 * @author lmichenaud
 *
 */
public class PListGenerator extends AbstractXmlMergeGenerator<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> {

	/**
	 * PList extension
	 */
	public static final String PLIST_EXTENSION = "plist";

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(PListGenerator.class);



	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.generators.ResourceGenerator#genere(com.a2a.adjava.xmodele.XProject, com.a2a.adjava.generators.DomainGeneratorContext)
	 */
	@Override
	public void genere(
			XProject<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> p_oMProject,
			DomainGeneratorContext p_oGeneratorContext) throws Exception {

		log.debug("> PListGenerator.genere");

		Chrono oChrono = new Chrono(true);

		this.genFrameworkPList(p_oMProject, p_oGeneratorContext);
		this.genWorkspacePList(p_oMProject, p_oGeneratorContext);

		log.debug("< PListGenerator.genere: {}", oChrono.stopAndDisplay());
	}
	
	/**
	 * Generate Workspace plist file 
	 * @param p_oMProject
	 * @param p_oGeneratorContext
	 * @throws Exception 
	 */
	private void genWorkspacePList(
			XProject<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> p_oMProject,
			DomainGeneratorContext p_oGeneratorContext) throws Exception {
		
		for (MIOSController oMIOSController : p_oMProject.getDomain().getDictionnary().getAllIOSControllers()) {
			if (oMIOSController.getControllerType().equals(MIOSControllerType.WORKSPACE)) {
				
				Document xDoc = JaxbUtils.marshalToDocument(oMIOSController);
				String sPlistFileName = StringUtils.join("work-", oMIOSController.getName(), StrUtils.DOT_S, PLIST_EXTENSION);
				File oPListFile = new File("resources/plist/work", sPlistFileName);
				log.debug("  generate: {}", oPListFile.getAbsolutePath());
				
				this.doXmlMergeGeneration(xDoc, XslTemplate.WORK_PLIST_TEMPLATE, oPListFile, p_oMProject, p_oGeneratorContext,XaConfFile.IOS_PLIST);
			}
		}
		
	}

	/**
	 * Generate framework plist
	 * @param p_oMProject project
	 * @param p_oGeneratorContext generator context
	 * @throws Exception exception
	 */
	private void genFrameworkPList( XProject<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> p_oMProject,
			DomainGeneratorContext p_oGeneratorContext ) throws Exception {

		MScreen oScreen = p_oMProject.getDomain().getDictionnary().getMainScreen();
		if ( oScreen != null ) {
			MIOSStoryBoard oStoryBoard = p_oMProject.getDomain().getDictionnary().getIOSStoryBoardByScreen(oScreen);
			String sPlistFileName = StringUtils.join("Framework-screens-menu.", PLIST_EXTENSION);

			MIOSDictionnary oDictionnary = p_oMProject.getDomain().getDictionnary();

			FrameworkConfig oFrameworkConfig = new FrameworkConfig();
			
			StoryboardConfig oMainScreenStoryboardConfig = null;
			
			Collection<StoryboardConfig> oOtherScreensStoryboardConfig = new ArrayList<StoryboardConfig>();

			MMenu oOptionMenu = null;
			
			Collection<MScreen> oScreens = oDictionnary.getAllScreens();
			for (MScreen oCurrentScreen : oScreens) {
				StoryboardConfig oCurrentScreenConfig = new StoryboardConfig();
				oCurrentScreenConfig.setName(oDictionnary.getIOSStoryBoardByScreen(oCurrentScreen).getName());
				Collection<String> oCurrentScreenNavigation = new ArrayList<String>();
				oOptionMenu = oCurrentScreen.getMenu("options");
				if (oOptionMenu != null) {
					for (MMenuItem oMenuItem : oOptionMenu.getMenuItems()) {
						oCurrentScreenNavigation.add(oDictionnary.getIOSStoryBoardByScreen(oMenuItem.getNavigation().getTarget()).getName());
					}
					oCurrentScreenConfig.setNavigation(oCurrentScreenNavigation);	
				}
				if (oCurrentScreen.isMain()) {
					oMainScreenStoryboardConfig = oCurrentScreenConfig;
				}
				else {
					oOtherScreensStoryboardConfig.add(oCurrentScreenConfig);					
				}
			}
			oFrameworkConfig.setStoryboardConfig(oOtherScreensStoryboardConfig);

			
			
			
			if (oMainScreenStoryboardConfig==null) {
				oMainScreenStoryboardConfig =  new StoryboardConfig();
				MScreen oMainScreen = oDictionnary.getMainScreen();
				oOptionMenu = oMainScreen.getMenu("options");
				if (oOptionMenu != null) {
					Collection<String> oMainScreenNavigation = new ArrayList<String>();
					for (MMenuItem oMenuItem : oOptionMenu.getMenuItems()) {
						oMainScreenNavigation.add(oDictionnary.getIOSStoryBoardByScreen(oMenuItem.getNavigation().getTarget()).getName());
					}
					oMainScreenStoryboardConfig.setNavigation(oMainScreenNavigation);
				}
			}
			
			oMainScreenStoryboardConfig.setName(oStoryBoard.getName());
			oFrameworkConfig.setMainStoryboardConfig(oMainScreenStoryboardConfig);

			
			File oPListFile = new File("resources/plist", sPlistFileName);
			log.debug("  generate: {}", oPListFile.getAbsolutePath());

			Document xDoc = JaxbUtils.marshalToDocument(oFrameworkConfig);

			this.doXmlMergeGeneration(xDoc, XslTemplate.FRAMEWORK_PLIST_TEMPLATE, oPListFile, p_oMProject, p_oGeneratorContext,XaConfFile.IOS_PLIST);
		}
	}


	/**
	 * Framework config bean
	 * @author lmichenaud
	 * @author jdborowy
	 *
	 */
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	private static class FrameworkConfig {

		/**
		 * Main story board
		 */
		private StoryboardConfig mainStoryboardConfig;

		/** 
		 * All storyboards
		 */
		private Collection<StoryboardConfig> storyboardConfig;

		/**
		 * Return main storyboard name
		 * @return main storyboard name
		 */
		@SuppressWarnings("unused")
		public StoryboardConfig getMainStoryboardConfig() {
			return mainStoryboardConfig;
		}

		/**
		 * Define main story board name
		 * @param p_oMainStoryboardConfig main story board name
		 */
		public void setMainStoryboardConfig(StoryboardConfig p_oMainStoryboardConfig) {
			this.mainStoryboardConfig = p_oMainStoryboardConfig;
		}

		/**
		 * Returns the storyboard configuration
		 * @return the storyboard configuration
		 */
		public Collection<StoryboardConfig> getStoryboardConfig() {
			return storyboardConfig;
		}

		/**
		 * Sets the storyboard configuration
		 * @return the storyboard configuration
		 */
		public void setStoryboardConfig(Collection<StoryboardConfig> p_oStoryboardConfig) {
			this.storyboardConfig = p_oStoryboardConfig;
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	private static class StoryboardConfig {

		/**
		 * Storyboard name
		 */
		private String name;

		/** 
		 * Menu navigation elements
		 */
		private Collection<String> navigation;

		/**
		 * Returns the name of the storyboard config
		 * @return the name of the storyboard config
		 */
		public String getName() {
			return name;
		}

		/**
		 * Sets the name of the storyboard config
		 * @param the name of the storyboard config
		 */
		public void setName(String p_sName) {
			this.name = p_sName;
		}

		/**
		 * Returns the menu navigation elements of the storyboard config
		 * @return the menu navigation elements of the storyboard config
		 */
		public Collection<String> getNavigation() {
			return navigation;
		}

		/**
		 * Sets the the menu navigation elements of the storyboard config
		 * @return the menu navigation elements of the storyboard config
		 */
		public void setNavigation(Collection<String> p_oNavigation) {
			this.navigation = p_oNavigation;
		}
	}
}



