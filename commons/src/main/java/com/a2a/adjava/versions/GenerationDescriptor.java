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

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Objet qui représente une génération
 * @author qLagarde
 * @since 6.4
 */
@XmlRootElement(name="generation") 
@XmlAccessorType(XmlAccessType.FIELD) 
public class GenerationDescriptor {
	
	public static String MAJOR_VERSION_ATTRIBUTE_NAME = "major-version";
	public static String MINOR_VERSION_ATTRIBUTE_NAME = "minor-version";
	public static String REVISION_NUMBER_ATTRIBUTE_NAME = "revision-number";
	public static String FIRST_GENERATION_ATTRIBUTE_VALUE = "first-generation";


	/**
	 * Constructeur vide
	 */
	public GenerationDescriptor() {
		super();
		DateFormat oDateFormatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.US);
		this.date = oDateFormatter.format(new Date());
		this.executionMetadataList = new ExecutionMetadataList();
		this.projectUpgraderList = new ProjectUpgraderDescriptorList(); 
	}

	/**
	 * La date de la génération
	 */
	@XmlID 
	@XmlAttribute(name="date") 
	private String date;

	/**
	 * La liste des métadonnées d'exécutions
	 */
	@XmlElement(name="info-save-bean-list") 
	private ExecutionMetadataList executionMetadataList;

	/**
	 * La liste des project-upgraders déja appliqués sur ce projet
	 */
	@XmlElement(name="applied-project-upgraders") 
	private ProjectUpgraderDescriptorList projectUpgraderList;

	/**
	 * Retourne l'objet de liste des project-upgraders
	 * @return l'objet de liste des project-upgraders
	 */ 
	public ProjectUpgraderDescriptorList getProjectUpgraderList() {
		return projectUpgraderList;
	}

	/**
	 * Met à jour l'objet de liste des project-upgraders
	 * @param projectUpgraderList l'objet de liste des project-upgraders
	 */
	public void setProjectUpgraderList(
			ProjectUpgraderDescriptorList projectUpgraderList) {
		this.projectUpgraderList = projectUpgraderList;
	}

	/**
	 * Retourne la date de la génération
	 * @return La date de la génération 
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Met à jour la date de la génération
	 * @param date La date de la génération
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Retourne la liste des métadonnées d'exécution pour cette génération
	 * @return La liste des métadonnées d'exécution pour cette génération
	 */
	public ExecutionMetadataList getExecutionMetadataList() {
		return executionMetadataList;
	}

	/**
	 * Met à jour la liste des métadonnées d'exécution pour cette génération
	 * @param executionMetadataList la liste des métadonnées d'exécution pour cette génération
	 */
	public void setExecutionMetadataList(ExecutionMetadataList executionMetadataList) {
		this.executionMetadataList = executionMetadataList;
	}
	
	public void addProjectUpgraderDescriptor(Map<String, ?> p_oProjectUpgraderParameters) {
		ProjectUpgraderDescriptor oProjectUpgraderDescriptor = new ProjectUpgraderDescriptor();
		oProjectUpgraderDescriptor.setMajorVersion((String) p_oProjectUpgraderParameters.get(MAJOR_VERSION_ATTRIBUTE_NAME));
		oProjectUpgraderDescriptor.setMinorVersion((String) p_oProjectUpgraderParameters.get(MINOR_VERSION_ATTRIBUTE_NAME));
		oProjectUpgraderDescriptor.setRevisionNumber((String) p_oProjectUpgraderParameters.get(REVISION_NUMBER_ATTRIBUTE_NAME));
		this.projectUpgraderList.getProjectUpgraders().add(oProjectUpgraderDescriptor);
	}

}
