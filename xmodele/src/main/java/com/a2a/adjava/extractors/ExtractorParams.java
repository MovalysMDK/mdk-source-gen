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
package com.a2a.adjava.extractors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parameters for extractor
 * @author lmichenaud
 *
 */
public class ExtractorParams {

	/**
	 * Parameter name
	 * @author lmichenaud
	 *
	 */
	public interface ParameterName {
		
		/** 
		 * Get parameter name
		 * @return parameter name
		 */
		public String getParameterName();
	}

	/**
	 * Extractor parameters
	 */
	private Map<String, String> parametersMap = new HashMap<String, String>();

	/**
	 * Return a string list of parameter in configuration
	 * @param p_sName the parameter name
	 * @return list of values
	 */
	public List<String> getValues(String p_sName) {
		List<String> lst = new ArrayList<String>();
		String sParameters = this.parametersMap.get(p_sName);
		if (sParameters != null) {
			for (String sToken : sParameters.split(",")) {
				sToken = sToken.trim();
				lst.add(sToken);
			}
		}
		return lst;
	}
	
	/**
	 * Return a string list of parameter in configuration
	 * @param p_sName the parameter name
	 * @return value of parameter
	 */
	public String getValue(ParameterName p_oParameter) {
		return this.parametersMap.get(p_oParameter.getParameterName());
	}
	
	/**
	 * Return a string list of parameter in configuration
	 * @param p_sName the parameter name
	 * @return value of parameter
	 */
	public String getValue(String p_sParameter) {
		return this.parametersMap.get(p_sParameter);
	}
	
	/**
	 * Return a string list of parameter in configuration
	 * @param p_sName the parameter name
	 * @param p_sDefaultValue default value
	 * @return value
	 */
	public String getValue(String p_sParameter, String p_sDefaultValue ) {
		String r_sValue = this.parametersMap.get(p_sParameter);
		if ( r_sValue == null ) {
			r_sValue = p_sDefaultValue;
		}
		return r_sValue;
	}
	
	/**
	 * Return true if parameter is available
	 * @param p_sName the parameter name
	 * @return true if parameter is available
	 */
	public boolean isParam(String p_sParameter) {
		return this.parametersMap.get(p_sParameter) != null ;
	}

	/**
	 * Set parameter value
	 * @param p_sName parameter name
	 * @param p_sValue parameter value
	 */
	public void setValue(String p_sName, String p_sValue) {
		this.parametersMap.put(p_sName, p_sValue );
	}
}
