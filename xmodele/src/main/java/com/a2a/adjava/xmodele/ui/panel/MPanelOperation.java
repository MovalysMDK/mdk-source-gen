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
package com.a2a.adjava.xmodele.ui.panel;

import java.util.ArrayList;
import java.util.List;

import com.a2a.adjava.xmodele.MStereotype;

/**
 * @author lmichenaud
 *
 */
public class MPanelOperation {

	/**
	 * Operation name
	 */
	private String name ;

	/**
	 * Operation stereotypes
	 */
	private List<MStereotype> stereotypes = new ArrayList<MStereotype>();
	
	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param p_sName
	 */
	public void setName(String p_sName) {
		this.name = p_sName;
	}
	
	/**
	 * @param p_sName
	 */
	public MPanelOperation(String p_sName) {
		super();
		this.name = p_sName;
	}

	/**
	 * Permet de récupérer la liste des Stereotypes de la classe
	 */
	public List<MStereotype> getStereotypes() {
		return this.stereotypes;
	}
	
	/**
	 * @param p_oStereotype
	 */
	public void addStereotype( MStereotype p_oStereotype ) {
		this.stereotypes.add( p_oStereotype );
	}
		 
	/**
	 * @param p_sStereotype
	 * @return
	 */
	public boolean hasStereotype( String p_sStereotype ) {
		boolean r_bHasStereotype = false ;
		for( MStereotype oStereotype: this.stereotypes) {
			if ( oStereotype.getName().equals(p_sStereotype)) {
				r_bHasStereotype = true ;
				break;
			}
		}
		return r_bHasStereotype ;
	}
}
