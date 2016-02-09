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

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>Parsing des options sur les relations</p>
 *
 * <p>Copyright (c) 2009
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */
public final class UmlAssociationEndOptionParser {

	/**
	 * Instance singleton
	 */
	private static UmlAssociationEndOptionParser instance;

	/**
	 * Constructeur private car singleton
	 */
	private UmlAssociationEndOptionParser() {
	}

	/**
	 * Retourne l'instance du singleton
	 * @return l'instance du singleton
	 */
	public static UmlAssociationEndOptionParser getInstance() {
		if (instance == null) {
			instance = new UmlAssociationEndOptionParser();
		}
		return instance;
	}
	
	/**
	 * Parse les options de la relation
	 * @param p_sName
	 * @return
	 */
	public Map<String,String> parse(String p_sChaine ) {
		
		Map<String,String> r_mapOptions = new HashMap<String,String>();
		parseOptions(p_sChaine, r_mapOptions);
		return r_mapOptions ;
	}
	
	/**
	 * @param p_sChaine
	 * @return
	 */
	private void parseOptions(String p_sChaine, Map<String,String> p_oMapOptions) {

		if (p_sChaine != null) {

			StringTokenizer oStrParser = new StringTokenizer(p_sChaine, "_");
			if (p_sChaine.length() > 0) {
				while (oStrParser.hasMoreTokens()) {
					String sOption = oStrParser.nextToken();
					String sOptionChaine = sOption.substring(0, 1);
					String sValeur = StringUtils.EMPTY;
					if (sOption.length() > 1) {
						sValeur = sOption.substring(1);
					}
					p_oMapOptions.put(sOptionChaine, sValeur);
				}
			}
		}
	}
}
