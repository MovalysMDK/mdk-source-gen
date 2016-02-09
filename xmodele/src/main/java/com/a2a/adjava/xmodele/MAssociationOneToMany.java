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

import org.dom4j.Element;

import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.uml.UmlAssociationEnd.AggregateType;

public class MAssociationOneToMany extends MAssociation {

	private boolean notNull ;  // clé étrangere non null
	
	/**
	 * @param p_sName
	 * @param p_oRefClass
	 * @param p_sParameterName
	 * @param p_sFkColumnName
	 * @param p_sFkName
	 */
	public MAssociationOneToMany(String p_sName, MEntityImpl p_oRefClass, MEntityImpl p_oOppositeClass, 
			String p_sVariableName, String p_sVariableListName, String p_sParameterName,
			ITypeDescription p_oTypeDescription, String p_sVisibility,
			boolean p_bNotNull, boolean p_bRelationOwner, String p_sMappedBy, 
			AggregateType p_oAggregateType, AggregateType p_oOppositeAggregateType, boolean p_bOppositeNavigable ) {
		super(p_sName, AssociationType.ONE_TO_MANY, p_oAggregateType, p_oOppositeAggregateType, p_oRefClass, p_oOppositeClass, p_sVariableName, 
			p_sVariableListName, p_sParameterName, p_oTypeDescription, p_sVisibility, p_bRelationOwner, p_sMappedBy, p_bOppositeNavigable);
		this.notNull = p_bNotNull ;
	}	
	
	/* (non-Javadoc)
	 * @see com.a2a.adjava.xmodele.MAssociation#toXml()
	 */
	public Element toXml() {
		Element r_xAssoc = super.toXml();
		r_xAssoc.addAttribute("not-null", Boolean.toString(this.notNull));
		
		// la relation est forcément obligatoire dans l'autre sens
		r_xAssoc.addElement("opposite-get-accessor").setText(
				"get" + this.getOppositeName().substring(0, 1).toUpperCase()
						+ this.getOppositeName().substring(1));
		r_xAssoc.addElement("opposite-set-accessor").setText(
				"set" + this.getOppositeName().substring(0, 1).toUpperCase()
						+ this.getOppositeName().substring(1));
		
		for( MIdentifierElem oMIdentifierElem : this.getRefClass().getIdentifier().getElems()) {
			r_xAssoc.add( oMIdentifierElem.toXml());
		}
		return r_xAssoc ;
	}
}
