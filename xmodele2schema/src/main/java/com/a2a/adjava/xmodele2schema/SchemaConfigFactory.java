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
package com.a2a.adjava.xmodele2schema;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.a2a.adjava.schema.SchemaFactory;
import com.a2a.adjava.schema.naming.DbNamingStrategy;

/**
 * <p>
 * Load configuration of SchemaExtractor
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

public class SchemaConfigFactory {

	/**
	 * 
	 */
	private SchemaConfigFactory() {
		// Utility class
	}
	
	/**
	 * Load configuration of SchemaExtractor
	 * 
	 * @param p_xConfig configuration element
	 * @param p_oConfig Schema configuration
	 * @throws Exception
	 */
	public static void loadConfiguration(Element p_xConfig, SchemaConfig p_oConfig) throws Exception {
		if (p_xConfig != null) {
			Element xNamingStrategy = p_xConfig.element("db-naming-strategy");
			if (xNamingStrategy != null) {
				String sNamingStrategy = xNamingStrategy.attributeValue("class");
				DbNamingStrategy oDbNamingStrategyClass = (DbNamingStrategy) Class.forName(sNamingStrategy).newInstance();
				Map<String, String> mapOptions = new HashMap<String, String>();
				for (Element xOption : (List<Element>) xNamingStrategy.elements("option")) {
					String sName = xOption.attributeValue("name");
					String sValue = xOption.attributeValue("value");
					mapOptions.put(sName, sValue);
				}
				oDbNamingStrategyClass.setOptions(mapOptions);
				p_oConfig.setDbNamingStrategyClass(oDbNamingStrategyClass);
			}
			
			String sSchemaFactory = p_xConfig.elementText("schema-factory");
			if ( sSchemaFactory != null ) {
				sSchemaFactory = sSchemaFactory.trim();
				p_oConfig.setSchemaFactory((SchemaFactory) Class.forName(sSchemaFactory).newInstance());
			}
		}
	}
}
