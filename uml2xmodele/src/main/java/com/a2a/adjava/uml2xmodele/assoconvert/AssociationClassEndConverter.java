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
import java.util.Map;

import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.optionsetters.OptionSetter;
import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.uml.UmlAssociationClass;
import com.a2a.adjava.uml.UmlAssociationEnd;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MAssociation;
import com.a2a.adjava.xmodele.MAssociationManyToOne;
import com.a2a.adjava.xmodele.MAssociationOneToMany;
import com.a2a.adjava.xmodele.MEntityImpl;

/**
 * <p>
 * Convertit une extremité de classe d'association
 * </p>
 * 
 * <p>
 * Copyright (c) 2009
 * <p>
 * Company: Adeuza
 * 
 * @author lmichenaud
 * 
 */
public final class AssociationClassEndConverter extends AbstractMAssociationConverter {

	/**
	 * Instance singleton
	 */
	private static AssociationClassEndConverter instance;

	/**
	 * Retourne l'instance singleton
	 * 
	 * @return instance singleton
	 */
	public static AssociationClassEndConverter getInstance() {
		if (instance == null) {
			instance = new AssociationClassEndConverter();
		}
		return instance;
	}

	/**
	 * Constructeur
	 */
	private AssociationClassEndConverter() {
	}

	/**
	 * Convertit l'extrémité d'une classe d'association
	 * @param p_oClass la classe d'association
	 * @param p_oEnd l'extrémité d'association
	 * @param p_oOppositeEnd l'extrémité opposée
	 * @param p_bIsEnd1 est-ce que c'est la relation 1
	 * @param p_oMapUmlClass2MClass map UmlClass=>MClass
	 * @param p_oConfig config adjava
	 * @param p_oModeleDictionnary dictionnaire des données
	 * @throws Exception 
	 */
	public void convertAssociationClassEnd(MEntityImpl p_oMClass, UmlAssociationEnd p_oEnd, UmlAssociationEnd p_oOppositeEnd, 
			UmlAssociationClass p_oUmlAssociationClass, boolean p_bIsEnd1, Map<String, MEntityImpl> p_oMapUmlClass2MClass,
			List<OptionSetter<Object>> p_listAssoOptionSetters, IDomain<IModelDictionary, IModelFactory> p_oDomain) throws Exception {

		// la classe qui va contenir l'association avec la map uml class => mclass
		MEntityImpl oRefClass = p_oMapUmlClass2MClass.get(p_oEnd.getRefClass().getFullName());

		// identifie le nom de l'association pour la classe d'association et celui de l'association pour la classe opposée
		boolean bPartOfPrimaryKey = p_oEnd.getName().charAt(0) == '@' || p_oEnd.isId();
		
		String sAssociatioEndName = p_oEnd.getName();
		if (bPartOfPrimaryKey) {
			sAssociatioEndName = sAssociatioEndName.substring(1);
		}

		String[] t_sEndNames = sAssociatioEndName.split("_");
		if ( t_sEndNames.length == 2 ) {
			String sNameForOppositeClass = t_sEndNames[0].substring(0, 1).toLowerCase() + t_sEndNames[0].substring(1);
	
			String sNameForAssociationClass = t_sEndNames[1].substring(0, 1).toLowerCase() + t_sEndNames[1].substring(1);
	
			ITypeDescription oObjectTypeDescription = p_oDomain.getLanguageConf().getTypeDescription("Object");
			ITypeDescription oTypeDescription = (ITypeDescription) oObjectTypeDescription.clone();
			oTypeDescription.setName(oRefClass.getMasterInterface().getFullName());
			
			String sParameterName = oTypeDescription.computeParameterName(sNameForAssociationClass);
			
			ITypeDescription oListTypeDescription = p_oDomain.getLanguageConf().getTypeDescription("List");
			
			String sVariableName = oTypeDescription.computeVariableName(sNameForAssociationClass);
			String sVariableListName = oListTypeDescription.computeListVariableName(sNameForAssociationClass);
	
			boolean bUnique = false;
			boolean bNotNull = true;
			
			MAssociationManyToOne oManyToOneAssociation = new MAssociationManyToOne(sNameForAssociationClass, oRefClass, p_oMClass, sVariableName,
					sVariableListName, sParameterName, oTypeDescription, p_oEnd.getVisibility(), bUnique, bNotNull, sNameForOppositeClass,
					p_oEnd.getAggregateType(), p_oOppositeEnd.getAggregateType(), p_oOppositeEnd.isNavigable());
	
			this.applyOptions(p_oUmlAssociationClass.getOptions(), oManyToOneAssociation, p_listAssoOptionSetters, p_oDomain.getLanguageConf());
			this.applyOptions(p_oEnd.getOptions(), oManyToOneAssociation, p_listAssoOptionSetters, p_oDomain.getLanguageConf());

			if (bPartOfPrimaryKey) {
				p_oMClass.getIdentifier().addElem(oManyToOneAssociation);
			} else {
				p_oMClass.addAssociation(oManyToOneAssociation);
			}
			
			String sUniqueKey = "U_" + p_oMClass.getMasterInterface().getName().toUpperCase();
			oManyToOneAssociation.setUniqueKey(sUniqueKey);
				
			p_oDomain.getDictionnary().addMissingIndexFor(oManyToOneAssociation);
		
			p_oMClass.addImport(oManyToOneAssociation.getRefClass().getMasterInterface().getFullName());
	
			// si la relation inverse est navigable, on va ajouter la relation 1..n à la classe destination
			if (p_oOppositeEnd.isNavigable()) {
	
				String[] t_sOppositeEndNames = p_oOppositeEnd.getName().split("_");
				String sAssoNameForRefClass = t_sOppositeEndNames[0].substring(0, 1).toLowerCase()
						+ t_sOppositeEndNames[0].substring(1);
	
				if (sAssoNameForRefClass.startsWith("@")) {
					sAssoNameForRefClass = sAssoNameForRefClass.substring(1);
				}
	
				oListTypeDescription = p_oDomain.getLanguageConf().getTypeDescription("List");
				ITypeDescription oObjectTypeDesc = p_oDomain.getLanguageConf().getTypeDescription("Object");
	
				sParameterName = oTypeDescription.computeParameterName(sAssoNameForRefClass);
				sVariableName = oObjectTypeDesc.computeVariableName(sAssoNameForRefClass);
				sVariableListName = oListTypeDescription.computeListVariableName(sAssoNameForRefClass);

				// Faire le one to many
	
				ITypeDescription oTypeDescForOne2Many = (ITypeDescription) oListTypeDescription.clone();
	
				ITypeDescription oContainedTypeDescriptionForOne2Many = (ITypeDescription) oObjectTypeDesc.clone();
				oContainedTypeDescriptionForOne2Many.setName(p_oMClass.getMasterInterface().getFullName());
	
				oTypeDescForOne2Many.setParameterizedElementType(oContainedTypeDescriptionForOne2Many);
				
				MAssociation oOneToManyAssociation = new MAssociationOneToMany(sAssoNameForRefClass, p_oMClass, oRefClass, sVariableName,
						sVariableListName, sParameterName, oTypeDescForOne2Many, p_oOppositeEnd.getVisibility(), bNotNull, false,
						sNameForAssociationClass, p_oEnd.getAggregateType(), p_oOppositeEnd.getAggregateType(), p_oOppositeEnd.isNavigable());
				
				this.applyOptions(p_oUmlAssociationClass.getOptions(), oOneToManyAssociation, p_listAssoOptionSetters, p_oDomain.getLanguageConf());
				this.applyOptions(p_oEnd.getOptions(), oOneToManyAssociation, p_listAssoOptionSetters, p_oDomain.getLanguageConf());
				
				// Ajout de l'association a la classe
				oRefClass.addAssociation(oOneToManyAssociation);
	
				// Ajoute les accesseurs a l'interface
			}
		}
		else {
			MessageHandler.getInstance().addError(
				"On ManyToMany associations, association ends must have two names using separator '_', association end name: {}, target class: {}, opposite association end name: {}, , opposite class: {}",
				p_oEnd.getName(), p_oEnd.getRefClass().getName(), p_oOppositeEnd.getRefClass().getName(),
				p_oOppositeEnd.getRefClass().getName());
		}
	}
}
