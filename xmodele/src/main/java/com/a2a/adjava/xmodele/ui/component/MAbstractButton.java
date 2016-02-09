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
package com.a2a.adjava.xmodele.ui.component;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Abstract Button
 * 
 * @author lmichenaud
 *
 */
public class MAbstractButton {
	
	/**
	 * 
	 */
	private String name ;
	
	/**
	 * 
	 */
	private String labelId ;
	
	/**
	 * 
	 */
	private String labelValue ;
	
	/**
	 * Button type
	 */
	private MButtonType buttonType ;
	
	/**
	 * Extra parameter
	 */
	private Map<String, String> parameters = new HashMap<String, String>();
	
	/**
	 * @param p_sName
	 * @param p_sLabelId
	 * @param p_sLabelValue
	 * @param p_oButtonType
	 * @param p_oAction
	 */
	public MAbstractButton(String p_sName, String p_sLabelId, String p_sLabelValue, MButtonType p_oButtonType) {
		this.name = p_sName ;
		this.labelId = p_sLabelId ;
		this.labelValue = p_sLabelValue ;
		this.buttonType = p_oButtonType ;
	}

	/**
	 * @param p_sName
	 * @param p_oAction
	 * @param p_oDomain
	 */
	public MAbstractButton(String p_sName, MButtonType p_oButtonType) {
		this.name = p_sName ;
		this.labelId = p_oButtonType.getLabelId();
		this.labelValue = p_oButtonType.getLabelValue();
		this.buttonType = p_oButtonType ;
	}
	
	/**
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}

	/**
	 * @return
	 */
	public String getLabelId() {
		return labelId;
	}
	
	
	public String getLabelValue() {
		return labelValue;
	}
	
	/**
	 * @param p_sKey
	 * @param p_sValue
	 */
	public final void addParameter(String p_sKey, String p_sValue) {
		this.parameters.put(p_sKey, p_sValue);
	}
	
	/**
	 * @return
	 */
	public MButtonType getButtonType() {
		return buttonType;
	}
	
	/**
	 * @return
	 */
	public Element toXml() {
		
		Element r_xButton = DocumentHelper.createElement("button");
		r_xButton.addAttribute("name", this.name );
		r_xButton.addAttribute("type", this.buttonType.name());
		r_xButton.addAttribute("label-id", this.labelId );
		
		Element xParams = r_xButton.addElement("parameters");
		for(Entry<String, String> oEntry : parameters.entrySet()) {
			Element xParam = xParams.addElement("parameter");
			xParam.setText(oEntry.getValue());
			xParam.addAttribute("name", oEntry.getKey());
		}
		
		return r_xButton ;
	}
}
