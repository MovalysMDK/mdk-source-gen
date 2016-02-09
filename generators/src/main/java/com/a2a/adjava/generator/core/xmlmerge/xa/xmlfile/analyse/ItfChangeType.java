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
package com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.analyse;

import org.dom4j.Document;

import com.a2a.adjava.AdjavaException;

/**
 * Traitement pour un changement
 * @author smaitre
 *
 */
public interface ItfChangeType {

	/**
	 * Donne la signature d'un changement 
	 * @param p_oChange le changement en cours de traitement
	 * @return la signature du changement
	 */
	public String getSig(Change p_oChange);
	
	/**
	 * Réalise le changement p_oChange sur le noeud p_oElement
	 * @param p_oChange le changement à effectuer
	 * @param p_oDoc l'élément xml à modifier
	 * @param p_oOriginalDoc Le document original
	 */
	public void processChange(Change p_oChange,Document p_oDoc, Document p_oOriginalDoc) throws AdjavaException;
	
	/**
	 * Simule le changement ^_oChange sur le noeud p_oElement
	 * @param p_oChange le changement à effectuer
	 * @param p_oDoc l'élément xml à modifier
	 * @param p_oOriginalDoc Le document original
	 */
	public void simulateProcessChange(Change p_oChange,Document p_oDoc, Document p_oOriginalDoc) throws AdjavaException;

}
