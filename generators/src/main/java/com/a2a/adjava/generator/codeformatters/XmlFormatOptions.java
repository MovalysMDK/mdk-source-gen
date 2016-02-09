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
package com.a2a.adjava.generator.codeformatters;

import com.a2a.adjava.codeformatter.FormatOptions;

/**
 * Xml Format options
 * @author lmichenaud
 *
 */
public class XmlFormatOptions extends FormatOptions {

	/**
	 * Dom4j
	 */
	private boolean useDom4j = false ;
	
	/**
	 * Standalone
	 */
	private boolean standalone = false ;

	/**
	 * Omit xml declaration
	 */
	private boolean omitXmlDeclaration = false ;
	
	/**
	 * New line after declaration
	 */
	private boolean newLineAfterDeclaration = false ;
	
	/**
	 * Use dom4j or not
	 * @return true if use dom4j
	 */
	public boolean isUseDom4j() {
		return this.useDom4j;
	}

	/**
	 * Get true if standalone
	 * @return true if standalone
	 */
	public boolean isStandalone() {
		return this.standalone;
	}

	/**
	 * Is omit xml declaration
	 * @return true if omit xml declaration
	 */
	public boolean isOmitXmlDeclaration() {
		return this.omitXmlDeclaration;
	}

	/**
	 * Set omit xml declaration
	 * @param p_bOmitXmlDeclaration true if omit xml declaration
	 */
	public void setOmitXmlDeclaration(boolean p_bOmitXmlDeclaration) {
		this.omitXmlDeclaration = p_bOmitXmlDeclaration;
	}

	/**
	 * Set true to use dom4j
	 * @param p_bUseDom4j true if use dom4j
	 */
	public void setUseDom4j(boolean p_bUseDom4j) {
		this.useDom4j = p_bUseDom4j;
	}

	/**
	 * Set standalone attribute for xml header
	 * @param p_bStandalone standalone attribute for xml header
	 */
	public void setStandalone(boolean p_bStandalone) {
		this.standalone = p_bStandalone;
	}

	/**
	 * Is new line after declaration
	 * @return Is new line after declaration
	 */
	public boolean isNewLineAfterDeclaration() {
		return this.newLineAfterDeclaration;
	}

	/**
	 * Set if new line after declaration
	 * @param p_bNewLineAfterDeclaration true if new line after declaration
	 */
	public void setNewLineAfterDeclaration(boolean p_bNewLineAfterDeclaration) {
		this.newLineAfterDeclaration = p_bNewLineAfterDeclaration;
	}
}
