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
package com.a2a.adjava.uml2xmodele.ui.screens;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.extractors.IAdditionalLayoutProcessor;
import com.a2a.adjava.types.IUITypeDescription;
import com.a2a.adjava.utils.VersionHandler;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MAdapter;
import com.a2a.adjava.xmodele.MLabel;
import com.a2a.adjava.xmodele.MLayout;
import com.a2a.adjava.xmodele.MLayoutFactory;
import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.MVisualField;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;

/**
 * Helper to create additional layouts
 * @author lmichenaud
 * 
 */
public class AdditionalLayoutProcessor implements IAdditionalLayoutProcessor {

	/**
	 * Logger de classe
	 */
	private static final Logger log = LoggerFactory.getLogger(AdditionalLayoutProcessor.class);
	
	/**
	 * Constructor
	 */
	public AdditionalLayoutProcessor() {
		// Empty constructor
	}

	/**
	 * @param p_oVm viewmodel
	 * @param p_sPath path
	 * @param p_oAdapter adapter
	 * @param p_oPage page
	 * @param p_oDomain domain
	 */
	public void addAdditionalLayouts(MViewModelImpl p_oVm, String p_sPath, MAdapter p_oAdapter,
			MPage p_oPage, IDomain<IModelDictionary, IModelFactory> p_oDomain) throws Exception {

		log.debug("AdditionalLayoutProcessor.addAdditionalLayouts: {}", p_oVm.getName());
		log.debug("  type: {}", p_oVm.getType());
		
		ViewModelType oVMType = p_oVm.getType();

		p_oVm.addImport(oVMType.getParametersByConfigName(p_oVm.getConfigName(), VersionHandler.getGenerationVersion().getStringVersion()).getListFullName());

		// Layout of list item
		MLayout oListItemLayout = MLayoutFactory.getInstance().createItemLayoutForInnerList(p_oPage, p_oVm); 
		p_oAdapter.addLayout("listitem", oListItemLayout);
		p_oDomain.getDictionnary().registerLayout(oListItemLayout);

		// Layout of selected item
		MLayout oSelectedItemLayout = MLayoutFactory.getInstance().createSelectedItemLayoutForInnerList(p_oPage, p_oVm);
		p_oAdapter.addLayout("selitem", oSelectedItemLayout);
		p_oDomain.getDictionnary().registerLayout(oSelectedItemLayout);

		IUITypeDescription oTextUiTypeDescription = p_oDomain.getLanguageConf().getUiTypeDescription(
				UITypeEnum.Text.name(), VersionHandler.getGenerationVersion().getStringVersion());
		String sFirstFieldComponent = !p_oVm.getVisualFields().isEmpty() ? p_oVm.getVisualFields().get(0).getComponent() : null ;
		
		if (p_oVm.getType().equals(ViewModelType.LIST_1__ONE_SELECTED)
				&& oTextUiTypeDescription.getROComponentType().equals(sFirstFieldComponent)
				&& (p_oVm.getVisualFields().size() == 1 || (p_oVm.getVisualFields().size() == 2 && p_oVm
						.getVisualFields().get(0).getFullName()
						.equals(p_oVm.getVisualFields().get(1).getFullName())))) {
			// dans le cas d'une combo avec un seul champ visuel de type text,
			// on change l'apparence visuel du champ
			// attention dans certaine conf on peut avoir 2 champs pour le même
			// attribut un pour la liste et un pour l'item sélectionné
			MVisualField oVisualField2 = null;
			MVisualField oVisualFieldTmp = null;
			
			String sListFieldName = "lst" + p_sPath;
			String sSelFieldName = "sel" + p_sPath;
					
			for (MVisualField oVisualField : p_oVm.getVisualFields()) {
				//oVisualField2 = new MVisualField(oVisualField);
				MLabel oLabel = p_oDomain.getXModeleFactory().createLabelFromVisualField(StringUtils.EMPTY, oVisualField);
				oVisualField2 = p_oDomain.getXModeleFactory().createVisualField(
						StringUtils.EMPTY, oVisualField, oLabel);

				oVisualField2.setComponent(p_oDomain.getLanguageConf()
						.getUiTypeDescription(UITypeEnum.CheckedText.name(), VersionHandler.getGenerationVersion().getStringVersion()).getROComponentType());
				
				switch (oVisualField.getLocalization()) {
				case DEFAULT:
					
					oLabel = p_oDomain.getXModeleFactory().createLabelFromVisualField(sListFieldName, oVisualField);
					oVisualFieldTmp = p_oDomain.getXModeleFactory().createVisualField(
							sListFieldName, oVisualField2, oLabel);
					oVisualFieldTmp.setViewModelProperty(oVisualField.getViewModelProperty());
					oVisualFieldTmp.setViewModelName(p_oVm.getName());
					oListItemLayout.addVisualField(oVisualFieldTmp);
					
					oLabel = p_oDomain.getXModeleFactory().createLabelFromVisualField(sSelFieldName, oVisualField);
					oVisualFieldTmp = p_oDomain.getXModeleFactory().createVisualField(
							sSelFieldName, oVisualField, oLabel);
					oVisualFieldTmp.setViewModelProperty(oVisualField.getViewModelProperty());
					oVisualFieldTmp.setViewModelName(p_oVm.getName());
					oSelectedItemLayout.addVisualField(oVisualFieldTmp);
					break;
				case DETAIL:
					oLabel = p_oDomain.getXModeleFactory().createLabelFromVisualField(sSelFieldName, oVisualField);
					oVisualFieldTmp = p_oDomain.getXModeleFactory().createVisualField(
							sSelFieldName, oVisualField, oLabel);
					oVisualFieldTmp.setViewModelProperty(oVisualField.getViewModelProperty());
					oVisualFieldTmp.setViewModelName(p_oVm.getName());
					oSelectedItemLayout.addVisualField(oVisualFieldTmp);
					break;
				case LIST:
					oLabel = p_oDomain.getXModeleFactory().createLabelFromVisualField(sListFieldName, oVisualField);
					oVisualFieldTmp = p_oDomain.getXModeleFactory().createVisualField(
							sListFieldName, oVisualField2, oLabel);
					oVisualFieldTmp.setViewModelProperty(oVisualField.getViewModelProperty());
					oVisualFieldTmp.setViewModelName(p_oVm.getName());
					oListItemLayout.addVisualField(oVisualFieldTmp);
					break;
				}
			}
			p_oAdapter.setFieldToChecked(p_oDomain.getXModeleFactory()
					.createVisualField(p_sPath, oVisualField2, null).getFullName());
		} else {
			String sListFieldName = "lst" + p_sPath;
			String sSelFieldName = "sel" + p_sPath;
						
			treatAdditionalLayouts(p_oVm, p_oDomain, oListItemLayout, oSelectedItemLayout, sListFieldName, sSelFieldName);
		}

		ExternalListAnalyser.getInstance().analyseExternalList(p_oVm, oSelectedItemLayout, p_sPath, p_oPage,
				p_oDomain);
	}
	
	/**
	 * 
	 * @param p_oVm
	 * @param p_oDomain
	 * @param oListItemLayout
	 * @param oSelectedItemLayout
	 * @param sListFieldName
	 * @param sSelFieldName
	 */
	protected void treatAdditionalLayouts(MViewModelImpl p_oVm, IDomain<IModelDictionary, IModelFactory> p_oDomain, 
			MLayout oListItemLayout,MLayout oSelectedItemLayout, String sListFieldName, String sSelFieldName){
		for (MVisualField oVisualField : p_oVm.getVisualFields()) {
			switch (oVisualField.getLocalization()) {
				case DEFAULT:
					MLabel oLabel = p_oDomain.getXModeleFactory().createLabelFromVisualField(sListFieldName, oVisualField);
					oListItemLayout.addVisualField(p_oDomain.getXModeleFactory().createVisualField(
							sListFieldName, oVisualField, oLabel));
					
					oLabel = p_oDomain.getXModeleFactory().createLabelFromVisualField(sSelFieldName, oVisualField);
					oSelectedItemLayout.addVisualField(p_oDomain.getXModeleFactory().createVisualField(
							sSelFieldName, oVisualField, oLabel));
					break;
				case DETAIL:
					
					oLabel = p_oDomain.getXModeleFactory().createLabelFromVisualField(sSelFieldName, oVisualField);
					oSelectedItemLayout.addVisualField(p_oDomain.getXModeleFactory().createVisualField(
							sSelFieldName, oVisualField, oLabel));
					break;
				case LIST:
					
					oLabel = p_oDomain.getXModeleFactory().createLabelFromVisualField(sListFieldName, oVisualField);
					oListItemLayout.addVisualField(p_oDomain.getXModeleFactory().createVisualField(
							sListFieldName, oVisualField, oLabel));
					break;
			}
		}
	}
}
