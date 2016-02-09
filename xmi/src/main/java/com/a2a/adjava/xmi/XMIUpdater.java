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

import java.util.Map;

import org.dom4j.Document;

/**
 * 
 * <p>Interface des XMIUpdater</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author mmadigand
 *
 */
public interface XMIUpdater {

	/**
	 * 
	 * TODO Décrire la méthode execute de la classe XMIUpdater
	 * @param p_xXmi
	 * @param p_oProjectConfig
	 * @throws Exception
	 */
	public void execute( Document p_xXmi ) throws Exception;

	/**
	 * @param p_mapParameters
	 */
	public void setParametersMap(Map<String, String> p_mapParameters);
}
