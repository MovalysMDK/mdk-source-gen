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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.types.TypeDescription;
import com.a2a.adjava.xmodele.ui.menu.MMenuDef;
import com.a2a.adjava.xmodele.ui.navigation.MNavigation;

public interface IModelDictionary {

	/**
	 * Constant type for NO TYPE
	 */
	public static final ITypeDescription NOTYPE = new TypeDescription();

	/**
	 * Define the view model creator
	 * @param p_oCreator view model creator
	 */
	public void registerViewModelCreator(MViewModelCreator p_oCreator);

	/**
	 * Return the view model creator
	 * @return view model creator
	 */
	public MViewModelCreator getViewModelCreator();

	/**
	 * Register a package
	 * @param p_oPackage package
	 */
	public void registerPackage(MPackage p_oPackage);

	/**
	 * Register an enumeration
	 * @param p_oEnumeration enumeration
	 */
	public void registerEnumeration(MEnumeration p_oEnumeration);

	/**
	 * Register an entity
	 * @param p_oClass entity
	 */
	public void registerClass(MEntityImpl p_oClass);

	/**
	 * Register a join class
	 * @param p_oJoinClass join class
	 */
	public void registerJoinClass(MJoinEntityImpl p_oJoinClass);

	/**
	 * Unregister a join class
	 * @param p_oJoinClass join class
	 */
	public void unregisterJoinClass(MJoinEntityImpl p_oJoinClass);

	/**
	 * Regsister an entity interface
	 * @param p_oInterface entity interface
	 * @param p_oEntity entity implementation
	 */
	public void registerInterface(MEntityInterface p_oInterface,
			MEntityImpl p_oEntity);

	/**
	 * Register a view model
	 * @param p_oViewModel view model
	 */
	public void registerViewModel(MViewModelImpl p_oViewModel);

	/**
	 * Register a view model interface
	 * @param p_oViewModel view model interface
	 */
	public void registerViewModelInterface(
			MViewModelInterface p_oViewModel);

	/**
	 * Register a layout
	 * @param p_oLayout layout
	 */
	public void registerLayout(MLayout p_oLayout);

	/**
	 * Register a screen
	 * @param p_oScreen screen
	 */
	public void registerScreen(MScreen p_oScreen);

	/**
	 * Register a comment
	 * @param p_oComment comment
	 */
	public void registerComment(MComment p_oComment);

	/**
	 * Return main screen of application
	 * @return main screen of application
	 */
	public MScreen getMainScreen();

	/**
	 * Register a panel
	 * @param p_oPanel panel
	 */
	public void registerPanel(MPage p_oPanel);

	/**
	 * Register a dialog
	 * @param p_oMDialog dialog
	 */
	public void registerDialog(MDialog p_oMDialog);

	/**
	 * Register an action
	 * @param p_oAction action
	 */
	public void registerAction(MAction p_oAction);

	/**
	 * Register an action interface
	 * @param p_oAction action interface
	 */
	public void registerActionInterface(MActionInterface p_oAction);

	/**
	 * Register an adapter
	 * @param p_oAdapter adapter
	 */
	public void registerAdapter(MAdapter p_oAdapter);

	/**
	 * Register a dao implementation
	 * @param p_oDao dao
	 */
	public void registerDao(MDaoImpl p_oDao);

	/**
	 * Register a dao interface
	 * @param p_oDaoItf dao interface
	 * @param p_oEntityInterface entity matching with the dao interface
	 */
	public void registerDaoItf(MDaoInterface p_oDaoItf,
			MEntityInterface p_oEntityInterface);

	/**
	 * Return dao interface by entity name
	 * @param p_sName entity interface name
	 * @return dao interface
	 */
	public MDaoInterface getDaoItfByEntityItf(String p_sName);

	/**
	 * Add missing index on many2one relation
	 * @param p_oMAssociationManyToOne many2one relation
	 */
	public void addMissingIndexFor(
			MAssociationManyToOne p_oMAssociationManyToOne);

	/**
	 * Return package by name
	 * @param p_sPackageName package name
	 * @return MPackage
	 */
	public MPackage getPackage(String p_sPackageName);

	/**
	 * Return enumeration by name
	 * @param p_sName enumeration name
	 * @return MEnumeration
	 */
	public MEnumeration getMEnumeration(String p_sName);

	/**
	 * Return all enumerations
	 * @return enumerations
	 */
	public Collection<MEnumeration> getAllEnumerations();

	/**
	 * Return MClass by name
	 * @param p_sName name
	 * @return MClass
	 */
	public MEntityImpl getMClass(String p_sName);

	/**
	 * Return MEntityImpl by interface name
	 * @param p_sName interface name
	 * @return MEntityImpl
	 */
	public MEntityImpl getEntityByItf(String p_sName);

	/**
	 * Return join entity
	 * @param p_sName name
	 * @return MJoinEntityImpl
	 */
	public MJoinEntityImpl getMJoinClass(String p_sName);

	/**
	 * Return Action by name
	 * @param p_sName action name
	 * @return action
	 */
	public MAction getAction(String p_sName);

	/**
	 * Return all entities
	 * @return entities
	 */
	public Collection<MEntityImpl> getAllEntities();

	/**
	 * Return all join entities
	 * @return join entities
	 */
	public Collection<MJoinEntityImpl> getAllJoinClasses();

	/**
	 * Return all entity interfaces
	 * @return entity interfaces
	 */
	public Collection<MEntityInterface> getAllInterfaces();

	/**
	 * Return all adapters
	 * @return all adapters
	 */
	public Collection<MAdapter> getAllAdapters();

	/**
	 * Return all actions
	 * @return all actions
	 */
	public Collection<MAction> getAllActions();

	/**
	 * Return all action interfaces
	 * @return all action interfaces
	 */
	public Collection<MActionInterface> getAllActionInterfaces();

	/**
	 * Return all daos
	 * @return all daos
	 */
	public Collection<MDaoImpl> getAllDaos();

	/**
	 * Return all viewmodels
	 * @return all viewmodels
	 */
	public Collection<MViewModelImpl> getAllViewModels();

	/**
	 * Return all viewmodel interfaces
	 * @return all viewmodel interfaces
	 */
	public Collection<MViewModelInterface> getAllViewModelInterfaces();

	/**
	 * Return all screens
	 * @return all screens
	 */
	public Collection<MScreen> getAllScreens();
	
	/**
	 * Return all panels
	 * @return all panels
	 */
	public Collection<MPage> getAllPanels();

	/**
	 * Return all dialogs
	 * @return all dialogs
	 */
	public Collection<MDialog> getAllDialogs();

	/**
	 * Return all comments
	 * @return all comments
	 */
	public Collection<MComment> getAllComments();

	/**
	 * Return screen by name
	 * @param p_sUmlName uml name
	 * @return MScreen
	 */
	public MScreen getScreen(String p_sUmlName);

	/**
	 * Return panel by name
	 * @param p_sUmlName panel name
	 * @return panel
	 */
	public MPage getPanel(String p_sUmlName);

	/**
	 * Return dialog by name
	 * @param p_sUmlName dialog name
	 * @return dialog
	 */
	public MDialog getDialog(String p_sUmlName);

	/**
	 * Return all layouts
	 * @return all layouts
	 */
	public Collection<MLayout> getAllLayouts();

	/**
	 * Return view model by key
	 * @param p_sKey view model key
	 * @return view model
	 */
	public MViewModelImpl getViewModel(String p_sKey);

	/**
	 * Return layout by name
	 * @param p_sLayoutName layout name
	 * @return layout
	 */
	public MLayout getLayout(String p_sLayoutName);

	/**
	 * Return all packages
	 * @return all packages
	 */
	public Collection<MPackage> getAllPackages();

	/**
	 * Return all missing indexes
	 * @return all missing indexes
	 */
	public List<MAssociationManyToOne> getMissingIndexes();

	/**
	 * Register a type description
	 * @param p_sId id
	 * @param p_oTypeDescription type description
	 */
	public void registerTypeDescription(String p_sId,
			ITypeDescription p_oTypeDescription);

	/**
	 * Register a menu definition
	 * @param p_oMenu menu definition
	 */
	public void registerMenuDef(MMenuDef p_oMenu);

	/**
	 * Return menu definition by id
	 * @param p_sMenuId id of menu definition
	 * @return MMenuDef
	 */
	public MMenuDef getMenuDef(String p_sMenuId);

	/**
	 * Return type description by id
	 * @param p_sId id of type description
	 * @return ITypeDescription
	 */
	public ITypeDescription getTypeDescription(String p_sId);

	/**
	 * Return map of UmlClass to MClass
	 * @return Map
	 */
	public Map<String, MEntityImpl> getMapUmlClassToMClasses();

	/**
	 * Add a mapping between uml name and entity class
	 * @param p_sUmlName uml name
	 * @param p_oClass entity class
	 */
	public void mapUmlClassToMClass(String p_sUmlName,
			MEntityImpl p_oClass);

	/**
	 * Add a label
	 * @param p_sKey label key
	 * @param p_sDefaultValue label value
	 */
	public void addLabel(MLabel p_oLabel);

	/**
	 * Return all labels
	 * @return all labels
	 */
	public Collection<MLabel> getLabels();
	
	/**
	 * Return all labels
	 * @return all labels
	 */
	public Map<String,MLabel> getLabelMap();

	/**
	 * Return type description for No Type
	 * @return  type description for No Type
	 */
	public ITypeDescription getNoType();

	/**
	 * Register a navigation
	 * @param p_oNav navigation to register
	 */
	public void registerNavigation(MNavigation p_oNav);

	/**
	 * Return navigation list that link to Screen
	 * @param p_oTargetScreen target screen
	 * @return navigation list
	 */
	public List<MNavigation> getNavigationsToScreen(
			MScreen p_oTargetScreen);
	
	/**
	 * Return navigation list that link from Screen
	 * @param p_oSourceScreen source screen
	 * @return navigation list
	 */
	public List<MNavigation> getNavigationsFromScreen(
			MScreen p_oSourceScreen);

	/**
	 * Return navigations between two screens
	 * @param p_oSourceScreen source screen
	 * @param p_oTargetScreen target screen
	 * @return navigation list
	 */
	public List<MNavigation> getNavigationsBetweenScreen(
			MScreen p_oSourceScreen, MScreen p_oTargetScreen);
}