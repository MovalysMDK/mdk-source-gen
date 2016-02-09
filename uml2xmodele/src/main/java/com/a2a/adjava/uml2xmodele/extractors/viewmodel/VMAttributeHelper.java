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
package com.a2a.adjava.uml2xmodele.extractors.viewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.languages.LanguageConfiguration;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.optionsetters.DefaultAttrOptionSetter;
import com.a2a.adjava.optionsetters.DefaultAttrOptionSetter.Option;
import com.a2a.adjava.optionsetters.OptionSetter;
import com.a2a.adjava.types.ITypeConvertion;
import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.types.IUITypeDescription;
import com.a2a.adjava.uml.UmlAttribute;
import com.a2a.adjava.uml2xmodele.attrconvert.UmlAttributeOptionParser;
import com.a2a.adjava.uml2xmodele.extractors.PojoExtractor;
import com.a2a.adjava.utils.VersionHandler;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MAssociation;
import com.a2a.adjava.xmodele.MAssociationOneToOne;
import com.a2a.adjava.xmodele.MAttribute;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MEnumeration;
import com.a2a.adjava.xmodele.MLabel;
import com.a2a.adjava.xmodele.MMethodParameter;
import com.a2a.adjava.xmodele.MMethodSignature;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.MVisualField;
import com.a2a.adjava.xmodele.ui.view.MVFLabelKind;
import com.a2a.adjava.xmodele.ui.view.MVFLocalization;
import com.a2a.adjava.xmodele.ui.view.MVFModifier;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;
import com.a2a.adjava.xmodele.ui.viewmodel.mappings.IVMMappingDesc;

/**
 * Helper for creating attributes in MViewModelImpl
 * @author lmichenaud
 * 
 */
public final class VMAttributeHelper {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(VMAttributeHelper.class);

	/**
	 * Singleton instance
	 */
	private static VMAttributeHelper instance = new VMAttributeHelper();

	/**
	 * Constructor
	 */
	private VMAttributeHelper() {
		// Empty constructor
	}

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	public static VMAttributeHelper getInstance() {
		return instance;
	}

	/**
	 * Add an id field to class
	 * @param p_oVmi the view model to update
	 * @param p_oFrom model entity
	 * @param p_sPrefix prefix
	 * @param p_oDomain domain
	 * @throws Exception exception
	 */
	public void addIdFieldToClass(MViewModelImpl p_oVmi, MEntityImpl p_oFrom, String p_sPrefix,
			IDomain<IModelDictionary, IModelFactory> p_oDomain) throws Exception {
		
		MAttribute oAttr = null;
		MMethodSignature oSig = null;

		// Add ids on viewmodel if not exist
		List<MAttribute> listAttrs = p_oFrom.getIdentifier().getElemOfTypeAttribute();
		if (!listAttrs.isEmpty()) {
			for (MAttribute oAttributeTarget : listAttrs) {
				Cags oCags = this.convertAttribute(p_sPrefix, "id_", oAttributeTarget, null, p_oVmi, true,
						false, true, p_oDomain);
				oAttr = oCags.attribute;
				if (!p_oVmi.hasAttribute(oAttr.getName())) {
					oSig = oCags.get;
					p_oVmi.getMasterInterface().addMethodSignature(oSig);
					oSig = oCags.set;
					p_oVmi.getMasterInterface().addMethodSignature(oSig);
					log.debug("--------------------------------------------->add attribute "
							+ oAttr.getName());
					p_oVmi.addAttribute(oAttr);
					ITypeDescription oTypeDesc = null;
					if (oAttributeTarget.getTypeDesc() != null) {
						oTypeDesc = oAttributeTarget.getTypeDesc();

					} else {
						oTypeDesc = p_oDomain.getDictionnary().getNoType();
					}
					log.debug("addFieldToClass : p_oVmi {} oAttr {} ", p_oVmi.getFullName() , oAttr.getName());

					p_oVmi.getMapping().addAttribute(p_oVmi, oAttributeTarget.getName(), oAttr.getName(),
							oAttr.getTypeDesc(), oTypeDesc, false, 
							oAttributeTarget.getTypeDesc().getUnsavedValue(), null, null, oAttributeTarget.getTypeDesc().isPrimitif());
				}
			}
		}
	}

	/**
	 * Convert a derived attribute
	 * @param p_sPrefix prefix
	 * @param p_oUmlAttribute uml attribute to convert
	 * @param p_oViewModel viewmodel to update
	 * @param p_bMasterReadonly is master readonly
	 * @param p_bTestIfExist test if exists
	 * @param p_oDomain domain
	 * @throws Exception exception
	 */
	public void convertDerivedAttribute(String p_sPrefix, UmlAttribute p_oUmlAttribute,
			MViewModelImpl p_oViewModel, boolean p_bMasterReadonly, boolean p_bTestIfExist, IDomain<IModelDictionary, IModelFactory> p_oDomain)
			throws Exception {

		// test si fait partie de la cle primaire
		String sTName = p_oUmlAttribute.getName();
		boolean bIsIdentifier = p_oUmlAttribute.isIdentifier();
		MAttribute oDumbAttr = null;

		if (sTName.charAt(0) == '@') {
			sTName = sTName.substring(1);
			bIsIdentifier = true;
		}
		if (p_bTestIfExist && p_oViewModel.hasAttribute(sTName)) {
			sTName = StringUtils.join(p_sPrefix, sTName);
		}

		log.debug("  conversion de l'attribut: {}, part of identifier: ", sTName, bIsIdentifier);

		IUITypeDescription oUiTd = p_oDomain.getLanguageConf().getUiTypeDescription(
				p_oUmlAttribute.getDataType().getName(), VersionHandler.getGenerationVersion().getStringVersion());
		if (oUiTd == null) {
			throw new AdjavaException("Can't find UI Type Description : {}", p_oUmlAttribute.getDataType().getName());
		}

		// parse les options
		Map<String, ?> mapAttrOptions = UmlAttributeOptionParser.getInstance().parse(
				p_oUmlAttribute.getInitialValue());

		// readonly
		boolean bReadOnly = false;

		ITypeDescription oVMTypeDescription = null;

		bReadOnly = p_bMasterReadonly
				|| mapAttrOptions.containsKey(DefaultAttrOptionSetter.Option.READ_ONLY.getUmlCode());
		if (bReadOnly) {
			log.debug("convert attribute readonly roviewmodeltype {}", oUiTd.getROViewModelType());
			oVMTypeDescription = p_oDomain.getLanguageConf().getTypeDescription(
					oUiTd.getROViewModelType());
			if ( oVMTypeDescription == null ) {
				throw new AdjavaException("Can't find TypeDescription '{}' referenced by ui type: '{}'",  oUiTd.getROViewModelType(), oUiTd.getUmlName());
			}
		} else {
			log.debug("convert attribute not readonly rwviewmodeltype {}", oUiTd.getRWViewModelType());
			oVMTypeDescription = p_oDomain.getLanguageConf().getTypeDescription(
					oUiTd.getRWViewModelType());
			if ( oVMTypeDescription == null ) {
				throw new AdjavaException("Can't find TypeDescription '{}' referenced by ui type: '{}'", oUiTd.getRWViewModelType(), oUiTd.getUmlName());
			}
		}
		
		ITypeDescription oVMTypeDescriptionClone = (ITypeDescription) oVMTypeDescription.clone();
		MEnumeration oInitAttrEnum = null;
		if (oUiTd.getUmlName().equals("EnumImage") || oUiTd.getUmlName().equals("EnumBackground")) {
			if (mapAttrOptions.containsKey(DefaultAttrOptionSetter.Option.COMPUTE_FIELD_TYPE.getUmlCode())) {
				String sEnumField = (String)mapAttrOptions.get(DefaultAttrOptionSetter.Option.COMPUTE_FIELD_TYPE.getUmlCode());
				if (sEnumField != null && !sEnumField.isEmpty()) {
					for( MEnumeration oEnumeration: p_oDomain.getDictionnary().getAllEnumerations()) {
						if (oEnumeration.getName().equals(sEnumField)) {
							oVMTypeDescriptionClone.setName(oEnumeration.getFullName());
							oInitAttrEnum = oEnumeration;
							break;
						}
					}
					if (oInitAttrEnum == null) {
						throw new AdjavaException("Can't find Enumeration for calculate field '{}' referenced by ui type: '{}'", p_oUmlAttribute.getName(), oUiTd.getUmlName());
					}
				} else {
					throw new AdjavaException("Can't define Enumeration for calculate field '{}' referenced by ui type: '{}'", p_oUmlAttribute.getName(), oUiTd.getUmlName());
				}
			} else {
				throw new AdjavaException("Can't find option _E for calculate field '{}' referenced by ui type: '{}'", p_oUmlAttribute.getName(), oUiTd.getUmlName());
			}
		}

		oDumbAttr = p_oDomain.getXModeleFactory().createMAttribute("dumb",
				p_oUmlAttribute.getVisibility(), bIsIdentifier, p_oUmlAttribute.isDerived(),
				p_oUmlAttribute.isTransient(), oVMTypeDescriptionClone, p_oUmlAttribute.getDocumentation());
		oDumbAttr.setMEnumeration(oInitAttrEnum);
		for (OptionSetter<Object> oSetter : p_oDomain.getExtractor(PojoExtractor.class)
				.getAttrOptionSetters()) {
			oSetter.applyOptions(mapAttrOptions, oDumbAttr, p_oDomain.getLanguageConf());
		}
		
		MLabel oLabel = p_oDomain.getXModeleFactory().createLabel(sTName, p_oViewModel);
		
		MVisualField oVisualField = p_oDomain.getXModeleFactory().createVisualField(
				sTName, oLabel,
				oUiTd,
				bReadOnly ? MVFModifier.READONLY: MVFModifier.WRITABLE,
				this.computeVFLabelKind(mapAttrOptions),
				oDumbAttr,
				p_oDomain,
				p_oViewModel.getEntityToUpdate() != null
					?  StringUtils.join(p_oViewModel.getEntityToUpdate().getMasterInterface().getFullName(), "_", oDumbAttr.getName()) : null, 
					oDumbAttr.isMandatory());
		
		oVisualField.setViewModelProperty(sTName);
		oVisualField.setViewModelName(p_oViewModel.getName());
		
		p_oViewModel.addVisualField(oVisualField);

		p_oViewModel.setReadOnly(p_oViewModel.isReadOnly() && bReadOnly);

		// Instanciation de l'attribut
		MAttribute oAttr = p_oDomain.getXModeleFactory().createMAttribute(sTName,
				p_oUmlAttribute.getVisibility(), bIsIdentifier, p_oUmlAttribute.isDerived(),
				p_oUmlAttribute.isTransient(), oVMTypeDescriptionClone, p_oUmlAttribute.getDocumentation(),
				bReadOnly);
		oAttr.setInitialisation(oDumbAttr.getInitialisation());
		oAttr.setMEnumeration(oDumbAttr.getMEnumeration());

		// Compute init value for viewmodel attribute
		this.computeInitValueForVMAttribute(p_oUmlAttribute.getInitialValue(), oAttr, p_oDomain.getLanguageConf());

		MMethodSignature r_oSigGet = null;
		
		String sNameGet = null;
		if (oVMTypeDescriptionClone.getShortName().toLowerCase(Locale.getDefault()).equals("boolean")) {
			sNameGet = StringUtils.join("is", StringUtils.capitalize(sTName));
		} else {
			sNameGet = StringUtils.join("get", StringUtils.capitalize(sTName));
		}
		log.debug("  convert the method: {}, part of identifier: {}, type: {}", 
				new Object[] {sNameGet, bIsIdentifier, oVMTypeDescriptionClone.getShortName()});

		r_oSigGet = new MMethodSignature(sNameGet, "public", "get", oVMTypeDescriptionClone);
		r_oSigGet.addOption("attribute", sTName);

		MMethodSignature r_oSigSet = null;
		String sNameSet = "set" + StringUtils.capitalize(sTName);
		r_oSigSet = new MMethodSignature(sNameSet, "public", "set", null);
		r_oSigSet.addOption("attribute", sTName);
		r_oSigSet.addParameter(new MMethodParameter(
				StringUtils.join("p_o", sTName), oVMTypeDescriptionClone));

		p_oViewModel.addAttribute(oAttr);
		p_oViewModel.getMasterInterface().addMethodSignature(r_oSigGet);
		p_oViewModel.getMasterInterface().addMethodSignature(r_oSigSet);
	}

	/**
	 * On transforme un champ en provenance d'une entité en view model (via un
	 * chemin dans l'uml)
	 * @param p_sPrefix prefix
	 * @param p_sPrefixM prefix m
	 * @param p_oEntityAttribute attribute of entity
	 * @param p_oVMUmlAttribute uml attribute of viewmodel
	 * @param p_oViewModel view model
	 * @param p_bMasterReadonly is master readonly
	 * @param p_bVisualDisplay visual display
	 * @param p_bTestIfExist test if exist
	 * @param p_oDomain domain
	 * @return Cags
	 * @throws Exception exception
	 */
	public Cags convertAttribute(String p_sPrefix, String p_sPrefixM, MAttribute p_oEntityAttribute,
			UmlAttribute p_oVMUmlAttribute, MViewModelImpl p_oViewModel, boolean p_bMasterReadonly,
			boolean p_bVisualDisplay, boolean p_bTestIfExist, IDomain<IModelDictionary, IModelFactory> p_oDomain) throws Exception {

		ITypeDescription oEntityTypeDesc = p_oEntityAttribute.getTypeDesc();

		// parse les options
		Map<String, ?> mapAttrOptions = null;
		IUITypeDescription oUIType= null; // représente le type visuel
		if (p_oVMUmlAttribute == null) {
			mapAttrOptions = new TreeMap<String, Object>();
			oUIType = p_oDomain.getLanguageConf().getUiTypeDescription(
					p_oEntityAttribute.getTypeDesc().getDefaultUiType(), VersionHandler.getGenerationVersion().getStringVersion());
			if ( oUIType == null ) {
				throw new AdjavaException("Can't find ui type : {}", p_oEntityAttribute.getTypeDesc().getDefaultUiType());
			}
		} else {
			mapAttrOptions = UmlAttributeOptionParser.getInstance().parse(
					p_oVMUmlAttribute.getInitialValue(), oEntityTypeDesc.getDefaultOptions(),
					oEntityTypeDesc.getDataType());

			if (p_oVMUmlAttribute.getDataType() != null) {
				oUIType = p_oDomain.getLanguageConf().getUiTypeDescription(
						p_oVMUmlAttribute.getDataType().getName(), VersionHandler.getGenerationVersion().getStringVersion());
				if ( oUIType == null ) {
					throw new AdjavaException("Can't find ui type : {}", p_oVMUmlAttribute.getDataType().getName());
				}
			} else {
				// pas de mapping est indiqué prendre le type par défaut du type
				// du model
				oUIType = p_oDomain.getLanguageConf().getUiTypeDescription(
						p_oEntityAttribute.getTypeDesc().getDefaultUiType(), VersionHandler.getGenerationVersion().getStringVersion());
				if ( oUIType == null ) {
					throw new AdjavaException("Can't find ui type : {}", p_oEntityAttribute.getTypeDesc().getDefaultUiType());
				}
			}
		}

		return this.convertAttribute(p_sPrefix, p_sPrefixM, p_oEntityAttribute, mapAttrOptions, oUIType,
				p_oViewModel, p_bMasterReadonly, p_bVisualDisplay, p_bTestIfExist, p_oVMUmlAttribute,
				p_oDomain);
	}

	/**
	 * Add attributes to the current viewmodel corresponding to the path of
	 * panel uml attribute.
	 * <p>
	 * VMPathContext must be valid.
	 * </p>
	 * @param p_oContextPath context path (hold the current vm)
	 * @param p_oVMUmlAttribute uml attribute in panel
	 * @param p_oDomain domain
	 * @throws Exception exception
	 */
	public void createVMAttributesFromUmlAttr(VMPathContext p_oContextPath, UmlAttribute p_oVMUmlAttribute,
			IDomain<IModelDictionary, IModelFactory> p_oDomain) throws Exception {

		MViewModelImpl oCurrentVmi = p_oContextPath.getCurrentVM();
		MEntityImpl oEntityTarget = p_oContextPath.getEntityTarget();
		IVMMappingDesc oCurrentMapping = p_oContextPath.getCurrentMapping();
		String sName = p_oContextPath.getCurrentElement();

		if (log.isDebugEnabled()) {
			log.debug("addFieldToClass");
			log.debug("  viewmodel to update: {}", p_oContextPath.getCurrentVM().getName());
			log.debug("  entity: {}", oEntityTarget.getName());
			log.debug("  attribute: {}", p_oVMUmlAttribute.getName());
			log.debug("  path end name: {}", sName);
			log.debug("  parent path: {}", p_oContextPath.getOldPath());
		}

		String sPrefix = p_oContextPath.getOldPath();
		sPrefix = sPrefix.replaceAll("\\.", StringUtils.EMPTY);

		// On teste la présence de l'option readonly
		Map<String, ?> mapAttrOptions = UmlAttributeOptionParser.getInstance().parse(
				p_oVMUmlAttribute.getInitialValue());

		String sOptionValue = (String) mapAttrOptions.get(DefaultAttrOptionSetter.Option.READ_ONLY.getUmlCode());

		boolean bReadOnly = mapAttrOptions.containsKey(DefaultAttrOptionSetter.Option.READ_ONLY.getUmlCode())
				&& (sOptionValue == null || "ui".equals(sOptionValue) || sOptionValue.contains("s") && sOptionValue.contains("l"));

		boolean bDisableSetter	= bReadOnly && !"ui".equals(sOptionValue);

		log.debug("  read only: {}", bReadOnly);
		
		if ( !this.mapWithAttribute(p_oVMUmlAttribute, oCurrentVmi, sName, oEntityTarget, sPrefix, bReadOnly, 
				bDisableSetter, oCurrentMapping, p_oDomain)) {
				MessageHandler.getInstance().addError("Attribute {} of panel {} doesnot match any attribute of entity {}",
					p_oContextPath.getFullPath(), p_oContextPath.getCurrentVM().getUmlName(), p_oContextPath.getEntityTarget().getUmlName());
		}
	}

	/**
	 * Try to map with an attribute of the target entity
	 * @param p_oVMUmlAttribute attribute of viewmodel
	 * @param p_oCurrentVmi viewmodel
	 * @param p_sName last part of the name of the vm attribute
	 * @param p_oTargetEntity target entity
	 * @param p_sPrefix prefix
	 * @param p_bReadOnly read only
	 * @param bDisableSetter disable setter
	 * @param p_oCurrentMapping mapping
	 * @param p_oDomain domain 
	 * @return true if at least one attribute in the target entity has been mapped.
	 * @throws Exception
	 */
	private boolean mapWithAttribute(UmlAttribute p_oVMUmlAttribute, MViewModelImpl p_oCurrentVmi, String p_sName, 
			MEntityImpl p_oTargetEntity, String p_sPrefix, boolean p_bReadOnly, boolean bDisableSetter, IVMMappingDesc p_oCurrentMapping,
			IDomain<IModelDictionary, IModelFactory> p_oDomain) throws Exception {
		boolean r_bValid = false ;
		
		// Buid a list of all attributes
		List<MAttribute> listAttrs = new ArrayList<MAttribute>();
		listAttrs.addAll(p_oTargetEntity.getAttributes());
		listAttrs.addAll(p_oTargetEntity.getIdentifier().getElemOfTypeAttribute());
		listAttrs.addAll( this.createFakeAttrFromExpandables(p_oTargetEntity, p_oDomain));
		
		for (MAttribute oEntityAttribute : listAttrs) {
			log.debug("addFieldToClass {} =? {}", p_sName, oEntityAttribute.getName());
			if ("all".equals(p_sName) || p_sName.equals(oEntityAttribute.getName())
					|| p_sName.equals(oEntityAttribute.getName().substring(1))) {
				log.debug("addFieldToClass {} =OK for add {}", p_sName, oEntityAttribute.getName());
				Cags oCags = this.convertAttribute(p_sPrefix, StringUtils.EMPTY, oEntityAttribute,
						p_oVMUmlAttribute, p_oCurrentVmi, p_bReadOnly, true, true, p_oDomain);
				MAttribute oAttr = oCags.attribute;
				MMethodSignature oSig = oCags.get;
				p_oCurrentVmi.getMasterInterface().addMethodSignature(oSig);
				oSig = oCags.set;
				p_oCurrentVmi.getMasterInterface().addMethodSignature(oSig);
				log.debug("------------------------------------------------------------------------------------------------>add attribute {}",
					oAttr.getName());
				p_oCurrentVmi.addAttribute(oAttr);
				ITypeDescription oTypeDesc = null;
				if (oEntityAttribute.getTypeDesc() != null) {
					oTypeDesc = oEntityAttribute.getTypeDesc();

				} else {
					oTypeDesc = p_oDomain.getDictionnary().getNoType();
				}
				
				if ( !oEntityAttribute.isBasic()) {
					p_oCurrentVmi.addImport(oEntityAttribute.getTypeDesc().getName());
				}
				
				log.debug("addFieldToClass p_oVmi {}/{}", p_oCurrentVmi, p_oCurrentVmi.getName());
				log.debug("addFieldToClass oAttr {}/{}", oAttr, oAttr.getName());

				if (p_oCurrentMapping != null) {
					
					String sExpandableEntity = null;
					if ( oEntityAttribute.getParameters().get("fromExpandable") != null ) {
						sExpandableEntity = oEntityAttribute.getTypeDesc().getName();
					}
					
					p_oCurrentMapping.addAttribute(p_oCurrentVmi, oEntityAttribute.getName(), oAttr.getName(),
							oAttr.getTypeDesc(), oTypeDesc, bDisableSetter, oEntityAttribute.getInitialisation(), oAttr.getInitialisation(), sExpandableEntity,
							oEntityAttribute.getTypeDesc().isPrimitif());
				}
				
				r_bValid = true ;
			}
		}
		
		return r_bValid;
	}
	
	/**
	 * Create fake MAttributes for associations created from an expandable
	 * @param p_oTargetEntity target entity
	 * @param p_oDomain domain 
	 * @return list of created attributes
	 * @throws Exception
	 */
	private List<MAttribute> createFakeAttrFromExpandables( MEntityImpl p_oTargetEntity, IDomain<IModelDictionary, IModelFactory> p_oDomain) throws Exception {
		
		List<MAttribute> r_listAttr = new ArrayList<MAttribute>();
				
		for (MAssociation oAssociation : p_oTargetEntity.getAssociations()) {
			if ( MAssociationOneToOne.class.isAssignableFrom(oAssociation.getClass())) {
				MAssociationOneToOne oOneToOneAsso = (MAssociationOneToOne) oAssociation ;
				if ( oOneToOneAsso.getExpandableTypeDesc() != null ) {
					
					if ( oOneToOneAsso.getExpandableTypeDesc().getDefaultUiType() != null ) {
					
						ITypeDescription oObjectTypeDescription = p_oDomain.getLanguageConf().getTypeDescription("Object");
						ITypeDescription oClonedTypeDescription = (ITypeDescription) oObjectTypeDescription.clone();
						oClonedTypeDescription.setUmlName(oOneToOneAsso.getExpandableTypeDesc().getUmlName());
						oClonedTypeDescription.setName(oAssociation.getRefClass().getMasterInterface().getFullName());
						oClonedTypeDescription.setDefaultUiType(oOneToOneAsso.getExpandableTypeDesc().getDefaultUiType());
						
						for( ITypeConvertion oTypeconversion : 
							p_oDomain.getLanguageConf().getTypeDescription(oOneToOneAsso.getExpandableTypeDesc().getUmlName()).getConvertions()) {
							oClonedTypeDescription.addTypeConvertion(oTypeconversion.getTo(), oTypeconversion.getFormula(), oTypeconversion.getImports());
						}
						
						MAttribute oAttr = p_oDomain.getXModeleFactory().createMAttribute(oOneToOneAsso.getName(), "private", false, false, oOneToOneAsso.isTransient(),
								oClonedTypeDescription, StringUtils.EMPTY, StringUtils.EMPTY, oOneToOneAsso.isNotNull(), 
								-1, -1, -1, false, oOneToOneAsso.getUniqueKey() != null, null, StringUtils.EMPTY);
						oAttr.addParameter("fromExpandable", "true");
						r_listAttr.add(oAttr);
					}
					else {
						MessageHandler.getInstance().addWarning("Missing default-ui-type on expandable type {}", oOneToOneAsso.getExpandableTypeDesc().getName());
					}
				}
			}
		}
		return r_listAttr;
	}

	/**
	 * On transforme un champ en provenance d'une entité en view model (via un chemin dans l'uml)
	 * @param p_sPrefix prefix
	 * @param p_sPrefixM prefix
	 * @param p_oEntityAttribute attribute of entity
	 * @param p_mapAttrOptions attribute options
	 * @param p_oUIType ui type description
	 * @param p_oViewModel view model
	 * @param p_bMasterReadonly is master readonly
	 * @param p_bVisualDisplay visual display
	 * @param p_bTestIfExist test if exists
	 * @param p_oVMUmlAttribute uml attribute of view model
	 * @param p_oDomain domain
	 * @return Cags
	 * @throws AdjavaException failure
	 */
	private Cags convertAttribute(String p_sPrefix, String p_sPrefixM, MAttribute p_oEntityAttribute,
			Map<String, ?> p_mapAttrOptions, IUITypeDescription p_oUIType, MViewModelImpl p_oViewModel,
			boolean p_bMasterReadonly, boolean p_bVisualDisplay, boolean p_bTestIfExist,
			UmlAttribute p_oVMUmlAttribute, IDomain<IModelDictionary, IModelFactory> p_oDomain) throws AdjavaException {

		String sTName = p_sPrefixM + p_oEntityAttribute.getName();
		if (p_bTestIfExist && p_oViewModel.hasAttribute(sTName)) {
			sTName = StringUtils.join(p_sPrefix, sTName );
		}

		Cags r_oResult = new Cags();

		MAttribute oAttr = null;

		log.debug("  conversion de l'attribut : {}/{}", p_oEntityAttribute.getName(), sTName);	
		
		// readonly
		boolean bReadOnly = false;

		switch (p_oViewModel.getType()) {
			case LIST_1__ONE_SELECTED:
				bReadOnly = this.convertAttribute(sTName, p_oEntityAttribute, p_mapAttrOptions, p_oUIType, p_oViewModel, p_bMasterReadonly, p_bVisualDisplay, p_oDomain,
						DefaultAttrOptionSetter.Option.COMBO);

				// Si affichage et cas de la combo dans une fixed list
				if (p_bVisualDisplay && p_oViewModel.getParent() != null && ViewModelType.FIXED_LIST == p_oViewModel.getParent().getType()) {
					// Ajout du champ dans le viewmodel parent en read-only pour le layout de list
					// LBR Modify : MVFLabelKind.NO_LABEL par this.computeVFLabelKind(p_mapAttrOptions)
					
					
					// this field name has been generated in two way
					// the friendly one is keep as default, 
					// and android keep the old name (not very friendly)
					String sVisualFieldName = p_oDomain.getXModeleFactory().createVisualFieldNameForFixedListCombo(p_oViewModel, sTName);
					String sPropertyName = p_oDomain.getXModeleFactory().createPropertyNameForFixedListCombo(p_oViewModel, sTName);
					
					MLabel oLabel = p_oDomain.getXModeleFactory().createLabelForAttributeOfCombo(sTName, sVisualFieldName, MVFLocalization.LIST, p_oViewModel);
					
					MVisualField oVisualField = p_oDomain.getXModeleFactory().createVisualField(sVisualFieldName, oLabel, p_oUIType,
							MVFModifier.READONLY,
							this.computeVFLabelKind(p_mapAttrOptions),
							p_oEntityAttribute,
							MVFLocalization.LIST,
							p_oDomain,
							p_oViewModel.getEntityToUpdate() != null
								? StringUtils.join(p_oViewModel.getEntityToUpdate().getMasterInterface().getFullName(), "_", p_oEntityAttribute.getName()) : null,
							p_oEntityAttribute.isMandatory());

					oVisualField.setViewModelProperty(sPropertyName);
					oVisualField.setViewModelName(p_oViewModel.getName());
					
					p_oViewModel.addVisualField(oVisualField);
					
					p_oViewModel.getParent().addVisualField(oVisualField);
				}

				break;
			case FIXED_LIST:
				bReadOnly = this.convertAttribute(sTName, p_oEntityAttribute, p_mapAttrOptions, p_oUIType, p_oViewModel, p_bMasterReadonly, p_bVisualDisplay, p_oDomain,
						DefaultAttrOptionSetter.Option.FIXED_LIST);

				break;
			default:
				bReadOnly = p_bMasterReadonly || p_mapAttrOptions.containsKey(DefaultAttrOptionSetter.Option.READ_ONLY.getUmlCode());
				if (p_bVisualDisplay) {		

					MLabel oLabel = p_oDomain.getXModeleFactory().createLabel(sTName, p_oViewModel);
					
					MVisualField oVisualField = p_oDomain.getXModeleFactory().createVisualField(
							sTName, oLabel,
							p_oUIType,
							bReadOnly ? MVFModifier.READONLY: MVFModifier.WRITABLE,
									this.computeVFLabelKind(p_mapAttrOptions),
									p_oEntityAttribute,
									p_oDomain,
									p_oViewModel.getEntityToUpdate() != null
										? StringUtils.join(p_oViewModel.getEntityToUpdate().getMasterInterface().getFullName(), "_", p_oEntityAttribute.getName()) : null,
											p_oEntityAttribute.isMandatory());

					oVisualField.setViewModelProperty(sTName);
					oVisualField.setViewModelName(p_oViewModel.getName());
					
					p_oViewModel.addVisualField(oVisualField);
				}
		}

		p_oViewModel.setReadOnly(p_oViewModel.isReadOnly() && bReadOnly);

		String sViewModelType = null;
		if (bReadOnly) {
			if ( p_oUIType.isSameTypeAsEntityForReadOnly() ) {
				sViewModelType = p_oEntityAttribute.getTypeDesc().getUmlName() ;
				log.debug("convert attribute readonly with entity type {}", sViewModelType);
			} else {
				log.debug("convert attribute readonly roviewmodeltype {}", p_oUIType.getROViewModelType());
				sViewModelType = p_oUIType.getROViewModelType();
			}
		} else {
			if ( p_oUIType.isSameTypeAsEntityForReadWrite() ) {
				sViewModelType = p_oEntityAttribute.getTypeDesc().getUmlName() ;
				log.debug("convert attribute writable with entity type {}", sViewModelType);
			} else {
				log.debug("convert attribute not readonly rwviewmodeltype {}", p_oUIType.getRWViewModelType());
				sViewModelType = p_oUIType.getRWViewModelType();
			}
		}
		ITypeDescription oVMAttrTypeDesc = p_oDomain.getLanguageConf().getTypeDescription(sViewModelType);
		// if not found and package startsWith com. org., create it from Object type
		if ( oVMAttrTypeDesc == null ) {
			if ( StringUtils.startsWithAny(sViewModelType,"com.", "org.", "net.")) {
				ITypeDescription oObjectTypeDescription = p_oDomain.getLanguageConf().getTypeDescription("Object");
				oVMAttrTypeDesc = (ITypeDescription) oObjectTypeDescription.clone();
				oVMAttrTypeDesc.setName(sViewModelType);
			}
			else {
				throw new AdjavaException("VMAttributeHelper - can't find type description : {}", sViewModelType );
			}
		}

		MEnumeration oEnum = null;
		if (oVMAttrTypeDesc.getName().equals("enumeration")) {
			oVMAttrTypeDesc = p_oEntityAttribute.getTypeDesc();
			oEnum = p_oEntityAttribute.getMEnumeration();
		}

		// Instanciation de l'attribut
		oAttr = p_oDomain.getXModeleFactory().createMAttribute(sTName, p_oEntityAttribute.getVisibility(),
				p_oEntityAttribute.isPartOfIdentifier(), false, false, oVMAttrTypeDesc,
				p_oEntityAttribute.getDocumentation(), bReadOnly);
		oAttr.setVisualDisplay(p_bVisualDisplay);

		oAttr.setMEnumeration(oEnum);

		// Compute init value for viewmodel attribute
		if (p_oVMUmlAttribute != null) {
			this.computeInitValueForVMAttribute(p_oVMUmlAttribute.getInitialValue(), oAttr, p_oDomain.getLanguageConf());
		}

		r_oResult.attribute = oAttr;

		MMethodSignature r_oSigGet = null;

		String sNameGet = null;
		if (oVMAttrTypeDesc.getShortName().equalsIgnoreCase("boolean")) {
			sNameGet = StringUtils.join("is", StringUtils.capitalize(sTName));
		} else {
			sNameGet = StringUtils.join("get", StringUtils.capitalize(sTName));
		}

		log.debug("  convert method: {}, part of identifier: {}, type: {}",				
			new Object[] { sNameGet, p_oEntityAttribute.isPartOfIdentifier(), oVMAttrTypeDesc.getShortName()});

		r_oSigGet = new MMethodSignature(sNameGet, "public", "get", oVMAttrTypeDesc);
		r_oSigGet.addOption("attribute", sTName);
		r_oResult.get = r_oSigGet;

		MMethodSignature r_oSigSet = null;
		String sNameSet = StringUtils.join("set", StringUtils.capitalize(sTName));
		r_oSigSet = new MMethodSignature(sNameSet, "public", "set", null);
		r_oSigSet.addOption("attribute", sTName);
		r_oSigSet.addParameter(new MMethodParameter( StringUtils.join("p_o", sTName), oVMAttrTypeDesc));
		r_oResult.set = r_oSigSet;
		return r_oResult;
	}

	/**
	 * Convert attribute
	 * @param p_sAttributeName attribute name
	 * @param p_oEntityAttribute attribute of entity
	 * @param p_mapAttrOptions attribute options
	 * @param p_oTypeVisual ui type description
	 * @param p_oViewModel view model
	 * @param p_bMasterReadonly is master readonly
	 * @param p_bVisualDisplay visual display
	 * @param p_oDomain domain
	 * @param p_oContainerType container type
	 * @return boolean
	 */
	private boolean convertAttribute(String p_sAttributeName, MAttribute p_oEntityAttribute, Map<String, ?> p_mapAttrOptions, IUITypeDescription p_oTypeVisual, MViewModelImpl p_oViewModel, boolean p_bMasterReadonly,
			boolean p_bVisualDisplay, IDomain<IModelDictionary, IModelFactory> p_oDomain, DefaultAttrOptionSetter.Option p_oContainerType) {

		String sValue = (String) p_mapAttrOptions.get(p_oContainerType.getUmlCode());

		boolean bDisplayIntoDetail = sValue == null || sValue.length() == 0 || sValue.contains("s");
		boolean bDisplayIntoListItem = sValue == null || sValue.length() == 0 || sValue.contains("l");
		MVFModifier oSelectModifier = null;
		MVFModifier oListModifier = null;
		
		if (bDisplayIntoDetail) {
			// ReadOnly au niveau du détail
			oSelectModifier = this.computeVFModifier(p_mapAttrOptions, p_bMasterReadonly, "s");

			if (p_bVisualDisplay) {
				
				MLabel oLabel = null;
				if ( p_oContainerType == DefaultAttrOptionSetter.Option.FIXED_LIST) {
					oLabel = p_oDomain.getXModeleFactory().createLabelForAttributeOfFixedList(p_sAttributeName, p_sAttributeName, MVFLocalization.DETAIL, p_oViewModel);
				}
				else if ( p_oContainerType == DefaultAttrOptionSetter.Option.COMBO) {
					oLabel = p_oDomain.getXModeleFactory().createLabelForAttributeOfCombo(p_sAttributeName, null, MVFLocalization.DETAIL, p_oViewModel);
				}
				else {
					oLabel = p_oDomain.getXModeleFactory().createLabel(p_sAttributeName, p_oViewModel);
				}
				
				MVisualField oNewMVisualField = p_oDomain.getXModeleFactory().createVisualField(
						p_sAttributeName,
						oLabel,
						p_oTypeVisual,
						oSelectModifier,
						this.computeVFLabelKind(p_mapAttrOptions),
						p_oEntityAttribute,
						MVFLocalization.DETAIL,
						p_oDomain,
						p_oViewModel.getEntityToUpdate() != null 
								? StringUtils.join(p_oViewModel.getEntityToUpdate().getMasterInterface().getFullName(), "_", p_oEntityAttribute.getName())
								: null, p_oEntityAttribute.isMandatory());
						
				if ( p_oContainerType == DefaultAttrOptionSetter.Option.FIXED_LIST) {
					oNewMVisualField.addParameter("inFixedList", "true");
					oNewMVisualField.addParameter("fixedListVm", p_oViewModel.getFullName());
				}
				else if ( p_oContainerType == DefaultAttrOptionSetter.Option.COMBO) {
					oNewMVisualField.addParameter("inCombo", "true");
					oNewMVisualField.addParameter("comboVm", p_oViewModel.getFullName());
				}
				
				oNewMVisualField.setViewModelProperty(p_sAttributeName);		
				oNewMVisualField.setViewModelName(p_oViewModel.getName());
				
				
				p_oViewModel.addVisualField( oNewMVisualField );
			}
		}

		if (bDisplayIntoListItem) {
			// ReadOnly au niveau de la liste.
			oListModifier = this.computeVFModifier(p_mapAttrOptions, p_bMasterReadonly, "l");

			if (p_bVisualDisplay) {
				
				MLabel oLabel = null;
				
				if ( p_oContainerType == DefaultAttrOptionSetter.Option.FIXED_LIST) {
					oLabel = p_oDomain.getXModeleFactory().createLabelForAttributeOfFixedList(p_sAttributeName, p_sAttributeName, MVFLocalization.LIST, p_oViewModel);
				}
				else if ( p_oContainerType == DefaultAttrOptionSetter.Option.COMBO) {
					oLabel = p_oDomain.getXModeleFactory().createLabelForAttributeOfCombo(p_sAttributeName, p_sAttributeName, MVFLocalization.LIST, p_oViewModel);
				} else {
					oLabel = p_oDomain.getXModeleFactory().createLabel(p_sAttributeName, p_oViewModel);
				}
				
				MVisualField oNewMVisualField = p_oDomain.getXModeleFactory().createVisualField(
						p_sAttributeName,
						oLabel,
						p_oTypeVisual,
						oListModifier,
						this.computeVFLabelKind(p_mapAttrOptions),
						p_oEntityAttribute,
						MVFLocalization.LIST,
						p_oDomain,
						p_oViewModel.getEntityToUpdate() != null
							? StringUtils.join( p_oViewModel.getEntityToUpdate().getMasterInterface().getFullName(), "_", p_oEntityAttribute.getName())
								: null, p_oEntityAttribute.isMandatory());
							
				if ( p_oContainerType == DefaultAttrOptionSetter.Option.FIXED_LIST) {
					oNewMVisualField.addParameter("inFixedList", "true");
					oNewMVisualField.addParameter("fixedListVm", p_oViewModel.getMasterInterface().getName());
				}
				else if ( p_oContainerType == DefaultAttrOptionSetter.Option.COMBO) {
					oNewMVisualField.addParameter("inCombo", "true");
					oNewMVisualField.addParameter("comboVm", p_oViewModel.getMasterInterface().getName());
				}
				oNewMVisualField.setViewModelProperty(p_oEntityAttribute.getName());
				oNewMVisualField.setViewModelName(p_oViewModel.getName());
				
				p_oViewModel.addVisualField( oNewMVisualField );
			}
		}

		return (!bDisplayIntoDetail || MVFModifier.READONLY.equals(oSelectModifier)) && (!bDisplayIntoListItem || MVFModifier.READONLY.equals(oListModifier));
	}
	
	/**
	 * Compute visual field modifier
	 * @param p_mapAttrOptions attribute options
	 * @param p_bMasterReadonly is master readonly
	 * @param p_sClassifier classifier
	 * @return visual field modifier
	 */
	private MVFModifier computeVFModifier(Map<String, ?> p_mapAttrOptions, boolean p_bMasterReadonly, String p_sClassifier) {
		MVFModifier r_oMVFModifier = MVFModifier.WRITABLE ;
		
		String sReadOnlyOption = (String) p_mapAttrOptions.get(DefaultAttrOptionSetter.Option.READ_ONLY.getUmlCode());
		
		if ( p_bMasterReadonly
				|| (p_mapAttrOptions.containsKey(DefaultAttrOptionSetter.Option.READ_ONLY.getUmlCode()) && (sReadOnlyOption == null
				|| sReadOnlyOption.length() == 0 || sReadOnlyOption.contains(p_sClassifier)))) {
			r_oMVFModifier = MVFModifier.READONLY ;
		}

		return r_oMVFModifier ;
	}
	
	/**
	 * Compute kind of label for visual field
	 * @param p_mapAttrOptions attribute options
	 * @return kind of label
	 */
	private MVFLabelKind computeVFLabelKind(Map<String, ?> p_mapAttrOptions) {
		return p_mapAttrOptions.containsKey(DefaultAttrOptionSetter.Option.NO_LABEL.getUmlCode())
			? MVFLabelKind.NO_LABEL: MVFLabelKind.WITH_LABEL ;
	}

	/**
	 * Compute initial value for attribute of viewmodel
	 * @param p_sInitValue init value
	 * @param p_oAttr attribute
	 * @param p_oLngConf language configuration
	 */
	private void computeInitValueForVMAttribute(String p_sInitValue, MAttribute p_oAttr, LanguageConfiguration p_oLngConf) {
		// parse les options
		String sInit = null;
		Map<String, ?> mapVMAttrOptions = UmlAttributeOptionParser.getInstance().parse(p_sInitValue,
				p_oAttr.getTypeDesc().getDefaultOptions(), p_oAttr.getTypeDesc().getDataType());
		String sInitValue = (String) mapVMAttrOptions.get(Option.INIT.getUmlCode());
		String sDefaultValue = null;
		if (sInitValue != null) {
			sDefaultValue = sInitValue;
		} else {
			sDefaultValue = p_oAttr.getTypeDesc().getDefaultValue();
		}
		if (!p_oAttr.getTypeDesc().isPrimitif() && p_oLngConf.getNullValue().equals(sDefaultValue)) {
			sInit = p_oLngConf.getNullValue();
		} else {
			sInit = p_oAttr.getTypeDesc().getInitFormat().replaceAll("\\?", sDefaultValue);
		}

		if (p_oAttr.isEnum()) {
			sInit = sInit.replaceAll("ENUMERATIONUPPERCASE", p_oAttr
					.getMEnumeration().getName().toUpperCase());
			sInit = sInit.replaceAll("ENUMERATION", p_oAttr.getMEnumeration().getName());
		}
		p_oAttr.setInitialisation(sInit);
	}
}
