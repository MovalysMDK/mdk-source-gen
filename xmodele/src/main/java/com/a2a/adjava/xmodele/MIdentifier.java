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
package com.a2a.adjava.xmodele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


public class MIdentifier {

	private Map<String,MIdentifierElem> mapElementByName ;
	private List<MIdentifierElem> elements ;
	
	/**
	 * 
	 */
	public MIdentifier() {
		this.elements = new ArrayList<MIdentifierElem>();
		this.mapElementByName = new HashMap<String,MIdentifierElem>();
	}
	
	/**
	 * @param p_oAttribute
	 */
	public void addElem( MIdentifierElem p_oMIdentifierElem ) {
		this.mapElementByName.put( p_oMIdentifierElem.getName(), p_oMIdentifierElem );
		this.elements.add( p_oMIdentifierElem );
	}
	
	/**
	 * @return
	 */
	public boolean isComposite() {
		return this.elements.size() > 1 ;
	}
	
	/**
	 * @return
	 */
	public boolean isEmpty() {
		return this.elements.isEmpty();
	}
	
	/**
	 * @return
	 */
	public List<MIdentifierElem> getElems() {
		return this.elements;
	}
	
	/**
	 * @param p_sName
	 * @return
	 */
	public MIdentifierElem getElemByName( String p_sName, boolean p_bCaseSensitive ) {
		MIdentifierElem r_oMIdentifierElem = null ;
		if ( p_bCaseSensitive ) {
			r_oMIdentifierElem = (MIdentifierElem) this.mapElementByName.get(p_sName);
		}
		else {
			CaseInsensitiveMap mapCaseInsensitive = new CaseInsensitiveMap(mapElementByName);
			r_oMIdentifierElem = (MIdentifierElem) mapCaseInsensitive.get(p_sName);
		}
		return r_oMIdentifierElem ;
	}
	
	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Element toXml() {
		Element r_xIdentifier = DocumentHelper.createElement("identifier");
		r_xIdentifier.addAttribute("is-composite", Boolean.toString(this.elements.size() > 1 ));
		for( MIdentifierElem oMIdentifierElem : this.elements ) {
			r_xIdentifier.add( oMIdentifierElem.toXml());
		}
		// Change le nom du noeud parameter-name pour les attributs d'association
		for( Element xAsso : (List<Element>) r_xIdentifier.elements("association")) {
			String sAssoName = xAsso.attributeValue("name");
			for( Element xAttr : (List<Element>) xAsso.elements("attribute")) {
				String sAttrName = xAttr.attributeValue("name");
				String sParameterName = xAttr.elementText("parameter-name");
				String sPrefix = sParameterName.substring(0, sParameterName.length() - sAttrName.length());
				StringBuffer sNewParameterName = new StringBuffer(sPrefix);
				sNewParameterName.append(sAssoName.substring(0,1).toUpperCase() + sAssoName.substring(1));
				sNewParameterName.append(sAttrName.substring(0,1).toUpperCase() + sAttrName.substring(1));
				xAttr.element("parameter-name").setText(sNewParameterName.toString());
			}
		}
		return r_xIdentifier ;
	}

	/**
	 * @return
	 */
	public List<MAssociation> getElemOfTypeAssociation() {
		List<MAssociation> r_listIdentifierElems = new ArrayList<MAssociation>();
		for( MIdentifierElem oMIdentifierElem : this.elements ) {
			if ( oMIdentifierElem instanceof MAssociation ) {
				r_listIdentifierElems.add( (MAssociation) oMIdentifierElem );
			}
		}
		return r_listIdentifierElems;
	}
	
	/**
	 * @return
	 */
	public List<MAttribute> getElemOfTypeAttribute() {
		List<MAttribute> r_listIdentifierElems = new ArrayList<MAttribute>();
		for( MIdentifierElem oMIdentifierElem : this.elements ) {
			if ( oMIdentifierElem instanceof MAttribute ) {
				r_listIdentifierElems.add( (MAttribute) oMIdentifierElem );
			}
		}
		return r_listIdentifierElems;
	}
}
