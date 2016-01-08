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

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.AdjavaException;
import com.a2a.adjava.languages.LanguageConfiguration;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlUsage;
import com.a2a.adjava.uml2xmodele.assoconvert.UmlAssociationEndOptionParser;
import com.a2a.adjava.uml2xmodele.extractors.MenuExtractor;
import com.a2a.adjava.uml2xmodele.extractors.ScreenExtractor;
import com.a2a.adjava.uml2xmodele.extractors.viewmodel.MViewModelFactory;
import com.a2a.adjava.utils.StrUtils;
import com.a2a.adjava.utils.VersionHandler;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MCascade;
import com.a2a.adjava.xmodele.MLayout;
import com.a2a.adjava.xmodele.MLayoutFactory;
import com.a2a.adjava.xmodele.MPackage;
import com.a2a.adjava.xmodele.MPage;
import com.a2a.adjava.xmodele.MScreen;
import com.a2a.adjava.xmodele.MViewModelImpl;
import com.a2a.adjava.xmodele.MVisualField;
import com.a2a.adjava.xmodele.ui.component.MGridPosition;
import com.a2a.adjava.xmodele.ui.component.MMultiPanelConfig;
import com.a2a.adjava.xmodele.ui.component.MNavigationButton;
import com.a2a.adjava.xmodele.ui.component.MWorkspaceConfig;
import com.a2a.adjava.xmodele.ui.component.MWorkspaceType;
import com.a2a.adjava.xmodele.ui.navigation.MNavigation;
import com.a2a.adjava.xmodele.ui.navigation.MNavigationType;
import com.a2a.adjava.xmodele.ui.view.MVFLabelKind;
import com.a2a.adjava.xmodele.ui.viewmodel.ViewModelType;

/**
 * Process dependencies between screens
 * @author lmichenaud
 *
 */
public class ScreenDependencyProcessor {

	/** 
	 * Logger de classe
	 **/
	private static final Logger log = LoggerFactory.getLogger(ScreenExtractor.class);
	
	/**
	 * Singleton instance
	 */
	private static ScreenDependencyProcessor instance = new ScreenDependencyProcessor();

	/**
	 * Prefix of the stereotype of an action in the UML model
	 **/
	public final static String PREFIX_STEREOTYPE_ACTION = "Mm_action_" ;
	
	/**
	 * Constructor
	 */
	protected ScreenDependencyProcessor() {
		// Empty constructor
	}

	/**
	 * Return singleton instance
	 * @return singleton instance
	 */
	public static ScreenDependencyProcessor getInstance() {
		return instance;
	}

	/**
	 * Traite les relations entre écrans
	 * @param p_oUmlDict le dictionnaire de donnée de l'application
	 * @param p_oViewModelCreator la classe permettant la
	 *            génération/récupération des objets de type <em>ViewModel</em>
	 * @throws Exception plantage lors du calcul des navigations
	 */
	public void treatScreenRelations( ScreenContext p_oScreenContext, UmlDictionary p_oUmlDict) throws Exception {

		IDomain<IModelDictionary, IModelFactory> oDomain = p_oScreenContext.getDomain();
		
		LanguageConfiguration oLngConf = oDomain.getLanguageConf();
		
		// dans un second temps on traite les dépendances entre écran
		for (UmlClass oScreenUmlClass : p_oScreenContext.getScreenUmlClasses(p_oUmlDict)) {

			// Master package is package of entity
			String sMasterPackageName = oDomain.getStrSubstitutor().replace(
					oScreenUmlClass.getPackage().getFullName());
			MPackage oMasterPackage = oDomain.getDictionnary().getPackage(sMasterPackageName);

			// Compute screen package name
			String sScreenPackageName = StringUtils.join( sMasterPackageName, StrUtils.DOT_S,
					oLngConf.getScreenSubPackageName());
			MPackage oScreenPackage = oDomain.getDictionnary().getPackage(sScreenPackageName);
			if (oScreenPackage == null) {
				oScreenPackage = new MPackage(oLngConf.getScreenSubPackageName(),
						oMasterPackage);
				oDomain.getDictionnary().registerPackage(oScreenPackage);
			}

			List<PanelAggregation> listPanelAggregations = p_oScreenContext.getPanelAggregations(oScreenUmlClass);
			log.debug("  aggregation count: {}", listPanelAggregations.size());
			
			// gestion des navigations
			MScreen oScreen = oDomain.getDictionnary().getScreen(oScreenUmlClass.getName());
			
			areNavigationDetailDependencesCorrect(oScreenUmlClass, listPanelAggregations, p_oScreenContext);
			isThereMissingStereotypeOnUsageForScreen(oScreenUmlClass, oDomain);
			
			for (UmlUsage oSimpleNavigationUsage : p_oScreenContext.getSimpleNavigationUsages(oScreenUmlClass)) {
				this.treatNavigationUsage(oSimpleNavigationUsage, oScreen, p_oScreenContext);
			}
			
			for (UmlUsage oDetailNavigationUsage : p_oScreenContext.getDetailNavigationUsages(oScreenUmlClass)) {
				this.treatNavigationDetailUsage(oDetailNavigationUsage, listPanelAggregations, oScreen, p_oScreenContext);
			}

			// Manage layout
			if (oScreen.getPageCount() == 1 && !oScreen.isMultiPanel()) {
				setLayoutForSinglePageScreen(oScreen);
			} else if (oScreen.isMultiPanel()) {

				// If many pages, screen viewmodel is a view model which
				// contains all page viewmodels.

				this.createViewModelForMultiPageScreen(oScreen, oScreenUmlClass, oDomain);
				this.createLayoutForMultiPageScreen(oScreen, p_oScreenContext, oDomain);
				this.sortWorkspacePages( oScreen );
				this.linkWorkspacePagesDetails(oScreen);
				
			} else {
				// No page (screens with navigation only and no viewmodel)
				// Nothing to do
			}
		}
	}

	/**
	 * In the case of a single page screen, insert the layout of the panel in the screen layout.
	 * @param p_oScreen
	 */
	protected void setLayoutForSinglePageScreen(MScreen p_oScreen) {
		p_oScreen.setLayout(p_oScreen.getMasterPage().getLayout());
	}
	
	/**
	 * Test the dependence constraints linked with a Navigation Detail stereotype. Throw an exception if those constraints
	 * are not respected.
	 * @param p_oScreenUmlClass
	 * @param listPanelAggregations
	 * @param p_oScreenContext
	 */
	private void areNavigationDetailDependencesCorrect(UmlClass p_oScreenUmlClass, List<PanelAggregation> listPanelAggregations, ScreenContext p_oScreenContext )
	{
		for(UmlUsage umlEnd : p_oScreenUmlClass.getUsages())
		{
			if(umlEnd.hasAnyStereotype(p_oScreenContext.getUsageNavigationDetailStereotype()))
			{
				if(null == umlEnd.getName() || "".equals(umlEnd.getName()))
				{
					MessageHandler.getInstance().addError(
							"There is an unamed usage stereotyped as a navigationDetail in this graph. Every usage stereotyped as such must be named.");
				}
				else if(listPanelAggregations.isEmpty())
				{
					MessageHandler.getInstance().addError(
							"The usage \"{}\" is stereotyped as a navigationDetail. In this configuration, "
							+ "there must be one aggregation present, between the screen \"{}\" and the panel list. (There is none)",					
							umlEnd.getName(), p_oScreenUmlClass.getName());
				}
				else if(null == listPanelAggregations.get(0).getName() || "".equals(listPanelAggregations.get(0).getName()))
				{
					MessageHandler.getInstance().addError(
							"The usage \"{}\" is stereotyped as a navigationDetail. In this configuration, "
							+ "the aggregation between the screen \"{}\" and the corresponding panelList \"{}\" must be named (with the same name)",					
							umlEnd.getName(), p_oScreenUmlClass.getName(), listPanelAggregations.get(0).getPanel().getName());
				}
				else if(!umlEnd.getName().equals(listPanelAggregations.get(0).getName()))
				{
					MessageHandler.getInstance().addError(
							"The usage \"{}\" is stereotyped as a navigationDetail. In this configuration, "
							+ "the aggregation \"{}\" and this usage \"{}\" must have the exact same name.",					
							umlEnd.getName(), listPanelAggregations.get(0).getName(), umlEnd.getName());
				}
			}
		}
	}
	
		/**
	 * Throws an exception if two stereotyped screens are linked by a not stereotyped aggregation : 
	 * it means a critical aggregation has been forgotten
	 * @param oScreenUmlClass
	 */
	private void isThereMissingStereotypeOnUsageForScreen(UmlClass p_oScreenUmlClass, IDomain<IModelDictionary, IModelFactory> p_oDomain )
	{
		MenuExtractor oMenuExtractor = (MenuExtractor) p_oDomain.getExtractor(MenuExtractor.class);

		if(p_oScreenUmlClass.getStereotypes().isEmpty() || oMenuExtractor != null && oMenuExtractor.isMenu(p_oScreenUmlClass))
		{
			return;
		}
		for(UmlUsage umlEnd : p_oScreenUmlClass.getUsages())
		{
			if(!umlEnd.getSupplier().getStereotypes().isEmpty() &&
			   umlEnd.getStereotypes().isEmpty())
			{
				MessageHandler.getInstance().addError(
						"If both screens '{}' and '{}' have a stereotype,"
								+ " the aggregation between them must also have a stereotype",					
								p_oScreenUmlClass.getName(), umlEnd.getSupplier().getName());
			}


		}
	}
	
	/**
	 * @param oScreen
	 */
	private void sortWorkspacePages(MScreen oScreen) {
		if ( oScreen.isWorkspace()) {
			Collections.sort(oScreen.getPages(), new WorkspacePageComparator());
		}
	}
	
	/**
	 * @param oScreen
	 */
	private void linkWorkspacePagesDetails(MScreen oScreen) {
		if ( oScreen.isWorkspace()) {
			for(MPage oPage : oScreen.getPages()){
				if(!oPage.equals(oScreen.getMasterPage()) &&
					!oPage.getViewModelImpl().getType().equals(ViewModelType.LIST_1) && 
					!oPage.getViewModelImpl().getType().equals(ViewModelType.LIST_2) && 
					!oPage.getViewModelImpl().getType().equals(ViewModelType.LIST_3)){
					oScreen.getMasterPage().getAssociatedDetails().add(oPage);
				}
			}
		}
	}

	/**
	 * Treat a simple navigation usage
	 * @param p_oNavigationUsage
	 * @param p_oScreen
	 * @param p_oScreenContext
	 */
	protected void treatNavigationUsage( UmlUsage p_oNavigationUsage, MScreen p_oScreen, ScreenContext p_oScreenContext ) throws Exception {
				
		IDomain<IModelDictionary, IModelFactory> oDomain = p_oScreenContext.getDomain();
		
		MScreen oScreenEnd = oDomain.getDictionnary()
				.getScreen(p_oNavigationUsage.getSupplier().getName());
		log.debug("treat navigation usage between {} and {}", p_oScreen.getName(), oScreenEnd.getName());
	
		if (p_oScreen.getPageCount() > 1) {
			// à voir comment le traiter mais pour le momment il
			// faudrait mettre un bouton de navigation sur chaque
			// page donc pas top.
			MessageHandler.getInstance().addError(
					"Cas non traité : navigation à partir de plusieurs pages, que faire ?");

		} else {
			
			MNavigation oNav = oDomain.getXModeleFactory().createNavigation("navigation",
					MNavigationType.NAVIGATION, p_oScreen, oScreenEnd);
			
			oDomain.getDictionnary().registerNavigation(oNav);
			
			if (!p_oScreen.hasMasterPage()) {
			
				// If navigation to screen with panel, use panel name to compute button name.
				// If navigation to a menu screen, use screen name to compute button name.
				
				String sButtonSuffix = oScreenEnd.getUmlName();
				
				MNavigationButton oNavButton = 
						p_oScreenContext.getDomain().getXModeleFactory().createNavigationButton(
						StringUtils.join("button_navigate_", sButtonSuffix),
						StringUtils.join("button_navigate_", sButtonSuffix),
						sButtonSuffix, oNav);
				p_oScreen.getLayout().addButton(oNavButton);
				p_oScreen.addImport(oScreenEnd.getFullName());
			}
		}
	}
	
	
	/**
	 * @param p_oNavigationUsage
	 * @param p_listPanelUsages
	 * @param p_oScreen
	 * @param p_oScreenContext
	 */
	protected void treatNavigationDetailUsage( UmlUsage p_oNavigationUsage, List<PanelAggregation> p_listPanelAggregations, 
			MScreen p_oScreen, ScreenContext p_oScreenContext ) {
		
		IDomain<IModelDictionary, IModelFactory> oDomain = p_oScreenContext.getDomain();
		MScreen oScreenEnd = oDomain.getDictionnary().getScreen(p_oNavigationUsage.getSupplier().getName());
		
		log.debug("treat navigation detail usage, screen:{}, navigation name: {}", p_oScreen.getName(), p_oNavigationUsage.getName());
		log.debug("  panel aggregation count: {}", p_listPanelAggregations.size());
		
		// une navigation de type détail est liée à un model par son
		// nom indiquant le type de l'élément à afficher
		// on cherche si le nom du usage est également le nom d'une
		// relation vers le view model.
		// Un usage navigationdetail (entre screen1 et screen2) est
		// lié à une relation "aggregation panel" de type list (entre
		// screen1 et panel1).
		for (PanelAggregation oPanelAggregation : p_listPanelAggregations) {
			
			// on recherche le liens entre les navigations
			if (oPanelAggregation.getName().equals(p_oNavigationUsage.getName())) {

				log.debug("relation between panel and screen found. Panel: {}",
						p_oNavigationUsage.getSupplier().getFullName());

				if (p_oScreen.getPageCount() > 1) {
					MessageHandler
							.getInstance()
							.addError(
									"Cas non traité : naviagtion à partir de plusieurs pages, que faire ?");
				} else {

					if (oScreenEnd.getPageCount() == 1) {

							// recopy current item key name to
							// screen target
							oScreenEnd
									.getMasterPage()
									.getViewModelImpl()
									.setCurrentItemKeyName(
											p_oScreen.getMasterPage().getViewModelImpl()
													.getCurrentItemKeyName());

							// il faut que le detail de la liste
							// oScreenEnd possède toutes les
							// cascades de l'affichage de la liste
							// oScreen
							log.debug("cascade : {}",
									p_oScreen.getMasterPage().getViewModelImpl()
											.getLoadCascades().size());
							MViewModelImpl oVmImpl = p_oScreen.getMasterPage().getViewModelImpl()
									.getSubViewModels().get(0);
							MViewModelImpl oVmImplEnd = oScreenEnd.getMasterPage()
									.getViewModelImpl();
							boolean bAdd = false;
							for (MCascade oCascade : oVmImpl.getLoadCascades()) {
								log.debug("search cascade {}", oCascade.getName());
								if (!oVmImplEnd.getLoadCascades().contains(oCascade)) {
									log.debug("not found !!!");
									oVmImplEnd.getLoadCascades().add(oCascade);
									bAdd = true;
								}
							}
							if (bAdd) {
								for (String sCascade : oVmImpl.getImportCascades()) {
									if (!oVmImplEnd.getImportCascades().contains(sCascade)) {
										oVmImplEnd.getImportCascades().add(sCascade);
									}
								}
							}
							
							MNavigation oNavDetail =
								oDomain.getXModeleFactory().createNavigation("navigationdetail", 
									MNavigationType.NAVIGATION_DETAIL, p_oScreen, oScreenEnd);
							oNavDetail.setSourcePage(p_oScreen.getPageByName(oPanelAggregation.getPanel().getName()));
							oDomain.getDictionnary().registerNavigation(oNavDetail);
							
							log.debug("  add navigation detail on page: {}", p_oScreen.getMasterPage().getName());
							p_oScreen.getMasterPage().addNavigation(oNavDetail);
							
							p_oScreen.getMasterPage().getAssociatedDetails().addAll(oScreenEnd.getPages());
					} else {

						MessageHandler.getInstance().addError(
								"Cas non traité : l'écran de destination à plusieurs pages");

					}
				}
				break;
			}
		}
	}
	
	/**
	 * Create the viewmodel for workpace, the one which handles the viewmodel of each page
	 * @param p_oScreen
	 * @param p_oScreenUmlClass
	 * @param p_oDomain
	 */
	protected void createViewModelForMultiPageScreen( MScreen p_oScreen, UmlClass p_oScreenUmlClass,
			IDomain<IModelDictionary, IModelFactory> p_oDomain) throws AdjavaException {
		
		LanguageConfiguration oLngConf = p_oDomain.getLanguageConf();
		
		String sVMPackageName = p_oDomain.getStrSubstitutor().replace(
				p_oScreenUmlClass.getPackage().getFullName())
				+ StrUtils.DOT_S + oLngConf.getViewModelImplementationSubPackageName();
		MPackage oPackageBaseViewModel = p_oDomain.getDictionnary().getPackage(sVMPackageName);
		if (oPackageBaseViewModel == null) {
			oPackageBaseViewModel = new MPackage(oLngConf
					.getViewModelImplementationSubPackageName(), p_oDomain.getDictionnary()
					.getPackage(p_oScreenUmlClass.getPackage().getFullName()));
			p_oDomain.getDictionnary().registerPackage(oPackageBaseViewModel);
		}

		MViewModelImpl oMasterVmi = MViewModelFactory.getInstance().createNewViewModel(
				oPackageBaseViewModel, p_oScreenUmlClass.getName(), p_oScreenUmlClass.getName(), null,
				ViewModelType.MASTER, p_oDomain,null, false);
		
		if ( p_oScreen.isWorkspace()) {
			oMasterVmi.getMasterInterface().setScreenWorkspace(true);
		}
		else {
			if ( p_oScreen.isMultiPanel()) {
				oMasterVmi.getMasterInterface().setScreenMultiPanel(true);
			}
		}

		for (MPage oPage : p_oScreen.getPages()) {
			oMasterVmi.addSubViewModel(oPage.getViewModelImpl());
			oMasterVmi.getMasterInterface().addSubViewModel(oPage.getViewModelImpl().getMasterInterface());
		}
		p_oDomain.getDictionnary().registerViewModel(oMasterVmi);
		p_oDomain.getDictionnary().registerViewModelInterface(oMasterVmi.getMasterInterface());
		oMasterVmi.setScreenViewModel(true);
		p_oScreen.setViewModel(oMasterVmi);
	}
	
	/**
	 * @param p_oScreen
	 * @param p_oDomain
	 */
	private void createLayoutForMultiPageScreen( MScreen p_oScreen, ScreenContext p_oScreenContext, IDomain<IModelDictionary, IModelFactory> p_oDomain ) throws Exception {
	
		MLayout oLayout = MLayoutFactory.getInstance().createDetailLayoutForScreen(p_oScreen.getViewModel().getUmlName()); 
	
		// dans le cas du workspace, il faut ajouter la configuration
		// correspondant aux pages du Screen
		if (p_oScreen.isWorkspace()) {
			
			String sComponent = null ;
			
			if ( p_oScreen.getWorkspaceType().equals(MWorkspaceType.MASTERDETAIL)) {
				sComponent = ViewModelType.WORKSPACE_MASTERDETAIL.getParametersByConfigName(ViewModelType.DEFAULT, VersionHandler.getGenerationVersion().getStringVersion()).getVisualComponentNameFull();
			}
			else {
				sComponent = ViewModelType.WORKSPACE_DETAIL.getParametersByConfigName(ViewModelType.DEFAULT, VersionHandler.getGenerationVersion().getStringVersion()).getVisualComponentNameFull();
			}
			
			MVisualField oWorkspaceVisualField = p_oDomain.getXModeleFactory().createVisualField(
					"main__" + p_oScreen.getViewModel().getUmlName().toLowerCase(Locale.getDefault()) + "__visualpanel", null,
					sComponent,	MVFLabelKind.NO_LABEL, null, false);//pas d'autobinding sur ce composant
			oLayout.addVisualField(oWorkspaceVisualField);
			
			this.computeWorkspaceConfig(oWorkspaceVisualField, p_oScreen, p_oScreenContext);
		}
		else {
			
			MVisualField oMultiPanelVisualField = p_oDomain.getXModeleFactory().createVisualField(
					"main__" + p_oScreen.getViewModel().getUmlName().toLowerCase(Locale.getDefault()) + "__visualpanel", null,
					ViewModelType.MULTIPANEL.getParametersByConfigName(ViewModelType.DEFAULT, VersionHandler.getGenerationVersion().getStringVersion()).getVisualComponentNameFull(), 
					MVFLabelKind.NO_LABEL, null, false);//pas d'autobinding sur ce composant
			oLayout.addVisualField(oMultiPanelVisualField);
			
			this.computeMultiPanelConfig(oMultiPanelVisualField, p_oScreen, p_oScreenContext);
		}
	
		p_oDomain.getDictionnary().registerLayout(oLayout);
		p_oScreen.setLayout(oLayout);
	}

	/**
	 * <p>
	 * 	Cette méthode ajoute dans le champ visuel envoyé en paramètre, la configuration relative au Workspace.
	 * 	Dans cette conf on va ajouter le nombre de colonnes de l'écran, ainsi que les layout qui seront affichés dans chaque page.
	 * </p>
	 *  
	 * @param p_oWorkspaceVisualField le champ visuel qui va porter la configuration
	 * @param p_oScreen l'écran correspondant au workspace.
	 */
	private void computeWorkspaceConfig(MVisualField p_oWorkspaceVisualField, MScreen p_oScreen, ScreenContext p_oScreenContext ) throws Exception {
		
		MWorkspaceConfig oWorkspaceConfig = new MWorkspaceConfig();
		
		// count number of tabs in first column
		int iTabCount = 0 ;
		if ( p_oScreen.getWorkspaceType().equals(MWorkspaceType.MASTERDETAIL)) {
			iTabCount = this.countWorkspaceTabs(p_oScreen, p_oScreenContext);
			log.debug("computeWorkspaceConfig - tabCount: {}", iTabCount);
			
			//calcul du main
			if ( iTabCount == 0 ) {
				oWorkspaceConfig.setMainPage(p_oScreen.getMasterPage());
				oWorkspaceConfig.setMainConfig(
					"get" + p_oScreen.getMasterPage().getViewModelImpl().getMasterInterface().getName(),
					p_oScreen.getMasterPage().getLayout().getName());
			}
			else {
				oWorkspaceConfig.setMainConfig("getVMTabConfiguration", 
					MLayoutFactory.getInstance().computeLayoutNameForWorkspaceTabs(p_oScreen));
			}
		}
		
		for( MPage oPage : p_oScreen.getPages()) {
			String sComputedGetterName = "get" + oPage.getViewModelImpl().getMasterInterface().getName();
			String sBasicLayoutName = oPage.getLayout().getName();
			
			if ( oPage.getParameterValue(MWorkspaceConfig.PANELTYPE_PARAMETER)
					.equals(MWorkspaceConfig.MASTER_PANELTYPE)) {
				if ( iTabCount != 0 ) {
					String sOptions = oPage.getParameterValue(MWorkspaceConfig.OPTIONS_PARAMETER);
					oWorkspaceConfig.addTabConfig(sComputedGetterName, sBasicLayoutName, 
							this.readPanelPosition(sOptions), oPage);
				}
				else {
					oPage.addParameter(MWorkspaceConfig.COLUMN_PARAMETER, "1");
					oPage.addParameter(MWorkspaceConfig.SECTION_PARAMETER, "1");
				}
			}
			else {
				String sOptions = oPage.getParameterValue(MWorkspaceConfig.OPTIONS_PARAMETER);
				oWorkspaceConfig.addDetailConfig(sComputedGetterName, sBasicLayoutName, this.readPanelPosition(sOptions), 
					oPage);
				
			}
		}
		
		oWorkspaceConfig.computeGrids();
		
		p_oWorkspaceVisualField.addVisualParameter(MWorkspaceConfig.VISUALFIELD_PARAMETER, oWorkspaceConfig);
	}
	
	
	/**
	 * <p>
	 * 	Cette méthode ajoute dans le champ visuel envoyé en paramètre, la configuration relative au Workspace.
	 * 	Dans cette conf on va ajouter le nombre de colonnes de l'écran, ainsi que les layout qui seront affichés dans chaque page.
	 * </p>
	 *  
	 * @param p_oWorkspaceVisualField le champ visuel qui va porter la configuration
	 * @param p_oScreen l'écran correspondant au workspace.
	 */
	private void computeMultiPanelConfig(MVisualField p_oMultiPanelVisualField, MScreen p_oScreen, ScreenContext p_oScreenContext ) throws Exception {
		
		MMultiPanelConfig oMultiPanelConfig = p_oScreenContext.getDomain().getXModeleFactory().createMultiPanelConfig();

		for( MPage oPage : p_oScreen.getPages()) {
			String sComputedGetterName = "get" + oPage.getViewModelImpl().getMasterInterface().getName();
			String sBasicLayoutName = oPage.getLayout().getName();	
			String sOptions = oPage.getParameterValue(MMultiPanelConfig.OPTIONS_PARAMETER);
			
			MGridPosition oSectionPos = this.readPanelPosition(sOptions);
			oSectionPos.setColumn(1);
			oMultiPanelConfig.addSectionConfig(sComputedGetterName, sBasicLayoutName, oSectionPos, oPage);
		}
		
		oMultiPanelConfig.computeGrids();
		
		p_oMultiPanelVisualField.addVisualParameter(MMultiPanelConfig.VISUALFIELD_PARAMETER, oMultiPanelConfig);
	}
	
	/**
	 * Read position of panel in the options
	 * Position can be in the following format :
	 * Cm : column index only, no section index.
	 * CmSn : both column and section indexes are valued.
	 * Sn : only section is valued, first column is used. 
	 * @param p_sOptions options to parse
	 * @return
	 */
	private MGridPosition readPanelPosition( String p_sOptions ) {
		MGridPosition r_oGridPosition = new MGridPosition();
		
		Map<String, ?> mapAssoOptions = UmlAssociationEndOptionParser.getInstance().parse(p_sOptions);
		
		String sPositionOption = (String) mapAssoOptions.get("C");
		String sSectionPositionOption = (String) mapAssoOptions.get("S");
		
		if ( sPositionOption != null || sSectionPositionOption != null ) {
			
			if ( sPositionOption == null ) {
				sPositionOption = "1" ;
				
				if ( sSectionPositionOption != null ) {
					sPositionOption = "1S" + sSectionPositionOption;
				}
			}
	
			int iSectionPos = sPositionOption.indexOf("S");
			if ( iSectionPos == -1 ) {
				r_oGridPosition.setColumn(Integer.parseInt(sPositionOption));
			}
			else {
				r_oGridPosition.setColumn(Integer.parseInt(sPositionOption.substring(0, iSectionPos)));
				r_oGridPosition.setSection(Integer.parseInt(sPositionOption.substring(iSectionPos+1)));
			}
		}
		
		return r_oGridPosition;
	}
	
	/**
	 * @param p_oScreen
	 * @return
	 */
	private int countWorkspaceTabs( MScreen p_oScreen, ScreenContext p_oScreenContext ) {
		// count number of page of type list
		int r_iCountTabs = 0 ;
		for( MPage oPage : p_oScreen.getPages()) {
			if ( oPage.getParameterValue(MWorkspaceConfig.PANELTYPE_PARAMETER).equals(
					MWorkspaceConfig.MASTER_PANELTYPE)) {
				r_iCountTabs++ ;
			}
		}
		// one list = 0 tabs
		return r_iCountTabs == 1 ? 0 : r_iCountTabs ;
	}
}
