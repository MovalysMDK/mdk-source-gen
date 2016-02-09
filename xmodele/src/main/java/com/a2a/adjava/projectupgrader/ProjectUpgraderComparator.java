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
package com.a2a.adjava.projectupgrader;

import java.util.Comparator;

import com.a2a.adjava.utils.VersionHandler;

/**
 * Permet de comparer 2 objets ProjectUpgraderComparator en fonction de leur numéro de version de destination
 * @author qLagarde
 * @since 6.4
 */
public class ProjectUpgraderComparator implements Comparator<ProjectUpgrader> {

	/**
	 * Le nom de l'attribut XML qui indique le numéro de version majeure du MDK
	 */
	private static String MAJOR_VERSION_ATTRIBUTE_NAME = "major-version";

	/**
	 * Le nom de l'attribut XML qui indique le numéro de version mineure du MDK
	 */
	private static String MINOR_VERSION_ATTRIBUTE_NAME = "minor-version";

	/**
	 * Le nom de l'attribut XML qui indique le numéro de révision du MDK
	 */
	private static String REVISION_NUMBER_ATTRIBUTE_NAME = "revision-number";

	@Override
	public int compare(ProjectUpgrader p_oProjectUpdater1, ProjectUpgrader p_oProjectUpdater2) {		
		Integer majorVersion1 = Integer.parseInt((String) p_oProjectUpdater1.getParametersMap().get(MAJOR_VERSION_ATTRIBUTE_NAME));
		Integer majorVersion2 = Integer.parseInt((String) p_oProjectUpdater2.getParametersMap().get(MAJOR_VERSION_ATTRIBUTE_NAME));

		Integer minorVersion1 = Integer.parseInt((String) p_oProjectUpdater1.getParametersMap().get(MINOR_VERSION_ATTRIBUTE_NAME));
		Integer minorVersion2 = Integer.parseInt((String) p_oProjectUpdater2.getParametersMap().get(MINOR_VERSION_ATTRIBUTE_NAME));

		Integer revisionNumber1 = Integer.parseInt((String) p_oProjectUpdater1.getParametersMap().get(REVISION_NUMBER_ATTRIBUTE_NAME));
		Integer revisionNumber2 = Integer.parseInt((String) p_oProjectUpdater2.getParametersMap().get(REVISION_NUMBER_ATTRIBUTE_NAME));
		
		return compareVersions(majorVersion1, majorVersion2, minorVersion1, minorVersion2, revisionNumber1, revisionNumber2);
	}

	/**
	 * Compare deux versions du MDK
	 * @param p_iMajorVersion1 Major version 1
	 * @param p_iMajorVersion2 Major version 2
	 * @param p_iMinorVersion1 Minor version 1
	 * @param p_iMinorVersion2 Minor version 2
	 * @param p_iRevisionNumber1 Revision number 1
	 * @param p_iRevisionNumber2 Revision number 2
	 * @return -1 si la version du MDK1 est inférieure à celle du MDK2, 0 si elles sont égales, 1 sinon.
	 */
	public static int compareVersions(Integer p_iMajorVersion1, Integer p_iMajorVersion2, 
			Integer p_iMinorVersion1, Integer p_iMinorVersion2,
			Integer p_iRevisionNumber1, Integer p_iRevisionNumber2) {

		if(p_iMajorVersion1.compareTo(p_iMajorVersion2) == 0) {
			if(p_iMinorVersion1.compareTo(p_iMinorVersion2) == 0) {
				return p_iRevisionNumber1.compareTo(p_iRevisionNumber2);
			}
			else {
				return p_iMinorVersion1.compareTo(p_iMinorVersion2);
			}
		}
		else {
			return p_iMajorVersion1.compareTo(p_iMajorVersion2);
		}
	}

	/**
	 * Indique si le project upgrader passé en paramètre est inférieure à la derniere version de génération
	 * @param p_oProjectUpgrarder Le project upgrader
	 * @return true si le projectUpgrader est pour une version antérieure à la dernière génération
	 */
	public boolean isAnteriorToLastGeneration(ProjectUpgrader p_oProjectUpgrarder) {
		Integer majorVersionUpgrader = Integer.parseInt((String) p_oProjectUpgrarder.getParametersMap().get(MAJOR_VERSION_ATTRIBUTE_NAME));
		Integer minorVersionUpgrader = Integer.parseInt((String) p_oProjectUpgrarder.getParametersMap().get(MINOR_VERSION_ATTRIBUTE_NAME));
		Integer revisionNumberUpgrader = Integer.parseInt((String) p_oProjectUpgrarder.getParametersMap().get(REVISION_NUMBER_ATTRIBUTE_NAME));

		String[] lastGenerationVersionComponents = VersionHandler.getLastMDKGenerationVersion().replace("-SNAPSHOT", "").split("\\.");
		Integer majorVersionLastGeneration = Integer.parseInt(lastGenerationVersionComponents[0]);
		Integer minorVersionLastGeneration = Integer.parseInt(lastGenerationVersionComponents[1]);
		Integer revisionNumberLastGeneration = Integer.parseInt(lastGenerationVersionComponents[2]);
		
		return (compareVersions(majorVersionUpgrader, majorVersionLastGeneration,
				minorVersionUpgrader, minorVersionLastGeneration,
				revisionNumberUpgrader, revisionNumberLastGeneration) <= 0);	
	}
}
