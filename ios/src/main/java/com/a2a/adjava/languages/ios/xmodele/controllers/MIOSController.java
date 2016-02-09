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
package com.a2a.adjava.languages.ios.xmodele.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.a2a.adjava.languages.ios.xmodele.MIOSClass;
import com.a2a.adjava.languages.ios.xmodele.connections.MIOSConnection;

/**
 * Ios Controller
 * @author lmichenaud
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="controller")
@XmlSeeAlso({MIOSNavigationController.class, MIOSViewController.class, MIOSFormViewController.class, MIOSListViewController.class, MIOS2DListViewController.class})
public class MIOSController {
	
	/**
	 * Controller id
	 */
	@XmlID
	@XmlAttribute(required=true)
	private String id ;
	
	/**
	 * Controller name
	 */
	@XmlElement(required=true)
	private String name ;
	
	/**
	 * Base name (without extension .h, .m) for custom class
	 */
	@XmlElement(required=false)
	private MIOSClass customClass ;
	
	/**
	 * Controller type
	 */
	@XmlAttribute(required=false)
	private MIOSControllerType controllerType ;

	/**
	 * Connections
	 */
	@XmlElementWrapper
	@XmlElement(name="connection")
	private List<MIOSConnection> connections = new ArrayList<MIOSConnection>();
	
	/**
	 * Return true if controller has custom class
	 * @return true if controller has custom class
	 */
	public boolean hasCustomClass() {
		return this.customClass != null ;
	}
	
	/**
	 * Return id
	 * @return id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Define id
	 * @param p_sId id
	 */
	public void setId(String p_sId) {
		this.id = p_sId;
	}

	/**
	 * Return controller type
	 * @return controller type
	 */
	public MIOSControllerType getControllerType() {
		return this.controllerType;
	}

	/**
	 * Define controller type
	 * @param p_oType controller type
	 */
	public void setControllerType(MIOSControllerType p_oType) {
		this.controllerType = p_oType;
	}
	
	/**
	 * Add a connection
	 * @param p_oIOSConnection ios connection
	 */
	public void addConnection( MIOSConnection p_oIOSConnection ) {
		this.connections.add(p_oIOSConnection);
	}

	/**
	 * Return connections
	 * @return connections
	 */
	public List<MIOSConnection> getConnections() {
		return this.connections;
	}
	
	/**
	 * Set the connections
	 * @param p_oConnections The connections to set
	 */
	public void setConnections(List<MIOSConnection> p_oConnections) {
		this.connections = p_oConnections;
	}

	/**
	 * Return custom class
	 * @return custom class
	 */
	public MIOSClass getCustomClass() {
		return this.customClass;
	}

	/**
	 * Define custom class
	 * @param p_oCustomClass custom class
	 */
	public void setCustomClass(MIOSClass p_oCustomClass) {
		this.customClass = p_oCustomClass;
	}

	/**
	 * Return controller name
	 * @return controller name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Define controller name
	 * @param p_sName controller name
	 */
	public void setName(String p_sName) {
		this.name = p_sName;
	}
}
