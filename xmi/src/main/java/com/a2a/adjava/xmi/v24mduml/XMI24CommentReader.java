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
package com.a2a.adjava.xmi.v24mduml;

import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.MEMBEREND_NODE;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.NAME_ATTR;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.OWNEDCOMMENT_BODY_ATTR;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.OWNEDCOMMENT_NODE;
import static com.a2a.adjava.xmi.v24mduml.XMI24Constants.ANNOTATEDELEMENT_NODE;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.OWNEDEND_NODE;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.PACKAGEELEMENT_TYPE_ASSOCIATION;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.XMIID2011_ATTR;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.XMIIDREF2011_ATTR;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.XMITYPE_ATTR;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.uml.UmlAssociation;
import com.a2a.adjava.uml.UmlAssociationClass;
import com.a2a.adjava.uml.UmlAssociationEnd;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlComment;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlUsage;
import com.a2a.adjava.uml.UmlAssociationEnd.AggregateType;
import com.a2a.adjava.utils.StrUtils;

/**
 * Documentation reader in model
 * @author lmichenaud
 *
 */
public final class XMI24CommentReader {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(XMI24OperationReader.class);

	/**
	 * Singleton instance
	 */
	private static XMI24CommentReader commentReader = new XMI24CommentReader();
		
	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	protected static XMI24CommentReader getInstance() {
		return commentReader ;
	}
	
	/**
	 * Constructor
	 */
	private XMI24CommentReader() {
		// Private because singleton instance
	}
	
	/**
	 * Read association
	 * @param p_xAssociation association node
	 * @param p_oModelDictionary model dictionary
	 */
	@SuppressWarnings("unchecked")
	protected void readComment(Element p_xComment, UmlDictionary p_oModelDictionary) {
	
//## Comment of package
//<packagedElement xmi:type='uml:Package' xmi:id='_17_0_5_1_20d60506_1433326852298_598711_3703' name='extractnote'>
//	<ownedComment xmi:type='uml:Comment' xmi:id='_17_0_5_1_20d60506_1433327061246_924695_3713' body='CommentCorps'>
//		<annotatedElement xmi:idref='_17_0_5_1_20d60506_1433323758996_361647_3591'/>
//	</ownedComment>
		
		// Extract Comment
		String sXmiId = p_xComment.attributeValue(XMIID2011_ATTR);
		String sName = p_xComment.attributeValue(NAME_ATTR);
		String sBody = p_xComment.attributeValue(OWNEDCOMMENT_BODY_ATTR);
//		if ( sBody != null ) {
//			if(sBody.contains("<html>")) {
//				MessageHandler.getInstance().addWarning("The comment must not be in html", sBody);
//				sBody = StringUtils.substringBetween(sBody, "<p>", "</p>");
//				sBody = StringUtils.replace(sBody, "\r\n", "");
//				sBody = StringUtils.replace(sBody, "\n", "");
//				sBody = sBody.trim();
//			}
//		}
		log.debug("comment: {}, id: {}", sBody, sXmiId);

		// Add Comment to ModelDictionary
		UmlComment oComment = new UmlComment(sName, null, sBody);
		p_oModelDictionary.registerComment(sXmiId, oComment);

			
		// Add comment to classes
		List<Element> xMembers = (List<Element>) p_xComment.elements(ANNOTATEDELEMENT_NODE);
		if (xMembers != null) {
			for( Element xAnnotated : xMembers) {
				// Search Class and Add Comment
				String sRefId = xMembers.get(0).attributeValue(XMIIDREF2011_ATTR);
				UmlClass oClass = p_oModelDictionary.getClassById(sRefId);
				if (oClass != null) {
					oClass.setComment(oComment);
				}
			}
		}
	}
}
