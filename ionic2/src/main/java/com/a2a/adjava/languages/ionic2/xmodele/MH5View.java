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
package com.a2a.adjava.languages.ionic2.xmodele;

import java.util.Map;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;



/**
 * HTML5 view data
 * @author ftollec
 *
 */
@XmlRootElement(name="html5-view")
@XmlAccessorType(XmlAccessType.FIELD)
public class MH5View {

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
	 * Name of the view name of the mainScreen
	 */
	@XmlAttribute
	private String mainScreenName;
	
	/**
	 * ExitState of this view 
	 */
	private String exitState;

	/**
	 * ExitStateParams of this view
	 */
	private Map<String, String> exitStateParams;
	
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
	 * Return name
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Define is the view comes from a screen or a panel
	 * @param p_sIsScreen true if the view comes from a screen or a panel
	 */
	public void setIsScreen(boolean p_sIsScreen) {
		this.isScreen = p_sIsScreen;
	}
	
	/**
	 * Return isScreen
	 * @return true if this view comes from a screen
	 */
	public boolean p_sIsScreen() {
		return isScreen;
	}

	/**
	 * Define is the view is the mainscreen of the app
	 * @param p_sIsMainScreen true if the view is the mainscreen of the app
	 */
	public void setIsMainScreen(boolean p_sIsMainScreen) {
		this.isMainScreen = p_sIsMainScreen;
	}
	
	/**
	 * Return isMainScreen
	 * @return true if this view is the main screen
	 */
	public boolean isMainScreen() {
		return isMainScreen;
	}
	
	
	/**
	 * Return the main screen name
	 * @return the main screen name
	 */
	public String getMainScreenName() {
		return mainScreenName;
	}

	/**
	 * Define the main screen name
	 * @param p_sMainScreenName name
	 */
	public void setMainScreenName(String p_sMainScreenName) {
		this.mainScreenName = p_sMainScreenName;
	}
	
	/**
	 * Define name
	 * @param p_sName name
	 */
	public void setName(String p_sName) {
		this.name = p_sName;
	}

	/**
	 * Return viewName
	 * @return viewName
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * Define viewName
	 * @param p_sViewName name
	 */
	public void setViewName(String p_sViewName) {
		this.viewName = p_sViewName;
	}

	

	/**
	 * @return the exitState
	 */
	public String getExitState() {
		return exitState;
	}

	/**
	 * @param exitState the exitState to set
	 * @param exitStateParams 
	 */
	public void setExitState(String pExitState, Map<String, String> pExitStateParams) {
		this.exitState = pExitState;
		this.exitStateParams = pExitStateParams;
	}

	/**
	 * to Xml of the HTML5 view
	 * @return the xml element of the view
	 */
	public Element toXml() {
		Element r_xAttr = DocumentHelper.createElement("view");
		r_xAttr.addElement("name").setText(this.getName());
		r_xAttr.addElement("name-lc").setText(this.getName().toLowerCase());
		r_xAttr.addElement("viewName").setText(this.getViewName());
		Element xmlExitState = r_xAttr.addElement("exitState");
		xmlExitState.addElement("state").setText(this.getExitState());
		for (String key : this.exitStateParams.keySet()) {
			xmlExitState.addElement("param").addAttribute("paramName", key).setText(this.exitStateParams.get(key));
		}
		r_xAttr.addElement("mainScreenName").setText(this.getMainScreenName());
		
		return r_xAttr;
	}


}
