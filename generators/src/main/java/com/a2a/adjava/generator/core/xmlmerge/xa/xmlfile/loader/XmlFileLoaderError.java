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
package com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.loader;

import com.a2a.adjava.generator.core.xmlmerge.xa.AbstractXALoaderError;

/**
 * Classe d'erreur du chargeur de xml
 * @author smaitre
 *
 */
public class XmlFileLoaderError extends AbstractXALoaderError<XMLFILE_LOADER_ERROR_TYPE>{

	/**
	 * Constructeur
	 * @param p_oType le type de l'erreur
	 * @param p_sLocalisation la localisation de l'erreur
	 * @param p_iPosition la position de l'erreur
	 */
	public XmlFileLoaderError(XMLFILE_LOADER_ERROR_TYPE p_oType, String p_sLocalisation, int p_iPosition) {
		super(p_oType, p_sLocalisation, p_iPosition);
	}

}
