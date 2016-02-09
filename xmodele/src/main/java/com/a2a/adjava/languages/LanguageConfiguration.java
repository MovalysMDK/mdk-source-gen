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
package com.a2a.adjava.languages;

import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.types.IUITypeDescription;
import com.a2a.adjava.types.TypeDescriptionFactory;
import com.a2a.adjava.types.UITypeDescriptionFactory;
import com.a2a.adjava.xmodele.ui.view.MVFModifier;

/**
 * Language configuration
 * @author lmichenaud
 *
 */
/**
 * @author lmichenaud
 *
 */
public class LanguageConfiguration {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(LanguageConfiguration.class);
	
	/**
	 * Language name
	 */
	private String name;

	/**
	 * Type description factory
	 */
	private TypeDescriptionFactory typeDescriptionFactory;
	
	/**
	 * UI Type description factory
	 */
	private UITypeDescriptionFactory uiTypeDescriptionFactory ;

	/**
	 * Type descriptions
	 */
	private Map<String, ITypeDescription> typeDescriptions = new TreeMap<String, ITypeDescription>();

	/**
	 * UI Type descriptions
	 */
	private Map<String, IUITypeDescription> uiTypeDescriptions = new TreeMap<String, IUITypeDescription>();

	/**
	 * Interface name prefix
	 */
	private String interfaceNamingPrefix;

	/**
	 * Interface name suffix
	 */
	private String interfaceNamingSuffix;

	/**
	 * Interface subpackage name
	 */
	private String interfaceSubPackageName;

	/**
	 * Name prefix for entity implementations
	 */
	private String implNamingPrefix;

	/**
	 * Name suffix for entity implementations
	 */
	private String implNamingSuffix;

	/**
	 * Subpackage for entity implementations
	 */
	private String implSubPackageName;

	/**
	 * Prefix for enumeration name
	 */
	private String enumNamingPrefix;
	
	/**
	 * Suffix for enumeration name
	 */
	private String enumNamingSuffix;

	/**
	 * Subpackage for enumeration
	 */
	private String enumSubPackageName;
	
	/**
	 * Name prefix for pojo factory
	 */
	private String pojoFactoryInterfaceNamingPrefix;

	/**
	 * Name suffix for pojo factory
	 */
	private String pojoFactoryInterfaceNamingSuffix;

	/**
	 * Subpackage name for pojo factory
	 */
	private String pojoFactoryInterfaceSubPackageName;

	/**
	 * Name prefix for pojo factory implementation
	 */
	private String pojoFactoryImplNamingPrefix;

	/**
	 * Name suffix for pojo factory implementation
	 */
	private String pojoFactoryImplNamingSuffix;

	/**
	 * Subpackage name for pojo factory implementation
	 */
	private String pojoFactoryImplSubPackageName;

	/**
	 * Name prefix for dao interface
	 */
	private String daoInterfaceNamingPrefix;

	/**
	 * Name suffix for dao interface
	 */
	private String daoInterfaceNamingSuffix;

	/**
	 * Subpackage for dao interface
	 */
	private String daoInterfaceSubPackageName;

	/**
	 * Name prefix for dao implementation
	 */
	private String daoImplementationNamingPrefix;

	/**
	 * Name suffix for dao implementation
	 */
	private String daoImplementationNamingSuffix;

	/**
	 * Subpackage for dao implementation
	 */
	private String daoImplementationSubPackageName;

	/**
	 * Name prefix for view model interface
	 */
	private String viewModelInterfaceNamingPrefix;

	/**
	 * Name Suffix for view model interface
	 */
	private String viewModelInterfaceNamingSuffix;

	/**
	 * Subpackage for view model interface
	 */
	private String viewModelInterfaceSubPackageName;

	/**
	 * Name prefix for view model implementation
	 */
	private String viewModelImplementationNamingPrefix;

	/**
	 * Name Suffix for view model implementation
	 */
	private String viewModelImplementationNamingSuffix;

	/**
	 * Subpackage for view model implementation
	 */
	private String viewModelImplementationSubPackageName;

	/**
	 * Name prefix for section interface
	 */
	private String sectionInterfaceNamingPrefix;

	/**
	 * Name Suffix for section interface
	 */
	private String sectionInterfaceNamingSuffix;

	/**
	 * Subpackage for section interface
	 */
	private String sectionInterfaceSubPackageName;

	/**
	 * Name prefix for section implementation
	 */
	private String sectionImplementationNamingPrefix;

	/**
	 * Name Suffix for section implementation
	 */
	private String sectionImplementationNamingSuffix;

	/**
	 * Subpackage for section implementation
	 */
	private String sectionImplementationSubPackageName;

	/**
	 * Name prefix for section interface
	 */
	private String dataloaderInterfaceNamingPrefix;

	/**
	 * Name Suffix for section interface
	 */
	private String dataloaderInterfaceNamingSuffix;

	/**
	 * Subpackage for section interface
	 */
	private String dataloaderInterfaceSubPackageName;

	/**
	 * Name prefix for section implementation
	 */
	private String dataloaderImplementationNamingPrefix;

	/**
	 * Name Suffix for section implementation
	 */
	private String dataloaderImplementationNamingSuffix;

	/**
	 * Subpackage for section implementation
	 */
	private String dataloaderImplementationSubPackageName;

	/**
	 * Name prefix for adapter implementation
	 */
	private String adapterImplementationNamingPrefix;

	/**
	 * Name Suffix for adapter implementation
	 */
	private String adapterImplementationNamingSuffix;

	/**
	 * Subpackage for adapter implementation
	 */
	private String adapterImplementationSubPackageName;

	/**
	 * Subpackage for screen
	 */
	private String screenSubPackageName;

	/**
	 * Name prefix for action implementation
	 */
	private String actionImplementationNamingPrefix;

	/**
	 * Name suffix for action implementation
	 */
	private String actionImplementationNamingSuffix;

	/**
	 * Subpackage for action implementation
	 */
	private String actionImplementationSubPackageName;

	/**
	 * Name prefix for action interface
	 */
	private String actionInterfaceNamingPrefix;

	/**
	 * Name suffix for action interface
	 */
	private String actionInterfaceNamingSuffix;

	/**
	 * Subpackage for action interface
	 */
	private String actionInterfaceSubPackageName;

	/**
	 * Null value
	 */
	private String nullValue ;
	
	/**
	 * Start marker for non-generated source code
	 */
	private String nonGeneratedStartMarker;

	/**
	 * End marker for non-generated source code
	 */
	private String nonGeneratedEndMarker;

	/**
	 * Start marker for includes
	 */
	private String includeStartMarker;

	/**
	 * End marker for includes
	 */
	private String includeEndMarker;

	/**
	 * Constructor
	 * 
	 * @param p_sName language config name
	 */
	public LanguageConfiguration(String p_sName) {
		this.name = p_sName;
	}

	/**
	 * Get type description by name
	 * @param p_sTypeDescName type description name
	 * @return ITypeDescription
	 */
	public ITypeDescription getTypeDescription(String p_sTypeDescName) {
		return this.typeDescriptions.get(p_sTypeDescName);
	}

	/**
	 * Create a new type description for class p_sFullName
	 * 
	 * @param p_sFullName full name of type description
	 * @return ITypeDescription
	 */
	public ITypeDescription createObjectTypeDescription(String p_sFullName) {
		ITypeDescription r_oObject = (ITypeDescription) this
				.getTypeDescription("Object").clone();
		r_oObject.setName(p_sFullName);
		return r_oObject;
	}

	/**
	 * Return the UI type description
	 * 
	 * @param p_sUiTypeDescName name of ui type description
	 * @return IUITypeDescription
	 */
	public IUITypeDescription getUiTypeDescription(String p_sUiTypeDescName, String p_sVersion) {
		IUITypeDescription oType = null;
		if (p_sVersion==null) {
			oType = this.uiTypeDescriptions.get(p_sUiTypeDescName);
		}
		else {
			oType = this.uiTypeDescriptions.get(p_sUiTypeDescName + "-" + p_sVersion);
			if (oType == null) {
				oType = this.uiTypeDescriptions.get(p_sUiTypeDescName);
			}
		}
		return oType;
	}

	/**
	 * Return all type descriptions
	 * @return map of type descriptions
	 */
	public Map<String, ITypeDescription> getTypeDescriptions() {
		return this.typeDescriptions;
	}

	/**
	 * Return all ui type descriptions
	 * @return map of ui type descriptions
	 */
	public Map<String, IUITypeDescription> getUiTypeDescriptions() {
		return this.uiTypeDescriptions;
	}

	/**
	 * Return all ui type descriptions
	 * @return map of ui type descriptions
	 */
	public IUITypeDescription getUiTypeDescriptionByComponentType( String p_sComponentType ) {
		IUITypeDescription r_oIUITypeDescription = null ;
		for ( IUITypeDescription oIUITypeDescription : this.uiTypeDescriptions.values() ) {
			if (oIUITypeDescription.getComponentType(MVFModifier.READONLY).equalsIgnoreCase(p_sComponentType)
				|| oIUITypeDescription.getComponentType(MVFModifier.WRITABLE).equalsIgnoreCase(p_sComponentType) ){
				r_oIUITypeDescription = oIUITypeDescription ;
				break ;
			}
		}
		return r_oIUITypeDescription;
	}
	
	
	/**
	 * Return a type description by UML name
	 * @return map of ui type descriptions
	 */
	public IUITypeDescription getUiTypeDescriptionByUmlName( String p_sUmlName ) {
		IUITypeDescription r_oIUITypeDescription = null ;
		for ( IUITypeDescription oIUITypeDescription : this.uiTypeDescriptions.values() ) {
			if (oIUITypeDescription.getUmlName().equalsIgnoreCase(p_sUmlName) ){
				r_oIUITypeDescription = oIUITypeDescription ;
				break ;
			}
		}
		return r_oIUITypeDescription;
	}
	
	/**
	 * Return suffix for dao interfaces
	 * @return suffix for dao interfaces
	 */
	public String getDaoInterfaceNamingSuffix() {
		return this.daoInterfaceNamingSuffix;
	}

	/**
	 * Return subpackage for dao interfaces
	 * @return subpackage for dao interfaces
	 */
	public String getDaoInterfaceSubPackageName() {
		return this.daoInterfaceSubPackageName;
	}

	/**
	 * Return prefix for dao implementations
	 * 
	 * @return prefix for dao implementations
	 */
	public String getDaoImplementationNamingPrefix() {
		return this.daoImplementationNamingPrefix;
	}

	/**
	 * Return suffix for dao implementations
	 * @return suffix for dao implementations
	 */
	public String getDaoImplementationNamingSuffix() {
		return this.daoImplementationNamingSuffix;
	}

	/**
	 * Return subpackage for dao implementations
	 * @return subpackage for dao implementations
	 */
	public String getDaoImplementationSubPackageName() {
		return this.daoImplementationSubPackageName;
	}

	/**
	 * Add a type description
	 * @param p_sName name of type description
	 * @param p_oTypeDescription type description
	 */
	public void addTypeDescription(String p_sName,
			ITypeDescription p_oTypeDescription) {
		this.typeDescriptions.put(p_sName, p_oTypeDescription);
	}

	/**
	 * Add a  ui type description
	 * @param p_sName name of ui type description
	 * @param p_oUiTypeDescription ui type description
	 */
	public void addUiTypeDescription(String p_sName,
			IUITypeDescription p_oUiTypeDescription) {
		this.uiTypeDescriptions.put(p_sName, p_oUiTypeDescription);
	}

	/**
	 * Return prefix for entity implementation
	 * @return prefix for entity implementation
	 */
	public String getImplNamingSuffix() {
		return this.implNamingSuffix;
	}

	/**
	 * Return subpackage for entity implementation
	 * @return subpackage for entity implementation
	 */
	public String getImplSubPackageName() {
		return this.implSubPackageName;
	}

	/**
	 * Return suffix for entity interface
	 * @return suffix for entity interface
	 */
	public String getInterfaceNamingSuffix() {
		return this.interfaceNamingSuffix;
	}

	/**
	 * Return subpackage for entity interfaces
	 * @return subpackage for entity interfaces
	 */
	public String getInterfaceSubPackageName() {
		return this.interfaceSubPackageName;
	}

	/**
	 * Return suffix for entity factory interface
	 * @return suffix for entity factory interface
	 */
	public String getPojoFactoryInterfaceNamingSuffix() {
		return this.pojoFactoryInterfaceNamingSuffix;
	}

	/**
	 * Return subpackage for entity factory interface
	 * 
	 * @return subpackage for entity factory interface
	 */
	public String getPojoFactoryInterfaceSubPackageName() {
		return this.pojoFactoryInterfaceSubPackageName;
	}

	/**
	 * Define suffix for entity interface
	 * @param p_sInterfaceNamingSuffix suffix for entity interface
	 */
	public void setInterfaceNamingSuffix(String p_sInterfaceNamingSuffix) {
		this.interfaceNamingSuffix = p_sInterfaceNamingSuffix;
	}

	/**
	 * Define subpackage for entity interface
	 * @param p_sInterfaceSubPackageName subpackage for entity interface
	 */
	public void setInterfaceSubPackageName(String p_sInterfaceSubPackageName) {
		this.interfaceSubPackageName = p_sInterfaceSubPackageName;
	}

	/**
	 * Define suffix for entity implementations
	 * @param p_sImplNamingSuffix suffix for entity implementations
	 */
	public void setImplNamingSuffix(String p_sImplNamingSuffix) {
		this.implNamingSuffix = p_sImplNamingSuffix;
	}

	/**
	 * Define subpackage for entity implementations
	 * @param p_sImplSubPackageName subpackage for entity implementations
	 */
	public void setImplSubPackageName(String p_sImplSubPackageName) {
		this.implSubPackageName = p_sImplSubPackageName;
	}

	/**
	 * Define suffix for pojo factory interfaces
	 * @param p_sPojoFactoryInterfaceNamingSuffix suffix for pojo factory interfaces
	 */
	public void setPojoFactoryInterfaceNamingSuffix(
			String p_sPojoFactoryInterfaceNamingSuffix) {
		this.pojoFactoryInterfaceNamingSuffix = p_sPojoFactoryInterfaceNamingSuffix;
	}

	/**
	 * Define subpackage for pojo factory interfaces
	 * @param p_sPojoFactoryInterfaceSubPackageName subpackage for pojo factory interfaces
	 */
	public void setPojoFactoryInterfaceSubPackageName(
			String p_sPojoFactoryInterfaceSubPackageName) {
		this.pojoFactoryInterfaceSubPackageName = p_sPojoFactoryInterfaceSubPackageName;
	}

	/**
	 * Define suffix for pojo factory implementation
	 * @param p_sPojoFactoryImplNamingSuffix suffix for pojo factory implementation
	 */
	public void setPojoFactoryImplNamingSuffix(
			String p_sPojoFactoryImplNamingSuffix) {
		this.pojoFactoryImplNamingSuffix = p_sPojoFactoryImplNamingSuffix;
	}

	/**
	 * Define subpackage for pojo factory implementation
	 * @param p_sPojoFactoryImplSubPackageName subpackage for pojo factory implementation
	 */
	public void setPojoFactoryImplSubPackageName(
			String p_sPojoFactoryImplSubPackageName) {
		this.pojoFactoryImplSubPackageName = p_sPojoFactoryImplSubPackageName;
	}

	/**
	 * Define suffix for dao interfaces
	 * @param p_sDaoInterfaceNamingSuffix suffix for dao interfaces
	 */
	public void setDaoInterfaceNamingSuffix(String p_sDaoInterfaceNamingSuffix) {
		this.daoInterfaceNamingSuffix = p_sDaoInterfaceNamingSuffix;
	}

	/**
	 * Define subpackage for dao interfaces
	 * @param p_sDaoInterfaceSubPackageName subpackage for dao interfaces
	 */
	public void setDaoInterfaceSubPackageName(
			String p_sDaoInterfaceSubPackageName) {
		this.daoInterfaceSubPackageName = p_sDaoInterfaceSubPackageName;
	}

	/**
	 * Define prefix for dao implementations
	 * @param p_sDaoImplementationNamingPrefix prefix for dao implementations
	 */
	public void setDaoImplementationNamingPrefix(
			String p_sDaoImplementationNamingPrefix) {
		this.daoImplementationNamingPrefix = p_sDaoImplementationNamingPrefix;
	}

	/**
	 * Define suffix for dao implementations
	 * @param p_sDaoImplementationNamingSuffix suffix for dao implementations
	 */
	public void setDaoImplementationNamingSuffix(
			String p_sDaoImplementationNamingSuffix) {
		this.daoImplementationNamingSuffix = p_sDaoImplementationNamingSuffix;
	}

	/**
	 * Define subpackage for dao implementations
	 * @param p_sDaoImplementationSubPackageName subpackage for dao implementations
	 */
	public void setDaoImplementationSubPackageName(
			String p_sDaoImplementationSubPackageName) {
		this.daoImplementationSubPackageName = p_sDaoImplementationSubPackageName;
	}

	/**
	 * Define start marker for non-generated bloc
	 * @param p_sNonGeneratedStartMarker start marker for non-generated bloc
	 */
	public void setNonGeneratedStartMarker(String p_sNonGeneratedStartMarker) {
		this.nonGeneratedStartMarker = p_sNonGeneratedStartMarker;
	}

	/**
	 * Define end marker for non-generated bloc
	 * @param p_sNonGeneratedEndMarker end marker for non-generated bloc
	 */
	public void setNonGeneratedEndMarker(String p_sNonGeneratedEndMarker) {
		this.nonGeneratedEndMarker = p_sNonGeneratedEndMarker;
	}

	/**
	 * Define start marker for includes
	 * @param p_sIncludeStartMarker start marker for includes
	 */
	public void setIncludeStartMarker(String p_sIncludeStartMarker) {
		this.includeStartMarker = p_sIncludeStartMarker;
	}

	/**
	 * Define end marker for includes
	 * @param p_sIncludeEndMarker end marker for includes
	 */
	public void setIncludeEndMarker(String p_sIncludeEndMarker) {
		this.includeEndMarker = p_sIncludeEndMarker;
	}

	/**
	 * Define prefix for pojo factory interfaces
	 * 
	 * @param p_sPojoFactoryInterfaceNamingPrefix prefix for pojo factory interfaces
	 */
	public void setPojoFactoryInterfaceNamingPrefix(
			String p_sPojoFactoryInterfaceNamingPrefix) {
		this.pojoFactoryInterfaceNamingPrefix = p_sPojoFactoryInterfaceNamingPrefix;
	}

	/**
	 * Return prefix for pojo factory interfaces
	 * 
	 * @return prefix for pojo factory interfaces
	 */
	public String getPojoFactoryImplNamingPrefix() {
		return this.pojoFactoryImplNamingPrefix;
	}

	/**
	 * Define prefix for pojo factory implementations
	 * 
	 * @param p_sPojoFactoryImplNamingPrefix p_sPojoFactoryImplNamingPrefix
	 */
	public void setPojoFactoryImplNamingPrefix(
			String p_sPojoFactoryImplNamingPrefix) {
		this.pojoFactoryImplNamingPrefix = p_sPojoFactoryImplNamingPrefix;
	}

	/**
	 * Return prefix for dao interfaces
	 * 
	 * @return prefix for dao interfaces
	 */
	public String getDaoInterfaceNamingPrefix() {
		return this.daoInterfaceNamingPrefix;
	}

	/**
	 * Define prefix for dao interfaces
	 * 
	 * @param p_sDaoInterfaceNamingPrefix
	 *            prefix for dao interfaces
	 */
	public void setDaoInterfaceNamingPrefix(String p_sDaoInterfaceNamingPrefix) {
		this.daoInterfaceNamingPrefix = p_sDaoInterfaceNamingPrefix;
	}

	/**
	 * Return prefix for entity interfaces
	 * 
	 * @return prefix for entity interfaces
	 */
	public String getInterfaceNamingPrefix() {
		return this.interfaceNamingPrefix;
	}

	/**
	 * Define prefix for entity interfaces
	 * 
	 * @param p_sInterfaceNamingPrefix prefix for entity interfaces
	 */
	public void setInterfaceNamingPrefix(String p_sInterfaceNamingPrefix) {
		this.interfaceNamingPrefix = p_sInterfaceNamingPrefix;
	}

	/**
	 * Return prefix for entity implementations
	 * 
	 * @return prefix for entity implementations
	 */
	public String getImplNamingPrefix() {
		return this.implNamingPrefix;
	}

	/**
	 * Define prefix for entity implementations
	 * 
	 * @param p_sImplNamingPrefix prefix for entity implementations
	 */
	public void setImplNamingPrefix(String p_sImplNamingPrefix) {
		this.implNamingPrefix = p_sImplNamingPrefix;
	}

	/**
	 * Get prefix for enumeration
	 * @return prefix for enumeration
	 */
	public String getEnumNamingPrefix() {
		return this.enumNamingPrefix;
	}

	/**
	 * Set prefix for enumeration
	 * @param p_sEnumNamingPrefix suffix for enumeration
	 */
	public void setEnumNamingPrefix(String p_sEnumNamingPrefix) {
		this.enumNamingPrefix = p_sEnumNamingPrefix;
	}

	/**
	 * Get suffix for enumeration
	 * @return suffix for enumeration
	 */
	public String getEnumNamingSuffix() {
		return this.enumNamingSuffix;
	}

	/**
	 * Set suffix for enumeration
	 * @param p_sEnumNamingSuffix suffix for enumeration
	 */
	public void setEnumNamingSuffix(String p_sEnumNamingSuffix) {
		this.enumNamingSuffix = p_sEnumNamingSuffix;
	}

	/**
	 * Get subpackage for enumeration
	 * @return subpackage for enumeration
	 */
	public String getEnumSubPackageName() {
		return this.enumSubPackageName;
	}

	/**
	 * Set subpackage name for enumeration
	 * @param p_sEnumSubPackageName package name for enumeration
	 */
	public void setEnumSubPackageName(String p_sEnumSubPackageName) {
		this.enumSubPackageName = p_sEnumSubPackageName;
	}

	/**
	 * Return prefix for pojo factory interfaces
	 * 
	 * @return prefix for pojo factory interfaces
	 */
	public String getPojoFactoryInterfaceNamingPrefix() {
		return this.pojoFactoryInterfaceNamingPrefix;
	}

	/**
	 * Return suffix for pojo factory interfaces
	 * 
	 * @return suffix for pojo factory interfaces
	 */
	public String getPojoFactoryImplNamingSuffix() {
		return this.pojoFactoryImplNamingSuffix;
	}

	/**
	 * Return subpackage for pojo factory implementations
	 * 
	 * @return String pojoFactoryImplSubPackageName
	 */
	public String getPojoFactoryImplSubPackageName() {
		return this.pojoFactoryImplSubPackageName;
	}

	/**
	 * Return end marker for non-generated bloc
	 * @return end marker for non-generated bloc
	 */
	public String getNonGeneratedEndMarker() {
		return nonGeneratedEndMarker;
	}

	/**
	 * Return start marker for non-generated bloc
	 * @return start marker for non-generated bloc
	 */
	public String getNonGeneratedStartMarker() {
		return nonGeneratedStartMarker;
	}

	/**
	 * Return start marker for includes
	 * @return start marker for includes
	 */
	public String getIncludeStartMarker() {
		return this.includeStartMarker;
	}

	/**
	 * Return end marker for includes
	 * @return end marker for includes
	 */
	public String getIncludeEndMarker() {
		return this.includeEndMarker;
	}

	/**
	 * Return factory for type description
	 * 
	 * @return factory for type description
	 */
	public TypeDescriptionFactory getTypeDescriptionFactory() {
		return this.typeDescriptionFactory;
	}

	/**
	 * Define factory for type description
	 * 
	 * @param p_oTypeDescriptionFactory factory for type description
	 */
	public void setTypeDescriptionFactory(
			TypeDescriptionFactory p_oTypeDescriptionFactory) {
		this.typeDescriptionFactory = p_oTypeDescriptionFactory;
	}

	/**
	 * Return name of language configuration
	 * 
	 * @return name of language configuration
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Return prefix for view model interfaces
	 * @return prefix for view model interfaces
	 */
	public String getViewModelInterfaceNamingPrefix() {
		return this.viewModelInterfaceNamingPrefix;
	}

	/**
	 * Define prefix for view model interfaces
	 * @param p_sViewModelInterfaceNamingPrefix prefix for view model interfaces
	 */
	public void setViewModelInterfaceNamingPrefix(
			String p_sViewModelInterfaceNamingPrefix) {
		this.viewModelInterfaceNamingPrefix = p_sViewModelInterfaceNamingPrefix;
	}

	/**
	 * Return suffix for view model interfaces
	 * @return suffix for view model interfaces
	 */
	public String getViewModelInterfaceNamingSuffix() {
		return this.viewModelInterfaceNamingSuffix;
	}

	/**
	 * Define suffix for view model interfaces
	 * @param p_sViewModelInterfaceNamingSuffix suffix for view model interfaces
	 */
	public void setViewModelInterfaceNamingSuffix(
			String p_sViewModelInterfaceNamingSuffix) {
		this.viewModelInterfaceNamingSuffix = p_sViewModelInterfaceNamingSuffix;
	}

	/**
	 * Return subpackage for view model interfaces
	 * @return subpackage for view model interfaces
	 */
	public String getViewModelInterfaceSubPackageName() {
		return this.viewModelInterfaceSubPackageName;
	}

	/**
	 * Define subpackage for view model interfaces
	 * @param p_sViewModelInterfaceSubPackageName subpackage for view model interfaces
	 */
	public void setViewModelInterfaceSubPackageName(
			String p_sViewModelInterfaceSubPackageName) {
		this.viewModelInterfaceSubPackageName = p_sViewModelInterfaceSubPackageName;
	}


	/**
	 * Return prefix for section interfaces
	 * @return prefix for section interfaces
	 */
	public String getSectionInterfaceNamingPrefix() {
		return this.sectionInterfaceNamingPrefix;
	}

	/**
	 * Define prefix for section interfaces
	 * @param p_sSectionInterfaceNamingPrefix prefix for section interfaces
	 */
	public void setSectionInterfaceNamingPrefix(
			String p_sSectionInterfaceNamingPrefix) {
		this.sectionInterfaceNamingPrefix = p_sSectionInterfaceNamingPrefix;
	}

	/**
	 * Return suffix for section interfaces
	 * @return suffix for section interfaces
	 */
	public String getSectionInterfaceNamingSuffix() {
		return this.sectionInterfaceNamingSuffix;
	}

	/**
	 * Define suffix for section interfaces
	 * @param p_sSectionInterfaceNamingSuffix suffix for section interfaces
	 */
	public void setSectionInterfaceNamingSuffix(
			String p_sSectionInterfaceNamingSuffix) {
		this.sectionInterfaceNamingSuffix = p_sSectionInterfaceNamingSuffix;
	}

	/**
	 * Return subpackage for section interfaces
	 * @return subpackage for section interfaces
	 */
	public String getSectionInterfaceSubPackageName() {
		return this.sectionInterfaceSubPackageName;
	}

	/**
	 * Define subpackage for section interfaces
	 * @param p_sSectionInterfaceSubPackageName subpackage for section interfaces
	 */
	public void setSectionInterfaceSubPackageName(
			String p_sSectionInterfaceSubPackageName) {
		this.sectionInterfaceSubPackageName = p_sSectionInterfaceSubPackageName;
	}

	/**
	 * Return prefix for section interfaces
	 * @return prefix for section interfaces
	 */
	public String getSectionImplementationNamingPrefix() {
		return this.sectionImplementationNamingPrefix;
	}

	/**
	 * Define prefix for section interfaces
	 * @param p_sSectionImplementationNamingPrefix prefix for section interfaces
	 */
	public void setSectionImplementationNamingPrefix(
			String p_sSectionImplementationNamingPrefix) {
		this.sectionImplementationNamingPrefix = p_sSectionImplementationNamingPrefix;
	}

	/**
	 * Return suffix for section interfaces
	 * @return suffix for section interfaces
	 */
	public String getSectionImplementationNamingSuffix() {
		return this.sectionImplementationNamingSuffix;
	}

	/**
	 * Define suffix for section interfaces
	 * @param p_sSectionImplementationNamingSuffix suffix for section interfaces
	 */
	public void setSectionImplementationNamingSuffix(
			String p_sSectionImplementationNamingSuffix) {
		this.sectionImplementationNamingSuffix = p_sSectionImplementationNamingSuffix;
	}

	/**
	 * Return subpackage for section interfaces
	 * @return subpackage for section interfaces
	 */
	public String getSectionImplementationSubPackageName() {
		return this.sectionImplementationSubPackageName;
	}

	/**
	 * Define subpackage for section interfaces
	 * @param p_sSectionImplementationSubPackageName subpackage for section interfaces
	 */
	public void setSectionImplementationSubPackageName(
			String p_sSectionImplementationSubPackageName) {
		this.sectionImplementationSubPackageName = p_sSectionImplementationSubPackageName;
	}

	
	/**
	 * Return prefix for dataloader interfaces
	 * @return prefix for dataloader interfaces
	 */
	public String getDataloaderInterfaceNamingPrefix() {
		return this.dataloaderInterfaceNamingPrefix;
	}

	/**
	 * Define prefix for dataloader interfaces
	 * @param p_sDataloaderInterfaceNamingPrefix prefix for dataloader interfaces
	 */
	public void setDataloaderInterfaceNamingPrefix(
			String p_sDataloaderInterfaceNamingPrefix) {
		this.dataloaderInterfaceNamingPrefix = p_sDataloaderInterfaceNamingPrefix;
	}

	/**
	 * Return suffix for dataloader interfaces
	 * @return suffix for dataloader interfaces
	 */
	public String getDataloaderInterfaceNamingSuffix() {
		return this.dataloaderInterfaceNamingSuffix;
	}

	/**
	 * Define suffix for dataloader interfaces
	 * @param p_sDataloaderInterfaceNamingSuffix suffix for dataloader interfaces
	 */
	public void setDataloaderInterfaceNamingSuffix(
			String p_sDataloaderInterfaceNamingSuffix) {
		this.dataloaderInterfaceNamingSuffix = p_sDataloaderInterfaceNamingSuffix;
	}

	/**
	 * Return subpackage for dataloader interfaces
	 * @return subpackage for dataloader interfaces
	 */
	public String getDataloaderInterfaceSubPackageName() {
		return this.dataloaderInterfaceSubPackageName;
	}

	/**
	 * Define subpackage for dataloader interfaces
	 * @param p_sDataloaderInterfaceSubPackageName subpackage for dataloader interfaces
	 */
	public void setDataloaderInterfaceSubPackageName(
			String p_sDataloaderInterfaceSubPackageName) {
		this.dataloaderInterfaceSubPackageName = p_sDataloaderInterfaceSubPackageName;
	}

	/**
	 * Return prefix for dataloader interfaces
	 * @return prefix for dataloader interfaces
	 */
	public String getDataloaderImplementationNamingPrefix() {
		return this.dataloaderImplementationNamingPrefix;
	}

	/**
	 * Define prefix for dataloader interfaces
	 * @param p_sDataloaderImplementationNamingPrefix prefix for dataloader interfaces
	 */
	public void setDataloaderImplementationNamingPrefix(
			String p_sDataloaderImplementationNamingPrefix) {
		this.dataloaderImplementationNamingPrefix = p_sDataloaderImplementationNamingPrefix;
	}

	/**
	 * Return suffix for dataloader interfaces
	 * @return suffix for dataloader interfaces
	 */
	public String getDataloaderImplementationNamingSuffix() {
		return this.dataloaderImplementationNamingSuffix;
	}

	/**
	 * Define suffix for dataloader interfaces
	 * @param p_sDataloaderImplementationNamingSuffix suffix for dataloader interfaces
	 */
	public void setDataloaderImplementationNamingSuffix(
			String p_sDataloaderImplementationNamingSuffix) {
		this.dataloaderImplementationNamingSuffix = p_sDataloaderImplementationNamingSuffix;
	}

	/**
	 * Return subpackage for dataloader interfaces
	 * @return subpackage for dataloader interfaces
	 */
	public String getDataloaderImplementationSubPackageName() {
		return this.dataloaderImplementationSubPackageName;
	}

	/**
	 * Define subpackage for dataloader interfaces
	 * @param p_sDataloaderImplementationSubPackageName subpackage for dataloader interfaces
	 */
	public void setDataloaderImplementationSubPackageName(
			String p_sDataloaderImplementationSubPackageName) {
		this.dataloaderImplementationSubPackageName = p_sDataloaderImplementationSubPackageName;
	}

	
	/**
	 * Return prefix for view model implementations
	 * @return prefix for view model implementations
	 */
	public String getViewModelImplementationNamingPrefix() {
		return this.viewModelImplementationNamingPrefix;
	}

	/**
	 * Define prefix for view model implementations
	 * @param p_sViewModelImplementationNamingPrefix prefix for view model implementations
	 */
	public void setViewModelImplementationNamingPrefix(
			String p_sViewModelImplementationNamingPrefix) {
		this.viewModelImplementationNamingPrefix = p_sViewModelImplementationNamingPrefix;
	}

	/**
	 * Return suffix for view model implementations
	 * @return suffix for view model implementations
	 */
	public String getViewModelImplementationNamingSuffix() {
		return this.viewModelImplementationNamingSuffix;
	}

	/**
	 * Define suffix for view model implementations
	 * @param p_sViewModelImplementationNamingSuffix suffix for view model implementations
	 */
	public void setViewModelImplementationNamingSuffix(
			String p_sViewModelImplementationNamingSuffix) {
		this.viewModelImplementationNamingSuffix = p_sViewModelImplementationNamingSuffix;
	}

	/**
	 * Return subpackage for view model implementations
	 * @return subpackage for view model implementations
	 */
	public String getViewModelImplementationSubPackageName() {
		return this.viewModelImplementationSubPackageName;
	}

	/**
	 * Define subpackage for view model implementations 
	 * @param p_sViewModelImplementationSubPackageName subpackage for view model implementations 
	 */
	public void setViewModelImplementationSubPackageName(
			String p_sViewModelImplementationSubPackageName) {
		this.viewModelImplementationSubPackageName = p_sViewModelImplementationSubPackageName;
	}

	/**
	 * Return subpackage for screen
	 * @return subpackage for screen
	 */
	public String getScreenSubPackageName() {
		return this.screenSubPackageName;
	}

	/**
	 * Define subpackage for screen
	 * @param p_sScreenSubPackageName subpackage for screen
	 */
	public void setScreenSubPackageName(String p_sScreenSubPackageName) {
		this.screenSubPackageName = p_sScreenSubPackageName;
	}

	/**
	 * Return prefix for adapter implementations
	 * @return prefix for adapter implementations
	 */
	public String getAdapterImplementationNamingPrefix() {
		return this.adapterImplementationNamingPrefix;
	}

	/**
	 * Define prefix for adapter implementations
	 * @param p_sAdapterImplementationNamingPrefix prefix for adapter implementations
	 */
	public void setAdapterImplementationNamingPrefix(
			String p_sAdapterImplementationNamingPrefix) {
		this.adapterImplementationNamingPrefix = p_sAdapterImplementationNamingPrefix;
	}

	/**
	 * Return suffix for adapter implementations
	 * @return suffix for adapter implementations
	 */
	public String getAdapterImplementationNamingSuffix() {
		return this.adapterImplementationNamingSuffix;
	}

	/**
	 * Define suffix for adapter implementations
	 * @param p_sAdapterImplementationNamingSuffix suffix for adapter implementations
	 */
	public void setAdapterImplementationNamingSuffix(
			String p_sAdapterImplementationNamingSuffix) {
		this.adapterImplementationNamingSuffix = p_sAdapterImplementationNamingSuffix;
	}

	/**
	 * Return subpackage for adapter implementations
	 * @return subpackage for adapter implementations
	 */
	public String getAdapterImplementationSubPackageName() {
		return this.adapterImplementationSubPackageName;
	}

	/**
	 * Define subpackage for adapter implementations
	 * @param p_sAdapterImplementationSubPackageName subpackage for adapter implementations
	 */
	public void setAdapterImplementationSubPackageName(
			String p_sAdapterImplementationSubPackageName) {
		this.adapterImplementationSubPackageName = p_sAdapterImplementationSubPackageName;
	}

	/**
	 * Return prefix for action implementations
	 * @return prefix for action implementations
	 */
	public String getActionImplementationNamingPrefix() {
		return this.actionImplementationNamingPrefix;
	}

	/**
	 * Define prefix for action implementations
	 * @param p_sActionImplementationNamingPrefix prefix for action implementations
	 */
	public void setActionImplementationNamingPrefix(
			String p_sActionImplementationNamingPrefix) {
		this.actionImplementationNamingPrefix = p_sActionImplementationNamingPrefix;
	}

	/**
	 * Return suffix for action implementations
	 * @return suffix for action implementations
	 */
	public String getActionImplementationNamingSuffix() {
		return this.actionImplementationNamingSuffix;
	}

	/**
	 * Define suffix for action implementations
	 * @param p_sActionImplementationNamingSuffix suffix for action implementations
	 */
	public void setActionImplementationNamingSuffix(
			String p_sActionImplementationNamingSuffix) {
		this.actionImplementationNamingSuffix = p_sActionImplementationNamingSuffix;
	}

	/**
	 * Return subpackage for action implementations
	 * @return subpackage for action implementations
	 */
	public String getActionImplementationSubPackageName() {
		return this.actionImplementationSubPackageName;
	}

	/**
	 * Define subpackage for action implementations
	 * @param p_sActionImplementationSubPackageName subpackage for action implementations
	 */
	public void setActionImplementationSubPackageName(
			String p_sActionImplementationSubPackageName) {
		this.actionImplementationSubPackageName = p_sActionImplementationSubPackageName;
	}

	/**
	 * Return prefix for action interfaces
	 * @return  prefix for action interfaces
	 */
	public String getActionInterfaceNamingPrefix() {
		return actionInterfaceNamingPrefix;
	}

	/**
	 * Define prefix for action interfaces
	 * @param p_sActionInterfaceNamingPrefix prefix for action interfaces
	 */
	public void setActionInterfaceNamingPrefix(
			String p_sActionInterfaceNamingPrefix) {
		this.actionInterfaceNamingPrefix = p_sActionInterfaceNamingPrefix;
	}

	/**
	 * Return suffix for action interfaces
	 * @return suffix for action interfaces
	 */
	public String getActionInterfaceNamingSuffix() {
		return actionInterfaceNamingSuffix;
	}

	/**
	 * Define suffix for action interfaces
	 * @param p_sActionInterfaceNamingSuffix suffix for action interfaces
	 */
	public void setActionInterfaceNamingSuffix(
			String p_sActionInterfaceNamingSuffix) {
		this.actionInterfaceNamingSuffix = p_sActionInterfaceNamingSuffix;
	}

	/**
	 * Return subpackage for action interfaces
	 * @return subpackage for action interfaces
	 */
	public String getActionInterfaceSubPackageName() {
		return this.actionInterfaceSubPackageName;
	}

	/**
	 * Define subpackage for action interfaces
	 * @param p_sActionInterfaceSubPackageName subpackage for action interfaces
	 */
	public void setActionInterfaceSubPackageName(
			String p_sActionInterfaceSubPackageName) {
		this.actionInterfaceSubPackageName = p_sActionInterfaceSubPackageName;
	}

	/**
	 * Return UI Type Description factory
	 * @return UI Type Description factory
	 */
	public UITypeDescriptionFactory getUITypeDescriptionFactory() {
		return uiTypeDescriptionFactory;
	}

	/**
	 * Define UI type description factory
	 * @param p_oUiTypeDescriptionFactory UI type description factory
	 */
	public void setUITypeDescriptionFactory(
			UITypeDescriptionFactory p_oUiTypeDescriptionFactory) {
		this.uiTypeDescriptionFactory = p_oUiTypeDescriptionFactory;
	}

	/**
	 * Get Null value
	 * @return null value
	 */
	public String getNullValue() {
		return this.nullValue;
	}

	/**
	 * Set null value
	 * @param p_sNullValue null value
	 */
	public void setNullValue(String p_sNullValue) {
		this.nullValue = p_sNullValue;
	}
}
