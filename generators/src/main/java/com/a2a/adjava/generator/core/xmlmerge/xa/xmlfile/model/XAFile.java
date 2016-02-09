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
package com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.analyse.Change;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.analyse.ChangeType;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.analyse.ChangesResult;

/**
 * Définit un fichier
 * @author smaitre
 *
 */
public class XAFile {
	
	private static Logger LOG = LoggerFactory.getLogger(XAFile.class);


	/** le nom du fichier */
	private Path filePath = null;
	/** l'ensemble des noeuds composant le fichier */
	private List<XANode> nodes = null;
	/** l'ensemble des noeuds composant le fichier par type de noeud */
	private Map<String, Map<String, XANode>> nodesByType = null;
	/** indique si le fichier xml possède un header */
	private boolean xmlHeader = false;
	
	private String xaConfName=null;
	
	
	/**
	 * Créer un nouveau fichier
	 */
	public XAFile() {
		this.nodes = new ArrayList<XANode>();
		this.nodesByType = new HashMap<String, Map<String, XANode>>();
	}

	
	
	/**
	 * Affecte le fait qu'un fichier possède un header
	 * @param p_bHeader true alors le fichier à un header
	 */
	public void setXmlHeader(boolean p_bHeader) {
		this.xmlHeader = p_bHeader;
	}
	
	/**
	 * Indique que le fichier à un header
	 * @return true si le fichier xml a un header
	 */
	public boolean hasXmlHeader() {
		return this.xmlHeader;
	}
	
	/**
	 * Ajoute un noeud au fichier
	 * @param p_oNode le noeud à ajouter
	 * @throws AdjavaException 
	 */
	public void addNode(XANode p_oNode) throws AdjavaException {
		this.nodes.add(p_oNode);
		Map<String, XANode> group = nodesByType.get(p_oNode.getName());
		if (group == null) {
			group = new HashMap<String, XANode>();
			nodesByType.put(p_oNode.getName(), group);
		}
		String uniqueKey = p_oNode.getUniqueXPathId();
		if(group.get(uniqueKey) != null){
			XANode existingNode = group.get(uniqueKey);
			AdjavaException exception = null;
				exception = new AdjavaException("In the file '"+this.getFilePathToString()+"', several nodes are found with the XPath "
					+ uniqueKey + "\n\tPlease fix the XA configuration file");

			throw exception;
		}
		else
			group.put(uniqueKey, p_oNode);
	}
	
	/**
	 * Donne le nombre de noeud xml composant le fichier
	 * @return le nombre de noeud xml
	 */
	public int size() {
		return this.nodes.size();
	}


	
	/**
	 * Retrouve un noeud en fonction d'un autre noeud (la clé du noeud c'est son nom)
	 * @param p_oNode le noeud utilisé pour retrouvé un noeud dans le fichier
	 * @return un noeud dont le nom est identique au noeud passé en paramètre
	 */
	public XANode getNode(XANode p_oNode) {
		Map<String, XANode> inter = this.nodesByType.get(p_oNode.getName());
		if (inter!=null) {
			return inter.get(p_oNode.getUniqueXPathId());
		}
		else {
			return null;
		}
	}
	
	/**
	 * Cherche les changements entre le fichier courant et le fichier passé en paramètre
	 * @param oldXAFile le fichier passé en paramètre
	 * @return l'ensemble des résultats de changement
	 * @throws AdjavaException 
	 */
	public ChangesResult findChanges(XAFile oldXAFile) throws AdjavaException {
		ChangesResult changes = new ChangesResult(oldXAFile, this);
		//Recherche des éléments ajoutés
		for(XANode newNode : this.nodes) {
			XANode oldNode = oldXAFile.getNode(newNode);
			if (oldNode==null) {
				//on ne trouve pas le noeud dans le xml d'origine il a été ajouté
				changes.addChange(new Change(ChangeType.ADD_NODE,null,newNode));
			}
			else {
				//on regarde si le chemin a été changé
				if (!newNode.isLike(oldNode)) {
					changes.addChange(new Change(ChangeType.MOVE_NODE, oldNode, newNode));
				}
				//on regarde si la valeur du noeud à été changé
				if (!newNode.isSameValue(oldNode)) {
					changes.addChange(new Change(ChangeType.MODIFY_NODE_VALUE, oldNode, newNode));
				}
				//analyse des attributs
				for(XAAttribute attrNewNode : newNode.getAttributes().values()) {
					XAAttribute attrOldNode = oldNode.getAttribute(attrNewNode.getName());
					if (attrOldNode==null) {
						//l'attribut a été ajouté
						changes.addChange(new Change(ChangeType.ADD_ATTRIBUTE,oldNode,newNode, null, attrNewNode));
					}
					//on regarde si la valeur de l'attribut à été changé
					else if (!attrNewNode.isSameValue(attrOldNode)) {
						changes.addChange(new Change(ChangeType.MODIFY_ATTRIBUTE_VALUE, oldNode, newNode, attrOldNode, attrNewNode));
					}
				}
				for(XAAttribute attrOldNode : oldNode.getAttributes().values()) {
					XAAttribute attrNewNode = newNode.getAttribute(attrOldNode.getName());
					if (attrNewNode==null) {
						//l'attribut a été supprimé
						changes.addChange(new Change(ChangeType.REMOVE_ATTRIBUTE,oldNode,newNode, attrOldNode, null));
					}
				}
			}
		}
		//Recherche des éléments supprimés
		for(XANode oldNode : oldXAFile.nodes) {
			XANode newNode = this.getNode(oldNode);
			if (newNode==null) {
				//on ne trouve pas le noeud dans le xml d'origine il a été supprimé
				changes.addChange(new Change(ChangeType.REMOVE_NODE, oldNode,null));
			}
		}
		return changes;
	}









	public Path getFilePath() {
		return filePath;
	}
	public String getFilePathToString() {
		return filePath.toString();
	}


	public void setFilePath(Path filePath) {
		this.filePath = filePath;
	}
	
	public String getBaseFileName(){
		return FilenameUtils.getBaseName(getFileName());
	}
	
	public String getFileName(){
		return this.filePath.getFileName().toString();
	}



	public String getXaConfName() {
		return xaConfName;
	}



	public void setXaConfName(String xaConfName) {
		this.xaConfName = xaConfName;
	}

	public String toString() {
		StringBuilder content = new StringBuilder();
		content.append("XAFILE :\n");
		content.append(this.getFileName());
		for(Entry<String, Map<String, XANode>> map1 : this.nodesByType.entrySet()) {
			for(Entry<String, XANode> map2 : map1.getValue().entrySet()) {
				content.append(map1.getKey());
				content.append(map2.getKey());
				content.append('\n');
			}
		}
		return content.toString();
	}
	
	

}
