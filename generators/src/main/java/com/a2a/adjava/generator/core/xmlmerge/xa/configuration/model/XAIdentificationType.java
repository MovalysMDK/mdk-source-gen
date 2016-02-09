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
package com.a2a.adjava.generator.core.xmlmerge.xa.configuration.model;

public enum XAIdentificationType {
	/**
	 * Affecte le fait que le nom du noeud suffise comme critère d'unicité
	 * si identificationTyppe = "single" alors le nom du noeud suffit, ne suffit pas dans tous les autres cas
	 */
	NAME("name",false,false,false),
	
	/**
	 * Indique que l'unicité se fait avec des informations en provenance du parent
	 * si identificationTyppe = "relativeId" alors l'unicité utilise le parent, ne l'utilise pas dans tous les autres cas
	 */

	PARENT_NAME("parent+name",false,true,false),

	/**
	 * If idAttribute value is null, it will be replaced by the position
	 */
	PARENT_NAME_ATTRIBUTE("parent+name+attr",true,true,false),

	PARENT_NAME_ATTRIBUTE_VALUE("parent+name+attr|value",true,true,true),
	
	NAME_ATTRIBUTE("name+attr",true,false,false),

	NAME_ATTRIBUTE_VALUE("name+attr|value",true,false,true),

	PARENT_NAME_SIBLING_KEY("parent+name+siblingKey",false,true,false),

	KEY_FOR_SIBLING("keyForSibling",false,true,false),

	/**
	 * Indique que ce noeud est le root du XML
	 */
	ROOT("root",false,false,false);

	private String name;	
	private boolean requiresAttr;

	private boolean requiresParent;
	private boolean valueAsKey;
	

	XAIdentificationType(String p_sName, boolean p_bRequiresAttr, boolean p_bRequiresParent, boolean p_bValueAsKey){
		name =p_sName;
		requiresAttr=p_bRequiresAttr;
		requiresParent=p_bRequiresParent;
		valueAsKey=p_bValueAsKey;
	}
	
	

	
	public static XAIdentificationType fromString(String val){
		if(val==null)
			return null;
		for (XAIdentificationType type : XAIdentificationType.values()) {
			if(type.getName().equals(val))
				return type;
		}
		return null;
	}
	
	/**
	 * If attribute not found, replace it by the position
	 * @return
	 */
	public boolean isRequiresAttribute(){
		return requiresAttr;
	}


	public boolean isRequiresParent(){
		return requiresParent;
	}
	
	public boolean isRoot(){
		return  this.equals(XAIdentificationType.ROOT);
	}
	
	public boolean isSingle(){
		return  this.equals(XAIdentificationType.ROOT) || this.equals(XAIdentificationType.NAME);

	}


	public boolean isKeyForSibling(){
		return this.equals(XAIdentificationType.KEY_FOR_SIBLING);
	}


	public String getName() {
		return name;
	}




	public boolean isValueAsKey() {
		return valueAsKey;
	}




	
}
