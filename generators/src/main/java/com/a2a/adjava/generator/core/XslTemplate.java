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
package com.a2a.adjava.generator.core;

/**
 * Enum of the XSL Template files
 * TODO: liste des XSL à compléter
 */
public enum XslTemplate {

	LABEL("label"),
	
	XIB_FIXED_LIST_ITEM("xib-fixedlistitem"),
	
	ACTION("action"),
	
	ACTION_INTERFACE("action-interface"),
	
	ADAPTER("adapter"),
	/**
	 * Xsl template for story board
	 */
	CORE_DATA_MODEL("coredatamodel"),

	/**
	 * Xsl template for story board
	 */
	STORYBOARD("storyboard"),
	
	/**
	 * Xsl template for plist
	 */
	FORM_PLIST_TEMPLATE("form-plist"),
	
	/**
	 * Xsl template for section plist
	 */
	SECTION_PLIST_TEMPLATE("section-plist"),
	
	/**
	 * Xsl template for section plist
	 */
	SECTION_FOR_LISTITEM_PLIST_TEMPLATE("section-listitem-plist"),

	/**
	 * Xsl template for workspace plist
	 */
	WORK_PLIST_TEMPLATE("workspace-plist"),
	
	/**
	 * Xsl template for plist
	 */
	FRAMEWORK_PLIST_TEMPLATE("framework-plist"),

	/**
	 * Xsl template for form xib for multi list
	 */
	XIB_EXPANDABLE_LIST_ITEM("xib-expandablelist-section"),
	
	/**
	 * Xsl template for form xib for fixed list
	 */
	XIB_PICKER_LIST_SELECTED_ITEM("xib-pickerlist-selected-item"),
	
	/**
	 * Xsl template for form xib for fixed list
	 */
	XIB_PICKER_LIST_ITEM("xib-pickerlist-item"),
	
	/**
	 * Xsl template for form plist for picker list selected item
	 */
	SECTION_FOR_COMBO_SELECTED_ITEM_PLIST_TEMPLATE ("form-combo-selected-item-plist"),

	
	/**
	 * Xsl template for form plist for picker list selected item
	 */
	SECTION_FOR_COMBO_ITEM_PLIST_TEMPLATE ("form-combo-item-plist"),
	
	/**
	 * Xsl template for story board
	 */
	TYPHOONCONFIG_TEMPLATE ( "typhoon-config"),
	
	/**
	 * Xsl template for cell configuration
	 */
	CELL_CONF_TEMPLATE ("cell-conf"),
	
	/**
	 * Xsl template for cell configuration
	 */
	SECTION_CONF_TEMPLATE ( "section-conf"),
	
	/**
	 * Xsl template for HTML5 JSon create table
	 */
	HTML5_CREATE_JSON_TABLES ( "database/json-schema-create-usermodel"),
	
	/**
	 * Xsl template for HTML5 JSon drop table
	 */
	HTML5_DROP_JSON_TABLES ( "database/json-schema-drop-usermodel");


	
	
	XslTemplate(String p_sName){
		this.fileName=p_sName;
	}
	
	private String fileName;
	private static final String EXTENSION="xsl";

	public String getFileName(){
		return fileName+"."+EXTENSION;
	}

	public String toString(){
		return fileName+"."+EXTENSION;
	}
	
	public boolean equals(String p_sName){
		return this.getFileName().equalsIgnoreCase(p_sName);
	}
	public boolean equals(XslTemplate p_oXsl){
		return this.getFileName().equals(p_oXsl.getFileName());
	}
	
	public static String getExtension(){
		return EXTENSION;
	}
	
}
