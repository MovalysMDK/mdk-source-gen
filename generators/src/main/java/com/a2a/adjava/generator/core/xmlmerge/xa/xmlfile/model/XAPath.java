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

/**
 * Représentation du noeud d'un chemin
 * @author smaitre
 *
 */
public class XAPath {

	/** le parent */
	private XAPath parent = null;
	/** le nom de l'élément courant */
	private String nodePath = null;
	/** le nom court du chemin */
	private String shortName = null;
	/** la position absolue par rapport à son père */
	private int positionAbsolute = 1;
	/** la position relative par rapport à son père et au type de noeud */
	private int positionRelative = 1;
	/** la valeur de l'attribut clé */
	//private String id = null;
	private Path filePath= null;
	
	public void setFilePath(Path filePath) {
		this.filePath=filePath;
	}
	
	/**
	 * Donne le nom
	 * @return le nom
	 */
	public String getFullPath() {
		return filePath+nodePath;
	}
	
	public Path getFilePath() {
		return filePath;
	}
	
	
	/**
	 * Affecte le nom
	 * @param nodePath le nom
	 */
	public void setNodePath(String name) {
		this.nodePath = name;
	}

	
	/**
	 * Donne la position absolue par rapport au parent: à l'intérieur du noeud parent, c'est la Xème balise (tout nom confondu)
	 * @return la position absolue
	 */
	public int getPositionAbsolute() {
		return positionAbsolute;
	}
	
	/**
	 * Affecte la position absolue: à l'intérieur du noeud parent, c'est la Xème balise (tout nom confondu)
	 * @param position la position absolue
	 */
	public void setPositionAbsolute(int position) {
		this.positionAbsolute = position;
	}
	
	/**
	 * Donne la position relative: à l'intérieur du noeud parent, c'est la Xème balise portant le même nom
	 * @param position la position relative par rapport au parent et au type de noeud
	 */
	public int getPositionRelative() {
		return positionRelative;
	}
	
	/**
	 * Affecte la position relative
	 * @param position
	 */
	public void setPositionRelative(int position) {
		this.positionRelative = position;
	}
	
	/**
	 * Donne le chemin du parent
	 * @return le chemin du parent
	 */
	public XAPath getParent() {
		return parent;
	}
	
	/**
	 * Affecte le chemin du parent
	 * @param parent le chemin parent
	 */
	public void setParent(XAPath parent) {
		this.parent = parent;
	}
	
	/**
	 * Affecte le nom court
	 * @param p_sName le nom court
	 */
	public void setShortName(String p_sName) {
		this.shortName = p_sName;
	}
	
	/**
	 * Donne le nom court
	 * @return le nom court
	 */
	public String getShortName() {
		return this.shortName;
	}
	
	/**
	 * Représentation chaîne
	 */
	public String toString() {
		if (this.parent!=null) {
			return this.parent.toString() + "/" + nodePath + "[" + positionAbsolute + "' child][" + positionRelative + "' child of this type]";
		}
		else {
			return nodePath + "[" + positionAbsolute + "' child][" + positionRelative + "' child of this type]";
		}
	}
}
