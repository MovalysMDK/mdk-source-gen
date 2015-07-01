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
 * <p>
 * 	Repr��sentation d'un commentaire HTML dans Movalys Framework.
 * </p>
 *
 * <p>Copyright (c) 2015</p>
 * <p>Company: Adeuza</p>
 *
 * @author lbrunelliere
 */
public class MComment extends SGeneratedElement {
	
	/**
	 * Name Screen
	 */
	private String nameScreen = null;

	/**
	 * Comment
	 */
	private String documentation = null;
	
	/**
	 * Constructeur
	 * @param p_sName nom court de la classe
	 * @param p_sFullName nom complet
	 */
	public MComment( String p_sName, String p_sFullName ) {
		super("comment", null, p_sName);
		this.setFullName(p_sFullName);
	}
	
	
	public void setNameScreen(String p_sNameScreen) {
		this.nameScreen = p_sNameScreen;
	}

	public void setComment(String p_sDocumentation) {
		this.documentation = p_sDocumentation;
	}

	@Override
	public Element toXml() {
		Element r_xElem = DocumentHelper.createElement("comment");
		r_xElem.addAttribute("name", this.getName() );
		r_xElem.addAttribute("full-name", this.getFullName() );
		if (documentation!=null) {
			r_xElem.addElement("documentation").setText(this.documentation);
		}
		return r_xElem ;
	}
}
