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
package com.a2a.adjava.xmi.v21mduml;

import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.OWNEDCOMMENT_BODY_ATTR;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.OWNEDCOMMENT_NODE;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import com.a2a.adjava.messages.MessageHandler;

/**
 * Documentation reader in model
 * @author lmichenaud
 *
 */
public final class XMI21DocReader {

	/**
	 * Singleton instance
	 */
	private static XMI21DocReader docReader = new XMI21DocReader();
		
	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	protected static XMI21DocReader getInstance() {
		return docReader ;
	}
	
	/**
	 * Constructor
	 */
	private XMI21DocReader() {
		// Private because singleton instance
	}
	
	/**
	 * Read the documentation on element
	 * @param p_xElement noeud xml de l'entite
	 * @return documentation in the specified language
	 */
	public String readDoc( Element p_xElement, String p_sModelTag ) {
		String r_sDocumentation = StringUtils.EMPTY;
	
//## Doc example
//	<ownedComment xmi:type='uml:Comment' xmi:id='_17_0_1_2_16a6041b_1356010853125_861632_1999' body='documentation de la classe simulation&#10;&#10;&lt; &gt; &quot; &#39;'>
//		<annotatedElement xmi:idref='_17_0_1_2_16a6041b_1355752086323_676713_1819'/>
//	</ownedComment>		
		
		Element xDoc = p_xElement.element(OWNEDCOMMENT_NODE);
		if ( xDoc != null ) {
			String sBody = xDoc.attributeValue(OWNEDCOMMENT_BODY_ATTR);
			if ( sBody != null ) {
				r_sDocumentation = sBody;				
			}
		}
		if(r_sDocumentation.contains("<html>"))
		{
			MessageHandler.getInstance().addWarning("The documentation for the attribute {} must not be in html", p_xElement.getName());
			r_sDocumentation = StringUtils.substringBetween(r_sDocumentation, "<p>", "</p>");
			r_sDocumentation = StringUtils.replace(r_sDocumentation, "\r\n", "");
			r_sDocumentation = StringUtils.replace(r_sDocumentation, "\n", "");
			r_sDocumentation = r_sDocumentation.trim();
		}
		return r_sDocumentation ;
	}
}
