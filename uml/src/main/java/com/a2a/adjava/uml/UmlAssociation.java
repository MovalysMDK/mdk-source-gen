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
import javax.xml.bind.annotation.XmlElement;

/**
 * <p>Uml Association</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UmlAssociation extends UmlStereotypedObject {

	/**
	 * Association name
	 */
	@XmlAttribute
	private String name ;
	
	/**
	 * Association end 1 
	 */
	@XmlElement
	private UmlAssociationEnd associationEnd1 ;
	
	/**
	 *  Association end 2
	 */
	@XmlElement
	private UmlAssociationEnd associationEnd2 ;
	
	/**
	 * Association options
	 */
	@XmlAttribute
	private String options ;
	
	/**
	 * Empty constructor for jaxb
	 */
	private UmlAssociation() {
		// empty constructor for jaxb
	}
	
	/**
	 * Constructor
	 * @param p_sName associatio name
	 * @param p_oAssociationEnd1 association end 1
	 * @param p_oAssociationEnd2 association end 2
	 * @param p_sOptions association options
	 */
	public UmlAssociation(String p_sName, UmlAssociationEnd p_oAssociationEnd1,
			UmlAssociationEnd p_oAssociationEnd2, String p_sOptions) {
		super();
		if (p_sName != null){
			this.name = p_sName.trim();
		}else {
			this.name = null ;
		}
		this.associationEnd1 = p_oAssociationEnd1;
		this.associationEnd2 = p_oAssociationEnd2;
		if ( this.associationEnd1 != null ) {
			this.associationEnd1.setAssociation(this);
			this.associationEnd1.setOppositeAssociation(this.associationEnd2);
		}
		if ( this.associationEnd2 != null ) {
			this.associationEnd2.setAssociation(this);
			this.associationEnd2.setOppositeAssociation(this.associationEnd1);
		}
		this.options = p_sOptions;
		
	}

	/**
	 * Get association name
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get association end 1
	 * @return
	 */
	public UmlAssociationEnd getAssociationEnd1() {
		return associationEnd1;
	}

	/**
	 * Get association end 2
	 * @return
	 */
	public UmlAssociationEnd getAssociationEnd2() {
		return associationEnd2;
	}

	/**
	 * @return
	 */
	public String getOptions() {
		return options;
	}

	/**
	 * @param p_sOptions
	 */
	public void setOptions(String p_sOptions) {
		this.options = p_sOptions;
	}
}
