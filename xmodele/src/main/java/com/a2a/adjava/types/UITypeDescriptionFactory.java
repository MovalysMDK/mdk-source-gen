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
package com.a2a.adjava.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UI Type description
 * @author lmichenaud
 *
 */
public class UITypeDescriptionFactory {
	
	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(UITypeDescriptionFactory.class);
	
	/**
	 * Separator in the view model type description 
	 */
	public static final String VIEW_MODEL_TYPE_DEFINITION_SEPARATOR = "\\|" ;
	
	/**
	 * Text indicating the storage type is not defined but use the entity type  
	 */
	public static final String VIEW_MODEL_TYPE_DEFINITION_SAME_AS_ENTITY_TYPE = "SameAsEntity" ;
	
	/**
	 * Create a ui type description from an xml configuration
	 * @param p_xType xml configuration
	 * @return IUITypeDescription
	 */
	public IUITypeDescription createUITypeDescription(Element p_xType) throws Exception {
		UITypeDescription r_oTypeDesc = new UITypeDescription();
		fillUITypeDescription(p_xType, r_oTypeDesc);
		return r_oTypeDesc;
	}
	
	/**
	 * Fill UI Type description using xml element
	 * @param p_xType xml element
	 * @param p_oTypeDescription UI Type description
	 * @throws Exception 
	 */
	protected void fillUITypeDescription(Element p_xUiType, UITypeDescription p_oTypeDescription ) throws Exception {
		p_oTypeDescription.setUmlName(p_xUiType.attributeValue("uml-name"));
		
		Element xRw = p_xUiType.element("read-write");
		if (xRw != null && xRw.elementText("view-model-type") != null ) {
			String[] aViewModelTypeDef = xRw.elementText("view-model-type").split(VIEW_MODEL_TYPE_DEFINITION_SEPARATOR);
			if (aViewModelTypeDef.length == 2){
				p_oTypeDescription.setSameTypeAsEntityForReadWrite(
						aViewModelTypeDef[0].equalsIgnoreCase(VIEW_MODEL_TYPE_DEFINITION_SAME_AS_ENTITY_TYPE) );
				p_oTypeDescription.setRWViewModelType(aViewModelTypeDef[1].trim());
			}else {
				p_oTypeDescription.setRWViewModelType(aViewModelTypeDef[0].trim());
				p_oTypeDescription.setSameTypeAsEntityForReadWrite(false);
			}
			p_oTypeDescription.setRWComponentType(xRw.elementText("language-type").trim());
			
			Element langageTypeOptions = xRw.element("language-type-options");
			//Si des options ont été spécifiées dans la configuration
			if (langageTypeOptions != null) {
				
				//Parcours des options pour les ajouter
				for( Element xOption : (List<Element>)langageTypeOptions.elements() ) {
					
					if (p_oTypeDescription.getRWLanguageTypeOptions() == null) {
						p_oTypeDescription.setRWLanguageTypeOptions(new HashMap<String, String>());
					}

					p_oTypeDescription.addRWLanguageTypeOption(
							xOption.getName(), 
							xOption.getTextTrim());
				}
				
			}
		}

		Element xRo = p_xUiType.element("read-only");
		if (xRo != null && xRo.elementText("view-model-type") != null ) {
			String[] aViewModelTypeDef = xRo.elementText("view-model-type").split(VIEW_MODEL_TYPE_DEFINITION_SEPARATOR);
			if (aViewModelTypeDef.length == 2){
				p_oTypeDescription.setSameTypeAsEntityForReadOnly(
						aViewModelTypeDef[0].equalsIgnoreCase(VIEW_MODEL_TYPE_DEFINITION_SAME_AS_ENTITY_TYPE) ) ;
				p_oTypeDescription.setROViewModelType(aViewModelTypeDef[1].trim());
			}else {
				p_oTypeDescription.setROViewModelType(aViewModelTypeDef[0].trim());
				p_oTypeDescription.setSameTypeAsEntityForReadOnly(false);
			}
			p_oTypeDescription.setROComponentType(xRo.elementText("language-type").trim());
			
			Element langageTypeOptions = xRw.element("language-type-options");
			//Si des options ont été spécifiées dans la configuration
			if (langageTypeOptions != null) {
				
				//Parcours des options pour les ajouter
				for( Element xOption : (List<Element>)langageTypeOptions.elements() ) {
					
					if (p_oTypeDescription.getROLanguageTypeOptions() == null) {
						p_oTypeDescription.setROLanguageTypeOptions(new HashMap<String, String>());
					}
					
					p_oTypeDescription.addROLanguageTypeOption(
							xOption.getName(), 
							xOption.getTextTrim());
				}
				
			}
		}
	}
}
