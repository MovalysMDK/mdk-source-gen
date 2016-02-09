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

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.utils.StrUtils;

public class MDaoBeanRef {

	private String name ;
	private ITypeDescription typeDesc ;
	private String parameterName ;
	private MDaoInterface daoInterface ;
	
	/**
	 * @param p_sName
	 * @param p_sTypeDescription
	 */
	public MDaoBeanRef( String p_sName, ITypeDescription p_sTypeDescription,
			String p_sParameterName, MDaoInterface p_oMDaoInterface ) {
		this.name = p_sName ;
		this.typeDesc = p_sTypeDescription ;
		this.parameterName = p_sParameterName ;
		this.daoInterface = p_oMDaoInterface ;
	}
	
	/**
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return
	 */
	public ITypeDescription getTypeDesc() {
		return this.typeDesc;
	}
	
	/**
	 * @return
	 */
	public String getParameterName() {
		return parameterName;
	}

	/**
	 * @return
	 */
	public Element toXml() {
		Element r_xAssociation = DocumentHelper.createElement("dao-bean-ref");
		r_xAssociation.addAttribute("name", this.name);
		r_xAssociation.addAttribute("type-name", this.typeDesc.getName());
		r_xAssociation.addAttribute("type-short-name", this.typeDesc.getShortName());

		r_xAssociation.addElement("get-accessor").setText("get" + StringUtils.capitalize(this.name));
		r_xAssociation.addElement("set-accessor").setText("set" + StringUtils.capitalize(this.name));
		r_xAssociation.addElement("parameter-name").setText(this.parameterName);

		if ( this.daoInterface.getMEntityImpl().getFactoryInterface() != null ) {
			Element xFactory = r_xAssociation.addElement("pojo-factory-interface");
			xFactory.addElement("name").setText(this.daoInterface.getMEntityImpl().getFactoryInterface().getName());
			xFactory.addElement("bean-name").setText(this.daoInterface.getMEntityImpl().getFactoryInterface().getBeanName());
			xFactory.addElement("import").setText(this.daoInterface.getMEntityImpl().getFactoryInterface().getPackage().getFullName().concat(StrUtils.DOT_S)
					.concat(this.daoInterface.getMEntityImpl().getFactoryInterface().getName()));
		}
		
		return r_xAssociation;
	}
}
