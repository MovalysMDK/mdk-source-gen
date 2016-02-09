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

import com.a2a.adjava.uml2xmodele.ui.actions.ActionConstants;
import com.a2a.adjava.uml2xmodele.ui.actions.ActionFactory;
import com.a2a.adjava.utils.VersionHandler;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MActionType;
import com.a2a.adjava.xmodele.MAdapter;
import com.a2a.adjava.xmodele.MDialog;
import com.a2a.adjava.xmodele.MLabel;
import com.a2a.adjava.xmodele.MLayout;
import com.a2a.adjava.xmodele.MLayoutFactory;
import com.a2a.adjava.xmodele.MPackage;
import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.MVisualField;
import com.a2a.adjava.xmodele.ui.view.MVFLabelKind;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;

/**
 * View model analyser class
 */
public class ViewModelAnalyser {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ViewModelAnalyser.class);
	
	/**
	 * Singleton instance
	 */
	private static ViewModelAnalyser instance = new ViewModelAnalyser();

	/**
	 * Constructor
	 */
	private ViewModelAnalyser() {
		// Empty constructor
	}

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	public static ViewModelAnalyser getInstance() {
		return instance;
	}

	/**
	 * Iterates through the view models to add the graphic components and the secondary layouts
	 * @param p_oParentType viewmodel parent type
	 * @param p_oLayout parent layout
	 * @param p_oVm viewmodel
	 * @param p_sPath path
	 * @param p_oAdapter viewmodel adapter
	 * @param p_oPage page
	 * @param p_bFirstLevel firstlevel
	 * @param p_bList viewmodel is list
	 * @param p_oScreenContext a screen context
	 * @return the newly created layout
	 * @throws Exception if any
	 */
	public MLayout analyseViewModel(ViewModelType p_oParentType, MLayout p_oLayout, MViewModelImpl p_oVm,
			String p_sPath, MAdapter p_oAdapter, MPage p_oPage, boolean p_bFirstLevel, boolean p_bList, ScreenContext p_oScreenContext) throws Exception {

		log.debug("AnalyseViewModel : {}", p_oVm.getName());
		log.debug("  type : {}", p_oVm.getType().name());
		
		IDomain<IModelDictionary, IModelFactory> oDomain = p_oScreenContext.getDomain();
		
		MLayout r_oLayout = null;
		if (p_oLayout == null) {

			String sLayoutIdForAdapter = null ;
			if ( MDialog.class.isAssignableFrom(p_oPage.getClass())) {
				r_oLayout = MLayoutFactory.getInstance().createLayoutForDialog(p_oPage.getUmlName());
				sLayoutIdForAdapter = "dialog";
			}
			else
			if ( p_oVm.getType().equals(ViewModelType.MASTER )) {
				r_oLayout = MLayoutFactory.getInstance().createDetailLayoutForScreen(p_oPage);
				sLayoutIdForAdapter = "detail";
			}
			else
			if ( p_bList ) {
				r_oLayout = MLayoutFactory.getInstance().createListLayoutForScreen(p_oPage.getUmlName(), p_oVm.getType());
				sLayoutIdForAdapter = "list";
			}
			else 
			if ( p_oVm.getType().equals(ViewModelType.LISTITEM_1 )) {
				r_oLayout = MLayoutFactory.getInstance().createItemLayoutForList(p_oPage.getUmlName(), p_oVm);
				sLayoutIdForAdapter = "listitem1";
			}
			else 
			if ( p_oVm.getType().equals(ViewModelType.LISTITEM_2 )) {
				r_oLayout = MLayoutFactory.getInstance().createItemLayoutForList(p_oPage.getUmlName(), p_oVm);
				sLayoutIdForAdapter = "listitem2";
			}
			else if ( p_oVm.getType().equals(ViewModelType.LISTITEM_3 )) {
				r_oLayout = MLayoutFactory.getInstance().createItemLayoutForList(p_oPage.getUmlName(), p_oVm);
				sLayoutIdForAdapter = "listitem3";
			}

			log.debug("  create new layout: {}", r_oLayout.getName());
			if (p_bFirstLevel) {
				p_oPage.setLayout(r_oLayout);
			}
			oDomain.getDictionnary().registerLayout(r_oLayout);
			if (p_oAdapter != null) {
				 // les layouts sont insérés dans l'ordre
				p_oAdapter.addLayout(sLayoutIdForAdapter, r_oLayout); 
			}

			if (p_bList && p_bFirstLevel) {
				log.debug("screenExtract.analyseViewModel");
				MVisualField oMasterVisualField = oDomain.getXModeleFactory().createVisualField(
						p_oVm.getSubViewModels().get(0).getName().toLowerCase() + "__list__value",
						null,
						p_oVm.getType().getParametersByConfigName(p_oVm.getConfigName(), VersionHandler.getGenerationVersion().getStringVersion()).getVisualComponentNameFull(),
						MVFLabelKind.NO_LABEL,
						p_oVm.getSubViewModels().get(0).getEntityToUpdate() != null ? p_oVm
								.getSubViewModels().get(0).getEntityToUpdate().getMasterInterface().getFullName() : null, false);
				oMasterVisualField.addParameter("master", "true");
				r_oLayout.addVisualField(oMasterVisualField);
				p_oPage.setMasterComponentName(
						p_oVm.getType().getParametersByConfigName(p_oVm.getConfigName(), VersionHandler.getGenerationVersion().getStringVersion()).getVisualComponentName(), 
						p_oVm.getType().getParametersByConfigName(p_oVm.getConfigName(), VersionHandler.getGenerationVersion().getStringVersion()).getVisualComponentNameFull(), 
						p_oVm.getSubViewModels().get(0).getName().toLowerCase() + "__list__value");
			}
			// ajout du type de VM en paramètre du layout pour traitement des
			// list item et décalage visuel des niveaux
			r_oLayout.addParameter("vmtype", p_oVm.getType().name());
			r_oLayout.addParameter("vmreadonly", Boolean.toString(p_oVm.isReadOnly()));
			if (p_oVm.getParent() != null) {
				r_oLayout.addParameter("parentvmtype", p_oVm.getParent().getType().name());
			}
		} else {
			r_oLayout = p_oLayout;
		}

		ViewModelType oParentType = p_oParentType;
		if (oParentType == null) {
			oParentType = p_oVm.getType();
		}

		MAdapter oAdapter = null;

		String sMasterPackageName = null;

		// seule les attributs de types listes on un intérêt
		for (MViewModelImpl oSubView : p_oVm.getSubViewModels()) {
			log.debug("  subviewmodel : " + oSubView.getName());

			if (oSubView.getType().equals(ViewModelType.LISTITEM_1)
					|| oSubView.getType().equals(ViewModelType.LISTITEM_2)
					|| oSubView.getType().equals(ViewModelType.LISTITEM_3)) {
				log.debug("  listitem type");
				analyseViewModel(oParentType, null, oSubView, oSubView.getName().toLowerCase() + "_",
						p_oAdapter, p_oPage, false, false, p_oScreenContext);
			} else if (oSubView.getType().equals(ViewModelType.LIST_1__N_SELECTED)) {
				log.debug("  n selected type");
			} else if (oSubView.getType().equals(ViewModelType.LIST_1__ONE_SELECTED)) {
				log.debug("  one selected type");
			} else if (ViewModelType.FIXED_LIST.equals(oSubView.getType())) {
				log.debug("  fixed list");
				p_oPage.addImport(oSubView.getMasterInterface().getFullName());
				p_oPage.addImport(oSubView.getEntityToUpdate().getMasterInterface().getFullName());
				// Création de l'adapter
				oAdapter = new MAdapter(p_oScreenContext.computeAdapterName(p_oPage.getUmlName()+oSubView.getUmlName()),
						oSubView.getPackage(), ViewModelType.FIXED_LIST.getParametersByConfigName(ViewModelType.DEFAULT, VersionHandler.getGenerationVersion().getStringVersion()).getAdapterName(),
						ViewModelType.FIXED_LIST.getParametersByConfigName(ViewModelType.DEFAULT, VersionHandler.getGenerationVersion().getStringVersion()).getAdapterFullName());

				oDomain.getDictionnary().registerAdapter(oAdapter);
				oAdapter.setViewModel(
						oSubView,
						StringUtils.join("get",
								StringUtils.capitalize(oSubView.getAccessorName())));

				oDomain.getAnalyserAndProcessorFactory().createAdditionalLayoutProcessor().addAdditionalLayouts(oSubView, p_sPath, oAdapter,
						p_oPage, oDomain);

				sMasterPackageName = oSubView.getPackage().getParent().getFullName();
				MPackage oMasterPackage = oDomain.getDictionnary().getPackage(sMasterPackageName);

				ActionFactory.getInstance().addAction(
						oDomain,
						false,
						p_oPage,
						oAdapter,
						new StringBuilder().append("DoAfter").append(oSubView.getMasterInterface().getName())
								.append(ActionConstants.PREFIX_ACTION_DELETE).toString(), oMasterPackage,
						MActionType.COMPUTE,
						oDomain.getDictionnary().getViewModelCreator().getFullName());

				ActionFactory.getInstance().addAction(
						oDomain,
						false,
						p_oPage,
						oAdapter,
						new StringBuilder().append("DoBefore")
								.append(oSubView.getMasterInterface().getName()).append("Add").toString(),
						oMasterPackage, MActionType.COMPUTE,
						oDomain.getDictionnary().getViewModelCreator().getFullName());

				String sModelComponentName = oSubView.getParameterValue("baseName");
				String sFixedListComponentName = p_sPath + '_' + sModelComponentName;
				// Les FIXED LIST non modifiable finissent par __value et non par __edit
				if (oSubView.isReadOnly()){
					for (MVisualField oVisualField : p_oVm.getVisualFields() ){
						if ( oVisualField.getName().equalsIgnoreCase( sModelComponentName  + "__edit") ) {
							oVisualField.setName( sModelComponentName + "__value");
						}
					}

					sFixedListComponentName +="__value";
				} else {
					sFixedListComponentName +="__edit";
				}				
				p_oPage.addExternalAdapter(oAdapter, sFixedListComponentName);
			} else {
				this.analyseViewModel(oParentType, r_oLayout, oSubView, p_sPath + "_" + oSubView.getName() + "_",
						p_oAdapter, p_oPage, false, false, p_oScreenContext);
			}
		}
		for (MVisualField oField : p_oVm.getVisualFields()) {
			log.debug("screenExtract.analyseViewModel");
			MLabel oLabel = p_oScreenContext.getDomain().getXModeleFactory().createLabelFromVisualField(p_sPath, oField);
			r_oLayout.addVisualField(oDomain.getXModeleFactory().createVisualField(p_sPath, oField, oLabel));
		}

		ExternalListAnalyser.getInstance().analyseExternalList(p_oVm, r_oLayout, p_sPath, p_oPage,
				oDomain);

		return r_oLayout;
	}
}
