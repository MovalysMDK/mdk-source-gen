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
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;

/**
 * @author lmichenaud
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UmlUsage extends UmlStereotypedObject {
	
	/**
	 * Client class
	 */
	@XmlAttribute
	@XmlIDREF
	private UmlClass client ;

	/**
	 * Supplier class 
	 */
	@XmlAttribute
	@XmlIDREF	
	private UmlClass supplier ;

	/**
	 * Usage name 
	 */
	@XmlAttribute
	private String name ;
	
	/**
	 * Constructor
	 */
	private UmlUsage() {
		// Empty constructor for jaxb
	}
	
	/**
	 * Constructor
	 * @param p_sName le nom de l'usage
	 * @param p_oCustomer le client de l'usage
	 * @param p_oSupplier le fournisseur de l'usage
	 */
	public UmlUsage(String p_sName, UmlClass p_oCustomer, UmlClass p_oSupplier) {
		super();
		this.client = p_oCustomer;
		this.supplier = p_oSupplier;
		if (p_sName != null){
			this.name = p_sName.trim();
		}else {
			this.name = null ;
		}
		
	}
	
	/**
	 * @return le nom de l'usage
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return la classe cliente
	 */
	public UmlClass getClient() {
		return this.client;
	}
	
	/**
	 * @return la classe fournisseur
	 */
	public UmlClass getSupplier() {
		return this.supplier;
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return StringUtils.join("UmlUsage[name:", this.name, ",client:", this.client, ",supplier:", this.supplier,"]");
	}
}
