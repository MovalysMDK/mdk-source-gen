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

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.types.ITypeDescription;
import com.a2a.adjava.utils.VersionHandler;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MEntityImpl;
import com.a2a.adjava.xmodele.MLabel;
import com.a2a.adjava.xmodele.MMethodParameter;
import com.a2a.adjava.xmodele.MMethodSignature;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.MVisualField;
import com.a2a.adjava.xmodele.ui.view.MVFLabelKind;
import com.a2a.adjava.xmodele.ui.view.MVFLocalization;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;

/**
 * Treat a path that matches a list. Example: path is a.b.c and b is a list in
 * a.
 * @author lmichenaud
 * 
 */
public class VMListPathProcessor {

	/** Logger */
	private static final Logger log = LoggerFactory.getLogger(VMListPathProcessor.class);

	/**
	 * Singleton instance
	 */
	private static VMListPathProcessor instance = new VMListPathProcessor();

	/**
     * Key in the map parameters of the fixed list view name
	 */
	public static String KEY_TO_FIXED_LIST_VIEW_NAME = "fixedlist-view-name" ;
	/**
     * Key in the map parameters of the cell in the fixed list view name
	 */
	public static String KEY_TO_CELL_ITEM_NAME = "cell-item-name" ;

	
	/**
	 * Constructor
	 */
	private VMListPathProcessor() {
		// Empty constructor
	}

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	public static VMListPathProcessor getInstance() {
		return instance;
	}

	/**
	 * Treat a list path
	 * @param p_sPath list path to treat
	 * @param p_oListVMView listview model
	 * @param p_iDepth current depth
	 * @param p_sAssociationEndName association end name
	 * @param p_oTargetEntity target entity
	 * @param p_oAssoViewModelType viewmodel type in function of the asso
	 * @param p_sParentPath parent path
	 * @param p_oVMCache viewmodel cache
	 * @return
	 */
	public MViewModelImpl treatListPath(VMPathContext p_oVMPathContext, int p_iDepth,
			MEntityImpl p_oTargetEntity, ViewModelType p_oAssoViewModelType,
			Map<String, MViewModelImpl> p_oVMCache, 
			IDomain<IModelDictionary, IModelFactory> p_oDomain, String p_sConfigName) throws Exception {

		return this.treatListPath(p_oVMPathContext, p_iDepth, p_oTargetEntity, p_oAssoViewModelType, p_oVMCache, p_oDomain, p_sConfigName, false);
	}

	/**
	 * Treat a list path
	 * @param p_sPath list path to treat
	 * @param p_oListVMView listview model
	 * @param p_iDepth current depth
	 * @param p_sAssociationEndName association end name
	 * @param p_oTargetEntity target entity
	 * @param p_oAssoViewModelType viewmodel type in function of the asso
	 * @param p_sParentPath parent path
	 * @param p_oVMCache viewmodel cache
	 * @return
	 */
	public MViewModelImpl treatListPath(VMPathContext p_oVMPathContext, int p_iDepth,
			MEntityImpl p_oTargetEntity, ViewModelType p_oAssoViewModelType,
			Map<String, MViewModelImpl> p_oVMCache, IDomain<IModelDictionary, IModelFactory> p_oDomain, 
			String p_sConfigName, boolean p_bMandatory) throws Exception {

		MViewModelImpl oMasterVM = p_oVMPathContext.getMasterVM();

		log.debug("treatListPath: {}", p_oVMPathContext.getCurrentPath());
		log.debug("  depth: {}", p_iDepth);
		log.debug("  master vm umlname: {}", oMasterVM.getUmlName());

		String sAssociationEndName = p_oVMPathContext.getCurrentElement();
		String sParentPath = p_oVMPathContext.getOldPath();
		
		// retrieve parent of viewmodel
		MViewModelImpl oParentVm = p_oVMCache.get(sParentPath);
		if (oParentVm == null) {
			oParentVm = oMasterVM;
		}		
		
		MViewModelImpl r_oViewModel = p_oVMCache.get(p_oVMPathContext.getCurrentPath());
		if (r_oViewModel == null) {

			// Compute the viewmodel type
			ViewModelType oViewModelType = computeViewModelTypeForTreatList(oMasterVM.getFirstParent(),
					p_iDepth, p_oAssoViewModelType);
			log.debug("  computed vm type : {}", oViewModelType);
			
			//Calcul du nom théorique du viewmodel
			String sTheoVmImplName = VMNamingHelper.getInstance().computeViewModelImplName(
					oMasterVM.getUmlName() + StringUtils.capitalize(sAssociationEndName), false, p_oDomain.getLanguageConf());	
			String sTheoVmItfName = VMNamingHelper.getInstance().computeViewModelInterfaceName(
					oMasterVM.getUmlName() + StringUtils.capitalize(sAssociationEndName), false, p_oDomain.getLanguageConf());	
			
			//on cherche si un view model porte déjà ce nom là
			MViewModelImpl oParentVmUsedForTest = null ;
			if ( oViewModelType.equals(ViewModelType.LIST_1__ONE_SELECTED) || oViewModelType.equals(ViewModelType.LIST_1__N_SELECTED)) {
				oParentVmUsedForTest = oMasterVM;
			}
			else {
				oParentVmUsedForTest = oParentVm;
			}
			
			int index = p_oVMPathContext.getIndex()-1;
			while(oParentVm.existViewModelName(sTheoVmImplName) || 
					oParentVmUsedForTest.getMasterInterface().hasMethodeSignature(computeGetterNameForParentVm(StringUtils.capitalize(sAssociationEndName)))) {
				
				sAssociationEndName = StringUtils.capitalize(p_oVMPathContext.getPathWithNoPoint(index--, p_oVMPathContext.getIndex()));
				sTheoVmImplName = VMNamingHelper.getInstance().computeViewModelImplName(
						oMasterVM.getUmlName() + StringUtils.capitalize(sAssociationEndName), 
						false, p_oDomain.getLanguageConf());
				sTheoVmItfName = VMNamingHelper.getInstance().computeViewModelInterfaceName(
						oMasterVM.getUmlName() + StringUtils.capitalize(sAssociationEndName), 
						false, p_oDomain.getLanguageConf());
			}
			
			// Si master vm has parent, we are in list component case
			boolean bIsRootList = !oMasterVM.getParentReferences().isEmpty();

			// Define type from asso or by the component.
			// If no parent, always from asso type. If parent, only if depth is
			// less than
			// the max depth of the parent viewmodel type.
			boolean bTypeFromAsso = !bIsRootList
					|| p_iDepth > oMasterVM.getFirstParent().getType().getParametersByConfigName(oMasterVM.getConfigName(), VersionHandler.getGenerationVersion().getStringVersion()).getMaxLevel();


			// le chemin n'est pas encore été traité on créer un nouveau
			// viewmodel
			// base name for viewmodel is panel name + association end name
			r_oViewModel = MViewModelFactory.getInstance().createNewViewModel(oMasterVM.getPackage(),
					oMasterVM.getUmlName() + StringUtils.capitalize(sAssociationEndName),
					StringUtils.capitalize(sAssociationEndName),
					p_oTargetEntity, oViewModelType,
					p_oDomain,null, false);

			r_oViewModel.setMandatory(p_bMandatory);

			VMAttributeHelper.getInstance().addIdFieldToClass(r_oViewModel, p_oTargetEntity,
					p_oVMPathContext.getCurrentPath(), p_oDomain);

			if (p_sConfigName!=null){
				r_oViewModel.setConfigName(p_sConfigName);
			}
			
			// Add to cache
			p_oVMCache.put(p_oVMPathContext.getCurrentPath(), r_oViewModel);

			if (!bTypeFromAsso) {
				
				oParentVm.addSubViewModel(r_oViewModel);

				// Complete the parent vm with generics
				oParentVm.getMasterInterface().getLinkedInterfaces().get(0)
						.addGenerics(r_oViewModel.getEntityToUpdate().getMasterInterface().getFullName());
				oParentVm.getMasterInterface().getLinkedInterfaces().get(0)
						.addGenerics(r_oViewModel.getMasterInterface().getName());
				
				oParentVm.getMasterInterface().addImport(r_oViewModel.getEntityToUpdate().getMasterInterface().getFullName());
				oParentVm.getMasterInterface().addImport(r_oViewModel.getMasterInterface().getFullName());
			} else {
				
				switch (r_oViewModel.getType()) {
				case LIST_1__ONE_SELECTED:
					{
						oParentVm.addExternalViewModel(r_oViewModel);
	
						String sBaseName = StringUtils.capitalize(sTheoVmItfName);
						
						// Dans le cas des combo, l'élément sélectionné est stocké
						// sur le viewmodel parent
						MMethodSignature oGet = new MMethodSignature("get"
								+ sBaseName, "public", "get", r_oViewModel.getMasterInterface()
								.getAssociatedType(p_oDomain));
						MMethodSignature oSet = new MMethodSignature("set"
								+ sBaseName, "public", "set", null);
						oSet.addParameter(new MMethodParameter("p_o" + sBaseName, r_oViewModel
								.getMasterInterface().getAssociatedType(p_oDomain)));
						oParentVm.getMasterInterface().addMethodSignature(oGet);
						oParentVm.getMasterInterface().addMethodSignature(oSet);
	
						String sComboName = sBaseName + "__edit";
						
						MVFLocalization oLocalization = null;
						MLabel oComboLabel = null; 
						
						if ( oParentVm.getType().equals(ViewModelType.FIXED_LIST)) {
							oLocalization = MVFLocalization.DETAIL;
							oComboLabel = p_oDomain.getXModeleFactory().createLabelForAttributeOfFixedList(sBaseName, r_oViewModel.getUmlName(), oLocalization, oParentVm );
						}
						else {
							oLocalization = MVFLocalization.DEFAULT;
							oComboLabel = p_oDomain.getXModeleFactory().createLabelForCombo(sBaseName, r_oViewModel.getUmlName(), oParentVm );
						}
						
						r_oViewModel.addParameter("baseName", sBaseName);
	
						// LBR Modify : MVFLabelKind.NO_LABEL par MVFLabelKind.WITH_LABEL
						// r_oViewModel.getType().getParametersByConfigName(r_oViewModel.getConfigName()).getVisualComponentNameFull(),
						MVisualField oMVisualField =  p_oDomain.getXModeleFactory().createVisualField(
								sComboName,
								oComboLabel,
								r_oViewModel.getType().getParametersByConfigName(r_oViewModel.getConfigName(), VersionHandler.getGenerationVersion().getStringVersion()).getVisualComponentNameFull(),
								MVFLabelKind.WITH_LABEL,
								oLocalization,
								r_oViewModel.getEntityToUpdate() != null ? r_oViewModel
										.getEntityToUpdate().getMasterInterface().getFullName() : null, r_oViewModel.isMandatory());
						oMVisualField.addParameter("combo", "true");
						oMVisualField.addParameter("comboVm", r_oViewModel.getFullName());
						
						oMVisualField.setViewModelProperty(sBaseName);
						oMVisualField.setViewModelName(r_oViewModel.getName());
						
						// ajout des noms des infos pour la fixed list : utilisés dans le StoryboardExtractor
						oParentVm.addVisualField(oMVisualField );
	
						// Dans le cas des combo, le contenu de la liste déroulante
						// est toujours portée par le VM de plus haut niveau.
						oParentVm = oMasterVM;
					}
					break;
					
				case LIST_1__N_SELECTED:
					{
						oParentVm.addExternalViewModel(r_oViewModel);
	
						String sBaseName = StringUtils.capitalize(sTheoVmItfName);
						
						// Dans le cas des combo, l'élément sélectionné est stocké
						// sur le viewmodel parent
						MMethodSignature oGet = new MMethodSignature("get"
								+ sBaseName, "public", "get", r_oViewModel.getMasterInterface()
								.getAssociatedType(p_oDomain));
						MMethodSignature oSet = new MMethodSignature("set"
								+ sBaseName, "public", "set", null);
						oSet.addParameter(new MMethodParameter("p_o" + sBaseName, r_oViewModel
								.getMasterInterface().getAssociatedType(p_oDomain)));
						oParentVm.getMasterInterface().addMethodSignature(oGet);
						oParentVm.getMasterInterface().addMethodSignature(oSet);
	
						String sComboName = sBaseName + "__edit";
						MLabel oComboLabel = null; 
						
						MVFLocalization oLocalization = null;
						if ( oParentVm.getType().equals(ViewModelType.FIXED_LIST)) {
							oLocalization = MVFLocalization.DETAIL;
							oComboLabel = p_oDomain.getXModeleFactory().createLabelForAttributeOfFixedList(sBaseName, r_oViewModel.getUmlName(), oLocalization, oParentVm );
						}
						else {
							oLocalization = MVFLocalization.DEFAULT;
							oComboLabel = p_oDomain.getXModeleFactory().createLabelForCombo(sBaseName, r_oViewModel.getUmlName(), oParentVm );
						}
						
						r_oViewModel.addParameter("baseName", sBaseName);
	
						// LBR Modify : MVFLabelKind.NO_LABEL par MVFLabelKind.WITH_LABEL
						// r_oViewModel.getType().getParametersByConfigName(r_oViewModel.getConfigName()).getVisualComponentNameFull(),
						MVisualField oMVisualField =  p_oDomain.getXModeleFactory().createVisualField(
								sComboName,
								oComboLabel,
								r_oViewModel.getType().getParametersByConfigName(r_oViewModel.getConfigName(), VersionHandler.getGenerationVersion().getStringVersion()).getVisualComponentNameFull(),
								MVFLabelKind.WITH_LABEL,
								oLocalization,
								r_oViewModel.getEntityToUpdate() != null ? r_oViewModel
										.getEntityToUpdate().getMasterInterface().getFullName() : null, r_oViewModel.isMandatory());
						oMVisualField.addParameter("combo", "true");
						oMVisualField.addParameter("comboVm", r_oViewModel.getFullName());
						
						String sPropertyName = p_oDomain.getXModeleFactory().createPropertyNameForFixedListCombo(r_oViewModel, sComboName);
						
						oMVisualField.setViewModelProperty(sPropertyName);
						oMVisualField.setViewModelName(r_oViewModel.getName());
						
						// ajout des noms des infos pour la fixed list : utilisés dans le StoryboardExtractor
						oParentVm.addVisualField(oMVisualField );
	
						// Dans le cas des combo, le contenu de la liste déroulante
						// est toujours portée par le VM de plus haut niveau.
						oParentVm = oMasterVM;
					}
					break;
					
				case FIXED_LIST:
					
					oParentVm.addSubViewModel(r_oViewModel);
					String sFixedListName = "lst"+ StringUtils.capitalize(sTheoVmItfName);
					String sFixedListComponentName = "lst"+ StringUtils.capitalize(sTheoVmItfName) + "__edit";
					String sFixedListLabelName = "lst" + StringUtils.capitalize(sTheoVmItfName) + "__label";
					MLabel oFixedListLabel = p_oDomain.getXModeleFactory().createLabelForFixedList(sFixedListName, r_oViewModel.getUmlName(), oParentVm);
					
					oParentVm.getMasterInterface().addImport(r_oViewModel.getEntityToUpdate().getMasterInterface().getFullName());
					oParentVm.getMasterInterface().addImport(r_oViewModel.getMasterInterface().getName());
					
					MVisualField oNewMVisualField = p_oDomain.getXModeleFactory().createVisualField(
							sFixedListComponentName,
							oFixedListLabel,
							ViewModelType.FIXED_LIST.getParametersByConfigName(r_oViewModel.getConfigName(), VersionHandler.getGenerationVersion().getStringVersion()).getVisualComponentNameFull(),
							MVFLabelKind.WITH_LABEL,
							r_oViewModel.getEntityToUpdate() != null ? r_oViewModel.getEntityToUpdate().getMasterInterface().getFullName() : null, false);
					
					r_oViewModel.addParameter("baseName", "lst" + StringUtils.capitalize(sTheoVmItfName));
							
					oNewMVisualField.addParameter("fixedList", "true");
					oNewMVisualField.addParameter("fixedListVm", r_oViewModel.getFullName());

					oNewMVisualField.setViewModelProperty(sFixedListComponentName);
					oNewMVisualField.setViewModelName(r_oViewModel.getName());
					
					oParentVm.addVisualField(oNewMVisualField );

					break;
				}
			}

			// Add getter/setter on viewmodel parent
			createGetterSetterOnParentVM(sTheoVmItfName, p_oTargetEntity, r_oViewModel, oViewModelType,
					oParentVm, oMasterVM.getUmlName(), p_oDomain);
		}
		return r_oViewModel;
	}

	/**
	 * @param p_oRootViewModel rootviewmodel (can be null)
	 * @param p_iDepth
	 * @param p_oAssoViewModelType
	 * @return
	 */
	private ViewModelType computeViewModelTypeForTreatList(MViewModelImpl p_oRootViewModel, int p_iDepth,
			ViewModelType p_oAssoViewModelType) {
		ViewModelType r_oViewModelType = null;
		// If depth more than component max depth, use the level type computed
		// with the asso type.
		// If depth less or equals than component max depth, the view model type
		// is defined by the component hierarchy.
		if (p_oRootViewModel == null || p_iDepth > p_oRootViewModel.getType().getParametersByConfigName(p_oRootViewModel.getConfigName(), VersionHandler.getGenerationVersion().getStringVersion()).getMaxLevel()) {
			r_oViewModelType = p_oAssoViewModelType;
		} else {
			int iDepth = 1;
			r_oViewModelType = p_oRootViewModel.getType().getSubType();
			while (iDepth < p_iDepth) {
				r_oViewModelType = r_oViewModelType.getSubType();
				iDepth++;
			}
		}
		return r_oViewModelType;
	}

	/**
	 * Create getter/setter
	 * @param p_sAssociationEndName
	 * @param p_oTargetEntity
	 * @param r_oViewModel
	 * @param oViewModelType
	 * @param oParentVm
	 * @param p_sPanelName panel name
	 */
	private void createGetterSetterOnParentVM(String p_sAssociationEndName, MEntityImpl p_oTargetEntity,
			MViewModelImpl r_oViewModel, ViewModelType p_oViewModelType, MViewModelImpl oParentVm,
			String p_sPanelName, IDomain<IModelDictionary, IModelFactory> p_oDomain) {
		log.debug("  add get and set methods on {}", oParentVm.getMasterInterface().getName());
		log.debug("    viewmodel type : " + p_oViewModelType.name());

		String sBaseName = StringUtils.capitalize(p_sAssociationEndName);

		ITypeDescription oType = (ITypeDescription) (p_oViewModelType.getParametersByConfigName(r_oViewModel.getConfigName(), VersionHandler.getGenerationVersion().getStringVersion()).getTypeDescription(p_oDomain).clone());
		oType.setParameterizedElementType(p_oTargetEntity.getMasterInterface().getAssociatedType(p_oDomain),
				r_oViewModel.getMasterInterface().getAssociatedType(p_oDomain));

		String sGetterName = computeGetterNameForParentVm(sBaseName);
		String sSetterName = computeSetterNameForParentVm(sBaseName);
		String sListGetterName = computeListGetterNameForParentVm(sBaseName);
		String sListSetterName = computeListSetterNameForParentVm(sBaseName);
			
		r_oViewModel.setAccessorGetName(sGetterName);
		r_oViewModel.setAccessorGetListName(sListGetterName);		

		r_oViewModel.setAccessorSetListName(sListSetterName);
		r_oViewModel.setAccessorSetName(sSetterName);
		
		MMethodSignature oGet = new MMethodSignature(sListGetterName, "public", "get", oType);
		oParentVm.getMasterInterface().addMethodSignature(oGet);

		MMethodSignature oSet = new MMethodSignature(sListSetterName, "public", "set", null);
		oSet.addParameter(new MMethodParameter("p_o" + sBaseName, oType));
		oParentVm.getMasterInterface().addMethodSignature(oSet);

		oParentVm.getMasterInterface().setSubVM(r_oViewModel.getMasterInterface().getName(),
				r_oViewModel.getMasterInterface().getFullName());
		oParentVm.getMasterInterface().setSub(r_oViewModel.getEntityToUpdate().getMasterInterface().getName(),
				r_oViewModel.getEntityToUpdate().getMasterInterface().getFullName());

		log.debug("  treatlist1 add subvm " + r_oViewModel.getName());
		log.debug("  treatlist1 add sub " + r_oViewModel.getEntityToUpdate().getMasterInterface().getName());
	}

	/**
	 * Create getter name for parentVM
	 */
	private String computeListGetterNameForParentVm(String sBaseName) {
		return "getLst" + sBaseName;
	}
	
	/**
	 * @param sBaseName
	 * @return
	 */
	private String computeListSetterNameForParentVm(String sBaseName) {
		return "setLst" + sBaseName;
	}

	/**
	 * Create getter name for parentVM
	 */
	private String computeGetterNameForParentVm(String sBaseName) {
		return "get" + sBaseName;
	}
	
	/**
	 * @param sBaseName
	 * @return
	 */
	private String computeSetterNameForParentVm(String sBaseName) {
		return "set" + sBaseName;
	}
}
