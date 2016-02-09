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
package com.a2a.adjava.languages.java.schema;

import org.dom4j.Element;

import com.a2a.adjava.datatypes.DataType;
import com.a2a.adjava.schema.Field;

/**
 * <p>
 * Field pour la génération Java
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

public class JField extends Field {

	/**
	 * JdbcType
	 */
	private String jdbcType;	
	
	/**
	 * Field pour la génération Java
	 * 
	 * @param p_sName
	 * @param p_sType
	 * @param p_bNotNull
	 * @param p_iLength
	 * @param p_iPrecision
	 * @param p_iScale
	 * @param p_bUnique
	 * @param p_sUniqueKey
	 * @param p_oDataType
	 * @param p_sJdbcType
	 */
	public JField(String p_sName, String p_sType, boolean p_bNotNull, int p_iLength, int p_iPrecision, int p_iScale, boolean p_bUnique,
			String p_sUniqueKey, DataType p_oDataType, String p_sJdbcType, String p_sInitialValue) {
		super(p_sName, p_sType, p_bNotNull, p_iLength, p_iPrecision, p_iScale, p_bUnique, p_sUniqueKey, p_oDataType, p_sInitialValue);
		this.jdbcType = p_sJdbcType;
	}

	/**
	 * @return
	 */
	public String getJdbcType() {
		return jdbcType;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.a2a.adjava.schema.Field#toXml()
	 */
	@Override
	public Element toXml() {
		Element r_xElem = super.toXml();
		r_xElem.addAttribute("jdbc-type", this.jdbcType);
		return r_xElem;
	}
}
