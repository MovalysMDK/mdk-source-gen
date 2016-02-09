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
package com.a2a.adjava.xmi;

import org.dom4j.Document;

import com.a2a.adjava.uml.UmlModel;

/**
 * XMI Reader
 * @author lmichenaud
 *
 */
public interface XMIReader {

	/**
	 * Return true if xmi reader can read the document
	 * @param p_xDocument p_xDocument
	 * @return true if xmi reader can read the document
	 */
	public boolean canRead( Document p_xDocument );
	
	/**
	 * Read Xmi document
	 * @param p_xDocument xmi document
	 * @return Uml model
	 * @throws Exception exception
	 */
	public UmlModel read( Document p_xDocument) throws Exception ;
}
