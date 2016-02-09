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
package com.a2a.adjava.uml2xmodele.extractors.viewmodel;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.commons.init.AdjavaInitializer;
import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml2xmodele.extractors.ScreenExtractor;
import com.a2a.adjava.utils.VersionHandler;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MLinkedInterface;
import com.a2a.adjava.xmodele.MPackage;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.MViewModelInterface;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelTypeConfiguration;

/**
 * Factory for ViewModel creation
 * @author lmichenaud
 *
 */
public class MViewModelFactory {
	
	/** Logger */
	private static final Logger log = LoggerFactory.getLogger(MViewModelFactory.class);
	
	/**
	 * Prefix for naming current item of list viewmodel.
	 */
	public static final String CURRENT_ITEM_OF = "currentItemOf";
	
	/**
	 * Singleton instance
	 */
	private static MViewModelFactory instance = new MViewModelFactory();

	/**
	 * Constructor
	 */
	private MViewModelFactory() {
		//Empty constructor
	}

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	public static MViewModelFactory getInstance() {
		return instance;
	}
	
	/**
	 * Création d'un ViewModel pour un type donné.
	 * @param p_oPackage package for new viewmodel
	 * @param p_sName viewname name
	 * @param p_sUmlName uml name
	 * @param p_oTypeToUpdate entity updated by viewmodel
	 * @param p_oViewModelType viewModel type (master, list1, list2 ou list3)
	 * @param p_sPathToModel indique le chemin vers le modèle
	 * @return le ViewModel
	 * @throws AdjavaException exception
	 */
	public MViewModelImpl createNewViewModel(MPackage p_oPackage, String p_sBaseName, String p_sUmlName,
			MEntityImpl p_oEntityToUpdate, ViewModelType p_oViewModelType, IDomain<IModelDictionary, IModelFactory> p_oDomain, 
			String p_sPathToModel, boolean p_bCustomizable ) throws AdjavaException {
		log.debug("createNewViewModel: {}", p_sBaseName);
		log.debug("  package: {}", p_oPackage.getFullName());
		// create viewmodel
		MViewModelImpl r_oNew = p_oDomain.getXModeleFactory().createViewModel(
				VMNamingHelper.getInstance().computeViewModelImplName(p_sBaseName, false, p_oDomain.getLanguageConf()),
				p_sUmlName, p_oPackage, p_oViewModelType, p_oEntityToUpdate, p_sPathToModel, p_bCustomizable);
		//r_oNew.setCurrentItemKeyName(CURRENT_ITEM_OF + StringUtils.capitalize(p_sUmlName));

		// Add super interface on the view model interface.
		this.createViewModelInterface(p_sBaseName, r_oNew, p_oDomain);
		
		return r_oNew ;
	}
	
	/**
	 * Create viewmodel interface for viewmodel impl
	 * @param p_sBaseName basename for interface
	 * @param p_oViewModelImpl viewmodel impl
	 * @return viewmodel interface
	 */
	private void createViewModelInterface( String p_sBaseName, MViewModelImpl p_oViewModelImpl, IDomain<IModelDictionary, IModelFactory> p_oDomain) throws AdjavaException {
				
		MViewModelInterface r_oMViewModelInterface = new MViewModelInterface(
				VMNamingHelper.getInstance().computeViewModelInterfaceName(p_sBaseName, false, p_oDomain.getLanguageConf()), 
				p_oViewModelImpl.getPackage());
		//l'entité liée va être null quand on va créer un ViewModel de screen
		if (p_oViewModelImpl.getEntityToUpdate()!= null){
			r_oMViewModelInterface.setEntityToUpdate(p_oViewModelImpl.getEntityToUpdate().getMasterInterface());
			
			ViewModelTypeConfiguration oVMTypeConf = p_oViewModelImpl.getType().getParametersByConfigName(p_oViewModelImpl.getConfigName(), VersionHandler.getGenerationVersion().getStringVersion());
			
			if ( oVMTypeConf.getInterfaceName() == null) {
				throw new AdjavaException("No <item-interface> for viewmodel type {} with configuration {}", p_oViewModelImpl.getType().name(), p_oViewModelImpl.getConfigName());
			}
			
			r_oMViewModelInterface.addLinkedInterface(
					new MLinkedInterface(oVMTypeConf.getInterfaceName(), oVMTypeConf.getInterfaceFullName(),
					p_oViewModelImpl.getEntityToUpdate().getMasterInterface().getFullName()));
		}else{
			r_oMViewModelInterface.addLinkedInterface(new MLinkedInterface(
				p_oViewModelImpl.getType().getParametersByConfigName(p_oViewModelImpl.getConfigName(), VersionHandler.getGenerationVersion().getStringVersion()).getInterfaceName(), 
				p_oViewModelImpl.getType().getParametersByConfigName(p_oViewModelImpl.getConfigName(), VersionHandler.getGenerationVersion().getStringVersion()).getInterfaceFullName()));
		}
		p_oViewModelImpl.setMasterInterface(r_oMViewModelInterface);
	}
	
	/**
	 * Méthode qui créé et déclare dans le registre des ViewModels, le <em>MViewModel</em> correspondant à la liste.
	 * @param p_oUmlClass la classe correspondant au ViewModel de la liste
	 * @param p_oMasterVmi le ViewModel correspondant à l'item de plus haut niveau dans la liste
	 * @param p_oPackageBaseViewModel le package du ViewModel de la liste
	 * @param p_oTypeToUpdate le type de l'entité lié à un item de liste
	 */
	public MViewModelImpl createViewModelList(UmlClass p_oUmlClass, MViewModelImpl p_oMasterVmi, MPackage p_oPackageBaseViewModel, 
			MEntityImpl p_oEntityToUpdate, IDomain<IModelDictionary, IModelFactory> p_oDomain){
		
		log.debug("createViewModelList");
				
		String sViewModelImplName = VMNamingHelper.getInstance().computeViewModelImplName(
			StringUtils.capitalize(p_oUmlClass.getName()), true, p_oDomain.getLanguageConf());
		
		log.debug("  name: {}", sViewModelImplName);
		log.debug("  master type: {}", p_oMasterVmi.getType().name());
		
		// define viewmodel type
		ViewModelType oViewModelType = this.computeViewModelType(p_oUmlClass, p_oDomain);
		log.debug("  list type: {}", oViewModelType.name());
				
		// create list viewmodel
		MViewModelImpl r_oListViewModel =  p_oDomain.getXModeleFactory().createViewModel(
				sViewModelImplName, p_oUmlClass.getName(), p_oPackageBaseViewModel, oViewModelType, p_oEntityToUpdate, null, false);
		//r_oListViewModel.setCurrentItemKeyName(CURRENT_ITEM_OF + StringUtils.capitalize(p_oUmlClass.getName()));
		
		// create list viewmodel interface
		MViewModelFactory.getInstance().createListViewModelInterface(r_oListViewModel, p_oMasterVmi, p_oDomain);
		
		//création des getter/setter pour la liste
		ITypeDescription oType = (ITypeDescription) r_oListViewModel.getType().getParametersByConfigName(r_oListViewModel.getConfigName(), VersionHandler.getGenerationVersion().getStringVersion()).getTypeDescription(p_oDomain).clone();
		r_oListViewModel.addSubViewModel(p_oMasterVmi);
		
		//génération de l'import pour la liste
		r_oListViewModel.addImport(oType.getName());
		
		oType.setParameterizedElementType(
				p_oMasterVmi.getEntityToUpdate().getMasterInterface().getAssociatedType(p_oDomain),
				p_oMasterVmi.getMasterInterface().getAssociatedType(p_oDomain));
		
		// add import on viewmodel interface
		r_oListViewModel.getMasterInterface().addImport(p_oMasterVmi.getEntityToUpdate().getMasterInterface().getFullName());
		r_oListViewModel.getMasterInterface().addImport(p_oMasterVmi.getMasterInterface().getFullName());
		
		//enregistrement du viewmodel list
		log.debug("  register viewmodel list : {}", r_oListViewModel.getFullName());
		p_oDomain.getDictionnary().registerViewModel(r_oListViewModel);
		p_oDomain.getDictionnary().registerViewModelInterface(r_oListViewModel.getMasterInterface());
		
		return r_oListViewModel ;
	}
	
	/**
	 * Create listviewmodel interface
	 * @param p_oViewModelImpl viewmodel implementation
	 * @param p_oItemViewModel item viewmodel implementation (item of list)
	 */
	public void createListViewModelInterface( MViewModelImpl p_oViewModelImpl, MViewModelImpl p_oItemViewModel, 
			IDomain<IModelDictionary, IModelFactory> p_oDomain) {
		log.debug("create listviewmodel interface");
		
		String sVMInterface = VMNamingHelper.getInstance().computeViewModelInterfaceName(p_oViewModelImpl.getUmlName(), true, p_oDomain.getLanguageConf());
		
		MViewModelInterface r_oMViewModelInterface = new MViewModelInterface(
				sVMInterface, p_oViewModelImpl.getPackage());
		r_oMViewModelInterface.setEntityToUpdate(p_oViewModelImpl.getEntityToUpdate().getMasterInterface());
		r_oMViewModelInterface.addLinkedInterface(new MLinkedInterface(
				p_oViewModelImpl.getType().getParametersByConfigName(p_oViewModelImpl.getConfigName(), VersionHandler.getGenerationVersion().getStringVersion()).getListName(), 
				p_oViewModelImpl.getType().getParametersByConfigName(p_oViewModelImpl.getConfigName(), VersionHandler.getGenerationVersion().getStringVersion()).getListFullName(),
				p_oViewModelImpl.getEntityToUpdate().getMasterInterface().getFullName(),
				p_oItemViewModel.getMasterInterface().getFullName()));
		r_oMViewModelInterface.addImport(p_oItemViewModel.getMasterInterface().getFullName());
		r_oMViewModelInterface.addImport(p_oItemViewModel.getFullName());
		p_oViewModelImpl.setMasterInterface(r_oMViewModelInterface);
		
		if ( log.isDebugEnabled()) {
			log.debug("  name: {}", sVMInterface.toString());
			log.debug("  master interface: {}", r_oMViewModelInterface.getLinkedInterfaces().get(0).getFullName());
			log.debug("  add import: {}", p_oItemViewModel.getMasterInterface().getFullName());
			log.debug("  add import: {}", r_oMViewModelInterface);
		}
	}
	
	/**
	 * @param p_oUmlClass
	 * @return
	 */
	private ViewModelType computeViewModelType(UmlClass p_oUmlClass, IDomain<IModelDictionary, IModelFactory> p_oDomain) {
		ScreenExtractor oScreenExtractor = p_oDomain.getExtractor(ScreenExtractor.class);
		ViewModelType oViewModelType = ViewModelType.MASTER ;
		if ( p_oUmlClass.hasStereotype(oScreenExtractor.getScreenContext().getPanelList1Stereotype())) {
			oViewModelType = ViewModelType.LIST_1 ;
		}
		else
		if ( p_oUmlClass.hasStereotype(oScreenExtractor.getScreenContext().getPanelList2Stereotype())) {
			oViewModelType = ViewModelType.LIST_2 ;	
		}
		else
		if ( p_oUmlClass.hasStereotype(oScreenExtractor.getScreenContext().getPanelList3Stereotype())) {
			oViewModelType = ViewModelType.LIST_3 ;
		}
		return oViewModelType;
	}
}
