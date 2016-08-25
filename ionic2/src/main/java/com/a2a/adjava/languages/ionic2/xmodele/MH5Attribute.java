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
package com.a2a.adjava.languages.ionic2.xmodele;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.a2a.adjava.xmodele.MVisualField;

public class MH5Attribute {

	
	/**
	 * viewmodel data of the attribute
	 */
	private String attributeFieldName;

	/**
	 * Visual information of the attribute
	 */
	private MVisualField visualFieldAttribute;

	/**
	 * Child attributes
	 */
	private List<MH5Attribute> childAttributes = new ArrayList<>();

	/**
	 * 
	 */
	private Map<String, String> complementaryOptions;

	/**
	 * Constructor of the HTML5 attribute
	 * @param p_oVisualFieldAttribute
	 */
	public MH5Attribute(MVisualField p_oVisualFieldAttribute) {
		this.visualFieldAttribute = p_oVisualFieldAttribute;
		computeAttributeFieldName();
	}

	/**
	 * Getter of the MVisualField corresponding to this MH5Attribute
	 * 
	 * @return the MVisualField linked to this MH5Attribute
	 */
	public MVisualField getVisualFieldAttribute() {
		return visualFieldAttribute;
	}

	/**
	 * Setter of the MVisualField corresponding to this MH5Attribute
	 * 
	 * @param vmDataAttribute
	 *            the new MVisualField linked to this MH5Attribute
	 */
	public void setVisualFieldAttribute(MVisualField pVisualFieldAttribute) {
		this.visualFieldAttribute = pVisualFieldAttribute;
		computeAttributeFieldName();
		
	}

	/**
	 * @return the complementaryOptions
	 */
	public Map<String, String> getComplementaryOptions() {
		return complementaryOptions;
	}

	/**
	 * @param complementaryOptions
	 *            the complementaryOptions to set
	 */
	public void setComplementaryOptions(Map<String, String> pComplementaryOptions) {
		this.complementaryOptions = pComplementaryOptions;
	}
	
	/**
	 * @param p_oChildAttr
	 */
	public void addChildAttribute( MH5Attribute p_oChildAttr ) {
		this.childAttributes.add( p_oChildAttr );
	}
	
	/**
	 * @return
	 */
	public List<MH5Attribute> getChildAttributes() {
		return this.childAttributes;
	}


	/**
	 * @return the attributeFieldName
	 */
	public String getAttributeFieldName() {
		return attributeFieldName;
	}

	public void setAttributeFieldName(String pAttributeFieldName) {
		this.attributeFieldName = pAttributeFieldName;
	}

	/**
	 * 
	 */
	public void computeAttributeFieldName(){
		String[] parts;
		parts = visualFieldAttribute.getName().split("__");
		if ( parts.length == 3 ) {
			attributeFieldName = parts[1];
		}
		else {
			attributeFieldName = parts[0];
		}
		
		attributeFieldName = attributeFieldName.replace('_', '.');
	}
	/**
	 * to Xml of the HTML5 attribute
	 * 
	 * @return the xml element of the attribute
	 */
	public Element toXml() {
		Element r_xChildAttribute = DocumentHelper.createElement("HTML-attribute");

		r_xChildAttribute.addElement("field-name").setText(this.attributeFieldName);

		r_xChildAttribute.add(this.visualFieldAttribute.toXml());

		if ( this.complementaryOptions != null ) {
			Element xOptions = r_xChildAttribute.addElement("options");
			for (String optionKey : this.complementaryOptions.keySet()) {
				xOptions = xOptions.addElement(optionKey);
				xOptions.setText(this.complementaryOptions.get(optionKey));
			}
		}
		
		Element xChildAttributes = r_xChildAttribute.addElement("child-attributes");
		for( MH5Attribute oMH5Attribute : this.childAttributes ) {
			xChildAttributes.add( oMH5Attribute.toXml());
		}
		
		return r_xChildAttribute;
	}
}
