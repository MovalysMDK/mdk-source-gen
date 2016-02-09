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
import com.a2a.adjava.generator.core.incremental.NonGeneratedBlocExtractor;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MViewModelCreator;
import com.a2a.adjava.xmodele.XProject;

public class ViewModelCreatorGenerator extends AbstractIncrementalGenerator<IDomain<IModelDictionary,IModelFactory>> {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ViewModelCreatorGenerator.class);
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.generators.ResourceGenerator#genere(com.a2a.adjava.xmodele.XProject, com.a2a.adjava.generators.DomainGeneratorContext)
	 */
	@Override
	public void genere(XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		log.debug("> ViewModelCreatorGenerator.genere");
		Chrono oChrono = new Chrono(true);
		NonGeneratedBlocExtractor oNonGeneratedBlocExtractor = new NonGeneratedBlocExtractor();
		
		this.createViewModelCreator(oNonGeneratedBlocExtractor, p_oProject, p_oContext);
		log.debug("< ViewModelCreatorGenerator.genere: {}", oChrono.stopAndDisplay());
	}
	
	/**
	 * @param p_oNonGeneratedBlocExtractor
	 * @param p_oProject
	 * @param p_oContext
	 * @throws Exception
	 */
	private void createViewModelCreator(NonGeneratedBlocExtractor p_oNonGeneratedBlocExtractor,
			XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		MViewModelCreator oVmc = p_oProject.getDomain().getDictionnary().getViewModelCreator();
		if (oVmc != null) {
			Element r_xViewModelFile = DocumentHelper.createElement("master-viewmodelcreator");
			r_xViewModelFile.add(oVmc.toXml());
			Document xViewModelDoc = DocumentHelper.createDocument(r_xViewModelFile);
			
			Element rootElement = xViewModelDoc.getRootElement();
			rootElement.addAttribute("main-project", p_oProject.getDomain().getGlobalParameters().get("mainProject"));
			
			String sViewModelFile = FileTypeUtils.computeFilenameForJavaClass(p_oProject.getSourceDir(), oVmc.getFullName());
			
			log.debug("  generation du fichier {}", sViewModelFile);
			this.doIncrementalTransform("viewmodelcreator.xsl", sViewModelFile, xViewModelDoc, p_oProject, p_oContext);
		}
	}
}
