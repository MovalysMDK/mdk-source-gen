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
package com.a2a.adjava.languages.ios.generators;

import org.apache.commons.io.FilenameUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.generator.core.incremental.AbstractIncrementalGenerator;
import com.a2a.adjava.generator.core.incremental.NonGeneratedBlocExtractor;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.languages.ios.extractors.IOSVMNamingHelper;
import com.a2a.adjava.uml2xmodele.extractors.viewmodel.VMNamingHelper;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.XProject;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;

/**
 * Viewmodel generator
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @author lmichenaud
 * @since MF-Annapurna
 */
public class ViewInterfaceFixedListGenerator extends AbstractIncrementalGenerator<IDomain<IModelDictionary,IModelFactory>> {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ViewInterfaceFixedListGenerator.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void genere(XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		log.debug("> ViewInterfaceFixedListGenerator.genere");
		Chrono oChrono = new Chrono(true);
		NonGeneratedBlocExtractor oNonGeneratedBlocExtractor = new NonGeneratedBlocExtractor();
		for(MViewModelImpl oViewModel : p_oProject.getDomain().getDictionnary().getAllViewModels()) {
			if (oViewModel.getType() == ViewModelType.FIXED_LIST) {
				this.createViewFixedList(oViewModel, oNonGeneratedBlocExtractor, p_oProject, p_oContext);
			}
		}
		log.debug("< ViewInterfaceFixedListGenerator.genere: {}", oChrono.stopAndDisplay());
	}
	
	/**
	 * Generate implementation of viewmodel
	 * @param p_oMViewModel viewmodel
	 * @param p_oNonGeneratedBlocExtractor non-generated bloc extractor
	 * @param p_oMProject project
	 * @param p_mapSession session
	 * @throws Exception exception
	 */
	private void createViewFixedList(MViewModelImpl p_oMViewModel, NonGeneratedBlocExtractor p_oNonGeneratedBlocExtractor,
			XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {

		Document xViewModelDoc = createDocument(p_oMViewModel, p_oProject);
		
		String sViewModelFile = this.getViewFixedListFilename(p_oMViewModel, p_oProject);
		
		log.debug("  generation du fichier: {}", sViewModelFile);
		this.doIncrementalTransform("view-fixedlist-interface.xsl", sViewModelFile, xViewModelDoc, p_oProject, p_oContext);
	}

	/**
	 * Create document
	 * @param p_oMViewModel viewmodel
	 * @param p_oProject project
	 * @return xml of viewmodel implementation
	 */
	protected Document createDocument(MViewModelImpl p_oMViewModel,
			XProject<IDomain<IModelDictionary, IModelFactory>> p_oProject) {
		Element r_xViewModelFile = p_oMViewModel.toXml();
		r_xViewModelFile.addElement("master-package").setText(p_oProject.getDomain().getRootPackage());
		r_xViewModelFile.addElement("view-fixedlist-name").setText(IOSVMNamingHelper.getInstance().computeViewNameOfFixedList(p_oMViewModel));
		
		return DocumentHelper.createDocument(r_xViewModelFile);
	}
	
	/**
	 * Compute viewmodel filename
	 * @param p_oMViewModel viewmodel
	 * @param p_oProject project
	 * @return viewmodel filename
	 */
	protected String getViewFixedListFilename( MViewModelImpl p_oMViewModel, XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject) {
		return FilenameUtils.normalize(FileTypeUtils.computeFilenameForIOSInterface(
				"view", IOSVMNamingHelper.getInstance().computeViewNameOfFixedList(p_oMViewModel), p_oProject.getSourceDir()));
	}

}
