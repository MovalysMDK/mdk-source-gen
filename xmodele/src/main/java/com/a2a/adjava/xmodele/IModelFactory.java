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
package com.a2a.adjava.xmodele;

import java.util.List;
import java.util.Map;

import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.types.IUITypeDescription;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.xmodele.ui.component.MActionButton;
import com.a2a.adjava.xmodele.ui.component.MMultiPanelConfig;
import com.a2a.adjava.xmodele.ui.component.MNavigationButton;
import com.a2a.adjava.xmodele.ui.menu.MMenu;
import com.a2a.adjava.xmodele.ui.menu.MMenuActionItem;
import com.a2a.adjava.xmodele.ui.menu.MMenuDef;
import com.a2a.adjava.xmodele.ui.menu.MMenuItem;
import com.a2a.adjava.xmodele.ui.navigation.MNavigation;
import com.a2a.adjava.xmodele.ui.navigation.MNavigationType;
import com.a2a.adjava.xmodele.ui.view.MVFLabelKind;
import com.a2a.adjava.xmodele.ui.view.MVFLocalization;
import com.a2a.adjava.xmodele.ui.view.MVFModifier;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelTypeConfiguration;

public interface IModelFactory {

	/**
	 * Create a xmodele instance
	 * @return xmodele instance
	 */
	public XModele<? extends IModelDictionary> createXModele();

	/**
	 * Create a attribute
	 * @param p_sName attribute name
	 * @param p_sVisibility visibility
	 * @param p_bIdentifier part of identifier
	 * @param p_bDerived derived
	 * @param p_bTranscient transient
	 * @param p_oTypeDescription type description
	 * @param p_sInitialisation initialisation
	 * @param p_sDefaultValue default value
	 * @param p_bIsMandory mandatory
	 * @param p_iLength length
	 * @param p_iPrecision precision
	 * @param p_iScale scale
	 * @param p_bHasSequence has sequence
	 * @param p_bUnique unique
	 * @param p_sUniqueKey name of unique key if part of unique key
	 * @param p_sDocumentation documentation
	 * @return attribute
	 */
	public MAttribute createMAttribute(String p_sName, String p_sVisibility,
			boolean p_bIdentifier, boolean p_bDerived, boolean p_bTranscient,
			ITypeDescription p_oTypeDescription, String p_sInitialisation,
			String p_sDefaultValue, boolean p_bIsMandory, int p_iLength,
			int p_iPrecision, int p_iScale, boolean p_bHasSequence,
			boolean p_bUnique, String p_sUniqueKey, String p_sDocumentation);

	/**
	 * Create an attribute
	 * @param p_sName name
	 * @param p_sVisibility visibility
	 * @param p_bIdentifier identifier
	 * @param p_bDerived derived
	 * @param p_bTranscient transient
	 * @param p_oTypeDescription type description
	 * @param p_sDocumentation documentation
	 * @return attribute
	 */
	public MAttribute createMAttribute(String p_sName, String p_sVisibility,
			boolean p_bIdentifier, boolean p_bDerived, boolean p_bTranscient,
			ITypeDescription p_oTypeDescription, String p_sDocumentation);

	/**
	 * Create an attribute
	 * @param p_sName attribute
	 * @param p_sVisibility visibility
	 * @param p_bIdentifier identifier
	 * @param p_bDerived derived
	 * @param p_bTransient transient
	 * @param p_oTypeDescription type description
	 * @param p_sDocumentation documentation
	 * @param p_bReadOnly readonly
	 * @return attribute
	 */
	public MAttribute createMAttribute(String p_sName, String p_sVisibility,
			boolean p_bIdentifier, boolean p_bDerived, boolean p_bTransient,
			ITypeDescription p_oTypeDescription, String p_sDocumentation,
			boolean p_bReadOnly);

	/**
	 * Create a page
	 * @param p_oParent screen parent
	 * @param p_oDomain 
	 * @param p_sPageName page name
	 * @param p_oUmlPage uml class from which panel is created
	 * @param p_oPackage package
	 * @param p_oVmImpl viewmodel implementation
	 * @param p_bTitled true if panel has title
	 * @return page
	 */
	public MPage createPage(MScreen p_oParent, IDomain<IModelDictionary, IModelFactory> p_oDomain, String p_sPageName,
			UmlClass p_oUmlPage, MPackage p_oPackage, MViewModelImpl p_oVmImpl,
			boolean p_bTitled);

	/**
	 * Create a screen
	 * @param p_sUmlName uml name
	 * @param p_sName name screen name
	 * @param p_oScreenPackage screen package
	 * @return MScreen
	 */
	public MScreen createScreen(String p_sUmlName, String p_sName,
			MPackage p_oScreenPackage);

	/**
	 * Create a comment
	 * @param p_sUmlName uml name
	 * @param p_sName name comment name
	 * @return MComment
	 */
	public MComment createComment(String p_sUmlName, String p_sName);

	/**
	 * Create a navigation
	 * @param p_sNavigationName navigation name
	 * @param p_oNavigationType navigation type
	 * @param p_oScreen source screen
	 * @param p_oScreenEnd target screen
	 * @return navigation
	 */
	public MNavigation createNavigation(String p_sNavigationName,
			MNavigationType p_oNavigationType, MScreen p_oScreen,
			MScreen p_oScreenEnd);

	/**
	 * Create a menu definition
	 * @param p_sId id
	 * @return menu definition
	 */
	public MMenuDef createMenuDef(String p_sId);

	/**
	 * Create a menu
	 * @param p_sId id
	 * @return menu
	 */
	public MMenu createMenu(String p_sId);

	/**
	 * Create a menu item
	 * @return menu item
	 */
	public MMenuItem createMenuItem();

	/**
	 * Create a action menu item
	 * @param p_sId the id of the menu
	 * @return an action menu item
	 */
	public MMenuActionItem createMenuActionItem(String p_sId);
	
	/**
	 * Create a viewmodel
	 * @param p_sName name
	 * @param p_sUmlName uml name
	 * @param p_oPackage package
	 * @param p_oType viewmodel type
	 * @param p_oTypeEntityToUpdate type description of entity to update
	 * @param p_sPathToModel path to model
	 * @param p_bCustomizable customizable
	 * @return viewmodel
	 */
	public MViewModelImpl createViewModel(String p_sName, String p_sUmlName,
			MPackage p_oPackage, ViewModelType p_oType,
			MEntityImpl p_oEntityToUpdate, String p_sPathToModel,
			boolean p_bCustomizable);

	/**
	 * Create an action.
	 * @param p_sName action  name
	 * @param p_oActionInterface action interface
	 * @param p_bIsRoot is root
	 * @param p_oPackage package
	 * @param p_oViewModel viewmodel implementation
	 * @param p_oDao dao interface
	 * @param p_listExternalDaos external daos
	 * @param p_sCreatorName creator name
	 * @return action
	 */
	public MAction createAction(String p_sName,
			MActionInterface p_oActionInterface, boolean p_bIsRoot,
			MPackage p_oPackage, MViewModelImpl p_oViewModel,
			MDaoInterface p_oDao, List<MDaoInterface> p_listExternalDaos,
			String p_sCreatorName);

	/**
	 * Create an action interface.
	 * @param p_sName interface name
	 * @param p_bRoot root
	 * @param p_oPackage package
	 * @param p_sInNameClass class of parameter in
	 * @param p_sOutNameClass class of parameter out
	 * @param p_sStepClass step class
	 * @param p_sProgressClass progress class
	 * @param p_oEntity entity implementation
	 * @param p_oActionType action type
	 * @return action interface
	 */
	public MActionInterface createActionInterface(String p_sName,
			boolean p_bRoot, MPackage p_oPackage, String p_sInNameClass,
			String p_sOutNameClass, String p_sStepClass,
			String p_sProgressClass, MEntityImpl p_oEntity,
			MActionType p_oActionType);

	/**
	 * Create a navigation button
	 * @param p_sName name
	 * @param p_sLabelId label id
	 * @param p_sLabelValue label value
	 * @param p_oNavigation navigation
	 * @return navigation button
	 */
	public MNavigationButton createNavigationButton(String p_sName,
			String p_sLabelId, String p_sLabelValue, MNavigation p_oNavigation);

	/**
	 * Create a navigation button
	 * @param p_sName button name
	 * @param p_oNavigation navigation
	 * @return navigation button
	 */
	public MNavigationButton createNavigationButton(String p_sName,
			MNavigation p_oNavigation);

	/**
	 * Create a save button
	 * @param p_sName button name
	 * @param p_oAction associated action
	 * @return button
	 */
	public MActionButton createSaveButton(String p_sName, MAction p_oAction);

	/**
	 * Create a delete button
	 * @param p_sName button  name
	 * @param p_oAction associated action
	 * @return delete button
	 */
	public MActionButton createDeleteButton(String p_sName, MAction p_oAction);

	/**
	 * Create an entity
	 * @param p_sName class name for entity (including prefix and suffix)
	 * @param p_oPackage package of entity
	 * @param p_sUmlName uml name
	 * @param p_sEntityName entity name
	 * @return entity instance
	 */
	public MEntityImpl createMEntity(String p_sName, MPackage p_oPackage,
			String p_sUmlName, String p_sEntityName);

	/**
	 * Create a dao implementation
	 * @param p_sDaoName dao name
	 * @param p_sDaoBeanName dao bean name
	 * @param p_oPackageDao package of dao
	 * @param p_oEntity entity implementation
	 * @param p_oEntityInterface entity interface
	 * @param p_sQueryDefinitionFile query definition file
	 * @return dao implementation
	 */
	public MDaoImpl createDaoImpl(String p_sDaoName, String p_sDaoBeanName,
			MPackage p_oPackageDao, MEntityImpl p_oEntity,
			MEntityInterface p_oEntityInterface, String p_sQueryDefinitionFile);

	/**
	 * Create a dao interface
	 * @param p_sInterfaceName interface name
	 * @param p_sBeanName bean name
	 * @param p_oDaoInterfacePackage package of dao interface
	 * @param p_oDao implementation of dao interface
	 * @param p_oClass entity impl
	 * @return instance of dao interface
	 */
	public MDaoInterface createDaoInterface(String p_sInterfaceName,
			String p_sBeanName, MPackage p_oDaoInterfacePackage,
			MDaoImpl p_oDao, MEntityImpl p_oClass);

	/**
	 * 
	 * @param p_sName
	 * @param p_sVisibility
	 * @param p_sType
	 * @param p_oReturnedType
	 * @param p_listReturnedProperties
	 * @param p_listNeedImports
	 * @param p_bByValue
	 * @return instance of dao method signature
	 */
	public MDaoMethodSignature createDaoSignature(String p_sName, String p_sVisibility, String p_sType,
			ITypeDescription p_oReturnedType, List<String> p_listReturnedProperties,
			List<String> p_listNeedImports, boolean p_bByValue );
	
	/**
	 * @param p_oViewModel
	 * @param p_sPrefix
	 * @param p_oTypeVisual
	 * @param p_bReadOnly
	 * @param p_bLabel
	 * @param p_oAttribute
	 */
	public MVisualField createVisualField(String p_sPrefix, MLabel p_oLabel,
			IUITypeDescription p_oTypeVisual, MVFModifier p_oModifier,
			MVFLabelKind p_oLabelKind, MAttribute p_oAttribute,
			IDomain<IModelDictionary, IModelFactory> p_oDomain,
			String p_sAttributeName, boolean p_bMandatory);

	/**
	 * @param p_oViewModel
	 * @param p_sPrefix
	 * @param p_oTypeVisual
	 * @param p_bReadOnly
	 * @param p_bLabel
	 * @param p_oAttribute
	 * @param p_oLocalisation
	 */
	public MVisualField createVisualField(String p_sPrefix, MLabel p_oLabel,
			IUITypeDescription p_oTypeVisual, MVFModifier p_bMVFModifier,
			MVFLabelKind p_oLabelKind, MAttribute p_oAttribute,
			MVFLocalization p_oLocalisation,
			IDomain<IModelDictionary, IModelFactory> p_oDomain,
			String p_sAttributeName, boolean p_bMandatory);

	/**
	 * Create a visual field
	 * @param p_sName name
	 * @param p_sLabelName label name
	 * @param p_sComponent component
	 * @param p_oLabelKind label kind
	 * @param p_oTarget localisation
	 * @param p_sAttributeName attribute name
	 * @param p_bMandatoryKind mandatory
	 * @return visual field
	 */
	public MVisualField createVisualField(String p_sName, MLabel p_oLabelName,
			String p_sComponent, MVFLabelKind p_oLabelKind,
			MVFLocalization p_oTarget, String p_sAttributeName,
			boolean p_bMandatoryKind);

	/**
	 * Create a visual field
	 * @param p_sName name
	 * @param p_sLabelName label name
	 * @param p_sComponent component
	 * @param p_oLabelKind label kind
	 * @param p_sAttributeName attribute name
	 * @param p_bMandatoryKind mandatory
	 * @return visual field
	 */
	public MVisualField createVisualField(String p_sName, MLabel p_oLabel,
			String p_sComponent, MVFLabelKind p_oLabelKind,
			String p_sAttributeName, boolean p_bMandatoryKind);

	/**
	 * Create a visual field
	 * @param p_sPath path
	 * @param p_oField visual field
	 * @return visual field
	 */
	public MVisualField createVisualField(String p_sPath, MVisualField p_oField, MLabel p_sLabel);

	/**
	 * Create a multipanel config
	 * @return multipanel config
	 */
	public MMultiPanelConfig createMultiPanelConfig();

	/**
	 * Create the name of the combo VisualField into combo box
	 * @param p_oViewModel
	 * @param sTName
	 * @return
	 */
	public String createVisualFieldNameForFixedListCombo(MViewModelImpl p_oViewModel, String p_sTName);

	/**
	 * Creates an external adapter
	 * @param p_oDomain domain to use
	 * @param p_oTypeParameters adapter type parameters
	 * @param p_oVm linked view model
	 * @param p_sBaseName base adapter name
	 * @return the adapter
	 */
	public MAdapter createExternalAdapter(IDomain<IModelDictionary, IModelFactory> p_oDomain, ViewModelTypeConfiguration p_oTypeParameters, 
			MViewModelImpl p_oVm, String p_sBaseName);

	/**
	 * Compute attribute name of the viewmodel for a combo
	 * @param p_oViewModel
	 * @return
	 */
	public String createVmAttributeNameForCombo(MViewModelImpl p_oViewModel);

	/**
	 * @param p_oViewModel
	 * @param p_sAttrName
	 * @return
	 */
	public String createPropertyNameForFixedListCombo(
			MViewModelImpl p_oViewModel, String p_sAttrName);

	public MLabel createLabel(String p_sBaseName, MViewModelImpl p_oViewModel );

	public MLabel createLabelFromVisualField( String p_sPath, MVisualField p_oVisualField);

	public MLabel createLabelForAttributeOfFixedList(String p_sBaseName1, String p_sBaseName2, MVFLocalization p_oLocalization,
			MViewModelImpl p_oViewModel);

	public MLabel createLabelForAttributeOfCombo(String p_sAttributeName, String p_sVisualFieldName,
			MVFLocalization p_oLocalization, MViewModelImpl p_oViewModel);
	
	public MLabel createLabelForScreen( MScreen p_oScreen );
	
	public MLabel createLabelForPage(MPage p_oPage);
	
	public MLabel createLabelForButton( String p_sKey, String p_sValue );
	
	public Map<String,MLabel> createLabelsForEnumeration( MEnumeration p_oEnumeration);
	
	/**
	 * @param p_sBaseName
	 * @return
	 */
	public String createLabelValue(String p_sValue );

	public MLabel createLabelForCombo(String p_sBaseName1, String p_sBaseName2, MViewModelImpl oParentVm);

	public MLabel createLabelForFixedList(String sFixedListName,
			String umlName, MViewModelImpl oParentVm);
}
