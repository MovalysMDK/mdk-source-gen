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
 * <p>TODO Décrire la classe MStereotype</p>
 *
 * <p>Copyright (c) 2009
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */

public class MStereotype {

	private String name ;
	private String documentation ;
	/**
	 * TODO Décrire le constructeur MStereotype
	 * @param p_sStereotype
	 */
	public MStereotype( String p_sStereotype, String p_sDocumentation) {
		this.name = p_sStereotype ;
		this.documentation = p_sDocumentation;
	}
	
	/**
	 * Retourne l'objet name
	 * @return Objet name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Affecte l'objet name 
	 * @param p_oName Objet name
	 */
	public void setName(String p_sName) {
		this.name = p_sName;
	}
	
	/**
	 * TODO Décrire la méthode toXml de la classe MStereotype
	 * @return
	 */
	public Element toXml() {
		Element r_xStereotype = DocumentHelper.createElement("stereotype");
		r_xStereotype.addAttribute("name", this.name);
		if(!"".equals(this.documentation)){
			r_xStereotype.addElement("documentation").setText(this.documentation);
		}
		return r_xStereotype ;
	}
}
