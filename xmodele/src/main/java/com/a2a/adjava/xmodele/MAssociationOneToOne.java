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
package com.a2a.adjava.xmodele;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.dom4j.Element;

import com.a2a.adjava.schema.Field;
import com.a2a.adjava.types.ExpandableType;
import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.uml.UmlAssociationEnd.AggregateType;

/**
 * @author lmichenaud
 *
 */
public class MAssociationOneToOne extends MAssociation implements MAssociationWithForeignKey {

	/**
	 * Mandatory
	 */
	private boolean notNull ;
	
	/**
	 * Unique key if part of unique key 
	 */
	private String uniqueKey ;
	
	/**
	 * Fields of foreign key 
	 */
	private List<Field> fields ;
		
	/**
	 * Expandable type if the association has been created from it. 
	 */
	private ITypeDescription expandableTypeDesc ;
	
	/**
	 * @param p_sName
	 * @param p_sHbmType
	 * @param p_oRefClass
	 * @param p_sParameterName
	 * @param p_sHbmPropertyRef
	 * @param p_bOppositeNavigable opposite navigable
	 */
	public MAssociationOneToOne(String p_sName, MEntityImpl p_oRefClass, MEntityImpl p_oOppositeClass, 
			String p_sVariableName, String p_sVariableListName,
			String p_sParameterName, ITypeDescription p_oTypeDescription, String p_sVisibility,
			boolean p_bRelationOwner, boolean p_bIsNotNull, String p_sOppositeName, 
			AggregateType p_oAggregateType, AggregateType p_oOppositeAggregateType, boolean p_bOppositeNavigable ) {
		super(p_sName, AssociationType.ONE_TO_ONE, p_oAggregateType, p_oOppositeAggregateType, p_oRefClass, p_oOppositeClass,
			p_sVariableName, p_sVariableListName, p_sParameterName, p_oTypeDescription, 
			p_sVisibility, p_bRelationOwner, p_sOppositeName, p_bOppositeNavigable );
		this.notNull = p_bIsNotNull ;
		this.fields = new ArrayList<Field>();
	}

	/**
	 * @return
	 */
	public boolean isNotNull() {
		return notNull;
	}


	/**
	 * @param p_oField
	 */
	public void addField( Field p_oField ) {
		this.fields.add( p_oField );
	}
	
	/**
	 * @see com.a2a.adjava.xmodele.MAssociationWithForeignKey#getUniqueKey()
	 */
	public String getUniqueKey() {
		return uniqueKey;
	}
	
	/**
	 * Affecte l'objet uniqueKey 
	 * @param p_sUniqueKey uniqueKey
	 */
	public void setUniqueKey(String p_sUniqueKey) {
		this.uniqueKey = p_sUniqueKey;
	}

	/**
	 * Type description of expandable
	 * @return type description of expandable
	 */
	public ITypeDescription getExpandableTypeDesc() {
		return this.expandableTypeDesc;
	}

	/**
	 * Set expandable type description
	 * @param p_oExpandableTypeDesc type description
	 */
	public void setExpandableTypeDesc(ITypeDescription p_oExpandableTypeDesc) {
		this.expandableTypeDesc = p_oExpandableTypeDesc;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.MAssociation#toXml()
	 */
	public Element toXml() {
		Element r_xAssoc = super.toXml();
		r_xAssoc.addAttribute("optional", Boolean.toString( !this.notNull ));
		if ( this.uniqueKey != null ) {
			r_xAssoc.addAttribute("unique-key", this.uniqueKey);
			r_xAssoc.addAttribute("unique-key-uppercase", this.uniqueKey.toUpperCase(Locale.getDefault()));
		}
		
		if ( this.isRelationOwner() || this.getRefClass().isTransient()) {
			for( MIdentifierElem oMIdentifierElem : this.getRefClass().getIdentifier().getElems()) {
				r_xAssoc.add( oMIdentifierElem.toXml());
			}
		}
		
		// Si ce n'est pas le propriétaire de la relation, la relation est forcément obligatoire dans l'autre sens
		if ( !this.isRelationOwner()) {
			r_xAssoc.addElement("opposite-get-accessor").setText(
					"get" + this.getOppositeName().substring(0, 1).toUpperCase()
							+ this.getOppositeName().substring(1));
			r_xAssoc.addElement("opposite-set-accessor").setText(
					"set" + this.getOppositeName().substring(0, 1).toUpperCase()
							+ this.getOppositeName().substring(1));
		}
		
		for( Field oField : this.fields ) {
			r_xAssoc.add( oField.toXml());
		}
		return r_xAssoc ;
	}
}
