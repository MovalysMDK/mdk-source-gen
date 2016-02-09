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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.a2a.adjava.datatypes.DataType;
import com.a2a.adjava.types.ITypeDescription.Property;
import com.a2a.adjava.utils.BeanUtils;

/**
 * <p>
 * TypeDescriptionFactory
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

public class TypeDescriptionFactory {

	/**
	 * Create a description type from an xml configuration
	 * @param p_xType xml configuration
	 * @return DescriptionType
	 */
	public ITypeDescription createTypeDescription(Element p_xType) throws Exception {
		TypeDescription r_oTypeDesc = new TypeDescription();
		fillTypeDescription(p_xType, r_oTypeDesc);
		return r_oTypeDesc;
	}

	/**
	 * TODO Décrire la méthode fillTypeDescription de la classe TypeDescriptionFactory
	 * @param p_xType
	 * @param p_oTypeDescription
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	protected void fillTypeDescription(Element p_xType, ITypeDescription p_oTypeDescription ) throws Exception {
		TypeDescription oTypeDescription = (TypeDescription) p_oTypeDescription;
		oTypeDescription.setUmlName(p_xType.elementText("uml-type").trim());
		
		BeanUtils.setStringIfNotNull(oTypeDescription, "name", p_xType.elementText("language-type"));
		BeanUtils.setStringIfNotNull(oTypeDescription, "initFormat", p_xType.elementText("init-format"));
		BeanUtils.setStringIfNotNull(oTypeDescription, "defaultValue", p_xType.elementText("default-value"));
		BeanUtils.setStringIfNotNull(oTypeDescription, "variablePrefix", p_xType.elementText("variable-prefix"));
		BeanUtils.setStringIfNotNull(oTypeDescription, "parameterPrefix", p_xType.elementText("parameter-prefix"));		
		BeanUtils.setStringIfNotNull(oTypeDescription, "returnPrefix", p_xType.elementText("return-prefix"));		
		BeanUtils.setStringIfNotNull(oTypeDescription, "getAccessorPrefix", p_xType.elementText("get-accessor-prefix"));
		BeanUtils.setStringIfNotNull(oTypeDescription, "setAccessorPrefix", p_xType.elementText("set-accessor-prefix"));
		BeanUtils.setStringIfNotNull(oTypeDescription, "wrapperName", p_xType.elementText("wrapper"));
		BeanUtils.setStringIfNotNull(oTypeDescription, "unsavedValue", p_xType.elementText("unsaved-value"));
		BeanUtils.setStringIfNotNull(oTypeDescription, "defaultOptions", p_xType.elementText("default-options"));
		BeanUtils.setStringIfNotNull(oTypeDescription, "sqlType", p_xType.elementText("sql-type"));
		BeanUtils.setStringIfNotNull(oTypeDescription, "sqlCompare", p_xType.elementText("sql-compare"));
		BeanUtils.setEnumIfNotNull(oTypeDescription, "dataType", p_xType.elementText("data-type"), DataType.class);
		BeanUtils.setEnumIfNotNull(oTypeDescription, "expandableType", p_xType.elementText("expandAs"), ExpandableType.class);	
		
		BeanUtils.setStringIfNotNull(oTypeDescription, "editType", p_xType.elementText("edit-type"));
		BeanUtils.setStringIfNotNull(oTypeDescription, "defaultUiType", p_xType.elementText("default-ui-type"));
		
		Element xTcs = p_xType.element("type-conversions");
		String sKey = null;
		String sFormula = null;
		List<String> lstImports = null;
		if (xTcs!=null) {
			for(Element xTc : (List<Element>)xTcs.elements("type-conversion")) {
				sKey = xTc.attributeValue("to");
				sFormula = xTc.elementText("formula");
				lstImports = new ArrayList<String>();
				for(Element xImport : (List<Element>)xTc.elements("import")) {
					lstImports.add(xImport.getText());
				}
				oTypeDescription.addTypeConvertion(sKey, sFormula, lstImports);
			}
		}
		
		Element xStereotypes = p_xType.element("stereotypes");
		if ( xStereotypes != null ) {
			for( Element xProperty : (List<Element>) xStereotypes.elements("stereotype")) {
				oTypeDescription.addStereotype(xProperty.getText());
			}
		}
		
		Element xProperties = p_xType.element("properties");
		if ( xProperties != null ) {
			for( Element xProperty : (List<Element>) xProperties.elements("property")) {
				String sPropName = xProperty.attributeValue("name");
				String sTypeName = xProperty.attributeValue("type");
				String sDefaultValue = xProperty.attributeValue("default-value");
				String sDefaultOptions = xProperty.attributeValue("default-options");
				Property oProperty = new Property();
				oProperty.setName(sPropName);
				oProperty.setTypeName(sTypeName);
				oProperty.setDefaultValue(sDefaultValue);
				oProperty.setDefaultOptions(sDefaultOptions);
				oTypeDescription.addProperty(oProperty);
			}
		}
		
		Element xEnum = p_xType.element("enum");
		if ( xEnum != null ) {
			for( Element xValue : (List<Element>) xEnum.elements("value")) {
				oTypeDescription.getEnumValues().add(xValue.getText());
			}
			oTypeDescription.setEnumeration(true);
		}
	}
}
