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

public enum XAConfigurationAttribute {
	
	NODE_NAME("name"),
	/**
	 * @see XAIdentificationType
	 */
	IDENTIFICATION_TYPE("identificationType"),
	/**
	 * Name of the attribute giving the unique identification of the node
	 */
	ID_ATTRIBUTE("idAttribute"),
	/**
	 * What is the content of this node ?
	 * @see XAConfigurationContentType
	 */
	CONTENT_TYPE("content");
	
	XAConfigurationAttribute(String _name){
		this.name=_name;
	}
	
	private String name;
	
	public String getName(){
		return name;
	}

	public String toString(){
		return name;
	}
	
	public boolean equals(String _name){
		return name.equalsIgnoreCase(_name);
	}
	
}
