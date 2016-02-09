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
package com.a2a.adjava.uml2xmodele.ui.screens;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.a2a.adjava.uml.UmlAssociationEnd;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlStereotype;

/**
 * @author lmichenaud
 *
 */
public class PanelAggregation {

	/**
	 * Screen uml class
	 */
	private UmlClass screen ;
	
	/**
	 * Panel uml class
	 */
	private UmlClass panel ;

	/**
	 * Aggregation name
	 */
	private String name ;
	
	/**
	 * 
	 */
	private String options ;
	
	/**
	 * List of the stereotype 
	 */
	private List<UmlStereotype> stereotypes ;
	/**
	 * Default constructor
	 * @param p_oAssociationEnd
	 */
	public PanelAggregation( UmlAssociationEnd p_oAssociationEnd ) {
		this.screen = p_oAssociationEnd.getOppositeAssociationEnd().getRefClass();
		this.panel = p_oAssociationEnd.getRefClass();
		this.name = p_oAssociationEnd.getAssociation().getName();
		this.stereotypes = new ArrayList<UmlStereotype>() ;
		this.addStereotypes(p_oAssociationEnd.getAssociation().getStereotypes());
		this.options = p_oAssociationEnd.getAssociation().getOptions();
	}

	/**
	 * @return
	 */
	public UmlClass getScreen() {
		return screen;
	}

	/**
	 * @return
	 */
	public UmlClass getPanel() {
		return panel;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public String getOptions() {
		return options;
	}
	
	/**
	 * Add a list of stereotypes
	 * @param p_oStereotype
	 */
	public void addStereotypes( Collection<UmlStereotype> p_listStereotypes ) {
		for( UmlStereotype oStereotype : p_listStereotypes ) {
			this.stereotypes.add( oStereotype );
		}
	}
	/**
	 * <p>Return true if the stereotype list contains the object that is linked to the specified name as parameter.</p>
	 * @param p_sName the name of the stereotype
	 * @return true if the stereotype is present, false otherwise
	 */
	public boolean hasStereotype(String p_sName){		
		for(UmlStereotype oStereotype : this.stereotypes ){
			if ( oStereotype !=null && oStereotype.getName().equalsIgnoreCase(p_sName)){
				return true ;
			}
		}
		return false ;		
	}
}
