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
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.XProject;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;

/**
 * <p>Classe pour la génération des interfaces des <em>ViewModel</em></p>.
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @since MF-Annapurna
 */
public class ViewModelInterfaceGenerator extends AbstractIncrementalGenerator<IDomain<IModelDictionary,IModelFactory>> {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ViewModelInterfaceGenerator.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void genere(XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		log.debug("> ViewModelInterfaceGenerator.genere");
		Chrono oChrono = new Chrono(true);
		for(MViewModelImpl oViewModel : p_oProject.getDomain().getDictionnary().getAllViewModels()) {
			this.createViewModelInterface(oViewModel, p_oProject, p_oContext);
		}
		log.debug("< ViewModelInterfaceGenerator.genere: {}", oChrono.stopAndDisplay());
	}
	
	/**
	 * <p>Génération du fichier correspondant à l'interface d'un <em>ViewModel</em></p>.
	 * @param p_oMViewModel le ViewModel dont on veux générer l'interface
	 * @param p_oMProject le Flux xml à envoyer à la xsl
	 * @param p_mapSession la session
	 * @throws Exception erreur lors de la génération
	 */
	protected void createViewModelInterface(MViewModelImpl p_oMViewModel, 
			XProject<IDomain<IModelDictionary,IModelFactory>> p_oMProject, DomainGeneratorContext p_oContext) throws Exception {
		String sViewModelFile = this.getVMInterfaceFilename(p_oMViewModel, p_oMProject);	
		log.debug("  generate file: {}", sViewModelFile);		
		
		Document xViewModelDoc = this.computeXml(p_oMViewModel);
		Element rootElement = xViewModelDoc.getRootElement();
		rootElement.addAttribute("main-project", p_oMProject.getDomain().getGlobalParameters().get("mainProject"));
		
		String sTemplate = this.getTemplate(p_oMViewModel);
		
		this.doIncrementalTransform(sTemplate, sViewModelFile, xViewModelDoc, p_oMProject, p_oContext);
	}

	/**
	 * Compute xml document of viewmodel
	 * @param p_oMViewModel viewmodel
	 * @return xml of viewmodel
	 */
	protected Document computeXml(MViewModelImpl p_oMViewModel) {
		Element r_xViewModelFile = p_oMViewModel.toXml();
		r_xViewModelFile.add(p_oMViewModel.getMasterInterface().toXml());
		return DocumentHelper.createDocument(r_xViewModelFile);
	}
	
	/**
	 * Compute viewmodel filename
	 * @param p_oMViewModel viewmodel
	 * @param p_oProject project
	 * @return viewmodel filename
	 */
	protected String getVMInterfaceFilename( MViewModelImpl p_oMViewModel, XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject) {
		return FileTypeUtils.computeFilenameForJavaClass(p_oProject.getSourceDir(), p_oMViewModel.getMasterInterface().getFullName());
	}
	
	/**
	 * Get template
	 * @param p_oMViewModel viewmodel
	 * @return template name
	 */
	protected String getTemplate( MViewModelImpl p_oMViewModel ) {
		String r_sTemplate = "viewmodel-interface.xsl" ;
		if ( p_oMViewModel.getMasterInterface().isScreenWorkspace()) {
			r_sTemplate = "viewmodel-workspace-interface.xsl" ;
		}
		else if ( p_oMViewModel.getMasterInterface().isScreenMultiPanel()) {
			r_sTemplate = "viewmodel-multipanel-interface.xsl" ;
		}
		return r_sTemplate ;
	}
}
