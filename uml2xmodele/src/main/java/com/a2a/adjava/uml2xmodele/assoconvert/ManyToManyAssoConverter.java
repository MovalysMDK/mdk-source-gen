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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.optionsetters.DefaultAssoOptionSetter;
import com.a2a.adjava.optionsetters.OptionSetter;
import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.uml.UmlAssociation;
import com.a2a.adjava.uml.UmlAssociationEnd;
import com.a2a.adjava.utils.StrUtils;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MAssociation;
import com.a2a.adjava.xmodele.MAssociation.AssociationType;
import com.a2a.adjava.xmodele.MAssociationManyToMany;
import com.a2a.adjava.xmodele.MAssociationPersistableManyToMany;
import com.a2a.adjava.xmodele.MAttribute;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MIdentifierElem;
import com.a2a.adjava.xmodele.MJoinEntityImpl;

/**
 * <p>
 * ManyToManyAssoConverter
 * </p>
 * 
 * <p>
 * Copyright (c) 2011
 * <p>
 * Company: Adeuza
 * 
 * @author lmichenaud
 * 
 */

public final class ManyToManyAssoConverter extends AbstractMAssociationConverter {

	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(ManyToManyAssoConverter.class);

	/**
	 * Singleton instance
	 */
	private static ManyToManyAssoConverter instance = new ManyToManyAssoConverter();

	/**
	 * Constructor
	 */
	private ManyToManyAssoConverter() {
		// Empty constructor
	}

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	public static ManyToManyAssoConverter getInstance() {
		return instance;
	}

	/**
	 * Convert uml association to ManyToMany association
	 * @param p_sAssociationEndName name of association name
	 * @param p_sAssociationEndSecondName second name of association name
	 * @param p_oEnd uml association end
	 * @param p_oOppositeEnd opposite uml association end
	 * @param p_oUmlAssociation uml association
	 * @param p_oRefClass target entity class
	 * @param p_oOppositeRefClass source entity class
	 * @param p_sVariableName variable name
	 * @param p_sVariableListName name for variable of type list
	 * @param p_sParameterName parameter name
	 * @param p_oTypeDescription type description
	 * @param p_sVisibility visibility
	 * @param p_sOppositeName opposite name
	 * @param p_sOppositeSecondName second opposite name
	 * @param p_bRelationOwner is relation owner
	 * @param p_listAssoOptionSetters option setters for association
	 * @param p_oDomain domain domain
	 * @return association
	 * @throws Exception exception
	 */
	protected MAssociation convertManyToMany(String p_sAssociationEndName,
			String p_sAssociationEndSecondName, UmlAssociationEnd p_oEnd, UmlAssociationEnd p_oOppositeEnd,
			UmlAssociation p_oUmlAssociation, MEntityImpl p_oRefClass, MEntityImpl p_oOppositeRefClass,
			String p_sVariableName, String p_sVariableListName, String p_sParameterName,
			ITypeDescription p_oTypeDescription, String p_sVisibility, String p_sOppositeName,
			String p_sOppositeSecondName, boolean p_bRelationOwner,
			List<OptionSetter<Object>> p_listAssoOptionSetters, IDomain<IModelDictionary, IModelFactory> p_oDomain) throws Exception {

		MAssociation r_oAssociation = null;

		if ( this.isPersistent(p_oUmlAssociation)) {

			// persistable many to many
			r_oAssociation = this.convertPersistableManyToMany(p_sAssociationEndName, p_sAssociationEndSecondName,
					p_oEnd, p_oOppositeEnd, p_oUmlAssociation, p_oRefClass, p_oOppositeRefClass,
					p_sVariableName, p_sVariableListName, p_sParameterName, p_oTypeDescription,
					p_sVisibility, p_sOppositeName, p_sOppositeSecondName, p_bRelationOwner, p_oDomain);

		} else {
			// transient many to many
			r_oAssociation = this.convertTransientManyToMany(p_sAssociationEndName, p_oEnd, p_oOppositeEnd, 
					p_oRefClass, p_oOppositeRefClass, p_sVariableName, p_sVariableListName, p_sParameterName, p_oTypeDescription,
					p_sVisibility, p_sOppositeName, p_bRelationOwner );
		}
		
		this.applyOptions(p_oUmlAssociation.getOptions(), r_oAssociation, p_listAssoOptionSetters, p_oDomain.getLanguageConf());
		this.applyOptions(p_oEnd.getOptions(), r_oAssociation, p_listAssoOptionSetters, p_oDomain.getLanguageConf());

		return r_oAssociation;
	}

	/**
	 * Convert uml association to persistable ManyToMany association
	 * @param p_sAssociationEndName name of association name
	 * @param p_sAssociationEndSecondName second name of association name
	 * @param p_oEnd uml association end
	 * @param p_oOppositeEnd opposite uml association end
	 * @param p_oUmlAssociation uml association
	 * @param p_oRefClass target entity class
	 * @param p_oOppositeRefClass source entity class
	 * @param p_sVariableName variable name
	 * @param p_sVariableListName name for variable of type list
	 * @param p_sParameterName parameter name
	 * @param p_oTypeDescription type description
	 * @param p_sVisibility visibility
	 * @param p_sOppositeName opposite name
	 * @param p_sOppositeSecondName second opposite name
	 * @param p_bRelationOwner is relation owner
	 * @param p_oDomain domain domain
	 * @return association
	 * @throws Exception exception
	 */
	private MAssociation convertPersistableManyToMany(String p_sAssociationEndName,
			String p_sAssociationEndSecondName, UmlAssociationEnd p_oEnd, UmlAssociationEnd p_oOppositeEnd,
			UmlAssociation p_oUmlAssociation, MEntityImpl p_oRefClass, MEntityImpl p_oOppositeRefClass,
			String p_sVariableName, String p_sVariableListName, String p_sParameterName,
			ITypeDescription p_oTypeDescription, String p_sVisibility, String p_sOppositeName,
			String p_sOppositeSecondName, boolean p_bRelationOwner, IDomain<IModelDictionary, IModelFactory> p_oDomain) throws Exception {

		MAssociation r_oAssociation = null;

		String sAssoNameForJoinClass = p_sAssociationEndSecondName;
		String sJoinClassName = p_oUmlAssociation.getName();

		if ( this.validatePersistableAsso(sAssoNameForJoinClass, sJoinClassName, p_oEnd, p_oOppositeEnd)) {

			String sName = p_sAssociationEndName;

			sName = StringUtils.uncapitalize(sName);
			sAssoNameForJoinClass = StringUtils.uncapitalize(sAssoNameForJoinClass);

			String sOppositeName = p_sOppositeName;
			String sOppositeNameForJoinClass = p_sOppositeSecondName;
			sOppositeName = StringUtils.uncapitalize(sOppositeName);
			sOppositeNameForJoinClass = StringUtils.uncapitalize(sOppositeNameForJoinClass);

			// Normalize le nom de la join classe
			int iPos = sJoinClassName.indexOf('_');
			if (iPos != -1) {
				MessageHandler
						.getInstance()
						.addWarning(
								"Le nom '{}' de la relation n..n est incorrect : il ne doit pas contenir le caractere '_'",
									p_oUmlAssociation.getName());
				while (iPos != -1) {
					sJoinClassName = sJoinClassName.substring(0, iPos)
							+ sJoinClassName.substring(iPos + 1, iPos + 2).toUpperCase(Locale.getDefault())
							+ sJoinClassName.substring(iPos + 2);
					iPos = sJoinClassName.indexOf('_');
				}
			}
			String sEntityName = sJoinClassName;
			sJoinClassName = StringUtils.join(sJoinClassName, p_oDomain.getLanguageConf().getImplNamingSuffix());

			MAssociationPersistableManyToMany oManyToManyAssociation = new MAssociationPersistableManyToMany(sName,
					sAssoNameForJoinClass, p_oRefClass, p_oOppositeRefClass, p_sVariableName,
					p_sVariableListName, p_sParameterName, p_oTypeDescription, p_sVisibility,
					p_bRelationOwner, sOppositeName, sOppositeNameForJoinClass, p_oEnd.getAggregateType(),
					p_oOppositeEnd.getAggregateType(), p_oOppositeEnd.isNavigable());

			// si relation owner, on cree la join class
			if (p_bRelationOwner) {

				this.createJoinClass(p_oUmlAssociation, p_oRefClass,
						p_oOppositeRefClass, p_oDomain, sAssoNameForJoinClass,
						sJoinClassName, sOppositeNameForJoinClass, sEntityName,
						oManyToManyAssociation);
			}
			// sinon elle existe deja
			else {
				MJoinEntityImpl oJoinClass = p_oDomain.getDictionnary().getMJoinClass(
						p_oRefClass.getPackage().getFullName() + StrUtils.DOT_S + sJoinClassName);
				oManyToManyAssociation.setJoinClass(oJoinClass);
				oManyToManyAssociation.setKeyAttrs(oJoinClass.getRightKeyAttrs());
				oManyToManyAssociation.setCriteriaAttrs(oJoinClass.getLeftKeyAttrs());

				StringBuilder sKeyAttrs = new StringBuilder();
				for (MAttribute oAttribute : oJoinClass.getRightKeyAttrs()) {
					sKeyAttrs.append(oAttribute.getName());
					sKeyAttrs.append(" - ");
				}

				log.debug("opposite association,  key attrs: {} matchs with relation {}, class: {}",
					new Object[] { sKeyAttrs.toString(), sOppositeNameForJoinClass, p_oOppositeRefClass.getName() });
				oJoinClass.setOppositionAssociation(oManyToManyAssociation );
			}

			r_oAssociation = oManyToManyAssociation;
		}
		return r_oAssociation;
	}

	/**
	 * Create a join class
	 * @param p_oUmlAssociation uml association
	 * @param p_oRefClass target ref class
	 * @param p_oOppositeRefClass source class
	 * @param p_oDomain domain
	 * @param p_sAssoNameForJoinClass association name for join class
	 * @param p_sJoinClassName join
	 * @param p_sOppositeNameForJoinClass opposite name for join class
	 * @param p_sEntityName entity name
	 * @param p_oManyToManyAssociation many to many association
	 */
	private void createJoinClass(UmlAssociation p_oUmlAssociation,
			MEntityImpl p_oRefClass, MEntityImpl p_oOppositeRefClass,
			IDomain<IModelDictionary, IModelFactory> p_oDomain,
			String p_sAssoNameForJoinClass, String p_sJoinClassName,
			String p_sOppositeNameForJoinClass, String p_sEntityName,
			MAssociationPersistableManyToMany p_oManyToManyAssociation) {
		
		List<MAttribute> listKeyAttrs = this.extractAttributesForJoinClass(p_sJoinClassName,
				p_oOppositeRefClass, p_oRefClass.getUmlName(), p_sOppositeNameForJoinClass, p_oDomain);

		StringBuilder sKeyAttrs = new StringBuilder();
		for (MAttribute oAttribute : listKeyAttrs) {
			sKeyAttrs.append(oAttribute.getName());
			sKeyAttrs.append(" - ");
		}
		log.debug("  key attrs: {} matchs with relation {}, class: {}",	new Object[] { sKeyAttrs.toString(), 
				p_sOppositeNameForJoinClass, p_oOppositeRefClass.getName()});

		List<MAttribute> listCriteriaAttrs = this.extractAttributesForJoinClass(p_sJoinClassName,
				p_oRefClass, p_oOppositeRefClass.getUmlName(), p_sAssoNameForJoinClass, p_oDomain);

		p_oManyToManyAssociation.setKeyAttrs(listKeyAttrs);
		p_oManyToManyAssociation.setCriteriaAttrs(listCriteriaAttrs);

		List<MAttribute> listJoinClassAttrs = new ArrayList<MAttribute>();
		listJoinClassAttrs.addAll(listKeyAttrs);
		listJoinClassAttrs.addAll(listCriteriaAttrs);

		MJoinEntityImpl oMJoinClass = new MJoinEntityImpl(p_sJoinClassName,
				p_oOppositeRefClass.getPackage(), p_oUmlAssociation.getName(), p_sEntityName,
				p_oManyToManyAssociation, listKeyAttrs, listCriteriaAttrs);
		p_oDomain.getDictionnary().registerJoinClass(oMJoinClass);

		p_oManyToManyAssociation.setJoinClass(oMJoinClass);
	}

	/**
	 * Convert uml association to transient ManyToMany association
	 * @param p_sAssociationEndName name of association name
	 * @param p_oEnd uml association end
	 * @param p_oOppositeEnd opposite uml association end
	 * @param p_oRefClass target entity class
	 * @param p_oOppositeRefClass source entity class
	 * @param p_sVariableName variable name
	 * @param p_sVariableListName name for variable of type list
	 * @param p_sParameterName parameter name
	 * @param p_oTypeDescription type description
	 * @param p_sVisibility visibility
	 * @param p_sOppositeName opposite name
	 * @param p_bRelationOwner is relation owner
	 * @return association
	 * @throws Exception exception
	 */
	private MAssociation convertTransientManyToMany(String p_sAssociationEndName,
			UmlAssociationEnd p_oEnd, UmlAssociationEnd p_oOppositeEnd,
			MEntityImpl p_oRefClass, MEntityImpl p_oOppositeRefClass,
			String p_sVariableName, String p_sVariableListName, String p_sParameterName,
			ITypeDescription p_oTypeDescription, String p_sVisibility, String p_sOppositeName,
			boolean p_bRelationOwner ) throws Exception {
	
		MAssociation r_oAssociation = new MAssociationManyToMany(p_sAssociationEndName, AssociationType.MANY_TO_MANY, 
				p_oEnd.getAggregateType(), p_oOppositeEnd.getAggregateType(), p_oRefClass,
				p_oOppositeRefClass, p_sVariableName, p_sVariableListName, p_sParameterName, p_oTypeDescription,
				p_sVisibility, p_bRelationOwner, p_sOppositeName, p_oOppositeEnd.isNavigable());
		
		return r_oAssociation;
	}
	
	/**
	 * return true uml association is persistent
	 * @param p_oAssociation uml association
	 * @return true uml association is persistent
	 */
	private boolean isPersistent(UmlAssociation p_oAssociation) {
		Map<String, ?> mapAssoOptions = UmlAssociationEndOptionParser.getInstance().parse(p_oAssociation.getOptions());
		return !mapAssoOptions.containsKey(DefaultAssoOptionSetter.TRANSIENT_OPTION);
	}

	/**
	 * Validate a persistable association
	 * @param p_sAssoNameForJoinClass association name for join class
	 * @param p_sJoinClassName join class name
	 * @param p_oEnd uml association end
	 * @param p_oOppositeEnd opposite uml association end
	 * @return true if value
	 */
	private boolean validatePersistableAsso(String p_sAssoNameForJoinClass, String p_sJoinClassName,
			UmlAssociationEnd p_oEnd, UmlAssociationEnd p_oOppositeEnd) {
		boolean r_bValide = true;

		if ( StringUtils.isEmpty(p_sAssoNameForJoinClass)) {
			MessageHandler.getInstance().addError(
				"On ManyToMany associations, association ends must have two names using separator '_', association end name: {}, target class: {}, opposite association end name: {}, opposite class: {}",
					p_oEnd.getName(), p_oEnd.getRefClass().getName(), p_oOppositeEnd.getRefClass().getName(), p_oOppositeEnd.getRefClass().getName());
			
			r_bValide = false;
		}

		if ( StringUtils.isEmpty(p_sJoinClassName)) {
			MessageHandler.getInstance().addError(
				"ManyToMany association must have a name, association end name: {}, target class : {}, opposite association end name: {}, opposite class : {}",
					p_oEnd.getName(), p_oEnd.getRefClass().getName(), p_oOppositeEnd.getRefClass().getName(), p_oOppositeEnd.getRefClass().getName());
			
			r_bValide = false;
		}

		return r_bValide;
	}

	/**
	 * Extrait les attributs pour la classe de jointure
	 * @param p_sJoinclassName nom de la classe de jointure
	 * @param p_oClass Classe ref (source)
	 * @param p_sOppositeClassUmlName nom de la classe opposée (destination)
	 * @param p_sNameForJoinClass nom de la relation pour la classe de jointure
	 * @param p_oDomain domain
	 * @return les attributs pour la classe de jointure
	 */
	private List<MAttribute> extractAttributesForJoinClass(String p_sJoinclassName, MEntityImpl p_oClass,
			String p_sOppositeClassUmlName, String p_sNameForJoinClass, IDomain<IModelDictionary, IModelFactory> p_oDomain) {
		List<MAttribute> r_listAttributes = new ArrayList<MAttribute>();

		for (MIdentifierElem oMIdentifierElem : p_oClass.getIdentifier().getElems()) {

			if (oMIdentifierElem instanceof MAttribute) {
				MAttribute oAttribute = (MAttribute) oMIdentifierElem;
				String sName = p_sNameForJoinClass + StringUtils.capitalize(oAttribute.getName());
				MAttribute oNewAttribute = p_oDomain.getXModeleFactory().createMAttribute(sName,
						oAttribute.getVisibility(), true, false, false, oAttribute.getTypeDesc(),
						oAttribute.getInitialisation(), oAttribute.getDefaultValue(), true, oAttribute.getLength(),
						oAttribute.getPrecision(), oAttribute.getScale(), false, false, null,
						oAttribute.getDocumentation());
				r_listAttributes.add(oNewAttribute);
			} else {
				MessageHandler.getInstance().addError(
					"La classe de jointure '{}' ne peut avoir une clé primaire de profondeur > 1 ( entre la classe '{}' et '{}'",
						p_sJoinclassName, p_oClass.getUmlName(), p_sOppositeClassUmlName);
			}
		}
		return r_listAttributes;
	}
}
