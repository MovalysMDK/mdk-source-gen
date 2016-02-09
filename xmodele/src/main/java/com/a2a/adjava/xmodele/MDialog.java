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

import com.a2a.adjava.uml.UmlClass;

/**
 * @author lmichenaud
 *
 */
public class MDialog extends MPage {

	/**
	 * Entity of viewmodel
	 */
	private MEntityImpl entity ;
	
	/**
	 * Viewmodel creator
	 */
	private MViewModelCreator viewModelCreator ;
	
	/**
	 * Constructor
	 * @param p_sType ignored attribute (always dialog)
	 * @param p_sUmlName uml name
	 * @param p_sName name
	 * @param p_oPackage package
	 */
	public MDialog( MScreen p_oScreen, String p_sDialogName, UmlClass p_oUmlPage, MPackage p_oPackage,
			MViewModelInterface p_oVm, MViewModelImpl p_oVmImpl, MEntityImpl p_oEntity,
			MViewModelCreator p_oViewModelCreator ) {
		super(p_oScreen, "dialog", p_sDialogName, p_oUmlPage, p_oPackage, p_oVmImpl, false);
		this.entity = p_oEntity ;
		this.viewModelCreator = p_oViewModelCreator ;
	}
		
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void toXmlInsertBeforeDocumentation(Element p_xElement) {
		super.toXmlInsertBeforeDocumentation(p_xElement);
		p_xElement.element("screenname").setName("dialogname");
		p_xElement.add(this.entity.toXml());
		Element xCreator = p_xElement.addElement("viewmodel-creator");
		xCreator.addElement("name").setText(this.viewModelCreator.getName());
		xCreator.addElement("full-name").setText(this.viewModelCreator.getFullName());
		
		p_xElement.add(this.getLayout().toXml());
	}
}
