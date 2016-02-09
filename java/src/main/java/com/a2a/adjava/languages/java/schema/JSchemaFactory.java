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

import com.a2a.adjava.languages.java.project.JTypeDescription;
import com.a2a.adjava.schema.Field;
import com.a2a.adjava.schema.SchemaFactory;
import com.a2a.adjava.xmodele.MAttribute;

/**
 * <p>
 * JSchemaFactory
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

public class JSchemaFactory extends SchemaFactory {

	/**
	 * Create a field
	 * 
	 * @param p_sFieldName
	 * @param p_sType
	 * @param p_bNotNull
	 * @param p_oAttribute
	 * @return
	 */
	@Override
	public Field createField(String p_sFieldName, String p_sType, boolean p_bNotNull, MAttribute p_oAttribute) {
		JTypeDescription oJTypeDescription = (JTypeDescription) p_oAttribute.getTypeDesc();
		return new JField(p_sFieldName, p_sType, p_bNotNull, p_oAttribute.getLength(), p_oAttribute.getPrecision(), p_oAttribute.getScale(),
				p_oAttribute.isUnique(), p_oAttribute.getUniqueKey(), p_oAttribute.getTypeDesc().getDataType(), oJTypeDescription.getJdbcType(),
				p_oAttribute.isInitialisationFromType() ? null : p_oAttribute.getInitialisation());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.a2a.adjava.schema.SchemaFactory#createField(java.lang.String, java.lang.String, boolean, boolean, java.lang.String,
	 *      com.a2a.adjava.schema.Field)
	 */
	@Override
	public Field createField(String p_sFieldName, String p_sType, boolean p_bNotNull, boolean p_bUniqueKey, String p_sUniqueKey, Field p_oPkField) {
		JField oJField = (JField) p_oPkField;
		return new JField(p_sFieldName, p_sType, p_bNotNull, p_oPkField.getLength(), p_oPkField.getPrecision(), p_oPkField.getScale(), p_bUniqueKey,
				p_sUniqueKey, p_oPkField.getDataType(), oJField.getJdbcType(), p_oPkField.getInitialValue());
	}
}
