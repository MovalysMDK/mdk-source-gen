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
import java.util.HashMap;
import java.util.Map;

/**
 * Contient l'ensemble des fichiers analysés (ancienne génération, nouvelle génération et ancienne génération modifié par l'utilisateur)
 * @author smaitre
 *
 */
public class XAFiles {

	/** l'instance du singleton */
	private static XAFiles _instance = null;
	
	/** donne le singleton */
	public static XAFiles getInstance() {
		if (_instance == null) {
			_instance = new XAFiles();
		}
		return _instance;
	}

	/** l'ensemble des fichiers "nouvelle génération" */
	private Map<String, XAFile> newGenFiles = null;
	/** l'ensemble des fichiers "ancienne génération" */
	private Map<String, XAFile> oldGenFiles = null;
	/** l'ensemble des fichiers "ancienne génération modifié par l'utilisateur */
	private Map<String, XAFile> modFiles = null;
	
	/**
	 * Construit un nouveau repository
	 */
	public XAFiles() {
		this.newGenFiles = new HashMap<String, XAFile>();
		this.oldGenFiles = new HashMap<String, XAFile>();
		this.modFiles = new HashMap<String, XAFile>();
	}
	
	/**
	 * Ajoute un fichier aux fichiers de type "nouvelle génération"
	 * @param p_oFile le fichier à ajouter
	 */
	public void addNewGenFile(XAFile p_oFile) {
		this.newGenFiles.put(p_oFile.getFilePath().toString(), p_oFile);
	}
	
	/**
	 * Donne le fichier de type "nouvelle génération" de nom p_sKey
	 * @param p_sKey le nom du fichier
	 * @return le fichier demandé
	 */
	public XAFile getNewGenFile(Path p_sKey) {
		return this.newGenFiles.get(p_sKey.toString());
	}
	
	/**
	 * Ajoute un fichier aux fichiers de type "ancienne génération"
	 * @param p_oFile le fichier à ajouter
	 */
	public void addOldGenFile(XAFile p_oFile) {
		this.oldGenFiles.put(p_oFile.getFilePath().toString(), p_oFile);
	}
	
	/**
	 * Donne le fichier de type "ancienne génération" de nom p_sKey
	 * @param p_sKey le nom du fichier
	 * @return le fichier demandé
	 */
	public XAFile getOldGenFile(Path p_sKey) {
		return this.oldGenFiles.get(p_sKey.toString());
	}
	
	/**
	 * Ajoute un fichier aux fichiers de type "nouvelle génération modifié par l'utilisateur"
	 * @param p_oFile le fichier à ajouter
	 */
	public void addModFile(XAFile p_oFile) {
		this.modFiles.put(p_oFile.getFilePath().toString(), p_oFile);
	}
	
	/**
	 * Donne le fichier de type "ancienne génération modifié" de nom p_sKey
	 * @param p_sKey le nom du fichier
	 * @return le fichier demandé
	 */
	public XAFile getModFile(Path p_sKey) {
		return this.modFiles.get(p_sKey.toString());
	}
	
	/**
	 * Vide le repository
	 */
	public void clear() {
		newGenFiles.clear();
		oldGenFiles.clear();
		modFiles.clear();
	}
}
