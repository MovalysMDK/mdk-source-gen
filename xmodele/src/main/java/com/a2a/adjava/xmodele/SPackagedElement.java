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
import java.util.Collection;
import java.util.List;

import org.dom4j.Element;

import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.utils.StrUtils;

/**
 * Defined an element to generate
 * @author smaitre
 */
public class SPackagedElement extends SGeneratedElement {

	/** 
	 * the package of generated element, could be null
	 */
	private MPackage mPackage = null;
	
	/**
	 * linked import 
	 */
	private List<String> importList = new ArrayList<>();
	
	/**
	 * Construct a new MUmlElement
	 * @param p_sType the type of element, the fisrt tag of xml
	 * @param p_sUmlName the uml name
	 * @param p_oPackage the associated package 
	 * @param p_sName the name used by element generated
	 */
	public SPackagedElement(String p_sType, String p_sUmlName, String p_sName, MPackage p_oPackage) {
		super(p_sType, p_sUmlName, p_sName);
		this.mPackage = p_oPackage;
		if (this.mPackage!=null) {
			this.setFullName(p_oPackage.getFullName() + StrUtils.DOT + this.getName());
		}
		p_oPackage.add(this);
	}
	
	/**
	 * Retourne le package d'un élément.
	 * @return objet de type <em>Mpackage</em>.
	 */
	public MPackage getPackage() {
		return this.mPackage;
	}
	
	/**
	 * Retourne la liste des imports d'un élément.
	 * @return une liste d'import sous la forme de chaines de caractères
	 */
	public List<String> getImportList(){
		return this.importList;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void toXmlInsertBeforeDocumentation(Element p_xElement) {
		super.toXmlInsertBeforeDocumentation(p_xElement);
		this.toXmlInsertBeforePackage(p_xElement);
		if (this.mPackage!=null) {
			p_xElement.addElement("package").setText(this.mPackage.getFullName());
		}
		this.toXmlInsertBeforeImport(p_xElement);
		this.importsToXml(p_xElement);
	}
	
	/**
	 * @param p_xElement
	 */
	protected void importsToXml( Element p_xElement ) {
		for( String sImport : this.importList ) {
			p_xElement.addElement("import").setText(sImport.trim());
		}
	}
	
	protected void toXmlInsertBeforePackage(Element p_xElement) {
		//Nothing to do
	}
	
	protected void toXmlInsertBeforeImport(Element p_xElement) {
		//Nothing to do
	}
	
	/**
	 * Add an import 
	 * @param p_oPackagedElement package element to import
	 */
	public void addImport( SPackagedElement p_oPackagedElement ) {
		if ( p_oPackagedElement != null ) {
			this.addImport( p_oPackagedElement.getFullName());
		}
	}	
	
	/**
	 * Add an import 
	 */
	public void addImport( String p_sImport ) {
		if (p_sImport!=null && !p_sImport.isEmpty())
		if ( !this.importList.contains( p_sImport )) {
			if(p_sImport.lastIndexOf(StrUtils.DOT)!=-1){
				String sPackage = p_sImport.substring( 0, p_sImport.lastIndexOf(StrUtils.DOT));
				if ( !sPackage.equals(this.mPackage.getFullName()) && !sPackage.startsWith("java.lang")) {
					this.importList.add(p_sImport);
				}
			}
		}
	}
	
	/**
	 * @param p_listImports
	 */
	public void addImports( Collection<String> p_listImports ) {
		for( String sImport : p_listImports ) {
			addImport(sImport);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public ITypeDescription getAssociatedType(IDomain p_oDomain) {
		return p_oDomain.getLanguageConf().createObjectTypeDescription(this.getFullName());
	}
	
}
