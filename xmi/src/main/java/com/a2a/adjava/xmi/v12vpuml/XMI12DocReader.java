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
package com.a2a.adjava.xmi.v12vpuml;

import java.util.List;

import org.dom4j.Element;

/**
 * <p>Permet de lire la documentation sur les entites du modele</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */

public class XMI12DocReader {

	private static XMI12DocReader docReader = null ;
	
	/**
	 * @return
	 */
	protected static XMI12DocReader getInstance() {
		if ( docReader == null ) {
			docReader = new XMI12DocReader();
		}
		return docReader ;
	}
	
	/**
	 * 
	 */
	private XMI12DocReader() {
	}
	
	/**
	 * Lit la documentation sur une entite documentable
	 * @param p_xElement noeud xml de l'entite
	 * @param p_oDocumentable instance de l'entite sur laquelle definir la doc
	 * @param p_oConfig configuration du projet
	 * @param p_sElementIdentifiant identifiant de l'element utilise pour les messages d'errreur
	 */
	public String readDoc( Element p_xElement, String p_sElementIdentifiant) {
		String r_sDocumentation = "";
		Element xTaggedValues = p_xElement.element("ModelElement.taggedValue");
		if(xTaggedValues!=null){
			for (Element xDocumentation : (List<Element>) xTaggedValues.elements("TaggedValue")) {
				String sTag = xDocumentation.attributeValue("tag");
				if("documentation".equals(sTag)){
					r_sDocumentation = xDocumentation.attributeValue("value");
				}
			}
		}
		return r_sDocumentation ;
	}
}
