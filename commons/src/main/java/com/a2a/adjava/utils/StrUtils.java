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
package com.a2a.adjava.utils;

/**
 * String utils
 * @author lmichenaud
 *
 */
public class StrUtils {

	/**
	 * Dot char
	 */
	public static final char DOT = '.' ;
	
	/**
	 * Dot string
	 */
	public static final String DOT_S = "." ;
	
	/**
	 * Path separator
	 */
	public static final char PATH_SEPARATOR_C = '/';
	
	/**
	 * Path separator
	 */
	public static final String PATH_SEPARATOR_S = "/";
	
	/**
	 * Utility class
	 */
	private StrUtils() {
		// private constructor
	}
	
	/**
	 * Return substring after last dot.
	 * If not found, return string as it.
	 * @param p_sString input string
	 * @return substring after last dot
	 */
	public static String substringAfterLastDot( String p_sString ) {
		return substringAfterLastChar(p_sString, DOT);
	}
	
	/**
	 * Return substring after last sep.
	 * If not found, return string as it.
	 * @param p_sString input string
	 * @param p_iSep separator
	 * @return substring after last sep
	 */
	public static String substringAfterLastChar( String p_sString, char p_iSep ) {
		
		String r_sResult = null ;
		int iPos = p_sString.lastIndexOf(p_iSep);
		if ( iPos != -1 ) {
			r_sResult = p_sString.substring(iPos+1);
		}
		else {
			r_sResult = p_sString;
		}
		return  r_sResult ;
	}
}
