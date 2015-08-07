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
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.languages.ios.xmodele.MIOSDictionnary;
import com.a2a.adjava.languages.ios.xmodele.MIOSModeleFactory;
import com.a2a.adjava.languages.ios.xmodele.MIOSStoryBoard;
import com.a2a.adjava.languages.ios.xmodele.MIOSXibComboContainer;
import com.a2a.adjava.languages.ios.xmodele.MIOSXibContainer;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSComboViewController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSControllerType;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSEditableView;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSSection;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSView;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSXibType;
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
 * Delegate for the combo management
 * @author ftollec
 */
public class MIOSComboDelegate {
	/**
	 * Key to save the name of the detail view controller 
	 */
	public static final String COMBO_DETAIL_VIEW_CONTROLLER_NAME = "comboDetailViewControllerName" ;

	/**
	 * Key in the map parameters of the combo view name
	 */
	public static final String KEY_TO_COMBO_VIEW_NAME = "combo-view-name" ;
	/**
	 * Key in the map parameters of the cell in the combo view name
	 */
	public static final String KEY_TO_CELL_ITEM_NAME = "cell-item-name" ;

	/**
	 * Key in the map parameters of the cell in the combo view name
	 */
	public static final String KEY_TO_CELL_SELECTED_ITEM_NAME = "cell-selected-item-name" ;

	/**
	 * Starting position in a visual field name of the related uml link
	 */
	public static final int UML_LINK_STARTING_POSITION = 3;


	/**
	 * Starting position in a visual field name of the related uml link
	 */
	public static final String KEY_TO_SEARCH_PARAMETER_NAME = "search";
	
	/**
	 * Starting position in a visual field name of the related uml link
	 */
	public static final String KEY_TO_LIST_ITEM_BINDING_DELEGATE_CLASS_PARAMETER_NAME = "listItemBindingDelegate";
	
	/**
	 * Starting position in a visual field name of the related uml link
	 */
	public static final String KEY_TO_SELECTED_ITEM_BINDING_DELEGATE_CLASS_PARAMETER_NAME = "selectedItemBindingDelegate";
	

	/**
	 * Starting position in a visual field name of the related uml link
	 */
	public static final String KEY_TO_PICKER_VALUES_KEY_PARAMETER_NAME = "pickerValuesKey";

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(MIOSComboDelegate.class);
	/**
	 * Delegator
	 */
	private StoryBoardExtractor delegator ;

	/**
	 * Constructor
	 * @param p_oDelegator storyboard extractor delegate
	 */
	public MIOSComboDelegate( StoryBoardExtractor p_oDelegator) {
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
	 * Update the position of a combo in the xib container to define the width and the height
	 */
	public void updatePositionOfCombo(){
		ViewModelTypeConfiguration oVmTypeConf = ViewModelType.LIST_1__ONE_SELECTED.getParametersByConfigName("default", VersionHandler.getGenerationVersion().getStringVersion());

		// Met à jour les hauteurs de chaque sous vue  de section contenant une combo
		for (MIOSSection oSection : this.getDomain().getDictionnary().getAllIOSSections() ){
			for ( MIOSView oSubview : oSection.getSubViews() ) {
				if ((oSubview != null) && oSubview instanceof MIOSEditableView 
						&& (null != ((MIOSEditableView)oSubview).getCellType()) && oSubview.getCustomClass().equalsIgnoreCase("MFPickerList")) {

					// j'ai une vue de type combo
					String sSelectedItemXibContainerName = ((MIOSEditableView)oSubview).getOptions().get(MIOSComboDelegate.KEY_TO_SELECTED_ITEM_BINDING_DELEGATE_CLASS_PARAMETER_NAME).replaceAll(IOSVMNamingHelper.COMBO_DELEGATE_SUFFIX, "");
					MIOSXibContainer oContainer = this.getDomain().getDictionnary().getIOSXibContainersByName(sSelectedItemXibContainerName);
					if ( oContainer != null ) {
						oSubview.setHeight(oContainer.getFrameHeight());
						oSubview.setWidth(oContainer.getFrameWidth());
					}
				}
			}
			if(MIOSControllerType.COMBOVIEW.equals(oSection.getController().getControllerType())) {			
				oSection.computeSubviewsPosition();
			}


		}
	}

	/**
	 * Create the section an view controller for the combo in the sub view models of the screen pages
	 * @param p_oScreen screen to search combo in 
	 * @param p_oStoryboard storyboard to complete to add the scene of the detail view controller
	 */
	public void createSectionAndControllerForCombo( MScreen p_oScreen , MIOSStoryBoard p_oStoryboard ) {
		// on va traiter les views models de type combo qui doivent avoir un detail view controller
		for( MPage oPage: p_oScreen.getPages()) {
			this.createSectionAndControllerForCombo(oPage, p_oStoryboard);			
		}
	}

	/**
	 * Creates the controller used by the combo on the page view model and all its children
	 * @param p_oPage page of the combo
	 * @param p_oStoryboard storyboard of the combo
	 */
	public void createSectionAndControllerForCombo(MPage p_oPage , MIOSStoryBoard p_oStoryboard ) {

		createSectionAndControllerForCombo(p_oStoryboard, p_oPage, p_oPage.getViewModelImpl());

		// on doit aussi traiter les subViewModels
		if ( p_oPage.getViewModelImpl().getSubViewModels() != null ) {
			for ( MViewModelImpl oViewModelImpl : p_oPage.getViewModelImpl().getSubViewModels() ) {

				createSectionAndControllerForCombo(p_oStoryboard, p_oPage, oViewModelImpl);
			}
		}

	}

	/**
	 * Creates the controller used by the combo
	 * @param p_oStoryboard storyboard containing the combo
	 * @param p_oPage page containing the combo
	 * @param p_oViewModelImpl view model of the combo
	 */
	private void createSectionAndControllerForCombo( MIOSStoryBoard p_oStoryboard, MPage p_oPage, MViewModelImpl p_oViewModelImpl) {

		for ( MViewModelImpl oViewModel : p_oViewModelImpl.getExternalViewModels() ) {

			if ( oViewModel.getType() == ViewModelType.LIST_1__ONE_SELECTED ) {

				//Principal name of all component created for this combobox. 
				String sGeneralComboName = IOSVMNamingHelper.getInstance().createGeneralComboName(p_oViewModelImpl, oViewModel);

				this.createComboItemViewModel(oViewModel, p_oPage);	

				//Create the item controler
				String sDetailViewControllerName = this.delegator.computeControllerName( 
						IOSVMNamingHelper.getInstance().computeDetailControllerNameOfItemCombo(sGeneralComboName)) ;

				MIOSComboViewController oNewItemViewController = this.createComboViewController( 
						oViewModel , p_oPage, sDetailViewControllerName, sGeneralComboName, false);
				this.delegator.computeSection(p_oStoryboard, oNewItemViewController, p_oPage, oViewModel, false  );

				//Create the selected item controler
				String sNewSelectedItemControllerName = IOSVMNamingHelper.getInstance().computeDetailControllerNameOfSelectedItemCombo(sGeneralComboName);

				MIOSComboViewController oNewSelectedItemViewController = oNewItemViewController.cloneWithNewName(sNewSelectedItemControllerName);
				oNewSelectedItemViewController.setIsSelectedItem(true);

				for(MIOSSection oSection : oNewItemViewController.getSections())
				{
					ListIterator<MIOSView> oLitr = oSection.getSubViews().listIterator();
					while(oLitr.hasNext()) {
						MIOSView oControllerComponent = oLitr.next();
						if(oControllerComponent.getLocalization().equals(MVFLocalization.DETAIL)){
							oLitr.remove();
						}
					}
					oSection.computeSubviewsPosition();
				}

				for(MIOSSection oSectionSelectedItem : oNewSelectedItemViewController.getSections())
				{
					ListIterator<MIOSView> oLitrSelectedItem = oSectionSelectedItem.getSubViews().listIterator();
					while(oLitrSelectedItem.hasNext()) {
						MIOSView oControllerComponentSelectedItem = oLitrSelectedItem.next();
						if(oControllerComponentSelectedItem.getLocalization().equals(MVFLocalization.LIST)){
							oLitrSelectedItem.remove();
						}
					}
					oSectionSelectedItem.computeSubviewsPosition();
				}

				this.getDomain().getDictionnary().registerIOSController(oNewSelectedItemViewController);
				this.getDomain().getDictionnary().registerIOSController(oNewItemViewController);


			}
		}
	}

	/**
	 * Generates the containers of the combo
	 * @param p_oPage page of the combo
	 * @param p_oViewModel view model of the combo
	 * @param p_oIOSSection section of the combo
	 * @param p_sParentBindingPath combo parent binding path
	 */
	public void computeFieldsOfCombo( MPage p_oPage, MViewModelImpl p_oViewModel, MIOSSection p_oIOSSection , String p_sParentBindingPath) {

		MIOSXibComboContainer oXibSelectedContainer = (MIOSXibComboContainer) this.getDomain().getXModeleFactory().createXibContainer(p_oPage, p_oViewModel, p_oIOSSection.getController());
		oXibSelectedContainer.setXibType(MIOSXibType.COMBOVIEWSELECTEDITEM);
		oXibSelectedContainer.setDelegateName(IOSVMNamingHelper.getInstance().computeDelegateNameForCombo(p_oViewModel, false));
		
		for( MVisualField oVisualField : p_oViewModel.getVisualFields()) {
			this.delegator.computeFieldForCombo(oVisualField, oVisualField.isReadOnly(), p_sParentBindingPath, p_oIOSSection,oXibSelectedContainer);
		}

		MIOSXibComboContainer oXibItemContainer = oXibSelectedContainer.clone();
		oXibItemContainer.setIsSelectedItem(false);
		oXibItemContainer.setXibType(MIOSXibType.COMBOVIEWLISTITEM);
		oXibItemContainer.setName(IOSVMNamingHelper.getInstance().computeXibNameOfListItemForCombo(p_oViewModel));
		oXibItemContainer.setDelegateName(IOSVMNamingHelper.getInstance().computeDelegateNameForCombo(p_oViewModel, true));


		ListIterator<MIOSView> oLitr = oXibItemContainer.getComponents().listIterator();
		while(oLitr.hasNext()) {
			MIOSView oXibComponent = oLitr.next();
			if(oXibComponent.getLocalization().equals(MVFLocalization.DETAIL)){
				oLitr.remove();
			}
		}


		ListIterator<MIOSView> oLitrSelectedItem = oXibSelectedContainer.getComponents().listIterator();
		while(oLitrSelectedItem.hasNext()) {
			MIOSView oXibComponent = oLitrSelectedItem.next();
			if(oXibComponent.getLocalization().equals(MVFLocalization.LIST)){
				oLitrSelectedItem.remove();
			}
		}

		oXibItemContainer.computeComponentsPosition();
		oXibSelectedContainer.computeComponentsPosition();

		
		this.getDomain().getDictionnary().registerIOSXibContainer(oXibSelectedContainer);
		this.getDomain().getDictionnary().registerIOSXibContainer(oXibItemContainer);
		
		
	}

	/**
	 * Computes the name of a combo visual field
	 * @param p_oVisualField visual field of the combo
	 * @param p_sParentBindingPath combo parent binding path
	 */
	public void modifyNameOfFieldForCombo( MVisualField p_oVisualField, String p_sParentBindingPath){
		if ( this.isComboField(p_oVisualField) ){
			String sOldFieldName = this.delegator.extractBindingPathFromFieldName(p_oVisualField.getName(), null, null, null);

			String sUmlLinkName = Introspector.decapitalize(sOldFieldName.substring(UML_LINK_STARTING_POSITION));
			String sNewName = sUmlLinkName + IOSVMNamingHelper.COMBO_VIEW_MODEL_LIST_SUFFIX;

			p_oVisualField.setName(p_oVisualField.getName().replaceAll(sOldFieldName, WordUtils.uncapitalize(sNewName)));
		}
	}


	/**
	 * Create a detail view controller to see the complete view model of the item of the combo
	 * Define the name of the item and add the information to the view model
	 * @param p_oViewModel view model completed with the infos on controller 
	 * @param p_oPage page where the view model is
	 * @param p_sControllerName name of new the controller
	 * @param p_sFormName name of the new form
	 * @param p_bIsSelected whether the new controller is selected 
	 * @return a new detail view controller for the combo
	 */
	public  MIOSComboViewController createComboViewController(MViewModelImpl p_oViewModel , MPage p_oPage , 
			String p_sControllerName, String p_sFormName, boolean p_bIsSelected) {

		MIOSComboViewController oNewViewController = this.getDomain().getXModeleFactory().createComboViewController(
				p_sControllerName, p_sFormName);
		log.debug("Create a detail view controller for the combo '{}' in page '{}' ",p_sControllerName , oNewViewController.getFormName() );

		oNewViewController.setViewModel(p_oViewModel.getName()) ;
		p_oViewModel.addParameter(COMBO_DETAIL_VIEW_CONTROLLER_NAME, oNewViewController.getName());
		p_oViewModel.addParameter("screen", p_oPage.getParent().getName() );
		p_oViewModel.addImport(oNewViewController.getName()) ;

		oNewViewController.setItemViewModel(p_oViewModel.getName());
		oNewViewController.setItemIdentifier(p_oViewModel.getIdentifier().getElems().get(0).getName());
		oNewViewController.setIsSelectedItem(p_bIsSelected);
		return oNewViewController ;
	}


	/**
	 * Return if the visual field is of type combo
	 * @param p_oVisualField field tested
	 * @return true if the conf type for combo is the same than the field
	 */
	public boolean isComboField(MVisualField p_oVisualField){

		ViewModelTypeConfiguration oVmTypeConf = ViewModelType.LIST_1__ONE_SELECTED.getParametersByConfigName("default", VersionHandler.getGenerationVersion().getStringVersion());
		return p_oVisualField.getComponent().equalsIgnoreCase( oVmTypeConf.getVisualComponentName() ) ;
	}

	/**
	 * Completes the description of a combo field
	 * @param p_oVisualField visual field of the view model
	 * @param p_oFieldView modified field view in the combo
	 * @param p_sParentBindingPath binding path of the parent
	 */
	public void completeFieldsOfCombo(MVisualField p_oVisualField, MIOSEditableView p_oFieldView, String p_sParentBindingPath) {

		String sVmName = p_oVisualField.getParameterValue("comboVm");
		MViewModelImpl oVm = this.delegator.getDomain().getDictionnary().getViewModel(sVmName);
		p_oVisualField.addVisualParameter(KEY_TO_COMBO_VIEW_NAME, 
				IOSVMNamingHelper.getInstance().computeViewNameOfCombo(oVm));
		p_oVisualField.addVisualParameter(KEY_TO_CELL_ITEM_NAME, 
				IOSVMNamingHelper.getInstance().computeXibNameOfListItemForCombo(oVm));

		p_oFieldView.setCustomClass("MFPickerList");
		p_oFieldView.setCellType("MFCell1ComponentHorizontal"); 
		p_oFieldView.setBindingPrefix("selected");
		p_oFieldView.setBindingSuffix("Item");

		boolean hasSearch = ViewModelType.FILTER.equals(oVm.getConfigName());
		Map<String, String> oFieldOptions = p_oFieldView.getOptions();
		if(oFieldOptions == null) {
			oFieldOptions = new HashMap<>();
		}
		oFieldOptions.put(KEY_TO_SEARCH_PARAMETER_NAME, hasSearch ? "true" : "false");
		oFieldOptions.put(KEY_TO_LIST_ITEM_BINDING_DELEGATE_CLASS_PARAMETER_NAME, IOSVMNamingHelper.getInstance().computeDelegateNameForCombo(oVm, true));
		oFieldOptions.put(KEY_TO_SELECTED_ITEM_BINDING_DELEGATE_CLASS_PARAMETER_NAME, IOSVMNamingHelper.getInstance().computeDelegateNameForCombo(oVm, false));

		oFieldOptions.put(KEY_TO_PICKER_VALUES_KEY_PARAMETER_NAME, StringUtils.join("lst", oVm.getName()));
		
		p_oFieldView.setViewOptions(oFieldOptions);

		p_oFieldView.setCustomParameterName(IOSVMNamingHelper.getInstance().computeCustomParameterNameForCombo(oVm, p_sParentBindingPath));		
	}

	/**
	 * Create the item view model of the combo
	 * @param p_oViewModel combo view model
	 * @param p_oPage page containing the combo
	 */
	public void createComboItemViewModel(MViewModelImpl p_oViewModel, MPage p_oPage  ) {

		MViewModelImpl oViewModelItemToCreate = p_oViewModel.clone(this.getDomain().getXModeleFactory());
		oViewModelItemToCreate.setMasterInterface( new MViewModelInterface( p_oViewModel.getName() , p_oViewModel.getPackage()));

		this.renameViewModel(oViewModelItemToCreate, VMNamingHelper.getInstance().computeViewModelInterfaceName(
				IOSVMNamingHelper.getInstance().computeItemViewModelNameOfCombo(p_oPage, oViewModelItemToCreate), false, this.getDomain().getLanguageConf()));
		this.modifyTypeOfItemViewModel(oViewModelItemToCreate) ;
		oViewModelItemToCreate.addImport(oViewModelItemToCreate.getName()) ;


		// Update the type of the list viewmodel of the combo 
		ViewModelTypeConfiguration oClonedVMTypeConf= ViewModelType.LIST_1__ONE_SELECTED.getVMTypeOptionMap().get(p_oViewModel.getConfigName()).clone();	
		oClonedVMTypeConf.setInterfaceName(oViewModelItemToCreate.getName());
		oClonedVMTypeConf.setInterfaceFullName(oViewModelItemToCreate.getFullName());
		ViewModelType.LIST_1__ONE_SELECTED.getVMTypeOptionMap().put(p_oViewModel.getName(), oClonedVMTypeConf);
		p_oViewModel.setConfigName(p_oViewModel.getName());

		// Linked interface is wrong, should be the list, we fix it here
		p_oViewModel.getMasterInterface().getLinkedInterfaces().remove(0);
		MLinkedInterface oNewLinkedInterface = new MLinkedInterface(oClonedVMTypeConf.getListName(), oClonedVMTypeConf.getListFullName(),
				p_oViewModel.getEntityToUpdate().getMasterInterface().getFullName());
		p_oViewModel.getMasterInterface().addLinkedInterface(oNewLinkedInterface);
		p_oViewModel.resetAttributes();
		p_oViewModel.resetMapping();

		// Register the item viewmodel of the combo
		this.getDomain().getDictionnary().registerViewModel(oViewModelItemToCreate);
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
	 * Modify the type of the item view model of the combo
	 * @param p_oViewModelModified item combo modified
	 */
	private void modifyTypeOfItemViewModel( MViewModelImpl p_oViewModelModified)  {
		p_oViewModelModified.setType(ViewModelType.LISTITEM_1);

		ViewModelTypeConfiguration oVMTypeConf = p_oViewModelModified.getType().getParametersByConfigName(p_oViewModelModified.getConfigName(), VersionHandler.getGenerationVersion().getStringVersion());

		MLinkedInterface oNewLinkedInterface = new MLinkedInterface(oVMTypeConf.getInterfaceName(), oVMTypeConf.getInterfaceFullName(),
				p_oViewModelModified.getEntityToUpdate().getMasterInterface().getFullName());
		p_oViewModelModified.addLinkedInterface(oNewLinkedInterface	);

		p_oViewModelModified.getMasterInterface().addLinkedInterface(oNewLinkedInterface);
	}

}
