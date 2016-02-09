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

import com.a2a.adjava.uml.UmlAssociationClass;
import com.a2a.adjava.uml.UmlAssociationEnd;
import com.a2a.adjava.uml.UmlAssociationEnd.AggregateType;
import com.a2a.adjava.uml.UmlAttribute;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml2xmodele.extractors.viewmodel.VMListPathProcessor;
import com.a2a.adjava.uml2xmodele.extractors.viewmodel.VMPathContext;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MCascade;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;

/**
 * Path processor for "Many To Many - Composite" association.
 * @author lmichenaud
 *
 */
public class VMAssoPathManyToManyComposite implements VMAssoPathProcessor {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(VMAssoPathManyToManyComposite.class);

	/**
	 * (non-Javadoc)
	 * @see com.a2a.adjava.uml2xmodele.extractors.viewmodel.associations.VMAssoPathProcessor#isValid(com.a2a.adjava.uml.UmlClass, com.a2a.adjava.uml.UmlAssociationEnd, com.a2a.adjava.uml.UmlAssociationEnd, java.lang.String, java.lang.String[], com.a2a.adjava.uml.UmlDictionary)
	 */
	@Override
	public boolean isValid(UmlClass p_oTarget, UmlAssociationEnd p_oAsso1, UmlAssociationEnd p_oAsso2,
			String p_sPathElement, String[] p_sAssoName, UmlDictionary p_oDictionnary) {
		boolean r_bR = false;
		boolean bFind = false;
		if (p_sAssoName.length==2) {
			if (p_oAsso1.getRefClass() == p_oTarget && p_sPathElement.equals(p_sAssoName[1]) && 
					p_oAsso1.getMultiplicityUpper() == -1 && p_oAsso2.getMultiplicityUpper() == -1 &&
					p_oAsso2.isNavigable() && p_oAsso1.getAggregateType().equals(AggregateType.COMPOSITE)) {
				bFind = false;
				for(UmlAssociationClass oClass : p_oDictionnary.getAssociationClasses()) {
					if ((oClass.getAssociationEnd1() == p_oAsso1 && oClass.getAssociationEnd2() == p_oAsso2)
							|| (oClass.getAssociationEnd2() == p_oAsso1 && oClass.getAssociationEnd1() == p_oAsso2)) {
						bFind = true;
					}
				}
				r_bR = bFind;
			}
		}
		return r_bR;
	}

	/**
	 * (non-Javadoc)
	 * @see com.a2a.adjava.uml2xmodele.extractors.viewmodel.associations.VMAssoPathProcessor#apply(com.a2a.adjava.uml2xmodele.extractors.viewmodel.VMPathContext, com.a2a.adjava.uml.UmlAssociationEnd, com.a2a.adjava.uml.UmlAssociationEnd, java.lang.String[], java.lang.String[], com.a2a.adjava.uml.UmlAttribute, com.a2a.adjava.uml.UmlDictionary, java.util.Map, com.a2a.adjava.xmodele.IDomain)
	 */
	@Override
	public void apply(VMPathContext p_oVMPathContext, UmlAssociationEnd p_oAsso1, UmlAssociationEnd p_oAsso2,
			String[] p_oAsso1Name, String[] p_oAsso2Name, UmlAttribute p_oUmlAttribute, UmlDictionary p_oDictionnary,
			Map<String, MViewModelImpl> p_oVMCache, IDomain<IModelDictionary, IModelFactory> p_oDomain) throws Exception {
		
		for(UmlAssociationClass oClass : p_oDictionnary.getAssociationClasses()) {
			if ((oClass.getAssociationEnd1() == p_oAsso1 && oClass.getAssociationEnd2() == p_oAsso2)
					|| (oClass.getAssociationEnd2() == p_oAsso1 && oClass.getAssociationEnd1() == p_oAsso2)) {
				p_oVMPathContext.setUmlTarget( oClass);
			}
		}
		p_oVMPathContext.setEntityTarget( p_oDomain.getDictionnary().getMapUmlClassToMClasses().get(p_oVMPathContext.getUmlTarget().getFullName()));
		
		MEntityImpl oCascadeEntity = p_oDomain.getDictionnary().getMapUmlClassToMClasses().get(p_oAsso2.getRefClass().getFullName());
		String sCascadeName = p_oVMPathContext.getUmlTarget().getName()+"Cascade."+p_oAsso2Name[0].toUpperCase();
		String sCascadeImport = p_oVMPathContext.getEntityTarget().getMasterInterface().getFullName() + "Cascade";
		
		MCascade oMCascade = new MCascade(sCascadeName, sCascadeImport, oCascadeEntity, p_oAsso2Name[0]);
		p_oVMPathContext.getMasterVM().addLoadCascade(oMCascade);
		p_oVMPathContext.getMasterVM().addSaveCascade(oMCascade);
		
		log.debug("find target {}/{}", p_oVMPathContext.getUmlTarget().getFullName(), p_oVMPathContext.getEntityTarget());
		log.debug("cas n->n start asso1 and asso2, with composite, one association class");
		p_oVMPathContext.setCurrentVM( VMListPathProcessor.getInstance().treatListPath(p_oVMPathContext, p_oVMPathContext.getIndex()+1, 
				p_oVMPathContext.getEntityTarget(), ViewModelType.LIST_1, p_oVMCache, p_oDomain,null));

		p_oVMPathContext.setCurrentMapping(p_oVMPathContext.getCurrentMapping().getOrAddEntity(p_oVMPathContext.getMasterVM(), 
				p_oVMPathContext.getEntityTarget(), p_oVMPathContext.getCurrentElement(), false, p_oDomain));
	
	}
	
	
//	//Cas des relations n->n start asso1 end asso2, avec classe d'asso avec une composition
//	else if (oAssoHelper.isValidCaseNtoNWithAsso(p_oUmlDict, p_oVMPathContext.getUmlTarget(), p_oAsso1, p_oAsso2, p_oVMPathContext.getCurrentElement(), name2)) {
//		for(UmlAssociationClass oClass : p_oUmlDict.getAssociationClasses()) {
//			if ((oClass.getAssociationEnd1() == p_oAsso1 && oClass.getAssociationEnd2() == p_oAsso2)
//					|| (oClass.getAssociationEnd2() == p_oAsso1 && oClass.getAssociationEnd1() == p_oAsso2)) {
//				p_oVMPathContext.setUmlTarget( oClass);
//			}
//		}
//		p_oVMPathContext.setEntityTarget( p_oDomain.getDictionnary().getMapUmlClassToMClasses().get(p_oVMPathContext.getUmlTarget().getFullName()));
//		p_oVMPathContext.getMasterVM().addLoadCascade(p_oVMPathContext.getUmlTarget().getName()+"Cascade."+name2[0].toUpperCase(), p_oVMPathContext.getEntityTarget().getMasterInterface().getFullName() + "Cascade");
//		p_oVMPathContext.getMasterVM().addSaveCascade(p_oVMPathContext.getUmlTarget().getName()+"Cascade."+name2[0].toUpperCase(), p_oVMPathContext.getEntityTarget().getMasterInterface().getFullName() + "Cascade");
//		
//		log.debug("find target "+ p_oVMPathContext.getUmlTarget().getFullName() + "/" + p_oVMPathContext.getEntityTarget());
//		log.debug("cas n->n start asso1 and asso2, with composite, one association class");
//		p_oVMPathContext.setCurrentVM( VMListPathProcessor.getInstance().treatListPath(p_oVMPathContext, p_oVMPathContext.getIndex()+1, 
//				p_oVMPathContext.getEntityTarget(), ViewModelType.LIST_1, p_oVMCache, p_oDomain));
//
//		p_oVMPathContext.setCurrentMapping(p_oVMPathContext.getCurrentMapping().getOrAddEntity(p_oVMPathContext.getMasterVM(), 
//				p_oVMPathContext.getEntityTarget(), p_oVMPathContext.getCurrentElement()));
//
//		p_oVMPathContext.setValid(true);
//	}
//	//Cas des relations n->n start asso2 end asso1, avec classe d'asso avec une composition
//	else if (oAssoHelper.isValidCaseNtoNWithAsso(p_oUmlDict, p_oVMPathContext.getUmlTarget(), p_oAsso2, p_oAsso1, p_oVMPathContext.getCurrentElement(), name1)) {
//		for(UmlAssociationClass oClass : p_oUmlDict.getAssociationClasses()) {
//			if ((oClass.getAssociationEnd1() == p_oAsso2 && oClass.getAssociationEnd2() == p_oAsso1)
//					|| (oClass.getAssociationEnd2() == p_oAsso2 && oClass.getAssociationEnd1() == p_oAsso1)) {
//				p_oVMPathContext.setUmlTarget( oClass );
//			}
//		}	
//		p_oVMPathContext.setEntityTarget( p_oDomain.getDictionnary().getMapUmlClassToMClasses().get(p_oVMPathContext.getUmlTarget().getFullName()));
//		p_oVMPathContext.getMasterVM().addLoadCascade(p_oVMPathContext.getUmlTarget().getName()+"Cascade."+name1[0].toUpperCase(), p_oVMPathContext.getEntityTarget().getMasterInterface().getFullName() + "Cascade");
//		p_oVMPathContext.getMasterVM().addSaveCascade(p_oVMPathContext.getUmlTarget().getName()+"Cascade."+name2[0].toUpperCase(), p_oVMPathContext.getEntityTarget().getMasterInterface().getFullName() + "Cascade");
//		
//		log.debug("find target "+ p_oVMPathContext.getUmlTarget().getFullName() + "/" + p_oVMPathContext.getEntityTarget());
//		log.debug("cas n->n start asso2 and asso1, with composite, one association class");
//		p_oVMPathContext.setCurrentVM( VMListPathProcessor.getInstance().treatListPath(p_oVMPathContext, p_oVMPathContext.getIndex()+1, 
//				p_oVMPathContext.getEntityTarget(), ViewModelType.LIST_1, p_oVMCache, p_oDomain));
//
//		p_oVMPathContext.setCurrentMapping(p_oVMPathContext.getCurrentMapping().getOrAddEntity(
//				p_oVMPathContext.getMasterVM(), p_oVMPathContext.getEntityTarget(), p_oVMPathContext.getCurrentElement()));
//		
//		p_oVMPathContext.setValid(true);
//	}
//}
}
