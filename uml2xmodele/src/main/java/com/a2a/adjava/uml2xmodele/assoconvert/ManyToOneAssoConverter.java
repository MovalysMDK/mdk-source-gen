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

import org.apache.commons.lang3.StringUtils;

import com.a2a.adjava.optionsetters.OptionSetter;
import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.uml.UmlAssociation;
import com.a2a.adjava.uml.UmlAssociationEnd;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MAssociation;
import com.a2a.adjava.xmodele.MAssociationManyToOne;
import com.a2a.adjava.xmodele.MEntityImpl;

/**
 * <p>Convert an association End to a MAssociationManyToOne object</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */

public class ManyToOneAssoConverter extends AbstractMAssociationConverter {

	/**
	 * Singleton instance
	 */
	private static ManyToOneAssoConverter instance = new ManyToOneAssoConverter();

	/**
	 * Constructor
	 */
	private ManyToOneAssoConverter() {
		//Empty constructor
	}

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	public static ManyToOneAssoConverter getInstance() {
		return instance;
	}
	
	/**
	 * @param p_sAssociationEndName
	 * @param p_oEnd
	 * @param p_oRefClass
	 * @param p_oOppositeRefClass
	 * @param p_sVariableName
	 * @param p_sVariableListName
	 * @param p_sParameterName
	 * @param p_oTypeDescription
	 * @param p_sVisibility
	 * @param p_sOppositeName
	 * @param p_oDomain 
	 * @return
	 */
	protected MAssociation convertManyToOne(String p_sAssociationEndName, UmlAssociationEnd p_oEnd, MEntityImpl p_oRefClass, 
			UmlAssociationEnd p_oOppositeEnd, MEntityImpl p_oOppositeRefClass, UmlAssociation p_oUmlAssociation,
			String p_sVariableName, String p_sVariableListName, String p_sParameterName, ITypeDescription p_oTypeDescription, String p_sVisibility,
			String p_sOppositeName, boolean p_bPartOfIdentifier, List<OptionSetter<Object>> p_listAssoOptionSetters, 
			IDomain<IModelDictionary, IModelFactory> p_oDomain) throws Exception {

		MAssociation r_oAssocation = null;
		String sName = p_sAssociationEndName;

		if ( validate()) {
		
			sName = StringUtils.uncapitalize(sName);
	
			boolean bUnique = false;
			boolean bNotNull = p_oEnd.getMultiplicityLower() == 1;
	
			MAssociationManyToOne oMAssociationManyToOne = new MAssociationManyToOne(sName, p_oRefClass, p_oOppositeRefClass, p_sVariableName,
					p_sVariableListName, p_sParameterName, p_oTypeDescription, p_sVisibility, bUnique, bNotNull, p_sOppositeName,
					p_oEnd.getAggregateType(), p_oOppositeEnd.getAggregateType(), p_oOppositeEnd.isNavigable());
	
			this.applyOptions(p_oUmlAssociation.getOptions(), oMAssociationManyToOne, p_listAssoOptionSetters, p_oDomain.getLanguageConf());
			this.applyOptions(p_oEnd.getOptions(), oMAssociationManyToOne, p_listAssoOptionSetters, p_oDomain.getLanguageConf());
	
			if (p_bPartOfIdentifier) {
				p_oOppositeRefClass.getIdentifier().addElem(oMAssociationManyToOne);
				p_oOppositeRefClass.addImport(oMAssociationManyToOne.getRefClass().getMasterInterface().getFullName());

			} else {
				r_oAssocation = oMAssociationManyToOne;
			}
		}
		return r_oAssocation;
	}
	
	private boolean validate() {
		boolean r_bValide = true ;
		
		return r_bValide ;
	}
}
