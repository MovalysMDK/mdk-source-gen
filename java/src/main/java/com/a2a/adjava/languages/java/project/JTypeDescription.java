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
package com.a2a.adjava.languages.java.project;

import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.types.TypeDescription;

/**
 * <p>
 * TypeDescription for java generation
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

public class JTypeDescription extends TypeDescription {

	/**
	 * Binding jdbc (PreparedStatement)
	 */
	private String jdbcBind;
	
	/**
	 * ResultSet Reading
	 */
	private String jdbcRetrieve;
	
	/**
	 * Jdbc Type
	 */
	private String jdbcType;
	
	/**
	 * Constructor
	 */
	public JTypeDescription() {
	}

	/**
	 * @return
	 */
	public String getJdbcBind() {
		return this.jdbcBind;
	}

	/**
	 * @return
	 */
	public String getJdbcRetrieve() {
		String r_sJdbcRetrieve = this.jdbcRetrieve ;
		if ( isEnumeration()) {
			r_sJdbcRetrieve = this.jdbcRetrieve.replaceAll("ENUMERATION", getShortName());
		}
		return r_sJdbcRetrieve;
	}

	/**
	 * @return
	 */
	public String getJdbcType() {
		return this.jdbcType;
	}

	/**
	 * @param p_sJdbcRetrieve
	 */
	public void setJdbcRetrieve(String p_sJdbcRetrieve) {
		this.jdbcRetrieve = p_sJdbcRetrieve;
	}

	/**
	 * Affecte l'objet jdbcBind
	 * 
	 * @param p_sJdbcBind
	 *            Objet jdbcBind
	 */
	public void setJdbcBind(String p_sJdbcBind) {
		this.jdbcBind = p_sJdbcBind;
	}

	/**
	 * Affecte l'objet jdbcType
	 * 
	 * @param p_sJdbcType
	 *            Objet jdbcType
	 */
	public void setJdbcType(String p_sJdbcType) {
		this.jdbcType = p_sJdbcType;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.a2a.adjava.project.TypeDescription#clone()
	 */
	@Override
	public Object clone() {
		JTypeDescription r_oTypeDescription = new JTypeDescription();
		copyPropertiesTo(r_oTypeDescription);
		return r_oTypeDescription;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.project.TypeDescription#copyPropertiesTo(com.a2a.adjava.project.ITypeDescription)
	 */
	@Override
	protected void copyPropertiesTo(ITypeDescription p_oTypeDescription) {
		super.copyPropertiesTo(p_oTypeDescription);
		JTypeDescription oTypeDescription = (JTypeDescription) p_oTypeDescription ;
		oTypeDescription.setJdbcRetrieve(this.jdbcRetrieve);
		oTypeDescription.setJdbcBind(this.jdbcBind);
		oTypeDescription.setJdbcType(this.jdbcType);			
	}
}
