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

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.extractors.ExtractorParams;
import com.a2a.adjava.languages.LanguageConfiguration;
import com.a2a.adjava.uml.UmlAttribute;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlOperation;
import com.a2a.adjava.uml.UmlStereotype;
import com.a2a.adjava.utils.StrUtils;
import com.a2a.adjava.utils.VersionHandler;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MAdapter;
import com.a2a.adjava.xmodele.MLayout;
import com.a2a.adjava.xmodele.MLayoutFactory;
import com.a2a.adjava.xmodele.MPackage;
import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.MScreen;
import com.a2a.adjava.xmodele.MStereotype;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.ui.component.MMultiPanelConfig;
import com.a2a.adjava.xmodele.ui.component.MWorkspaceConfig;
import com.a2a.adjava.xmodele.ui.component.MWorkspaceType;
import com.a2a.adjava.xmodele.ui.panel.MPanelOperation;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;

/**
 * Process uml usage relations for screen
 */
public class ScreenAggregationPanelProcessor {

	/**
	 * Singleton instance
	 */
	private static ScreenAggregationPanelProcessor instance = new ScreenAggregationPanelProcessor();

	/**
	 * Constructor
	 */
	private ScreenAggregationPanelProcessor() {
		// Empty constructor
	}

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	public static ScreenAggregationPanelProcessor getInstance() {
		return instance;
	}

	/**
	 * Process aggregations between screens and panels
	 * @param p_oScreen the screen aggregating the panels
	 * @param p_oScreenUmlClass the UmlClass of the screen
	 * @param p_oScreenContext the context of the screen
	 * @throws Exception if any
	 */
	public void processPanelAggregations(MScreen p_oScreen,
			UmlClass p_oScreenUmlClass, ScreenContext p_oScreenContext)
			throws Exception {

		// Master package is the uml entity package.
		MPackage oMasterPackage = p_oScreenContext
				.computeMasterPackage(p_oScreenUmlClass);

		// Compute package for viewmodel
		MPackage oVMPackage = p_oScreenContext
				.computeViewModelPackage(oMasterPackage);

		//Test si les agrégations entre screens et panels ne contiennent que des stéréotypes corrects
		p_oScreenContext.constrainAggregationToAllowedStereotypes(p_oScreenUmlClass);
		
		//Test si une agrégation critique n'a pas été oubliée lors des placements de stéréotypes
		p_oScreenContext.isThereMissingStereotypeOnUsageForPanel(p_oScreenUmlClass);
		
		// initialisation des panels liés au Screen courant
		List<PanelAggregation> listPanelAggregations = p_oScreenContext
				.getPanelAggregations(p_oScreenUmlClass);

		// screen has at least one panel
		if (!listPanelAggregations.isEmpty()) {

			boolean bSinglePage = listPanelAggregations.size() == 1;

			// dans le cas du WorkspacePanel il y a plusieurs Panel
			for (PanelAggregation oPanelAggregation : listPanelAggregations) {

				// récupération du clone du view model d'une page classique
				// (ou item de liste)
				MViewModelImpl oPageVm = this.getViewModelByPanelAggregation(
						oPanelAggregation, oVMPackage, false, bSinglePage,
						p_oScreenContext);

				
				if (oPanelAggregation.getPanel().hasAnyStereotype("Mm_multiInstance")) {
					for (UmlAttribute oAttribute : oPanelAggregation.getPanel().getAttributes()) {
						// restriction pour la version 4.2.0
						if (!oAttribute.getInitialValue().contains("_R")) {
							throw new AdjavaException("Mm_multiInstance cannot be use on panels with writtable field");
						}
					}
					oPageVm.setMultiInstance(true);
				}
				
				// cas d'un panel de type liste
				if (p_oScreenContext.isListPanel(oPanelAggregation.getPanel())) {
					// panel of type list
					treatListPanelAggregation(oPanelAggregation,
									p_oScreen, bSinglePage, p_oScreenUmlClass,
									oMasterPackage, oVMPackage,
									p_oScreenContext);

				} else {
					// cas d'un panel simple/classique
					treatPanelAggregation(oPanelAggregation,
									p_oScreen, oPageVm, p_oScreenUmlClass,
									oMasterPackage, bSinglePage, p_oScreenContext);
				}
			}

			// cas d'un menu (pas de view model juste un écran).
		} else if (listPanelAggregations.isEmpty()) {

			MLayout oLayout = MLayoutFactory.getInstance()
					.createLayoutForNoVMScreen(p_oScreenUmlClass.getName());
			p_oScreen.setLayout(oLayout);
			p_oScreenContext.getDomain().getDictionnary()
					.registerLayout(oLayout);
		}
	}

	/**
	 * Treat a usage panel. Panel is of type simple. A usage panel is between a
	 * screen and a panel.
	 * 
	 * @param p_oUmlUsage
	 *            uml usage
	 * @param p_oScreen
	 *            screen source of uml usage
	 * @param p_oPageVm
	 *            viewmodel of page
	 * @param p_oVmc
	 *            viewmodel creator
	 * @param p_oScreenUmlClass
	 *            umlclass of screen
	 * @param p_oMasterPackage
	 *            master package
	 * @param p_bSinglePage 
	 * @throws Exception
	 */
	private void treatPanelAggregation(PanelAggregation p_oPanelAggregation,
			MScreen p_oScreen, MViewModelImpl p_oPageVm,
			UmlClass p_oScreenUmlClass, MPackage p_oMasterPackage,
			boolean p_bSinglePage, ScreenContext p_oScreenContext) throws Exception {

		MPage oPage = this.createPage(p_oScreenContext, 
					p_oScreen, 
					p_oPanelAggregation, 
					p_oPageVm 
				);

		StringBuilder sPath = new StringBuilder();
		if (p_oPanelAggregation.getName() == null
				|| p_oPanelAggregation.getName().isEmpty()) {
			sPath.append(p_oScreenUmlClass.getName().toLowerCase());
		} else {
			sPath.append(p_oPanelAggregation.getName().toLowerCase());
		}
		sPath.append('_');

		this.computePanelOperations(oPage, p_oPanelAggregation.getPanel());

		// parcours des views models pour ajouter les composants graphiques et
		// créer les layouts secondaires
		ViewModelAnalyser.getInstance().analyseViewModel(null, null, p_oPageVm,
				sPath.toString(), null, oPage, true, false, p_oScreenContext);

		p_oScreenContext.getDomain().getDictionnary().registerPanel(oPage);
		p_oScreen.add(oPage);

		if (!p_oScreen.isWorkspace()) {
			// Add scrolling to the master page
			if ( p_bSinglePage) {
				p_oScreen
					.getMasterPage()
					.getLayout()
					.addParameter("addscroll",
							p_oScreen.getMasterPage().getLayout().getName());
			}
			else {
				oPage.addParameter(
						MMultiPanelConfig.OPTIONS_PARAMETER,
						p_oPanelAggregation.getOptions());
			}
		} else {
			if (p_oPanelAggregation.hasStereotype(p_oScreenContext.getAggregationPanelWksStereotype())) {
				oPage.addParameter( MWorkspaceConfig.PANELTYPE_PARAMETER, MWorkspaceConfig.DETAIL_PANELTYPE );
				oPage.addParameter( MWorkspaceConfig.OPTIONS_PARAMETER, p_oPanelAggregation.getOptions() );
			}
		}
	}

	/**
	 * Treat a usage panel. Panel is of type list. A usage panel is between a
	 * screen and a panel.
	 * 
	 * @param p_oPanelAggregation
	 *            uml usage
	 * @param p_oScreen
	 *            Screen source
	 * @param p_bSinglePage
	 *            screen has only one page
	 * @param p_oScreenUmlClass
	 *            uml class of screen
	 * @param p_oMasterPackage
	 * @param oVMPackage
	 * @throws Exception
	 */
	private void treatListPanelAggregation(
			PanelAggregation p_oPanelAggregation, MScreen p_oScreen,
			boolean p_bSinglePage, UmlClass p_oScreenUmlClass,
			MPackage p_oMasterPackage, MPackage p_oVMPackage,
			ScreenContext p_oScreenContext) throws Exception {

		IDomain<IModelDictionary, IModelFactory> oDomain = p_oScreenContext
				.getDomain();
		ExtractorParams oParams = p_oScreenContext.getExtractorParams();

		MAdapter r_oAdapter = null;
		// récupération du ViewModel principal de la liste :
		// "PageName_UsageName_ScreenName"
		MViewModelImpl oListViewModel = this.getViewModelByPanelAggregation(
				p_oPanelAggregation, p_oVMPackage, true, p_bSinglePage,
				p_oScreenContext);

		// changement de type à la volée : on ne peut pas connaître le vrai type
		// avant
		if (p_oPanelAggregation.getPanel().hasStereotype(
				oParams.getValue(ScreenParameters.S_PANELLIST1))) {
			r_oAdapter = new MAdapter(
					p_oScreenContext.computeAdapterName(p_oPanelAggregation
							.getPanel().getName()), p_oVMPackage,
					ViewModelType.LIST_1.getParametersByConfigName(
							ViewModelType.DEFAULT, VersionHandler.getGenerationVersion().getStringVersion()).getAdapterName(),
					ViewModelType.LIST_1.getParametersByConfigName(
							ViewModelType.DEFAULT, VersionHandler.getGenerationVersion().getStringVersion()).getAdapterFullName());

		} else if (p_oPanelAggregation.getPanel().hasAllStereotypes(
				oParams.getValue(ScreenParameters.S_PANELLIST2),
				oParams.getValue(ScreenParameters.S_OPTIONMULTISELECT))) {
			// cas d'une liste à deux niveau à sélection multiple
			r_oAdapter = new MAdapter(
					p_oScreenContext.computeAdapterName(p_oPanelAggregation
							.getPanel().getName()), p_oVMPackage,
					ViewModelType.LIST_2.getParametersByConfigName(
							ViewModelType.MULTISELECT, VersionHandler.getGenerationVersion().getStringVersion()).getAdapterName(),
					ViewModelType.LIST_2.getParametersByConfigName(
							ViewModelType.MULTISELECT, VersionHandler.getGenerationVersion().getStringVersion()).getAdapterFullName());
			oListViewModel.setConfigName(ViewModelType.MULTISELECT);

		} else if (p_oPanelAggregation.getPanel().hasStereotype(
				oParams.getValue(ScreenParameters.S_PANELLIST2))) {
			r_oAdapter = new MAdapter(
					p_oScreenContext.computeAdapterName(p_oPanelAggregation
							.getPanel().getName()), p_oVMPackage,
					ViewModelType.LIST_2.getParametersByConfigName(
							ViewModelType.DEFAULT, VersionHandler.getGenerationVersion().getStringVersion()).getAdapterName(),
					ViewModelType.LIST_2.getParametersByConfigName(
							ViewModelType.DEFAULT, VersionHandler.getGenerationVersion().getStringVersion()).getAdapterFullName());
		} else if (p_oPanelAggregation.getPanel().hasStereotype(
				oParams.getValue(ScreenParameters.S_PANELLIST3))) {
			r_oAdapter = new MAdapter(
					p_oScreenContext.computeAdapterName(p_oPanelAggregation
							.getPanel().getName()), p_oVMPackage,
					ViewModelType.LIST_3.getParametersByConfigName(
							ViewModelType.DEFAULT, VersionHandler.getGenerationVersion().getStringVersion()).getAdapterName(),
					ViewModelType.LIST_3.getParametersByConfigName(
							ViewModelType.DEFAULT, VersionHandler.getGenerationVersion().getStringVersion()).getAdapterFullName());
		}

		// définition du getter du ViewModel dans l'adapter
		r_oAdapter.setViewModel(oListViewModel, StringUtils.join("get",
				StringUtils.capitalize(oListViewModel.getAccessorName())));

		MPage oPage = this.createPage(
					p_oScreenContext, 
					p_oScreen, 
					p_oPanelAggregation, 
					oListViewModel
				);
		
		oPage.setAdapter(r_oAdapter);
		if (r_oAdapter != null) {
			oDomain.getDictionnary().registerAdapter(r_oAdapter);
		}

		StringBuilder sPath = new StringBuilder("list");
		if (p_oPanelAggregation.getName() == null) {
			sPath.append(p_oScreenUmlClass.getName().toLowerCase());
		} else {
			sPath.append(p_oPanelAggregation.getName().toLowerCase());
		}
		sPath.append('_');

		this.computePanelOperations(oPage, p_oPanelAggregation.getPanel());

		// parcours des views models pour ajouter les composants graphiques et
		// créer les layouts secondaires
		ViewModelAnalyser.getInstance().analyseViewModel(null, null,
				oListViewModel, sPath.toString(), r_oAdapter, oPage, true,
				true, p_oScreenContext);

		oDomain.getDictionnary().registerPanel(oPage);

		// si workspace et l'usage contient le sterotype Mm_panel on le met en
		// premier
		if (p_oScreen.isWorkspace()
				&& p_oPanelAggregation.hasStereotype(p_oScreenContext.getAggregationPanelStereotype())) {
			p_oScreen.insert(oPage, this.computePageDetailPosition(p_oScreen));
			p_oScreen.setWorkspaceType(MWorkspaceType.MASTERDETAIL);
			oPage.addParameter( MWorkspaceConfig.PANELTYPE_PARAMETER, MWorkspaceConfig.MASTER_PANELTYPE );
			oPage.addParameter( MWorkspaceConfig.OPTIONS_PARAMETER,   p_oPanelAggregation.getOptions() );
		} else {
			oPage.addParameter(
					MMultiPanelConfig.OPTIONS_PARAMETER,
					p_oPanelAggregation.getOptions());
			p_oScreen.add(oPage);
		}
	}

	/**
	 * Compute page detail position for workspace. List panels are inserted
	 * before last detail page.
	 * 
	 * @param p_oScreen
	 * @return
	 */
	private int computePageDetailPosition(MScreen p_oScreen) {
		int r_iPos = 0;
		for (MPage oPage : p_oScreen.getPages()) {
			if (oPage.getParameterValue(MWorkspaceConfig.PANELTYPE_PARAMETER)
					.equals(MWorkspaceConfig.MASTER_PANELTYPE)) {
				r_iPos++;
			} else {
				break;
			}
		}
		return r_iPos;
	}

	/**
	 * Get a viewmodel for usage.isViewModel If single page, use the existing
	 * view model. If not single page, clone the existing view model
	 * @param p_oPanelAggregation the panel aggregation held by the view model
	 * @param p_oVMPackage the current package 
	 * @param p_bIsList true if this is a list view model
	 * @param p_bSinglePage true if the screen has only one page
	 * @param p_oScreenContext the screen context to use
	 * @return the {@link MViewModelImpl} found
	 * @throws Exception if any
	 */
	public MViewModelImpl getViewModelByPanelAggregation(
			PanelAggregation p_oPanelAggregation, MPackage p_oVMPackage,
			boolean p_bIsList, boolean p_bSinglePage,
			ScreenContext p_oScreenContext) throws Exception {

		MViewModelImpl r_oViewModel = null;

		LanguageConfiguration oLngConf = p_oScreenContext.getDomain()
				.getLanguageConf();

		String sViewModelShortName =
			p_oScreenContext.computeViewModelName(p_oPanelAggregation.getPanel().getName(), p_bIsList);
		
		// création du nom complet du supplier de l'usage courant (package.name)
		StringBuilder oViewModelFullInitialNameBuilder = new StringBuilder();
		oViewModelFullInitialNameBuilder.append(p_oVMPackage.getFullName())
				.append(StrUtils.DOT);
		oViewModelFullInitialNameBuilder.append(sViewModelShortName);

		// récupération du ViewModel de la Page au bout de la relation d'usage
		r_oViewModel = p_oScreenContext.getDomain().getDictionnary()
				.getViewModel(oViewModelFullInitialNameBuilder.toString());

		if (r_oViewModel == null) {
			throw new AdjavaException("Can't find view model {} in dictionnary", oViewModelFullInitialNameBuilder);
		}

		// dans le cas d'une liste on change la base du nom de l'accesseur (sauf
		// dans le cas d'un workspace ou il est déjà calculé
		if (p_bIsList) {
			StringBuilder oAccessorNameBuilder = new StringBuilder();
			oAccessorNameBuilder.append("Lst");
			oAccessorNameBuilder.append(oLngConf
					.getViewModelImplementationNamingPrefix());
			oAccessorNameBuilder.append(p_oPanelAggregation.getPanel()
					.getName());
			r_oViewModel.setAccessorName(oAccessorNameBuilder.toString());
		}

		return r_oViewModel;
	}

	/**
	 * Computes the {@link MPanelOperation} held by a given panel
	 * @param p_oPage the page to use
	 * @param p_oUmlClass uml class of panel
	 */
	private void computePanelOperations(MPage p_oPage, UmlClass p_oPanelUmlClass) {
		// read uml operations and adds them on panel
		for (UmlOperation oUmlOperation : p_oPanelUmlClass.getOperations()) {
			MPanelOperation oPanelOperation = new MPanelOperation(
					oUmlOperation.getName());
			for (UmlStereotype oUmlStereotype : oUmlOperation.getStereotypes()) {
				oPanelOperation.getStereotypes().add(
						new MStereotype(oUmlStereotype.getName(),
								oUmlStereotype.getDocumentation()));
			}
			p_oPage.addPanelOperation(oPanelOperation);
		}
	}
	
	/**
	 * Creates a page with the given parameters
	 * @param p_oScreenContext the screen context to use
	 * @param p_oScreen the screen on which the page should be created
	 * @param p_oPanelAggregation the {@link PanelAggregation} to use
	 * @param p_oPageVM the view model to set on the page
	 * @return the newly created page
	 */
	private MPage createPage(ScreenContext p_oScreenContext, MScreen p_oScreen, PanelAggregation p_oPanelAggregation, MViewModelImpl p_oPageVM) {
		
		MPackage oPagePackage = p_oScreenContext.computePanelPackage(p_oScreen.getPackage());
		
		return p_oScreenContext
			.getDomain()
			.getXModeleFactory()
			.createPage(
					p_oScreen,
					p_oScreenContext.getDomain(),
					p_oPanelAggregation.getPanel().getName(),
					p_oPanelAggregation.getPanel(),
					oPagePackage,
					p_oPageVM,
					p_oPanelAggregation.getPanel()
							.hasStereotype("Mm_title"));
	}
	
}
