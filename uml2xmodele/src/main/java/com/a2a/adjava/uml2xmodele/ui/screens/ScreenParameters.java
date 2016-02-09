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
package com.a2a.adjava.uml2xmodele.ui.screens;

import com.a2a.adjava.extractors.ExtractorParams.ParameterName;

/**
 * <p>Énumération représentant la liste des énumérations utilisables dans la modélisation UML d'un projet.</p>
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @since MF-Annapurna
 */
public enum ScreenParameters implements ParameterName {

	/** stéréotype pour définir un screen */
	S_SCREEN("stereotype-screen"),
	/** stéréotype pour définir l'écran principal de l'application */
	S_SCREENROOT("stereotype-screenroot"),
	/** stereotype to define a search screen */
	S_SEARCHSCREEN("stereotype-searchscreen"),
	/** stéréotype pour définir un panel de type liste à 1 niveau */
	S_PANELLIST1("stereotype-panellist1"),
	/** stéréotype pour définir un panel de type liste à 2 niveau */
	S_PANELLIST2("stereotype-panellist2"),
	/** stéréotype pour définir un panel de type liste à 2 niveau mais en mode multiselection */
	S_OPTIONMULTISELECT("stereotype-option-multi-select"),
	/** stéréotype pour définir un panel de type liste à 3 niveau */
	S_PANELLIST3("stereotype-panellist3"),
	/** stéréotype pour définir un panel */
	S_AGGREGATION_PANEL("stereotype-aggregation-panel"),
	/** stéréotype pour définir un composant de type workspace */
	S_AGGREGATION_PANEL_WORKSPACE("stereotype-aggregation-panel-workspace"),
	/**
	 * stéréotype pour définir un panel faisant partie d'un composant détail
	 * dans un workspace
	 */
	S_USAGE_NAVIGATIONDETAIL("stereotype-usage-navigation-detail"),
	/** stéréotype pour définir une navigation entre écran */
	S_USAGE_NAVIGATION("stereotype-usage-navigation");

	/** le nom du paramètre */
	private String parameterName = null;

	/**
	 * Construit l'un des élément de l'énumération.
	 * @param p_sParametersName le nom du paramètre
	 */
	private ScreenParameters(String p_sParametersName) {
		parameterName = p_sParametersName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getParameterName() {
		return parameterName;
	}

}
