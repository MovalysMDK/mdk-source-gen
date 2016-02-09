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

import com.a2a.adjava.uml2xmodele.extractors.SearchDialogExtractor;
import com.a2a.adjava.xmodele.MDialog;
import com.a2a.adjava.xmodele.MLayout;
import com.a2a.adjava.xmodele.MScreen;


/**
 * Extractor for search dialogs on ios platform
 * @author Jean-Danoie l 
 *
 */
public class IOSSearchDialogExtractor extends SearchDialogExtractor {

	/**
	 * Add validation buttons
	 * @param p_oSearchLayout layout to complete
	 * @param p_sBaseName base name for button
	 */
	protected void addValidationButtons(MLayout p_oSearchLayout, String p_sBaseName) {
		// nothing to do on this platform
	}

	/**
	 * Add filter buttons
	 * @param p_oDialog dialog to add filter button to
	 * @param p_oListScreen screen hosting the dialog
	 */
	protected void addFilterButton(MDialog p_oDialog, MScreen p_oListScreen) {
		// nothing to do on this platform
	}
}
