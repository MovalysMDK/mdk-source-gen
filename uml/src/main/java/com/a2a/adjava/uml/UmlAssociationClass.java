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
package com.a2a.adjava.uml;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>Uml Association class</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */
public class UmlAssociationClass extends UmlClass {
	
	/**
	 * Association name
	 */
	private String associationName ;
	
	/**
	 * Association end 1
	 */
	private UmlAssociationEnd associationEnd1 ;
	
	/**
	 * Association end 2 
	 */
	private UmlAssociationEnd associationEnd2 ;
	
	/**
	 * Options 
	 */
	private String options ;
	
	/**
	 * @param p_sAssociationName
	 */
	public void setAssociationName(String p_sAssociationName) {
		if (p_sAssociationName != null) {
			this.associationName = p_sAssociationName.trim();
		} else {
			this.associationName = null;
		}
	}

	/**
	 * @param p_oAssociationEnd1
	 */
	public void setAssociationEnd1(UmlAssociationEnd p_oAssociationEnd1 ) {
		this.associationEnd1 = p_oAssociationEnd1 ;
	}

	/**
	 * @param p_oAssociationEnd2
	 */
	public void setAssociationEnd2(UmlAssociationEnd p_oAssociationEnd2 ) {
		this.associationEnd2 = p_oAssociationEnd2 ;
	}

	/**
	 * @return
	 */
	public String getAssociationName() {
		return associationName;
	}

	/**
	 * @return
	 */
	public UmlAssociationEnd getAssociationEnd1() {
		return this.associationEnd1;
	}

	/**
	 * @return
	 */
	public UmlAssociationEnd getAssociationEnd2() {
		return this.associationEnd2;
	}

	/**
	 * @return
	 */
	public String getOptions() {
		return options;
	}

	/**
	 * @param p_sOptions
	 */
	public void setOptions(String p_sOptions) {
		this.options = p_sOptions;
	}
	
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return StringUtils.join("UmlAssociationClass[", this.getFullName(), "]");
	}
}
