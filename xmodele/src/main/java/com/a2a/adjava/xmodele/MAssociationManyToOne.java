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

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import com.a2a.adjava.schema.Field;
import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.uml.UmlAssociationEnd.AggregateType;

/**
 * Association many to one
 * @author lmichenaud
 *
 */
public class MAssociationManyToOne extends MAssociation implements MAssociationWithForeignKey {

	/**
	 * True if mandatory
	 */
	private boolean notNull ;
	
	/**
	 * Name of unique key (if many-to-one belongs to a unique constraint) 
	 */
	private String uniqueKey ;
	
	/**
	 * 
	 */
	private List<Field> fields ;

	/**
	 * @param p_sName
	 * @param p_oRefClass
	 * @param p_sParameterName
	 */
	public MAssociationManyToOne(String p_sName, MEntityImpl p_oRefClass, MEntityImpl p_oOppositeClass, 
			String p_sVariableName, String p_sVariableListName,
			String p_sParameterName, ITypeDescription p_oTypeDescription, String p_sVisibility, 
			boolean p_bUnique, boolean p_bNotNull, String p_sMappedBy,
			AggregateType p_oAggregateType, AggregateType p_oOppositeAggregateType, boolean p_bOppositeNavigable ) {
		super(p_sName, AssociationType.MANY_TO_ONE, p_oAggregateType, p_oOppositeAggregateType, 
				p_oRefClass, p_oOppositeClass, p_sVariableName, p_sVariableListName, p_sParameterName, 
				p_oTypeDescription, p_sVisibility, true, p_sMappedBy, p_bOppositeNavigable );
		this.notNull = p_bNotNull ;
		this.fields = new ArrayList<Field>();
	}
			
	/**
	 * @return
	 */
	public boolean isNotNull() {
		return this.notNull;
	}
	
	/**
	 * @param p_oField
	 */
	public void addField( Field p_oField ) {
		this.fields.add( p_oField );
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.MAssociationWithForeignKey#getUniqueKey()
	 */
	public String getUniqueKey() {
		return uniqueKey;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setUniqueKey(String p_sUniqueKey) {
		this.uniqueKey = p_sUniqueKey;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.MAssociation#toXml()
	 */
	@Override
	public Element toXml() {
		Element r_xAssoc = super.toXml();
		r_xAssoc.addAttribute("optional", Boolean.toString(!this.notNull ));
		if ( this.uniqueKey != null ) {
			r_xAssoc.addAttribute("unique-key", this.uniqueKey);
			r_xAssoc.addAttribute("unique-key-uppercase", this.uniqueKey.toUpperCase(Locale.getDefault()));
		}
		
		for( MIdentifierElem oMIdentifierElem : this.getRefClass().getIdentifier().getElems()) {
			r_xAssoc.add( oMIdentifierElem.toXml());
		}
		
		for( Field oField : this.fields ) {
			r_xAssoc.add( oField.toXml());
		}
		return r_xAssoc ;
	}
}
