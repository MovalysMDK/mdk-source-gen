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

import org.dom4j.DocumentHelper;
import org.dom4j.Element;


/**
 * 
 * <p>TODO Décrire la classe MDao</p>
 *
 * <p>Copyright (c) 2009
 * <p>Company: Adeuza
 *
 * @author mmadigand
 *
 */
public class MDaoExtends extends SPackagedElement {

	private String beanName ;
	private MPackage mpackage ;
	private MDaoImpl mdao ;

	
	/**
	 * @param p_sName
	 * @param p_mPackage
	 * @param p_oClass
	 * @param p_oInterface
	 */
	public MDaoExtends( String p_sName, String p_sBeanName, MDaoImpl p_oMDao ) {
		super("dao-extends", null, p_sName, p_oMDao.getPackage());
		this.beanName = p_sBeanName;
		this.mdao = p_oMDao;
	}
	
	/**
	 * @return
	 */
	public String getBeanName() {
		return beanName;
	}	

	@Override
	protected void toXmlInsertAfterName(Element p_xElement) {
		super.toXmlInsertAfterName(p_xElement);
		p_xElement.addElement("bean-name").setText(this.beanName);
	}
	
	
	@Override
	protected void toXmlInsertBeforeImport(Element p_xElement) {
		super.toXmlInsertBeforeImport(p_xElement);
		p_xElement.add( mdao.toXml());
		Element r_xDaoInterface = DocumentHelper.createElement("dao-interface");
		r_xDaoInterface.addElement("name").setText(this.mdao.getMasterInterface().getName());
		p_xElement.add(r_xDaoInterface);
		this.addImport(this.mdao.getMasterInterface().getFullName());
	}	
}
