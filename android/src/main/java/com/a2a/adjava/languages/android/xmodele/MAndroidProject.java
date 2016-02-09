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
package com.a2a.adjava.languages.android.xmodele;

import org.apache.commons.io.FilenameUtils;

import com.a2a.adjava.xmodele.XProject;

/**
 * Android project
 */
public class MAndroidProject<D extends MAndroidDomain<? extends MAndroidDictionnary, ? extends MAndroidModeleFactory>>  extends XProject<D>{

	/**
	 * Raw directory
	 */
	private static final String RAW_DIRECTORY = "raw";
	
	/**
	 * Raw directory
	 */
	private static final String MENU_DIRECTORY = "menu";
	
	/**
	 * Android resource directories
	 */
	private String resDir ;
	
	/**
	 * Android Manifest file
	 */
	private String androidManifestFile;

	/**
	 * Res directory
	 * @return res directory
	 */
	public String getResDir() {
		return resDir;
	}

	/**
	 * Set res directory
	 * @param p_sResDir res directory
	 */
	public void setResDir(String p_sResDir) {
		this.resDir = p_sResDir;
	}

	/**
	 * Get android manifest file
	 * @return android manifest file
	 */
	public String getAndroidManifestFile() {
		return androidManifestFile;
	}

	/**
	 * Set android manifest file
	 * @param p_sAndroidManifestFile android manifest file
	 */
	public void setAndroidManifestFile(String p_sAndroidManifestFile) {
		this.androidManifestFile = p_sAndroidManifestFile;
	}

	/**
	 * Return raw directory
	 * @return raw directory
	 */
	public String getRawDirectory() {
		return FilenameUtils.concat(resDir, RAW_DIRECTORY);
	}

	/**
	 * Return menu directory
	 * @return menu directory
	 */
	public String getMenuDir() {
		return FilenameUtils.concat(resDir, MENU_DIRECTORY);
	}
}
