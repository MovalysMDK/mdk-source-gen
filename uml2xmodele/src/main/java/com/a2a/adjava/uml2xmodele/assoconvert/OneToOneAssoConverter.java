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
import com.a2a.adjava.uml2xmodele.uml.UmlExpandedAssociationEnd;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MAssociation;
import com.a2a.adjava.xmodele.MAssociationOneToOne;
import com.a2a.adjava.xmodele.MEntityImpl;

/**
 * <p>TODO Décrire la classe OneToOneAssoConverter</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */

public class OneToOneAssoConverter extends AbstractMAssociationConverter {

	/**
	 * Singleton instance
	 */
	private static OneToOneAssoConverter instance = new OneToOneAssoConverter();

	/**
	 * Constructor
	 */
	private OneToOneAssoConverter() {
		//Empty constructor
	}

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	public static OneToOneAssoConverter getInstance() {
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
	 * @param p_oDomain domain
	 * @return
	 * @throws Exception
	 */
	protected MAssociation convertOneToOne(String p_sAssociationEndName, UmlAssociationEnd p_oEnd, UmlAssociationEnd p_oOppositeEnd,
			UmlAssociation p_oAssociation,
			MEntityImpl p_oRefClass, MEntityImpl p_oOppositeRefClass, String p_sVariableName, String p_sVariableListName, String p_sParameterName,
			ITypeDescription p_oTypeDescription, String p_sVisibility, String p_sOppositeName, boolean p_bRelationOwner, boolean p_bPartOfIdentifier,
			List<OptionSetter<Object>> p_listAssoOptionSetters, IDomain<IModelDictionary, IModelFactory> p_oDomain) throws Exception {

		MAssociation r_oAssocation = null;
		String sName = p_sAssociationEndName;
		sName = sName.substring(0, 1).toLowerCase() + sName.substring(1);

		MAssociationOneToOne oMAssociationOneToOne = null;

		// Si unidirectionel
		if ( !p_oOppositeEnd.isNavigable()) {

			boolean bNotNull = p_oEnd.getMultiplicityLower() == 1;

			oMAssociationOneToOne = new MAssociationOneToOne(sName, p_oRefClass, p_oOppositeRefClass, p_sVariableName, p_sVariableListName,
					p_sParameterName, p_oTypeDescription, p_sVisibility, true, bNotNull, p_sOppositeName, p_oEnd.getAggregateType(),
					p_oOppositeEnd.getAggregateType(), false);
		}
		// Sinon bidirectionnelle
		else {
			boolean bNotNull = p_oEnd.getMultiplicityLower() == 1;

			oMAssociationOneToOne = new MAssociationOneToOne(sName, p_oRefClass, p_oOppositeRefClass, p_sVariableName, p_sVariableListName,
					p_sParameterName, p_oTypeDescription, p_sVisibility, p_bRelationOwner, bNotNull, p_sOppositeName, 
					p_oEnd.getAggregateType(), p_oOppositeEnd.getAggregateType(), true );
		}

		if ( UmlExpandedAssociationEnd.class.isAssignableFrom(p_oEnd.getClass())) {
			UmlExpandedAssociationEnd oUmlExpandedAssociationEnd = (UmlExpandedAssociationEnd) p_oEnd ;
			oMAssociationOneToOne.setExpandableTypeDesc(oUmlExpandedAssociationEnd.getExpandableTypeDesc());
		}
		
		this.applyOptions(p_oAssociation.getOptions(), oMAssociationOneToOne, p_listAssoOptionSetters, p_oDomain.getLanguageConf());
		this.applyOptions(p_oEnd.getOptions(), oMAssociationOneToOne, p_listAssoOptionSetters, p_oDomain.getLanguageConf());

		if (p_bPartOfIdentifier) {
			p_oOppositeRefClass.getIdentifier().addElem(oMAssociationOneToOne);
			p_oOppositeRefClass.addImport(oMAssociationOneToOne.getRefClass().getMasterInterface().getFullName());
		} else {
			r_oAssocation = oMAssociationOneToOne;
		}
		return r_oAssocation;
	}
}
