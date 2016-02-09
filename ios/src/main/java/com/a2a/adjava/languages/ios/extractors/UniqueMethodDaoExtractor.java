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
package com.a2a.adjava.languages.ios.extractors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import com.a2a.adjava.languages.ios.xmodele.MIOSDictionnary;
import com.a2a.adjava.languages.ios.xmodele.MIOSDomain;
import com.a2a.adjava.languages.ios.xmodele.MIOSModeleFactory;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.uml.UmlOperation;
import com.a2a.adjava.uml2xmodele.extractors.AbstractExtractor;
import com.a2a.adjava.xmodele.MAssociation;
import com.a2a.adjava.xmodele.MAssociationManyToOne;
import com.a2a.adjava.xmodele.MAssociationOneToOne;
import com.a2a.adjava.xmodele.MAttribute;
import com.a2a.adjava.xmodele.MEntityImpl;

/**
 * Add exist method
 * @author lmichenaud
 *
 */
public class UniqueMethodDaoExtractor extends AbstractExtractor<MIOSDomain<MIOSDictionnary, MIOSModeleFactory>>{

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.extractors.MExtractor#initialize(org.dom4j.Element)
	 */
	@Override
	public void initialize(Element p_xConfig) throws Exception {
		// nothing to initialize
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.extractors.MExtractor#extract(com.a2a.adjava.uml.UmlModel)
	 */
	@Override
	public void extract(UmlModel p_oModele) throws Exception {

		for (UmlClass oUmlClass : p_oModele.getDictionnary().getAllClasses()) {
			MEntityImpl oEntity = getDomain().getDictionnary().getMapUmlClassToMClasses().get(oUmlClass.getFullName());
			
			// is null when uml class is not an entity
			if ( oEntity != null ) {
				this.addUniqueOperations(oUmlClass, oEntity);
			}
		}
	}

	/**
	 * Find unique constraints and add a unique operation for it
	 * @param p_oUmlClass uml class
	 * @param p_oEntity entity
	 */
	private void addUniqueOperations(UmlClass p_oUmlClass, MEntityImpl p_oEntity) {
		Map<String,List<String>> mapUniqueConstraints = this.createMapOfUniqueContraints(p_oEntity);
		this.addMethodsOnEntity(p_oUmlClass, mapUniqueConstraints);
	}
	
	
	/**
	 * Create a map of unique constraints for entity
	 * @param p_oEntity entity
	 * @return map of unique constraints for entity
	 */
	public Map<String, List<String>> createMapOfUniqueContraints( MEntityImpl p_oEntity ) {
		
		Map<String,List<String>> r_mapUniqueConstraints = new HashMap<String,List<String>>();
		
		for( MAttribute oAttribute : p_oEntity.getAttributes()) {
			if ( StringUtils.isNotEmpty(oAttribute.getUniqueKey())) {
				this.addUniqueKey(r_mapUniqueConstraints, oAttribute.getUniqueKey(), oAttribute.getName());
			}
			else
			if ( oAttribute.isUnique()) {
				this.addUniqueKey(r_mapUniqueConstraints, oAttribute.getName(), oAttribute.getName());
			}
		}
		
		for( MAssociation oAssociation : p_oEntity.getAssociations()) {
			if ( oAssociation instanceof MAssociationManyToOne) {
				MAssociationManyToOne oMAssociationManyToOne = (MAssociationManyToOne) oAssociation ;
				if ( oMAssociationManyToOne.getUniqueKey() != null ) {
					this.addUniqueKey(r_mapUniqueConstraints, oMAssociationManyToOne.getUniqueKey(), oMAssociationManyToOne.getName());
				}
			}
			else
			if ( oAssociation instanceof MAssociationOneToOne) {
				MAssociationOneToOne oMAssociationOneToOne = (MAssociationOneToOne) oAssociation ;
				if ( oMAssociationOneToOne.getUniqueKey() != null ) {
					this.addUniqueKey(r_mapUniqueConstraints, oMAssociationOneToOne.getUniqueKey(), oMAssociationOneToOne.getName());
				}
			}
		}
		
		return r_mapUniqueConstraints;
	}
	
	/**
	 * Add/Complete a unique key in the map of unique keys
	 * @param p_listUniqueConstraints map of unique keys, key is the constraint name, value if the list of constraint parts.
	 * @param p_sUniqueKey name of unique key
	 * @param p_sPart contraint part to add to the unique key
	 */
	private void addUniqueKey( Map<String,List<String>> p_listUniqueConstraints, String p_sUniqueKey, String p_sPart ) {
		List<String> listParts = p_listUniqueConstraints.get(p_sUniqueKey);
		if ( listParts == null ) {
			listParts = new ArrayList<String>();
			p_listUniqueConstraints.put(p_sUniqueKey, listParts);
		}
		listParts.add(p_sPart);
	}
	
	/**
	 * Create missing operations on entity using the map of unique constraints
	 * @param p_oUmlClass uml class
	 * @param p_lListUniqueConstraints map of unique constraints
	 */
	private void addMethodsOnEntity( UmlClass p_oUmlClass, Map<String,List<String>> p_lListUniqueConstraints ) {
		
		for( List<String> oEntry : p_lListUniqueConstraints.values()) {
			StringBuilder sCriteriaPart = new StringBuilder();
			for( String sPart : oEntry ) {
				sCriteriaPart.append('_');
				sCriteriaPart.append(sPart);
			}			
			p_oUmlClass.addOperation(new UmlOperation(
				StringUtils.join("exist_By", sCriteriaPart.toString()), p_oUmlClass));
			p_oUmlClass.addOperation(new UmlOperation(
				StringUtils.join("get", p_oUmlClass.getName(), "_By", sCriteriaPart.toString()), p_oUmlClass));
		}
	}
}
