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
package com.a2a.adjava.languages.ios.umlupdater;

import java.util.Map;

import com.a2a.adjava.uml.UmlAssociation;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.umlupdater.AbstractUmlUpdater;

/**
 * Enable navigability on all one-to-one associations.
 * @author lmichenaud
 *
 */
public class ToOneNavigableUpdater extends AbstractUmlUpdater {

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.umlupdater.UmlUpdater#execute(com.a2a.adjava.uml.UmlModel, java.util.Map)
	 */
	@Override
	public void execute(UmlModel p_oUmlModele, Map<String, ?> p_oGlobalSession)
			throws Exception {
		
		for( UmlAssociation oAsso : p_oUmlModele.getDictionnary().getAssociations()) {			
			if ( oAsso.getAssociationEnd1().getMultiplicityUpper() != null && oAsso.getAssociationEnd1().getMultiplicityUpper() == 1 ) {
				oAsso.getAssociationEnd1().setNavigable(true);
			}
			
			if ( oAsso.getAssociationEnd2().getMultiplicityUpper() != null && oAsso.getAssociationEnd2().getMultiplicityUpper() == 1 ) {
				oAsso.getAssociationEnd2().setNavigable(true);
			}
		}
	}
}
