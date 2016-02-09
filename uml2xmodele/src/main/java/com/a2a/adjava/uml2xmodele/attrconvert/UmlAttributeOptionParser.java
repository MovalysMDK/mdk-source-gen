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
package com.a2a.adjava.uml2xmodele.attrconvert;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.a2a.adjava.datatypes.DataType;
import com.a2a.adjava.optionsetters.DefaultAttrOptionSetter;

/**
 * <p>Parse les options des attributs exprimées dans la conf ou sur le modèle</p>
 *
 * <p>Copyright (c) 2009
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */
public final class UmlAttributeOptionParser {

	/**
	 * Séparateur d'option
	 */
	private static final String OPTION_SEPARATOR = "_";
	
	/**
	 * Option (adjava2) pour indiquer que la valeur est générée par la base 
	 */
	public static final String GENERATEDVALUE_ADJAVA2_OPTION = "S";
	
	/**
	 * Instance du singleton
	 */
	private static UmlAttributeOptionParser instance;

	/**
	 * 
	 */
	private UmlAttributeOptionParser() {
	}

	/**
	 * Retourne l'instance singleton
	 * @return l'instance singleton
	 */
	public static UmlAttributeOptionParser getInstance() {
		if (instance == null) {
			instance = new UmlAttributeOptionParser();
		}
		return instance;
	}

	/**
	 * Merge la chaine d'options venant du modèle et celle du type par défaut
	 * @param p_sModelOptions la chaine venant de l'attribut du modèle
	 * @param p_sDefaultOptions les options par défaut
	 * @return la map d'options mergés
	 */
	public Map<String,?> parse(String p_sModelOptions, String p_sDefaultOptions, DataType p_oDataType ) {

		Map<String,String> r_mapOptions = new HashMap<String,String>();
		parseOptions(p_sDefaultOptions, r_mapOptions);
		parseAdjava2Options(p_sModelOptions, r_mapOptions, p_oDataType );
		
		return r_mapOptions;
	}
	
	/**
	 * Parse options of the model.
	 * Default options and adjava2 options are not managed in this method 
	 * @param p_sModelOptions
	 * @return
	 */
	public Map<String,?> parse(String p_sModelOptions ) {
		Map<String,String> r_mapOptions = new HashMap<String,String>();
		parseOptions(p_sModelOptions, r_mapOptions);		
		return r_mapOptions;
	}

	/**
	 * Parse les options et alimente la map d'options
	 * @param p_sChaine chaine à parser
	 * @param p_oMapOptions map à alimenter
	 */
	private void parseOptions(String p_sChaine, Map<String,String> p_oMapOptions) {

		if (p_sChaine != null) {

			StringTokenizer oStrParser = new StringTokenizer(p_sChaine, OPTION_SEPARATOR);
			if (p_sChaine.length() > 0) {
				if (p_sChaine.charAt(0) != '_') {
					p_oMapOptions.put(DefaultAttrOptionSetter.Option.INIT.getUmlCode(), oStrParser.nextToken());
				}

				while (oStrParser.hasMoreTokens()) {
					String sOption = oStrParser.nextToken();
					String sOptionChaine = sOption.substring(0, 1);
					String sValeur = "";
					if (sOption.length() > 1) {
						sValeur = sOption.substring(1);
					}
					p_oMapOptions.put(sOptionChaine, sValeur);
				}
			}
		}
	}
	
	/**
	 * Parse les options au format adjava 2
	 * @param p_sChaine options au format chaine de caractères
	 * @param p_oMapOptions la map d'options à alimenter
	 */
	private void parseAdjava2Options(String p_sChaine, Map<String,String> p_oMapOptions, DataType p_oDataType ) {

		if (p_sChaine != null) {

			// Découpe la chaine
			StringTokenizer oStrParser = new StringTokenizer(p_sChaine, OPTION_SEPARATOR);
			if (!p_sChaine.isEmpty()) {
				
				// Traite la valeur d'init s'il y en une
				if ( !p_sChaine.substring(0,1).equals(OPTION_SEPARATOR)) {
					p_oMapOptions.put(DefaultAttrOptionSetter.Option.INIT.getUmlCode(), oStrParser.nextToken());
				}

				int iPos = 1 ;
				// Puis, parcours les éléments parsés
				while (oStrParser.hasMoreTokens()) {
					String sOption = oStrParser.nextToken();
					String sOptionKey = sOption.substring(0, 1);
					
					// Si valeur généré ( _S en adjava2 )
					if ( sOption.equals(GENERATEDVALUE_ADJAVA2_OPTION) ) {
						p_oMapOptions.put(DefaultAttrOptionSetter.Option.GENERATEDVALUE.getUmlCode(), "");
					} 
					// Sinon, si la clé commence par un chiffre, la lettre de l'option est fonction de la position.
					// position1 : precision ou longueur
					// position2 : scaling
					else if ( "123456789".indexOf(sOptionKey) != -1 ) {
						String sValeur = sOption.trim();
						switch( iPos ) {
							case 1:
								// precision ou longueur
								if ( p_oDataType.equals(DataType.NUMERIC)) {
									p_oMapOptions.put(DefaultAttrOptionSetter.Option.PRECISION.getUmlCode(), sValeur);
								}
								else if ( p_oDataType.equals(DataType.ALPHANUMERIC)) {
									p_oMapOptions.put(DefaultAttrOptionSetter.Option.LENGTH.getUmlCode(), sValeur);
								}
								break ;
							case 2:
								p_oMapOptions.put(DefaultAttrOptionSetter.Option.SCALE.getUmlCode(), sValeur);
								break ;
						default:
							throw new IllegalArgumentException(" iPos > 2");
						}					
						iPos++ ;
					}
					// Sinon, c'est une lettre ( option au format adjava4 )
					else {
						p_oMapOptions.put(sOptionKey, sOption.substring(1));
					}
				}
			}
		}
	}
}
