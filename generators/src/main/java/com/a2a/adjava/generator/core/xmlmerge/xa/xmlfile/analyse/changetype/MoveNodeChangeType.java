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
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.process.MergeProcessor;

/**
 * Traitement de déplacement d'un noeud
 * @author smaitre
 */
public class MoveNodeChangeType extends AbstractChangeType implements ItfChangeType {

	/**
	 * Logger of this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(MoveNodeChangeType.class);

	@Override
	public void processChange(Change p_oChange, Document p_oDoc, Document p_oDoc2) throws AdjavaException {

		LOG.debug("[MoveNodeChangeType#processChange]  "+p_oChange.getNewNode());


		Element oParentNewNodeElt = p_oChange.getNewNode().getParent().retrieveSingleNode(p_oDoc); //récupération de l'élément parent
		if (oParentNewNodeElt==null) {
			// des noeuds intermédiaires sans id doivent être ajoutés ?
			throw new AdjavaException("[MoveNodeChangeType#processChange] new node's parent "+p_oChange.getNewNode().getParent()+" not found in the existing document (modified by user) !");
		}


		//récupération de l'élément frère
		XANode oBrotherNewNode = p_oChange.getNewNode().getBrother();
		Element oBrotherNewNodeElt = null;
		while(oBrotherNewNode!=null && oBrotherNewNodeElt==null) {
			oBrotherNewNodeElt = oBrotherNewNode.retrieveSingleNode(p_oDoc); //recupération du frère
			oBrotherNewNode = oBrotherNewNode.getBrother();
		}


		// STEP 1: remove node from the old location
		Element oOldNodeElt = p_oChange.getOldNode().retrieveSingleNode(p_oDoc);
		if (oOldNodeElt == null) {
			oOldNodeElt = p_oChange.getOldNode().retrieveSingleNode(p_oDoc2);
		}
		if (oOldNodeElt!=null) {
			oOldNodeElt.detach();

			// STEP 2: add node into the new location
			if (oBrotherNewNodeElt==null && p_oChange.getNewNode().getBrother()!=null) {
				//pas de frère trouvé on le met en dernière position
				oParentNewNodeElt.add(oOldNodeElt);
			} else if (p_oChange.getNewNode().getBrother()==null) {
				oParentNewNodeElt.elements().add(0,oOldNodeElt);
				} else {
					oParentNewNodeElt.elements().add(oParentNewNodeElt.elements().indexOf(oBrotherNewNodeElt)+1, oOldNodeElt);
				}
			} 
//			else {
//			MergeProcessor.logConflict(p_oChange,"The node to move is not found in the existing document (modified by user) !");
//		}
	}

	@Override
	public void simulateProcessChange(Change p_oChange, Document p_oDoc, Document p_oDoc2)
			throws AdjavaException {
		LOG.debug("[MoveNodeChangeType#processChange]  "+p_oChange.getNewNode());


//		Element parentNewNodeElt = p_oChange.getNewNode().getParent().retrieveSingleNode(p_oDoc); //récupération de l'élément parent
//		if (parentNewNodeElt==null) {
			// des noeuds intermédiaires sans id doivent être ajoutés ?
//			throw new AdjavaException("[MoveNodeChangeType#processChange] new node's parent "+p_oChange.getNewNode().getParent()+" not found in the existing document (modified by user) !");
//		}


		//récupération de l'élément frère
//		XANode brotherNewNode = p_oChange.getNewNode().getBrother();
//		Element brotherNewNodeElt = null;
//		while(brotherNewNode!=null && brotherNewNodeElt==null) {
//			brotherNewNodeElt = brotherNewNode.retrieveSingleNode(p_oDoc); //recupération du frère
//			brotherNewNode = brotherNewNode.getBrother();
//		}


		// STEP 1: remove node from the old location
		Element oOldNodeElt = p_oChange.getOldNode().retrieveSingleNode(p_oDoc);
		if (oOldNodeElt!=null) {
			LOG.debug("No simulation changes for MoveNodeChangeType");

//			oldNodeElt.detach();
//
//			// STEP 2: add node into the new location
//			if (brotherNewNodeElt==null && p_oChange.getNewNode().getBrother()!=null) {
//				//pas de frère trouvé on le met en dernière position
//				parentNewNodeElt.add(oldNodeElt);
//			}
//			else if (p_oChange.getNewNode().getBrother()==null) {
//				parentNewNodeElt.elements().add(0,oldNodeElt);
//			}
//			else {
//				parentNewNodeElt.elements().add(parentNewNodeElt.elements().indexOf(brotherNewNodeElt)+1, oldNodeElt);
//			}

		}
		else {
			MergeProcessor.logConflict(p_oChange,"The node to move is not found in the existing document (modified by user) !");
		}		
	}
}
