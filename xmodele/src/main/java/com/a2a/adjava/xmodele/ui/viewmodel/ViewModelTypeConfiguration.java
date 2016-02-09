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
package com.a2a.adjava.xmodele.ui.viewmodel;

import java.util.Map;

import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;


/**
 * <p>Detail of an option for a specific <code>ViewModel</code> type.</p>
 *
 * <p>Copyright (c) 2012</p>
 * <p>Company: Adeuza</p>
 *
 * @author fbourlieux
 */
public class ViewModelTypeConfiguration {

	/** ioption du composant. Ex: permet de différencier <code>MMSpinner</code> et <code>MMSearchSpinner</code> */
	private String configName ;
	/** objet à utiliser pour stocker une liste d'élément */
	private String listName ; 
	/** */
	private String listFullName ;
	/** imports supplémentaire à utiliser*/
	private String[] imports ;  
	/** nom du composant visuel à utiliser */
	private String visualComponentFullName ; 
	/** */
	private String visualComponentName ;
	/** nom de l'adapter à utiliser */
	private String adapterName ; 
	/** */
	private String adapterFullName ;
	/** nom de l'interface de l'élément de base */
	private String interfaceName ; 
	/** */
	private String interfaceFullName ;
	/** nom de l'interface des sous éléments */
	private String subElementName ; 
	/** */
	private String subElementFullName ;
	/** */
	private int maxLevel = 0;
	/** liste dynamique de propriétés*/
	private Map<String, String> propertyList;
	/** nom de l'implémentation de l'élément de base */
	private String implementationName ;
	
	
	/**
	 * @param p_oDomain
	 * @return
	 */
	public ITypeDescription getTypeDescription(IDomain<IModelDictionary, IModelFactory> p_oDomain) {
		ITypeDescription typeDesc = null;
		if (getListFullName()!=null && !getListFullName().isEmpty()) {
			typeDesc = p_oDomain.getLanguageConf().createObjectTypeDescription(getListFullName());
		}
		else {
			typeDesc = IModelDictionary.NOTYPE;
		}
		return typeDesc;
	}
	
	/**
	 * TODO Décrire la méthode getOption de la classe ViewModelTypeOption
	 * @return
	 */
	public String getConfigName() {
		return this.configName;
	}

	/**
	 * TODO Décrire la méthode setOption de la classe ViewModelTypeOption
	 * @param p_sOption
	 */
	public void setConfigName(String p_sOption) {
		this.configName = p_sOption;
	}

	/**
	 * TODO Décrire la méthode setInterfaceName de la classe ViewModelTypeOption
	 * @param p_sInterfaceName
	 */
	public void setInterfaceName(final String p_sInterfaceName){
		interfaceName=p_sInterfaceName;
	}
	
	/**
	 * TODO Décrire la méthode getInterfaceName de la classe ViewModelTypeOption
	 * @return
	 */
	public String getInterfaceName() {
		return interfaceName;
	}

	/**
	 * TODO Décrire la méthode setInterfaceFullName de la classe ViewModelTypeOption
	 * @param p_sInterfaceFullName
	 */
	public void setInterfaceFullName(final String p_sInterfaceFullName) {
		interfaceFullName=p_sInterfaceFullName;
	}
	
	/**
	 * TODO Décrire la méthode getInterfaceFullName de la classe ViewModelTypeOption
	 * @return
	 */
	public String getInterfaceFullName() {
		return interfaceFullName;
	}
	
	/**
	 * TODO Décrire la méthode setAdapterName de la classe ViewModelTypeOption
	 * @param p_sAdapterName
	 */
	public void setAdapterName(final String p_sAdapterName){
		adapterName=p_sAdapterName;
	}
	
	/**
	 * TODO Décrire la méthode getAdapterName de la classe ViewModelTypeOption
	 * @return
	 */
	public String getAdapterName() {
		return adapterName;
	}

	/**
	 * TODO Décrire la méthode setAdapterFullName de la classe ViewModelTypeOption
	 * @param p_sAdapterFullName
	 */
	public void setAdapterFullName(final String p_sAdapterFullName){
		adapterFullName=p_sAdapterFullName;
	}

	/**
	 * TODO Décrire la méthode getAdapterFullName de la classe ViewModelTypeOption
	 * @return
	 */
	
	public String getAdapterFullName() {
		return adapterFullName;
	}

	/**
	 * TODO Décrire la méthode setListName de la classe ViewModelTypeOption
	 * @param p_sListName
	 */
	public void setListName(final String p_sListName){
		listName=p_sListName;
	}

	/**
	 * TODO Décrire la méthode getListName de la classe ViewModelTypeOption
	 * @return
	 */
	public String getListName() {
		if (listName!=null) {
			return listName;
		}
		else {
			return "";
		}
	}

	/**
	 * TODO Décrire la méthode setVisualComponentNameFull de la classe ViewModelTypeOption
	 * @param p_sVisualComponentNameFull
	 */
	public void setVisualComponentNameFull(final String p_sVisualComponentNameFull) {
		visualComponentFullName=p_sVisualComponentNameFull;
	}
	
	/**
	 * TODO Décrire la méthode getVisualComponentNameFull de la classe ViewModelTypeOption
	 * @return
	 */
	public String getVisualComponentNameFull() {
		return visualComponentFullName;
	}

	/**
	 * TODO Décrire la méthode setVisualComponentName de la classe ViewModelTypeOption
	 * @param p_sVisualComponentName
	 */
	public void setVisualComponentName(final String p_sVisualComponentName) {
		visualComponentName=p_sVisualComponentName;
	}

	/**
	 * TODO Décrire la méthode getVisualComponentName de la classe ViewModelTypeOption
	 * @return
	 */
	public String getVisualComponentName() {
		return visualComponentName;
	}
	
	/**
	 * TODO Décrire la méthode setListFullName de la classe ViewModelTypeOption
	 * @param p_sListFullName
	 */
	
	public void setListFullName(final String p_sListFullName){
		listFullName=p_sListFullName;
	}
	
	/**
	 * TODO Décrire la méthode getListFullName de la classe ViewModelTypeOption
	 * @return
	 */
	public String getListFullName() {
		if (listFullName!=null) {
			return listFullName;
		}
		else {
			return "";
		}
	}

	/**
	 * TODO Décrire la méthode setImports de la classe ViewModelTypeOption
	 * @param p_oImports
	 */
	public void setImports(final String[] p_oImports){
		imports=p_oImports;
	}

	/**
	 * TODO Décrire la méthode getImports de la classe ViewModelTypeOption
	 * @return
	 */
	public String[] getImports() {
		return imports;
	}
	
	/**
	 * TODO Décrire la méthode setSubElementName de la classe ViewModelTypeOption
	 * @param p_sSubElementName
	 */
	public void setSubElementName(final String p_sSubElementName) {
		subElementName=p_sSubElementName;
	}

	/**
	 * TODO Décrire la méthode getSubElementName de la classe ViewModelTypeOption
	 * @return
	 */

	public String getSubElementName() {
		return subElementName;
	}


	/**
	 * TODO Décrire la méthode setSubElementFullName de la classe ViewModelTypeOption
	 * @param p_sSubElementFullName
	 */
	public void setSubElementFullName(final String p_sSubElementFullName) {
		subElementFullName=p_sSubElementFullName;
	}

	/**
	 * TODO Décrire la méthode getSubElementFullName de la classe ViewModelTypeOption
	 * @return
	 */
	public String getSubElementFullName() {
		return subElementFullName;
	}

	/**
	 * TODO Décrire la méthode setMaxLevel de la classe ViewModelTypeOption
	 * @param p_iMaxLevel
	 */
	public void setMaxLevel(final int p_iMaxLevel){
		maxLevel=p_iMaxLevel;
	}

	/**
	 * TODO Décrire la méthode getMaxLevel de la classe ViewModelTypeOption
	 * @return
	 */
	public int getMaxLevel()  {
		return maxLevel;
	}
	
	public ViewModelTypeConfiguration clone()
	{
		ViewModelTypeConfiguration clonedVMTypeConf = new ViewModelTypeConfiguration();
		
		clonedVMTypeConf.setConfigName(configName);
		clonedVMTypeConf.setListName(listName) ; 
		clonedVMTypeConf.setListFullName(listFullName) ;
		clonedVMTypeConf.setImports(imports);  
		clonedVMTypeConf.setVisualComponentNameFull(visualComponentFullName); 
		clonedVMTypeConf.setVisualComponentName(visualComponentName);
		clonedVMTypeConf.setAdapterName(adapterName); 
		clonedVMTypeConf.setAdapterFullName(adapterFullName);
		clonedVMTypeConf.setInterfaceName(interfaceName); 
		clonedVMTypeConf.setInterfaceFullName(interfaceFullName);
		clonedVMTypeConf.setSubElementName(subElementName); 
		clonedVMTypeConf.setSubElementFullName(subElementFullName);
		clonedVMTypeConf.setMaxLevel(maxLevel);
		clonedVMTypeConf.setImplementationName(implementationName); 
		
		return clonedVMTypeConf;
	}

	/**
	 * Get the property list
	 * @return property list
	 */
	
	public Map<String, String> getPropertyList() {
		return propertyList;
	}
	
	/**
	 * Set the property list
	 * @param p_propertyList the property list
	 */

	public void setPropertyList(Map<String, String> p_propertyList) {
		this.propertyList = p_propertyList;
	}

	/**
	 * Get the implementation name
	 * @return implementation name
	 */
	public String getImplementationName() {
		return implementationName;
	}

	/**
	 * Set the implementation name
	 * @param p_implementationName the implementation name
	 */
	public void setImplementationName(String p_implementationName) {
		this.implementationName = p_implementationName;
	}
}
