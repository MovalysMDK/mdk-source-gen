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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.commons.init.InitializingBean;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.uml.UmlAssociation;
import com.a2a.adjava.uml.UmlAssociationClass;
import com.a2a.adjava.uml.UmlAssociationEnd;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlComment;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlEnum;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.uml.UmlPackage;
import com.a2a.adjava.uml.UmlUsage;
import com.a2a.adjava.umlupdater.UmlUpdater;

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
	 * umlExclude
	 */
	private List<String> listUmlExcludes = new ArrayList<String>();

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
		
		// Read umlExcludes
		Element xUmlExcludes = p_xRoot.element("umlExcludes");
		if ( xUmlExcludes != null ) {
			for( Element xUmlExclude: (List<Element>) xUmlExcludes.elements()) {
				this.listUmlExcludes.add(xUmlExclude.getName());
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

		// Generate UmlModel
		UmlModel r_oModele = oXMIReader.read(xDoc);

		//To Debug: JaxbUtils.marshal(r_oModele, new File("/tmp/mduml-mesnotesendirect.xml"));
		
		// Filter umlExclude on UmlModel
		r_oModele = filterModel(r_oModele);
		
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
	 * Filter UmlModel
	 * 
	 * @param p_oModele
	 * @return p_oModele
	 * @throws Exception
	 */
	private UmlModel filterModel(UmlModel p_oModele) throws Exception {

		// New Dictionnary for Duplicate Model
		UmlModel r_oModele = p_oModele.copy();
		UmlDictionary oDictionary = r_oModele.getDictionnary();
		
		// Filter Class
		Collection<UmlClass> oClasses = oDictionary.getAllClasses();
		Iterator<UmlClass> iterCl = oClasses.iterator();
		while (iterCl.hasNext()) {
			UmlClass oClass = (UmlClass) iterCl.next();
		
			if (isExcludePackage(oClass.getPackage())) {
                // Remove Class				
				iterCl.remove();
			}
		}
		
		// Filter AssociationClass
		Collection<UmlAssociationClass> oAssociationClasses = oDictionary.getAssociationClasses();
		Iterator<UmlAssociationClass> iterAC = oAssociationClasses.iterator();
		while (iterAC.hasNext()) {
			UmlAssociationClass oAssociationClass = (UmlAssociationClass) iterAC.next();
		
			if (isExcludePackage(oAssociationClass.getPackage())) {
				// Remove AssociationClass		
				iterAC.remove();
			}
		}
		
		// Filter Association
		Collection<UmlAssociation> oAssociations = oDictionary.getAssociations();
		Iterator<UmlAssociation> iterA = oAssociations.iterator();
		while (iterA.hasNext()) {
			UmlAssociation oAssociation = (UmlAssociation) iterA.next();
		
			if (isExcludePackage(oAssociation.getAssociationEnd1().getRefClass().getPackage()) 
					|| isExcludePackage(oAssociation.getAssociationEnd2().getRefClass().getPackage())) {
				// Remove Association
				iterA.remove();
			}
		}

		// Filter AssociationEnd
		Collection<UmlAssociationEnd> oAssociationEnds = oDictionary.getAllAssociationEnds();
		Iterator<UmlAssociationEnd> iterAE = oAssociationEnds.iterator();
		while (iterAE.hasNext()) {
			UmlAssociationEnd oAssociationEnd = (UmlAssociationEnd) iterAE.next();
		
			if (oAssociationEnd.getRefClass() != null && oAssociationEnd.getRefClass().getPackage() != null) {
				if (isExcludePackage(oAssociationEnd.getRefClass().getPackage())) {
					// Remove AssociationEnd
					iterAE.remove();
				}
			}
			
			if (oAssociationEnd.getRefClass() == null) {
				// Remove AssociationEnd
				iterAE.remove();
			}
		}
		
		// Filter Enum
		Collection<UmlEnum> oEnumerations = oDictionary.getAllEnumerations();
		Iterator<UmlEnum> iterE = oEnumerations.iterator();
		while (iterE.hasNext()) {
			UmlEnum oEnum = (UmlEnum) iterE.next();
		
			if (isExcludePackage(oEnum.getUmlPackage())) {
				// Remove Enumeration
				iterE.remove();
			}
		}
	
		// Filter Usage
		Collection<UmlUsage> oUsages = oDictionary.getUsages();
		Iterator<UmlUsage> iterU = oUsages.iterator();
		while (iterU.hasNext()) {
			UmlUsage oUsage = (UmlUsage) iterU.next();

			if ((oUsage.getClient() != null && oUsage.getClient().getPackage() != null)
					&& (oUsage.getSupplier() != null && oUsage.getSupplier().getPackage() != null)) {
				if (isExcludePackage(oUsage.getClient().getPackage()) 
						|| isExcludePackage(oUsage.getSupplier().getPackage())) {
					// Remove Usage
					iterU.remove();
			
					// Remove Usage for Class
					UmlClass oClient = oUsage.getClient();
					oClient.removeUsage(oUsage);

					// Remove Usage for Class
					UmlClass oSupplier = oUsage.getSupplier();
					oSupplier.removeUsage(oUsage);
				}
			}
			
			if (oUsage.getClient() == null || oUsage.getSupplier() == null) {
				// Remove Usage
				iterU.remove();
			}
		}
		
		// Filter Package
		Collection<UmlPackage> oPackages = oDictionary.getAllPackages();
		Iterator<UmlPackage> iterP = oPackages.iterator();
		while (iterP.hasNext()) {
			UmlPackage oPackage = (UmlPackage) iterP.next();

			if (isExcludePackage(oPackage)) {
				// Remove Package
				iterP.remove();
			}
		}
		
		// Filter Comments
		List<UmlComment> oComments = oDictionary.getComments();
		Iterator<UmlComment> iterC = oComments.iterator();
		while (iterC.hasNext()) {
			UmlComment oComment = (UmlComment) iterC.next();

			if (!isCommentInClass(oDictionary, oComment)) {
				// Remove Comment
				iterC.remove();
			}
		}

		return r_oModele;
	}
	
	/**
	 * Is Exclude Package
	 * 
	 * @param p_oModele
	 * @return p_oModele
	 * @throws Exception
	 */
	private boolean isExcludePackage(UmlPackage p_oPackage) throws Exception {

		for (String sUmlExclude : listUmlExcludes) {
			if ((sUmlExclude.equals(p_oPackage.getFullName()) && sUmlExclude.length() == p_oPackage.getFullName().length())
					|| (p_oPackage.getFullName().startsWith(sUmlExclude) && p_oPackage.getFullName().length() > sUmlExclude.length()))
				return true;
		}
		return (false);
	}
	
	/**
	 * Is Comment In Class
	 * 
	 * @param p_oModele
	 * @return p_oModele
	 * @throws Exception
	 */
	private boolean isCommentInClass(UmlDictionary p_oDictionary, UmlComment p_oComment) throws Exception {

		boolean r_bCommentInClass = false;
	
		// Find Comment in to Class
		Collection<UmlClass> oClasses = p_oDictionary.getAllClasses();
		Iterator<UmlClass> iterCl = oClasses.iterator();
		while (iterCl.hasNext()) {
			UmlClass oClass = (UmlClass) iterCl.next();
		
			if (oClass.getComment() != null && oClass.getComment().equals(p_oComment)) {
				r_bCommentInClass = true;
				}
			}
		return (r_bCommentInClass);
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
