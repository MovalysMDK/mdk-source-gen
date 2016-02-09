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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.rits.cloning.Cloner;

@XmlRootElement
public class UmlModel extends UmlStereotypedObject {

	/**
	 * Packages
	 */
	@XmlElement(name="package")
	private List<UmlPackage> packages = new ArrayList<UmlPackage>();
	
	/**
	 * Classes
	 */
	@XmlTransient
	private List<UmlClass> classes = new ArrayList<UmlClass>();
	
	/**
	 * Dictionary
	 */
	@XmlElement
	private UmlDictionary dictionary = new UmlDictionary();

	/**
	 * @return
	 */
	public List<UmlPackage> getPackages() {
		return this.packages ;
	}

	/**
	 * @param p_oPackage
	 */
	public void addPackage(UmlPackage p_oPackage) {
		this.packages.add(p_oPackage);
	}
	
	/**
	 * @return
	 */
	public List<UmlClass> getClasses() {
		return this.classes ;
	}

	/**
	 * @param p_oPackage
	 */
	public void addClass(UmlClass p_oClass) {
		this.classes.add(p_oClass);
	}

	/**
	 * @return
	 */
	public UmlDictionary getDictionnary() {
		return this.dictionary;
	}
	
	/**
	 * Copy uml model
	 * @return
	 */
	public UmlModel copy() {
		Cloner oCloner = new Cloner();
		return oCloner.deepClone(this);
	}
}
