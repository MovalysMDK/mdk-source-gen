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
import com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.process.MergeProcessor;

/**
 * Traitement de la modification d'un noeud
 * @author smaitre
 */
public class ModifyNodeChangeType extends AbstractChangeType implements ItfChangeType {

	/**
	 * Logger
	 */
	private static Logger LOG = LoggerFactory.getLogger(ModifyNodeChangeType.class);

	@Override
	public void processChange(Change p_oChange, Document p_oDoc, Document p_oDoc2) throws AdjavaException {
		//System.out.println("   [MODIFY_NODE_VALUE]" + oChange.getOldNode().getValue() + " TO " + oChange.getNewNode().getValue());
		Element oElement = p_oChange.getOldNode().retrieveSingleNode(p_oDoc);
		if (oElement!=null) {
			LOG.debug("[ModifyNodeChangeType#processChange]  "+p_oChange.getOldNode()+" = "+ p_oChange.getNewNode().getValue());
			oElement.setText(p_oChange.getNewNode().getValue());
		}
	}

	@Override
	public void simulateProcessChange(Change p_oChange, Document p_oDoc, Document p_oDoc2)
			throws AdjavaException {

		Element oElement = p_oChange.getOldNode().retrieveSingleNode(p_oDoc);
		if (oElement == null) {
			MergeProcessor.logConflict(p_oChange,"The node to modify is not found in the existing document (modified by user) !");
		}		
	}
}
