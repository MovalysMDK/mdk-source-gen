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
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.types.ITypeConvertion;
import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.uml.UmlAssociationEnd.AggregateType;
import com.a2a.adjava.utils.StrUtils;
import com.a2a.adjava.xmodele.MViewModelImpl;

/**
 * Mapping for attribute
 * vm.attribute <=> entity.attribute
 * @author lmichenaud
 *
 */
public class MMappingAttribute {
	
	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(MMappingAttribute.class);
	
	/**
	 * Getter for attribute of entity
	 */
	private String getter;

	/**
	 * Formula to read value
	 */
	private String getterFormula;

	/**
	 * Setter for attribute of entity
	 */
	private String setter;

	/**
	 * Formula to set value
	 */
	private String setterFormula;

	/**
	 * Initial value for attribute of entity
	 */
	private String initialValue;
	
	/**
	 * Initial value for attribute of viewmodel
	 */
	private String vmInitialValue;

	/**
	 * true if the value in the view model is primitive
	 */
	private boolean vmPrimitiveType;
	
	/**
	 * Property name in viewmodel
	 */
	private String vmAttr;
	
	/**
	 * Read only
	 */
	private boolean readOnly ;

	/**
	 * Expandable entity
	 */
	private String expandableEntity ;

	/**
	 * Constructor
	 * @param p_oViewModel viewmodel
	 * @param p_sEntityAttr property name in entity
	 * @param p_sVMAttr property name in viewmodel
	 * @param p_bReadOnly readonly
	 * @param p_oTypeViewModel property type in viewmodel
	 * @param p_oTypeModel property type in model
	 * @param p_sInitialValue initial value
	 * @param p_sVmInitialValue initial value in the view model
	 * @param p_sExpandableEntity 
	 */
	public MMappingAttribute(MViewModelImpl p_oViewModel, String p_sEntityAttr, String p_sVMAttr,
			boolean p_bReadOnly, ITypeDescription p_oTypeViewModel, ITypeDescription p_oTypeModel,
			String p_sInitialValue, String p_sVmInitialValue, String p_sExpandableEntity) {
		this(p_oViewModel, p_sEntityAttr, p_sVMAttr, p_bReadOnly, p_oTypeViewModel, p_oTypeModel, p_sInitialValue,
				p_sVmInitialValue, p_sExpandableEntity, false);
	}
	
	/**
	 * Constructor
	 * @param p_oViewModel viewmodel
	 * @param p_sEntityAttr property name in entity
	 * @param p_sVMAttr property name in viewmodel
	 * @param p_bReadOnly readonly
	 * @param p_oTypeViewModel property type in viewmodel
	 * @param p_oTypeModel property type in model
	 * @param p_sInitialValue initial value
	 * @param p_sVmInitialValue initial value in the view model
	 * @param p_sExpandableEntity 
	 * @param p_bVmPrimitiveType true if the value type in the view model is a primitive 
	 */
	public MMappingAttribute(MViewModelImpl p_oViewModel, String p_sEntityAttr, String p_sVMAttr,
			boolean p_bReadOnly, ITypeDescription p_oTypeViewModel, ITypeDescription p_oTypeModel,
			String p_sInitialValue, String p_sVmInitialValue, String p_sExpandableEntity, boolean p_bVmPrimitiveType) {
		this(p_sEntityAttr, p_sVMAttr, p_bReadOnly, p_oTypeModel.getGetAccessorPrefix(), p_oTypeModel
				.getSetAccessorPrefix(), p_sInitialValue, p_sExpandableEntity);

		if ( log.isDebugEnabled()) {
			log.debug("MMappingAttribute");
			log.debug("  entity attribute name: {}", p_sEntityAttr);
			log.debug("  vm attribute name: {}", p_sVMAttr);
			log.debug("  readonly: {}", p_bReadOnly);
			log.debug("  model attribute type name: {}", p_oTypeModel.getName());
		}

		this.vmInitialValue = p_sVmInitialValue ;
		
		this.vmPrimitiveType = p_bVmPrimitiveType;
		
		this.readOnly = p_bReadOnly ;

		ITypeConvertion oVModelTypeToModelType = p_oTypeViewModel.getConvertion(p_oTypeModel);
		if (!p_bReadOnly && oVModelTypeToModelType != null) {
			log.debug("  setter formula: {}", oVModelTypeToModelType.applyFormula("VALUE"));
			this.setterFormula = oVModelTypeToModelType.getFormula();
			p_oViewModel.addImports(oVModelTypeToModelType.getImports());
		}
		
		ITypeConvertion oModelTypeToVModelType = p_oTypeModel.getConvertion(p_oTypeViewModel);
		if (oModelTypeToVModelType != null) {
			log.debug("  getter formula: {}", oModelTypeToVModelType.applyFormula("VALUE"));
			this.getterFormula = oModelTypeToVModelType.getFormula();
			p_oViewModel.addImports(oModelTypeToVModelType.getImports());
		}
		
		this.expandableEntity = p_sExpandableEntity ;
	}

	/**
	 * Protected constructor
	 * @param p_sEntityAttr property name in entity
	 * @param p_sVMAttr property name in viewmodel
	 * @param p_bReadOnly readonly
	 * @param p_sGetAccessorPrefix getter in viewmodel
	 * @param p_sSetAccessorPrefix setter in viewmodel
	 * @param p_sInitialValue initial value
	 * @param p_sExpandableEntity expandable entity
	 */
	protected MMappingAttribute(String p_sEntityAttr, String p_sVMAttr, boolean p_bReadOnly,
			String p_sGetAccessorPrefix, String p_sSetAccessorPrefix, String p_sInitialValue, String p_sExpandableEntity) {

		this.initialValue = p_sInitialValue;
		
		if ( !p_sGetAccessorPrefix.isEmpty()) {
			this.getter = p_sGetAccessorPrefix.concat(StringUtils.capitalize(p_sEntityAttr));
		}
		else {
			this.getter = p_sEntityAttr ;
		}
		
		if ( !p_sSetAccessorPrefix.isEmpty()) {
			this.setter = p_sSetAccessorPrefix.concat(StringUtils.capitalize(p_sEntityAttr));	
		}
		else {
			this.setter = p_sEntityAttr ;
		}
		
		this.vmAttr = p_sVMAttr;
		this.expandableEntity = p_sExpandableEntity ;
	}
	
	/**
	 * get setter
	 * @return setter name
	 */
	protected String getSetter() {
		return this.setter;
	}

	/**
	 * True if setter must be generated
	 * @return
	 */
	protected boolean generateSetter() {
		return !this.readOnly && this.setter != null ; 
	}
	
	/**
	 * get ExpandableEntityShortName
	 * @return ExpandableEntityShortName
	 */
	public String getExpandableEntityShortName()
	{
		if(this.expandableEntity !=null && !this.expandableEntity.isEmpty())
		{
			return StrUtils.substringAfterLastDot(this.expandableEntity);
		}
		return this.expandableEntity;
	}
	
	
	
	
	public String getGetter() {
		return getter;
	}

	public void setGetter(String getter) {
		this.getter = getter;
	}

	public void setSetter(String setter) {
		this.setter = setter;
	}

	/**
	 * To xml
	 * @return xml representation
	 */
	public Element toXml() {
		Element r_xAttribute = DocumentHelper.createElement("attribute");

		r_xAttribute.addAttribute("vm-attr", this.vmAttr);
		r_xAttribute.addAttribute("initial-value", this.initialValue);
		if ( this.vmInitialValue != null ) {
			Element xDefValue = r_xAttribute.addElement("vm-attr-initial-value");
			xDefValue.addAttribute("primitive", Boolean.toString(this.vmPrimitiveType));
			xDefValue.setText(this.vmInitialValue);
		}
		
		if ( this.expandableEntity != null ) {
			r_xAttribute.addAttribute("expandableEntity", this.expandableEntity );
			r_xAttribute.addAttribute("expandableEntityShortName", StrUtils.substringAfterLastDot(this.expandableEntity));
		}

		Element xGetter = r_xAttribute.addElement("getter");
		xGetter.addAttribute("name", this.getter);
		if (this.getterFormula != null) {
			xGetter.addAttribute("formula", this.getterFormula);
		}

		if (generateSetter()) {
			Element xSetter = r_xAttribute.addElement("setter");
			xSetter.addAttribute("name", this.setter);
			if (this.setterFormula != null) {
				xSetter.addAttribute("formula", this.setterFormula);
			}
		}

		return r_xAttribute;
	}
}
