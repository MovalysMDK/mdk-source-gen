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

/**
 * @author lmichenaud
 *
 */
public class Schema {

	private List<Table> tables;
	
//	private Map<String,Table> tableByClassName ;
	
	/**
	 * 
	 */
	protected Schema() {
		this.tables = new ArrayList<Table>();
		//this.tableByClassName = new HashMap<String,Table>();
	}

	/**
	 * @param p_table
	 */
	public void addTable(Table p_oTable) {
		this.tables.add(p_oTable);
//		this.tableByClassName.put(p_oTable.getClasse().getFullName(), p_oTable);
	}

//	/**
//	 * @param p_sClassName
//	 * @return
//	 */
//	public Table getTableOfClass( String p_sClassName ) {
//		return this.tableByClassName.get(p_sClassName);
//	}

	/**
	 * @return
	 */
	public List<Table> getTables() {
		return this.tables;
	}
	
	/**
	 * @return
	 */
	public int countTables() {
		return this.tables.size();
	}

	/**
	 * @return
	 */
	public Element toXml() {
		Element r_xElement = DocumentHelper.createElement("schema");
		for( Table oTable: this.tables ) {
			r_xElement.add(oTable.toXml());
		}
		return r_xElement;
	}
}
