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
package com.a2a.adjava.languages.html5.xmodele;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.a2a.adjava.xmodele.ModelDictionary;

public class MH5Dictionary extends ModelDictionary {

	/**
	 * Story boards
	 */
	private Map<String, MH5View> views = new HashMap<>();
	
	/**
	 * Register a View with its name
	 * @param p_oMH5View View object
	 */
	public void registerMH5View( MH5View p_oMH5View ) {
		this.views.put(p_oMH5View.getName(), p_oMH5View);
	}
	
	/**
	 * Return all View objects
	 * @return all XIB container
	 */
	public Collection<MH5View> getAllMH5Views() {
		Map<String, MH5View> treeMapViews = new TreeMap<String, MH5View>(this.views);
		return treeMapViews.values();
	}
	
	/**
	 * Return a view from its name
	 * @return the view corresponding to this name
	 */
	public MH5View getMH5ViewFromName(String p_sName) {
		return this.views.get(p_sName);
	}
}
