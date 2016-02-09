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
package com.a2a.adjava.uml2xmodele.ui.actions;

import static com.a2a.adjava.utils.StrUtils.DOT;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.languages.LanguageConfiguration;
import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MAction;
import com.a2a.adjava.xmodele.MActionInterface;
import com.a2a.adjava.xmodele.MActionType;
import com.a2a.adjava.xmodele.MAdapter;
import com.a2a.adjava.xmodele.MDaoInterface;
import com.a2a.adjava.xmodele.MDialog;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MPackage;
import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.MViewModelImpl;

/**
 * Action factory
 * @author lmichenaud
 *
 */
public class ActionFactory {

	/**
	 * Singleton instance
	 */
	private static ActionFactory instance = new ActionFactory();

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ActionFactory.class);
	
	/**
	 * Constructor
	 */
	private ActionFactory() {
		//Empty constructor
	}

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	public static ActionFactory getInstance() {
		return instance;
	}
	
	/**
	 * Create an action for page
	 * @param p_oDomain domain
	 * @param p_bRoot first action after application initialization
	 * @param p_oPage target page
	 * @param p_oAdapter adapter (can be null)
	 * @param p_sActionName action short name
	 * @param p_oMasterPackage root package from which the action package will be computed.
	 * @param p_oPackageBase
	 * @param p_oType action type
	 * @param p_sViewModelCreatorFullName
	 * @return
	 */
	public MAction addAction(IDomain<IModelDictionary, IModelFactory> p_oDomain, boolean p_bRoot, MPage p_oPage, MAdapter p_oAdapter, 
			String p_sActionBaseName, MPackage p_oMasterPackage, MActionType p_oType, String p_sViewModelCreatorFullName) {
		
		MEntityImpl oEntity = null ;
		MDaoInterface oDao = null ;
		List<MDaoInterface> listExternalDaos = null;
		MViewModelImpl oPageVM = p_oPage.getViewModelImpl();
			
		if ( oPageVM != null ) {
			oEntity = oPageVM.getEntityToUpdate();

			if ( !oEntity.isTransient()) {
				oDao = p_oDomain.getDictionnary().getDaoItfByEntityItf(oPageVM.getEntityToUpdate().getMasterInterface().getFullName());
			}
				
			listExternalDaos = new ArrayList<MDaoInterface>();
			this.computeExternalDaos(p_oDomain, oPageVM, listExternalDaos);
		}
		
		MAction r_oAction = this.createAction(p_sActionBaseName, p_oType, p_oPage.getUmlName(), p_oPage.getViewModelImpl(), 
				p_oMasterPackage, p_oAdapter, p_bRoot, p_sViewModelCreatorFullName, oEntity, oDao, listExternalDaos, p_oDomain);
		return r_oAction ;
	}
	
	/**
	 * Create an action for page
	 * @param p_oDomain domain
	 * @param p_bRoot first action after application initialization
	 * @param p_oPage target page
	 * @param p_oAdapter adapter (can be null)
	 * @param p_sActionName action short name
	 * @param p_oMasterPackage root package from which the action package will be computed.
	 * @param p_oPackageBase
	 * @param p_oType action type
	 * @param p_sViewModelCreatorFullName
	 * @return action
	 */
	public MAction addActionForDialog(IDomain<IModelDictionary, IModelFactory> p_oDomain, 
			boolean p_bRoot, MDialog p_oMDialog, MAdapter p_oAdapter, 
			String p_sActionBaseName, MPackage p_oMasterPackage, MActionType p_oType, String p_sViewModelCreatorFullName) {
		
		MEntityImpl oEntity = p_oMDialog.getViewModelImpl().getEntityToUpdate();
		MDaoInterface oDao = null ;
		List<MDaoInterface> listExternalDaos = null;
		
		if ( !oEntity.isTransient()) {
			oDao = p_oDomain.getDictionnary().getDaoItfByEntityItf(oEntity.getMasterInterface().getFullName());
		}
		
		listExternalDaos = new ArrayList<MDaoInterface>();
		computeExternalDaos(p_oDomain, p_oMDialog.getViewModelImpl(), listExternalDaos);
		
		MAction r_oAction = this.createAction(p_sActionBaseName, p_oType, p_oMDialog.getUmlName(), p_oMDialog.getViewModelImpl(), 
				p_oMasterPackage, p_oAdapter, p_bRoot, p_sViewModelCreatorFullName, oEntity, oDao, listExternalDaos, p_oDomain);
		
		r_oAction.addParameter("dialog", p_oMDialog.getName());
		r_oAction.addImport(p_oMDialog.getFullName());
		
		return r_oAction;
	}

	
	/**
	 * Create action
	 * @param p_sActionBaseName action base name
	 * @param p_oType action type
	 * @param p_sPageUmlName uml name of page
	 * @param p_oPageVM page viewmodel
	 * @param p_oMasterPackage master package
	 * @param p_oAdapter adapter
	 * @param p_bRoot main action launched after application init
	 * @param p_sViewModelCreatorFullName viewmodel creator full name
	 * @param p_oEntity
	 * @param p_oDao
	 * @param p_listExternalDaos
	 * @param p_oDomain domain
	 * @return
	 */
	private MAction createAction( String p_sActionBaseName, MActionType p_oType, 
			String p_sPageUmlName, MViewModelImpl p_oPageVM, MPackage p_oMasterPackage,
			MAdapter p_oAdapter, boolean p_bRoot, String p_sViewModelCreatorFullName,
			MEntityImpl p_oEntity, MDaoInterface p_oDao, List<MDaoInterface> p_listExternalDaos, IDomain<IModelDictionary, IModelFactory> p_oDomain) {
		
		log.debug("create action : {}", p_sActionBaseName );
		log.debug("  action type: {}", p_oType.name());
		log.debug("  page umlname: {}", p_sPageUmlName);
		if ( p_oEntity != null ) {
			log.debug("  entity: {}", p_oEntity.getName());
		}
		if ( p_oDao != null ) {
			log.debug("  dao: {}", p_oDao.getName());
		}
		
		LanguageConfiguration oLangConf = p_oDomain.getLanguageConf();
		
		MPackage oActionPackage = this.computeActionPackage(p_sActionBaseName, p_oMasterPackage, p_oDomain);
		log.debug("  action package: {}", oActionPackage.getFullName());
		
		//génération du nom de l'action
		String sActionShortName = this.computeActionShortName(p_sActionBaseName, oLangConf);
		log.debug("  action short name: {}", sActionShortName);

		//génération du nom de l'interface de l'action
		String sActionInterfaceShortName = this.computeActionInterfaceShortName(p_sActionBaseName, oLangConf);
		log.debug("  action interface short name: {}", sActionInterfaceShortName);
		MActionInterface oActionInterface =  p_oDomain.getXModeleFactory().createActionInterface(sActionInterfaceShortName, p_bRoot, oActionPackage, 
				p_oType.getParamInFullName(), p_oType.getParamOutFullName(), p_oType.getParamStepFullName(),
				p_oType.getParamProgressFullName(), p_oEntity, p_oType);
		
		MAction r_oAction = p_oDomain.getXModeleFactory().createAction(sActionShortName, oActionInterface, p_bRoot, oActionPackage, 
				p_oPageVM, p_oDao, p_listExternalDaos, p_sViewModelCreatorFullName);
		
		if ( p_oAdapter != null && r_oAction.getType().equals(MActionType.COMPUTE)) {
			p_oAdapter.addAction(r_oAction);
		}
		
		p_oDomain.getDictionnary().registerAction(r_oAction);
		p_oDomain.getDictionnary().registerActionInterface(oActionInterface);
		
		return r_oAction ;
	}
	
	
	/**
	 * Compute action short name ( add prefix/suffix to base name)
	 * @param p_sActionName action base name
	 * @param p_oLangConf language configuration
	 * @return
	 */
	public String computeActionShortName(String p_sActionName, LanguageConfiguration p_oLangConf) {
		StringBuilder oActionNameBuilder = new StringBuilder();
		if ( p_oLangConf.getActionImplementationNamingPrefix() != null ) {
			oActionNameBuilder.append(p_oLangConf.getActionImplementationNamingPrefix());
		}
		oActionNameBuilder.append(p_sActionName);
		if ( p_oLangConf.getActionImplementationNamingSuffix() != null ) {
			oActionNameBuilder.append(p_oLangConf.getActionImplementationNamingSuffix());
		}
		return oActionNameBuilder.toString();
	}
	
	/**
	 * Compute action interface short name ( add prefix/suffix to base name)
	 * @param p_sActionName action base name
	 * @param p_oLangConf  language configuration
	 * @return short name for action interface
	 */
	private String computeActionInterfaceShortName(String p_sActionName, LanguageConfiguration p_oLangConf) {
		StringBuilder oActionInterfaceNameBuilder = new StringBuilder();
		if ( p_oLangConf.getActionInterfaceNamingPrefix() != null ) {
			oActionInterfaceNameBuilder.append(p_oLangConf.getActionInterfaceNamingPrefix());
		}
		oActionInterfaceNameBuilder.append(p_sActionName);
		if ( p_oLangConf.getActionInterfaceNamingSuffix() != null ) {
			oActionInterfaceNameBuilder.append(p_oLangConf.getActionInterfaceNamingSuffix());
		}
		return oActionInterfaceNameBuilder.toString();
	}

	/**
	 * @param p_sActionName action short name
	 * @param p_oMasterPackage master package
	 * @param p_oDomain domain
	 * @return
	 */
	public MPackage computeActionPackage(String p_sActionName, MPackage p_oMasterPackage, IDomain<IModelDictionary, IModelFactory> p_oDomain ) {
		
		LanguageConfiguration oLangConf = p_oDomain.getLanguageConf();
		
		// action package is : master package + sub package for actions + action name to lowercase
		StringBuilder oPackageNameActionBuilder = new StringBuilder();
		oPackageNameActionBuilder.append(p_oMasterPackage.getFullName());
		oPackageNameActionBuilder.append(DOT);
		oPackageNameActionBuilder.append(oLangConf.getActionImplementationSubPackageName());
		oPackageNameActionBuilder.append(DOT);
		oPackageNameActionBuilder.append(p_sActionName.toLowerCase());
		
		log.debug("  action name: {}", oPackageNameActionBuilder.toString());

		MPackage r_oActionPackage = p_oDomain.getDictionnary().getPackage(oPackageNameActionBuilder.toString());
		if (r_oActionPackage==null) {

			StringBuilder oPackageNameBuilder = new StringBuilder();
			oPackageNameBuilder.append(oLangConf.getActionImplementationSubPackageName());
			oPackageNameBuilder.append(DOT);
			oPackageNameBuilder.append(p_sActionName.toLowerCase());
			
			r_oActionPackage = new MPackage(oPackageNameBuilder.toString(), p_oMasterPackage);
			p_oDomain.getDictionnary().registerPackage(r_oActionPackage);
		}
		return r_oActionPackage;
	}
	
	/**
	 * @param p_oDomain
	 * @param p_oViewModel
	 * @param p_listExternalDaos
	 */
	private void computeExternalDaos(IDomain<IModelDictionary, IModelFactory> p_oDomain, MViewModelImpl p_oViewModel, List<MDaoInterface> p_listExternalDaos) {
		
		for (MViewModelImpl oExternalVM : p_oViewModel.getExternalViewModels()) {
			MDaoInterface oMDaoInterface = p_oDomain.getDictionnary().getDaoItfByEntityItf(oExternalVM.getEntityToUpdate().getMasterInterface().getFullName());
			if (oMDaoInterface != null) {
				// cas des entités transientes
				p_listExternalDaos.add(oMDaoInterface);
			}
		}

		for (MViewModelImpl oSubView : p_oViewModel.getSubViewModels()) {
			computeExternalDaos(p_oDomain, oSubView, p_listExternalDaos);
		}
	}
	
	
//	/**
//	 * Treatement for action detail
//	 * @param p_oDomain domain
//	 * @param p_sPageUmlName page uml name
//	 * @param p_oAdapter adapter
//	 * @param p_oMasterPackage master package
//	 * @param oLangConf language configuration
//	 * @param r_oAction
//	 */
//	private void doTreatmentForActionDetail(MAction r_oAction, String p_sPageUmlName, MAdapter p_oAdapter,
//			MPackage p_oMasterPackage, IDomain<IModelDictionary, IModelFactory> p_oDomain) {
//		
//		LanguageConfiguration oLangConf = p_oDomain.getLanguageConf();
//		
//		p_oAdapter.addImport(r_oAction.getMasterInterface().getFullName());
//		
//		// l'action de sauvegarde liée doit provoquer un refresh du view model lié
//		addViewModelToLinkedAction( ActionConstants.PREFIX_ACTION_SAVE, 
//			p_oDomain ,p_oMasterPackage, oLangConf , p_oAdapter , p_sPageUmlName );
//		addViewModelToLinkedAction( ActionConstants.PREFIX_ACTION_DELETE, 
//			p_oDomain ,p_oMasterPackage, oLangConf , p_oAdapter , p_sPageUmlName ); 
//	}
		
	/**
	 * Add the view model linked to the action named with the prefix with an adapter
	 * @param p_sActionPrefix action linked to display for modification 	 
	 * @param p_oDomain
	 * @param p_oPackageBase
	 * @param p_oLangConf
	 * @param p_oViewModel view model to add to the action prefixed
	 * @param p_oPage
	 */
	public void addViewModelToLinkedAction(String p_sActionPrefix , IDomain<IModelDictionary, IModelFactory> p_oDomain, MPackage p_oPackageBase , 
			LanguageConfiguration p_oLangConf , MViewModelImpl p_oViewModel , String p_sPageUmlName )  {
		// l'action de sauvegarde liée doit provoquer un refresh du view model lié
		StringBuilder oSaveActionNameBuilder = new StringBuilder();
		oSaveActionNameBuilder.append(p_oPackageBase.getFullName()).append(DOT);
		oSaveActionNameBuilder.append(p_oLangConf.getActionImplementationSubPackageName()).append(DOT);
		oSaveActionNameBuilder.append(p_sActionPrefix.toLowerCase()).append(p_sPageUmlName.toLowerCase()).append(DOT);
		oSaveActionNameBuilder.append(p_oLangConf.getActionImplementationNamingPrefix());
		oSaveActionNameBuilder.append(p_sActionPrefix).append(p_sPageUmlName);
		oSaveActionNameBuilder.append(p_oLangConf.getActionImplementationNamingSuffix());
		
		MAction oSaveAction = p_oDomain.getDictionnary().getAction(oSaveActionNameBuilder.toString()) ;
		if (oSaveAction != null ) {
			oSaveAction.addLinkedViewModel(p_oViewModel);
		}
	}
	/**
	 * Add the view model linked to the action named with the prefix with an adapter of a list 
	 * @param p_sActionPrefix action linked to display for modification 
	 * @param p_oDomain
	 * @param p_oPackageBase 
	 * @param oLangConf
	 * @param p_oAdapter not null adapter for the display action : it give the view model linked
	 * @param p_oPage
	 */
	public void addViewModelToLinkedAction(String p_sActionPrefix , IDomain<IModelDictionary, IModelFactory> p_oDomain, MPackage p_oPackageBase , 
			LanguageConfiguration p_oLangConf , MAdapter p_oAdapter , String p_sPageUmlName )  {
		MViewModelImpl oViewModel = null ;
		switch (p_oAdapter.getViewModel().getType()) {
			case LIST_3:
			oViewModel = p_oAdapter.getViewModel().getSubViewModels().get(0);
			oViewModel = oViewModel.getSubViewModels().get(0);
				break;
			case LIST_2:
			oViewModel = p_oAdapter.getViewModel().getSubViewModels().get(0);
				break;
			case LIST_1:
				oViewModel = p_oAdapter.getViewModel();
				break;
		}
		if ( oViewModel != null ){
			addViewModelToLinkedAction(p_sActionPrefix, p_oDomain, p_oPackageBase, p_oLangConf, oViewModel, p_sPageUmlName );
		}
	}
}
