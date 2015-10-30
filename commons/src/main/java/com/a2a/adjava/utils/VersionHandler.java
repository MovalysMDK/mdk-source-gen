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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;

public class VersionHandler {

	private static final Logger log = LoggerFactory.getLogger(VersionHandler.class);
	
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
	
	/**
	 * Version of the widgets used on the project
	 */
	private static ItfWidget widgetVariant;
	
	public VersionHandler(String p_sVersion, Class<ItfVersion> p_oVersionImplementationClass,
			String p_sWidget, String p_oWidgetImplementationClass) throws AdjavaException {
		
		for (ItfVersion eVersion : p_oVersionImplementationClass.getEnumConstants()) {
			
			if (eVersion.getStringVersion().equalsIgnoreCase(p_sVersion)) {
				generationVersion = eVersion;
				break;
			}
			
		}
		if (generationVersion == null) {
			throw new AdjavaException("no version matches {} in Version implementation {}", p_sVersion, p_oVersionImplementationClass.getName());
		}
		
		if (p_sWidget!= null && p_oWidgetImplementationClass != null) {
			try {
				Class<ItfWidget> oWidgetClass = (Class<ItfWidget>) Class.forName(p_oWidgetImplementationClass);
				
				for (ItfWidget eWidget : oWidgetClass.getEnumConstants()) {
					
					if (eWidget.getStringWidget().equalsIgnoreCase(p_sWidget)) {
						widgetVariant = eWidget;
						break;
					}
					
				}
				if (widgetVariant == null) {
					throw new AdjavaException("no widget matches {} in Widget implementation {}", p_sWidget, oWidgetClass.getName());
				}
			} catch (ClassNotFoundException e) {
				log.error("Class {} not found", p_oWidgetImplementationClass);
			}
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
	
	/**
	 * Returns the variant of the widgets used on the generated project.
	 * @return the variant of the widgets
	 */
	public static ItfWidget getWidgetVariant() {
		return widgetVariant;
	}
	
}
