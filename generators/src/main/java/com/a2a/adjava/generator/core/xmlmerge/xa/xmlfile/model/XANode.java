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
package com.a2a.adjava.generator.core.xmlmerge.xa.xmlfile.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.generator.core.xmlmerge.xa.configuration.model.XAConfigurationNode;

/**
 * Représente un noeud xml
 * @author smaitre
 *
 */
public class XANode {
	
	
	private static Logger LOG = LoggerFactory.getLogger(XANode.class);

	/** le log à utiliser */
	//private static Logger LOG = LoggerFactory.getLogger(XANode.class);
	
	/** le nom du noeud */
	private String name = null;
	/** sa valeur textuelle si elle existe */
	private String value = null;
	/** l'ensemble des attributs */
	private Map<String, XAAttribute> attributes = null;
	/** la configuration du noeud */
	private XAConfigurationNode configuration = null;
	/** le chemin du noeud */
	private XAPath path = null;
	/** le noeud parent */
	private XANode parent = null;
	/** le noeud frère */
	private XANode brother = null;
	/** la profondeur du noeud */
	private int depth = 1;
	
	private String uniqueXPathId=null;
	
	/**
	 * Construit un nouveau noeud
	 */
	public XANode() {
		this.attributes = new HashMap<String, XAAttribute>();
	}
	
	/**
	 * Affecte la profondeur du noeud
	 * @param p_iDepth la profondeur du noeud
	 */
	public void setDepth(int p_iDepth) {
		this.depth = p_iDepth;
	}
	
	/**
	 * Donne la profondeur du noeud
	 * @return la profondeur du noeud
	 */
	public int getDepth() {
		return this.depth;
	}
	
	/**
	 * Donne le noeud parent
	 * @return le noeud parent
	 */
	public XANode getParent() {
		return this.parent;
	}
	
	/**
	 * Affecte le noeud parent
	 * @param p_oParent le noeud parent
	 */
	public void setParent(XANode p_oParent) {
		this.parent = p_oParent;
	}
	
	/**
	 * Donne le noeud frère
	 * @return le noeud frère
	 */
	public XANode getBrother() {
		return this.brother;
	}
	
	/**
	 * Affecte le noeud frère
	 * @param p_oBrother le noeud frère
	 */
	public void setBrother(XANode p_oBrother) {
		this.brother = p_oBrother;
	}
	
	/**
	 * Ajoute un attribut
	 * @param p_oAttribute l'attribut à ajouter
	 */
	public void addAttribute(XAAttribute p_oAttribute) {
		this.attributes.put(p_oAttribute.getName(), p_oAttribute);
	}
	
	/**
	 * Donne un attribut de nom p_sKey
	 * @param p_sKey le nom de l'attribut
	 * @return un attribut
	 */
	public XAAttribute getAttribute(String p_sKey) {
		return this.attributes.get(p_sKey);
	}

	/**
	 * Donne le nom du noeud
	 * @return le nom
	 */
	public String getName() {
		return name;
	}

	/**
	 * Affecte le nom du noeud
	 * @param name le nom du noeud
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Donne la configuration associé au noeud
	 * @return une configuration
	 */
	public XAConfigurationNode getConfiguration() {
		return configuration;
	}

	/**
	 * Affecte la configuration associée
	 * @param configuration la configuration associée
	 */
	public void setConfiguration(XAConfigurationNode configuration) {
		this.configuration = configuration;
	}

	/**
	 * Donne la valeur
	 * @return la valeur du noeud (le texte associé s'il existe)
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Affecte la valeur du noeud (ie le texte associé)
	 * @param value le texte
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Indique si le noeud est managé ie qu'il est identifiable de manière unique.
	 * Pour identifier de manière unique un noeud il existe plusieurs mécaniques :
	 * 1/ identification par le nom de la balise single = true
	 * 2/ identification par un attribut clé idName !=null
	 * 3/ identification par le parent relativeId = true (en prenant en compre éventuellement un attribut clé s'il existe)
	 * @return
	 */
	public boolean isIdentifiableWithoutPosition() {
		if (this.configuration!=null) {
			boolean result = true;
			if( this.getConfiguration().getIdentificationType().isSingle() ){
				return true;
			}
			
			if( this.getConfiguration().getIdentificationType().isRequiresAttribute() ){
				result=false;
				for(String attr:this.getConfiguration().getIdAttribute()){
					result = result || this.attributes.keySet().contains(attr);
				}
			}
			
			if( this.getConfiguration().getIdentificationType().isRequiresParent() ){
				result = result && this.getDepth() > 1 ;
				if(this.getParent()!=null)
					result = result && this.getParent().isIdentifiableWithoutPosition();
				else
					return false;
			}
			
			return result;
		}
		else {
			return false;
		}
	}

	
	public boolean isValid() {
		if (this.configuration==null) {
			return false;
		}
		if( this.getConfiguration().getIdentificationType().isSingle() ){
			return true;
		}

/*		boolean result = true;
		if( this.getConfiguration().getIdentificationType().requiresAttribute() ){
			result=false;
			for(String attr:this.getConfiguration().getIdAttribute()){
				result = result || this.attributes.keySet().contains(attr);
			}
		}*/
		
		if( this.getConfiguration().getIdentificationType().isRequiresParent() ){
			if(this.getDepth() <= 1 || this.getParent()==null)
				return false;
		}
			
		return true;
	}
	/**
	 * Donne le chemin du noeud
	 * @return le chemin du noeud
	 */
	public XAPath getPath() {
		return path;
	}

	/**
	 * Affecte le chemin du noeud
	 * @param path le chemin du noeud
	 */
	public void setPath(XAPath path) {
		this.path = path;
	}



	/**
	 * Indique si le noeud courant et le nom passé en paramètre on la même valeur
	 * @param oldNode le noeud avec lequel on veut comparer
	 * @return true si les deux noeuds sont identiques
	 */
	public boolean isSameValue(XANode oldNode) {
		if (value==null && oldNode.value== null) {
			return true;
		}
		else if (value!=null && oldNode.value!=null) {
			return value.equals(oldNode.value);
		}
		else {
			return false;
		}
	}

	/**
	 * Donne l'ensemble des attributs d'un noeud
	 * @return l'ensemble des attributs d'un noeud
	 */
	public Map<String, XAAttribute> getAttributes() {
		return attributes;
	}
	
	/**
	 * Le noeud au format chaîne
	 */
	public String toString() {
		StringBuilder content = new StringBuilder();
		content.append("UniqueXPathId : ");
		content.append((this.getUniqueXPathId() != null)  ?this.getUniqueXPathId() : "null");
		content.append('\n');
		content.append("Brother UniqueXPathId : ");
		content.append((this.getBrother() != null && this.getBrother().getUniqueXPathId() != null)  ?this.getBrother().getUniqueXPathId() : "null");
		content.append('\n');
		content.append("Parent UniqueXPathId : ");
		content.append((this.getParent() != null && this.getParent().getUniqueXPathId() != null)  ?this.getParent().getUniqueXPathId() : "null");
		content.append('\n');
		content.append("Value : ");
		content.append((this.getValue() != null)  ?this.getValue() : "null");
		content.append('\n');
		return content.toString();
	}

	/**
	 * Permet de créer un noeud XML (dom4j)
	 * @param p_oMaster le noeud parent
	 * @return un nouveau noeud dom4j attention, le noeud est détaché du parent
	 */
	public Element createNode(Element p_oMaster) {
		Element oElement = p_oMaster.addElement(this.name);
		oElement.detach();//pas très propre mais semble le plus efficace
		if (value!=null) {
			oElement.setText(this.value);
		}
		for(XAAttribute oAttr : this.attributes.values()) {
			oElement.addAttribute(oAttr.getName(), oAttr.getValue());
		}
		return oElement;
	}

	/**
	 * Indique si le noeud courant et le noeud passé en paramètre sont identiques
	 * @param p_oldNode le noeud à comparer
	 * @return true si les deux noeuds sont identiques (2 noeuds sont identiques si
	 * - ils ont le même parent,
	 * - ils ont le même frère
	 */
	public boolean isLike(XANode p_oldNode) {
		if (this.parent == null && p_oldNode.parent == null) {
			return true;
		}
		else if (this.parent != null && p_oldNode.parent!=null) {
			if (this.parent.getUniqueXPathId().equals(p_oldNode.parent.getUniqueXPathId())) {
				// le père est identique
				if (this.brother == null && p_oldNode.brother==null) {
					return true;
				}
				else if (this.brother !=null && p_oldNode.brother !=null) {
					//if(LOG.isDebugEnabled()) LOG.debug(this.getUniqueXPathId()+"'s brother ("+this.brother.getUniqueXPathId()+") <=> "+p_oldNode.getUniqueXPathId()+"'s brother ("+p_oldNode.brother.getUniqueXPathId()+") ");
					return (this.brother.getUniqueXPathId().equals(p_oldNode.brother.getUniqueXPathId())); 
				}
				else {
					if(LOG.isDebugEnabled()) LOG.debug(this.getUniqueXPathId()+"'s brother ("+this.brother+") or "+p_oldNode.getUniqueXPathId()+"'s brother ("+p_oldNode.brother+") is NULL");
					return (this.brother ==null && p_oldNode.brother ==null);
				}
			}
			else {
				if(LOG.isDebugEnabled()) LOG.debug(this.getUniqueXPathId()+"'s parent ("+this.parent.getUniqueXPathId()+") is different from "+p_oldNode.getUniqueXPathId()+"'s parent ("+p_oldNode.parent.getUniqueXPathId()+") ");
				return false;				
			}
		}
		else {
			if(LOG.isDebugEnabled()) LOG.debug(this.getUniqueXPathId()+"'s parent or "+p_oldNode.getUniqueXPathId()+"'s parent is NULL ");
			return this.parent == null && p_oldNode.parent ==null; 
		}
	}

	
	
	public String getUniqueXPathId(){
		if(this.getConfiguration() == null || this.getConfiguration().getIdentificationType() == null ){
			uniqueXPathId=null;
		}
		else if(uniqueXPathId==null) {
			
			uniqueXPathId="//" + this.getName();
			
			if (!this.getConfiguration().getIdentificationType().isSingle()) {
				
				if (this.getConfiguration().getIdentificationType().isRequiresParent() && this.getParent() != null ) {
					uniqueXPathId = this.getParent().getUniqueXPathId()+"/"+this.getName();
				}
				
				if (this.getConfiguration().getIdentificationType().isRequiresAttribute()) {
					String attr = this.getXPathAttributesPortion();
					if(attr != null && attr.length()>0)
						uniqueXPathId+=attr;
					else 
						uniqueXPathId+= "[" + String.valueOf(this.getPath().getPositionRelative()) + "]";
				}
				else if(this.getConfiguration().getIdentificationType().isRequiresParent()){
					uniqueXPathId += "[" + String.valueOf(this.getPath().getPositionRelative()) + "]";	
				}
			}
		}
		return uniqueXPathId;		
	}

	/**
	 * Retrouve le noeud courant dans un document
	 * @param p_oNode
	 * @param p_oDoc
	 * @return
	 */
	public List<Element> retrieveNodes(Document p_oDoc) {
		if (this.getConfiguration()!=null) {
			XPath xPath = null;
			xPath = p_oDoc.getRootElement().createXPath(this.getUniqueXPathId());
			
			Object res = xPath.evaluate(p_oDoc);
			if (res==null) {
				return null;
			} 
			else if (res instanceof List<?>) {
				@SuppressWarnings("unchecked")
				List<Element> res2 = (List<Element>) res;
				if (res2!=null && !res2.isEmpty()) {
					return res2;
				}
				else {
					return null;
				}
			}
			else {
				List<Element> result= new ArrayList<Element>();
				result.add((Element)res);
				return result;
			}
		}
		else {
			return null;
		}
	}
	/**
	 * Retrouve le noeud courant dans un document
	 * @param p_oNode
	 * @param p_oDoc
	 * @return
	 * @throws AdjavaException 
	 */
	public Element retrieveSingleNode(Document p_oDoc) throws AdjavaException {
		if (this.getConfiguration()!=null) {
			XPath xPath = null;
			String xPathId = this.getUniqueXPathId();
			xPath = p_oDoc.getRootElement().createXPath(xPathId);
			
			Object res = xPath.evaluate(p_oDoc);
			if (res==null) {
				return null;
			} 
			else if (res instanceof List<?>) {
				if(((List<Element>) res).isEmpty())
					return null;
				if(((List<Element>) res).size()==1)
					return (Element) ((List<Element>) res).get(0);
				else {
					throw new AdjavaException("The following node is not unique : "+xPathId);
				}
			}
			else {
				return (Element)res;
			}
		}
		else {
			return null;
		}
	}

	private String getXPathAttributesPortion() {
		if(this.name.equalsIgnoreCase("dict")) {
			this.name = "dict";
		}
		String result="[";
		boolean isFist=true;
		boolean attributeFound=false;
		for(String attr:this.getConfiguration().getIdAttribute()){
			if(this.getAttribute(attr) != null && this.getAttribute(attr).getValue() != null && !this.getAttribute(attr).getValue().trim().isEmpty()){
				if(!isFist){
					result+=" and ";
				}
				else{
					isFist=false;
				}
				result+="@" +attr + "='" + this.getAttribute(attr).getValue()+"'";
				attributeFound=true;
			}
		}
		result+="]";
		if(!attributeFound){
			if(this.getConfiguration().getIdentificationType().isValueAsKey() && this.value!= null && !this.value.trim().isEmpty())
				return "='"+this.value+"'";
			else
				return null;
		}
		else
			return result;
	}
	
	public boolean isDeeper(XANode p_oAnotherNode) {
		boolean result = this.getUniqueXPathId().contains(p_oAnotherNode.getUniqueXPathId());
		XANode oCurrentNode = this;
		while(!result  && oCurrentNode.getParent() != null) {
			oCurrentNode = oCurrentNode.getParent();
			result = oCurrentNode.getUniqueXPathId().contains(p_oAnotherNode.getUniqueXPathId());
		}
		return result;
		
	}
}
