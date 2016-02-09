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
package com.a2a.adjava.uml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * <p>
 * Uml Attribute
 * </p>
 * 
 * <p>
 * Copyright (c) 2009
 * <p>
 * Company: Adeuza
 * 
 * @author mmadigand
 * @author lmichenaud
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UmlAttribute extends UmlStereotypedObject {

	/**
	 * Name
	 */
	@XmlAttribute
	private String name;
	
	/**
	 * Full name (with class name)
	 */
	@XmlAttribute
	@XmlID
	private String fullName ;
	
	/**
	 * Visibility
	 */
	@XmlAttribute
	private String visibility;
	
	/**
	 * Uml datatype 
	 */
	@XmlElement(name="datatype")
	private UmlDataType umlDataType;
	
	/**
	 * Initial value 
	 */
	@XmlAttribute
	private String initialValue;
	
	/**
	 * Documentation
	 */
	@XmlElement
	private String documentation;

	/**
	 * Derived attribute (computed from others) 
	 */
	@XmlAttribute
	private boolean derived = false;
	
	/**
	 * Transient (not persisted in database)
	 */
	@XmlAttribute
	private boolean transcient = false ;
	
	/**
	 * Part of identifier
	 */
	@XmlAttribute
	private boolean identifier = false;
	
	/**
	 * Constructor 
	 */
	private UmlAttribute() {
		// For Jaxb
	}
	
	/**
	 * Constructor
	 * @param p_sName attribute name
	 * @param p_sVisibility visibility
	 * @param p_oUmlDatatype data type
	 * @param p_sInitialValue initial value
	 * @param p_sDocumentation documentation
	 */
	public UmlAttribute(String p_sName, UmlClass p_oClass, String p_sVisibility, UmlDataType p_oUmlDatatype, String p_sInitialValue, String p_sDocumentation) {
		super();
		if (p_sName != null) {
			this.name = p_sName.trim();
		} else {
			this.name = null;
		}
		this.fullName = StringUtils.join(p_oClass.getFullName(), ".", this.name );
		this.visibility = p_sVisibility;
		this.umlDataType = p_oUmlDatatype;
		this.initialValue = p_sInitialValue;
		this.documentation = p_sDocumentation;
		this.derived = false;
	}
	
	/**
	 * Constructor
	 * @param p_sName attribute name
	 * @param p_sVisibility visibility
	 * @param p_oUmlDatatype data type
	 * @param p_sInitialValue initial value
	 * @param p_sDocumentation documentation
	 */
	public UmlAttribute(UmlAttribute p_oNewUmlAttribute) {
		super();
		if(p_oNewUmlAttribute != null){
			if (p_oNewUmlAttribute.getName() != null) {
				this.name = p_oNewUmlAttribute.getName();
			} else {
				this.name = null;
			}
			this.fullName = StringUtils.join(p_oNewUmlAttribute.getClass(), ".", this.name );
			this.visibility = p_oNewUmlAttribute.getVisibility();
			this.umlDataType = p_oNewUmlAttribute.getDataType();
			this.initialValue = p_oNewUmlAttribute.getInitialValue();
			this.documentation = p_oNewUmlAttribute.getDocumentation();
			this.derived = false;
		}
	}
	
	/**
	 * @return true si l'attribut est calculé
	 */
	public boolean isDerived() {
		return this.derived;
	}
	
	/**
	 * Permet de mettre jour l'information dérivée.
	 * @param p_bDerived true si l'attribut est calculé
	 */
	public void setDerived(boolean p_bDerived) {
		this.derived = p_bDerived;
	}

	/**
	 * @return
	 */
	public boolean isTransient() {
		return transcient;
	}

	/**
	 * @param p_bTransient
	 */
	public void setTransient(boolean p_bTransient) {
		this.transcient = p_bTransient;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public UmlDataType getDataType() {
		return this.umlDataType;
	}

	/**
	 * Ajoute la description du type de l'attribut.
	 */
	public void setDataType(UmlDataType p_oUmlDataType) {
		this.umlDataType = p_oUmlDataType;
	}

	/**
	 * @return
	 */
	public String getVisibility() {
		return visibility;
	}

	/**
	 * @return
	 */
	public String getInitialValue() {
		return this.initialValue;
	}
	
	/**
	 * Setter of initialValue
	 * @param p_sInitialValue
	 */
	public void setInitialValue(String p_sInitialValue) {
		this.initialValue = p_sInitialValue;
	}

	/**
	 * Return documentation
	 * 
	 * @return documentation
	 */
	public String getDocumentation() {
		return this.documentation;
	}

	/**
	 * True if part of identifier
	 * @return true if part of identifier
	 */
	public boolean isIdentifier() {
		return this.identifier;
	}

	/**
	 * Set attribute as part of identifier
	 * @param p_bIdentifier true if attribute part of identifier
	 */
	public void setIdentifier(boolean p_bIdentifier) {
		this.identifier = p_bIdentifier;
	}

	/**
	 * Set name of attribute
	 * @param p_sName attribute name
	 * @param p_oUmlClass uml class of attribute
	 */
	public void setName(String p_sName, UmlClass p_oUmlClass) {
		this.name = p_sName.trim();
		this.fullName = StringUtils.join(p_oUmlClass.getFullName(), ".", this.name );
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return StringUtils.join("UmlAttribute[", this.fullName, "]");
	}
}
