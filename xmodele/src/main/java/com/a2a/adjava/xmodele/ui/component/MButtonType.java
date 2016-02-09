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
package com.a2a.adjava.xmodele.ui.component;

public enum MButtonType {

	NAVIGATION(null,null),
	CREATE("buttonCreate","Create"),
	SAVE("buttonSave","Save"),
	DELETE("buttonDelete","Delete"),
	CANCEL("buttonCancel","Cancel");

	/**
	 * 
	 */
	private String labelId ;
	
	/**
	 * 
	 */
	private String labelValue ;
	
	/**
	 * @param p_sLabelId
	 * @param p_sLabelValue
	 */
	private MButtonType( String p_sLabelId, String p_sLabelValue ) {
		this.labelId = p_sLabelId ;
		this.labelValue = p_sLabelValue ;
	}

	public String getLabelId() {
		return labelId;
	}

	public String getLabelValue() {
		return labelValue;
	}
}
