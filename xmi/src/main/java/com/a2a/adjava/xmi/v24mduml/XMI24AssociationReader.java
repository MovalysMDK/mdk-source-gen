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
package com.a2a.adjava.xmi.v24mduml;

import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.XMIEXTENSION_NODE;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.LOWERVALUE_NODE;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.MEMBEREND_NODE;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.MODELEXTENSION_NODE;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.NAME_ATTR;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.OWNEDATTRIBUTE_AGGREGATION_ATTR;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.OWNEDATTRIBUTE_ISID_ATTR;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.OWNEDATTRIBUTE_NODE;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.OWNEDATTRIBUTE_VISIBILITY_ATTR;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.OWNEDEND_NODE;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.PACKAGEELEMENT_TYPE_ASSOCIATION;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.TYPE_ATTR;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.UPPERVALUE_NODE;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.VALUE_ATTR;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.XMIID2011_ATTR;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.XMIIDREF2011_ATTR;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.XMITYPE_ATTR;
import static com.a2a.adjava.xmi.v21mduml.XMI21Constants.LITERAL_UNLIMITEDNATURAL_VALUE;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.dom4j.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.uml.UmlAssociation;
import com.a2a.adjava.uml.UmlAssociationClass;
import com.a2a.adjava.uml.UmlAssociationEnd;
import com.a2a.adjava.uml.UmlAssociationEnd.AggregateType;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlMultiplicity;

/**
 * Association reader
 * @author lmichenaud
 *
 */
public final class XMI24AssociationReader {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(XMI24AssociationReader.class);

	/**
	 * Association reader
	 */
	private static XMI24AssociationReader associationReader = new XMI24AssociationReader();

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	protected static XMI24AssociationReader getInstance() {
		return associationReader;
	}

	/**
	 * Constructor
	 */
	private XMI24AssociationReader() {
		// private because singleton pattern
	}

	/**
	 * Read association
	 * @param p_xAssociation association node
	 * @param p_oModelDictionary model dictionary
	 */
	@SuppressWarnings("unchecked")
	protected void readAssociation(Element p_xAssociation, UmlDictionary p_oModelDictionary) {
	
//## Association navigable two way
//	<packagedElement xmi:type='uml:Association' xmi:id='_17_0_1_2_16a6041b_1355753290033_630078_1914' name='SimulationNote'>
//		<memberEnd xmi:idref='_17_0_1_2_16a6041b_1355753290034_213736_1915'/>
//		<memberEnd xmi:idref='_17_0_1_2_16a6041b_1355753290034_658700_1916'/>
//	</packagedElement>

//## Association navigable one way
//	<packagedElement xmi:type='uml:Association' xmi:id='_17_0_1_2_16a6041b_1355753290033_630078_1914' name='SimulationNote'>
//		<memberEnd xmi:idref='_17_0_1_2_16a6041b_1355753290034_213736_1915'/>
//		<memberEnd xmi:idref='_17_0_1_2_16a6041b_1355753290034_658700_1916'/>
//		<ownedEnd xmi:type='uml:Property' xmi:id='_17_0_1_2_16a6041b_1355753290034_213736_1915' name='responsables' 
//		visibility='private' aggregation='shared' isID='true' type='_17_0_1_2_16a6041b_1355752086323_676713_1819' 
//		association='_17_0_1_2_16a6041b_1355753290033_630078_1914'>
//			<xmi:Extension extender='MagicDraw UML 17.0.1'>
//				<modelExtension>
//					<lowerValue xmi:type='uml:LiteralInteger' xmi:id='_17_0_1_2_16a6041b_1355932707637_780567_1983' value='1'/>
//				</modelExtension>
//			</xmi:Extension>
//			<xmi:Extension extender='MagicDraw UML 17.0.1'>
//				<modelExtension>
//					<upperValue xmi:type='uml:LiteralUnlimitedNatural' xmi:id='_17_0_1_2_16a6041b_1355753317154_261136_1933' value='1'/>
//				</modelExtension>
//			</xmi:Extension>
//		</ownedEnd>
//	</packagedElement>
		
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
		UmlClass oEnd2Target = p_oModelDictionary.getClassById(oAssoEnd2.getRefClassId());
		oAssoEnd2.setRefClass(oEnd2Target);
				
		oAssoEnd1.setOppositeAssociation(oAssoEnd2);
		oAssoEnd2.setOppositeAssociation(oAssoEnd1);
		
		// Swap association end type (to be like VP)
		AggregateType oAggrType = oAssoEnd1.getAggregateType();
		oAssoEnd1.setAggregateType(oAssoEnd2.getAggregateType());
		oAssoEnd2.setAggregateType(oAggrType);
		
		if ( p_xAssociation.attributeValue(XMITYPE_ATTR).equals(PACKAGEELEMENT_TYPE_ASSOCIATION)) {
			UmlAssociation r_oAssociation = new UmlAssociation(sName, oAssoEnd1, oAssoEnd2, sOptions);
			p_oModelDictionary.registerAssociation(sXmiId, r_oAssociation);
		}
		else {
			UmlAssociationClass oAssoClass = (UmlAssociationClass) p_oModelDictionary.getClassById(sXmiId);
			if (oAssoClass.getAssociationEnd1() == null && oAssoClass.getAssociationEnd2() == null) {
				oAssoClass.setAssociationName(sName);
				oAssoClass.setAssociationEnd1(oAssoEnd1);
				oAssoClass.setAssociationEnd2(oAssoEnd2);
				oAssoClass.setOptions(sOptions);
				p_oModelDictionary.registerAssociationClass(sXmiId, oAssoClass);
			} else {
				MessageHandler.getInstance().addError(
				"An association class has already been defined on class {}. The first one is between {} and {}. The second one is between {} and {}",
					new Object[] { oAssoClass.getFullName(),
						oAssoClass.getAssociationEnd1().getRefClass().getFullName(),
						oAssoClass.getAssociationEnd2().getRefClass().getFullName(),
						oAssoEnd1.getRefClass().getFullName(),
						oAssoEnd2.getRefClass().getFullName() });
			}
		}
	}

	/**
	 * Read association end
	 * @param p_xAssociationEnd node of association end
	 * @param p_oModelDictionary model dictionary
	 * @return association end
	 */
	protected UmlAssociationEnd readAssociationEnd(Element p_xAssociationEnd,
			UmlDictionary p_oModelDictionary) {

//***** 0..n Navigable example:
//  inside class:
//	<ownedAttribute xmi:type='uml:Property' xmi:id='_17_0_1_2_16a6041b_1355753290034_213736_1915' name='responsables' visibility='private'
//		aggregation='composite' isID='true' type='_17_0_1_2_16a6041b_1355752086323_676713_1819' 
//		association='_17_0_1_2_16a6041b_1355753290033_630078_1914'>
//		<lowerValue xmi:type='uml:LiteralInteger' xmi:id='_17_0_1_2_16a6041b_1355753317154_149283_1932'/>
//		<upperValue xmi:type='uml:LiteralUnlimitedNatural' xmi:id='_17_0_1_2_16a6041b_1355753317154_261136_1933' value='*'/>
//	</ownedAttribute>
	
//	inside package		
//	<packagedElement xmi:type='uml:Association' xmi:id='_17_0_1_2_16a6041b_1355753290033_630078_1914'>
//		<memberEnd xmi:idref='_17_0_1_2_16a6041b_1355753290034_213736_1915'/>
//		<memberEnd xmi:idref='_17_0_1_2_16a6041b_1355753290034_658700_1916'/>
//	</packagedElement>

//***** 1..n Navigable example:
//  inside class:
//		<ownedAttribute xmi:type='uml:Property' xmi:id='_17_0_1_2_16a6041b_1355753290034_213736_1915' name='responsables' visibility='private' aggregation='composite' isID='true' type='_17_0_1_2_16a6041b_1355752086323_676713_1819' association='_17_0_1_2_16a6041b_1355753290033_630078_1914'>
//			<xmi:Extension extender='MagicDraw UML 17.0.1'>
//				<modelExtension>
//					<lowerValue xmi:type='uml:LiteralInteger' xmi:id='_17_0_1_2_16a6041b_1355753317154_149283_1932' value='1'/>
//				</modelExtension>
//			</xmi:Extension>
//			<upperValue xmi:type='uml:LiteralUnlimitedNatural' xmi:id='_17_0_1_2_16a6041b_1355753317154_261136_1933' value='*'/>
//		</ownedAttribute>

//		inside package		
//		<packagedElement xmi:type='uml:Association' xmi:id='_17_0_1_2_16a6041b_1355753290033_630078_1914'>
//			<memberEnd xmi:idref='_17_0_1_2_16a6041b_1355753290034_213736_1915'/>
//			<memberEnd xmi:idref='_17_0_1_2_16a6041b_1355753290034_658700_1916'/>
//		</packagedElement>
			
		UmlAssociationEnd r_oMAssociationEnd = null;
		String sXmiId = p_xAssociationEnd.attributeValue(XMIID2011_ATTR);
		String sName = p_xAssociationEnd.attributeValue(NAME_ATTR);

		// Visibility
		String sVisibility = this.readVisibility(p_xAssociationEnd);
		
		// Navigable (true if node is ownedAttribute)
		boolean bNavigable = p_xAssociationEnd.getQName().equals(OWNEDATTRIBUTE_NODE);
		
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
		
		// Part of Identifier
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

		// Multiplicity examples
		
//## 0..*
//				<lowerValue xmi:type='uml:LiteralInteger' xmi:id='_17_0_1_2_16a6041b_1355753317154_149283_1932'/>
//				<upperValue xmi:type='uml:LiteralUnlimitedNatural' xmi:id='_17_0_1_2_16a6041b_1355753317154_261136_1933' value='*'/>
//## 1..*
//				<xmi:Extension extender='MagicDraw UML 17.0.1'>
//					<modelExtension>
//						<lowerValue xmi:type='uml:LiteralInteger' xmi:id='_17_0_1_2_16a6041b_1355753317154_149283_1932' value='1'/>
//					</modelExtension>
//				</xmi:Extension>
//				<upperValue xmi:type='uml:LiteralUnlimitedNatural' xmi:id='_17_0_1_2_16a6041b_1355753317154_261136_1933' value='*'/>
//## *
//				<lowerValue xmi:type='uml:LiteralUnlimitedNatural' xmi:id='_17_0_1_2_16a6041b_1355932553348_473495_1982'/>
//				<upperValue xmi:type='uml:LiteralUnlimitedNatural' xmi:id='_17_0_1_2_16a6041b_1355753317154_261136_1933' value='*'/>	
//## 0..1
//				<lowerValue xmi:type='uml:LiteralInteger' xmi:id='_17_0_1_2_16a6041b_1355932707637_780567_1983'/>
//				<xmi:Extension extender='MagicDraw UML 17.0.1'>
//					<modelExtension>
//						<upperValue xmi:type='uml:LiteralUnlimitedNatural' xmi:id='_17_0_1_2_16a6041b_1355753317154_261136_1933' value='1'/>
//					</modelExtension>
//				</xmi:Extension>
//## 1
//				<xmi:Extension extender='MagicDraw UML 17.0.1'>
//					<modelExtension>
//						<lowerValue xmi:type='uml:LiteralInteger' xmi:id='_17_0_1_2_16a6041b_1355932707637_780567_1983' value='1'/>
//					</modelExtension>
//				</xmi:Extension>
//				<xmi:Extension extender='MagicDraw UML 17.0.1'>
//					<modelExtension>
//						<upperValue xmi:type='uml:LiteralUnlimitedNatural' xmi:id='_17_0_1_2_16a6041b_1355753317154_261136_1933' value='1'/>
//					</modelExtension>
//				</xmi:Extension>		
		
	
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
				r_oMultiplicity.setLower(null);
				r_oMultiplicity.setUpper(null);
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
		if ( xBound == null ) {
			List<Element> xExtensions = p_xParent.elements(XMIEXTENSION_NODE);
			for( Element xExtension : xExtensions ) {
				if ( xExtension != null ) {
					Element xModelExtension = xExtension.element(MODELEXTENSION_NODE);
					if ( xModelExtension != null ) {
						xBound = xModelExtension.element(p_oNodeName);
					}
				}
				if ( xBound != null ) {
					break ;
				}
			}
		}

		if ( xBound != null ) {
			String sValue = xBound.attributeValue(VALUE_ATTR);
			if ( sValue != null ) {
				r_sBoundValue = sValue ;
			}
			else {
				// node(lower/upper) exists but no value, means 0
				r_sBoundValue = "0";
			}
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
