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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Uml enumeration
 * @author lmichenaud
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UmlEnum extends UmlStereotypedObject {

	/**
	 * Enumeration name
	 */
	@XmlAttribute
	private String name ;
	
	/**
	 * Enumeration fullName
	 */
	@XmlAttribute
	@XmlID
	private String fullName ; 
	
	/**
	 * Uml package 
	 */
	@XmlTransient
	private UmlPackage umlPackage ;
	
	/**
	 * Enum values 
	 */
	@XmlElementWrapper(name="values")
	@XmlElement(name="value")
	private List<String> enumValues ;
	
	/**
	 * Datatype 
	 */
	@XmlElement
	private UmlDataType dataType ;
	
	/**
	 * Constructor
	 */
	private UmlEnum() {
		// empty constructor for jaxb
	}
	
	/**
	 * @param p_sName
	 * @param p_oPackage
	 * @param p_listEnumValues
	 */
	public UmlEnum( String p_sName, UmlDataType p_oUmlDataType ) {
		super();
		if (p_sName != null) {
			this.name = p_sName.trim();	
		} else {
			this.name = null;
		}
		this.enumValues = new ArrayList<String>();
		this.dataType = p_oUmlDataType ;
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
	public String getFullName() {
		return fullName;
	}

	/**
	 * @return
	 */
	public UmlPackage getUmlPackage() {
		return this.umlPackage;
	}

	/**
	 * @param p_oPackage
	 */
	public void setUmlPackage(UmlPackage p_oPackage) {
		this.umlPackage = p_oPackage;
		this.fullName = p_oPackage.getFullName() + "." + this.name ;
	}

	/**
	 * @return
	 */
	public List<String> getEnumValues() {
		return enumValues;
	}
	
	/**
	 * @param p_sValue
	 */
	public void addEnumValue( String p_sValue ) {
		this.enumValues.add( p_sValue );
	}

	/**
	 * @return
	 */
	public UmlDataType getDataType() {
		return this.dataType;
	}
}
