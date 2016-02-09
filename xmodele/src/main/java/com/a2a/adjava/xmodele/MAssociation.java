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
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.uml.UmlAssociationEnd.AggregateType;
import com.a2a.adjava.utils.StrUtils;

/**
 * <p>
 * Represente une association entre deux classes
 * </p>
 * 
 * <p>
 * Copyright (c) 2009
 * <p>
 * Company: Adeuza
 * 
 * @author lmichenaud
 * 
 */
public class MAssociation implements SAnnotable {

	/**
	 * <p>
	 * Les différents types d'association
	 * </p>
	 * 
	 * <p>
	 * Copyright (c) 2009
	 * <p>
	 * Company: Adeuza
	 * 
	 * @author lmichenaud
	 * 
	 */
	public enum AssociationType {
		/**
		 * Relation Many to one
		 */
		MANY_TO_ONE,
		/**
		 * Relation One to one
		 */
		ONE_TO_ONE,
		/**
		 * Relation one to many
		 */
		ONE_TO_MANY,
		/**
		 * Relation many to many
		 */
		MANY_TO_MANY;

		/**
		 * Retourne la representation xml du type d'association
		 * 
		 * @return la representation xml du type d'association
		 */
		public String toXml() {
			return name().toLowerCase().replaceAll("_", "-");
		}
	}

	/**
	 * Nom de l'association
	 */
	private String name;

	/**
	 * Type d'association
	 */
	private AssociationType associationType;

	/**
	 * 
	 */
	private AggregateType aggregateType = AggregateType.NONE ;

	/**
	 * Nom de la cascade
	 */
	private String cascadeName;

	/**
	 * La classe referencee (destination)
	 */
	private MEntityImpl refClass;

	/**
	 * La classe opposee (source)
	 */
	private MEntityImpl oppositeClass;

	/**
	 * Nom de la variable a utiliser pour l'association
	 */
	private String variableName;

	/**
	 * Nom de la variable de type List a utiliser pour l'association
	 */
	private String variableListName;

	/**
	 * Nom de parametre a utiliser pour l'association
	 */
	private String parameterName;

	/**
	 * Descripteur du type
	 */
	private ITypeDescription typeDesc;

	/**
	 * Visibilite dans la classe
	 */
	private String visibility;

	/**
	 * La classe destination est la meme que la classe source
	 */
	private boolean selfRef;

	/**
	 * L'association a deux sens. Est-ce que c'est le sens proprietaire ?
	 */
	private boolean relationOwner;

	/**
	 * Nom de l'association dans l'autre sens
	 */
	private String oppositeName;

	/**
	 * 
	 */
	private AggregateType oppositeAggregateType ;

	/**
	 * True if opposite association end is navigable
	 */
	private boolean oppositeNavigable ;
	
	/**
	 * Annotations
	 */
	private List<MAnnotation> annotations = new ArrayList<MAnnotation>();

	/**
	 * Transient
	 */
	private boolean bTransient = false ;

	
	/**
	 * Constructeur d'une association
	 * @param p_sName nom de l'association
	 * @param p_oAssociationType type d'association
	 * @param p_oAggregateType type de l'aggrégation
	 * @param p_oOppositeAggregateType type de l'aggrégation inverse
	 * @param p_oRefClass classe référencée (destination)
	 * @param p_oOppositeClass classe opposée (source)
	 * @param p_sVariableName nom de la variable à utiliser pour l'association
	 * @param p_sVariableListName nom de la variable de type liste à utiliser pour l'association
	 * @param p_sParameterName nom de paramètre à utiliser pour l'association
	 * @param p_oTypeDescription descripteur du type
	 * @param p_sVisibility visibilité
	 * @param p_bRelationOwnerL'association a deux sens. Est-ce que c'est le sens propriétaire ?
	 * @param p_sOppositeName Nom de l'association dans l'autre sens
	 * @param p_bOppositeNavigable true si la fin de l'association est navigable, false sinon
	 */
	public MAssociation(String p_sName, AssociationType p_oAssociationType, AggregateType p_oAggregateType, 
			AggregateType p_oOppositeAggregateType, MEntityImpl p_oRefClass, MEntityImpl p_oOppositeClass, String p_sVariableName,
			String p_sVariableListName, String p_sParameterName, ITypeDescription p_oTypeDescription, 
			String p_sVisibility, boolean p_bRelationOwner, String p_sOppositeName, boolean p_bOppositeNavigable) {
		this.name = p_sName;
		this.cascadeName = this.name.toUpperCase();
		this.refClass = p_oRefClass;
		this.oppositeClass = p_oOppositeClass;
		this.variableName = p_sVariableName;
		this.variableListName = p_sVariableListName;
		this.parameterName = p_sParameterName;
		this.typeDesc = p_oTypeDescription;
		this.visibility = p_sVisibility;
		this.selfRef = p_oRefClass.equals(p_oOppositeClass);
		this.relationOwner = p_bRelationOwner;
		this.oppositeName = p_sOppositeName;
		this.visibility = p_sVisibility;
		this.associationType = p_oAssociationType;
		this.aggregateType = p_oAggregateType ;
		this.oppositeAggregateType = p_oOppositeAggregateType ;
		this.oppositeNavigable = p_bOppositeNavigable ;
	}

	/**
	 * Retourne le nom de l'association
	 * 
	 * @return le nom de l'association
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Retourne la classe référencée
	 * 
	 * @return la classe référencée
	 */
	public MEntityImpl getRefClass() {
		return this.refClass;
	}

	/**
	 * Le nom du paramètre à utiliser pour l'association
	 * 
	 * @return nom du paramètre à utiliser pour l'association
	 */
	public String getParameterName() {
		return this.parameterName;
	}

	/**
	 * Le descripteur de type
	 * 
	 * @return descripteur de type
	 */
	public ITypeDescription getTypeDesc() {
		return this.typeDesc;
	}

	/**
	 * Retourne la classe opposée (source)
	 * 
	 * @return la classe opposée (la source)
	 */
	public MEntityImpl getOppositeClass() {
		return this.oppositeClass;
	}

	/**
	 * L'association a deux sens. Est-ce que c'est le sens propriétaire ?
	 * 
	 * @return vrai si l'association est le sens propriétaire
	 */
	public boolean isRelationOwner() {
		return relationOwner;
	}

	/**
	 * Retourne le nom opposé de l'association
	 * 
	 * @return le nom opposé de l'association
	 */
	public String getOppositeName() {
		return this.oppositeName;
	}

	/**
	 * Retourne le type de l'association
	 * 
	 * @return le type de l'association
	 */
	public AssociationType getAssociationType() {
		return associationType;
	}

	/**
	 * Retourne le nom de la variable à utiliser pour l'association
	 * 
	 * @return le nom de la variable à utiliser pour l'association
	 */
	public String getVariableName() {
		return variableName;
	}

	/**
	 * Retourne le nom de la variable de type liste à utiliser pour l'association
	 * 
	 * @return le nom de la variable de type liste à utiliser pour l'association
	 */
	public String getVariableListName() {
		return variableListName;
	}

	/**
	 *  Ajoute une annotation
	 * @param p_oAnnotation L'annotation à ajouter
	 */
	public void addAnnotation( MAnnotation p_oAnnotation ) {
		this.annotations.add( p_oAnnotation );
	}

	/**
	 * Retourne la liste des annotations
	 * @return la liste des annotations
	 */
	public List<MAnnotation> getAnnotations() {
		return this.annotations ;
	}

	/**
	 * Retourne le type de l'aggrégation
	 * @return Le type de l'aggrégation
	 */
	public AggregateType getAggregateType() {
		return aggregateType;
	}

	/**
	 * Retourne le type de l'aggrégation inverse
	 * @return Le type de l'aggrégation inverse
	 */
	public AggregateType getOppositeAggregateType() {
		return oppositeAggregateType;
	}

	/**
	 * Met à jour le type de l'aggrégation inverse
	 * @param p_oOppositeAggregateType  Le type de l'aggrégation inverse
	 */
	public void setOppositeAggregateType(AggregateType p_oOppositeAggregateType) {
		this.oppositeAggregateType = p_oOppositeAggregateType;
	}

	/**
	 * Retourne le caractère transient de l'association
	 * @return Indique si l'association est transiente
	 */
	public boolean isTransient() {
		return this.bTransient;
	}

	/**
	 * Met à jour le caractère transient de l'association
	 * @param p_bTransient Met à jour le caractère transient de l'association
	 */
	public void setTransient(boolean p_bTransient) {
		this.bTransient = p_bTransient;
	}

	/**
	 * Indique si l'association référence la même classe au début 
	 * et à la fin de l'association
	 * @return true si l'association référence la même classe de de début et de fin, 
	 * false sinon
	 */
	public boolean isSelfRef() {
		return this.selfRef;
	}

	/**
	 * Retourne la représentation xml de l'association
	 * 
	 * @return la représentation xml de l'association
	 */
	public Element toXml() {
		Element r_xAssociation = DocumentHelper.createElement("association");
		r_xAssociation.addAttribute("name", this.name);
		r_xAssociation.addAttribute("type", this.associationType.toXml());
		r_xAssociation.addAttribute("cascade-name", this.cascadeName);
		r_xAssociation.addAttribute("visibility", this.visibility);
		String type = "";
		if (this.typeDesc.getParameterizedElementType().size() > 0)
		{
			type = this.typeDesc.getName().replaceAll("__T__", this.typeDesc.getParameterizedElementType().get(0).getShortName());
		}
		else
		{
			type = this.typeDesc.getName().replaceAll("<__T__>", type);
		}
		r_xAssociation.addAttribute("type-name", type);
		// r_xAssociation.addAttribute("type-name", this.typeDesc.getName());
		String init = "";
		if (this.typeDesc.getParameterizedElementType().size() > 0)
		{
			init = this.typeDesc.getDefaultValue().replaceAll("__T__", this.typeDesc.getParameterizedElementType().get(0).getShortName());
		}
		else
		{
			init = this.typeDesc.getDefaultValue().replaceAll("<__T__>", type);
		}
		r_xAssociation.addAttribute("type-init-format", init);
		type = "";
		if (this.typeDesc.getParameterizedElementType().size() > 0)
		{
			type = this.typeDesc.getShortName().replaceAll("__T__", this.typeDesc.getParameterizedElementType().get(0).getShortName());
		}
		else
		{
			type = this.typeDesc.getShortName().replaceAll("<__T__>", type);
		}
		r_xAssociation.addAttribute("type-short-name", type);
		//r_xAssociation.addAttribute("type-short-name", this.typeDesc.getShortName());
		r_xAssociation.addAttribute("aggregate-type", this.aggregateType.name());
		r_xAssociation.addAttribute("opposite-aggregate-type", this.oppositeAggregateType.name());
		if (this.typeDesc.getParameterizedElementType().size() == 1) {
			r_xAssociation.addAttribute("contained-type-name", this.typeDesc.getParameterizedElementType().get(0).getName());
			r_xAssociation.addAttribute("contained-type-short-name", this.typeDesc.getParameterizedElementType().get(0).getShortName());
		}
		r_xAssociation.addAttribute("class", this.getRefClass().getFullName());
		r_xAssociation.addAttribute("self-ref", Boolean.toString(this.selfRef));
		r_xAssociation.addAttribute("relation-owner", Boolean.toString(this.relationOwner));
		r_xAssociation.addAttribute("opposite-name", this.oppositeName);
		r_xAssociation.addAttribute("opposite-capitalized-name", StringUtils.capitalize(this.oppositeName));
		r_xAssociation.addAttribute("opposite-navigable", Boolean.toString(this.oppositeNavigable));
		if(this.oppositeName != null) {
			r_xAssociation.addAttribute("opposite-cascade-name", this.oppositeName.toUpperCase());
		}

		// On vérifie si la relation est transiente également.
		// Une relation est transiente si l'un des entités qu'elle associe est transiente.
		boolean isTransient = this.isTransient();
		isTransient = isTransient || this.refClass.isTransient();
		for(MAssociation oAssociaton : this.refClass.getAssociations()) {
			if(oAssociaton.refClass.getAssociations().contains(this)) {
				isTransient = isTransient || oAssociaton.refClass.isTransient();
			}
		}
		r_xAssociation.addAttribute("transient", Boolean.toString(isTransient));

		Element xInterface = r_xAssociation.addElement("interface");
		xInterface.addElement("full-name").setText(this.refClass.getMasterInterface().getFullName());
		xInterface.addElement("name").setText(this.refClass.getMasterInterface().getName());
		Element xLinkedInterface = xInterface.addElement("linked-interfaces");
		for( MLinkedInterface oLinkedInterface : this.refClass.getMasterInterface().getLinkedInterfaces()) {
			xLinkedInterface.add( oLinkedInterface.toXml());
		}

		Element xClass = r_xAssociation.addElement("class");
		xClass.addElement("full-name").setText(this.refClass.getFullName());
		xClass.addElement("name").setText(this.refClass.getName());

		if ( this.refClass.getFactoryInterface() != null ) {
			Element xFactory = r_xAssociation.addElement("pojo-factory-interface");
			xFactory.addElement("name").setText(this.refClass.getFactoryInterface().getName());
			xFactory.addElement("bean-name").setText(this.refClass.getFactoryInterface().getBeanName());
			xFactory.addElement("import").setText(
					this.refClass.getFactoryInterface().getPackage().getFullName().concat(StrUtils.DOT_S).concat(this.refClass.getFactoryInterface().getName()));
		}

		r_xAssociation.addElement("get-accessor").setText("get" + this.name.substring(0, 1).toUpperCase() + this.name.substring(1));
		r_xAssociation.addElement("set-accessor").setText("set" + this.name.substring(0, 1).toUpperCase() + this.name.substring(1));

		if (this.refClass.getDao() != null) {
			Element xDao = r_xAssociation.addElement("dao");
			xDao.addElement("name").setText(this.refClass.getDao().getName());
			xDao.addElement("full-name").setText(this.refClass.getDao().getFullName());
			xDao.addElement("bean-ref").setText(
					this.refClass.getDao().getName().substring(0, 1).toLowerCase() + this.refClass.getDao().getName().substring(1));

			Element xDaoInterface = r_xAssociation.addElement("dao-interface");
			xDaoInterface.addElement("name").setText(this.refClass.getDao().getMasterInterface().getName());
			xDaoInterface.addElement("full-name").setText(this.refClass.getDao().getMasterInterface().getFullName());
			String sDaoInterfaceBeanRef = this.refClass.getDao().getMasterInterface().getName().substring(0, 1).toLowerCase()
					+ this.refClass.getDao().getMasterInterface().getName().substring(1);
			xDaoInterface.addElement("bean-ref").setText(sDaoInterfaceBeanRef);

			Element xElem = xDao.addElement("delete-cascade");
			for( MAssociation oDeleteCascade: this.refClass.getDao().getDeleteCascade()) {
				Element xCascade = xElem.addElement("cascade");
				xCascade.addAttribute("name", oDeleteCascade.getName().toUpperCase());
				xCascade.addElement("entity-interface").setText(oDeleteCascade.getOppositeClass().getMasterInterface().getName());
			}
		}

		Element xOppositeInterface = r_xAssociation.addElement("opposite-interface");
		xOppositeInterface.addElement("full-name").setText(this.oppositeClass.getMasterInterface().getFullName());
		xOppositeInterface.addElement("name").setText(this.oppositeClass.getMasterInterface().getName());

		if (this.oppositeClass.getDao() != null) {
			Element xDao = r_xAssociation.addElement("opposite-dao");
			xDao.addElement("name").setText(this.oppositeClass.getDao().getName());
			xDao.addElement("full-name").setText(this.oppositeClass.getDao().getFullName());

			Element xOppositeDaoInterface = r_xAssociation.addElement("opposite-dao-interface");
			xOppositeDaoInterface.addElement("name").setText(this.oppositeClass.getDao().getMasterInterface().getName());
			xOppositeDaoInterface.addElement("full-name").setText(this.oppositeClass.getDao().getMasterInterface().getFullName());
		}

		r_xAssociation.addElement("method-crit-name").setText( StringUtils.capitalize(this.parameterName));
		if (this.oppositeName != null && this.oppositeName.length() > 0) {
			r_xAssociation.addElement("method-crit-opposite-name").setText(
					this.oppositeName.substring(0, 1).toUpperCase() + this.oppositeName.substring(1));
		}
		r_xAssociation.addElement("parameter-name").setText(this.parameterName);
		r_xAssociation.addElement("variable-name").setText(this.variableName);
		r_xAssociation.addElement("variable-list-name").setText(this.variableListName);
		r_xAssociation.addAttribute("name-capitalized", StringUtils.capitalize(this.name));

		Element xAnnotations = r_xAssociation.addElement("annotations");
		for (MAnnotation oAnnotation : this.annotations) {
			xAnnotations.add(oAnnotation.toXml());
		}

		return r_xAssociation;
	}
}
