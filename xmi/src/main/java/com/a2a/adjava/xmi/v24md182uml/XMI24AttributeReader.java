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
package com.a2a.adjava.xmi.v24md182uml;

import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.DEFAULTVALUE_NODE;
import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.NAME_ATTR;
import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.OWNEDATTRIBUTE_DERIVED_ATTR;
import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.OWNEDATTRIBUTE_ISID_ATTR;
import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.OWNEDATTRIBUTE_VISIBILITY_ATTR;
import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.TYPE_ATTR;
import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.TYPE_NODE;
import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.VALUE_ATTR;
import static com.a2a.adjava.xmi.v24md182uml.XMI24Constants.XMIID2011_ATTR;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.uml.UmlAttribute;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDataType;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.utils.StrUtils;

/**
 * Attribute reader
 * @author lmichenaud
 * 
 */
public class XMI24AttributeReader {

	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(XMI24AttributeReader.class);

	/**
	 * Singleton attribute reader
	 */
	private static XMI24AttributeReader attributeReader = new XMI24AttributeReader();

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	protected static XMI24AttributeReader getInstance() {
		return attributeReader;
	}

	/**
	 * Read attribute
	 * @param p_xAttr attribute node
	 * @param p_oClass Uml class of attribute
	 * @param p_oModelDictonnary model dictionary
	 * @return Uml attribute
	 * @throws Exception exception
	 */
	protected UmlAttribute readAttribute(Element p_xAttr, UmlClass p_oClass, UmlDictionary p_oModelDictonnary)
			throws Exception {

		UmlAttribute r_oAttr = null;
		
		// Example:
//		<ownedAttribute xmi:type='uml:Property' xmi:id='_17_0_1_2_16a6041b_1355752412585_489244_1865' 
//			    name='id' visibility='private' isID='true'>
//				<type href='UML_Standard_Profile.mdzip#eee_1045467100323_784316_63'>
//					<xmi:Extension extender='MagicDraw UML 17.0.1'>
//						<referenceExtension referentPath='UML Standard Profile::MagicDraw Profile::datatypes::long' referentType='DataType'/>
//					</xmi:Extension>
//				</type>
//				<defaultValue xmi:type='uml:LiteralString' xmi:id='_17_0_1_2_16a6041b_1355905057106_452654_1997' value='AREMPLIR_L30'/>
//			</ownedAttribute>

		String sId = p_xAttr.attributeValue(XMIID2011_ATTR).trim();
		String sAttrName = p_xAttr.attributeValue(NAME_ATTR);
		log.debug("attribute: {}", sAttrName);

		if ( StringUtils.isNotBlank(sAttrName)) {
					
			sAttrName = sAttrName.trim();
			
			// Read visibility
			String sVisibility = this.readVisibility(p_xAttr);

			// Read type of attribute
			UmlDataType oUmlDataType = this.readType(p_xAttr, p_oModelDictonnary);

			// Read initial value
			String sInitialValue = this.readInitialValue(p_xAttr);

			// Read documentation
			String sDocumentation = XMI24DocReader.getInstance().readDoc(p_xAttr, StringUtils.join("Attribute:", p_oClass.getName(), StrUtils.DOT_S, sAttrName ));
			
			r_oAttr = new UmlAttribute(sAttrName, p_oClass, sVisibility, oUmlDataType, sInitialValue, sDocumentation);
			
			r_oAttr.setDerived(this.readIsDerived(p_xAttr));
			r_oAttr.setIdentifier(this.readIsId(p_xAttr));

			if (log.isDebugEnabled()) {
				log.debug("  visibility: {}", sVisibility);
				log.debug("  initial value: {}", sInitialValue);
				log.debug("  datatype: {}", oUmlDataType);
				log.debug("  isID: {}", r_oAttr.isIdentifier());
			}
			
			p_oModelDictonnary.registerStereotypeObject(sId, r_oAttr);
			
		} else {
			MessageHandler.getInstance()
					.addError("Attribute is empty in class : {}", p_oClass.getName());
		}

		return r_oAttr;
	}

	/**
	 * Read visibility
	 * @param p_xAttr attribute node
	 * @return visibility
	 */
	private String readVisibility(Element p_xAttr) {
		return p_xAttr.attributeValue(OWNEDATTRIBUTE_VISIBILITY_ATTR);
	}

	/**
	 * Read initial value
	 * @param p_xAttr attribute node
	 * @return initial value, empty String if none
	 */
	private String readInitialValue(Element p_xAttr) {
		String r_sInitialValue = StringUtils.EMPTY;
		Element xInitialValue = p_xAttr.element(DEFAULTVALUE_NODE);
		if (xInitialValue != null) {
			r_sInitialValue = xInitialValue.attributeValue(VALUE_ATTR, StringUtils.EMPTY);
		}
		return r_sInitialValue;
	}
	
	/**
	 * Return true if attribute part of identifier
	 * @param p_xAttr ownedAttribute node
	 * @return true if attribute part of identifier
	 */
	private boolean readIsId(Element p_xAttr) {
		return "true".equals(p_xAttr.attributeValue(OWNEDATTRIBUTE_ISID_ATTR));
	}

	
	/**
	 * Read derived attribute
	 * @param p_xAttr ownedAttribute node
	 * @return true if derived
	 */
	private boolean readIsDerived(Element p_xAttr) {
		return "true".equals(p_xAttr.attributeValue(OWNEDATTRIBUTE_DERIVED_ATTR));
	}
	
	/**
	 * Read type of attribute.
	 * 
	 * If standard type of MagicDraw:
	 * <type href='UML_Standard_Profile.mdzip#eee_1045467100323_784316_63'>
	 *		<xmi:Extension extender='MagicDraw UML 17.0.1'>
	 * 			<referenceExtension referentPath='UML Standard Profile::MagicDraw Profile::datatypes::long' referentType='DataType'/>
	 * 		</xmi:Extension>
	 * </type>
	 * 
	 * If custom type (ex: note.matiere : EditText or Enumeration) =>
	 * <ownedAttribute xmi:type='uml:Property' xmi:id='_17_0_1_2_16a6041b_1355753769252_296855_1993' name='note.matiere'
			    visibility='private' type='_17_0_1_2_16a6041b_1355817705296_200102_1816'/>
			    
	 * @param p_xOwnedAttribute attribute node
	 * @param p_oModelDictonnary model dictionary
	 * @return UmlDataType
	 */
	private UmlDataType readType( Element p_xOwnedAttribute, UmlDictionary p_oModelDictonnary ) {
		UmlDataType r_oUmlDataType = null ;
		
		Element xType = p_xOwnedAttribute.element(TYPE_NODE);
		if ( xType != null ) {
			String sHref = xType.attributeValue("href");
			if ( sHref.startsWith("http://www.omg.org/spec/UML/20131001/PrimitiveTypes.xmi")) {
				String sTypeName = StringUtils.substringAfterLast(sHref, "#");
				r_oUmlDataType = p_oModelDictonnary.getDataTypeByName(sTypeName);
			}
			else {
				String sTypeId = StringUtils.substringAfterLast(sHref, "#");
				r_oUmlDataType = p_oModelDictonnary.getDataType(sTypeId);
			}
		}
		else {
			// Two 'type' attributes are present: type and xmi:type.
			// So, use qname to read the correct one.
			String sTypeAttr = p_xOwnedAttribute.attributeValue(TYPE_ATTR);
			if ( sTypeAttr != null ) {
				r_oUmlDataType = p_oModelDictonnary.getDataType(sTypeAttr);
			}
		}
		
		return r_oUmlDataType ;
	}
}
