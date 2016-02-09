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
package com.a2a.adjava.projectupgrader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;

/**
 * <p>MUpdater abstrait.</p>
 *
 * <p>Copyright (c) 2014</p>
 * <p>Company: Adeuza</p>
 *
 * @since 6.4
 * @author qlagarde
 *
 */
public abstract class AbstractProjectUpgrader implements ProjectUpgrader {
	
	private static String DOMAIN_ANDROID_NAME = "mbandroid";
	private static String DOMAIN_IOS_NAME = "ios";


	/**
	 * Liste des projectUpgraders similaires
	 */
	private List<String> similarProjectUpgraders = null;

	/**
	 * Map des parametres
	 */
	private Map<String,?> parametersMap = null;
	
	/**
	 * Mode du projectUpgrader
	 */
	private ProjectUpgraderMode mode = null;

	/**
	 * {@inheritDoc}
	 * @see ProjectUpgrader#getParametersMap()
	 */
	public Map<String, ?> getParametersMap() {
		return this.parametersMap;
	}

	/**
	 * {@inheritDoc}
	 * @see ProjectUpgrader#setParametersMap(Map)
	 */
	public void setParametersMap(Map<String, ?> p_oParametersMap) {
		this.parametersMap = p_oParametersMap;
	}

	/**
	 * {@inheritDoc}
	 * @see ProjectUpgrader#getSimilarProjectUpgraders(List<String>)
	 */
	public List<String> getSimilarProjectUpgraders() {
		return (this.similarProjectUpgraders == null) ? new ArrayList<String>() : this.similarProjectUpgraders;
	}

	/**
	 * {@inheritDoc}
	 * @see ProjectUpgrader#setSimilarProjectUpgraders(List<String>)
	 */
	public void setSimilarProjectUpgraders(List<String> similarProjectUpgraders) {
		this.similarProjectUpgraders = similarProjectUpgraders;
	}	

	@Override
	public void execute(IDomain<IModelDictionary, IModelFactory> p_oDomain,
			Map<String, ?> p_oGlobalSession) throws Exception {
		switch(p_oDomain.getName()) {
			case "mbandroid":
				executeFixesForAndroid(p_oDomain, p_oGlobalSession);
				break;
			case "ios":
				executeFixesForIOS(p_oDomain, p_oGlobalSession);
				break;
		}
	}

	public ProjectUpgraderMode getMode() {
		return mode;
	}

	public void setMode(ProjectUpgraderMode p_oProjectUpgraderMode) {
		this.mode = p_oProjectUpgraderMode;
	}
	


}
