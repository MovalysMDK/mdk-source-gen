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
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.extractors.IExpandableTypeProcessor;
import com.a2a.adjava.optionsetters.DefaultAttrOptionSetter;
import com.a2a.adjava.types.ExpandableType;
import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.types.ITypeDescription.Property;
import com.a2a.adjava.uml.UmlAssociation;
import com.a2a.adjava.uml.UmlAssociationEnd;
import com.a2a.adjava.uml.UmlAssociationEnd.AggregateType;
import com.a2a.adjava.uml.UmlAttribute;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDataType;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.uml.UmlPackage;
import com.a2a.adjava.uml.UmlStereotype;
import com.a2a.adjava.uml2xmodele.attrconvert.UmlAttributeOptionParser;
import com.a2a.adjava.uml2xmodele.uml.UmlExpandedAssociationEnd;

/**
 * Processor used to handle the data types flagged as expendable. This processor modifies the UmlModel by adding the
 * entities and associations that are necessary for each kind of expendable type.
 */
public class ExpandableTypeProcessor implements IExpandableTypeProcessor {

	/**
	 * Logger instance
	 */
	private static final Logger log = LoggerFactory.getLogger(ExpandableTypeProcessor.class);

	public static final String EMBEDDED_STEREOTYPE = "Mm_embedded";

	/**
	 * Constructor
	 */
	public ExpandableTypeProcessor() {
	}

	/**
	 * Handle attributes with a datatype flagged as expendable
	 * @param p_oModele uml modele
	 * @param p_oContext context
	 */
	public void expandAttributes(UmlModel p_oModele, PojoContext p_oContext) throws Exception {
		List<UmlClass> listNewEntities = new ArrayList<UmlClass>();

		for (UmlClass oUmlClass : p_oModele.getDictionnary().getAllClasses()) {
			if (p_oContext.isEntity(oUmlClass)) {
				List<UmlAttribute> listAttrToRemove = new ArrayList<UmlAttribute>();
				for (UmlAttribute oUmlAttribute : oUmlClass.getAttributes()) {
					ITypeDescription oTypeDesc = p_oContext.getDomain().getDictionnary().getTypeDescription(
							oUmlAttribute.getDataType().getName());
					// Handle the ONE_TO_MANY and ONE_TO_ONE cases
					if (oTypeDesc != null && (oTypeDesc.getExpandableType().equals(ExpandableType.ONE_TO_MANY) ||
							oTypeDesc.getExpandableType().equals(ExpandableType.ONE_TO_ONE))) {
						// Create a new entity, with attributes parsing and association creation
						UmlClass oNewEntity = this.expandAttr(oUmlAttribute, oTypeDesc, oUmlClass, oTypeDesc.getExpandableType(), listNewEntities, p_oModele, p_oContext);

						listAttrToRemove.add(oUmlAttribute);
						listNewEntities.add(oNewEntity);
					}
					// Handle the EMBEDDED case
					else if (oTypeDesc != null && oTypeDesc.getExpandableType().equals(ExpandableType.EMBEDDED)) {

						String sNewEntityName = oUmlClass.getName() + WordUtils.capitalize(oUmlAttribute.getName());
						log.debug("New entity from embedded expandable attribute: {}", sNewEntityName);

						// Update the data type of the attribute with the newly created entity
						ITypeDescription oNewUmlType = (ITypeDescription) oTypeDesc.clone();
						String sPackage = oUmlClass.getPackage().getFullName();
						oNewUmlType.setName(sPackage + "." + sNewEntityName);
						oNewUmlType.setInitFormat("new " + sNewEntityName + "()");
						p_oContext.getDomain().getDictionnary().registerTypeDescription(oNewUmlType.getName(), oNewUmlType);
						UmlDataType oOldUmlDataType = oUmlAttribute.getDataType();
						oUmlAttribute.setDataType(new UmlDataType(oNewUmlType.getName()));

						// Create a new entity, with attributes parsing and without association creation
						UmlClass oNewEntity = this.expandAttrBegin(oUmlAttribute, oTypeDesc, sNewEntityName,
								oUmlClass.getPackage(), oTypeDesc.getExpandableType(), listNewEntities, p_oModele, p_oContext);
						// Add the Entity and Transient stereotypes. An Embedded data type is transient because it does
						// not have any database table, as it is stored directly inside the parent entity
						oNewEntity.addStereotype(new UmlStereotype(p_oContext.getEntityStereotypes().get(0), p_oContext.getEntityStereotypes().get(0)));
						oNewEntity.addStereotype(new UmlStereotype(p_oContext.getTranscientStereotypes().get(0), p_oContext.getTranscientStereotypes().get(0)));
						// Add the Embedded stereotype, with the following parameters:
						// 1: Initial data type : this is the data type initially set for the embedded data type.
						// 2: Full class name of the parent entity, in order to later be able to hit its datatable, which
						//    will contain the data of the new entity
						// 3: Name of the new entity attribute inside the parent entity : later used to generate the names
						//    of the table columns in the parent entity table related to our new entity.
						oNewEntity.addStereotype(new UmlStereotype(EMBEDDED_STEREOTYPE, EMBEDDED_STEREOTYPE),
								oOldUmlDataType,
								oUmlClass.getFullName(),
								oUmlAttribute.getName());
						// Add the entity to the list of entities to register
						listNewEntities.add(oNewEntity);
					}
				}
				oUmlClass.removeAttributes(listAttrToRemove);
			}
		}

		for (UmlClass oUmlEntity : listNewEntities) {
			p_oModele.getDictionnary().registerClass(
					Long.toString(oUmlEntity.getName().hashCode()), oUmlEntity);
		}
	}

	/**
	 * Expand an attribute by creating a new UmlClass to handle the attribute, and by creating the association
	 * corresponding to the expandable type.
	 * @param p_oUmlAttribute uml attribute to expand
	 * @param p_oTypeDesc type description
	 * @param p_oUmlClass uml class
	 * @param p_oExpandableType expandable type
	 * @param p_oModele model
	 * @param p_oContext pojo context
	 */
	protected UmlClass expandAttr(UmlAttribute p_oUmlAttribute, ITypeDescription p_oTypeDesc,
	                              UmlClass p_oUmlClass, ExpandableType p_oExpandableType, List<UmlClass> p_listNewEntities, UmlModel p_oModele, PojoContext p_oContext) throws Exception {

		String sNewEntityName = p_oUmlClass.getName() + WordUtils.capitalize(p_oUmlAttribute.getName());
		log.debug("new entity from expandable attribute: {}", sNewEntityName);

		UmlClass r_oNewUmlClass = expandAttrBegin(p_oUmlAttribute, p_oTypeDesc,
				sNewEntityName, p_oUmlClass.getPackage(), p_oExpandableType, p_listNewEntities, p_oModele, p_oContext);

		return expandAttrEnd(r_oNewUmlClass, p_oUmlAttribute, p_oTypeDesc,
				p_oUmlClass, p_oExpandableType, p_listNewEntities, p_oModele, p_oContext);
	}

	/**
	 * Create a new UmlClass in order to handle the expanded attribute.
	 * @param p_oUmlAttribute
	 * @param p_oTypeDesc
	 * @param p_sNewEntityName
	 * @param p_sPackage
	 * @param p_oExpandableType
	 * @param p_listNewEntities
	 * @param p_oModele
	 * @param p_oContext
	 * @return
	 * @throws Exception
	 */
	protected UmlClass expandAttrBegin(UmlAttribute p_oUmlAttribute, ITypeDescription p_oTypeDesc,
	                                   String p_sNewEntityName, UmlPackage p_sPackage, ExpandableType p_oExpandableType, List<UmlClass> p_listNewEntities, UmlModel p_oModele, PojoContext p_oContext) throws Exception {

		log.debug("new entity from expandable attribute: {}", p_sNewEntityName);

		UmlClass r_oNewUmlClass = new UmlClass();
		r_oNewUmlClass.init(p_sNewEntityName, p_sPackage);

		// permet de différencier dans le pojo les classes issues des types externalisées
		UmlStereotype oUmlExpandableAttrStereotype = new UmlStereotype("adjava_classFromExpandableAttributeStereotype", "adjava_classFromExpandableAttributeStereotype");
		oUmlExpandableAttrStereotype.setDocumentation(p_oExpandableType.toString());
		r_oNewUmlClass.addStereotype(oUmlExpandableAttrStereotype);

		UmlDictionary oDictionnary = p_oModele.getDictionnary();

		// Create attributes in entity from the properties defined in the type. 
		for (Property oProperty : p_oTypeDesc.getProperties()) {
			UmlDataType oDataType = oDictionnary.getDataTypeByName(oProperty.getTypeName());
			if (oDataType == null) {
				oDataType = new UmlDataType(oProperty.getTypeName());
				log.debug("  create datatype: {}", oDataType.getName());
				oDictionnary.addDataType(
						Integer.toString(oDataType.getName().hashCode()),
						oDataType);
				p_oContext.getDomain().getExtractor(DataTypeExtractor.class)
						.convertUmlDataType(oDataType);
			}

			//Reconstruction de la chaine contenant la valeur par d��faut et les options
			StringBuilder initialValue = new StringBuilder();
			String value = null;

			if (oProperty.getDefaultValue() != null) {
				initialValue.append(oProperty.getDefaultValue());
			}

			if (oProperty.getDefaultOptions() != null) {
				initialValue.append(oProperty.getDefaultOptions());
			}

			if (!initialValue.toString().equals("")) {
				value = initialValue.toString();
			}

			UmlAttribute oUmlAttribute = new UmlAttribute(oProperty.getName(),
					r_oNewUmlClass, "private", oDataType,
					value, StringUtils.EMPTY);
			ITypeDescription oTypeDesc = p_oContext.getDomain()
					.getDictionnary()
					.getTypeDescription(oUmlAttribute.getDataType().getName());
			if (oTypeDesc != null
					&& (oTypeDesc.getExpandableType().equals(
					ExpandableType.ONE_TO_MANY) || oTypeDesc
					.getExpandableType().equals(
							ExpandableType.ONE_TO_ONE))) {
				UmlClass oNewEntity = this.expandAttr(oUmlAttribute, oTypeDesc,
						r_oNewUmlClass, oTypeDesc.getExpandableType(),
						p_listNewEntities, p_oModele, p_oContext);
				p_listNewEntities.add(oNewEntity);
			} else {
				r_oNewUmlClass.addAttribute(oUmlAttribute);
				log.debug("  add attribute: {}, type: {}", oUmlAttribute
						.getName(), oUmlAttribute.getDataType().getName());
			}
		}
		return r_oNewUmlClass;
	}

	/**
	 * Create the association to handle the expanded attribute. This can be either a ONE_TO_ONE or a ONT_TO_MANY
	 * association.
	 * @param p_oUmlResultClass
	 * @param p_oUmlAttribute
	 * @param p_oTypeDesc
	 * @param p_oUmlClass
	 * @param p_oExpandableType
	 * @param p_listNewEntities
	 * @param p_oModele
	 * @param p_oContext
	 * @return
	 * @throws Exception
	 */
	protected UmlClass expandAttrEnd(UmlClass p_oUmlResultClass, UmlAttribute p_oUmlAttribute, ITypeDescription p_oTypeDesc,
	                                 UmlClass p_oUmlClass, ExpandableType p_oExpandableType, List<UmlClass> p_listNewEntities, UmlModel p_oModele, PojoContext p_oContext) throws Exception {

		UmlAssociationEnd oUmlAssociationEnd1 = null;
		UmlAssociationEnd oUmlAssociationEnd2 = null;
		if (p_oExpandableType.equals(ExpandableType.ONE_TO_MANY)) {

			Map<String, ?> mapAttrOptions = UmlAttributeOptionParser.getInstance().parse(
					p_oUmlAttribute.getInitialValue(), p_oTypeDesc.getDefaultOptions(), p_oTypeDesc.getDataType());
			int iLowerMultiplicity = 1;
			if (mapAttrOptions.containsKey(DefaultAttrOptionSetter.Option.OPTIONAL.getUmlCode())) {
				iLowerMultiplicity = 0;
			}

			// Asso end one-to-many to the new entity
			oUmlAssociationEnd1 = new UmlAssociationEnd(
					p_oUmlAttribute.getName(), "private", false, true, iLowerMultiplicity, -1, StringUtils.EMPTY, AggregateType.NONE);
			oUmlAssociationEnd1.setRefClass(p_oUmlResultClass);
			log.debug("  add one to many association end: {} to {}", oUmlAssociationEnd1.getName(), p_oUmlClass.getName());

			// Asso end many-to-one to the model entity
			oUmlAssociationEnd2 = new UmlAssociationEnd(
					p_oUmlAttribute.getName() + WordUtils.capitalize(p_oUmlClass.getName()),
					"private", false, true, 1, 1, StringUtils.EMPTY, AggregateType.COMPOSITE);
			oUmlAssociationEnd2.setRefClass(p_oUmlClass);
			log.debug("  add many to one association end: {} to {}", oUmlAssociationEnd2.getName(), p_oUmlResultClass.getName());
		} else if (p_oExpandableType.equals(ExpandableType.ONE_TO_ONE)) {

			Map<String, ?> mapAttrOptions = UmlAttributeOptionParser.getInstance().parse(
					p_oUmlAttribute.getInitialValue(), p_oTypeDesc.getDefaultOptions(), p_oTypeDesc.getDataType());
			int iLowerMultiplicity = 1;
			if (mapAttrOptions.containsKey(DefaultAttrOptionSetter.Option.OPTIONAL.getUmlCode())) {
				iLowerMultiplicity = 0;
			}

			UmlExpandedAssociationEnd oExpandedUmlAssoEnd = new UmlExpandedAssociationEnd(
					p_oUmlAttribute.getName(), "private", false, true, iLowerMultiplicity, 1, StringUtils.EMPTY, AggregateType.NONE);
			oExpandedUmlAssoEnd.setExpandableType(ExpandableType.ONE_TO_ONE);
			oExpandedUmlAssoEnd.setExpandableTypeDesc(p_oTypeDesc);

			oUmlAssociationEnd1 = oExpandedUmlAssoEnd;
			oUmlAssociationEnd1.setRefClass(p_oUmlResultClass);
			log.debug("  add one to one association end: {} to {}", oUmlAssociationEnd1.getName(), p_oUmlClass.getName());

			// Asso end one-to-one to the model entity
			oUmlAssociationEnd2 = new UmlAssociationEnd(
					p_oUmlAttribute.getName() + WordUtils.capitalize(p_oUmlClass.getName()),
					"private", false, true, 1, 1, StringUtils.EMPTY, AggregateType.COMPOSITE);
			oUmlAssociationEnd2.setRefClass(p_oUmlClass);
			log.debug("  add one to one association end: {} to {}", oUmlAssociationEnd2.getName(), p_oUmlResultClass.getName());
		}


		// Add the entity stereotype
		UmlStereotype oEntityStereotype = p_oModele.getDictionnary().getStereotypeByName(p_oContext.getEntityStereotypes().get(0));
		p_oUmlResultClass.addStereotype(oEntityStereotype);
		log.debug("  add stereotype: {} to {} (from config)", oEntityStereotype.getName(), p_oUmlResultClass.getName());

		// Compute stereotype prefix for domain (ex: Mm_, Bo_ )
		String sStereotypePrefix = oEntityStereotype.getName().split("_")[0] + "_";

		// Copy the stereotypes from type description to the new entity
		for (String sStereotypeFromType : p_oTypeDesc.getStereotypes()) {
			String sStereotypeName = sStereotypePrefix + sStereotypeFromType;
			UmlStereotype oUmlStereotype = p_oModele.getDictionnary().getStereotypeByName(sStereotypeName);
			if (oUmlStereotype == null) {
				String sId = Long.toString(sStereotypeName.hashCode());
				oUmlStereotype = new UmlStereotype(sId, sStereotypeName);
				p_oModele.getDictionnary().registerStereotype(sId, oUmlStereotype);
			}
			p_oUmlResultClass.addStereotype(oUmlStereotype);
			log.debug("  add stereotype: {} (from type desc) to {}", oEntityStereotype.getName(), p_oUmlResultClass.getName());
		}

		// Copy the stereotypes from the uml expandable attribute to the new entity
		for (UmlStereotype oStereotypeFromAttr : p_oUmlAttribute.getStereotypes()) {
			p_oUmlResultClass.addStereotype(oStereotypeFromAttr);
			log.debug("  add stereotype: {} to {} (from uml attribute)", oStereotypeFromAttr.getName(), p_oUmlResultClass.getName());
		}

		UmlAssociation oUmlAssociation = new UmlAssociation(null, oUmlAssociationEnd1, oUmlAssociationEnd2, null);
		p_oModele.getDictionnary().registerAssociation(Integer.toString(oUmlAssociation.hashCode()), oUmlAssociation);

		p_oUmlClass.addAssociation(oUmlAssociationEnd1);
		p_oUmlResultClass.addAssociation(oUmlAssociationEnd2);
		p_oUmlClass.getPackage().addClass(p_oUmlResultClass);

		return p_oUmlResultClass;
	}
}
