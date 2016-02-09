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
package com.a2a.adjava.languages.java.xmodele;

import org.dom4j.Element;

import com.a2a.adjava.languages.java.project.JTypeDescription;
import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.xmodele.MAttribute;

/**
 * <p>
 * </p>
 * 
 * <p>
 * Copyright (c) 2011
 * <p>
 * Company: Adeuza
 * 
 * @author lmichenaud
 * 
 */

public class MJAttribute extends MAttribute {

	/**
	 * Constructor
	 * 
	 * @param p_sName
	 * @param p_sVisibility
	 * @param p_bIdentifier
	 * @param p_bIdentifier
	 * @param p_oTypeDescription
	 * @param p_sDocumentation
	 * @param p_bDerived
	 */
	protected MJAttribute(String p_sName, String p_sVisibility, boolean p_bIdentifier, boolean p_bDerived, boolean p_bTranscient,
			ITypeDescription p_oTypeDescription, String p_sDocumentation) {
		super(p_sName, p_sVisibility, p_bIdentifier, p_bDerived, p_bTranscient, p_oTypeDescription, p_sDocumentation);
	}

	/**
	 * TODO Décrire le constructeur MJAttribute
	 * @param p_sName
	 * @param p_sVisibility
	 * @param p_bIdentifier
	 * @param p_bIdentifier
	 * @param p_bDerived
	 * @param p_oTypeDescription
	 * @param p_sInitialisation
	 * @param p_sDefaultValue
	 * @param p_bIsMandory
	 * @param p_iLength
	 * @param p_iPrecision
	 * @param p_iScale
	 * @param p_bHasSequence
	 * @param p_bHasSequence
	 * @param p_bUnique
	 * @param p_sUniqueKey
	 * @param p_sDocumentation
	 */
	protected MJAttribute(String p_sName, String p_sVisibility, boolean p_bIdentifier, boolean p_bDerived, boolean p_bTranscient, ITypeDescription p_oTypeDescription,
			String p_sInitialisation, String p_sDefaultValue, boolean p_bIsMandory, int p_iLength, int p_iPrecision, int p_iScale, boolean p_bHasSequence, boolean p_bUnique,
			String p_sUniqueKey, String p_sDocumentation) {
		super(p_sName, p_sVisibility, p_bIdentifier, p_bDerived, p_bTranscient, p_oTypeDescription, p_sInitialisation, p_sDefaultValue, p_bIsMandory, p_iLength, p_iPrecision, p_iScale,
				p_bHasSequence, p_bUnique, p_sUniqueKey, p_sDocumentation);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.a2a.adjava.xmodele.MAttribute#toXml()
	 */
	@Override
	public Element toXml() {
		Element r_xAttr = super.toXml();
		JTypeDescription oTypeDesc = (JTypeDescription) getTypeDesc();
		r_xAttr.addElement("jdbc-retrieve").setText(oTypeDesc.getJdbcRetrieve());
		r_xAttr.addElement("jdbc-bind").setText(oTypeDesc.getJdbcBind());
		if ( this.isBasic()) {
			r_xAttr.addElement("jdbc-type").setText(oTypeDesc.getJdbcType());
		}
		return r_xAttr;
	}
}
