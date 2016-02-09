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
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.a2a.adjava.xmodele.MPage;

/**
 * MWorkspace config
 * 
 * @author lmichenaud
 * 
 */
public class MMultiPanelConfig extends MGridConfig {

	/**
	 * Visual Field parameter
	 */
	public static final String VISUALFIELD_PARAMETER = "multipanel-config";

	/**
	 * Detail configs for details columns
	 */
	private List<MGridSectionConfig> sectionConfigs = new ArrayList<MGridSectionConfig>();
	
	/**
	 * Section Grid 
	 */
	private Map<Integer, List<MGridSectionConfig>> sectionGrid ;
	
	/**
	 * Multipanel
	 */
	public MMultiPanelConfig() {
		super.setSingleColumn(Boolean.TRUE);
	}
	
	/**
	 * Add a detail column config
	 * @param p_sViewModelGetter
	 * @param p_sLayoutName
	 * @param p_oGridPosition
	 * @param p_oPage
	 */
	public void addSectionConfig(String p_sViewModelGetter, String p_sLayoutName, MGridPosition p_oGridPosition, MPage p_oPage) {
		
		MGridSectionConfig oSectionConfig = new MGridSectionConfig();
		oSectionConfig.setVmPath(p_sViewModelGetter);
		oSectionConfig.setLayout(p_sLayoutName);
		oSectionConfig.setPosition(p_oGridPosition);
		oSectionConfig.setPage(p_oPage);
		
		this.sectionConfigs.add(oSectionConfig);
	}

	/**
	 * @return
	 */
	public List<MGridSectionConfig> getSectionConfigs() {
		return this.sectionConfigs;
	}

	/**
	 * 
	 */
	public void computeGrids() {
		this.sectionGrid = this.computeGrid(this.sectionConfigs);
	}

	/**
	 * @return
	 */
	public Element toXml() {
		Element r_xWksConfig = DocumentHelper.createElement(VISUALFIELD_PARAMETER);
		Element xGrid = this.toXml(this.sectionGrid);
		r_xWksConfig.add(xGrid);
		return r_xWksConfig;
	}
}
