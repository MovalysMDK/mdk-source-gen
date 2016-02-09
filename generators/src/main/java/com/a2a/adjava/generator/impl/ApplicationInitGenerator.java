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
package com.a2a.adjava.generator.impl;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.generator.core.incremental.AbstractIncrementalGenerator;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.utils.StrUtils;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.XProject;

/**
 * 
 * <p>Génere les classes de personnalisation de l'initialisation</p>
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 * @author spacreau
 */
public class ApplicationInitGenerator extends AbstractIncrementalGenerator<IDomain<IModelDictionary, IModelFactory>> {
	/**
	 * Logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(ApplicationInitGenerator.class);

	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.generators.ResourceGenerator#genere(com.a2a.adjava.xmodele.XProject, com.a2a.adjava.generators.DomainGeneratorContext)
	 */
	public void genere(XProject<IDomain<IModelDictionary, IModelFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		log.debug("> ApplicationInitGenerator");
		Chrono oChrono = new Chrono(true);
		
		this.genereJavaFile(p_oProject, "custominit.xsl" , "CustomInitImpl", p_oContext);
		this.genereJavaFile(p_oProject, "customapplicationinit.xsl" , "CustomApplicationInitImpl" , p_oContext);
		// this.genereJavaFile(p_oProject, "customconfhandlerinit.xsl" , "CustomConfigurationsHandlerInitImpl", p_oContext);
		
		log.debug("< ApplicationInitGenerator", oChrono.stopAndDisplay());
	}
	
	/**
	 * @param p_oProject
	 * @param p_sXSLFileName
	 * @param p_sOutputClass
	 * @param p_oContext
	 * @throws Exception
	 */
	private void genereJavaFile( XProject<IDomain<IModelDictionary, IModelFactory>> p_oProject , String p_sXSLFileName , String p_sOutputClass, DomainGeneratorContext p_oContext) 
			throws Exception {

		Element xElement = DocumentHelper.createElement("application");
		p_oProject.getDomain().toXml(xElement);
		
		String sFileName = FileTypeUtils.computeFilenameForJavaClass(p_oProject.getSourceDir(), 
			p_oProject.getDomain().getRootPackage() + StrUtils.DOT + p_sOutputClass);
		
		Document xDomain = DocumentHelper.createDocument(xElement);
		log.debug("  generation du fichier : {}", sFileName);
		this.doIncrementalTransform(p_sXSLFileName, sFileName, xDomain, p_oProject, p_oContext);
	}
}
