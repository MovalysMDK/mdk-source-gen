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
import org.dom4j.io.DOMReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.generator.core.incremental.AbstractIncrementalGenerator;
import com.a2a.adjava.generator.core.incremental.NonGeneratedBlocExtractor;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.languages.ios.extractors.IOSVMNamingHelper;
import com.a2a.adjava.languages.ios.xmodele.MIOSDictionnary;
import com.a2a.adjava.languages.ios.xmodele.MIOSMultiXibContainer;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.utils.JaxbUtils;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.XProject;

/**
 * Viewmodel generator
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @author lmichenaud
 * @since MF-Annapurna
 */
public class DelegateInterfacePickerListGenerator extends AbstractIncrementalGenerator<IDomain<IModelDictionary,IModelFactory>> {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(DelegateInterfacePickerListGenerator.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void genere(XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		log.debug("> DelegateInterfacePickerListGenerator.genere");
		Chrono oChrono = new Chrono(true);
		NonGeneratedBlocExtractor oNonGeneratedBlocExtractor = new NonGeneratedBlocExtractor();
		for(MIOSMultiXibContainer oMultiXibContainer : ((MIOSDictionnary)p_oProject.getDomain().getDictionnary()).getAllIOSMultiXibContainers()) {
				this.createDelegatePickerList(oMultiXibContainer, oNonGeneratedBlocExtractor, p_oProject, p_oContext);
		}
		log.debug("< DelegateInterfacePickerListGenerator.genere: {}", oChrono.stopAndDisplay());
	}
	
	/**
	 * Generate implementation of delegate
	 * @param p_oMultiXibContainer multi-xi container
	 * @param p_oNonGeneratedBlocExtractor non-generated bloc extractor
	 * @param p_oMProject project
	 * @param p_mapSession session
	 * @throws Exception exception
	 */
	private void createDelegatePickerList(MIOSMultiXibContainer p_oMultiXibContainer, NonGeneratedBlocExtractor p_oNonGeneratedBlocExtractor,
			XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {

		org.w3c.dom.Document xViewModelDoc = JaxbUtils.marshalToDocument(p_oMultiXibContainer);
		org.dom4j.io.DOMReader reader = new DOMReader();
		org.dom4j.Document document = reader.read(xViewModelDoc);
		
		String sViewModelFile = this.getDelegatePickerListFilename(p_oMultiXibContainer, p_oProject);
		
		log.debug("  generation du fichier: {}", sViewModelFile);
		this.doIncrementalTransform("delegate-pickerlist-interface.xsl", sViewModelFile, document, p_oProject, p_oContext);
	}
	
	/**
	 * Compute viewmodel filename
	 * @param p_oMViewModel viewmodel
	 * @param p_oProject project
	 * @return viewmodel filename
	 */
	protected String getDelegatePickerListFilename( MIOSMultiXibContainer p_oMultiXibContainer, XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject) {
		return FilenameUtils.normalize(FileTypeUtils.computeFilenameForIOSInterface(
				"delegate", IOSVMNamingHelper.getInstance().computeDelegateNameOfPickerList(p_oMultiXibContainer), p_oProject.getSourceDir()));
	}

}
