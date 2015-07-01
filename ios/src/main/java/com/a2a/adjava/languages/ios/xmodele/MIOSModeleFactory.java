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

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.a2a.adjava.languages.ios.extractors.IOSVMNamingHelper;
import com.a2a.adjava.languages.ios.project.IOSUITypeDescription;
import com.a2a.adjava.languages.ios.xmodele.connections.MIOSActionConnection;
import com.a2a.adjava.languages.ios.xmodele.connections.MIOSSegueConnection;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOS2DListViewController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSComboViewController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSControllerType;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSFixedListViewController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSFormViewController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSListViewController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSNavigationController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSSearchViewController;
import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSViewController;
import com.a2a.adjava.languages.ios.xmodele.relationship.MIOSActionRelationShip;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSButtonView;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSSection;
import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.types.IUITypeDescription;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MAttribute;
import com.a2a.adjava.xmodele.MDaoImpl;
import com.a2a.adjava.xmodele.MDaoInterface;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MEntityInterface;
import com.a2a.adjava.xmodele.MLabel;
import com.a2a.adjava.xmodele.MPackage;
import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.MVisualField;
import com.a2a.adjava.xmodele.XModeleFactory;
import com.a2a.adjava.xmodele.ui.navigation.MNavigation;
import com.a2a.adjava.xmodele.ui.view.MVFLabelKind;
import com.a2a.adjava.xmodele.ui.view.MVFLocalization;
import com.a2a.adjava.xmodele.ui.view.MVFModifier;

/**
 * IOS Model factory
 * @author lmichenaud
 *
 */
public class MIOSModeleFactory extends XModeleFactory {

	/**
	 * Scene prefix
	 */
	private static final String SCENE_PREFIX = "scene_";

	/**
	 * Placeholder prefix
	 */
	private static final String PLACEHOLDER_PREFIX = "placeholder_";

	/**
	 * Navigation Controller prefix
	 */
	private static final String NAVCONTROLLER_PREFIX = "navcontroller_";

	/**
	 * Navigation bar prefix
	 */
	private static final String NAVBAR_PREFIX = "navbar_";

	/**
	 * View Controller prefix
	 */
	private static final String VIEWCONTROLLER_PREFIX = "viewcontroller_";

	/**
	 * Segue root view controller prefix
	 */
	private static final String SEGUE_ROOTVIEWCONTROLLER_PREFIX = "seguerootviewcontroller_";

	/**
	 * View id prefix
	 */
	private static final String VIEWID_PREFIX = "view_";

	/**
	 * Navigation item id prefix
	 */
	private static final String NAVIGATIONITEM_PREFIX = "navigationitem_";

	/**
	 * Connection prefix
	 */
	private static final String CONNECTION_PREFIX = "connection_";

	/**
	 * Button pressed suffix
	 */
	private static final String GENERIC_BUTTON_PRESSED_ACTION = "genericButtonPressed:";

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.a2a.adjava.xmodele.XModeleFactory#createMAttribute(java.lang.String, java.lang.String, boolean,
	 *      com.a2a.adjava.project.ITypeDescription, java.lang.String)
	 */
	@Override
	public MAttribute createMAttribute(String p_sName, String p_sVisibility, boolean p_bIdentifier, 
			boolean p_bDerived, boolean p_bTranscient, ITypeDescription p_oTypeDescription,
			String p_sDocumentation) {
		return new MIOSAttribute(p_sName, p_sVisibility, p_bIdentifier, p_bDerived, p_bTranscient, p_oTypeDescription, p_sDocumentation);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.a2a.adjava.xmodele.XModeleFactory#createMAttribute(java.lang.String, java.lang.String, boolean,
	 *      com.a2a.adjava.project.ITypeDescription, java.lang.String, java.lang.String, boolean, int, int, int, boolean, boolean, java.lang.String, java.lang.String)
	 */
	@Override
	public MAttribute createMAttribute(String p_sName, String p_sVisibility, boolean p_bIdentifier, boolean p_bDerived, boolean p_bTranscient, ITypeDescription p_oTypeDescription,
			String p_sInitialisation, String p_sDefaultValue, boolean p_bIsMandory, int p_iLength, int p_iPrecision, int p_iScale, boolean p_bHasSequence, boolean p_bUnique,
			String p_sUniqueKey, String p_sDocumentation) {
		return new MIOSAttribute(p_sName, p_sVisibility, p_bIdentifier, p_bDerived, p_bTranscient, p_oTypeDescription, p_sInitialisation, p_sDefaultValue, 
				p_bIsMandory, p_iLength, p_iPrecision, p_iScale, p_bHasSequence, p_bUnique, p_sUniqueKey, p_sDocumentation);
	}


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
	public MAttribute createMAttribute(String p_sName, String p_sVisibility, boolean p_bIdentifier, 
			boolean p_bDerived, boolean p_bTransient, ITypeDescription p_oTypeDescription, String p_sDocumentation, boolean p_bReadOnly) {
		return new MIOSAttribute(p_sName, p_sVisibility, p_bIdentifier, p_bDerived, p_bTransient, p_oTypeDescription, p_sDocumentation, p_bReadOnly);
	}

	/**
	 * Create an ios story board
	 * @param p_sName story board name
	 * @return story board
	 */
	public MIOSStoryBoard createStoryBoard(String p_sName) {
		MIOSStoryBoard r_oMIOSStoryBoard = new MIOSStoryBoard();
		r_oMIOSStoryBoard.setName(p_sName);
		return r_oMIOSStoryBoard;
	}

	/**
	 * Create a scene
	 * @param p_sName base name
	 * @param p_oController 
	 * @return scene
	 */
	public MIOSScene createScene( String p_sName, MIOSController p_oController ) {
		MIOSScene r_oMIOSScene = new MIOSScene();
		r_oMIOSScene.setId(this.genereId(SCENE_PREFIX, p_sName));
		r_oMIOSScene.setPlaceHolderId(this.genereId(PLACEHOLDER_PREFIX, p_sName));
		r_oMIOSScene.setController(p_oController);
		return r_oMIOSScene;
	}

	/**
	 * Create a navigation controller
	 * @param p_sName base name
	 * @param p_oRootController Root Controller
	 * @return navigation controller
	 */
	public MIOSNavigationController createNavigationController( String p_sName, MIOSController p_oRootController ) {
		MIOSNavigationController r_oMIOSController = new MIOSNavigationController();
		r_oMIOSController.setId(this.genereId(NAVCONTROLLER_PREFIX, p_sName));
		r_oMIOSController.setName(p_sName);
		r_oMIOSController.setNavigationBarId(this.genereId(NAVBAR_PREFIX));
		r_oMIOSController.setRootViewControllerSegueId(this.genereId(SEGUE_ROOTVIEWCONTROLLER_PREFIX));
		r_oMIOSController.setRootViewController(p_oRootController);
		return r_oMIOSController ;
	}

	/**
	 * Create a view controller
	 * @param p_sName name
	 * @return view controller
	 */
	public MIOSViewController createViewController(String p_sName) {
		MIOSViewController r_oMIOSController = new MIOSViewController();
		r_oMIOSController.setId(this.genereId(VIEWCONTROLLER_PREFIX, p_sName));
		r_oMIOSController.setName(p_sName);
		r_oMIOSController.setViewId(this.genereId(VIEWID_PREFIX, p_sName));
		r_oMIOSController.setNavigationItemId(this.genereId(NAVIGATIONITEM_PREFIX, p_sName));
		return r_oMIOSController ;
	}

	/**
	 * Create a view controller
	 * @param p_sName name
	 * @param p_sFormName form name
	 * @return view controller
	 */
	public MIOSViewController createViewController(String p_sName, String p_sFormName) {
		MIOSViewController r_oMIOSController = this.createViewController(p_sName);
		r_oMIOSController.setFormName(p_sFormName);
		return r_oMIOSController ;
	}

	/**
	 * Create a form view controller
	 * @param p_sName controller name
	 * @param p_sFormName form name
	 * @param p_listSaveActionNames action names
	 * @return view controller
	 */
	public MIOSFormViewController createFormViewController(String p_sName, String p_sFormName, List<String> p_listSaveActionNames) {
		MIOSFormViewController r_oMIOSController = new MIOSFormViewController();
		r_oMIOSController.setId(this.genereId(VIEWCONTROLLER_PREFIX, p_sName));
		r_oMIOSController.setName(p_sName);
		r_oMIOSController.setFormName(p_sFormName);
		r_oMIOSController.setViewId(this.genereId(VIEWID_PREFIX, p_sName));
		r_oMIOSController.setNavigationItemId(this.genereId(NAVIGATIONITEM_PREFIX, p_sName));
		r_oMIOSController.setSaveActionNames(p_listSaveActionNames);
		return r_oMIOSController ;
	}
	
	/**
	 * Create a form view controller
	 * @param p_sName controller name
	 * @param p_sFormName form name
	 * @param p_listSaveActionNames action names
	 * @return view controller
	 */
	public MIOSSearchViewController createSearchViewController(String p_sName, String p_sFormName, List<String> p_listSaveActionNames) {
		MIOSSearchViewController r_oMIOSController = new MIOSSearchViewController();
		r_oMIOSController.setId(this.genereId(VIEWCONTROLLER_PREFIX, p_sName));
		r_oMIOSController.setName(p_sName);
		r_oMIOSController.setFormName(p_sFormName);
		r_oMIOSController.setViewId(this.genereId(VIEWID_PREFIX, p_sName));
		r_oMIOSController.setNavigationItemId(this.genereId(NAVIGATIONITEM_PREFIX, p_sName));
		r_oMIOSController.setSaveActionNames(p_listSaveActionNames);
		return r_oMIOSController;
	}

	/**
	 * Create a list view controller
	 * @param p_sName controller name
	 * @param p_sFormName form name
	 * @return view controller
	 */
	public MIOSListViewController createListViewController(String p_sName, String p_sFormName) {
		MIOSListViewController r_oMIOSController = new MIOSListViewController();
		r_oMIOSController.setId(this.genereId(VIEWCONTROLLER_PREFIX, p_sName));
		r_oMIOSController.setName(p_sName);
		r_oMIOSController.setFormName(p_sFormName);
		r_oMIOSController.setViewId(this.genereId(VIEWID_PREFIX, p_sName));
		r_oMIOSController.setNavigationItemId(this.genereId(NAVIGATIONITEM_PREFIX, p_sName));
		return r_oMIOSController ;
	}

	/**
	 * Create a list view controller
	 * @param p_sName controller name
	 * @param p_sFormName form name
	 * @return view controller
	 */
	public MIOS2DListViewController create2DListViewController(String p_sName, String p_sFormName) {
		MIOS2DListViewController r_oMIOSController = new MIOS2DListViewController();
		r_oMIOSController.setId(this.genereId(VIEWCONTROLLER_PREFIX, p_sName));
		r_oMIOSController.setName(p_sName);
		r_oMIOSController.setFormName(p_sFormName);
		r_oMIOSController.setViewId(this.genereId(VIEWID_PREFIX, p_sName));
		r_oMIOSController.setNavigationItemId(this.genereId(NAVIGATIONITEM_PREFIX, p_sName));
		return r_oMIOSController ;
	}

	/**
	 * Create a list view controller
	 * @param p_sName controller name
	 * @param p_sFormName form name
	 * @return view controller
	 */
	public MIOSFixedListViewController createFixedListViewController(String p_sName, String p_sFormName) {
		MIOSFixedListViewController r_oMIOSController = new MIOSFixedListViewController();
		r_oMIOSController.setId(this.genereId(VIEWCONTROLLER_PREFIX, p_sName));
		r_oMIOSController.setName(p_sName);
		r_oMIOSController.setFormName(p_sFormName);
		r_oMIOSController.setViewId(this.genereId(VIEWID_PREFIX, p_sName));
		r_oMIOSController.setNavigationItemId(this.genereId(NAVIGATIONITEM_PREFIX, p_sName));
		return r_oMIOSController ;
	}

	/**
	 * Create a list view controller
	 * @param p_sName controller name
	 * @param p_sFormName form name
	 * @return view controller
	 */
	public MIOSComboViewController createComboViewController(String p_sName, String p_sFormName) {
		MIOSComboViewController r_oMIOSController = new MIOSComboViewController();
		r_oMIOSController.setId(this.genereId(VIEWCONTROLLER_PREFIX, p_sName));
		r_oMIOSController.setName(p_sName);
		r_oMIOSController.setFormName(p_sFormName);
		r_oMIOSController.setViewId(this.genereId(VIEWID_PREFIX, p_sName));
		r_oMIOSController.setNavigationItemId(this.genereId(NAVIGATIONITEM_PREFIX, p_sName));
		r_oMIOSController.setSelectedItemCellClassName(p_sFormName + "SelectedItem");
		r_oMIOSController.setItemCellClassName(p_sFormName + "Item");
		return r_oMIOSController ;
	}
	
	/**
	 * Create a section
	 * @param p_sName section name
	 * @return the created section
	 */
	public MIOSSection createSection( String p_sName ) {
		MIOSSection r_oMIOSSection = new MIOSSection();
		r_oMIOSSection.setName(p_sName);
		return r_oMIOSSection;
	}

	/**
	 * Create a view controller with custom class
	 * @param p_sName name
	 * @param p_oMIOSClass custom class
	 * @return view controller
	 */
	public MIOSViewController createViewController(String p_sName, MIOSClass p_oMIOSClass ) {
		MIOSViewController r_oMIOSController = this.createViewController(p_sName);
		r_oMIOSController.setCustomClass(p_oMIOSClass);
		return r_oMIOSController ;
	}

	/**
	 * Create an action relation ship
	 * @param p_sName name
	 * @return action relation ship
	 */
	public MIOSActionRelationShip createActionRelationShip( String p_sName ) {
		MIOSActionRelationShip r_oMIOSActionRelationShip = new MIOSActionRelationShip();
		r_oMIOSActionRelationShip.setName(p_sName);
		return r_oMIOSActionRelationShip;
	}

	/**
	 * Create a connection
	 * @param p_oMIOSButtonView button view
	 * @param p_oMNavigation navigation
	 */
	public void createPressedConnectionForNavButton( MIOSButtonView p_oMIOSButtonView, MNavigation p_oMNavigation ) {
		MIOSActionConnection oConnection = new MIOSActionConnection();
		StringBuilder sSb = new StringBuilder(p_oMIOSButtonView.getId());
		sSb.append(GENERIC_BUTTON_PRESSED_ACTION);
		oConnection.setId(this.genereId(CONNECTION_PREFIX, sSb.toString()));
		oConnection.setSelector(GENERIC_BUTTON_PRESSED_ACTION);
		p_oMIOSButtonView.addConnection(oConnection);
	}

	/**
	 * Creates a segue connection
	 * @param p_sourceController the source controller
	 * @param p_destinationController the destination controller
	 */
	public void createSegueConnection(MIOSController p_sourceController, MIOSController p_destinationController) {
		MIOSSegueConnection oConnection = new MIOSSegueConnection();
		oConnection.setDestination(p_destinationController);
		p_sourceController.addConnection(oConnection);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.XModeleFactory#createMEntity(java.lang.String, com.a2a.adjava.xmodele.MPackage, java.lang.String, java.lang.String)
	 */
	@Override
	public MEntityImpl createMEntity(String p_sName, MPackage p_oPackage,
			String p_sUmlName, String p_sEntityName) {
		MIOSEntity r_oMIOSEntity = new MIOSEntity(p_sName, p_oPackage, p_sUmlName, p_sEntityName);
		r_oMIOSEntity.setImportDlg(this.createImportDelegate(r_oMIOSEntity));
		return r_oMIOSEntity ;
	}


	/**
	 * Create import delegate
	 * @param p_oDelegator delegator
	 * @return delegator
	 */
	public MIOSImportDelegate createImportDelegate( Object p_oDelegator) {
		return new MIOSImportDelegate(p_oDelegator);
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.XModeleFactory#createDaoImpl(java.lang.String, java.lang.String, com.a2a.adjava.xmodele.MPackage, com.a2a.adjava.xmodele.MEntityImpl, com.a2a.adjava.xmodele.MEntityInterface, java.lang.String)
	 */
	@Override
	public MDaoImpl createDaoImpl(String p_sDaoName, String p_sDaoBeanName,
			MPackage p_oPackageDao, MEntityImpl p_oEntity,
			MEntityInterface p_oEntityInterface, String p_sQueryDefinitionFile) {
		return new MIOSDaoImpl(p_sDaoName, p_sDaoBeanName, p_oPackageDao, p_oEntity, p_oEntityInterface, p_sQueryDefinitionFile);
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.XModeleFactory#createDaoInterface(java.lang.String, java.lang.String, com.a2a.adjava.xmodele.MPackage, com.a2a.adjava.xmodele.MDaoImpl, com.a2a.adjava.xmodele.MEntityImpl)
	 */
	@Override
	public MDaoInterface createDaoInterface(String p_sInterfaceName,
			String p_sBeanName, MPackage p_oDaoInterfacePackage,
			MDaoImpl p_oDao, MEntityImpl p_oClass) {
		return new MIOSDaoInterface(p_sInterfaceName, p_sBeanName, p_oDaoInterfacePackage, p_oDao, p_oClass);
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.XModeleFactory#createVisualField(java.lang.String, java.lang.String, java.lang.String, com.a2a.adjava.xmodele.ui.view.MVFLabelKind, com.a2a.adjava.xmodele.ui.view.MVFLocalization, java.lang.String, boolean)
	 */
	@Override
	public MVisualField createVisualField(String p_sName, MLabel p_oLabel,
			String p_sComponent, MVFLabelKind p_oLabelKind,
			MVFLocalization p_oTarget, String p_sAttributeName,
			boolean p_bMandatoryKind) {
		return new MIOSVisualField(p_sName, p_oLabel, p_sComponent, p_oLabelKind, p_oTarget,
				p_sAttributeName, p_bMandatoryKind);
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.XModeleFactory#createVisualField(java.lang.String, com.a2a.adjava.xmodele.MVisualField)
	 */
	@Override
	public MVisualField createVisualField(String p_sPath, MVisualField p_oField, MLabel p_oLabel) {
		MIOSVisualField r_oMIOSVisualField = new MIOSVisualField(p_sPath, p_oField, p_oLabel);
		return r_oMIOSVisualField;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.XModeleFactory#createVisualField(java.lang.String, java.lang.String, java.lang.String, com.a2a.adjava.xmodele.ui.view.MVFLabelKind, java.lang.String, boolean)
	 */
	@Override
	public MVisualField createVisualField(String p_sName, MLabel p_oLabel, String p_sComponent, MVFLabelKind p_oLabelKind,
			String p_sAttributeName, boolean p_bMandatoryKind) {
		return new MIOSVisualField(p_sName, p_oLabel, p_sComponent, p_oLabelKind, p_sAttributeName, p_bMandatoryKind);
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.XModeleFactory#createVisualField(java.lang.String, com.a2a.adjava.types.IUITypeDescription, com.a2a.adjava.xmodele.ui.view.MVFModifier, com.a2a.adjava.xmodele.ui.view.MVFLabelKind, com.a2a.adjava.xmodele.MAttribute, com.a2a.adjava.xmodele.ui.view.MVFLocalization, com.a2a.adjava.xmodele.IDomain, java.lang.String, boolean)
	 */
	@Override
	public MVisualField createVisualField(String p_sPrefix, MLabel p_oLabel, IUITypeDescription p_oUiType, MVFModifier p_bMVFModifier,
			MVFLabelKind p_oLabelKind, MAttribute p_oAttribute,	MVFLocalization p_oLocalisation,
			IDomain<IModelDictionary, IModelFactory> p_oDomain,
			String p_sAttributeName, boolean p_bMandatory) {
		MIOSVisualField r_oMIOSVisualField = new MIOSVisualField(p_sPrefix + "__value", p_oLabel,
				p_oUiType.getComponentType(p_bMVFModifier), p_oAttribute.getTypeDesc().getEditType(),
				p_oAttribute.getLength(), p_oAttribute.getPrecision(), p_oAttribute.getScale(),
				p_oLabelKind, p_oLocalisation, p_sAttributeName, p_bMandatory, p_oAttribute.getMEnumeration(), p_bMVFModifier == MVFModifier.READONLY, p_oUiType.getUmlName());
		r_oMIOSVisualField.setCellType(((IOSUITypeDescription)p_oUiType).getCellType(p_bMVFModifier));
		return r_oMIOSVisualField;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.XModeleFactory#createVisualField(java.lang.String, com.a2a.adjava.types.IUITypeDescription, com.a2a.adjava.xmodele.ui.view.MVFModifier, com.a2a.adjava.xmodele.ui.view.MVFLabelKind, com.a2a.adjava.xmodele.MAttribute, com.a2a.adjava.xmodele.XDomain, java.lang.String, boolean)
	 */
	@Override
	public MVisualField createVisualField(String p_sPrefix,	MLabel p_oLabel, IUITypeDescription p_oTypeVisual, MVFModifier p_oModifier,
			MVFLabelKind p_oLabelKind, MAttribute p_oAttribute,	IDomain<IModelDictionary, IModelFactory> p_oDomain,
			String p_sAttributeName, boolean p_bMandatory) {
		return this.createVisualField(p_sPrefix, p_oLabel, p_oTypeVisual, p_oModifier, p_oLabelKind, p_oAttribute,
				MVFLocalization.DEFAULT, p_oDomain, p_sAttributeName, p_bMandatory);
	}

	/**
	 * Genere an IOS id
	 * @param p_sParts string parts to generate id
	 * @return ios identifier
	 */
	public String genereId( String... p_sParts ) {
		if (StringUtils.join(p_sParts).hashCode() > Integer.MIN_VALUE) {
			return Integer.toString(Math.abs(StringUtils.join(p_sParts).hashCode()));
		} else {
			return "0";
		}
	}

	/**
	 * Creates a xib container 
	 * @param p_oPage the page of the container
	 * @param p_oVm view model
	 * @param p_oController controller
	 * @return new xib container
	 */
	public MIOSXibContainer createXibContainer( MPage p_oPage, MViewModelImpl p_oVm,  MIOSController p_oController ) {

		if(p_oController.getControllerType().equals(MIOSControllerType.FIXEDLISTVIEW)) {
			MIOSXibFixedListContainer r_oFixedListXibContainer = new MIOSXibFixedListContainer( 
					IOSVMNamingHelper.getInstance().computeXibNameOfFixedListItem(p_oVm));
			r_oFixedListXibContainer.setViewFixedListName( 
					IOSVMNamingHelper.getInstance().computeViewNameOfFixedList(p_oVm));
			r_oFixedListXibContainer.setCellItemFixedListName( 
					IOSVMNamingHelper.getInstance().computeViewModelNameOfFixedListItem( p_oPage , p_oVm ));
			return r_oFixedListXibContainer;
		}
		else if(p_oController.getControllerType().equals(MIOSControllerType.LISTVIEW2D)) {
			MIOS2DListViewController oList2DViewController = (MIOS2DListViewController) p_oController;
			MIOSExpandableListXibContainer r_oMIOSExpandableListXibContainer = new MIOSExpandableListXibContainer( 
					IOSVMNamingHelper.getInstance().computeXibNameOfExpandableListSection(oList2DViewController));
			r_oMIOSExpandableListXibContainer.setViewExpandableListSectionName( 
					IOSVMNamingHelper.getInstance().computeViewNameOfExpandableListSection(oList2DViewController, oList2DViewController.getFirstSection()));
			return r_oMIOSExpandableListXibContainer;
		}
		else if(p_oController.getControllerType().equals(MIOSControllerType.COMBOVIEW)) {
			MIOSXibComboContainer r_oMIOSXibContainer = new MIOSXibComboContainer( 
					IOSVMNamingHelper.getInstance().computeXibNameOfSelectedItemForCombo(p_oVm ), true);
			r_oMIOSXibContainer.setPickerItemName(IOSVMNamingHelper.getInstance().computeXibNameOfItemForCombo(p_oVm ));
			r_oMIOSXibContainer.setPickerSelectedItemName(IOSVMNamingHelper.getInstance().computeXibNameOfSelectedItemForCombo(p_oVm));

			return r_oMIOSXibContainer;
		}
		return null;
	}
	
	/**
	 * Creates a label for a controller
	 * @param p_oControllerType the type of the controller
	 * @param p_sBaseKeyName the key name
	 * @param p_sBaseName the base name
	 * @return label for the controller
	 */
	public MIOSLabel createLabelForController( MIOSControllerType p_oControllerType, String p_sBaseKeyName, String p_sBaseName) {
		
		StringBuilder sBuilder = new StringBuilder();
		switch(p_oControllerType) {
		case COMBOVIEW:
			return null;
		case NAVIGATION : 
			return null;
		case WORKSPACE:
			sBuilder.append("workspace");
			break;
		default:
			sBuilder.append("screen");
			break;
		}
		sBuilder.append("_title_");
		sBuilder.append(p_sBaseKeyName);
		
		return new MIOSLabel(sBuilder.toString(),  this.createLabelValue(p_sBaseName));
	}
}
