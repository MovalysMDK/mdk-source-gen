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
package com.a2a.adjava.uml2xmodele.extractors;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dom4j.Element;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.uml.UmlDataType;
import com.a2a.adjava.uml.UmlModel;

/**
 * <p>
 * DataType Extractor
 * </p>
 * 
 * <p>
 * Copyright (c) 2011
 * <p>
 * Company: Adeuza
 * 
 * @author lmichenaud
 * 
 */

public class DataTypeExtractor extends AbstractExtractor {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(DataTypeExtractor.class);

	/**
	 * Extract datatype from model
	 * 
	 * @param p_oModele
	 */
	public void extract(UmlModel p_oModele) throws Exception {

		// Conversion des datatypes
		for (UmlDataType oDataType : p_oModele.getDictionnary().getDataTypes()) {
			convertUmlDataType(oDataType);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.a2a.adjava.uml2xmodele.extractors.MExtractor#initialize()
	 */
	@Override
	public void initialize( Element p_xConfig ) {

	}

	/**
	 * Convert UmlDataType to ITypeDescription
	 * 
	 * @param p_oDataType
	 * @return
	 */
	public void convertUmlDataType(UmlDataType p_oDataType) throws Exception {

		ITypeDescription r_oTypeDescription = getLngConfiguration().getTypeDescription(p_oDataType.getName());
		if (r_oTypeDescription != null) {

			String sName = p_oDataType.getName();

			boolean bPrimitif = "boolean".equals(sName) || "long".equals(sName) || "int".equals(sName) || "double".equals(sName)
					|| "short".equals(sName) || "char".equals(sName) || "byte".equals(sName) || "float".equals(sName);

			if ("String".equals(sName)) {
				sName = String.class.getName();
			} else if ("Timestamp".equals(sName)) {
				sName = Timestamp.class.getName();
			} else if ("Date".equals(sName)) {
				sName = Date.class.getName();
			} else if ("Clob".equals(sName)) {
				sName = Clob.class.getName();
			} else if ("Blob".equals(sName)) {
				sName = Blob.class.getName();
			}

			r_oTypeDescription.setPrimitif(bPrimitif);

			if (bPrimitif) {
				ITypeDescription oWrapper = getLngConfiguration().getTypeDescription(r_oTypeDescription.getWrapperName());
				if (oWrapper != null) {
					r_oTypeDescription.setWrapper(oWrapper);
					log.debug("  pour le type primitif: {}, wrapper: {}", r_oTypeDescription.getUmlName(), oWrapper.getName());
				} else {
					throw new AdjavaException("Impossible de trouver le wrapper du type '{}', wrapper name = '{}'", r_oTypeDescription.getUmlName()
							+ r_oTypeDescription.getWrapperName());
				}
			}
			
			getDomain().getDictionnary().registerTypeDescription(r_oTypeDescription.getUmlName(), r_oTypeDescription);
		}
	}
}
