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

//import com.a2a.adjava.xmodele.MAssociationWithForeignKey;

/**
 * Classe Cl� Etrang�re
 */
public class ForeignKey extends AbstractDBObject {

	private List<Field> localFields; // liste des champs composant la cl� �trang�re
	private List<Field> referencedFields; // liste des champs composant la cl� �trang�re
	private Table tableRef; // table r�f�renc�e par la cl� �trang�re
	//private MAssociationWithForeignKey association; // Association du diagramme expliquant la FK
	private boolean deleteOnCascade; // suppression en cascade

	/**
	 * @param p_sName
	 * @param p_listFields
	 * @param p_oTableRef
	 * @param p_bDeleteOnCascade
	 * @param p_sTable
	 * @param p_oAssociation
	 */
	protected ForeignKey(String p_sName, List<Field> p_localFields, List<Field> p_listReferencedFields,
			Table p_oTableRef, boolean p_bDeleteOnCascade/*, MAssociationWithForeignKey p_oAssociation*/) {
		this.setName(p_sName);
		this.localFields = p_localFields;
		this.referencedFields = p_listReferencedFields;
		this.tableRef = p_oTableRef;
		//this.association = p_oAssociation;
		this.deleteOnCascade = p_bDeleteOnCascade;
	}
	
	/**
	 * @return
	 */
	public List<Field> getLocalFields() {
		return this.localFields;
	}

	/**
	 * @param p_listField
	 */
	public void setLocalFields(List<Field> p_listLocalField) {
		this.localFields = p_listLocalField;
	}

	/**
	 * @return
	 */
	public List<Field> getReferencedFields() {
		return this.referencedFields;
	}

	/**
	 * @param p_listField
	 */
	public void setReferencedFields(List<Field> p_listReferencedLocalFields) {
		this.referencedFields = p_listReferencedLocalFields;
	}	
	
	/**
	 * @return
	 */
	public Table getTableRef() {
		return tableRef;
	}

//	/**
//	 * @return
//	 */
//	public MAssociationWithForeignKey getAssociation() {
//		return association;
//	}

	/**
	 * @return
	 */
	public boolean isDeleteOnCascade() {
		return deleteOnCascade;
	}

	/**
	 * @return
	 */
	public Element toXml() {
		Element r_xForeignKeys = DocumentHelper.createElement("foreign-key");
		r_xForeignKeys.addAttribute("name", this.getName());
		r_xForeignKeys.addAttribute("delete-cascade", Boolean.toString(this.deleteOnCascade));		
		for( Field oField : this.localFields ) {
			r_xForeignKeys.addElement("field").setText( oField.getName());
		}
		Element xTableRef = r_xForeignKeys.addElement("table-ref");
		xTableRef.addAttribute("name", this.tableRef.getName());
		for( Field oField : this.referencedFields ) {
			xTableRef.addElement("field").setText( oField.getName());
		}
	
		return r_xForeignKeys ;
	}
}
