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
package com.a2a.adjava.uml2xmodele.uml;

import com.a2a.adjava.types.ExpandableType;
import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.uml.UmlAssociationEnd;

/**
 * Uml association end created from an expandable
 * @author lmichenaud
 *
 */
public class UmlExpandedAssociationEnd extends UmlAssociationEnd {

	/**
	 * Expdanble type
	 */
	private ExpandableType expandableType;
	
	/**
	 * Type description
	 */
	private ITypeDescription expandableTypeDesc ;
	
	/**
	 * Constructor
	 * @param p_sName association end name
	 * @param p_sVisibility visibility
	 * @param p_bOrdered ordered
	 * @param p_bNavigable navigable
	 * @param p_iMultiplicityLower multiplicity lower
	 * @param p_iMultiplicityUpper multiplicity upper
	 * @param p_oRefClass target class
	 * @param p_sOptions options
	 * @param p_oAggregateType aggregate type
	 */
	public UmlExpandedAssociationEnd(String p_sName, String p_sVisibility,
			boolean p_bOrdered, boolean p_bNavigable,
			Integer p_iMultiplicityLower, Integer p_iMultiplicityUpper,
			String p_sOptions, AggregateType p_oAggregateType) {
		super(p_sName, p_sVisibility, p_bOrdered, p_bNavigable, p_iMultiplicityLower,
				p_iMultiplicityUpper, p_sOptions, p_oAggregateType);
	}

	/**
	 * Get expandable type
	 * @return expandable type
	 */
	public ExpandableType getExpandableType() {
		return this.expandableType;
	}

	/**
	 * Set expandable type
	 * @param p_oExpandableType
	 */
	public void setExpandableType(ExpandableType p_oExpandableType) {
		this.expandableType = p_oExpandableType;
	}

	/**
	 * Get expandable type
	 * @return expandable type
	 */
	public ITypeDescription getExpandableTypeDesc() {
		return this.expandableTypeDesc;
	}

	/**
	 * Set expandable type
	 * @param p_oExpandableTypeDesc expandable type
	 */
	public void setExpandableTypeDesc(ITypeDescription p_oExpandableTypeDesc) {
		this.expandableTypeDesc = p_oExpandableTypeDesc;
	}
}
