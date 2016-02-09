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

import com.a2a.adjava.uml.UmlDataType;
import com.a2a.adjava.uml.UmlModel;

/**
 * <p>Classe abstraite pour la gestion des UmlUpdater.</p>
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @since Annapurna
 */
public abstract class AbstractUmlUpdater implements UmlUpdater {

	/** propriétés du modèle */
	private Map<String,String> parametersMap = null;

	/**
	 * Retourne l'objet parametersMap
	 * @return Objet parametersMap
	 */
	public Map<String, String> getParametersMap() {
		return this.parametersMap;
	}

	/**
	 * Affecte l'objet parametersMap 
	 * @param p_oParametersMap Objet parametersMap
	 */
	public void setParametersMap(Map<String, String> p_oParametersMap) {
		this.parametersMap = p_oParametersMap;
	}
	
	/**
	 * <p>
	 * 	Cette méthode sert à récupérer le type UML d'un attribut du model 
	 * 	en fonction du nom envoyé en paramètre.
	 * 	Les types acceptés sont ceux présents dans l'objet UmlDataType lui même.
	 * 	<ul>
	 * 		<li>String</li>
	 * 		<li>long</li>
	 * 		<li>int</li>
	 * 		<li>Date</li>
	 * 		<li>etc.</li>
	 * 	</ul>
	 * 	Si le modèle courant ne contient pas le type dont on veux récupérer une 
	 * 	instance, il sera alors créé de 0 et ajouté au dictionnaire de donné avant
	 * 	d'être retourné par la méthode.
	 * </p>
	 * 
	 * @param p_oUmlModele 
	 * 		le modèle courant
	 * @param p_sTypeName 
	 * 		le nom du type dont on veux récupérer le <em>UmlDataType</em>
	 * 
	 * @return objet de type <em>UmlDataType</em>
	 */
	protected UmlDataType findUmlDataType(UmlModel p_oUmlModele, String p_sTypeName){
		String sTypeIdentifier = p_sTypeName + UmlDataType.ALLIAS_IDENTIFIER;
		UmlDataType r_oDataType = p_oUmlModele.getDictionnary().getDataType(sTypeIdentifier);
		if (r_oDataType == null){
			r_oDataType = new UmlDataType(p_sTypeName);
			p_oUmlModele.getDictionnary().addDataType(sTypeIdentifier, r_oDataType);
		}
		return r_oDataType;
	}
	
}
