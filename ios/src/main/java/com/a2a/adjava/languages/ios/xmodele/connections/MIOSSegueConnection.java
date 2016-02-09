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
package com.a2a.adjava.languages.ios.xmodele.connections;

import com.a2a.adjava.languages.ios.xmodele.controllers.MIOSController;

/**
 * Outlet connection
 * @author lmichenaud
 *
 */
public class MIOSSegueConnection extends MIOSConnection {

	/**
	 * destination of the segue connection
	 */
	private MIOSController destination;

	/**
	 * Constructor
	 */
	public MIOSSegueConnection() {
		super(MIOSConnectionType.SEGUE);
	}

	/**
	 * Gets the destination of the connection
	 * @return the destination of the connection
	 */
	public MIOSController getDestination() {
		return destination;
	}

	/**
	 * Sets the destination of the connection
	 * @param p_oDestination the destination of the connection
	 */
	public void setDestination(MIOSController p_oDestination) {
		this.destination = p_oDestination;
	}

}
