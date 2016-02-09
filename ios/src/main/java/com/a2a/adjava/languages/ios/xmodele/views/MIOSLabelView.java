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
package com.a2a.adjava.languages.ios.xmodele.views;

/**
 * IOS Label
 * @author lmichenaud
 *
 */
public class MIOSLabelView extends MIOSView {

	/**
	 * Constructor
	 */
	public MIOSLabelView(){
		super();
	}
	/**
	 * Label value
	 */
	private String value ;

	/**
	 * Return value
	 * @return value
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Define value
	 * @param p_sValue value
	 */
	public void setValue(String p_sValue) {
		this.value = p_sValue;
	}
	
	/**
	 * Copies a view to this one
	 * @param p_oMIOSView the view to copy
	 */
	public void copyTo(MIOSLabelView p_oMIOSView) {
		super.copyTo(p_oMIOSView);
		p_oMIOSView.setValue(this.value);
	}
}
