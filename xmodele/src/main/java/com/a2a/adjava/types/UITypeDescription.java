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
public class UITypeDescription implements IUITypeDescription {

	/**
	 * Uml name
	 */
	private String umlName;
	
	/**
	 * Matching type for view model when ui component is rw
	 */
	private String rwVmt;
	
	/**
	 * Component type for rw mode
	 */
	private String rwCt;
	
	/**
	 *  Matching type for view model when ui component is read-only
	 */
	private String roVmt;
	
	/**
	 * Component type for readonly mode
	 */
	private String roCt;
	
	/**
	 * Is the same type as entity for read only mode
	 */
	private boolean sameTypeAsEntityForReadOnly = false ;

	/**
	 * Is the same type as entity for read write mode
	 */
	private boolean sameTypeAsEntityForReadWrite = false ;
	
	/**
	 * Language type options for read only mode
	 */
	private Map<String, String> languageTypeOptionsForReadOnly;
	
	/**
	 * Language type options for read write mode
	 */
	private Map<String, String> languageTypeOptionsForReadWrite;
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.IUITypeDescription#getUmlName()
	 */
	@Override
	public String getUmlName() {
		return this.umlName;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.IUITypeDescription#setUmlName(java.lang.String)
	 */
	@Override
	public void setUmlName(String p_sUmlName) {
		this.umlName = p_sUmlName;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.IUITypeDescription#getRWViewModelType()
	 */
	@Override
	public String getRWViewModelType() {
		return this.rwVmt;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.IUITypeDescription#setRWViewModelType(java.lang.String)
	 */
	@Override
	public void setRWViewModelType(String p_sVmt) {
		this.rwVmt = p_sVmt;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.IUITypeDescription#getRwComponentType()
	 */
	@Override
	public String getRwComponentType() {
		return this.rwCt;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.IUITypeDescription#setRWComponentType(java.lang.String)
	 */
	@Override
	public void setRWComponentType(String p_sComponentType) {
		this.rwCt = p_sComponentType;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.IUITypeDescription#getROViewModelType()
	 */
	@Override
	public String getROViewModelType() {
		if (this.roVmt!=null) {
			return this.roVmt;
		}
		else {
			return this.rwVmt;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.IUITypeDescription#isSameTypeAsEntityForReadOnly()
	 */
	@Override
	public boolean isSameTypeAsEntityForReadOnly() {
		return this.sameTypeAsEntityForReadOnly;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.IUITypeDescription#isSameTypeAsEntityForReadWrite()
	 */
	@Override
	public boolean isSameTypeAsEntityForReadWrite() {
		return this.sameTypeAsEntityForReadWrite;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.IUITypeDescription#setSameTypeAsEntityForReadOnly()
	 */
	@Override
	public void setSameTypeAsEntityForReadOnly(	boolean p_bSameTypeAsEntityForReadOnly) {
		this.sameTypeAsEntityForReadOnly = p_bSameTypeAsEntityForReadOnly;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.IUITypeDescription#setSameTypeAsEntityForReadWrite()
	 */
	@Override
	public void setSameTypeAsEntityForReadWrite(boolean p_bSameTypeAsEntityForReadWrite) {
		this.sameTypeAsEntityForReadWrite = p_bSameTypeAsEntityForReadWrite;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.IUITypeDescription#setROViewModelType(java.lang.String)
	 */
	@Override
	public void setROViewModelType(String p_sVmt) {
		this.roVmt = p_sVmt;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.IUITypeDescription#getROComponentType()
	 */
	@Override
	public String getROComponentType() {
		if (this.roCt!=null) {
			return this.roCt;
		}
		else {
			return this.rwCt;
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.IUITypeDescription#setROComponentType(java.lang.String)
	 */
	@Override
	public void setROComponentType(String p_sComponentType) {
		this.roCt = p_sComponentType;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.IUITypeDescription#getComponentType(com.a2a.adjava.xmodele.ui.view.MVFModifier)
	 */
	@Override
	public String getComponentType(MVFModifier p_oMVFModifier) {
		return MVFModifier.READONLY.equals(p_oMVFModifier) ? this.getROComponentType() : this.getRwComponentType();
	}
	
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		UITypeDescription r_oUITypeDescription = new UITypeDescription();
		this.copyPropertiesTo(r_oUITypeDescription);
		return r_oUITypeDescription;
	}

	/**
	 * Copy current values to a target ui type description
	 * @param p_oUITypeDescription ui type description target
	 */
	protected void copyPropertiesTo(UITypeDescription p_oUITypeDescription) {
		UITypeDescription oUITypeDescription = (UITypeDescription) p_oUITypeDescription;
		oUITypeDescription.setROComponentType(this.roCt.trim());
		oUITypeDescription.setROViewModelType(this.roVmt.trim());
		oUITypeDescription.setRWComponentType(this.rwCt.trim());
		oUITypeDescription.setRWViewModelType(this.rwVmt.trim());
		oUITypeDescription.setUmlName(this.umlName.trim());
		oUITypeDescription.setRWLanguageTypeOptions(this.languageTypeOptionsForReadWrite);
	}


	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.IUITypeDescription#getRWLanguageTypeOptions()
	 */
	
	public Map<String, String> getRWLanguageTypeOptions() {
		return this.languageTypeOptionsForReadWrite;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.IUITypeDescription#setRWLanguageTypeOptions(java.util.Map)
	 */
	
	public void setRWLanguageTypeOptions(Map<String, String> languageTypeOptions) {
		this.languageTypeOptionsForReadWrite = languageTypeOptions;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.IUITypeDescription#addRWLanguageTypeOption(java.lang.String, java.lang.String)
	 */
	
	public void addRWLanguageTypeOption(String propertyName, String propertyValue) {
		this.languageTypeOptionsForReadWrite.put(propertyName, propertyValue.trim());
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.IUITypeDescription#getROLanguageTypeOptions()
	 */
	
	public Map<String, String> getROLanguageTypeOptions() {
		return this.languageTypeOptionsForReadOnly;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.IUITypeDescription#setROLanguageTypeOptions(java.util.Map)
	 */
	
	public void setROLanguageTypeOptions(Map<String, String> languageTypeOptions) {
		this.languageTypeOptionsForReadOnly = languageTypeOptions;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.IUITypeDescription#addROLanguageTypeOption(java.lang.String, java.lang.String)
	 */
	
	public void addROLanguageTypeOption(String propertyName, String propertyValue) {
		this.languageTypeOptionsForReadOnly.put(propertyName, propertyValue);
	}
	
}
