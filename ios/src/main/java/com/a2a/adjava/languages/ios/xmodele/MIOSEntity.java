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

import org.dom4j.Element;

import com.a2a.adjava.languages.ios.xmodele.MIOSImportDelegate.MIOSImportCategory;
import com.a2a.adjava.xmodele.MAssociation;
import com.a2a.adjava.xmodele.MAttribute;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MPackage;

/**
 * IOS Entity
 * @author lmichenaud
 *
 */
public class MIOSEntity extends MEntityImpl {
	
	/**
	 * X Position on diagram
	 */
	private int posX ;
	
	/**
	 * Y Position on diagram
	 */
	private int posY ;
	
	/**
	 * Height on diagram
	 */
	private int height ;
	
	/**
	 * Width on diagram
	 */
	private int width ;
	
	/**
	 * Import delegate
	 */
	private MIOSImportDelegate importDlg ;
	
	/**
	 * Constructor
	 * @param p_sName entity name
	 * @param p_oPackage package
	 * @param p_sUmlName uml name
	 * @param p_sEntityName entity name
	 */
	protected MIOSEntity(String p_sName, MPackage p_oPackage,
			String p_sUmlName, String p_sEntityName ) {
		super(p_sName, p_oPackage, p_sUmlName, p_sEntityName);
	}
	
	/**
	 * Override this method because enumeration imports are not needed in objective c.
	 * @param p_oAttribute attribute
	 */
	@Override
	protected void addImportForAttribute( MAttribute p_oAttribute ) {
		if ( !p_oAttribute.getTypeDesc().isEnumeration()) {
			super.addImportForAttribute(p_oAttribute);
		}
		else {
			this.importDlg.addImport(MIOSImportCategory.ENUMERATION.name(), p_oAttribute.getTypeDesc().getName());
		}
	}
	
	/**
	 * Get x position
	 * @return x position
	 */
	public int getPosX() {
		return this.posX;
	}

	/**
	 * Set x position
	 * @param p_iPosX x position
	 */
	public void setPosX(int p_iPosX) {
		this.posX = p_iPosX;
	}

	/**
	 * Get y position
	 * @return y position
	 */
	public int getPosY() {
		return this.posY;
	}

	/**
	 * Set y position
	 * @param p_iPosY y position
	 */
	public void setPosY(int p_iPosY) {
		this.posY = p_iPosY;
	}

	/**
	 * Get height
	 * @return height
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Set height
	 * @param p_iHeight height
	 */
	public void setHeight(int p_iHeight) {
		this.height = p_iHeight;
	}

	/**
	 * Return width
	 * @return width
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Set width
	 * @param p_iWidth width
	 */
	public void setWidth(int p_iWidth) {
		this.width = p_iWidth;
	}
	
	/**
	 * Set import delegate
	 * @param p_oImportDlg import delegate
	 */
	public void setImportDlg(MIOSImportDelegate p_oImportDlg) {
		this.importDlg = p_oImportDlg;
	}

	/**
	 * Return objc classes
	 * @return objc classes
	 */
	private List<String> computeObjcClasses() {
		List<String> r_listClasses = new ArrayList<String>();
		
		for( MAssociation oAssoc : this.getAssociations()) {
			if ( !r_listClasses.contains(oAssoc.getRefClass().getName())) {
				r_listClasses.add( oAssoc.getRefClass().getName());
			}
		}
		
		return r_listClasses ;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.SGeneratedElement#toXml()
	 */
	@Override
	public Element toXml() {
		Element r_xElement = super.toXml();
		r_xElement.addAttribute("pos-x", Integer.toString(this.posX));
		r_xElement.addAttribute("pos-y", Integer.toString(this.posY));
		r_xElement.addAttribute("height", Integer.toString(this.height));
		r_xElement.addAttribute("width", Integer.toString(this.width));
		
		Element xClasses = r_xElement.addElement("objc-classes");
		for( String sClass : this.computeObjcClasses()) {
			xClasses.addElement("objc-class").setText(sClass);
		}
		
		r_xElement.add(this.importDlg.toXml());
		
		return r_xElement;
	}
}
