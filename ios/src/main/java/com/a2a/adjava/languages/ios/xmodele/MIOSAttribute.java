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

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.languages.ios.project.IOSTypeDescription;
import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.xmodele.MAttribute;

/**
 * IOS Attribute
 * @author lmichenaud
 *
 */
public class MIOSAttribute extends MAttribute {

	/**
	 * Constructor
	 * @param p_sName attribute name
	 * @param p_sVisibility visibility
	 * @param p_bIdentifier is part of identifier
	 * @param p_bDerived is derived
	 * @param p_bTranscient transient
	 * @param p_oTypeDescription type description
	 * @param p_sDocumentation documentation
	 */
	protected MIOSAttribute(String p_sName, String p_sVisibility, boolean p_bIdentifier, boolean p_bDerived, boolean p_bTranscient,
			ITypeDescription p_oTypeDescription, String p_sDocumentation) {
		super(p_sName, p_sVisibility, p_bIdentifier, p_bDerived, p_bTranscient, p_oTypeDescription, p_sDocumentation);
	}

	/**
	 * Contructor
	 * @param p_sName attribute name
	 * @param p_sVisibility visibility
	 * @param p_bIdentifier is part of identifier
	 * @param p_bDerived is derived
	 * @param p_bTranscient transient
	 * @param p_oTypeDescription type description
	 * @param p_sInitialisation initialisation
	 * @param p_sDefaultValue default value
	 * @param p_bIsMandory mandatory
	 * @param p_iLength length
	 * @param p_iPrecision precision
	 * @param p_iScale scale
	 * @param p_bHasSequence sequence
	 * @param p_bUnique unique
	 * @param p_sUniqueKey name of unique key if part of a unique constraint
	 * @param p_sDocumentation documentation
	 */
	protected MIOSAttribute(String p_sName, String p_sVisibility, boolean p_bIdentifier, boolean p_bDerived, boolean p_bTranscient, ITypeDescription p_oTypeDescription,
			String p_sInitialisation, String p_sDefaultValue, boolean p_bIsMandory, int p_iLength, int p_iPrecision, int p_iScale, boolean p_bHasSequence, 
			boolean p_bUnique, String p_sUniqueKey,	String p_sDocumentation) {
		super(p_sName, p_sVisibility, p_bIdentifier, p_bDerived, p_bTranscient, p_oTypeDescription, p_sInitialisation, p_sDefaultValue, p_bIsMandory, 
				p_iLength, p_iPrecision, p_iScale, p_bHasSequence, p_bUnique, p_sUniqueKey, p_sDocumentation);
	}

	/**
	 * Constructeur
	 * @param p_sName name
	 * @param p_sVisibility visibility
	 * @param p_bIdentifier part of identifier
	 * @param p_bDerived derived
	 * @param p_bTransient transient
	 * @param p_oTypeDescription type description
	 * @param p_sDocumentation documentation
	 * @param p_bReadOnly readonly
	 */
	protected MIOSAttribute(String p_sName, String p_sVisibility,
			boolean p_bIdentifier, boolean p_bDerived, boolean p_bTransient,
			ITypeDescription p_oTypeDescription, String p_sDocumentation,
			boolean p_bReadOnly) {
		super(p_sName, p_sVisibility, p_bIdentifier, p_bDerived, p_bTransient, p_oTypeDescription, p_sDocumentation, p_bReadOnly);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.a2a.adjava.xmodele.MAttribute#toXml()
	 */
	@Override
	public Element toXml() {
		Element r_xAttr = super.toXml();
		IOSTypeDescription oTypeDesc = (IOSTypeDescription) getTypeDesc();
		
		// type for core data
		if ( oTypeDesc.getCoreDataType() != null ) {
			r_xAttr.addElement("coredata-type").setText(oTypeDesc.getCoreDataType());
		}
		r_xAttr.addElement("vm-binded").setText(Boolean.toString(oTypeDesc.getVmBinded()));
		
		// default value for core data
		if ( this.isPartOfIdentifier() && this.getTypeDesc().getUnsavedValue() != null) {
			r_xAttr.addElement("coredata-default-value").setText(StringUtils.remove(this.getTypeDesc().getUnsavedValue(), '@'));
		}
		else
		if ( this.getDefaultValue() != null && !this.getDefaultValue().equalsIgnoreCase("Nil")) {
			String sDefaultValue = null ;
			if ( oTypeDesc.getCoreDataType().equals("Boolean")) {
				if ( this.getDefaultValue().equalsIgnoreCase("true")) {
					sDefaultValue = "YES";
				}
				else {
					sDefaultValue = "NO";
				}
			}
			else if ( isEnum()) {
				int iIndex = 1;
				sDefaultValue = "0";
				for( String sEnumValue : this.getMEnumeration().getEnumValues()) {
					if ( sEnumValue.equalsIgnoreCase(this.getDefaultValue())) {
						sDefaultValue = Integer.toString(iIndex);
						break;
					}
					iIndex++ ;
				}
			}
			else {
				sDefaultValue = this.getDefaultValue();
			}
			r_xAttr.addElement("coredata-default-value").setText(sDefaultValue);
		}
		
		
		return r_xAttr;
	}
	
	/**
	 * Overrides enum default value from ViewModel, to be compatible with iOS.
	 * 
	 * @see com.a2a.adjava.xmodele.MAttribute#setInitialisation()
	 * 
	 * @param p_sInitialisation initialisation name
	 */
	@Override
	public void setInitialisation(String p_sInitialisation) {
		if (isEnum() && p_sInitialisation.contains(".")) {
			super.setInitialisation(getTypeDesc().getShortName().toUpperCase()+'_'+p_sInitialisation.split("\\.")[1]);
		} else {
			super.setInitialisation(p_sInitialisation);
		}
	}
}
