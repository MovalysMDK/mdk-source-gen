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

import org.dom4j.Element;

import com.a2a.adjava.xmodele.ui.navigation.MNavigation;

/**
 * @author lmichenaud
 *
 */
public class MNavigationButton extends MAbstractButton {

	/**
	 * Navigation
	 */
	private MNavigation navigation ;
	
	/**
	 * @param p_sName
	 * @param p_oButtonType
	 * @param p_oNavigation
	 */
	public MNavigationButton(String p_sName, MButtonType p_oButtonType, MNavigation p_oNavigation ) {
		super(p_sName, p_oButtonType);
		this.navigation = p_oNavigation ;
	}

	/**
	 * @param p_sName
	 * @param p_sLabelId
	 * @param p_sLabelValue
	 * @param p_oButtonType
	 * @param p_oNavigation
	 */
	public MNavigationButton(String p_sName, String p_sLabelId, String p_sLabelValue, MButtonType p_oButtonType, MNavigation p_oNavigation) {
		super(p_sName, p_sLabelId, p_sLabelValue, p_oButtonType);
		this.navigation = p_oNavigation ;
	}
	
	/**
	 * Return navigation
	 * @return navigation
	 */
	public MNavigation getNavigation() {
		return this.navigation;
	}

	/**
	 * (non-Javadoc)
	 * @see com.a2a.adjava.xmodele.ui.component.MAbstractButton#toXml()
	 */
	@Override
	public Element toXml() {
		Element r_xNavButton = super.toXml();
		r_xNavButton.add(this.navigation.toXml());
		return r_xNavButton;
	}
}
