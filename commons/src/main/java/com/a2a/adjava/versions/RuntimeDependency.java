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
 * <p>Dependance d'une éxécution</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author mmadigand
 *
 */
public class RuntimeDependency {

	/**
	 * Artifact Id
	 */
	private String artifactId;
	
	/**
	 * Version
	 */
	private String version;

	/**
	 * Date du dernier update
	 */
	private String lastUpdated;	
	
	/**
	 * Constructeur vide
	 */
	public RuntimeDependency() {
		super();
	}

	/**
	 * Constructeur avec les paramètres
	 * 
	 * @param p_sArtifactId Artifact Id
	 * @param p_sVersion Version
	 * @param p_sLastUpdated Date du dernier update
	 */
	public RuntimeDependency(String p_sArtifactId, String p_sVersion, String p_sLastUpdated) {
		super();
		this.artifactId = p_sArtifactId;
		this.version = p_sVersion;
		this.lastUpdated = p_sLastUpdated;
	}


	/**
	 * Retourne l'Artifact Id
	 * 
	 * @return l'Artifact Id
	 */
	public String getArtifactId() {
		return this.artifactId;
	}

	/**
	 * Affecte l'Artifact Id
	 * 
	 * @param p_sArtifactId l'Artifact Id
	 */
	public void setArtifactId(String p_sArtifactId) {
		this.artifactId = p_sArtifactId;
	}

	/**
	 * Retourne la Version
	 * 
	 * @return la Version
	 */
	public String getVersion() {
		return this.version;
	}

	/**
	 * Affecte la Version
	 * 
	 * @param p_sVersion la Version
	 */
	public void setVersion(String p_sVersion) {
		this.version = p_sVersion;
	}

	/**
	 * Retourne la date du dernier update
	 * 
	 * @return la date du dernier update
	 */
	public String getLastUpdated() {
		return this.lastUpdated;
	}

	/**
	 * Affecte la date du dernier update
	 * 
	 * @param p_sLastUpdated la date du dernier update
	 */
	public void setLastUpdated(String p_sLastUpdated) {
		this.lastUpdated = p_sLastUpdated;
	}
	
}
