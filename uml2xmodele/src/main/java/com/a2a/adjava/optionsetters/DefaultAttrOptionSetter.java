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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.languages.LanguageConfiguration;
import com.a2a.adjava.xmodele.MAttribute;

/**
 * <p>
 * Définit les options standards sur les attributs
 * </p>
 * 
 * <p>
 * Copyright (c) 2009
 * <p>
 * Company: Adeuza
 * 
 * @author lmichenaud
 * @since MF-Annapurna
 */
public class DefaultAttrOptionSetter extends AbstractOptionSetter<MAttribute> {

	/**
	 * <p>
	 * Enumération correspondant aux différentes options définissable sur les
	 * attributs d'une classe dans ADJava.
	 * </p>
	 * 
	 * <p>
	 * Copyright (c) 2011
	 * </p>
	 * <p>
	 * Company: Adeuza
	 * </p>
	 * 
	 * @author lmichenaud
	 * @since MF-Annapurna
	 */
	public enum Option {

		/**
		 * L'option valeur d'initialisation
		 */
		INIT("init"),

		/**
		 * Option pour indiquer que la valeur est générée par la base
		 */
		GENERATEDVALUE("G"),

		/**
		 * Option pour indiquer la longueur d'une chaine
		 */
		LENGTH("L"),

		/**
		 * Option pour indiquer que la précision d'un nombre
		 */
		PRECISION("P"),

		/**
		 * Option pour indiquer le nombre de chiffres après la virgule d'un
		 * nombre
		 */
		SCALE("S"),

		/**
		 * Option pour indiquer l'unicité de l'attribut
		 */
		UNIQUE("U"),

		/**
		 * Option read only
		 */
		READ_ONLY("R"),

		/**
		 * No label
		 */
		NO_LABEL("N"),
		
		/**
		 * Optional
		 */
		OPTIONAL("O"),

		/**
		 * Param for combobox indique si le champ est présent dans la liste ou
		 * dans l'élément selected (s : élément selected, l : dans la liste)
		 */
		COMBO("C"),

		/**
		 * FixedList Indique si le champ est présent dans la liste ou dans le
		 * détail de l'élément sélectionné (s : élément sélectionné, l : dans la
		 * liste)
		 */
		FIXED_LIST("F"),
		
		/**
		 * Transient
		 */
		TRANSIENT("T"),
		
		/**
		 * Compute Field Type
		 */
		COMPUTE_FIELD_TYPE("E");

		/** Le code d'une option */
		private String code = null;

		/**
		 * Enumération de type Option.
		 * 
		 * @param p_sUmlCode
		 *            le code de l'option à créer.
		 */
		private Option(String p_sUmlCode) {
			this.code = p_sUmlCode;
		}

		/**
		 * retourne le code d'une option.
		 * 
		 * @return le code de type String. Ex: F, C, N, etc.
		 */
		public String getUmlCode() {
			return this.code;
		}
	}

	/**
	 * Use default value
	 */
	private static final String DEF = "DEF";
	
	private static final Logger log = LoggerFactory.getLogger(DefaultAttrOptionSetter.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void applyOptions(Map<String, ?> p_mapOptions,
			MAttribute p_oAttribute, LanguageConfiguration p_oLngConf) {

		log.debug("  > applyOption: map:{}, attr:{}", p_mapOptions, p_oAttribute);
		
		boolean bGenerated = p_mapOptions.get(Option.GENERATEDVALUE
				.getUmlCode()) != null;
//		boolean bMandatory = p_mapOptions.get("N") == null;
		boolean bMandatory = p_mapOptions.get(Option.OPTIONAL.getUmlCode()) == null;
		
		log.debug("  > applyOption: bMandatory:{}", bMandatory);
		
		int iLength = -1;
		if (p_mapOptions.get(Option.LENGTH.getUmlCode()) != null) {
			iLength = Integer.parseInt((String) p_mapOptions.get(Option.LENGTH
					.getUmlCode()));
		}
		int iPrecision = -1;
		if (p_mapOptions.get(Option.PRECISION.getUmlCode()) != null) {
			iPrecision = Integer.parseInt((String) p_mapOptions
					.get(Option.PRECISION.getUmlCode()));
		}
		int iScale = -1;
		if (p_mapOptions.get(Option.SCALE.getUmlCode()) != null) {
			iScale = Integer.parseInt((String) p_mapOptions.get(Option.SCALE
					.getUmlCode()));
		}
		boolean bUnique = p_mapOptions.get(Option.UNIQUE.getUmlCode()) != null
				&& ((String) p_mapOptions.get(Option.UNIQUE.getUmlCode()))
						.length() == 0;
		String sUniqueKey = (String) p_mapOptions.get(Option.UNIQUE
				.getUmlCode());

		String sModelInitValue = (String) p_mapOptions.get(Option.INIT.getUmlCode());
		if ( p_oAttribute.isBasic()) {
			setInitValueForBasicAttribute(sModelInitValue, p_oAttribute, p_oLngConf );
		}
		else {
			setInitValueForCompositeAttribute(sModelInitValue, p_oAttribute, p_oLngConf );
		}
		
		String sTransient = (String) p_mapOptions.get(Option.TRANSIENT.getUmlCode());
		if ( sTransient != null ) {
			p_oAttribute.setTransient(true);
		}

		p_oAttribute.setMandatory(bMandatory);
		p_oAttribute.setUnique(bUnique);
		p_oAttribute.setUniqueKey(sUniqueKey);

		p_oAttribute.setHasSequence(bGenerated);
		p_oAttribute.setLength(iLength);
		p_oAttribute.setPrecision(iPrecision);
		p_oAttribute.setScale(iScale);
	}

	/**
	 * @param p_sModelInitValue
	 * @param p_oAttribute
	 */
	private void setInitValueForBasicAttribute(String p_sModelInitValue,
			MAttribute p_oAttribute, LanguageConfiguration p_oLngConf) {

		// calcul la chaine d'initialisation à partir de la valeur par défaut
		String sInit = null;
		String sDefaultValue = null;
		boolean bDefaultValueFromType = false;
		if (p_sModelInitValue != null) {
			sDefaultValue = p_sModelInitValue;
		} else {
			sDefaultValue = p_oAttribute.getTypeDesc().getDefaultValue();
			bDefaultValueFromType = true;
		}

		if (!p_oAttribute.getTypeDesc().isPrimitif()
				&& p_oLngConf.getNullValue().equals(sDefaultValue)) {
			sInit = p_oLngConf.getNullValue();
		} else {
			sInit = p_oAttribute.getTypeDesc().getInitFormat()
					.replaceAll("\\?", sDefaultValue);
		}

		if (p_oAttribute.isEnum()) {
			sInit = sInit.replaceAll("ENUMERATIONUPPERCASE", p_oAttribute
					.getMEnumeration().getName().toUpperCase());
			
			sInit = sInit.replaceAll("ENUMERATION", p_oAttribute
					.getMEnumeration().getName());
		}
		
		p_oAttribute.setInitialisation(sInit);
		p_oAttribute.setInitialisationFromType(bDefaultValueFromType);
		p_oAttribute.setDefaultValue(sDefaultValue);
	}

	/**
	 * @param p_sModelInitValue init value from model
	 * @param p_oAttribute attribute
	 * @param p_oLngConf language configuration
	 */
	private void setInitValueForCompositeAttribute(String p_sModelInitValue,
			MAttribute p_oAttribute, LanguageConfiguration p_oLngConf) {

		// Construct array of default values of properties of attributes 
		String[] t_sDefaultValues = new String[p_oAttribute.getProperties().size()];
		int iPos = 0 ;
		for( MAttribute oProperty : p_oAttribute.getProperties()) {
			t_sDefaultValues[iPos] = oProperty.getTypeDesc().getDefaultValue();
			iPos++;
		}
		
		// Override values in array with the ones in model
		if ( p_sModelInitValue != null ) {
			String[] t_sSplittedValues = StringUtils.splitPreserveAllTokens(p_sModelInitValue,'|');
			iPos = 0 ;
			for( String sValueFromModel : t_sSplittedValues) {
				if ( !DEF.equals(sValueFromModel)) {
					t_sDefaultValues[iPos] = sValueFromModel;
				}
				iPos++;
			}
		}
		
		// Compute init format for each property
		iPos = 0 ;
		for( MAttribute oProperty : p_oAttribute.getProperties()) {
			if ( oProperty.isBasic()) {
				this.setInitValueForBasicAttribute(t_sDefaultValues[iPos], oProperty, p_oLngConf);
			}
			iPos++ ;
		}
		
		// Apply values to init format
		String sInitFormat = p_oAttribute.getTypeDesc().getInitFormat();
		p_oAttribute.setInitialisation(sInitFormat);
		p_oAttribute.setInitialisationFromType(true);
	}
}
