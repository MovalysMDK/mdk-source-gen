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

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.a2a.adjava.xmodele.MScreen;

/**
 * @author lmichenaud
 * 
 */
public class MMenu {

	/**
	 * 
	 */
	private String id;

	/**
	 * 
	 */
	private MScreen client;

	/**
	 * Menu items
	 */
	private List<MMenuItem> menuItems = new ArrayList<MMenuItem>();

	/**
	 * @param p_sId
	 */
	public MMenu(String p_sId) {
		this.id = p_sId;
	}

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param p_oScreen
	 */
	public void setClient(MScreen p_oClient) {
		this.client = p_oClient;
	}

	/**
	 * @return
	 */
	public MScreen getClient() {
		return this.client;
	}

	/**
	 * Add menu item
	 * @param p_oMenuItem
	 */
	public void addMenuItem(MMenuItem p_oMenuItem) {
		this.menuItems.add(p_oMenuItem);
	}

	/**
	 * @return
	 */
	public List<MMenuItem> getMenuItems() {
		return menuItems;
	}

	/**
	 * Xml format
	 * @return
	 */
	public Element toXml() {
		Element r_xMenu = DocumentHelper.createElement("menu");
		r_xMenu.addAttribute("id", this.id);
		for (MMenuItem oMenuItem : this.menuItems) {
			r_xMenu.add(oMenuItem.toXml());
		}
		return r_xMenu;
	}
}
