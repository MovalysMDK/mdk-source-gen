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

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.uml.UmlAssociationEnd.AggregateType;

/**
 * @author lmichenaud
 *
 */
public class MAssociationPersistableManyToMany extends MAssociationManyToMany {

	private MJoinEntityImpl joinClass;
	private List<MAttribute> keyAttrs;
	private List<MAttribute> criteriaAttrs;
	private String nameForJoinClass;
	private String oppositeNameForJoinClass;

	/**
	 * @param p_sName
	 * @param p_oRefClass
	 * @param p_sParameterName
	 */
	public MAssociationPersistableManyToMany(String p_sName, String p_sNameForJoinClass,
			MEntityImpl p_oRefClass, MEntityImpl p_oOppositeClass, String p_sVariableName,
			String p_sVariableListName, String p_sParameterName, ITypeDescription p_oTypeDescription,
			String p_sVisibility, boolean p_bRelationOwner, String p_sOppositeName,
			String p_sOppositeNameForJoinClass, AggregateType p_oAggregateType,
			AggregateType p_oOppositeAggregateType, boolean p_bOppositeNavigable) {
		super(p_sName, AssociationType.MANY_TO_MANY, p_oAggregateType, p_oOppositeAggregateType, p_oRefClass,
				p_oOppositeClass, p_sVariableName, p_sVariableListName, p_sParameterName, p_oTypeDescription,
				p_sVisibility, p_bRelationOwner, p_sOppositeName, p_bOppositeNavigable);
		this.nameForJoinClass = p_sNameForJoinClass;
		this.oppositeNameForJoinClass = p_sOppositeNameForJoinClass;
	}

	/**
	 * @return
	 */
	public String getNameForJoinClass() {
		return nameForJoinClass;
	}

	/**
	 * @param p_oJoinClass
	 */
	public void setJoinClass(MJoinEntityImpl p_oJoinClass) {
		this.joinClass = p_oJoinClass;
	}

	/**
	 * @return
	 */
	public List<MAttribute> getKeyAttrs() {
		return keyAttrs;
	}

	/**
	 * @param p_listKeyAttrs
	 */
	public void setKeyAttrs(List<MAttribute> p_listKeyAttrs) {
		this.keyAttrs = p_listKeyAttrs;
	}

	/**
	 * @return
	 */
	public MJoinEntityImpl getJoinClass() {
		return joinClass;
	}

	/**
	 * @return
	 */
	public List<MAttribute> getCriteriaAttrs() {
		return criteriaAttrs;
	}

	/**
	 * @param p_listCriteriaAttrs
	 */
	public void setCriteriaAttrs(List<MAttribute> p_listCriteriaAttrs) {
		this.criteriaAttrs = p_listCriteriaAttrs;
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.xmodele.MAssociation#toXml()
	 */
	@Override
	public Element toXml() {
		Element r_xAssoc = super.toXml();

		r_xAssoc.addAttribute("joinclass-cascade-name", this.nameForJoinClass.toUpperCase());
		r_xAssoc.addAttribute("joinclass-opposite-cascade-name", this.oppositeNameForJoinClass.toUpperCase());
		Element xJoinClass = r_xAssoc.addElement("join-table");
		
		if ( this.joinClass.getTable() != null ) {
			xJoinClass.addElement("name").setText(this.joinClass.getTable().getName());
			
			Element xKeyAttrs = xJoinClass.addElement("key-fields");
			xKeyAttrs.addAttribute("asso-name", this.oppositeNameForJoinClass);
			for (MAttribute oAttribute : this.keyAttrs) {
				Element xField = oAttribute.getField().toXml();
				xField.addAttribute("attr-name", oAttribute.getName());
				xField.addAttribute("method-crit-name", oAttribute.getMethodCritName());
				xKeyAttrs.add(xField);
			}

			Element xCritAttrs = xJoinClass.addElement("crit-fields");
			xCritAttrs.addAttribute("asso-name", this.nameForJoinClass);
			for (MAttribute oAttribute : this.criteriaAttrs) {
				Element xField = oAttribute.getField().toXml();
				xField.addAttribute("attr-name", oAttribute.getName());
				xField.addAttribute("method-crit-name", oAttribute.getMethodCritName());
				xCritAttrs.add(xField);
			}
		}
		
		Element xInterface = xJoinClass.addElement("interface");
		xInterface.addElement("name").setText(this.joinClass.getMasterInterface().getName());

		if ( this.joinClass.getDao() != null ) {
			Element xDao = xJoinClass.addElement("dao");
			xDao.addElement("bean-ref").setText(
					StringUtils.capitalize(this.joinClass.getDao().getName()));
			xDao.addElement("name").setText(this.joinClass.getDao().getName());
	
			Element xDaoInterface = xJoinClass.addElement("dao-interface");
			xDaoInterface.addElement("bean-ref").setText(
					this.joinClass.getDao().getMasterInterface().getName().substring(0, 1).toLowerCase()
							+ this.joinClass.getDao().getMasterInterface().getName().substring(1));
			xDaoInterface.addElement("name").setText(this.joinClass.getDao().getMasterInterface().getName());
		}
		// on ajoute les détails sur l association
		this.joinClass.leftAssociationToXml(xJoinClass);
		this.joinClass.rightAssociationToXml(xJoinClass);
		
		r_xAssoc.element("method-crit-name").setText(StringUtils.capitalize(this.nameForJoinClass));
		r_xAssoc.element("method-crit-opposite-name").setText(StringUtils.capitalize(this.oppositeNameForJoinClass));
		r_xAssoc.addElement("save-method").setText("save" + StringUtils.capitalize(this.getName()));

		return r_xAssoc;
	}
}
