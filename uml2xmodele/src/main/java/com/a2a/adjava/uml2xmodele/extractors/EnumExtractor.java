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
package com.a2a.adjava.uml2xmodele.extractors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dom4j.Element;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.types.ITypeConvertion;
import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlEnum;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.xmodele.MEnumeration;
import com.a2a.adjava.xmodele.MPackage;

/**
 * <p>EnumExtractor</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */

public class EnumExtractor extends AbstractExtractor {

	private static final String STEREOTYPES_PARAMETER = "stereotypes";

	/**
	 * Logger 
	 */
	private static final Logger log = LoggerFactory.getLogger(PackageExtractor.class);
	
	/**
	 * Stereotypes
	 */
	private List<String> listStereotypes = new ArrayList<String>();
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.uml2xmodele.extractors.MExtractor#initialize()
	 */
	@Override
	public void initialize( Element p_xConfig ) throws Exception {
		String sStereotypes = getParameters().getValue(STEREOTYPES_PARAMETER);
		if ( sStereotypes != null ) {
			for (String sStereotype : sStereotypes.split(",")) {
				sStereotype = sStereotype.trim();
				listStereotypes.add(sStereotype);
			}
		}
		else {
			throw new AdjavaException("Parameter '{}' is missing on extractor EnumExtractor", STEREOTYPES_PARAMETER);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.uml2xmodele.extractors.MExtractor#extract(com.a2a.adjava.uml.UmlModel)
	 */
	@Override
	public void extract(UmlModel p_oModele) throws Exception {
		
		UmlDictionary oDict = p_oModele.getDictionnary();
		
		Map<String, MEnumeration> mapUmlEnum2MEnum = new HashMap<String, MEnumeration>();
		for (UmlEnum oUmlEnum : oDict.getAllEnumerations()) {		
			if (isEnumerationOfDomain(oUmlEnum)) {
				log.debug("convert enumeration " + oUmlEnum.getFullName());
				MEnumeration oMEnumeration = this.convertUmlEnumeration(oUmlEnum);
				mapUmlEnum2MEnum.put(oUmlEnum.getFullName(), oMEnumeration);
			}
		}
	}
	
	/**
	 * @param p_oEnum
	 * @param p_oModele
	 * @param p_oModelDictionnary
	 * @return
	 */
	private MEnumeration convertUmlEnumeration(UmlEnum p_oEnum) {
		// Calcule le nom de l'enumeration
		StringBuilder oEnumName = new StringBuilder();
		if ( this.getLngConfiguration().getEnumNamingPrefix() != null ) {
			oEnumName.append(this.getLngConfiguration().getImplNamingPrefix());
		}
		oEnumName.append(p_oEnum.getName());
		if ( this.getLngConfiguration().getEnumNamingSuffix() != null ) {
			oEnumName.append(this.getLngConfiguration().getEnumNamingSuffix());
		}
		String sName = oEnumName.toString();
		
		log.debug("  name: {}", sName);

		MPackage oPackageBase = getDomain().getDictionnary().getPackage(p_oEnum.getUmlPackage().getFullName());
		MPackage oPackageEnum = oPackageBase;
		
		if (this.getLngConfiguration().getInterfaceSubPackageName() != null && this.getLngConfiguration().getInterfaceSubPackageName().length() > 0) {
			oPackageEnum = oPackageBase.getChildPackage(this.getLngConfiguration().getInterfaceSubPackageName());
			if (oPackageEnum == null) {
				oPackageEnum = new MPackage(this.getLngConfiguration().getInterfaceSubPackageName(), oPackageBase);
				oPackageBase.addPackage(oPackageEnum);
			}
		}
		log.debug("  package enum = " + oPackageEnum.getFullName());

		ITypeDescription oTypeDesc = (ITypeDescription) this.getLngConfiguration().getTypeDescription(
				"enumeration");
		
		oTypeDesc = (ITypeDescription) oTypeDesc.clone();
		oTypeDesc.setName(p_oEnum.getName());
		oTypeDesc.setUmlName(oTypeDesc.getShortName());
		oTypeDesc.setPrimitif(false);
		oTypeDesc.setEnumeration(true);
		
		//log.info("enum : {} {}", oTypeDesc.getName(), getDomain().getName());
		
		MEnumeration r_oEnum = new MEnumeration(sName, oPackageEnum, p_oEnum.getEnumValues(), oTypeDesc);

		// Update formula for type conversions
		for( ITypeConvertion oTypeConvertion : oTypeDesc.getConvertions()) {
			oTypeConvertion.setFormula(oTypeConvertion.getFormula().replace("ENUMERATION", r_oEnum.getName()));
			oTypeConvertion.getImports().add(r_oEnum.getFullName());
			//log.info("  convert to : {} - {}", oTypeConvertion.getTo(), oTypeConvertion.getFormula());
		}

		// Update other type descriptions that can convert to enumeration
		for( ITypeDescription oTypeDescription : getDomain().getLanguageConf().getTypeDescriptions().values()) {
			//log.info("loop type desc " + oTypeDescription.getName() +  " " + oTypeDescription.getConvertionTypeNames().size());
			ITypeConvertion oEnumTypeConversion = oTypeDescription.getConvertion("enumeration");
			if ( oEnumTypeConversion != null ) {
				List<String> listImports = new ArrayList<String>();
				listImports.addAll(oEnumTypeConversion.getImports());
				listImports.add(r_oEnum.getFullName());
				String sNewFormula = oEnumTypeConversion.getFormula().replace("ENUMERATION", r_oEnum.getName());
				oTypeDescription.addTypeConvertion(oTypeDesc.getUmlName(), sNewFormula, listImports);
				//log.info("  convert from : " + oTypeDescription.getUmlName() + " - " + sNewFormula + " - " + listImports.toString());
			}
		}

		getDomain().getDictionnary().registerEnumeration(r_oEnum);	
		getDomain().getDictionnary().registerTypeDescription(oTypeDesc.getUmlName(), oTypeDesc);

		return r_oEnum;
	}
	
	/**
	 * Return true if uml class is of type Entity
	 * 
	 * @param p_oUmlClass UmlClass
	 * @return true if uml class is of type Entity
	 */
	private boolean isEnumerationOfDomain(UmlEnum p_oUmlEnum) {
		return p_oUmlEnum.hasAnyStereotype(this.listStereotypes) ;
	}
}
