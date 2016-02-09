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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.analyse.changetype.AddAttributeChangeType;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.analyse.changetype.AddNodeChangeType;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.analyse.changetype.ModifyAttributeChangeType;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.analyse.changetype.ModifyNodeChangeType;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.analyse.changetype.MoveNodeChangeType;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.analyse.changetype.RemoveAttributeChangeType;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.analyse.changetype.RemoveNodeChangeType;

/**
 * Donne l'ensemble des types de changement entre deux fichiers xml gérés par l'outil
 * @author smaitre
 *
 */
public enum ChangeType {
	/** ajout d'un noeud */
	ADD_NODE(AddNodeChangeType.class.getName()),
	/** suppression d'un noeud */
	REMOVE_NODE(RemoveNodeChangeType.class.getName()),
	/** déplacement d'un noeud */	
	MOVE_NODE(MoveNodeChangeType.class.getName()),
	/** modification du text d'un noeud */
	MODIFY_NODE_VALUE(ModifyNodeChangeType.class.getName()),
	/** ajout d'un attribut */
	ADD_ATTRIBUTE(AddAttributeChangeType.class.getName()),
	/** suppression d'un attribut */
	REMOVE_ATTRIBUTE(RemoveAttributeChangeType.class.getName()),
	/** modification d'un attribut */
	MODIFY_ATTRIBUTE_VALUE(ModifyAttributeChangeType.class.getName());
	
	private static Map<String, ItfChangeType> changes = new HashMap<String, ItfChangeType>();
	
	/** le logger à utiliser */
	private static Logger LOG = LoggerFactory.getLogger(ChangeType.class);
	
	/** le nom de la classe associée */
	private String className = null;
	
	/**
	 * Constructeur 
	 * @param p_sClassName le nom de la classe associée
	 */
	ChangeType(String p_sClassName) {
		this.className = p_sClassName;
	}
	
	/**
	 * Donne la classe associée à l'enumération, attention il s'agit d'un singleton
	 * @return la classe associée
	 * @throws AdjavaException 
	 */
	public ItfChangeType getAssociatedClass() throws AdjavaException {
		ItfChangeType r_oClass = changes.get(this.className);
		if (r_oClass == null) {
			try {
				r_oClass = (ItfChangeType) Class.forName(this.className).newInstance();
				changes.put(this.className, r_oClass);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				throw new AdjavaException("Problème sur la classe " + this.className, e);
				
			}
		}
		return r_oClass;
	}
}
