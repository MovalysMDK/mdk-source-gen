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
package com.a2a.adjava.generator.core.fiximport;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.a2a.adjava.utils.StrUtils;

/**
 * @author lmichenaud
 *
 */
public class TypeVisitorResult {

	/**
	 * Package of class
	 */
	private String packageName;

	/**
	 * Current path like package.Class.InnerClass1
	 */
	private String currentPath;

	/**
	 * Declared types
	 */
	private List<String> types = new ArrayList<String>();
	
	/**
	 * Used imports
	 */
	private List<String> imports = new ArrayList<String>();

	/**
	 * @return
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @param p_sPackageName
	 */
	public void setPackageName(String p_sPackageName) {
		this.packageName = p_sPackageName;
		this.currentPath = p_sPackageName;
	}

	/**
	 * @return
	 */
	public String getCurrentPath() {
		return currentPath;
	}

	/**
	 * @param p_sCurrentPath
	 */
	public void setCurrentPath(String p_sCurrentPath) {
		this.currentPath = p_sCurrentPath;
	}

	/**
	 * 
	 */
	public void addType(String p_sSimpleName) {
		this.types.add(this.currentPath + StrUtils.DOT + p_sSimpleName);
	}
	
	/**
	 * @param p_sName
	 */
	public void appendPath( String p_sName ) {
		this.currentPath += StrUtils.DOT + p_sName ;
	}
	
	/**
	 * 
	 */
	public void popPath() {
		this.currentPath = StringUtils.substringBeforeLast(this.currentPath, StrUtils.DOT_S);
	}

	/**
	 * @return
	 */
	public List<String> getTypes() {
		return types;
	}

	/**
	 * @return
	 */
	public List<String> getImports() {
		return imports;
	}
	
	/**
	 * @param p_sImport
	 */
	public void addImport( String p_sImport ) {
		this.imports.add( p_sImport );
	}
}
