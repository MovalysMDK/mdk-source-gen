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
package com.a2a.adjava.uml2xmodele.extractors;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.extractors.ExtractorParams.ParameterName;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.uml.UmlUsage;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MScreen;
import com.a2a.adjava.xmodele.ui.menu.MMenu;
import com.a2a.adjava.xmodele.ui.menu.MMenuDef;
import com.a2a.adjava.xmodele.ui.menu.MMenuItem;
import com.a2a.adjava.xmodele.ui.navigation.MNavigation;
import com.a2a.adjava.xmodele.ui.navigation.MNavigationType;

public class MenuExtractor extends AbstractExtractor<IDomain<IModelDictionary,IModelFactory>> {

	/** Logger */
	private static final Logger log = LoggerFactory.getLogger(MenuExtractor.class);

	/**
	 * MenuExtractor parameter
	 */
	private enum Parameters implements ParameterName {
		S_USAGE_NAVIGATIONMENU("stereotype-usage-navigation-menu");

		private String parameterName = null;

		private Parameters(String p_sParametersName) {
			parameterName = p_sParametersName;
		}

		@Override
		public String getParameterName() {
			return parameterName;
		}
	}

	/**
	 * Stereotype list indicating that a UmlClass is a view model of the domain.
	 */
	private List<String> listStereotypes = new ArrayList<String>();

	@Override
	public void initialize(Element p_xConfig) throws Exception {
		String sStereotypes = getParameters().getValue("stereotypes");
		for (String sStereotype : sStereotypes.split(",")) {
			sStereotype = sStereotype.trim();
			this.listStereotypes.add(sStereotype);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void extract(UmlModel p_oModele) throws Exception {
		extractMenuDefs(p_oModele.getDictionnary());
		extractMenu(p_oModele.getDictionnary());
	}

	/**
	 * @param oUmlDict
	 */
	private void extractMenuDefs(UmlDictionary oUmlDict) throws Exception {

		// Loop over uml classes
		for (UmlClass oUmlClass : oUmlDict.getAllClasses()) {

			// If UmlClass is a menu
			if (isMenu(oUmlClass)) {

				// Instantiate menu
				MMenuDef oMenuDef = this.getDomain().getXModeleFactory().createMenuDef(oUmlClass.getName());

				this.getDomain().getDictionnary().registerMenuDef(oMenuDef);

				// Loop over menu items
				for (UmlUsage oMenuItemUsage : oUmlClass.getUsages()) {

					MScreen oScreenEnd = this.getDomain().getDictionnary()
							.getScreen(oMenuItemUsage.getSupplier().getName());

					// Create menu item
					MMenuItem oMenuItem = this.getDomain().getXModeleFactory().createMenuItem();
					MNavigation oMenuNav = getDomain().getXModeleFactory().createNavigation(
						"menu-navigation", MNavigationType.NAVIGATION_MENU, null, oScreenEnd);
					oMenuItem.setNavigation(oMenuNav);
					oMenuDef.addMenuItem(oMenuItem);
				}
			}
		}
	}

	/**
	 * Permet d'extraire le Menu de l'application.
	 * @param p_oUmlDict la liste des classes du model
	 * @throws Exception
	 */
	private void extractMenu(UmlDictionary p_oUmlDict) throws Exception {

		ScreenExtractor oScreenExtractor = getDomain().getExtractor(ScreenExtractor.class);

		// Loop over screen
		for (UmlClass oUmlClass : oScreenExtractor.getScreenContext().getScreenUmlClasses(p_oUmlDict)) {

			MScreen oClientScreen = this.getDomain().getDictionnary().getScreen(oUmlClass.getName());

			if (oClientScreen == null) {
				throw new AdjavaException("Can't find screen {}", oUmlClass.getFullName());
			}

			// Loop over UmlUsage
			for (UmlUsage oUsage : oUmlClass.getUsages()) {

				// If navigation menu usage
				if (oUsage.hasStereotype(this.getParameterValue(Parameters.S_USAGE_NAVIGATIONMENU))) {

					if (oUsage.getName() != null && !oUsage.getName().isEmpty()) {

						MMenuDef oMenuDef = this.getDomain().getDictionnary()
								.getMenuDef(oUsage.getSupplier().getName());
						MMenu oMenu = oMenuDef.createMenu(oUsage.getName(), oClientScreen, getDomain().getXModeleFactory());
						oClientScreen.addMenu(oMenu);

					} else {
						MessageHandler.getInstance().addError(
								"Navigation menu must be name between screen " + oUsage.getClient().getName()
										+ " and screen " + oUsage.getSupplier().getName());
					}
				}
			}
		}
	}

	/**
	 * Return true if uml class is of type ViewModel for the domain
	 * 
	 * @param p_oUmlClass UmlClass
	 * @return true if uml class is of type Entity
	 */
	public boolean isMenu(UmlClass p_oUmlClass) {
		return p_oUmlClass.hasAnyStereotype(this.listStereotypes);
	}
}
