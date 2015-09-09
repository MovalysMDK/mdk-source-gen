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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.a2a.adjava.types.ITypeDescription;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import com.a2a.adjava.schema.Table;
import com.a2a.adjava.utils.StrUtils;
import com.a2a.adjava.xmodele.MAssociation.AssociationType;

/**
 * <p>Represents an entity of the model layer</p>
 * <p/>
 * <p>Copyright (c) 2009
 * <p>Company: Adeuza
 */
public class MEntityImpl extends SClass<MEntityInterface, MMethodSignature> implements Cloneable {

	/**
	 * Entity name
	 */
	private String entityName;

	/**
	 * Association map
	 */
	private Map<String, MAssociation> mapNameAssociations;

	/**
	 * Associations
	 */
	private List<MAssociation> associations;

	/**
	 * Non-navigable associations
	 */
	private List<MAssociation> nonNavigableAssociations;

	/**
	 * Dao
	 */
	private MDaoImpl dao;

	/**
	 * Factory implementation
	 */
	private MFactory factory;

	/**
	 * Factory interface
	 */
	private MFactoryInterface factoryInterface;

	/**
	 * Table
	 */
	private Table table;

	/**
	 * Transient
	 */
	private boolean transcient = false;

	/**
	 * Customizable
	 */
	private boolean customizable = false;

	/**
	 * Embedded
	 * If set, this entity represents a data type that is embedded in its parent entity.
	 */
	private boolean embedded = false;

	/**
	 * If embedded, type description of the root data type to be used by this entity.
	 */
	private ITypeDescription embeddedInitialType;

	/**
	 * Full class name of the parent entity
	 */
	private String embeddedParentClassName;

	/**
	 * Name of the attribute pointing to the current entity in its parent.
	 */
	private String embeddedAttributeNameInParent;

	/**
	 * Bean scope
	 */
	private MBeanScope scope = MBeanScope.REQUEST;

	/**
	 * Constructor
	 *
	 * @param p_sName       entity name
	 * @param p_oPackage    package
	 * @param p_sUmlName    uml name
	 * @param p_sEntityName entity name
	 */
	protected MEntityImpl(String p_sName, MPackage p_oPackage, String p_sUmlName, String p_sEntityName) {
		super("class", p_sUmlName, p_sName, p_oPackage);
		this.entityName = p_sEntityName;
		this.mapNameAssociations = new HashMap<String, MAssociation>();
		this.associations = new ArrayList<MAssociation>();
		this.nonNavigableAssociations = new ArrayList<MAssociation>();
		this.setDocumentation("");
	}

	/**
	 * Return associations
	 *
	 * @return associations
	 */
	public List<MAssociation> getAssociations() {
		return this.associations;
	}

	/**
	 * Get association by name
	 *
	 * @param p_sName          association name
	 * @param p_bCaseSensitive case sensitive match
	 * @return association
	 */
	public MAssociation getAssociationByName(String p_sName, boolean p_bCaseSensitive) {
		MAssociation r_oAssociation = null;
		if (p_bCaseSensitive) {
			r_oAssociation = this.mapNameAssociations.get(p_sName);
		} else {
			CaseInsensitiveMap oCaseInsensitiveMap = new CaseInsensitiveMap(this.mapNameAssociations);
			r_oAssociation = (MAssociation) oCaseInsensitiveMap.get(p_sName);
		}
		if (r_oAssociation == null) {
			MIdentifierElem oMIdentifierElem = this.getIdentifier().getElemByName(p_sName, p_bCaseSensitive);
			if (oMIdentifierElem instanceof MAssociation) {
				r_oAssociation = (MAssociation) oMIdentifierElem;
			}
		}
		return r_oAssociation;
	}

	/**
	 * Get association by criteria name
	 *
	 * @param p_sCriteriaName criteria name
	 * @return association
	 */
	public MAssociation getAssociationByCritereName(String p_sCriteriaName) {

		List<MAssociation> listAssociations = new ArrayList<MAssociation>(this.mapNameAssociations.values());
		listAssociations.addAll(this.getIdentifier().getElemOfTypeAssociation());
		listAssociations.addAll(this.nonNavigableAssociations);

		MAssociation r_oAssociation = null;
		Iterator<MAssociation> iterAssocs = listAssociations.iterator();
		while (iterAssocs.hasNext() && r_oAssociation == null) {
			MAssociation oAsso = (MAssociation) iterAssocs.next();
			if (oAsso.getAssociationType() == AssociationType.MANY_TO_MANY && !oAsso.isTransient()) {
				MAssociationPersistableManyToMany oMAssociationManyToMany = (MAssociationPersistableManyToMany) oAsso;
				if (oMAssociationManyToMany.getNameForJoinClass().equalsIgnoreCase(p_sCriteriaName)) {
					r_oAssociation = oMAssociationManyToMany;
				}
			} else {
				if (oAsso.getName().equalsIgnoreCase(p_sCriteriaName)) {
					r_oAssociation = oAsso;
				}
			}
		}
		return r_oAssociation;
	}

	/**
	 * Get non navigable associations
	 *
	 * @return non navigable associations
	 */
	public List<MAssociation> getNonNavigableAssociations() {
		return this.nonNavigableAssociations;
	}

	/**
	 * Get non navigable association by criteria name
	 *
	 * @param p_sCriteriaName criteria name
	 * @return non navigable association
	 */
	public MAssociation getNonNavigableAssociationByCritereName(String p_sCriteriaName) {

		MAssociation r_oAssociation = null;
		Iterator<MAssociation> iterAssocs = this.nonNavigableAssociations.iterator();
		while (iterAssocs.hasNext() && r_oAssociation == null) {
			MAssociation oAsso = (MAssociation) iterAssocs.next();
			if (oAsso.getAssociationType() == AssociationType.MANY_TO_MANY && !oAsso.isTransient()) {
				MAssociationPersistableManyToMany oMAssociationManyToMany = (MAssociationPersistableManyToMany) oAsso;
				if (oMAssociationManyToMany.getNameForJoinClass().equalsIgnoreCase(p_sCriteriaName)) {
					r_oAssociation = oMAssociationManyToMany;
				}
			} else {
				if (oAsso.getName().equalsIgnoreCase(p_sCriteriaName)) {
					r_oAssociation = oAsso;
				}
			}
		}
		return r_oAssociation;
	}

	/**
	 * Add an association
	 *
	 * @param p_oAssociation association
	 */
	public void addAssociation(MAssociation p_oAssociation) {
		this.mapNameAssociations.put(p_oAssociation.getName(), p_oAssociation);
		this.associations.add(p_oAssociation);
		this.addImport(p_oAssociation.getTypeDesc().getName());
		if (p_oAssociation.getTypeDesc().getParameterizedElementType().size() == 1) {
			this.addImport(p_oAssociation.getTypeDesc().getParameterizedElementType().get(0).getName());
		}
	}

	/**
	 * Add a non-navigable association
	 *
	 * @param p_oAssociation non-navigable association
	 */
	public void addNonNavigableAssociation(MAssociation p_oAssociation) {
		this.nonNavigableAssociations.add(p_oAssociation);
	}

	/**
	 * Return dao
	 *
	 * @return dao
	 */
	public MDaoImpl getDao() {
		return this.dao;
	}

	/**
	 * Define dao for entity
	 *
	 * @param p_oDao dao
	 */
	public void setDao(MDaoImpl p_oDao) {
		this.dao = p_oDao;
	}

	/**
	 * Get factory for entity
	 *
	 * @return factory for entity
	 */
	public MFactory getFactory() {
		return this.factory;
	}

	/**
	 * Set factory
	 *
	 * @param p_oFactory factory for entity
	 */
	public void setFactory(MFactory p_oFactory) {
		this.factory = p_oFactory;
	}

	/**
	 * Get factory interface of entity
	 *
	 * @return factory interface of entity
	 */
	public MFactoryInterface getFactoryInterface() {
		return factoryInterface;
	}

	/**
	 * Define factory interface for entity
	 *
	 * @param p_oFactoryInterface factory interface for entity
	 */
	public void setFactoryInterface(MFactoryInterface p_oFactoryInterface) {
		this.factoryInterface = p_oFactoryInterface;
	}

	/**
	 * Get table for entity (if persistent)
	 *
	 * @return table for entity
	 */
	public Table getTable() {
		return table;
	}

	/**
	 * Define table of entity (if persistent)
	 *
	 * @param p_oTable table of entity
	 */
	public void setTable(Table p_oTable) {
		this.table = p_oTable;
	}

	/**
	 * Get entity name
	 *
	 * @return entity name
	 */
	public String getEntityName() {
		return this.entityName;
	}

	/**
	 * Set entity name
	 *
	 * @param p_sEntityName entity name
	 */
	public void setEntityName(String p_sEntityName) {
		this.entityName = p_sEntityName;
	}

	/**
	 * Return true if entity is transient
	 *
	 * @return true if entity is transient
	 */
	public boolean isTransient() {
		return this.transcient;
	}

	/**
	 * Define if entity is transient
	 *
	 * @param p_bTransient true if entity is transient
	 */
	public void setTransient(boolean p_bTransient) {
		this.transcient = p_bTransient;
	}

	/**
	 * Return true if customizable
	 *
	 * @return true if this entity is customizable
	 */
	public boolean isCustomizable() {
		return this.customizable;
	}

	/**
	 * Define if entity is customizable
	 *
	 * @param p_bCutomizable true if this entity is customizable.
	 */
	public void setCustomizable(boolean p_bCutomizable) {
		this.customizable = p_bCutomizable;
	}

	/**
	 * Return true if this entity has an embedded data type.
	 *
	 * @return Return true if this entity has an embedded data type
	 */
	public boolean isEmbedded() {
		return this.embedded;
	}

	/**
	 * Define whether this entity data type is embedded.
	 *
	 * @param p_bEmbedded true if this entity has an embedded data type
	 */
	public void setEmbedded(boolean p_bEmbedded) {
		this.embedded = p_bEmbedded;
	}

	/**
	 * Returns the embedded initial data type, if any
	 *
	 * @return
	 */
	public ITypeDescription getEmbeddedInitialType() {
		return this.embeddedInitialType;
	}

	/**
	 * Set the value of the embedded data type to be used
	 *
	 * @param p_oTypeDescription
	 */
	public void setEmbeddedInitialType(ITypeDescription p_oTypeDescription) {
		this.embeddedInitialType = p_oTypeDescription;
	}

	/**
	 * Get bean scope
	 *
	 * @return bean scope
	 */
	public MBeanScope getScope() {
		return this.scope;
	}

	/**
	 * Define bean scope
	 *
	 * @param p_oScope bean scope
	 */
	public void setScope(MBeanScope p_oScope) {
		this.scope = p_oScope;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		return new MEntityImpl(this.getName(), this.getPackage(), this.getUmlName(), this.getEntityName());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.a2a.adjava.xmodele.SClass#toXmlInsertBeforeIdentifier(org.dom4j.Element)
	 */
	@Override
	protected void toXmlInsertBeforeIdentifier(Element p_xElement) {
		super.toXmlInsertBeforeIdentifier(p_xElement);
		if (this.table != null) {
			p_xElement.addElement("table-name").setText(this.table.getName());
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.a2a.adjava.xmodele.SClass#toXmlInsertAfterIdentifier(org.dom4j.Element)
	 */
	@Override
	protected void toXmlInsertAfterIdentifier(Element p_xElement) {
		super.toXmlInsertAfterIdentifier(p_xElement);
		if (this.factoryInterface != null) {
			Element r_xFactoryInterface = p_xElement.addElement("pojo-factory-interface");
			r_xFactoryInterface.addElement("name").setText(this.factoryInterface.getName());
			r_xFactoryInterface.addElement("bean-name").setText(this.factoryInterface.getBeanName());
			r_xFactoryInterface.addElement("import").setText(
					StringUtils.join(this.factoryInterface.getPackage().getFullName(), StrUtils.DOT_S, this.factoryInterface.getName()));
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.a2a.adjava.xmodele.SClass#toXmlInsertBeforeImport(org.dom4j.Element)
	 */
	@Override
	protected void toXmlInsertBeforeImport(Element p_xElement) {
		super.toXmlInsertBeforeImport(p_xElement);
		for (MAssociation oMAssociationEnd : this.associations) {
			p_xElement.add(oMAssociationEnd.toXml());
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see com.a2a.adjava.xmodele.SClass#toXmlInsertBeforeDocumentation(org.dom4j.Element)
	 */
	@Override
	protected void toXmlInsertBeforeDocumentation(Element p_xElement) {
		super.toXmlInsertBeforeDocumentation(p_xElement);

		p_xElement.addElement("name-uppercase").setText(this.getName().toUpperCase());
		p_xElement.addElement("name-uncapitalized").setText(StringUtils.uncapitalize(this.getName()));
		p_xElement.addElement("transient").setText(Boolean.toString(this.isTransient()));
		p_xElement.addElement("customizable").setText(Boolean.toString(this.isCustomizable()));
		p_xElement.addElement("scope").setText(this.scope.name());

		// If the entity is embedded (which means its data is stored inside its parent entity datatble),
		// output the embedded flag and the initial data type of the entity
		p_xElement.addElement("embedded").setText(Boolean.toString(this.isEmbedded()));
		if (this.isEmbedded()) {
			p_xElement.addElement("embedded-initial-type-full-name").setText(this.embeddedInitialType.getName());
			p_xElement.addElement("embedded-initial-type-short-name").setText(this.embeddedInitialType.getShortName());
		}

		if (this.dao != null) {
			Element xDao = p_xElement.addElement("dao");
			xDao.addAttribute("name", this.dao.getName());
			xDao.addAttribute("full-name", this.dao.getFullName());
			Element xDaoInterface = p_xElement.addElement("dao-interface");
			xDaoInterface.addAttribute("name", this.dao.getMasterInterface().getName());
			xDaoInterface.addAttribute("full-name", this.dao.getMasterInterface().getFullName());
		}

		int iJdbcPos = 1;
		for (Element xAttr : (List<Element>) p_xElement.selectNodes(
				"descendant::attribute[(not(../@type) or ../@type!='one-to-many') and (not(../../@type) or ../../@type!='one-to-many')]")) {
			xAttr.addAttribute("pos", Integer.toString(iJdbcPos));
			iJdbcPos++;
		}
	}

	/**
	 * Return the full class name of the parent entity
	 */
	public String getEmbeddedParentClassName() {
		return embeddedParentClassName;
	}

	/**
	 * Set the full class name of the parent entity
	 *
	 * @param embeddedParentClassName
	 */
	public void setEmbeddedParentClassName(String embeddedParentClassName) {
		this.embeddedParentClassName = embeddedParentClassName;
	}

	/**
	 * Return the name of the attribute pointing to the current entity in its parent.
	 */
	public String getEmbeddedAttributeNameInParent() {
		return embeddedAttributeNameInParent;
	}

	/**
	 * Set the name of the attribute of the parent entity
	 *
	 * @param embeddedAttributeNameInParent
	 */
	public void setEmbeddedAttributeNameInParent(String embeddedAttributeNameInParent) {
		this.embeddedAttributeNameInParent = embeddedAttributeNameInParent;
	}
}
