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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

/**
 * <p>Dictionnary for uml objects</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UmlDictionary {

	/**
	 * Uml classes
	 */
	@XmlTransient
	private Map<String,UmlClass> classes = new HashMap<String,UmlClass>();
	
	/**
	 * Uml packages 
	 */
	@XmlTransient
	private Map<String,UmlPackage> packages = new HashMap<String,UmlPackage>();
	
	/**
	 * Uml Associations 
	 */
	@XmlElementWrapper(name="associations")
	@XmlElement(name="association")
	private List<UmlAssociation> associations = new ArrayList<UmlAssociation>();
	
	/**
	 * Uml Association ends
	 */
	@XmlTransient
	private Map<String,UmlAssociationEnd> associationEnds = new HashMap<String,UmlAssociationEnd>();
	
	/**
	 * Uml association classes
	 */
	@XmlTransient
	private Map<String,UmlAssociationClass> associationClasses = new HashMap<String,UmlAssociationClass>();
	
	/**
	 * Stereotypes by id
	 */
	@XmlTransient
	private Map<String,UmlStereotype> stereotypesById = new HashMap<String,UmlStereotype>();
	
	/**
	 * Stereotypes by name
	 */
	@XmlTransient
	private Map<String,UmlStereotype> stereotypesByName = new HashMap<String,UmlStereotype>();
	
	/**
	 * Enumerations 
	 */
	@XmlTransient
	private Map<String,UmlEnum> enumerations = new HashMap<String,UmlEnum>();
	
	/**
	 * Datatypes 
	 */
	@XmlTransient
	private Map<String,UmlDataType> dataTypes = new HashMap<String,UmlDataType>();
	
	/**
	 * Usages 
	 */
	@XmlTransient
	private List<UmlUsage> usages = new ArrayList<UmlUsage>();
	
	/**
	 * Map of stereotype objects
	 */
	@XmlTransient
	private Map<String,UmlStereotypedObject> stereotypeObjects = new HashMap<String,UmlStereotypedObject>();
	
	/**
	 * Comments 
	 */
	@XmlTransient
	private List<UmlComment> comments = new ArrayList<UmlComment>();
	
	/**
	 * Protected constructor
	 */
	protected UmlDictionary() {
		//Protected constructor
	}
	
	/**
	 * Return uml class by id
	 * @param p_sId class id
	 * @return uml class
	 */
	public UmlClass getClassById( String p_sId ) {
		return (UmlClass) this.classes.get(p_sId);
	}
	
	/**
	 * Return uml classes by stereotypes
	 * @param p_oStereotype a stereotype
	 * @return uml classes with the given stereotype
	 */
	public Collection<UmlClass> getClassesByStereotype(UmlStereotype p_oStereotype ) {
		List<UmlClass> oClasses = new ArrayList<UmlClass>();
		for(UmlClass oUmlClass : this.classes.values()) {
			if(oUmlClass.hasStereotype(p_oStereotype.getId())) {
				oClasses.add(oUmlClass);
			}
		}
		return oClasses;
	}
	
	/**
	 * Return all uml classes
	 * @return all uml classes
	 */
	public Collection<UmlClass> getAllClasses() {
		return this.classes.values();
	}
	
	/**
	 * Return all uml associationEnd
	 * @return all uml associationEnd
	 */
	public Collection<UmlAssociationEnd> getAllAssociationEnds() {
		return this.associationEnds.values() ;
	}
	
	/**
	 * Return all uml stereotypes
	 * @return all uml stereotypes
	 */
	public Collection<UmlStereotype> getAllStereotypes() {
		return this.stereotypesByName.values();
	}

	/**
	 * Return uml enumeration by id
	 * @param p_sId id
	 * @return uml enumeration
	 */
	public UmlEnum getEnumById( String p_sId ) {
		return (UmlEnum) this.enumerations.get(p_sId);
	}
	
	/**
	 * Return all enumerations
	 * @return all enumerations
	 */
	public Collection<UmlEnum> getAllEnumerations() {
		return this.enumerations.values();
	}
	
	/**
	 * Register an uml class
	 * @param p_sId class id
	 * @param p_oClass uml class
	 */
	public void registerClass(String p_sId, UmlClass p_oClass) {
		this.classes.put(p_sId, p_oClass);
		this.stereotypeObjects.put(p_sId, p_oClass);
	}
	
	/**
	 * Register an enumeration
	 * @param p_sId enumeration id
	 * @param p_oEnum enumeration
	 */
	public void registerEnumeration(String p_sId, UmlEnum p_oEnum ) {
		this.enumerations.put(p_sId, p_oEnum);
		this.stereotypeObjects.put(p_sId, p_oEnum);
	}
	
	/**
	 * Register a package
	 * @param p_oPackage uml package
	 */
	public void registerPackage( UmlPackage p_oPackage ) {
		this.packages.put( p_oPackage.getFullName(), p_oPackage );
	}
	
	/**
	 * Return all packages
	 * @return all packages
	 */
	public Collection<UmlPackage> getAllPackages() {
		return packages.values();
	}
	
	/**
	 * Return all uml associations
	 * @return all uml associations
	 */
	public Collection<UmlAssociation> getAssociations() {
		return this.associations ;
	}
	
	/**
	 * Add an association
	 * @param p_sId id
	 * @param p_oAssociation association
	 */
	public void registerAssociation( String p_sId, UmlAssociation p_oAssociation ) {
		this.associations.add( p_oAssociation );
		this.stereotypeObjects.put(p_sId, p_oAssociation);
	}
	
	/**
	 * Register an association class
	 * @param p_sId association class id
	 * @param p_oAssociationClass association class
	 */
	public void registerAssociationClass( String p_sId, UmlAssociationClass p_oAssociationClass ) {
		this.associationClasses.put( p_sId, p_oAssociationClass );
		this.stereotypeObjects.put(p_sId, p_oAssociationClass);
	}
	
	/**
	 * Return association class by id
	 * @param p_sId id
	 * @return association class
	 */
	public UmlAssociationClass getAssociationClassById( String p_sId ) {
		return (UmlAssociationClass) this.associationClasses.get(p_sId);
	}
	
	/**
	 * Return association end by id
	 * @param p_sId id
	 * @return association end
	 */
	public UmlAssociationEnd getAssociationEndById( String p_sId ) {
		return (UmlAssociationEnd) this.associationEnds.get(p_sId);
	}

	/**
	 * Return all association classes
	 * @return association class
	 */
	public Collection<UmlAssociationClass> getAssociationClasses() {
		return this.associationClasses.values();
	}
	
	/**
	 * Register a stereotype
	 * @param p_sId stereotype id
	 * @param p_oUmlStereotype uml stereotype
	 */
	public void registerStereotype( String p_sId, UmlStereotype p_oUmlStereotype ) {
		this.stereotypesById.put( p_sId, p_oUmlStereotype );
		this.stereotypesByName.put( p_oUmlStereotype.getName(), p_oUmlStereotype );
	}
	
	/**
	 * Return stereotype by id
	 * @param p_sId id
	 * @return stereotype
	 */
	public UmlStereotype getStereotypeById( String p_sId ) {
		for(String sKey : this.stereotypesById.keySet() ){
			if ( sKey !=null && sKey.equalsIgnoreCase(p_sId)){
				return this.stereotypesById.get(sKey) ;
			}
		}
		return null ;
	}
	
	/**
	 * Return stereotype by name
	 * @param p_sName name
	 * @return stereotype
	 */
	public UmlStereotype getStereotypeByName( String p_sName ) {
		for( UmlStereotype oStereotype : this.stereotypesByName.values() ){
			if ( oStereotype !=null && oStereotype.getName().equalsIgnoreCase(p_sName)){
				return oStereotype ;
			}
		}
		return null ;
	}
	
	/**
	 * Register an uml stereotype object
	 * @param p_sId id
	 * @param p_oUmlStereotypedObject stereotype object
	 */
	public void registerStereotypeObject( String p_sId, UmlStereotypedObject p_oUmlStereotypedObject ) {
		this.stereotypeObjects.put(p_sId, p_oUmlStereotypedObject);
	}
	
	/**
	 * Add a datatype
	 * @param p_sId datatype id
	 * @param p_oUmlDataType datatype
	 */
	public void addDataType(String p_sId, UmlDataType p_oUmlDataType ) {
		this.dataTypes.put(p_sId, p_oUmlDataType );		
	}
	
	/**
	 * Return datatype by id
	 * @param p_sId id
	 * @return datatype
	 */
	public UmlDataType getDataType( String p_sId ) {
		return (UmlDataType) this.dataTypes.get( p_sId );
	}

	/**
	 * Return all datatypes
	 * @return all datatypes
	 */
	public Collection<UmlDataType> getDataTypes() {
		return this.dataTypes.values();
	}
	
	/**
	 * Return datatype by name
	 * @param p_sName name
	 * @return datatype
	 */
	public UmlDataType getDataTypeByName( String p_sName ) {
		for( UmlDataType oDataType : this.dataTypes.values()) {
			if ( oDataType.getName().equals(p_sName)) {
				return oDataType ;
			}
		}
		return null ;
	}
	
	/**
	 * Return uml usages
	 * @return uml usages
	 */
	public List<UmlUsage> getUsages() {
		return this.usages;
	}
	
	/**
	 * Register a uml usage
	 * @param p_sId usage id
	 * @param p_oUsage usage
	 */
	public void registerUsage(String p_sId, UmlUsage p_oUsage) {
		this.usages.add(p_oUsage);
		this.stereotypeObjects.put(p_sId, p_oUsage);
	}

	/**
	 * Return uml comments
	 * @return uml comments
	 */
	public List<UmlComment> getComments() {
		return this.comments;
	}
	
	/**
	 * Register a uml comment
	 * @param p_sId comment id
	 * @param p_oComment comment
	 */
	public void registerComment(String p_sId, UmlComment p_oComment) {
		this.comments.add(p_oComment);
		this.stereotypeObjects.put(p_sId, p_oComment);
	}
	
	/**
	 * Register an association end
	 * @param p_sId if of association end
	 * @param p_oUmlAssociationEnd association end
	 */
	public void registerAssociationEnd(String p_sId, UmlAssociationEnd p_oUmlAssociationEnd) {
		this.associationEnds.put(p_sId, p_oUmlAssociationEnd);
		this.stereotypeObjects.put(p_sId, p_oUmlAssociationEnd);
	}
	
	/**
	 * Return stereotype object by id
	 * @param p_sId stereotype id
	 * @return stereotype object
	 */
	public UmlStereotypedObject getStereotypeObjectById( String p_sId ) {
		return this.stereotypeObjects.get(p_sId);
	}
}
