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
package com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.loader;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.generator.core.xmlmerge.xa.AbstractXAXLoader;
import com.a2a.adjava.generator.core.xmlmerge.xa.configuration.model.XAConfiguration;
import com.a2a.adjava.generator.core.xmlmerge.xa.configuration.model.XAConfigurationNode;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.model.XAAttribute;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.model.XAFile;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.model.XAFiles;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.model.XANode;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.model.XAPath;

/**
 * Loader chargeant les fichiers xml
 * @author smaitre
 *
 */
public class XmlFileLoader extends AbstractXAXLoader<XMLFILE_LOADER_ERROR_TYPE, XmlFileLoaderError> {
	
	/**
	 * Ensemble des types de fichiers
	 * @author smaitre
	 *
	 */
	public enum Type {
		/** fichier A (ancienne génération) */
		OLDGEN, 
		/** fichier B (nouvelle génération) */
		NEWGEN, 
		/** fichier C (ancienne génération + modification utilisateur) */
		MOD
	}
	
	/** le log à utiliser */
	private static Logger LOG = LoggerFactory.getLogger(XmlFileLoader.class);
	
	/** le type de chargement en cours */
	private Type type = null;
	
	/**
	 * Constructeur
	 * @param p_sPath le chemin dans lequel on doit charger les fichiers
	 * @param p_oType le type de fichier que l'on va charger
	 * @param p_sSuffix l'extension des fichiers à charger
	 */
	public XmlFileLoader(Path p_sPath, Type p_oType, String p_sSuffix, String p_sXaConfName) {
		super(p_sPath, p_sSuffix,p_sXaConfName);
		this.type = p_oType;
		// LOG.debug("[XmlFileLoader#constructor]  p_sPath = "+p_sPath);
		// LOG.debug("[XmlFileLoader#constructor]  p_oType = "+p_oType.toString());
		// LOG.debug("[XmlFileLoader#constructor]  p_sSuffix = "+p_sSuffix);
	}

	/**
	 * Création de l'erreur
	 */
	@Override
	protected XmlFileLoaderError createError(XMLFILE_LOADER_ERROR_TYPE p_oType,
			String p_sPath, int p_iPosition) {
		return new XmlFileLoaderError(p_oType, p_sPath, p_iPosition);
	}

	/**
	 * {@inheritDoc}
	 * @throws AdjavaException 
	 */
	@Override
	protected void treat(Path filePath, Document p_oDoc, boolean p_bHeader, String xaConfName) throws AdjavaException {
		String rootName = p_oDoc.getRootElement().getName();
		
		if(XAConfiguration.getInstance().getNode(xaConfName,rootName) == null){
			LOG.warn("[XmlFileLoader#treat] the root node '"+rootName+"' of the file " +filePath+" is not defined in the configuration file '"+xaConfName+"'");
			return;
		}

		XAFile oFile = new XAFile();
		oFile.setXmlHeader(p_bHeader);
		oFile.setFilePath(filePath);
		oFile.setXaConfName(xaConfName);

		LOG.debug("[XmlFileLoader#treat]  Reference as '"+type.name()+"' the file '" + oFile.getFilePath()+"'");
		if (Type.OLDGEN.equals(type)) {
			XAFiles.getInstance().addOldGenFile(oFile);
		}
		else if (Type.NEWGEN.equals(type)) {
			XAFiles.getInstance().addNewGenFile(oFile);
		}
		else {
			XAFiles.getInstance().addModFile(oFile);
		}
		
		XAPath path = new XAPath();
		path.setFilePath(filePath);
		path.setNodePath("/" +rootName );
		path.setShortName(rootName);
		path.setPositionAbsolute(1);
		path.setPositionRelative(1);
		this.treatElement(oFile, null, null,  p_oDoc.getRootElement(), path, 1);
	}
	
	/**
	 * Traitement d'un noeud
	 * @param p_oFile le fichier en cours de traitement
	 * @param p_oParent le noeud parent
	 * @param p_oBrother le noeud frère
	 * @param p_xNode le noeud (xml) courant
	 * @param p_oPath le chemin courant
	 * @param p_iDepth la profondeur
	 * @return le noeud courant
	 * @throws AdjavaException 
	 */
	protected XANode treatElement(XAFile p_oFile, XANode p_oParent, XANode p_oBrother, Element p_xNode, XAPath p_oPath, int p_iDepth) throws AdjavaException {
		//Traitement du noeud
		//boucle sur l'ensemble des attributs
		XAConfigurationNode oConfNode = XAConfiguration.getInstance().getNode(p_oFile.getXaConfName(),p_xNode.getName());
		if (oConfNode == null) {
			//LOG.error("The node >"+p_xNode.getName()+"< not found in configuration file >"+p_oFile.getXaConfName()+"<");
			this.addError(XMLFILE_LOADER_ERROR_TYPE.MISSING_CONFIGURATION,"The definition of the node '"+p_oPath.getFullPath()+"' cannot be found in the configuration file '"+p_oFile.getXaConfName()+"'  " , p_oPath.getPositionAbsolute());
			return null;
		}
		else {
			// LOG.debug("[XmlFileLoader#treatElement]  Process node '"+p_xNode.getName()+"' (absolute='"+p_oPath.getPositionAbsolute()+"'relative='"+p_oPath.getPositionRelative()+"', depth="+p_iDepth+" , file='"+p_oFile.getBaseFileName()+"')");

			// LOG.debug("Traitement de " + p_xNode.getName());
			XANode oNode = new XANode();
			oNode.setDepth(p_iDepth);
			oNode.setParent(p_oParent);
			oNode.setBrother(p_oBrother);
			oNode.setName(p_xNode.getName());
			oNode.setConfiguration(oConfNode);
			if (p_xNode.isTextOnly() && oConfNode.getContentType().hasText()) {
				oNode.setValue(p_xNode.getText());
			}
			oNode.setPath(p_oPath);
			XAAttribute oAttribute = null;
			String sAttributeName = null;
			for(Attribute xAttribute : ((List<Attribute>)p_xNode.attributes())) {
				oAttribute = new XAAttribute();
				sAttributeName = xAttribute.getQualifiedName();
				oAttribute.setName(sAttributeName);
				oAttribute.setValue(xAttribute.getText());
				/*if (oConfNode.hasIdAttribute(oAttribute.getName())) {
					p_oPath.addId(oAttribute.getValue());
				}*/
				oNode.addAttribute(oAttribute);
			}
			if (oNode.isValid()) {
				p_oFile.addNode(oNode);
			}
			else {
				LOG.info(oNode.getName() + " is not a handled node !");
				this.addError(XMLFILE_LOADER_ERROR_TYPE.MISSING_CONFIGURATION, oNode.getName() + " is not a handled node !",p_iDepth);
			}
			
			//Traitement des fils
			XAPath path = null;
			int i = 1;
			XANode oBrother = null;
			Map<String, Integer> numbers = new HashMap<String, Integer>();
			Integer number = null;
			for(Element xSubNode : ((List<Element>)p_xNode.elements())) {
				path = new XAPath();
				path.setFilePath(p_oPath.getFilePath());
				path.setNodePath(xSubNode.getName());
				path.setShortName(xSubNode.getName());
				number = numbers.get(path.getFullPath());
				if (number == null) {
					number = new Integer(1);
				}
				else {
					number = new Integer(number.intValue() +1);
				}
				numbers.put(path.getFullPath(), number);					
				// LOG.debug(">>>>>>>>>> rel "+path.getName()+" = "+number+" -- "+numbers);
				path.setPositionAbsolute(i++);
				path.setPositionRelative(number.intValue());
				path.setParent(p_oPath);
				oBrother = treatElement(p_oFile, oNode, oBrother, xSubNode, path,p_iDepth+1);
			}
			return oNode;
		}
	}
}
