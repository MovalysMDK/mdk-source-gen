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
package com.a2a.adjava.projectupgrader;

import java.util.List;
import java.util.Map;

import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;

/**
 * <p>Interface permettant d'exeuter des Updaters apres la generation des objets "xmodele"</p>
 *
 * <p>Copyright (c) 2014</p>
 * <p>Company: Adeuza</p>
 *
 * @since 6.4
 * @author qlagarde
 *
 */
public interface ProjectUpgrader{
	
	/**
	 * Enumération décrivant le mode du PU
	 * @author qLagarde
	 *
	 */
	public enum ProjectUpgraderMode{
	       PROJECT_UPGRADER_MODE_BEFORE_GEN("pre-generation"), 
	       PROJECT_UPGRADER_MODE_AFTER_GEN("post-generation");
	       
	       private String code;
	       
	       private ProjectUpgraderMode(String p_sCode) {
	            this.code = p_sCode;
	       }
	       public String getCode(){
	        return code;
	       }
	   }
	
	
	/**
	 * Methode permettant de lancer l'execution de l'upgrader.
	 * @param p_oProjectConfig configuration du projet
	 * @param p_oMModele modele M
	 * @param p_oGlobalSession session globale
	 * 
	 * @throws Exception exception remontee lors de l'execution de l'upgrader
	 */
	public void execute(IDomain<IModelDictionary, IModelFactory> p_oDomain, Map<String,?> p_oGlobalSession ) throws Exception;
	
	/**
	 * Methode permettant de lancer l'execution de l'updater.
	 * @param p_oProjectConfig configuration du projet
	 * @param p_oMModele modele M
	 * @param p_oGlobalSession session globale
	 * 
	 * @throws Exception exception remontee lors de l'execution de l'updater
	 */
	public void executeFixesForAndroid(IDomain<IModelDictionary, IModelFactory> p_oDomain, Map<String,?> p_oGlobalSession ) throws Exception;
	
	/**
	 * Methode permettant de lancer l'execution de l'updater.
	 * @param p_oProjectConfig configuration du projet
	 * @param p_oMModele modele M
	 * @param p_oGlobalSession session globale
	 * 
	 * @throws Exception exception remontee lors de l'execution de l'updater
	 */
	public void executeFixesForIOS(IDomain<IModelDictionary, IModelFactory> p_oDomain, Map<String,?> p_oGlobalSession ) throws Exception;
	
	/**
	 * Retourne l'objet parametersMap
	 * 
	 * @return Objet parametersMap
	 */
	public Map<String, ?> getParametersMap();

	/**
	 * Affecte l'objet parametersMap 
	 * 
	 * @param p_oParametersMap Objet parametersMap
	 */
	public void setParametersMap(Map<String, ?> p_oParametersMap);	
	
	/**
	 * Retourne la liste des projectUpgraders similaires
	 * @return La liste des projectUpgraders similaires
	 */
	public List<String> getSimilarProjectUpgraders();

	/**
	 * Met à jour la liste des projectUpgraders similaires
	 * @param similarProjectUpgraders La liste des projectUpgraders similaires.
	 */
	public void setSimilarProjectUpgraders(List<String> similarProjectUpgraders);
	
	/**
	 * Retourne le mode du PU
	 * @return le mode du PU
	 */
	public ProjectUpgraderMode getMode();
	
	/**
	 * Met à jour le mode du PU
	 * @param p_oProjectUpgraderMode le mode du PU à mettre à jour
	 */
	public void setMode(ProjectUpgraderMode p_oProjectUpgraderMode);
	
	
	
}



