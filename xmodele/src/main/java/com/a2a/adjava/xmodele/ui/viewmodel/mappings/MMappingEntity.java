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
package com.a2a.adjava.xmodele.ui.viewmodel.mappings;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import com.a2a.adjava.languages.LanguageConfiguration;
import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MViewModelImpl;

/**
 * Mapping for entity
 * vm.subvm <=> entity.entity
 * @author lmichenaud
 *
 */
public class MMappingEntity extends MMapping {
	
	/**
	 * Property getter in parent entity
	 */
	private String getter;

	/**
	 * Property setter in parent entity
	 */
	private String setter;

	/**
	 * Interface of entity
	 */
	private String entityInterface;

	/**
	 * Factory for entity
	 */
	private String factory;
	
	/**
	 * 
	 */
	private boolean mandatory ;

	/**
	 * Constructor
	 * @param p_oViewModel viewmodel
	 * @param p_oEntity entity
	 * @param p_sEntityAttr property name in entity
	 */
	public MMappingEntity(MViewModelImpl p_oViewModel, MEntityImpl p_oEntity, String p_sEntityAttr, boolean p_bMandatory, LanguageConfiguration p_oLngConf) {
		p_oViewModel.addImport(p_oEntity.getMasterInterface());
		p_oViewModel.addImport(p_oEntity.getFactoryInterface());
		
		ITypeDescription oTypeDesc = p_oLngConf.getTypeDescription("Object");
		if ( !oTypeDesc.getGetAccessorPrefix().isEmpty()) {
			this.getter = oTypeDesc.getGetAccessorPrefix().concat(StringUtils.capitalize(p_sEntityAttr));
		}
		else {
			this.getter = p_sEntityAttr ;
		}
		
		if ( !oTypeDesc.getSetAccessorPrefix().isEmpty()) {
			this.setter = oTypeDesc.getSetAccessorPrefix().concat(StringUtils.capitalize(p_sEntityAttr));	
		}
		else {
			this.setter = p_sEntityAttr ;
		}

		this.entityInterface = p_oEntity.getMasterInterface().getName();
		if ( p_oEntity.getFactoryInterface() != null ) {
			this.factory = p_oEntity.getFactoryInterface().getName();
		}
		this.mandatory = p_bMandatory;
	}

	
	
	public String getGetter() {
		return getter;
	}



	public void setGetter(String getter) {
		this.getter = getter;
	}



	public String getSetter() {
		return setter;
	}



	public void setSetter(String setter) {
		this.setter = setter;
	}



	/**
	 * (non-Javadoc)
	 * @see com.a2a.adjava.xmodele.ui.viewmodel.mappings.MMapping#toXml()
	 */
	public Element toXml() {
		Element r_xEntity = super.toXml();
		r_xEntity.setName("entity");
		r_xEntity.addAttribute("type", this.entityInterface);
		r_xEntity.addElement("getter").addAttribute("name", this.getter);
		r_xEntity.addAttribute("mandatory", Boolean.toString(this.mandatory));
		
		Element xSetter = r_xEntity.addElement("setter");
		xSetter.addAttribute("name", this.setter);
		xSetter.addAttribute("factory", this.factory);

		return r_xEntity;
	}
}