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

import org.apache.commons.lang3.StringUtils;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.languages.LanguageConfiguration;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.xmodele.MAssociation;
import com.a2a.adjava.xmodele.MAssociationManyToMany;
import com.a2a.adjava.xmodele.MAssociationWithForeignKey;

/**
 * <p>Définit les options par défaut sur les assos</p>
 *
 * <p>Copyright (c) 2009
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */
public class DefaultAssoOptionSetter extends AbstractOptionSetter<MAssociation> {

	/**
	 * Option pour indiquer l'unicité de l'attribut
	 */
	public static final String UNIQUE_OPTION = "U";	
	
	/**
	 * Option pour indiquer l'unicité de l'attribut
	 */
	public static final String TRANSIENT_OPTION = "T";	
	
	/**
	 * {@inheritDoc}
	 */
	public void applyOptions(Map<String, ?> p_mapOptions, MAssociation p_oAssociation, LanguageConfiguration p_oLngConf) throws AdjavaException {
		
		String sUniqueKey = (String) p_mapOptions.get(UNIQUE_OPTION);
		if ( sUniqueKey != null && ( p_oAssociation instanceof MAssociationWithForeignKey )) {
			if ( StringUtils.isNotBlank(sUniqueKey)) {
				MAssociationWithForeignKey oMAssociationWithForeignKey = (MAssociationWithForeignKey) p_oAssociation ;
				oMAssociationWithForeignKey.setUniqueKey(sUniqueKey);
			}
			else {
				MessageHandler.getInstance().addError("Unique key on association {} to target class {} must be named", 
					p_oAssociation.getName(), p_oAssociation.getRefClass().getName());
			}
		}
		
		String sTransient = (String) p_mapOptions.get(TRANSIENT_OPTION);
		if ( sTransient != null ) {
			p_oAssociation.setTransient(true);
			if ( p_oAssociation instanceof MAssociationManyToMany ) {
				
			}
		}
	}
}
