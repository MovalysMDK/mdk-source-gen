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
package com.a2a.adjava.languages.java.mupdater;

import java.util.Map;

import com.a2a.adjava.languages.java.project.JTypeDescription;
import com.a2a.adjava.mupdater.AbstractMUpdater;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MEnumeration;

/**
 * <p>Set 'jdbc-retrieve' property on type description of enumeration</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */

public class JdbcRetrieveForEnumeration extends AbstractMUpdater {

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.mupdater.MUpdater#execute(com.a2a.adjava.project.ProjectConfig, com.a2a.adjava.xmodele.MModele, java.util.Map)
	 */
	@Override
	public void execute(IDomain<IModelDictionary, IModelFactory> p_oDomain, Map<String,?> p_oGlobalSession) throws Exception {
		
		for( MEnumeration oEnumeration: p_oDomain.getDictionnary().getAllEnumerations()) {
			JTypeDescription oTypeDescription = (JTypeDescription) oEnumeration.getTypeDescription();
			oTypeDescription.setJdbcRetrieve(oTypeDescription.getJdbcRetrieve().replaceAll("ENUMERATION", oEnumeration.getName()));
		}
	}
}
