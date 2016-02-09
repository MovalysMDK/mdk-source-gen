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

import org.dom4j.Element;

import com.a2a.adjava.AdjavaException;

/**
 * Représente la configuration d'un noeud
 * Le but de la configuration est de donner un critère d'unicité pour un noeud
 * Plusieurs possibilités exitent pour décrire l'unicité d'un noeud :
 * 1/ unicité par le nom du noeud : single = true (dans un fichier xml il ne peut y avoir qu'un seul noeud avec ce nom là)
 * 2/ unicité par identifiant : idAttribute<>null (dans un fichier xml il ne peut y avoir qu'un seul noeud avec ce nom et cet identiifant)
 * 3/ unicité relative : relativeId = true et (idAttribute null ou pas null) (le critère d'unicité est donné par le parent avec l'identifiant du noeud ou le nom du noeud)
 * @author smaitre
 */
public class XAConfigurationNode {


	/** le nom du noeud */
	private String name = null;
	/** le nom de la clé du noeud (peut être null) */
	private String[] idAttribute = null;
	
	/** indique si le noeud possède du contenu texte */
	private XAConfigurationContentType content = XAConfigurationContentType.TEXT_CHILDREN;
	
	private XAIdentificationType identificationType=null;
	

	public XAConfigurationNode(String name,String contentType,String identificationType) throws AdjavaException{
		
		if(name==null ||name.trim().isEmpty() )
			throw new AdjavaException("the attribute >"+XAConfigurationAttribute.NODE_NAME+"< is missing in one of the configuration files");

		//if(contentType==null ||contentType.trim().isEmpty() )
		//	throw new AdjavaException("the attribute >"+XAConfigurationAttribute.CONTENT_TYPE+"< is missing for the node >"+name+"< in one of the XACONF files ");

		if(identificationType==null ||identificationType.trim().isEmpty() )
			throw new AdjavaException("the attribute >"+XAConfigurationAttribute.IDENTIFICATION_TYPE+"< is missing for the definition of the node >"+name+"< in one of the configuration files ");

		this.setContentType(contentType);
		this.setName(name);
		this.setIdentificationType(identificationType);
	}
	
	public XAConfigurationNode(Element p_oItem) throws AdjavaException{
		this(
				p_oItem.attributeValue(XAConfigurationAttribute.NODE_NAME.toString()),
				p_oItem.attributeValue(XAConfigurationAttribute.CONTENT_TYPE.toString()),
				p_oItem.attributeValue(XAConfigurationAttribute.IDENTIFICATION_TYPE.toString()));	
		
		if(this.getIdentificationType().isRequiresAttribute())
			this.setIdAttribute(p_oItem.attributeValue(XAConfigurationAttribute.ID_ATTRIBUTE.toString()));
		}

	
	public XAConfigurationContentType getContentType() {
		return content;
	}

	public void setContentType(String contentStr) {
		this.content = XAConfigurationContentType.fromString(contentStr);
		if(this.content == null){
			content = XAConfigurationContentType.TEXT_CHILDREN;
		}
	}

	
	
	/**
	 * Donne le nom du noeud pour lequel on construit la configuration
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Affecte le nom de la configuration
	 * @param name le nom de la configuration
	 */
	public void setName(String name) {
		this.name = name;
	}


	
	/**
	 * Donne l'identifiant du noeud (le nom de l'attribut servant de clé)
	 * @return le nom de l'attribut
	 */
	public String[] getIdAttribute() {
		return idAttribute;
	}
	
	/**
	 * Permet d'affecter l'identifant du noeud
	 * @param idAttribute le nom de l'attribut identifiant le noeud
	 */
	public void setIdAttribute(String idName) {
		if(idName == null || idName.trim().isEmpty()){
			//this.idAttribute = new String[0];
			this.idAttribute = null;
		}
		else{
			
			if(idName.contains("|"))
				this.idAttribute = idName.split("\\|");
			else if(idName.contains("+"))
				this.idAttribute = idName.split("\\+");
			else {
				this.idAttribute = new String[1];
				this.idAttribute[0]=idName;				
			}
			
			
				
			if(identificationType==null){
				identificationType = XAIdentificationType.NAME_ATTRIBUTE;
			}
		}
	}

	public boolean isRoot() {
		return this.identificationType.isRoot();
	}
	

	public XAIdentificationType getIdentificationType(){
		return identificationType;
	}
	
	public void setIdentificationType(String identificationType) throws AdjavaException{

		this.identificationType=XAIdentificationType.fromString(identificationType);

		if(this.identificationType == null)
			throw new AdjavaException("The identification type >"+identificationType+"< is missing or not valid for definition of the node >"+this.name+"<");

		if(this.identificationType.isRoot()){
			this.content=XAConfigurationContentType.CHILDREN;
		}
	}

	public boolean hasIdAttribute(String attributeName) {
		if(attributeName==null || this.idAttribute==null) return false;
		for(String attr:this.idAttribute){
			if(attr.equalsIgnoreCase(attributeName))
				return true;
		}
		return false;
	}
	
	
	private static final String GROUPING_NODE_NAME="artificialGroupingForXmlMerge";
	private static final String GROUPING_NODE_ATTRIBUTE="key";

	public static XAConfigurationNode getArtificialGroupingNode() throws AdjavaException{
		XAConfigurationNode p_oNode = new XAConfigurationNode(XAConfigurationNode.GROUPING_NODE_NAME,XAConfigurationContentType.CHILDREN.toString(),XAIdentificationType.PARENT_NAME_ATTRIBUTE.getName());
		p_oNode.setIdAttribute(XAConfigurationNode.GROUPING_NODE_ATTRIBUTE);
		return p_oNode;
	}

}
