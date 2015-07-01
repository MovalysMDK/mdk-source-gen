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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.xmodele.ui.menu.MMenuDef;
import com.a2a.adjava.xmodele.ui.navigation.MNavigation;

/**
 * Dictionnary
 * @author Michenux
 *
 */
public class ModelDictionary implements IModelDictionary {
	
	/**
	 * Packages
	 */
	private Map<String,MPackage> packages = new HashMap<>();
	
	/**
	 * Classes 
	 */
	private Map<String,MEntityImpl> classes = new HashMap<>();
	
	/**
	 * Classes by interface 
	 */
	private Map<String,MEntityImpl> classesByItf = new HashMap<>();
	
	/**
	 * Enumeration 
	 */
	private Map<String,MEnumeration> enumerations = new HashMap<>();
	
	/**
	 * Join classes 
	 */
	private Map<String,MJoinEntityImpl> joinClasses = new HashMap<>();
	
	/**
	 * Map for interfaces 
	 */
	private Map<String,MEntityInterface> interfaces = new HashMap<>();
	
	/**
	 * Map for dao implementations
	 */
	private Map<String,MDaoImpl> daos = new HashMap<>();
	
	/**
	 * Map for dao interfaces 
	 */
	private Map<String,MDaoInterface> daosItfByEntityItf = new HashMap<>();
	
	/**
	 * Map for missing indexes
	 */
	private List<MAssociationManyToOne> missingIndexes = new ArrayList<>();
	
	/**
	 * Type descriptions
	 */
	private Map<String,ITypeDescription> typeDescriptions = new HashMap<>();
	
	/**
	 * Map umlClass to MClass
	 */
	private Map<String,MEntityImpl> mapUmlClassToMClasses = new HashMap<>();
	
	/**
	 * Map for viewmodels 
	 */
	private Map<String,MViewModelImpl> viewModels = new HashMap<>();
	
	/**
	 * Map for viewModel interfaces
	 */
	private Map<String,MViewModelInterface> viewModelInterfaces = new HashMap<>();
	
	/**
	 * Map for screens 
	 */
	private Map<String,MScreen> screens = new HashMap<>();
	
	/**
	 * Map for panels
	 */
	private Map<String,MPage> panels = new HashMap<>();
	
	/**
	 * Map for layouts 
	 */
	private Map<String,MLayout> layouts = new HashMap<>();
	
	/**
	 * Map for adapters 
	 */
	private Map<String,MAdapter> adapters = new HashMap<>();
	
	/**
	 * Map for actions
	 */
	private Map<String,MAction> actions = new HashMap<>();
	
	/**
	 * Map for action interfaces
	 */
	private Map<String,MActionInterface> actionInterfaces = new HashMap<>();
	
	/**
	 * Map for menu definitions 
	 */
	private Map<String,MMenuDef> menuDefs = new HashMap<>();
	
	/**
	 * Map for dialogs 
	 */
	private Map<String,MDialog> dialogs = new HashMap<>();
	
	/**
	 * Map for comments 
	 */
	private Map<String,MComment> comments = new HashMap<>();
	
	/**
	 * Viewmodel controller 
	 */
	private MViewModelCreator vmc ;
	
	/**
	 * Map for labels 
	 */
	private Map<String,MLabel> labels = new TreeMap<>() ;
	
	/**
	 * Navigations 
	 */
	private List<MNavigation> navigations = new ArrayList<>() ;
		
	static {
		NOTYPE.setName(StringUtils.EMPTY);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#registerViewModelCreator(com.a2a.adjava.xmodele.MViewModelCreator)
	 */
	@Override
	public void registerViewModelCreator(MViewModelCreator p_oCreator) {
		this.vmc = p_oCreator;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getViewModelCreator()
	 */
	@Override
	public MViewModelCreator getViewModelCreator() {
		return this.vmc;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#registerPackage(com.a2a.adjava.xmodele.MPackage)
	 */
	@Override
	public void registerPackage( MPackage p_oPackage) {
		this.packages.put(p_oPackage.getFullName(), p_oPackage);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#registerEnumeration(com.a2a.adjava.xmodele.MEnumeration)
	 */
	@Override
	public void registerEnumeration(MEnumeration p_oEnumeration) {
		this.enumerations.put(p_oEnumeration.getFullName(), p_oEnumeration);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#registerClass(com.a2a.adjava.xmodele.MEntityImpl)
	 */
	@Override
	public void registerClass( MEntityImpl p_oClass ) {
		this.classes.put(p_oClass.getFullName(), p_oClass);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#registerJoinClass(com.a2a.adjava.xmodele.MJoinEntityImpl)
	 */
	@Override
	public void registerJoinClass( MJoinEntityImpl p_oJoinClass ) {
		this.joinClasses.put(p_oJoinClass.getFullName(), p_oJoinClass);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#unregisterJoinClass(com.a2a.adjava.xmodele.MJoinEntityImpl)
	 */
	@Override
	public void unregisterJoinClass( MJoinEntityImpl p_oJoinClass ) {
		this.joinClasses.remove(p_oJoinClass.getFullName());
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#registerInterface(com.a2a.adjava.xmodele.MEntityInterface, com.a2a.adjava.xmodele.MEntityImpl)
	 */
	@Override
	public void registerInterface( MEntityInterface p_oInterface, MEntityImpl p_oEntity) {
		this.interfaces.put( p_oInterface.getFullName(), p_oInterface);
		this.classesByItf.put(p_oInterface.getFullName(), p_oEntity);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#registerViewModel(com.a2a.adjava.xmodele.MViewModelImpl)
	 */
	@Override
	public void registerViewModel( MViewModelImpl p_oViewModel) {
		this.viewModels.put( p_oViewModel.getFullName(), p_oViewModel);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#registerViewModelInterface(com.a2a.adjava.xmodele.MViewModelInterface)
	 */
	@Override
	public void registerViewModelInterface( MViewModelInterface p_oViewModel) {
		this.viewModelInterfaces.put( p_oViewModel.getFullName(), p_oViewModel);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#registerLayout(com.a2a.adjava.xmodele.MLayout)
	 */
	@Override
	public void registerLayout(MLayout p_oLayout) {
		this.layouts.put(p_oLayout.getName(), p_oLayout);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#registerScreen(com.a2a.adjava.xmodele.MScreen)
	 */
	@Override
	public void registerScreen( MScreen p_oScreen) {
		this.screens.put( p_oScreen.getUmlName(), p_oScreen);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#registerComment(com.a2a.adjava.xmodele.MComment)
	 */
	@Override
	public void registerComment( MComment p_oComment) {
		this.comments.put( p_oComment.getName(), p_oComment);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getMainScreen()
	 */
	@Override
	public MScreen getMainScreen() {
		MScreen r_oScreen = null ;
		for( MScreen oScreen : this.screens.values()) {
			if ( oScreen.isMain()) {
				r_oScreen = oScreen ;
				break;
			}
		}
		return r_oScreen ;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#registerPanel(com.a2a.adjava.xmodele.MPage)
	 */
	@Override
	public void registerPanel( MPage p_oPanel ) {
		this.panels.put(p_oPanel.getUmlName(), p_oPanel);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#registerDialog(com.a2a.adjava.xmodele.MDialog)
	 */
	@Override
	public void registerDialog( MDialog p_oMDialog) {
		this.dialogs.put( p_oMDialog.getUmlName(), p_oMDialog);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#registerAction(com.a2a.adjava.xmodele.MAction)
	 */
	@Override
	public void registerAction( MAction p_oAction) {
		this.actions.put( p_oAction.getFullName(), p_oAction);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#registerActionInterface(com.a2a.adjava.xmodele.MActionInterface)
	 */
	@Override
	public void registerActionInterface( MActionInterface p_oAction) {
		this.actionInterfaces.put( p_oAction.getFullName(), p_oAction);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#registerAdapter(com.a2a.adjava.xmodele.MAdapter)
	 */
	@Override
	public void registerAdapter( MAdapter p_oAdapter) {
		this.adapters.put( p_oAdapter.getFullName(), p_oAdapter);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#registerDao(com.a2a.adjava.xmodele.MDaoImpl)
	 */
	@Override
	public void registerDao( MDaoImpl p_oDao) {
		this.daos.put(p_oDao.getName(), p_oDao);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#registerDaoItf(com.a2a.adjava.xmodele.MDaoInterface, com.a2a.adjava.xmodele.MEntityInterface)
	 */
	@Override
	public void registerDaoItf(MDaoInterface p_oDaoItf, MEntityInterface p_oEntityInterface) {
		this.daosItfByEntityItf.put(p_oEntityInterface.getFullName(), p_oDaoItf);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getDaoItfByEntityItf(java.lang.String)
	 */
	@Override
	public MDaoInterface getDaoItfByEntityItf(String p_sName) {
		return this.daosItfByEntityItf.get(p_sName);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#addMissingIndexFor(com.a2a.adjava.xmodele.MAssociationManyToOne)
	 */
	@Override
	public void addMissingIndexFor( MAssociationManyToOne p_oMAssociationManyToOne ) {
		this.missingIndexes.add( p_oMAssociationManyToOne );
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getPackage(java.lang.String)
	 */
	@Override
	public MPackage getPackage( String p_sPackageName ) {
		return (MPackage) this.packages.get(p_sPackageName);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getMEnumeration(java.lang.String)
	 */
	@Override
	public MEnumeration getMEnumeration(String p_sName) {
		return (MEnumeration) this.enumerations.get(p_sName);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getAllEnumerations()
	 */
	@Override
	public Collection<MEnumeration> getAllEnumerations() {
		return this.enumerations.values();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getMClass(java.lang.String)
	 */
	@Override
	public MEntityImpl getMClass( String p_sName ) {
		return (MEntityImpl) this.classes.get(p_sName) ;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getEntityByItf(java.lang.String)
	 */
	@Override
	public MEntityImpl getEntityByItf( String p_sName ) {
		return (MEntityImpl) this.classesByItf.get(p_sName) ;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getMJoinClass(java.lang.String)
	 */
	@Override
	public MJoinEntityImpl getMJoinClass( String p_sName ) {
		return (MJoinEntityImpl) this.joinClasses.get(p_sName) ;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getAction(java.lang.String)
	 */
	@Override
	public MAction getAction( String p_sName ) {
		return (MAction) this.actions.get(p_sName) ;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getAllEntities()
	 */
	@Override
	public Collection<MEntityImpl> getAllEntities() {
		return this.classes.values();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getAllJoinClasses()
	 */
	@Override
	public Collection<MJoinEntityImpl> getAllJoinClasses() {
		return this.joinClasses.values();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getAllInterfaces()
	 */
	@Override
	public Collection<MEntityInterface> getAllInterfaces() {
		return this.interfaces.values();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getAllAdapters()
	 */
	@Override
	public Collection<MAdapter> getAllAdapters() {
		return this.adapters.values();
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getAllActions()
	 */
	@Override
	public Collection<MAction> getAllActions() {
		return this.actions.values();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getAllActionInterfaces()
	 */
	@Override
	public Collection<MActionInterface> getAllActionInterfaces() {
		return this.actionInterfaces.values();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getAllDaos()
	 */
	@Override
	public Collection<MDaoImpl> getAllDaos() {
		return this.daos.values();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getAllViewModels()
	 */
	@Override
	public Collection<MViewModelImpl> getAllViewModels() {
		return this.viewModels.values();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getAllViewModelInterfaces()
	 */
	@Override
	public Collection<MViewModelInterface> getAllViewModelInterfaces() {
		return this.viewModelInterfaces.values();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getAllScreens()
	 */
	@Override
	public Collection<MScreen> getAllScreens() {
		return this.screens.values();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getAllPanels()
	 */
	@Override
	public Collection<MPage> getAllPanels() {
		return this.panels.values();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getAllDialogs()
	 */
	@Override
	public Collection<MDialog> getAllDialogs() {
		return this.dialogs.values();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getAllComments()
	 */
	@Override
	public Collection<MComment> getAllComments() {
		return this.comments.values();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getScreen(java.lang.String)
	 */
	@Override
	public MScreen getScreen(String p_sUmlName) {
		return this.screens.get(p_sUmlName);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getPanel(java.lang.String)
	 */
	@Override
	public MPage getPanel( String p_sUmlName ) {
		return this.panels.get( p_sUmlName );
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getDialog(java.lang.String)
	 */
	@Override
	public MDialog getDialog(String p_sUmlName) {
		return this.dialogs.get(p_sUmlName);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getAllLayouts()
	 */
	@Override
	public Collection<MLayout> getAllLayouts() {
		return this.layouts.values();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getViewModel(java.lang.String)
	 */
	@Override
	public MViewModelImpl getViewModel(String p_sKey) {
		return this.viewModels.get(p_sKey);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getLayout(java.lang.String)
	 */
	@Override
	public MLayout getLayout(String p_sLayoutName) {
		return this.layouts.get(p_sLayoutName);
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getAllPackages()
	 */
	@Override
	public Collection<MPackage> getAllPackages() {
		return this.packages.values();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getMissingIndexes()
	 */
	@Override
	public List<MAssociationManyToOne> getMissingIndexes() {
		return missingIndexes ;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#registerTypeDescription(java.lang.String, com.a2a.adjava.types.ITypeDescription)
	 */
	@Override
	public void registerTypeDescription(String p_sId, ITypeDescription p_oTypeDescription ) {
		this.typeDescriptions.put(p_sId, p_oTypeDescription );		
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#registerMenuDef(com.a2a.adjava.xmodele.ui.menu.MMenuDef)
	 */
	@Override
	public void registerMenuDef(MMenuDef p_oMenu) {
		this.menuDefs.put(p_oMenu.getId(), p_oMenu);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getMenuDef(java.lang.String)
	 */
	@Override
	public MMenuDef getMenuDef( String p_sMenuId ) {
		return (MMenuDef) this.menuDefs.get( p_sMenuId );
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getTypeDescription(java.lang.String)
	 */
	@Override
	public ITypeDescription getTypeDescription( String p_sId ) {
		return (ITypeDescription) this.typeDescriptions.get( p_sId );
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getMapUmlClassToMClasses()
	 */
	@Override
	public Map<String, MEntityImpl> getMapUmlClassToMClasses() {
		return this.mapUmlClassToMClasses;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#mapUmlClassToMClass(java.lang.String, com.a2a.adjava.xmodele.MEntityImpl)
	 */
	@Override
	public void mapUmlClassToMClass( String p_sUmlName, MEntityImpl p_oClass ) {
		this.mapUmlClassToMClasses.put(p_sUmlName, p_oClass);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#addLabel(java.lang.String, java.lang.String)
	 */
	@Override
	public void addLabel( MLabel p_oLabel ) {
		this.labels.put(p_oLabel.getKey(), p_oLabel);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getLabels()
	 */
	@Override
	public Collection<MLabel> getLabels() {
		return this.labels.values();
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getNoType()
	 */
	@Override
	public ITypeDescription getNoType() {
		return NOTYPE;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#registerNavigation(com.a2a.adjava.xmodele.ui.navigation.MNavigation)
	 */
	@Override
	public void registerNavigation( MNavigation p_oNav ) {
		this.navigations.add(p_oNav);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getNavigationsToScreen(com.a2a.adjava.xmodele.MScreen)
	 */
	@Override
	public List<MNavigation> getNavigationsToScreen( MScreen p_oTargetScreen ) {
		List<MNavigation> r_listNavigations = new ArrayList<MNavigation>();
		for( MNavigation oNav: this.navigations ) {
			if ( oNav.getTarget() == p_oTargetScreen ) {
				r_listNavigations.add(oNav);
			}
		}
		return r_listNavigations;
	}

	
	public List<MNavigation> getNavigationsFromScreen( MScreen p_oSourceScreen ) {
		List<MNavigation> r_listNavigations = new ArrayList<MNavigation>();
		for( MNavigation oNav: this.navigations ) {
			if ( oNav.getSource() == p_oSourceScreen ) {
				r_listNavigations.add(oNav);
			}
		}
		return r_listNavigations;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.IModeleDictionnary#getNavigationsBetweenScreen(com.a2a.adjava.xmodele.MScreen, com.a2a.adjava.xmodele.MScreen)
	 */
	@Override
	public List<MNavigation> getNavigationsBetweenScreen(MScreen p_oSourceScreen, MScreen p_oTargetScreen) {
		List<MNavigation> r_listNavigations = new ArrayList<MNavigation>();
		for( MNavigation oNav: this.navigations ) {
			if ( oNav.getTarget() == p_oTargetScreen && oNav.getSource() == p_oSourceScreen ) {
				r_listNavigations.add(oNav);
			}
		}
		return r_listNavigations;
	}

	@Override
	public Map<String, MLabel> getLabelMap() {
		return this.labels;
	}
}
