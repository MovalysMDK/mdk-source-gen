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
 * 
 * <p>TODO Décrire la classe MFactoryInterface</p>
 *
 * <p>Copyright (c) 2009
 * <p>Company: Adeuza
 *
 * @author mmadigand
 * @author lmichenaud
 *
 */
public class MFactoryInterface extends SInterface {

	private String entityInterfaceName ;
	private String entityInterfaceImport ;
	private String entityImplName ;
	private String entityImplImport ;

	/**
	 * @param p_oUmlClass
	 * @param p_sTableName
	 */
	public MFactoryInterface( String p_sName, MPackage p_oPackage, MEntityInterface p_oEntityInterface, MEntityImpl p_oEntityClass) {
		super("pojo-factory-interface", null, p_sName, p_oPackage);
		this.entityInterfaceName = p_oEntityInterface.getName();
		this.entityInterfaceImport = p_oEntityInterface.getPackage().getFullName().concat(StrUtils.DOT_S).concat(p_oEntityInterface.getName());
		this.entityImplName = p_oEntityClass.getName();
		this.entityImplImport = p_oEntityClass.getPackage().getFullName().concat(StrUtils.DOT_S).concat(p_oEntityClass.getName());
	}
	
	@Override
	protected void toXmlInsertBeforeDocumentation(Element p_xElement) {
		super.toXmlInsertBeforeDocumentation(p_xElement);
		Element xInterface = p_xElement.addElement("interface");
		xInterface.addAttribute("name", this.entityInterfaceName);
		xInterface.addElement("import").setText(this.entityInterfaceImport);
	}
}