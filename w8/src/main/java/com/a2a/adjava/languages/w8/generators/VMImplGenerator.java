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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.generator.core.incremental.NonGeneratedBlocExtractor;
import com.a2a.adjava.generator.impl.ViewModelGenerator;
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
 * Viewmodel generator for Win8
 * @author lmichenaud
 *
 */
public class VMImplGenerator extends ViewModelGenerator {
	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(VMImplGenerator.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void genere(XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		log.debug("> ViewModelGenerator.genere");
		Chrono oChrono = new Chrono(true);
		NonGeneratedBlocExtractor oNonGeneratedBlocExtractor = new NonGeneratedBlocExtractor();
		
		for(MViewModelImpl oViewModel : p_oProject.getDomain().getDictionnary().getAllViewModels()) {
			
			if(!oViewModel.isScreenViewModel()) {
				
				this.createViewModel(oViewModel, oNonGeneratedBlocExtractor, p_oProject, p_oContext);
			
			}
		}
		log.debug("< ViewModelGenerator.genere: {}", oChrono.stopAndDisplay());
	}
	
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.generator.impl.ViewModelGenerator#getVMImplFilename(com.a2a.adjava.xmodele.MViewModelImpl, com.a2a.adjava.xmodele.XProject)
	 */
	@Override
	protected String getVMImplFilename(MViewModelImpl p_oMViewModel,
			XProject<IDomain<IModelDictionary, IModelFactory>> p_oProject) {
		return FileTypeUtils.computeFilenameForCSharpImpl(
				"Viewmodel", p_oMViewModel.getName(), p_oProject.getSourceDir());
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.a2a.adjava.generator.impl.ViewModelGenerator#getTemplate(com.a2a.adjava.xmodele.MViewModelImpl)
	 */
	@Override
	protected String getTemplate( MViewModelImpl p_oMViewModel ) {
		String r_sTemplate = "viewmodel-impl.xsl" ;
		if ( p_oMViewModel.getType().equals(ViewModelType.LIST_1)
			 || p_oMViewModel.getType().equals(ViewModelType.LIST_2) 
			 || p_oMViewModel.getType().equals(ViewModelType.LIST_3	)
			 || p_oMViewModel.getType().equals(ViewModelType.FIXED_LIST	)
			 || p_oMViewModel.getType().equals(ViewModelType.LIST_1__ONE_SELECTED )) {
			r_sTemplate = "viewmodel-list-impl.xsl" ;
		}
		return r_sTemplate ;
	}
}
