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

import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.uml.UmlAssociationEnd.AggregateType;

public class MAssociationManyToMany extends MAssociation {

	/**
	 * @param p_sName
	 * @param p_oAssociationType
	 * @param p_oAggregateType
	 * @param p_oOppositeAggregateType
	 * @param p_oRefClass
	 * @param p_oOppositeClass
	 * @param p_sVariableName
	 * @param p_sVariableListName
	 * @param p_sParameterName
	 * @param p_oTypeDescription
	 * @param p_sVisibility
	 * @param p_bRelationOwner
	 * @param p_sOppositeName
	 */
	public MAssociationManyToMany(String p_sName, AssociationType p_oAssociationType,
			AggregateType p_oAggregateType, AggregateType p_oOppositeAggregateType, MEntityImpl p_oRefClass,
			MEntityImpl p_oOppositeClass, String p_sVariableName, String p_sVariableListName,
			String p_sParameterName, ITypeDescription p_oTypeDescription, String p_sVisibility,
			boolean p_bRelationOwner, String p_sOppositeName, boolean p_bOppositeNavigable) {
		
		super(p_sName, p_oAssociationType, p_oAggregateType, p_oOppositeAggregateType, p_oRefClass,
				p_oOppositeClass, p_sVariableName, p_sVariableListName, p_sParameterName, p_oTypeDescription,
				p_sVisibility, p_bRelationOwner, p_sOppositeName, p_bOppositeNavigable);
	}

}
