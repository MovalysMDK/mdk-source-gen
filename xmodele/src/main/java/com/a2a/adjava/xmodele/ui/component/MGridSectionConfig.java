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

import com.a2a.adjava.xmodele.MPage;

/**
 * @author lmichenaud
 * 
 */
public class MGridSectionConfig {

	/**
	 * VM Path
	 */
	private String vmPath ;
	
	/**
	 * Layout
	 */
	private String layout;

	/**
	 * Position
	 */
	private MGridPosition position;

	/**
	 * Detail page
	 */
	private MPage page;

	/**
	 * @return
	 */
	public String getLayout() {
		return this.layout;
	}

	/**
	 * @return
	 */
	public MPage getPage() {
		return this.page ;
	}
	
	/**
	 * @param p_oPage
	 */
	public void setPage(MPage p_oPage) {
		this.page = p_oPage ;
	}

	/**
	 * @param p_sConfig
	 */
	public void setLayout(String p_sLayout) {
		this.layout = p_sLayout;
	}

	/**
	 * @return
	 */
	public String getVmPath() {
		return vmPath;
	}

	/**
	 * @param p_sVmPAth
	 */
	public void setVmPath(String p_sVmPath) {
		this.vmPath = p_sVmPath;
	}

	/**
	 * @return
	 */
	public MGridPosition getPosition() {
		return position;
	}

	/**
	 * @param p_oPosition
	 */
	public void setPosition(MGridPosition p_oPosition) {
		this.position = p_oPosition;
	}
}