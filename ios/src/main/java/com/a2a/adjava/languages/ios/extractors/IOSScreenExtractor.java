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
package com.a2a.adjava.languages.ios.extractors;

import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.uml2xmodele.extractors.ScreenExtractor;
import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.MScreen;
import com.a2a.adjava.xmodele.MStereotype;

/**
 * Screen extractor for iOs platform
 */
public class IOSScreenExtractor extends ScreenExtractor {

	/**
	 * iOs uml stereotype for single screen controller
	 */
	public static final String MM_IOS_SINGLE_CONTROLLER = "Mm_iosSingleController";

	@Override
	public void extract(UmlModel p_oModele) throws Exception {
		super.extract(p_oModele);
	}
	
	/**
	 * Creates an ios scene from uml
	 * @param p_oScreenUmlClass the uml scene
	 * @return the ios scene created
	 * @throws Exception exception
	 */
	@Override
	protected MScreen createScreen( UmlClass p_oScreenUmlClass ) throws Exception {
		MScreen r_oScreen = super.createScreen(p_oScreenUmlClass);
		
		if (p_oScreenUmlClass.hasStereotype(MM_IOS_SINGLE_CONTROLLER)) {
			r_oScreen.addStereotype(new MStereotype(MM_IOS_SINGLE_CONTROLLER, ""));
		}
		
		return r_oScreen;
	}
}
