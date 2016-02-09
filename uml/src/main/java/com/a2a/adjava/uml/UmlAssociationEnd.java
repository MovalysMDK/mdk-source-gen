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
package com.a2a.adjava.uml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Uml association end
 * @author lmichenaud
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UmlAssociationEnd extends UmlStereotypedObject {

	/**
	 * Aggregate Type
	 * @author lmichenaud
	 *
	 */
	public enum AggregateType {
		
		/**
		 * No aggregation 
		 */
		NONE,
		
		/**
		 * Composition 
		 */
		COMPOSITE,
		
		/**
		 * Aggregation
		 */
		AGGREGATE;
		
		/**
		 * Return enum by value
		 * @param p_sValue value
		 * @return enum
		 */
		public static AggregateType getByString(String p_sValue) {
			AggregateType r_oAggregateType = NONE ;
			if ("composite".equals(p_sValue)) {
				r_oAggregateType = COMPOSITE;
			}
			else if ("aggregate".equals(p_sValue) || "shared".equals(p_sValue)) {
				r_oAggregateType = AGGREGATE;
			}
			return r_oAggregateType ;
		}
	}
	
	/**
	 * End name
	 */
	@XmlAttribute
	private String name;

	/**
	 * Visibility
	 */
	@XmlAttribute
	private String visibility;

	/**
	 * Ordered
	 */
	@XmlAttribute
	private boolean ordered;

	/**
	 * Navigable
	 */
	@XmlAttribute
	private boolean navigable;

	/**
	 * Multiplicity lower
	 */
	@XmlAttribute
	private Integer multiplicityLower;

	/**
	 * Multiplicity upper
	 */
	@XmlAttribute
	private Integer multiplicityUpper;

	/**
	 * Target class
	 */
	@XmlAttribute
	@XmlIDREF
	private UmlClass refClass;
	
	/**
	 * Target class id
	 */
	@XmlTransient
	private String refClassId;
	
	/**
	 * Options
	 */
	@XmlAttribute
	private String options ;
	
	/**
	 * Aggregate type
	 */
	@XmlAttribute
	private AggregateType aggregateType ;
	
	/**
	 * Association
	 */
	@XmlTransient
	private UmlAssociation association ;
	
	/**
	 * Opposite association end
	 */
	@XmlTransient
	private UmlAssociationEnd oppositeAssociationEnd ;
	
	/**
	 * Part of id
	 */
	@XmlAttribute
	private boolean id ;
	
	/**
	 * Constructor
	 */
	private UmlAssociationEnd() {
		// empty constructor for jaxb
	}
	
	/**
	 * Constructor
	 * @param p_sName association end name
	 * @param p_sVisibility visibility
	 * @param p_bOrdered ordered
	 * @param p_bNavigable navigable
	 * @param p_iMultiplicityLower multiplicity lower
	 * @param p_iMultiplicityUpper multiplicity upper
	 * @param p_oRefClass target class
	 * @param p_sOptions options
	 * @param p_oAggregateType aggregate type
	 */
	public UmlAssociationEnd(String p_sName, String p_sVisibility,
			boolean p_bOrdered, boolean p_bNavigable, Integer p_iMultiplicityLower,
			Integer p_iMultiplicityUpper, String p_sOptions, AggregateType p_oAggregateType) {
		if (p_sName != null) {
			this.name = p_sName.trim();
		} else {
			this.name = null;
		}
		this.visibility = p_sVisibility;
		this.ordered = p_bOrdered;
		this.navigable = p_bNavigable;
		this.multiplicityLower = p_iMultiplicityLower;
		this.multiplicityUpper = p_iMultiplicityUpper;
		this.options = p_sOptions ;
		this.aggregateType = p_oAggregateType;
	}
	
	/**
	 * Get aggregate type
	 * @return aggregate type
	 */
	public AggregateType getAggregateType() {
		return this.aggregateType;
	}

	/**
	 * Get multiplicity lower
	 * @return multiplicity lower
	 */
	public Integer getMultiplicityLower() {
		return this.multiplicityLower;
	}

	/**
	 * Get multiplicity upper
	 * @return multiplicity upper
	 */
	public Integer getMultiplicityUpper() {
		return this.multiplicityUpper;
	}

	/**
	 * Get association end name
	 * @return association end name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Return true if navigable
	 * @return true if navigable
	 */
	public boolean isNavigable() {
		return this.navigable;
	}

	/**
	 * Return true if ordered
	 * @return true if ordered
	 */
	public boolean isOrdered() {
		return this.ordered;
	}

	/**
	 * Get visibility
	 * @return visibility
	 */
	public String getVisibility() {
		return this.visibility;
	}

	/**
	 * Get target class
	 * @return target class
	 */
	public UmlClass getRefClass() {
		return this.refClass;
	}

	/**
	 * Get options
	 * @return options
	 */
	public String getOptions() {
		return this.options;
	}

	/**
	 * Get association
	 * @return association
	 */
	public UmlAssociation getAssociation() {
		return this.association;
	}

	/**
	 * Set association
	 * @param p_oAssociation association
	 */
	public void setAssociation(UmlAssociation p_oAssociation) {
		this.association = p_oAssociation;
	}

	/**
	 * Get opposite association end
	 * @return opposite association end
	 */
	public UmlAssociationEnd getOppositeAssociationEnd() {
		return this.oppositeAssociationEnd;
	}

	/**
	 * Set opposite association end
	 * @param p_oOppositeAssociationEnd opposite association end
	 */
	public void setOppositeAssociation(UmlAssociationEnd p_oOppositeAssociationEnd) {
		this.oppositeAssociationEnd = p_oOppositeAssociationEnd;
	}

	/**
	 * Set visibility
	 * @param p_sVisibility visibility
	 */
	public void setVisibility(String p_sVisibility) {
		this.visibility = p_sVisibility;
	}

	/**
	 * Id of target class
	 * @return Id of target class
	 */
	public String getRefClassId() {
		return this.refClassId;
	}

	/**
	 * Set target class
	 * @param p_oRefClass target class
	 */
	public void setRefClass(UmlClass p_oRefClass) {
		this.refClass = p_oRefClass;
	}

	/**
	 * Set if of target class
	 * @param p_sRefClassId id of target class
	 */
	public void setRefClassId(String p_sRefClassId) {
		this.refClassId = p_sRefClassId;
	}
	
	/**
	 * Set aggregate type
	 * @param p_oAggregateType aggregate type
	 */
	public void setAggregateType(AggregateType p_oAggregateType) {
		this.aggregateType = p_oAggregateType;
	}

	/**
	 * Return id
	 * @return id
	 */
	public boolean isId() {
		return this.id;
	}

	/**
	 * Define id
	 * @param p_bId id
	 */
	public void setId(boolean p_bId) {
		this.id = p_bId;
	}

	/**
	 * Set navigability
	 * @param p_bNavigable true if navigable
	 */
	public void setNavigable(boolean p_bNavigable) {
		this.navigable = p_bNavigable;
	}
}
