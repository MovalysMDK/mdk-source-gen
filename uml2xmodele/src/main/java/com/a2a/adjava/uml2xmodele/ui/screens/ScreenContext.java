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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.a2a.adjava.extractors.ExtractorParams;
import com.a2a.adjava.messages.MessageHandler;
import com.a2a.adjava.uml.UmlAssociation;
import com.a2a.adjava.uml.UmlAssociationEnd;
import com.a2a.adjava.uml.UmlAssociationEnd.AggregateType;
import com.a2a.adjava.uml.UmlClass;
import com.a2a.adjava.uml.UmlDictionary;
import com.a2a.adjava.uml.UmlUsage;
import com.a2a.adjava.uml2xmodele.extractors.ViewModelExtractor;
import com.a2a.adjava.uml2xmodele.extractors.viewmodel.VMNamingHelper;
import com.a2a.adjava.utils.StrUtils;
import com.a2a.adjava.xmodele.IDomain;
import com.a2a.adjava.xmodele.IModelDictionary;
import com.a2a.adjava.xmodele.IModelFactory;
import com.a2a.adjava.xmodele.MPackage;
import com.a2a.adjava.xmodele.MScreen;

/**
 * Screen Extractor context
 * 
 * @author lmichenaud
 * 
 */
public class ScreenContext {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ScreenContext.class);

	/**
	 * the package where panel goes
	 */
	private static final String PANEL_PACKAGE = "panel";
	
	/**
	 * Domain
	 */
	private IDomain<IModelDictionary, IModelFactory> domain;

	/**
	 * ScreenExtractor parameters
	 */
	private ExtractorParams extractorParams;

	/**
	 * Stereotype list indicating that a UmlClass is a screen of the domain.
	 */
	private List<String> screenStereotypes = new ArrayList<String>();

	/**
	 * 
	 */
	private String titleStereotype;

	/**
	 * @param p_oDomain
	 */
	public ScreenContext(List<String> p_listScreenStereotypes, String p_sTitleStereotype, ExtractorParams p_oExtractorParams,
			IDomain<IModelDictionary, IModelFactory> p_oDomain) {
		this.domain = p_oDomain;
		this.extractorParams = p_oExtractorParams;
		this.screenStereotypes = p_listScreenStereotypes;
		this.titleStereotype = p_sTitleStereotype;
	}

	/**
	 * Retourne la liste des classes qui sont des screen dans un modèle.
	 * 
	 * @param p_oDict
	 *            le dictionnaire de donnée du modèle courant.
	 * @return l aliste de classe qui sont des écran de l'application.
	 */
	public List<UmlClass> getScreenUmlClasses(UmlDictionary p_oDict) {
		List<UmlClass> listScreens = new ArrayList<>();
		for (UmlClass oUmlClass : p_oDict.getAllClasses()) {
			if (isScreen(oUmlClass)) {
				listScreens.add(oUmlClass);
			}
		}
		return listScreens;
	}

	/**
	 * Retourne la liste des classes qui sont des screen dans un modèle.
	 * 
	 * @param p_oDict
	 *            le dictionnaire de donnée du modèle courant.
	 * @return l aliste de classe qui sont des écran de l'application.
	 */
	public List<UmlClass> getPageUmlClasses(UmlDictionary p_oDict) {
		List<UmlClass> listPages = new ArrayList<>();
		for (UmlClass oUmlClass : p_oDict.getAllClasses()) {
			if (isPage(oUmlClass)) {
				listPages.add(oUmlClass);
			}
		}
		return listPages;
	}

	/**
	 * Génère le package de base d'un écran.
	 * 
	 * @param p_oTargetScreen
	 *            le screen dont on veut le package de base
	 * @return objet de type <em>MPackage</em>
	 */
	public MPackage computePackageBaseFromScreen(MScreen p_oTargetScreen) {
		MPackage r_oPackage = null;
		String sScreenSubPackage = this.domain.getLanguageConf().getScreenSubPackageName();
		if (!sScreenSubPackage.isEmpty()) {
			sScreenSubPackage = StrUtils.DOT_S + sScreenSubPackage;
			String sPackage = p_oTargetScreen.getFullName().substring(0, p_oTargetScreen.getFullName().lastIndexOf(sScreenSubPackage));
			r_oPackage = this.domain.getDictionnary().getPackage(sPackage);
		} else {
			r_oPackage = p_oTargetScreen.getPackage();
		}
		return r_oPackage;
	}

	/**
	 * Génère un objet de type <em>MPackage</em> correspondant à la classe envoyée en paramètre.
	 * 
	 * @param oUmlClass
	 *            la classe dont on veux générer le package.
	 * @return objet de type <em>MPackage</em>
	 */
	public MPackage computeMasterPackage(UmlClass oUmlClass) {
		if (oUmlClass == null) {
			log.debug("oUmlClass.getPackage() == null");
		}
		String sMasterPackageName = this.domain.getStrSubstitutor().replace(oUmlClass.getPackage().getFullName());
		MPackage oMasterPackage = this.domain.getDictionnary().getPackage(sMasterPackageName);
		return oMasterPackage;
	}

	/**
	 * Génère le package ou sont présents les différents screens de l'application.
	 * 
	 * @param p_oMasterPackageName
	 *            le package de base du projet android.
	 * @return Un objet de type <em>MPackage</em>
	 */
	public MPackage computeScreenPackage(MPackage p_oMasterPackageName) {
		StringBuilder oScreenPackageNameBuilder = new StringBuilder(p_oMasterPackageName.getFullName());
		if ( this.domain.getLanguageConf().getScreenSubPackageName()!= null && 
				!this.domain.getLanguageConf().getScreenSubPackageName().isEmpty()) {
			oScreenPackageNameBuilder.append(StrUtils.DOT);
			oScreenPackageNameBuilder.append(this.domain.getLanguageConf().getScreenSubPackageName());	
		}
		
		MPackage oScreenPackage = this.domain.getDictionnary().getPackage(oScreenPackageNameBuilder.toString());
		if (oScreenPackage == null) {
			oScreenPackage = new MPackage(this.domain.getLanguageConf().getScreenSubPackageName(), p_oMasterPackageName);
			this.domain.getDictionnary().registerPackage(oScreenPackage);
		}
		return oScreenPackage;
	}
	
	/**
	 * Génère le package ou sont présents les différents panel de l'application.
	 * 
	 * @param p_oScreenPackage
	 *            le package de base du projet android.
	 * @return Un objet de type <em>MPackage</em>
	 */
	public MPackage computePanelPackage(MPackage p_oScreenPackage) {
		return new MPackage(PANEL_PACKAGE, p_oScreenPackage.getParent());
	}
	
	/**
	 * Génère le package ou sont présents les différents viewmodels de
	 * l'application.
	 * @param p_oMasterPackage le package de base du projet android.
	 * @return Un objet de type <em>MPackage</em>
	 */
	public MPackage computeViewModelPackage(MPackage p_oMasterPackage) {
		MPackage r_oVMPackage = null ;
		StringBuilder oVMPackageNameBuilder = new StringBuilder(p_oMasterPackage.getFullName());
		String sVMSubpackage = this.domain.getLanguageConf().getViewModelImplementationSubPackageName();
		if ( sVMSubpackage != null && !sVMSubpackage.isEmpty()) {
			oVMPackageNameBuilder.append(StrUtils.DOT);
			oVMPackageNameBuilder.append(sVMSubpackage);
			
			r_oVMPackage = getDomain().getDictionnary().getPackage(oVMPackageNameBuilder.toString());
			if ( r_oVMPackage == null ) {
				r_oVMPackage = new MPackage(this.domain.getLanguageConf().getViewModelImplementationSubPackageName(),
					p_oMasterPackage);
				this.getDomain().getDictionnary().registerPackage(r_oVMPackage);
			}
		}
		else {
			r_oVMPackage = p_oMasterPackage ;
		}
		return r_oVMPackage;
	}
	
	/**
	 * Compute view model name
	 * @param p_sName base name
	 * @param p_bIsList true if type of viewmodel is list.
	 * @return
	 */
	public String computeViewModelName( String p_sName, boolean p_bIsList ) {
		return VMNamingHelper.getInstance().computeViewModelImplName(p_sName, p_bIsList, this.domain.getLanguageConf());
	}

	/**
	 * @param oUmlUsage
	 * @return
	 */
	public boolean isListPanel(UmlClass p_oUmlClass) {
		return p_oUmlClass.hasAnyStereotype(	this.extractorParams.getValue(ScreenParameters.S_PANELLIST1),
												this.extractorParams.getValue(ScreenParameters.S_PANELLIST2),
												this.extractorParams.getValue(ScreenParameters.S_PANELLIST3));
	}

	/**
	 * Permet de savoir si une classe est un écran de notre application ou non.
	 * 
	 * @param p_oUmlClass
	 *            la classe à tester
	 * @return true si la classe correspond à un écran. false sinon.
	 */
	public boolean isScreen(UmlClass p_oUmlClass) {
		return p_oUmlClass.hasAnyStereotype(this.screenStereotypes) ;
	}
	
	/**
	 * Permet de savoir si une classe est un écran de notre application ou non.
	 * 
	 * @param p_oUmlClass
	 *            la classe à tester
	 * @return true si la classe correspond à un écran. false sinon.
	 */
	public boolean isPage(UmlClass p_oUmlClass) {
		return p_oUmlClass.hasAnyStereotype(getDomain().getExtractor(ViewModelExtractor.class).getListStereotypes()) ;
	}
	
	/**
	 * Permet de savoir si une classe contient le stéréotype dédié au titre
	 * @param p_oUmlClass
	 * 			la classe à tester
	 * @return true si la classe contient le stéréotype dédié au titre
	 */
	public boolean hasTitle(UmlClass p_oUmlClass) {
		return p_oUmlClass.hasStereotype(this.titleStereotype);
	}	

	/**
	 * Retourne le paramètre du stéréotype de type Liste 1.
	 * 
	 * @return Chaîne
	 */
	public String getPanelList1Stereotype() {
		return this.extractorParams.getValue(ScreenParameters.S_PANELLIST1);
	}

	/**
	 * Retourne le paramètre du stéréotype de type Liste 2.
	 * 
	 * @return Chaîne
	 */
	public String getPanelList2Stereotype() {
		return this.extractorParams.getValue(ScreenParameters.S_PANELLIST2);
	}

	/**
	 * Retourne le paramètre du stéréotype de type Liste 3.
	 * 
	 * @return Chaîne
	 */
	public String getPanelList3Stereotype() {
		return this.extractorParams.getValue(ScreenParameters.S_PANELLIST3);
	}
	
	/**
	 * Retourne le paramètre du stéréotype de type Liste 3.
	 * 
	 * @return Chaîne
	 */
	public String getScreenRootStereotype() {
		return this.extractorParams.getValue(ScreenParameters.S_SCREENROOT);
	}

	/**
	 * Retourne le paramètre du stéréotype de type Liste 3.
	 * 
	 * @return Chaîne
	 */
	public String getAggregationPanelStereotype() {
		return this.extractorParams.getValue(ScreenParameters.S_AGGREGATION_PANEL);
	}

	/**
	 * Retourne le paramètre du stéréotype de type Liste 3.
	 * 
	 * @return Chaîne
	 */
	public String getAggregationPanelWksStereotype() {
		return this.extractorParams.getValue(ScreenParameters.S_AGGREGATION_PANEL_WORKSPACE);
	}

	/**
	 * Retourne le paramètre du stéréotype de type Liste 3.
	 * 
	 * @return Chaîne
	 */
	public String getUsageNavigationStereotype() {
		return this.extractorParams.getValue(ScreenParameters.S_USAGE_NAVIGATION);
	}

	/**
	 * Retourne le paramètre du stéréotype de type Liste 3.
	 * 
	 * @return Chaîne
	 */
	public String getUsageNavigationDetailStereotype() {
		return this.extractorParams.getValue(ScreenParameters.S_USAGE_NAVIGATIONDETAIL);
	}
	/**
	 * @return
	 */
	public String getSearchScreenStereotype() {
		return this.extractorParams.getValue(ScreenParameters.S_SEARCHSCREEN);
	}

	/**
	 * Méthode pour savoir si la classe envoyée en paramètre détient un usage vers un panel de type Workspace.
	 * 
	 * @param p_oScreenUmlClass
	 *            e tester
	 * @return true si c'est un écran de type workspace, false sinon.
	 */
	public boolean isWorkspaceScreen(UmlClass p_oScreenUmlClass) {
		boolean r_bResult = false;
		for (UmlAssociationEnd oAssoEnd : p_oScreenUmlClass.getAssociations()) {
			if (oAssoEnd.getAssociation().hasStereotype(this.getAggregationPanelWksStereotype())) {
				r_bResult = true;
				break;
			}
		}
		return r_bResult;
	}
	
	/**
	 * Méthode pour savoir si la classe envoyée en paramètre détient un objet Comment.
	 * 
	 * @param p_oScreenUmlClass a tester
	 *            
	 * @return true si un objet comment est non vide, false sinon.
	 */
	public boolean isCommentScreen(UmlClass p_oScreenUmlClass) {
		return (p_oScreenUmlClass.getComment() != null);
	}
	
	/**
	 * @param p_oScreenUmlClass
	 * @return
	 */
	public boolean isSearchScreen(UmlClass p_oScreenUmlClass) {
		return p_oScreenUmlClass.hasStereotype(this.getSearchScreenStereotype());
	}

	/**
	 * @param p_oScreenUmlClass
	 * @return
	 */
	public List<PanelAggregation> getPanelAggregations(UmlClass p_oScreenUmlClass) {
		List<PanelAggregation> r_listPanelAggregations = new ArrayList<PanelAggregation>();

		log.debug("    asso count: {}", p_oScreenUmlClass.getAssociations().size());
		
		for (UmlAssociationEnd oAssociationEnd : p_oScreenUmlClass.getAssociations()) {
			UmlAssociation oAsso = oAssociationEnd.getAssociation();
			log.debug("    asso: {}", oAsso.getName());
			log.debug("    panel stereotype: {}", this.getAggregationPanelStereotype());
			log.debug("    workspace panel stereotype: {}", this.getAggregationPanelWksStereotype());
			
			if (oAsso.hasAnyStereotype( this.getAggregationPanelStereotype(), 
					this.getAggregationPanelWksStereotype())) {
				
				log.debug("    panel or workspace panel");
				
				if ( oAssociationEnd.getOppositeAssociationEnd().getAggregateType().equals(AggregateType.AGGREGATE)) {
					r_listPanelAggregations.add(new PanelAggregation(oAssociationEnd));
				}
				else {
					MessageHandler.getInstance().addError(
						"Associations between Screen and Panel must be of type Aggregation. Screen: {}, Panel: {}",
							p_oScreenUmlClass.getName(), oAssociationEnd.getRefClass().getName());
				}
			}
		}
		return r_listPanelAggregations;
	}
	
	
	
	/**
	 * This function should grow as the aggregation stereotype constrains are implemented.
	 * For now, it just forbids the stereotype "PanelList" on a panel aggregation. 
	 * @param p_oScreenContext
	 */
	public void constrainAggregationToAllowedStereotypes(UmlClass p_oScreenUmlClass)
	{
		for (UmlAssociationEnd oAssociationEnd : p_oScreenUmlClass.getAssociations()) {
			
			UmlAssociation oAsso = oAssociationEnd.getAssociation();
			if( oAsso.hasAnyStereotype(this.getPanelList1Stereotype(),
							this.getPanelList2Stereotype(),this.getPanelList3Stereotype()))
			{
				MessageHandler.getInstance().addError(
						"An aggregation can't have \"{}\", \"{}\", nor \"{}\" as its stereotype. Please correct the aggregation \"{}\" connected to the screen \"{}\"",						
						this.getPanelList1Stereotype(),this.getPanelList2Stereotype(),this.getPanelList3Stereotype(),
						oAsso.getName(), oAsso.getAssociationEnd1().getRefClass().getName());
			}
		}
	}
	
	/**
	 * Throws an exception if a stereotyped screen and a stereotyped panel are linked by a not stereotyped agregation : 
	 * it means a critical agregation has been forgotten
	 * @param p_oScreenUmlClass
	 * @return
	 */
	public void isThereMissingStereotypeOnUsageForPanel(UmlClass p_oScreenUmlClass) {

		for (UmlAssociationEnd oAssociationEnd : p_oScreenUmlClass.getAssociations()) {
			UmlAssociation oAsso = oAssociationEnd.getAssociation();

			if (oAsso.getStereotypes().isEmpty()
				&& !(oAsso.getAssociationEnd1().getRefClass().getStereotypes().isEmpty()||
				     oAsso.getAssociationEnd2().getRefClass().getStereotypes().isEmpty()))
			{
					MessageHandler.getInstance().addError(
							"If '{}' and '{}' each have a stereotype,"
									+ " the aggregation '{}' between them must also have a stereotype",						
							oAsso.getAssociationEnd2().getRefClass().getName(), oAsso.getAssociationEnd1().getRefClass().getName(),
							oAsso.getName() );
			}
			
		}

	}
	

	/**
	 * Return navigation usages (navigation and navigation detail)
	 * @param p_oScreenUmlClass
	 * @return
	 */
	public List<UmlUsage> getNavigationAndNavigationDetailUsages(UmlClass p_oScreenUmlClass) {
		return this.getNavigationUsageByStereotypes(p_oScreenUmlClass, this.getUsageNavigationStereotype(),
			this.getUsageNavigationDetailStereotype());
	}
	
	/**
	 * @param p_oScreenUmlClass
	 * @return
	 */
	public List<UmlUsage> getSimpleNavigationUsages(UmlClass p_oScreenUmlClass) {
		return this.getNavigationUsageByStereotypes(p_oScreenUmlClass, this.getUsageNavigationStereotype());
	}
	
	/**
	 * @param p_oScreenUmlClass
	 * @return
	 */
	public List<UmlUsage> getDetailNavigationUsages(UmlClass p_oScreenUmlClass) {
		return this.getNavigationUsageByStereotypes(p_oScreenUmlClass, this.getUsageNavigationDetailStereotype());
	}
	
	/**
	 * @param p_oScreenUmlClass
	 * @param p_listStereotypes
	 * @return
	 */
	private List<UmlUsage> getNavigationUsageByStereotypes( UmlClass p_oScreenUmlClass, String... p_listStereotypes) {
		log.debug("ScreenContext.getNavigationUsageByStereotypes: {}", p_listStereotypes);
		List<UmlUsage> r_listUmlUsages = new ArrayList<UmlUsage>();

		for (UmlUsage oCurrentUsage : p_oScreenUmlClass.getUsages()) {
			log.debug("  usage: {}", oCurrentUsage.getName());
			if (oCurrentUsage.hasAnyStereotype(p_listStereotypes)) {
				log.debug("  has any: true");
				if ( this.isScreen(oCurrentUsage.getSupplier())) {
					r_listUmlUsages.add(oCurrentUsage);
				} else {
					MessageHandler.getInstance().addError(
							"Navigation Usages are allowed only between screens : fix usage between {} and {}",
								oCurrentUsage.getClient().getName(), oCurrentUsage.getSupplier().getName());
				}
			}
		}

		log.debug("  result: {}", r_listUmlUsages.size());
		return r_listUmlUsages;
	}
	
	/**
	 * @param p_oScreenUmlClass
	 * @return
	 */
	public boolean isScreenRoot( UmlClass p_oScreenUmlClass ) {
		return p_oScreenUmlClass.hasStereotype(this.getScreenRootStereotype());
	}

	/**
	 * méthode privée pour la génération d'un nom avec les Préfix et Suffic adapter
	 * 
	 * @param p_sBaseName
	 *            corp du nom
	 * @return le nom de l'adapter avec préfix et suffix
	 */
	public String computeAdapterName(String p_sBaseName) {
		return StringUtils.join(this.domain.getLanguageConf().getAdapterImplementationNamingPrefix(), p_sBaseName,
				this.domain.getLanguageConf().getAdapterImplementationNamingSuffix());
	}

	/**
	 * @return
	 */
	public IDomain<IModelDictionary, IModelFactory> getDomain() {
		return this.domain;
	}

	/**
	 * @return
	 */
	public ExtractorParams getExtractorParams() {
		return extractorParams;
	}

	/**
	 * Get screen stereotypes
	 * @return screen stereotypes
	 */
	public List<String> getScreenStereotypes() {
		return this.screenStereotypes;
	}

	/**
	 * Get title stereotype
	 * @return title stereotype
	 */
	public String getTitleStereotype() {
		return this.titleStereotype;
	}
}
