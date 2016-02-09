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
import java.util.List;
import java.util.Vector;

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
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOS2DListViewController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSComboViewController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSControllerType;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSViewController;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSSection;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSView;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSEditableView;
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

		this.genFormPList(p_oMProject, p_oGeneratorContext);
		this.genFrameworkPList(p_oMProject, p_oGeneratorContext);
		this.genSectionPList(p_oMProject, p_oGeneratorContext);
		this.genWorkspacePList(p_oMProject, p_oGeneratorContext);

		log.debug("< PListGenerator.genere: {}", oChrono.stopAndDisplay());
	}

	/**
	 * Generate form plist files
	 * @param p_oMProject project
	 * @param p_oGeneratorContext generator context
	 * @throws Exception exception
	 */
	private void genFormPList(
			XProject<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> p_oMProject,
			DomainGeneratorContext p_oGeneratorContext) throws Exception {

		for (MIOSController oMIOSController : p_oMProject.getDomain().getDictionnary().getAllIOSControllers()) {
			if ( oMIOSController.getControllerType().equals(MIOSControllerType.FORMVIEW) 
				|| oMIOSController.getControllerType().equals(MIOSControllerType.LISTVIEW) 
				|| oMIOSController.getControllerType().equals(MIOSControllerType.LISTVIEW2D) 
				|| oMIOSController.getControllerType().equals(MIOSControllerType.FIXEDLISTVIEW) 
				|| oMIOSController.getControllerType().equals(MIOSControllerType.SEARCHVIEW)) {
				MIOSViewController oFormViewController = (MIOSViewController) oMIOSController ;
				Document xDoc = JaxbUtils.marshalToDocument(oMIOSController);
				String sPlistFileName = StringUtils.join("form-", oFormViewController.getFormName(), StrUtils.DOT_S, PLIST_EXTENSION);
				File oPListFile = new File("resources/plist/forms", sPlistFileName);
				log.debug("  generate: {}", oPListFile.getAbsolutePath());

				if(!oMIOSController.getControllerType().equals(MIOSControllerType.LISTVIEW2D)) {
					this.doXmlMergeGeneration(xDoc, XslTemplate.FORM_PLIST_TEMPLATE, oPListFile, p_oMProject, p_oGeneratorContext,XaConfFile.IOS_PLIST);

				}

				if (oMIOSController.getControllerType().equals(MIOSControllerType.FIXEDLISTVIEW)
						//Le contrôleur doit être marqué pour la génération
						//Autrement, on n'a pas besoin de générer le IOS_PLIST du détail (il n'y aura pas de vue)
						&& oMIOSController.hasCustomClass()  && oMIOSController.getCustomClass().isDoGeneration()) {
					// il faut générer aussi le detail
					oMIOSController.setControllerType(MIOSControllerType.FORMVIEW) ;
					xDoc = JaxbUtils.marshalToDocument(oMIOSController);
					sPlistFileName = StringUtils.join("form-", oFormViewController.getName(), StrUtils.DOT_S, PLIST_EXTENSION);
					oPListFile = new File("resources/plist/forms", sPlistFileName);
					log.debug("  generate: {}", oPListFile.getAbsolutePath());

					this.doXmlMergeGeneration(xDoc, XslTemplate.FORM_PLIST_TEMPLATE, oPListFile, p_oMProject, p_oGeneratorContext,XaConfFile.IOS_PLIST);
					oMIOSController.setControllerType(MIOSControllerType.FIXEDLISTVIEW) ;
				}
				else if(oMIOSController.getControllerType().equals(MIOSControllerType.LISTVIEW2D)) {		
					//On récupère le 2DListViewcontroller créé qui contient toutes les données nécessaires.
					MIOS2DListViewController oList2DViewcontroller = (MIOS2DListViewController)oFormViewController; 
//					String sOriginalName = o2DListViewcontroller.getName();
					List<MIOSSection> oSectionList = new ArrayList<MIOSSection>(oList2DViewcontroller.getSections());
					
					//Pour chaque section, on modifie le contrôleur courant avec des données spécifiques afin de
					// générer autant de fichiers IOS_PLIST (form et section) que nécessaire.
					int iIndex = oSectionList.size() -1;
					for(MIOSSection oSection : oSectionList) {
						oList2DViewcontroller.removeAllSections();
						oList2DViewcontroller.addSection(oSection);
						oList2DViewcontroller.setCellClassName(oList2DViewcontroller.getTypeNames()[iIndex]);
						xDoc = JaxbUtils.marshalToDocument(oMIOSController);
						sPlistFileName = StringUtils.join("form-", oList2DViewcontroller.getFormsTitles()[iIndex], StrUtils.DOT_S, PLIST_EXTENSION);
						oPListFile = new File("resources/plist/forms", sPlistFileName);
						log.debug("  generate: {}", oPListFile.getAbsolutePath());
						this.doXmlMergeGeneration(xDoc, XslTemplate.FORM_PLIST_TEMPLATE, oPListFile, p_oMProject, p_oGeneratorContext,XaConfFile.IOS_PLIST);
						
						iIndex--;
					}
					
					//On restaure les données de base du ViewController
					oList2DViewcontroller.setSections(oSectionList);
				}
			}
		}
	}

	/**
	 * Generate section plist files
	 * @param p_oMProject project to use
	 * @param p_oGeneratorContext generator context
	 * @throws Exception
	 */
	private void genSectionPList( XProject<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> p_oMProject,
			DomainGeneratorContext p_oGeneratorContext) throws Exception {

		for (MIOSController oMIOSController : p_oMProject.getDomain().getDictionnary().getAllIOSControllers()) {
			if ( oMIOSController.getControllerType().equals(MIOSControllerType.FORMVIEW) 
					|| oMIOSController.getControllerType().equals(MIOSControllerType.LISTVIEW)
					|| oMIOSController.getControllerType().equals(MIOSControllerType.LISTVIEW2D)
					|| oMIOSController.getControllerType().equals(MIOSControllerType.FIXEDLISTVIEW)
					|| oMIOSController.getControllerType().equals(MIOSControllerType.COMBOVIEW)
					|| oMIOSController.getControllerType().equals(MIOSControllerType.SEARCHVIEW)) {

				MIOSViewController oViewController = (MIOSViewController) oMIOSController;
				log.debug("genSectionPList: " + oMIOSController.getName());
				//Le IOS_PLIST de section n'est généré que si le contréleur associé est généré
				if (oViewController.hasCustomClass() && oViewController.getCustomClass().isDoGeneration()) {

					List<MIOSSection> oSectionList = new ArrayList<MIOSSection>(oViewController.getSections());
					for (MIOSSection oMIOSSection : oSectionList) {

						String sPlistFileName = StringUtils.join("section-", oMIOSSection.getName(), StrUtils.DOT_S, PLIST_EXTENSION);
						File oPListFile = new File("resources/plist/sections", sPlistFileName);

						if ( oMIOSSection.getController().getControllerType().equals(MIOSControllerType.FORMVIEW)
								|| oMIOSSection.getController().getControllerType().equals(MIOSControllerType.SEARCHVIEW)) {
							Document xDoc = JaxbUtils.marshalToDocument(oMIOSSection);
							this.doXmlMergeGeneration(xDoc, XslTemplate.SECTION_PLIST_TEMPLATE, oPListFile, p_oMProject, p_oGeneratorContext,XaConfFile.IOS_PLIST);
						}
						else if ( oMIOSController.getControllerType().equals(MIOSControllerType.FIXEDLISTVIEW)) {
							for(MIOSView  oSubview : oMIOSSection.getSubViews())
							{
								if(oSubview instanceof MIOSEditableView && 
										"MFCellComponentPickerList".equals(((MIOSEditableView)oSubview).getCellType()))
								{
									((MIOSEditableView) oSubview).setCustomParameterName("parentViewModel.parentViewModel." + ((MIOSEditableView) oSubview).getCustomParameterName());
								}							
							}

							Document xDoc = JaxbUtils.marshalToDocument(oMIOSSection);
							this.doXmlMergeGeneration(xDoc, XslTemplate.SECTION_PLIST_TEMPLATE, oPListFile, p_oMProject, p_oGeneratorContext,XaConfFile.IOS_PLIST);
						}
						else if ( oMIOSSection.getController().getControllerType() == MIOSControllerType.LISTVIEW ) {
							// list view has only one section, and we need some information on the controller to generate it
							Document xDoc = JaxbUtils.marshalToDocument(oMIOSController);
							this.doXmlMergeGeneration(xDoc, XslTemplate.SECTION_FOR_LISTITEM_PLIST_TEMPLATE, oPListFile, p_oMProject, p_oGeneratorContext,XaConfFile.IOS_PLIST);
						}
						else if ( oMIOSSection.getController().getControllerType() == MIOSControllerType.LISTVIEW2D ) {
							MIOS2DListViewController oTemporaryViewController = (MIOS2DListViewController) oViewController.clone();
							oTemporaryViewController.removeAllSections();
							oTemporaryViewController.addSection(oMIOSSection);
							Document xDoc = JaxbUtils.marshalToDocument(oTemporaryViewController);
							this.doXmlMergeGeneration(xDoc, XslTemplate.SECTION_FOR_LISTITEM_PLIST_TEMPLATE, oPListFile, p_oMProject, p_oGeneratorContext,XaConfFile.IOS_PLIST);
						}
					}
					oViewController.setSections(oSectionList);
				}
			}
			if ( oMIOSController.getControllerType().equals(MIOSControllerType.COMBOVIEW))
			{
				MIOSComboViewController oFormViewController = (MIOSComboViewController) oMIOSController ;
				Document xDoc = JaxbUtils.marshalToDocument(oMIOSController);
				
				if(oFormViewController.isSelectedItem())
				{
					String sPlistComboSelectedItemFileName = StringUtils.join("form-", oFormViewController.getSelectedItemCellClassName(), StrUtils.DOT_S, PLIST_EXTENSION);
					File oPlistComboSelectedItemFile = new File("resources/plist/forms", sPlistComboSelectedItemFileName);
					log.debug("  generate: {}", oPlistComboSelectedItemFile.getAbsolutePath());
					
					this.doXmlMergeGeneration(xDoc, XslTemplate.SECTION_FOR_COMBO_SELECTED_ITEM_PLIST_TEMPLATE, oPlistComboSelectedItemFile, p_oMProject, p_oGeneratorContext,XaConfFile.IOS_PLIST);
				}
				else
				{
					String sPListComboItemFileName = StringUtils.join("form-", oFormViewController.getItemCellClassName(), StrUtils.DOT_S, PLIST_EXTENSION);
					File oPListComboItemFile = new File("resources/plist/forms", sPListComboItemFileName);
					log.debug("  generate: {}", oPListComboItemFile.getAbsolutePath());
					
					this.doXmlMergeGeneration(xDoc, XslTemplate.SECTION_FOR_COMBO_ITEM_PLIST_TEMPLATE, oPListComboItemFile, p_oMProject, p_oGeneratorContext,XaConfFile.IOS_PLIST);
				}
			}
		}
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



