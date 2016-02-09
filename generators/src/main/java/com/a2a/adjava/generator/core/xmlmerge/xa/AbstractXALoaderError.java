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
package com.a2a.adjava.generator.core.xmlmerge.xa;


/**
 * Classe d'erreur abstraite
 * @author smaitre
 *
 * @param <TYPE> le type de l'erreur
 */
public abstract class AbstractXALoaderError<TYPE extends ErrorType> {
	
	/** le type de l'erreur */
	private TYPE type = null;
	/** la localisation de l'erreur */
	private String localisation = null;
	/** la position de l'erreur dans le flux, vaut -1 si la position n'est pas connue */
	private int position = -1;

	/**
	 * Constructeur
	 * @param p_oType le type de l'erreur
	 * @param p_sLocalisation la localisation de l'erreur
	 * @param p_iPosition la position de l'erreur dans le flux
	 */
	public AbstractXALoaderError(TYPE p_oType, String p_sLocalisation, int p_iPosition) {
		this.type = p_oType;
		this.localisation = p_sLocalisation;
		this.position = p_iPosition;
	}
	
	/**
	 * Donne le type de l'erreur
	 * @return le type de l'erreur
	 */
	public TYPE getType() {
		return type;
	}

	/**
	 * Affecte le type de l'erreur
	 * @param p_oType le type de l'erreur
	 */
	public void setType(TYPE p_oType) {
		this.type = p_oType;
	}

	/**
	 * Donne la localisation de l'erreur
	 * @return la localisation de l'erreur
	 */
	public String getLocalisation() {
		return localisation;
	}

	/**
	 * Affecte la localisation de l'erreur
	 * @param p_sLocalisation la localisation de l'erreur
	 */
	public void setLocalisation(String p_sLocalisation) {
		this.localisation = p_sLocalisation;
	}

	/**
	 * Donne la position de l'erreur
	 * @return la position de l'erreur
	 */
	public int getNodePosition() {
		return position;
	}

	/**
	 * Affecte la position de l'erreur, mettre -1 si la position n'est pas connue
	 * @param p_iNodePosition Position Node
	 */
	public void setNodePosition(int p_iNodePosition) {
		this.position = p_iNodePosition;
	}

	/**
	 * Formate le message d'erreur
	 * @return le message d'erreur à afficher
	 */
	public String getMessage() {
		return "[" + this.type.name() + "] " + this.localisation + " [" + this.position + "]" ;
	}
	
}
