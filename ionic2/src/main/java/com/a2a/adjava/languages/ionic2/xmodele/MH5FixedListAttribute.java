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

import org.dom4j.Element;

import com.a2a.adjava.xmodele.MVisualField;

/**
 * @author lmichenaud
 *
 */
public class MH5FixedListAttribute extends MH5Attribute {


	/**
	 * Attributes of detail
	 */
	private List<MH5Attribute> detailAttributes = new ArrayList<>();
	
	/**
	 * Partial for detail
	 */
	private String detailPartial;
	
	/**
	 * @param p_oVisualFieldAttribute
	 */
	public MH5FixedListAttribute(MVisualField p_oVisualFieldAttribute) {
		super(p_oVisualFieldAttribute);
	}
	
	/**
	 * @param p_oChildAttr
	 */
	public void addDetailAttribute( MH5Attribute p_oChildAttr ) {
		this.detailAttributes.add( p_oChildAttr );
	}
	
	/**
	 * @return
	 */
	public List<MH5Attribute> getDetailAttributes() {
		return this.detailAttributes;
	}
	
	/**
	 * @return
	 */
	public String getDetailPartial() {
		return this.detailPartial;
	}

	/**
	 * @param p_sDetailPartial
	 */
	public void setDetailPartial(String p_sDetailPartial) {
		this.detailPartial = p_sDetailPartial;
	}

	/**
	 * to Xml of the HTML5 attribute
	 * 
	 * @return the xml element of the attribute
	 */
	@Override
	public Element toXml() {
		Element r_xAttribute = super.toXml();
		
		if(this.detailPartial!=null){
			r_xAttribute.addElement("detail-partial").setText(this.detailPartial);
		}
		Element xDetailAttributes = r_xAttribute.addElement("detail-attributes");
		for( MH5Attribute oMH5Attribute : this.detailAttributes ) {
			xDetailAttributes.add( oMH5Attribute.toXml());
		}
		
		return r_xAttribute;
	}
}
