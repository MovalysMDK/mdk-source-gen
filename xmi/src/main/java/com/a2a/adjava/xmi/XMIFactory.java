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
package com.a2a.adjava.xmi;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.commons.init.InitializingBean;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.umlupdater.UmlUpdater;
import com.a2a.adjava.utils.JaxbUtils;

/**
 * 
 * <p>
 * XMI Factory
 * 
 * <p>
 * Copyright (c) 2010
 * </p>
 * <p>
 * Company: Adeuza
 * </p>
 * 
 * @author mmadigand
 * 
 */
public final class XMIFactory implements InitializingBean {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(XMIFactory.class);

	/**
	 * XMI Updater 
	 */
	private List<XMIUpdater> xmiUpdaters = new ArrayList<XMIUpdater>();
	
	/**
	 * XMI Readers
	 */
	private List<XMIReader> xmiReaders = new ArrayList<XMIReader>();
	
	/**
	 * Uml Updaters 
	 */
	private List<UmlUpdater> umlUpdaters = new ArrayList<UmlUpdater>();
	
	/**
	 * InlineDocLng
	 */
	private String inlineDocLng;
	
	/**
	 * Xmi File
	 */
	private String xmiFile;

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.a2a.adjava.project.InitializingBean#initialize(org.dom4j.Element)
	 */
	@Override
	public void initialize(Element p_xRoot, Map<String,String> p_mapGlobalProperties) throws Exception {
		Element xXmi = p_xRoot.element("xmi");

		if ( xXmi != null ) {
			String sXmiFile = xXmi.elementText("xmi-file");
			if ( sXmiFile != null ) {
				this.xmiFile = sXmiFile ;
			}
			Element xInlineDoc = xXmi.element("inline-doc");
			if (xInlineDoc != null) {
				this.inlineDocLng = xInlineDoc.elementText("lng");
			}
		}
		
		this.readXmiReaders(xXmi);
		this.readXmiUpdaters(xXmi);
		this.readUmlUpdaters(p_xRoot);

	}

	/**
	 * Read xmi readers
	 * 
	 * @param p_xRoot
	 */
	private void readXmiReaders(Element p_xXmi) throws Exception {
		if (p_xXmi != null) {
			Element xXmiReaders = (Element) p_xXmi.selectSingleNode("xmi-readers");
			if (xXmiReaders != null) {

				String sReplace = xXmiReaders.attributeValue("replace");
				if (sReplace != null && Boolean.parseBoolean(sReplace)) {
					this.xmiReaders.clear();
				}

				for (Element xXmiReader : (List<Element>) xXmiReaders.selectNodes("xmi-reader")) {
					String sClass = xXmiReader.attributeValue("class");
					XMIReader oXMIReader = (XMIReader) Class.forName(sClass).newInstance();
					log.debug("xmi reader: {}", sClass );
					this.xmiReaders.add(oXMIReader);
				}
			}
		}
	}

	/**
	 * @param p_oProjectConfig
	 * @param xRoot
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	private void readXmiUpdaters(Element p_xXmi) throws Exception {
		// Lecture des xmi updater
		if (p_xXmi != null) {
			Element xXmiUpdaters = p_xXmi.element("xmi-updaters");
			if (xXmiUpdaters != null) {

				String sReplace = xXmiUpdaters.attributeValue("replace");
				if (sReplace != null && Boolean.parseBoolean(sReplace)) {
					this.xmiUpdaters.clear();
				}

				for (Element xXmiUpdater : (List<Element>) xXmiUpdaters.elements("xmi-updater")) {
					String sGenClass = xXmiUpdater.attributeValue("class");
					XMIUpdater oXMIUpdater = (XMIUpdater) Class.forName(sGenClass).newInstance();
					Map<String, String> oParametersMap = new HashMap<String, String>();
					for (Element xProperty : (List<Element>) xXmiUpdater.elements("property")) {
						String sName = xProperty.attributeValue("name");
						String sValue = xProperty.attributeValue("value");
						oParametersMap.put(sName, sValue);
					}
					oXMIUpdater.setParametersMap(oParametersMap);
					this.xmiUpdaters.add(oXMIUpdater);
				}
			}
		}
	}

	/**
	 * @param p_oProjectConfig
	 * @param p_xRoot
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	private void readUmlUpdaters(Element p_xRoot) throws Exception {
		Element xUmlUpdaters = p_xRoot.element("uml-updaters");
		if (xUmlUpdaters != null) {

			String sReplace = xUmlUpdaters.attributeValue("replace");
			if (sReplace != null && Boolean.parseBoolean(sReplace)) {
				this.umlUpdaters.clear();
			}

			for (Element xUmlUpdater : (List<Element>) xUmlUpdaters.elements("uml-updater")) {
				String sGenClass = xUmlUpdater.attributeValue("class");
				UmlUpdater oUmlUpdater = (UmlUpdater) Class.forName(sGenClass).newInstance();
				Map<String, String> oParametersMap = new HashMap<String, String>();
				for (Element xProperty : (List<Element>) xUmlUpdater.elements("property")) {
					String sName = xProperty.attributeValue("name");
					String sValue = xProperty.attributeValue("value");
					oParametersMap.put(sName, sValue);
				}
				oUmlUpdater.setParametersMap(oParametersMap);
				this.umlUpdaters.add(oUmlUpdater);
			}
		}
	}

	/**
	 * Find a compatible xmi reader for reading a xmi document
	 * @param p_xDocument xmi document to read
	 * @return XMIReader to use
	 * @throws Exception exception
	 */
	public XMIReader getXMIReader( Document p_xDocument ) throws Exception {
		XMIReader r_oXMIReader = null ;
		for( XMIReader oXmiReader : this.xmiReaders ) {
			if ( oXmiReader.canRead(p_xDocument)) {
				r_oXMIReader = oXmiReader ;
				break;
			}
		}
		
		if ( r_oXMIReader == null ) {
			throw new AdjavaException("Your xmi file is not compatible with any configured xmi readers");
		}
		
		return r_oXMIReader ;
	}

	/**
	 * @param p_sXmiFile
	 * @return
	 * @throws Exception
	 */
	public UmlModel load() throws Exception {

		SAXReader oReader = new SAXReader();
		oReader.setValidation(false);
		Document xDoc = oReader.read(this.xmiFile);

		// Xmi Updater execution
		for (XMIUpdater oXMIUpdater : this.xmiUpdaters) {
			oXMIUpdater.execute(xDoc);
		}

		// Find a reader to read the xmi file
		XMIReader oXMIReader = getXMIReader(xDoc);

		UmlModel r_oModele = oXMIReader.read(xDoc);

		//To Debug: JaxbUtils.marshal(r_oModele, new File("/tmp/mduml-mesnotesendirect.xml"));
		
		// UmlUpdater execution
		MessageHandler oMessageHandler = MessageHandler.getInstance();
		if (!MessageHandler.getInstance().hasErrors()) {

			Map<String, ?> oMapSession = new HashMap<String, Object>();
			for (UmlUpdater oUmlUpdater : this.umlUpdaters) {
				oUmlUpdater.execute(r_oModele, oMapSession);
				if (oMessageHandler.hasErrors()) {
					break;
				}
			}
		}

		return r_oModele;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.commons.init.InitializingBean#afterInitialization()
	 */
	@Override
	public void afterInitialization( Map<String,String> p_mapGlobalProperties) throws Exception {
		
		if ( this.xmiFile == null || this.xmiFile.isEmpty()) {
			MessageHandler.getInstance().addError("Xmi file has not been set up in configuration.");
		}
		
		if ( this.inlineDocLng == null || this.inlineDocLng.isEmpty()) {
			MessageHandler.getInstance().addError("inline-doc has not been set up in configuration.");
		}
	}
}
