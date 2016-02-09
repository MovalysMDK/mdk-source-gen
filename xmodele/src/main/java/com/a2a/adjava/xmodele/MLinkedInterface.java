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
import java.util.List;

import org.dom4j.Element;

import com.a2a.adjava.utils.StrUtils;

/**
 * 
 * <p>
 * Interface venant des stéréotypes de la classe
 * </p>
 * 
 * <p>
 * Copyright (c) 2009
 * </p>
 * <p>
 * Company: Adeuza
 * </p>
 * 
 * @since 2.5
 * @author mmadigand
 * 
 */
public class MLinkedInterface extends SGeneratedElement {

	private List<String> listTypeGenerics;

	/**
	 * Constructeur
	 * 
	 * @param p_sName nom de l'interface
	 * @param p_sFullName nom complet de l'interface
	 * @param p_t_sGenericParameters liste des types génériques
	 */
	public MLinkedInterface(String p_sName, String p_sFullName, String... p_t_sGenericParameters) {
		super("linked-interface", null, p_sName);
				
		this.setFullName(p_sFullName);
		this.listTypeGenerics = new ArrayList<String>();
		for (String sParam : p_t_sGenericParameters) {
			this.listTypeGenerics.add(sParam);
		}
	}

	/**
	 * Retourne a liste des types générique d'un objet LinkedInterface 
	 * @return liste de String. Package + class 
	 */
	public List<String> getListTypeGenerics(){
		return this.listTypeGenerics;
	}
	
	/**
	 * Affecte une nouvelle liste de générics à une interterface.
	 * @param p_sListTypeGenerics la liste de type générique à associée à l'interface courrante.
	 */
	public void setListTypeGenerics(List<String> p_sListTypeGenerics){
		this.listTypeGenerics = p_sListTypeGenerics;
	}
	
	/**
	 * Ajoute à liste des types générique d'un objet LinkedInterface un nouveau type 
	 * @param p_sGen le nouveau type générique à ajouter sous la forme d'une chaine contenant le package + la classe 
	 */
	public void addGenerics(String p_sGen) {
		this.listTypeGenerics.add(p_sGen);
	}

	/**
	 * @param p_iPosition
	 * @return
	 */
	public Element toXml(int p_iPosition) {
		Element oElement = super.toXml();
		oElement.addElement("position").setText(String.valueOf(p_iPosition));
		return oElement;
	}

	@Override
	protected void toXmlInsertBeforeDocumentation(Element p_xElement) {
		super.toXmlInsertBeforeDocumentation(p_xElement);
		Element xParameters = p_xElement.addElement("generic-parameters");
		Element xParam = null;
		for (String sParam : this.listTypeGenerics) {

			xParam = xParameters.addElement("param");
			xParam.addAttribute("name", StrUtils.substringAfterLastDot(sParam));
			xParam.addAttribute("full-name", sParam);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.getFullName().equalsIgnoreCase(((MLinkedInterface)obj).getFullName());
	}
	
	
}
