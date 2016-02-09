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



import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;

/**
 * Loader abstrait permettant de gérer les erreurs
 * @author smaitre
 *
 * @param <ERROR_TYPE> le type d'erreur
 * @param <ERROR_CLASS> la classe à utiliser pour encapsuler le type d'erreur
 */
public abstract class AbstractXALoader<ERROR_TYPE extends ErrorType, ERROR_CLASS extends AbstractXALoaderError<ERROR_TYPE>> {

	/** 
	 * Le log à utiliser 
	 * */
	private static final Logger LOG = LoggerFactory.getLogger(AbstractXALoader.class);
	
	/** l'ensemble des erreurs chargées par le loader */
	private List<ERROR_CLASS> errors = null;
			
	/**
	 * Construit un nouveau loader
	 */
	public AbstractXALoader() {
		this.errors = new ArrayList<ERROR_CLASS>();
	}
	
	/**
	 * Charge les données
	 * @throws AdjavaException 
	 */
	public void load() throws AdjavaException {
		
		LOG.debug("[AbstractXALoader#load] ----------  Beginning of data loading...");
		
		this.treat();
		
		this.logErrors();

		LOG.debug("[AbstractXALoader#load] ----------   End of data loading");
	}
	
	/**
	 * Traitement principale
	 * 
	 * @throws AdjavaException
	 */
	protected abstract void treat() throws AdjavaException;

	/**
	 * Logs les erros dans le log courant
	 * 
	 * @throws AdjavaException 
	 */
	private void logErrors() throws AdjavaException {
		StringBuilder sErrorMsg = new StringBuilder();
		for(ERROR_CLASS oError : this.errors) {
			LOG.error(oError.getMessage());

			sErrorMsg.append(oError.getMessage().toString());
			sErrorMsg.append('\n');
		}
		if(!this.errors.isEmpty()) {
			throw new AdjavaException("Errors found while merging XML files\n"+sErrorMsg);
		}
	}
	
	/**
	 * Permet de créer une erreur
	 * 
	 * @param p_oType le type de l'erreur
	 * @param p_sPath le chemin indicatif de l'erreur
	 * @param p_iPosition la position de l'élément qui pose problème dans le flux analysé, à pour valeur -1 si pas de position connue
	 * @return une erreur
	 */
	protected abstract ERROR_CLASS createError(ERROR_TYPE p_oType, String p_sPath, int  p_iPosition);
	
	/**
	 * Ajout d'une erreur
	 * 
	 * @param p_oType le type de l'erreur
	 * @param p_sPath le chemin indicatif de l'erreur
	 * @param p_iPosition la position de l'élément qui pose problème dans le flux analysé, à pour valeur -1 si pas de position connue
	 */
	public void addError(ERROR_TYPE p_oType, Path p_sPath, int  p_iPosition) {
		addError( p_oType, p_sPath.toString(),   p_iPosition);
	}

	/**
	 * Ajout d'une erreur
	 * 
	 * @param p_oType le type de l'erreur
	 * @param p_sPath le chemin indicatif de l'erreur
	 * @param p_iPosition la position de l'élément qui pose problème dans le flux analysé, à pour valeur -1 si pas de position connue
	 */
	public void addError(ERROR_TYPE p_oType, String p_sPath, int  p_iPosition) {
		ERROR_CLASS oError = createError(p_oType, p_sPath, p_iPosition);
		this.errors.add(oError);
		LOG.error(oError.getMessage());
	}
	
	/**
	 * L'ensemble des erreurs levées par le loader
	 * @return la liste des erreurs
	 */
	public List<ERROR_CLASS> getErrors() {
		return this.errors;
	}
	
	/**
	 * Donne la première erreur du type passé en paramètre
	 * 
	 * @param p_oType le type dont on cherche la première occurence de l'erreur
	 * @return une erreur du type recherché
	 */
	public ERROR_CLASS getFirstErrorByType(ERROR_TYPE p_oType) {
		ERROR_CLASS r_oError = null;
		for(ERROR_CLASS oError : this.errors) {
			if (p_oType.equals(oError.getType())) {
				r_oError = oError;
				break;
			}
		}
		return r_oError;
	}
	
	/**
	 * Indique si le loader a relevé des erreurs
	 * @return true si loader a relevé des erreurs
	 */
	public boolean hasError() {
		return this.errors.size()>0;
	}
}
