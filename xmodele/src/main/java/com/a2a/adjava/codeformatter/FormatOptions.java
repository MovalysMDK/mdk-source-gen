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
package com.a2a.adjava.codeformatter;

/**
 * Format options
 * @author lmichenaud
 *
 */
public class FormatOptions {

	/**
	 * Format options
	 */
	public static final FormatOptions NO_FORMAT = new FormatOptions(false);
	
	/**
	 * Format, true by default
	 */
	private boolean format = true ;
	
	/**
	 * Constructor
	 */
	public FormatOptions() {
		super();
	}
	
	/**
	 * Constructor
	 * @param p_bFormat format
	 */
	public FormatOptions(boolean p_bFormat) {
		super();
		this.format = p_bFormat;
	}

	/**
	 * Format file or not
	 * @return true if the generated file must be formatted
	 */
	public boolean doFormat() {
		return this.format;
	}
}
