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

import com.a2a.adjava.languages.java.xmodele.JXModeleFactory;
import com.a2a.adjava.xmodele.MPackage;
import com.a2a.adjava.xmodele.MScreen;
import com.a2a.adjava.xmodele.XModele;
import com.a2a.adjava.xmodele.ui.menu.MMenuItem;

/**
 * @author lmichenaud
 *
 */
public class MAndroidModeleFactory extends JXModeleFactory {

	/**
	 * @return
	 */
	@Override
	public XModele<?> createXModele() {
		return new XModele<MAndroidDictionnary>( new MAndroidDictionnary());
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.XModeleFactory#createScreen(java.lang.String, java.lang.String, com.a2a.adjava.xmodele.MPackage)
	 */
	@Override
	public MScreen createScreen(String p_sUmlName, String p_sName, MPackage p_oScreenPackage) {
		return new MAndroidScreen(p_sUmlName, p_sName, p_oScreenPackage);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.XModeleFactory#createMenuItem()
	 */
	@Override
	public MMenuItem createMenuItem() {
		return new MAndroidMenuItem();
	}
}
