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
package com.a2a.adjava.xmodele;

import com.a2a.adjava.schema.Field;

/**
 * <p>Association impliquant la génération d'une foreign key</p>
 *
 * <p>Copyright (c) 2009
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */
public interface MAssociationWithForeignKey extends MIdentifierElem {

	/**
	 * @see com.a2a.adjava.xmodele.MIdentifierElem#getName()
	 */
	public String getName();
	
	/**
	 * Retourne la classe référencée
	 * @return la classe référencée
	 */
	public MEntityImpl getRefClass();
	
	/**
	 * Ajoute un champs
	 * @param p_oField champs à ajouter
	 */
	public void addField( Field p_oField );
	
	/**
	 * Retourne le nom de la clé unique
	 * @return le nom de la clé unique
	 */
	public String getUniqueKey();
	
	/**
	 * Définit le nom de la clé unique
	 * @param p_sUniqueKey le nom de la clé unique
	 */
	public void setUniqueKey( String p_sUniqueKey );
}
