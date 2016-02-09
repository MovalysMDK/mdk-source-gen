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
package com.a2a.adjava.languages.ios.xmodele.views;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.a2a.adjava.xmodele.ui.view.MVFLocalization;

/**
 * IOS View
 * @author lmichenaud
 *
 */
@XmlSeeAlso({MIOSButtonView.class, MIOSLabelView.class, MIOSEditableView.class})
@XmlAccessorType(XmlAccessType.FIELD)
public class MIOSView {

	/**
	 * Constructor
	 */
	public MIOSView(){
		viewClass = this.getClass().getSimpleName();
		userInteractionEnabled = true;
	}
	
	/**
	 * View id
	 */
	@XmlAttribute
	@XmlID
	private String id ;
	
	/**
	 * Custom class
	 */
	@XmlElement
	private String customClass ;
	
	/**
	 * Position X
	 */
	@XmlAttribute
	private int posX ;
	
	/**
	 * Position y
	 */
	@XmlAttribute
	private int posY ;
	
	/**
	 * Width
	 */
	@XmlAttribute
	private int width ;

	@XmlAttribute
	private String  viewClass;

	/**
	 * Width
	 */
	@XmlAttribute
	private int height ;

	/**
	 * Binding in viewmodel
	 */
	@XmlElement
	private String binding ;
	
	/**
	 * Prefix used for the binding
	 */
	@XmlElement
	private String bindingPrefix;
	
	/**
	 * Prefix used for the binding
	 */
	@XmlElement
	private String bindingSuffix;
	
	/**
	 * user interaction in viewmodel
	 */
	@XmlAttribute
	private boolean userInteractionEnabled ;	

	/**
	 * Property name
	 */
	@XmlElement
	private String propertyName;
	/**
	 * Localisation
	 */
	@XmlElement
	private MVFLocalization localization ;
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
	 * Return custom class
	 * @return custom class
	 */
	public String getCustomClass() {
		return this.customClass;
	}

	/**
	 * Define custom class
	 * @param p_sCustomClass custom class
	 */
	public void setCustomClass(String p_sCustomClass) {
		this.customClass = p_sCustomClass;
	}

	/**
	 * Return x position
	 * @return x position
	 */
	public int getPosX() {
		return posX;
	}

	/**
	 * Define x position
	 * @param p_iPosX x position
	 */
	public void setPosX(int p_iPosX) {
		this.posX = p_iPosX;
	}

	/**
	 * Return y position
	 * @return y position
	 */
	public int getPosY() {
		return this.posY;
	}

	/**
	 * Define y position
	 * @param p_iPosY y position
	 */
	public void setPosY(int p_iPosY) {
		this.posY = p_iPosY;
	}

	/**
	 * Return width
	 * @return width
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Define width
	 * @param p_iWidth width
	 */
	public void setWidth(int p_iWidth) {
		this.width = p_iWidth;
	}

	/**
	 * Return height
	 * @return height
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Define height
	 * @param p_iHeight height
	 */
	public void setHeight(int p_iHeight) {
		this.height = p_iHeight;
	}
	

	/**
	 * Return UserInteractionEnabled
	 * @return true if UserInteraction is enabled
	 */
	public boolean isUserInteractionEnabled() {
		return this.userInteractionEnabled;
	}

	/**
	 * Define UserInteractionEnabled
	 * @param p_bIsUserInteractionEnabled is user interaction enabled
	 */
	public void setUserInteractionEnabled(boolean p_bIsUserInteractionEnabled) {
		this.userInteractionEnabled = p_bIsUserInteractionEnabled;
	}


	/**
	 * Get binding
	 * @return binding
	 */
	public String getBinding() {
		return this.binding;
	}

	/**
	 * Set binding
	 * @param p_sBinding binding
	 */
	public void setBinding(String p_sBinding) {
		this.binding = p_sBinding;
	}
	
	/**
	 * Get bindingPrefix
	 * @return bindingPrefix
	 */
	public String getBindingPrefix() {
		return this.bindingPrefix;
	}
	
	/**
	 * Set bindingPrefix
	 * @param p_sBindingPrefix bindingPrefix
	 */
	public void setBindingPrefix(String p_sBindingPrefix) {
		this.bindingPrefix = p_sBindingPrefix;
	}
	
	/**
	 * Get bindingSuffix
	 * @return bindingSuffix
	 */
	public String getBindingSuffix() {
		return this.bindingSuffix;
	}
	
	/**
	 * Set prefix
	 * @param p_sBindingSuffix prefix
	 */
	public void setBindingSuffix(String p_sBindingSuffix) {
		this.bindingSuffix = p_sBindingSuffix;
	}

	/**
	 * Get property name
	 * @return property name
	 */
	public String getPropertyName() {
		return this.propertyName;
	}

	/**
	 * Set property name
	 * @param p_sPropertyName property name
	 */
	public void setPropertyName(String p_sPropertyName) {
		this.propertyName = p_sPropertyName;
	}
	/**
	 * Return localization of the view
	 * @return the localization of the view
	 */
	public MVFLocalization getLocalization() {
		return localization;
	}
	/**
	 * Set localization of the view
	 * @param p_oLocalisation the localization to set
	 */
	public void setLocalization(MVFLocalization p_oLocalisation) {
		this.localization = p_oLocalisation;
	}
	
	/**
	 * Returns the class of the view
	 * @return the class of the view
	 */
	public String getViewClass() {
		return viewClass;
	}

	/**
	 * Sets the class of the view
	 * @param p_sViewClass the class to set
	 */
	public void setViewClass(String p_sViewClass) {
		this.viewClass = p_sViewClass;
	}

	/**
     * Copy the attributes in the other view
	 * @param p_oClonedView view modified to have the same values  of attributes
	 */
	public void copyTo(MIOSView p_oClonedView) {
		p_oClonedView.setBinding(this.binding);
		p_oClonedView.setBindingPrefix(this.bindingPrefix);
		p_oClonedView.setBindingSuffix(this.bindingSuffix);
		p_oClonedView.setCustomClass(this.customClass);
		p_oClonedView.setHeight(this.height);
		p_oClonedView.setWidth(this.width);
		p_oClonedView.setId(this.id);
		p_oClonedView.setPosX(this.posX);
		p_oClonedView.setUserInteractionEnabled(this.userInteractionEnabled);
		p_oClonedView.setPosY(this.posY);
		p_oClonedView.setPropertyName(this.propertyName);
		p_oClonedView.setLocalization(this.getLocalization()) ;

	}
	

	/**
	 * This height includes the height of the associated label if it is displayed
	 * @return total height of the view
	 */
	public int getTotalHeight(){
		return this.getHeight();
	}
}
