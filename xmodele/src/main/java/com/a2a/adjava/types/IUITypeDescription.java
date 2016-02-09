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
package com.a2a.adjava.types;

import java.util.Map;

import com.a2a.adjava.xmodele.ui.view.MVFModifier;

/**
 * UI Type description
 * @author lmichenaud
 *
 */
public interface IUITypeDescription {

	/**
	 * Return uml name
	 * @return uml name
	 */
	public String getUmlName();
	
	/**
	 * Define uml name
	 * @param p_sUmlName uml name
	 */
	public void setUmlName(String p_sUmlName);
	
	/**
	 * Return type for viewmodel when readwrite mode. Contains the type of derived data if  {@link com.a2a.adjava.types.IUITypeDescription#isSameTypeAsEntityForReadWrite()} is true
	 * @return type for viewmodel when readwrite mode
	 */
	public String getRWViewModelType();
	
	/**
	 * Define type for viewmodel when readwrite mode
	 * @param p_sVmt type for viewmodel when readwrite mode
	 */
	public void setRWViewModelType(String p_sVmt);
	
	/**
	 * Return ui component to use when readwrite mode
	 * @return ui component to use when readwrite mode
	 */
	public String getRwComponentType();
	
	/**
	 * Return ui component to use
	 * @param 
	 * @return ui component to use
	 */
	public String getComponentType( MVFModifier p_oMVFModifier );
	
	/**
	 * Define ui component to use when readwrite mode
	 * @param p_sComponentType ui component to use when readwrite mode
	 */
	public void setRWComponentType(String p_sComponentType);
	
	/**
	 * Return type for viewmodel when readonly mode. Contains the type of derived data if {@link com.a2a.adjava.types.IUITypeDescription#isSameTypeAsEntityForReadOnly()} is true
	 * @return type for viewmodel when readonly mode
	 */
	public String getROViewModelType();
	
	/**
	 * Define type for viewmodel when readonly mode
	 * @param p_sVmt type for viewmodel when readonly mode
	 */
	public void setROViewModelType(String p_sVmt);
	
	/**
	 * Return ui component when readonly mode
	 * @return ui component when readonly mode
	 */
	public String getROComponentType();
	
	/**
	 * Define ui component when readonly mode
	 * @param p_sComponentType ui component when readonly mode
	 */
	public void setROComponentType(String p_sComponentType);
	
	/**
	 * Return true if the type used to store the entity data is the same than the type used to store the data in the ui component in the read only mode else false
	 * @return Return true if the type used to store the entity data is the same than the type used to store the data in the ui component in the read only mode else false
	 */
	public boolean isSameTypeAsEntityForReadOnly();
	
	/**
	 * Return true if the type used to store the entity data is the same than the type used to store the data in the ui component in the writable mode else false
	 * @return Return true if the type used to store the entity data is the same than the type used to store the data in the ui component in the writable mode else false
	 */
	public boolean isSameTypeAsEntityForReadWrite();
	
	/**
	 * Define if the ui component has the same storage type for data as the entity in readonly mode
	 * @param p_bSameTypeAsEntityForReadOnly is the same type for data storage as entity when readonly mode
	 */
	public void setSameTypeAsEntityForReadOnly(	boolean p_bSameTypeAsEntityForReadOnly);
	
	/**
	 * Define if the ui component has the same storage type for data as the entity in read write mode
	 * @param p_bSameTypeAsEntityForReadWrite is the same type for data storage as entity when read write mode
	 */
	public void setSameTypeAsEntityForReadWrite(boolean p_bSameTypeAsEntityForReadWrite);
	
	/**
	 * Get the language type options property list for read write mode
	 */
	
	public Map<String, String> getRWLanguageTypeOptions();

	/**
	 * Set the language type options properties
	 * @param languageTypeOptions : a map of language type options property list for read write mode
	 */
	
	public void setRWLanguageTypeOptions(Map<String, String> languageTypeOptions);
	
	/**
	 * Add a language type option to the language type options property list for read write mode
	 * @param propertyName : the language type option property name
	 * @param propertyValue : the language type option property value
	 */
	
	public void addRWLanguageTypeOption(String propertyName, String propertyValue);
	
	/**
	 * Get the language type options property list for read only mode
	 */
	
	public Map<String, String> getROLanguageTypeOptions();

	/**
	 * Set the language type options properties
	 * @param languageTypeOptions : a map of language type options property list for read only mode
	 */
	
	public void setROLanguageTypeOptions(Map<String, String> languageTypeOptions);
	
	/**
	 * Add a language type option to the language type options property list for read only mode
	 * @param propertyName : the language type option property name
	 * @param propertyValue : the language type option property value
	 */
	
	public void addROLanguageTypeOption(String propertyName, String propertyValue);
	
	
}
