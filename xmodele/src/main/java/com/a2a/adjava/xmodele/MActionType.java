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

import com.a2a.adjava.utils.StrUtils;

/**
 * @author lmichenaud
 *
 */
public enum MActionType {

	/**
	 * Permet la sauvegarde d'un détail automappé et la sortie de l'écran grâce
	 * à l'ajout d'un bouton
	 */
	SAVEDETAIL,

	/**
	 * Permet la suppression de l'élément courant dans le formulaire courant
	 * grâce à l'ajout d'un bouton
	 */
	DELETEDETAIL,

	/**
	 * Type des actions effectuant uniquement des calculs métiers.
	 */
	COMPUTE,
	
	/**
	 * Action d'affichage d'un dialogue
	 */
	DIALOG;

	/**
	 * 
	 */
	private String paramIn;
	
	/**
	 * 
	 */
	private String paramInFullName;

	/**
	 * 
	 */
	private String paramOut;
	
	/**
	 * 
	 */
	private String paramOutFullName;
	
	/**
	 * 
	 */
	private String paramStep;
	
	/**
	 * 
	 */
	private String paramStepFullName;
	
	/**
	 * 
	 */
	private String paramProgress;
	
	/**
	 * 
	 */
	private String paramProgressFullName;

	/**
	 * 
	 */
	private MActionType() {
	}

	/**
	 * @return
	 */
	public String getParamIn() {
		return this.paramIn;
	}

	/**
	 * @return
	 */
	public String getParamInFullName() {
		return this.paramInFullName;
	}

	/**
	 * @param paramInFullName
	 */
	public void setParamInFullName(String paramInFullName) {
		this.paramInFullName = paramInFullName;
		this.paramIn = StrUtils.substringAfterLastDot(this.paramInFullName);
	}

	/**
	 * @return
	 */
	public String getParamOut() {
		return this.paramOut;
	}

	/**
	 * @return
	 */
	public String getParamOutFullName() {
		return this.paramOutFullName;
	}

	/**
	 * @param paramOutFullName
	 */
	public void setParamOutFullName(String p_sParamOutFullName) {
		this.paramOutFullName = p_sParamOutFullName;
		this.paramOut = StrUtils.substringAfterLastDot(this.paramOutFullName);
	}

	/**
	 * @return
	 */
	public String getParamStep() {
		return this.paramStep;
	}
	
	/**
	 * @return
	 */
	public String getParamStepFullName() {
		return this.paramStepFullName;
	}

	/**
	 * @param p_sParamStepFullName
	 */
	public void setParamStepFullName(String p_sParamStepFullName) {
		this.paramStepFullName = p_sParamStepFullName;
		this.paramStep = StrUtils.substringAfterLastDot(this.paramStepFullName);
	}

	/**
	 * @return
	 */
	public String getParamProgress() {
		return this.paramProgress;
	}
	
	/**
	 * @return
	 */
	public String getParamProgressFullName() {
		return this.paramProgressFullName;
	}

	/**
	 * @param p_sParamProgressFullName
	 */
	public void setParamProgressFullName(String p_sParamProgressFullName) {
		this.paramProgressFullName = p_sParamProgressFullName;
		this.paramProgress = StrUtils.substringAfterLastDot(this.paramProgressFullName);
	}
}