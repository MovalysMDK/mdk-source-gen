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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.a2a.adjava.uml.jaxb.MapUmlStereotypeAdapter;

/**
 * Represente un objet UML avec une liste de stereotype
 * @author smaitre
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UmlStereotypedObject {

	/**
	 * Uml stereotypes
	 */
	@XmlJavaTypeAdapter(MapUmlStereotypeAdapter.class)
	@XmlAttribute
	private Map<String, UmlStereotype> stereotypes = new HashMap<>();

	/**
	 * Parameters passed to the UML stereotypes
	 */
	private Map<String, Object[]> stereotypesParameters = new HashMap<>();

	/**
	 * Retourne l'objet stereoTypes
	 * @return o collection of <em>UmlStereotype</em> values
	 */
	public Collection<UmlStereotype> getStereotypes() {
		return this.stereotypes.values();
	}

	/**
	 * Return stereotype names
	 * @return a collection of String values
	 */
	public Collection<String> getStereotypeNames() {
		return this.stereotypes.keySet();
	}

	/**
	 * Add a stereotype in the list
	 * @param p_oStereotype
	 */
	public void addStereotype(UmlStereotype p_oStereotype) {
		this.stereotypes.put(p_oStereotype.getName(), p_oStereotype);
	}

	/**
	 * Add a stereotype in the list, with its corresponding parameters
	 * @param p_oStereotype Stereotype to add to the class
	 * @param p_oParameters Stereotype parameters
	 */
	public void addStereotype(UmlStereotype p_oStereotype, Object... p_oParameters) {

		this.addStereotype(p_oStereotype);
		this.stereotypesParameters.put(p_oStereotype.getId(), p_oParameters);
	}

	/**
	 * Add a list of stereotypes
	 * @param p_listStereotypes
	 */
	public void addStereotypes(Collection<UmlStereotype> p_listStereotypes) {
		for (UmlStereotype oStereotype : p_listStereotypes) {
			this.stereotypes.put(oStereotype.getName(), oStereotype);
		}
	}

	/**
	 * Return true if at least one stereotype of p_sLstStrereotype is in current stereotype list
	 * @param p_sListStereotype the stereotypes list
	 * @return true or false
	 */
	public boolean hasAnyStereotype(List<String> p_sListStereotype) {
		if (p_sListStereotype != null) {
			for (String sKey : this.getStereotypeNames()) {
				for (String sSearchedKey : p_sListStereotype) {
					if (sKey != null && sKey.equalsIgnoreCase(sSearchedKey)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Return true if at least one stereotype of p_sLstStrereotype is in current stereotype list
	 * @param p_sLstStereotype the stereotypes list
	 * @return true or false
	 */
	public boolean hasAnyStereotype(HashSet<UmlStereotype> p_sLstStereotype) {
		if (p_sLstStereotype != null) {
			for (String sKey : this.getStereotypeNames()) {
				for (UmlStereotype oStereoType : p_sLstStereotype) {
					if (sKey != null && sKey.equalsIgnoreCase(oStereoType.getName())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Return true if at least one stereotype of p_sLstStrereotype is in current stereotype list
	 * @param p_listStereotypes the stereotypes list
	 * @return true or false
	 */
	public boolean hasAnyStereotype(String... p_listStereotypes) {
		for (String sKey : this.getStereotypeNames()) {
			for (String sSearchedKey : p_listStereotypes) {
				if (sKey != null && sKey.equalsIgnoreCase(sSearchedKey)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Return true if the stereotype list contains the object that is linked to the specified name as parameter.
	 * @param p_sName the name of the stereotype
	 * @return true if the stereotype is present, false otherwise
	 */
	public boolean hasStereotype(String p_sName) {
		for (String sKey : this.getStereotypeNames()) {
			if (sKey != null && sKey.equalsIgnoreCase(p_sName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return true if the object has all stereotypes.
	 * @param p_listStereotypes stereotypes name
	 * @return true if the object has all stereotypes.
	 */
	public boolean hasAllStereotypes(String... p_listStereotypes) {
		for (String sSearchedKey : p_listStereotypes) {
			boolean bFound = false;
			for (String sKey : this.getStereotypeNames()) {
				if (sKey != null && sKey.equalsIgnoreCase(sSearchedKey)) {
					bFound = true;
					break;
				}
			}
			if (!bFound) {
				return false;
			}
		}
		return true;
	}

	public Object[] getStereotypeParameters(String p_sStereotypeId) {
		return this.stereotypesParameters.get(p_sStereotypeId);
	}
}
