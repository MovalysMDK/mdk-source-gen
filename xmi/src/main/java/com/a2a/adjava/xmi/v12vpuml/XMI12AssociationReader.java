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
package com.a2a.adjava.xmi.v12vpuml;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dom4j.Element;

import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.uml.UmlAssociation;
import com.a2a.adjava.uml.UmlAssociationClass;
import com.a2a.adjava.uml.UmlAssociationEnd;
import com.a2a.adjava.uml.UmlStereotype;
import com.a2a.adjava.uml.UmlAssociationEnd.AggregateType;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;

/**
 * <p>
 * XMI12AssociationReader
 * </p>
 * 
 * <p>
 * Copyright (c) 2011
 * <p>
 * Company: Adeuza
 * 
 * @author lmichenaud
 * 
 */
public final class XMI12AssociationReader {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(XMI12AssociationReader.class);

	/**
	 * Association reader
	 */
	private static XMI12AssociationReader associationReader = null;

	/**
	 * @return
	 */
	protected static XMI12AssociationReader getInstance() {
		if (associationReader == null) {
			associationReader = new XMI12AssociationReader();
		}
		return associationReader;
	}

	/**
	 * Constructor
	 */
	private XMI12AssociationReader() {

	}

	/**
	 * @param p_xAssociation
	 * @param p_oModelDictonnary
	 * @param p_oConfig
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected void readAssociation(Element p_xAssociation, UmlDictionary p_oModelDictonnary) {
		log.debug("> XMI12Reader.readAssociation");

		String sXmiId = p_xAssociation.attributeValue("xmi.id");

		String sName = p_xAssociation.attributeValue("name");

		log.debug("  xmi id: {}", sXmiId);
		log.debug("  name: {}", sName);

		String sOptions = "";
		if (sName != null) {
			int iPos = sName.lastIndexOf('$');
			if (iPos != -1) {
				sOptions = sName.substring(iPos + 1);
				sName = sName.substring(0, iPos);
			}
		}
		
		Element xConnection = p_xAssociation.element("Association.connection");

		List<Element> listAssociations = xConnection.elements("AssociationEnd");

		String sClassIdref1 = listAssociations.get(0).attributeValue("participant");
		UmlClass oParticipant1 = p_oModelDictonnary.getClassById(sClassIdref1);
		String sClassIdref2 = listAssociations.get(1).attributeValue("participant");
		UmlClass oParticipant2 = p_oModelDictonnary.getClassById(sClassIdref2);

		boolean bError = false;
		if (oParticipant1 == null) {
			MessageHandler.getInstance().addError(
					"Can't find Class with xmi.id '{}'. Check that class is located inside 'com' package.", sClassIdref1);
			bError = true;
		}

		if (oParticipant2 == null) {
			MessageHandler.getInstance().addError(
					"Can't find Class with xmi.id '{}'. Check that class is located inside 'com' package.", sClassIdref2 );
			bError = true;
		}

		if (!bError) {

			UmlAssociationEnd oAssociationEnd1 = readAssociationEnd(listAssociations.get(0),
					p_oModelDictonnary, oParticipant1);
			oParticipant2.addAssociation(oAssociationEnd1);
			UmlAssociationEnd oAssociationEnd2 = readAssociationEnd(listAssociations.get(1),
					p_oModelDictonnary, oParticipant2);
			oParticipant1.addAssociation(oAssociationEnd2);

			String sClassAssocId = XMI12Helper.getClassIdOnAssociationClassByAssociationId(p_xAssociation,
					sXmiId);
			log.debug("  association class id: {}", sClassAssocId);
			if (sClassAssocId == null) {
				UmlAssociation r_oAssociation = new UmlAssociation(sName, oAssociationEnd1, oAssociationEnd2, sOptions);

				// lecture des steretotype
				Element xStereotypes = p_xAssociation.element("ModelElement.stereotype");
				if (xStereotypes != null) {
					String sId = null;
					UmlStereotype oStereotype = null;
					for (Object xStereotype : xStereotypes.elements("Stereotype")) {
						sId = ((Element) xStereotype).attributeValue("xmi.idref");
						oStereotype = p_oModelDictonnary.getStereotypeById(sId);
						r_oAssociation.addStereotype(oStereotype);
					}
				}

				p_oModelDictonnary.registerAssociation(sXmiId, r_oAssociation);
			} else {
				log.debug("xmi id de la classe d'assoc : {}", sClassAssocId);
				UmlAssociationClass oUmlAssociationClass = (UmlAssociationClass) p_oModelDictonnary
						.getClassById(sClassAssocId);
				if (oUmlAssociationClass.getAssociationEnd1() == null
						&& oUmlAssociationClass.getAssociationEnd2() == null) {
					oUmlAssociationClass.setAssociationName(sName);
					oUmlAssociationClass.setOptions(sOptions);
					oUmlAssociationClass.setAssociationEnd1(oAssociationEnd1);
					oUmlAssociationClass.setAssociationEnd2(oAssociationEnd2);
					oAssociationEnd1.setOppositeAssociation(oAssociationEnd2);
					oAssociationEnd2.setOppositeAssociation(oAssociationEnd1);
					p_oModelDictonnary.registerAssociationClass(sXmiId, oUmlAssociationClass);
				} else {
					MessageHandler.getInstance().addError(
							"An association class has already been defined on class "
									+ oUmlAssociationClass.getFullName() + ". The first one is between "
									+ oUmlAssociationClass.getAssociationEnd1().getRefClass().getFullName()
									+ " and "
									+ oUmlAssociationClass.getAssociationEnd2().getRefClass().getFullName()
									+ ". The second one is between "
									+ oAssociationEnd1.getRefClass().getFullName() + " and "
									+ oAssociationEnd2.getRefClass().getFullName());
				}
			}
		}

		log.debug("< XMI12Reader.readAssociation");
	}

	/**
	 * @param p_xAssociationEnd
	 * @return
	 */
	protected UmlAssociationEnd readAssociationEnd(Element p_xAssociationEnd,
			UmlDictionary p_oModelDictonnary, UmlClass p_oTargetClass) {

		log.debug("> XMI12Reader.readAssociationEnd");

		UmlAssociationEnd r_oMAssociationEnd = null;
		String sXmiId = p_xAssociationEnd.attributeValue("xmi.id");
		log.debug("  xmi.id: {}", sXmiId);
		String sName = p_xAssociationEnd.attributeValue("name");
		log.debug("  name: {}", sName);
		String sVisibility = p_xAssociationEnd.attributeValue("visibility");
		boolean bNavigable = Boolean.valueOf(p_xAssociationEnd.attributeValue("isNavigable")).booleanValue();
		log.debug("  navigable: {}", bNavigable);
			
		String sOptions = "";
		if (sName != null) {
			int iPos = sName.lastIndexOf('$');
			if (iPos != -1) {
				sOptions = sName.substring(iPos + 1);
				sName = sName.substring(0, iPos);
			}
		}

		boolean bOrdered = false;
		String sOrderingAttr = p_xAssociationEnd.attributeValue("ordering");
		if (sOrderingAttr != null) {
			bOrdered = !sOrderingAttr.equals("unordered");
		}
		Element xMultiplicity = p_xAssociationEnd.element("AssociationEnd.multiplicity").element(
			"Multiplicity");
		Element xMultiplicityRange = xMultiplicity.element("Multiplicity.range").element(
			"MultiplicityRange");
		String sLowerMultiplicity = xMultiplicityRange.attributeValue("lower");

		String sAggregate = p_xAssociationEnd.attributeValue("aggregation");
		Integer iLower = null;
		Integer iUpper = null;

		if ( sLowerMultiplicity != null && !sLowerMultiplicity.equals("Unspecified")) {
			if ( sLowerMultiplicity.equals("*")) {
				iLower = 0;
				iUpper = -1;
			} else {
				iLower = Integer.parseInt(sLowerMultiplicity);
				iUpper = iLower;

				String sUpper = xMultiplicityRange.attributeValue("upper");
				if (sUpper != null) {
					if (sUpper.equals("*")) {
						sUpper = "-1";
					}
					iUpper = Integer.parseInt(sUpper);
				}
			}
		}
		
		log.debug("  multiplicity: {}..{}", iLower, iUpper);
		r_oMAssociationEnd = new UmlAssociationEnd(sName, sVisibility, bOrdered, bNavigable, iLower,
			iUpper, sOptions, AggregateType.getByString(sAggregate));
		r_oMAssociationEnd.setRefClass(p_oTargetClass);
	
		log.debug("< XMI12Reader.readAssociationEnd");

		return r_oMAssociationEnd;
	}
}
