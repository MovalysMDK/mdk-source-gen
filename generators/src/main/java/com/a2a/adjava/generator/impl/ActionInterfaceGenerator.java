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

import com.a2a.adjava.generator.core.XslTemplate;
import com.a2a.adjava.generator.core.incremental.AbstractIncrementalGenerator;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MActionInterface;
import com.a2a.adjava.xmodele.XProject;

/**
 * Generator for interface of action
 * @author lmichenaud
 *
 */
public class ActionInterfaceGenerator extends AbstractIncrementalGenerator<IDomain<IModelDictionary,IModelFactory>> {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ActionInterfaceGenerator.class);
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void genere(XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		log.debug("> ActionInterfaceGenerator.genere");
		Chrono oChrono = new Chrono(true);
		for(MActionInterface oAction : p_oProject.getDomain().getDictionnary().getAllActionInterfaces()) {
			createAction(oAction, p_oProject, p_oContext);
		}
		log.debug("< ActionInterfaceGenerator.genere: {}", oChrono.stopAndDisplay());
	}
	
	/**
	 * @param p_oAction
	 * @param p_oMProject
	 * @param p_oContext
	 * @throws Exception
	 */
	protected void createAction(MActionInterface p_oAction, 
			XProject<IDomain<IModelDictionary,IModelFactory>> p_oMProject, DomainGeneratorContext p_oContext) throws Exception {

		Document xDoc = this.computeXml(p_oAction, p_oMProject);
		String sFile = this.getFilename(p_oAction, p_oMProject);
		
		log.debug("  generation du fichier {}", sFile);
		this.doIncrementalTransform( this.getTemplate(), sFile, xDoc, p_oMProject, p_oContext);
	}

	/**
	 * Compute xml of action interface
	 * @param p_oAction interface of action
	 * @param p_oMProject Le projet
	 * @return xml
	 */
	protected Document computeXml(MActionInterface p_oAction, XProject<IDomain<IModelDictionary,IModelFactory>> p_oMProject) {
		Element xFile = DocumentHelper.createElement("master-action-interface");
		xFile.add(p_oAction.toXml());
		xFile.addAttribute("main-project", p_oMProject.getDomain().getGlobalParameters().get("mainProject"));
		return DocumentHelper.createDocument(xFile);
	}
	
	/**
	 * Get output filename
	 * @param p_oAction action 
	 * @param p_oMProject project
	 * @return output filename
	 */
	protected String getFilename( MActionInterface p_oAction, XProject<IDomain<IModelDictionary,IModelFactory>> p_oMProject) {
		return FileTypeUtils.computeFilenameForJavaClass(p_oMProject.getSourceDir(), p_oAction.getFullName());
	}
	
	/**
	 * Get xsl template
	 * @return xsl template
	 */
	protected String getTemplate() {
		return XslTemplate.ACTION_INTERFACE.getFileName();
	}
}
