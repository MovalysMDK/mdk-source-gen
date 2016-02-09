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

import java.util.TreeMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import com.a2a.adjava.utils.StrUtils;

/**
 * Defined an element to generate
 * @author smaitre
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SGeneratedElement extends SElement {

	/**
	 * The name of generated element
	 */
	private String name = null;
	
	/**
	 * The full name of generated element, can be equals to name
	 */
	@XmlID
	private String fullName = null;
	
	/**
	 * Documentation
	 */
	private String documentation = null;
	
	/**
	 * Empty constructor for jaxb
	 */
	protected SGeneratedElement() {
		super();
	}
	
	/**
	 * Construct a new MGeneratedElement without package
	 * @param p_sType the type of element, the first tag of xml
	 * @param p_sUmlName the uml name
	 * @param p_sName the name used by element generated
	 */
	public SGeneratedElement(String p_sType, String p_sUmlName, String p_sName) {
		super(p_sType, p_sUmlName);
		this.name = p_sName;
		this.fullName = p_sName;
	}
	
	/**
	 * Define name
	 * @param p_sName name
	 */
	public void setName(String p_sName) {
		this.fullName = p_sName;
		this.name = StrUtils.substringAfterLastDot(p_sName);
	}
	
	/**
	 * Define full name
	 * @param p_sName full name
	 */
	protected void setFullName(String p_sName) {
		this.fullName = p_sName;
	}
	
	/**
	 * Return full name
	 * @return full name
	 */
	public String getFullName() {
		return this.fullName;
	}
	
	/**
	 * Return name
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Define documentation
	 * @param p_sDocumentation documentation
	 */
	public void setDocumentation(String p_sDocumentation) {
		this.documentation = p_sDocumentation;
	}
	
	/** 
	 * Converted the element to xml
	 * @return the xml representation of element
	 */
	public Element toXml() {
		Element r_xElem = super.toXml();
		r_xElem.addElement("name").setText(this.name);
		this.toXmlInsertAfterName(r_xElem);
		r_xElem.addElement("full-name").setText(this.fullName);
		this.toXmlInsertBeforeDocumentation(r_xElem);
		if (documentation!=null) {
			r_xElem.addElement("documentation").setText(this.documentation);
		}
		return r_xElem;
	}
	
	/**
	 * Called after setting name of xml element
	 * @param p_xElement xml element
	 */
	protected void toXmlInsertAfterName(Element p_xElement) {
		//NothingToDo
	}
	
	/**
	 * Called before adding documentation on xml element
	 * @param p_xElement xml element
	 */
	protected void toXmlInsertBeforeDocumentation(Element p_xElement) {
		//NothingToDo
	}
	
	public void copyTo(SGeneratedElement oSGeneratedElement){
		super.copyTo(oSGeneratedElement);
		oSGeneratedElement.documentation = this.documentation;
		oSGeneratedElement.fullName = this.fullName;
		oSGeneratedElement.name = this.name;
	}
}
