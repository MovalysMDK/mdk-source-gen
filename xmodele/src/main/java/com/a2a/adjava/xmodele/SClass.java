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
package com.a2a.adjava.xmodele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.dom4j.Element;

import com.a2a.adjava.utils.ToXmlUtils;


public class SClass<MASTERINTERFACE extends SInterface, METHODSIGNATURE extends MMethodSignature> extends SWithMethodElement<METHODSIGNATURE> {

	/**
	 * Attributes map
	 */
	private Map<String,MAttribute> mapNameAttributes ;
	
	/**
	 * Attribute list 
	 */
	private List<MAttribute> attributes ;

	/**
	 * Identifier
	 */
	private MIdentifier identifier ;
	
	/**
	 * Stereotypes
	 */
	private List<MStereotype> stereotypes ;
	
	/**
	 * Master interface 
	 */
	private MASTERINTERFACE masterInterface ;
	
	/**
	 * @param p_sType
	 * @param p_sUmlName
	 * @param p_sName
	 * @param p_oPackage
	 */
	public SClass(String p_sType, String p_sUmlName, String p_sName, MPackage p_oPackage) {
		super(p_sType, p_sUmlName, p_sName, p_oPackage);
		this.attributes = new ArrayList<MAttribute>();
		this.mapNameAttributes = new HashMap<String,MAttribute>();
		this.stereotypes = new ArrayList<MStereotype>();
		this.identifier = new MIdentifier();
		this.setBeanName(null);//pour rester compatible avec l'existant
	}
	
	/**
	 * @param p_oInterface
	 */
	public void setMasterInterface( MASTERINTERFACE p_oInterface ) {
		this.masterInterface = p_oInterface;
		addImport( p_oInterface.getFullName());
	}
	
	/**
	 * @return
	 */
	public MASTERINTERFACE getMasterInterface() {
		return this.masterInterface;
	}
	
	/**
	 * @return
	 */
	public MIdentifier getIdentifier() {
		return this.identifier;
	}
	
	
	/**
	 * @return
	 */
	protected void setIdentifier( MIdentifier p_oIdentifier) {
		this.identifier = p_oIdentifier;
	}
	
	
	/**
	 * @return
	 */
	public List<MAttribute> getAttributes() {
		return this.attributes;
	}
	
	public List<String> getAttributeNames() {
		List<String> r_listNames = new ArrayList<>();
		for( MAttribute oAttr : this.attributes ) {
			r_listNames.add(oAttr.getName());
		}
		return r_listNames;
	}
	
	/**
	 * @param p_listAttributes
	 */
	protected void setAttributes( List<MAttribute> p_listAttributes ) {
		this.attributes = p_listAttributes;
	}
	
	/**
	 * @param p_sName
	 * @return
	 */
	public MAttribute getAttributeByName( String p_sName, boolean p_bCaseSensitive ) {
		MAttribute r_oAttribute = null ;
		if ( p_bCaseSensitive ) {
			r_oAttribute = (MAttribute) this.mapNameAttributes.get(p_sName);
		} else {
			CaseInsensitiveMap oCaseInsensitiveMap = new CaseInsensitiveMap(this.mapNameAttributes);
			r_oAttribute = (MAttribute) oCaseInsensitiveMap.get(p_sName);
		}
		
		if ( r_oAttribute == null ) {
			MIdentifierElem oMIdentifierElem = this.identifier.getElemByName(p_sName, p_bCaseSensitive );
			if ( oMIdentifierElem instanceof MAttribute ) {
				r_oAttribute = (MAttribute) oMIdentifierElem ;
			}
		}
		return r_oAttribute ;
	}
	
	/**
	 * @param p_oAttribute
	 */
	public void addAttribute( MAttribute p_oAttribute ) {
		if ( p_oAttribute.isPartOfIdentifier()) {
			this.identifier.addElem(p_oAttribute);
		} else {
			this.attributes.add( p_oAttribute );
			this.mapNameAttributes.put( p_oAttribute.getName(), p_oAttribute );
		}
		this.addImportForAttribute(p_oAttribute);
	}
	
	public void resetAttributes() {
		this.attributes = new ArrayList<MAttribute>();
		this.mapNameAttributes = new HashMap<String,MAttribute>();
	}
	
	/**
	 * @param p_oAttribute
	 */
	protected void addImportForAttribute( MAttribute p_oAttribute ) {
		if ( !p_oAttribute.getTypeDesc().isPrimitif()) {
			addImport( p_oAttribute.getTypeDesc().getName());
		}
		for( MAttribute oSubAttr : p_oAttribute.getProperties()) {
			addImportForAttribute(oSubAttr);
		}
	}
	
	/**
	 * Retourne vrai si la classe possède l'attribut
	 * @param p_sAttributeName nom de l'attribut recherché
	 * @return vrai si la classe possède l'attribut
	 */
	public boolean hasAttribute( String p_sAttributeName ) {
		boolean r_bHasAttribute = false ;
		
		Iterator<MAttribute> iterAttr = this.attributes.iterator();
		while( iterAttr.hasNext() && !r_bHasAttribute ) {
			MAttribute oAttr = iterAttr.next();
			r_bHasAttribute = oAttr.getName().equalsIgnoreCase(p_sAttributeName);
		}
		
		if ( !r_bHasAttribute ) {
			iterAttr = this.identifier.getElemOfTypeAttribute().iterator();
			while( iterAttr.hasNext() && !r_bHasAttribute ) {
				MAttribute oAttr = iterAttr.next();
				r_bHasAttribute = oAttr.getName().equalsIgnoreCase(p_sAttributeName);
			}
		}
		
		return r_bHasAttribute ;
	}
	
	public void addStereotype( MStereotype p_oStereotype ) {
		this.stereotypes.add( p_oStereotype );
	}

	/**
	 * Permet de récupérer la liste des Stereotypes de la classe
	 */
	public List<MStereotype> getStereotypes() {
		return this.stereotypes;
	}
	 
	/**
	 * @param p_sStereotype
	 * @return
	 */
	public boolean hasStereotype( String p_sStereotype ) {
		boolean r_bHasStereotype = false ;
		for( MStereotype oStereotype: this.stereotypes) {
			if ( oStereotype.getName().equals(p_sStereotype)) {
				r_bHasStereotype = true ;
				break;
			}
		}
		return r_bHasStereotype ;
	}
	
	@Override
	protected void toXmlInsertBeforeDocumentation(Element p_xElement) {
		super.toXmlInsertBeforeDocumentation(p_xElement);
		Element xStereotype = p_xElement.addElement("stereotypes");
		for( MStereotype oStereotype : this.stereotypes ) {
			xStereotype.add(oStereotype.toXml());
		}
	}
	
	@Override
	protected void toXmlInsertBeforeImport(Element p_xElement) {
		super.toXmlInsertBeforeImport(p_xElement);
		this.toXmlInsertBeforeIdentifier(p_xElement);
		p_xElement.add( this.identifier.toXml());
		this.toXmlInsertAfterIdentifier(p_xElement);
		for( MAttribute oAttribute : getAttributes()) {
			p_xElement.add( oAttribute.toXml());
		}
		ToXmlUtils.addImplements(p_xElement, this.masterInterface);
	}
	
	protected void toXmlInsertBeforeIdentifier(Element p_xElement) {
		//NothingToDo
	}
	
	protected void toXmlInsertAfterIdentifier(Element p_xElement) {
		//NothingToDo
	}
}
