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
package com.a2a.adjava.languages.ios.extractors;

/**
 * Screen config
 * @author lmichenaud
 *
 */
public class ScreenConfig {

	/**
	 * Height
	 */
	private int height ;
	
	/**
	 * Width
	 */
	private int width ;

	/**
	 * Constructor
	 * @param p_iHeight screen height
	 * @param p_iWidth screen width
	 */
	public ScreenConfig(int p_iHeight, int p_iWidth) {
		super();
		this.height = p_iHeight;
		this.width = p_iWidth;
	}

	/**
	 * Return screen height
	 * @return screen height
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Return screen width
	 * @return screen width
	 */
	public int getWidth() {
		return this.width;
	}
}
