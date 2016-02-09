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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dom4j.Element;

import com.a2a.adjava.AdjavaProperty;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.uml.UmlPackage;
import com.a2a.adjava.utils.StrUtils;
import com.a2a.adjava.xmodele.MPackage;

/**
 * <p>Package extractor</p>
 *
 * <p>Copyright (c) 2011
 * <p>Company: Adeuza
 *
 * @author lmichenaud
 *
 */

public class PackageExtractor extends AbstractExtractor {

	/**
	 * Logger 
	 */
	private static final Logger log = LoggerFactory.getLogger(PackageExtractor.class);
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.uml2xmodele.extractors.MExtractor#initialize()
	 */
	@Override
	public void initialize( Element p_xConfig ) {
		
	}

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.uml2xmodele.extractors.MExtractor#extract(com.a2a.adjava.uml.UmlModel)
	 */
	@Override
	public void extract(UmlModel p_oModele) throws Exception {
		// Conversion des packages
		for (UmlPackage oUmlPackage : p_oModele.getPackages()) {
			log.debug("convert package " + oUmlPackage.getFullName());
			MPackage oPackage = convertUmlPackage(oUmlPackage, null );
			getDomain().getModel().addPackage(oPackage);
		}
		
		this.createPackageTree( this.getDomain().getRootPackage());
	}
	
	/**
	 * Create all MPackage tree from package full name
	 * @param p_sPackageFullName
	 */
	public void createPackageTree( String p_sPackageFullName ) throws Exception {
		String[] sPathParts = p_sPackageFullName.split("\\.");
		MPackage oParent = null ;
		StringBuilder sFullPath = new StringBuilder();
		for( String sPathPart: sPathParts) {
			sFullPath.append(sPathPart);
			MPackage oPackage = getDomain().getDictionnary().getPackage(sFullPath.toString());
			if ( oPackage == null ) {
				oPackage = new MPackage(sPathPart, oParent);
				if ( oParent != null ) {
					oParent.addPackage(oPackage);
				}
				getDomain().getDictionnary().registerPackage(oPackage);
			}
			oParent = oPackage ;
			sFullPath.append(StrUtils.DOT);
		}
	}
	
	/**
	 * @param p_oUmlPackage
	 * @param p_oPackageParent
	 * @param p_oModele
	 * @param p_oModelDictionnary
	 * @return
	 */
	private MPackage convertUmlPackage(UmlPackage p_oUmlPackage, MPackage p_oPackageParent) {

		String p_UmlPackageName = p_oUmlPackage.getName();
		//Control of the packages' names
		if(p_UmlPackageName.contains(Character.toString('.')))
		{
			MessageHandler.getInstance().addError("The char '.' is unauthorized in a package name (package name \"{}\" unauthorized).", p_UmlPackageName);
		}
		
		//The package full path must contain the path present in the .bat of the generator
		String projetPath = (String)getDomain().getGlobalParameters().get(AdjavaProperty.BASE_PACKAGE.getName());
		log.debug("projetPath = "+projetPath);
		if(null!=p_oUmlPackage.getFullName() && !p_oUmlPackage.getFullName().contains(projetPath) && !projetPath.contains(p_oUmlPackage.getFullName()))
		{
			MessageHandler.getInstance().addError("The basePackage \""+projetPath+ "\" present in the pom.xml must correspond to the path created in the UML xml \""+	p_oUmlPackage.getFullName() +"\"");
		}
		
		String sPackageName = getDomain().getStrSubstitutor().replace(p_UmlPackageName);

		// Because of the substitutor, the package may already exist. So we check first in
		// the dictionnary.
		MPackage r_oPackage = getDomain().getDictionnary().getPackage(sPackageName);
		if ( r_oPackage == null ) {
			r_oPackage = new MPackage(sPackageName, p_oPackageParent);
			getDomain().getDictionnary().registerPackage(r_oPackage);
		}
		
		// Lecture des sous-packages
		for (UmlPackage oUmlPackage : p_oUmlPackage.getPackages()) {
			log.debug("conversion du package " + oUmlPackage.getFullName());
			MPackage oPackage = convertUmlPackage(oUmlPackage, r_oPackage);
			r_oPackage.addPackage(oPackage);
		}

		return r_oPackage;
	}
}
