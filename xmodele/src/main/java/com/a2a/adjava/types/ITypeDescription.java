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

import java.util.Collection;
import java.util.List;

import com.a2a.adjava.datatypes.DataType;

/**
 * <p>Type de donnees</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */

public interface ITypeDescription {

	/**
	 * Return full name of type description
	 * @return name
	 */
	public String getName();
	
	/**
	 * Return short name of type description
	 * @return full name
	 */
	public String getShortName();
	
	/**
	 * Set full name of type description 
	 * This name must include package.
	 * Short name is recomputed from this name.
	 * @param p_sName le nom avec package
	 */
	public void setName( String p_sName );
	
	/**
	 * Return Uml Name
	 * @return uml name
	 */
	public String getUmlName();
	
	/**
	 * Set uml name
	 * @param p_sUmlName uml name
	 */
	public void setUmlName( String p_sUmlName );
	
	/**
	 * Return true if it is a primitive type
	 * @return true if primitive type
	 */
	public boolean isPrimitif();
	
	/**
	 * Set if type is primitive
	 * @param p_bPrimitif primitive
	 */
	public void setPrimitif(boolean p_bPrimitif);
	
	/**
	 * Return the wrapper type if type is primitive
	 * @return the wrapper type
	 */
	public ITypeDescription getWrapper();
	
	/**
	 * Set the wrapper type (for primitive type)
	 * @param p_oWrapper wrapper type
	 */
	public void setWrapper(ITypeDescription p_oWrapper);
	
	/**
	 * Return the name of the wrapper
	 * @return name of the wrapper
	 */
	public String getWrapperName();
	
	/**
	 * Return the value indicating that the entity has not yet been saved.
	 * @return unsaved value
	 */
	public String getUnsavedValue();
	
	/**
	 * Return the default options
	 * @return default options
	 */
	public String getDefaultOptions();
	
	/**
	 * Define default options
	 * @param p_sDefaultOptions default options
	 */
	public void setDefaultOptions( String p_sDefaultOptions );
	
	/**
	 * Return the default value
	 * @return default value
	 */
	public String getDefaultValue();
	
	/**
	 * Define the default value
	 * @param p_sDefaultValue default value
	 */
	public void setDefaultValue(String p_sDefaultValue );
	
	/**
	 * Return init format
	 * @return init format
	 */
	public String getInitFormat();
	
	/**
	 * Define init format
	 * @param p_sInitFormat default value
	 */
	public void setInitFormat(String p_sInitFormat);
	
	/**
	 * Return true if type is enumeration
	 * @return true if type is enumeration
	 */
	public boolean isEnumeration();
	
	/**
	 * Define type as Enumeration
	 * @param p_bEnumeration true if enumeration
	 */
	public void setEnumeration(boolean p_bEnumeration);
	
	/**
	 * Return dataType
	 * @return data type
	 */
	public DataType getDataType();
		
	/**
	 * Return the prefix for parameter
	 * @return prefix to use for parameter
	 */
	public String getParameterPrefix();
	
	/**
	 * Return the variable prefix
	 * @return prefix for variables
	 */
	public String getVariablePrefix();
	
	/**
	 * Return the prefix for getter
	 * @return prefix for getter
	 */
	public String getGetAccessorPrefix();

	/**
	 * Return the prefix for setter
	 * @return prefix for setter
	 */
	public String getSetAccessorPrefix();
	
	/**
	 * If list, return the type of element contained in the list
	 * @return type of parameterized element
	 */
	public List<ITypeDescription> getParameterizedElementType();
		
	/**
	 * Set the parameterized element type used by object
	 * @param p_oContainedElementType he parameterized element type used by object
	 */
	public void setParameterizedElementType(ITypeDescription...p_oContainedElementType);
	
	/**
	 * Return sql type
	 * @return sql type
	 */
	public String getSqlType();
	
	/**
	 * Define sql type
	 * @param p_sSqlType sql type
	 */
	public void setSqlType( String p_sSqlType );
	
	/**
	 * Clonage du type
	 * @return a clone
	 */
	public Object clone();
	
	/**
	 * Return edit type for ui component.
	 * (example: a numeric attribute is a text field with a numeric configuration (user can't input letters))
	 * @return edit type
	 */
	public String getEditType();
	
	/**
	 * Define edit type for ui component.
	 * (example: a numeric attribute is a text field with a numeric configuration (user can't input letters))
	 * @param p_sEditType edit type
	 */
	public void setEditType(String p_sEditType);
	
	/**
	 * Default ui type
	 * @param p_sDefaultUiType default ui type
	 */
	public void setDefaultUiType(String p_sDefaultUiType);
	
	/**
	 * Return the default ui type
	 * @return default ui type
	 */
	public String getDefaultUiType();
	
	/**
	 * Add a type convertion
	 * @param p_sTo target type
	 * @param p_sFormula convertion formula
	 * @param p_listImports needed imports 
	 */
	public void addTypeConvertion(String p_sTo, String p_sFormula, List<String> p_listImports);
	
	/**
	 * Return type convertion for type description
	 * @param p_oType type description
	 * @return type convertion
	 */
	public ITypeConvertion getConvertion(ITypeDescription p_oType);
	
	/**
	 * Return type convertion for uml type
	 * @param p_sUmlName name of uml type
	 * @return type convertion for uml type
	 */
	public ITypeConvertion getConvertion(String p_sUmlName);
	
	/**
	 * Return all type convertions
	 * @return all type convertions
	 */
	public Collection<ITypeConvertion> getConvertions();
	
	/**
	 * Return names of type convertions
	 * @return names of type convertions
	 */
	public Collection<String> getConvertionTypeNames();
	
	/**
	 * Return true if type description is a composite type
	 * @return true if type description is a composite type
	 */
	public boolean isComposite();
	
	/**
	 * Returns the properties of the composite type
	 * @return properties of the composite type
	 */
	public List<Property> getProperties();
	
	/**
	 * Return expandable type
	 * @return expandable type
	 */
	public ExpandableType getExpandableType();
	
	/**
	 * Define expandable type
	 * @param p_oExpandableType expandable type
	 */
	public void setExpandableType( ExpandableType p_oExpandableType);
	
	/**
	 * Return stereotypes to add on the entity owning the attribute
	 * @return stereotypes
	 */
	public List<String> getStereotypes();
	
	/**
	 * Add a stereotype
	 * @param p_sStereotypeName stereotype name
	 */
	public void addStereotype( String p_sStereotypeName );
	
	/**
	 * Compute parameter name using base name
	 * @param p_sBaseName base name
	 * @return parameter name
	 */
	public String computeParameterName( String p_sBaseName );
	
	/**
	 * Compute variable name using base name
	 * @param p_sVariableName base name
	 * @return variable name
	 */
	public String computeVariableName( String p_sVariableName );
	
	/**
	 * Compute list variable name using base name
	 * @param p_sListVariableName base name
	 * @return list variable name
	 */
	public String computeListVariableName( String p_sListVariableName );
	
	/**
	 * Enum values (if type is enum)
	 * @return enum values
	 */
	public List<String> getEnumValues();
	
	/**
	 * Property is sub attribute of the type description. It is used
	 * to manage composite type. 
	 * @author lmichenaud
	 *
	 */
	public class Property {
		
		/**
		 * Property name
		 */
		private String name ;

		/**
		 * Type name
		 */
		private String typeName ;
		
		/**
		 * Default value for initialisation pattern
		 */
		private String defaultValue ;
		
		/**
		 * Default options of type
		 */
		private String defaultOptions ;
		
		/**
		 * Sql type
		 */
		private String sqlType ;
		
		/**
		 * Type description
		 */
		private ITypeDescription typeDescription;
		
		/**
		 * Return name
		 * @return name
		 */
		public String getName() {
			return this.name;
		}

		/**
		 * Define name
		 * @param p_sName name
		 */
		public void setName(String p_sName) {
			this.name = p_sName;
		}

		/**
		 * Return type description
		 * @return type description
		 */
		public ITypeDescription getTypeDescription() {
			return this.typeDescription;
		}

		/**
		 * Return type name
		 * @return type name
		 */
		public String getTypeName() {
			return this.typeName;
		}

		/**
		 * Define type name
		 * @param p_sTypeName name of type description
		 */
		public void setTypeName(String p_sTypeName) {
			this.typeName = p_sTypeName;
		}

		/**
		 * Define type description
		 * @param p_oTypeDescription type description
		 */
		public void setTypeDescription(ITypeDescription p_oTypeDescription) {
			this.typeDescription = p_oTypeDescription;
		}

		/**
		 * Return default value
		 * @return default value
		 */
		public String getDefaultValue() {
			return defaultValue;
		}

		/**
		 * Define default value
		 * @param p_sDefaultValue default value
		 */
		public void setDefaultValue(String p_sDefaultValue) {
			this.defaultValue = p_sDefaultValue;
		}

		/**
		 * Return default options
		 * @return default options
		 */
		public String getDefaultOptions() {
			return defaultOptions;
		}

		/**
		 * Define default options
		 * @param p_sDefaultOptions default options
		 */
		public void setDefaultOptions(String p_sDefaultOptions) {
			this.defaultOptions = p_sDefaultOptions;
		}

		/**
		 * Return sql type
		 * @return sql type
		 */
		public String getSqlType() {
			return this.sqlType;
		}

		/**
		 * Define sql type
		 * @param p_sSqlType sql type
		 */
		public void setSqlType(String p_sSqlType) {
			this.sqlType = p_sSqlType;
		}
		
		/**
		 * Copy properties to another instance
		 * @param p_oProperty target instance
		 */
		public void copyTo( Property p_oProperty ) {
			p_oProperty.setName(this.name);
			p_oProperty.setTypeName(this.typeName);
			p_oProperty.setDefaultValue(this.defaultValue);
			p_oProperty.setDefaultOptions(this.defaultOptions);
			p_oProperty.setSqlType(this.sqlType);
			p_oProperty.setTypeDescription(this.typeDescription);
		}
	}
}