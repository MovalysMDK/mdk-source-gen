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

import com.a2a.adjava.generator.core.incremental.NonGeneratedBlocExtractor;
import com.a2a.adjava.generators.DomainGeneratorContext;
import com.a2a.adjava.languages.html5.xmodele.MH5ImportDelegate;
import com.a2a.adjava.languages.html5.xmodele.MH5ModeleFactory;
import com.a2a.adjava.utils.Chrono;
import com.a2a.adjava.utils.FileTypeUtils;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MAttribute;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.XProject;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;

public class ViewModelFactoryGenerator extends com.a2a.adjava.generator.impl.ViewModelGenerator {

	
	private static final String docPath = "webapp/src/app/views/";
	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ViewModelFactoryGenerator.class);
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void genere(XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject, DomainGeneratorContext p_oContext) throws Exception {
		log.debug("> ViewModelFactoryGenerator.genere");
		Chrono oChrono = new Chrono(true);
		NonGeneratedBlocExtractor oNonGeneratedBlocExtractor = new NonGeneratedBlocExtractor();
		for(MViewModelImpl oViewModel : p_oProject.getDomain().getDictionnary().getAllViewModels()) {
			if(!oViewModel.isScreenViewModel()){
				this.createViewModel(oViewModel, oNonGeneratedBlocExtractor, p_oProject, p_oContext);
			}
		}
		log.debug("< ViewModelFactoryGenerator.genere: {}", oChrono.stopAndDisplay());
	}
	
	
	/**
	 * Compute viewmodelFactory filename
	 * @param p_oMViewModel viewmodel
	 * @param p_oProject project
	 * @return viewmodelFactory filename
	 */
	@Override
	protected String getVMImplFilename( MViewModelImpl p_oMViewModel, XProject<IDomain<IModelDictionary,IModelFactory>> p_oProject) {
		
		String docControllerPath = computeDocPath(p_oMViewModel);
			
		return FileTypeUtils.computeFilenameForJS(docControllerPath, p_oMViewModel.getName()+"Factory");
	}
	
	//isScreenViewModel
	
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
		String r_sTemplate = "viewmodel-factory.xsl" ;
		return r_sTemplate;
	}
	
	@Override
	protected Document createDocument(MViewModelImpl p_oMViewModel,
			XProject<IDomain<IModelDictionary, IModelFactory>> p_oProject){
		
		Element r_xViewModelElement = p_oMViewModel.toXml();
		r_xViewModelElement.addElement("master-package").setText(p_oProject.getDomain().getRootPackage());
		
		MH5ModeleFactory oModeleFactory = (MH5ModeleFactory) p_oProject.getDomain().getXModeleFactory();
		
		MH5ImportDelegate oMH5ImportDelegate = oModeleFactory.createImportDelegate(this);
		this.computeImportForViewModelFactory(oMH5ImportDelegate, p_oMViewModel, p_oProject);
		r_xViewModelElement.add(oMH5ImportDelegate.toXml());
		
		Document r_xViewModelFile = DocumentHelper.createDocument(r_xViewModelElement);
		
		r_xViewModelFile.getRootElement().addElement("viewName").setText(this.getViewModelModule(p_oMViewModel));
		r_xViewModelFile.getRootElement().addElement("nameFactory").setText(p_oMViewModel.getName()+"Factory");
		r_xViewModelFile.getRootElement().addElement("is-factory").setText("true");

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
	
	
	/**
	 * Compute imports for view model factory implementation
	 * @param p_oMDataLoader dataloader
	 * @param p_oMProject project
	 * @param p_oContext context
	 * @throws Exception
	 */
	protected void computeImportForViewModelFactory( MH5ImportDelegate p_oMH5ImportDelegate,
			MViewModelImpl p_oMViewModel, XProject<IDomain<IModelDictionary, IModelFactory>> p_oMProject) {
		
		
		p_oMH5ImportDelegate.addImport(p_oMViewModel.getName());

		if(!p_oMViewModel.isScreenViewModel() && p_oMViewModel.getType().isList()){
			//<!-- 	For a list VM Factory  -->
			for(MViewModelImpl subVm :p_oMViewModel.getSubViewModels()){
				p_oMH5ImportDelegate.addImport(subVm.getName()+"Factory");
				if ( subVm.getEntityToUpdate() != null ) {
					p_oMH5ImportDelegate.addImport(subVm.getEntityToUpdate().getFactory().getName());
				}
			}
		}
		else{
			//<!-- Panel VM Factory  -->
			if(p_oMViewModel.getEntityToUpdate()!=null)	{
				p_oMH5ImportDelegate.addImport(p_oMViewModel.getEntityToUpdate().getFactoryInterface().getName());
			}

			// Imports for fixed list
			for(MViewModelImpl subVm :p_oMViewModel.getSubViewModels()){
				p_oMH5ImportDelegate.addImport(subVm.getName()+"Factory");
				if (subVm.getEntityToUpdate() != null) {
					p_oMH5ImportDelegate.addImport(subVm.getEntityToUpdate().getFactory().getName());
				}
				
				// for combos inside fixed list
				computeImportsForCombo(subVm, p_oMH5ImportDelegate);
			}

		}
		
		// Enum Imports
		boolean hasEnum = false;
		boolean hasEnumImage = false;

		for(MAttribute attr : p_oMViewModel.getAttributes()){
			if(!attr.isTransient() && attr.isEnum()){
				p_oMH5ImportDelegate.addImport(attr.getTypeDesc().getShortName());
				if(attr.getTypeDesc().getName().contains("enumimage")){
					hasEnumImage = true;
				}
				else{
					hasEnum = true;
				}
			}
		}		
		if(hasEnum){
			p_oMH5ImportDelegate.addImport("MFRadioVMFactory");
		}
		if(hasEnumImage){
			p_oMH5ImportDelegate.addImport("MFValueImageVMFactory");
			p_oMH5ImportDelegate.addImport("MFPictureTypeEnum");
		}
					
		//ComboBox imports
		computeImportsForCombo(p_oMViewModel, p_oMH5ImportDelegate);
	}


	
	private void computeImportsForCombo( MViewModelImpl p_oViewModel, MH5ImportDelegate p_oMH5ImportDelegate ) {
		if(!p_oViewModel.getExternalViewModels().isEmpty()){
			p_oMH5ImportDelegate.addImport("MFIntegerConverter");
			p_oMH5ImportDelegate.addImport("MFComboVMFactory");
			for(MViewModelImpl extVm : p_oViewModel.getExternalViewModels())
			{
				p_oMH5ImportDelegate.addImport(extVm.getName()+"Factory");
			}
		}
		boolean hasAddressLocation = false;
		boolean hasPhoto = false;

		for(MAttribute oAttr : p_oViewModel.getAttributes()){
			if(oAttr.getTypeDesc().getShortName().equals("MFPositionViewModel")){
				hasAddressLocation = true;
			}
			if(oAttr.getTypeDesc().getShortName().equals("MFPhotoViewModel")){
				hasPhoto = true;
			}
			if(oAttr.isEnum()){
//				if(oAttr.getTypeDesc().getDefaultUiType().equals("RadioGroup")){
					p_oMH5ImportDelegate.addImport(oAttr.getTypeDesc().getShortName()+"Converter");
//				}
			}
		}
		if(hasAddressLocation)
		{
			p_oMH5ImportDelegate.addImport("MFAddressLocationFactory");
			p_oMH5ImportDelegate.addImport("MFAddressLocationVMFactory");
		}
		if(hasPhoto)
		{
			p_oMH5ImportDelegate.addImport("MFPhotoFactory");
			p_oMH5ImportDelegate.addImport("MFPhotoVMFactory");

		}
	}
	
}
