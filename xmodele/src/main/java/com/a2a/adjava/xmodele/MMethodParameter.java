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

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.a2a.adjava.types.ITypeDescription;

/**
 * Parameter of method
 * @author lmichenaud
 *
 */
public class MMethodParameter {

	/**
	 * Parameter name
	 */
	private String name ;
	
	/**
	 * Type description 
	 */
	private ITypeDescription typeDesc ;
	
	/**
	 * Constructor
	 * @param p_sName parameter name
	 * @param p_oTypeDesc parameter type
	 */
	public MMethodParameter( String p_sName, ITypeDescription p_oTypeDesc ) {
		this.name = p_sName ;
		this.typeDesc = p_oTypeDesc ;
	}
	
	/**
	 * Get parameter name
	 * @return parameter name
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * Get parameter type
	 * @return parameter type
	 */
	public ITypeDescription getTypeDesc() {
		return this.typeDesc;
	}
	
	/**
	 * Xml representation of method parameter
	 * @return xml element
	 */
	public Element toXml() {
		Element r_xParameter = DocumentHelper.createElement("method-parameter");
		r_xParameter.addAttribute("name", this.name);
		r_xParameter.addAttribute("name-capitalized", StringUtils.capitalize(this.name));
		r_xParameter.addAttribute("type-name", this.typeDesc.getName());
		r_xParameter.addAttribute("type-short-name", this.typeDesc.getShortName());
		if ( this.typeDesc.getParameterizedElementType().size() == 1 ) {
			r_xParameter.addAttribute("contained-type-name", 
				this.typeDesc.getParameterizedElementType().get(0).getName());
			r_xParameter.addAttribute("contained-type-short-name", 
				this.typeDesc.getParameterizedElementType().get(0).getShortName());
		}
		else {
			Element xPz = r_xParameter.addElement("parameterized");
			int iPos = 0;
			for(ITypeDescription oType : this.typeDesc.getParameterizedElementType()) {
				iPos++;
				Element xParam = xPz.addElement("param");
				xParam.addAttribute("pos", String.valueOf(iPos));
				xParam.addAttribute("type-name", oType.getName());
				xParam.addAttribute("type-short-name", oType.getShortName());
			}
		}
			
		return r_xParameter ;
	}
}
