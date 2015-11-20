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

import java.beans.Introspector;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.languages.ios.xmodele.MIOSDictionnary;
import com.a2a.adjava.languages.ios.xmodele.MIOSModeleFactory;
import com.a2a.adjava.languages.ios.xmodele.MIOSScene;
import com.a2a.adjava.languages.ios.xmodele.MIOSStoryBoard;
import com.a2a.adjava.languages.ios.xmodele.MIOSVisualField;
import com.a2a.adjava.languages.ios.xmodele.MIOSXibContainer;
import com.a2a.adjava.languages.ios.xmodele.MIOSXibFixedListContainer;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSControllerType;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSFixedListViewController;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSEditableView;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSSection;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSView;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSXibType;
import com.a2a.adjava.uml2xmodele.extractors.viewmodel.VMListPathProcessor;
import com.a2a.adjava.uml2xmodele.extractors.viewmodel.VMNamingHelper;
import com.a2a.adjava.utils.VersionHandler;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.MLinkedInterface;
import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.MScreen;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.MViewModelInterface;
import com.a2a.adjava.xmodele.MVisualField;
import com.a2a.adjava.xmodele.ui.view.MVFLocalization;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelTypeConfiguration;

/**
 * Delegate for the fixed list management
 * @author spacreau
 */
public class MIOSFixedListDelegate {
	
	/**
	 * Key to save the name of the detail view controller 
	 */
	public static final String FIXED_LIST_DETAIL_VIEW_CONTROLLER_NAME = "screenDetailViewControllerName" ;

	/**
	 * Key to save the name of the detail view controller 
	 */
	public static final String FIXED_LIST_DETAIL_STORYBOARD_NAME = "screenDetailStoryboardName" ;
	
	/**
	 * Key to save the name of the controller configuration property
	 */
	public static final String CONFIGURATION_DETAIL_CONTROLLER_PROPERTY = "detail-controller";
	
	/**
	 * Key to save the name of a xib for the cell
	 */
	public static final String CONFIGURATION_XIB_PROPERTY = "xib-item";
	
	/**
	 * Photo fixed list configuration name
	 */
	public static final String PHOTO_FIXED_LIST_CONFIGURATION_NAME = "photo";
	
	/**
	 * Boolean to avoid the generation of the fixed list cell
	 */
	private boolean bCellXibNoGeneration = false;
	
	/**
	 * Key in the map parameters of the fixed list view name
	 */
	public static final String KEY_TO_FIXED_LIST_VIEW_NAME = "fixedlist-view-name" ;
	
	/**
	 * Key in the map parameters of the cell in the fixed list view name
	 */
	public static final String KEY_TO_CELL_ITEM_NAME = "cell-item-name" ;

	/**
	 * Starting position in a visual field name of the related uml link
	 */
	public static final int UML_LINK_STARTING_POSITION = 3;
	
	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(MIOSFixedListDelegate.class);
	/**
	 * Delegator
	 */
	private StoryBoardExtractor delegator ;

	/**
	 * Configurations of fixed list in XML parameters
	 */
	private static final Map<String,ViewModelTypeConfiguration> VM_FIXEDLIST_TYPE_CONF_LIST = ViewModelType.FIXED_LIST.getVMTypeOptionMap();

	/**
	 * Constructor
	 * @param p_oDelegator Storyboard delegate
	 */
	public MIOSFixedListDelegate( StoryBoardExtractor p_oDelegator) {
		this.delegator = p_oDelegator ;
	}
	
	/**
	 * Return the object who delegate the task
	 * @return the object who delegate the task
	 */
	public StoryBoardExtractor getDelegator() {
		return delegator;
	}
	
	/**
	 * Accessor to the domain
	 * @return a domain 
	 */
	private IDomain<MIOSDictionnary,MIOSModeleFactory> getDomain() {
		return this.getDelegator().getDomain() ;
	}
	
	/**
	 * Update the position of a fixed list in the xib container for define the width and the height
	 */
	public void updatePositionOfFixedList(){
		
		boolean bConfigurationFound = false;

		// Met éé jour les hauteurs de chaque sous vue  de section contenant une fixed list
		for (MIOSSection oSection : this.getDomain().getDictionnary().getAllIOSSections() ){
			for ( MIOSView oSubview : oSection.getSubViews() ) {
				if ((oSubview != null) && oSubview instanceof MIOSEditableView) {

					//On regarde si la sous vue lue correspond éé une configuration de fixed list
					for (String mapKey : VM_FIXEDLIST_TYPE_CONF_LIST.keySet()) {

						if (((MIOSEditableView)oSubview).getCellType().equalsIgnoreCase(VM_FIXEDLIST_TYPE_CONF_LIST.get(mapKey).getVisualComponentName())) {
							bConfigurationFound = true;
						}
					}

					if (bConfigurationFound) {
						// j'ai une vue de type fixed_list
						MIOSXibContainer oContainer = this.getDomain().getDictionnary().getIOSXibContainersByName( ((MIOSEditableView)oSubview).getLinkedType());
						if (oContainer != null) {
							oSubview.setHeight(oContainer.getFrameHeight());
							oSubview.setWidth(oContainer.getFrameWidth());
							bConfigurationFound = false;
						}
					}
				}
			}
			if(MIOSControllerType.FIXEDLISTVIEW.equals(oSection.getController().getControllerType())) {			
				oSection.computeSubviewsPositionForLocalization(MVFLocalization.LIST);
				for(MIOSView oSubView : oSection.getSubViews()) {
					((MIOSEditableView)oSubView).computeCellHeight();
				}
			}
			else {
				oSection.computeSubviewsPosition();
			}

		}
	}

	/**
	 * Create the section an dview controller for the fixed list in the sub view models of the screen pages
	 * @param p_oScreen screen to search fixed list in 
	 * @param p_oStoryboard storyboard to complete to add the scene of the detail view controller
	 */
	public void createSectionAndControllerForFixedList( MScreen p_oScreen , MIOSStoryBoard p_oStoryboard ) {
		// on va traiter les views models de type fixed list qui doivent avoir un detail view controller
		for( MPage oPage: p_oScreen.getPages()) {
			createSectionAndControllerForFixedList(oPage, p_oStoryboard);
		}
	}
	
	/**
	 * Creates the scene and controller of the fixedlist
	 * @param p_oPage page hosting the fixedlist
	 * @param p_oStoryboard storyboard hosting the fixedlist
	 */
	public void createSectionAndControllerForFixedList(MPage p_oPage, MIOSStoryBoard p_oStoryboard) {
		for ( MViewModelImpl oViewModel : p_oPage.getViewModelImpl().getSubViewModels() ) {
			if ( oViewModel.getType() == ViewModelType.FIXED_LIST ) {

				//On regarde s'il faut géénéérer  un detail view controller
				//On va éégalement regarder si un nom de XIB de cellule est spéécifiéé
				boolean bNoDetailController = false;
				String sFixedListCellName = new String();
				this.bCellXibNoGeneration = false;

				//Recherche de la configuration du view model dans le fichier de configuration
				for (String mapKey : VM_FIXEDLIST_TYPE_CONF_LIST.keySet()) {

					//Si la configuration de ce view model est trouvéée
					if (mapKey.equals(oViewModel.getConfigName())) {

						//Parcours de la liste dynamique de propriéétéés de cette configuration
						for (String sKey : VM_FIXEDLIST_TYPE_CONF_LIST.get(mapKey).getPropertyList().keySet())  {

							//Si la propriéétéé du contrééleur déétail est spéécifiéée
							//Si la valeur est "false", le contrééleur déétail ne doit pas éétre géénééréé
							if (sKey.equalsIgnoreCase(CONFIGURATION_DETAIL_CONTROLLER_PROPERTY)
								&& VM_FIXEDLIST_TYPE_CONF_LIST.get(mapKey).getPropertyList().get(sKey).equals("false")) {
								
								bNoDetailController = true;
							}

							//Si la propriéétéé xib est spéécifiéée
							//Si une valeur est spéécifiéée, c'est le nom de la cellule qui doit éétre réécupééréé
							if (sKey.equalsIgnoreCase(CONFIGURATION_XIB_PROPERTY)
								&& !VM_FIXEDLIST_TYPE_CONF_LIST.get(mapKey).getPropertyList().get(sKey).equals("")
								&&  VM_FIXEDLIST_TYPE_CONF_LIST.get(mapKey).getPropertyList().get(sKey) != null) {
								
								sFixedListCellName = VM_FIXEDLIST_TYPE_CONF_LIST.get(mapKey).getPropertyList().get(sKey);
								this.bCellXibNoGeneration = true;
							}
						}

					}

				}



				this.createFixedListItemViewModel(oViewModel, p_oPage);
				this.renameViewModel(oViewModel, IOSVMNamingHelper.getInstance().computeViewModelNameOfFixedList(oViewModel));
				oViewModel.setEntityToUpdate(null); // permet de virer l'updateToIdentifiable qui n'a rien éé faire léé

				//Crééation du contrééleur de la vue de déétail de la fixed list
				String sDetailControllerName= IOSVMNamingHelper.getInstance().computeDetailControllerNameOfFixedList(p_oPage, oViewModel);
				String sDetailViewControllerName = this.delegator.computeControllerName( sDetailControllerName ) ;

				MIOSFixedListViewController oNewViewController = this.createFixedListDetailViewController( 
						oViewModel , p_oPage, sDetailViewControllerName, sFixedListCellName, bNoDetailController);
			
				this.delegator.addControllerTitleLabel(oNewViewController.getControllerType(), sDetailViewControllerName, sDetailControllerName);

				this.getDomain().getDictionnary().registerIOSController(oNewViewController);
				this.delegator.computeSection(p_oStoryboard, oNewViewController, p_oPage, oViewModel, false  ) ;

				//Le contrééleur de déétail doit éétre géénééréé
				if (!bNoDetailController) {

					//Il faut crééer la scééne de storyboard associéée
					MIOSScene oDetailViewControllerScene = this.getDomain().getXModeleFactory().createScene( 
							oNewViewController.getViewModel() , oNewViewController);
					p_oStoryboard.addScene(oDetailViewControllerScene);	
				}
				else
				{
					//Le contrééleur de déétail créééé ne devra pas éétre géénééréé : on en a juste besoin pour la géénéération
					//du PLIST "form-XXXX" qui déécrit une cellule de la fixed list
					oNewViewController.getCustomClass().setDoGeneration(false);
				}
			}
		}
	}

	/**
	 * Computes the name of the visual fields found in the fixedlist to create
	 * @param p_oPage page hosting the fixedlist
	 * @param p_oViewModel view model of the fixedlist
	 * @param p_oIOSSection section hosting the fixedlist
	 * @param p_sParentBindingPath parent binding path
	 */
	public void computeFieldsOfFixedList(MPage p_oPage , MViewModelImpl p_oViewModel, MIOSSection p_oIOSSection , String p_sParentBindingPath) {

		MIOSXibFixedListContainer oXibContainer = null;

		//Si le XIB de cellule de la fixed list doit être géénéré
		if (!this.bCellXibNoGeneration) {
			oXibContainer = (MIOSXibFixedListContainer) this.getDomain().getXModeleFactory().createXibContainer(p_oPage, p_oViewModel, p_oIOSSection.getController());
			oXibContainer.setXibType(MIOSXibType.FIXEDLISTITEM);
			
			oXibContainer.setDetailScreenStoryBoardName(p_oViewModel.getParameterValue(FIXED_LIST_DETAIL_STORYBOARD_NAME));
			oXibContainer.setDetailScreenViewControllerName(p_oViewModel.getParameterValue(FIXED_LIST_DETAIL_VIEW_CONTROLLER_NAME));
			oXibContainer.setViewModelItemName(p_oViewModel.getSubViewModels().get(0).getName());
		}

		//Néécessaire pour géénéérer la propriéétéé "field" du PLIST de la cellule de la fixed list,
		//que le xib soit géénééréé ou non
		for( MVisualField oVisualField : p_oViewModel.getVisualFields()) {

			//Pas de label sur les éléments dans le cas d'une photo fixed list
			//De plus, ces éléments ne doivent pas être éditables
			if (p_oViewModel.getConfigName().equals(PHOTO_FIXED_LIST_CONFIGURATION_NAME)) {
				oVisualField.setCreateLabel(false);
				oVisualField.setReadOnly(true);
				oVisualField.addVisualParameter(PHOTO_FIXED_LIST_CONFIGURATION_NAME, "true");
			}
			this.delegator.computeField(oVisualField, oVisualField.isReadOnly(), p_sParentBindingPath, p_oIOSSection,oXibContainer);
			

		}

		//Si le XIB de cellule de la fixed list doit éétre géénééréé
		if (!this.bCellXibNoGeneration) {

			ListIterator<MIOSView> oLitr = oXibContainer.getComponents().listIterator();
			while (oLitr.hasNext()) {
				MIOSView oXibComponent = oLitr.next();
				if (oXibComponent.getLocalization().equals(MVFLocalization.DETAIL)) {
					oLitr.remove();
				}
			}

			this.getDomain().getDictionnary().registerIOSXibContainer(oXibContainer);
		}

	}
	/**
	 * Create a detail view controller to see the complete view model of the item of the fixed list
	 * Define the name of the item and add the information to the view model
	 * @param p_oViewModel view model completed with the infos on controller 
	 * @param p_oPage page where is the view model
	 * @param p_sControllerName name of new the controller
	 * @param p_sFixedListCellName name of the fixed list cell
	 * @param p_bNoDetailController has the controller got a detail
	 * @return a new detail view controller for the fixed list
	 */
	public  MIOSFixedListViewController createFixedListDetailViewController(MViewModelImpl p_oViewModel , MPage p_oPage , 
			String p_sControllerName, String p_sFixedListCellName, boolean p_bNoDetailController) {

		String sFixedListCellName = new String();
		//Si un nom est spéécifiéé pour la cellule de la fixed list (réécupééréé depuis le fichier de configuration),
		//il est utiliséé. Autrement, il est géénééréé.
		if (p_sFixedListCellName != null && !p_sFixedListCellName.equals("")) {
			sFixedListCellName = p_sFixedListCellName;
		}
		else
		{
			sFixedListCellName = IOSVMNamingHelper.getInstance().computeViewModelNameOfFixedListItem( p_oPage , p_oViewModel);
		}

		MIOSFixedListViewController oNewViewController = this.getDomain().getXModeleFactory().createFixedListViewController(
				p_sControllerName, 
				sFixedListCellName);

		log.debug("Create a detail view controller for the fixed list '{}' in page '{}' ",p_sControllerName , oNewViewController.getFormName() );

		oNewViewController.setViewModel(p_oViewModel.getName()) ;
		//On passe en paraméétre du contrééleur un boolééen indiquant si la cellule doit éétre géénééréée ou pas.
		//Ce boolééen sera utiliséé dans le géénéérateur de cellule pour conditionner la crééation de ses classes 
		oNewViewController.setDoCellGeneration((p_sFixedListCellName == null || p_sFixedListCellName.equals("")));

		//Ajout du nom du déétail contrééleur seulement si le contrééleur de déétail doit éére géénééréé
		if (!p_bNoDetailController) {
			p_oViewModel.addParameter(FIXED_LIST_DETAIL_VIEW_CONTROLLER_NAME, oNewViewController.getName());
		}
		p_oViewModel.addParameter(FIXED_LIST_DETAIL_STORYBOARD_NAME, p_oPage.getParent().getName() );
		p_oViewModel.addImport(oNewViewController.getName()) ;

		MViewModelImpl oItemVm = p_oViewModel.getMasterSubViewModel();
		oNewViewController.setItemViewModel(oItemVm.getName());
		oNewViewController.setItemIdentifier(oItemVm.getIdentifier().getElems().get(0).getName());

		
		return oNewViewController ;
	}

	/**
	 * Create the item view model of the fixed list 
	 * @param p_oViewModel fixed list view model
	 * @param p_oPage page hosting the view model
	 */
	public void createFixedListItemViewModel(MViewModelImpl p_oViewModel, MPage p_oPage  ) {

		MViewModelImpl oViewModelItemToCreate = p_oViewModel.clone(this.getDomain().getXModeleFactory());
		oViewModelItemToCreate.setMasterInterface( new MViewModelInterface( p_oViewModel.getName() , p_oViewModel.getPackage()));

		this.renameViewModel(oViewModelItemToCreate, VMNamingHelper.getInstance().computeViewModelInterfaceName(
				IOSVMNamingHelper.getInstance().computeViewModelNameOfFixedListItem(p_oPage, oViewModelItemToCreate), false, this.getDomain().getLanguageConf()));
		this.modifyTypeOfItemViewModel(oViewModelItemToCreate) ;
		oViewModelItemToCreate.addImport(oViewModelItemToCreate.getName()) ;
		p_oViewModel.addSubViewModel(oViewModelItemToCreate);

		this.getDomain().getDictionnary().registerViewModel(oViewModelItemToCreate) ;
	}
	
	/**
	 * Rename the view model and the interface of it
	 * @param p_oViewModelToRename view model modified
	 * @param p_sNewName new suffix of the name the view model
	 */
	public void renameViewModel( MViewModelImpl p_oViewModelToRename , String p_sNewName ){
		p_oViewModelToRename.setName(p_sNewName);
		p_oViewModelToRename.getMasterInterface().setName(p_sNewName);
		p_oViewModelToRename.addImport(p_oViewModelToRename.getName()) ;
	}
	
	/**
	 * Modify the type of the item view model of the fixed list
	 * @param p_oViewModelModified item fixed list modified
	 */
	private void modifyTypeOfItemViewModel( MViewModelImpl p_oViewModelModified)  {
		p_oViewModelModified.setType(ViewModelType.FIXED_LIST_ITEM);

		ViewModelTypeConfiguration oVMTypeConf = p_oViewModelModified.getType().getParametersByConfigName(p_oViewModelModified.getConfigName(), VersionHandler.getGenerationVersion().getStringVersion());

		MLinkedInterface oNewLinkedInterface = new MLinkedInterface(oVMTypeConf.getInterfaceName(), oVMTypeConf.getInterfaceFullName(),
				p_oViewModelModified.getEntityToUpdate().getMasterInterface().getFullName());
		p_oViewModelModified.addLinkedInterface(oNewLinkedInterface	);

		p_oViewModelModified.getMasterInterface().addLinkedInterface(oNewLinkedInterface);
	}
	
	/**
	 * Sets the name of the fixedlist visual field
	 * @param p_oVisualField visual field of the fixedlist
	 * @param p_sParentBindingPath parent binding path
	 */
	public void modifyNameOfFieldForList(MVisualField p_oVisualField, String p_sParentBindingPath){
		if ( this.isFixedListField(p_oVisualField) ){
			String sOldFieldName = this.delegator.extractBindingPathFromFieldName(p_oVisualField.getName(), null, null, null);

			String sUmlLinkName = Introspector.decapitalize(sOldFieldName.substring(UML_LINK_STARTING_POSITION));
			String sNewName = sUmlLinkName + IOSVMNamingHelper.LIST_SUFFIX;

			p_oVisualField.setName(p_oVisualField.getName().replaceAll(sOldFieldName, WordUtils.uncapitalize(sNewName)));
			p_oVisualField.getLabel().setValue( p_oVisualField.getLabel().getValue().replaceAll(sOldFieldName, sNewName) );
			p_oVisualField.setLabel( p_oVisualField.getLabel());
		}
	}
	/**
	 * 
	 * @param p_oVisualField visual field of the view model
	 * @param p_oFieldView modified field view in the fixed list
	 */
	public void completeFieldsOfFixedList(MVisualField p_oVisualField, MIOSEditableView p_oFieldView) {

		if ( p_oVisualField.getParameterValue("fixedList") != null || p_oVisualField.getParameterValue("inFixedList") != null ) {

			String sVmName = p_oVisualField.getParameterValue("fixedListVm");
			MViewModelImpl oVm = this.delegator.getDomain().getDictionnary().getViewModel(sVmName);
			p_oVisualField.addVisualParameter(KEY_TO_FIXED_LIST_VIEW_NAME, 
					IOSVMNamingHelper.getInstance().computeViewNameOfFixedList(oVm));
			p_oVisualField.addVisualParameter(KEY_TO_CELL_ITEM_NAME, 
					IOSVMNamingHelper.getInstance().computeXibNameOfFixedListItem(oVm));
		}		

		Object oFixedListView = p_oVisualField.getVisualParameter(KEY_TO_FIXED_LIST_VIEW_NAME) ;
		if (oFixedListView!= null){
			p_oFieldView.setCustomClass("MDKFixedList");
			p_oFieldView.setCellType("MFCell1ComponentHorizontal");
			p_oFieldView.addOption("dataDelegateName", oFixedListView.toString());
			p_oFieldView.addOption("canAddItem", "YES");
			p_oFieldView.addOption("canEditItem", "YES");
			p_oFieldView.addOption("canDeleteItem", "YES");
			p_oFieldView.addOption("editMode", "0");
			String sVmName = p_oVisualField.getParameterValue("fixedListVm");
			MViewModelImpl oVm = this.delegator.getDomain().getDictionnary().getViewModel(sVmName);
			if (oVm.getConfigName().equals(PHOTO_FIXED_LIST_CONFIGURATION_NAME)) {
				p_oFieldView.addOption("isPhotoFixedList", "YES");
			}
			//p_oFieldView.setCustomClass(oFixedListView.toString());
			//p_oFieldView.setCellType(p_oVisualField.getComponent());
		} else {
			p_oFieldView.setCustomClass(p_oVisualField.getComponent());
			p_oFieldView.setCellType(((MIOSVisualField) p_oVisualField).getCellType()); 
		}
		Object oItemCellView = p_oVisualField.getVisualParameter(VMListPathProcessor.KEY_TO_CELL_ITEM_NAME) ;
		if (oItemCellView!= null) {

			//Réécupéération du nom de la cellule de la fixed list
			String sFixedListItemName = oItemCellView.toString();

			//Recherche d'une configuration de fixed list associéée au champ lu
			for (String mapKey : VM_FIXEDLIST_TYPE_CONF_LIST.keySet()) {
				//Une configuration a éétéé trouvéée
				if (p_oVisualField.getComponent().equalsIgnoreCase(VM_FIXEDLIST_TYPE_CONF_LIST.get(mapKey).getVisualComponentName())) {

					//Parcours de la liste dynamique de propriéétéés de cette configuration
					for (String sKey : VM_FIXEDLIST_TYPE_CONF_LIST.get(mapKey).getPropertyList().keySet())  {

						//Si la propriété xib est spécifiée et qu'une valeur est spécifiée, 
						// c'est le nom de la cellule : on le recupére
						if (sKey.equalsIgnoreCase(CONFIGURATION_XIB_PROPERTY) 
							&& !VM_FIXEDLIST_TYPE_CONF_LIST.get(mapKey).getPropertyList().get(sKey).equals("")
							&&  VM_FIXEDLIST_TYPE_CONF_LIST.get(mapKey).getPropertyList().get(sKey) != null) {
								sFixedListItemName = VM_FIXEDLIST_TYPE_CONF_LIST.get(mapKey).getPropertyList().get(sKey);
						}

					}


				}
			}

			p_oFieldView.setLinkedType(sFixedListItemName);

		}
	}
	/**
	 * Return if the visual field is of type fixed list
	 * @param p_oVisualField field tested
	 * @return true if the conf type for fixed list is the same than the field
	 */
	public boolean isFixedListField(MVisualField p_oVisualField){

		//Recherche d'une configuration de fixed list associéée au champ lu
		for (String mapKey : VM_FIXEDLIST_TYPE_CONF_LIST.keySet()) {
			if (p_oVisualField.getComponent().equalsIgnoreCase(VM_FIXEDLIST_TYPE_CONF_LIST.get(mapKey).getVisualComponentName())) {
				return true;
			}
		}

		return false;
	}
}
