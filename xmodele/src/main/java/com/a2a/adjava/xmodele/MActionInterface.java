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
package com.a2a.adjava.xmodele;

import org.dom4j.Element;

import com.a2a.adjava.utils.StrUtils;

/**
 * Action interface
 * 
 * @author lmichenaud
 *
 */
public class MActionInterface extends SInterface {

	/**
	 * Class of in parameter
	 */
	private String inClass ;
	
	/**
	 * Class of out parameter
	 */
	private String outClass ;
	
	/**
	 * Class of step parameter
	 */
	private String stepClass ;
	
	/**
	 * Class of progress parameter
	 */
	private String progressClass ;
	
	/**
	 * 
	 */
	private boolean isRoot = false;
	
	/**
	 * Action type id (from config)
	 */
	private MActionType actionType ;
	
	/**
	 * 
	 */
	private MEntityImpl entity ;
	
	/**
	 * @return
	 */
	public String getInClass() {
		return inClass;
	}

	/**
	 * @return
	 */
	public String getOutClass() {
		return outClass;
	}

	/**
	 * @return
	 */
	public String getStepClass() {
		return stepClass;
	}

	/**
	 * @return
	 */
	public String getProgressClass() {
		return progressClass;
	}

	/**
	 * @return
	 */
	public MActionType getActionType() {
		return actionType;
	}

	/**
	 * @param p_sActionTypeId
	 */
	protected void setActionType(MActionType p_oActionType) {
		this.actionType = p_oActionType;
	}

	/**
	 * @param p_sName
	 * @param p_bRoot
	 * @param p_oPackage
	 * @param p_sInNameClass
	 * @param p_sOutNameClass
	 * @param p_sStepClass
	 * @param p_sProgressClass
	 */
	public MActionInterface(String p_sName, boolean p_bRoot, MPackage p_oPackage, String p_sInNameClass, String p_sOutNameClass,
			String p_sStepClass, String p_sProgressClass, MEntityImpl p_oEntity, MActionType p_oActionType ) {
		super("action-interface", null, p_sName, p_oPackage);
		this.addImport(p_sInNameClass);
		this.addImport(p_sOutNameClass);
		this.addImport(p_sStepClass);
		this.addImport(p_sProgressClass);
		this.inClass = p_sInNameClass;
		this.outClass = p_sOutNameClass;
		this.stepClass = p_sStepClass;
		this.progressClass = p_sProgressClass;
		this.isRoot = p_bRoot;
		this.entity = p_oEntity ;
		this.actionType = p_oActionType ;
	}
	
	/**
	 * Retourne l'objet entity
	 * @return Objet entity
	 */
	public MEntityImpl getEntity() {
		return this.entity;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.a2a.adjava.xmodele.SWithMethodElement#toXmlInsertBeforeDocumentation(org.dom4j.Element)
	 */
	@Override
	protected void toXmlInsertBeforeDocumentation(Element p_xElement) {
		super.toXmlInsertBeforeDocumentation(p_xElement);
		if ( this.inClass != null ) {
			p_xElement.addElement("in").setText(StrUtils.substringAfterLastDot(this.inClass));
		}
		if ( this.outClass != null ) {
			p_xElement.addElement("out").setText(StrUtils.substringAfterLastDot(this.outClass));
		}
		if ( this.stepClass != null ) {
			p_xElement.addElement("step").setText(StrUtils.substringAfterLastDot(this.stepClass));
		}
		if ( this.progressClass != null ) {
			p_xElement.addElement("progress").setText(StrUtils.substringAfterLastDot(this.progressClass));
		}
		p_xElement.addElement("action-type").setText(this.actionType.name());
		if (isRoot) {
			p_xElement.addElement("root");
		}
		
		if (this.getEntity() != null) {
			p_xElement.add(this.getEntity().toXml());
		}
	}
}
