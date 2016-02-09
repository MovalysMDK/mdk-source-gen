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

import com.a2a.adjava.messages.MessageHandler;

/**
 * Gère l'historique des générations
 * @author qLagarde
 * @since 6.4
 *
 */
@XmlRootElement(name="generation-history") 
@XmlAccessorType(XmlAccessType.FIELD) 
public final class GenerationHistory {

		
	/**
	 * Liste des générations
	 */
	 @XmlElement(name="generation") 
	private List<GenerationDescriptor> generations;

	public GenerationHistory() {
		this.generations = new ArrayList<>();
	}


	 /**
	  * Retourne la liste des anciennes générations
	  * @return
	  */
	public List<GenerationDescriptor> getGenerations() {
		return generations;
	}

	/**
	 * Met à jour l'historique des générations
	 * @param generations
	 */
	public void setGenerations(List<GenerationDescriptor> generations) {
		this.generations = generations;
	}

}
