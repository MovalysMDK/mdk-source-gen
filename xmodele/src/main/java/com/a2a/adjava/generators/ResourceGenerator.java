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
package com.a2a.adjava.generators;

import java.util.Map;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.XProject;

/**
 * 
 * <p>Generateur de resources</p>
 *
 * <p>Copyright (c) 2009
 * <p>Company: Adeuza
 *
 * @author mmadigand
 * @author lmichenaud
 *
 */
public interface ResourceGenerator<D extends IDomain<? extends IModelDictionary, ? extends IModelFactory>> {
	
	/**
	 * Initialisation du generateur
	 */
	public void initialize() throws AdjavaException;
	
	/**
	 * 
	 * Realise la generation
	 * @param p_oProjectConfig configuration du projet
	 * @param p_oMModele Modele
	 * @param p_oSchema Schema
	 * @param p_oGlobalSession session globale aux generateurs
	 * @throws Exception échec de la generation
	 */
	public void genere( XProject<D> p_oMProject, DomainGeneratorContext p_oGeneratorContext ) throws Exception;
	
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
		
	/**
	 * Enable/disable debug
	 * @param p_bDebug
	 */
	public void setDebug( boolean p_bDebug);
}
