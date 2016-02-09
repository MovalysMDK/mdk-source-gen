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
package com.a2a.adjava.languages.html5.xmodele;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.a2a.adjava.xmodele.ui.menu.MMenu;
import com.a2a.adjava.xmodele.ui.navigation.MNavigation;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;



/**
 * HTML5 view data
 * @author ftollec
 *
 */
@XmlRootElement(name="html5-view")
@XmlAccessorType(XmlAccessType.FIELD)
public class MH5ScreenView extends MH5View{

	/**
	 * Name of the screen/panel that correspond to this view
	 */
	@XmlID
	private String name ;
	
	/**
	 * Name of the view name
	 */
	@XmlAttribute
	private String viewName;
	
	
	/**
	 * true if this view comes from a screen element on magicDraw
	 */
	@XmlAttribute
	private boolean isScreen;
	
	/**
	 * true if this view comes from the screenroot element on magicDraw
	 */
	@XmlAttribute
	private boolean isMainScreen;
	
	/**
	 * true if this view comes from the workspace element on magicDraw
	 */
	@XmlAttribute
	private boolean isWorkspace;
	
	/**
	 * The name of the first detail panel of a workspace screen
	 */
	private String firstDetailOfWorkspacePanel = null;
	/**
	 * Does screen has save action (for workspace only)
	 */
	private boolean hasSaveAction;
	
	/**
	 * List of screen linked to this one 
	 */
	@XmlElement(name="navigation-from-screen")
	private List<MNavigation> navigationFromScreen = new ArrayList<>();
	
	/**
	 * List of screen linked to this one 
	 */
	@XmlElement(name="navigation-to-screen")
	private List<MNavigation> navigationToScreen = new ArrayList<>();
	
	/**
	* Menu map
	*/
	private Map<String,MMenu> menus = new HashMap<>();
	
	/**
	 * List of panels linked to this one 
	 */
	@XmlElement(name="nestedSubview")
	private List<String> nestedSubview = new ArrayList<>();

	/**
	 * Map of panels VM type in this screen
	 */
	private Map<String, ViewModelType> nestedSubviewType = new HashMap<>();
	
	/**
	 * Visual fields
	 */
	@XmlElement(name="sectionAttributes")
	private List<MH5Attribute> sectionAttributes = new ArrayList<>();
	

	/**
	 * Return name
	 * @return name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Define is the view comes from a screen or a panel
	 * @param p_sIsScreen true if the view comes from a screen or a panel
	 */
	@Override
	public void setIsScreen(boolean p_sIsScreen) {
		this.isScreen = p_sIsScreen;
	}
	
	/**
	 * Return isScreen
	 * @return true if this view comes from a screen
	 */
	@Override
	public boolean p_sIsScreen() {
		return isScreen;
	}

	/**
	 * Define is the view is the mainscreen of the app
	 * @param p_sIsMainScreen true if the view is the mainscreen of the app
	 */
	@Override
	public void setIsMainScreen(boolean p_sIsMainScreen) {
		this.isMainScreen = p_sIsMainScreen;
	}
	
	/**
	 * Return isMainScreen
	 * @return true if this view is the main screen
	 */
	@Override
	public boolean isMainScreen() {
		return isMainScreen;
	}
	
	/**
	 * Retrun isWorkspace
	 * @return true if the view is a workspace
	 */
	public boolean isWorkspace() {
		return isWorkspace;
	}
	/**
	 * Define is the view a workspace
	 * @param isWorkspace
	 */
	public void setIsWorkspace(boolean isWorkspace) {
		this.isWorkspace = isWorkspace;
	}

	public boolean hasSaveAction() {
		return this.hasSaveAction;
	}
	
	public void setHasSaveAction(boolean p_bHasSaveAction) {
		this.hasSaveAction = p_bHasSaveAction;
	}
	
	/**
	 * Define name
	 * @param p_sName name
	 */
	@Override
	public void setName(String p_sName) {
		this.name = p_sName;
	}

	/**
	 * Return viewName
	 * @return viewName
	 */
	@Override
	public String getViewName() {
		return viewName;
	}

	/**
	 * Define viewName
	 * @param p_sViewName name
	 */
	@Override
	public void setViewName(String p_sViewName) {
		this.viewName = p_sViewName;
	}

	/**
	 * Add a panels name
	 * @param p_oNestedSubview panels to add
	 * @param p_oViewModelType 
	 * @param isFirstDetail 
	 */
	public void addNestedSubview(String p_oNestedSubview, ViewModelType p_oViewModelType, boolean isFirstDetail) {
		if(!this.nestedSubview.contains(p_oNestedSubview)){
			this.nestedSubview.add(p_oNestedSubview);
			this.nestedSubviewType.put(p_oNestedSubview, p_oViewModelType);
			if (isFirstDetail) {
				this.firstDetailOfWorkspacePanel = p_oNestedSubview;
			}
		}
	}
	

	/**
	 * Get panels name
	 * @return panels
	 */
	public List<String> getNestedSubview() {
		return this.nestedSubview;
	}
	
	/**
	 * Add a list of attributes
	 * @param list the list of attributes to add
	 */
	public void addAllSectionAttributes(List<MH5Attribute> p_oList) {
		this.sectionAttributes.addAll(p_oList);
	}
	
	/**
	 * Add an attributes to the view 
	 * @param p_oScene scene to add
	 */
	public void addSectionAttribute(MH5Attribute p_oAttr) {
		this.sectionAttributes.add(p_oAttr);
	}
	
	

	/**
	 * Get scenes
	 * @return scenes
	 */
	public List<MH5Attribute> getSectionAttribute() {
		return this.sectionAttributes;
	}
	

	/**
	 * @return the screenNavigation
	 */
	public List<MNavigation> getNavigationFromScreenList() {
		return navigationFromScreen;
	}

	/**
	 * @param screenNavigation the screenNavigation to set
	 */
	public void setNavigationFromScreenList(List<MNavigation> screenNavigation) {
		this.navigationFromScreen = screenNavigation;
	}

	/**
	 * @return the navigationToScreen
	 */
	public List<MNavigation> getNavigationToScreenList() {
		return navigationToScreen;
	}

	/**
	 * @param navigationToScreen the navigationToScreen to set
	 */
	public void setNavigationToScreenList(List<MNavigation> navigationToScreen) {
		this.navigationToScreen = navigationToScreen;
	}

	
	/**
	 * Retourne le menu du screen courant dont l'identifiant correspond à celui envoyé en paramètre.
	 * @param p_sId l'identifiant pour réaliser la recherche.
	 * @return le Menu correspondant si on en trouve un, null sinon
	 */
	public MMenu getMenu( String p_sId ) {
		return this.menus.get(p_sId);
	}
	
	/**
	 * Retourne la liste des menus à ajouter au Screen courant.
	 * @return une collection d'objet MMenu
	 */
	public Collection<MMenu> getMenus() {
		return this.menus.values();
	}
	
	/**
	 * Ajoute un nouveau menu au screen.
	 * @param p_oMenu l'objet MMenu à ajouter
	 */
	public void addMenu( MMenu p_oMenu ) {
		this.menus.put(p_oMenu.getId(), p_oMenu);
	}
	
	/**
	 * Set the screen list of menu to the list put into the parameters
	 * @param menus2
	 */
	public void setMenuList(Collection<MMenu> menus2) {
		this.menus.clear();
		for(MMenu menu : menus2){
			addMenu(menu);
		}
	}
	
	/**
	 * to Xml of the HTML5 view
	 * @return the xml element of the view
	 */
	@Override
	public Element toXml() {
		Element r_xAttr = super.toXml();

		r_xAttr.addAttribute("isScreen", "true");

		r_xAttr.addAttribute("isMainScreen", String.valueOf(this.isMainScreen));
//		if(this.isMainScreen)
//		{
//			r_xAttr.addAttribute("isMainScreen", "true");
//		}else{
//			r_xAttr.addAttribute("isMainScreen", "false");
//		}

		r_xAttr.addAttribute("isWorkspace", String.valueOf(this.isWorkspace));
//		if (this.isWorkspace) {
//			r_xAttr.addAttribute("isWorkspace", "true");
//		} else {
//			r_xAttr.addAttribute("isWorkspace", "false");
//		}
		
		r_xAttr.addAttribute("hasSaveAction", String.valueOf(this.hasSaveAction));

		
		Element xNavigationFromScreens = r_xAttr.addElement("navigation-from-screen-list");
		for( MNavigation oNavigationFromScreen : this.getNavigationFromScreenList() ) {
			if(oNavigationFromScreen != null)
			{
				Element xNavigationFromScreen= oNavigationFromScreen.toXml();
				xNavigationFromScreen.setName("navigation-from-screen");
				xNavigationFromScreens.add(xNavigationFromScreen);
			}
		}
		
		Element xNavigationToScreens = r_xAttr.addElement("navigation-to-screen-list");
		for( MNavigation oNavigationToScreen : this.getNavigationToScreenList() ) {
			if(oNavigationToScreen != null)
			{
				Element xNavigationToScreen= oNavigationToScreen.toXml();
				xNavigationToScreen.setName("navigation-to-screen");
				xNavigationToScreens.add(xNavigationToScreen);
			}
		}
		
		Element xNestedSubviews = r_xAttr.addElement("nestedSubviews");
		Element xNestedSubview = null;
		for(String currNestedSubview : this.getNestedSubview()) {
			if(currNestedSubview != null)
			{
				xNestedSubview = xNestedSubviews.addElement("nestedSubview");
				xNestedSubview.setText(currNestedSubview);
				boolean isList = this.nestedSubviewType.get(currNestedSubview).equals(ViewModelType.LIST_1) || 
						this.nestedSubviewType.get(currNestedSubview).equals(ViewModelType.LIST_2) || 
						this.nestedSubviewType.get(currNestedSubview).equals(ViewModelType.LIST_3);
				xNestedSubview.addAttribute("isList",  String.valueOf(isList));
				if (this.firstDetailOfWorkspacePanel != null && this.firstDetailOfWorkspacePanel.equals(currNestedSubview)) {
					xNestedSubview.addAttribute("isFirstDetail", "true");
				}
			}
		}
		
		Element xMenus = r_xAttr.addElement("menus");
		for( MMenu oMenu : this.menus.values()) {
			xMenus.add(oMenu.toXml());
		}
		
		return r_xAttr;
	}


}
