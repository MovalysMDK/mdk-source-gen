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
 * Action connection
 * @author lmichenaud
 *
 */
public class MIOSActionConnection extends MIOSConnection {

	/**
	 * Selector
	 */
	private String selector;

	/**
	 * Constructor
	 */
	public MIOSActionConnection() {
		super(MIOSConnectionType.ACTION);
	}
	
	/**
	 * Return selector
	 * @return selector
	 */
	public String getSelector() {
		return this.selector;
	}

	/**
	 * Define selector
	 * @param p_sSelector selector
	 */
	public void setSelector(String p_sSelector) {
		this.selector = p_sSelector;
	}
}
