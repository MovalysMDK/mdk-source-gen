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
package com.a2a.adjava.languages.w8.project;

import org.dom4j.Element;

import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.types.TypeDescription;
import com.a2a.adjava.types.TypeDescriptionFactory;

public class W8TypeDescriptionFactory extends TypeDescriptionFactory {

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.project.TypeDescriptionFactory#createTypeDescription(org.dom4j.Element)
	 */
	@Override
	public TypeDescription createTypeDescription(Element p_xType) throws Exception {
		W8TypeDescription r_oTypeDescription = new W8TypeDescription();
		fillTypeDescription(p_xType, r_oTypeDescription);
		return r_oTypeDescription;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.project.TypeDescriptionFactory#fillTypeDescription(org.dom4j.Element, com.a2a.adjava.project.TypeDescription)
	 */
	@Override
	protected void fillTypeDescription(Element p_xType, ITypeDescription p_oTypeDescription) throws Exception {
		super.fillTypeDescription(p_xType, p_oTypeDescription);
		W8TypeDescription oTypeDescription = (W8TypeDescription) p_oTypeDescription;
		//TODO: Customize for html 5
	}
}
