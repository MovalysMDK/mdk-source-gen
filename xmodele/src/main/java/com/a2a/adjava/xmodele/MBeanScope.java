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

/**
 * @author lmichenaud
 *
 */
public enum MBeanScope {

	/**
	 * Request scope:
	 * Bean instance is available during the time of the request. At the end of request, bean is destroyed.
	 */
	REQUEST,
	
	/**
	 * Page scope:
	 * Bean instance is available as long as the user is on the same window. When user changes the displayed window, bean is destroyed.
	 */
	WINDOW,
	
	/**
	 * User scope
	 * Bean instance is available as long as the user is connected. When user logs out, bean is destroyed.
	 */
	SESSION,
	
	/**
	 * Bean instance is available during all the application lifecycle. When the application stops, bean is destroyed.
	 * Application scope
	 */
	APPLICATION ;
}
