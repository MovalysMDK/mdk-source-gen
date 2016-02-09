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
import java.util.List;


/**
 * Model of domain
 * @author lmichenaud
 *
 * @param <MD> dictionary
 */
public class XModele<MD extends IModelDictionary> {

	/**
	 * Dictionary
	 */
	private MD modelDictionnary;
	
	/**
	 * Packages 
	 */
	private List<MPackage> packages ;
	
	/**
	 * Classes
	 */
	private List<MEntityImpl> classes ;

	/**
	 * Constructor
	 * @param p_oModeleDictionnary dictionary
	 */
	public XModele( MD p_oModeleDictionnary) {
		this.modelDictionnary = p_oModeleDictionnary ;
		this.packages = new ArrayList<MPackage>();
		this.classes = new ArrayList<MEntityImpl>();
	}

	/**
	 * Get dictionary
	 * @return dictionary
	 */
	public MD getModelDictionnary() {
		return this.modelDictionnary;
	}
	
	/**
	 * Get packages
	 * @return packages
	 */
	public List<MPackage> getPackages() {
		return this.packages ;
	}

	/**
	 * Add package
	 * @param p_oPackage package
	 */
	public void addPackage( MPackage p_oPackage) {
		this.packages.add(p_oPackage);
	}
	
	/**
	 * Classes
	 * @return classes
	 */
	public List<MEntityImpl> getClasses() {
		return this.classes ;
	}

	/**
	 * Add a class
	 * @param p_oClass class to add
	 */
	public void addClass( MEntityImpl p_oClass) {
		this.classes.add(p_oClass);
	}

	/**
	 * Set dictionary
	 * @param p_oModelDictionary dictionary
	 */
	public void setModelDictionnary(MD p_oModelDictionary) {
		this.modelDictionnary = p_oModelDictionary;
	}
}
