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
import java.util.Iterator;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Construit une table � partir d'une classe
 */
public class Table extends AbstractDBObject {

	private List<Field> fields; // champs de la table
	private PrimaryKey primaryKey; // La cl� primaire
	private List<ForeignKey> foreignKeys; // liste des cl�s �trang�res de la table
	private List<Index> indexes;
	private List<UniqueConstraint> uniqueConstraints;
	//private MClass classe;
	private boolean joinTable = false ;

	/**
	 * Constructeur
	 */
	protected Table( String p_sName, PrimaryKey p_oPrimaryKey, boolean p_bJoinTable ) {
		this.setName(p_sName);
		this.fields = new ArrayList<Field>();
		this.foreignKeys = new ArrayList<ForeignKey>();
		this.indexes = new ArrayList<Index>();
		this.uniqueConstraints = new ArrayList<UniqueConstraint>();
		//this.classe = p_oClass ;
		this.joinTable = p_bJoinTable ;
		this.primaryKey = p_oPrimaryKey;
	}

	/**
	 * Ajout un champs comme cl� primaire
	 */
	public void setPrimaryKey( PrimaryKey p_oPrimaryKey ) {
		this.primaryKey = p_oPrimaryKey ;
	}

	/**
	 * @return
	 */
	public boolean hasPrimaryKey() {
		return this.primaryKey != null && !this.primaryKey.getFields().isEmpty(); 
	}
	
	/**
	 * @param p_oField
	 */
	public void addField( Field p_oField ) {
		this.fields.add(p_oField);
	}
	
	/**
	 * Ajoute une cl� �trang�re
	 */
	public void addForeignKey(ForeignKey p_oForeignKey ) {
		this.foreignKeys.add(p_oForeignKey);
	}

	/**
	 * Ajoute un index � la table
	 */
	public void addIndex(Index p_oIndex) {
		this.indexes.add(p_oIndex);
	}
	
	/**
	 * @param p_oUniqueConstraint
	 */
	public void addUniqueConstraint( UniqueConstraint p_oUniqueConstraint) {
		this.uniqueConstraints.add(p_oUniqueConstraint);
	}
	
	/**
	 * @return
	 */
	public List<Field> getFields() {
		return this.fields ;
	}
	
	/**
	 * Retourne la liste des champs de la table n�cessaire � une insertion
	 */
	public List<Field> getFieldsForInsert() {
		
		List<Field> r_listFields = new ArrayList<Field>();

		for( Field oField : this.fields ) {
			if (!oField.hasSequence()) {
				r_listFields.add(oField);
			}
		}

		return r_listFields ;
	}

	/**
	 * Retourne la liste des champs de la table � un update ajout les champs
	 * h�rit�es si la base de donn�es g�re l'h�ritage
	 */
	public List<Field> getFieldsForUpdate() {
		
		List<Field> r_listFields = new ArrayList<Field>();

		for( Field oField : this.fields ) {
			if (!oField.hasSequence()) {
				r_listFields.add(oField);
			}
		}

		return r_listFields ;
	}

	/**
	 * Retourne la cl� �trang�re d�finit sur le champs donn� en param�re Si
	 * aucune, retourne null Si la base de donn�es g�re l'h�ritage, scanne aussi
	 * les tables parents
	 */
	public ForeignKey getForeignKeyByField(Field p_oField) {
		
		ForeignKey r_oFk = null ;
		boolean bTrouve = false;
		Iterator<ForeignKey> iterFk = this.foreignKeys.iterator();
		while (iterFk.hasNext() && !bTrouve) {
			ForeignKey oFk = (ForeignKey) iterFk.next();
			bTrouve = oFk.getLocalFields().contains(p_oField);
		}
		
		if ( !bTrouve ) {
			r_oFk = null ;
		}
		
		return r_oFk ;
	}

	/**
	 * Retourne la cl� �trang�re d�finit sur le champs donn� en param�re Si
	 * aucune, retourne null Si la base de donn�es g�re l'h�ritage, scanne aussi
	 * les tables parents
	 */
//	public ForeignKey getForeignKeyByAssociation(
//			MAssociationManyToOne p_oMAssociationManyToOne ) {
//
//		ForeignKey r_oFk = null ;
//		boolean bTrouve = false;
//		Iterator<ForeignKey> iterFk = this.foreignKeys.iterator();
//		while (iterFk.hasNext() && !bTrouve) {
//			ForeignKey oFk = (ForeignKey) iterFk.next();
//			bTrouve = oFk.getAssociation() == p_oMAssociationManyToOne;
//		}
//		
//		if ( !bTrouve ) {
//			r_oFk = null ;
//		}
//		
//		return r_oFk ;
//	}
	
	/**
	 * Retourne la liste des foreign keys de la table
	 * @return liste des foreign keys de la table
	 */
	public List<ForeignKey> getForeignKeys() {
		return this.foreignKeys ;
	}

//	/**
//	 * @return
//	 */
//	public MClass getClasse() {
//		return classe;
//	}

	/**
	 * @return
	 */
	public PrimaryKey getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * @return
	 */
	public List<Index> getIndexes() {
		return indexes;
	}

	public Element toXml() {
		Element r_xTable = DocumentHelper.createElement("table");
		r_xTable.addAttribute("name", this.getName());
		r_xTable.addAttribute("join-table", Boolean.toString(this.joinTable));
		Element xFields = r_xTable.addElement("fields");
		for( Field oField : this.fields ) {
			xFields.add( oField.toXml());
		}
		if ( hasPrimaryKey()) {
			r_xTable.add(this.primaryKey.toXml());
		}
		Element xForeignKeys = r_xTable.addElement("foreign-keys");
		for( ForeignKey oForeignKey : this.foreignKeys ) {
			xForeignKeys.add( oForeignKey.toXml());
		}
		Element xIndexes = r_xTable.addElement("indexes");
		for( Index oIndex : this.indexes ) {
			xIndexes.add( oIndex.toXml());
		}
		Element xUniqueConstraints = r_xTable.addElement("unique-constraints");
		for( UniqueConstraint oUniqueConstraint : this.uniqueConstraints ) {
			xUniqueConstraints.add( oUniqueConstraint.toXml());
		}
		return r_xTable;
	}
}
