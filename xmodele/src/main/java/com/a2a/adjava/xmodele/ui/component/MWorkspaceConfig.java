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
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.xmodele.MPage;

/**
 * MWorkspace config
 * 
 * @author lmichenaud
 * 
 */
public class MWorkspaceConfig extends MGridConfig {

	/**
	 * 
	 */
	public static final String VISUALFIELD_PARAMETER = "workspace-config";

	/**
	 * Workspace panel type
	 */
	public static final String PANELTYPE_PARAMETER = "workspace-panel-type";

	/**
	 * Master panel type
	 */
	public static final String MASTER_PANELTYPE = "master";

	/**
	 * Detail panel type
	 */
	public static final String DETAIL_PANELTYPE = "detail";
	
	/**
	 * Main config prefix
	 */
	private static final String MAIN_PREFIX = "main";

	/**
	 * Tab config prefix
	 */
	private static final String TAB_PREFIX = "tab";

	/**
	 * Main config
	 * Optional
	 */
	private String mainConfig;

	/**
	 * Main layout
	 * Optional
	 */
	private String mainLayout;

	/**
	 * Tab Configs for master column
	 * Optional
	 */
	private List<MGridSectionConfig> tabConfigs = new ArrayList<MGridSectionConfig>();

	/**
	 * Detail configs for details columns
	 */
	private List<MGridSectionConfig> detailConfigs = new ArrayList<MGridSectionConfig>();

	/**
	 * Detail Grid 
	 */
	private Map<Integer, List<MGridSectionConfig>> detailGrid ;
	
	/**
	 * Tab Grid 
	 * Optional
	 */
	private Map<Integer, List<MGridSectionConfig>> tabGrid ;

	private String viewModelGetter;

	private MPage mainPage;
	
	/**
	 * 
	 */
	public MWorkspaceConfig() {
		
	}
	
	/**
	 * Add tab config
	 * 
	 * @param p_sConfig
	 */
	public void addTabConfig(String p_sViewModelGetter, String p_sLayoutName, MGridPosition p_oGridPosition, MPage p_oPage) throws Exception {	
		MGridSectionConfig oMWKSSectionConfig = new MGridSectionConfig();
		oMWKSSectionConfig.setVmPath(p_sViewModelGetter);
		oMWKSSectionConfig.setLayout(p_sLayoutName);
		oMWKSSectionConfig.setPage(p_oPage);
		oMWKSSectionConfig.setPosition(p_oGridPosition);
		this.tabConfigs.add(oMWKSSectionConfig);
	}

	/**
	 * Add a detail column config
	 * @param p_sViewModelGetter
	 * @param p_sLayoutName
	 * @param p_oMWKSPosition
	 * @param p_oPage
	 */
	public void addDetailConfig(String p_sViewModelGetter, String p_sLayoutName, MGridPosition p_oMWKSPosition, MPage p_oPage) {
		MGridSectionConfig oMWKSDetailConfig = new MGridSectionConfig();
		oMWKSDetailConfig.setVmPath(p_sViewModelGetter);
		oMWKSDetailConfig.setLayout(p_sLayoutName);
		oMWKSDetailConfig.setPosition(p_oMWKSPosition);
		oMWKSDetailConfig.setPage(p_oPage);
		this.detailConfigs.add(oMWKSDetailConfig);
	}

	/**
	 * @return
	 */
	public String getMainConfig() {
		return mainConfig;
	}

	/**
	 * 
	 * @return
	 */
	public String getViewModelGetter() {
		return viewModelGetter;
	}
	
	/**
	 * @param p_sMainConfig
	 */
	public void setMainConfig(String p_sViewModelGetter, String p_sLayoutName) {
		this.mainConfig = StringUtils.join(MAIN_PREFIX, SEPARATOR,
				p_sViewModelGetter, SEPARATOR, p_sLayoutName);
		this.viewModelGetter = p_sViewModelGetter;
		this.mainLayout = p_sLayoutName;
	}

	/**
	 * @return
	 */
	public List<MGridSectionConfig> getTabConfigs() {
		return this.tabConfigs;
	}

	/**
	 * @return
	 */
	public List<MGridSectionConfig> getDetailConfigs() {
		return this.detailConfigs;
	}

	/**
	 * @return
	 */
	public String getMainLayout() {
		return mainLayout;
	}

	/**
	 * @return
	 */
	public boolean hasTabs() {
		return !this.tabConfigs.isEmpty();
	}

	/**
	 * 
	 */
	public void computeGrids() {
		this.detailGrid = computeGrid(this.detailConfigs);
		this.tabGrid = computeGrid(this.tabConfigs);
		this.checkTabGrid();
	}
	
	/**
	 * 
	 */
	private void checkTabGrid() {
		for( List<MGridSectionConfig> listSections : this.tabGrid.values()) {
			if ( listSections.size() > 1 ) {
				MessageHandler.getInstance().addError("Sections are forbidden in the master part of a workspace.");
			}
		}
	}

	/**
	 * @return
	 */
	public Element toXml() {
		Element r_xWksConfig = DocumentHelper.createElement("workspace-config");
		if ( this.mainConfig != null ) {
			r_xWksConfig.addElement("managment-main").setText(this.mainConfig);
			r_xWksConfig.addElement("tab-count").setText(
				Integer.toString(this.tabConfigs.size()));
			
			int iPos = 1;
			for (Entry<Integer, List<MGridSectionConfig>> listColumns : this.tabGrid.entrySet()) {
				// only one section allowed in a tab
				MGridSectionConfig oTabConfig = listColumns.getValue().get(0);
				Element xTab = r_xWksConfig.addElement("managment-tab");
				xTab.addAttribute("pos", Integer.toString(iPos));
				xTab.setText(StringUtils.<Object> join(TAB_PREFIX, iPos, SEPARATOR, oTabConfig.getVmPath(),
					SEPARATOR, oTabConfig.getLayout()));
				iPos++;
			}
		}
		
		r_xWksConfig.addElement("detail-count").setText(
				Integer.toString(this.detailConfigs.size()));
		r_xWksConfig.add( this.toXml(this.detailGrid));
		
		return r_xWksConfig;
	}

	public MPage getMainPage() {
		return mainPage;
	}
	
	public void setMainPage(MPage masterPage) {
		this.mainPage = masterPage;
	}
}
