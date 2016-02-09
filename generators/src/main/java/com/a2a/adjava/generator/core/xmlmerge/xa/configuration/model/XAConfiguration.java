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
package com.a2a.adjava.generator.core.xmlmerge.xa.configuration.model;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;

/**
 * Singleton comprenant l'ensemble de la configuration des noeuds xml
 * @author smaitre
 */
public class XAConfiguration {
	
	private static final Logger log = LoggerFactory.getLogger(XAConfiguration.class);

	/** unique instance de l'ensemble de la configuration */
	private static XAConfiguration _instance = null;
	private static final String MAP_KEY_SEPARATOR="#";
	
	/** gestion du singleton */
	public static XAConfiguration getInstance() {
		if (_instance == null) {
			_instance = new XAConfiguration();
		}
		return _instance;
	}

	/** contient l'ensemble de la configuration indexée par le nom du noeud */
	private Map<String, XAConfigurationNode> nodes = null;

	
	private Map<String, String> keysForSibling = null;
	
	
	/**
	 * Construit le nouveau repository des noeuds
	 */
	public XAConfiguration() {
		this.nodes = new HashMap<String, XAConfigurationNode>();
		this.keysForSibling = new HashMap<String,String>();
	}
	
	/**
	 * Permet d'ajouter la configuration d'un noeud à la configuration globale
	 * @param p_oNode le noeud à ajouter
	 * @throws AdjavaException 
	 */
	public void addNode(String xaConfName,XAConfigurationNode p_oNode) throws AdjavaException {
		if(p_oNode.getIdentificationType().isKeyForSibling()){
			this.addKeyForSibling(xaConfName, p_oNode.getName());
		}

		String key = xaConfName+XAConfiguration.MAP_KEY_SEPARATOR+p_oNode.getName();
		if(this.nodes.get(key) !=null)
			throw new AdjavaException("The node '"+p_oNode.getName()+"' is defined several times in the configuration file '"+xaConfName+"'");
		else
			this.nodes.put(key, p_oNode);
	}
	
	/**
	 * Donne la configuration d'un noeud en fonction de son nom.
	 * @param p_sKey le nom du noeud
	 * @return la configuration du noeud
	 */
	public XAConfigurationNode getNode(String xaConfName,String p_sKey) {
		return this.nodes.get(xaConfName+XAConfiguration.MAP_KEY_SEPARATOR+p_sKey);
	}
	
	public int getNodesNumber() {
		if(this.nodes == null)
			return 0;
		else
			return this.nodes.size();
	}

	/**
	 * Get the name of the 'keyForSiblings' node defined in this configuration file
	 * @param xaConfName
	 * @return
	 */
	public String getKeyForSiblingsNodeName(String xaConfName) {
		return this.keysForSibling.get(xaConfName);
	}

	
	public void addKeyForSibling(String xaConfName, String keyForSibling) throws AdjavaException{

		log.debug("[XAConfiguration#addKeyForSibling] add to keyForSibling: ("+xaConfName+","+keyForSibling+")");
		
		if(this.keysForSibling.get(xaConfName) != null){
			throw new AdjavaException("Only one 'keyForSibling' is allowed by XA conf file.");
		}
		else 
			this.keysForSibling.put(xaConfName, keyForSibling);
	}
	

	public boolean requiresSiblingKeyGrouping(String xaConfName){
		log.debug("[XAConfiguration#requiresSiblingKeyGrouping] in file "+xaConfName+", found node "+this.keysForSibling.get(xaConfName)+")");
		return xaConfName!=null && this.keysForSibling.get(xaConfName) != null ;
	}

	
	/**
	 * Permet de reseter la configuration globale
	 */
	public void clear() {
		this.nodes.clear();
		this.keysForSibling.clear();
	}

}
