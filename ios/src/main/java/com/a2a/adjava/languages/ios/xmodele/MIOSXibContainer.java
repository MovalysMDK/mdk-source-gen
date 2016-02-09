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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.a2a.adjava.languages.ios.xmodele.views.MIOSButtonView;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSView;
import com.a2a.adjava.languages.ios.xmodele.views.MIOSXibType;
import com.a2a.adjava.xmodele.ui.view.MVFLocalization;
/**
 * IOS Xib components description
 * @author spacreau
 *
 */
@XmlRootElement(name="xib-container")
@XmlAccessorType(XmlAccessType.FIELD)
public class MIOSXibContainer {
	private String name ;
	/**
	 * List of the components contained in the Xib description of the view
	 */
	@XmlElementWrapper
	@XmlElement(name="component")
	private List<MIOSView> components = new ArrayList<MIOSView>() ;
	/**
	 * Width of the main view in the xib
	 */
	@XmlAttribute
	private int frameWidth = 600 ;
	/**
	 * Height of the main view in the xib
	 */
	@XmlAttribute
	private int frameHeight = -1;
	/**
	 * Margin between elements in the right or top or bottom
	 */
	@XmlAttribute
	private int cellMargin = 10 ;
	
	@XmlElement
	private MIOSXibType xibType;

	/**
	 * Does nothing
	 */
	public MIOSXibContainer() {

	}
	
	public MIOSXibContainer(String p_sName) {
		this.name = p_sName ;
		this.components = new ArrayList<MIOSView>() ;
		this.frameWidth = 600 ;
	}
	/**
	 * Return the List of subview contained in the the xib 
	 * @return list of views
	 */
	public List<MIOSView> getComponents() {
		return this.components;
	}
	/**
	 * Modify the List of subview contained in the the xib 
	 * @param new list of views
	 */
	public void setComponents(List<MIOSView> p_oListComponents) {
		this.components = p_oListComponents;
	}
	
	/**
	 * Return the List of subview contained in the the xib by adding the view 
	 * @param  view added to the liste of subviews
	 */
	public void clearAndAddComponents(List<MIOSView> p_oListComponents ) {
		this.components = new ArrayList<MIOSView>();
		this.components.addAll(p_oListComponents) ;
	}
	/**
	 * Return the List of subview contained in the the xib by adding the view 
	 * @param  view added to the liste of subviews
	 */
	public void addComponent(MIOSView p_oNewMIOSView ) {
		this.components.add(p_oNewMIOSView) ;
	}
	/**
	 * Return the name of xib 
	 * @return name of the xib
	 */
	public String getName() {
		return name;
	}
	/**
	 * Modify the name of xib 
	 * @param name of the xib
	 */
	public void setName(String p_sName) {
		this.name = p_sName;
	}
	/**
	 * Return the height of the main view in the xib 
	 * @return the height of the main view in the xib
	 */
	public int getFrameHeight() {
		return frameHeight;
	}
	/**
	 * Return the width of the main view in the xib 
	 * @return the width of the main view in the xib
	 */
	public int getFrameWidth() {
		return frameWidth;
	}
	/**
	 * Return the size of the margin view in the xib 
	 * @return the size of the margin view in the xib
	 */
	public int getCellMargin() {
		return cellMargin;
	}
	
	/**
	 * Returns the Xib type for this XIB container
	 * @return The xib type
	 */
	public MIOSXibType getXibType() {
		return xibType;
	}

	/**
	 * Sets the XIB type for this container
	 * @param xibType the xib type
	 */
	public void setXibType(MIOSXibType xibType) {
		this.xibType = xibType;
	}
	
	
	/**
	 * Compute the positions of the subview in the XIB and the height of the view
	 */
	public void computeComponentsPosition() {
		int lastYPosition = this.cellMargin ;
		//int firstComponentHeight = 0 ;
		for( MIOSView oView : this.components ) {
			/*
			if ( firstComponentHeight == 0 && oView instanceof MIOSLabelView ){// on ne prend pas en compte le premier label pour positionner les elements
				firstComponentHeight = oView.getHeight() +2;
				lastYPosition = firstComponentHeight ;
				oView.setPosY( this.cellMargin );
				continue ;
			}*/
			// SPA évite de déplacer les boutons centrés 
			if ( oView instanceof MIOSButtonView  ){// on ne traite pas les boutons
				continue ;
			}
			oView.setPosX( this.cellMargin );
			oView.setPosY( lastYPosition );
			//oView.setWidth(this.frameWidth - ( 2 * this.cellMargin) );
			//oView.setHeight( this.cellHeight );
			lastYPosition = lastYPosition + oView.getTotalHeight() + this.cellMargin ;  
		}
		this.frameHeight = lastYPosition;//  + this.cellMargin;
	}
}
