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
package com.a2a.adjava.mupdater;

import java.util.Map;

/**
 * <p>MUpdater abstrait.</p>
 *
 * <p>Copyright (c) 2009</p>
 * <p>Company: Adeuza</p>
 *
 * @since 2.5
 * @author mmadigand
 *
 */
public abstract class AbstractMUpdater implements MUpdater {

	/**
	 * Map des parametres
	 */
	private Map<String,String> parametersMap = null;

	/**
	 * {@inheritDoc}
	 * @see MUpdater#getParametersMap()
	 */
	public Map<String, String> getParametersMap() {
		return this.parametersMap;
	}

	/**
	 * {@inheritDoc}
	 * @see MUpdater#setParametersMap(Map)
	 */
	public void setParametersMap(Map<String, String> p_oParametersMap) {
		this.parametersMap = p_oParametersMap;
	}	
	
}
