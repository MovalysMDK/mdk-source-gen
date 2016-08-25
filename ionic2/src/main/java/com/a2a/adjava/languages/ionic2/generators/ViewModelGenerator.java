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
package com.a2a.adjava.languages.ionic2.generators;


import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.generator.core.incremental.NonGeneratedBlocExtractor;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.XProject;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;

public class ViewModelGenerator extends com.a2a.adjava.generator.impl.ViewModelGenerator {

	
	private static final String docPath = "webapp/src/app/views/";

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ViewModelGenerator.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void genere(XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		log.debug("> ViewModelGenerator.genere");
		Chrono oChrono = new Chrono(true);
		NonGeneratedBlocExtractor oNonGeneratedBlocExtractor = new NonGeneratedBlocExtractor();
		for(MViewModelImpl oViewModel : p_oProject.getDomain().getDictionnary().getAllViewModels()) {
			if(!oViewModel.isScreenViewModel()){
				this.createViewModel(oViewModel, oNonGeneratedBlocExtractor, p_oProject, p_oContext);
			}
		}
		log.debug("< ViewModelGenerator.genere: {}", oChrono.stopAndDisplay());
	}
	
	
	
	/**
	 * Compute viewmodel filename
	 * @param p_oMViewModel viewmodel
	 * @param p_oProject project
	 * @return viewmodel filename
	 */
	@Override
	protected String getVMImplFilename( MViewModelImpl p_oMViewModel, XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject) {
		
		String docControllerPath = computeDocPath(p_oMViewModel);	
		
		return FileTypeUtils.computeFilenameForJS(docControllerPath, p_oMViewModel.getName());
	}
	
	
	protected String computeDocPath(MViewModelImpl p_oMViewModel)
	{
		String docControllerPath = docPath;
		
		if(ViewModelType.LISTITEM_1.equals(p_oMViewModel.getType()) || 
				ViewModelType.LISTITEM_2.equals(p_oMViewModel.getType()) || 
				ViewModelType.LISTITEM_3.equals(p_oMViewModel.getType()) ||
				ViewModelType.LIST_1__ONE_SELECTED.equals(p_oMViewModel.getType()) ||
				ViewModelType.FIXED_LIST.equals(p_oMViewModel.getType()))
			{
				return computeDocPath(p_oMViewModel.getParent());
			}
			else 
			{
				docControllerPath += p_oMViewModel.getUmlName() + "/vm";
			}
		
		return docControllerPath;
	}
	
	
	/**
	 * Get template
	 * @param p_oMViewModel viewmodel
	 * @return template name
	 */
	@Override
	protected String getTemplate( MViewModelImpl p_oMViewModel ) {
		String r_sTemplate = "viewmodel.xsl" ;
		return r_sTemplate;
	}
	
	@Override
	protected Document createDocument(MViewModelImpl p_oMViewModel,
			XProject<IDomain<IModelDictionary, IModelFactory>> p_oProject) {
		Document r_xViewModelFile = super.createDocument(p_oMViewModel, p_oProject);
		r_xViewModelFile.getRootElement().addElement("viewName").setText(this.getViewModelModule(p_oMViewModel));
		return r_xViewModelFile;
	}

	private String getViewModelModule(MViewModelImpl p_oMViewModel) {
		if ( p_oMViewModel.getFirstParent() == null ) {
			return "view_"+p_oMViewModel.getUmlName();
		}
		
		if ( p_oMViewModel.getParent() !=null ) {
			// TODO get the first panel parent
			if ( p_oMViewModel.getParent().equals(p_oMViewModel.getFirstParent()) && p_oMViewModel.getParent().getFirstParent() == null ) {
				// case VM of panel
				if(p_oMViewModel.getType().equals(ViewModelType.LIST_1__ONE_SELECTED) ||
						p_oMViewModel.getType().equals(ViewModelType.FIXED_LIST)||
						p_oMViewModel.getType().equals(ViewModelType.LISTITEM_1)||
						p_oMViewModel.getType().equals(ViewModelType.LISTITEM_2)||
						p_oMViewModel.getType().equals(ViewModelType.LISTITEM_3))
				{
					return "view_"+p_oMViewModel.getFirstParent().getUmlName();
				}
				return "view_"+p_oMViewModel.getUmlName();
			} else {
				// module is the parent module
				return this.getViewModelModule(p_oMViewModel.getParent());
			}
		}
		return "non_handled_case";
	}
	
}
