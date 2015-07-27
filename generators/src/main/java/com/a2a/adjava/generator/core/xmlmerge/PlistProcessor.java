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
package com.a2a.adjava.generator.core.xmlmerge;

import java.io.ByteArrayInputStream;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.generator.core.GeneratorUtils;
import com.a2a.adjava.generator.core.xmlmerge.xa.configuration.XaConfFile;
import com.a2a.adjava.generator.core.xmlmerge.xa.configuration.model.XAConfiguration;
import com.a2a.adjava.generator.core.xmlmerge.xa.configuration.model.XAConfigurationNode;

/**
 * <p>Abstract class to generate XML files including previous user modifications</p>
 *
 * <p>Copyright (c) 2013</p>
 * <p>Company: Adeuza</p>
 *
 * @author pedubreuil
 * @since Cotopaxi
 */
public abstract class PlistProcessor {

	/**
	 * The Loagger
	 */
	private static final Logger log = LoggerFactory.getLogger(PlistProcessor.class);

	/**
	 * Transforms a flat classic PLIST Xml as a grouped XML ready for merge
	 * Attention (SMA) : à la base ce code se veut générique, mais maintentant (01/04/2014) il ne fonctionne que pour plist ios.
	 * Il faut modifier ce code pour le déporter dans des "transformers" déclarés sur chaque configuration
	 * @param p_oFileToConvert The file to convert
	 * @param p_oXaConfFile The XA configuration file
	 * @throws AdjavaException An AdjavaException that can be thrown by the merge
	 */
	public static Document fromFlatXmlToGroupedXml(File p_oFileToConvert, XaConfFile p_oXaConfFile) throws AdjavaException{

		if(p_oXaConfFile==null || ! p_oXaConfFile.requiresSiblingKeyGrouping() || p_oFileToConvert == null || !p_oFileToConvert.isFile()){
			log.warn("[AbstractXmlMergeGenerator#fromFlatXmlToGroupedXml] cannot convert this file.");
			return null;
		}
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setEntityResolver(new EntityResolver() {

				@Override
				public InputSource resolveEntity (String p_sPublicId, String p_sSystemId)
				{
					return new InputSource(new ByteArrayInputStream(new byte[]{}));

				}
			});
			Document xmlBeforeXsl = null;
			int numberOfCycles = 0;
			final int maxNumberOfCycles = 10;
			Exception exception = null;
			while(xmlBeforeXsl == null && numberOfCycles < maxNumberOfCycles) {
				log.debug("[AbstractXmlMergeGenerator#fromFlatXmlToGroupedXml] FILEPATH : "+ p_oFileToConvert.getAbsolutePath());
				try {
					xmlBeforeXsl = builder.parse(p_oFileToConvert);
				}
				catch (Exception e) {
					exception = e;
					log.debug("[AbstractXmlMergeGenerator#fromFlatXmlToGroupedXml] catch parse exception");

				}
				finally {
					numberOfCycles++;
					log.debug("**********");
				}

			}
			if(xmlBeforeXsl == null) {
				throw exception;
			}

			NodeList keysList = xmlBeforeXsl.getElementsByTagName("key");
			//    		XAConfigurationNode groupingNode = XAConfigurationNode.getArtificialGroupingNode();
			XAConfigurationNode groupingNode = XAConfiguration.getInstance().getNode(p_oXaConfFile.getName(), "artificialGroupingForXmlMerge");


			log.debug("[AbstractXmlMergeGenerator#fromFlatXmlToGroupedXml] "+keysList.getLength()+" nodes '"+p_oXaConfFile.getKeyForSiblingsNodeName()+"' found");

			for(int i=0;i<keysList.getLength();i++){
				Node currentKey = keysList.item(i);
				// log.debug(">>>>> key: "+currentKey.getNodeName()+" = "+currentKey.getTextContent());

				Node currentAssociatedNode = null;
				Node currentSibling = currentKey.getNextSibling();
				while(currentAssociatedNode == null && currentSibling!=null){
					if(Node.ELEMENT_NODE == currentSibling.getNodeType() || Node.ENTITY_NODE== currentSibling.getNodeType() ) {
						currentAssociatedNode = currentSibling;
					}
					else {
						currentSibling = currentSibling.getNextSibling();
					}
				}

				Node currentParent = currentKey.getParentNode();
				Element containerNode = xmlBeforeXsl.createElement(groupingNode.getName());

				currentParent.insertBefore(containerNode, currentKey);

				containerNode.setAttribute(groupingNode.getIdAttribute()[0], currentKey.getTextContent());
				// log.debug("[AbstractXmlMergeGenerator#fromFlatXmlToGroupedXml] moving '"+currentKey.getNodeName()+"' and '"+currentAssociatedNode.getNodeName()+"' into '"+containerNode.getNodeName()+"' ");

				currentKey.normalize();
				containerNode.appendChild(currentKey);

				if(currentAssociatedNode!=null){
					currentAssociatedNode.normalize();
					containerNode.appendChild(currentAssociatedNode);
				}

			}

			// Ajout du 01/04
			// une fois l'agglométation des balises clé et valeur des dictionnaires
			String[] uniqueNames = new String[]{"name", "_include"};

			for(int dictNodeIndex = 0; dictNodeIndex < xmlBeforeXsl.getElementsByTagName("dict").getLength(); dictNodeIndex++){
				String nodeNameValue = "rootDict";
				Element dictNode = (Element)xmlBeforeXsl.getElementsByTagName("dict").item(dictNodeIndex);
				NodeList artificialNodes = dictNode.getChildNodes();
				for(int artificialNodeIndex = 0 ; artificialNodeIndex < artificialNodes.getLength(); artificialNodeIndex++) {
					Node currentNode = artificialNodes.item(artificialNodeIndex);
					if(currentNode instanceof Element) {
						Element artificialNode = (Element)currentNode;
						for(String uniqueName : uniqueNames) {
							if(artificialNode.getAttribute("key").equalsIgnoreCase(uniqueName)) {
								Node artificialNameNode = artificialNode.getElementsByTagName("string").item(0);
								if(artificialNameNode != null) {
									nodeNameValue = artificialNameNode.getTextContent();
								}
								break;
							}
						}							
					}
				}
				if("rootDict".equals(nodeNameValue) && "artificialGroupingForXmlMerge".equals(dictNode.getParentNode().getNodeName())) {
					Node parentDictNode = dictNode.getParentNode();
					if(parentDictNode instanceof Element) {
						Element parentDictElement = (Element)parentDictNode;
						nodeNameValue = parentDictElement.getAttribute("key");
					}
				}
				dictNode.setAttribute("key", nodeNameValue);

				for(int artificialNodeIndex = 0 ; artificialNodeIndex < artificialNodes.getLength(); artificialNodeIndex++) {
					Node currentNode = artificialNodes.item(artificialNodeIndex);
					if(currentNode instanceof Element) {
						Element artificialNode = (Element)currentNode;
						String currentNodeKeyValue = artificialNode.getAttribute("key");
						String fullNodeKeyValue = currentNodeKeyValue.concat("_"+nodeNameValue);
						artificialNode.setAttribute("key", fullNodeKeyValue);
					}
				}


			}

			// 	File tempForTesting = new File(fileToConvert.getParentFile(),"test-"+fileToConvert.getName());
			//log.debug("[AbstractXmlMergeGenerator#fromFlatXmlToGroupedXml] write XML into "+fileToConvert.getPath());
			GeneratorUtils.clearEmptyNodes(xmlBeforeXsl);
			log.debug("[AbstractXmlMergeGenerator#fromFlatXmlToGroupedXml] XML converted (from flat to grouped)");
			return xmlBeforeXsl;
		}
		catch ( Exception e) {
			throw new AdjavaException("[AbstractXmlMergeGenerator#fromFlatXmlToGroupedXml] problem while converting the file "+p_oFileToConvert.getPath()+" from flat XML to grouped XML",e);
		}
	}

	/**
	 * Transforms a grouped merged XML to a flat PLIST Xml format
	 * @param p_oFileToConvert The file to convert
	 * @param p_oXaConfFile The XA configuration file
	 * @throws AdjavaException An AdjavaException that can be thrown by the merge
	 */
	public static Document fromGroupedXmlToFlatXml(File p_oFileToConvert, XaConfFile p_oXaConfFile) throws AdjavaException{

		if(p_oXaConfFile==null || ! p_oXaConfFile.requiresSiblingKeyGrouping() || p_oFileToConvert == null || !p_oFileToConvert.isFile()){
			log.warn("[AbstractXmlMergeGenerator#fromGroupedXmlToFlatXml] cannot convert this file.");
			return null;
		}

		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document xmlBeforeXsl = builder.parse(p_oFileToConvert);
			XAConfigurationNode groupingNode = XAConfiguration.getInstance().getNode(p_oXaConfFile.getName(), "artificialGroupingForXmlMerge");

			NodeList groupedNodesList = xmlBeforeXsl.getElementsByTagName(groupingNode.getName());

			log.debug("[AbstractXmlMergeGenerator#fromGroupedXmlToFlatXml] "+groupedNodesList.getLength()+" nodes '"+groupingNode.getName()+"' found");

			for(int groupIndex=groupedNodesList.getLength()-1;groupIndex>=0;groupIndex--){
				Node artificialGroup = groupedNodesList.item(groupIndex);
				Node currentParent = artificialGroup.getParentNode();

				while (artificialGroup.hasChildNodes()) {
					currentParent.insertBefore(artificialGroup.getFirstChild(), artificialGroup);
				}

				currentParent.removeChild(artificialGroup);
			}


			// Ajout du 01/04
			// une fois l'agglométation des balises clé et valeur des dictionnaires
			for(int dictNodeIndex = 0; dictNodeIndex < xmlBeforeXsl.getElementsByTagName("dict").getLength(); dictNodeIndex++){
				Element dictNode = (Element)xmlBeforeXsl.getElementsByTagName("dict").item(dictNodeIndex);
				if(dictNode.hasAttribute("key")) {
					dictNode.removeAttribute("key");
				}
			}

			// log.debug("[AbstractXmlMergeGenerator#fromGroupedXmlToFlatXml] write XML into "+fileToConvert.getAbsolutePath());
			GeneratorUtils.clearEmptyNodes(xmlBeforeXsl);
			log.debug("[AbstractXmlMergeGenerator#fromGroupedXmlToFlatXml] XML converted (from grouped to flat)");
			return xmlBeforeXsl;
		}
		catch ( Exception e) {
			throw new AdjavaException("[AbstractXmlMergeGenerator#fromGroupedXmlToFlatXml] problem while converting the file "+p_oFileToConvert.getPath()+" from grouped XML to flat XML",e);
		}
	}

}
