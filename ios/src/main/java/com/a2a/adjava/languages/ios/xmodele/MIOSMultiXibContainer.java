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

package com.a2a.adjava.languages.ios.xmodele;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * iOS Multi-XibContainer
 * @author qlagarde
 *
 */
@XmlRootElement(name="multi-xib-container")
@XmlAccessorType(XmlAccessType.FIELD)
public class MIOSMultiXibContainer {
		
	/**
	 * Xibs Containers
	 */
	@XmlElementWrapper
	@XmlElement(name = "xib")
	private List<MIOSXibContainer> xibs = new ArrayList<MIOSXibContainer>();
	
	@XmlElement
	private String name;

	/**
	 * Returns the list of xib-containers of the multi xib-container
	 * @return The list of xib-container of the multi xib-container
	 */
	public List<MIOSXibContainer> getXibs() {
		return xibs;
	}
	
	/**
	 * Adds a XIB to the Xib-Container
	 * @param p_oXibContainer The Xib-Container to add
	 */
	public void addXib(MIOSXibContainer p_oXibContainer) {
		this.xibs.add(p_oXibContainer);
	}
	
	/**
	 * Returns the name of this multi-xib-conatainer
	 * @return The name of this multi-xib-container
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this multi-xib-container
	 * @param name The name to set
	 */
	public void setName(String p_sName) {
		this.name = p_sName;
	}
}