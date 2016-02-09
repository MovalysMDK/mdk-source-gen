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
package com.a2a.adjava.uml2xmodele.extractors;

import java.util.List;

import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MBeanScope;

/**
 * @author lmichenaud
 *
 */
public class PojoContext {

	/**
	 * Domain
	 */
	private IDomain<IModelDictionary, IModelFactory> domain ;
	
	/**
	 * Stereotype list indicating that a UmlClass is a screen of the domain.
	 */
	private List<String> entityStereotypes ;
	
	/**
	 * Stereotypes
	 */
	private List<String> listTranscientStereotypes ;
	
	/**
	 * Stereotypes
	 */
	private List<String> listApplicationScopeStereotypes ;

	/**
	 * Stereotypes for customizable stereotypes
	 */
	private List<String> customizableStereotypes;
	
	/**
	 * @param p_listEntityStereotypes
	 * @param listTranscientStereotypes2
	 * @param listApplicationScopeStereotypes2
	 * @param domain2
	 */
	public PojoContext(List<String> p_listEntityStereotypes, List<String> p_listTranscientStereotypes,
			List<String> p_listApplicationScopeStereotypes, List<String> p_listCustomizableStereotypes,
			IDomain<IModelDictionary, IModelFactory> p_oDomain) {
		this.entityStereotypes = p_listEntityStereotypes ;
		this.listTranscientStereotypes = p_listTranscientStereotypes;
		this.listApplicationScopeStereotypes = p_listApplicationScopeStereotypes;
		this.customizableStereotypes = p_listCustomizableStereotypes;
		this.domain = p_oDomain ;
	}

	/**
	 * @return
	 */
	public IDomain<IModelDictionary, IModelFactory> getDomain() {
		return domain;
	}

	/**
	 * @return
	 */
	public List<String> getEntityStereotypes() {
		return entityStereotypes;
	}

	/**
	 * Permet de savoir si une classe est un écran de notre application ou non.
	 * @param p_oUmlClass la classe à tester
	 * @return true si la classe correspond à un écran. false sinon.
	 */
	public boolean isEntity(UmlClass p_oUmlClass) {
		return p_oUmlClass.hasAnyStereotype(this.entityStereotypes) ;
	}
	
	/**
	 * Return true if uml class is of type Entity
	 * 
	 * @param p_oUmlClass UmlClass
	 * @return true if uml class is of type Entity
	 */
	public boolean isTransient(UmlClass p_oUmlClass) {
		return p_oUmlClass.hasAnyStereotype(this.listTranscientStereotypes) ;
	}
	
	/**
	 * Return true if uml class has ApplicationScope stereotype
	 * 
	 * @param p_oUmlClass UmlClass
	 * @return true if uml class has ApplicationScope stereotype
	 */
	public boolean isApplicationScope(UmlClass p_oUmlClass) {
		return p_oUmlClass.hasAnyStereotype(this.listApplicationScopeStereotypes) ;
	}
	

	/**
	 * Return true if uml class is customizable
	 * 
	 * @param p_oUmlClass UmlClass
	 * @return true if uml class is customizable
	 */

	public boolean isCustomizable(UmlClass p_oUmlClass) {
		return p_oUmlClass.hasAnyStereotype(this.customizableStereotypes) ;
	}

	/**
	 * Return true if uml class is of type Entity
	 * 
	 * @param p_oUmlClass UmlClass
	 * @return true if uml class is of type Entity
	 */
	public MBeanScope getBeanScope(UmlClass p_oUmlClass) {
		MBeanScope r_oBeanScope = MBeanScope.REQUEST ;
		if ( p_oUmlClass.hasAnyStereotype(this.listApplicationScopeStereotypes) ){
			r_oBeanScope = MBeanScope.APPLICATION ;
		}
		return r_oBeanScope;
	}

	/**
	 * Return transient stereotypes
	 * @return transient stereotypes
	 */
	public List<String> getTranscientStereotypes() {
		return this.listTranscientStereotypes;
	}

	/**
	 * Return application scope stereotypes
	 * @return application scope stereotypes
	 */
	public List<String> getApplicationScopeStereotypes() {
		return this.listApplicationScopeStereotypes;
	}

	/**
	 * Return customizable stereotypes
	 * @return customizable stereotypes
	 */
	public List<String> getCustomizableStereotypes() {
		return this.customizableStereotypes;
	}
}
