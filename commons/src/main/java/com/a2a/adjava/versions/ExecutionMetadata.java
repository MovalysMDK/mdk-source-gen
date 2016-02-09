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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>Métadonnées d'une éxécution.</p>
 *
 * <p>Copyright (c) 2010</p>
 * <p>Company: Adeuza</p>
 *
 * @author mmadigand
 *
 */
@XmlRootElement(name="info-save-bean") 
@XmlAccessorType(XmlAccessType.FIELD) 
public class ExecutionMetadata {

	/**
	 * L'id de l'éxécution
	 */
    @XmlID 
    @XmlAttribute(name="executionId") 
	private String executionId;
	
	/**
	 * Liste des dépendances des éxécutions
	 */
	 @XmlElement(name="dependency") 
	private List<RuntimeDependency> runtimeDependencys;

	/**
	 * Constructeur vide
	 */
	public ExecutionMetadata() {
		super();
		this.executionId = "";
		this.runtimeDependencys = new ArrayList<RuntimeDependency>();
	}

	/**
	 * Retourne l'id de l'éxécution
	 * 
	 * @return l'id de l'éxécution
	 */
	public String getExecutionId() {
		return this.executionId;
	}

	/**
	 * Affecte l'id de l'éxécution
	 * 
	 * @param p_sExecutionId l'id de l'éxécution
	 */
	public void setExecutionId(String p_sExecutionId) {
		this.executionId = p_sExecutionId;
	}

	/**
	 * Retourne la liste des dépendances des éxécutions
	 * 
	 * @return la liste des dépendances des éxécutions
	 */
	public List<RuntimeDependency> getRuntimeDependencys() {
		return this.runtimeDependencys;
	}

	/**
	 * Affecte la liste des dépendances des éxécutions
	 * 
	 * @param p_listRuntimeDependency la liste des dépendances des éxécutions
	 */
	public void setRuntimeDependencys(List<RuntimeDependency> p_listRuntimeDependency) {
		this.runtimeDependencys = p_listRuntimeDependency;
	}


}
