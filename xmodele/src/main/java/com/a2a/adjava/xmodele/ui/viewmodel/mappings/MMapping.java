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

import java.util.LinkedHashMap;
import java.util.Map;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.uml.UmlAssociationEnd.AggregateType;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MViewModelImpl;

/**
 * <p>
 * ViewModel mapping with entity
 * </p>
 * 
 * <p>
 * Copyright (c) 2011
 * <p>
 * Company: Adeuza
 * 
 * @author emalespine
 * 
 */

public class MMapping implements IVMMappingDesc {

	/**
	 * Attributes Mapping 
	 */
	private Map<String, MMappingAttribute> attributes = new LinkedHashMap<String, MMappingAttribute>();

	/**
	 * Entities mapping
	 */
	private Map<String, MMappingEntity> entities = new LinkedHashMap<String, MMappingEntity>();

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.ui.viewmodel.mappings.IVMMappingDesc#addAttribute(com.a2a.adjava.xmodele.MViewModelImpl, java.lang.String, java.lang.String, com.a2a.adjava.types.ITypeDescription, com.a2a.adjava.types.ITypeDescription, boolean, java.lang.String, java.lang.String)
	 */
	@Override
	public void addAttribute(MViewModelImpl p_oViewModel, String p_sEntityAttr, String p_sVMAttr,
			ITypeDescription p_oTypeViewModel, ITypeDescription p_oTypeModel, 
			boolean p_bReadOnly, String p_sInitialValue, String p_sVmAttrInitialValue, String p_sExpandableEntity) {
		this.attributes.put(p_sVMAttr, new MMappingAttribute(p_oViewModel, p_sEntityAttr, p_sVMAttr,
				p_bReadOnly, p_oTypeViewModel, p_oTypeModel, p_sInitialValue, p_sVmAttrInitialValue, p_sExpandableEntity));
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.ui.viewmodel.mappings.IVMMappingDesc#addAttribute(com.a2a.adjava.xmodele.MViewModelImpl, java.lang.String, java.lang.String, com.a2a.adjava.types.ITypeDescription, com.a2a.adjava.types.ITypeDescription, boolean, java.lang.String, java.lang.String)
	 */
	@Override
	public void addAttribute(MViewModelImpl p_oViewModel, String p_sEntityAttr, String p_sVMAttr,
			ITypeDescription p_oTypeViewModel, ITypeDescription p_oTypeModel, 
			boolean p_bReadOnly, String p_sInitialValue, String p_sVmAttrInitialValue, String p_sExpandableEntity, boolean p_bVmPrimitiveType) {
		this.attributes.put(p_sVMAttr, new MMappingAttribute(p_oViewModel, p_sEntityAttr, p_sVMAttr,
				p_bReadOnly, p_oTypeViewModel, p_oTypeModel, p_sInitialValue, p_sVmAttrInitialValue, p_sExpandableEntity, p_bVmPrimitiveType));
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.ui.viewmodel.mappings.IVMMappingDesc#addEntity(com.a2a.adjava.xmodele.MViewModelImpl, com.a2a.adjava.xmodele.MEntityImpl, java.lang.String, java.lang.String, com.a2a.adjava.xmodele.ui.viewmodel.mappings.MMappingType, boolean)
	 */
	@Override
	public void addEntity(MViewModelImpl p_oViewModel, MEntityImpl p_oEntity, String p_sEntityAttr,
			String p_sVMAttr, MMappingType p_oType, boolean p_bMandatory, IDomain<?,?> p_oDomain, AggregateType p_oAggregateType ) {
		this.attributes.put(p_sVMAttr, new MMappingAttrEntity(p_oViewModel, p_oEntity, p_sEntityAttr,
				p_sVMAttr, p_oType, p_oDomain.getLanguageConf().getNullValue(), p_bMandatory, p_oDomain, p_oAggregateType));
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.ui.viewmodel.mappings.IVMMappingDesc#getOrAddEntity(com.a2a.adjava.xmodele.MViewModelImpl, com.a2a.adjava.xmodele.MEntityImpl, java.lang.String, boolean)
	 */
	@Override
	public MMappingEntity getOrAddEntity(MViewModelImpl p_oViewModel, MEntityImpl p_oEntity,
			String p_sEntityAttr, boolean p_bMandatory, IDomain<?,?> p_oDomain) {
		MMappingEntity r_oEntity = null;
		if (this.entities.containsKey(p_sEntityAttr)) {
			r_oEntity = this.entities.get(p_sEntityAttr);
		} else {
			r_oEntity = new MMappingEntity(p_oViewModel, p_oEntity, p_sEntityAttr, p_bMandatory, p_oDomain.getLanguageConf());
			this.entities.put(p_sEntityAttr, r_oEntity);
		}
		return r_oEntity;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.ui.viewmodel.mappings.IVMMappingDesc#toXml()
	 */
	@Override
	public Element toXml() {
		Element r_xMapping = DocumentHelper.createElement("mapping");

		for (MMappingAttribute oAttr : this.attributes.values()) {
			r_xMapping.add(oAttr.toXml());
		}

		for (MMappingEntity oEntity : this.entities.values()) {
			r_xMapping.add(oEntity.toXml());
		}

		return r_xMapping;
	}

	@Override
	public void removeEntityOrAttribute(String p_sEntityAttr) {
		this.entities.remove(p_sEntityAttr);
		this.attributes.remove(p_sEntityAttr);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.ui.viewmodel.mappings.IVMMappingDesc#getMapAttributes()
	 */
	@Override
	public Map<String, MMappingAttribute>  getMapAttributes()
	{
		return this.attributes;
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.ui.viewmodel.mappings.IVMMappingDesc#getMapEntities()
	 */
	@Override
	public Map<String, MMappingEntity>  getMapEntities()
	{
		return this.entities;
	}

	@Override
	public void clear() {
		this.attributes.clear();
		this.entities.clear();
	}
	
}
