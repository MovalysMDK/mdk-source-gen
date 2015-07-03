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

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Defined an element from the UML model
 * @author smaitre
 */
public class SElement {

	/**
	 * The type of element : first tag in xml
	 */
	private String type = null;

	/**
	 * The name of umlElement, if null then element is not defined in Uml
	 */
	protected String umlName = null;

	/**
	 * Parameter map
	 */
	private Map<String, String> parameters = null;

	/**
	 * Empty constructor for jaxb
	 */
	protected SElement() {
		// Empty constructor for jaxb
	}

	/**
	 * Construct a new MUmlElement
	 * @param p_sType the type of element, the fisrt tag of xml
	 * @param p_sUmlName the uml name
	 */
	public SElement(String p_sType, String p_sUmlName) {
		this.umlName = p_sUmlName;
		this.type = p_sType;
		this.parameters = new TreeMap<String, String>();
	}

	/**
	 * Return uml name
	 * @return the uml name
	 */
	public String getUmlName() {
		return this.umlName;
	}

	/**
	 * Return parameter map
	 * @return parameter map
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}

	/**
	 * Return the parameter value
	 * @param p_sParameterName parameter name
	 * @return value of parameter 
	 */
	public String getParameterValue( String p_sParameterName ) {
		return this.parameters.get(p_sParameterName);
	}

	/**
	 * Add a parameter
	 * @param p_sKey parameter name
	 * @param p_sValue parameter value
	 */
	public final void addParameter(String p_sKey, String p_sValue) {
		this.parameters.put(p_sKey, p_sValue);
	}

	/** 
	 * Converted the element to xml
	 * @return the xml representation of element
	 */
	public Element toXml() {
		Element r_x = DocumentHelper.createElement(this.type);
		if (this.umlName!=null) {
			r_x.addElement("uml-name").setText(this.umlName);
			r_x.addElement("uml-name-u").setText(StringUtils.capitalize(this.umlName));
		}
		Element xParams = r_x.addElement("parameters");
		Element xParam = null;
		if(parameters !=null) {
			for(Entry<String, String> oEntry : parameters.entrySet()) {
				xParam = xParams.addElement("parameter");
				xParam.setText(oEntry.getValue());
				xParam.addAttribute("name", oEntry.getKey());
			}
		}
		return r_x;
	}
	
	public void copyTo(SElement oSElement){
		oSElement.parameters = new TreeMap<String, String>();
		oSElement.parameters.putAll(this.parameters);
		oSElement.type = this.type;
	}
}
