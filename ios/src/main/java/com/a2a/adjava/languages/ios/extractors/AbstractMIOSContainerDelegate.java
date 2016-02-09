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

/**
 * ios delegate to create containers
 */
public abstract class AbstractMIOSContainerDelegate {

	
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


	/**
	 * Constructer
	 * @param p_storyBoardExtractor storyboard to use to create the class instance
	 */
	public AbstractMIOSContainerDelegate(StoryBoardExtractor p_storyBoardExtractor) {
		this.delegator = p_storyBoardExtractor;
		this.modelFactory = this.delegator.getDomain().getXModeleFactory();
	}
	
	/**
	 * Creates a storyboard from a scene
	 * @param p_oScreen scene to use to create the storyboard
	 * @return the created storyboard
	 */
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
	 * @param p_oScreen screen from which we create storyboard
	 * @return the main scene
	 */
	protected abstract MIOSScene createContainerScene(MScreen p_oScreen);
	
	/**
	 * Creates the scene for a list of pages
	 * @param p_oPages pages from which we create the scene
	 * @param p_oStoryBoard storyboard hosting the scene to create
	 * @return the created scene
	 */
	protected MIOSScene createSceneFromPages(Collection<MPage> p_oPages,
			MIOSStoryBoard p_oStoryBoard) {
		Iterator<MPage> oPageIterator = p_oPages.iterator();
		MPage oFirstPage = oPageIterator.next();
		
		MIOSScene r_oScene = createSceneFromPage(oFirstPage, oPageIterator, p_oStoryBoard);
		
		return r_oScene;
	}
	
	/**
	 * Computes the name of the controller to create
	 * @param p_oPage page hosting the controller
	 * @return the controller name computed
	 */
	public String computeControllerName( MPage p_oPage ) {
		String sBaseName = p_oPage.getUmlName();
		return this.delegator.computeControllerName(sBaseName);
	}
	
	/**
	 * Create an ios scene from a page
	 * @param p_oPage page to use to create scene
	 * @param p_oNextSections sections to use to create scene
	 * @param p_oStoryBoard storyboard to use to create scene
	 * @return the created scene
	 */
	protected MIOSScene createSceneFromPage(MPage p_oPage, Iterator<MPage> p_oNextSections, MIOSStoryBoard p_oStoryBoard) {
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
		List<String> lActions = new ArrayList<String>();
		
		if ( p_oPage.getViewModelImpl().getType().equals(ViewModelType.MASTER)) {		
			
			if (p_oPage.getActionOfType(MActionType.SAVEDETAIL) != null) {
				lActions.add(p_oPage.getActionOfType(MActionType.SAVEDETAIL)
						.getName());
			}
			
			oRootViewController = this.modelFactory.createFormViewController(
					sControllerName, p_oPage.getUmlName(), lActions);
			
		} else if (p_oPage.getViewModelImpl().getType().equals(ViewModelType.LIST_1) ){
			MIOSListViewController oListViewController = this.modelFactory.createListViewController(sControllerName, p_oPage.getUmlName());

			MViewModelImpl oItemVm = p_oPage.getViewModelImpl().getMasterSubViewModel();
			oListViewController.setItemViewModel(oItemVm.getName());
			oListViewController.setItemIdentifier(oItemVm.getIdentifier().getElems().get(0).getName());
			oListViewController.setNewItemButton(p_oPage.hasPanelOperation("create"));

			oRootViewController = oListViewController ;
		}
		else if (p_oPage.getViewModelImpl().getType().equals(ViewModelType.LIST_2) ){
			MIOS2DListViewController oList2DViewController = this.modelFactory.create2DListViewController(sControllerName, p_oPage.getUmlName());

			MViewModelImpl oItemVm = p_oPage.getViewModelImpl().getMasterSubViewModel();
			oList2DViewController.setItemViewModel(oItemVm.getSubViewModels().get(0).getSubViewModels().get(0).getName());
			oList2DViewController.setItemIdentifier(oItemVm.getIdentifier().getElems().get(0).getName());
			oList2DViewController.setNewItemButton(p_oPage.hasPanelOperation("create"));

			oRootViewController = oList2DViewController ;

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
		while (p_oNextSections != null && p_oNextSections.hasNext()) {
			MPage oPage = (MPage) p_oNextSections.next();
			this.delegator.computeSection(p_oStoryBoard, oRootViewController, oPage, oPage.getViewModelImpl(), this.isViewModelInGlobalViewModelForOtherSection(p_oPage));
			if(oPage.getActionOfType(MActionType.SAVEDETAIL) != null) {
				lActions.add(oPage.getActionOfType(MActionType.SAVEDETAIL)
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
	
	/**
	 * Returns true if the view model is on the first section
	 * @param p_oPage page hosting the view model
	 * @return true if the view model is on the first section
	 */
	public boolean isViewModelInGlobalViewModelForFirstSection(MPage p_oPage) {
		return false;
	}
	
	/**
	 * Returns true if the view model is on an other section than the first one
	 * @param p_oPage page hosting the view model
	 * @return if view model is in another section
	 */
	public boolean isViewModelInGlobalViewModelForOtherSection(MPage p_oPage) {
		return false;
	}

	/**
	 * Callback on controller creation
	 * @param p_oRootViewController controller created
	 * @param p_oType type of the viewmodel linked to the controller
	 */
	protected abstract void doAfterCreateController(MIOSViewController p_oRootViewController, ViewModelType p_oType);
	
	
}
