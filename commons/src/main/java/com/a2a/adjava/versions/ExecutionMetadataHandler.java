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
package com.a2a.adjava.versions;


/**
 * Classe utilitaire qui permet de récupérer des informations à partir de l'objet 
 * ExecutionMetadataList.
 * @author qLagarde
 *
 */
public class ExecutionMetadataHandler {
	
	/**
	 * Renvoie le numéro de MDK (ou numéro de mf4mdd) de la précédente génération.
	 * @param p_oLastGenerationDescriptor L'historique des précédentes générations
	 * @return Le numéro de version de la précédente génération.
	 */
	public static String getLastGenerationVersion(GenerationDescriptor p_oLastGenerationDescriptor) {
		String sLastGenerationVersion = null;
		
		for(ExecutionMetadata oExecutionMetadata :  p_oLastGenerationDescriptor.getExecutionMetadataList().getInfoSaveBeanList()) {
			if(oExecutionMetadata.getExecutionId().equalsIgnoreCase("generate")) {
				RuntimeDependency oRuntimeDependency = ExecutionMetadataHandler.getMf4mddDependency(oExecutionMetadata);
				if(oRuntimeDependency != null) {
					sLastGenerationVersion =  oRuntimeDependency.getVersion();
					break;
				}
			}
		}
		return sLastGenerationVersion;
	}
	
	/**
	 * Recupère la dépendance indiquant la version de génération : il s'agit de la dépendance "mf4mdd" car c'est elle qui porte le numéro de MDK
	 * @param oExecutionMetadata Un objet Metadata de la génération en cours
	 * @return La dépendance RuntimeDependency correspondant à "mf4mdd"
	 */
	private static RuntimeDependency getMf4mddDependency(ExecutionMetadata oExecutionMetadata) {
		for(RuntimeDependency oDependency : oExecutionMetadata.getRuntimeDependencys()) {
			if(oDependency.getArtifactId().startsWith("mf4mdd-")) {
				return oDependency;
			}
		}
		return null;
	}

}
