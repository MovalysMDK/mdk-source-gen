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
 * La liste des project-upgraders déjà appliqué sur le projet en cours de génération
 * @author qLagarde
 * @since 6.4
 *
 */
@XmlRootElement(name="applied-project-upgraders") 
@XmlAccessorType(XmlAccessType.FIELD) 
public class ProjectUpgraderDescriptorList {

	public ProjectUpgraderDescriptorList() {
		this.projectUpgraders = new ArrayList<ProjectUpgraderDescriptor>();
	}

	/**
	 * Liste des projecs-upgraders déja appliqués sur ce projet
	 */
	@XmlElement(name="project-upgrader") 
	private List<ProjectUpgraderDescriptor> projectUpgraders;

	/**
	 * Retourne la liste des projecs-upgraders déjas appliqués sur ce projet
	 * @return la liste des projecs-upgraders déjas appliqués sur ce projet
	 */
	public List<ProjectUpgraderDescriptor> getProjectUpgraders() {
		return projectUpgraders;
	}

}
