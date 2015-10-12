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
package com.a2a.adjava.xmi.v21vpuml;

import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.LOWERVALUE_NODE;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.MEMBEREND_NODE;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.MODELEXTENSION_NODE;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.NAME_ATTR;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.OWNEDATTRIBUTE_AGGREGATION_ATTR;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.OWNEDATTRIBUTE_ISID_ATTR;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.OWNEDATTRIBUTE_NODE;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.OWNEDATTRIBUTE_VISIBILITY_ATTR;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.OWNEDEND_NODE;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.PACKAGEELEMENT_TYPE_ASSOCIATION;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.TYPE_ATTR;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.UPPERVALUE_NODE;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.VALUE_ATTR;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.XMIEXTENSION_NODE;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.XMIID2011_ATTR;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.XMIIDREF2011_ATTR;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.XMITYPE_ATTR;
import static com.a2a.adjava.xmi.v21vpuml.XMI21Constants.XMIVALUE2011_ATTR;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.dom4j.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.uml.UmlAssociation;
import com.a2a.adjava.uml.UmlAssociationClass;
import com.a2a.adjava.uml.UmlAssociationEnd;
import com.a2a.adjava.uml.UmlStereotype;
import com.a2a.adjava.uml.UmlAssociationEnd.AggregateType;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlMultiplicity;

/**
 * Association reader
 * @author lmichenaud
 *
 */
public final class XMI21AssociationReader {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(XMI21AssociationReader.class);

	/**
	 * Association reader
	 */
	private static XMI21AssociationReader associationReader = new XMI21AssociationReader();

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	protected static XMI21AssociationReader getInstance() {
		return associationReader;
	}

	/**
	 * Constructor
	 */
	private XMI21AssociationReader() {
		// private because singleton pattern
	}

	/**
	 * Read association
	 * @param p_xAssociation association node
	 * @param p_oModelDictionary model dictionary
	 */
	@SuppressWarnings("unchecked")
	protected void readAssociation(Element p_xAssociation, UmlDictionary p_oModelDictionary) 
			throws Exception {
	
		// XMI example: Aggregation
		// <ownedMember isAbstract="false" isDerived="false" isLeaf="false" name="mainPanel" xmi:id="PBaAFAqAUDAUjAwB" xmi:type="uml:Association">
		// 		<memberEnd xmi:idref="PBaAFAqAUDAUjAwC"/>
		// 		<ownedEnd aggregation="none" association="PBaAFAqAUDAUjAwB" isDerived="false" isDerivedUnion="false" isLeaf="false" isNavigable="true" isReadOnly="false" isStatic="false" type="HeygoAqAUDAUUQ6d" xmi:id="PBaAFAqAUDAUjAwC" xmi:type="uml:Property">
		//			...
		// 		</ownedEnd>
		// 		<memberEnd xmi:idref="PBaAFAqAUDAUjAwE"/>
		// 		<ownedEnd aggregation="shared" association="PBaAFAqAUDAUjAwB" isDerived="false" isDerivedUnion="false" isLeaf="false" isNavigable="true" isReadOnly="false" isStatic="false" type="WlWgoAqAUDAUUQ61" xmi:id="PBaAFAqAUDAUjAwE" xmi:type="uml:Property">
		// 			...
		// 		</ownedEnd>
		//		<xmi:Extension extender="Visual Paradigm">
		//			<qualityScore value="-1"/>
		//			<appliedStereotype xmi:value="Association_Mm_panel_id"/>
		//		</xmi:Extension>
		// </ownedMember>
		
		// XMI example: Relation N to N
		// <ownedMember isAbstract="false" isDerived="false" isLeaf="false" name="ChildrenSkill" xmi:id="Z9.VFAqAUDAUjB1I" xmi:type="uml:Association">
		//		<memberEnd xmi:idref="Z9.VFAqAUDAUjB1J"/>
		//		<ownedEnd aggregation="none" association="Z9.VFAqAUDAUjB1I" isDerived="false" isDerivedUnion="false" isLeaf="false" isNavigable="true" isReadOnly="false" isStatic="false" name="childrens_children" type="XpBgoAqAUDAUUQ7K" visibility="private" xmi:id="Z9.VFAqAUDAUjB1J" xmi:type="uml:Property">
		//			<xmi:Extension extender="Visual Paradigm">
		//				<associationEnd/>
		//				<qualifier name="" xmi:id="Z9.VFAqAUDAUjB1K" xmi:type="qualifier">
		//					<xmi:Extension extender="Visual Paradigm">
		//						<qualityScore value="-1"/>
		//					</xmi:Extension>
		//				</qualifier>
		//				<qualityScore value="-1"/>
		//			</xmi:Extension>
		//			<lowerValue value="0" xmi:id="Z9.VFAqAUDAUjB1J_multiplicity_lowerValue" xmi:type="uml:LiteralString"/>
		//			<upperValue value="*" xmi:id="Z9.VFAqAUDAUjB1J_multiplicity_upperValue" xmi:type="uml:LiteralString"/>
		//		</ownedEnd>
		//		<memberEnd xmi:idref="Z9.VFAqAUDAUjB1L"/>
		//		<ownedEnd aggregation="shared" association="Z9.VFAqAUDAUjB1I" isDerived="false" isDerivedUnion="false" isLeaf="false" isNavigable="true" isReadOnly="false" isStatic="false" name="skills_skill" type="uQoVFAqAUDAUjB0Z" visibility="private" xmi:id="Z9.VFAqAUDAUjB1L" xmi:type="uml:Property">
		//			<xmi:Extension extender="Visual Paradigm">
		//				<associationEnd/>
		//				<qualifier name="" xmi:id="Z9.VFAqAUDAUjB1M" xmi:type="qualifier">
		//					<xmi:Extension extender="Visual Paradigm">
		//						<qualityScore value="-1"/>
		//					</xmi:Extension>
		//				</qualifier>
		//				<qualityScore value="-1"/>
		//			</xmi:Extension>
		//			<lowerValue value="0" xmi:id="Z9.VFAqAUDAUjB1L_multiplicity_lowerValue" xmi:type="uml:LiteralString"/>
		//			<upperValue value="*" xmi:id="Z9.VFAqAUDAUjB1L_multiplicity_upperValue" xmi:type="uml:LiteralString"/>
		//		</ownedEnd>
		//		<xmi:Extension extender="Visual Paradigm">
		//			<qualityScore value="-1"/>
		//		</xmi:Extension>
		// </ownedMember>
		
		String sXmiId = p_xAssociation.attributeValue(XMIID2011_ATTR);
		String sName = p_xAssociation.attributeValue(NAME_ATTR);

		log.debug("association: {}, id: {}", sName, sXmiId);

		String sOptions = StringUtils.EMPTY;
		if (sName != null) {
			int iPos = sName.lastIndexOf('$');
			if (iPos != -1) {
				sOptions = sName.substring(iPos + 1);
				sName = sName.substring(0, iPos);
			}
		}
		else {
			sName = StringUtils.EMPTY;
		}
		
		// Read non-navigable association ends
		for( Element xOwnedEnd : (List<Element>) p_xAssociation.elements(OWNEDEND_NODE)) {
			this.readAssociationEnd(xOwnedEnd, p_oModelDictionary);
		}

		// Read members of association
		List<Element> xMembers = (List<Element>) p_xAssociation.elements(MEMBEREND_NODE);
		String sEnd1Id = xMembers.get(0).attributeValue(XMIIDREF2011_ATTR);
		String sEnd2Id = xMembers.get(1).attributeValue(XMIIDREF2011_ATTR);
		UmlAssociationEnd oAssoEnd1 = p_oModelDictionary.getAssociationEndById(sEnd1Id);
		UmlAssociationEnd oAssoEnd2 = p_oModelDictionary.getAssociationEndById(sEnd2Id);
		
		// Define target and source for association ends
		UmlClass oEnd1Target = p_oModelDictionary.getClassById(oAssoEnd1.getRefClassId());
		oAssoEnd1.setRefClass(oEnd1Target);
		oEnd1Target.addAssociation(oAssoEnd2);
	
		UmlClass oEnd2Target = p_oModelDictionary.getClassById(oAssoEnd2.getRefClassId());
		oAssoEnd2.setRefClass(oEnd2Target);
		oEnd2Target.addAssociation(oAssoEnd1);
				
		oAssoEnd1.setOppositeAssociation(oAssoEnd2);
		oAssoEnd2.setOppositeAssociation(oAssoEnd1);
		
		// Swap association end type (to be like VP)
		AggregateType oAggrType = oAssoEnd1.getAggregateType();
		oAssoEnd1.setAggregateType(oAssoEnd2.getAggregateType());
		oAssoEnd2.setAggregateType(oAggrType);
		
		if ( p_xAssociation.attributeValue(XMITYPE_ATTR).equals(PACKAGEELEMENT_TYPE_ASSOCIATION)) {
			UmlAssociation r_oAssociation = new UmlAssociation(sName, oAssoEnd2, oAssoEnd1, sOptions);
	
			// Read Extension - Stereotype
			Element xEltExtension = p_xAssociation.element(XMIEXTENSION_NODE);
			if (xEltExtension != null) {
				for (Element xStereoType : (List<Element>) xEltExtension.elements("appliedStereotype")) { 
					String sIdRef = xStereoType.attributeValue(XMIVALUE2011_ATTR);
					UmlStereotype oUmlStereoType = p_oModelDictionary.getStereotypeById(sIdRef);
					if (oUmlStereoType == null) {
						throw new AdjavaException("Impossible de trouver le stereotype d'id {}", sIdRef);
					}
					r_oAssociation.addStereotype(oUmlStereoType);
				}
			}
			
			p_oModelDictionary.registerAssociation(sXmiId, r_oAssociation);
		}
//		else { Not Use
//			UmlAssociationClass oAssoClass = (UmlAssociationClass) p_oModelDictionary.getClassById(sXmiId);
//			if (oAssoClass.getAssociationEnd1() == null && oAssoClass.getAssociationEnd2() == null) {
//				oAssoClass.setAssociationName(sName);
//				oAssoClass.setAssociationEnd1(oAssoEnd1);
//				oAssoClass.setAssociationEnd2(oAssoEnd2);
//				oAssoClass.setOptions(sOptions);
//				p_oModelDictionary.registerAssociationClass(sXmiId, oAssoClass);
//			} else {
//				MessageHandler.getInstance().addError(
//				"An association class has already been defined on class {}. The first one is between {} and {}. The second one is between {} and {}",
//					new Object[] { oAssoClass.getFullName(),
//						oAssoClass.getAssociationEnd1().getRefClass().getFullName(),
//						oAssoClass.getAssociationEnd2().getRefClass().getFullName(),
//						oAssoEnd1.getRefClass().getFullName(),
//						oAssoEnd2.getRefClass().getFullName() });
//			}
//		}
	}

	/**
	 * Read association end
	 * @param p_xAssociationEnd node of association end
	 * @param p_oModelDictionary model dictionary
	 * @return association end
	 */
	protected UmlAssociationEnd readAssociationEnd(Element p_xAssociationEnd,
			UmlDictionary p_oModelDictionary) {
		
		// XMI example - Screen - Panel :
		// <ownedMember isAbstract="false" isDerived="false" isLeaf="false" name="mainPanel" xmi:id="PBaAFAqAUDAUjAwB" xmi:type="uml:Association">
		// 		<memberEnd xmi:idref="PBaAFAqAUDAUjAwC"/>
		// 		<ownedEnd aggregation="none" association="PBaAFAqAUDAUjAwB" isDerived="false" isDerivedUnion="false" isLeaf="false" isNavigable="true" isReadOnly="false" isStatic="false" type="HeygoAqAUDAUUQ6d" xmi:id="PBaAFAqAUDAUjAwC" xmi:type="uml:Property">
		//			...
		// 		</ownedEnd>
		// 		<memberEnd xmi:idref="PBaAFAqAUDAUjAwE"/>
		// 		<ownedEnd aggregation="shared" association="PBaAFAqAUDAUjAwB" isDerived="false" isDerivedUnion="false" isLeaf="false" isNavigable="true" isReadOnly="false" isStatic="false" type="WlWgoAqAUDAUUQ61" xmi:id="PBaAFAqAUDAUjAwE" xmi:type="uml:Property">
		// 			...
		// 		</ownedEnd>
		// </ownedMember>
		
		// XMI example - Model Navigable 0..N to 0..N :
		// <ownedMember isAbstract="false" isDerived="false" isLeaf="false" name="ChildrenSkill" xmi:id="Z9.VFAqAUDAUjB1I" xmi:type="uml:Association">
		//		<memberEnd xmi:idref="Z9.VFAqAUDAUjB1J"/>
		//		<ownedEnd aggregation="none" association="Z9.VFAqAUDAUjB1I" isDerived="false" isDerivedUnion="false" isLeaf="false" isNavigable="true" isReadOnly="false" isStatic="false" name="childrens_children" type="XpBgoAqAUDAUUQ7K" visibility="private" xmi:id="Z9.VFAqAUDAUjB1J" xmi:type="uml:Property">
		//			<lowerValue value="0" xmi:id="Z9.VFAqAUDAUjB1J_multiplicity_lowerValue" xmi:type="uml:LiteralString"/>
		//			<upperValue value="*" xmi:id="Z9.VFAqAUDAUjB1J_multiplicity_upperValue" xmi:type="uml:LiteralString"/>
		//		</ownedEnd>
		//		<memberEnd xmi:idref="Z9.VFAqAUDAUjB1L"/>
		//		<ownedEnd aggregation="shared" association="Z9.VFAqAUDAUjB1I" isDerived="false" isDerivedUnion="false" isLeaf="false" isNavigable="true" isReadOnly="false" isStatic="false" name="skills_skill" type="uQoVFAqAUDAUjB0Z" visibility="private" xmi:id="Z9.VFAqAUDAUjB1L" xmi:type="uml:Property">
		//			<lowerValue value="0" xmi:id="Z9.VFAqAUDAUjB1L_multiplicity_lowerValue" xmi:type="uml:LiteralString"/>
		//			<upperValue value="*" xmi:id="Z9.VFAqAUDAUjB1L_multiplicity_upperValue" xmi:type="uml:LiteralString"/>
		//		</ownedEnd>
		// </ownedMember>

		// XMI example - Model Navigable 0..N to 1 :
		// <ownedMember isAbstract="false" isDerived="false" isLeaf="false" xmi:id="86m1FAqAUDAUjB34" xmi:type="uml:Association">
		// 		<memberEnd xmi:idref="86m1FAqAUDAUjB35"/>
		// 		<ownedEnd aggregation="none" association="86m1FAqAUDAUjB34" isDerived="false" isDerivedUnion="false" isLeaf="false" isNavigable="true" isReadOnly="false" isStatic="false" type="XpBgoAqAUDAUUQ7K" xmi:id="86m1FAqAUDAUjB35" xmi:type="uml:Property">
		// 			<lowerValue value="0" xmi:id="86m1FAqAUDAUjB35_multiplicity_lowerValue" xmi:type="uml:LiteralString"/>
		// 			<upperValue value="*" xmi:id="86m1FAqAUDAUjB35_multiplicity_upperValue" xmi:type="uml:LiteralString"/>
		// 		</ownedEnd>
		// 		<memberEnd xmi:idref="86m1FAqAUDAUjB37"/>
		// 		<ownedEnd aggregation="shared" association="86m1FAqAUDAUjB34" isDerived="false" isDerivedUnion="false" isLeaf="false" isNavigable="true" isReadOnly="false" isStatic="false" name="day" type="Tro1FAqAUDAUjB3d" visibility="private" xmi:id="86m1FAqAUDAUjB37" xmi:type="uml:Property">
		// 			<lowerValue value="1" xmi:id="86m1FAqAUDAUjB37_multiplicity_lowerValue" xmi:type="uml:LiteralString"/>
		// 		</ownedEnd>
		// </ownedMember>
		
		// XMI example - Model Navigable 0..N to 0..1 :
		// <ownedMember isAbstract="false" isDerived="false" isLeaf="false" xmi:id="cNVNFAqAUDAUjB5j" xmi:type="uml:Association">
		// 		<memberEnd xmi:idref="cNVNFAqAUDAUjB5k"/>
		// 		<ownedEnd aggregation="none" association="cNVNFAqAUDAUjB5j" isDerived="false" isDerivedUnion="false" isLeaf="false" isNavigable="true" isReadOnly="false" isStatic="false" type="XpBgoAqAUDAUUQ7K" xmi:id="cNVNFAqAUDAUjB5k" xmi:type="uml:Property">
		// 			<lowerValue value="0" xmi:id="cNVNFAqAUDAUjB5k_multiplicity_lowerValue" xmi:type="uml:LiteralString"/>
		// 			<upperValue value="*" xmi:id="cNVNFAqAUDAUjB5k_multiplicity_upperValue" xmi:type="uml:LiteralString"/>
		// 		</ownedEnd>
		// 		<memberEnd xmi:idref="cNVNFAqAUDAUjB5m"/>
		// 		<ownedEnd aggregation="shared" association="cNVNFAqAUDAUjB5j" isDerived="false" isDerivedUnion="false" isLeaf="false" isNavigable="true" isReadOnly="false" isStatic="false" name="lastname" type="CgyNFAqAUDAUjB41" visibility="private" xmi:id="cNVNFAqAUDAUjB5m" xmi:type="uml:Property">
		// 			<lowerValue value="0" xmi:id="cNVNFAqAUDAUjB5m_multiplicity_lowerValue" xmi:type="uml:LiteralString"/>
		// 			<upperValue value="1" xmi:id="cNVNFAqAUDAUjB5m_multiplicity_upperValue" xmi:type="uml:LiteralString"/>
		// 		</ownedEnd>
		// </ownedMember>		
		
		UmlAssociationEnd r_oMAssociationEnd = null;
		String sXmiId = p_xAssociationEnd.attributeValue(XMIID2011_ATTR);
		String sName = p_xAssociationEnd.attributeValue(NAME_ATTR);

		// Visibility
		String sVisibility = this.readVisibility(p_xAssociationEnd);
		
		// Navigable
		boolean bNavigable = Boolean.valueOf(p_xAssociationEnd.attributeValue("isNavigable")).booleanValue();
		
		// Compute name and options
		String sOptions = StringUtils.EMPTY;
		if (sName != null) {
			String[] t_sParts = StringUtils.split(sName, '$');
			sName = t_sParts[0] ;
			if ( t_sParts.length > 1 ) {
				sOptions = t_sParts[1];
			}
		}
		else {
			sName = StringUtils.EMPTY ;
		}

		// Ordered (not implemented)
		boolean bOrdered = false;

		// Multiplicity
		UmlMultiplicity oMultiplicity = this.readMultiplicity( p_xAssociationEnd );
		
		// Read aggregation kind
		String sAggregate = this.readAggregation(p_xAssociationEnd);
			
		// Target Class Id
		String sTargetClassId = this.readTargetClassId(p_xAssociationEnd);
		
		// Part of Identifier - Use uml:AssociationClass
		boolean bPartOfIdentifier = this.readIsId(p_xAssociationEnd);
		
		log.debug("association end: {}, id: {}, multiplicity: {}..{}, aggregate: {}, visibility: {}, navigable: {}, options: {}, id: {}", 
			new Object[] {sName, sXmiId, oMultiplicity.getLower(), oMultiplicity.getUpper(), sAggregate, sVisibility, bNavigable, sOptions, bPartOfIdentifier});
		
		r_oMAssociationEnd = new UmlAssociationEnd(sName, sVisibility, bOrdered, bNavigable, oMultiplicity.getLower(),
			oMultiplicity.getUpper(), sOptions, AggregateType.getByString(sAggregate));
		r_oMAssociationEnd.setRefClassId(sTargetClassId);
		r_oMAssociationEnd.setId(bPartOfIdentifier);
		
		p_oModelDictionary.registerAssociationEnd(sXmiId, r_oMAssociationEnd);
		
		return r_oMAssociationEnd;
	}

	/**
	 * Read id of target class
	 * @param p_xAssociationEnd association end node
	 * @return id of target class
	 */
	private String readTargetClassId(Element p_xAssociationEnd) {
		return p_xAssociationEnd.attributeValue(TYPE_ATTR);
	}

	/**
	 * Read kind of aggregation (aggregation or composition)
	 * @param p_xAssociationEnd association end node
	 * @return kind of aggregation
	 */
	private String readAggregation(Element p_xAssociationEnd) {
		return p_xAssociationEnd.attributeValue(OWNEDATTRIBUTE_AGGREGATION_ATTR);
	}

	/**
	 * Read visibility
	 * @param p_xAssociationEnd association end node
	 * @return visibility
	 */
	private String readVisibility(Element p_xAssociationEnd) {
		return p_xAssociationEnd.attributeValue(OWNEDATTRIBUTE_VISIBILITY_ATTR);
	}
	
	/**
	 * Read multiplicity
	 * @param p_xAssociationEnd association end
	 * @return multiplicity
	 */
	private UmlMultiplicity readMultiplicity(Element p_xAssociationEnd) {
		UmlMultiplicity r_oMultiplicity = new UmlMultiplicity();

		// XMI example - Model Navigable 0..N :
		// <lowerValue value="0" xmi:id="Z9.VFAqAUDAUjB1J_multiplicity_lowerValue" xmi:type="uml:LiteralString"/>
		// <upperValue value="*" xmi:id="Z9.VFAqAUDAUjB1J_multiplicity_upperValue" xmi:type="uml:LiteralString"/>

		// XMI example - Model Navigable 1 :
		// <lowerValue value="1" xmi:id="86m1FAqAUDAUjB37_multiplicity_lowerValue" xmi:type="uml:LiteralString"/>
		
		// XMI example - Model Navigable 0..1 :
		// <lowerValue value="0" xmi:id="cNVNFAqAUDAUjB5m_multiplicity_lowerValue" xmi:type="uml:LiteralString"/>
		// <upperValue value="1" xmi:id="cNVNFAqAUDAUjB5m_multiplicity_upperValue" xmi:type="uml:LiteralString"/>

		String sLower = this.findBoundValueNode(p_xAssociationEnd, LOWERVALUE_NODE);
		String sUpper = this.findBoundValueNode(p_xAssociationEnd, UPPERVALUE_NODE);
		
		if ( !sLower.isEmpty()) {
			r_oMultiplicity.setLower(Integer.parseInt(sLower));
			
			if ( !sUpper.isEmpty()) {
				if ("*".equals(sUpper)) {
					sUpper = "-1";
				}
				r_oMultiplicity.setUpper(Integer.parseInt(sUpper));
			}
			else {
				if (Integer.parseInt(sLower) == 1) {
					sUpper = "1";
					r_oMultiplicity.setUpper(Integer.parseInt(sUpper));
				} else {
					r_oMultiplicity.setLower(null);
					r_oMultiplicity.setUpper(null);
				}
			}
		}
		
		return r_oMultiplicity ;
	}
	
	/**
	 * Return bound value 
	 * @param p_xParent multiplicity node
	 * @param p_sName node to search (either lower or upper)
	 * @return bound value, empty if not found
	 */
	@SuppressWarnings("unchecked")
	private String findBoundValueNode( Element p_xParent, QName p_oNodeName ) {
		String r_sBoundValue = StringUtils.EMPTY;
		Element xBound = p_xParent.element(p_oNodeName);
//		if ( xBound == null ) {
//			List<Element> xExtensions = p_xParent.elements(XMIEXTENSION_NODE);
//			for( Element xExtension : xExtensions ) {
//				if ( xExtension != null ) {
//					Element xModelExtension = xExtension.element(MODELEXTENSION_NODE);
//					if ( xModelExtension != null ) {
//						xBound = xModelExtension.element(p_oNodeName);
//					}
//				}
//				if ( xBound != null ) {
//					break ;
//				}
//			}
//		}

		if ( xBound != null ) {
			String sValue = xBound.attributeValue(VALUE_ATTR);
			if ( sValue != null ) {
				r_sBoundValue = sValue ;
			}
//			else {
//				// node(lower/upper) exists but no value, means 0
//				r_sBoundValue = "0";
//			}
		}
		
		return r_sBoundValue ;
	}
	
	/**
	 * Return true if association end is part of identifier
	 * @param p_xAssociationEnd node of association end
	 * @return true if association end is part of identifier
	 */
	private boolean readIsId(Element p_xAssociationEnd) {
		return "true".equals(p_xAssociationEnd.attributeValue(OWNEDATTRIBUTE_ISID_ATTR));
	}
}
