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

import org.dom4j.Element;

import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.types.TypeDescription;
import com.a2a.adjava.types.TypeDescriptionFactory;

/**
 * <p>Factory for java type description</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */

public class JTypeDescriptionFactory extends TypeDescriptionFactory {

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.project.TypeDescriptionFactory#createTypeDescription(org.dom4j.Element)
	 */
	@Override
	public TypeDescription createTypeDescription(Element p_xType) throws Exception {
		JTypeDescription r_oJTypeDescription = new JTypeDescription();
		fillTypeDescription(p_xType, r_oJTypeDescription);
		return r_oJTypeDescription;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.project.TypeDescriptionFactory#fillTypeDescription(org.dom4j.Element, com.a2a.adjava.project.TypeDescription)
	 */
	@Override
	protected void fillTypeDescription(Element p_xType, ITypeDescription p_oTypeDescription) throws Exception {
		super.fillTypeDescription(p_xType, p_oTypeDescription);
		JTypeDescription oJTypeDescription = (JTypeDescription) p_oTypeDescription;
		oJTypeDescription.setJdbcRetrieve(p_xType.elementText("jdbc-retrieve"));
		oJTypeDescription.setJdbcBind(p_xType.elementText("jdbc-bind"));
		oJTypeDescription.setJdbcType(p_xType.elementText("jdbc-type"));
	}
}
