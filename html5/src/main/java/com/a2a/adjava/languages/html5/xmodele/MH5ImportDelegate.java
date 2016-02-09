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
package com.a2a.adjava.languages.html5.xmodele;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.DOMWriter;

import com.a2a.adjava.utils.StrUtils;
import com.a2a.adjava.xmodele.SClass;

/**
 * Delegate for imports
 * @author lmichenaud
 *
 */
public class MH5ImportDelegate {

	/**
	 * Imports for html5
	 */
	private SortedSet<String> objcImports = new TreeSet<>();	

	/**
	 * Delegator
	 */
	private Object delegator ;

	/**
	 * Constructor
	 * @param p_oDelegator delegator
	 */
	public MH5ImportDelegate( Object p_oDelegator ) {
		this.delegator = p_oDelegator ;
	}

	/**
	 * Add import
	 * @param p_oCategory category
	 * @param p_sImport import
	 */
	public void addImport(String p_sImport) {
		if ( StringUtils.isNotBlank(p_sImport) && !StringUtils.startsWith(p_sImport, "NS")) {
			String sImport = p_sImport;
			if(StringUtils.countMatches(p_sImport, ".") > 1) {
				sImport = StrUtils.substringAfterLastDot(p_sImport);	
			}
			SortedSet<String> listImports = this.objcImports;
			if (!listImports.contains( sImport )) {
				listImports.add(sImport);
			}
		}
	}



	/**
	 * Get imports for category
	 * @param p_sCategoryName category name
	 * @return category name
	 */
	public SortedSet<String> getImports() {
		return this.objcImports;
	}

	/**
	 * toXml
	 * @return xml for imports
	 */
	public Element toXml() {
		Element r_xObjcImports = DocumentHelper.createElement("objc-imports");
		this.genereXml(r_xObjcImports);		
		return r_xObjcImports;
	}

	/**
	 * Genere xml
	 * @param p_xParent parent node
	 */
	protected void genereXml( Element p_xParent ) {
		toXml( p_xParent, this.getImports());
	}

	/**
	 * Add import of a category to a xml node
	 * @param p_xParent parent
	 * @param p_oCat category of import
	 * @param p_listImports imports of category
	 */
	protected void toXml( Element p_xParent, Collection<String> p_listImports ) {
		for( String sImport: p_listImports ) {
			Element xImport = p_xParent.addElement("objc-import");
			xImport.addAttribute("import", sImport);
			xImport.addAttribute("import-in-function", sImport);

			if ( SClass.class.isAssignableFrom(this.delegator.getClass()) &&
					sImport.equals(((SClass)this.delegator).getMasterInterface().getName())) {
				xImport.addAttribute("self", "true");
			}
			xImport.addAttribute("scope", "local");
		}
	}

	/**
	 * xml for imports
	 * @return dom xml
	 * @throws DocumentException
	 */
	public org.w3c.dom.Document toDomXml() throws DocumentException {
		org.dom4j.Document dom4jDoc = DocumentHelper.createDocument(this.toXml());
		return new DOMWriter().write(dom4jDoc);
	}

}
