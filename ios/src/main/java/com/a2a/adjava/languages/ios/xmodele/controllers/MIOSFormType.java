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

import javax.xml.bind.annotation.XmlEnum;

/**
 * Ios type controllers
 * @author lmichenaud
 *
 */
@XmlEnum
public enum MIOSFormType {

	/**
	 * A controller that have a unique tableView
	 */
	TABLE,
	
	/**
	 * A controller that does not have a tableView but screen fields only
	 */
	NO_TABLE,
	
	/**
	 * A controller that has a tableView and screen fields
	 */
	MIXTE,
	
	/**
	 * A controller that does not represent a form
	 */
	NOT_A_FORM
}
