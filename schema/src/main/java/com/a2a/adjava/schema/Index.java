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
package com.a2a.adjava.schema;

import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Index extends AbstractDBObject {

	private boolean unique ;	
	private List<Field> fields ; // liste des champs composant l'index
	
	/**
	 * @param p_sName
	 * @param p_listFields
	 */
	protected Index(String p_sName, List<Field> p_listFields, boolean p_bUnique ) {
		this.setName(p_sName);
		this.unique = p_bUnique ;
		this.fields = p_listFields ;
	}

	/**
	 * @return
	 */
	public boolean isUnique() {
		return this.unique;
	}

	/**
	 * @return
	 */
	public List<Field> getFields() {
		return fields;
	}

	/**
	 * @return
	 */
	public Element toXml() {
		Element r_xIndex = DocumentHelper.createElement("index");
		r_xIndex.addAttribute("name", this.getName());
		r_xIndex.addAttribute("unique", Boolean.toString(this.unique));
		for( Field oField : this.fields ) {
			r_xIndex.addElement("field").setText(oField.getName());
		}
		return r_xIndex ;
	}
}