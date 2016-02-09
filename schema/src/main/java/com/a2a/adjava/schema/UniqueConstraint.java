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

public class UniqueConstraint extends AbstractDBObject {

	private String name;
	private List<Field> fields;

	/**
	 * @param p_sName
	 * @param p_listFields
	 */
	protected UniqueConstraint(String p_sName, List<Field> p_listFields) {
		this.name = p_sName ;
		this.fields = p_listFields;
	}
	
	/**
	 * @param p_sName
	 * @param p_listFields
	 */
	protected UniqueConstraint(String p_sName, Field p_oField ) {
		this.name = p_sName ;
		this.fields = new ArrayList<Field>();
		this.fields.add( p_oField);
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
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
		Element r_xUniqueConstraint = DocumentHelper.createElement("unique-constraint");
		r_xUniqueConstraint.addAttribute("name", this.name );
		for( Field oField : this.fields ) {
			r_xUniqueConstraint.addElement("field").setText( oField.getName());
		}
		return r_xUniqueConstraint ;
	}
}