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

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.generator.core.xmlmerge.xa.configuration.XaConfFile;
import com.a2a.adjava.generator.core.xmlmerge.xa.configuration.model.XAConfiguration;
import com.a2a.adjava.generator.core.xmlmerge.xa.configuration.model.XAConfigurationNode;

/**
 * Loader chargeant les propriétés en provenance du code
 * @author smaitre
 *
 */
public class ConfigurationLoader extends AbstractConfigurationFileLoader<CONFIGURATION_LOADER_ERROR_TYPE, ConfigurationLoaderError> {
	
	/** le logger à utiliser */
	private static Logger LOG = LoggerFactory.getLogger(ConfigurationLoader.class);
	
	/**
	 * Constructeur
	 */
	public ConfigurationLoader(Path p_sPath) {
		super(p_sPath, XaConfFile.getExtension(), null, "node");
	}

	/**
	 * Création de l'erreur
	 */
	@Override
	protected ConfigurationLoaderError createError(CONFIGURATION_LOADER_ERROR_TYPE p_oType,
			String p_sPath, int p_iPosition) {
		return new ConfigurationLoaderError(p_oType, p_sPath, p_iPosition);
	}

	/**
	 * Lecture du noeud principale,
	 * lecture du nom du noeud, 
	 * de l'identifiant d'un noeud, l'identifiant n'est pas obligatoire,
	 * de l'attribut single : indique que le noeud est unique dans le xml
	 * de l'attribut text : qui indique si le noeud contient du texte ou pas
	 * de l'attribut relativeId : qui indique que l'unicité du noeud dépend d'un de ses parents
	 * @throws AdjavaException 
	 */
	@Override
	protected void treatMainsElements(Path p_sPath, Element p_oItem, int p_iPosition) throws AdjavaException  {
		
		XAConfigurationNode oNode = new XAConfigurationNode(p_oItem);
		
		if (XAConfiguration.getInstance().getNode(p_sPath.getFileName().toString(),oNode.getName())!=null) {
			this.addError(CONFIGURATION_LOADER_ERROR_TYPE.CONF_NODE_ALREADY_EXIST, p_sPath, p_iPosition);
			LOG.error("La configuration du noeud " + oNode.getName() + " existe déjà.");

		}
		else {
			XAConfiguration.getInstance().addNode(p_sPath.getFileName().toString(),oNode);
		}
	}


}
