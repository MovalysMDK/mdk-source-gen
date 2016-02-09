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
package com.a2a.adjava.languages.ios.xmodele;

import java.util.Collection;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
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
public class MIOSImportDelegate {

	/**
	 * Imports for objective c
	 */
	private Map<String,SortedSet<String>> objcImports = new TreeMap<String,SortedSet<String>>();	

	/**
	 * Delegator
	 */
	private Object delegator ;

	/**
	 * Constructor
	 * @param p_oDelegator delegator
	 */
	public MIOSImportDelegate( Object p_oDelegator ) {
		this.addCategory(MIOSImportCategory.ENUMERATION.name());
		this.addCategory(MIOSImportCategory.ENTITIES.name());
		this.addCategory(MIOSImportCategory.FACTORIES.name());
		this.addCategory(MIOSImportCategory.VALIDATORS.name());
		this.addCategory(MIOSImportCategory.DAO.name());
		this.addCategory(MIOSImportCategory.VIEWMODEL.name());
		this.addCategory(MIOSImportCategory.CONTROLLER.name());
		this.addCategory(MIOSImportCategory.ACTION.name());
		this.delegator = p_oDelegator ;
	}

	/**
	 * Add import
	 * @param p_oCategory category
	 * @param p_sImport import
	 */
	public void addImport(String p_sCategory, String p_sImport) {
		if ( StringUtils.isNotBlank(p_sImport) && !StringUtils.startsWith(p_sImport, "NS")) {
			String sImport = p_sImport;
			if(StringUtils.countMatches(p_sImport, ".") > 1) {
				sImport = StrUtils.substringAfterLastDot(p_sImport);	
			}
			SortedSet<String> listImports = this.objcImports.get(p_sCategory);
			if (!listImports.contains( sImport )) {
				listImports.add(sImport);
			}
		}
	}

	/**
	 * Add a category
	 * @param p_sName category name
	 */
	public void addCategory( String p_sName ) {
		this.objcImports.put(p_sName, new TreeSet<String>());
	}

	/**
	 * Get imports for category
	 * @param p_sCategoryName category name
	 * @return category name
	 */
	public SortedSet<String> getImportsForCategory( String p_sCategoryName ) {
		return this.objcImports.get(p_sCategoryName);
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
		toXml( p_xParent, MIOSImportCategory.ENUMERATION.name(), this.getImportsForCategory(MIOSImportCategory.ENUMERATION.name()));
		toXml( p_xParent, MIOSImportCategory.ENTITIES.name(), this.getImportsForCategory(MIOSImportCategory.ENTITIES.name()));
		toXml( p_xParent, MIOSImportCategory.FACTORIES.name(), this.getImportsForCategory(MIOSImportCategory.FACTORIES.name()));
		toXml( p_xParent, MIOSImportCategory.VALIDATORS.name(), this.getImportsForCategory(MIOSImportCategory.VALIDATORS.name()));
		toXml( p_xParent, MIOSImportCategory.DAO.name(), this.getImportsForCategory(MIOSImportCategory.DAO.name()));
		toXml( p_xParent, MIOSImportCategory.VIEWMODEL.name(), this.getImportsForCategory(MIOSImportCategory.VIEWMODEL.name()));
		toXml( p_xParent, MIOSImportCategory.CONTROLLER.name(), this.getImportsForCategory(MIOSImportCategory.CONTROLLER.name()));
		toXml( p_xParent, MIOSImportCategory.ACTION.name(), this.getImportsForCategory(MIOSImportCategory.ACTION.name()));
	}

	/**
	 * Add import of a category to a xml node
	 * @param p_xParent parent
	 * @param p_sCat category of import
	 * @param p_listImports imports of category
	 */
	protected void toXml( Element p_xParent, String p_sCat, Collection<String> p_listImports ) {
		for( String sImport: p_listImports ) {
			Element xImport = p_xParent.addElement("objc-import");
			xImport.addAttribute("category", p_sCat);
			xImport.addAttribute("class", sImport);
			xImport.addAttribute("header", sImport + ".h");

			if ( SClass.class.isAssignableFrom(this.delegator.getClass()) 
				&& sImport.equals(((SClass)this.delegator).getMasterInterface().getName())) {
				xImport.addAttribute("self", "true");
			}
			xImport.addAttribute("scope", "local");
		}
	}

	/**
	 * xml for imports
	 * @return dom xml
	 * @throws DocumentException exception
	 */
	public org.w3c.dom.Document toDomXml() throws DocumentException {
		org.dom4j.Document r_oDom4jDoc = DocumentHelper.createDocument(this.toXml());
		return new DOMWriter().write(r_oDom4jDoc);
	}

	/**
	 * Category of import
	 * @author lmichenaud
	 *
	 */
	public enum MIOSImportCategory {

		/**
		 * enumeration import
		 */
		ENUMERATION,
		/**
		 * entities import
		 */
		ENTITIES,
		/**
		 * factories import
		 */
		FACTORIES,
		/**
		 * validators import
		 */
		VALIDATORS,
		/**
		 * daos import
		 */
		DAO,
		/**
		 * viewmodels import
		 */
		VIEWMODEL,
		/**
		 * controllers import
		 */
		CONTROLLER,
		/**
		 * actions import
		 */
		ACTION
	}
}
