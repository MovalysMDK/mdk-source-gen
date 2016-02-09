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
package com.a2a.adjava.uml2xmodele.assoconvert;

import java.util.List;
import java.util.Map;

import com.a2a.adjava.languages.LanguageConfiguration;
import com.a2a.adjava.optionsetters.OptionSetter;
import com.a2a.adjava.xmodele.MAssociation;

/**
 * @author lmichenaud
 *
 */
public class AbstractMAssociationConverter {

	/**
	 * @param p_sOptions
	 * @param p_oAssociation
	 * @param p_listAssoOptionSetters
	 * @param p_oLngConf language configuration
	 * @throws Exception
	 */
	public void applyOptions( String p_sOptions, MAssociation p_oAssociation, List<OptionSetter<Object>> p_listAssoOptionSetters, LanguageConfiguration p_oLngConf ) throws Exception {
		
		Map<String, ?> mapAssoOptions = UmlAssociationEndOptionParser.getInstance().parse(p_sOptions);
		for (OptionSetter<Object> oOptionSetter : p_listAssoOptionSetters) {
			oOptionSetter.applyOptions(mapAssoOptions, p_oAssociation, p_oLngConf);
		}
	}
}
