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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Uml package
 * @author lmichenaud
 *
 */
@XmlRootElement
public class UmlPackage {

	/**
	 * name
	 */
	@XmlAttribute
	private String name ;
	
	/**
	 * Full name
	 */
	@XmlID
	@XmlAttribute
	private String fullName ;
	
	/**
	 * Parent package 
	 */
	@XmlTransient
	private UmlPackage parent ;
	
	/**
	 * child packages 
	 */
	@XmlElement(name="package")
	private List<UmlPackage> packages ;
	
	/**
	 * Classes of package 
	 */
	@XmlElement(name="class")
	private List<UmlClass> classes ;
	
	/**
	 * Association classes 
	 */
	@XmlElement(name="associationClass")
	private List<UmlAssociationClass> associationClasses ;
	
	/**
	 * Enumerations 
	 */
	@XmlElement(name="enum")
	private List<UmlEnum> enumerations ;
	
	/**
	 * Constructor
	 */
	private UmlPackage() {
		// for jaxb
	}
	
	/**
	 * @param p_sName
	 * @param p_oParent
	 */
	public UmlPackage( String p_sName, UmlPackage p_oParent ) {
		if (p_sName != null) {
			this.name = p_sName.trim();
		} else {
			this.name = null;
		}
		this.parent = p_oParent ;
		if ( p_oParent != null ) {
			this.fullName = p_oParent.getFullName() + "." + p_sName ;
		} else {
			this.fullName = p_sName ;
		}
		this.packages = new ArrayList<UmlPackage>();
		this.classes = new ArrayList<UmlClass>();
		this.enumerations = new ArrayList<UmlEnum>();
		this.associationClasses = new ArrayList<UmlAssociationClass>();
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
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public List<UmlPackage> getPackages() {
		return packages;
	}
	
	/**
	 * @param packages
	 */
	public void addPackage(UmlPackage p_oPackage) {
		this.packages.add( p_oPackage );
	}
	
	/**
	 * @return
	 */
	public UmlPackage getParent() {
		return parent;
	}
	
	/**
	 * @return
	 */
	public List<UmlClass> getClasses() {
		return this.classes ;
	}
	
	/**
	 * @return
	 */
	public List<UmlEnum> getEnumeration() {
		return this.enumerations ;
	}
	
	/**
	 * @param packages
	 */
	public void addClass(UmlClass p_oClass) {
		this.classes.add( p_oClass );
	}
	
	/**
	 * @param packages
	 */
	public void addEnumeration(UmlEnum p_oEnum) {
		this.enumerations.add( p_oEnum );
	}
	
	/**
	 * @param packages
	 */
	public void addAssociationClass(UmlAssociationClass p_oAssocationClass) {
		this.associationClasses.add( p_oAssocationClass );
	}
	
	/**
	 * @return
	 */
	public List<UmlAssociationClass> getAssociationClasses() {
		return this.associationClasses ;
	}
}
