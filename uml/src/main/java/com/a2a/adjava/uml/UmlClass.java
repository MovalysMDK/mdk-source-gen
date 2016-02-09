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
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * <p>Uml Class</p>
 *
 * <p>Copyright (c) 2009
 * <p>Company: Adeuza
 *
 * @author mmadigand
 * @author lmichenaud 
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UmlClass extends UmlStereotypedObject {

	/**
	 * Uml class name
	 */
	@XmlAttribute
	private String name ;
	
	/**
	 * Full name of uml class  
	 */
	@XmlAttribute
	@XmlID
	private String fullName ; 
	
	/**
	 * Uml package
	 */
	@XmlTransient
	private UmlPackage mpackage ;
	
	/**
	 * Attributes 
	 */
	@XmlElement(name="attribute")
	private List<UmlAttribute> attributes ;
	
	/**
	 * Association ends 
	 */
	@XmlElement(name="associationEnd")
	private List<UmlAssociationEnd> associations ;
	
	/**
	 * Operations 
	 */
	@XmlElement(name="operation")
	private List<UmlOperation> operations ;
	
	/**
	 * Usages
	 */
	@XmlElement(name="usage")
	private List<UmlUsage> usages ;
	
	/**
	 * Inherit
	 */
	private UmlInherit inherit ;
	
	/**
	 * Documentation 
	 */
	private String documentation ;	
		
	/**
	 * @param p_sName
	 * @param p_oPackage
	 */
	public void init( String p_sName, UmlPackage p_oPackage ) {
		if (p_sName != null) {
			this.name = p_sName.trim();
		} else {
			this.name = null;
		}
		
		this.mpackage = p_oPackage ;
		this.attributes = new ArrayList<UmlAttribute>();

		if ( p_oPackage == null ) {
			this.fullName = p_sName ;
		} else {
			this.fullName = StringUtils.join(p_oPackage.getFullName(), ".", p_sName );
		}
		
		this.associations = new ArrayList<UmlAssociationEnd>();
		this.operations = new ArrayList<UmlOperation>();
		this.documentation = StringUtils.EMPTY;
		this.usages = new ArrayList<UmlUsage>();
	}

	/**
	 * @return la liste des usages
	 */
	public List<UmlUsage> getUsages() {
		return this.usages;
	}
	
	/**
	 * Add a uses
	 * @param p_oUsage
	 */
	public void addUsage(UmlUsage p_oUsage) {
		this.usages.add(p_oUsage);
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Affecte le nom de la classe
	 * @param p_sName le nouveau nom de la classe
	 */
	public void setName(String p_sName) {
		this.name = p_sName;
		
		if ( mpackage == null ) {
			this.fullName = p_sName ;
		} else {
			this.fullName = StringUtils.join(mpackage.getFullName(), ".", p_sName );
		}
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
	public UmlPackage getPackage() {
		return this.mpackage;
	}

	/**
	 * @return
	 */
	public List<UmlAttribute> getAttributes() {
		return this.attributes;
	}
	
	/**
	 * TODO Décrire la méthode hasAttribute de la classe UmlClass
	 * @param p_sAttributeName
	 * @return
	 */
	public boolean hasAttribute( String p_sAttributeName ) {
		boolean r_bHasAttribute = false ;
		
		Iterator<UmlAttribute> iterAttr = this.attributes.iterator();
		while( iterAttr.hasNext() && !r_bHasAttribute) {
			UmlAttribute oAttr = iterAttr.next();
			String sName = oAttr.getName();
			if ( sName.charAt(0) == '@') {
				sName = sName.substring(1);
			}
			r_bHasAttribute = p_sAttributeName.equalsIgnoreCase(sName);
		}
		return r_bHasAttribute ;
	}
	
	/**
	 * Test if uml class has an operation
	 * @param p_sOperationName operation name
	 * @return true if uml class has the operation
	 */
	public boolean hasOperation( String p_sOperationName ) {
		boolean r_bHasOperation = false ;
		
		for( UmlOperation oOperation : this.operations) {
			if ( p_sOperationName.equalsIgnoreCase(oOperation.getName())) {
				r_bHasOperation = true ;
				break;
			}
		}
		return r_bHasOperation ;
	}
	
	
	/**
	 * Add an attribute to class
	 * @param p_oAttribute attribute
	 */
	public void addAttribute( UmlAttribute p_oAttribute ) {
		this.attributes.add( p_oAttribute );
	}

	/**
	 * @return
	 */
	public List<UmlAssociationEnd> getAssociations() {
		return this.associations ;
	}
	
	/**
	 * @return
	 */
	public List<UmlOperation> getOperations() {
		return this.operations ;
	}
	
	/**
	 * Add an association end
	 * @param p_oAssociationEnd association end to add
	 */
	public void addAssociation( UmlAssociationEnd p_oAssociationEnd ) {
		this.associations.add(p_oAssociationEnd);
	}
	
	/**
	 * @param p_oAttribute
	 */
	public void addOperation( UmlOperation p_oOperation ) {
		if ( !hasOperation(p_oOperation.getName())) {
			this.operations.add(p_oOperation );
		}
	}	
	
	/**
	 * @return
	 */
	public UmlInherit getInherit() {
		return inherit;
	}

	/**
	 * @param p_iInherit
	 */
	public void setInherit(UmlInherit p_iInherit) {
		this.inherit = p_iInherit;
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
	 * @param p_oUmlAttribute
	 */
	public void removeAttribute(UmlAttribute p_oUmlAttribute) {
		this.attributes.remove(p_oUmlAttribute);
	}
	
	/**
	 * @param p_listAttributes
	 */
	public void removeAttributes(List<UmlAttribute> p_listAttributes) {
		this.attributes.removeAll(p_listAttributes);
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UmlClass[" + fullName + "]";
	}
}
