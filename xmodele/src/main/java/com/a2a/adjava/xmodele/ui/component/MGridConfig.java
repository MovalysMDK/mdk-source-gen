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
package com.a2a.adjava.xmodele.ui.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author lmichenaud
 *
 */
public class MGridConfig {

	/**
	 * Options parameter for detail page
	 */
	public static final String OPTIONS_PARAMETER = "grid-options-parameter";
	
	/**
	 * Column index parameter
	 */
	public static final String COLUMN_PARAMETER = "grid-column-parameter";
	
	/**
	 * Section index parameter
	 */
	public static final String SECTION_PARAMETER = "grid-section-parameter";
	
	/**
	 * Caractère de séparation pour le paramètrage des pages
	 */
	protected static final String SEPARATOR = "#";
	
	/**
	 * Column prefix
	 */
	private static final String COLUMN_PREFIX = "c";
	
	/**
	 * Section prefix
	 */
	private static final String SECTION_PREFIX = "s";
	
	/**
	 * 
	 */
	private boolean singleColumn = false ;
	
	/**
	 * 
	 */
	public Map<Integer, List<MGridSectionConfig>> computeGrid( List<MGridSectionConfig> p_listSectionConfigs ) {
		
		Map<Integer, List<MGridSectionConfig>> r_oGrid = new LinkedHashMap<Integer, List<MGridSectionConfig>>();
		
		// Sort the list of details
		Collections.sort(p_listSectionConfigs, new MGridPositionComparator());
				
		// Dispatch the details into the grid
		int iCurrentColumn = 1;
		MGridSectionConfig lastSectionConfig = null;
		for (MGridSectionConfig oCurrentSectionConfig : p_listSectionConfigs) {
			if (lastSectionConfig == null) {
				List<MGridSectionConfig> listSections = new ArrayList<MGridSectionConfig>();
				listSections.add(oCurrentSectionConfig);
				r_oGrid.put(iCurrentColumn, listSections);
			} else {
				if (lastSectionConfig.getPosition().getColumn() == Integer.MAX_VALUE
						|| lastSectionConfig.getPosition().getColumn() < oCurrentSectionConfig
								.getPosition().getColumn()) {
					iCurrentColumn++;
					List<MGridSectionConfig> listSections = new ArrayList<MGridSectionConfig>();
					listSections.add(oCurrentSectionConfig);
					r_oGrid.put(iCurrentColumn, listSections);
				} else {
					List<MGridSectionConfig> listSections = r_oGrid
							.get(iCurrentColumn);
					listSections.add(oCurrentSectionConfig);
				}
			}
			lastSectionConfig = oCurrentSectionConfig;
		}

		// Recompute position for each details of the grid
		iCurrentColumn = 1;
		for (Entry<Integer, List<MGridSectionConfig>> listColumns : r_oGrid.entrySet()) {
			int iCurrentSection = 1;
			for (MGridSectionConfig oSectionConfig : listColumns.getValue()) {
				oSectionConfig.getPosition().setColumn(iCurrentColumn);
				oSectionConfig.getPosition().setSection(iCurrentSection);
				oSectionConfig.getPage().addParameter(COLUMN_PARAMETER, Integer.toString(iCurrentColumn));
				oSectionConfig.getPage().addParameter(SECTION_PARAMETER, Integer.toString(iCurrentSection));
				iCurrentSection++;
			}
			iCurrentColumn++;
		}
		
		return r_oGrid;
	}
	
	/**
	 * @param p_bSingleColumn
	 */
	public void setSingleColumn(boolean p_bSingleColumn) {
		this.singleColumn = p_bSingleColumn;
	}

	/**
	 * @param p_mapGrid
	 * @return
	 */
	protected Element toXml( Map<Integer, List<MGridSectionConfig>> p_mapGrid ) {
		Element r_xGrid = DocumentHelper.createElement("managment-details");
		for (Entry<Integer, List<MGridSectionConfig>> listColumns : p_mapGrid
				.entrySet()) {
			Element r_xColumn = r_xGrid.addElement("column");
			r_xColumn.addAttribute("pos",
					Integer.toString(listColumns.getKey()));
			for (MGridSectionConfig oSectionConfig : listColumns.getValue()) {
				Element xDetail = r_xColumn.addElement("managment-detail");
				xDetail.addAttribute("section", Integer
						.toString(oSectionConfig.getPosition().getSection()));
				
				String sDetailValue = "";
				if ( ! this.singleColumn ) {
					sDetailValue = StringUtils.<Object> join(COLUMN_PREFIX, oSectionConfig.getPosition().getColumn());
				}
				
				xDetail.setText( StringUtils.<Object> join(
						sDetailValue, 
						SECTION_PREFIX,
						oSectionConfig.getPosition().getSection(),
						SEPARATOR,
						oSectionConfig.getVmPath(), SEPARATOR, oSectionConfig.getLayout()));
			}
		}
		return r_xGrid;
	}
}
