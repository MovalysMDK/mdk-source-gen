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
package com.a2a.adjava.uml;

import com.a2a.adjava.uml.UmlAssociationEnd.AggregateType;

/**
 * To ascii class (for log)
 * @author lmichenaud
 *
 */
public class UmlAscii {

	/**
	 * Singleton instance
	 */
	private static UmlAscii instance = new UmlAscii();

	/**
	 * Constructor
	 */
	private UmlAscii() {
		//Empty constructor
	}

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	public static UmlAscii getInstance() {
		return instance;
	}
	
	/**
	 * Return ascii representation of association
	 * @param p_oAssoEnd1 association end 1
	 * @param p_oAssoEnd2 association end 2
	 * @return ascii representation
	 */
	public String toAscii( UmlAssociationEnd p_oAssoEnd1, UmlAssociationEnd p_oAssoEnd2 ) {
		
		StringBuilder r_sb = new StringBuilder();
		
		// Class name
		r_sb.append('[');
		r_sb.append(p_oAssoEnd1.getRefClass().getName());
		r_sb.append("] ");
		
		// Multiplicity
		this.appendMultiplicity(p_oAssoEnd1, r_sb);
		r_sb.append(' ');
		
		// Asso end name
		r_sb.append(p_oAssoEnd1.getName());
		r_sb.append(' ');
		
		
		// Aggregation kind
		if ( p_oAssoEnd1.getAggregateType().equals(AggregateType.COMPOSITE)) {
			r_sb.append('@');
		}
		else if ( p_oAssoEnd1.getAggregateType().equals(AggregateType.AGGREGATE)) {
			r_sb.append('O');
		}
		
		// Navigability
		if ( p_oAssoEnd1.isNavigable()) {
			r_sb.append('<');
		}
		
		// Line
		r_sb.append("---------");
		
		// Navigability
		if ( p_oAssoEnd2.isNavigable()) {
			r_sb.append('>');
		}
		
		// Aggregation kind
		if ( p_oAssoEnd2.getAggregateType().equals(AggregateType.COMPOSITE)) {
			r_sb.append('O');
		}
		else if ( p_oAssoEnd2.getAggregateType().equals(AggregateType.AGGREGATE)) {
			r_sb.append('@');
		}
		
		// Asso end name
		r_sb.append(' ');
		r_sb.append(p_oAssoEnd2.getName());
		r_sb.append(' ');
		
		// Multiplicity
		this.appendMultiplicity(p_oAssoEnd2, r_sb);
		
		// Ref class
		r_sb.append(" [");
		r_sb.append(p_oAssoEnd2.getRefClass().getName());
		r_sb.append(']');
		
		return r_sb.toString();
	}
	
	/**
	 * Ascii representation of multiplicity of association end
	 * @param p_oUmlAssoEnd association end
	 * @param p_oSb StringBuilder
	 */
	public void appendMultiplicity( UmlAssociationEnd p_oUmlAssoEnd, StringBuilder p_oSb ) {
		this.appendMultiplicity(p_oUmlAssoEnd.getMultiplicityLower(), p_oUmlAssoEnd.getMultiplicityUpper(), p_oSb);
	}
	
	/**
	 * Ascii representation of multiplicity
	 * @param p_iLower lower
	 * @param p_iUpper upper
	 * @param p_oSb StringBuilder
	 */
	public void appendMultiplicity( int p_iLower, int p_iUpper, StringBuilder p_oSb ) {
		if ( p_iLower == 1 && p_iUpper == 1 ) {
			p_oSb.append("1");
		}
		else {
			p_oSb.append(p_iLower);
			p_oSb.append("..");
			if ( p_iUpper == -1 ) {
				p_oSb.append('n');
			}
			else {
				p_oSb.append(p_iUpper);
			}
		}
	}
}
