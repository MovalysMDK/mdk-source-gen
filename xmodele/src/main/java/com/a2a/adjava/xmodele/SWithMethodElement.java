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

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;


/**
 * Common class between class and interface
 * @author smaitre
 *
 */
public class SWithMethodElement<METHODSIGNATURE extends MMethodSignature> extends SPackagedElement {

	private List<METHODSIGNATURE> methodSignatures = null;

	private String beanName; //au sens spring

	private List<MLinkedInterface> listInterfaces;

	public SWithMethodElement(String p_sType, String p_sUmlName, String p_sName, MPackage p_oPackage) {
		super(p_sType, p_sUmlName, p_sName, p_oPackage);
		this.methodSignatures = new ArrayList<METHODSIGNATURE>();
		this.listInterfaces = new ArrayList<MLinkedInterface>();
		this.beanName = Introspector.decapitalize(p_sName);
	}

	public void addLinkedInterface(MLinkedInterface p_oInterface) {
		if(!this.listInterfaces.contains(p_oInterface)) {
			this.listInterfaces.add(p_oInterface);
		}
	}

	/**
	 * Retourne l'objet linkedInterfaces
	 * @return Objet linkedInterfaces
	 */
	public List<MLinkedInterface> getLinkedInterfaces() {
		return this.listInterfaces;
	}

	/**
	 * Affecte l'objet linkedInterfaces 
	 * @param p_oLinkedInterfaces Objet linkedInterfaces
	 */
	public void setLinkedInterfaces(List<MLinkedInterface> p_oLinkedInterfaces) {
		this.listInterfaces = p_oLinkedInterfaces;
	}

	/**
	 * @param p_oMMethodSignature
	 */
	public void addMethodSignature( METHODSIGNATURE p_oMMethodSignature ) {
		if ( p_oMMethodSignature.getReturnedType() != null 
				&& !p_oMMethodSignature.getReturnedType().isPrimitif()) {
			addImport( p_oMMethodSignature.getReturnedType().getName());

			if ( p_oMMethodSignature.getReturnedType().getParameterizedElementType().size() == 1) {
				addImport( p_oMMethodSignature.getReturnedType().getParameterizedElementType().get(0).getName());
			}
		}

		for(MMethodParameter oParameter : p_oMMethodSignature.getParameters()) {
			if ( !oParameter.getTypeDesc().isPrimitif()) {
				addImport( oParameter.getTypeDesc().getName());
			}
		}

		this.methodSignatures.add(p_oMMethodSignature);
	}

	public List<METHODSIGNATURE> getMethodSignatures() {
		return this.methodSignatures;
	}

	/**
	 * Renvoie vrai si l'interface possède la linkedinterface
	 * @param p_sIdentifiableInterfaceName nom de la linkedinterface recherchée
	 * @return vrai  si l'interface possède la linkedinterface
	 */
	public boolean hasLinkedInterface(String p_sIdentifiableInterfaceName) {
		for( MLinkedInterface oLinkInterface : listInterfaces ) {
			if ( oLinkInterface.getName().equals(p_sIdentifiableInterfaceName)) {
				return true ;
			}
		}
		return false;
	}

	protected void setBeanName(String p_sBeanName) {
		this.beanName = p_sBeanName;
	}

	public String getBeanName() {
		return this.beanName;
	}

	@Override
	protected void toXmlInsertAfterName(Element p_xElement) {
		if (this.beanName != null) {
			p_xElement.addElement("bean-name").setText(this.beanName);
		}
	}

	@Override
	protected void toXmlInsertBeforeDocumentation(Element p_xElement) {
		super.toXmlInsertBeforeDocumentation(p_xElement);
		int i = 1;
		for( MLinkedInterface oMLinkedInterface : this.listInterfaces) {
			p_xElement.add(oMLinkedInterface.toXml(i++));
			p_xElement.addElement("import").setText(oMLinkedInterface.getFullName());
		}
	}

	@Override
	protected void toXmlInsertBeforeImport(Element p_xElement) {
		super.toXmlInsertBeforeImport(p_xElement);
		Iterator<METHODSIGNATURE> iterMethodSignatures = this.methodSignatures.listIterator();
		while (iterMethodSignatures.hasNext()) {
			MMethodSignature oMethodSignature = iterMethodSignatures.next();
			p_xElement.add(oMethodSignature.toXml());
		}
	}
}
