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
package com.a2a.adjava.uml2xmodele.extractors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.optionsetters.OptionSetter;
import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.types.ITypeDescription.Property;
import com.a2a.adjava.uml.UmlAssociation;
import com.a2a.adjava.uml.UmlAssociationClass;
import com.a2a.adjava.uml.UmlAssociationEnd;
import com.a2a.adjava.uml.UmlAttribute;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.uml.UmlOperation;
import com.a2a.adjava.uml.UmlStereotype;
import com.a2a.adjava.uml2xmodele.assoconvert.AssociationClassEndConverter;
import com.a2a.adjava.uml2xmodele.assoconvert.AssociationEndConverter;
import com.a2a.adjava.uml2xmodele.attrconvert.UmlAttributeOptionParser;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MAssociation;
import com.a2a.adjava.xmodele.MAssociationManyToMany;
import com.a2a.adjava.xmodele.MAssociationManyToOne;
import com.a2a.adjava.xmodele.MAssociationOneToOne;
import com.a2a.adjava.xmodele.MAssociationPersistableManyToMany;
import com.a2a.adjava.xmodele.MAttribute;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MEntityInterface;
import com.a2a.adjava.xmodele.MEnumeration;
import com.a2a.adjava.xmodele.MIdentifierElem;
import com.a2a.adjava.xmodele.MJoinEntityImpl;
import com.a2a.adjava.xmodele.MMethodParameter;
import com.a2a.adjava.xmodele.MMethodSignature;
import com.a2a.adjava.xmodele.MPackage;
import com.a2a.adjava.xmodele.MStereotype;

/**
 * <p>
 * PojoExtractor
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

public class PojoExtractor extends AbstractExtractor<IDomain<IModelDictionary,IModelFactory>> {

	/** Logger */
	private static final Logger log = LoggerFactory.getLogger(PojoExtractor.class);

	/** Maximum length of a class name */
	private static final int MAX_CLASS_NAME_LENGTH = 25;

	/** Screen Extractor Context */
	private PojoContext pojoContext;
	
	/** Attribute option setters */
	private List<OptionSetter<Object>> attrOptionSetters = new ArrayList<OptionSetter<Object>>();
	
	/** Association option setters */
	private List<OptionSetter<Object>> assoOptionSetters = new ArrayList<OptionSetter<Object>>();

	@Override
	public void initialize(Element p_xConfig) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		List<String> listStereotypes = new ArrayList<String>();
		String sStereotypes = getParameters().getValue("stereotypes");
		for (String sStereotype : sStereotypes.split(",")) {
			sStereotype = sStereotype.trim();
			listStereotypes.add(sStereotype);
		}

		if (p_xConfig != null) {
			readAssoOptionSetters(p_xConfig);
			readAttrOptionSetters(p_xConfig);
		}

		List<String> listTranscientStereotypes = new ArrayList<String>();
		String sTranscientStereotypes = getParameters().getValue("transient-stereotypes");
		if ( sTranscientStereotypes != null ) {
			for (String sStereotype : sTranscientStereotypes.split(",")) {
				sStereotype = sStereotype.trim();
				listTranscientStereotypes.add(sStereotype);
			}
		}

		List<String> listApplicationScopeStereotypes = new ArrayList<String>();
		String sApplicationScopeStereotypes = getParameters().getValue("applicationScope-stereotypes");
		if ( sApplicationScopeStereotypes != null ) {
			for (String sStereotype : sApplicationScopeStereotypes.split(",")) {
				sStereotype = sStereotype.trim();
				listApplicationScopeStereotypes.add(sStereotype);
			}
		}

		List<String> listCustomizableStereotypes = new ArrayList<String>();
		String sCustomizableStereotypes = this.getParameters().getValue("customizable-stereotypes");
		if ( sCustomizableStereotypes != null ) {
			for (String sStereotype : sCustomizableStereotypes.split(",")) {
				sStereotype = sStereotype.trim();
				listCustomizableStereotypes.add(sStereotype);
			}
		}

		this.pojoContext = new PojoContext(listStereotypes, listTranscientStereotypes,
				listApplicationScopeStereotypes, listCustomizableStereotypes, getDomain());
	}

	/**
	 * Read association attribute setter
	 * @param p_xRoot xml element
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws Exception
	 */
	private void readAssoOptionSetters(Element p_xRoot) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Element xAssoOptionSetters = p_xRoot.element("association-option-setters");
		if (xAssoOptionSetters != null) {

			String sReplace = xAssoOptionSetters.attributeValue("replace");
			if (sReplace != null && Boolean.parseBoolean(sReplace)) {
				this.assoOptionSetters.clear();
			}

			List<OptionSetter<Object>> listAssoOptionSetters = new ArrayList<OptionSetter<Object>>();
			readOptionSetters(xAssoOptionSetters, listAssoOptionSetters);
			this.assoOptionSetters.addAll(listAssoOptionSetters);
		}
	}

	/**
	 * Read attribute option setters
	 * @param p_xRoot xml element
	 * @throws ClassNotFoundException a class was not found
	 * @throws IllegalAccessException an illegal access occured
	 * @throws InstantiationException an instanciation could not be performed 
	 */
	private void readAttrOptionSetters(Element p_xRoot) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Element xAttrOptionSetters = p_xRoot.element("attribute-option-setters");
		if (xAttrOptionSetters != null) {

			String sReplace = xAttrOptionSetters.attributeValue("replace");
			if (sReplace != null && Boolean.parseBoolean(sReplace)) {
				this.attrOptionSetters.clear();
			}

			List<OptionSetter<Object>> listAttrOptionSetters = new ArrayList<OptionSetter<Object>>();
			readOptionSetters(xAttrOptionSetters, listAttrOptionSetters);
			this.attrOptionSetters.addAll(listAttrOptionSetters);
		}
	}

	/**
	 * Lit les noeud option-setter du noeud passe en parametre
	 * @param p_xParent noeud parent
	 * @param p_listOptionSetters la liste des OptionSetter a alimenter
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@SuppressWarnings("unchecked")
	private void readOptionSetters(Element p_xParent, List<OptionSetter<Object>> p_listOptionSetters) 
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		for (Element xAttrOptionSetter : (List<Element>) p_xParent.elements("option-setter")) {
			String sGenClass = xAttrOptionSetter.attributeValue("class");
			OptionSetter<Object> oOptionSetter = (OptionSetter<Object>) Class.forName(sGenClass).newInstance();
			Map<String, String> oParametersMap = new HashMap<String, String>();
			for (Element xProperty : (List<Element>) xAttrOptionSetter.elements("property")) {
				String sName = xProperty.attributeValue("name");
				String sValue = xProperty.attributeValue("value");
				oParametersMap.put(sName, sValue);
			}
			oOptionSetter.setParametersMap(oParametersMap);
			p_listOptionSetters.add(oOptionSetter);
		}
	}

	/**
	 * Returns the setter for attributes option
	 * @return the setter for attributes option
	 */
	public List<OptionSetter<Object>> getAttrOptionSetters() {
		return attrOptionSetters;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.extractors.MExtractor#extract(com.a2a.adjava.uml.UmlModel)
	 */
	@Override
	public void extract(UmlModel p_oModele) throws Exception {

		log.debug("> PojoExtractor.extract");
		
		UmlDictionary oUmlDict = p_oModele.getDictionnary();

		// Treat expandable attributes
		((ExpandableTypeProcessor) this.getDomain().getAnalyserAndProcessorFactory().createExpandableTypesProcessor()).expandAttributes( p_oModele, this.pojoContext );
		
		// Convert uml classes to MClass
		for (UmlClass oUmlClass : oUmlDict.getAllClasses()) {
			if ( this.pojoContext.isEntity(oUmlClass)) {
				log.debug("conversion de la classe: {}", oUmlClass.getFullName());
				MEntityImpl oClass = this.convertUmlClass(oUmlClass);
				this.getDomain().getDictionnary().mapUmlClassToMClass(oUmlClass.getFullName(), oClass);
				this.extractInterfaceFromClass(oClass);
			}
		}

		// Convert associations
		AssociationEndConverter oAssociationEndConverter = AssociationEndConverter.getInstance();
		for (UmlAssociation oUmlAssociation : oUmlDict.getAssociations()) {
			UmlAssociationEnd oEnd1 = oUmlAssociation.getAssociationEnd1();
			UmlAssociationEnd oEnd2 = oUmlAssociation.getAssociationEnd2();
		
			if (this.pojoContext.isEntity(oEnd1.getRefClass()) && this.pojoContext.isEntity(oEnd2.getRefClass())
					&& this.checkAssociationEnd(oEnd1) && checkAssociationEnd(oEnd2)) {

				log.debug("conversion de l'association name : {}", oUmlAssociation.getName());
				log.debug("conversion de l'association end1 : {}", oUmlAssociation.getAssociationEnd1().getName());
				log.debug("conversion de l'association end2 : {}", oUmlAssociation.getAssociationEnd2().getName());
				
				// Convert association ends
				boolean bEnd1OwnerRelation = oEnd1.isNavigable();
	
				// Relation owner must be converted first
				if (bEnd1OwnerRelation) {
					oAssociationEndConverter.convertAssociationEnd(oEnd1, oEnd2, bEnd1OwnerRelation,
							oUmlAssociation, getDomain(), this.assoOptionSetters);
	
					oAssociationEndConverter.convertAssociationEnd(oEnd2, oEnd1, false, oUmlAssociation,
							getDomain(), this.assoOptionSetters);
				} else {
					oAssociationEndConverter.convertAssociationEnd(oEnd2, oEnd1, true, oUmlAssociation,
							getDomain(), this.assoOptionSetters);
	
					oAssociationEndConverter.convertAssociationEnd(oEnd1, oEnd2, false, oUmlAssociation,
							getDomain(), this.assoOptionSetters);
				}
			}
		}

		// Convert association classes
		AssociationClassEndConverter oAssoClassEndConverter = AssociationClassEndConverter.getInstance();
		for (UmlAssociationClass oUmlAssociationClass : oUmlDict.getAssociationClasses()) {

			if (this.pojoContext.isEntity(oUmlAssociationClass)) {

				UmlAssociationEnd oEnd1 = oUmlAssociationClass.getAssociationEnd1();
				UmlAssociationEnd oEnd2 = oUmlAssociationClass.getAssociationEnd2();

				if ( this.checkAssociationEnd(oEnd1) && checkAssociationEnd(oEnd2)) {
				
					controlStereotypesFromModelClasses(oEnd1,oEnd2);
					// the three classes must be entities
					if (this.pojoContext.isEntity(oEnd1.getRefClass()) && this.pojoContext.isEntity(oEnd2.getRefClass())) {
	
						MEntityImpl oMClass = getDomain().getDictionnary().getMapUmlClassToMClasses()
								.get(oUmlAssociationClass.getFullName());
						oAssoClassEndConverter.convertAssociationClassEnd(oMClass, oEnd1, oEnd2, oUmlAssociationClass, true,
								getDomain().getDictionnary().getMapUmlClassToMClasses(), this.assoOptionSetters, getDomain());
						oAssoClassEndConverter.convertAssociationClassEnd(oMClass, oEnd2, oEnd1, oUmlAssociationClass, false,
								getDomain().getDictionnary().getMapUmlClassToMClasses(), this.assoOptionSetters, getDomain());
	
					}
				}
			}
		}

		// For classes, add accessors from associations
		for (MEntityImpl oMClass : getDomain().getDictionnary().getAllEntities()) {
			createAccessorsFromAssociations(oMClass);
		}

		// For join classes, add accessors from associations
		for (MJoinEntityImpl oMClass : getDomain().getDictionnary().getAllJoinClasses()) {
			createAccessorsFromAssociations(oMClass);
		}

		// Extract interfaces from join classes
		for (MJoinEntityImpl oMJoinClass : getDomain().getDictionnary().getAllJoinClasses()) {
			extractInterfaceFromClass(oMJoinClass);
		}

		// Add missing operations
		addNeededMethods(oUmlDict);

		// Verification des identifiers
		for (MEntityImpl oClass : getDomain().getDictionnary().getAllEntities()) {
			if (oClass.getIdentifier().isEmpty()) {
				MessageHandler.getInstance().addError("Class has no identifier: {}", oClass.getUmlName());
			}
		}
		
		log.debug("< PojoExtractor.extract");
	}

	
	/**
	 * Control that all classes from the model part of the UML possess a Mm_Model stereotype.
	 * @param p_oAsso1 first association
	 * @param p_oAsso2 second association
	 */
	private void controlStereotypesFromModelClasses( UmlAssociationEnd p_oAsso1, UmlAssociationEnd p_oAsso2 ) {
		PojoExtractor oPojoExtractor = getDomain().getExtractor(PojoExtractor.class);
		List<String> p_PojoStereotype =  oPojoExtractor.getPojoContext().getEntityStereotypes();
		if(p_oAsso1.getRefClass().hasAnyStereotype( p_PojoStereotype) && !p_oAsso2.getRefClass().hasAnyStereotype(p_PojoStereotype) 
			|| p_oAsso2.getRefClass().hasAnyStereotype(p_PojoStereotype) && !p_oAsso1.getRefClass().hasAnyStereotype(p_PojoStereotype)) {
			MessageHandler.getInstance().addError("The classes \"{}\" et \"{}\" must both have a stereotype in \"{}\"", p_oAsso1.getRefClass().getName() ,p_oAsso2.getRefClass().getName(), p_PojoStereotype  );
		}
	}
	/**
	 * Convert UmlClass to MClass
	 * @param p_oClass UmlClass to convert
	 * @return MClass
	 * @throws AdjavaException 
	 * @throws Exception
	 */
	protected MEntityImpl convertUmlClass(UmlClass p_oClass) throws AdjavaException {

		MEntityImpl r_oClass = null;

		if (checkEntity(p_oClass)) {

			// Compute class name
			String sName = p_oClass.getName();

			// Delete underscore in class name, and generate a warning
			int iPos = sName.indexOf('_');
			while (iPos != -1) {
				sName = sName.substring(0, iPos) + sName.substring(iPos + 1, iPos + 2).toUpperCase()
						+ sName.substring(iPos + 2);
				iPos = sName.indexOf('_');
			}
			String sEntityName = sName;

			StringBuilder oEntityImplName = new StringBuilder();
			if ( this.getLngConfiguration().getImplNamingPrefix() != null ) {
				oEntityImplName.append(this.getLngConfiguration().getImplNamingPrefix());
			}
			oEntityImplName.append(sName);
			if ( this.getLngConfiguration().getImplNamingSuffix() != null ) {
				oEntityImplName.append(this.getLngConfiguration().getImplNamingSuffix());
			}
			sName = oEntityImplName.toString();
			
			log.debug("  name = {}", sName);

			String sPackageName = getDomain().getStrSubstitutor()
					.replace(p_oClass.getPackage().getFullName());

			// Determine le package d'implementation
			MPackage oPackageBase = getDomain().getDictionnary().getPackage(sPackageName);
			MPackage oPackageImpl = oPackageBase;

			if (this.getLngConfiguration().getImplSubPackageName() != null
					&& this.getLngConfiguration().getImplSubPackageName().length() > 0) {
				oPackageImpl = oPackageBase.getChildPackage(this.getLngConfiguration()
						.getImplSubPackageName());
				if (oPackageImpl == null) {
					oPackageImpl = new MPackage(this.getLngConfiguration().getImplSubPackageName(),
							oPackageBase);
					oPackageBase.addPackage(oPackageImpl);
				}
			}
			log.debug("  package impl: {}", oPackageImpl.getFullName());

			// Instanciation de la classe
			r_oClass = this.getDomain().getXModeleFactory().createMEntity(sName, oPackageImpl, p_oClass.getName(), sEntityName);

			// Convert attributes
			for (UmlAttribute oUmlAttribute : p_oClass.getAttributes()) {
				MAttribute oAttr = convertAttribute(oUmlAttribute);
				
				// Attribute validation
				if (this.validateAttribute(oAttr, oUmlAttribute, p_oClass)) {
					r_oClass.addAttribute(oAttr);
				}
			}

			// Stereotype conversion
			for (UmlStereotype oUmlStereotype : p_oClass.getStereotypes()) {
				r_oClass.addStereotype(new MStereotype(oUmlStereotype.getName(), oUmlStereotype
						.getDocumentation()));
			}
			
			r_oClass.setTransient(this.pojoContext.isTransient(p_oClass));
			r_oClass.setCustomizable(this.pojoContext.isCustomizable(p_oClass));
			r_oClass.setScope(this.pojoContext.getBeanScope(p_oClass));

			r_oClass.setDocumentation(p_oClass.getDocumentation());

			getDomain().getDictionnary().registerClass(r_oClass);
		}

		return r_oClass;
	}
	
	/**
	 * Validate the given attribute
	 * @param p_oAttribute the attribute to validate
	 * @param p_oUmlAttribute the original UML attribute 
	 * @param p_oClass the UmlClass to convert 
	 * @return true if validated
	 */
	private boolean validateAttribute(MAttribute p_oAttribute, UmlAttribute p_oUmlAttribute, UmlClass p_oClass) {
		boolean r_bValidated = true;
		
		if ( !p_oAttribute.isBasic() && p_oAttribute.isPartOfIdentifier()) {
			MessageHandler.getInstance().addError(
				"Composite attributes are forbidden as identifier of a class. Class: '{}', attribute: '{}'",
				p_oClass.getName(), p_oUmlAttribute.getName());
			r_bValidated = false;
		}
		
		if (p_oAttribute.isPartOfIdentifier() 
			&& !"long".equals(p_oAttribute.getTypeDesc().getUmlName()) 
			&& !"Long".equals(p_oAttribute.getTypeDesc().getUmlName())) {
			MessageHandler.getInstance().addError("Type of class identifiers must be long or Long. Class: '{}'', attribute: '{}', type: '{}'",
				p_oClass.getName(), p_oUmlAttribute.getName(), p_oAttribute.getTypeDesc().getUmlName());
			r_bValidated = false;
		}
		return r_bValidated;
	}

	/**
	 * Check UmlClass for entity conversion
	 * @param p_oUmlClass uml class
	 * @return true if UmlClass can be converted to Entity
	 * @throws AdjavaException the exception
	 */
	public boolean checkEntity(UmlClass p_oUmlClass) throws AdjavaException {
		boolean r_bOk = true;

		if (p_oUmlClass.getName().length() > MAX_CLASS_NAME_LENGTH) {
			MessageHandler.getInstance().addError(
					"Class name length must be less than 26 characters : {}", p_oUmlClass.getName());
		}
		
		if (p_oUmlClass.getName().indexOf('_') != -1) {
			MessageHandler.getInstance().addWarning(
					"Character '_' is not allowed in entity class name : {}", p_oUmlClass.getName());
		}

		if (p_oUmlClass.getAttributes().isEmpty()) {
			MessageHandler.getInstance().addError(
					"Entity must have at least one attribute : {}", p_oUmlClass.getName());
			r_bOk = false;
		}
		
		for (UmlAttribute oAttr : p_oUmlClass.getAttributes()) {
			if (oAttr.getDataType() == null) {
				MessageHandler.getInstance().addError(
						"Class attribute has no type : {}.{}", p_oUmlClass.getName(), oAttr.getName());
				r_bOk = false;
			}
			else {
				if (getDomain().getDictionnary().getTypeDescription(oAttr.getDataType().getName()) == null) {
					throw new AdjavaException("Impossible de trouver le type '{}' dans la configuration des types, pour l'attribut '{}' de la classe {}",
						oAttr.getDataType().getName(), oAttr.getName(), p_oUmlClass.getName());
				}
			}
		}

		return r_bOk;

	}

	/**
	 * Check entity association end validity
	 * @param p_oUmlAssociationEnd the association end to check
	 * @return true if the association is valid
	 */
	private boolean checkAssociationEnd( UmlAssociationEnd p_oUmlAssociationEnd ) {
		
		boolean r_bValid = true ;
			
		if ( p_oUmlAssociationEnd.isNavigable() && p_oUmlAssociationEnd.getVisibility() == null ) {
			MessageHandler.getInstance().addWarning(
				"[PojoExtractor] Visibility is mandatory on navigable association end: {}, target class: {}, source class: {}",
				p_oUmlAssociationEnd.getName(), p_oUmlAssociationEnd.getRefClass().getName(), p_oUmlAssociationEnd.getOppositeAssociationEnd().getRefClass().getName());
			p_oUmlAssociationEnd.setVisibility("private");
		}
		
		if (p_oUmlAssociationEnd.isNavigable() && StringUtils.isEmpty(p_oUmlAssociationEnd.getName())) {
			MessageHandler.getInstance().addError(
				"[PojoExtractor] Navigable association end must be named, target class : {} source class : {}", 	
					 p_oUmlAssociationEnd.getRefClass().getName(), 
					 p_oUmlAssociationEnd.getOppositeAssociationEnd().getRefClass().getName());
			r_bValid = false ;
		}
		
		if (p_oUmlAssociationEnd.getMultiplicityLower() == null || p_oUmlAssociationEnd.getMultiplicityUpper() == null ) {
			MessageHandler.getInstance().addError(
				"[PojoExtractor] Multiplicity is missing on association end: {}, target class: {}, source class: {}",
				p_oUmlAssociationEnd.getName(), p_oUmlAssociationEnd.getRefClass().getName(), p_oUmlAssociationEnd.getOppositeAssociationEnd().getName());
		}
		
		return r_bValid ;
	}
	
	/**
	 * Convert UmlAttribute to MAttribute
	 * @param p_oUmlAttribute Uml Attribute
	 * @return MAttribute the generated attribute
	 * @throws AdjavaException an exception occurred
	 */
	private MAttribute convertAttribute(UmlAttribute p_oUmlAttribute) throws AdjavaException {

		MAttribute r_oAttr = null;
		
		// test si fait partie de la cle primaire
		String sName = p_oUmlAttribute.getName();
		boolean bIsIdentifier = p_oUmlAttribute.isIdentifier();
		if (sName.charAt(0) == '@' ) {
			sName = sName.substring(1);
			bIsIdentifier = true;
		}

		log.debug("  conversion de l'attribut : {}, part of identifier: {}", sName, bIsIdentifier);

		ITypeDescription oTypeDesc = getDomain().getDictionnary().getTypeDescription(
				p_oUmlAttribute.getDataType().getName());

		r_oAttr = this.convertToAttribute(p_oUmlAttribute, sName, bIsIdentifier, oTypeDesc);

		return r_oAttr;
	}

	
	/**
	 * Convert an UML attribute to a model attribute
	 * @param p_oUmlAttribute the UML attribute to convert
	 * @param p_sAttributeName the model attribute name
	 * @param p_bIdentifier true if the attribute is an identifier
	 * @param p_oTypeDesc the type description of the attribute
	 * @return the model attribute
	 * @throws AdjavaException an exception occured
	 */
	private MAttribute convertToAttribute( UmlAttribute p_oUmlAttribute, String p_sAttributeName, boolean p_bIdentifier, ITypeDescription p_oTypeDesc ) 
			throws AdjavaException {
		MAttribute r_oAttr = null;
		
		// parse les options
		Map<String, ?> mapAttrOptions = UmlAttributeOptionParser.getInstance().parse(
				p_oUmlAttribute.getInitialValue(), p_oTypeDesc.getDefaultOptions(), p_oTypeDesc.getDataType());
		
		// Instanciation de l'attribut
		r_oAttr = getDomain().getXModeleFactory().createMAttribute(p_sAttributeName, p_oUmlAttribute.getVisibility(),
				p_bIdentifier, p_oUmlAttribute.isDerived(), p_oUmlAttribute.isTransient(), p_oTypeDesc, p_oUmlAttribute.getDocumentation());

		// ici on envoie l'énumération correspondant à l'attribut dans le but d'ajouter ses valeurs dans le flux xml 
		if (p_oTypeDesc.isEnumeration()){
			MEnumeration oEnum = this.getDomain().getDictionnary().getMEnumeration(p_oTypeDesc.getName());
			r_oAttr.setMEnumeration(oEnum);
		}
		
		for( Property oProperty : p_oTypeDesc.getProperties()) {
			convertPropertyToAttribute(oProperty, r_oAttr);
		}	
		
		for (OptionSetter<Object> oAttrOptionSetter : this.attrOptionSetters) {
			oAttrOptionSetter.applyOptions(mapAttrOptions, r_oAttr, getDomain().getLanguageConf());
		}
				
		return r_oAttr ;
	}
	
	/**
	 * Convert a property of a type description into an attribute. 
	 * @param p_oProperty property to convert
	 * @param p_oParentAttribute parent owning the newly created attribute
	 * @throws AdjavaException an exception occurred
	 */
	private void convertPropertyToAttribute( Property p_oProperty, MAttribute p_oParentAttribute )  throws AdjavaException {
		
		ITypeDescription oTypeDesc = (ITypeDescription) p_oProperty.getTypeDescription().clone();
		
		if ( p_oProperty.getDefaultOptions() != null ) {
			oTypeDesc.setDefaultOptions(p_oProperty.getDefaultOptions());	
		}
		if ( p_oProperty.getDefaultValue() != null ) {
			oTypeDesc.setDefaultValue(p_oProperty.getDefaultValue());
		}
		if ( p_oProperty.getSqlType() != null ) {
			oTypeDesc.setSqlType(p_oProperty.getSqlType());
		}
		// parse les options de la propriete du type pour les passer à l'attribut
		Map<String, ?> mapAttrOptions = UmlAttributeOptionParser.getInstance().parse(
			p_oProperty.getDefaultValue(), oTypeDesc.getDefaultOptions(), oTypeDesc.getDataType());
		
		// Instanciation de l'attribut
		MAttribute oAttr = getDomain().getXModeleFactory().createMAttribute(p_oProperty.getName(), "private",
			false, false, false, oTypeDesc, StringUtils.EMPTY );
		
		if (oTypeDesc.isEnumeration()){
			MPackage oEnumPackage = null;
			for( String sPackagePart : StringUtils.substringBeforeLast(oTypeDesc.getName(), ".").split("\\.")) {
				MPackage oPackage = new MPackage(sPackagePart, oEnumPackage);
				oEnumPackage = oPackage;
			}
			MEnumeration oEnum = new MEnumeration(oTypeDesc.getShortName(), oEnumPackage, oTypeDesc.getEnumValues(), oTypeDesc);
			oAttr.setMEnumeration(oEnum);
		}
		
		p_oParentAttribute.addProperty(oAttr);
		
		for( Property oProperty : oTypeDesc.getProperties()) {
			convertPropertyToAttribute(oProperty, oAttr);
		}
		
		for (OptionSetter<Object> oAttrOptionSetter : this.attrOptionSetters) {
			oAttrOptionSetter.applyOptions(mapAttrOptions, oAttr, getDomain().getLanguageConf());
		}
		
		// if attribute on uml model is optional, all nested properties must be optional too
		oAttr.setMandatory(p_oParentAttribute.isMandatory());
	}
	
	
	/**
	 * Create MInterface from MClass
	 * @param p_oMClass MClass from which to create MInterface
	 * @return MInterface of MClass
	 */
	protected MEntityInterface extractInterfaceFromClass(MEntityImpl p_oMClass) {

		// calcul le package de l'interface
		MPackage oBasePackage = p_oMClass.getPackage();
		if (getLngConfiguration().getImplSubPackageName() != null
				&& getLngConfiguration().getImplNamingSuffix().length() > 0) {
			oBasePackage = p_oMClass.getPackage().getParent();
		}

		MPackage oPackageInterface = oBasePackage;
		if (getLngConfiguration().getInterfaceSubPackageName() != null
				&& getLngConfiguration().getInterfaceSubPackageName().length() > 0) {
			oPackageInterface = oBasePackage.getChildPackage(getLngConfiguration()
					.getInterfaceSubPackageName());
			if (oPackageInterface == null) {
				oPackageInterface = new MPackage(getLngConfiguration().getInterfaceSubPackageName(),
						oBasePackage);
				oBasePackage.addPackage(oPackageInterface);
			}
		}

		// calcul le nom de l 'interface
		StringBuilder oInterfaceName = new StringBuilder();
		if ( this.getLngConfiguration().getInterfaceNamingPrefix() != null ) {
			oInterfaceName.append(this.getLngConfiguration().getInterfaceNamingPrefix());
		}
		oInterfaceName.append(p_oMClass.getEntityName());
		if ( this.getLngConfiguration().getInterfaceNamingSuffix() != null ) {
			oInterfaceName.append(this.getLngConfiguration().getInterfaceNamingSuffix());
		}

		MEntityInterface r_oInterface = new MEntityInterface(oInterfaceName.toString(), oPackageInterface);

		// cree les accesseurs a partir de la cle primaire
		for (MIdentifierElem oMIdentifierElem : p_oMClass.getIdentifier().getElems()) {
			if (oMIdentifierElem instanceof MAttribute) {
				createAccessorsFromAttribute((MAttribute) oMIdentifierElem, r_oInterface);
			}
		}

		for (MAttribute oAttribute : p_oMClass.getAttributes()) {
			createAccessorsFromAttribute(oAttribute, r_oInterface);
		}

		p_oMClass.setMasterInterface(r_oInterface);
		getDomain().getDictionnary().registerInterface(r_oInterface, p_oMClass);
		return r_oInterface;
	}

	/**
	 * Crée les accesseurs d'un attribut
	 * @param p_oAttribute l'attribut dont il faut creer les accesseurs
	 * @param p_oInterface interface sur laquelle on va ajouter les accesseurs crees
	 */
	protected void createAccessorsFromAttribute(MAttribute p_oAttribute, MEntityInterface p_oInterface) {

		// Crée les accesseurs à partir des attributs
		ITypeDescription oTypeDesc = p_oAttribute.getTypeDesc();
		MMethodSignature oGetMethod = new MMethodSignature(oTypeDesc.getGetAccessorPrefix()
				+ StringUtils.capitalize(p_oAttribute.getName()), "public", "get-accessor", oTypeDesc);

		MMethodSignature oSetMethod = new MMethodSignature(oTypeDesc.getSetAccessorPrefix()
				+ StringUtils.capitalize(p_oAttribute.getName()), "public", "set-accessor", null);
		MMethodParameter oParameter = new MMethodParameter(oTypeDesc.computeParameterName(p_oAttribute.getName()), oTypeDesc);
		oSetMethod.addParameter(oParameter);

		p_oInterface.addMethodSignature(oGetMethod);
		p_oInterface.addMethodSignature(oSetMethod);
	}

	/**
	 * Create getter/setter from associations of a MClass
	 * @param p_oMClass MClass
	 */
	private void createAccessorsFromAssociations(MEntityImpl p_oMClass) {
		for (MAssociation oAssociation : p_oMClass.getAssociations()) {
			createAccessorsFromAssociationEnd(oAssociation, p_oMClass.getMasterInterface());
		}
	}

	/**
	 * Create getter/setter on MInterface corresponding to an association
	 * @param p_oAssociation association to create getter/setter from
	 * @param p_oInterface interface where the getter/setter will be added.
	 */
	private void createAccessorsFromAssociationEnd(MAssociation p_oAssociation, MEntityInterface p_oInterface) {

		MMethodSignature oGetMethod = new MMethodSignature("get"
				+ StringUtils.capitalize(p_oAssociation.getName()), "public", "get-accessor",
				p_oAssociation.getTypeDesc());

		MMethodSignature oSetMethod = new MMethodSignature("set"
				+ StringUtils.capitalize(p_oAssociation.getName()), "public", "set-accessor", null);
		MMethodParameter oParameter = new MMethodParameter(p_oAssociation.getParameterName(),
				p_oAssociation.getTypeDesc());
		oSetMethod.addParameter(oParameter);

		p_oInterface.addMethodSignature(oGetMethod);
		p_oInterface.addMethodSignature(oSetMethod);
	}

	/**
	 * Adds Uml Operation to a dictionnary
	 * @param p_oUmlDictionnary the uml dictionnary to process
	 */
	private void addNeededMethods(UmlDictionary p_oUmlDictionnary) {

		// Ajout des methodes necessaires
		for (UmlClass oUmlClass : p_oUmlDictionnary.getAllClasses()) {
			if (this.pojoContext.isEntity(oUmlClass)) {
				MEntityImpl oClass = (MEntityImpl) getDomain().getDictionnary().getMapUmlClassToMClasses().get(oUmlClass.getFullName());
				
				if ( !oClass.isTransient()) {
				
					for (MAssociation oAssoc : oClass.getAssociations()) {
						if ( !oAssoc.isTransient() && !oAssoc.getRefClass().isTransient()) {
							if (oAssoc instanceof MAssociationManyToOne) {
								StringBuilder sOperationName = new StringBuilder("getList");
								sOperationName.append(oClass.getUmlName());
								sOperationName.append("_By_");
								sOperationName.append(oAssoc.getName());
								oUmlClass.addOperation(new UmlOperation(sOperationName.toString(), oUmlClass));
							}
							if (oAssoc instanceof MAssociationPersistableManyToMany) {
								MAssociationPersistableManyToMany oMAssociationManyToMany = (MAssociationPersistableManyToMany) oAssoc;
								StringBuilder sOperationName = new StringBuilder("getList");
								sOperationName.append(oClass.getUmlName());
								sOperationName.append("_By_");
								sOperationName.append(oMAssociationManyToMany.getNameForJoinClass());
								oUmlClass.addOperation(new UmlOperation(sOperationName.toString(), oUmlClass));
							}
							if (oAssoc instanceof MAssociationOneToOne && oAssoc.isRelationOwner()) {
								StringBuilder sOperationName = new StringBuilder("get");
								sOperationName.append(oClass.getUmlName());
								sOperationName.append("_By_");
								sOperationName.append(oAssoc.getName());
								oUmlClass.addOperation(new UmlOperation(sOperationName.toString(), oUmlClass));
							}
						}
					}
					for (MAssociation oAssoc : oClass.getNonNavigableAssociations()) {
						if ( !oAssoc.isTransient() && !oAssoc.getRefClass().isTransient() 
							&& oAssoc instanceof MAssociationManyToMany ) {
							MAssociationPersistableManyToMany oMAssociationManyToMany = (MAssociationPersistableManyToMany) oAssoc;
							StringBuilder sOperationName = new StringBuilder("getList");
							sOperationName.append(oClass.getUmlName());
							sOperationName.append("_By_");
							sOperationName.append(oMAssociationManyToMany.getNameForJoinClass());
							oUmlClass.addOperation(new UmlOperation(sOperationName.toString(), oUmlClass));
						}
					}
				}
			}
		}

		for (UmlAssociationClass oUmlAssociationClass : p_oUmlDictionnary.getAssociationClasses()) {
			if (this.pojoContext.isEntity(oUmlAssociationClass)) {
				MEntityImpl oClass = (MEntityImpl) getDomain().getDictionnary().getMapUmlClassToMClasses()
						.get(oUmlAssociationClass.getFullName());
				List<MAssociation> listAssos = new ArrayList<MAssociation>();
				listAssos.addAll(oClass.getAssociations());
				listAssos.addAll(oClass.getIdentifier().getElemOfTypeAssociation());

				for (MAssociation oAssoc : listAssos) {
					if (oAssoc instanceof MAssociationManyToOne) {
						MAssociationManyToOne oMAssociationManyToOne = (MAssociationManyToOne) oAssoc;
						StringBuilder sOperationName = new StringBuilder("getList");
						sOperationName.append(oClass.getUmlName());
						sOperationName.append("_By_");
						sOperationName.append(oMAssociationManyToOne.getName());
						oUmlAssociationClass.addOperation(new UmlOperation(sOperationName.toString(), oUmlAssociationClass));
					}
				}
			}
		}
	}

	/**
	 * Return pojo context
	 * @return the pojo context
	 */
	public PojoContext getPojoContext() {
		return this.pojoContext;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.uml2xmodele.extractors.AbstractExtractor#preValidate(com.a2a.adjava.uml.UmlModel)
	 */
	@Override
	public void preValidate(UmlModel p_oModele) throws Exception {
		UmlDictionary oUmlDict = p_oModele.getDictionnary();
		
		PojoExtractor oPojoExtractor = this.getDomain().getExtractor(PojoExtractor.class);
		
		List<String> listPojoStereotypes = new ArrayList<String>();
		listPojoStereotypes.addAll(oPojoExtractor.getPojoContext().getEntityStereotypes());
		listPojoStereotypes.addAll(oPojoExtractor.getPojoContext().getApplicationScopeStereotypes());
		listPojoStereotypes.addAll(oPojoExtractor.getPojoContext().getTranscientStereotypes());

		ViewModelExtractor oViewModelExtractor = this.getDomain().getExtractor(ViewModelExtractor.class);
		List<String> listViewModelStereotypes = null ;
		if ( oViewModelExtractor != null ) {
			listViewModelStereotypes = oViewModelExtractor.getListStereotypes();
		}
		
		ScreenExtractor oScreenExtractor = this.getDomain().getExtractor(ScreenExtractor.class);
		List<String> listScreenStereotypes = null ;
		if ( oScreenExtractor != null ) {
			listScreenStereotypes = oScreenExtractor.getScreenContext().getScreenStereotypes();
		}
		
		if (listScreenStereotypes != null || listViewModelStereotypes != null){
			for( String sEntityStereotype: listPojoStereotypes ) {
				this.getStereotypesValidator().addIncompatibility(sEntityStereotype, listViewModelStereotypes);
				this.getStereotypesValidator().addIncompatibility(sEntityStereotype,listScreenStereotypes);
			}
			if ( listViewModelStereotypes != null && listScreenStereotypes != null ){
				for( String sViewModelStereotype: listViewModelStereotypes ) {
					this.getStereotypesValidator().addIncompatibility(sViewModelStereotype, listScreenStereotypes);
				}
			}
		}		
		
		// verifie toutes les entités
		for (UmlClass oUmlClass : oUmlDict.getAllClasses()) {
			if ( this.pojoContext.isEntity(oUmlClass)) {
				this.getStereotypesValidator().verifyStereotypesOfObject(oUmlClass) ;
				// verifie l'existence du type des attributs
				for( UmlAttribute oUmlAttribute: oUmlClass.getAttributes()) {
					if ( oUmlAttribute.getDataType() == null ) {
						MessageHandler.getInstance().addError(
							"Attribute named '{}' of the class '{}' ({}) must have a data type", oUmlAttribute.getName(), oUmlClass.getName(), oUmlClass.getFullName());
					}
				}
			}
		}		
		// verifie les associations entre entités
		for (UmlAssociation oUmlAssociation : oUmlDict.getAssociations()) {
			UmlAssociationEnd oEnd1 = oUmlAssociation.getAssociationEnd1();
			UmlAssociationEnd oEnd2 = oUmlAssociation.getAssociationEnd2();
		
			if (this.pojoContext.isEntity(oEnd1.getRefClass()) && this.pojoContext.isEntity(oEnd2.getRefClass())
					&& this.checkAssociationEnd(oEnd1) && checkAssociationEnd(oEnd2)) {
				this.getStereotypesValidator().verifyStereotypesOfObject(oUmlAssociation) ;
			}
		}
		// verifie les classes d'associations définissant une entité
		for (UmlAssociationClass oUmlAssociationClass : oUmlDict.getAssociationClasses()) {
			if (this.pojoContext.isEntity(oUmlAssociationClass)) {
				this.getStereotypesValidator().verifyStereotypesOfObject(oUmlAssociationClass) ;
			}
		}
	}
}
