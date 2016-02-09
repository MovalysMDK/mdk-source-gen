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
package com.a2a.adjava.languages.ios.xmodele;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MPackage;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;
import com.a2a.adjava.xmodele.ui.viewmodel.mappings.IVMMappingDesc;

/**
 * Viewmodel for IOS
 * @author lmichenaud
 *
 */
public class MIOSViewModel extends MViewModelImpl {

	/**
	 * Create a new view model.
	 * @param p_sName viewmodel name (including prefix/suffix)
	 * @param p_sUmlName uml name
	 * @param p_oPackage package of viewmodel
	 * @param p_sType viewmodel type
	 * @param p_oTypeEntityToUpdate entity to update
	 * @param p_sPathToModel path to model
	 * @param p_bCustomizable customizable
	 * @param p_oMapping mapping with entity
	 */
	public MIOSViewModel(String p_sName, String p_sUmlName,
			MPackage p_oPackage, ViewModelType p_sType,
			MEntityImpl p_oEntityToUpdate, String p_sPathToModel,
			boolean p_bCustomizable, IVMMappingDesc p_oMapping) {
		super(p_sName, p_sUmlName, p_oPackage, p_sType, p_oEntityToUpdate,
				p_sPathToModel, p_bCustomizable, p_oMapping);
	}
	
	/**
	 * Return objc classes
	 * @return objc classes
	 */
	private List<String> computeObjcClasses() {
		List<String> r_listClasses = new ArrayList<String>();
		
		return r_listClasses ;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.SGeneratedElement#toXml()
	 */
	@Override
	public Element toXml() {
		Element r_xElement = super.toXml();
		
		Element xClasses = r_xElement.addElement("objc-classes");
		for( String sClass : this.computeObjcClasses()) {
			xClasses.addElement("objc-class").setText(sClass);
		}
		
		return r_xElement;
	}
}
