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
package com.a2a.adjava.languages.ios.xmodele.connections;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * IOS Connection
 * @author lmichenaud
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({MIOSActionConnection.class, MIOSOutletConnection.class, MIOSSegueConnection.class})
public class MIOSConnection {

	/**
	 * Connection id
	 */
	@XmlAttribute
	@XmlID
	private String id;

	/**
	 * Connection type
	 */
	private MIOSConnectionType connType ;
	
	/**
	 * Constructor
	 */
	protected MIOSConnection() {
		//for jaxb
	}
	
	/**
	 * Constructor
	 * @param p_oConnectionType connection type
	 */
	protected MIOSConnection( MIOSConnectionType p_oConnectionType ) {
		this.connType = p_oConnectionType ;
	}
	
	/**
	 * Return Connection id
	 * @return connection id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Define connection id
	 * @param p_sId id
	 */
	public void setId(String p_sId) {
		this.id = p_sId;
	}
	
	/**
	 * Get connection type
	 * @return connection type
	 */
	public MIOSConnectionType getConnType() {
		return this.connType;
	}

	/**
	 * Set connection type
	 * @param p_oConnType connection type
	 */
	public void setConntype(MIOSConnectionType p_oConnType) {
		this.connType = p_oConnType;
	}
}
