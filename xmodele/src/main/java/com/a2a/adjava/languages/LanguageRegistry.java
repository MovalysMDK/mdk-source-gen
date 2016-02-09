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
package com.a2a.adjava.languages;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.commons.init.InitializingBean;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.types.ITypeDescription.Property;
import com.a2a.adjava.types.IUITypeDescription;
import com.a2a.adjava.types.TypeDescriptionFactory;
import com.a2a.adjava.types.UITypeDescriptionFactory;
import com.a2a.adjava.utils.BeanUtils;
import com.a2a.adjava.utils.StrUtils;
import com.a2a.adjava.xmodele.MActionType;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;

/**
 * <p>Language Registry</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */

public class LanguageRegistry implements InitializingBean {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(LanguageRegistry.class);
	
	
	/**
	 * Language configurations 
	 */
	private Map<String,LanguageConfiguration> languageConfigurations =
		new HashMap<String,LanguageConfiguration>();

	/**
	 * Get language configuration by name
	 * @param p_sLngName name of language configuration
	 * @return language configuration
	 */
	public LanguageConfiguration getLanguageConfiguration( String p_sLngName ) {
		return this.languageConfigurations.get(p_sLngName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(Element p_xElement, Map<String,String> p_mapGlobalProperties) throws Exception {
		Element xLanguages = p_xElement.element("languages");
		if ( xLanguages != null ) {			
			for( Element xLng : (List<Element>) xLanguages.elements("language")) {
				String sLngName = xLng.attributeValue("name");
				LanguageConfiguration oLanguageConfiguration = this.languageConfigurations.get(sLngName);
				if ( oLanguageConfiguration == null ) {
					oLanguageConfiguration = new LanguageConfiguration(sLngName);
					this.languageConfigurations.put(sLngName, oLanguageConfiguration);
				}
				String sTypeDescriptionFactory = xLng.elementText("typedescription-factory");
				if ( sTypeDescriptionFactory != null ) {
					sTypeDescriptionFactory = sTypeDescriptionFactory.trim();
					oLanguageConfiguration.setTypeDescriptionFactory((TypeDescriptionFactory) Class.forName(sTypeDescriptionFactory).newInstance());
				}
				
				String sUITypeDescriptionFactory = xLng.elementText("ui-typedescription-factory");
				if ( sUITypeDescriptionFactory != null ) {
					sUITypeDescriptionFactory = sUITypeDescriptionFactory.trim();
					oLanguageConfiguration.setUITypeDescriptionFactory((UITypeDescriptionFactory) Class.forName(sUITypeDescriptionFactory).newInstance());
				}
				
				// Read naming rules
				this.readNamingRules(xLng, oLanguageConfiguration);
				
				//Read ui type description
				this.readUITypeDescriptions(oLanguageConfiguration, xLng.element("ui-types"));
				
				// Read type description
				this.readTypeDescriptions(xLng.element("types"), oLanguageConfiguration);
				
				// null value
				BeanUtils.setIfNotNull( oLanguageConfiguration, "nullValue", xLng.elementText("null-value"));
				
				Element xNonGenerated = xLng.element("non-generated");
				if ( xNonGenerated != null ) {
					BeanUtils.setIfNotNull( oLanguageConfiguration, "nonGeneratedStartMarker", xNonGenerated.elementText("start-marker"));
					BeanUtils.setIfNotNull( oLanguageConfiguration, "nonGeneratedEndMarker", xNonGenerated.elementText("end-marker"));
				}

				Element xInclude = xLng.element("include");
				if (xInclude != null) {
					BeanUtils.setIfNotNull( oLanguageConfiguration, "includeStartMarker", xInclude.elementText("start-marker"));
					BeanUtils.setIfNotNull( oLanguageConfiguration, "includeEndMarker", xInclude.elementText("end-marker"));
				}

				//lecture du noeud type_viewmodels
				//attention la lecture de ce noeud écrase l'énumeration ViewModelType
				this.readViewModelTypes(xLng.element("type-viewmodels"));
				
				//lecture du noeud type_actions
				//attention la lecture de ce noeud écrase l'énumeration MAction.ActionType
				this.readActionTypes(xLng.element("type-actions"));
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.commons.init.InitializingBean#afterInitialization()
	 */
	@Override
	public void afterInitialization( Map<String,String> p_mapGlobalProperties) throws Exception {
			
		for( LanguageConfiguration oLngConfiguration : this.languageConfigurations.values()) {
		
			this.linkPropertiesOfCompositeTypes( oLngConfiguration );
			
			if ( oLngConfiguration.getTypeDescriptionFactory() == null ) {
				MessageHandler.getInstance().addError("<typedescription-factory> has not been defined for language : {}", oLngConfiguration.getName());
			}
			
			if ( oLngConfiguration.getNonGeneratedStartMarker() == null ) {
				MessageHandler.getInstance().addError("<non-generated><start-marker> has not been defined for language : {}", oLngConfiguration.getName());
			}
			
			if ( oLngConfiguration.getNonGeneratedEndMarker() == null ) {
				MessageHandler.getInstance().addError("<non-generated><end-marker> has not been defined for language : {}", oLngConfiguration.getName());
			}
		}
	}
	
	
	/**
	 * Read naming rules
	 * @param p_xLanguageConf xml node of language configuration
	 * @param p_oLanguageConfiguration language configuration
	 * @throws Exception exception
	 */
	private void readNamingRules( Element p_xLanguageConf, LanguageConfiguration p_oLanguageConfiguration ) throws Exception {
		
		this.readNamingRule(p_xLanguageConf.element("interfaces"), "interfaceNamingPrefix", "interfaceNamingSuffix", "interfaceSubPackageName", p_oLanguageConfiguration);
		this.readNamingRule(p_xLanguageConf.element("implementation"), "implNamingPrefix", "implNamingSuffix", "implSubPackageName", p_oLanguageConfiguration);
		this.readNamingRule(p_xLanguageConf.element("enum"), "enumNamingPrefix", "enumNamingSuffix", "enumSubPackageName", p_oLanguageConfiguration);
		this.readNamingRule(p_xLanguageConf.element("pojo-factory-interfaces"), "pojoFactoryInterfaceNamingPrefix", "pojoFactoryInterfaceNamingSuffix", "pojoFactoryInterfaceSubPackageName", p_oLanguageConfiguration);
		this.readNamingRule(p_xLanguageConf.element("pojo-factory-implementation"), "pojoFactoryImplNamingPrefix", "pojoFactoryImplNamingSuffix", "pojoFactoryImplSubPackageName", p_oLanguageConfiguration);
		this.readNamingRule(p_xLanguageConf.element("dao"), "daoInterfaceNamingPrefix", "daoInterfaceNamingSuffix", "daoInterfaceSubPackageName", p_oLanguageConfiguration);
		this.readNamingRule(p_xLanguageConf.element("dao-implementation"), "daoImplementationNamingPrefix", "daoImplementationNamingSuffix", "daoImplementationSubPackageName", p_oLanguageConfiguration);
		this.readNamingRule(p_xLanguageConf.element("viewmodel-interface"), "viewModelInterfaceNamingPrefix", "viewModelInterfaceNamingSuffix", "viewModelInterfaceSubPackageName", p_oLanguageConfiguration);
		this.readNamingRule(p_xLanguageConf.element("viewmodel-implementation"), "viewModelImplementationNamingPrefix", "viewModelImplementationNamingSuffix", "viewModelImplementationSubPackageName", p_oLanguageConfiguration);
		this.readNamingRule(p_xLanguageConf.element("adapter-implementation"), "adapterImplementationNamingPrefix", "adapterImplementationNamingSuffix", "adapterImplementationSubPackageName", p_oLanguageConfiguration);
		this.readNamingRule(p_xLanguageConf.element("action-implementation"), "actionImplementationNamingPrefix", "actionImplementationNamingSuffix", "actionImplementationSubPackageName", p_oLanguageConfiguration);
		this.readNamingRule(p_xLanguageConf.element("action-interface"), "actionInterfaceNamingPrefix", "actionInterfaceNamingSuffix", "actionInterfaceSubPackageName", p_oLanguageConfiguration);		
		this.readNamingRule(p_xLanguageConf.element("screen"), "screenNamingPrefix", "screenNamingSuffix", "screenSubPackageName", p_oLanguageConfiguration);
		this.readNamingRule(p_xLanguageConf.element("section-interface"), "sectionInterfaceNamingPrefix", "sectionInterfaceNamingSuffix", "sectionInterfaceSubPackageName", p_oLanguageConfiguration);
		this.readNamingRule(p_xLanguageConf.element("section-implementation"), "sectionImplementationNamingPrefix", "sectionImplementationNamingSuffix", "sectionImplementationSubPackageName", p_oLanguageConfiguration);
		this.readNamingRule(p_xLanguageConf.element("dataloader-name"), "dataloaderImplementationNamingPrefix", "dataloaderImplementationNamingSuffix", "dataloaderImplementationSubPackageName", p_oLanguageConfiguration);
	}
	
	
	/**
	 * Read naming rule for a type of objects
	 * @param p_xTypeNamingRule node description naming rules for the type
	 * @param p_sPrefixPropertyName prefix property name of language configuration
	 * @param p_sSuffixPropertyName suffix property name of language configuration
	 * @param p_sSubPackagePropertyName subpackage property name of language configuration
	 * @param p_oLngConf language configuration
	 * @throws Exception exception
	 */
	private void readNamingRule( Element p_xTypeNamingRule, String p_sPrefixPropertyName, String p_sSuffixPropertyName,
			String p_sSubPackagePropertyName, LanguageConfiguration p_oLngConf) throws Exception {
		if ( p_xTypeNamingRule != null ) {
			BeanUtils.setIfNotNull( p_oLngConf, p_sPrefixPropertyName, p_xTypeNamingRule.elementText("prefix-naming"));
			BeanUtils.setIfNotNull( p_oLngConf, p_sSuffixPropertyName, p_xTypeNamingRule.elementText("suffix-naming"));
			BeanUtils.setIfNotNull( p_oLngConf, p_sSubPackagePropertyName, p_xTypeNamingRule.elementText("sub-package-name"));
		}
	}
	
	
	/**
	 * Read view model types
	 * @param p_xViewModelTypes xml node of view model types
	 */
	private void readViewModelTypes( Element p_xViewModelTypes ) {
		if (p_xViewModelTypes!=null) {
			for(Element xTypeViewModel : (List<Element>)p_xViewModelTypes.elements("type-viewmodel")) {
			
				String sName = xTypeViewModel.attributeValue("id");
				//on cherche l'enum correspondante
				ViewModelType oCurrentViewModelType = null;
				for(ViewModelType oType :ViewModelType.values()) {
					if (oType.name().equals(sName)) {
						oCurrentViewModelType = oType;
						break;
					}
				}
				if (oCurrentViewModelType!=null) {
					this.readViewModelTypeConfiguration(xTypeViewModel, oCurrentViewModelType);
				}
			}
		}
	}
	
	
	/**
	 * Read view model type configuration
	 * @param p_xViewModelType xml node of configuration
	 * @param p_oViewModelType view model type
	 */
	private void readViewModelTypeConfiguration( Element p_xViewModelType, ViewModelType p_oViewModelType ) {
		for(Element xConfigurations : (List<Element>) p_xViewModelType.elements("configuration")) {
			
			String sOption = xConfigurations.attributeValue("name");						
			Element xListInterface = xConfigurations.element("list-interface");
			String sListInterfaceRef = null;
			String sListInterfaceLongRef = null;
			if (xListInterface!=null) {
				sListInterfaceLongRef = xListInterface.attributeValue("ref");
				sListInterfaceRef = StrUtils.substringAfterLastDot(sListInterfaceLongRef);
			}
			Element xItemInterface = xConfigurations.element("item-interface");
			String sItemIterfaceRef = null;
			String sItemInterfaceLongRef = null;
			if (xItemInterface!=null) {
				sItemInterfaceLongRef = xItemInterface.attributeValue("ref");
				sItemIterfaceRef = StrUtils.substringAfterLastDot(sItemInterfaceLongRef);
			}
			Element xSubItemInterface = xConfigurations.element("subitem-interface");
			String sSubItemInterfaceRef = null;
			String sSubItemInterfaceLongRef = null;
			if (xSubItemInterface!=null) {
				sSubItemInterfaceLongRef = xSubItemInterface.attributeValue("ref");
				sSubItemInterfaceRef = StrUtils.substringAfterLastDot(sSubItemInterfaceLongRef);
			}
			Element xAdapter = xConfigurations.element("adapter");
			String sAdapterRef = null;
			String sAdapterLongRef = null;
			if (xAdapter!=null) {
				sAdapterLongRef = xAdapter.attributeValue("ref");
				sAdapterRef = StrUtils.substringAfterLastDot(sAdapterLongRef);
			}
			Element xComponent = xConfigurations.element("component");
			String sComponentRef = null;
			String sComponentLongRef = null;
			if (xComponent!=null) {
				sComponentLongRef = xComponent.attributeValue("ref");
				sComponentRef = StrUtils.substringAfterLastDot(sComponentLongRef);
			}
			Element xMaxLevel = xConfigurations.element("max-level");
			int iMaxLevel = 0;
			if (xMaxLevel != null) {
				iMaxLevel = Integer.parseInt(xMaxLevel.getTextTrim());
			}
			Element xProperties = xConfigurations.element("properties");
			Map<String, String> propertyList = new HashMap<String, String>();

			if (xProperties != null) {
				
				//Récupération de l'ensemble des propriétés dynamiques définies pour ce view model
				Iterator propertyListIterator = xProperties.elementIterator();
				
		        while (propertyListIterator.hasNext()) {
		        	Element readProperty = (Element)propertyListIterator.next();
		        	//Chaque propriété lue est stockée dans la liste des propriétés
		        	propertyList.put(readProperty.getName(), readProperty.getTextTrim());
		        }
				
			}
			
			log.debug("set value {}/{}/{}/{}/{}/{}/{}/{}/{}/{}/{}",
				new Object[] {sOption, sListInterfaceRef, sListInterfaceLongRef, sItemIterfaceRef, sItemInterfaceLongRef,
					sComponentRef, sComponentLongRef, sAdapterRef, sAdapterLongRef, sSubItemInterfaceRef, sSubItemInterfaceLongRef});
			
			p_oViewModelType.update(
				sOption,sListInterfaceRef, sListInterfaceLongRef, sItemIterfaceRef, sItemInterfaceLongRef, 
				sComponentRef, sComponentLongRef, sAdapterRef, sAdapterLongRef, sSubItemInterfaceRef, 
				sSubItemInterfaceLongRef, iMaxLevel, propertyList);
		}
	}
	
	/**
	 * Read action types
	 * @param p_xActionTypes xml node of action types 
	 */
	private void readActionTypes( Element p_xActionTypes ) {
		if (p_xActionTypes!=null) {
			for(Element xAction : (List<Element>)p_xActionTypes.elements("type-action")) {
				String sName = xAction.attributeValue("id");
				//on cherche l'enum correspondante
				MActionType oCurrentActionType = null;
				for(MActionType oType :MActionType.values()) {
					if (oType.name().equals(sName)) {
						oCurrentActionType = oType;
						break;
					}
				}
				if (oCurrentActionType!=null) {
					oCurrentActionType.setParamInFullName(xAction.elementText("in"));
					oCurrentActionType.setParamOutFullName(xAction.elementText("out"));
					oCurrentActionType.setParamStepFullName(xAction.elementText("step"));
					oCurrentActionType.setParamProgressFullName(xAction.elementText("progress"));
				}
			}
		}
	}
	
	/**
	 * Read ui type description
	 * @param p_oLanguageConfiguration language configuration
	 * @param p_xUiTypes ui type xml node
	 * @throws Exception exception
	 */
	private void readUITypeDescriptions(
			LanguageConfiguration p_oLanguageConfiguration, Element p_xUiTypes) throws Exception {
		if (p_xUiTypes!=null) {
			UITypeDescriptionFactory oUITypeDescFactory = p_oLanguageConfiguration.getUITypeDescriptionFactory();
			for(Element xUiType : (List<Element>)p_xUiTypes.elements("ui-type")) {
				IUITypeDescription oTypeDesc = oUITypeDescFactory.createUITypeDescription(xUiType);
				p_oLanguageConfiguration.addUiTypeDescription(oTypeDesc.getUmlName(), oTypeDesc);
			}
		}
	}
	
	/**
	 * Lit les descriptions des types Java dans le noeud xml
	 * @param p_xParent noeud parent contenant la description des types xml
	 * @param p_oLanguageConfiguration language configuration
	 * @throws Exception exception
	 */
	private void readTypeDescriptions( Element p_xParent, LanguageConfiguration p_oLanguageConfiguration ) throws Exception {
		if ( p_xParent != null ) {
			
			String sReplace = p_xParent.attributeValue("replace");
			if ( sReplace != null && Boolean.parseBoolean(sReplace)) {
				p_oLanguageConfiguration.getTypeDescriptions().clear();
			}
			
			TypeDescriptionFactory oTypeDescFactory = p_oLanguageConfiguration.getTypeDescriptionFactory();

			for( Element xElement : (List<Element>) p_xParent.elements("type")) {
				ITypeDescription oTypeDesc = oTypeDescFactory.createTypeDescription(xElement);
				log.debug("register: type description: {}", oTypeDesc.getUmlName());
				p_oLanguageConfiguration.addTypeDescription(oTypeDesc.getUmlName(), oTypeDesc);	        	
			}
		}
	}
	
	/**
	 * Define the type description for each property of the composite types
	 */
	private void linkPropertiesOfCompositeTypes( LanguageConfiguration oLngConfiguration ) throws AdjavaException {
		for( ITypeDescription oTypeDesc : oLngConfiguration.getTypeDescriptions().values()) {
			if ( oTypeDesc.isComposite()) {
				for( Property oProperty : oTypeDesc.getProperties()) {
					ITypeDescription oPropTypeDesc = oLngConfiguration.getTypeDescription(oProperty.getTypeName());
					if ( oPropTypeDesc != null ) {
						oProperty.setTypeDescription((ITypeDescription)oPropTypeDesc.clone());
					}
					else {
						throw new AdjavaException("Can't find type of property '{}' of type '{}'. Check your type configuration.",
							oProperty.getTypeName(), oTypeDesc.getUmlName());
					}
				}
			}
		}
	}
}
