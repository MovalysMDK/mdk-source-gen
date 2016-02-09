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
package com.a2a.adjava.uml2xmodele.extractors.validators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.uml.UmlStereotype;
import com.a2a.adjava.uml.UmlStereotypedObject;

/**
 * <p>Class used to verify the compatibility between the stereotype of a stereotype object</p>
 * <p>Copyright (c) 2012</p>
 * <p>Company: Adeuza</p>
 * @author spacreau
 */

public class StereotypesCompatibilityValidator {

	/**
	 * 
	 */
	private Map<String,HashSet<UmlStereotype>> stereotypesIncompatibilities ;
	
	/**
	 * Construct an empty list of incompatibilities
	 */
	public StereotypesCompatibilityValidator() {
		stereotypesIncompatibilities = new HashMap<String,HashSet<UmlStereotype>>() ;  
	}
	
	/**
	 * Add an incompatibility between the stereotype in first parameter and the all stereotype of the list
	 * @param p_sStereotypeName name of the incompatible stereotype 
	 * @param p_listStereotypeName name's list of the  of the incompatible stereotype 
	 */
	public void addIncompatibility(String p_sStereotypeName , String... p_listStereotypeName  ){
		List<String> listIncompatibilties = new ArrayList<String>();
		if (p_listStereotypeName!=null){
			for( String sIncompat : p_listStereotypeName ) {
				listIncompatibilties.add(sIncompat);
			}
		}
		this.addIncompatibility(p_sStereotypeName, listIncompatibilties);
	}
	
	/**
	 * Add an incompatibility between the stereotype in first parameter and the all stereotype of the list 
	 * @param p_sStereotypeName name of the incompatible stereotype 
	 * @param p_listStereotypeName name's list of the  of the incompatible stereotype 
	 */
	public void addIncompatibility(String p_sStereotypeName , List<String> p_listStereotypeName  ){
		String sStereotypeName1 = p_sStereotypeName.toLowerCase(); 
		HashSet<UmlStereotype> oIncompatibilitiesForAStereotype  = stereotypesIncompatibilities.get(p_sStereotypeName);
		if ( oIncompatibilitiesForAStereotype == null){
			oIncompatibilitiesForAStereotype = new HashSet<UmlStereotype>();
			stereotypesIncompatibilities.put(sStereotypeName1, oIncompatibilitiesForAStereotype);
		}
		if (p_listStereotypeName!=null){
			for ( String sStereotype : p_listStereotypeName){
				String sStereotypeLower = sStereotype.toLowerCase() ;
				oIncompatibilitiesForAStereotype.add(new UmlStereotype(sStereotype,sStereotype));
				this.addLowerCaseIncompatibility(sStereotypeLower, sStereotypeName1) ;
			}
		}
	}
	
	/**
	 * @param sStereotypeName1
	 * @param sStereotypeName2
	 */
	private void addLowerCaseIncompatibility(String sStereotypeName1 , String sStereotypeName2){
		HashSet<UmlStereotype> oIncompatibilitiesForAStereotype  = stereotypesIncompatibilities.get(sStereotypeName1);
		if ( oIncompatibilitiesForAStereotype == null){
			oIncompatibilitiesForAStereotype = new HashSet<UmlStereotype>();
			stereotypesIncompatibilities.put(sStereotypeName1, oIncompatibilitiesForAStereotype);
		}
		oIncompatibilitiesForAStereotype.add(new UmlStereotype(sStereotypeName2,sStereotypeName2));
	}
	
	/**
	 * Verify if the object is well stereotyped
	 * @param p_oBjectWithStereotype object to verify
	 * @return true if compatible, false if not
	 */
	public void verifyStereotypesOfObject( UmlStereotypedObject p_oObjectWithStereotype ){
		Collection<String> oListStereotypesNames = p_oObjectWithStereotype.getStereotypeNames() ;
		HashSet<UmlStereotype> oIncompatibilitiesForAStereotype ;
		String sSearchedStereoType ;
		for (String sStereoTypeName : oListStereotypesNames){
			sSearchedStereoType = sStereoTypeName.toLowerCase() ;
			oIncompatibilitiesForAStereotype  = stereotypesIncompatibilities.get(sSearchedStereoType);
			boolean bIsIncompatible = p_oObjectWithStereotype.hasAnyStereotype( oIncompatibilitiesForAStereotype);
			if ( bIsIncompatible ){
				MessageHandler.getInstance().addError("Class {}\r\n has an incompatible stereotype : '{}' in the list '{}'", 
						p_oObjectWithStereotype.toString() , 
						sSearchedStereoType , 
						p_oObjectWithStereotype.getStereotypeNames() );
			}
		}
	}
}
