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
package com.a2a.adjava.generator.core.xmlmerge.xa.configuration.loader;


import java.nio.file.Path;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.generator.core.xmlmerge.xa.AbstractXALoaderError;
import com.a2a.adjava.generator.core.xmlmerge.xa.AbstractXAXLoader;
import com.a2a.adjava.generator.core.xmlmerge.xa.ErrorType;


/**
 * Loader abastrait pour les fichiers xml
 * @author smaitre
 *
 * @param <ERROR_TYPE> le type de l'erreur
 * @param <ERROR_CLASS> la classe représentative de l'erreur
 */
public abstract class AbstractConfigurationFileLoader<ERROR_TYPE extends ErrorType, ERROR_CLASS extends AbstractXALoaderError<ERROR_TYPE>>  extends AbstractXAXLoader<ERROR_TYPE, ERROR_CLASS>{

	/** le logger à utiliser */
	private static final Logger LOG = LoggerFactory.getLogger(AbstractConfigurationFileLoader.class);

	/** l'ensemble des noms des noeuds racines à traiter */
	private String[] rootElement = null;
			
	/**
	 * Constructeur
	 * @param p_sPath le chemin du répertoire où trouver les fichiers à lire
	 * @param p_sExtension l'extension des fichiers devant être lus
	 * @param p_sRootElement l'ensemble des noms des noeuds racines à traiter 
	 */
	public AbstractConfigurationFileLoader(Path p_sPath, String p_sExtension,String p_sXaConfName, String...p_sRootElement) {
		super(p_sPath, p_sExtension,p_sXaConfName);
		this.rootElement = p_sRootElement;
	}
	
	/**
	 * Traitement du noeud principale, on cherche tous les sous noeuds définis dans rootElement
	 * Attention le contenu du tableau rootElement peut être de la forme x/y/z
	 * @param p_oFilePath le chemin actuel du traitement (utilisé pour la localisation des erreurs)
	 * @param p_oDoc le document xml à traiter
	 * @param p_bHeader
	 * @param p_sXaConfName
	 * 
	 * @throws AdjavaException
	 */
	@Override
	protected void treat(Path p_oFilePath,  Document p_oDoc, boolean p_bHeader, String p_sXaConfName) throws AdjavaException {
		
		p_sXaConfName = p_oFilePath.getFileName().toString();
		
		String[] sPaths = null;
		List<Element> oList = null;
		Element oCurrent = null;
		int i = 0;
		for(String sRoot : this.rootElement) {
			oCurrent = p_oDoc.getRootElement();
			sPaths = sRoot.split("/");
			i = 0;
			for(String sSubPath : sPaths) {
				i++;
				if (i == sPaths.length) {
					LOG.debug("Cherche une liste " + sSubPath);
					oList = oCurrent.elements(sSubPath);
				}
				else {
					oCurrent = oCurrent.element(sSubPath);
					if (oCurrent == null) {
						oList = null;
						break;
					}
				}
			}
			
			if (oList!=null) {
				i = 0;
				for(Element oElement : oList) {
					i++;
					this.treatMainsElements(p_oFilePath, oElement,i);
				}
			}
		}
		
		LOG.debug("[AbstractConfigurationFileLoader#treat] configuration file processed");

	}
	
	/**
	 * Traitement des éléments principaux
	 * @param p_sPath le chemin actuel du traitement (utilisé pour la localisation des erreurs)
	 * @param p_oItem l'item à traiter
	 * @param p_iPosition la position de l'élément à traiter dans le flux
	 * 
	 * @throws AdjavaException
	 */
	protected abstract void treatMainsElements(Path p_sPath, Element p_oItem, int p_iPosition) throws AdjavaException;


}
