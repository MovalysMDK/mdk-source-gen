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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.datatypes.DataType;
import com.a2a.adjava.utils.StrUtils;
import com.a2a.adjava.xmodele.MAttribute;

/**
 * Type Description
 * @author lmichenaud
 *
 */
public class TypeDescription implements ITypeDescription, Cloneable {

	/**
	 * Uml name
	 */
	private String umlName;
	
	/**
	 * Name (package included) 
	 */
	private String name;
	
	/**
	 * Short name (without package) 
	 */
	private String shortName;
	
	/**
	 * Init format 
	 */
	private String initFormat;
	
	/**
	 * Default value 
	 */
	private String defaultValue;
	
	/**
	 * Variable prefix 
	 */
	private String variablePrefix;
	
	/**
	 * Parameter prefix 
	 */
	private String parameterPrefix;
	
	/**
	 * Return prefix 
	 */
	private String returnPrefix;
	
	/**
	 * Getter prefix 
	 */
	private String getAccessorPrefix;
	
	/**
	 * Setter prefix 
	 */
	private String setAccessorPrefix;
	
	/**
	 * True if primitive type
	 */
	private boolean primitif;
	
	/**
	 * Is enumeration 
	 */
	private boolean enumeration;
	
	/**
	 * Name of the wrapper (valued if type is a primitive)
	 */
	private String wrapperName;
	
	/**
	 * Type description of the wrapper 
	 */
	private ITypeDescription wrapper;
	
	/**
	 * Unsaved value 
	 */
	private String unsavedValue;
	
	/**
	 * Default options 
	 */
	private String defaultOptions;
	
	/**
	 * Parameterized types 
	 */
	private List<ITypeDescription> parameterizedElementTypes = new ArrayList<ITypeDescription>();
	
	/**
	 * Sql types 
	 */
	private String sqlType;
	
	/**
	 * Sql keyword for comparaison
	 */
	private String sqlCompare;
	
	/**
	 * Global datatype 
	 */
	private DataType dataType;
	
	/**
	 * UI Edit type 
	 */
	private String editType;
	
	/**
	 * Default ui type
	 */
	private String defaultUiType ;
	
	/**
	 * Type convertions 
	 */
	private Map<String, ITypeConvertion> convertions = new TreeMap<String, ITypeConvertion>();
	
	/**
	 * Properties of a composite type
	 */
	private List<Property> properties = new ArrayList<Property>();

	/**
	 * Expandable type
	 */
	private ExpandableType expandableType = ExpandableType.NONE;

	/**
	 * Stereotypes to add
	 */
	private List<String> stereotypes = new ArrayList<String>();

	/**
	 * Enum values
	 */
	private List<String> enumValues = new ArrayList<String>();
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#setPrimitif(boolean)
	 */
	@Override
	public void setPrimitif(boolean p_bPrimitif) {
		this.primitif = p_bPrimitif;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getDefaultValue()
	 */
	@Override
	public String getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getInitFormat()
	 */
	@Override
	public String getInitFormat() {
		return this.initFormat;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getUmlName()
	 */
	@Override
	public String getUmlName() {
		return this.umlName;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getParameterPrefix()
	 */
	@Override
	public String getParameterPrefix() {
		return this.parameterPrefix;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getVariablePrefix()
	 */
	@Override
	public String getVariablePrefix() {
		return this.variablePrefix;
	}

	/**
	 * Return result prefix
	 * @return result prefix
	 */
	public String getReturnPrefix() {
		return this.returnPrefix;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getShortName()
	 */
	@Override
	public String getShortName() {
		return this.shortName;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#isPrimitif()
	 */
	@Override
	public boolean isPrimitif() {
		return this.primitif;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getGetAccessorPrefix()
	 */
	@Override
	public String getGetAccessorPrefix() {
		return this.getAccessorPrefix;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getSetAccessorPrefix()
	 */
	@Override
	public String getSetAccessorPrefix() {
		return this.setAccessorPrefix;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getWrapper()
	 */
	@Override
	public ITypeDescription getWrapper() {
		return this.wrapper;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#setWrapper(com.a2a.adjava.types.ITypeDescription)
	 */
	@Override
	public void setWrapper(ITypeDescription p_oWrapper) {
		this.wrapper = p_oWrapper;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getWrapperName()
	 */
	@Override
	public String getWrapperName() {
		return this.wrapperName;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getUnsavedValue()
	 */
	@Override
	public String getUnsavedValue() {
		return this.unsavedValue;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getDefaultOptions()
	 */
	@Override
	public String getDefaultOptions() {
		return this.defaultOptions;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#setName(java.lang.String)
	 */
	@Override
	public void setName(String p_sName) {
		this.name = p_sName;
		this.shortName = StrUtils.substringAfterLastDot(this.name);
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#isEnumeration()
	 */
	@Override
	public boolean isEnumeration() {
		return enumeration;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#setEnumeration(boolean)
	 */
	@Override
	public void setEnumeration(boolean p_bEnumeration) {
		this.enumeration = p_bEnumeration;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getParameterizedElementType()
	 */
	@Override
	public List<ITypeDescription> getParameterizedElementType() {
		return this.parameterizedElementTypes;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#setParameterizedElementType(com.a2a.adjava.types.ITypeDescription[])
	 */
	@Override
	public void setParameterizedElementType(ITypeDescription... p_oElementType) {
		for (ITypeDescription oType : p_oElementType) {
			this.parameterizedElementTypes.add(oType);
		}
	}

	/**
	 * Set parameterized types
	 * @param p_oList parameterized types
	 */
	public void setParameterizedElementType(List<ITypeDescription> p_oList) {
		this.parameterizedElementTypes.addAll(p_oList);
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getSqlType()
	 */
	@Override
	public String getSqlType() {
		return this.sqlType;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getDataType()
	 */
	@Override
	public DataType getDataType() {
		return this.dataType;
	}

	/**
	 * Return sql keyword for comparaison
	 * 
	 * @return Objet sql keyword for comparaison
	 */
	public String getSqlCompare() {
		return this.sqlCompare;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#setUmlName(java.lang.String)
	 */
	@Override
	public void setUmlName(String p_sUmlName) {
		this.umlName = p_sUmlName;
	}

	/**
	 * Set init format
	 * @param p_sInitFormat init format
	 */
	public void setInitFormat(String p_sInitFormat) {
		this.initFormat = p_sInitFormat;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#setDefaultValue(java.lang.String)
	 */
	@Override
	public void setDefaultValue(String p_sDefaultValue) {
		this.defaultValue = p_sDefaultValue;
	}

	/**
	 * Set variable prefix
	 * @param p_sVariablePrefix variable prefix
	 */
	public void setVariablePrefix(String p_sVariablePrefix) {
		this.variablePrefix = p_sVariablePrefix;
	}

	/**
	 * Set parameter prefix
	 * @param p_sParameterPrefix parameter prefix
	 */
	public void setParameterPrefix(String p_sParameterPrefix) {
		this.parameterPrefix = p_sParameterPrefix;
	}

	/**
	 * Set return prefix
	 * 
	 * @param p_sReturnPrefix return prefix
	 */
	public void setReturnPrefix(String p_sReturnPrefix) {
		this.returnPrefix = p_sReturnPrefix;
	}

	/**
	 * Set prefix for getter
	 * 
	 * @param p_sGetAccessorPrefix prefix for getter
	 */
	public void setGetAccessorPrefix(String p_sGetAccessorPrefix) {
		this.getAccessorPrefix = p_sGetAccessorPrefix;
	}

	/**
	 * Set prefix for setter
	 * 
	 * @param p_sSetAccessorPrefix prefix for setter
	 */
	public void setSetAccessorPrefix(String p_sSetAccessorPrefix) {
		this.setAccessorPrefix = p_sSetAccessorPrefix;
	}

	/**
	 * Set wrapper name
	 * 
	 * @param p_sWrapperName wrapper name
	 */
	public void setWrapperName(String p_sWrapperName) {
		this.wrapperName = p_sWrapperName;
	}

	/**
	 * Set unsaved value
	 * 
	 * @param p_sUnsavedValue unsaved value
	 */
	public void setUnsavedValue(String p_sUnsavedValue) {
		this.unsavedValue = p_sUnsavedValue;
	}

	/**
	 * Set default options
	 * 
	 * @param p_sDefaultOptions default options
	 */
	public void setDefaultOptions(String p_sDefaultOptions) {
		this.defaultOptions = p_sDefaultOptions;
	}

	/**
	 * Set sql type
	 * 
	 * @param p_sSqlType sql type
	 */
	public void setSqlType(String p_sSqlType) {
		this.sqlType = p_sSqlType;
	}

	/**
	 * Set sql keyword for comparaison
	 * 
	 * @param p_sSqlCompare sql keyword for comparaison
	 */
	public void setSqlCompare(String p_sSqlCompare) {
		this.sqlCompare = p_sSqlCompare;
	}

	/**
	 * Set datatype
	 * 
	 * @param p_oDataType datatype
	 */
	public void setDataType(DataType p_oDataType) {
		this.dataType = p_oDataType;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getEditType()
	 */
	@Override
	public String getEditType() {
		return this.editType;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#setEditType(java.lang.String)
	 */
	@Override
	public void setEditType(String p_sEditType) {
		this.editType = p_sEditType;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#setDefaultUiType(java.lang.String)
	 */
	@Override
	public void setDefaultUiType(String p_sDefaultUiType) {
		this.defaultUiType = p_sDefaultUiType;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getDefaultUiType()
	 */
	@Override
	public String getDefaultUiType() {
		return this.defaultUiType;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#addTypeConvertion(java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	public void addTypeConvertion(String p_sTo, String p_sFormula, List<String> p_lstImports) {
		TypeConvertion oConversion = new TypeConvertion();
		oConversion.setTo(p_sTo);
		oConversion.setFormula(p_sFormula);
		oConversion.setImports(p_lstImports);
		this.convertions.put(p_sTo, oConversion);
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getConvertion(com.a2a.adjava.types.ITypeDescription)
	 */
	@Override
	public ITypeConvertion getConvertion(ITypeDescription p_oType) {
		return this.convertions.get(p_oType.getUmlName());
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getConvertion(java.lang.String)
	 */
	@Override
	public ITypeConvertion getConvertion(String p_sUmlName) {
		return this.convertions.get(p_sUmlName);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getConvertionTypeNames()
	 */
	@Override
	public Collection<String> getConvertionTypeNames() {
		return this.convertions.keySet();
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getConvertions()
	 */
	@Override
	public Collection<ITypeConvertion> getConvertions() {
		return this.convertions.values();
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#isComposite()
	 */
	@Override
	public boolean isComposite() {
		return !this.properties.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getProperties()
	 */
	@Override
	public List<Property> getProperties() {
		return this.properties;
	}
	
	/**
	 * Add a property
	 * @param p_oProperty property to add
	 */
	public void addProperty( Property p_oProperty ) {
		this.properties.add(p_oProperty);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getExpandableType()
	 */
	@Override
	public ExpandableType getExpandableType() {
		return this.expandableType;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#setExpandableType(com.a2a.adjava.types.ExpandableType)
	 */
	public void setExpandableType(ExpandableType p_oExpandableType) {
		this.expandableType = p_oExpandableType;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getEntityStereotypes()
	 */
	@Override
	public List<String> getStereotypes() {
		return this.stereotypes;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#addStereotype(java.lang.String)
	 */
	@Override
	public void addStereotype(String p_sStereotypeName) {
		this.stereotypes.add(p_sStereotypeName);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#getEnumValues()
	 */
	public List<String> getEnumValues() {
		return this.enumValues;
	}

	/**
	 * {@inheritDoc}
	 * @param p_listEnumValues
	 */
	public void setEnumValues(List<String> p_listEnumValues) {
		this.enumValues = p_listEnumValues;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#computeParameterName(java.lang.String)
	 */
	@Override
	public String computeParameterName(String p_sBaseName) {
		String r_sParameterName = null ;
		if ( StringUtils.isNotEmpty(this.getParameterPrefix())) {
			r_sParameterName = this.getParameterPrefix() + StringUtils.capitalize(p_sBaseName);
		}
		else {
			r_sParameterName = StringUtils.uncapitalize(p_sBaseName);
		}
		return r_sParameterName;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#computeVariableName(java.lang.String)
	 */
	@Override
	public String computeVariableName(String p_sBaseName) {
		String r_sVariableName = null ;
		if ( StringUtils.isNotEmpty(this.variablePrefix)) {
			r_sVariableName = this.variablePrefix + StringUtils.capitalize(p_sBaseName);
		}
		else {
			r_sVariableName = StringUtils.uncapitalize(p_sBaseName);
		}
		return r_sVariableName;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.ITypeDescription#computeListVariableName(java.lang.String)
	 */
	@Override
	public String computeListVariableName(String p_sBaseName) {
		String r_sListVariableName = null ;
		if ( StringUtils.isNotEmpty(this.variablePrefix)) {
			r_sListVariableName = this.getVariablePrefix() + StringUtils.capitalize(p_sBaseName);
		}
		else {
			r_sListVariableName = StringUtils.uncapitalize(p_sBaseName);
		}
		return r_sListVariableName;
	}
	
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		TypeDescription r_oTypeDescription = new TypeDescription();
		this.copyPropertiesTo(r_oTypeDescription);
		return r_oTypeDescription;
	}

	/**
	 * Copy instance to another instance
	 * @param p_oTypeDescription target instance
	 */
	protected void copyPropertiesTo(ITypeDescription p_oTypeDescription) {
		TypeDescription oTypeDescription = (TypeDescription) p_oTypeDescription;
		oTypeDescription.setUmlName(this.umlName);
		oTypeDescription.setName(this.name);
		oTypeDescription.setInitFormat(this.initFormat);
		oTypeDescription.setDefaultValue(this.defaultValue);
		oTypeDescription.setVariablePrefix(this.variablePrefix);
		oTypeDescription.setParameterPrefix(this.parameterPrefix);
		oTypeDescription.setReturnPrefix(this.returnPrefix);
		oTypeDescription.setGetAccessorPrefix(this.getAccessorPrefix);
		oTypeDescription.setSetAccessorPrefix(this.setAccessorPrefix);
		oTypeDescription.setWrapperName(this.wrapperName);
		oTypeDescription.setUnsavedValue(this.unsavedValue);
		oTypeDescription.setDefaultOptions(this.defaultOptions);
		for (ITypeDescription oType : this.parameterizedElementTypes) {
			oTypeDescription.setParameterizedElementType((ITypeDescription) oType.clone());
		}
		oTypeDescription.setSqlType(this.sqlType);
		oTypeDescription.setSqlCompare(this.sqlCompare);
		oTypeDescription.setDataType(this.dataType);
		oTypeDescription.setPrimitif(this.primitif);
		oTypeDescription.setWrapper(this.wrapper);
		oTypeDescription.setEnumeration(this.enumeration);
		oTypeDescription.setEditType(this.editType);
		oTypeDescription.setDefaultUiType(this.defaultUiType);
		oTypeDescription.setExpandableType(this.expandableType);
		for( Property oProperty : this.properties ) {
			Property oClonedProperty = new Property();
			oProperty.copyTo(oClonedProperty);
			oTypeDescription.addProperty(oClonedProperty);
		}
		for( String sEnumValue : this.enumValues ) {
			oTypeDescription.getEnumValues().add(sEnumValue);
		}
		for (ITypeConvertion oTypeConvertion : this.getConvertions()) {
			oTypeDescription.addTypeConvertion(oTypeConvertion.getTo(), oTypeConvertion.getFormula(),
					oTypeConvertion.getImports());
		}
	}
}
