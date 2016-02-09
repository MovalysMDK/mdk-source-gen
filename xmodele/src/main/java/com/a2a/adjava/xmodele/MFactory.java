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

import java.beans.Introspector;

import org.dom4j.Element;

import com.a2a.adjava.utils.StrUtils;


/**
 * 
 * <p>TODO Décrire la classe MFactory</p>
 *
 * <p>Copyright (c) 2009
 * <p>Company: Adeuza
 *
 * @author mmadigand
 * @author lmichenaud
 *
 */
public class MFactory extends SClass<MFactoryInterface, MMethodSignature> {

	private MEntityImpl mEntity ;
	private MEntityInterface mEntityInterface;
	
	/**
	 * @param p_oUmlClass
	 * @param p_sTableName
	 */
	public MFactory( String p_sName, MPackage p_oPackage, MEntityInterface p_oInterface, MEntityImpl p_oMClass) {
		super("pojo-factory", null, p_sName, p_oPackage);
		this.mEntity = p_oMClass;
		this.mEntityInterface = p_oInterface;
		this.setBeanName(Introspector.decapitalize(this.getName()));
	}
	
	@Override
	protected void toXmlInsertBeforeDocumentation(Element p_xElement) {
		super.toXmlInsertBeforeDocumentation(p_xElement);
		Element r_xFactoryInterface = p_xElement.addElement("pojo-factory-interface");
		r_xFactoryInterface.addElement("name").setText(this.getMasterInterface().getName());
		r_xFactoryInterface.addElement("bean-name").setText(this.getMasterInterface().getBeanName());
		
		p_xElement.add(this.mEntity.toXml());
		
		Element xInterface = p_xElement.addElement("interface");
		xInterface.addAttribute("name", mEntityInterface.getName());
		xInterface.addElement("import").setText(mEntityInterface.getPackage().getFullName().concat(StrUtils.DOT_S).concat(mEntityInterface.getName()));
	}
}