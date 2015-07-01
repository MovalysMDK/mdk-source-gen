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
package com.a2a.adjava.languages.html5.xmodele;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.a2a.adjava.xmodele.MVisualField;

/**
 * @author lmichenaud
 *
 */
public class MH5ComboAttribute extends MH5Attribute {

	/**
	 * Attributes displayed in selection
	 */
	private List<MH5Attribute> displayedAttributesInSelection = new ArrayList<>();

	/**
	 * Attribute used for the selected value
	 */
	private String valueAttribute ;

	
	/**
	 * @param p_oVisualFieldAttribute
	 */
	public MH5ComboAttribute(MVisualField p_oVisualFieldAttribute) {
		super(p_oVisualFieldAttribute);
	}
	
	/**
	 * @param p_oChildAttr
	 */
	public void addSelectionAttribute( MH5Attribute p_oChildAttr ) {
		this.displayedAttributesInSelection.add( p_oChildAttr );
	}
	
	/**
	 * @return
	 */
	public List<MH5Attribute> getDisplayedAttributesInSelection() {
		return this.displayedAttributesInSelection;
	}

	/**
	 * @return
	 */
	public String getValueAttribute() {
		return this.valueAttribute;
	}

	/**
	 * @param p_sValueAttribute
	 */
	public void setValueAttribute(String p_sValueAttribute) {
		this.valueAttribute = p_sValueAttribute;
	}

	/**
	 * to Xml of the HTML5 attribute
	 * 
	 * @return the xml element of the attribute
	 */
	@Override
	public Element toXml() {
		Element r_xAttribute = super.toXml();
		
		// do this correctly
		String sComboName = this.getAttributeFieldName();
		String sFirstLetter = sComboName.substring(0, 1);
		sComboName = sFirstLetter.toLowerCase() + sComboName.substring(1);
		r_xAttribute.element("field-name").setText(sComboName);
		
		r_xAttribute.addElement("value-attribute").setText(this.valueAttribute);
		
		Element xSelectionAttributes = r_xAttribute.addElement("displayed-attributes-in-selection");
		for( MH5Attribute oMH5Attribute : this.displayedAttributesInSelection ) {
			xSelectionAttributes.add( oMH5Attribute.toXml());
		}
		
		return r_xAttribute;
	}
}
