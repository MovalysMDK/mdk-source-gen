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

/**
 * Position of detail panel in a workspace.
 * 
 * @author lmichenaud
 * 
 */
public class MGridPosition {

	/**
	 * Column index (start at 1)
	 */
	private int column = Integer.MAX_VALUE;

	/**
	 * Section index (start at 1)
	 */
	private int section = Integer.MAX_VALUE;

	/**
	 * @return
	 */
	public int getColumn() {
		return this.column;
	}

	/**
	 * @param p_iColumn
	 */
	public void setColumn(int p_iColumn) {
		this.column = p_iColumn;
	}

	/**
	 * @return
	 */
	public int getSection() {
		return section;
	}

	/**
	 * @param p_iSection
	 */
	public void setSection(int p_iSection) {
		this.section = p_iSection;
	}
}