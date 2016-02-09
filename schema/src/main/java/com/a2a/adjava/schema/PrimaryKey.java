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

import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class PrimaryKey extends AbstractDBObject {

	private List<Field> fields; // liste des champs composant la clé

	/**
	 * @param p_sName
	 * @param p_listFields
	 */
	protected PrimaryKey(String p_sName) {
		this.setName(p_sName);
		this.fields = new ArrayList<Field>();
	}

	/**
	 * @param p_oField
	 */
	public void addField(Field p_oField) {
		this.fields.add(p_oField);
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
		Element r_xPrimaryKey = DocumentHelper.createElement("primary-key");
		r_xPrimaryKey.addAttribute("name", this.getName());
		for( Field oField : this.fields ) {
			r_xPrimaryKey.addElement("field").setText( oField.getName());
		}
		return r_xPrimaryKey;
	}
}