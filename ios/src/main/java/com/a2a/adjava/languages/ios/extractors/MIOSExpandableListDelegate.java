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
package com.a2a.adjava.languages.ios.extractors;

import org.apache.commons.lang3.StringUtils;

import com.a2a.adjava.languages.ios.xmodele.MIOSScene;
import com.a2a.adjava.languages.ios.xmodele.MIOSStoryBoard;
import com.a2a.adjava.languages.ios.xmodele.MIOSXibContainer;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOS2DListViewController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSControllerType;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSSectionType;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSViewController;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSSection;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSXibType;
import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.MVisualField;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;

/**
 * Delegate for the fixed list management
 * @author qlagarde
 */
public class MIOSExpandableListDelegate {

	/**
	 * Extension for multi section page
	 */
	private static final String MULTI_LIST_SECTION_PAGE_EXTENSION = "Section";

	/**
	 * Delegator
	 */
	private StoryBoardExtractor delegator ;

	/**
	 * Constructor
	 * @param p_oDelegator storyboard extractor delegator
	 */
	public MIOSExpandableListDelegate( StoryBoardExtractor p_oDelegator) {
		this.delegator = p_oDelegator ;
	}

	/**
	 * Create the section and view controller for the fixed list in the sub view models of the screen pages
	 * @param p_oStoryBoard storyboard to complete to add the scene of the detail view controller
	 * @param p_oMainPage page to search fixed list in 
	 * @param p_oFormExpandableListViewController The expandable list view controller that will be used to generate
	 * @return false if the method created the additionnal sections
	 */
	public boolean createAdditionalSectionsForExpandableList(MIOSStoryBoard p_oStoryBoard, MPage p_oMainPage , MIOSViewController p_oFormExpandableListViewController ) {
		if(p_oFormExpandableListViewController.getControllerType().equals(MIOSControllerType.LISTVIEW2D)) {
			MIOS2DListViewController oList2DViewcontroller = (MIOS2DListViewController) p_oFormExpandableListViewController;
			
			MViewModelImpl oCurrentViewModelImpl = p_oMainPage.getViewModelImpl();
			oList2DViewcontroller.removeAllSections();

			oList2DViewcontroller.setViewModel(oCurrentViewModelImpl.getName());
			for (int iIndex = 0 ; iIndex < 2 ; iIndex ++) {
				while(!oCurrentViewModelImpl.getType().equals(ViewModelType.LIST_1) 
					&& !oCurrentViewModelImpl.getType().equals(ViewModelType.LIST_2)) {
					oCurrentViewModelImpl = oCurrentViewModelImpl.getSubViewModels().get(0);
				}
				MPage oPageToRegister = new MPage(p_oMainPage.getParent(),
						StringUtils.join(p_oMainPage.getName(), this.getPageSuffixes()[iIndex]),
						p_oMainPage.getUmlClass(),
						p_oMainPage.getPackage(),
						oCurrentViewModelImpl,
						false);		
				MIOSSection oCreatedSection = this.delegator.computeSection(p_oStoryBoard, oList2DViewcontroller, oPageToRegister, oCurrentViewModelImpl, false);
				this.defineSectionType(oCreatedSection, oPageToRegister);
				if(oCurrentViewModelImpl.getType().equals(ViewModelType.LIST_2) ) {
					this.computeFieldsOfExpandableList( oPageToRegister, oCreatedSection , oList2DViewcontroller.getSectionFormName());
				}
				if(oCurrentViewModelImpl.getSubViewModels().size() > 0) {
					oCurrentViewModelImpl = oCurrentViewModelImpl.getSubViewModels().get(0);
				}
			}
			return false;
		}
		return true;
	}

	/**
	 * Computes labels of the visual fields stored in the page
	 * @param p_oPage page hosting the visual fields
	 * @param p_oIOSSection scetion hosting the visual fields
	 * @param p_sParentBindingPath parent binding path
	 */
	private void computeFieldsOfExpandableList(MPage p_oPage , MIOSSection p_oIOSSection , String p_sParentBindingPath) {

		MIOSXibContainer oXibContainer = null;

		//Si le XIB de cellule de la fixed list doit être généré
		oXibContainer = this.delegator.getDomain().getXModeleFactory().createXibContainer(p_oPage, p_oPage.getViewModelImpl(), p_oIOSSection.getController());
		oXibContainer.setXibType(MIOSXibType.EXPANDABLELISTSECTION);

		for( MVisualField oVisualField : p_oPage.getViewModelImpl().getSubViewModels().get(0).getVisualFields()) {
			this.delegator.computeField(oVisualField, oVisualField.isReadOnly(), p_sParentBindingPath, p_oIOSSection,oXibContainer);
		}

		this.delegator.getDomain().getDictionnary().registerIOSXibContainer(oXibContainer);

	}

	/**
	 * Page name suffixes
	 * @return Page name suffixes
	 */
	private String[] getPageSuffixes() {
		return new String[]{MULTI_LIST_SECTION_PAGE_EXTENSION, ""};
	}

	/**
	 * Defines the type of a section based on the view model of the hosting page
	 * @param p_oSection scetion to analyse
	 * @param p_oOriginalPage page hosting the section
	 */
	private void defineSectionType(MIOSSection p_oSection, MPage p_oOriginalPage) {

		if(p_oOriginalPage.getViewModelImpl().getType().equals(ViewModelType.LIST_3)) {
			p_oSection.setSectionType(MIOSSectionType.LISTHEADER);
		}
		else if(p_oOriginalPage.getViewModelImpl().getType().equals(ViewModelType.LIST_2)) {
			p_oSection.setSectionType(MIOSSectionType.LISTSECTION);
		}
		else if(p_oOriginalPage.getViewModelImpl().getType().equals(ViewModelType.LIST_1)) {
			p_oSection.setSectionType(MIOSSectionType.LISTCELL);
		}
		else {
			p_oSection.setSectionType(MIOSSectionType.FORM);
		}
	}

	/**
	 * This method fix the sections generation problem for 2D/3D lists.
	 * We're actually not able to clone the ViewController used to generate storyboard and
	 * custom class of controllers before this treatment. 
	 * The clone is done here to remove unused sections for storyboard generation.
	 * @param p_oStoryboard The storyboard that we want to fix.
	 */
	public void fixStoryboardGeneration(MIOSStoryBoard p_oStoryboard) {
		MIOSScene oMainScene = p_oStoryboard.getMainScene();
		MIOSViewController oMainViewController = (MIOSViewController) oMainScene.getController();

		if(oMainViewController instanceof MIOS2DListViewController) {
			MIOS2DListViewController oCloneViewController = ((MIOS2DListViewController) oMainViewController).clone();
				for(MIOSSection oCurrentSection : oMainViewController.getSections()) {
					if(!oCurrentSection.getSectionType().equals(MIOSSectionType.LISTCELL)) {
						oCloneViewController.removeSection(oCurrentSection);
					}
				}
			
			oMainScene.setController(oCloneViewController);
		}
	}
}
