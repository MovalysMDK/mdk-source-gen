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

import com.a2a.adjava.uml.UmlAssociationEnd.AggregateType;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MViewModelImpl;

/**
 * @author lmichenaud
 *
 */
public class MMappingAttrEntity extends MMappingAttribute {

	/**
	 * Interface of viewmodel
	 */
	private String vmInterface;
	
	/**
	 * Interface of entity
	 */
	private String entityInterface;
	
	/**
	 * Factory interface for entity
	 */
	private String factory;
	
	/**
	 * Mapping type
	 */
	private MMappingType type;

	/**
	 * Mandatory
	 */
	private boolean mandatory ;
	
	/**
	 * ViewModel
	 */
	private MViewModelImpl viewModel ;
	
	/**
	 * Agregation type with the entity
	 */
	
	private AggregateType aggregateType;
	
	/**
	 * Constructor
	 * @param p_oViewModel viewmodel
	 * @param p_oEntity entity
	 * @param p_sEntityAttr property name in entity
	 * @param p_sVMAttr property name in viewmodel
	 * @param p_oType mapping type
	 * @param p_sInitialValue initial value
	 * @param p_oAggregateType the aggregate type of the entity
	 */
	public MMappingAttrEntity(MViewModelImpl p_oViewModel, MEntityImpl p_oEntity, String p_sEntityAttr,
			String p_sVMAttr, MMappingType p_oType, String p_sInitialValue, boolean p_bMandatory, IDomain<?,?> p_oDomain,
			AggregateType p_oAggregateType) {
		super(p_sEntityAttr, p_sVMAttr, p_oViewModel.isReadOnly() && MMappingType.vmlist.equals(p_oType), 
			p_oDomain.getLanguageConf().getTypeDescription("Object").getGetAccessorPrefix(), 
			p_oDomain.getLanguageConf().getTypeDescription("Object").getSetAccessorPrefix(), p_sInitialValue, null);		

		this.vmInterface = p_oViewModel.getMasterInterface().getName();
		this.entityInterface = p_oEntity.getMasterInterface().getName();
		this.factory = p_oEntity.getFactoryInterface().getName();
		this.type = p_oType;
		this.mandatory = p_bMandatory;
		
		this.viewModel = p_oViewModel ;
		this.aggregateType = p_oAggregateType;
		
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.ui.viewmodel.mappings.MMappingAttribute#generateSetter()
	 */
	@Override
	protected boolean generateSetter() {
		return !this.viewModel.isReadOnly() || !MMappingType.vmlist.equals(this.viewModel.getType());
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.a2a.adjava.xmodele.ui.viewmodel.mappings.MMappingAttribute#toXml()
	 */
	public Element toXml() {
		Element r_xEntity = super.toXml();
		r_xEntity.setName("entity");
		r_xEntity.addAttribute("type", this.entityInterface);
		r_xEntity.addAttribute("vm-type", this.vmInterface);
		r_xEntity.addAttribute("mapping-type", this.type.name());
		r_xEntity.addAttribute("mandatory", Boolean.toString(this.mandatory));
		if ( this.type  == MMappingType.vmlist && this.viewModel!= null) {
			r_xEntity.addAttribute("vm-typelist", this.viewModel.getMasterInterface().getName());
			r_xEntity.addAttribute("vm-property-name", StringUtils.uncapitalize( this.viewModel.getName() )); 
		}
		Element xSetter = r_xEntity.element("setter");
		if ( xSetter != null ) {
			xSetter.addAttribute("factory", this.factory);
		}
		
		if (this.aggregateType != null) {
			r_xEntity.addAttribute("aggregate-type", this.aggregateType.name());
		}

		return r_xEntity;
	}
}