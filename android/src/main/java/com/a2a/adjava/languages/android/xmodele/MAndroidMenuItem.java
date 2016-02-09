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
package com.a2a.adjava.languages.android.xmodele;

import org.dom4j.Element;

import com.a2a.adjava.xmodele.ui.menu.MMenuItem;

/**
 * MenuItem in android
 * @author lmichenaud
 *
 */
public class MAndroidMenuItem extends MMenuItem {

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.ui.menu.MMenuItem#toXml()
	 */
	@Override
	public Element toXml() {
		Element r_xElement = super.toXml();
		
		//menu naming: "menu_" + screen name owning the menu + "_" + menu id + "_" + target screen name   
		//menu_ctrlabsenceabonnesscreen_options_ctrldocumentscreen
		r_xElement.addAttribute("id",
				"menu_" + this.getParent().getClient().getName().toLowerCase() 
				+ "_" + this.getParent().getId() + "_" + this.getNavigation().getTarget().getName().toLowerCase());
		return r_xElement;
	}
}
