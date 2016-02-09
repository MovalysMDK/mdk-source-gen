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
package com.a2a.adjava.languages.w8.extractors;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.a2a.adjava.uml.UmlModel;
import com.a2a.adjava.uml2xmodele.extractors.AbstractExtractor;
import com.a2a.adjava.uml2xmodele.extractors.viewmodel.VMNamingHelper;
import com.a2a.adjava.utils.VersionHandler;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MLinkedInterface;
import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.MViewModelInterface;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelTypeConfiguration;

/**
 * Viewmodel processor class<BR>
 * Creates sub view models for Fixedlists and spinners
 */
public class W8ViewModelProcessor extends AbstractExtractor<IDomain<IModelDictionary,IModelFactory>> {

	
	@Override
	public void initialize(Element p_xConfig) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void extract(UmlModel p_oModele) throws Exception {
		List<MViewModelImpl> oListTmp = new ArrayList<MViewModelImpl>();
		for(MViewModelImpl oViewModel : this.getDomain().getDictionnary().getAllViewModels()) {

			if(oViewModel.getType().equals(ViewModelType.FIXED_LIST)){
				WeakReference<MPage> oPage = oViewModel.getPage();
				
				MViewModelImpl oViewModelTmp = oViewModel.getParent();
				
				while(oPage == null && oViewModelTmp != null){
					oPage = oViewModelTmp.getPage();
					oViewModelTmp = oViewModelTmp.getParent();
				}

				if(oPage != null){
				MViewModelImpl oViewModelItemToCreate = oViewModel.clone(this.getDomain().getXModeleFactory());
				oViewModelItemToCreate.setConfigName(oViewModel.getConfigName());
				oViewModelItemToCreate.setMasterInterface( new MViewModelInterface( oViewModel.getMasterInterface().getName() , oViewModel.getPackage()));
				
				this.renameViewModel(oViewModelItemToCreate, 
						oViewModel.getPackage().getFullName() + "." + VMNamingHelper.getInstance().computeViewModelImplName(
								W8VMNamingHelper.getInstance().computeViewModelNameOfFixedListItem(oPage.get(), oViewModelItemToCreate), false, this.getDomain().getLanguageConf()), 
						oViewModel.getPackage().getFullName() + "." + VMNamingHelper.getInstance().computeViewModelInterfaceName(
								W8VMNamingHelper.getInstance().computeViewModelNameOfFixedListItem(oPage.get(), oViewModelItemToCreate), false, this.getDomain().getLanguageConf()));
				this.modifyTypeOfItemViewModelFixedListItem(oViewModelItemToCreate) ;
//				oViewModelItemToCreate.addImport(oViewModelItemToCreate.getName()) ;
				oViewModel.addSubViewModel(oViewModelItemToCreate);
				oListTmp.add(oViewModelItemToCreate);
				}
			} else if(oViewModel.getType().equals(ViewModelType.LIST_1__ONE_SELECTED)){
				WeakReference<MPage> oPage = oViewModel.getPage();
				
				MViewModelImpl oViewModelTmp = oViewModel.getParent();
				
				while(oPage == null && oViewModelTmp != null){
					oPage = oViewModelTmp.getPage();
					oViewModelTmp = oViewModelTmp.getParent();
				}

				if(oPage != null){
					MViewModelImpl oViewModelItemToCreate = oViewModel.clone(this.getDomain().getXModeleFactory());
					oViewModelItemToCreate.setMasterInterface( new MViewModelInterface( oViewModel.getMasterInterface().getName() , oViewModel.getPackage()));

					this.renameViewModel(oViewModelItemToCreate, 
							oViewModel.getPackage().getFullName() + "." + VMNamingHelper.getInstance().computeViewModelImplName(
									W8VMNamingHelper.getInstance().computeItemViewModelNameOfCombo(oPage.get(), oViewModelItemToCreate), false, this.getDomain().getLanguageConf()), 
							oViewModel.getPackage().getFullName() + "." + VMNamingHelper.getInstance().computeViewModelInterfaceName(
									W8VMNamingHelper.getInstance().computeItemViewModelNameOfCombo(oPage.get(), oViewModelItemToCreate), false, this.getDomain().getLanguageConf()));
					this.modifyTypeOfItemViewModelComboItem(oViewModelItemToCreate) ;
					//oViewModelItemToCreate.addImport(oViewModelItemToCreate.getName()) ;
					 
					// Update the type of the list viewmodel of the combo 
					ViewModelTypeConfiguration oClonedVMTypeConf= ViewModelType.LIST_1__ONE_SELECTED.getVMTypeOptionMap().get(oViewModel.getConfigName()).clone();	
					oClonedVMTypeConf.setInterfaceName(oViewModelItemToCreate.getMasterInterface().getName());
					oClonedVMTypeConf.setInterfaceFullName(oViewModelItemToCreate.getMasterInterface().getFullName());
					oClonedVMTypeConf.setImplementationName(oViewModelItemToCreate.getName());
					ViewModelType.LIST_1__ONE_SELECTED.getVMTypeOptionMap().put(oViewModel.getName(), oClonedVMTypeConf);
					oViewModel.setConfigName(oViewModel.getName());
					
					// Linked interface is wrong, should be the list, we fix it here
					oViewModel.getMasterInterface().getLinkedInterfaces().remove(0);
					MLinkedInterface oNewLinkedInterface = new MLinkedInterface(oClonedVMTypeConf.getListName(), oClonedVMTypeConf.getListFullName(),
					oViewModel.getEntityToUpdate().getMasterInterface().getFullName());
					oViewModel.getMasterInterface().addLinkedInterface(oNewLinkedInterface);
					oViewModel.resetAttributes();
					oViewModel.resetMapping();
					
					//oViewModel.addSubViewModel(oViewModelItemToCreate);
					oListTmp.add(oViewModelItemToCreate);
					// Register the item viewmodel of the combo
					//this.getDomain().getDictionnary().registerViewModel(oViewModelItemToCreate);
				}
			}
		}
		
		for(MViewModelImpl oTmp : oListTmp){
			this.getDomain().getDictionnary().registerViewModel(oTmp) ;
		}
	}
	
	
	/**
	 * Modify the type of the item view model of the fixed list
	 * @param p_oViewModelModified item fixed list modified
	 */
	private void modifyTypeOfItemViewModelFixedListItem( MViewModelImpl p_oViewModelModified)  {
		p_oViewModelModified.setType(ViewModelType.FIXED_LIST_ITEM);

		ViewModelTypeConfiguration oVMTypeConf = p_oViewModelModified.getType().getParametersByConfigName(p_oViewModelModified.getConfigName(), VersionHandler.getGenerationVersion().getStringVersion());

		MLinkedInterface oNewLinkedInterface = new MLinkedInterface(oVMTypeConf.getInterfaceName(), oVMTypeConf.getInterfaceFullName(),
				p_oViewModelModified.getEntityToUpdate().getMasterInterface().getFullName());
		p_oViewModelModified.addLinkedInterface(oNewLinkedInterface	);

		p_oViewModelModified.getMasterInterface().addLinkedInterface(oNewLinkedInterface);
	}
	
	/**
	 * Modify the type of the item view model of the combo
	 * @param p_oViewModelModified item combo modified
	 */
	private void modifyTypeOfItemViewModelComboItem( MViewModelImpl p_oViewModelModified)  {
		p_oViewModelModified.setType(ViewModelType.LISTITEM_1);
		
		ViewModelTypeConfiguration oVMTypeConf = p_oViewModelModified.getType().getParametersByConfigName(p_oViewModelModified.getConfigName(), VersionHandler.getGenerationVersion().getStringVersion());
		
		MLinkedInterface oNewLinkedInterface = new MLinkedInterface(oVMTypeConf.getInterfaceName(), oVMTypeConf.getInterfaceFullName(),
				p_oViewModelModified.getEntityToUpdate().getMasterInterface().getFullName());
		p_oViewModelModified.addLinkedInterface(oNewLinkedInterface	);
		
		p_oViewModelModified.getMasterInterface().addLinkedInterface(oNewLinkedInterface);
	}
	
	/**
	 * Rename the view model and the interface of it
	 * @param p_oViewModelToRename view model modified
	 * @param p_sNewNameImpl new suffix of the name the view model implementation
	 * @param p_sNewNameInterface new suffix of the name the view model interface
	 */
	public void renameViewModel( MViewModelImpl p_oViewModelToRename , String p_sNewNameImpl, String p_sNewNameInterface ){
		p_oViewModelToRename.setName(p_sNewNameImpl);
		p_oViewModelToRename.getMasterInterface().setName(p_sNewNameInterface);
		p_oViewModelToRename.addImport(p_oViewModelToRename.getFullName()) ;
	}

}
