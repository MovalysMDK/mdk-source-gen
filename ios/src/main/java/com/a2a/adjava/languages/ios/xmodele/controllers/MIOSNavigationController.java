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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * Navigation controller
 * @author lmichenaud
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class MIOSNavigationController extends MIOSViewController {

	/**
	 * Navigation bar id
	 */
	@XmlAttribute
	private String navigationBarId ;

	/**
	 * Root controller
	 */
	@XmlIDREF
	@XmlAttribute
	private MIOSController rootController;
	
	/**
	 * Segue id for root view controller
	 */
	@XmlAttribute
	private String rootControllerSegueId;
	
	/**
	 * Constructor
	 */
	public MIOSNavigationController() {
		this.setControllerType(MIOSControllerType.NAVIGATION);
	}
	
	/**
	 * Return navigation bar id 
	 * @return navigation bar id
	 */
	public String getNavigationBarId() {
		return navigationBarId;
	}

	/**
	 * Define navigation bar id
	 * @param p_sNavigationBarId navigation bar id
	 */
	public void setNavigationBarId(String p_sNavigationBarId) {
		this.navigationBarId = p_sNavigationBarId;
	}

	/**
	 * Return root controller
	 * @return root controller
	 */
	public MIOSController getRootController() {
		return this.rootController;
	}

	/**
	 * Define root controller
	 * @param p_oRootController root controller
	 */
	public void setRootViewController(MIOSController p_oRootController) {
		this.rootController = p_oRootController;
	}

	/**
	 * Return segue id of root controller
	 * @return segue id of root controller
	 */
	public String getRootControllerSegueId() {
		return this.rootControllerSegueId;
	}

	/**
	 * Define segue id of root controller
	 * @param p_sRootControllerSegueId segue id of root controller
	 */
	public void setRootViewControllerSegueId(String p_sRootControllerSegueId) {
		this.rootControllerSegueId = p_sRootControllerSegueId;
	}
}
