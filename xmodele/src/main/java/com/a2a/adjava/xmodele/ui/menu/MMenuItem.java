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
package com.a2a.adjava.xmodele.ui.menu;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.a2a.adjava.xmodele.ui.navigation.MNavigation;

/**
 * Application Menu item
 * @author lmichenaud
 *
 */
public class MMenuItem {

	/**
	 * 
	 */
	protected MMenu parent ;
		
	/**
	 * Target screen
	 */
	private MNavigation navigation ;
	
	/**
	 * @return
	 */
	public MNavigation getNavigation() {
		return this.navigation;
	}

	/**
	 * @param p_oNavigation
	 */
	public void setNavigation(MNavigation p_oNavigation) {
		this.navigation = p_oNavigation;
	}
	
	/**
	 * @return
	 */
	public MMenu getParent() {
		return this.parent;
	}

	/**
	 * @param p_oParent
	 */
	public void setParent(MMenu p_oParent) {
		this.parent = p_oParent;
	}

	/**
	 * @return
	 */
	public Element toXml() {
		Element r_xMenu = DocumentHelper.createElement("menu-item");
		r_xMenu.add(this.navigation.toXml());
		return r_xMenu ;
	}
}
