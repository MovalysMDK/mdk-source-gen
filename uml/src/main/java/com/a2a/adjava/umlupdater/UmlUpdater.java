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
package com.a2a.adjava.umlupdater;

import java.util.Map;

import com.a2a.adjava.uml.UmlModel;

/**
 * <p>TODO Decrire la classe UmlUpdater</p>
 *
 * <p>Copyright (c) 2009
 * <p>Company: Adeuza
 *
 * @author mmadigand
 *
 */
public interface UmlUpdater {

	/**
	 * 
	 * TODO Decrire la methode genere de la classe ResourceGenerator
	 * @param p_oProjectConfig
	 * @param p_oMModele
	 * @param p_oSchema
	 * @param p_oGlobalSession
	 * @throws Exception
	 */
	public void execute(UmlModel p_oUmlModele, Map<String,?> p_oGlobalSession ) throws Exception;
	
	/**
	 * Retourne l'objet parametersMap
	 * @return Objet parametersMap
	 */
	public Map<String, String> getParametersMap();

	/**
	 * Affecte l'objet parametersMap 
	 * @param p_oParametersMap Objet parametersMap
	 */
	public void setParametersMap(Map<String, String> p_oParametersMap);	
	
}
