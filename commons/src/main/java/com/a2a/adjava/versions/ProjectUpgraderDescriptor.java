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
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="project-upgrader") 
@XmlAccessorType(XmlAccessType.FIELD) 
public class ProjectUpgraderDescriptor {

	/**
	 * La version majeure
	 */
    @XmlID 
    @XmlAttribute(name="majorVersion") 
	private String majorVersion;
    
	/**
	 * La version mineure
	 */
    @XmlID 
    @XmlAttribute(name="minorVersion") 
	private String minorVersion;
    
	/**
	 * Le numéro de révision
	 */
    @XmlID 
    @XmlAttribute(name="revisionNumber") 
	private String revisionNumber;
    
	/**
	 * La date d'application du project-upgrader
	 */
    @XmlID 
    @XmlAttribute(name="date") 
	private String date;

    /**
     * Constructeur
     */
	public ProjectUpgraderDescriptor() {
		DateFormat oDateFormatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.US);
		this.date = oDateFormatter.format(new Date());
	}

	/**
	 * Retourne la version majeure
	 * @return la version majeure
	 */
	public String getMajorVersion() {
		return majorVersion;
	}

	/**
	 * Met à jour la version majeure
	 * @param majorVersion la version majeure
	 */
	public void setMajorVersion(String majorVersion) {
		this.majorVersion = majorVersion;
	}

	/**
	 * Retourne la version mineure
	 * @return la version mineure
	 */
	public String getMinorVersion() {
		return minorVersion;
	}

	/**
	 * Met à jour la version mineure
	 * @param minorVersion la version mineure
	 */
	public void setMinorVersion(String minorVersion) {
		this.minorVersion = minorVersion;
	}

	/**
	 * Retourne le numéro de révison
	 * @return le numéro de révision
	 */
	public String getRevisionNumber() {
		return revisionNumber;
	}

	/**
	 * Met à jour le numéro de révision
	 * @param revisionNumber le numéro de révision
	 */
	public void setRevisionNumber(String revisionNumber) {
		this.revisionNumber = revisionNumber;
	}

	/**
	 * Retourne la date de construction du descripteur
	 * @return la date de construction de ce descripteur
	 */
	public String getDate() {
		return date;
	}
	
	/**
	 * Permet de définir les 3 paramètres principaux (majorVersion, minorVersio, revisionNumber) depuis la
	 * Map des paramètres issues d'un ProjectUpgrader
	 * @param p_oVersionParameters
	 */
	public void setParametersFromMap(Map<String,?> p_oVersionParameters) {
		this.majorVersion = (String) p_oVersionParameters.get(GenerationDescriptor.MAJOR_VERSION_ATTRIBUTE_NAME);
		this.minorVersion = (String) p_oVersionParameters.get(GenerationDescriptor.MINOR_VERSION_ATTRIBUTE_NAME);
		this.revisionNumber = (String) p_oVersionParameters.get(GenerationDescriptor.REVISION_NUMBER_ATTRIBUTE_NAME);
	}

	@Override
	public boolean equals(Object p_oObject) {
		super.equals(p_oObject);
		if(p_oObject != null && (p_oObject instanceof ProjectUpgraderDescriptor)) {
			ProjectUpgraderDescriptor oProjectUpgraderDescriptor = (ProjectUpgraderDescriptor)p_oObject;
			return (this.minorVersion.equalsIgnoreCase(oProjectUpgraderDescriptor.getMinorVersion()) &&
					this.majorVersion.equalsIgnoreCase(oProjectUpgraderDescriptor.getMajorVersion()) &&
					this.revisionNumber.equalsIgnoreCase(oProjectUpgraderDescriptor.getRevisionNumber()));
		}
		return false;
		
	}
	
    
}
