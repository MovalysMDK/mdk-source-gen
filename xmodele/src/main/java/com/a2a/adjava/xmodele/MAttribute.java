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
package com.a2a.adjava.xmodele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.a2a.adjava.AdjavaRuntimeException;
import com.a2a.adjava.schema.Field;
import com.a2a.adjava.types.ITypeDescription;

/**
 * 
 * <p>MAttribute : represente un attribut d'une MClasse</p>
 *
 * <p>Copyright (c) 2009
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */
public class MAttribute implements MIdentifierElem, SAnnotable {

	/**
	 * Name
	 */
	private String name ;
	
	/**
	 * Visibility 
	 */
	private String visibility ;
	
	/**
	 * Part of identifier
	 */
	private boolean partOfIdentifier ;
	
	/**
	 * Type description
	 */
	private ITypeDescription typeDesc ;
	
	/**
	 * Initialisation
	 */
	private String initialisation ;
	
	/**
	 * Default value (from model)
	 */
	private String defaultValue ;
	
	/**
	 * Visual display 
	 */
	private boolean visualDisplay = true;
	
	/**
	 * Initial value from type or model ?
	 */
	private boolean initialisationFromType;
	
	/**
	 * Mandatory
	 */
	private boolean mandatory ;
	
	/**
	 * Length
	 */
	private int length ;
	
	/**
	 * Precision 
	 */
	private int precision ;
	
	/**
	 * Scale 
	 */
	private int scale ;
	
	/**
	 * Has a matching database sequence
	 */
	private boolean hasSequence ;
	
	/**
	 * Parameter name 
	 */
	private String parameterName ;
	
	/**
	 * Method crit name
	 */
	private String methodCritName ;
	
	/**
	 * Unique
	 */
	private boolean unique ;
	
	/**
	 * Name of unique key if attribute is part of a unicity constraint 
	 */
	private String uniqueKey ;
	
	/**
	 * SQL Field 
	 */
	private Field field ;
	
	/**
	 * Association name if attribute was generated from an association.
	 */
	private String generatedFromAssocName ;
	
	/**
	 * Documentation
	 */
	private String documentation ;
	
	/**
	 * Annotations
	 */
	private List<MAnnotation> annotations = new ArrayList<>();
	
	/**
	 * Read-only
	 */
	private boolean readonly;
	
	/**
	 * Computed from other fields
	 */
	private boolean derived ;
	
	/**
	 * Transient (not persisted)
	 */
	private boolean transcient ;
	
	/**
	 * True if attribute is an enumeration. 
	 */
	private boolean isEnum;
	
	/**
	 * Corresponding enumeration if attribute is an enumeration 
	 */
	private MEnumeration enumeration; 
	
	/**
	 * Properties
	 */
	private List<MAttribute> properties = new ArrayList<>();
	
	/**
	 * Extra parameters
	 */
	private Map<String, String> parameters = new HashMap<>();
	
	/**
	 * Construct an MAttribute object.
	 * @param p_sName name
	 * @param p_sVisibility visibility
	 * @param p_bIdentifier part of identifier
	 * @param p_bDerived derived or not
	 * @param p_bTransient transient or not
	 * @param p_oTypeDescription type description
	 * @param p_sInitialisation init format
	 * @param p_sDefaultValue default value
	 * @param p_bIsMandory mandatory
	 * @param p_iLength length
	 * @param p_iPrecision precision
	 * @param p_iScale scale
	 * @param p_bHasSequence sequence in database
	 * @param p_bUnique unique
	 * @param p_sUniqueKey name of unique key
	 * @param p_sDocumentation doc
	 */
	public MAttribute( String p_sName, String p_sVisibility, boolean p_bIdentifier, boolean p_bDerived, boolean p_bTransient,
			ITypeDescription p_oTypeDescription, String p_sInitialisation, String p_sDefaultValue, boolean p_bIsMandory,
			int p_iLength, int p_iPrecision, int p_iScale, boolean p_bHasSequence,
			boolean p_bUnique, String p_sUniqueKey, String p_sDocumentation) {
		
		this.name = p_sName ;
		this.visibility = p_sVisibility ;
		this.partOfIdentifier = p_bIdentifier ;
		this.typeDesc = p_oTypeDescription ;
		this.initialisation = p_sInitialisation ;
		this.defaultValue = p_sDefaultValue ;
		this.mandatory = p_bIsMandory ;
		this.length = p_iLength ;
		this.precision = p_iPrecision ;
		this.scale = p_iScale ;
		this.hasSequence = p_bHasSequence ;
		this.parameterName = this.typeDesc.computeParameterName(this.name);
		this.methodCritName = StringUtils.capitalize(this.name);
		this.unique = p_bUnique ;
		this.uniqueKey = p_sUniqueKey ;
		this.documentation = p_sDocumentation;
		this.derived = p_bDerived ;
		this.transcient = p_bTransient ;
		this.isEnum = p_oTypeDescription.isEnumeration();
	}
	
	/**
	 * Constructeur
	 * @param p_sName nom de l'attribut
	 * @param p_sVisibility visibilité
	 * @param p_bIdentifier fait parti de l'identifiant
	 * @param p_bDerived derived
	 * @param p_bTransient transient
	 * @param p_oTypeDescription descripteur du type
	 * @param p_sDocumentation documentation sur l'attribut
	 */
	public MAttribute( String p_sName, String p_sVisibility, boolean p_bIdentifier, boolean p_bDerived, boolean p_bTransient,
			ITypeDescription p_oTypeDescription, String p_sDocumentation) {
		this.name = p_sName ;
		this.visibility = p_sVisibility ;
		this.partOfIdentifier = p_bIdentifier ;
		this.typeDesc = p_oTypeDescription ;
		this.parameterName = this.typeDesc.computeParameterName(this.name);
		this.methodCritName = StringUtils.capitalize(this.name);
		this.documentation = p_sDocumentation;
		this.derived = p_bDerived ;
		this.transcient = p_bTransient ;
		this.isEnum = p_oTypeDescription.isEnumeration();
		if (p_bIdentifier) {
			this.visualDisplay = false;
		}
	}
	
	/**
	 * Constructeur
	 * @param p_sName name
	 * @param p_sVisibility visibility
	 * @param p_bIdentifier part of identifier
	 * @param p_bDerived derived
	 * @param p_bTransient transient
	 * @param p_oTypeDescription type description
	 * @param p_sDocumentation documentation
	 * @param p_bReadOnly readonly
	 */
	public MAttribute( String p_sName, String p_sVisibility, boolean p_bIdentifier, boolean p_bDerived, boolean p_bTransient,
			ITypeDescription p_oTypeDescription, String p_sDocumentation, boolean p_bReadOnly ) {
		this.name = p_sName ;
		this.visibility = p_sVisibility ;
		this.partOfIdentifier = p_bIdentifier ;
		this.typeDesc = p_oTypeDescription ;
		this.parameterName = this.typeDesc.computeParameterName(this.name);
		this.methodCritName = StringUtils.capitalize(this.name);
		this.documentation = p_sDocumentation;
		this.readonly = p_bReadOnly;
		this.derived = p_bDerived ;
		this.transcient = p_bTransient ;
		this.isEnum = p_oTypeDescription.isEnumeration();
		if (p_bIdentifier) {
			this.visualDisplay = false;
		}
	}
	
	/**
	 * Return true if readonly
	 * @return true if readonly
	 */
	public boolean isReadOnly() {
		return this.readonly;
	}
	
	/**
	 * Return true if has sequence
	 * @return true if has sequence
	 */
	public boolean hasSequence() {
		return this.hasSequence ;
	}
	
	/**
	 * Return attribute name
	 * @return attribute name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set attribute name
	 * @param p_sName attribute name
	 */
	public void setName(String p_sName) {
		this.name = p_sName;
		this.parameterName = this.typeDesc.computeParameterName(this.name);
	}
	
	/**
	 * Return visibility
	 * @return visibility
	 */
	public String getVisibility() {
		return visibility;
	}

	/**
	 * Return true if part of identifier
	 * @return true if part of identifier
	 */
	public boolean isPartOfIdentifier() {
		return this.partOfIdentifier;
	}
	
	/**
	 * Return init format
	 * @return init format
	 */
	public String getInitialisation() {
		return this.initialisation;
	}
	
	/**
	 * Set init format
	 * @param p_sInitialisation init format
	 */
	public void setInitialisation(String p_sInitialisation) {
		this.initialisation = p_sInitialisation;
	}
	
	/**
	 * Return <code>true</code> if initialisation was computed from type.
	 * @return <code>true</code> if initialisation was computed from type.
	 */
	public boolean isInitialisationFromType() {
		return this.initialisationFromType;
	}

	/**
	 * Set initialisation as computed from type. 
	 * @param p_bInitialisationFromType true if init computed from type
	 */
	public void setInitialisationFromType(boolean p_bInitialisationFromType) {
		this.initialisationFromType = p_bInitialisationFromType;
	}
	
	/**
	 * Return type description
	 * @return type description
	 */
	public ITypeDescription getTypeDesc() {
		return this.typeDesc;
	}

	/**
	 * Return true if mandatory
	 * @return true if mandatory
	 */
	public boolean isMandatory() {
		return this.mandatory;
	}

	/**
	 * Return length
	 * @return length
	 */
	public int getLength() {
		return this.length;
	}

	/**
	 * Return precision
	 * @return precision
	 */
	public int getPrecision() {
		return this.precision;
	}
	
	/**
	 * Return scale
	 * @return scale
	 */
	public int getScale() {
		return this.scale;
	}
	
	/**
	 * Return parameter name
	 * @return parameter name
	 */
	public String getParameterName() {
		return this.parameterName;
	}
	
	/**
	 * Return true if unique
	 * @return true if unique
	 */
	public boolean isUnique() {
		return this.unique;
	}

	/**
	 * Return unique key name if part of unique key
	 * @return unique key name if part of unique key
	 */
	public String getUniqueKey() {
		return this.uniqueKey;
	}

	/**
	 * Return matching field in database
	 * @return matching field in database
	 */
	public Field getField() {
		return this.field;
	}

	/**
	 * Define matching field in database
	 * @param p_oField matching field in database
	 */
	public void setField(Field p_oField) {
		this.field = p_oField;
	}
	
	/**
	 * Return method criteria name
	 * @return method criteria name
	 */
	public String getMethodCritName() {
		return this.methodCritName;
	}

	/**
	 * Return the association name from which the attribute was generated
	 * @return association name from which the attribute was generated
	 */
	public String getGeneratedFromAssocName() {
		return this.generatedFromAssocName;
	}

	/**
	 * Define the association name from which the attribute was generated
	 * @param p_sGeneratedFromAssocName the association name from which the attribute was generated
	 */
	public void setGeneratedFromAssocName(String p_sGeneratedFromAssocName) {
		this.generatedFromAssocName = p_sGeneratedFromAssocName;
	}

	/**
	 * Return documentation
	 * @return documentation
	 */
	public String getDocumentation() {
		return this.documentation;
	}	
	
	/**
	 * Add an annotation
	 * @param p_oAnnotation annotation
	 */
	public void addAnnotation( MAnnotation p_oAnnotation ) {
		this.annotations.add( p_oAnnotation );
	}
	
	/**
	 * Return annotations of attribute
	 * @return annotations of attribute
	 */
	public List<MAnnotation> getAnnotations() {
		return this.annotations ;
	}
	
	/**
	 * Define that the attribute is part of the identifier
	 * @param p_bPartOfIdentifier true if attribute part of identifier
	 */
	public void setPartOfIdentifier(boolean p_bPartOfIdentifier) {
		this.partOfIdentifier = p_bPartOfIdentifier;
		if (p_bPartOfIdentifier) {
			this.visualDisplay = false;
		}
	}

	/**
	 * Set attribute as mandatory
	 * @param p_bMandatory true if attribute is mandatory
	 */
	public void setMandatory(boolean p_bMandatory) {
		this.mandatory = p_bMandatory;
	}

	/**
	 * Set attribute length
	 * @param p_iLength length
	 */
	public void setLength(int p_iLength) {
		this.length = p_iLength;
	}

	/**
	 * Set precision
	 * @param p_iPrecision precision
	 */
	public void setPrecision(int p_iPrecision) {
		this.precision = p_iPrecision;
	}

	/**
	 * Set scale
	 * @param p_iScale scale
	 */
	public void setScale(int p_iScale) {
		this.scale = p_iScale;
	}

	/**
	 * Define that attribute needs a sequence in database
	 * @param p_bHasSequence true if attribute needs a sequence
	 */
	public void setHasSequence(boolean p_bHasSequence) {
		this.hasSequence = p_bHasSequence;
	}

	/**
	 * Set true if unique
	 * @param p_bUnique true if unique
	 */
	public void setUnique(boolean p_bUnique) {
		this.unique = p_bUnique;
	}

	/**
	 * Set unique key name if part of unique key
	 * @param p_sUniqueKey unique key name if part of unique key
	 */
	public void setUniqueKey(String p_sUniqueKey) {
		this.uniqueKey = p_sUniqueKey;
	}

	/**
	 * Return true if derived
	 * @return true if derived
	 */
	public boolean isDerived() {
		return derived;
	}
	
	/**
	 * Return true if transient
	 * @return true if transient
	 */
	public boolean isTransient() {
		return transcient;
	}

	/**
	 * Return true if attribute is enumeration
	 * @return true if attribute is enumeration
	 */
	public boolean isEnum() {
		return isEnum;
	}
	
	/**
	 * Affecte l'énumération correspondant au type de l'attribut.
	 * @param p_oEnum enumération à affecter à l'attribut
	 */
	public void setMEnumeration(MEnumeration p_oEnum){
		this.enumeration = p_oEnum;
	}

	/**
	 * Return enumeration
	 * @return enumeration
	 */
	public MEnumeration getMEnumeration() {
		return this.enumeration;
	}

	/**
	 * Return true if visual display
	 * @return true if visual display
	 */
	public boolean isVisualDisplay() {
		return this.visualDisplay;
	}
	
	/**
	 * Set if visual display
	 * @param p_bVisualDisplay true if visual display
	 */
	public void setVisualDisplay(boolean p_bVisualDisplay) {
		this.visualDisplay = p_bVisualDisplay;
	}
	
	/**
	 * Return parameters
	 * @return parameters
	 */
	public Map<String, String> getParameters() {
		return this.parameters;
	}

	/**
	 * Add a paramater
	 * @param p_sKey parameter key
	 * @param p_sValue parameter value
	 */
	public final void addParameter(String p_sKey, String p_sValue) {
		this.parameters.put(p_sKey, p_sValue);
	}
	
	/**
	 * Add a property
	 * @param p_oAttribute property to add
	 */
	public void addProperty( MAttribute p_oAttribute ) {
		this.properties.add(p_oAttribute);
	}
	
	/**
	 * Return true if basic
	 * @return true if basic
	 */
	public boolean isBasic() {
		return this.properties.isEmpty();
	}
	
	/**
	 * Return all properties of attribute
	 * @return all properties of attribute
	 */
	public List<MAttribute> getProperties() {
		return properties;
	}
	
	/**
	 * Define if attribute is transient
	 * @param p_bTransient true if transient
	 */
	public void setTransient(boolean p_bTransient) {
		this.transcient = p_bTransient;
	}

	/**
	 * Get default value
	 * @return default value
	 */
	public String getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 * Set default value
	 * @param p_sDefaultValue default value
	 */
	public void setDefaultValue(String p_sDefaultValue) {
		this.defaultValue = p_sDefaultValue;
	}

	/**
	 * Return xml element
	 * @return xml element
	 */
	public Element toXml() {
		Element r_xAttr = DocumentHelper.createElement("attribute");
		this.genCommonXml(r_xAttr);
		if ( this.isBasic()) {
			this.genXmlForBasicAttr(r_xAttr);
		}
		else {
			this.genXmlForCompositeAttr(r_xAttr);
		}
		
		return r_xAttr;
	}

	/**
	 * Generate common xml part in xml element
	 * @param p_xAttr xml element to fill
	 */
	public void genCommonXml( Element p_xAttr ) {
		p_xAttr.addAttribute("name", this.name);
		p_xAttr.addAttribute("name-uppercase", this.name.toUpperCase(Locale.getDefault()));
		p_xAttr.addAttribute("name-capitalized", StringUtils.capitalize(this.name));
		p_xAttr.addAttribute("visibility", this.visibility );
		p_xAttr.addAttribute("readonly", Boolean.toString(this.readonly));
		p_xAttr.addAttribute("derived", Boolean.toString(this.derived));
		p_xAttr.addAttribute("transient", Boolean.toString(this.isTransient()));
		p_xAttr.addAttribute("visualdisplay", Boolean.toString(this.visualDisplay));
		p_xAttr.addAttribute("type-name", this.typeDesc.getName());
		p_xAttr.addAttribute("type-short-name", this.typeDesc.getShortName());
		p_xAttr.addAttribute("type-short-name-capitalized", StringUtils.capitalize(this.typeDesc.getShortName()));
		p_xAttr.addAttribute("init", this.initialisation );
		p_xAttr.addAttribute("nullable", Boolean.toString(!this.mandatory));
		
		p_xAttr.addElement("get-accessor").setText(
			StringUtils.join(this.typeDesc.getGetAccessorPrefix(), StringUtils.capitalize(this.name)));
		p_xAttr.addElement("set-accessor").setText(
				StringUtils.join(this.typeDesc.getSetAccessorPrefix(), StringUtils.capitalize(this.name)));
		p_xAttr.addElement("parameter-name").setText( this.parameterName );
		p_xAttr.addElement("method-crit-name").setText(this.methodCritName);

		
		if (this.typeDesc.getParameterizedElementType().size()==1) {
			p_xAttr.addAttribute("contained-type-name", this.typeDesc.getParameterizedElementType().get(0).getName());
			p_xAttr.addAttribute("contained-type-short-name", this.typeDesc.getParameterizedElementType().get(0).getShortName());
		}
		
		Element xAnnotations = p_xAttr.addElement("annotations");
		for( MAnnotation oAnnotation : this.annotations ) {
			xAnnotations.add( oAnnotation.toXml());
		}

		Element xParams = p_xAttr.addElement("parameters");
		Element xParam = null;
		for(Entry<String, String> oEntry : parameters.entrySet()) {
			xParam = xParams.addElement("parameter");
			xParam.setText(oEntry.getValue());
			xParam.addAttribute("name", oEntry.getKey());
		}
		
		this.genDocXml(p_xAttr);
	}
	
	/**
	 * Generate the xml element for a basic attribute
	 * @param p_xAttr p_xAttr to fill
	 */
	public void genXmlForBasicAttr( Element p_xAttr ) {
		p_xAttr.addAttribute("kind", "basic");
		p_xAttr.addAttribute("primitif", Boolean.toString(this.typeDesc.isPrimitif()));
		
		if ( this.length != -1 ) {
			p_xAttr.addAttribute("length", Integer.toString(this.length));
		}
		if ( this.precision != -1 ) {
			p_xAttr.addAttribute("precision", Integer.toString(this.precision));
		}
		if ( this.scale != -1 ) {
			p_xAttr.addAttribute("scale", Integer.toString(this.scale));
		}
		
		if ( this.unique ) {
			p_xAttr.addAttribute("unique", "true");
		}
		if ( this.uniqueKey != null && StringUtils.isNotEmpty(this.uniqueKey)){
			p_xAttr.addAttribute("unique-key", this.uniqueKey );
			p_xAttr.addAttribute("unique-key-uppercase", this.uniqueKey.toUpperCase(Locale.getDefault()));
		}

		if ( this.field != null ) {
			p_xAttr.add(this.field.toXml());
		}

		if ( this.typeDesc.getWrapper() != null ) {
			Element xWrapper = p_xAttr.addElement("wrapper");
			xWrapper.addElement("name").setText(this.typeDesc.getWrapper().getName());
			xWrapper.addElement("short-name").setText(this.typeDesc.getWrapper().getShortName());
			xWrapper.addElement("init").setText(this.typeDesc.getWrapper().getInitFormat());
		}

		if ( this.typeDesc.getUnsavedValue() != null ) {
			p_xAttr.addAttribute("unsaved-value", this.typeDesc.getUnsavedValue());
		}
		
		this.genXmlEnum(p_xAttr);
	}
	
	/**
	 * Generate the xml element for a composite attribute
	 * @param p_xAttr xml element to fill
	 */
	public void genXmlForCompositeAttr( Element p_xAttr ) {
		p_xAttr.addAttribute("kind", "composite");
		Element xAttrs = p_xAttr.addElement("properties");
		for( MAttribute oAttr : this.properties ) {
			Element xAttr = oAttr.toXml();
			xAttr.setName("property");
			xAttrs.add(xAttr);
		}
	}
	
	/**
	 * Add documentation xml elements to the passed element
	 * @param p_xAttr element
	 */
	protected void genDocXml( Element p_xAttr ) {
		Element xDoc = p_xAttr.addElement("documentation");

		//Ancienne convention
		if(this.documentation.contains("<doc-"))
		{
			if(this.documentation.indexOf("<doc-attribute>")!=-1){
				if(this.documentation.indexOf("</doc-attribute>")==-1){
					throw new AdjavaRuntimeException("La documentation de l'attribut {} est incorrect (balise </doc-attribute> manquante)", this.name);
				}
				String sAttributeDocumentation = StringUtils.substringBetween(this.documentation, "<doc-attribute>", "</doc-attribute>"); 
				xDoc.addElement("doc-attribute").setText(sAttributeDocumentation);
			}
			if(this.documentation.indexOf("<doc-getter>")!=-1){
				if(this.documentation.indexOf("</doc-getter>")==-1){
					throw new AdjavaRuntimeException("La documentation de l'attribut {} est incorrect (balise </doc-getter> manquante)", this.name);
				}
				String sGetterDocumentation = StringUtils.substringBetween(this.documentation, "<doc-getter>", "</doc-getter>");
				xDoc.addElement("doc-getter").setText(sGetterDocumentation);
			}
			
			if(this.documentation.indexOf("<doc-setter>")!=-1){
				if(this.documentation.indexOf("</doc-setter>")==-1){
					throw new AdjavaRuntimeException("La documentation de l'attribut {} est incorrect (balise </doc-setter> manquante)", this.name);				
				}
				String sSetterDocumentation = StringUtils.substringBetween(this.documentation, "<doc-setter>", "</doc-setter>"); 
				xDoc.addElement("doc-setter").setText(sSetterDocumentation);
			}
		}
		else
		{
			//Nouvelle utilisation, sans balise et ou la meme doc est présente pour l'attribut et le setter.
			String sGlobalDocumentation = this.documentation;
			xDoc.addElement("doc-attribute").setText(sGlobalDocumentation);
			xDoc.addElement("doc-setter").setText(sGlobalDocumentation);
		}
	}
	
	/**
	 * Generate enumeration xml for attribute
	 * @param p_xAttr xml element to fill
	 */
	protected void genXmlEnum(Element p_xAttr) {
		p_xAttr.addAttribute("enum", Boolean.toString(this.isEnum));
		if (this.isEnum) {
			Element xEnumValues = p_xAttr.addElement("enumeration-values");
			int iCpt = 1;
			for (String sEnumName : this.enumeration.getEnumValues()){
				Element xEnumVal = xEnumValues.addElement("enum-value");
				xEnumVal.addAttribute("pos", String.valueOf(iCpt));
				xEnumVal.setText(sEnumName);
				iCpt++;
			}
		}
	}
}
