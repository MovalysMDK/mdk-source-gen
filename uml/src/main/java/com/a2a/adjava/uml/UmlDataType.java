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
 * <p>Uml DataType</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UmlDataType {
	
	/**
	 * Definition of the int type in the model
	 */
	public static final String INT = "int";
	
	/**
	 * Definition of the long type in the model
	 */
	public static final String LONG = "long";
	
	/**
	 * Definition of the String type in the model
	 */
	public static final String STRING = "String";
	
	/**
	 * Definition of the Date type in the model
	 */
	public static final String DATE = "Timestamp";
	
	/**
	 * the alias identifier of a uml data type
	 */
	public static final String ALLIAS_IDENTIFIER = "_id";
	
	/**
	 * The type name
	 */
	@XmlAttribute
	@XmlID
	private String name ;

	/**
	 * Constructor
	 */
	private UmlDataType() {
		// empty constructor for jaxb
	}
	
	/**
	 * UmlDataType
	 * @param p_sName
	 */
	public UmlDataType( String p_sName ) {
		if (p_sName != null) {
			this.name = p_sName.trim();
		} else {
			this.name = null;
		}
	}
	
	/**
	 * Retourne l'objet name
	 * @return Objet name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Affecte l'objet name 
	 * @param p_sName Objet name
	 */
	public void setName(String p_sName) {
		this.name = p_sName;
	}

	/**
	 * {@inheritDoc} 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return StringUtils.join("UmlDataType[", this.name, "]");
	}
}
