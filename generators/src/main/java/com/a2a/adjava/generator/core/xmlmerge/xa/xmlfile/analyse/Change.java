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

import java.nio.file.Path;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.model.XAAttribute;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.model.XANode;

/**
 * Permet de caractérisé un changement entre deux fichiers xml A et B
 * @author smaitre
 */
public class Change {

	/** le type du changement */
	private ChangeType type = null;
	/** le noeud de la version A (peut être null)*/
	private XANode newNode = null;
	/** le noeud de la version B (peut être null)*/
	private XANode oldNode = null;
	/** l'attribut de la version A concerné par le changement (peut être null)*/
	private XAAttribute newAttribute = null;
	/** l'attribut de la version B concerné par le changement (peut être null)*/
	private XAAttribute oldAttribute = null;
	
	
	/**
	 * Construit un nouveau changement. A utiliser lorsque le changement est au niveau noeud.
	 * @param p_oType le type du changement
	 * @param p_oOldNode le noeud de la version A
	 * @param p_oNewNode le noeud de la version B
	 */
	public Change(ChangeType p_oType, XANode p_oOldNode, XANode p_oNewNode) {
		this.type = p_oType;
		this.newNode = p_oNewNode;
		this.oldNode = p_oOldNode;
	}
	
	/**
	 * Construit un nouveau changement. A utiliser lorsque le changement est au niveau attribut
	 * @param p_oType le type du changement
	 * @param p_oOldNode le noeud de la version A
	 * @param p_oNewNode le noeud de la version B
	 * @param p_oOldAttribute l'attribut de la version A
	 * @param p_oNewAttribute l'attribut de la version A
	 */
	public Change(ChangeType p_oType, XANode p_oOldNode, XANode p_oNewNode,
			XAAttribute p_oOldAttribute, XAAttribute p_oNewAttribute) {
		this(p_oType, p_oNewNode, p_oOldNode);
		this.setNewAttribute(p_oNewAttribute);
		this.setOldAttribute(p_oOldAttribute);
	}

	/**
	 * Donne le noeud de la version A
	 * @return le noeud de la version A
	 */
	public XANode getOldNode() {
		return oldNode;
	}
	
	
	/**
	 * Affecte le noeud de la version A
	 * @param oldNode le noeud de la version A
	 */
	public void setOldNode(XANode oldNode) {
		this.oldNode = oldNode;
	}
	
	/**
	 * Donne le noeud de la version B
	 * @return le noeud de la version B
	 */
	public XANode getNewNode() {
		return newNode;
	}
	
	/**
	 * Affecte le noeud de la version B
	 * @param newNode le noeud de la version B
	 */
	public void setNewNode(XANode newNode) {
		this.newNode = newNode;
	}
	
	/**
	 * Donne le type de changement
	 * @return le type de changement
	 */
	public ChangeType getType() {
		return type;
	}
	
	/**
	 * Affecte le type de changement
	 * @param type le type de changement
	 */
	public void setType(ChangeType type) {
		this.type = type;
	}

	/**
	 * Donne l'attribut de la version B
	 * @return l'attribut de la version B
	 */
	public XAAttribute getNewAttribute() {
		return newAttribute;
	}

	/**
	 * Affecte l'attribut de la version B
	 * @param newAttribute l'attribut de la version B
	 */
	public void setNewAttribute(XAAttribute newAttribute) {
		this.newAttribute = newAttribute;
	}

	/**
	 * Donne l'attribut de la version A
	 * @return l'attribut de la version A
	 */
	public XAAttribute getOldAttribute() {
		return oldAttribute;
	}

	/**
	 * Affecte l'attribut de la version A
	 * @param oldAttribute l'attribut de la version A
	 */
	public void setOldAttribute(XAAttribute oldAttribute) {
		this.oldAttribute = oldAttribute;
	}
	
	/**
	 * Permet de convertir l'objet en chaîne pour les logs
	 */
	public String toString() {
		StringBuilder objectToString = new StringBuilder();
		objectToString.append("--------------------------------------------------\n\n");
		objectToString.append("CHANGE TYPE = " + this.type.toString()+ "\n");
		objectToString.append("OLD NODE = " + ((this.oldNode != null) ? this.oldNode.toString() : "null")+ "\n");
		objectToString.append("NEW NODE = " + ((this.newNode != null) ? this.newNode.toString() : "null")+ "\n");
		objectToString.append("OLD ATTRIBUTE = " + ((this.oldAttribute != null) ? this.oldAttribute.toString() : "null")+ "\n");
		objectToString.append("NEW ATTRIBUTE = " + ((this.newAttribute != null) ? this.newAttribute.toString() : "null")+ "\n\n");
		objectToString.append("--------------------------------------------------\n");
		return objectToString.toString();
	}

	/**
	 * Donne la signature d'un changement (pour comparer des changements entre eux)
	 * Si deux changements ont une signature identique alors ces changements portent sur les même éléments (voir pour gérer les conflits)
	 * @return la signature d'un changement
	 * @throws AdjavaException 
	 */
	public String getSig() throws AdjavaException {
		return this.getType().getAssociatedClass().getSig(this);
	}
	
	/**
	 * Returns the old file path of this change
	 * @return the old file path of this change
	 */
	public Path getOldFilePath(){
		if(this.oldNode==null)
			return null;
		else
			return this.oldNode.getPath().getFilePath();
	}

	/**
	 * Returns the new file path of this change
	 * @return the new file path of this change
	 */
	public Path getNewFilePath(){
		if(this.newNode==null)
			return null;
		else
			return this.newNode.getPath().getFilePath();
	}
	
	/**
	 * Indicates if two changes are of the same type
	 * @param p_oAnotherChange Another change we will to compare the type of this
	 * @return true if the changes are of the same type, false otherwise
	 */
	public boolean isSameTypeOf(Change p_oAnotherChange) {
		boolean bResult = false;
		if(p_oAnotherChange != null) {
			bResult = this.getType().equals(p_oAnotherChange.getType());
		}
		return bResult;
	}
}
