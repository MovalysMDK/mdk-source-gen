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
package com.a2a.adjava.mupdater;

import java.util.Map;

import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;

/**
 * <p>Interface permettant d'exeuter des Updaters apres la generation des objets "xmodele"</p>
 *
 * <p>Copyright (c) 2009</p>
 * <p>Company: Adeuza</p>
 *
 * @since 2.5
 * @author mmadigand
 *
 */
public interface MUpdater {

	/**
	 * Methode permettant de lancer l'execution de l'updater.
	 * @param p_oProjectConfig configuration du projet
	 * @param p_oMModele modele M
	 * @param p_oGlobalSession session globale
	 * 
	 * @throws Exception exception remontee lors de l'execution de l'updater
	 */
	public void execute(IDomain<IModelDictionary, IModelFactory> p_oDomain, Map<String,?> p_oGlobalSession ) throws Exception;
	
	/**
	 * Retourne l'objet parametersMap
	 * 
	 * @return Objet parametersMap
	 */
	public Map<String, String> getParametersMap();

	/**
	 * Affecte l'objet parametersMap 
	 * 
	 * @param p_oParametersMap Objet parametersMap
	 */
	public void setParametersMap(Map<String, String> p_oParametersMap);	
	
}
