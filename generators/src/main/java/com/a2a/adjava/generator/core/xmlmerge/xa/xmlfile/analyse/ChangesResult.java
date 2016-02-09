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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.model.XAFile;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.model.XAPath;

/**
 * Contient l'ensemble des changements pour deux fichiers données
 * @author smaitre
 *
 */
public class ChangesResult {

	private static Logger LOG = LoggerFactory.getLogger(ChangesResult.class);


	//	/** contient l'ensemble des modifications classé par nom du noeud, puis par identifiant du noeud */
	//1ere clé : le nom de la balise
	//2eme clé : la valeur de la clé
	//	private Map<String, Map<String, List<Change>>> changesByNodeName = null; //ne contient que les modifications
	/** contient l'ensemble des changements classés par signature */
	private Map<String, Change> changesListByNodeSignature = null; //contient tous les changements
	/**contient l'ensemble des changements par le xpath du noeud d'origine */
	private Map<String, List<Change>> changesListByXPath = null;
	/** contient l'ensemble des ajouts de noeuds */
	private List<Change> additionChangesList = null; // contient les add nodes
	/** contient l'ensemble des ajouts de noeuds */
	private List<Change> removeChangesList = null; // contient les add nodes
	/** contient l'ensemble des ajouts de noeuds */
	private List<Change> withoutRemoveChangesList = null; // contient les add nodes
	/** contient l'ensemble des changements classés avec le comparateur de changement */
	private List<Change> allChangesList = null;

	private XAFile oldFile;

	private XAFile newfile;

	/**
	 * Création du conteneur
	 */
	public ChangesResult(XAFile p_oOldGeneratedFile, XAFile p_oNewGeneratedfile) {
		//		this.changesByNodeName = new HashMap<String, Map<String, List<Change>>>();
		this.changesListByNodeSignature = new HashMap<String, Change>();
		this.changesListByXPath = new HashMap<String, List<Change>>(); 
		this.additionChangesList = new ArrayList<Change>();
		this.removeChangesList = new ArrayList<Change>();
		this.withoutRemoveChangesList = new ArrayList<Change>();
		this.allChangesList = new ArrayList<Change>();
		this.oldFile = p_oOldGeneratedFile;
		this.newfile = p_oNewGeneratedfile;
	}

	/**
	 * Ajoute un changement au conteneur
	 * @param p_oChange le changement à ajouter
	 * @throws AdjavaException 
	 */
	public void addChange(Change p_oChange) throws AdjavaException {

		this.allChangesList.add(p_oChange);

		if (ChangeType.ADD_NODE.equals(p_oChange.getType())) {
			this.additionChangesList.add(p_oChange);
			String signature = p_oChange.getSig();
			if(this.changesListByNodeSignature.get(signature) !=null){
				throw new AdjavaException("this XPath is common for several nodes whereas it should be unique: "+signature);
			}
			else {
				this.changesListByNodeSignature.put(signature, p_oChange);
			}
		}
		else if (p_oChange.getOldNode()!=null && p_oChange.getOldNode().isIdentifiableWithoutPosition() 
				|| p_oChange.getNewNode()!=null && p_oChange.getNewNode().isIdentifiableWithoutPosition()) {
			this.changesListByNodeSignature.put(p_oChange.getSig(), p_oChange);
		}

		if(p_oChange.getOldNode() != null) {
			List<Change> lstChanges = this.changesListByXPath.get(p_oChange.getOldNode().getPath().getFullPath());

			if (lstChanges == null) {
				lstChanges = new ArrayList<Change>();
				this.changesListByXPath.put(p_oChange.getOldNode().getUniqueXPathId(), lstChanges);
			}
			lstChanges.add(p_oChange);
		}

		if(LOG.isDebugEnabled()) {
			String log = ">>>> found change '"+p_oChange.getType()+"' (";
			if (p_oChange.getOldNode()!=null)
				log+="  old: "+p_oChange.getOldNode().getUniqueXPathId()+"  ";
			if (p_oChange.getNewNode()!=null)
				log+="  new: "+p_oChange.getNewNode().getUniqueXPathId()+"  ";

			LOG.debug(log+")");
		}
	}
	
	/**
	 * Supprime un changement de la liste de tous les changements
	 * @param p_oChange Le changement à supprimer.
	 */
	public void removeChange(Change p_oChange) {
		this.allChangesList.remove(p_oChange);
	}

	/**
	 * Donne l'ensemble des changements
	 * @return l'ensemble des changements
	 */
	public List<Change> getChanges() {
		Collections.sort(this.allChangesList, new ChangeComparator(1));
		return this.allChangesList;
	}

	/**
	 * Donne l'ensemble des changements
	 * @return l'ensemble des changements
	 */
	public List<Change> getRemoveChanges() {
		Collections.sort(this.removeChangesList, new ChangeComparator(-1));
		return this.allChangesList;
	}

	/**
	 * Donne l'ensemble des changements
	 * @return l'ensemble des changements
	 */
	public List<Change> getWithoutRemoveChanges() {
		Collections.sort(this.withoutRemoveChangesList, new ChangeComparator(1));
		return this.allChangesList;
	}



	/**
	 * Donne l'ensemble des changements qui ajoutent des noeuds
	 * @return l'ensemble des changements qui ajoutent des noeuds
	 */
	public List<Change> getAddChanges() {
		return this.additionChangesList;
	}

	/**
	 * Donne un changement en fonction de sa signature
	 * @param p_sSig la signature cherchée
	 * @return un changement de même signature
	 */
	public Change getChanges(String p_sSig) {
		return this.changesListByNodeSignature.get(p_sSig);
	}

	public List<Change> getChangesByXPath(String p_oUniqueXPathId) {
		return this.changesListByXPath.get(p_oUniqueXPathId);
	}

	/**
	 * Permet de convertir l'objet en chaîne pour les logs
	 * @return Une chaîne destinée à loguer l'objet
	 */
	public String toString() {
		StringBuilder content = new StringBuilder();
		content.append("CHANGES :\n");
		content.append(allChangesList.toString());
		content.append('\n');
		if(allChangesList.size() > 0) {
			content.append('\n');
			content.append("OLDFILE :");
			content.append('\n');
			content.append(this.oldFile != null ? this.oldFile.toString() : "null");
			content.append('\n');
			content.append('\n');
			content.append("NEWFILE :");
			content.append('\n');
			content.append(this.newfile != null ? this.newfile.toString() : "null");
			content.append('\n');
			for(Change change : allChangesList) {
				content.append("CHANGE SIGNATURE :");
				content.append('\n');
				try {
					content.append(change.getSig() != null ? change.getSig() : "null");
				} catch (AdjavaException e) {
					content.append("null exception");
				}
				content.append('\n');
			}		
		}

		content.append('\n');

		return content.toString();
	}
}
