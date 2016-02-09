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
package com.a2a.adjava.uml2xmodele.extractors.viewmodel.associations;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.uml.UmlAssociationEnd;
import com.a2a.adjava.uml.UmlAssociationEnd.AggregateType;
import com.a2a.adjava.uml.UmlAttribute;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml2xmodele.extractors.viewmodel.VMPathContext;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MCascade;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MViewModelImpl;

/**
 * Path processor for "Many To One - Composite" association.
 * @author lmichenaud
 *
 */
public class VMAssoPathManyToOne implements VMAssoPathProcessor {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(VMAssoPathManyToOne.class);

	/**
	 * (non-Javadoc)
	 * @see com.a2a.adjava.uml2xmodele.extractors.viewmodel.associations.VMAssoPathProcessor#isValid(com.a2a.adjava.uml.UmlClass,
	 *      com.a2a.adjava.uml.UmlAssociationEnd,
	 *      com.a2a.adjava.uml.UmlAssociationEnd, java.lang.String,
	 *      java.lang.String[])
	 */
	@Override
	public boolean isValid(UmlClass p_oTargetEntity, UmlAssociationEnd p_oAsso1, UmlAssociationEnd p_oAsso2,
			String p_sPathElement, String[] p_sAssoName, UmlDictionary p_oDictionnary) {
		
		boolean r_bR = false;
		if ( log.isDebugEnabled()) {
			log.debug("-------------- VMAssoPathManyToOneComposite");
			log.debug("isValidCaseNto1Composite p_oAsso1.getRefClass() == p_oTarget                        " + (p_oAsso1.getRefClass() == p_oTargetEntity));
			log.debug("isValidCaseNto1Composite p_sName.equals(p_sNames[0])                                " + (p_sPathElement.equals(p_sAssoName[0])));
			log.debug("isValidCaseNto1Composite p_oAsso1.getMultiplicityUpper() == -1)                     " + (p_oAsso1.getMultiplicityUpper() == -1));
			log.debug("isValidCaseNto1Composite p_oAsso2.getMultiplicityUpper() == 1)                      " + (p_oAsso2.getMultiplicityUpper() == 1));
			log.debug("isValidCaseNto1Composite p_oAsso2.isNavigable())                                    " + (p_oAsso2.isNavigable()));
			log.debug("isValidCaseNto1Composite p_oAsso1.getAggregation().equals(AggregateType.COMPOSITE)) " + (p_oAsso1.getAggregateType().equals(AggregateType.NONE)));
			log.debug("--------------");
		}
		if (p_oAsso1.getRefClass() == p_oTargetEntity && p_sPathElement.equals(p_sAssoName[0]) && 
				p_oAsso1.getMultiplicityUpper() == -1 && p_oAsso2.getMultiplicityUpper() == 1 &&
				p_oAsso2.isNavigable() && p_oAsso1.getAggregateType().equals(AggregateType.NONE)) {
			r_bR = true;
		}
		return r_bR;
	}

	/**
	 * (non-Javadoc)
	 * @see com.a2a.adjava.uml2xmodele.extractors.viewmodel.associations.VMAssoPathProcessor#apply(com.a2a.adjava.uml2xmodele.extractors.viewmodel.VMPathContext, com.a2a.adjava.uml.UmlAssociationEnd, com.a2a.adjava.uml.UmlAssociationEnd, java.lang.String[], java.lang.String[], com.a2a.adjava.uml.UmlAttribute, com.a2a.adjava.uml.UmlDictionary, java.util.Map, com.a2a.adjava.xmodele.IDomain)
	 */
	@Override
	public void apply( VMPathContext p_oVMPathContext, UmlAssociationEnd p_oAsso1, UmlAssociationEnd p_oAsso2,
			String[] p_oAsso1Name, String[] p_oAsso2Name, UmlAttribute p_oUmlAttribute, UmlDictionary p_oDictionnary,
			Map<String, MViewModelImpl> p_oVMCache, IDomain<IModelDictionary, IModelFactory> p_oDomain) throws Exception {

		boolean bMandatory = p_oAsso2.getMultiplicityLower() == 1;
		
		MEntityImpl oCascadeEntity = p_oDomain.getDictionnary().getMapUmlClassToMClasses().get(p_oAsso2.getRefClass().getFullName());
		String sCascadeName = p_oVMPathContext.getUmlTarget().getName()+"Cascade."+p_oAsso2Name[0].toUpperCase();
		String sCascadeImport = p_oVMPathContext.getEntityTarget().getMasterInterface().getFullName() + "Cascade";
		
		MCascade oCascade = new MCascade(sCascadeName, sCascadeImport,oCascadeEntity, p_oAsso2Name[0]);
		
		p_oVMPathContext.getMasterVM().addLoadCascade(oCascade);
		p_oVMPathContext.getMasterVM().addSaveCascade(oCascade);

		p_oVMPathContext.setUmlTarget(p_oAsso2.getRefClass());
		log.debug("new target is : {}", p_oVMPathContext.getUmlTarget().getName());
		p_oVMPathContext.setEntityTarget(p_oDomain.getDictionnary().getMapUmlClassToMClasses()
				.get(p_oVMPathContext.getUmlTarget().getFullName()));

		p_oVMPathContext.setCurrentMapping(p_oVMPathContext.getCurrentMapping().getOrAddEntity(
				p_oVMPathContext.getCurrentVM(), p_oVMPathContext.getEntityTarget(),
				p_oVMPathContext.getCurrentElement(), bMandatory, p_oDomain));

		log.debug("find target {}/{}", p_oVMPathContext.getUmlTarget().getFullName(), p_oVMPathContext.getEntityTarget());
		log.debug("cas n->1 with composite");
	}
	
	
	// ancien code:
	
	//Cas des relations n->1 start asso1, end asso2 avec une composition [OK tester TEB]
//	else if (oAssoHelper.isValidCaseNto1Composite(p_oVMPathContext.getUmlTarget(), p_oAsso1, p_oAsso2, p_oVMPathContext.getCurrentElement(), name2)) {
//		p_oVMPathContext.getMasterVM().addLoadCascade(p_oVMPathContext.getUmlTarget().getName()+"Cascade."+name2[0].toUpperCase(), p_oVMPathContext.getTargetEntity().getMasterInterface().getFullName() + "Cascade");
//		p_oVMPathContext.getMasterVM().addSaveCascade(p_oVMPathContext.getUmlTarget().getName()+"Cascade."+name2[0].toUpperCase(), p_oVMPathContext.getTargetEntity().getMasterInterface().getFullName() + "Cascade");
//		
//		p_oVMPathContext.setUmlTarget( p_oAsso2.getRefClass();
//		log.debug("new target is : " + p_oVMPathContext.getUmlTarget().getName());
//		p_oVMPathContext.setTargetEntity(p_oDomain.getDictionnary().getMapUmlClassToMClasses().get(p_oVMPathContext.getUmlTarget().getFullName()));
//
//		p_oVMPathContext.setCurrentMapping(
//				p_oVMPathContext.getCurrentMapping().getOrAddEntity(p_oVMPathContext.getMasterVM(), p_oVMPathContext.getTargetEntity(), p_oVMPathContext.getCurrentElement()));
//
//		log.debug("find target "+ p_oVMPathContext.getUmlTarget().getFullName() + "/" + p_oVMPathContext.getTargetEntity());
//		log.debug("cas n->1 start asso1 and asso2, with composite");
//		p_oVMPathContext.setValid(true);
//	}
	//Cas des relations n->1 start asso2, end asso1 avec une composition [OK tester TEB]
//	else if (oAssoHelper.isValidCaseNto1Composite(p_oVMPathContext.getUmlTarget(), p_oAsso2, p_oAsso1, p_oVMPathContext.getCurrentElement(), name1)) {
//		p_oVMPathContext.getMasterVM().addLoadCascade(p_oVMPathContext.getUmlTarget().getName()+"Cascade."+name1[0].toUpperCase(), p_oVMPathContext.getTargetEntity().getMasterInterface().getFullName() + "Cascade");
//		p_oVMPathContext.getMasterVM().addSaveCascade(p_oVMPathContext.getUmlTarget().getName()+"Cascade."+name2[0].toUpperCase(), p_oVMPathContext.getTargetEntity().getMasterInterface().getFullName() + "Cascade");
//		
//		p_oVMPathContext.setUmlTarget( p_oAsso1.getRefClass();
//		log.debug("new target is : " + p_oVMPathContext.getUmlTarget().getName());
//		p_oVMPathContext.setTargetEntity(p_oDomain.getDictionnary().getMapUmlClassToMClasses().get(p_oVMPathContext.getUmlTarget().getFullName()));
//
//		p_oVMPathContext.setCurrentMapping(p_oVMPathContext.getCurrentMapping().getOrAddEntity(p_oVMPathContext.getMasterVM(), 
//			p_oVMPathContext.getTargetEntity(), p_oVMPathContext.getCurrentElement()));
//
//		log.debug("find target "+ p_oVMPathContext.getUmlTarget().getFullName() + "/" + p_oVMPathContext.getTargetEntity());
//		log.debug("cas n->1 start asso2 and asso1, with composite");
//
//		p_oVMPathContext.setValid(true);
//	}
}
