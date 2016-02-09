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

public class MMethodCriteriaParameter extends MMethodParameter {

	private MAssociationWithForeignKey assocWithFk ;
	private MAssociationManyToMany assocManyToMany ;
	private MAttribute attribute ;
	private boolean byValue ;
	
	/**
	 * @param p_sName
	 * @param p_oTypeDesc
	 */
	public MMethodCriteriaParameter(String p_sName, ITypeDescription p_oTypeDesc, MAttribute p_oAttribute, boolean p_bByValue ) {
		super(p_sName, p_oTypeDesc);
		this.attribute = p_oAttribute ;
		this.byValue = p_bByValue ;
	}
	
	/**
	 * @param p_sName
	 * @param p_oTypeDesc
	 */
	public MMethodCriteriaParameter(String p_sName, ITypeDescription p_oTypeDesc, MAssociationWithForeignKey p_oMAssociationWithForeignKey, boolean p_bByValue ) {
		super(p_sName, p_oTypeDesc);
		this.assocWithFk = p_oMAssociationWithForeignKey ;
		this.byValue = p_bByValue ;
	}
	
	/**
	 * @param p_sName
	 * @param p_oTypeDesc
	 */
	public MMethodCriteriaParameter(String p_sName, ITypeDescription p_oTypeDesc, MAssociationManyToMany p_oMAssociationManyToMany, boolean p_bByValue ) {
		super(p_sName, p_oTypeDesc);
		this.assocManyToMany = p_oMAssociationManyToMany ;
		this.byValue = p_bByValue ;
	}

	/* (non-Javadoc)
	 * @see com.a2a.adjava.xmodele.MMethodParameter#toXml()
	 */
	public Element toXml() {
		Element r_xMethodCriteria = super.toXml();
		r_xMethodCriteria.addAttribute("by-value", Boolean.toString(this.byValue));
		if ( this.assocWithFk != null ) {
			r_xMethodCriteria.add(assocWithFk.toXml());
		}
		if ( this.attribute != null ) {
			r_xMethodCriteria.add(this.attribute.toXml());
		}
		if ( this.assocManyToMany != null ) {
			Element xAsso = this.assocManyToMany.toXml();
			
			if ( !this.byValue) {
				// Passage du critère par object, on a besoin de connaitre les attributs de l'objet passé en paramètre
				for( MIdentifierElem oIdentifierElem : this.assocManyToMany.getRefClass().getIdentifier().getElems()) {
					xAsso.add( oIdentifierElem.toXml());
				}
			}
			r_xMethodCriteria.add(xAsso);
		}
		return r_xMethodCriteria ;
	}
}
