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
package com.a2a.adjava.languages.ios.xmodele.controllers;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Form view controller
 * @author lmichenaud
 *
 */
@XmlRootElement(name="controller")
@XmlAccessorType(XmlAccessType.FIELD)
public class MIOSSearchViewController extends MIOSViewController {
		
	/**
	 * Save action name
	 */
	@XmlElementWrapper
	@XmlElement(name="saveActionName")
	private List<String> saveActionNames ;
	
	/**
	 * Constructor
	 */
	public MIOSSearchViewController() {
		this.setControllerType(MIOSControllerType.SEARCHVIEW);
	}

	/**
	 * Get action names for save
	 * @return action names
	 */
	public List<String> getSaveActionNames() {
		return saveActionNames;
	}

	/**
	 * Set action names
	 * @param p_listSaveActionNames action names for save
	 */
	public void setSaveActionNames(List<String> p_listSaveActionNames) {
		this.saveActionNames = p_listSaveActionNames;
	}
}
