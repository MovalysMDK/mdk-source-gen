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
package com.a2a.adjava.languages.w8.generators;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.generator.impl.ViewModelInterfaceGenerator;
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
 * Viewmodel interface generator
 * @author lmichenaud
 *
 */
public class VMInterfaceGenerator extends ViewModelInterfaceGenerator {
	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(VMInterfaceGenerator.class);
	
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
	 * {@inheritDoc}
	 * @see com.a2a.adjava.generator.impl.ViewModelInterfaceGenerator#getVMInterfaceFilename(com.a2a.adjava.xmodele.MViewModelImpl, com.a2a.adjava.xmodele.XProject)
	 */
	@Override
	protected String getVMInterfaceFilename(MViewModelImpl p_oVMImpl,
			XProject<IDomain<IModelDictionary, IModelFactory>> p_oProject) {
		return FilenameUtils.normalize(FileTypeUtils.computeFilenameForCSharpImpl(
				"Viewmodel/Interfaces", p_oVMImpl.getMasterInterface().getName(), p_oProject.getSourceDir()));
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.generator.impl.ViewModelInterfaceGenerator#getTemplate(com.a2a.adjava.xmodele.MViewModelInterface)
	 */
	@Override
	protected String getTemplate(MViewModelImpl p_oMViewModel) {
		String r_sTemplate = "viewmodel-interface.xsl" ;
		if ( p_oMViewModel.getType().equals(ViewModelType.LIST_1)
				 || p_oMViewModel.getType().equals(ViewModelType.LIST_2)
				 || p_oMViewModel.getType().equals(ViewModelType.LIST_3	) 
				 || p_oMViewModel.getType().equals(ViewModelType.FIXED_LIST	) 
				 || p_oMViewModel.getType().equals(ViewModelType.LIST_1__ONE_SELECTED )) {
			r_sTemplate = "viewmodel-list-interface.xsl" ;
		}
		return r_sTemplate ;
	}
}
