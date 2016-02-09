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

import java.util.ArrayList;
import java.util.List;

import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MScreen;
import com.a2a.adjava.xmodele.ui.navigation.MNavigationType;

/**
 * Application Menu
 * @author lmichenaud
 *
 */
public class MMenuDef {

	/**
	 * Id
	 */
	private String id ;
	
	/**
	 * Menu items
	 */
	private List<MMenuItem> menuItems = new ArrayList<MMenuItem>();
	
	/**
	 * @param p_sId
	 */
	public MMenuDef(String p_sId) {
		this.id = p_sId;
	}

	/**
	 * Menu id
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return
	 */
	public List<MMenuItem> getMenuItems() {
		return menuItems;
	}

	/**
	 * Add menu item
	 * @param p_oMenuItem
	 */
	public void addMenuItem( MMenuItem p_oMenuItem ) {
		this.menuItems.add(p_oMenuItem);
	}

	/**
	 * Créé un nouveau menu dont le nom et le screen correspond aux paramètres envoyé en entrée de la méthode.
	 * @param p_sName le nom du menu à créer
	 * @param p_oClientScreen le screen dans lequel le menu sera ajouté 
	 * @return un nouveau menu.
	 */
	public MMenu createMenu(String p_sName, MScreen p_oClientScreen, IModelFactory p_oModeleFactory) {
		MMenu r_oMenu = p_oModeleFactory.createMenu(p_sName);
		r_oMenu.setClient(p_oClientScreen);
		for( MMenuItem oMenuItem: this.getMenuItems()) {
			if ( oMenuItem.getNavigation().getTarget() != p_oClientScreen ) {
				MMenuItem oCloneMenuItem = p_oModeleFactory.createMenuItem();
				oCloneMenuItem.setParent(r_oMenu);
				oCloneMenuItem.setNavigation( p_oModeleFactory.createNavigation(
						oMenuItem.getNavigation().getName(), MNavigationType.NAVIGATION_MENU,
						p_oClientScreen, oMenuItem.getNavigation().getTarget()));
				r_oMenu.addMenuItem(oCloneMenuItem);
				oMenuItem.getNavigation().setSource(p_oClientScreen);
			}
		}
		return r_oMenu;
	}
}
