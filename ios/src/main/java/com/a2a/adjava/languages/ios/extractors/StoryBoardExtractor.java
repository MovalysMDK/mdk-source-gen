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
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.languages.ios.project.IOSUITypeDescription;
import com.a2a.adjava.languages.ios.xmodele.MIOSDictionnary;
import com.a2a.adjava.languages.ios.xmodele.MIOSDomain;
import com.a2a.adjava.languages.ios.xmodele.MIOSLabel;
import com.a2a.adjava.languages.ios.xmodele.MIOSModeleFactory;
import com.a2a.adjava.languages.ios.xmodele.MIOSScene;
import com.a2a.adjava.languages.ios.xmodele.MIOSStoryBoard;
import com.a2a.adjava.languages.ios.xmodele.MIOSVisualField;
import com.a2a.adjava.languages.ios.xmodele.MIOSXibComboContainer;
import com.a2a.adjava.languages.ios.xmodele.MIOSXibContainer;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOS2DListViewController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSControllerType;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSListViewController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSNavigationController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSViewController;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSEditableView;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSLabelView;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSSection;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSXibType;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.uml2xmodele.extractors.AbstractExtractor;
import com.a2a.adjava.uml2xmodele.extractors.ScreenExtractor;
import com.a2a.adjava.xmodele.MAction;
import com.a2a.adjava.xmodele.MActionType;
import com.a2a.adjava.xmodele.MDialog;
import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.MScreen;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.MVisualField;
import com.a2a.adjava.xmodele.ui.view.MVFLocalization;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;

/**
 * Story board extractor
 * @author lmichenaud
 *
 */
public class StoryBoardExtractor extends AbstractExtractor<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>> {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(StoryBoardExtractor.class);

	/**
	 * Label typeresources
	 */
	private static final String LABEL_TYPE = "UILabel";
	
	/**
	 * PhotoFixedList Cellidentifier
	 */
	private static final String PHOTO_COMPONENT_CELL = "PhotoFixedListItemCell";
	
	/**
	 * Stereotype to catch
	 */
	private static final String MM_IOS_SINGLE_CONTROLLER = "Mm_iOS_singleController";

	/**
	 * Delegate for fixed list use and creation of detail
	 */
	private MIOSFixedListDelegate fixedListDelegate ;

	/**
	 * Delegate for combo use and creation of detail
	 */
	private MIOSComboDelegate comboDelegate ;

	/**
	 * Delegate for 2D and 3D lists use and creation of detail
	 */
	private MIOSExpandableListDelegate expandableListDelegate ;

	/**
	 * Delegate for workspace use and creation of detail
	 */
	private MIOSWorkspaceDelegate workspaceDelegate;

	/**
	 * Delegate for multipanel use and creation of detail
	 */
	private MIOSMultiPanelDelegate multiPanelDelegate;

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.extractors.MExtractor#initialize(org.dom4j.Element)
	 */
	@Override
	public void initialize(Element p_xConfig) throws Exception {
		this.expandableListDelegate = new MIOSExpandableListDelegate( this );
		this.fixedListDelegate = new MIOSFixedListDelegate( this ) ;
		this.comboDelegate = new MIOSComboDelegate( this ) ;

		this.multiPanelDelegate = new MIOSMultiPanelDelegate(this);
		this.workspaceDelegate = new MIOSWorkspaceDelegate(this);
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.extractors.MExtractor#extract(com.a2a.adjava.uml.UmlModel)
	 */
	@Override
	public void extract(UmlModel p_oModele) throws Exception {

		ScreenConfig oScreenConfig = this.getScreenConfig();

		// Create storyboards from screens
		for( MScreen oScreen : this.getDomain().getDictionnary().getAllScreens()) {
			this.createStoryBoardFromScreen(oScreen);
		}

		// Add navigation links between storyboards
		for( MScreen oScreen : this.getDomain().getDictionnary().getAllScreens()) {
			IOSNavigationHelper.getInstance().computeNavigation(oScreen, oScreenConfig, this.getDomain());
		}


		for( MIOSStoryBoard oStoryboard : this.getDomain().getDictionnary().getAllIOSStoryBoards()) {
			this.expandableListDelegate.fixStoryboardGeneration(oStoryboard);
		}


		// Met à jour les hauteurs de chaque sous vue  de section contenant une fixed list
		for( MIOSXibContainer oXibContainer : this.getDomain().getDictionnary().getAllIOSXibContainers() ) {
			oXibContainer.computeComponentsPosition() ;
		}

		this.fixedListDelegate.updatePositionOfFixedList();
		this.comboDelegate.updatePositionOfCombo();
	}

	/**
	 * Create storyboard from screen
	 * @param p_oScreen screen
	 * @return storyboard
	 */
	protected MIOSStoryBoard createStoryBoardFromScreen( MScreen p_oScreen ) {

		log.debug("StoryBoardExtractor.createStoryBoardFromScreen : {}", p_oScreen.getName());

		MIOSModeleFactory oModeleFactory = this.getDomain().getXModeleFactory();

		MIOSStoryBoard r_oStoryBoard = null;

		// workspace create X Scene on one storyboard...
		if (p_oScreen.isWorkspace()) {

			r_oStoryBoard = this.workspaceDelegate.createStoryBoardFromScreen(p_oScreen);

		} else if (p_oScreen.isMultiPanel() && !p_oScreen.hasStereotype(MM_IOS_SINGLE_CONTROLLER)) {

			r_oStoryBoard = this.multiPanelDelegate.createStoryBoardFromScreen(p_oScreen);

		} else {
			r_oStoryBoard = oModeleFactory.createStoryBoard(p_oScreen.getUmlName());

			// that create one scene by screen
			MIOSScene oScene = this.createSceneFromScreen(p_oScreen, r_oStoryBoard);
			if ( oScene != null ) {

				// Add scene on storyboard
				r_oStoryBoard.setMainScene(oScene);

				//--------------------------------
				if ( p_oScreen.isMain()) {
					// Create a navigation controller for the container scene (if main screen)
					MIOSNavigationController oNavController = oModeleFactory.createNavigationController(p_oScreen.getUmlName(), 
							oScene.getController());

					// Create the scene for the container
					MIOSScene oContainerScene = oModeleFactory.createScene( StringUtils.join(p_oScreen.getUmlName(), "Container"),
							oNavController);

					// Define main controller on storyboard
					r_oStoryBoard.setMainController(oNavController);
					r_oStoryBoard.addScene(oContainerScene);

					oContainerScene.addLinkTo(oScene);
					oScene.addLinkFrom(oContainerScene);
				}
				else {

					// Define main controller on storyboard
					r_oStoryBoard.setMainController(oScene.getController());
				}
				//--------------------------------

				// compute scene positions
				IOSSceneHelper.getInstance().computeScenePositions(r_oStoryBoard);
			}
			//--------------------------------

			// compute scene positions
			IOSSceneHelper.getInstance().computeScenePositions(r_oStoryBoard);
		}


		// register story board in dictionary
		this.getDomain().getDictionnary().registerIOSStoryBoard(r_oStoryBoard, p_oScreen);

		return r_oStoryBoard ;
	}

	/**
	 * Create a scene from screen
	 * 
	 * @param p_oScreen screen
	 * @param p_oStoryboard storyboard to complete in order to add the scene of the detail view controller
	 * @return scene
	 */
	protected MIOSScene createSceneFromScreen( MScreen p_oScreen , MIOSStoryBoard p_oStoryboard ) {

		log.debug("StoryBoardExtractor.createSceneFromScreen : {}", p_oScreen.getName());
		log.debug("  master page : {}", p_oScreen.hasMasterPage());
		if ( p_oScreen.hasMasterPage()) {
			log.debug("  viewmodel type : {}", p_oScreen.getMasterPage().getViewModelImpl().getType());
		}
		MIOSScene r_oScreenScene = null ;

		MIOSModeleFactory oModelFactory = this.getDomain().getXModeleFactory();


		/*************************************************************************
		 *************************************************************************
		 *********** THIS CODE IS DUPLICATE IN THE MIOSContainerDelegate**********
		 *************************************************************************
		 * Ce code est appeler dans les delegates des contrainers pour "garder"  *
		 * les fonctionnalitées des listes2d, fixedlist, etc... dans les control-*
		 * -ler inclues dans les containers.									 *
		 * Dans le mellieur des cas il faut factoriser ce code sans pour autant  *
		 * changer l'implementation de cet extractor ni du model de donnée.		 *
		 * 																		 *
		 * Dans un premier temps et pour garentir le fonctionnement du code il   *
		 * faut copier le code present dans cette section dans la methode 		 *
		 * "createSceneFromPage" de la classe MIOSContainerDelegate et faire les *
		 * ajustement necessaire.												 *
		 *************************************************************************/
		// Form screen case
		if ( p_oScreen.hasMasterPage() 
				&& 
				( p_oScreen.getMasterPage().getViewModelImpl().getType().equals(ViewModelType.MASTER) 
						|| p_oScreen.getMasterPage().getViewModelImpl().getType().equals(ViewModelType.LIST_1)
						|| p_oScreen.getMasterPage().getViewModelImpl().getType().equals(ViewModelType.LIST_2)
						|| p_oScreen.getMasterPage().getViewModelImpl().getType().equals(ViewModelType.FIXED_LIST))
						&& !p_oScreen.isWorkspace()) {

			String sControllerName = this.computeControllerName(p_oScreen.getUmlName());

			// Create the view controller
			MIOSViewController oRootViewController = null;

			boolean bShouldRegisterPages = true;
			if ( p_oScreen.getMasterPage().getViewModelImpl().getType().equals(ViewModelType.MASTER)) {				
				oRootViewController = oModelFactory.createFormViewController(
						sControllerName, p_oScreen.getUmlName(), this.getSaveActionNames(p_oScreen));
			} else if (p_oScreen.getMasterPage().getViewModelImpl().getType().equals(ViewModelType.LIST_1) ){
				MIOSListViewController oListViewController = oModelFactory.createListViewController(sControllerName, p_oScreen.getUmlName());

				MViewModelImpl oItemVm = p_oScreen.getMasterPage().getViewModelImpl().getMasterSubViewModel();
				oListViewController.setItemViewModel(oItemVm.getName());
				oListViewController.setItemIdentifier(oItemVm.getIdentifier().getElems().get(0).getName());
				oListViewController.setNewItemButton(p_oScreen.getMasterPage().hasPanelOperation("create"));

				oRootViewController = oListViewController ;
			}
			else if (p_oScreen.getMasterPage().getViewModelImpl().getType().equals(ViewModelType.LIST_2) ){
				MIOS2DListViewController o2DListViewController = oModelFactory.create2DListViewController(sControllerName, p_oScreen.getUmlName());

				MViewModelImpl oItemVm = p_oScreen.getMasterPage().getViewModelImpl().getMasterSubViewModel();
				o2DListViewController.setItemViewModel(oItemVm.getSubViewModels().get(0).getSubViewModels().get(0).getName());
				o2DListViewController.setItemIdentifier(oItemVm.getIdentifier().getElems().get(0).getName());
				o2DListViewController.setNewItemButton(p_oScreen.getMasterPage().hasPanelOperation("create"));

				oRootViewController = o2DListViewController;
				bShouldRegisterPages = this.expandableListDelegate.createAdditionalSectionsForExpandableList(p_oStoryboard, p_oScreen.getPages().get(0), oRootViewController);		
			}

			boolean bVmOnScreen = false ;

			// Define viewmodel
			if (bShouldRegisterPages && p_oScreen.getViewModel() != null ) {
				bVmOnScreen = true ;
				oRootViewController.setViewModel(p_oScreen.getViewModel().getName());
			}

			// Comment for Screen
			oRootViewController.setIsInCommentScreen(p_oScreen.isComment());
			
			this.getDomain().getDictionnary().registerIOSController(oRootViewController);

			if(bShouldRegisterPages) {
				// Create a section for each page
				for( MPage oPage: p_oScreen.getPages()) {
					this.computeSection(p_oStoryboard, oRootViewController, oPage, oPage.getViewModelImpl(),  bVmOnScreen);
				}
			}

			this.fixedListDelegate.createSectionAndControllerForFixedList(p_oScreen, p_oStoryboard);	
			this.comboDelegate.createSectionAndControllerForCombo(p_oScreen, p_oStoryboard);			

			if(!oRootViewController.getIsInContainerViewController()) {
				this.addControllerTitleLabel(oRootViewController.getControllerType(), sControllerName, p_oScreen.getUmlName());
			}

			r_oScreenScene = this.createScene(oModelFactory, p_oScreen.getUmlName(), oRootViewController);
		}

		/*************************************************************************
		 *************************************************************************
		 ************************** END OF DUPLICATE CODE ************************
		 *************************************************************************
		 *************************************************************************/
		// Menu screen
		else if ( !p_oScreen.hasMasterPage()) {
			String sBaseName = p_oScreen.getUmlName();
			String sControllerName = this.computeControllerName(sBaseName);
			MIOSViewController oRootViewController = oModelFactory.createViewController(sControllerName, p_oScreen.getUmlName());
			this.addControllerTitleLabel(oRootViewController.getControllerType(), sControllerName, sBaseName);

			// Comment for Screen
			oRootViewController.setIsInCommentScreen(p_oScreen.isComment());

			this.getDomain().getDictionnary().registerIOSController(oRootViewController);

			r_oScreenScene = this.createScene(oModelFactory, p_oScreen.getUmlName(), oRootViewController);
		} else {
			log.debug("Not implemented");
		}

		return r_oScreenScene;
	}

	/**
	 * Creates a scene for a View Controller.
	 * The created scene could be specific for a  given View Controllert type
	 * @param p_oModelFactory The mode factory
	 * @param p_sSceneName The name of the screen
	 * @param p_oViewController The view controller
	 * @return The created scene
	 */
	public MIOSScene createScene(MIOSModeleFactory p_oModelFactory, String p_sSceneName, MIOSViewController p_oViewController) {
		MIOSScene oCreatedScene = null;
		if(p_oViewController.getControllerType().equals(MIOSControllerType.LISTVIEW2D)) {
			MIOSViewController r_oCloneViewController = p_oViewController.clone();
			ArrayList<MIOSSection> oSectionList = new ArrayList<MIOSSection>(p_oViewController.getSections());
			r_oCloneViewController.removeAllSections();
			r_oCloneViewController.addSection(oSectionList.get(1));
			oCreatedScene = p_oModelFactory.createScene(p_sSceneName, r_oCloneViewController);
		}
		else {
			oCreatedScene = p_oModelFactory.createScene(p_sSceneName, p_oViewController);
		}
		return oCreatedScene;

	}

	/**
	 * Compute section 
	 * @param p_oStoryboard the storyboard to process
	 * @param p_oViewController view controller
	 * @param p_oPage linked page
	 * @param p_oViewModel linked view model
	 * @param p_bViewModelOnscreen parent binding path
	 * @return the computed section
	 * @throws Exception 
	 */
	public MIOSSection computeSection(MIOSStoryBoard p_oStoryboard, MIOSViewController p_oViewController, MPage p_oPage, MViewModelImpl p_oViewModel, boolean p_bViewModelOnscreen ) {

		// Create the section for the page
		MIOSSection oIOSSection;
		if (p_oViewController.getControllerType().equals(MIOSControllerType.FIXEDLISTVIEW)) {
			oIOSSection = this.getDomain().getXModeleFactory().createSection(p_oViewController.getName()); // creer une section pour le detail 
			oIOSSection.setController(p_oViewController) ;
		} else if ( p_oViewController.getControllerType().equals(MIOSControllerType.COMBOVIEW)) {
			oIOSSection = this.getDomain().getXModeleFactory().createSection(p_oViewController.getName()); // creer une section pour le detail 
			oIOSSection.setController(p_oViewController) ;
		} else {
			oIOSSection = this.getDomain().getXModeleFactory().createSection(p_oPage.getName());
		}
		// Set titled visibility (Mm_title) of section
		oIOSSection.setTitled(p_oPage.isTitled());

		// Set viewmodel of section
		oIOSSection.setViewModel(p_oPage.getViewModelImpl().getMasterInterface().getName());

		// Si le ViewModel courant a un parent, on recupère son nom, et le nom de sa propriété dans le parent.
		// C'est notament utile dans le cas du Workspace où tous les ViewModels des colonnes ont pour parent
		// le viewModel du Workspace. On récupère alors sur cette colonne le ViewModel qui nous intéresse, via
		// la nom de la classe du ViewModel parent et le nom de la propriété du viewModel que l'on souhaite récupérer
		// dans le parent (construction d'un keyPath).
		if(p_oPage.getViewModelImpl().getParent() != null) {
			oIOSSection.setParentViewModelClass(p_oPage.getViewModelImpl().getParent().getName());
			oIOSSection.setViewModelAttributeInParent(StringUtils.uncapitalize(p_oPage.getViewModelImpl().getName()));
		}

		this.getDomain().getDictionnary().registerIOSSection(oIOSSection);
		p_oViewController.addSection(oIOSSection);

		this.computeFieldsOfSection(p_oViewController, p_oPage, p_oViewModel, oIOSSection , p_bViewModelOnscreen);

		// Compute the search panel if the controller is a LISTVIEW and has a Dialog
		if (p_oViewController.getControllerType().equals(MIOSControllerType.LISTVIEW)) {
			List<MDialog> lDialogs = p_oPage.getDialogs();
			if (!lDialogs.isEmpty()) {
				if (p_oViewController instanceof MIOSListViewController) {
					((MIOSListViewController) p_oViewController).setHasSearchViewController(true);
				}
				MIOSModeleFactory oModelFactory = this.getDomain().getXModeleFactory();
				MPage oSearchPanel = lDialogs.get(0);

				this.getDomain().getAnalyserAndProcessorFactory().createCUDActionProcessor().treatCUDOperationForSearchPanel(oSearchPanel, oSearchPanel.getUmlClass(),
						getDomain().getExtractor(ScreenExtractor.class).getScreenContext());
				List<MAction> lSaveActions = oSearchPanel.getActions();
				List<String> lSaveActionNames = new ArrayList<String>();
				for (MAction oAction : lSaveActions) {
					lSaveActionNames.add(oAction.getName());
				}

				MIOSViewController oSearchViewController = oModelFactory.createSearchViewController(
						this.computeControllerName(oSearchPanel.getName()),
						oSearchPanel.getName(), lSaveActionNames);
				((MIOSListViewController) p_oViewController).setSearchViewController(oSearchViewController.getName());
				this.getDomain().getDictionnary().registerIOSController(oSearchViewController);
				MIOSScene r_oScreenScene = oModelFactory.createScene(oSearchPanel.getName(), oSearchViewController);
				p_oStoryboard.addScene(r_oScreenScene);
				
				this.comboDelegate.createSectionAndControllerForCombo(oSearchPanel, p_oStoryboard);
				this.computeSection(p_oStoryboard, oSearchViewController, oSearchPanel, oSearchPanel.getViewModelImpl(), false);

			}
		}
		oIOSSection.setNoTable(p_oPage.hasStereotype(MPage.MM_IOS_NO_TABLE));

		return oIOSSection;
		//TODO: Action Button Not Implemented
	}

	/**
	 * Create the fields of a section in function of the controller type
	 * @param p_oViewController controller 
	 * @param p_oPage page of the storyboard
	 * @param p_oViewModel linked view model
	 * @param p_oIOSSection section of the model
	 * @param p_bViewModelOnscreen is the view model displayed in the 
	 */
	private void computeFieldsOfSection(MIOSViewController p_oViewController, MPage p_oPage, MViewModelImpl p_oViewModel, MIOSSection p_oIOSSection ,
			boolean p_bViewModelOnscreen ) {
		// Convert MVisualField to View 
		String sParentBindingPath = null;
		if ( p_bViewModelOnscreen ) {
			sParentBindingPath = StringUtils.uncapitalize(p_oPage.getViewModelImpl().getName());
		}

		if ( p_oViewController.getControllerType().equals(MIOSControllerType.FORMVIEW) || p_oViewController.getControllerType().equals(MIOSControllerType.SEARCHVIEW)) {

			for( MVisualField oVisualField : p_oPage.getLayout().getFields()) {
				//SPA à rajouter certainement pour éviter les doublons sur les listes if ( oVisualField.getLocalization() == MVFLocalization.DEFAULT ||  oVisualField.getLocalization() == MVFLocalization.DETAIL ) {
				this.computeFieldForFixedList(oVisualField, oVisualField.isReadOnly(), sParentBindingPath, p_oIOSSection, null);
			}
		} else if ( p_oViewController.getControllerType().equals(MIOSControllerType.FIXEDLISTVIEW)) {
			// for list controller, section is the item layout which is described in the first subview model.
			this.fixedListDelegate.computeFieldsOfFixedList( p_oPage , p_oViewModel, p_oIOSSection , sParentBindingPath);
		} else if ( p_oViewController.getControllerType().equals(MIOSControllerType.COMBOVIEW)) {
			// for list controller, section is the item layout which is described in the first subview model.
			this.comboDelegate.computeFieldsOfCombo( p_oPage , p_oViewModel,  p_oIOSSection , sParentBindingPath);
		} else if ( p_oViewController.getControllerType().equals(MIOSControllerType.LISTVIEW)) {

			// for list controller, section is the item layout which is described in the first subview model.
			for( MVisualField oVisualField : p_oPage.getViewModelImpl().getSubViewModels().get(0).getVisualFields()) {
				if ( oVisualField.getLocalization() == MVFLocalization.DEFAULT 
						|| oVisualField.getLocalization() == MVFLocalization.LIST ) {
					this.computeFieldForFixedList(oVisualField, oVisualField.isReadOnly(), sParentBindingPath, p_oIOSSection, null);
				}
			}
		}
		else if ( p_oViewController.getControllerType().equals(MIOSControllerType.LISTVIEW2D) ) {

			// for list controller, section is the item layout which is described in the first subview model.
			MViewModelImpl oCurrentViewModel =  p_oPage.getViewModelImpl();
			while(!oCurrentViewModel.getType().equals(ViewModelType.LISTITEM_1) 
				&& !oCurrentViewModel.getType().equals(ViewModelType.LISTITEM_2) 
				&& !oCurrentViewModel.getType().equals(ViewModelType.LISTITEM_3)) {
				if(oCurrentViewModel.getSubViewModels() != null && oCurrentViewModel.getSubViewModels().size() > 0) {
					oCurrentViewModel = oCurrentViewModel.getSubViewModels().get(0);
				}	
				else {
					break;
				}
			}
			for( MVisualField oVisualField : oCurrentViewModel.getVisualFields()) {
				if ( oVisualField.getLocalization() == MVFLocalization.DEFAULT 
						|| oVisualField.getLocalization() == MVFLocalization.LIST ) {
					this.computeField(oVisualField, oVisualField.isReadOnly(), sParentBindingPath, p_oIOSSection, null);
				}
			}
		}
	}	

	/**
	 * Create the label (if necessary) and the editable view in the section for a field in a section
	 * @param p_oVisualField visual field linked
	 * @param p_oReadOnly read only field
	 * @param p_sParentBindingPath parent binding path
	 * @param p_oIOSSection ios section containing the field
	 * @param p_oXibContainer xib container for fixed list 
	 */
	public void computeFieldForFixedList( MVisualField p_oVisualField, boolean p_oReadOnly, String p_sParentBindingPath, MIOSSection p_oIOSSection , 
			MIOSXibContainer p_oXibContainer ) {

		this.fixedListDelegate.modifyNameOfFieldForList(p_oVisualField,p_sParentBindingPath);
		this.computeField( p_oVisualField, p_oReadOnly, p_sParentBindingPath, p_oIOSSection , p_oXibContainer );
	}

	/**
	 * Create the label (if necessary) and the editable view in the section for a field in a section
	 * @param p_oVisualField visual field linked
	 * @param p_oReadOnly read only field
	 * @param p_sParentBindingPath parent binding path
	 * @param p_oIOSSection ios section containing the field
	 * @param p_oXibContainer xib container for combo 
	 */
	public void computeFieldForCombo( MVisualField p_oVisualField, boolean p_oReadOnly, String p_sParentBindingPath, MIOSSection p_oIOSSection , 
			MIOSXibContainer p_oXibContainer) {

		this.comboDelegate.modifyNameOfFieldForCombo(p_oVisualField,p_sParentBindingPath);
		this.computeField( p_oVisualField, p_oReadOnly, p_sParentBindingPath, p_oIOSSection , p_oXibContainer );
	}


	/**
	 * General method: Create the label (if necessary) and the editable view in the section for a field in a section
	 * @param p_oVisualField visual field linked
	 * @param p_oReadOnly read only field
	 * @param p_sParentBindingPath parent binding path
	 * @param p_oIOSSection ios section containing the field
	 * @param p_oXibContainer xib container of our component 
	 */
	public void computeField( MVisualField p_oVisualField, boolean p_oReadOnly, String p_sParentBindingPath, MIOSSection p_oIOSSection , 
			MIOSXibContainer p_oXibContainer ) {

		//		this.fixedListDelegate.modifyNameOfFieldForList(p_oVisualField,p_sParentBindingPath);

		MIOSLabelView  oLabelView = this.createLabelView(p_oVisualField, p_oXibContainer, p_oIOSSection) ;
		//		if (p_oVisualField.isCreateLabel()) {
		//			p_oIOSSection.addSubView(oLabelView);
		//		}

		MIOSEditableView oFieldView = this.createEditableView(p_oVisualField, p_oXibContainer, p_sParentBindingPath, oLabelView, p_oIOSSection) ;
		oFieldView.setReadOnly(p_oReadOnly);
		if(oFieldView.getLinkedType() != null && oFieldView.getLinkedType().equalsIgnoreCase(StoryBoardExtractor.PHOTO_COMPONENT_CELL)) {
			oFieldView.setHeight(158);
		}

		String sEnumClassName = p_oVisualField.getParameterValue("enum");
		if(sEnumClassName != null) {
			oFieldView.addOption("enumClassName", sEnumClassName);
		}
		
		if ( p_oXibContainer != null ) {
			if(oFieldView instanceof MIOSEditableView && 
					p_oXibContainer instanceof MIOSXibComboContainer)
			{
				((MIOSEditableView) oFieldView).setVisibleLabel(false);
				((MIOSEditableView) oFieldView).setUserInteractionEnabled(false);
			}

			MIOSEditableView oEditable = new MIOSEditableView();
			oFieldView.copyTo(oEditable);
			p_oXibContainer.addComponent( oEditable );
		}
		

		
		if(p_oXibContainer == null || !p_oXibContainer.getXibType().equals(MIOSXibType.EXPANDABLELISTSECTION)) {
			p_oIOSSection.addSubView(oFieldView);
		}
	}
	/**
	 * Create the editable view of a field in the section
	 * @param p_oVisualField linked editable field
	 * @param p_oXibContainer The XibContainer of the editable field
	 * @param p_sParentBindingPath  binding path in the view model
	 * @param p_oLabelView the view label describing the field
	 * @param p_oSection the linked section
	 * @return a new editable view 
	 */
	private MIOSEditableView createEditableView(MVisualField p_oVisualField, MIOSXibContainer p_oXibContainer, String p_sParentBindingPath, MIOSLabelView p_oLabelView, MIOSSection p_oSection) {
		MIOSEditableView r_oFieldView = new MIOSEditableView();
		r_oFieldView.setId(this.computeViewId(p_oVisualField.getFullName(), p_oSection.getName()));

		if ( this.fixedListDelegate.isFixedListField(p_oVisualField) ){
			this.fixedListDelegate.completeFieldsOfFixedList(p_oVisualField, r_oFieldView );
		}else if ( this.comboDelegate.isComboField(p_oVisualField) ){
			this.comboDelegate.completeFieldsOfCombo(p_oVisualField, r_oFieldView, p_sParentBindingPath ); 
		}else{
			r_oFieldView.setCustomClass(p_oVisualField.getComponent());
			r_oFieldView.setCellType(((MIOSVisualField)p_oVisualField).getCellType());
		}
		r_oFieldView.setBinding( this.extractBindingPathFromFieldName(p_oVisualField.getFullName(), p_sParentBindingPath, r_oFieldView.getBindingPrefix(), r_oFieldView.getBindingSuffix()));

		//La recherche se fait par le nom du type UML : en effet plusieurs type UML 
		//peuvent être utilisés pour le même composant 
		IOSUITypeDescription oUITypeDesc = (IOSUITypeDescription) this.getLngConfiguration().getUiTypeDescriptionByUmlName(p_oVisualField.getUmlName());

		if (oUITypeDesc!= null) {

			//Lecture seule
			if (p_oVisualField.isReadOnly()) {

				if (oUITypeDesc.getRoComponentWidth() > 0 ) {
					r_oFieldView.setWidth( oUITypeDesc.getRoComponentWidth());
				}
				if (oUITypeDesc.getRoComponentHeight() > 0 ) {
					r_oFieldView.setHeight(oUITypeDesc.getRoComponentHeight());
				}

			} else {

				if (oUITypeDesc.getRwComponentWidth() > 0 ) {
					r_oFieldView.setWidth( oUITypeDesc.getRwComponentWidth());
				}
				if (oUITypeDesc.getRwComponentHeight() > 0 ) {
					r_oFieldView.setHeight(oUITypeDesc.getRwComponentHeight());
				}

			}
		}
		r_oFieldView.setMandatory(p_oVisualField.isMandatory());		
		r_oFieldView.setVisibleLabel(p_oVisualField.isCreateLabel());
		r_oFieldView.setPropertyName(this.computePropertyName(p_oVisualField.getFullName()));
		r_oFieldView.setLabelView(p_oLabelView);
		r_oFieldView.setLocalization(p_oVisualField.getLocalization()) ;
		r_oFieldView.setMaxLength(p_oVisualField.getMaxLength());
		//		if ( p_oXibContainer != null ) {
		//			MIOSEditableView oEditable = new MIOSEditableView();
		//			r_oFieldView.copyTo(oEditable);
		//			p_oXibContainer.addComponent( oEditable );
		//		}

		String sEnumClassName = p_oVisualField.getParameterValue("enum");
		if(sEnumClassName != null) {
			r_oFieldView.addOption("enumClassName", sEnumClassName);
		}

		//Ajout des options spécifiques à ce type de vue
		if (oUITypeDesc != null) {

			//Lecture seule
			if (p_oVisualField.isReadOnly()) {

				if (oUITypeDesc.getROLanguageTypeOptions() != null) {
					r_oFieldView.setViewOptions(oUITypeDesc.getROLanguageTypeOptions());
				}

			} else {

				if (oUITypeDesc.getRWLanguageTypeOptions() != null) {
					r_oFieldView.setViewOptions(oUITypeDesc.getRWLanguageTypeOptions());
				}

			}
		}

		return r_oFieldView ;
	}
	/**
	 * Create the label view of a field in the section
	 * @param p_oVisualField linked field
	 * @param p_oSection the linked section
	 * @return a new label view for an editable field
	 */
	private MIOSLabelView createLabelView(MVisualField p_oVisualField, MIOSXibContainer p_oXibContainer, MIOSSection p_oSection) {

		MIOSLabelView r_oLabelView = new MIOSLabelView();
		IOSUITypeDescription oUITypeDesc = (IOSUITypeDescription) this.getLngConfiguration().getUiTypeDescriptionByComponentType("MFLabel");
		if (oUITypeDesc!= null) {
			if (oUITypeDesc.getRwComponentWidth() > 0 ) {
				r_oLabelView.setWidth( oUITypeDesc.getRwComponentWidth());
			}
			if (oUITypeDesc.getRwComponentHeight() > 0 ) {
				r_oLabelView.setHeight(oUITypeDesc.getRwComponentHeight());
			}
		}
		String sViewId = this.computeViewId(p_oVisualField.getLabel().getKey(), p_oSection.getName());

		r_oLabelView.setId(sViewId);
		r_oLabelView.setCustomClass(this.getLabelType());
		r_oLabelView.setValue(p_oVisualField.getLabel().getKey());
		r_oLabelView.setPropertyName( this.computePropertyName(p_oVisualField.getLabel().getKey()));
		r_oLabelView.setLocalization(p_oVisualField.getLocalization()) ;

		//		if ( p_oXibContainer != null ) {
		//			MIOSLabelView oEditableLabel = new MIOSLabelView();
		//			r_oLabelView.copyTo(oEditableLabel);
		//			p_oXibContainer.addComponent( oEditableLabel );
		//		}

		this.getDomain().getDictionnary().addLabel(p_oVisualField.getLabel());

		return r_oLabelView  ;
	}

	/**
	 * Compute controller name
	 * @param p_sBaseName base name
	 * @return controller name
	 */
	public String computeControllerName( String p_sBaseName ) {

		StringBuilder r_oControllerName = new StringBuilder(this.getPrefixForControllerName());
		r_oControllerName.append(p_sBaseName);
		r_oControllerName.append(this.getSuffixForControllerName());
		return r_oControllerName.toString();
	}

	/**
	 * Adds a title to a controller
	 * @param p_oControllerType controller to compute
	 * @param p_sBaseKeyName base key name to use
	 * @param p_sBaseName base name to use
	 */
	public void addControllerTitleLabel (MIOSControllerType p_oControllerType, String p_sBaseKeyName, String p_sBaseName ) {
		MIOSLabel oLabel = 
				getDomain().getXModeleFactory().createLabelForController(p_oControllerType, p_sBaseKeyName, p_sBaseName);
		if ( oLabel != null ) {
			this.getDomain().getDictionnary().addLabel(oLabel);
		}
	}

	/**
	 * Extract binding path from field name
	 * @param p_sFieldName name
	 * @param p_sParentBindingPath parent binding path
	 * @param p_sFieldPrefix prefix to use to compute name
	 * @param p_sFieldSuffix suffix to use to compute name
	 * @return binding path
	 */
	public String extractBindingPathFromFieldName( String p_sFieldName, String p_sParentBindingPath, String p_sFieldPrefix, String p_sFieldSuffix ) {
		// définier une autre méthode pour les champs de différents commençant par lstVm
		String r_sBinding = null ;
		String[] r_sIdParts = p_sFieldName.split("__");

		//Traitement de la bindingKey en fonction des Ids du composant
		if ( r_sIdParts != null ) {
			StringBuilder sBindingPath = new StringBuilder();
			if ( p_sParentBindingPath != null ) {
				sBindingPath.append(p_sParentBindingPath);
				sBindingPath.append('.');
			}
			if(p_sFieldPrefix != null) {
				sBindingPath.append(p_sFieldPrefix);
			}
			if ( r_sIdParts.length == 2 ) {
				sBindingPath.append(r_sIdParts[0]);
			} else {
				sBindingPath.append(r_sIdParts[1]);
			}
			if(p_sFieldSuffix != null) {
				sBindingPath.append(p_sFieldSuffix);
			}
			r_sBinding = sBindingPath.toString();
		}

		//Traitement de la bindingKey en fonction des sousViewModel
		String[] r_sVMParts = r_sBinding.split("_");
		if(r_sVMParts != null && r_sVMParts.length > 1 ) {
			StringBuilder sBindingPath = new StringBuilder();
			for(String sVmPath : r_sVMParts) {
				if(!r_sVMParts[r_sVMParts.length-1].equals(sVmPath)) {
					sBindingPath.append(sVmPath);
					sBindingPath.append('.');
				}
				else {
					sBindingPath.append(sVmPath);
				}
			}
			r_sBinding = sBindingPath.toString();
		}
		return r_sBinding ;
	}

	/**
	 * Return suffix for controller name
	 * 
	 * @return suffix for controller name
	 */
	public String getPrefixForControllerName() {
		String r_sSuffix = this.getParameterValue("controller-naming-prefix");
		if ( r_sSuffix == null ) {
			r_sSuffix = StringUtils.EMPTY;
		}
		return r_sSuffix;
	}

	/**
	 * Return suffix for controller name
	 * 
	 * @return suffix for controller name
	 */
	public String getSuffixForControllerName() {
		String r_sSuffix = this.getParameterValue("controller-naming-suffix");
		if ( r_sSuffix == null ) {
			r_sSuffix = StringUtils.EMPTY;
		}
		return r_sSuffix;
	}

	/**
	 * Returns the name of the save actions of the screen
	 * @param p_oScreen linked screen
	 * @return a list of names
	 */
	public List<String> getSaveActionNames(MScreen p_oScreen) {
		List<String> r_listActionNames = new ArrayList<String>();
		for( MPage oPage : p_oScreen.getPages()) {
			MAction oCurrentAction = oPage.getActionOfType(MActionType.SAVEDETAIL);
			if (null != oCurrentAction) {
				r_listActionNames.add(oPage.getActionOfType(MActionType.SAVEDETAIL).getName());
			}
		}
		return r_listActionNames;
	}

	/**
	 * Get label type
	 * @return label type
	 */
	protected String getLabelType() {
		return LABEL_TYPE;
	}

	/**
	 * Compute property name from visual field
	 * @param p_sVisualFieldName visual field name
	 * @return property name
	 */
	private String computePropertyName( String p_sVisualFieldName ) {
		String r_sPropertName = null ;
		String[] t_sSplittedName = p_sVisualFieldName.split("__");
		if ( t_sSplittedName.length == 2 ) {
			r_sPropertName = t_sSplittedName[0] + StringUtils.capitalize(t_sSplittedName[1]);
		} else if (t_sSplittedName.length == 3) {
			r_sPropertName = t_sSplittedName[1] + StringUtils.capitalize(t_sSplittedName[2]);
		}
		return r_sPropertName ;
	}

	/**
	 * Compute label value from visual field
	 * @param p_sVisualFieldName visual field name
	 * @return label value
	 */
	private String computeLabelValue( String p_sVisualFieldName ) {
		String r_sLabelValue = null ;
		String[] t_sSplittedName = p_sVisualFieldName.split("__");
		if ( t_sSplittedName.length == 2 ) {
			r_sLabelValue = t_sSplittedName[0];
		} else if (t_sSplittedName.length == 3) {
			r_sLabelValue = t_sSplittedName[1] ;
		}
		return r_sLabelValue ;
	}

	/**
	 * Return screen config
	 * @return screen config
	 */
	private ScreenConfig getScreenConfig() {
		return new ScreenConfig(this.getIntParameterValue("screen-height"), this.getIntParameterValue("screen-width"));
	}

	/**
	 * Compute view id
	 * @param p_sBaseName button id
	 * @param p_sSectionNameContainer the section name
	 * @return name for ios button
	 */
	private String computeViewId( String p_sBaseName, String p_sSectionNameContainer) {
		String sBaseName = StringUtils.join(p_sSectionNameContainer, "-", p_sBaseName);
		String r_sViewId = sBaseName.replaceAll("__", "-");
		return r_sViewId.replaceAll("_", "-");
	}

	/**
	 * Getter for the fixed list delegate
	 * @return the fixed list delegate
	 */
	public MIOSFixedListDelegate getFixedListDelegate() {
		return this.fixedListDelegate;
	}

	/**
	 * returns the expandable list delegate
	 * @return the expandable list delegate
	 */
	public MIOSExpandableListDelegate getExpandableListDelegate() {
		return this.expandableListDelegate;
	}

	/**
	 * Return the combo delegate
	 * @return the combo delegate
	 */
	public MIOSComboDelegate getComboDelegate() {
		return this.comboDelegate;
	}
}
