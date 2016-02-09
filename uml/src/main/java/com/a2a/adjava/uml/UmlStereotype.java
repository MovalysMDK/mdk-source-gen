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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

/**
 * Uml stereotype
 * @author lmichenaud
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="stereotype")
public class UmlStereotype {

	/**
	 * id
	 */
	@XmlTransient
	private String id ;
	
	/**
	 * Name
	 */
	@XmlValue
	private String name ;
	
	/**
	 * 
	 */
	@XmlTransient
	private String namespace ;
	
	/**
	 * Documentation 
	 */
	@XmlTransient
	private String documentation ;
	
	/**
	 * Constructor
	 */
	private UmlStereotype() {
		// empty constructor need by jaxb
	}
	
	/**
	 * Constructor
	 * @param p_sId
	 * @param p_sName
	 */
	public UmlStereotype( String p_sId, String p_sName ) {
		this.id = p_sId ;
		if (p_sName != null) {
			this.name = p_sName.trim() ;
		} else {
			this.name = null ;
		}
		this.documentation = "";
	}
	
	/**
	 * @return
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @param p_sId
	 */
	public void setId(String p_sId) {
		this.id = p_sId;
	}
	/**
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param p_sName
	 */
	public void setName(String p_sName) {
		this.name = p_sName.trim();
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
	 * Return namespace
	 * @return namespace
	 */
	public String getNamespace() {
		return this.namespace;
	}

	/**
	 * Define namespace
	 * @param p_sNamespace namespace
	 */
	public void setNamespace(String p_sNamespace) {
		this.namespace = p_sNamespace;
	}
}
