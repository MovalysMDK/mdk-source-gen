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
package com.a2a.adjava.uml2xmodele.assoconvert;

import java.util.List;

import com.a2a.adjava.optionsetters.OptionSetter;
import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.uml.UmlAssociation;
import com.a2a.adjava.uml.UmlAssociationEnd;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MAssociation;
import com.a2a.adjava.xmodele.MAssociationOneToMany;
import com.a2a.adjava.xmodele.MEntityImpl;

/**
 * <p>TODO Décrire la classe OneToManyAssoConvertor</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */

public class OneToManyAssoConverter extends AbstractMAssociationConverter {

	/**
	 * Singleton instance
	 */
	private static OneToManyAssoConverter instance = new OneToManyAssoConverter();

	/**
	 * Constructor
	 */
	private OneToManyAssoConverter() {
		//Empty constructor
	}

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	public static OneToManyAssoConverter getInstance() {
		return instance;
	}
	
	/**
	 * @param p_sAssociationEndName
	 * @param p_oEnd
	 * @param p_oOppositeEnd
	 * @param p_oRefClass
	 * @param p_oOppositeRefClass
	 * @param p_sVariableName
	 * @param p_sVariableListName
	 * @param p_sParameterName
	 * @param p_oTypeDescription
	 * @param p_sVisibility
	 * @param p_sOppositeName
	 * @param p_bRelationOwner
	 * @return
	 * @throws Exception 
	 */
	protected MAssociation convertOneToMany(String p_sAssociationEndName, UmlAssociationEnd p_oEnd, UmlAssociationEnd p_oOppositeEnd,
			UmlAssociation p_oUmlAssociation,
			MEntityImpl p_oRefClass, MEntityImpl p_oOppositeRefClass, String p_sVariableName, String p_sVariableListName, String p_sParameterName,
			ITypeDescription p_oTypeDescription, String p_sVisibility, String p_sOppositeName, boolean p_bRelationOwner,
			List<OptionSetter<Object>> p_listAssoOptionSetters, IDomain<IModelDictionary, IModelFactory> p_oDomain) throws Exception {

		String sName = p_sAssociationEndName;

		boolean bNotNull = p_oOppositeEnd.getMultiplicityLower() == 1;
		sName = sName.substring(0, 1).toLowerCase() + sName.substring(1);

		MAssociationOneToMany r_oMAssociationOneToMany = 
				new MAssociationOneToMany(sName, p_oRefClass, p_oOppositeRefClass, p_sVariableName, p_sVariableListName, p_sParameterName,
				p_oTypeDescription, p_sVisibility, bNotNull, p_bRelationOwner, p_sOppositeName, p_oEnd.getAggregateType(), 
				p_oOppositeEnd.getAggregateType(), p_oOppositeEnd.isNavigable());
		
		this.applyOptions(p_oUmlAssociation.getOptions(), r_oMAssociationOneToMany, p_listAssoOptionSetters, p_oDomain.getLanguageConf());
		this.applyOptions(p_oEnd.getOptions(), r_oMAssociationOneToMany, p_listAssoOptionSetters, p_oDomain.getLanguageConf());
		
		return r_oMAssociationOneToMany ;
	}
}
