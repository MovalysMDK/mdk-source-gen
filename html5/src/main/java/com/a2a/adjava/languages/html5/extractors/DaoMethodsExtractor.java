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
package com.a2a.adjava.languages.html5.extractors;

import java.util.LinkedList;
import java.util.List;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.uml2xmodele.extractors.AbstractExtractor;
import com.a2a.adjava.utils.StrUtils;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.MAssociation;
import com.a2a.adjava.xmodele.MAssociation.AssociationType;
import com.a2a.adjava.xmodele.MDaoImpl;
import com.a2a.adjava.xmodele.MDaoMethodSignature;
import com.a2a.adjava.xmodele.MMethodParameter;
import com.a2a.adjava.languages.html5.xmodele.MH5Dictionary;
import com.a2a.adjava.languages.html5.xmodele.MH5Domain;
import com.a2a.adjava.languages.html5.xmodele.MH5ModeleFactory;


/**
 * html5 dao methods extractor
 * @author fgouy
 *
 */
public class DaoMethodsExtractor extends AbstractExtractor<MH5Domain<MH5Dictionary, MH5ModeleFactory>> {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(DaoMethodsExtractor.class);



	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.extractors.MExtractor#initialize(org.dom4j.Element)
	 */
	@Override
	public void initialize(Element p_xConfig) throws Exception {
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.extractors.MExtractor#extract(com.a2a.adjava.uml.UmlModel)
	 */
	@Override
	public void extract(UmlModel p_oModele) throws Exception {		
		IModelDictionary oDictionnary = this.getDomain().getDictionnary();
		
		
		for (MDaoImpl oClass : oDictionnary.getAllDaos()) {			
			log.debug("DaoMethodsExtractor.extract : {}", oClass.getName());
			

			String oClassName = oClass.getMEntityImpl().getName();
			LinkedList<MDaoMethodSignature> listMethodsToAdd = new LinkedList<MDaoMethodSignature>();
			List<MMethodParameter> listMethodsParametersToAdd;
			log.debug("   oClassName : {}", oClassName);
			
			// For all methods of the class
			for (MDaoMethodSignature oMethod : oClass.getMethodSignatures()) {				
				listMethodsParametersToAdd = oMethod.getParameters();
				
				// If the methods might be cloned  to create a new one
				if(oMethod.getType().equals("getListEntite") && !oMethod.isByValue()) {
					log.debug("      method signature might be cloned: {}", oMethod.getName());					
					MMethodParameter oParameter = oMethod.getParameters().get(0);
					String oParameterClassName = oParameter.getTypeDesc().getShortName();
					
					for (MAssociation oAssociation : oClass.getMEntityImpl().getAssociations()) {
						log.debug("         oParameter.getName(): {} (oParameter.getTypeDesc().getShortName(): {})", oParameter.getName(), oParameter.getTypeDesc().getShortName());
						log.debug("         oAssociation.getName(): {} (oAssociation.getAssociationType(): {})", oAssociation.getName(), oAssociation.getAssociationType());
						log.debug("         oAssociation.getOppositeClass().getName(): {} VS oClassName: {}", oAssociation.getOppositeClass().getName(), oClassName);
						log.debug("         oAssociation.getRefClass().getName(): {} VS oParameterClassName: {}", oAssociation.getRefClass().getName(), oParameterClassName);
						AssociationType oAssociationType = oAssociation.getAssociationType();
						
						// If the association corresponds to our criteria
						if( 	(oAssociationType == AssociationType.MANY_TO_MANY || oAssociationType == AssociationType.MANY_TO_ONE)
								&& oAssociation.getOppositeClass().getName().equals(oClassName)
								&& oAssociation.getRefClass().getName().equals(oParameterClassName)) {
							
							log.debug("            method signature MUST be cloned: {}", oMethod.getName());
							
							// Create the method and add it to the list of methods to add
							listMethodsToAdd.add(createMethodSignature(oClass, oMethod, listMethodsParametersToAdd));
													
						} // end if association
					} // end for associations
					
				} // end if method
			} // end for methods
			
			// Add created methods to the class
			oClass.getMethodSignatures().addAll(listMethodsToAdd);			
			
		} // end for classes
		
		
	}
	
	
	/**
	 * Creates a new method signature which is very close to the one given in parameter (only type and parameters are modified)
	 * @param p_oClass
	 * @param p_oOldParameters
	 * @param p_oPreviousTypeDescription
	 * @return MDaoMethodSignature the new method signature
	 */
	protected MDaoMethodSignature createMethodSignature( MDaoImpl p_oClass, MDaoMethodSignature p_oOldMethodSignature, List<MMethodParameter> p_oOldParameters) {
		log.debug("DaoMethodsExtractor.createMethodSignature : {}", p_oOldMethodSignature.getName());
		
		
		MDaoMethodSignature r_oNewMethodSignature;
		
		// Create new method
		r_oNewMethodSignature = new MDaoMethodSignature(
				p_oOldMethodSignature.getName(), 
				p_oOldMethodSignature.getVisibility(), 
				new StringBuilder(p_oOldMethodSignature.getType()).append("ByIds").toString(), 
				p_oOldMethodSignature.getReturnedType(), 
				new LinkedList<String>(), 
				p_oOldMethodSignature.getNeededImports(), 
				true);

		// Add old parameter(s) to new method
		r_oNewMethodSignature.addParameters(p_oOldParameters);
		
		
		return r_oNewMethodSignature ;
	}
	
	
	
	/**
	 * Adds an import
	 * @param p_sImport import
	 */
	public void addImport(String p_sImport, List<String> p_importList) {
		if (!p_importList.contains(p_sImport) && p_sImport.lastIndexOf(StrUtils.DOT) != -1) {
			String sPackage = p_sImport.substring(0, p_sImport.lastIndexOf(StrUtils.DOT));
			if ( !sPackage.startsWith("java.lang")) {
				p_importList.add(p_sImport);
			}
		}
	}
}
