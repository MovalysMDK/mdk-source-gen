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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * IOS Xib components description
 * @author spacreau
 *
 */
@XmlRootElement(name="xib-container")
@XmlAccessorType(XmlAccessType.FIELD)
public class MIOSXibFixedListContainer extends MIOSXibContainer{

	/**
	 * Name of the view of the fixed list
	 */
	@XmlElement(name="view-fixedlist-name")
	private String viewFixedListName = null ;
	/**
	 * Name of the item of the fixed list
	 */
	@XmlElement(name="cellitem-fixedlist-name")
	private String cellItemFixedListName = null ;
	
	/**
	 * Does nothing
	 */
	public MIOSXibFixedListContainer() {

	}
	
	public MIOSXibFixedListContainer(String p_sName) {
		super(p_sName);
	}

	/**
	 * Modify the name of the view of the fixed list
	 * @param new name of the view of the fixed list
	 */
	public void setViewFixedListName(String viewFixedListName) {
		this.viewFixedListName = viewFixedListName;
	}
	/**
	 * Return the name of the view of the fixed list
	 * @return name of the view of the fixed list
	 */
	public String getViewFixedListName() {
		return viewFixedListName;
	}
	/**
	 * Modify the name of item of the fixed list  
	 * @param new name of the item of the fixed list
	 */
	public void setCellItemFixedListName(String cellItemFixedListName) {
		this.cellItemFixedListName = cellItemFixedListName;
	}
	/**
	 * Return the name of item of the fixed list  
	 * @return name of the item of the fixed list
	 */
	public String getCellItemFixedListName() {
		return cellItemFixedListName;
	}
	
}