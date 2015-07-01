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
 * 
 * <p>
 * Uml Comment
 * </p>
 * 
 * @author lbrunelliere
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UmlComment extends UmlStereotypedObject {

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
	 * Documentation 
	 */
	private String documentation ;

	/**
	 * Constructor 
	 */
	private UmlComment() {
		// For Jaxb
	}
	
	/**
	 * Constructor
	 * @param p_sName attribute name
	 * @param p_oClass Class associate
	 * @param p_sComment Comment
	 */
	public UmlComment(String p_sName, UmlClass p_oClass, String p_sDocumentation) {
		super();
		if (p_sName != null) {
			this.name = p_sName.trim();
		} else {
			this.name = null;
		}
		if (p_sName != null) {
			this.fullName = StringUtils.join(p_oClass.getFullName(), ".", this.name );
		}
		this.documentation = p_sDocumentation;
	}
	
	/**
	 * @return String name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retourne la cha��ne documentation
	 * @return String documentation
	 */
	public String getDocumentation() {
		return this.documentation;
	}

	/**
	 * Affecte la cha��ne documentation
	 * @param p_sDocumentation Cha��ne documentation
	 */
	public void setDocumentation(String p_sDocumentation) {
		this.documentation = p_sDocumentation;
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
