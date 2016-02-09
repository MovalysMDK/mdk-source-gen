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
package com.a2a.adjava.optionsetters;

import java.util.Map;

/**
 * <p>Définit les caractéristiques de l'attribut à partir des options par défault et du modèle</p>
 *
 * <p>Copyright (c) 2009
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 * @param <T> type d'objet auquel s'applique l'option
 */

public abstract class AbstractOptionSetter<T> implements OptionSetter<T> {

	/**
	 * Map des paramètres
	 */
	private Map<String,String> parameters = null;
	
	/**
	 * {@inheritDoc}
	 * @see MUpdater#getParametersMap()
	 */
	public Map<String, String> getParameters() {
		return this.parameters;
	}

	/**
	 * {@inheritDoc}
	 * @see MUpdater#setParametersMap(Map)
	 */
	public void setParametersMap(Map<String, String> p_mapParameters) {
		this.parameters = p_mapParameters;
	}
}
