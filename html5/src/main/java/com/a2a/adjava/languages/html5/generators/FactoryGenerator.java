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
package com.a2a.adjava.languages.html5.generators;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.generator.core.incremental.AbstractIncrementalGenerator;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.languages.html5.xmodele.MH5Dictionary;
import com.a2a.adjava.languages.html5.xmodele.MH5ImportDelegate;
import com.a2a.adjava.languages.html5.xmodele.MH5ModeleFactory;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.MAttribute;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.XProject;

/**
 * <p>
 * Generation des factories
 * </p>
 * 
 * <p>
 * Copyright (c) 2009
 * <p>
 * Company: Adeuza
 * 
 * 
 */
public class FactoryGenerator extends
		AbstractIncrementalGenerator<IDomain<MH5Dictionary, MH5ModeleFactory>> {

	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(FactoryGenerator.class);
	
	private static final String docPath = "webapp/src/app/data/model/factories";

	
	
	/**
	 * @see com.a2a.adjava.generator.ResourceGenerator#genere(com.a2a.adjava.project.ProjectConfig,
	 *      com.a2a.adjava.xmodele.MModele, com.a2a.adjava.schema.Schema,
	 *      java.util.Map)
	 */
	@Override
	public void genere(XProject<IDomain<MH5Dictionary, MH5ModeleFactory>> p_oProject,
			DomainGeneratorContext p_oContext) throws Exception {
		log.debug("> FactoryGenerator.genere");
		Chrono oChrono = new Chrono(true);
		IModelDictionary oDictionnary = p_oProject.getDomain().getDictionnary();
		
		
		for (MEntityImpl oClass : oDictionnary.getAllEntities()) {
			this.genereFactory(oClass, p_oProject, p_oContext);
		}
		
		log.debug("< FactoryGenerator.genere: {}", oChrono.stopAndDisplay());
	}

	
	
	/**
	 * @param p_oProjectConfig
	 * @param oClass
	 * @param p_sProjectPackage
	 * @throws Exception
	 */
	private void genereFactory(MEntityImpl p_oClass, XProject<IDomain<MH5Dictionary, MH5ModeleFactory>> p_oMProject, DomainGeneratorContext p_oContext)
			throws Exception {

		Element r_xFile = p_oClass.getFactory().toXml();
		
		MH5ImportDelegate oMH5ImportDelegate = p_oMProject.getDomain().getXModeleFactory().createImportDelegate(this);
		this.computeImportForModelFactory(oMH5ImportDelegate, p_oClass, p_oMProject, p_oContext);
		r_xFile.add(oMH5ImportDelegate.toXml());
		
		Document xFactorysDoc = DocumentHelper.createDocument(r_xFile);

		String sFactoryFile = FileTypeUtils.computeFilenameForJS(docPath, p_oClass.getFactory().getName());
		log.debug("  generation du fichier: {}", sFactoryFile);
		this.doIncrementalTransform("entity-factory.xsl", sFactoryFile, xFactorysDoc, p_oMProject, p_oContext);
	}
	
	
	/**
	 * Compute imports for controller implementation
	 * @param p_oMDataLoader dataloader
	 * @param p_oMProject project
	 * @param p_oContext context
	 * @throws Exception
	 */
	protected void computeImportForModelFactory( MH5ImportDelegate p_oMH5ImportDelegate,
			MEntityImpl p_oClass, XProject<IDomain<MH5Dictionary, MH5ModeleFactory>> p_oMProject,
			DomainGeneratorContext p_oContext) throws Exception {
		

		p_oMH5ImportDelegate.addImport(p_oClass.getName());

// Commented in order to prevent circular dependencies errors
//		for(MAssociation assos : p_oClass.getAssociations()){
//			if(assos.getAssociationType().equals(AssociationType.MANY_TO_ONE) ||
//				(!assos.isTransient() && assos.getAssociationType().equals(AssociationType.ONE_TO_ONE))){
//				p_oMH5ImportDelegate.addImport(assos.getRefClass().getFactoryInterface().getName());
//			}
//		}
		
		for(MAttribute attr : p_oClass.getAttributes()){
			if((!attr.isTransient() && attr.isEnum())){
				p_oMH5ImportDelegate.addImport(attr.getTypeDesc().getShortName());
			}
			else if(attr.getTypeDesc().getShortName().equals("MFAddressLocation") ||
					attr.getTypeDesc().getShortName().equals("MFPhoto")){
				p_oMH5ImportDelegate.addImport(attr.getTypeDesc().getShortName() + "Factory");
			}
		}
	}
	
}
