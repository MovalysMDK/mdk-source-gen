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
package com.a2a.adjava.utils;

import com.a2a.adjava.AdjavaException;

public class VersionHandler {

	/**
	 * Version of generation
	 */
	private static ItfVersion generationVersion;

	/**
	 * Version of MDK associated to the lase generation
	 */
	private static String lastMDKGenerationVersion;
	
	/**
	 * Version of MDK associated to the current generation
	 */
	private static String currentMDKGenerationVersion;
	
	public VersionHandler(String p_sVersion, Class<ItfVersion> p_oVersionImplementationClass) throws AdjavaException {
		
		for (ItfVersion eVersion : p_oVersionImplementationClass.getEnumConstants()) {
			
			if (eVersion.getStringVersion().equalsIgnoreCase(p_sVersion)) {
				generationVersion = eVersion;
				break;
			}
			
		}
		if (generationVersion == null) {
			throw new AdjavaException("no version matches {} in Version implementation {}", p_sVersion, p_oVersionImplementationClass.getName());
		}
	}

	/**
	 * Return the generation version
	 * @return
	 */
	public static ItfVersion getGenerationVersion() {
		return generationVersion;
	}

	/**
	 * Returns the version of MDK associated to the last generation
	 * @return the version of MDK associated to the last generation
	 */
	public static String getLastMDKGenerationVersion() {
		return lastMDKGenerationVersion;
	}

	/**
	 * Set the version of MDK assiociated to the last generation
	 * @param lastMDKGenerationVersion The version of MDK assiociated to the last generation
	 */
	public static void setLastMDKGenerationVersion(
			String lastMDKGenerationVersion) {
		VersionHandler.lastMDKGenerationVersion = lastMDKGenerationVersion;
	}

	/**
	 * Returns the version of MDK associated to the current generation
	 * @return the version of MDK associated to the current generation
	 */
	public static String getCurrentMDKGenerationVersion() {
		return currentMDKGenerationVersion;
	}

	/**
	 * Set the version of MDK assiociated to the current generation
	 * @param lastMDKGenerationVersion The version of MDK assiociated to the current generation
	 */
	public static void setCurrentMDKGenerationVersion(
			String currentMDKGenerationVersion) {
		VersionHandler.currentMDKGenerationVersion = currentMDKGenerationVersion;
	}
	
}
