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

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.a2a.adjava.xmodele.MAction;
import com.a2a.adjava.xmodele.MActionType;
import com.a2a.adjava.xmodele.MScreen;
import com.a2a.adjava.xmodele.ui.component.MNavigationButton;

public class MMenuActionItem extends MMenuItem {

	private List<MAction> actions;
	private String id;
	private MNavigationButton navigation;
	private String menuClassName;
	private String menuPackageName;
	private MScreen screen;
	
	public MMenuActionItem(String p_sId) {
		this.id = p_sId;
		this.actions = new ArrayList<MAction>();
		this.navigation = null;
		this.menuClassName = StringUtils.EMPTY;
		this.menuPackageName = StringUtils.EMPTY;
	}
	
	public String getId() {
		return this.id;
	}
	
	public MScreen getScreen() {
		return this.screen;
	}
	
	public void setScreen(MScreen p_oscreen)
	{
		this.screen = p_oscreen;
		updateClassMenu();
	}
	
	
	public void addMenuAction(MAction p_oAction) {
		this.actions.add(p_oAction);
		if ( this.actions.size() == 1 ) {
			updateClassMenu();
		}
	}

	public void addMenuAction(MNavigationButton p_oNavigation) {
		this.navigation = p_oNavigation;
	}
	
	public List<MAction> getActions() {
		return actions;
	}
	
	public MNavigationButton getNavigationButton() {
		return navigation;
	}
	
	
	public void updateClassMenu(){
		MAction oAction = !this.actions.isEmpty() ? this.actions.get(0): null;
		if(null!=oAction && null!=this.screen)
		{
			String sufix = StringUtils.EMPTY;
			if (oAction.getType().equals(MActionType.SAVEDETAIL)) {
				sufix  = "SaveActionProvider";
			} else if (oAction.getType().equals(MActionType.DELETEDETAIL)) {
				sufix = "DeleteActionProvider";
			} else {
				sufix = "ActionProvider";
			}
			menuClassName = this.screen.getName()+sufix;
			menuPackageName = this.screen.getPackage().getParent().getFullName()+".action.actionprovider";
		}
	}
	/**
	 * @return
	 */
	public Element toXml() {
		Element r_xMenu = DocumentHelper.createElement("menu-item");
		r_xMenu.addAttribute("id", this.id);
		if ( !actions.isEmpty()) {
			Element actionProvider = r_xMenu.addElement("action-provider");
			actionProvider.addElement("name").addText(menuClassName);
			actionProvider.addElement("full-name").addText(menuPackageName+"."+menuClassName);
			actionProvider.addElement("package").addText(menuPackageName);
			actionProvider.addElement("type").addText(actions.get(0).getType().name());
		} else if (this.navigation != null) {
			r_xMenu.add(this.navigation.toXml());
		}
		return r_xMenu ;
	}

	public String getMenuClassName() {
		return menuClassName;
	}

	public void setMenuClassName(String className) {
		this.menuClassName = className;
	}

	public String getMenuPackageName() {
		return menuPackageName;
	}

	public void setMenuPackageName(String packageName) {
		this.menuPackageName = packageName;
	}


	
}
