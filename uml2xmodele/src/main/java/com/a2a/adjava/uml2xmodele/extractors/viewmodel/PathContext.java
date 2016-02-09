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
package com.a2a.adjava.uml2xmodele.extractors.viewmodel;

import org.apache.commons.lang3.StringUtils;

import com.a2a.adjava.utils.StrUtils;

/**
 * Context for browsing a path.
 * Path format is a.b.c....
 * @author lmichenaud
 *
 */
public class PathContext {

	/**
	 * Path depth
	 */
	private int index = 0 ;
	
	/**
	 * Full path to browse
	 */
	private String fullPath;
	
	/**
	 * Path elements.
	 * Elements are splitted by DOT in full path
	 */
	private String[] pathElements ;
	
	/**
	 * Current browsing path
	 */
	private String currentPath ;
	
	/**
	 * Old path
	 */
	private String oldPath = StringUtils.EMPTY ;
	
	/**
	 * @param currentPath
	 */
	public PathContext(String p_sFullPath) {
		super();
		this.fullPath = p_sFullPath;
		this.pathElements = p_sFullPath.split("\\.");
		this.currentPath = this.pathElements[this.index];
	}

	/**
	 * Move to next element in full path
	 */
	public void moveNext() {
		this.oldPath = this.currentPath;
		this.index++;
		this.currentPath =  this.oldPath + StrUtils.DOT + this.pathElements[this.index];
	}
	
	/**
	 * Return true if next element is last
	 * @return
	 */
	public boolean isNextLastElement() {
		return (this.index + 1) == getNbElements() -1 ;
	}
	
	public String getPathWithNoPoint(int p_iStart, int p_iEnd) {
		String sPath = StringUtils.EMPTY;
		for(int i=p_iStart;i<=p_iEnd;i++) {
			sPath = sPath + this.pathElements[i];
		}
		return sPath;
	}
	
	/**
	 * @return
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @return
	 */
	public String getCurrentPath() {
		return currentPath;
	}
	
	
	/**
	 * @return
	 */
	public String getCurrentElement() {
		return this.pathElements[this.index];
	}
	

	/**
	 * @return
	 */
	public boolean isLastElement() {
		return this.index == getNbElements() -1 ;
	}
	
	/**
	 * @return
	 */
	public String getOldPath() {
		return this.oldPath;
	}

	/**
	 * Return number of tokens in full path
	 * @return
	 */
	public int getNbElements() {
		return this.pathElements.length;
	}

	/**
	 * @return
	 */
	public String getFullPath() {
		return this.fullPath;
	}
}
