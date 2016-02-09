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
package com.a2a.adjava.languages.android.xmodele;

import org.dom4j.Element;

import com.a2a.adjava.xmodele.MPackage;
import com.a2a.adjava.xmodele.MScreen;
import com.a2a.adjava.xmodele.ui.menu.MMenu;

/**
 * @author lmichenaud
 * Temporary class: in future, will be replaced by MAndroidActivity
 */
public class MAndroidScreen extends MScreen {

	/**
	 * @param p_sUmlName uml name
	 * @param p_sName screen name
	 * @param p_oPackage screen package
	 */
	protected MAndroidScreen(String p_sUmlName, String p_sName, MPackage p_oPackage) {
		super(p_sUmlName, p_sName, p_oPackage);
	}

	/** 
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.MScreen#toXmlInsertBeforeDocumentation(org.dom4j.Element)
	 */
	protected void toXmlInsertBeforeDocumentation(Element p_xElement) {
		super.toXmlInsertBeforeDocumentation(p_xElement);
		MMenu oOptionMenu = this.getMenu("options");
		if ( oOptionMenu != null ) {
			Element xOptionMenu = oOptionMenu.toXml();
			xOptionMenu.setName("options-menu");
			xOptionMenu.attribute("id").setText(this.getName().toLowerCase() + "_options");
			p_xElement.add(xOptionMenu);
		}
	}
}
