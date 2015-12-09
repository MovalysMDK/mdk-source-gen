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
package com.a2a.adjava.languages.ios.project;

import com.a2a.adjava.types.UITypeDescription;
import com.a2a.adjava.xmodele.ui.view.MVFModifier;

/**
 * IOS Ui Type Description
 * @author lmichenaud
 *
 */
public class IOSUITypeDescription extends UITypeDescription implements Cloneable {

	/**
	 * Height of readonly component
	 */
	private int roComponentHeight;
	
	/**
	 * Height of readwrite component
	 */
	private int rwComponentHeight;

	/**
	 * Width of readonly component
	 */
	private int roComponentWidth;
	
	/**
	 * Width of readwrite component
	 */
	private int rwComponentWidth;

	/**
	 * Storyboard type for readonly component
	 */
	private String roStoryboardType;
	
	/**
	 * Storyboard type for readwrite component
	 */
	private String rwStoryboardType;
	
	/**
	 * Cell type for read-only mode
	 */
	private String roCellType;

	/**
	 * Cell type for write mode
	 */
	private String rwCellType;
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.a2a.adjava.project.TypeDescription#clone()
	 */
	@Override
	public Object clone() {
		super.clone();
		IOSUITypeDescription r_oTypeDescription = new IOSUITypeDescription();
		this.copyPropertiesTo(r_oTypeDescription);
		return r_oTypeDescription;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.project.TypeDescription#copyPropertiesTo(com.a2a.adjava.project.ITypeDescription)
	 */
	@Override
	protected void copyPropertiesTo(UITypeDescription p_oUITypeDescription) {
		super.copyPropertiesTo(p_oUITypeDescription);
		IOSUITypeDescription oUITypeDescription = (IOSUITypeDescription) p_oUITypeDescription;
		oUITypeDescription.setRoComponentHeight(this.roComponentHeight);
		oUITypeDescription.setRwComponentHeight(this.rwComponentHeight);
		oUITypeDescription.setRoComponentWidth(this.roComponentWidth);
		oUITypeDescription.setRwComponentWidth(this.rwComponentWidth);
		oUITypeDescription.setRoStoryboardType(this.roStoryboardType);
		oUITypeDescription.setRwStoryboardType(this.rwStoryboardType);
		oUITypeDescription.setRoCellType(this.roCellType);
		oUITypeDescription.setRwCellType(this.rwCellType);
	}

	/**
	 * Return storyboard type of ro component
	 * @return storyboard type of ro component
	 */
	public String getRoStoryboardType() {
		return roStoryboardType;
	}

	/**
	 * Define storyboard type of ro component
	 * @param p_sRoStoryboardType Storyboard type of ro component
	 */
	public void setRoStoryboardType(String p_sRoStoryboardType) {
		this.roStoryboardType = p_sRoStoryboardType;
	}

	/**
	 * Return storyboard type of rw component
	 * @return Storyboard type of rw component
	 */
	public String getRwStoryboardType() {
		return rwStoryboardType;
	}

	/**
	 * Define storyboard type of rw component
	 * @param p_sRwStoryboardType Storyboard type of rw component
	 */
	public void setRwStoryboardType(String p_sRwStoryboardType) {
		this.rwStoryboardType = p_sRwStoryboardType;
	}
	
	
	/**
	 * Return height of ro component
	 * @return height of ro component
	 */
	public int getRoComponentHeight() {
		return roComponentHeight;
	}

	/**
	 * Define height of ro component
	 * @param p_iRoComponentHeight height of ro component
	 */
	public void setRoComponentHeight(int p_iRoComponentHeight) {
		this.roComponentHeight = p_iRoComponentHeight;
	}

	/**
	 * Return height of rw component
	 * @return height of rw component
	 */
	public int getRwComponentHeight() {
		return rwComponentHeight;
	}

	/**
	 * Define height of rw component
	 * @param p_iRwComponentHeight height of rw component
	 */
	public void setRwComponentHeight(int p_iRwComponentHeight) {
		this.rwComponentHeight = p_iRwComponentHeight;
	}

	/**
	 * Return width of readonly component
	 * @return width of readonly component
	 */
	public int getRoComponentWidth() {
		return this.roComponentWidth;
	}

	/**
	 * Define width of readonly component
	 * @param p_iRoComponentWidth width of readonly component
	 */
	public void setRoComponentWidth(int p_iRoComponentWidth) {
		this.roComponentWidth = p_iRoComponentWidth;
	}

	/**
	 * Return width of readwrite component
	 * @return width of readwrite component
	 */
	public int getRwComponentWidth() {
		return this.rwComponentWidth;
	}

	/**
	 * Define width of readwrite component
	 * @param p_iRwComponentWidth width of readwrite component
	 */
	public void setRwComponentWidth(int p_iRwComponentWidth) {
		this.rwComponentWidth = p_iRwComponentWidth;
	}

	/**
	 * Get cell type for read-only mode
	 * @return cell type for read-only mode
	 */
	public String getRoCellType() {
		return this.roCellType;
	}

	/**
	 * Set cell type for read-only mode
	 * @param p_sRoCellType cell type
	 */
	public void setRoCellType(String p_sRoCellType) {
		this.roCellType = p_sRoCellType;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.types.IUITypeDescription#getComponentType(com.a2a.adjava.xmodele.ui.view.MVFModifier)
	 */
	public String getCellType(MVFModifier p_oMVFModifier) {
		return MVFModifier.READONLY.equals(p_oMVFModifier) ? this.getRoCellType() : this.getRwCellType();
	}

	/**
	 * Get cell type for write mode
	 * @return cell type for write mode
	 */
	public String getRwCellType() {
		return this.rwCellType;
	}

	/**
	 * Sets cell type for write mode
	 * @param p_sRwCellType cell type for write mode
	 */
	public void setRwCellType(String p_sRwCellType) {
		this.rwCellType = p_sRwCellType;
	}


}
