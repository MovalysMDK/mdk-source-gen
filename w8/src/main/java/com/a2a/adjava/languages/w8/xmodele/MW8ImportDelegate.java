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
package com.a2a.adjava.languages.w8.xmodele;

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

/**
 * Delegate for imports
 * @author lmichenaud
 *
 */
public class MW8ImportDelegate {

	/**
	 * Imports for objective c
	 */
	private Map<String,SortedSet<String>> imports = new TreeMap<String,SortedSet<String>>();

	/**
	 * Delegator
	 */
//	private Object delegator ;

	/**
	 * Constructor
	 * @param p_oDelegator delegator
	 */
	public MW8ImportDelegate( Object p_oDelegator ) {
		this.addCategory(MW8ImportCategory.ENUMERATION.name());
		this.addCategory(MW8ImportCategory.ENTITIES.name());
		this.addCategory(MW8ImportCategory.FACTORIES.name());
		this.addCategory(MW8ImportCategory.VALIDATORS.name());
		this.addCategory(MW8ImportCategory.DAO.name());
		this.addCategory(MW8ImportCategory.VIEWMODEL.name());
		this.addCategory(MW8ImportCategory.CONTROLLER.name());
		this.addCategory(MW8ImportCategory.ACTION.name());
//		this.delegator = p_oDelegator ;
	}

	/**
	 * Add import
	 * @param p_sCategory category
	 * @param p_sImport import
	 */
	public void addImport(String p_sCategory, String p_sImport) {
		if ( StringUtils.isNotBlank(p_sImport)) {
			String sImport = p_sImport;
			SortedSet<String> listImports = this.imports.get(p_sCategory);
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
		this.imports.put(p_sName, new TreeSet<String>());
	}

	/**
	 * Get imports for category
	 * @param p_sCategoryName category name
	 * @return category name
	 */
	public SortedSet<String> getImportsForCategory( String p_sCategoryName ) {
		return this.imports.get(p_sCategoryName);
	}

	/**
	 * toXml
	 * @return xml for imports
	 */
	public Element toXml() {
		Element r_xObjcImports = DocumentHelper.createElement("imports");
		this.genereXml(r_xObjcImports);		
		return r_xObjcImports;
	}

	/**
	 * Genere xml
	 * @param p_xParent parent node
	 */
	protected void genereXml( Element p_xParent ) {
		toXml( p_xParent, MW8ImportCategory.ENUMERATION.name(), this.getImportsForCategory(MW8ImportCategory.ENUMERATION.name()));
		toXml( p_xParent, MW8ImportCategory.ENTITIES.name(), this.getImportsForCategory(MW8ImportCategory.ENTITIES.name()));
		toXml( p_xParent, MW8ImportCategory.FACTORIES.name(), this.getImportsForCategory(MW8ImportCategory.FACTORIES.name()));
		toXml( p_xParent, MW8ImportCategory.VALIDATORS.name(), this.getImportsForCategory(MW8ImportCategory.VALIDATORS.name()));
		toXml( p_xParent, MW8ImportCategory.DAO.name(), this.getImportsForCategory(MW8ImportCategory.DAO.name()));
		toXml( p_xParent, MW8ImportCategory.VIEWMODEL.name(), this.getImportsForCategory(MW8ImportCategory.VIEWMODEL.name()));
		toXml( p_xParent, MW8ImportCategory.CONTROLLER.name(), this.getImportsForCategory(MW8ImportCategory.CONTROLLER.name()));
		toXml( p_xParent, MW8ImportCategory.ACTION.name(), this.getImportsForCategory(MW8ImportCategory.ACTION.name()));
	}

	/**
	 * Add import of a category to a xml node
	 * @param p_xParent parent
	 * @param p_sCat category of import
	 * @param p_listImports imports of category
	 */
	protected void toXml( Element p_xParent, String p_sCat, Collection<String> p_listImports ) {
		for( String sImport: p_listImports ) {
			Element xImport = p_xParent.addElement("import");
			xImport.addAttribute("category", p_sCat);
			xImport.addAttribute("class", sImport);
		}
	}

	/**
	 * xml for imports
	 * @return dom xml
	 * @throws DocumentException
	 */
	public org.w3c.dom.Document toDomXml() throws DocumentException {
		org.dom4j.Document oDom4jDoc = DocumentHelper.createDocument(this.toXml());
		return new DOMWriter().write(oDom4jDoc);
	}

	/**
	 * Category of import
	 * @author lmichenaud
	 *
	 */
	public enum MW8ImportCategory {
		/**
		 * Enumerations category
		 */
		ENUMERATION,
		/**
		 * Entities category
		 */
		ENTITIES,
		/**
		 * Factories category
		 */
		FACTORIES,
		/**
		 * Validators category
		 */
		VALIDATORS,
		/**
		 * Daos category
		 */
		DAO,
		/**
		 * Viewmodels category
		 */
		VIEWMODEL,
		/**
		 * Controllers category
		 */
		CONTROLLER,
		/**
		 * Actions category
		 */
		ACTION
	}
}
