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

import org.dom4j.Element;

import com.a2a.adjava.xmodele.MAction;

/**
 * Button that starts an action
 * 
 * @author lmichenaud
 *
 */
public class MActionButton extends MAbstractButton {
	
	/**
	 * Action to launch
	 */
	private MAction action ;
	
	/**
	 * @param p_sName
	 * @param p_oAction
	 * @param p_oDomain
	 */
	public MActionButton(String p_sName, String p_sLabelId, String p_sLabelValue, MButtonType p_oButtonType, MAction p_oAction) {
		super(p_sName, p_sLabelId, p_sLabelValue, p_oButtonType);
		this.action = p_oAction ;
	}
	
	/**
	 * @param p_sName
	 * @param p_oAction
	 * @param p_oDomain
	 */
	public MActionButton(String p_sName, MButtonType p_oButtonType, MAction p_oAction) {
		super(p_sName, p_oButtonType);
		this.action = p_oAction ;
	}

	/**
	 * @return
	 */
	public MAction getAction() {
		return action;
	}

	/**
	 * @return
	 */
	public Element toXml() {
		
		Element r_xButton = super.toXml();
		r_xButton.addAttribute("action-name", action.getName());
		r_xButton.setText(this.action.getMasterInterface().getFullName());		
		return r_xButton ;
	}
}
