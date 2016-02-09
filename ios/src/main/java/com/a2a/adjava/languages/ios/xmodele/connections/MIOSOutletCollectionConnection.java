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

/**
 * OutletCollection connection
 * @author qlagarde
 *
 */
public class MIOSOutletCollectionConnection extends MIOSConnection {

	/**
	 * Property
	 */
	private String property ;

	/**
	 * Constructor
	 */
	public MIOSOutletCollectionConnection() {
		super(MIOSConnectionType.OUTLET_COLLECTION);
	}
	
	/**
	 * Return property
	 * @return property
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * Set property
	 * @param p_sProperty define property
	 */
	public void setProperty(String p_sProperty) {
		this.property = p_sProperty;
	}
}
