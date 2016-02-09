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
package com.a2a.adjava.languages.ios.xmodele;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.a2a.adjava.languages.ios.xmodele.relationship.MIOSRelationShip;
import com.a2a.adjava.utils.StrUtils;
import com.a2a.adjava.xmodele.MPackage;

/**
 * IOS Class
 * @author lmichenaud
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MIOSClass {

	/**
	 * Class name
	 */
	@XmlElement
	private String name ;
	
	/**
	 * Parent Package
	 */
	@XmlIDREF
	private MPackage parent ;
	
	/**
	 * Super class name
	 */
	@XmlElement
	private String superClassName ;
	
	/**
	 * Do generation
	 */
	@XmlTransient
	private boolean doGeneration = true;
	
	/**
	 * Relation ships
	 */
	@XmlElementWrapper
	@XmlElement(name="relationShip")
	private List<MIOSRelationShip> relationShips = new ArrayList<MIOSRelationShip>();

	/**
	 * Return name
	 * @return name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Return full name
	 * @return full name
	 */
	public String getFullName() {
		StringBuilder sFullName = new StringBuilder();
		if ( this.parent != null ) {
			sFullName.append(this.parent.getFullName());
			sFullName.append(StrUtils.DOT);
		}
		sFullName.append(this.name);
		return sFullName.toString();
	}

	/**
	 * Define name
	 * @param p_sName name
	 */
	public void setName(String p_sName) {
		this.name = p_sName;
	}

	/**
	 * Return parent
	 * @return parent
	 */
	public MPackage getParent() {
		return this.parent;
	}

	/**
	 * Define parent
	 * @param p_oParent parent
	 */
	public void setParent(MPackage p_oParent) {
		this.parent = p_oParent;
	}

	/**
	 * Return super class name
	 * @return super class name
	 */
	public String getSuperClassName() {
		return this.superClassName;
	}

	/**
	 * Define super class name
	 * @param p_sSuperClassName super class name
	 */
	public void setSuperClassName(String p_sSuperClassName) {
		this.superClassName = p_sSuperClassName;
	}

	/**
	 * Class to generate
	 * @return true is must be generated
	 */
	public boolean isDoGeneration() {
		return this.doGeneration;
	}

	/**
	 * Define do generation
	 * @param p_bDoGeneration do generation
	 */
	public void setDoGeneration(boolean p_bDoGeneration) {
		this.doGeneration = p_bDoGeneration;
	}

	/**
	 * Return relation ships
	 * @return relation ships
	 */
	public List<MIOSRelationShip> getRelationShips() {
		return relationShips;
	}

	/**
	 * Add a relation ship
	 * @param p_oRelationShips relation ship
	 */
	public void addRelationShip( MIOSRelationShip p_oRelationShips) {
		this.relationShips.add(p_oRelationShips);
	}
}
