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
package com.a2a.adjava.languages.ios.xmodele.relationship;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Relation Ship
 * @author lmichenaud
 *
 */
@XmlRootElement(name="relationShip")
@XmlSeeAlso({MIOSActionRelationShip.class})
@XmlAccessorType(XmlAccessType.FIELD)
public class MIOSRelationShip {

	/**
	 * Relation ship name
	 */
	@XmlElement(required=true)
	private String name ;

	/**
	 * Relation ship kind
	 */
	@XmlAttribute
	private MIOSRelationShipKind kind ;
	
	/**
	 * Constructor
	 */
	protected MIOSRelationShip() {
		// empty constructor for jaxb
	}
	
	/**
	 * Constructors
	 * @param p_oKind kind of relation ship
	 */
	public MIOSRelationShip( MIOSRelationShipKind p_oKind ) {
		this.kind = p_oKind ;
	}
	
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
	 * Return kind of relation ship
	 * @return kind of relation ship
	 */
	public MIOSRelationShipKind getKind() {
		return this.kind;
	}

	/**
	 * Define kind of relation ship
	 * @param p_oKind kind of relation ship
	 */
	public void setKind(MIOSRelationShipKind p_oKind) {
		this.kind = p_oKind;
	}
}
