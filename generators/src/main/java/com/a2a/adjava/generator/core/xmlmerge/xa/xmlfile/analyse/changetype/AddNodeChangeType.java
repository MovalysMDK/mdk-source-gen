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
package com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.analyse.changetype;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.analyse.AbstractChangeType;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.analyse.Change;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.analyse.ItfChangeType;
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.model.XANode;

/**
 * Traitement de l'ajout d'un noeud
 * @author smaitre
 */
public class AddNodeChangeType extends AbstractChangeType implements ItfChangeType {

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(AddNodeChangeType.class);

	@Override
	public void processChange(Change p_oChange, Document p_oDoc, Document p_oDoc2) throws AdjavaException {
		LOG.debug("[AddNodeChangeType#processChange]  "+p_oChange.getNewNode());
		
		Element parentNewNodeElt = p_oChange.getNewNode().getParent().retrieveSingleNode(p_oDoc);
		if(parentNewNodeElt == null) {
			throw new AdjavaException("[AddNodeChangeType#processChange] new node's parent "+p_oChange.getNewNode().getParent()+" not found in the existing document (modified by user) !");
		}
		
		XANode brotherNewNode = p_oChange.getNewNode().getBrother();
		Element brotherNewNodeElt = null;
		while(brotherNewNode!=null && brotherNewNodeElt==null) {
			brotherNewNodeElt = brotherNewNode.retrieveSingleNode(p_oDoc); //recupération du frère
			brotherNewNode = brotherNewNode.getBrother();
		}
		if (brotherNewNodeElt==null && p_oChange.getNewNode().getBrother()!=null) {
			//pas de frère trouvé on le met en dernière position
			parentNewNodeElt.add(p_oChange.getNewNode().createNode(parentNewNodeElt));
		}
		else if (p_oChange.getNewNode().getBrother()==null) {
			parentNewNodeElt.elements().add(0,p_oChange.getNewNode().createNode(parentNewNodeElt));
		}
		else {
			parentNewNodeElt.elements().add(parentNewNodeElt.elements().indexOf(brotherNewNodeElt)+1, p_oChange.getNewNode().createNode(parentNewNodeElt));
		}
	}

	@Override
	public void simulateProcessChange(Change p_oChange, Document p_oDoc, Document p_oDoc2)
			throws AdjavaException {
		LOG.debug("No simulation for changes of type AddNodeChangeType");
	}
	
	
}
