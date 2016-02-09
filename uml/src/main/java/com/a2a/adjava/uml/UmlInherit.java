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
package com.a2a.adjava.uml;

/**
 * Uml inherit
 * @author lmichenaud
 *
 */
public class UmlInherit {

	private UmlClass parent ;
	private UmlClass child ;
	private int type ;
	private String childDiscriminant ;
	private String parentDiscriminant ;
	
	/**
	 * @return
	 */
	public UmlClass getParent() {
		return parent;
	}
	
	/**
	 * @param p_oParent
	 */
	public void setParent(UmlClass p_oParent) {
		this.parent = p_oParent;
	}
	
	/**
	 * @return
	 */
	public UmlClass getChild() {
		return child;
	}
	
	/**
	 * @param p_oChild
	 */
	public void setChild(UmlClass p_oChild) {
		this.child = p_oChild;
	}
	/**
	 * @return
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * @param p_iType
	 */
	public void setType(int p_iType) {
		this.type = p_iType;
	}
	
	/**
	 * @return
	 */
	public String getChildDiscriminant() {
		return childDiscriminant;
	}
	
	/**
	 * @param p_sChildDiscriminant
	 */
	public void setChildDiscriminant(String p_sChildDiscriminant) {
		this.childDiscriminant = p_sChildDiscriminant;
	}
	
	/**
	 * @return
	 */
	public String getParentDiscriminant() {
		return parentDiscriminant;
	}
	
	/**
	 * @param p_sParentDiscriminant
	 */
	public void setParentDiscriminant(String p_sParentDiscriminant) {
		this.parentDiscriminant = p_sParentDiscriminant;
	}
}
