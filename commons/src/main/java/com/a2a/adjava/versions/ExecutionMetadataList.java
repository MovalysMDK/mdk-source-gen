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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>Liste de métadonnées des éxécutions.</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author mmadigand
 *
 */
@XmlRootElement(name="info-save-bean-list") 
@XmlAccessorType(XmlAccessType.FIELD) 
public class ExecutionMetadataList {

	/**
	 * Liste de métadonnées des éxécutions
	 */
	 @XmlElement(name="info-save-bean") 
	private List<ExecutionMetadata> infoSaveBeanList;

	/**
	 * Constructeur vide
	 */
	public ExecutionMetadataList() {
		super();
		this.infoSaveBeanList = new ArrayList<ExecutionMetadata>();
	}

	/**
	 * Retourne la liste de métadonnées des éxécutions
	 * 
	 * @return la liste de métadonnées des éxécutions
	 */
	public List<ExecutionMetadata> getInfoSaveBeanList() {
		return this.infoSaveBeanList;
	}

	/**
	 * Affecte la liste de métadonnées des éxécutions
	 * 
	 * @param p_oInfoSaveBeanList la liste de métadonnées des éxécutions
	 */
	public void setInfoSaveBeanList(List<ExecutionMetadata> p_oInfoSaveBeanList) {
		this.infoSaveBeanList = p_oInfoSaveBeanList;
	}
}
