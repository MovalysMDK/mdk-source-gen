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

import java.util.Map;
import org.dom4j.Element;

import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.uml.UmlAssociationEnd.AggregateType;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MViewModelImpl;

/**
 * Descriptor for viewmodel mapping
 * @author lmichenaud
 *
 */
public interface IVMMappingDesc {

	/**
	 * Add mapping of an attribute 
	 * @param p_oViewModel viewmodel
	 * @param p_sEntityAttr property name in entity 
	 * @param p_sVMAttr property name in viewmodel 
	 * @param p_oTypeViewModel property type in viewmodel
	 * @param p_oTypeModel property type in entity
	 * @param p_bReadOnly read only attribute
	 * @param p_sInitialValue initial value
	 * @param p_sExpandableEntity expandable entity if vm attr is linked to an expandable of the entity
	 */
	public void addAttribute(MViewModelImpl p_oViewModel, String p_sEntityAttr,
			String p_sVMAttr, ITypeDescription p_oTypeViewModel,
			ITypeDescription p_oTypeModel, boolean p_bReadOnly,
			String p_sInitialValue, String p_sVmAttrInitialValue, String p_sExpandableEntity);

	/**
	 * Add mapping of an attribute 
	 * @param p_oViewModel viewmodel
	 * @param p_sEntityAttr property name in entity 
	 * @param p_sVMAttr property name in viewmodel 
	 * @param p_oTypeViewModel property type in viewmodel
	 * @param p_oTypeModel property type in entity
	 * @param p_bReadOnly read only attribute
	 * @param p_sInitialValue initial value
	 * @param p_sVmAttrInitialValue 
	 * @param p_sExpandableEntity expandable entity if vm attr is linked to an expandable of the entity
	 * @param p_bVmPrimitiveType true if the attribute type is a primitive 
	 */
	public void addAttribute(MViewModelImpl p_oViewModel, String p_sEntityAttr,
			String p_sVMAttr, ITypeDescription p_oTypeViewModel,
			ITypeDescription p_oTypeModel, boolean p_bReadOnly,
			String p_sInitialValue, String p_sVmAttrInitialValue, String p_sExpandableEntity, boolean p_bVmPrimitiveType);
	
	/**
	 * Add mapping with an entity 
	 * @param p_oViewModel viewmodel
	 * @param p_oEntity entity
	 * @param p_sEntityAttr property name in entity 
	 * @param p_sVMAttr property name in viewmodel
	 * @param p_oType MMapping type (vm or vmlist)
	 * @param p_bMandatory mandatory or not
	 * @param p_oDomain domain
	 */
	public void addEntity(MViewModelImpl p_oViewModel, MEntityImpl p_oEntity,
			String p_sEntityAttr, String p_sVMAttr, MMappingType p_oType,
			boolean p_bMandatory, IDomain<?,?> p_oDomain, AggregateType p_oAggregateType);

	
	/**
	 * Add mapping with an entity 
	 * @param p_sEntityAttr property name of entity to remove
	 */
	public void removeEntityOrAttribute(String p_sEntityAttr);
	

	/**
	 * Get MMappingEntity by name, create if not exist
	 * @param p_oViewModel viewmodel
	 * @param p_oEntity entity
	 * @param p_oDomain domain
	 * @param p_sEntityAttr property name
	 * @return MMappingEntity
	 */
	public MMappingEntity getOrAddEntity(MViewModelImpl p_oViewModel,
			MEntityImpl p_oEntity, String p_sEntityAttr, boolean p_bMandatory, IDomain<?,?> p_oDomain);
	
	
	/**
	 * Getter for the list of attributes of this mapping
	 * @return the map of all the attributes
	 */
	public Map<String, MMappingAttribute>  getMapAttributes();
	
	/**
	 * Getter for the list of entities of this mapping
	 * @return the map of all the entities
	 */
	public Map<String, MMappingEntity>  getMapEntities();
	
	public void clear();
	
	/**
	 * To Xml
	 * @return xml representation
	 */
	public Element toXml();

}
