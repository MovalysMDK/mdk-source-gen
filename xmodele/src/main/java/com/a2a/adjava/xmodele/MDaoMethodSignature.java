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

import org.dom4j.Element;

import com.a2a.adjava.types.ITypeDescription;

public class MDaoMethodSignature extends MMethodSignature {

	private List<String> returnedProperties ;
	private List<String> neededImports ;
	private boolean byValue ; 
	
	
	/**
	 * @param p_sName
	 * @param p_sVisibility
	 * @param p_sType
	 * @param p_oReturnedType
	 */
	public MDaoMethodSignature(String p_sName, String p_sVisibility, String p_sType,
			ITypeDescription p_oReturnedType, List<String> p_listReturnedProperties,
			List<String> p_listNeedImports, boolean p_bByValue ) {
		super(p_sName, p_sVisibility, p_sType, p_oReturnedType);
		this.returnedProperties = p_listReturnedProperties ;
		this.neededImports = p_listNeedImports ;
		this.byValue = p_bByValue ;
	}

	/**
	 * @return
	 */
	public List<String> getNeededImports() {
		return this.neededImports;
	}

	/**
	 * @return the byValue
	 */
	public boolean isByValue() {
		return byValue;
	}

	/* (non-Javadoc)
	 * @see com.a2a.adjava.xmodele.MMethodSignature#toXml()
	 */
	public Element toXml() {
		Element r_xMethod = super.toXml();
		r_xMethod.addAttribute("by-value", Boolean.toString(this.byValue));
		for( String sReturnedProperty : this.returnedProperties ) {
			r_xMethod.addElement("returned-property").setText(sReturnedProperty);
		}
		return r_xMethod ;
	}
}
