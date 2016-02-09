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
package com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.model;

/**
 * Definit un attribut xml
 * @author smaitre
 *
 */
public class XAAttribute {

	/** le nom de l'attribut */
	private String name = null;
	/** la valeur de l'attribut */
	private String value = null;
	
	/**
	 * Donne le nom de l'attribut
	 * @return le nom de l'attribut
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Affecte le nom de l'attribut
	 * @param name le nom de l'attribut
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Donne la valeur de l'attribut
	 * @return la valeur de l'attribut
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Affecte la valeur de l'attribut
	 * @param la valeur de l'attribut
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Regarde si deux attributs ont la même valeur
	 * @param attribute l'attribut avec lequel comparé
	 * @return true si les deux attributs sont identiques
	 */
	public boolean isSameValue(XAAttribute attribute) {
		if (value==null && attribute.value== null) {
			return true;
		}
		else if (value!=null && attribute.value!=null) {
			return value.equals(attribute.value);
		}
		else {
			return false;
		}
	}
	
	/**
	 * Transforme en chaîne
	 */
	public String toString() {
		return this.name + "[" + this.value + "]";
	}
}
