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

public enum XAConfigurationContentType {
	
	NONE("none"),
	TEXT("text"),
	TEXT_CHILDREN("text","children"),
	CHILDREN("children");
	
	
	
	
	XAConfigurationContentType(String... _name){
		this.name=_name;
	}
	
	private String[] name;
	
	public String toString(){
		String result="";
		boolean isFirst=true;
		for(String param:name){
			if(isFirst)
				isFirst=false;
			else
				result+="+";
			result+=param;
		}
		return result;
	}
	
	public boolean equals(String _name){
		if(_name ==null || _name.trim().isEmpty())
			return false;
		String[] paramAttrs = _name.split("\\+");
		if(paramAttrs.length != this.name.length)
			return false;
		
		for(String paramAttr:paramAttrs){
			boolean attrFound=false;
			for(String thisAttr:name){
				if(thisAttr.equalsIgnoreCase(paramAttr.trim()))
					attrFound=true;
			}
			if(!attrFound)
				return false;
		}
		return true;
	}
	
	public static XAConfigurationContentType fromString(String val){
		for (XAConfigurationContentType type : XAConfigurationContentType.values()) {
			if(type.equals(val))
				return type;
		}
		return null;
	}
	
	/**
	 * Indique si le noeud possède du contenu texte
	 * @return true si le noeud possède du text
	 */
	public boolean hasText(){
		return this.equals(XAConfigurationContentType.TEXT) || this.equals(XAConfigurationContentType.TEXT_CHILDREN);
	}

}
