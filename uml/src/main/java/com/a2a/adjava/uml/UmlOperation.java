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
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;

/**
 * Uml operation
 * @author lmichenaud
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UmlOperation extends UmlStereotypedObject {

	/**
	 * Name
	 */
	@XmlAttribute
	private String name ;
	
	/**
	 * Full name
	 */
	@XmlID
	private String fullName ;
	
	/**
	 * Documentation
	 */
	private String documentation ;
	
	/**
	 * Constructor
	 */
	private UmlOperation() {
		// Empty constructor for jaxb
	}
	
	/**
	 * @param p_sName
	 */
	public UmlOperation(String p_sName, UmlClass p_oUmlClass) {
		super();
		if (p_sName != null) {
			this.name = p_sName.trim();
		} else {
			this.name = null;
		}
		this.fullName = StringUtils.join( p_oUmlClass.getFullName(), ".", this.name );
		this.documentation = StringUtils.EMPTY;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Retourne la chaîne documentation
	 * @return Chaîne documentation
	 */
	public String getDocumentation() {
		return this.documentation;
	}

	/**
	 * Affecte la chaîne documentation 
	 * @param p_sDocumentation Chaîne documentation
	 */
	public void setDocumentation(String p_sDocumentation) {
		this.documentation = p_sDocumentation;
	}

	/**
	 * @return
	 */
	@Override
	public String toString() {
		return StringUtils.join("UmlOperation[", this.fullName, "]");
	}
}