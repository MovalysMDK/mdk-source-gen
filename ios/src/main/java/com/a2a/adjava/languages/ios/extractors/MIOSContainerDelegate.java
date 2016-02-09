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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.a2a.adjava.languages.ios.xmodele.MIOSModeleFactory;
import com.a2a.adjava.languages.ios.xmodele.MIOSScene;
import com.a2a.adjava.languages.ios.xmodele.MIOSStoryBoard;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOS2DListViewController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSListViewController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSViewController;
import com.a2a.adjava.xmodele.MActionType;
import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.MScreen;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;

public abstract class MIOSContainerDelegate {

	
	/**
	 * Delegator
	 */
	protected StoryBoardExtractor delegator ;
	/**
	 * model Factory
	 */
	protected MIOSModeleFactory modelFactory;
	/**
	 * controller container
	 */
	protected MIOSViewController containerMainController;
;


	/**
	 * Constructer
	 * @param p_storyBoardExtractor
	 */
	public MIOSContainerDelegate(StoryBoardExtractor p_storyBoardExtractor) {
		this.delegator = p_storyBoardExtractor;
		this.modelFactory = this.delegator.getDomain().getXModeleFactory();
	}
	
	
	protected MIOSStoryBoard createStoryBoardFromScreen(MScreen p_oScreen) {
		MIOSStoryBoard r_oStoryBoard = this.modelFactory
				.createStoryBoard(p_oScreen.getUmlName());

		// fake scene for MFWorksaceViewController
		MIOSScene oMainScene = this.createContainerScene(p_oScreen);
		r_oStoryBoard.setMainController(oMainScene.getController());
		r_oStoryBoard.setMainScene(oMainScene);
		
		// In Workspace each page is a ViewController (not a Section)
		for (MPage oPage : p_oScreen.getPages()) {
			MIOSScene oScene = this.createSceneFromPage(oPage, null, r_oStoryBoard);
			r_oStoryBoard.addScene(oScene);
			
		}
		
		return r_oStoryBoard;
	}
	
	/**
	 * create the main scene for the storyboard
	 * @param p_oScreen
	 * @return the main scene
	 */
	protected abstract MIOSScene createContainerScene(MScreen p_oScreen);
	
	protected MIOSScene createSceneFromPages(Collection<MPage> p_oPages,
			MIOSStoryBoard p_oStoryBoard) {
		Iterator<MPage> oPageIterator = p_oPages.iterator();
		MPage firstPage = oPageIterator.next();
		
		MIOSScene r_oScene = createSceneFromPage(firstPage, oPageIterator, p_oStoryBoard);
		
		return r_oScene;
	}
	
	public String computeControllerName( MPage p_oPage ) {
		String sBaseName = p_oPage.getUmlName();
		String sComputedName = this.delegator.computeControllerName(sBaseName);
		return sComputedName;
	}
	

	
	protected MIOSScene createSceneFromPage(MPage p_oPage, Iterator<MPage> nextSections, MIOSStoryBoard p_oStoryBoard) {
		MIOSScene r_oScreenScene = null;

		String sControllerName = this.computeControllerName(p_oPage);

		/*************************************************************************
		 *************************************************************************
		 ********************* SAME CODE IN StoryBoardExtractor ******************
		 *************************************************************************
		 * Le duplicata est "presque" le même que celui du storyboardExtractor   *
		 * à la différence qu'il est appelé avec comme parametre la page courante*
		 * et non l'écran.														 *
		 *************************************************************************/
		boolean bShouldRegisterPages = true;
		// Create the view controller
		MIOSViewController oRootViewController = null;
		List<String> actions = new ArrayList<String>();
		actions = new ArrayList<String>();

		if ( p_oPage.getViewModelImpl().getType().equals(ViewModelType.MASTER)) {		
			
			if (p_oPage.getActionOfType(MActionType.SAVEDETAIL) != null) {
				actions.add(p_oPage.getActionOfType(MActionType.SAVEDETAIL)
						.getName());
			}
			
			oRootViewController = this.modelFactory.createFormViewController(
					sControllerName, p_oPage.getUmlName(), actions);
			
		} else if (p_oPage.getViewModelImpl().getType().equals(ViewModelType.LIST_1) ){
			MIOSListViewController oListViewController = this.modelFactory.createListViewController(sControllerName, p_oPage.getUmlName());

			MViewModelImpl oItemVm = p_oPage.getViewModelImpl().getMasterSubViewModel();
			oListViewController.setItemViewModel(oItemVm.getName());
			oListViewController.setItemIdentifier(oItemVm.getIdentifier().getElems().get(0).getName());
			oListViewController.setNewItemButton(p_oPage.hasPanelOperation("create"));

			oRootViewController = oListViewController ;
		}
		else if (p_oPage.getViewModelImpl().getType().equals(ViewModelType.LIST_2) ){
			MIOS2DListViewController o2DListViewController = this.modelFactory.create2DListViewController(sControllerName, p_oPage.getUmlName());

			MViewModelImpl oItemVm = p_oPage.getViewModelImpl().getMasterSubViewModel();
			o2DListViewController.setItemViewModel(oItemVm.getSubViewModels().get(0).getSubViewModels().get(0).getName());
			o2DListViewController.setItemIdentifier(oItemVm.getIdentifier().getElems().get(0).getName());
			o2DListViewController.setNewItemButton(p_oPage.hasPanelOperation("create"));

			oRootViewController = o2DListViewController ;

			bShouldRegisterPages = this.delegator.getExpandableListDelegate().createAdditionalSectionsForExpandableList(p_oStoryBoard, p_oPage, oRootViewController);		
		}
		doAfterCreateController(oRootViewController, p_oPage.getViewModelImpl().getType());
		
		oRootViewController.setIsInContainerViewController(true);
		
		// Define viewmodel
		if (bShouldRegisterPages && p_oPage.getViewModelImpl() != null ) {
			oRootViewController.setViewModel(p_oPage.getViewModelImpl().getName());
		}
		
		this.delegator.getDomain().getDictionnary().registerIOSController(oRootViewController);

		//create the connection beetwin the main controller and the differentes pages 
		this.modelFactory.createSegueConnection(this.containerMainController, oRootViewController);
		

		r_oScreenScene = this.delegator.createScene(this.modelFactory, p_oPage.getUmlName() , oRootViewController);
		

		// Create a section for each page
		if(bShouldRegisterPages) {
			this.delegator.computeSection(p_oStoryBoard, oRootViewController, p_oPage, p_oPage.getViewModelImpl(), this.isViewModelInGlobalViewModelForFirstSection(p_oPage));
		}
		//Dans le cas du  Workspace uniquement :
		//Dans le cas du MultiSection multi controller, on n'a qu'une section par controller
		//Dans le  du workspace, seule la première section de la première page peut être de type LIST,LSIT2D ou LIST3D,
		//donc le booléen bShouldRegisterPages (indique si on a déja traité le cas des section pour une liste 2D ou 3D) .
		//n'a pas d'influence dans le cas des autres section (nextSections)
		while (nextSections != null && nextSections.hasNext()) {
			MPage oPage = (MPage) nextSections.next();
			this.delegator.computeSection(p_oStoryBoard, oRootViewController, oPage, oPage.getViewModelImpl(), this.isViewModelInGlobalViewModelForOtherSection(p_oPage));
			if(oPage.getActionOfType(MActionType.SAVEDETAIL) != null) {
				actions.add(oPage.getActionOfType(MActionType.SAVEDETAIL)
						.getName());
			}
		}
		
		
		this.delegator.getFixedListDelegate().createSectionAndControllerForFixedList(p_oPage, p_oStoryBoard);	
		this.delegator.getComboDelegate().createSectionAndControllerForCombo(p_oPage, p_oStoryBoard);
		
		/*************************************************************************
		 *************************************************************************
		 ******************** END SAME CODE IN StoryBoardExtractor ***************
		 *************************************************************************
		 *************************************************************************/

		return r_oScreenScene;
	}
	
	public boolean isViewModelInGlobalViewModelForFirstSection(MPage p_oPage) {
		return false;
	}
	
	public boolean isViewModelInGlobalViewModelForOtherSection(MPage p_oPage) {
		return false;
	}

	protected abstract void doAfterCreateController(MIOSViewController oRootViewController, ViewModelType type);
	
	
}
