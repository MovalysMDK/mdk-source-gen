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
package com.a2a.adjava.uml2xmodele.extractors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.uml.UmlUsage;
import com.a2a.adjava.uml2xmodele.extractors.viewmodel.MViewModelFactory;
import com.a2a.adjava.uml2xmodele.extractors.viewmodel.VMNamingHelper;
import com.a2a.adjava.uml2xmodele.extractors.viewmodel.VMPathAttributeAnalyser;
import com.a2a.adjava.utils.VersionHandler;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MLinkedInterface;
import com.a2a.adjava.xmodele.MPackage;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelTypeConfiguration;

/**
 * <p>Classe de type Extractor permettant de récupérer dans le flux xml, les données correspondant aux différents <em>ViewModel</em>.</p>
 *
 * <p>Copyright (c) 2011</p>
 * <p>Company: Adeuza</p>
 *
 * @author smaitre
 * @since MF-Annapurna
 */
public class ViewModelExtractor extends AbstractExtractor<IDomain<IModelDictionary,IModelFactory>> {
	
	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ViewModelExtractor.class);
	
	/**
	 * Stereotype list indicating that a UmlClass is a view model of the domain.
	 */
	private List<String> listStereotypes = new ArrayList<String>();
	
	/**
	 * Stereotype list indicating the link between viewmodel and model.
	 */
	private List<String> listStereotypesUsageViewModel2Model = new ArrayList<String>();

	/**
	 * Stereotype list indicating that a Viewmodel is customizable
	 */
	private List<String> customizableStereotypes;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize(Element p_xConfig) throws Exception {
		this.listStereotypes = getParameters().getValues("stereotypes");
		this.listStereotypesUsageViewModel2Model = getParameters().getValues("stereotypes-usage-viewmodel2model");
		this.customizableStereotypes = getParameters().getValues("customizable-stereotypes");
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void extract(UmlModel p_oModele) throws Exception {
			
		// on boucle sur l'ensemble des classes du modèle et on cherche les classes de views
		// une description de viewmodels a un stéréotype défini dans la configuration
		for (UmlClass oUmlClass : p_oModele.getDictionnary().getAllClasses()) {
			if (isViewModel(oUmlClass)) {
				treatViewModelUmlClass(oUmlClass, p_oModele.getDictionnary());
			}
		}
	}
	

	/**
	 * Treat uml class of type "view model" 
	 * @param p_oVMUmlClass uml class
	 * @param p_oUmlDictionnary dictionnary
	 */
	private void treatViewModelUmlClass( UmlClass p_oVMUmlClass, UmlDictionary p_oUmlDictionnary ) throws Exception {
		
		ScreenExtractor oScreenExtractor = getDomain().getExtractor(ScreenExtractor.class);
		
		log.debug("conversion de la classe (pour génération de view model) {}", p_oVMUmlClass.getFullName());
		
		//analyse des données
		// un viewmodel dans l'uml doit obligatoirement pointer vers un et un seul élément du modèle
		// le pointage viewmodel -> model se fait par un usage portant un stéréotype particulier
		// il doit y avoir un seul usage possible

		List<UmlUsage> listModelUsages = this.getViewModel2ModelUsages(p_oVMUmlClass);
		if (listModelUsages.size() > 1) {
			MessageHandler.getInstance().addError("{} must have only one usage of type Mm_panel",
				p_oVMUmlClass.getName());
		}
		else {
			
			//un seul usage vers la classe de modèle, on peut commencer le traitement
			//on commence par récupérer la classe entité associée
			// l'entité associé par l'usage au view model
			MEntityImpl oAssociatedEntity = null ;
			String sModelUsageName = null ;
			if ( listModelUsages.size() == 1 ) {			
				oAssociatedEntity = this.getDomain().getDictionnary().getMapUmlClassToMClasses().get(
						listModelUsages.get(0).getSupplier().getFullName());
				log.debug("associated entity full name: {}", listModelUsages.get(0).getSupplier().getFullName());
				log.debug("associated entity : {}/{}", listModelUsages.get(0).getSupplier().getName(), oAssociatedEntity);
							
				controlStereotypeFromModelClassLinkedToPanel(listModelUsages.get(0).getClient(),listModelUsages.get(0).getSupplier());
				sModelUsageName = listModelUsages.get(0).getName();
			}
			
			//	calcul du nom du package du view model
			MPackage oPackageBaseViewModel = VMNamingHelper.getInstance().computeViewModelImplPackage(p_oVMUmlClass, this.getDomain());
			
			//Création du view model associée
			//attention ce view model peut être modifier lors de l'extraction des écrans voir ScreenExtractor
			MViewModelImpl oMasterVmi = MViewModelFactory.getInstance().createNewViewModel(oPackageBaseViewModel, p_oVMUmlClass.getName(),
					p_oVMUmlClass.getName(), oAssociatedEntity, ViewModelType.MASTER, getDomain(), sModelUsageName,
					p_oVMUmlClass.hasAnyStereotype(this.customizableStereotypes));

			oMasterVmi.setDocumentation(p_oVMUmlClass.getDocumentation());
			// Si view model de liste
			if ( this.isListViewModel(p_oVMUmlClass, oScreenExtractor )) {
				
				log.debug("Panel '{}' needs a view model list", p_oVMUmlClass.getName());
				// création + déclaration dans le registre du ViewModel de liste
				MViewModelImpl oListVM = MViewModelFactory.getInstance().createViewModelList(p_oVMUmlClass, oMasterVmi, oPackageBaseViewModel, oAssociatedEntity, getDomain());
				this.updateMasterViewModelForList(oMasterVmi, oListVM);
			}
			else {
				log.debug("Panel '{}' doesnot need a view model list", p_oVMUmlClass.getName());
			}

			//la création d'un view model peut engendrer la création de sous view model lors de lecture des attributs
			Map<String, MViewModelImpl> otherViewModels = new TreeMap<String, MViewModelImpl>(); 
			
			// Treat view model attributes
			VMPathAttributeAnalyser.getInstance().treatViewModelAttributes(
					oMasterVmi, p_oVMUmlClass, oAssociatedEntity, p_oUmlDictionnary, otherViewModels, getDomain());
			
			// Register view model
			log.debug("register view model {}/{} attributes", oMasterVmi.getName(), oMasterVmi.getAttributes().size());
			this.getDomain().getDictionnary().registerViewModel(oMasterVmi);
			this.getDomain().getDictionnary().registerViewModelInterface(oMasterVmi.getMasterInterface());
			
			//enregistrement des autres views models
			this.registerSubViewModel(oMasterVmi, oMasterVmi.getName());
		}
	}

	/**
	 * Read view model to model usages
	 * @param p_oVMUmlClass
	 * @return
	 */
	private List<UmlUsage> getViewModel2ModelUsages(UmlClass p_oVMUmlClass) {
		List<UmlUsage> lstUsage = new ArrayList<UmlUsage>();// la liste des usages associées à une classe de view model
		for(UmlUsage oUsage  : p_oVMUmlClass.getUsages()) {
			if (oUsage.hasAnyStereotype(this.listStereotypesUsageViewModel2Model)) {
				lstUsage.add(oUsage);
			}
			else
			{
				isThereMissingStereotypeOnUsageForModel(oUsage);
			}
		}
		return lstUsage;
	}
	
	/**
	 * Control that a model class linked to a panel correctly possess a Mm_model stereotype.
	 * @param umlClass
	 * @param umlClass2
	 */
	private void controlStereotypeFromModelClassLinkedToPanel( UmlClass umlClass,UmlClass umlClass2 )
	{
		
		PojoExtractor oPojoExtractor = getDomain().getExtractor(PojoExtractor.class);
		List<String> p_PojoStereotype =  oPojoExtractor.getPojoContext().getEntityStereotypes();
		if( null !=umlClass && null !=umlClass2 &&
			!(umlClass.hasAnyStereotype( p_PojoStereotype) ||umlClass2.hasAnyStereotype(p_PojoStereotype)))
		{
			MessageHandler.getInstance().addError("Either \"{}\" et \"{}\" must have a stereotype in \"{}\"", umlClass.getName() ,umlClass2.getName(), p_PojoStereotype  );
		}
	}
	
	/**
	 * Throws an exception if a Panel and a Model are linked by a not stereotyped aggregation : 
	 * it means a critical aggregation has been forgotten
	 * @param umlUsage
	 */
	private void isThereMissingStereotypeOnUsageForModel(UmlUsage umlUsage)
	{
		if(!umlUsage.getClient().getStereotypes().isEmpty() &&
		   !umlUsage.getSupplier().getStereotypes().isEmpty() &&
		   umlUsage.getStereotypes().isEmpty())
		{
			MessageHandler.getInstance().addError(
				"If the panel '{}' and the model '{}' each have a stereotype,"
				+ " the aggregation '{}' between them must also have a stereotype",						
				umlUsage.getClient().getName(), umlUsage.getSupplier().getName(), umlUsage.getName());
		}
	}
	
	
	/**
	 * Return true if umlclass is a list viewmodel.
	 * @param oUmlClass
	 * @param oScreenExtractor
	 * @return
	 */
	private boolean isListViewModel(UmlClass oUmlClass, ScreenExtractor oScreenExtractor ) {
		return oUmlClass.hasAnyStereotype(	oScreenExtractor.getScreenContext().getPanelList1Stereotype(),
										oScreenExtractor.getScreenContext().getPanelList2Stereotype(),
										oScreenExtractor.getScreenContext().getPanelList3Stereotype());
	}
				
	/**
	 * Update MasterViewModel so it can be used as item of list
	 * @param p_oMViewModelImpl master viewmodel
	 * @param p_oMListViewModel list viewmodel
	 */
	private void updateMasterViewModelForList( MViewModelImpl p_oMViewModelImpl, MViewModelImpl p_oMListViewModel ) {
			
		p_oMViewModelImpl.getMasterInterface().getLinkedInterfaces().clear();
		
		ViewModelTypeConfiguration oVMTypeConf = p_oMListViewModel.getType().getParametersByConfigName(p_oMViewModelImpl.getConfigName(), VersionHandler.getGenerationVersion().getStringVersion());
		
		p_oMViewModelImpl.getMasterInterface().addLinkedInterface(
				new MLinkedInterface(
						oVMTypeConf.getInterfaceName(), 
						oVMTypeConf.getInterfaceFullName(),
						p_oMListViewModel.getEntityToUpdate().getMasterInterface().getFullName()));
		p_oMViewModelImpl.setType(p_oMListViewModel.getType().getSubType());
	}
	
	/**
	 * @param p_oViewModel
	 * @param p_sPath
	 */
	private void registerSubViewModel(MViewModelImpl p_oViewModel, String p_sPath) {
		// le path sert en cas de doublon
		MViewModelImpl ex = null;
		log.debug("register sub view model of {}", p_oViewModel.getName());
		for(MViewModelImpl oVmi : p_oViewModel.getSubViewModels()) {
			log.debug("try to register : {}/{}/{}", new Object[] { oVmi.getName(), oVmi.getType(), oVmi.getConfigName()});
			for(String sImport : oVmi.getType().getParametersByConfigName(oVmi.getConfigName(), VersionHandler.getGenerationVersion().getStringVersion()).getImports()) {
				log.debug("registerSubViewModel add import {}", sImport);
				p_oViewModel.addImport(sImport);
			}
			ex = this.getDomain().getDictionnary().getViewModel(oVmi.getFullName());
			if (ex!=null) {
				//il existe déjà un view model avec le même nom
				if (ex.isSame(oVmi)) {
					//on ajoute le chemin dans le nom du view model
					oVmi.setName(p_sPath + oVmi.getName().substring(2));
				}
			}
			this.getDomain().getDictionnary().registerViewModel(oVmi);
			this.getDomain().getDictionnary().registerViewModelInterface(oVmi.getMasterInterface());
			log.debug("registerSubViewModel itf {}/{}/{} ", new Object[] { oVmi.getMasterInterface().getName(), 
					oVmi.getMasterInterface().getSubVM(), oVmi.getAttributes().size() });
			this.registerSubViewModel(oVmi, p_sPath + p_sPath.substring(2));
		}

		for (MViewModelImpl oVmi : p_oViewModel.getExternalViewModels()) {
			log.debug("try to register : {}/{}", oVmi.getName(), oVmi.getType());
			for(String sImport : oVmi.getType().getParametersByConfigName(oVmi.getConfigName(), VersionHandler.getGenerationVersion().getStringVersion()).getImports()) {
				log.debug("registerSubViewModel add import {}", sImport);
				p_oViewModel.addImport(sImport);
			}
			ex = this.getDomain().getDictionnary().getViewModel(oVmi.getFullName());
			if (ex!=null) {
				//il existe déjà un view model avec le même nom
				if (ex.isSame(oVmi)) {
					//on ajoute le chemin dans le nom du view model
					oVmi.setName(p_sPath + oVmi.getName().substring(2));
				}
			}
			this.getDomain().getDictionnary().registerViewModel(oVmi);
			this.getDomain().getDictionnary().registerViewModelInterface(oVmi.getMasterInterface());
		}
	}
	
	/**
	 * @return
	 */
	public List<String> getListStereotypes() {
		return this.listStereotypes;
	}

	/**
	 * @return
	 */
	public List<String> getListStereotypesUsageViewModel2Model() {
		return this.listStereotypesUsageViewModel2Model;
	}

	/**
	 * Return true if uml class is of type ViewModel for the domain
	 * 
	 * @param p_oUmlClass UmlClass
	 * @return true if uml class is of type Entity
	 */
	private boolean isViewModel(UmlClass p_oUmlClass) {
		log.debug(" sterotypes : {}", this.listStereotypes);
		log.debug(" sterotypes umlclass : {}", p_oUmlClass.getStereotypeNames());
		return p_oUmlClass.hasAnyStereotype(this.listStereotypes);
	}
}
