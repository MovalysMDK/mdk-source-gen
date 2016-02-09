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
package com.a2a.adjava.commons.init;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.a2a.adjava.AdjavaException;

/**
 * 
 * <p>
 * Factory to initialize adjava configuration
 * </p>
 * 
 * <p>
 * Copyright (c) 2009
 * <p>
 * Company: Adeuza
 * 
 * @author mmadigand
 * @author lmichenaud
 * 
 */
public final class AdjavaInitializer {

	/**
	 * Beans initializing with adjava configuration
	 */
	private List<InitializingBean> listInitializingBeans = new ArrayList<InitializingBean>();

	/**
	 * Add configuration
	 */
	private List<Element> listConfigurations = new ArrayList<Element>();
	
	/**
	 * Register a bean for initialization
	 * 
	 * @param p_oInitializingBean bean to initialize
	 */
	public void registerInitializingBean(InitializingBean p_oInitializingBean) {
		this.listInitializingBeans.add(p_oInitializingBean);
	}

	/**
	 * Add array of files to configuration
	 * 
	 * @param p_files configuration files
	 * @throws Exception exception
	 */
	public void addConfiguration(File[] p_files) throws Exception {
		for (File oFile : p_files) {
			this.addConfiguration(oFile);
		}
	}

	/**
	 * Add array of files to configuration
	 * 
	 * @param p_files configuration files
	 * @throws Exception exception
	 */
	public void addConfiguration(String[] p_files) throws Exception {
		for (String sFile : p_files) {
			addConfiguration(sFile);
		}
	}
	
	/**
	 * Add configuration file
	 * 
	 * @param p_sPath configuration path
	 * @throws Exception exception
	 */
	public void addConfiguration(String p_sPath) throws Exception {
		this.addConfiguration( this.readDocument(p_sPath).getRootElement());
	}

	/**
	 * Add file to configuration
	 * 
	 * @param p_oFile configuration file
	 * @throws Exception exception
	 */
	public void addConfiguration(File p_oFile) throws Exception {
		this.addConfiguration(readDocument(p_oFile.getAbsolutePath()).getRootElement());
	}

	/**
	 * Add element to configuration
	 * 
	 * @param p_xRoot configuration xml node
	 * @throws Exception exception
	 */
	public void addConfiguration(Element p_xRoot) throws Exception {
		this.listConfigurations.add(p_xRoot);
	}

	/**
	 * Load and merge configuration files
	 * 
	 * @throws Exception exception
	 */
	public void load() throws Exception {
		
		Map<String,String> mapGlobalProperties = new HashMap<String,String>();
		
		// global properties must be read first
		for (Element xConf : this.listConfigurations) {
			// Read global properties
			Element xProperties = xConf.element("properties");
			if ( xProperties != null ) {
				for( Element xProperty: (List<Element>) xProperties.elements()) {
					mapGlobalProperties.put(xProperty.getName(), xProperty.getTextTrim());
				}
			}
		}
		
		// initialize beans from xml conf
		for (Element xConf : this.listConfigurations) {
			for (InitializingBean oInitializingBean : this.listInitializingBeans) {
				oInitializingBean.initialize(xConf, mapGlobalProperties);
			}
		}

		for (InitializingBean oInitializingBean : this.listInitializingBeans) {
			oInitializingBean.afterInitialization(mapGlobalProperties);
		}
	}

	/**
	 * Load file either from filesystem or classpath
	 * 
	 * @param p_sFileName file path
	 * @return xml document
	 * @throws Exception load exception
	 */
	private Document readDocument(String p_sFileName) throws Exception {
		Document r_xConf = null;
		File oFile = new File(p_sFileName);
		SAXReader oReader = new SAXReader();
		// try on filesystem
		if (oFile.exists()) {
			r_xConf = oReader.read(oFile);
		} else {
			// try on classpath
			InputStream oIs = this.getClass().getResourceAsStream(p_sFileName);
			if (oIs != null) {
				try {
					r_xConf = oReader.read(oIs);
				} finally {
					oIs.close();
				}
			} else {
				throw new AdjavaException("Can't find configuration file neither on filesystem or classpath : {}", p_sFileName);
			}
		}
		return r_xConf;
	}
}
