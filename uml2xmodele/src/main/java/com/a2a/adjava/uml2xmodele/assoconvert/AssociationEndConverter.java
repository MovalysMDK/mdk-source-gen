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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.optionsetters.OptionSetter;
import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.uml.UmlAssociation;
import com.a2a.adjava.uml.UmlAssociationEnd;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MAssociation;
import com.a2a.adjava.xmodele.MEntityImpl;

/**
 * 
 * <p>
 * Convert association end/p>
 * 
 * <p>
 * Copyright (c) 2009
 * <p>
 * Company: Adeuza
 * 
 * @author mmadigand
 * @author lmichenaud
 * 
 */
public final class AssociationEndConverter {
	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(AssociationEndConverter.class);

	/**
	 * Instance singleton
	 */
	private static AssociationEndConverter instance;
	
	/**
	 * Retourne l'instance singleton
	 * 
	 * @return l'instance singleton
	 */
	public static AssociationEndConverter getInstance() {
		if (instance == null) {
			instance = new AssociationEndConverter();
		}
		return instance;
	}

	/**
	 * Constructeur
	 */
	private AssociationEndConverter() {
	}

	/**
	 * Convertit l'extremite d'association
	 * 
	 * @param p_oEnd
	 *            l'extremite d'association
	 * @param p_oOppositeEnd
	 *            l'extremite oppose
	 * @param p_bRelationOwner
	 *            proprietaire de la relation
	 * @param p_oUmlAssociation
	 *            l'association
	 * @param p_oMapUmlClass2MClass
	 *            Map UmlClass=>MClass
	 * @param p_oConfig
	 *            config adjava
	 * @param p_oModeleDictionnary
	 *            dictionnaire du modele
	 * @throws Exception
	 *             echec de la conversion
	 */
	public void convertAssociationEnd(UmlAssociationEnd p_oEnd, UmlAssociationEnd p_oOppositeEnd, boolean p_bRelationOwner,
			UmlAssociation p_oUmlAssociation, IDomain<IModelDictionary, IModelFactory> p_oDomain, 
			List<OptionSetter<Object>> p_listAssoOptionSetters ) throws Exception {

		log.debug("> ConvertAssociationEnd");
		
		// Class that will contains the associations
		MEntityImpl oRefClass = p_oDomain.getDictionnary().getMapUmlClassToMClasses().get(p_oEnd.getRefClass().getFullName());
		log.debug("  dest class: {}", oRefClass.getFullName());

		// la classe a l'extremite de l'association
		MEntityImpl oOppositeRefClass = p_oDomain.getDictionnary().getMapUmlClassToMClasses().get(p_oOppositeEnd.getRefClass().getFullName());
		log.debug("  source class: {}", oOppositeRefClass.getFullName());

		String sName = p_oEnd.getName();
		String sSecondName = null;

		String sOppositeName = p_oOppositeEnd.getName();
		String sOppositeSecondName = null;

		String sVisibility = p_oEnd.getVisibility();
		String sVariableName = null;
		String sVariableListName = null;

		ITypeDescription oTypeDescription = null;
		boolean bPartOfIdentifier = p_oEnd.isId();

		if (sName != null && sName.length() > 1 && sName.charAt(0) == '@') {
			bPartOfIdentifier = true;
			sName = sName.substring(1);
		}

		if (sOppositeName != null && sOppositeName.length() > 1 && sOppositeName.charAt(0) == '@') {
			sOppositeName = sOppositeName.substring(1);
		}

		ITypeDescription oObjectTypeDescription = p_oDomain.getLanguageConf().getTypeDescription("Object");
		ITypeDescription oListTypeDescription = p_oDomain.getLanguageConf().getTypeDescription("List");

		if (sName != null && sName.length() > 0) {
			// Si nom compose
			int iPos = sName.indexOf('_');
			if (iPos != -1) {
				sSecondName = sName.substring(iPos + 1);
				sName = sName.substring(0, iPos);
			}

			sVariableName = oObjectTypeDescription.computeVariableName(sName);
			sVariableListName = oListTypeDescription.computeListVariableName(sName);
		}

		if (sOppositeName != null && sOppositeName.length() > 0) {
			// Si nom compose
			int iPos = sOppositeName.indexOf('_');
			if (iPos != -1) {
				sOppositeSecondName = sOppositeName.substring(iPos + 1);
				sOppositeName = sOppositeName.substring(0, iPos);
			}
		}

		// Calcule le type description pour la fin d'association ainsi
		// que le nom du parametre pour les methodes
		if (p_oEnd.getMultiplicityLower() >= 0 && p_oEnd.getMultiplicityUpper() == -1) {
			oTypeDescription = (ITypeDescription) oListTypeDescription.clone();

			ITypeDescription oContainedTypeDescription = (ITypeDescription) oObjectTypeDescription.clone();
			oContainedTypeDescription.setName(oRefClass.getMasterInterface().getFullName());
			oTypeDescription.setParameterizedElementType(oContainedTypeDescription);
		} else {
			oTypeDescription = (ITypeDescription) oObjectTypeDescription.clone();
			oTypeDescription.setName(oRefClass.getMasterInterface().getFullName());
		}
		
		String sParameterName = oTypeDescription.computeParameterName(sName);
		
		MAssociation oAssociation = null;
		
		if (p_oEnd.isNavigable()) {
			log.debug("  ajout de l'association end {} a la classe {}", p_oEnd.getName(), p_oOppositeEnd.getRefClass().getName());

			// Relation Many to many
			if (p_oEnd.getMultiplicityUpper() == -1 && p_oOppositeEnd.getMultiplicityUpper() == -1) {				
				oAssociation = ManyToManyAssoConverter.getInstance().convertManyToMany(sName, sSecondName, 
						p_oEnd, p_oOppositeEnd, p_oUmlAssociation, oRefClass, oOppositeRefClass,
						sVariableName, sVariableListName, sParameterName, oTypeDescription, sVisibility, sOppositeName, sOppositeSecondName,
						p_bRelationOwner, p_listAssoOptionSetters, p_oDomain );

			}

			// Relation Many to one
			if (p_oEnd.getMultiplicityUpper() == 1 && p_oOppositeEnd.getMultiplicityUpper() == -1) {

				oAssociation = ManyToOneAssoConverter.getInstance().convertManyToOne(sName, p_oEnd, oRefClass, 
						p_oOppositeEnd, oOppositeRefClass, p_oUmlAssociation, sVariableName, sVariableListName, sParameterName,
						oTypeDescription, sVisibility, sOppositeName, bPartOfIdentifier, p_listAssoOptionSetters, p_oDomain);
			}

			// Relation One to many
			if (p_oEnd.getMultiplicityUpper() == -1 && p_oOppositeEnd.getMultiplicityUpper() == 1) {

				oAssociation = OneToManyAssoConverter.getInstance().convertOneToMany(sName, p_oEnd, p_oOppositeEnd, 
						p_oUmlAssociation, oRefClass, oOppositeRefClass, sVariableName, sVariableListName,
						sParameterName, oTypeDescription, sVisibility, sOppositeName, p_bRelationOwner, p_listAssoOptionSetters, p_oDomain);
			}

			// Relation One to one
			if (p_oEnd.getMultiplicityUpper() == 1 && p_oOppositeEnd.getMultiplicityUpper() == 1) {

				oAssociation = OneToOneAssoConverter.getInstance().convertOneToOne(sName, p_oEnd, p_oOppositeEnd, p_oUmlAssociation,
						oRefClass, oOppositeRefClass, sVariableName, sVariableListName,
						sParameterName, oTypeDescription, sVisibility, sOppositeName, p_bRelationOwner, bPartOfIdentifier, p_listAssoOptionSetters, p_oDomain);
			}

			if (oAssociation != null) {

				log.debug("  type: {}", oAssociation.getAssociationType());
				log.debug("  relation owner: {}", oAssociation.isRelationOwner());

				// Ajout de l'association a la classe
				oOppositeRefClass.addAssociation(oAssociation);
			}
		} else {
			// Relation Many to many
			if (p_oEnd.getMultiplicityUpper() == -1 && p_oOppositeEnd.getMultiplicityUpper() == -1) {

				// L'extremite opposee doit etre navigable
				if (p_oOppositeEnd.isNavigable()) {
					oAssociation = ManyToManyAssoConverter.getInstance().convertManyToMany(sName, sSecondName, p_oEnd, p_oOppositeEnd, p_oUmlAssociation, oRefClass,
							oOppositeRefClass, sVariableName, sVariableListName, sParameterName, oTypeDescription, sVisibility, sOppositeName,
							sOppositeSecondName, p_bRelationOwner, p_listAssoOptionSetters, p_oDomain );
					oOppositeRefClass.addNonNavigableAssociation(oAssociation);
				} else {
					MessageHandler.getInstance().addError(
						"L'association '{}..{}' entre la classe '{}' et '{}' doit forcement etre navigable dans un sens.",
							p_oEnd.getName(), p_oOppositeEnd.getName(), p_oEnd.getRefClass().getName(), p_oOppositeEnd.getRefClass().getName());
				}
			}
			
			// Relation Many to one
			if (p_oEnd.getMultiplicityUpper() == 1 && p_oOppositeEnd.getMultiplicityUpper() == -1 ) {
				
				oAssociation = ManyToOneAssoConverter.getInstance().convertManyToOne(sName, p_oEnd, oRefClass, 
						p_oOppositeEnd, oOppositeRefClass, p_oUmlAssociation, sVariableName, sVariableListName, sParameterName,
						oTypeDescription, sVisibility, sOppositeName, bPartOfIdentifier, p_listAssoOptionSetters, p_oDomain);
				
				if ( !oAssociation.isTransient()) {
					MessageHandler.getInstance().addError(
						"ManyToOne persistable association must be navigable, association end name: {}, target class: {}, opposite association end name: {}, opposite class: {}",
						p_oEnd.getName(), p_oEnd.getRefClass().getName(), p_oOppositeEnd.getName(), p_oOppositeEnd.getRefClass().getName());					
					oAssociation = null ;
				}
				else {
					oOppositeRefClass.addNonNavigableAssociation(oAssociation);
				}
			}
		}
		
		log.debug("< ConvertAssociationEnd");
	}
}
