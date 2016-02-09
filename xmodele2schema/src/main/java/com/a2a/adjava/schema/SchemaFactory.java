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

import com.a2a.adjava.xmodele.MAssociationWithForeignKey;
import com.a2a.adjava.xmodele.MAttribute;

/**
 * <p>
 * TODO Decrire la classe SchemaFactory
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

public class SchemaFactory {

	/**
	 * Create a field
	 * 
	 * @param p_sFieldName
	 * @param p_sType
	 * @param p_bNotNull
	 * @param p_oAttribute
	 * @return
	 */
	public Field createField(String p_sFieldName, String p_sType, boolean p_bNotNull, MAttribute p_oAttribute) {
		return new Field(p_sFieldName, p_sType, p_bNotNull, p_oAttribute.getLength(), p_oAttribute.getPrecision(), p_oAttribute.getScale(),
				p_oAttribute.isUnique(), p_oAttribute.getUniqueKey(), p_oAttribute.getTypeDesc().getDataType(),
				p_oAttribute.isInitialisationFromType() ? null : p_oAttribute.getInitialisation());
	}

	/**
	 * Create a field from another field
	 * 
	 * @param p_sFieldName
	 * @param p_sType
	 * @param p_bNotNull
	 * @param p_bUniqueKey
	 * @param p_sUniqueKey
	 * @param p_oPkField
	 * @return
	 */
	public Field createField(String p_sFieldName, String p_sType, boolean p_bNotNull, boolean p_bUniqueKey, String p_sUniqueKey, Field p_oPkField) {
		return new Field(p_sFieldName, p_sType, p_bNotNull, p_oPkField.getLength(), p_oPkField.getPrecision(), p_oPkField.getScale(), p_bUniqueKey,
				p_sUniqueKey, p_oPkField.getDataType(), p_oPkField.getInitialValue());
	}

	/**
	 * Create a foreign key
	 * 
	 * @param p_sName
	 * @param p_localFields
	 * @param p_listReferencedFields
	 * @param p_oTableRef
	 * @param p_bDeleteOnCascade
	 * @param p_oAssociation
	 * @return
	 */
	public ForeignKey createForeignKey(String p_sName, List<Field> p_localFields, List<Field> p_listReferencedFields, Table p_oTableRef,
			boolean p_bDeleteOnCascade, MAssociationWithForeignKey p_oAssociation) {
		return new ForeignKey(p_sName, p_localFields, p_listReferencedFields, p_oTableRef, p_bDeleteOnCascade);
	}

	/**
	 * Create index
	 * 
	 * @param p_sName
	 * @param p_listFields
	 * @param p_bUnique
	 * @return
	 */
	public Index createIndex(String p_sName, List<Field> p_listFields, boolean p_bUnique) {
		return new Index(p_sName, p_listFields, p_bUnique);
	}

	/**
	 * Create a primarykey object
	 * @param p_sName PK Name
	 * @return
	 */
	public PrimaryKey createPrimaryKey(String p_sName) {
		return new PrimaryKey(p_sName);
	}
	
	/**
	 * Create a schema
	 * @return
	 */
	public Schema createSchema() {
		return new Schema();
	}
	
	/**
	 * Create a sequence
	 * @param p_sName
	 * @param p_sMaxValue
	 * @return
	 */
	public Sequence createSequence( String p_sName, String p_sMaxValue ) {
		return new Sequence(p_sName, p_sMaxValue);
	}
	
	/**
	 * Create a table
	 * @param p_sName table name
	 * @param p_sPkName primary key name
	 * @param p_bJoinTable true if join table
	 * @return created table
	 */
	public Table createTable( String p_sName, String p_sPkName, boolean p_bJoinTable ) {
		return new Table(p_sName, this.createPrimaryKey(p_sPkName), p_bJoinTable );
	}
	
	/**
	 * Create a unique constraint
	 * @param p_sName unique constraint name
	 * @param p_listFields fields of unique constraint
	 * @return
	 */
	public UniqueConstraint createUniqueConstraint(String p_sName, List<Field> p_listFields) {
		return new UniqueConstraint(p_sName, p_listFields);
	}
	
	/**
	 * Create a unique constraint
	 * @param p_sName unique constraint name
	 * @param p_oField field of the unique constraint
	 * @return
	 */
	public UniqueConstraint createUniqueConstraint(String p_sName, Field p_oField) {
		return new UniqueConstraint(p_sName, p_oField);
	}
}
